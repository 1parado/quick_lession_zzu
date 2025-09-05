

优化： （智能化课程负载预测，利用NLP分析课程评论中的关键词（如“作业量巨大”、“每周都要读paper”、“考试难度高”），得出一个综合负载评分）（先不做，先进行手动打分）（最后做）

​	方案B: 使用预训练模型进行微调（Fine-tuning）（强烈推荐）

- **描述**：使用Hugging Face上的一个轻量级预训练模型（如BERT的变体`BERT-tiny`, `BERT-mini`或`RoBERTa`），用自己的“课程负载”标注数据进行微调。

- **优点**：

  1. **效果最佳**：模型专门为你的“课程负载”任务优化，理解相关语境（如“作业多”、“没压力”、“挂科率高”）。
  2. **数据安全**：模型部署在自己的服务器上，数据不出内网。
  3. **成本可控**：一次训练，无限次使用。推理所需计算资源很小，SpringBoot服务器完全可以承载。

- **结论**：**这是最适合你需求的方案**。

  

  集成成功。
  BUG：都是给五分 1

  训练得有一些问题。

  

  

  

  ### 基于方案B（微调）的详细实现步骤

  #### 阶段一：数据准备（最关键的一步）

  你需要有监督数据来教模型什么是“高负载”，什么是“低负载”。

  1. **收集历史数据**：从现有论坛中收集一批帖子，特别是那些已经包含了用户手动评分的帖子。

  2. **人工标注**：如果历史数据不够，需要组织人员（如运营、开发自己）进行标注。标注规则要清晰：

     - **定义标签**：例如 `0: 轻松`, `1: 中等`, `2: 繁重`
     - **标注指南**：
       - `轻松`：出现“水课”、“没作业”、“随便过”等词。
       - `繁重`：出现“作业巨多”、“天天熬夜”、“一周好几个ddl”等词。
       - `中等`：介于两者之间或信息不明显。

  3. **数据格式**：整理成一个CSV文件，包含两列：`text` 和 `label`。

     csv

     

     复制

     

     下载

     ```
     text,label
     "这门课是良心水课，老师从不点名，期末交篇论文就行。",0
     "作业量非常大，每周都要读几十页文献，但是收获也很大。",2
     "课程难度一般，有一定作业量，需要平时稍微听听讲。",1
     ```

  #### 阶段二：模型微调（Python环境）

  这是一个离线的、一次性的任务。

  1. **环境准备**：使用Python，安装`transformers`, `datasets`, `torch`, `sklearn`库。
  2. **选择模型**：从Hugging Face Model Hub选择一个轻量级中文预训练模型，如：
     - `hfl/chinese-bert-wwm-ext` (效果好)
     - `hfl/chinese-roberta-wwm-ext-mini` (速度更快)
  3. **编写训练脚本**：加载你的CSV数据，使用`Trainer` API进行微调。这个过程会自动下载模型，并用你的数据教它学习“课程负载”任务。Hugging Face提供了大量示例代码，很容易修改。
  4. **评估模型**：训练完成后，在预留的测试集上评估模型准确率，确保效果达标。
  5. **保存模型**：将微调好的最终模型保存到磁盘上的一个文件夹中。

  #### 阶段三：模型部署与集成（SpringBoot后端）

  现在需要让SpringBoot能调用这个训练好的模型。

  1. **部署模型**：将阶段二保存的**整个模型文件夹**放到你的SpringBoot项目的资源目录下（例如 `src/main/resources/model`）。

  2. **引入Java推理库**：

     - **推荐使用 `Deep Java Library (DJL)`**：这是一个非常优秀的Java深度学习库，由AWS开发，完美支持加载Hugging Face模型，并且与SpringBoot集成无缝。
     - 在`pom.xml`中添加DJL和PyTorch的依赖。

     xml

     

     复制

     

     下载

     

     运行

     ```
     <dependency>
         <groupId>ai.djl</groupId>
         <artifactId>api</artifactId>
         <version>0.25.0</version>
     </dependency>
     <dependency>
         <groupId>ai.djl.pytorch</groupId>
         <artifactId>pytorch-engine</artifactId>
         <version>0.25.0</version>
         <scope>runtime</scope>
     </dependency>
     ```

  3. **编写Service层代码**：在SpringBoot中创建一个`AiService`，编写加载模型和推理的代码。

     java

     

     复制

     

     下载

     ```
     @Service
     public class CourseLoadService {
     
         // 推荐使用懒加载，项目启动时加载一次模型
         private Criteria<String, Classifications> criteria;
     
         @PostConstruct
         public void init() throws ModelException, IOException {
             // 指定模型路径、输入输出类型
             criteria = Criteria.builder()
                     .optEngine("PyTorch")
                     .setTypes(String.class, Classifications.class)
                     .optModelPath(Paths.get("src/main/resources/model")) // 你的模型路径
                     .optTranslator(new MyTranslator()) // 需要自定义一个Translator处理文本预处理
                     .optProgress(new ProgressBar())
                     .build();
         }
     
         public PredictResult predict(String postContent) throws Exception {
             // 加载模型并进行预测
             try (ZooModel<String, Classifications> model = criteria.loadModel();
                  Predictor<String, Classifications> predictor = model.newPredictor()) {
                 Classifications classifications = predictor.predict(postContent);
                 // 获取预测结果中最可能的那个类别
                 Classifications.Classification best = classifications.best();
                 return new PredictResult(best.getClassName(), best.getProbability());
             }
         }
     }
     ```

  4. **创建RESTful接口**：创建一个Controller，供前端调用。

     java

     

     复制

     

     下载

     ```
     @RestController
     @RequestMapping("/api/ai")
     public class AiController {
     
         @Autowired
         private CourseLoadService courseLoadService;
     
         @PostMapping("/predict-load")
         public ResponseEntity<PredictResult> predictLoad(@RequestBody PostContent postContent) {
             try {
                 PredictResult result = courseLoadService.predict(postContent.getContent());
                 return ResponseEntity.ok(result);
             } catch (Exception e) {
                 return ResponseEntity.internalServerError().build();
             }
         }
     }
     ```

  #### 阶段四：前端调用（Vue3）

  在发帖页面的文本编辑器（如Vditor、Quill）旁添加一个“智能评估”按钮。

  1. 用户输入内容后，点击按钮。
  2. 前端通过Axios调用SpringBoot的 `/api/ai/predict-load` 接口，将文本框内容作为JSON发送。
  3. 后端返回预测结果（如 `{ "label": "2", "className": "繁重", "probability": 0.95 }`）。
  4. 前端接收到结果后，自动填充或建议用户填充“课程负载”的评分字段。

  vue

  

  复制

  

  下载

  ```
  <template>
    <div>
      <textarea v-model="postContent"></textarea>
      <button @click="predictLoad">智能评估负载</button>
      <select v-model="selectedLoad">
        <option value="0">轻松</option>
        <option value="1">中等</option>
        <option value="2">繁重</option>
      </select>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import api from '@/api';
  
  const postContent = ref('');
  const selectedLoad = ref('');
  
  const predictLoad = async () => {
    if (!postContent.value) return;
    try {
      const response = await api.ai.predictLoad({ content: postContent.value });
      selectedLoad.value = response.data.label; // 自动填充选择框
      // 或者可以弹个提示：`AI建议评分：繁重（置信度95%），是否采用？`
    } catch (error) {
      console.error("预测失败", error);
    }
  };
  </script>
  ```






重新训练流程：

重写csv训练数据文件

执行python train.py

执行python convert_to_onnx.py

复制course_load_model文件夹、course_load_model.onnx文件、.txt文件