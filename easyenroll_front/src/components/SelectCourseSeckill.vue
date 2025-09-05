<template>

    <div style="border: 2px solid #bae7ff; width: 1660px; height: 80px">
      <el-text class="mx-1" type="primary" style="font-size: 30px; margin-left: 20px">选课信息预填</el-text><br>
      <el-text class="mx-1" style="margin-left: 20px;margin-top:10px">提前填写选课信息，选课开放时快速自动提交</el-text>
      <el-button type="primary" plain style="margin-top: -35px; margin-left: 1120px" size="large" @click="openPreSelectionDialogVisible()">去填写</el-button>
      <el-button type="primary" plain
                 style="margin-top: -35px; margin-left: 30px"
                 size="large"
                 @click="batchSeckill()
      ">预选抢课</el-button>
    </div>
  <el-divider />

  <el-table :data="courseTableData" stripe style="width: 100%" v-loading="vloading">
    <el-table-column prop="name" label="课程名" wixdth="180" />
    <el-table-column prop="courseCode" label="课程代码" wixdth="180" />
    <el-table-column prop="courseType" label="课程类型" wixdth="180" />
    <el-table-column prop="college" label="所属学院" wixdth="180" />
    <el-table-column prop="semester" label="开设学期" wixdth="180" />
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
    <el-table-column prop="store" label="剩余名额" wixdth="180" />
    <el-table-column prop="status" label="抢选阶段" width="180">
      <template #default="scope">
        <el-tag :type="getStatusTagType(scope.row.status)">
          {{ formatStatus(scope.row.status) }}
        </el-tag>
      </template>
    </el-table-column>
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
        <el-button
            v-if="scope.row.status === Status.ONGOING"
            link
            type="primary"
            @click="selectCourse(scope.row)"
            :loading="scope.row.loading"
            :disabled="scope.row.loading"
        >
          选课
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
  />
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
        <el-form-item label="开始时间" >
          <el-input :value="formatTime(courseData.startTime)"  />
        </el-form-item>
        <el-form-item label="结束时间" >
          <el-input :value="formatTime(courseData.endTime)"  />
        </el-form-item>
        <el-form-item label="名额" >
          <el-input v-model="courseData.store" />
        </el-form-item>
        <el-form-item label="抢选阶段" >
          <el-input v-model="courseData.status" />
        </el-form-item>
        <el-form-item label="学分" >
          <el-input v-model="courseData.credit" />
        </el-form-item>
        <el-form-item label="课程容量" >
          <el-input v-model="courseData.capacity" />
        </el-form-item>
<!--        <el-form-item label="剩余名额" >
          <el-input v-model="courseData.remain" />
        </el-form-item>-->
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

  <el-dialog v-model="PreSelectionDialogVisible" title="预选课程" width="1500" align-center>
    <el-table
        :data="PreCourseTableData"
        stripe style="width: 100%"
        v-loading="vloading"
        v-if="PreCourseTableData.length > 0"
        @selection-change="handlePreSelectionChange"
        ref="preSelectionTableRef"
    >
      <!-- 添加复选框列 -->
      <el-table-column type="selection" width="55" />
      <el-table-column prop="name" label="课程名" wixdth="180" />
      <el-table-column prop="courseCode" label="课程代码" wixdth="180" />
      <el-table-column prop="courseType" label="课程类型" wixdth="180" />
      <el-table-column prop="college" label="所属学院" wixdth="180" />
      <el-table-column prop="semester" label="开设学期" wixdth="180" />
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
      <el-table-column prop="store" label="剩余名额" wixdth="180" />
      <el-table-column prop="status" label="抢选阶段" width="180">
        <template #default="scope">
          <el-tag :type="getStatusTagType(scope.row.status)">
            {{ formatStatus(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
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
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页条 -->
    <el-pagination
        :current-page="preCurrentPage"
        :page-size="prePageSize"
        :total="preTotal"
        layout="prev, pager, next"
        @current-change="handlePrePageChange"
        style="margin-top: 20px; justify-content: center;"
        v-if="PreCourseTableData.length > 0"
    />
    <el-empty :image-size="200" v-else style="margin-top: 0px"/>
    <template #footer>
      <div>
        <el-button @click="PreSelectionDialogVisible = false">
          取消
        </el-button>
        <el-button @click="clearPreSelection">清空选择</el-button>
        <el-button type="primary" @click="submitPreSelection()" :disabled="selectedPreCourses.length === 0">
          提交
        </el-button>
        <el-text style="margin-left: 10px;">已选择 {{ selectedPreCourses.length }} 门课程</el-text>
      </div>
    </template>
  </el-dialog>
</template>


<script lang="ts" setup>
import {onMounted, onUnmounted, ref} from "vue";
import axios from '../utils/http.js';
import {ElMessage, ElPagination} from 'element-plus';

const vloading = ref(false)

const preSelectionTableRef = ref(null);

// 轮询定时器
let pollInterval = null;
// 状态枚举
const Status = {
  NOT_STARTED: 'NOT_STARTED',
  ONGOING: 'ONGOING',
  FINISHED: 'FINISHED'
};
// 格式化状态显示
const formatStatus = (status) => {
  const statusMap = {
    [Status.NOT_STARTED]: '未开始',
    [Status.ONGOING]: '抢课中',
    [Status.FINISHED]: '已结束'
  };
  return statusMap[status] || status;
};

// 获取状态标签类型
const getStatusTagType = (status) => {
  const typeMap = {
    [Status.NOT_STARTED]: 'info',
    [Status.ONGOING]: 'warning',
    [Status.FINISHED]: 'danger'
  };
  return typeMap[status] || '';
};
// 检查并更新课程状态
const checkAndUpdateStatus = async (courses) => {
  const now = new Date();
  let needUpdate = false;
  const updatedCourses = [];

  for (const course of courses) {
    const startTime = new Date(course.startTime);
    const endTime = new Date(course.endTime);

    let newStatus;
    if (now < startTime) {
      newStatus = Status.NOT_STARTED;
    } else if (now > endTime) {
      newStatus = Status.FINISHED;
    } else {
      newStatus = Status.ONGOING;
    }

    // 只有当状态变化时才标记需要更新
    if (course.status !== newStatus) {
      needUpdate = true;
      updatedCourses.push({
        ...course,
        status: newStatus
      });
    }
  }

  // 如果有课程状态变化，更新后端
  if (needUpdate && updatedCourses.length > 0) {
    try {
      await axios.post('/task/task/updateStatus',
          updatedCourses.map(course => ({
            id: course.courseId,
            status: course.status
          }))
      );
      // 更新成功后，更新前端显示
      return courses.map(course => {
        const updatedCourse = updatedCourses.find(uc => uc.id === course.id);
        return updatedCourse || course;
      });
    } catch (error) {
      console.error('更新课程状态失败:', error);
      return courses; // 返回原始数据
    }
  }

  return courses; // 没有变化，返回原始数据
};

//课程表格数据
const courseTableData = ref([{
  name: '',
  courseCode: '',
  courseType: '',
  college: '',
  semester: '',
  selectStatus: '', // 用于显示选课状态
  startTime: '',
  endTime: '',
  status: '',
  store: ''
}])

const PreCourseTableData = ref([]);

//一个课程的信息
const courseData = ref()

//一次选择的信息
const selectionDate = ref()

// 预选课程相关状态
const selectedPreCourses = ref([]); // 存储选中的预选课程

//分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const preCurrentPage = ref(1);
const prePageSize = ref(10);
const preTotal = ref(0);

//决定是否弹出课程详情页
const CourseDetailDialogVisible = ref(false);
// 决定是否弹出预选课程窗口
const PreSelectionDialogVisible = ref(false);

/**
 * 初始化加载数据
 */
onMounted(() => {
  fetchData()
  startPolling()
})

onUnmounted(() => {
  // 组件卸载时清除定时器
  if (pollInterval) {
    clearInterval(pollInterval);
    pollInterval = null;
  }
});

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
 * 获取数据
 */
const fetchData = async () => {
  try {
    //获取秒杀课程列表
    const response = await axios.get('/task/task/all', {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })
    console.log(response)
    //获取当前学生的选课记录(courseCode)
    const selectionResponse = await axios.get('/sel/selection/courseCode/' + sessionStorage.getItem('studentId'))
    //合并数据
    const selectedCourseIds = selectionResponse.data.data.map(item => item.courseCode);
    console.log("selectionResponse.data.data")
    console.log(selectionResponse.data.data)

    let courses = response.data.data.map(course => ({
      ...course,
      selectStatus: selectedCourseIds.includes(course.courseCode) ? '已选' : '未选',
      loading: false // 添加 loading 状态
    }));
    console.log("response.data.data")
    console.log(response.data.data)


    // 检查并更新状态
    courses = await checkAndUpdateStatus(courses);
    courseTableData.value = courses;
    total.value = Number(response.data.message);

    // 复制抢选阶段为"未开始"的记录
    PreCourseTableData.value = courseTableData.value.filter(item => item.status === Status.NOT_STARTED);
  } catch (error) {
    console.error('获取数据失败:', error);
  }
}

// 启动轮询
const startPolling = () => {
  // 每5秒检查一次状态
  pollInterval = setInterval(async () => {
    console.log('轮询检查课程状态...');
    const courses = await checkAndUpdateStatus(courseTableData.value);
    courseTableData.value = [...courses]; // 触发响应式更新
  }, 5000); // 30秒
};

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

  vloading.value = true;
  //禁用选课按钮
  rowData.loading = true;

  // 设置超时（比如10秒后停止轮询）
  const timeout = 1000000; // 10秒超时
  const startTime = Date.now();

  try {
    // 获取当前课程ID

    //获取动态地址
    const response = await axios.get('/seckill/seckill/' + sessionStorage.getItem('account') + '/' + rowData.courseCode)
    if (response.data.code === 200) {
      //console.log(response.data.data);
    } else {
      window.alert("无法获取秒杀地址")
      vloading.value = false;
    }
    //console.log("path" + response.data.data)

    const seckillResponse = await axios.get('/seckill/seckill/path/' + response.data.data + "/" + sessionStorage.getItem('account') + '/' + rowData.courseCode )
    if (seckillResponse.data.code === 429 && seckillResponse.data.message === '系统繁忙，请稍后再试') {
      window.alert("系统繁忙，请稍后再试")
      vloading.value = false;
      return;
    }
    if (seckillResponse.data.code === 200) {
      let checkInterval = setInterval(async () => {
        if (Date.now() - startTime > timeout) {
          clearInterval(checkInterval);
          ElMessage("秒杀超时");
          return;
        }
        try {
          const response = await axios.get('/seckill/seckill/status', {
            params: {
              studentSno: sessionStorage.getItem('account'),
              courseCode: rowData.courseCode
            }
          })
          if (response.data.code === 200 && response.data.data === 1) {
            clearInterval(checkInterval);
            ElMessage.success("选课成功");
            fetchData();
            vloading.value = false;
          } else {
            clearInterval(checkInterval);
            ElMessage.error("选课失败");
            fetchData();
            vloading.value = false;
          }
        } catch (error) {
          ElMessage("抢课失败" + error)
          fetchData();
          vloading.value = false;
        }
      }, 1000);//每秒轮询一次
      fetchData();
      vloading.value = false;
    } else if (seckillResponse.data.message === '秒杀地址错误') {
      window.alert("秒杀地址错误")
      vloading.value = false;
    } else if (seckillResponse.data.message === '重复秒杀') {
      window.alert("请勿重复秒杀")
      vloading.value = false;
    } else if (seckillResponse.data.message === '秒杀失败') {
      window.alert("抢课失败")
      vloading.value = false;
    }
  } catch (error) {
    ElMessage("抢课失败");
    vloading.value = false;
  } finally {
    rowData.loading = false;
    vloading.value = false;
  }

}

// 处理预选课程的选择变化
const handlePreSelectionChange = (selection) => {
  selectedPreCourses.value = selection;
};

// 清空选择
const clearPreSelection = () => {
  console.log("清空")
  console.log(preSelectionTableRef)
  if (preSelectionTableRef.value) {
    preSelectionTableRef.value.clearSelection();
  }
  selectedPreCourses.value = [];
};

// 提交预选课程
const submitPreSelection = async () => {
  try {
    // 提取课程代码或其他必要信息
    const courseCodes = selectedPreCourses.value.map(course => course.courseCode);

    // 这里可以添加提交到后端的逻辑
    console.log("提交的预选课程:", selectedPreCourses.value);

    const response = await axios.post('/sel/selection/pre', {
      studentId: sessionStorage.getItem('studentId'),
      courseCodes: courseCodes
    });
    if (response.data.code === 200) {
      ElMessage.success(`成功预选 ${selectedPreCourses.value.length} 门课程`);
      // 清空选择
      clearPreSelection();
      // 关闭对话框
      PreSelectionDialogVisible.value = false;
    } else {
      ElMessage.error("预选失败")
    }
  } catch (error) {
    console.error('提交预选课程失败:', error);
    ElMessage.error('提交预选课程失败');
  }
};

// 打开预选对话框时加载数据
const openPreSelectionDialogVisible = () => {
  PreSelectionDialogVisible.value = true;
  fetchData()
};

// 预选课程分页处理
const handlePrePageChange = (page) => {
  preCurrentPage.value = page;
  fetchData()
};

/**
 * 预选批量抢课
 */
const batchSeckill = async () => {
  // 设置超时（比如10秒后停止轮询）
  const timeout = 1000000; // 10秒超时
  const startTime = Date.now();

  try {

    //获取动态地址
    const response = await axios.get('/seckill/seckill/batch/' + sessionStorage.getItem('studentId'))

    if (response.data.code === 200) {
      //console.log(response.data.data);
    } else {
      window.alert("无法获取秒杀地址")
    }

    const seckillResponse = await axios.get('/seckill/seckill/batch/path/' + response.data.data + "/" + sessionStorage.getItem('account'))
    if (seckillResponse.data.code === 429 && seckillResponse.data.message === '系统繁忙，请稍后再试') {
      window.alert("系统繁忙，请稍后再试");
      return;
    }

    if (seckillResponse.data.code === 200) {
      let checkInterval = setInterval(async () => {
        if (Date.now() - startTime > timeout) {
          clearInterval(checkInterval);
          ElMessage.error("秒杀超时");
          return;
        }
        try {
          const response = await axios.get('/seckill/seckill/status/batch/' + sessionStorage.getItem('studentId'))
          if (response.data.code === 200 && response.data.data === '1') {
            clearInterval(checkInterval);
            ElMessage.success("选课成功");
            fetchData();
          } else {
            clearInterval(checkInterval);
            ElMessage.error("选课失败：" + response.data.data);
            fetchData();
          }
        } catch (error) {
          ElMessage("抢课失败" + error)
          fetchData();
        }
      }, 1000);//每秒轮询一次
      fetchData();
    } else {
      //ElMessage.error(seckillResponse.data.message)
      ElMessage.error("预选失败：" + seckillResponse.data.message)
    }
    /**
     *  else if (seckillResponse.data.message === '秒杀地址错误') {
     *       window.alert("秒杀地址错误")
     *     } else if (seckillResponse.data.message === '重复秒杀') {
     *       window.alert("请勿重复秒杀")
     *     }
     */
  } catch (e) {
    ElMessage.error("抢选失败：" + e)
  }
}


</script>


<style scoped>
body {
  margin: 0;
}
.example-showcase .el-loading-mask {
  z-index: 9;
}
</style>