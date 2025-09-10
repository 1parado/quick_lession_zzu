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