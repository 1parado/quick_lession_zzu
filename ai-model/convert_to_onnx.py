import torch
from transformers import AutoTokenizer, AutoModelForSequenceClassification
import numpy as np

# 加载训练好的模型
model_path = "./course_load_model"
print("正在加载模型和分词器...")
tokenizer = AutoTokenizer.from_pretrained(model_path)
model = AutoModelForSequenceClassification.from_pretrained(model_path)

# 设置为评估模式
model.eval()

# 创建示例输入
print("创建示例输入...")
dummy_input = tokenizer(
    "这是一个测试句子，用于模型转换。这门课程作业很多，需要花费大量时间。", 
    return_tensors="pt", 
    padding="max_length", 
    truncation=True, 
    max_length=128
)

# 导出为ONNX格式（使用新的导出方法）
print("开始转换模型为ONNX格式...")
torch.onnx.export(
    model,
    (dummy_input['input_ids'], dummy_input['attention_mask']),
    "course_load_model.onnx",
    input_names=['input_ids', 'attention_mask'],
    output_names=['logits'],
    dynamic_axes={
        'input_ids': {0: 'batch_size', 1: 'sequence_length'},
        'attention_mask': {0: 'batch_size', 1: 'sequence_length'},
        'logits': {0: 'batch_size'}
    },
    opset_version=14,
    export_params=True,
    do_constant_folding=True,
    verbose=True
)

print("模型已成功转换为ONNX格式: course_load_model.onnx")

# 测试ONNX模型
print("测试ONNX模型...")
import onnxruntime as ort

# 加载ONNX模型
ort_session = ort.InferenceSession("course_load_model.onnx")

# 准备输入
inputs = {
    'input_ids': dummy_input['input_ids'].numpy(),
    'attention_mask': dummy_input['attention_mask'].numpy()
}

# 运行推理
outputs = ort_session.run(None, inputs)
logits = outputs[0]

# 计算softmax概率
probabilities = np.exp(logits) / np.sum(np.exp(logits), axis=1, keepdims=True)
predicted_class = np.argmax(probabilities, axis=1)[0]
confidence = probabilities[0][predicted_class]

print(f"预测结果: 类别 {predicted_class}, 置信度 {confidence:.4f}")
print(f"对应评分: {predicted_class + 1}分")
print("转换和测试完成！")