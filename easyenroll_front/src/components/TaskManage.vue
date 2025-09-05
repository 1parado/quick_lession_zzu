<template>
  <!-- 新增按钮 -->
  <el-button type="primary" plain @click="openInsertDialog()" style="margin-bottom: 30px">新增选课任务</el-button>

  <el-input v-model="searchText" style="width: 240px;margin-left: 20px;margin-bottom: 30px" placeholder="请输入学生姓名" />
  <el-button style="margin-left: 20px;margin-bottom: 30px" type="primary" @click="searchTask()" round>搜索</el-button>

  <el-table :data="taskTableData" stripe style="width: 100%" v-if="taskTableData.length > 0">
    <el-table-column prop="name" label="课程名" wixdth="180" />
    <el-table-column prop="startTime" label="开始时间" wixdth="180">
      <template #default="scope">
        <!-- 使用之前定义的 formatTime 函数格式化时间 -->
        {{ formatTime(scope.row.startTime) }}
      </template>
    </el-table-column>
    <el-table-column prop="endTime" label="结束时间" wixdth="180" >
      <template #default="scope">
        <!-- 使用之前定义的 formatTime 函数格式化时间 -->
        {{ formatTime(scope.row.endTime) }}
      </template>
    </el-table-column>
    <el-table-column prop="store" label="名额" wixdth="180" />
    <el-table-column prop="status" label="秒杀状态" wixdth="180" />
    <el-table-column fixed="right" label="操作" min-width="180" align="center">
      <template #default="scope">
        <el-button link type="primary" @click="detailTask(scope.row)">详情</el-button>
        <el-button link type="primary" @click="updateTask(scope.row)">修改</el-button>
        <el-button link type="primary" @click="deleteTask(scope.row.id)" >删除</el-button>
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
      v-if="taskTableData.length > 0"
  />
  <el-empty :image-size="200" v-else style="margin-top: 175px"/>

  <!-- 详情页弹窗 -->
  <el-dialog v-model="TaskDetailDialogVisible" title="选课任务详情信息" width="1000" align-center>
    <div>
      <el-form :model="taskData" label-position="left" label-width="110px" :disabled="true">
        <el-form-item label="ID" >
          <el-input v-model="taskData.id" />
        </el-form-item>
        <el-form-item label="开始时间" >
          <el-input :value="formatTime(taskData.startTime)"  />
        </el-form-item>
        <el-form-item label="结束时间" >
          <el-input :value="formatTime(taskData.endTime)"  />
        </el-form-item>
        <el-form-item label="状态" >
          <el-input v-model="taskData.status" />
        </el-form-item>
        <el-form-item label="名额" >
          <el-input v-model="taskData.store" />
        </el-form-item>
        <el-form-item label="课程容量" >
          <el-input v-model="taskData.capacity" />
        </el-form-item>
        <el-form-item label="剩余名额" >
          <el-input v-model="taskData.remain" />
        </el-form-item>
        <el-form-item label="课程名" >
          <el-input v-model="taskData.name" />
        </el-form-item>
        <el-form-item label="课程代码" >
          <el-input v-model="taskData.courseCode" />
        </el-form-item>
        <el-form-item label="所属学院" >
          <el-input v-model="taskData.college" />
        </el-form-item>
        <el-form-item label="开设学期" >
          <el-input v-model="taskData.semester" />
        </el-form-item>
        <el-form-item label="任课教师" >
          <el-input v-model="taskData.teacherName" />
        </el-form-item>
        <el-form-item label="教师工号" >
          <el-input v-model="taskData.tno" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button type="primary" @click="TaskDetailDialogVisible = false">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 修改页弹窗 -->
  <el-dialog v-model="TaskDetailUpdateVisible" title="修改选任务信息" width="1000" align-center>
    <div>
      <el-form
          :model="taskData"
          :rules="rules"
          ref="updateFormRef"
          label-position="left"
          label-width="110px"
          @close="handleDialogClose('update')"
      >
        <el-form-item label="ID" prop="id">
          <el-input v-model="taskData.id" disabled/>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
              style="width: 1000px"
              v-model.number="taskData.startTime"
              type="datetime"
              placeholder="选择开始时间"
              :disabled-date="disabledDate"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
              style="width: 1000px"
              v-model="taskData.endTime"
              type="datetime"
              placeholder="选择结束时间"
              :disabled-date="disabledDate"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-input v-model="taskData.status" disabled/>
        </el-form-item>
        <el-form-item label="名额" prop="store">
          <el-input
              v-model.number="taskData.store"
              type="number"
              :min="1"
              :max="taskData.remain"
              @change="validateStore"
          />
          <div v-if="taskData.remain" style="color: #888; font-size: 12px;">
            剩余名额: {{ taskData.remain }}
          </div>
        </el-form-item>
        <el-form-item label="课程容量" prop="capacity">
          <el-input v-model="taskData.capacity" disabled/>
        </el-form-item>
        <el-form-item label="剩余名额" prop="remain">
          <el-input v-model="taskData.remain" disabled/>
        </el-form-item>
        <el-form-item label="课程名" prop="name">
          <el-input v-model="taskData.name" disabled/>
        </el-form-item>
        <el-form-item label="课程代码" prop="courseCode">
          <el-input v-model="taskData.courseCode" disabled/>
        </el-form-item>
        <el-form-item label="所属学院" prop="college">
          <el-input v-model="taskData.college" disabled/>
        </el-form-item>
        <el-form-item label="开设学期" prop="semester">
          <el-input v-model="taskData.semester" disabled/>
        </el-form-item>
        <el-form-item label="任课教师" prop="teacherName">
          <el-input v-model="taskData.teacherName" disabled/>
        </el-form-item>
        <el-form-item label="教师工号" prop="tno">
          <el-input v-model="taskData.tno" disabled/>
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="TaskDetailUpdateVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="updateTaskRel()">
          修改
        </el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 新增页弹窗 -->
  <el-dialog v-model="TaskInsertDialogVisible" title="新增选课任务" width="1000" align-center>
    <div>
      <el-form :model="taskData" :rules="rules" ref="insertFormRef" label-position="left" label-width="110px"
               @close="handleDialogClose('update')">
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
              style="width: 1000px"
              v-model="taskData.startTime"
              type="datetime"
              placeholder="选择开始时间"
              :disabled-date="disabledDate"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
              style="width: 1000px"
              v-model="taskData.endTime"
              type="datetime"
              placeholder="选择结束时间"
              :disabled-date="disabledDate"
          />
        </el-form-item>
        <el-form-item label="名额" prop="store">
          <el-input
              v-model="taskData.store"
              type="number"
              :min="1"
              :max="taskData.remain"
              @change="validateStore"
          />
          <div v-if="taskData.remain" style="color: #888; font-size: 12px;">
            剩余名额: {{ taskData.remain }}
          </div>
        </el-form-item>

        <!-- 课程代码下拉选择 -->
        <el-form-item label="课程代码" prop="courseCode">
          <el-select
              v-model="taskData.courseCode"
              placeholder="请选择课程代码"
              filterable
              @change="handleCourseChange"
              style="width: 100%"
          >
            <el-option
                v-for="course in courseList"
                :key="course.courseCode"
                :label="`${course.courseCode}(${course.name})`"
                :value="course.courseCode"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="课程容量" prop="capacity">
          <el-input v-model="taskData.capacity" disabled/>
        </el-form-item>
        <el-form-item label="剩余名额" prop="remain">
          <el-input v-model="taskData.remain" disabled/>
        </el-form-item>
        <el-form-item label="课程名" prop="name">
          <el-input v-model="taskData.name" disabled/>
        </el-form-item>
        <el-form-item label="所属学院" prop="college">
          <el-input v-model="taskData.college" disabled/>
        </el-form-item>
        <el-form-item label="开设学期" prop="semester">
          <el-input v-model="taskData.semester" disabled/>
        </el-form-item>
        <el-form-item label="任课教师姓名" prop="teacherName">
          <el-input v-model="taskData.teacherName" disabled/>
        </el-form-item>
        <el-form-item label="任课教师工号" prop="tno">
          <el-input v-model="taskData.tno" disabled/>
        </el-form-item>


      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="TaskInsertDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="insertTask()">
          新增
        </el-button>
      </div>
    </template>
  </el-dialog>

</template>


<script lang="ts" setup>

import {onMounted, ref} from "vue";
import axios from '../utils/http.js';
import {ElMessage, ElPagination} from 'element-plus';

//搜索框内容
const searchText = ref('')

//任务表格数据
const taskTableData = ref([{
  name: '',
  store: '',
  startTime: '',
  endTime: '',
  status: ''
}])

//一个课程的信息
const taskData = ref()

// 新增一个专门用于新增的初始化方法
const initTaskForCreate = () => {
  taskData.value = {
    id: '',
    startTime: '',
    endTime: '',
    status: 'NOT_STARTED',
    store: '',
    capacity: '',
    remain: 0 ,
    name: '',
    courseCode: '',
    college: '',
    semester: '',
    teacherName: '',
    tno: ''
  }
}

// 修改新增按钮的点击事件
const openInsertDialog = () => {
  TaskInsertDialogVisible.value = true
  initTaskForCreate() // 仅在这里初始化
}

//分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

/**
 * 处理窗口关闭
 * @param type
 */
const handleDialogClose = (type: 'insert' | 'update') => {
  if (type === 'update' && updateFormRef.value) {
    // 重置修改表单，丢弃未保存的更改
    updateFormRef.value.resetFields();
  }
  fetchData(); // 刷新数据
  if (type === 'insert') {
    TaskInsertDialogVisible.value = false;
  } else {
    TaskDetailUpdateVisible.value = false;
  }
}

//决定是否弹出任务详情页
const TaskDetailDialogVisible = ref(false);
//决定是否弹出任务修改页
const TaskDetailUpdateVisible = ref(false);
//决定是否弹出任务增加页
const TaskInsertDialogVisible = ref(false);

// 定义表单引用
const updateFormRef = ref(null)
const insertFormRef = ref(null)

// 验证规则
// 验证规则
const rules = {
  id: [],
  startTime: [],
  endTime: [],
  status: [],
  store: [
    { required: true, message: '请输入名额', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error('请输入名额'))
        } else if (isNaN(value)) {
          callback(new Error('必须输入数字'))
        } else if (Number(value) <= 0) {
          callback(new Error('名额必须大于0'))
        } else if (taskData.value.remain && Number(value) > Number(taskData.value.remain)) {
          callback(new Error('名额不能超过剩余名额'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  capacity: [],
  remain: [],
  name: [],
  courseCode: [],
  college: [],
  semester: [],
  teacherName: [],
  tno: []
};

const validateStore = () => {
  if (TaskInsertDialogVisible.value && insertFormRef.value) {
    insertFormRef.value.validateField('store')
  } else if (TaskDetailUpdateVisible.value && updateFormRef.value) {
    updateFormRef.value.validateField('store')
  }
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

// 禁用今天之前的日期
const disabledDate = (time: Date) => {
  return time.getTime() < Date.now() - 86400000
}

// 在 ref 定义部分添加以下内容
const courseList = ref([]) // 存储所有课程数据

/**
 * 初始化加载数据
 */
onMounted(() => {
  fetchData()
  fetchCourses()
})

/**
 * 获取课程列表
 */
const fetchCourses = async () => {
  try {
    const response = await axios.get('/cou/course/list')
    console.log("课程列表")
    console.log(response)
    courseList.value = response.data.data
  } catch (error) {
    console.error('获取课程列表失败:', error)
  }
}


/**
 * 课程选择变化处理方法
 * @param courseCode
 */
const handleCourseChange = (courseCode) => {
  const selectedCourse = courseList.value.find(c => c.courseCode === courseCode)
  if (selectedCourse) {
    taskData.value = {
      ...taskData.value,
      name: selectedCourse.name,
      college: selectedCourse.college,
      capacity: selectedCourse.capacity,
      remain: selectedCourse.remain || selectedCourse.capacity, // 确保有值
      semester: selectedCourse.semester,
      teacherName: selectedCourse.teacherName,
      tno: selectedCourse.tno
    }
  }
}


/**
 * 获取数据
 */
const fetchData = async () => {
  try {
    const response = await axios.get('/task/task', {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })
    console.log(response);
    taskTableData.value = response.data.data;
    total.value = Number(response.data.message);
    console.log("Total:", total.value, "Type:", typeof total.value);
  } catch (error) {
    console.error('获取数据失败:', error);
  }
}

/**
 * 搜索选课任务
 */
const searchTask = async () => {
  if (searchText.value === '' || searchText.value.length <= 0) {
    fetchData();
    return;
  }
  try {
    const response = await axios.get('/task/task/search/' + searchText.value, {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })
    console.log(response);
    taskTableData.value = response.data.data;
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
 * 删除任务
 * @param id
 */
const deleteTask = async (id) => {
  try {
    //先询问是否删除
    if(window.confirm('是否删除')){
      const response = await axios.delete('/task/task/' + id)
      console.log(response);
      if (response.data.code === 200){
        ElMessage.success("删除成功")
        fetchData();
      } else {
        ElMessage.error("删除失败")
      }
    }
  } catch (error) {
    console.log(error)
  }
}

/**
 * 修改，打开弹窗
 * @param rowData
 */
const updateTask = (rowData) => {
  TaskDetailUpdateVisible.value = true;
  // 使用深拷贝，避免直接修改原始数据
  taskData.value = JSON.parse(JSON.stringify(rowData));
}

/**
 * 修改，真正修改
 */
const updateTaskRel = async () => {
  //将taskData打包送给后端
  // 验证表单
  await updateFormRef.value.validate()

  try {
    console.log(taskData.value)
    const response = await axios.put('/task/task', taskData.value)
    console.log(response);
    if (response.data.code === 200){
      ElMessage.success("更新成功")
      fetchData();
      TaskDetailUpdateVisible.value = false;
    } else if (response.data.message === '超额') {
      ElMessage.error("余额不足，请重新设置名额")
      fetchData();
    } else {
      ElMessage.error("更新失败")
      fetchData();
    }
  } catch (error) {
    console.log(error)
  }
}

/**
 * 详情,打开弹窗
 * @param rowData
 */
const detailTask = (rowData) => {
  //打开一个弹窗，用taskTableData传数据
  TaskDetailDialogVisible.value = true;
  console.log(rowData)
  taskData.value = rowData
}

/**
 * 新增
 */
const insertTask = async () => {
  // 验证表单
  try {
    await insertFormRef.value.validate()
  } catch (error) {
    window.alert("格式有误")
  }

  console.log(taskData.value)
  try {
    const response = await axios.post('/task/task', taskData.value,  {
      headers: {
        'Content-Type': 'application/json'
      }
    })
    //console.log(response);
    if (response.data.code === 200){
      ElMessage.success("新增成功")
      TaskInsertDialogVisible.value = false;
      fetchData();
    } else if (response.data.message === '任务重复') {
      ElMessage.error("任务重复，该课程已设置选课任务")
      fetchData();
    } else if (response.data.message === '超额') {
      ElMessage.error("余额不足，请重新设置名额")
      fetchData();
    } else {
      ElMessage.error("新增失败")
      fetchData();
    }
  } catch (error) {
    console.log(error)
  }
}

</script>


<style scoped>

</style>