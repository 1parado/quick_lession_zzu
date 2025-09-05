<template>
  <el-input v-model="searchText" style="width: 240px;margin-bottom: 30px" placeholder="请输入课程名" />
  <el-button style="margin-left: 20px;margin-bottom: 30px" type="primary" @click="searchCourse()" round>搜索</el-button>
  <el-table :data="courseTableData" stripe style="width: 100%" v-loading="vloading" v-if="courseTableData.length > 0">
    <el-table-column prop="name" label="课程名" wixdth="180" />
    <el-table-column prop="courseCode" label="课程代码" wixdth="180" />
    <el-table-column prop="courseType" label="课程类型" wixdth="180" />
    <el-table-column prop="college" label="所属学院" wixdth="180" />
    <el-table-column prop="semester" label="开设学期" wixdth="180" />
    <el-table-column prop="selectStatus" label="选择状态" width="120" align="center">
      <template #default="scope">
        <el-tag :type="scope.row.selectStatus === '已选' ? 'success' : 'info'">
          {{ scope.row.selectStatus }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column fixed="right" label="操作" min-width="180" align="center">
      <template #default="scope">
        <el-button link type="primary" @click="detailCourse(scope.row)">详情</el-button>
        <el-button link type="primary" @click="selectCourse(scope.row)" >选课</el-button>
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
      v-if="courseTableData.length > 0"
  />
  <el-empty :image-size="200" v-else style="margin-top: 175px"/>
  <!-- 详情页弹窗 -->
  <el-dialog v-model="CourseDetailDialogVisible" title="课程详情信息" width="1000" align-center>
    <div>
      <el-form :model="courseData" label-position="left" label-width="110px" :disabled="true">
        <el-form-item label="课程名" >
          <el-input v-model="courseData.name" />
        </el-form-item>
        <el-form-item label="课程代码" >
          <el-input v-model="courseData.courseCode" />
        </el-form-item>
        <el-form-item label="课程类型" >
          <el-input v-model="courseData.courseType" />
        </el-form-item>
        <el-form-item label="所属学院" >
          <el-input v-model="courseData.college" />
        </el-form-item>
        <el-form-item label="开设学期" >
          <el-input v-model="courseData.semester" />
        </el-form-item>
        <el-form-item label="学分" >
          <el-input v-model="courseData.credit" />
        </el-form-item>
        <el-form-item label="课程容量" >
          <el-input v-model="courseData.capacity" />
        </el-form-item>
        <el-form-item label="剩余名额" >
          <el-input v-model="courseData.remain" />
        </el-form-item>
        <el-form-item label="是否需要抢选" >
          <el-input v-model="courseData.isSeckill" />
        </el-form-item>
        <el-form-item label="任课教师" >
          <el-input v-model="courseData.teacherName" />
        </el-form-item>
        <el-form-item label="上课时间" >
          <el-input v-model="courseData.classTime" />
        </el-form-item>
        <el-form-item label="上课地点" >
          <el-input v-model="courseData.classLocation" />
        </el-form-item>
        <el-form-item label="周次范围" >
          <el-input v-model="courseData.weekRange" />
        </el-form-item>
        <el-form-item label="前置课程" >
          <el-input v-model="courseData.preCourseCode" />
        </el-form-item>
        <el-form-item label="课程描述" >
          <el-input v-model="courseData.description" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button type="primary" @click="CourseDetailDialogVisible = false">
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

//搜索框的输入内容
const searchText = ref('');

//  表格的加载动画参数
const vloading = ref(false);

//课程表格数据
const courseTableData = ref([{
  name: '',
  courseCode: '',
  courseType: '',
  college: '',
  semester: '',
  selectStatus: '' // 新增字段，用于显示选课状态
}])

//一个课程的信息
const courseData = ref()

//一次选择的信息
const selectionDate = ref()

//分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

//决定是否弹出课程详情页
const CourseDetailDialogVisible = ref(false);

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
    //获取普通课程列表
    const response = await axios.get('/cou/course/noseckill', {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })
    console.log(response)
    //获取当前学生的选课记录
    const selectionResponse  = await axios.get('/sel/selection/' + sessionStorage.getItem('studentId'))

    //合并数据
    const selectedCourseIds = selectionResponse.data.data.map(item => item.courseId);

    courseTableData.value = response.data.data.map(course => ({
      ...course,
      selectStatus: selectedCourseIds.includes(course.id) ? '已选' : '未选' // 动态计算选课状态
    }));

    total.value = Number(response.data.message);

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
const detailCourse = (rowData) => {
  //打开一个弹窗，用courseTableData传数据
  CourseDetailDialogVisible.value = true;
  console.log(rowData)
  courseData.value = rowData
}

/**
 * 选课操作
 * @param rowData
 */
const selectCourse = async (rowData) => {
  if (!window.confirm('确定选课？')) {
    return;
  }
  vloading.value = true;
  if (rowData.selectStatus === '已选') {
    ElMessage("请勿重复选择")
    vloading.value = false;
    return;
  }

  selectionDate.value = {
    "id": null,
    "courseId": rowData.id,
    "studentId": sessionStorage.getItem('studentId'),
    "semester": rowData.semester,
    "selectionTime": null,
    "status": 'NORMAL',
    "isSeckill": 0
  }

  const response = await axios.post('/sel/selection', selectionDate.value,  {
    headers: {
      'Content-Type': 'application/json'
    }
  })
  console.log(response);
  if (response.data.code === 200) {
    ElMessage.success("选课成功")
    rowData.selectStatus = '已选';
    fetchData();
    vloading.value = false;
  } else if (response.data.message == '前置课程未选择'){
    ElMessage.error("前置课程未选择")
    fetchData();
    vloading.value = false;
  } else if (response.data.message == '课程时间冲突'){
    ElMessage.error("课程时间冲突")
    fetchData();
    vloading.value = false;
  } else {
    ElMessage.error("选课失败")
    fetchData()
    vloading.value = false;
  }
}

/**
 * 模糊搜索功能
 */
const searchCourse = async () => {
  if (searchText.value === '' || searchText.value.length <= 0) {
    fetchData();
    return;
  }
  try {
    // 获取普通、满足条件的课程列表
    const response = await axios.get('/cou/course/search/' + searchText.value, {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    });

    //获取当前学生的选课记录
    const selectionResponse  = await axios.get('/sel/selection/' + sessionStorage.getItem('studentId'))

    //合并数据
    const selectedCourseIds = selectionResponse.data.data.map(item => item.courseId);

    courseTableData.value = response.data.data.map(course => ({
      ...course,
      selectStatus: selectedCourseIds.includes(course.id) ? '已选' : '未选' // 动态计算选课状态
    }));

    total.value = Number(response.data.message);
  } catch (error) {
    console.log(error)
  }
}

</script>


<style scoped>

</style>