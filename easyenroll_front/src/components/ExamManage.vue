<template>

  <el-breadcrumb :separator-icon="ArrowRight">
    <el-breadcrumb-item :to="{ path: '/' }">主页</el-breadcrumb-item>
    <el-breadcrumb-item>考试管理</el-breadcrumb-item>
  </el-breadcrumb>

  <el-button type="primary" plain @click="openExamPlan()" style="margin-bottom: 0px; margin-top: 40px">查看全部考试安排</el-button>

  <el-table :data="courseTableData" stripe style="width: 100%; margin-top: 30px" v-if="courseTableData.length > 0">
    <el-table-column prop="name" label="课程名" wixdth="180" />
    <el-table-column prop="courseCode" label="课程代码" wixdth="180" />
    <el-table-column prop="courseType" label="课程类型" wixdth="180" />
    <el-table-column prop="college" label="所属学院" wixdth="180" />
    <el-table-column prop="semester" label="开设学期" wixdth="180" />
    <el-table-column fixed="right" label="操作" min-width="180" align="center">
      <template #default="scope">
        <el-button link type="primary" @click="detailCourse(scope.row)">详情</el-button>
        <el-button link type="primary" @click="detailStudent(scope.row.id)">查看</el-button>
        <el-button
            v-if="!scope.row.hasExam"
            link type="primary"
            @click="openExamDialog(scope.row)">
          添加考试安排
        </el-button>
        <el-button
            v-else
            link type="primary"
            @click="openUpdateExamDialog(scope.row)">
          修改考试安排
        </el-button>
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
  <el-empty :image-size="200" v-else style="margin-top: 175px" description='您还未教授任何课程'/>
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

  <!-- 考试安排新增弹窗 -->
  <el-dialog v-model="InsertExamDialogVisible" title="添加考试安排" width="1000" align-center>
    <div>
      <el-form :model="examPlanData" label-position="left" label-width="110px">
        <el-form-item label="课程名" >
          <el-input v-model="examPlanData.name" disabled />
        </el-form-item>
        <el-form-item label="课程代码" >
          <el-input v-model="examPlanData.courseCode" disabled  />
        </el-form-item>
        <el-form-item label="课程类型" >
          <el-input v-model="examPlanData.courseType" disabled />
        </el-form-item>
        <el-form-item label="所属学院" >
          <el-input v-model="examPlanData.college" disabled />
        </el-form-item>
        <el-form-item label="学分" >
          <el-input v-model="examPlanData.credit" disabled />
        </el-form-item>
        <el-form-item label="考试开始时间" >
          <el-date-picker
              style="width: 1000px"
              v-model.number="examPlanData.examStartTime"
              type="datetime"
              placeholder="选择开始时间"
              :disabled-date="disabledDate"
          />
        </el-form-item>
        <el-form-item label="考试结束时间" >
          <el-date-picker
              style="width: 1000px"
              v-model.number="examPlanData.examEndTime"
              type="datetime"
              placeholder="选择结束时间"
              :disabled-date="disabledDate"
          />
        </el-form-item>

      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="InsertExamDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="insertExam()">
          新增考试安排
        </el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 考试安排修改弹窗 -->
  <el-dialog v-model="UpdateExamDialogVisible" title="修改考试安排" width="1000" align-center>
    <div>
      <el-form :model="examPlanData" label-position="left" label-width="110px">
        <el-form-item label="课程名" >
          <el-input v-model="examPlanData.name" disabled />
        </el-form-item>
        <el-form-item label="课程代码" >
          <el-input v-model="examPlanData.courseCode" disabled  />
        </el-form-item>
        <el-form-item label="课程类型" >
          <el-input v-model="examPlanData.courseType" disabled />
        </el-form-item>
        <el-form-item label="所属学院" >
          <el-input v-model="examPlanData.college" disabled />
        </el-form-item>
        <el-form-item label="学分" >
          <el-input v-model="examPlanData.credit" disabled />
        </el-form-item>
        <el-form-item label="考试开始时间" >
          <el-date-picker
              style="width: 1000px"
              v-model.number="examPlanData.examStartTime"
              type="datetime"
              placeholder="选择开始时间"
              :disabled-date="disabledDate"
          />
        </el-form-item>
        <el-form-item label="考试结束时间" >
          <el-date-picker
              style="width: 1000px"
              v-model.number="examPlanData.examEndTime"
              type="datetime"
              placeholder="选择结束时间"
              :disabled-date="disabledDate"
          />
        </el-form-item>

      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="UpdateExamDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="updateExam()">
          修改考试安排
        </el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 考试安排展示弹窗 -->
  <el-dialog v-model="ExamDialogVisible" title="考试安排" width="1500" align-center>
    <div>
      <el-table :data="examTableDate" stripe style="width: 100%" v-if="examTableDate.length > 0">
        <el-table-column prop="name" label="课程名" wixdth="180" />
        <el-table-column prop="courseCode" label="课程代码" wixdth="180" />
        <el-table-column prop="courseType" label="课程类型" wixdth="180" />
        <el-table-column prop="college" label="开设学院" wixdth="180" />
        <el-table-column prop="credit" label="学分" wixdth="180" />
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
        <el-table-column fixed="right" label="操作" min-width="180" align="center">
          <template #default="scope">
            <el-button link type="primary" @click="closeExam(scope.row)" >结束考试</el-button>
            <el-button link type="primary" @click="deleteExam(scope.row.id)" >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页条 -->
      <el-pagination
          :current-page="currentPageExam"
          :page-size="pageSizeExam"
          :total="totalExam"
          layout="prev, pager, next"
          @current-change="handlePageChangeExam"
          style="margin-top: 20px; justify-content: center;"
          v-if="examTableDate.length > 0"
      />
      <el-empty :image-size="200" v-else style="margin-top: 175px"/>
    </div>
  </el-dialog>

</template>


<script lang="ts" setup>
import {onMounted, ref} from "vue";
import axios from '../utils/http.js';
import {ElMessage, ElPagination} from 'element-plus';
import { ArrowRight } from '@element-plus/icons-vue'
import router from "../router";

//课程表格数据
const courseTableData = ref([{
  name: '',
  courseCode: '',
  courseType: '',
  college: '',
  semester: ''
}])

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

//一个课程的信息
const courseData = ref()

//一个考试安排的信息
const examPlanData = ref()

// 专门用于新增考试安排的初始化方法
const initExamPlanForCreate = () => {
  examPlanData.value = {
    name: '',
    courseCode: '',
    courseType: '',
    college: '',
    credit: '',
    examStartTime: '',
    examEndTime: ''
  }
}

//分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 考试安排分页相关
const currentPageExam = ref(1)
const pageSizeExam = ref(10)
const totalExam = ref(0)

//决定是否弹出课程详情页
const CourseDetailDialogVisible = ref(false);
// 决定是否弹出添加考试安排弹窗
const InsertExamDialogVisible = ref(false);
// 决定是否弹出修改考试安排弹窗
const UpdateExamDialogVisible = ref(false);
// 决定是否弹出 查看所有考试安排的表格窗口
const ExamDialogVisible = ref(false);

// 禁用今天之前的日期
const disabledDate = (time: Date) => {
  return time.getTime() < Date.now() - 86400000
}

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
  detailExamPlan();
})


/**
 * 获取数据
 */
const fetchData = async () => {
  try {
    const response = await axios.get('/cou/course/' + sessionStorage.getItem('account'), {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })
    console.log(response);


    // 获取课程列表
    const courses = response.data.data;

    // 为每个课程检查是否有考试安排
    const coursesWithExamStatus = await Promise.all(
        courses.map(async (course) => {
          try {
            // 调用API检查该课程是否有考试安排
            const examResponse = await axios.get('/exam/exam/course/' + course.id);
            console.log("examResponse")
            console.log(examResponse)
            return {
              ...course,
              hasExam: examResponse.data.code === 200 && examResponse.data.data !== null
            };
          } catch (error) {
            console.error(`检查课程 ${course.name} 考试安排失败:`, error);
            return {
              ...course,
              hasExam: false
            };
          }
        })
    );

    //courseTableData.value = response.data.data;
    courseTableData.value = coursesWithExamStatus;
    total.value = Number(response.data.message);
    //console.log("Total:", total.value, "Type:", typeof total.value);


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
 * 考试安排分页页码变化处理
 * @param page
 */
const handlePageChangeExam = (page) => {
  currentPageExam.value = page
  detailExamPlan();
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
 * 查看学生
 */
const detailStudent = (id) => {
  router.push({
    path: "/examCourseStudentManage",
    query: { id:id }
  });
}

/**
 * 打开考试安排弹窗
 */
const openExamDialog = (row) => {
  //console.log(row)//{id: 1, courseCode: '8888001', name: '数据结构', credit: 2.5, courseType: 'REQUIRED', …}
  InsertExamDialogVisible.value = true;
  initExamPlanForCreate();
  //examPlanData.value = row;

  // 只选择需要的属性
  const { id, name, courseCode, courseType, college, credit } = row;
  examPlanData.value = { id, name, courseCode, courseType, college, credit };
  //注意：这个Id是课程id
}

/**
 * 打开考试安排修改弹窗
 * @param row
 */
const openUpdateExamDialog = (row) => {
  UpdateExamDialogVisible.value = true;
  const { id, name, courseCode, courseType, college, credit } = row;
  examPlanData.value = { id, name, courseCode, courseType, college, credit };

  fetchExamPlanData(id);
}

/**
 * 获取已有的考试安排数据
 * @param courseId
 */
const fetchExamPlanData = async (courseId) => {
  try {
    const response = await axios.get('/exam/exam/course/' + courseId);
    if (response.data.code === 200 && response.data.data) {
      // 将已有的考试时间数据合并到 examPlanData
      console.log("response")
      console.log(response)
      examPlanData.value = {
        ...examPlanData.value,
        examStartTime: response.data.data.examStartTime,
        examEndTime: response.data.data.examEndTime
      };
      console.log("examPlanData")
      console.log(examPlanData)
    }
  } catch (error) {
    console.error('获取考试安排数据失败:', error);
  }
}

/**
 * 真正新增考试安排
 */
const insertExam = async () => {
  console.log(examPlanData.value)
  try {
    const response = await axios.post("/exam/exam", examPlanData.value);
    if (response.data.code === 200) {
      ElMessage.success("新增考试安排成功")

      InsertExamDialogVisible.value = false;

      // 更新对应课程的 hasExam 状态
      const courseId = examPlanData.value.id; // 确保 examPlanData 包含课程ID
      const index = courseTableData.value.findIndex(item => item.id === courseId);
      if (index !== -1) {
        courseTableData.value[index].hasExam = true;
        // 为了触发响应式更新，使用以下方式
        courseTableData.value = [...courseTableData.value];
      }

      fetchData();
      detailExamPlan();

    } else {
      ElMessage.error("新增考试安排失败")
    }

  } catch (e) {
    ElMessage.error(e);
  }
}

/**
 * 真正修改考试安排
 */
const updateExam = async () => {
  //console.log(examPlanData.value)
  try {
    const response = await axios.put("/exam/exam", examPlanData.value);
    if (response.data.code === 200) {
      ElMessage.success("修改考试安排成功")
      UpdateExamDialogVisible.value = false;
      fetchData();
      detailExamPlan();
    } else {
      ElMessage.error("修改考试安排失败")
    }
  } catch (e) {
    ElMessage.error(e);
  }
}

/**
 * 打开全部考试安排的窗口
 */
const openExamPlan = () => {
  ExamDialogVisible.value = true;
}

/**
 * 获取已有的全部的考试安排数据
 */
const detailExamPlan = async () => {
  try {
    const response = await axios.get('/exam/exam/course', {
      params: {
        page: currentPageExam.value,
        size: pageSizeExam.value
      }
    });

    if (response.data.code === 200 && response.data.data) {
      examTableDate.value = response.data.data;
      totalExam.value = Number(response.data.message);
    } else {
      ElMessage.error("数据获取失败")
    }
  } catch (error) {
    console.error('获取考试安排数据失败:', error);
  }
}

/**
 * 删除指定考试安排
 */
const deleteExam = async (id) => {
  //课程id
  try {
    const response = await axios.delete('/exam/exam/' + id + '/' + sessionStorage.getItem('account'))
    if (response.data.code === 200) {
      ElMessage.success("删除成功")
      detailExamPlan();
      fetchData();
    } else if (response.data.message === '无权限') {
      ElMessage.error("您无权删除该数据")
    }else {
      ElMessage.error("删除失败")
    }
  } catch (e) {
    ElMessage.error(e)
  }
}

/**
 * 结束考试
 * @param row
 */
const closeExam = async (row) => {
  try {
    const newDate = {
      ...row,
      state: 3
    };
    const response = await axios.put("/exam/exam", newDate);
    if (response.data.code === 200) {
      ElMessage.success("结束考试成功")
      UpdateExamDialogVisible.value = false;
      fetchData();
      detailExamPlan();
    } else {
      ElMessage.error("结束考试失败")
    }
  } catch (e) {
    ElMessage.error(e);
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