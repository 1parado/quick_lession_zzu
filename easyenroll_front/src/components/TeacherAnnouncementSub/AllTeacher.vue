
<template>
  <el-text class="mx-1" type="primary"  style="font-size: 28px;" >最新公告</el-text>

  <div style="padding-top: 25px">
      <el-form :model="announcementData" label-position="left" label-width="80px" :disabled="true">
        <el-form-item label="发布人" >
          <el-input v-model="announcementData.publicName" />
        </el-form-item>
        <el-form-item label="发布时间" >
          <el-input :value="formatTime(announcementData.publishTime)" />
        </el-form-item>
        <el-form-item label="公告标题" >
          <el-input v-model="announcementData.title" />
        </el-form-item>
        <el-form-item label="公告状态" >
          <el-input v-model="announcementData.status" />
        </el-form-item>
        <el-form-item label="公告内容" >
          <el-input type='textarea' v-model="announcementData.content" />
        </el-form-item>
      </el-form>
  </div>
</template>



<script setup>

import {onMounted, ref} from "vue";
import axios from '../../utils/http.js';


const announcementData = ref({});

// 定义时间格式化函数
const formatTime = (time) => {
  if (!time) return ''; // 处理空值情况
  const date = new Date(time);
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  const hours = date.getHours().toString().padStart(2, '0');
  const minutes = date.getMinutes().toString().padStart(2, '0');
  const seconds = date.getSeconds().toString().padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

/**
 * 初始化加载数据
 */
onMounted(() => {
  fetchData()
})

/**
 * 获取数据
 */
const fetchData = async () => {
  try {
    const response = await axios.get('/ann/announcement/' + 3, {
      params: {
        page: 0,
        size: 0
      }
    })
    console.log(response);
    announcementData.value = response.data.data || {}; // 确保即使数据为空也有默认对象
  } catch (error) {
    console.error('获取数据失败:', error);
  }
}



</script>

<style scoped>

</style>