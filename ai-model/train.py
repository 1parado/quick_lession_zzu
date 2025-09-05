# 安装所需的库。复制到cmd中运行，而不是这里。
# pip install transformers datasets torch scikit-learn pandas

# 在 train.py 的最最开头添加这两行！
import os
os.environ['HF_ENDPOINT'] = 'https://hf-mirror.com'  # 使用国内镜像站
os.environ['HF_HUB_DISABLE_SYMLINKS_WARNING'] = '1'  # 添加这行来禁用symlinks警告

import pandas as pd
from datasets import Dataset
from transformers import (
    AutoTokenizer,
    AutoModelForSequenceClassification,
    TrainingArguments,
    Trainer,
    DataCollatorWithPadding
)
import numpy as np
from sklearn.metrics import accuracy_score
import os

# 1. 读取数据
print("正在读取数据...")
df = pd.read_csv('course_load_data.csv')

# 添加标签转换：将1-5转换为0-4
df['label'] = df['label'] - 1
print("标签分布转换后:")
print(df['label'].value_counts().sort_index())

dataset = Dataset.from_pandas(df)

# 2. 选择模型和分词器
model_name = "hfl/chinese-macbert-base"
print(f"正在加载模型和分词器: {model_name}")
tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModelForSequenceClassification.from_pretrained(model_name, num_labels=5)  # 仍然是5个类别，但标签是0-4

# 3. 对数据进行分词处理
def tokenize_function(examples):
    # 对文本进行tokenize
    tokenized = tokenizer(
        examples['text'], 
        truncation=True, 
        max_length=128, 
        padding='max_length'
    )
    # 确保返回label字段（现在已经是0-4）
    tokenized['labels'] = examples['label']
    return tokenized

print("正在处理文本数据...")
tokenized_dataset = dataset.map(tokenize_function, batched=True, remove_columns=['text'])

# 4. 拆分数据集 (80%训练, 20%验证)
split_dataset = tokenized_dataset.train_test_split(test_size=0.2, seed=42)
train_dataset = split_dataset['train']
eval_dataset = split_dataset['test']

# 5. 设置训练参数
training_args = TrainingArguments(
    output_dir="./course_load_model",  # 输出目录
    num_train_epochs=5,               # 训练5轮
    per_device_train_batch_size=4,    # 批量大小
    per_device_eval_batch_size=4,
    learning_rate=2e-5,
    eval_strategy="epoch",            # 改：evaluation_strategy -> eval_strategy
    save_strategy="epoch",            # 这行保持不变
    logging_dir='./logs',
    remove_unused_columns=False       # 防止报错
)

# 6. 定义评估指标
def compute_metrics(eval_pred):
    predictions, labels = eval_pred
    predictions = np.argmax(predictions, axis=1)
    return {'accuracy': accuracy_score(labels, predictions)}

# 7. 创建训练器
# 由于我们已经做了padding，可以不需要DataCollator
trainer = Trainer(
    model=model,
    args=training_args,
    train_dataset=train_dataset,
    eval_dataset=eval_dataset,
    # 删除 data_collator=data_collator, 让Trainer使用默认的
    compute_metrics=compute_metrics,
)

# 8. 开始训练！
print("开始训练模型，请耐心等待...")
train_result = trainer.train()

# 9. 保存最终模型
print("训练完成！正在保存模型...")
output_dir = "./course_load_model"
trainer.save_model(output_dir)
tokenizer.save_pretrained(output_dir)
print(f"模型已保存至: {os.path.abspath(output_dir)}")
print("所有步骤已完成！")