1. **安装与配置Ollama**：在服务器上部署本地大模型。

2. **Spring Boot后端集成**：让你的后端服务能够与Ollama对话。

3. **业务数据对接（关键）**：教模型理解你的业务数据。

4. **Vue3前端集成**：构建用户界面。

   

   ### 第一阶段：安装与配置Ollama

   这一步的目标是在你的服务器或开发机上安装Ollama，并拉取一个合适的大模型。

   1. **下载Ollama**：

      - 访问Ollama官网：**[https://ollama.com](https://ollama.com/)**
      - 根据你的操作系统（Windows/macOS/Linux）下载对应的安装包。对于生产环境，通常选择Linux。

   2. **安装并验证**：

      - **Windows/macOS**：直接运行下载的安装程序。

      - **Linux**：通常可通过命令行安装[3](https://developer.baidu.com/article/details/3333308)：

        bash

        ```
        curl -fsSL https://ollama.com/install.sh | sh
        ```

        

      - 安装完成后，打开终端，运行 `ollama --version` 验证是否安装成功。

   3. **拉取大模型**：

      - Ollama支持多种模型[1](https://www.cnblogs.com/qwg-/p/18967196)[3](https://developer.baidu.com/article/details/3333308)。对于中文业务场景，**Qwen**系列是很好的选择。对于入门和测试，可以先从一个较小的模型开始（对硬件要求低）。

      - 在终端中执行拉取命令[1](https://www.cnblogs.com/qwg-/p/18967196)[5](https://www.showapi.com/news/article/6798d5794ddd79f11a3310c8)：

        bash

        ```
        # 拉取一个非常小巧的模型（适合所有机器，快速响应）
        ollama pull qwen2:0.5b
        # 或拉取一个能力更强的7B模型（需要至少8GB以上内存，推荐）
        ollama pull qwen2:7b
        ```

        

      - 这个过程会自动下载模型，时间取决于你的网络速度和模型大小。

   4. **运行并测试模型**：

      - 运行刚下载的模型进行测试[1](https://www.cnblogs.com/qwg-/p/18967196)：

        bash

        ```
        ollama run qwen2:7b
        ```

        

      - 在弹出的交互界面中，尝试输入 `你好`，看它是否能正常回复。使用 `Ctrl+D` 退出交互界面。

      - **重要**：Ollama的API服务会默认运行在 `http://localhost:11434`[1](https://www.cnblogs.com/qwg-/p/18967196)[3](https://developer.baidu.com/article/details/3333308)。

   ------

   ### 第二阶段：Spring Boot后端集成

   这一步的目标是让你的Spring Boot项目能够通过HTTP客户端调用本地Ollama服务的API。

   1. **添加依赖**：

      - 在你的Spring Boot项目的 `pom.xml` 文件中添加以下依赖。**强烈建议使用Spring AI的starter**，它能极大简化代码[1](https://www.cnblogs.com/qwg-/p/18967196)[3](https://developer.baidu.com/article/details/3333308)。

        xml

        ```
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-ollama-spring-boot-starter</artifactId>
            <version>0.8.1</version> <!-- 请检查最新版本 -->
        </dependency>
        ```

        

      - 如果无法找到Spring AI依赖，可能需要添加Spring的里程碑仓库或版本管理。

   2. **配置连接信息**：

      - 在 `application.yml` 或 `application.properties` 文件中配置Ollama服务器的连接信息[1](https://www.cnblogs.com/qwg-/p/18967196)[3](https://developer.baidu.com/article/details/3333308)。

        yaml

        ```
        # application.yml
        spring:
          ai:
            ollama:
              base-url: http://localhost:11434 # Ollama默认地址
              chat:
                options:
                  model: qwen2:7b # 你之前拉取的模型名称
        ```

        

   3. **创建AIService**：

      - 创建一个Service类，注入 `OllamaChatClient`，并编写调用方法[1](https://www.cnblogs.com/qwg-/p/18967196)。

        java

        ```
        import org.springframework.ai.chat.client.ChatClient;
        import org.springframework.stereotype.Service;
        
        @Service
        public class AIService {
        
            private final ChatClient chatClient;
        
            // 通过构造器注入ChatClient
            public AIService(ChatClient.Builder chatClientBuilder) {
                // 这里会自动使用配置文件中spring.ai.ollama的配置
                this.chatClient = chatClientBuilder.build();
            }
        
            public String chatWithAI(String userMessage) {
                return chatClient.prompt()
                        .user(userMessage)
                        .call()
                        .content();
            }
        }
        ```

        

   4. **创建RESTful API接口**：

      - 创建一个Controller，暴露一个API端点供前端调用。

        java

        ```
        import org.springframework.web.bind.annotation.PostMapping;
        import org.springframework.web.bind.annotation.RequestBody;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RestController;
        import java.util.Map;
        
        @RestController
        @RequestMapping("/api/ai")
        public class AIController {
        
            private final AIService aiService;
        
            public AIController(AIService aiService) {
                this.aiService = aiService;
            }
        
            @PostMapping("/chat")
            public Map<String, String> chat(@RequestBody Map<String, String> request) {
                String userMessage = request.get("message");
                String response = aiService.chatWithAI(userMessage);
                return Map.of("reply", response);
            }
        }
        ```

        

   5. **测试后端连接**：

      - 启动你的Spring Boot应用。
      - 使用Postman或curl工具发送一个POST请求到 `http://你的服务器地址:端口/api/ai/chat`，Body为JSON格式：`{"message": "你好，介绍一下你自己"}`。
      - 如果一切正常，你应该能收到Ollama模型的回复。

   ------

   ### 第三阶段：业务数据对接（让模型了解你的业务）

   这是最核心的一步。直接问模型“选了数据结构的同学还会选什么”，它不可能知道答案，因为它无法访问你的数据库。我们需要通过 **“提示词工程（Prompt Engineering）”** 将业务数据“喂”给它。

   **核心思想**：在提问时，**动态地将从数据库查询到的相关数据作为上下文（Context）**，和用户的问题一起组合成一个完整的提示词（Prompt），再发送给Ollama。

   1. **编写数据查询服务**：

      - 假设你有一个 `CourseSelectionService`，可以查询选课数据。
      - 编写一个方法，专门用于查询“选择了某课程的学生，还选择了哪些其他课程”的统计信息。

      java

      ```
      import org.springframework.jdbc.core.JdbcTemplate;
      import org.springframework.stereotype.Service;
      import java.util.List;
      import java.util.stream.Collectors;
      
      @Service
      public class CourseSelectionService {
      
          private final JdbcTemplate jdbcTemplate;
      
          public CourseSelectionService(JdbcTemplate jdbcTemplate) {
              this.jdbcTemplate = jdbcTemplate;
          }
      
          /**
           * 查询选择了指定课程的学生，还选择的其他课程（统计排名）
           * @param courseName 指定的课程名，如"数据结构"
           * @return 一个格式化好的字符串，包含统计结果
           */
          public String getRelatedCourses(String courseName) {
              // 示例SQL：查询同时选择‘数据结构’和其他课程的数据
              // 请根据你的实际数据库表结构修改此SQL
              String sql = """
                      SELECT other_course, COUNT(*) as count
                      FROM (
                          SELECT s1.student_id, s2.course_name as other_course
                          FROM student_courses s1
                          JOIN student_courses s2 ON s1.student_id = s2.student_id
                          WHERE s1.course_name = ? AND s2.course_name != ?
                      ) t
                      GROUP BY other_course
                      ORDER BY count DESC
                      LIMIT 10
                      """;
      
              List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, courseName, courseName);
      
              // 将查询结果格式化成模型容易理解的文本
              if (results.isEmpty()) {
                  return "没有找到相关的选课数据。";
              }
      
              return results.stream()
                      .map(row -> String.format("- %s (%s 人选择)", row.get("other_course"), row.get("count")))
                      .collect(Collectors.joining("\n"));
          }
      }
      ```

      

   2. **改造AIService**：

      - 修改 `AIService`，注入 `CourseSelectionService`，并创建一个新的方法来处理业务相关问题。

      java

      ```
      @Service
      public class AIService {
      
          private final ChatClient chatClient;
          private final CourseSelectionService courseSelectionService;
      
          public AIService(ChatClient.Builder chatClientBuilder, CourseSelectionService courseSelectionService) {
              this.chatClient = chatClientBuilder.build();
              this.courseSelectionService = courseSelectionService;
          }
      
          // ... 保留之前的通用chat方法 ...
      
          /**
           * 处理与选课业务相关的特定问题
           */
          public String chatAboutCourseSelection(String userMessage) {
              // 1. 从用户问题中提取关键信息（这里简单实现，实际可用NLP或正则表达式优化）
              String targetCourse = extractCourseName(userMessage); // 例如，从问题中提取"数据结构"
      
              // 2. 查询数据库获取真实数据
              String realData = courseSelectionService.getRelatedCourses(targetCourse);
      
              // 3. 构建一个强大的系统提示词（System Prompt），指导模型如何行为
              String systemPrompt = """
                      你是一个专业的教务选课分析助手。请严格根据用户提供的【真实选课数据】来回答问题。
                      你的任务是分析和总结这些数据，用友好、专业的口吻回复用户。
                      如果数据中有相关记录，请直接基于数据回答；如果数据中没有相关信息，请如实告知。
                      **绝对不要编造不存在的信息！**
      
                      【真实选课数据】如下：
                      """ + realData;
      
              // 4. 调用模型，同时传入系统指令和用户问题
              return chatClient.prompt()
                      .system(s -> s.text(systemPrompt)) // 设置系统指令
                      .user(userMessage) // 设置用户问题
                      .call()
                      .content();
          }
      
          // 一个简单的方法示例，用于从问题中提取课程名
          private String extractCourseName(String message) {
              // 这里应该用更复杂的方式实现，例如关键词匹配或NLP模型
              // 为了方便演示，假设用户问题中第一个引号内的内容就是课程名
              // 例如：“选了「数据结构」的同学还会去选择什么”
              if (message.contains("“") && message.contains("”")) {
                  return message.substring(message.indexOf("“") + 1, message.indexOf("”"));
              } else if (message.contains("「") && message.contains("」")) {
                  return message.substring(message.indexOf("「") + 1, message.indexOf("」"));
              }
              // 如果无法提取，返回一个默认值或抛出异常，这里简单返回"数据结构"
              return "数据结构";
          }
      }
      ```

      

   3. **改造AIController**：

      - 你可以在Controller中新增一个端点，或者改造原来的端点，让它能区分是通用问题还是业务问题。

      java

      ```
      @PostMapping("/chat")
      public Map<String, String> chat(@RequestBody Map<String, String> request) {
          String userMessage = request.get("message");
          String response;
      
          // 简单判断：如果问题中包含“选”和“课”字，则认为是一个业务问题
          if (userMessage.contains("选") && userMessage.contains("课")) {
              response = aiService.chatAboutCourseSelection(userMessage);
          } else {
              response = aiService.chatWithAI(userMessage);
          }
      
          return Map.of("reply", response);
      }
      ```

      

   ------

   ### 第四阶段：Vue3前端集成

   这一步是构建用户界面，让用户可以通过网页提问并看到回答。

   1. **安装Axios**：

      - 在你的Vue3项目根目录下，运行 `npm install axios`。

   2. **创建API服务模块**：

      - 在 `src/services/` 目录下创建一个文件，例如 `api.js`。

        javascript

        ```
        import axios from 'axios';
        
        const API_BASE_URL = 'http://你的SpringBoot后端地址:端口'; // 例如 'http://localhost:8080'
        
        const apiClient = axios.create({
          baseURL: API_BASE_URL,
          headers: {
            'Content-Type': 'application/json',
          },
        });
        
        export const aiService = {
          chat(message) {
            return apiClient.post('/api/ai/chat', { message });
          },
        };
        ```

        

   3. **创建Vue组件**：

      - 创建一个组件，例如 `AIChat.vue`。

        vue

        ```
        <template>
          <div class="ai-chat">
            <h2>选课智能分析助手</h2>
            <div class="chat-box">
              <div v-for="(msg, index) in messages" :key="index" :class="['message', msg.role]">
                {{ msg.content }}
              </div>
            </div>
            <div class="input-area">
              <input v-model="userInput" @keyup.enter="sendMessage" placeholder="请输入您的问题，例如：选了「数据结构」的同学还会选什么课？" />
              <button @click="sendMessage" :disabled="isLoading">发送</button>
            </div>
          </div>
        </template>
        
        <script setup>
        import { ref } from 'vue';
        import { aiService } from '@/services/api';
        
        const userInput = ref('');
        const messages = ref([]);
        const isLoading = ref(false);
        
        const sendMessage = async () => {
          if (!userInput.value.trim() || isLoading.value) return;
        
          const userMessage = userInput.value;
          messages.value.push({ role: 'user', content: userMessage });
          userInput.value = '';
          isLoading.value = true;
        
          try {
            const response = await aiService.chat(userMessage);
            const aiReply = response.data.reply;
            messages.value.push({ role: 'assistant', content: aiReply });
          } catch (error) {
            console.error('调用AI失败:', error);
            messages.value.push({ role: 'assistant', content: '抱歉，出错了，请稍后再试。' });
          } finally {
            isLoading.value = false;
          }
        };
        </script>
        
        <style scoped>
        .chat-box {
          height: 300px;
          border: 1px solid #ccc;
          padding: 10px;
          margin-bottom: 10px;
          overflow-y: auto;
        }
        .message.user {
          text-align: right;
          color: blue;
        }
        .message.assistant {
          text-align: left;
          color: green;
        }
        .input-area {
          display: flex;
          gap: 10px;
        }
        input {
          flex-grow: 1;
          padding: 8px;
        }
        </style>
        ```

        

   4. **使用组件**：

      - 在你的主页面（例如 `App.vue` 或某个路由页面中）引入并使用这个 `AIChat` 组件。