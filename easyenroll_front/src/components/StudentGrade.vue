<template>

  <!-- 在el-table上方添加筛选区域 -->
  <div class="filter-container">
    <el-select v-model="selectedSemester" placeholder="请选择学期" @change="handleSemesterChange" style="width: 500px">
      <el-option label="全部学期" value="all" />
      <el-option
          v-for="semester in semesterOptions"
          :key="semester"
          :label="semester"
          :value="semester"
      />
    </el-select>

    <!-- 添加统计信息 -->
    <div class="stats" v-if="selectedSemester !== 'all'" style="margin-left: 940px">
      <span>当前学期平均分：{{ averageGrade }}</span>
    </div>
  </div>

  <el-button type="primary" plain @click="exportGrade()" style="margin-left: 1460px;margin-bottom: 30px; margin-top: 0px" >导出成绩单（所有课程）</el-button>

  <el-table :data="courseTableData" stripe style="width: 100%" v-if="courseTableData.length > 0">
    <el-table-column prop="name" label="课程名" />
    <el-table-column prop="courseCode" label="课程代码" />
    <el-table-column prop="courseType" label="课程类型" />
    <el-table-column prop="semester" label="开设学期" />
    <el-table-column prop="grade" label="成绩" />
    <el-table-column prop="state" label="状态">
      <template #default="scope">
        <span :class="'status-' + getStatusClass(scope.row.state)">
          {{ getStatusText(scope.row.state) }}
        </span>
      </template>
    </el-table-column>
    <el-table-column fixed="right" label="操作" min-width="180" align="center">
      <template #default="scope">
        <el-button link type="primary" @click="detailCourse(scope.row)">详情</el-button>
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

  <el-button type="primary" plain @click="router.push('/leaderboard')">优秀学子</el-button>

</template>


<script lang="ts" setup>
import {computed, onMounted, ref} from "vue";
import axios from '../utils/http.js';
import {ElMessage, ElPagination} from 'element-plus';
import router from "../router";

//课程表格数据
const courseTableData = ref([{
  name: '',
  courseCode: '',
  courseType: '',
  semester: '',
  grade: '',
  state: ''
}])

//一个课程的信息
const courseData = ref()

//分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

//决定是否弹出课程详情页
const CourseDetailDialogVisible = ref(false)

//学期划分相关
const selectedSemester = ref('all'); // 默认选择全部学期
const semesterOptions = ref([]); // 学期选项
const allCourseData = ref([]); // 存储所有课程数据

// 计算属性 - 根据选择的学期过滤数据
const filteredCourseData = computed(() => {
  if (selectedSemester.value === 'all') {
    return allCourseData.value;
  }
  return allCourseData.value.filter(course => course.semester === selectedSemester.value);
});

// 计算属性 - 计算平均分
const averageGrade = computed(() => {
  if (filteredCourseData.value.length === 0) return 0;

  const sum = filteredCourseData.value.reduce((total, course) => {
    return total + (parseFloat(course.grade) || 0);
  }, 0);

  return (sum / filteredCourseData.value.length).toFixed(2);
});

/**
 * 初始化加载数据
 */
onMounted(() => {
  fetchData();
})


/**
 * 获取数据
 */
const fetchData = async () => {
  try {
    //获取所有已经有成绩的（grade表）的 课程 + 成绩 数据
    const response = await axios.get('/grade/grade/' + sessionStorage.getItem('account'), {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })

    // 保存所有数据
    allCourseData.value = response.data.data;
    courseTableData.value = response.data.data;
    total.value = Number(response.data.message);

    // 提取学期选项
    extractSemesterOptions();
  } catch (error) {
    console.error('获取数据失败:', error);
  }
}

// 提取学期选项
const extractSemesterOptions = () => {
  const semesters = new Set();
  allCourseData.value.forEach(course => {
    if (course.semester) {
      semesters.add(course.semester);
    }
  });
  semesterOptions.value = Array.from(semesters).sort().reverse(); // 按时间倒序排列
}

// 学期变更处理
const handleSemesterChange = (semester) => {
  currentPage.value = 1; // 重置到第一页
  if (semester === 'all') {
    courseTableData.value = allCourseData.value;
  } else {
    courseTableData.value = allCourseData.value.filter(course => course.semester === semester);
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

// 获取状态文本
const getStatusText = (state) => {
  const statusMap = {
    1: '正常',
    0: '缺考',
    2: '作弊'
  }
  return statusMap[state] || '未知状态'
}

// 获取状态对应的CSS类名
const getStatusClass = (state) => {
  const classMap = {
    1: 'normal',
    0: 'absent',
    2: 'cheat'
  }
  return classMap[state] || 'unknown'
}

/**
 * 导出学生成绩列表（全部导出）
 */
const exportGrade = async () => {
  try {
    //响应类型为blob
    const response = await axios.get('/exc/export/grade/' + sessionStorage.getItem('account'), {
      responseType: "blob"
    })
    //文件名
    let fileName = '成绩单_' + new Date() + ".xlsx";

    //创建Blob对象
    const blob = new Blob([response.data], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })

    //创建下载链接并触发下载
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a')
    a.href = url;
    a.download = fileName;
    document.body.appendChild(a);
    a.click();

  } catch (e) {
    ElMessage.error('下载文件失败');
  }
}


</script>


<style scoped>
.filter-container {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
}

.filter-label {
  font-weight: 500;
}

.stats {
  margin-left: auto;
  font-size: 14px;
  color: #606266;
}

.status-normal {
  color: #67c23a;
  font-weight: 500;
}
.status-absent {
  color: #909399;
  font-weight: 500;
}
.status-cheat {
  color: #e6a23c;
  font-weight: 500;
}
.status-unknown {
  color: #f56c6c;
  font-weight: 500;
}
</style>