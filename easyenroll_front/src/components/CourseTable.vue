<template>

  <el-button type="primary" plain @click="exportCourseTable()" style="margin-bottom: 30px;">导出课程表</el-button>

  <!-- 课程表控制栏 -->
  <div class="timetable-controls">
    <el-select v-model="currentSemester" placeholder="选择学期" @change="filterCourses">
      <el-option
          v-for="semester in uniqueSemesters"
          :key="semester"
          :label="semester"
          :value="semester"
      />
    </el-select>

    <el-select v-model="currentWeek" placeholder="选择周次" @change="filterCourses">
      <el-option
          v-for="week in 20"
          :key="week"
          :label="'第' + week + '周'"
          :value="week"
      />
    </el-select>
  </div>

  <!-- 课程表主体 -->
  <div class="timetable-container">
    <table class="timetable">
      <thead>
      <tr>
        <th>时间</th>
        <th v-for="day in weekDays" :key="day">{{ day }}</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="timeSlot in timeSlots" :key="timeSlot.time">
        <td class="time-slot">{{ timeSlot.time }}</td>
        <td
            v-for="day in weekDays"
            :key="day"
            @click="handleCellClick(getCourseAt(day, timeSlot.time))"
            :class="{
              'has-course': getCourseAt(day, timeSlot.time),
              'current-week': isCurrentWeekCourse(getCourseAt(day, timeSlot.time))
            }"
        >
          <div v-if="getCourseAt(day, timeSlot.time)" class="course-cell">
            <div class="course-name">{{ getCourseAt(day, timeSlot.time).name }}</div>
            <div class="course-location">{{ getCourseAt(day, timeSlot.time).classLocation }}</div>
            <div class="course-teacher">{{ getCourseAt(day, timeSlot.time).teacherName }}</div>
            <div class="course-weeks" v-if="getCourseAt(day, timeSlot.time).weekRange">
              周次: {{ formatWeekRange(getCourseAt(day, timeSlot.time).weekRange) }}
            </div>
          </div>
        </td>
      </tr>
      </tbody>
    </table>
  </div>



  <el-table :data="courseTableData" stripe style="width: 100%">
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
import {computed, onMounted, ref} from "vue";
import axios from '../utils/http.js';
import {ElMessage, ElPagination} from 'element-plus';



// 星期数组
const weekDays = ['周一', '周二', '周三', '周四', '周五', '周六', '周日'];

// 时间段数组
const timeSlots = [
  { time: '8:00-9:40', startClass: 1, endClass: 2 }, // 第1-2节
  { time: '10:00-11:40', startClass: 3, endClass: 4 }, // 第3-4节
  { time: '13:30-15:10', startClass: 5, endClass: 6 }, // 第5-6节
  { time: '15:30-17:10', startClass: 7, endClass: 8 }, // 第7-8节
  { time: '18:30-20:10', startClass: 9, endClass: 10 } // 第9-10节
];
// 过滤后的课程数据（用于课程表显示）
const filteredCourses = ref([]);
// 当前学期
const currentSemester = ref('');
// 当前周次
const currentWeek = ref(1);
/**
 * 计算所有唯一的学期
 */
const uniqueSemesters = computed(() => {
  const semesters = new Set();
  fullCourseData.value.forEach(course => {
    if (course.semester) {
      semesters.add(course.semester);
    }
  });
  return Array.from(semesters);
});
/**
 * 过滤课程数据
 */
const filterCourses = () => {
  console.log('执行过滤，当前学期:', currentSemester.value, '当前周:', currentWeek.value);

  filteredCourses.value = fullCourseData.value.filter(course => {
    // 调试：打印当前课程的学期和周次范围
    console.log(`检查课程 ${course.name}:`, {
      semester: course.semester,
      weekRange: course.weekRange
    });

    // 1. 学期筛选（如果currentSemester有值才筛选）
    if (currentSemester.value && course.semester !== currentSemester.value) {
      console.log(`课程 ${course.name} 学期不匹配`);
      return false;
    }

    // 2. 周次筛选（如果weekRange存在才筛选）
    if (course.weekRange) {
      try {
        const [startWeek, endWeek] = course.weekRange.split('-').map(Number);
        if (currentWeek.value < startWeek || currentWeek.value > endWeek) {
          console.log(`课程 ${course.name} 周次不在范围内`);
          return false;
        }
      } catch (e) {
        console.error(`解析weekRange失败: ${course.weekRange}`, e);
        return false;
      }
    }

    console.log(`课程 ${course.name} 通过筛选`);
    return true;
  });

  console.log('过滤后的课程列表:', JSON.parse(JSON.stringify(filteredCourses.value)));
};

/**
 * 解析上课时间 - 适配格式"每周一，第1、2节"
 */
const parseClassTime = (classTime) => {
  if (!classTime) return null;

  console.log('原始classTime:', classTime); // 调试用

  // 适配格式："每周一，第1、2节" 或 "每周一第1、2节"（有无逗号都支持）
  const timePattern = /周([一二三四五六日])[，]?第(\d+)[、,](\d+)节/g;
  const result = [];
  let match;

  while ((match = timePattern.exec(classTime)) !== null) {
    const day = match[1];
    const startClass = parseInt(match[2]);
    const endClass = parseInt(match[3]);

    console.log('匹配到:', {day, startClass, endClass}); // 调试用

    result.push({
      day: '周' + day,
      startClass,
      endClass
    });
  }

  console.log('解析结果:', result); // 调试用
  return result.length > 0 ? result : null;
};

/**
 * 获取指定时间和日期的课程
 */
// 修改获取课程的方法
const getCourseAt = (day, time) => {
  const timeSlot = timeSlots.find(slot => slot.time === time);
  if (!timeSlot) return null;

  return filteredCourses.value.find(course => {
    const classTimes = parseClassTime(course.classTime);
    if (!classTimes) return false;

    return classTimes.some(classTime => {
      // 检查星期几是否匹配
      if (classTime.day !== day) return false;

      // 检查节次是否匹配
      console.log(`检查 ${course.name}: ${classTime.day} 第${classTime.startClass}-${classTime.endClass}节 vs ${timeSlot.time} 第${timeSlot.startClass}-${timeSlot.endClass}节`);

      return (
          classTime.startClass <= timeSlot.endClass &&
          classTime.endClass >= timeSlot.startClass
      );
    });
  });
};

/**
 * 检查课程是否在当前周
 */
const isCurrentWeekCourse = (course) => {
  if (!course || !course.weekRange) return false;

  const [startWeek, endWeek] = course.weekRange.split('-').map(Number);
  return currentWeek.value >= startWeek && currentWeek.value <= endWeek;
};

/**
 * 处理单元格点击
 */
const handleCellClick = (course) => {
  if (course) {
    detailCourse(course);
  }
};
/**
 * 格式化周次范围显示
 */
const formatWeekRange = (weekRange) => {
  if (!weekRange) return '';

  // 如果是"1-16"格式
  if (weekRange.includes('-')) {
    const [start, end] = weekRange.split('-');
    return `${start}-${end}`;
  }

  // 其他格式直接显示
  return weekRange;
};




//课程表格数据
const courseTableData = ref([{
  name: '',
  courseCode: '',
  courseType: '',
  college: '',
  semester: ''
}])

//全部数据
const fullCourseData = ref([{
  name: '',
  courseCode: '',
  courseType: '',
  college: '',
  semester: ''
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
  fetchData();
  fetchFullData();
})


/**
 * 获取数据
 */
const fetchData = async () => {
  try {
    //获取已选择的课程列表
    const response = await axios.get('/sel/selection/list/' + sessionStorage.getItem('studentId'), {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })

    courseTableData.value = response.data.data;
    total.value = Number(response.data.message);

  } catch (error) {
    console.error('获取数据失败:', error);
  }
}

/**
 * 获取全部数据不分页
 */
const fetchFullData = async () => {
  try {
    const response = await axios.get('/sel/selection/fullList/' + sessionStorage.getItem('studentId'))
    fullCourseData.value = response.data.data;
    console.log("fullCourseData.value")
    console.log(fullCourseData.value)

    // 初始化过滤课程
    if (fullCourseData.value.length > 0) {
      currentSemester.value = fullCourseData.value[0].semester || '';
      filterCourses();
    }
  } catch (error) {
    console.log(error);
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
 * 退课操作
 * @param rowData
 */
const selectCourse = async (courseId) => {
  //这个id是课程id
  if (!window.confirm('确定退课？该课程可能是其他课程的前置课程，请谨慎选择')) {
    return;
  }
  console.log("id")
  console.log(courseId)

  const response = await axios.delete('/sel/selection/' + courseId + '/' + sessionStorage.getItem('studentId'))
  console.log(response);
  if (response.data.code === 200) {
    ElMessage.success('退课成功')
    fetchData();
  } else {
    ElMessage.error('退课失败')
    fetchData();
  }
}

/**
 * 导出课程表
 */
const exportCourseTable = async () => {
  try {
    // 设置responseType为blob，接收二进制流
    const response = await axios.get('/exc/export/course/' + sessionStorage.getItem('account') + '/' + sessionStorage.getItem('role')+ '/' + currentSemester.value, {
      responseType: 'blob' // 必须设置，否则会解析为JSON导致乱码
    });

    // 从响应头获取文件名（如果后端设置了的话）
    let fileName = '课程表.xlsx'; // 默认文件名


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
};

</script>


<style scoped>
/* 课程表容器样式 */
.timetable-container {
  margin-bottom: 30px;
  overflow-x: auto;
}

/* 课程表基本样式 */
.timetable {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  table-layout: fixed; /* 新增：固定表格布局 */
}

/* 表头样式 */
.timetable th {
  background-color: #f5f7fa;
  color: #909399;
  font-weight: 500;
  height: 50px; /* 固定表头高度 */
  padding: 8px;
  text-align: center;
  border: 1px solid #ebeef5;
}

/* 时间列样式 */
.timetable .time-slot {
  background-color: #f5f7fa;
  color: #909399;
  font-weight: 500;
  width: 100px; /* 固定时间列宽度 */
  padding: 8px;
  border: 1px solid #ebeef5;
}

/* 课程单元格样式 */
.timetable td {
  height: 80px; /* 固定单元格高度 */
  min-height: 80px;
  padding: 0; /* 移除内边距，由内部元素控制 */
  border: 1px solid #ebeef5;
  text-align: center;
  vertical-align: top;
  position: relative;
}

/* 课程信息容器 */
.course-cell {
  height: 100%;
  padding: 4px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  overflow: hidden; /* 防止内容溢出 */
}

/* 课程名称样式 */
.course-name {
  font-weight: bold;
  margin-bottom: 2px;
  color: #303133;
  font-size: 14px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100%;
}

/* 课程地点和教师样式 */
.course-location,
.course-teacher {
  font-size: 12px;
  color: #606266;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100%;
}

/* 鼠标悬停效果 */
.timetable td:hover {
  background-color: #f0f7ff;
}

/* 有课程的单元格样式 */
.timetable td.has-course {
  background-color: #ecf5ff;
  border-color: #d9ecff;
}

/* 当前周的课程样式 */
.timetable td.current-week {
  background-color: #d9ecff;
  border-color: #c6e2ff;
}

/* 星期列宽均匀分配 */
.timetable th:nth-child(n+2),
.timetable td:nth-child(n+2) {
  width: calc((100% - 100px) / 7); /* 总宽度减去时间列，均分7天 */
}

/* 课程周次信息样式 */
.course-weeks {
  font-size: 11px;
  color: #909399;
  margin-top: 2px;
  font-style: italic;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100%;
}

/* 调整课程信息容器 */
.course-cell {
  padding: 4px;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}
</style>