<template>

  <div class="common-layout">
    <el-container>
      <el-main>
        <el-scrollbar height="700px" class="scrollbar-container">
          <div
              v-for="course in courseList"
              :key="course.courseCode"
              @click="viewCourseDetail(course)"
          >
            <el-card shadow="hover" :body-style="{ padding: '20px' }">
              <div class="course-name">
                {{ course.courseName }}
              </div>
              <div class="course-code">
                {{ course.courseCode }}
              </div>
              <div class="course-rating">
                <span>评分: {{ course.rating !== null && course.rating !== undefined ? course.rating.toFixed(2) : '0.00' }}/5.00</span>
              </div>
            </el-card>
          </div>
        </el-scrollbar>
      </el-main>
      <el-aside width="400px">
      </el-aside>
    </el-container>
  </div>

</template>


<script setup>

import {onMounted, ref} from "vue";
import {ElMessage} from "element-plus";
import axios from "../../utils/http.js"
import router from "../../router.js";

//课程列表数据
const courseList = ref([{
  courseName: '',
  courseCode: '',
  rating: 0.0
}])

onMounted(() => {
  fetchData();
})

const fetchData = async () => {
  try {
    const response = await axios.get('/page/course')
    if (response.data.code === 200) {
      courseList.value = response.data.data
    } else {
      ElMessage.error("获取数据失败")
    }
  } catch (e) {
    ElMessage.error("获取数据失败：" + e)
  }
}


const viewCourseDetail = (course) => {
  console.log(course)
  const courseJson = JSON.stringify(course);
  router.push({ path: 'forumCourse', query: { c: courseJson } });
}

</script>

<style scoped>
.scrollbar-demo-item {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  margin: 10px;
  text-align: center;
  border-radius: 4px;
  background: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}

.common-layout {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.el-card {
  margin-bottom: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  border-radius: 12px;
  border: 1px solid #ebeef5;
}

.el-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12) !important;
}

.course-name {
  font-size: 18px;
  font-weight: 600;
  color: #1f2f3d;
  margin-bottom: 8px;
}

.course-code {
  font-size: 14px;
  color: #5e6d82;
  margin-bottom: 12px;
}

.course-rating {
  display: flex;
  align-items: center;
  color: #f7ba2a;
  font-weight: 500;
}


</style>