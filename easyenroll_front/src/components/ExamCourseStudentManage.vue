
<template>

  <el-breadcrumb :separator-icon="ArrowRight">
    <el-breadcrumb-item :to="{ path: '/' }">主页</el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: '/examManage'}">考试管理</el-breadcrumb-item>
    <el-breadcrumb-item>学生列表</el-breadcrumb-item>
  </el-breadcrumb>

  <el-button type="primary" plain @click="exportStudentList()" style="margin-bottom: -20px;margin-top: 30px" v-if="studentTableData">导出课程表</el-button>


  <el-table :data="studentTableData" stripe style="width: 100%; margin-top: 30px" v-if="studentTableData">
    <el-table-column prop="name" label="姓名" wixdth="180" />
    <el-table-column prop="sno" label="学号" wixdth="180" />
    <el-table-column prop="college" label="学院" wixdth="180" />
    <el-table-column prop="major" label="专业" wixdth="180" />
    <el-table-column fixed="right" label="操作" min-width="180" align="center">
      <template #default="scope">
        <el-button link type="primary" @click="detailStudent(scope.row)">详情</el-button>
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
      v-if="studentTableData"
  />
  <el-empty :image-size="200" v-else style="margin-top: 175px" description='暂无数据'/>


  <!-- 详情页弹窗 -->
  <el-dialog v-model="StudentDetailDialogVisible" title="学生详情信息" width="1000">
    <div>
      <el-form :model="studentData" label-position="left" label-width="80px" :disabled="true">
        <el-form-item label="姓名" >
          <el-input v-model="studentData.name" />
        </el-form-item>
        <el-form-item label="学号" >
          <el-input v-model="studentData.sno" />
        </el-form-item>
        <el-form-item label="学院" >
          <el-input v-model="studentData.college" />
        </el-form-item>
        <el-form-item label="专业" >
          <el-input v-model="studentData.major" />
        </el-form-item>
        <el-form-item label="性别" >
          <el-input v-model="studentData.gender" />
        </el-form-item>
        <el-form-item label="年龄" >
          <el-input v-model="studentData.age" />
        </el-form-item>
        <el-form-item label="学分" >
          <el-input v-model="studentData.credit" />
        </el-form-item>
        <el-form-item label="联系方式" >
          <el-input v-model="studentData.phone" />
        </el-form-item>
        <el-form-item label="入学时间" >
          <el-input v-model="studentData.inputTime" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button type="primary" @click="StudentDetailDialogVisible = false">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>

</template>


<script lang="ts" setup>
import {onMounted, ref} from "vue";
import axios from '../utils/http.js';
import {ElMessage, ElPagination} from 'element-plus';
import {ArrowRight} from "@element-plus/icons-vue";
import {useRoute} from "vue-router";

const route = useRoute();
const courseId = route.query.id; // 获取 id
console.log(courseId)

//学生表格数据
const studentTableData = ref([{
  name: '',
  sno: '',
  college: '',
  major: '',
}])

//一个学生的信息
const studentData = ref()

//分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

//决定是否弹出学生详情页
const StudentDetailDialogVisible = ref(false);

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
    const response = await axios.get('/stu/student/' + courseId, {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })
    console.log(response);
    studentTableData.value = response.data.data;
    total.value = Number(response.data.message);
    console.log("Total:", total.value, "Type:", typeof total.value);
  } catch (error) {
    console.error('获取数据失败:', error);
  }
}

/**
 * 页码变化处理
 * @param page
 */
const handlePageChange = (page) => {
  currentPage.value = page
  fetchData()
}

/**
 * 详情,打开弹窗
 * @param rowData
 */
const detailStudent = (rowData) => {
  //打开一个弹窗，用studentTableData传数据
  StudentDetailDialogVisible.value = true;
  console.log(rowData)
  studentData.value = rowData
}

/**
 * 导出学生列表
 */
const exportStudentList = async () => {
  try {
    // 关键：设置responseType为blob，接收二进制流
    const response = await axios.get('/exc/export/student/' + courseId, {
      responseType: 'blob' // 必须设置，否则会解析为JSON导致乱码
    });

    // 从响应头获取文件名（如果后端设置了的话）
    let fileName = '学生列表.xlsx'; // 默认文件名


    // 创建Blob对象（指定MIME类型为Excel）
    const blob = new Blob([response.data], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    });

    // 创建下载链接并触发下载
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = fileName; // 设置文件名
    document.body.appendChild(a);
    a.click(); // 触发点击下载

    // 清理资源
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);

    ElMessage.success('导出成功');
  } catch (error) {
    console.error('导出失败:', error);
    // 处理后端返回的错误信息（如果后端在异常时返回JSON）
    if (error.response?.data instanceof Blob) {
      // 若错误响应是Blob，尝试解析为JSON
      const reader = new FileReader();
      reader.onload = () => {
        const errorMsg = JSON.parse(reader.result).message || '导出失败';
        ElMessage.error(errorMsg);
      };
      reader.readAsText(error.response.data);
    } else {
      ElMessage.error('导出失败，请重试');
    }
  }
}

</script>


<style scoped>

</style>