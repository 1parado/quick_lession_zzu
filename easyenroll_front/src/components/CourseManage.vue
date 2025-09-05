<template>
  <!-- 新增按钮 -->
  <el-button type="primary" plain @click="openInsertDialog()" style="margin-bottom: 30px">新增课程</el-button>

  <el-input v-model="searchText" style="width: 240px;margin-left: 20px;margin-bottom: 30px" placeholder="请输入课程名" />
  <el-button style="margin-left: 20px;margin-bottom: 30px" type="primary" @click="searchCourse()" round>搜索</el-button>

  <el-button type="primary" plain @click="exportCourseList()" style="margin-left: 1120px;margin-bottom: 30px" >导出课程列表</el-button>


  <el-table :data="courseTableData" stripe style="width: 100%" v-if="courseTableData.length > 0">
    <el-table-column prop="name" label="课程名" wixdth="180" />
    <el-table-column prop="courseCode" label="课程代码" wixdth="180" />
    <el-table-column prop="courseType" label="课程类型" wixdth="180" />
    <el-table-column prop="college" label="所属学院" wixdth="180" />
    <el-table-column prop="semester" label="开设学期" wixdth="180" />
    <el-table-column fixed="right" label="操作" min-width="180" align="center">
      <template #default="scope">
        <el-button link type="primary" @click="detailCourse(scope.row)">详情</el-button>
        <el-button link type="primary" @click="updateCourse(scope.row)">修改</el-button>
        <el-button link type="primary" @click="deleteCourse(scope.row.id)" >删除</el-button>
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
  <!-- 修改页弹窗 -->
  <el-dialog v-model="CourseDetailUpdateVisible" title="修改课程信息" width="1000" align-center>
    <div>
      <el-form :model="courseData" :rules="rules" ref="updateFormRef" label-position="left" label-width="110px" >
        <el-form-item label="课程名" prop="name" >
          <el-input v-model="courseData.name" />
        </el-form-item>
        <el-form-item label="课程代码" prop="courseCode">
          <el-input v-model="courseData.courseCode" />
        </el-form-item>
        <el-form-item label="课程类型" prop="courseType">
          <el-select v-model="courseData.courseType" placeholder="请选择课程类型" style="width: 100%">
            <el-option label="必修课" value="REQUIRED" />
            <el-option label="选修课" value="ELECTIVE" />
            <el-option label="通识课" value="GENERAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属学院" prop="college">
          <el-input v-model="courseData.college" />
        </el-form-item>
        <el-form-item label="开设学期" prop="semester">
          <el-input v-model="courseData.semester" />
        </el-form-item>
        <el-form-item label="学分" prop="credit">
          <el-input v-model="courseData.credit" />
        </el-form-item>
        <el-form-item label="课程容量" prop="capacity">
          <el-input v-model="courseData.capacity" />
        </el-form-item>
        <el-form-item label="剩余名额" prop="remain">
          <el-input v-model="courseData.remain" />
        </el-form-item>
        <el-form-item label="是否需要抢选" prop="isSeckill">
          <el-select
              v-model="courseData.isSeckill"
              placeholder="请选择是否需要抢选"
              @change="handleSeckillChange"
          >
            <el-option label="不需要" value="0" />
            <el-option label="需要" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="任课教师" prop="tno">
          <el-select
              v-model="courseData.tno"
              placeholder="选择任课教师"
              filterable
              @change="handleTeacherChange"
              style="width: 100%"
          >
            <el-option
                v-for="teacher in teacherList"
                :key="teacher.tno"
                :label="`${teacher.name} (${teacher.tno})`"
                :value="teacher.tno"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="上课时间" prop="classTime">
          <el-input v-model="courseData.classTime" />
        </el-form-item>
        <el-form-item label="上课地点" prop="classLocation">
          <el-input v-model="courseData.classLocation" />
        </el-form-item>
        <el-form-item label="周次范围" prop="weekRange" >
          <el-input v-model="courseData.weekRange" />
        </el-form-item>
        <el-form-item label="前置课程" prop="preCourseCode">
          <el-input v-model="courseData.preCourseCode" />
        </el-form-item>
        <el-form-item label="课程描述" prop="description">
          <el-input v-model="courseData.description" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="CourseDetailUpdateVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="updateCourseRel()">
          修改
        </el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 新增页弹窗 -->
  <el-dialog v-model="CourseInsertDialogVisible" title="新增课程" width="1000" align-center>
    <div>
      <el-form :model="courseData" :rules="rules" ref="insertFormRef" label-position="left" label-width="110px">
        <el-form-item label="课程名" prop="name" >
          <el-input v-model="courseData.name" />
        </el-form-item>
        <el-form-item label="课程代码" prop="courseCode">
          <el-input v-model="courseData.courseCode" />
        </el-form-item>
        <el-form-item label="课程类型" prop="courseType">
          <el-select v-model="courseData.courseType" placeholder="请选择课程类型" style="width: 100%">
            <el-option label="必修课" value="REQUIRED" />
            <el-option label="选修课" value="ELECTIVE" />
            <el-option label="通识课" value="GENERAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属学院" prop="college">
          <el-input v-model="courseData.college" />
        </el-form-item>
        <el-form-item label="开设学期" prop="semester">
          <el-input v-model="courseData.semester" placeholder='格式："xxxx年上/下学期"' />
        </el-form-item>
        <el-form-item label="学分" prop="credit">
          <el-input v-model="courseData.credit" />
        </el-form-item>
        <el-form-item label="课程容量" prop="capacity">
          <el-input v-model="courseData.capacity" />
        </el-form-item>
        <el-form-item label="剩余名额" prop="remain">
          <el-input v-model="courseData.remain" />
        </el-form-item>
        <el-form-item label="是否需要抢选" prop="isSeckill">
          <el-select
              v-model="courseData.isSeckill"
              placeholder="请选择是否需要抢选"
              @change="handleSeckillChange"
          >
            <el-option label="不需要" value="0" />
            <el-option label="需要" value="1" />
          </el-select>
        </el-form-item>
<!--        <el-form-item label="任课教师" prop="tno">
          <el-input v-model="courseData.tno" placeholder="填写任课教师工号" />
        </el-form-item>-->
        <!-- 任课教师下拉选择 -->
        <el-form-item label="任课教师" prop="tno">
          <el-select
              v-model="courseData.tno"
              placeholder="填写任课教师工号"
              filterable
              @change="handleTeacherChange"
              style="width: 100%"
          >
            <el-option
                v-for="teacher in teacherList"
                :key="teacher.tno"
                :label="`${teacher.name} (${teacher.tno})`"
                :value="teacher.tno"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="上课时间" prop="classTime">
          <el-input v-model="courseData.classTime" placeholder='格式："每周X，第Y、Z节（如每周一，第1、2节）"' />
        </el-form-item>
        <el-form-item label="上课地点" prop="classLocation">
          <el-input v-model="courseData.classLocation" />
        </el-form-item>
        <el-form-item label="周次范围" prop="weekRange" >
          <el-input v-model="courseData.weekRange" />
        </el-form-item>
        <el-form-item label="前置课程" prop="preCourseCode">
          <el-input v-model="courseData.preCourseCode" placeholder="填写前置课程代码" />
        </el-form-item>
        <el-form-item label="课程描述" prop="description">
          <el-input v-model="courseData.description" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="CourseInsertDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="insertCourse()">
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

//搜索框文本
const searchText = ref('');

//课程表格数据
const courseTableData = ref([{
  name: '',
  courseCode: '',
  courseType: '',
  college: '',
  semester: ''
}])

//一个课程的信息
const courseData = ref()

// 新增一个专门用于新增的初始化方法
const initCourseForCreate = () => {
  courseData.value = {
    name: '',
    courseCode: '',
    courseType: '',
    college: '',
    semester: '',
    credit: '',
    capacity: '',
    remain: '',
    isSeckill: '',
    tno: '',
    classTime: '',
    classLocation: '',
    weekRange: '',
    preCourseCode: '',
    description: ''
  }
}

// 修改新增按钮的点击事件
const openInsertDialog = () => {
  CourseInsertDialogVisible.value = true
  initCourseForCreate() // 仅在这里初始化
}

//分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

//决定是否弹出课程详情页
const CourseDetailDialogVisible = ref(false);
//决定是否弹出课程修改页
const CourseDetailUpdateVisible = ref(false);
//决定是否弹出课程增加页
const CourseInsertDialogVisible = ref(false);

// 定义表单引用
const updateFormRef = ref(null)
const insertFormRef = ref(null)

// 验证规则
// 验证规则
const rules = {
  name: [
    { required: true, message: '课程名不能为空', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在2到50个字符', trigger: 'blur' }
  ],
  courseCode: [
    { required: true, message: '课程代码不能为空', trigger: 'blur' },
    {
      pattern: /^\d{7}$/,
      message: '课程代码必须是7位数字（如8888001）',
      trigger: 'blur'
    }
  ],
  tno: [
    { required: true, message: '任课教师不能为空', trigger: 'blur' },
    {
      pattern: /^\d{7}$/,
      message: '课程代码必须是7位数字（如6666001）',
      trigger: 'blur'
    }
  ],
  courseType: [
    { required: true, message: '课程类型不能为空', trigger: 'blur' }
  ],
  college: [
    { required: true, message: '所属学院不能为空', trigger: 'blur' },
    {
      pattern: /^[\u4e00-\u9fa5]{1,10}学院$/,
      message: '学院名称格式应为“xx学院”，如“软件学院”',
      trigger: 'blur'
    }
  ],
  semester: [
    { required: true, message: '请选择开设时间', trigger: 'blur' },
    {
      pattern: /^\d{4}年(上|下)学期$/,
      message: '开设学期格式应为：xxxx年上学期 或 xxxx年下学期，如：2024年上学期',
      trigger: 'blur'
    }
  ],
  credit: [
    { required: true, message: '学分不能为空', trigger: 'blur' },
    {
      type: 'number',
      message: '学分必须是数字',
      trigger: 'blur',
      transform: (value) => Number(value) || 0
    }
  ],
  capacity: [
    {
      validator: (rule, value, callback) => {
        if (courseData.value.isSeckill === '1' && !value) {
          callback(new Error('秒杀课程必须填写课程容量'));
        } else if (courseData.value.isSeckill === '0' && value) {
          callback(new Error('非秒杀课程不能填写课程容量'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    },
    {
      type: 'number',
      message: '课程容量必须是数字',
      trigger: 'blur',
      transform: (value) => value ? Number(value) : null
    }
  ],
  remain: [
    {
      validator: (rule, value, callback) => {
        if (courseData.value.isSeckill === '1' && !value) {
          callback(new Error('秒杀课程必须填写剩余名额'));
        } else if (courseData.value.isSeckill === '0' && value) {
          callback(new Error('非秒杀课程不能填写剩余名额'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    },
    {
      type: 'number',
      message: '剩余名额必须是数字',
      trigger: 'blur',
      transform: (value) => value ? Number(value) : null
    }
  ],
  isSeckill: [
    { required: true, message: '请选择是否需要抢选', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value === '1') {
          // 如果isSeckill为1，检查capacity和remain是否填写
          if (!courseData.value.capacity || !courseData.value.remain) {
            callback(new Error('秒杀课程必须填写课程容量和剩余名额'));
          } else {
            callback();
          }
        } else if (value === '0') {
          // 如果isSeckill为0，检查capacity和remain是否为空
          if (courseData.value.capacity || courseData.value.remain) {
            callback(new Error('非秒杀课程不能填写课程容量和剩余名额'));
          } else {
            callback();
          }
        } else {
          callback(new Error('请填写0或1（0表示不需要，1表示需要）'));
        }
      },
      trigger: 'blur'
    }
  ],
  teacherName: [
    { required: true, message: '任课教师不能为空', trigger: 'blur' }
  ],
  classTime: [
    { required: true, message: '上课时间不能为空', trigger: 'blur' },
    {
      pattern: /^每周[一二三四五六日]，第[\d、]+节$/,
      message: '格式应为"每周X，第Y、Z节"（如每周一，第1、2节）',
      trigger: 'blur'
    }
  ],
  classLocation: [
    { required: true, message: '上课地点不能为空', trigger: 'blur' }
  ],
  weekRange: [
    { required: true, message: '周次范围不能为空', trigger: 'blur' }
  ],
  preCourseCode: [
    // 非必填
  ],
  description: [
    // 非必填
    { max: 500, message: '描述最多500个字符', trigger: 'blur' }
  ]
};

// 在 ref 定义部分添加以下内容
const teacherList = ref([]) // 存储所有课程数据

/**
 * 下拉列表选择任课教师
 * @param courseCode
 */
const handleTeacherChange = (tno) => {
  const selectedTeacher = teacherList.value.find(teacher => teacher.tno === tno)
  if (selectedTeacher) {
    courseData.value = {
      ...courseData.value,
      tno: selectedTeacher.tno,
      teacherName: selectedTeacher.name // 同时更新教师姓名
    }
  }
}

/**
 * 下拉列表选择是否抢课
 * @param value
 */
const handleSeckillChange = (value) => {
  if (value === '0') {
    // 如果选择不需要秒杀，清空容量和剩余名额
    courseData.value.capacity = '';
    courseData.value.remain = '';
  }
}

/**
 * 初始化加载数据
 */
onMounted(() => {
  fetchTeacher()
  fetchData()
})

/**
 * 获取课程列表
 */
const fetchTeacher = async () => {
  try {
    const response = await axios.get('/tea/teacher/list')
    console.log("教师列表列表")
    console.log(response)
    teacherList.value = response.data.data
  } catch (error) {
    console.error('获取教师列表失败:', error)
  }
}

/**
 * 获取数据
 */
const fetchData = async () => {
  try {
    const response = await axios.get('/cou/course', {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })
    console.log(response);
    courseTableData.value = response.data.data;
    total.value = Number(response.data.message);
    console.log("Total:", total.value, "Type:", typeof total.value);
  } catch (error) {
    console.error('获取数据失败:', error);
  }
}

/**
 *  搜索课程
 */
const searchCourse = async () => {
  if (searchText.value === '' || searchText.value.length <= 0) {
    fetchData();
    return;
  }
  try {
    const response = await axios.get('/cou/course/search/all/' + searchText.value, {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })
    console.log(response);
    courseTableData.value = response.data.data;
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
 * 删除课程
 * @param id
 */
const deleteCourse = async (id) => {
  try {
    //先询问是否删除
    if(window.confirm('是否删除')){
      const response = await axios.delete('/cou/course/' + id)
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
const updateCourse = (rowData) => {
  CourseDetailUpdateVisible.value = true;
  courseData.value = rowData
}

/**
 * 修改，真正修改
 */
const updateCourseRel = async () => {
  //将courseData打包送给后端
  // 验证表单
  await updateFormRef.value.validate()

  try {
    const response = await axios.put('/cou/course', courseData.value)
    console.log(response);
    if (response.data.code === 200){
      ElMessage.success("更行成功")
      fetchData();
      CourseDetailUpdateVisible.value = false;
    } else if (response.data.message === '课程代码重复') {
      ElMessage.error("课程代码重复")
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
const detailCourse = (rowData) => {
  //打开一个弹窗，用courseTableData传数据
  CourseDetailDialogVisible.value = true;
  console.log(rowData)
  courseData.value = rowData
}

/**
 * 新增
 */
const insertCourse = async () => {
  // 验证表单
  try {
    await insertFormRef.value.validate()
  } catch (error) {
    window.alert("格式有误")
  }

  console.log(courseData.value)
  try {
    const response = await axios.post('/cou/course', courseData.value,  {
      headers: {
        'Content-Type': 'application/json'
      }
    })
    //console.log(response);
    if (response.data.code === 200){
      ElMessage.success("新增成功")
      fetchData();
      CourseInsertDialogVisible.value = false;
    } else if (response.data.message === '课程代码重复') {
      ElMessage.error("课程代码重复")
      fetchData();
    } else {
      ElMessage.error("新增失败")
      fetchData();
    }
  } catch (error) {
    console.log(error)
  }
}

/**
 * 导出课程
 */

const exportCourseList = async () => {
  try {
    // 关键：设置responseType为blob，接收二进制流
    const response = await axios.get('/exc/export/course', {
      responseType: 'blob' // 必须设置，否则会解析为JSON导致乱码
    });

    // 从响应头获取文件名（如果后端设置了的话）
    let fileName = '课程列表.xlsx'; // 默认文件名


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