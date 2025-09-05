<template>
  <div class="flex gap-4">
    <div style="margin-left: 20px">
      <el-autocomplete
          v-model="searchText"
          :fetch-suggestions="querySearch"
          :trigger-on-focus="false"
          clearable
          class="inline-input w-50"
          placeholder="请输入学生姓名或学号"
          @select="handleSelect"
          :debounce="300"
          style="width: 300px;"
      >
        <template #default="{ item }">
          <div>{{ item.value }} ({{ item.sno }})</div>
        </template>
      </el-autocomplete>
      <el-button style="margin-left: 20px" type="primary" @click="searchStudent()" round>搜索</el-button>
    </div>
    <div style="margin-left: 20px; margin-top: 50px">
      <el-table :data="courseTableData" stripe style="width: 100%"  v-if="courseTableData.length > 0">
        <el-table-column prop="name" label="课程名" wixdth="180" />
        <el-table-column prop="courseCode" label="课程代码" wixdth="180" />
        <el-table-column prop="courseType" label="课程类型" wixdth="180" />
        <el-table-column prop="college" label="所属学院" wixdth="180" />
        <el-table-column prop="semester" label="开设学期" wixdth="180" />
        <el-table-column fixed="right" label="操作" min-width="180" align="center">
          <template #default="scope">
            <el-button link type="primary" @click="detailCourse(scope.row)">详情</el-button>
            <el-button link type="primary" @click="selectCourse(scope.row.id)" >退课</el-button>
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
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import axios from "../utils/http"
import {ElMessage, ElPagination} from "element-plus";

interface StudentItem {
  value: string  // 显示文本
  sno: string    // 学号
  id: number     // 学生ID
}

const searchText = ref('')
const searchValue = ref('')
const loading = ref(false)

//课程表格数据
/*const courseTableData = ref([{
  name: '',
  courseCode: '',
  courseType: '',
  college: '',
  semester: ''
}])*/

const courseTableData = ref([]);

//一个课程的信息
const courseData = ref()

//分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

//决定是否弹出课程详情页
const CourseDetailDialogVisible = ref(false);

/**
 * 获取搜索建议
 */
const querySearch = async (queryString: string, cb: (items: StudentItem[]) => void) => {
  if (!queryString.trim()) {
    cb([])
    return
  }

  loading.value = true
  try {
    const response = await axios.get('/stu/student/suggestions', {
      params: { keyword: queryString }
    })

    if (response.data.code === 200) {
      // 转换数据结构，适配el-autocomplete
      const suggestions = response.data.data.map((student: any) => ({
        value: student.name,  // 显示姓名
        sno: student.sno,     // 学号
        id: student.id        // 学生ID
      }))
      searchValue.value = suggestions.valueOf()[0].id
      cb(suggestions)
    } else {
      cb([])
    }
  } catch (error) {
    console.error('获取学生搜索建议失败:', error)
    cb([])
  } finally {
    loading.value = false
  }
}

/**
 * 选中建议项
 */
const handleSelect = (item: StudentItem) => {
  // 这里设置搜索框显示学号
  searchValue.value = String(item.id)
}

/**
 * 搜索
 */
const searchStudent = async () => {
  const response = await axios.get('/sel/selection/list/' + searchValue.value, {
    params: {
      page: currentPage.value,
      size: pageSize.value
    }
  });
  if (response.data.code === 200) {
    courseTableData.value = response.data.data;
    total.value = Number(response.data.message);
  }
}

/**
 * 页码变化处理
 * @param page
 */
const handlePageChange = (page) => {
  currentPage.value = page
  searchStudent()
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
 * 退课操作
 * @param courseId
 */
const selectCourse = async (courseId) => {
  //这个id是课程id
  if (!window.confirm('确定退课？该课程可能是其他课程的前置课程，请谨慎选择')) {
    return;
  }
  console.log("id")
  console.log(courseId)

  const response = await axios.delete('/sel/selection/' + courseId + '/' + searchValue.value)
  console.log(response);
  if (response.data.code === 200) {
    ElMessage('退课成功')
    searchStudent();
  } else {
    ElMessage('退课失败')
    searchStudent();
  }
}

</script>

<style scoped>
/* 可以添加加载状态样式 */
.el-autocomplete {
  width: 300px;
}
</style>