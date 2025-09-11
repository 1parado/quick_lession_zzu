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
import axios from '../../utils/http.js'

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
    const response = await axios.post('/ai/chat/sync', null, {
      params: {
        message: userMessage
      },
      timeout: 60000
    });
    console.log("调用成功");
    console.log(response);
    const aiReply = response.data.data;
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
.ai-chat {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  height: 100%;
  display: flex;
  flex-direction: column;
}

h2 {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 20px;
  font-weight: 600;
  font-size: 24px;
}

.chat-box {
  flex: 1;
  border: 1px solid #e1e4e8;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 20px;
  overflow-y: auto;
  background: white;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.message {
  padding: 12px 16px;
  border-radius: 18px;
  max-width: 80%;
  line-height: 1.5;
  word-wrap: break-word;
}

.message.user {
  align-self: flex-end;
  background: #007bff;
  color: white;
  border-bottom-right-radius: 4px;
}

.message.assistant {
  align-self: flex-start;
  background: #f1f3f5;
  color: #333;
  border-bottom-left-radius: 4px;
}

.input-area {
  display: flex;
  gap: 12px;
  align-items: center;
}

input {
  flex-grow: 1;
  padding: 14px 16px;
  border: 1px solid #ddd;
  border-radius: 24px;
  font-size: 16px;
  outline: none;
  transition: border-color 0.3s;
}

input:focus {
  border-color: #007bff;
  box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
}

button {
  padding: 14px 24px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 24px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
  transition: background-color 0.3s;
}

button:hover:not(:disabled) {
  background: #0056b3;
}

button:disabled {
  background: #6c757d;
  cursor: not-allowed;
}

/* 滚动条样式 */
.chat-box::-webkit-scrollbar {
  width: 6px;
}

.chat-box::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.chat-box::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 10px;
}

.chat-box::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>