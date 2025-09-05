

<template>
  <div class="common-layout">
    <el-container>
      <el-aside width="700px">
        <el-scrollbar height="700px" style="width: 700px;" >
          <el-space direction="vertical" style="margin-right: 100px;margin-left: 100px">
            <el-card
                v-for="(item, index) in announcementData"
                :key="index"
                class="box-card"
                style="width: 498px; margin-bottom: 20px;"
            >
              <template #header>
                <div class="card-header">
                  <span>{{ item.title }}</span>
                  <el-tag v-if="item.status === 'PUBLISHED'" type="success" size="small">已发布</el-tag>
                </div>
              </template>
              <div class="card-content">
                <p>{{ item.content }}</p>
                <div class="card-footer">
                  <div class="footer-info">
                    <span class="publisher">发布人: {{ item.publicName }}</span>
                    <span class="time">发布时间: {{ formatTime(item.publishTime) }}</span>
                  </div>
                </div>
              </div>
            </el-card>
          </el-space>
        </el-scrollbar>
      </el-aside>
      <el-main>
        <el-calendar v-model="value">
          <template #date-cell="{ data }">
            <div class="calendar-day" :class="{ 'has-course': hasCourseOnDate(data.day) }">
              <div class="day-number">{{ data.day.split('-')[2] }}{{ console.log(data) }}</div>
              <div v-if="hasCourseOnDate(data.day)" class="course-mark"></div>
            </div>
          </template>
        </el-calendar>

      </el-main>
    </el-container>
  </div>

</template>


<script setup>
import {onMounted, ref} from "vue";
import axios from '../utils/http.js';


  const value = ref(new Date())

  const announcementData = ref([]);

  const courseTableData = ref([]);

  onMounted(() => {
    fetchData();
    fetchTableData();
    fetchTeacherTableData();
  });


  const fetchData = async () => {
    const response = await axios.get('/ann/announcement/allPublish')
    console.log("response.data.data")
    console.log(response.data.data)
    if (response.data.code === 200) {
      announcementData.value = response.data.data;
    }
    console.log("announcementData")
    console.log(announcementData)
  }

// 定义时间格式化函数
const formatTime = (time) => {
  if (!time) return '';
  const date = new Date(time);
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  const hours = date.getHours().toString().padStart(2, '0');
  const minutes = date.getMinutes().toString().padStart(2, '0');
  const seconds = date.getSeconds().toString().padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

const fetchTableData = async () => {
  try {
    //获取已选择的课程列表
    const response = await axios.get('/sel/selection/list/noPage/' + sessionStorage.getItem('account'))
    console.log('API响应数据:', response.data); // 调试用

    //const TeacherResponse = await axios.get('/sel/selection/list/teacher/noPage/' + sessionStorage.getItem('account'))
    courseTableData.value = response.data.data;

    if (sessionStorage.getItem('role') === 'STUDENT') {
      courseTableData.value = response.data.data;
    } /*else if (sessionStorage.getItem('role') === 'TEACHER') {
      courseTableData.value = TeacherResponse.data.data;
    }*/
    console.log("courseTableData.value")
    console.log(courseTableData.value)

  } catch (error) {
    console.error('获取数据失败:', error);
  }
}
const fetchTeacherTableData = async () => {
  try {
    //获取已选择的课程列表
    const TeacherResponse = await axios.get('/sel/selection/list/teacher/noPage/' + sessionStorage.getItem('account'))
    if (sessionStorage.getItem('role') === 'TEACHER') {
      courseTableData.value = TeacherResponse.data.data;
    }

  } catch (error) {
    console.error('获取数据失败:', error);
  }
}


// 判断指定日期是否有课
const hasCourseOnDate = (dateString) => { //"2025-02-17"
  if (!courseTableData.value || courseTableData.value.length === 0) return false;

  const date = new Date(dateString);
  if (isNaN(date.getTime())) {
    console.error('无效日期:', dateString);
    return false;
  }
  const dayOfWeek = date.getDay(); // 0是周日，1是周一，...，6是周六
  const weekNumber = getWeekNumber(date);

  return courseTableData.value.some(course => {
    // 检查周数范围
    const inRange = isInWeekRange(weekNumber, course.weekRange);
    console.log("weekRange", course.weekRange)
    console.log(inRange)
    const isDay = isCourseDay(course.classTime, dayOfWeek);
    console.log("classTime", course.classTime)
    console.log(isDay)
    // 检查星期几
    return inRange && isDay;
  });
};

// 计算日期是学期第几周
const getWeekNumber = (date) => {
  const year = date.getFullYear();
  const month = date.getMonth() + 1;


  // 判断学期
  let semesterStart;
  if (month >= 2 && month <= 7) {
    // 上学期: 2月17日
    semesterStart = new Date(year, 1, 17);
  } else {
    // 下学期: 9月1日
    semesterStart = new Date(month >= 9 ? year : year - 1, 8, 1);
  }

  // 修正3：处理日期早于学期开始的情况
  if (date < semesterStart) return 0;

  // 计算周数
  const diffTime = date - semesterStart;
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  const weekNum = Math.floor(diffDays / 7) + 1;
  return weekNum;
};

// 检查是否在课程周数范围内
const isInWeekRange = (currentWeek, weekRange) => {
  if (!weekRange) return true;
  const [start, end] = weekRange.replace('周', '').split('-').map(Number);
  console.log(currentWeek)
  return currentWeek >= start && currentWeek <= end;
};

// 检查是否是课程安排的星期几
const isCourseDay = (classTime, dayOfWeek) => {
  if (!classTime) return false;

  // 更健壮的匹配逻辑
  const weekDayMap = {
    '日': 0, '天': 0,
    '一': 1, '二': 2, '三': 3, '四': 4, '五': 5, '六': 6
  };

  const match = classTime.match(/每周[周]?([一二三四五六七日天])/);
  if (!match) return false;

  const chineseDay = match[1];
  return weekDayMap[chineseDay] === dayOfWeek;
};
</script>



<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-content {
  font-size: 14px;
  color: #606266;
  line-height: 1.5;
  min-height: 80px;
}

.card-footer {
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
  border-top: 1px solid #f0f0f0;
  padding-top: 8px;
}

.footer-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.publisher {
  font-weight: 500;
  color: #666;
}

.time {
  color: #999;
}

.box-card {
  transition: all 0.3s;
}

.box-card:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);
  transform: translateY(-5px);
}

/* 日历样式优化 */
.calendar-day {
  height: 100%;
  position: relative;
  padding: 8px;
  transition: all 0.2s ease;
  border-radius: 4px;
}

.calendar-day:hover {
  background-color: #f5f5f5;
}

.day-number {
  text-align: right;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 4px;
  color: #333;
}

.has-course {
  background-color: #e6f7ff;
  box-shadow: 0 0 0 1px #bae7ff inset;
}

.has-course .day-number {
  color: #1890ff;
  font-weight: 600;
}

.course-mark {
  position: absolute;
  bottom: 8px;
  left: 50%;
  transform: translateX(-50%);
  width: 24px;
  height: 4px;
  border-radius: 2px;
  background-color: #1890ff;
  opacity: 0.8;
}

/* 日历基础样式 */
.calendar-day {
  height: 100%;
  position: relative;
  padding: 4px;
  box-sizing: border-box;
}

.day-number {
  text-align: right;
  font-size: 14px;
  margin: 0;
  padding: 2px 4px 0 0;
}

/* 有课日期样式 */
.has-course {
  background-color: rgba(230, 247, 255, 0.3); /* 半透明浅蓝色背景 */
}

.has-course .day-number {
  font-weight: 500;
  color: #1890ff;
}

/* "有课"文字标记 */
.course-mark {
  position: absolute;
  bottom: 2px;
  left: 0;
  right: 0;
  text-align: center;
  font-size: 12px;
  color: #1890ff;
  padding: 1px 0;
  background-color: rgba(230, 247, 255, 0.5);
  border-radius: 2px;
}

/* 今日日期特殊样式 */
:deep(.el-calendar-table td.is-today) {
  background-color: rgba(240, 249, 235, 0.5);
}

:deep(.el-calendar-table td.is-today .day-number) {
  color: #67c23a;
  font-weight: 500;
}

/* 日历整体调整 */
:deep(.el-calendar-table td) {
  padding: 0;
}

:deep(.el-calendar-day) {
  height: 80px;
  padding: 0;
}
</style>