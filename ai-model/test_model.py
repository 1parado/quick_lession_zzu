from transformers import AutoTokenizer, AutoModelForSequenceClassification
import torch
import numpy as np

# 加载模型
model_path = "./course_load_model"
print("正在加载模型...")
tokenizer = AutoTokenizer.from_pretrained(model_path)
model = AutoModelForSequenceClassification.from_pretrained(model_path)
model.eval()
print("模型加载完成！")

# 测试数据 - 使用你训练时的1分数据
test_texts = [
    "这门课 workload 爆炸，每周都有 deadline，作业量巨大无比，考试难度天花板，不建议同时上其他硬课。",  # 应该是1分
    "噩梦般的课程，需要投入所有时间和精力，project 极其复杂，挂科率超高，慎选！",  # 应该是1分
    "著名的水课，老师不点名，没有作业，期末开卷考，随便学学就能过。",  # 应该是5分
    "超级放松的课程，workload 几乎为零，对 GPA 很友好，是完美的选修课。",  # 应该是5分
    "普通的课程，有一定作业量但不算多",  # 应该是3分
]

for i, text in enumerate(test_texts, 1):
    # Tokenize
    inputs = tokenizer(
        text,
        return_tensors="pt",
        truncation=True,
        max_length=128,
        padding="max_length"
    )
    
    # Predict
    with torch.no_grad():
        outputs = model(**inputs)
        logits = outputs.logits
        probabilities = torch.nn.functional.softmax(logits, dim=-1)
        predicted_class = torch.argmax(probabilities, dim=-1).item()
        confidence = probabilities[0][predicted_class].item()
    
    # 转换回1-5分
    final_score = predicted_class + 1
    
    print(f"文本 {i}:")
    print(f"  内容: {text[:50]}...")
    print(f"  预测分数: {final_score}")
    print(f"  置信度: {confidence:.4f}")
    print(f"  所有类别概率: {[f'{p:.4f}' for p in probabilities[0].tolist()]}")
    print("-" * 80)