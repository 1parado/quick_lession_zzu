
<template>
  <el-table :data="examTableDate" stripe style="width: 100%" v-if="examTableDate.length > 0">
    <el-table-column prop="name" label="课程名" width="180" />
    <el-table-column prop="courseCode" label="课程代码" width="180" />
    <el-table-column prop="courseType" label="课程类型" width="180" />
    <el-table-column prop="college" label="开设学院" width="180" />
    <el-table-column prop="credit" label="学分" width="180" />
    <el-table-column prop="examStartTime" label="考试开始时间" width="180" >
      <template #default="scope">
        {{ formatTime(scope.row.examStartTime) }}
      </template>
    </el-table-column>
    <el-table-column prop="examEndTime" label="考试结束时间" width="180" >
      <template #default="scope">
        {{ formatTime(scope.row.examEndTime) }}
      </template>
    </el-table-column>
    <el-table-column prop="state" label="状态">
      <template #default="scope">
        <span :class="'status-' + getStatusClass(scope.row.state)">
          {{ getStatusText(scope.row.state) }}
        </span>
      </template>
    </el-table-column>
  </el-table>
  <!-- 分页条 -->
  <el-pagination
      :current-page="currentPage"
      :page-size="pageSize"
      :total="total"
      layout="prev, pager, next"
      @current-change="handlePageChange"
      style="margin-top: 20px; justify-content: center;"
      v-if="examTableDate.length > 0"
  />
  <el-empty :image-size="200" v-else style="margin-top: 175px"/>
</template>

<script setup>

import {ElMessage, ElPagination} from "element-plus";
import {onMounted, ref} from "vue";
import axios from "../utils/http.js";



//考试安排表格数据
const examTableDate = ref([{
  name: '',
  courseCode: '',
  courseType: '',
  college: '',
  credit: '',
  examStartTime: '',
  examEndTime: ''
}])

//分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

onMounted(() => {
  fetchData();
})

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

// 页码变化处理
const handlePageChange = (page) => {
  currentPage.value = page
  fetchData()
}

/**
 * 初始化考试安排数据数据
 */
const fetchData = async () => {
  try {
    const response = await axios.get('/exam/exam/' + sessionStorage.getItem('account'), {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })
    if (response.data.code === 200) {
      examTableDate.value = response.data.data;
      total.value = Number(response.data.message);
    } else {
      ElMessage.error("获取数据失败")
    }
  } catch (error) {
    console.error('获取数据失败:', error);
  }
}

// 获取状态文本
const getStatusText = (state) => {
  const statusMap = {
    1: '正常',
    0: '取消',
    2: '延迟',
    3: '考试已结束'
  }
  return statusMap[state] || '未知状态'
}

// 获取状态对应的CSS类名
const getStatusClass = (state) => {
  const classMap = {
    1: 'normal',
    0: 'cancel',
    2: 'delay',
    3: 'finished'
  }
  return classMap[state] || 'unknown'
}

</script>

<style scoped>
.status-normal {
  color: #67c23a;
  font-weight: 500;
}
.status-cancel {
  color: #909399;
  font-weight: 500;
}
.status-delay {
  color: #e6a23c;
  font-weight: 500;
}
.status-unknown {
  color: #f56c6c;
  font-weight: 500;
}
.status-finished {
  color: #909399;  /* 可以使用灰色表示已结束 */
  font-weight: 500;
}
</style>