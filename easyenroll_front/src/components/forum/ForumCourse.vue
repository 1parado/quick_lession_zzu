
<template>

  <el-affix :offset="800" class="bottom-right-affix">
    <el-button type="primary" :icon="RefreshLeft" size="large" plain circle @click="fetchData()" />
  </el-affix>

  <div class="common-layout">
    <el-container>
      <el-main>
        <div v-if="courseData" class="course-card">
          <h2 class="course-title">{{ courseData.courseName }}</h2>
          <div class="course-id">{{ courseData.courseCode }}</div>
          <div class="rating">
            <span class="rating-text">综合负载评价：</span>
            <span class="rating-score">评分: {{ courseData.rating !== null && courseData.rating !== undefined ? courseData.rating.toFixed(2) : '0.00' }}/5.00</span>
          </div>
        </div>
        <div v-else>
          加载中...
        </div>
        <div>
          <el-scrollbar height="1000px" @scroll="handleScroll" @end-reached="loadMore">

            <el-input v-model="searchText" style="width: 910px;height: 50px;margin-bottom: 45px;margin-top: 30px" placeholder="搜索您感兴趣的内容" />
            <el-button type="primary" plain :icon="Search" style="height: 50px;margin-left: 30px;margin-bottom: 45px;margin-top: 30px;" @click="searchPage()" round></el-button>

            <div style="margin-bottom: 45px">
              <el-radio-group v-model="byType" @change="handleTypeChange">
                <el-radio-button value="time" :class="{ 'active-type': byType === 'time' }">最新</el-radio-button>
                <el-radio-button value="hot" :class="{ 'active-type': byType === 'hot' }">最热</el-radio-button>
              </el-radio-group>
            </div>

            <div v-if="postListData.length > 0">
              <div
                  v-for="post in postListData"
                  :key="post.id"
                  class="post-card"
                  @click="viewPostDetail(post)"
              >
                <el-card shadow="hover" :body-style="{ padding: '20px' }">
                  <div class="post-header">
                    <div class="post-title">
                      {{ post.title }}
                      <span class="tag tag-course" v-if="post.mentionedCourseName">
                        {{ post.mentionedCourseName }}
                      </span>
                    </div>
                  </div>

                  <div class="post-content">
                    {{ post.content }}
                  </div>

                  <div class="post-footer">
                    <div class="post-stats">
                      <span><i class="el-icon-user"></i> {{ post.publishName }}</span>
                      <span><i class="el-icon-time"></i> {{ formatTime(post.publishTime) }}</span>
                      <span><i class="el-icon-chat-dot-round"></i> {{ post.commentCount }} 条回复</span>
                    </div>
                  </div>
                </el-card>
              </div>
            </div>

            <div class="empty-state" v-else>
              <el-empty description="暂无帖子" :image-size="200"></el-empty>
            </div>

            <div style="text-align: center; padding: 20px;">
              <el-button
                  :loading="loading"
                  @click="loadMore"
                  v-if="hasMore && postListData.length > 0"
              >
                加载更多
              </el-button>
              <span v-else>没有更多帖子了</span>
            </div>

          </el-scrollbar>
        </div>
      </el-main>
      <el-aside width="400px">
        <div class="smart-recommend">
          <div class="recommend-title">智能推荐</div>

          <div class="recommend-item" v-if="recommendCourseDataList && recommendCourseDataList.length >= 3">
            <span class="recommend-desc">您可能对这些课程感兴趣：</span>
            <span class="course-list">{{ recommendCourseDataList[0].courseName }}，{{ recommendCourseDataList[1].courseName }}，{{ recommendCourseDataList[2].courseName }}</span>
          </div>

          <div class="recommend-item" v-else-if="recommendCourseDataList && recommendCourseDataList.length > 0">
            <span class="recommend-desc">您可能对这些课程感兴趣：</span>
            <span class="course-list">{{ recommendCourseDataList.map(c => c.courseName).join('，') }}</span>
          </div>

          <div class="recommend-item" v-if="recommendCourseDataByUserList && recommendCourseDataByUserList.length >= 3">
            <span class="recommend-desc">选了这门课的同学，也选了：</span>
            <span class="course-list">{{ recommendCourseDataByUserList[0].courseName }}，{{ recommendCourseDataByUserList[1].courseName }}，{{ recommendCourseDataByUserList[2].courseName }}</span>
          </div>

          <div class="recommend-item" v-else-if="recommendCourseDataByUserList && recommendCourseDataByUserList.length > 0">
            <span class="recommend-desc">选了这门课的同学，也选了：</span>
            <span class="course-list">{{ recommendCourseDataByUserList.map(c => c.courseName).join('，') }}</span>
          </div>
        </div>
        <div style="display: flex; justify-content: center; align-items: center;">
          <el-button type="primary" size="large" plain @click="goCourseGroup()" style="margin-top: 50px">
            加入专属【{{ courseData.courseName }}】课程小组
          </el-button>
        </div>
      </el-aside>
    </el-container>
  </div>

</template>


<script setup>
import {useRoute} from "vue-router";
import {nextTick, onMounted, ref, watch} from "vue";
import {ElMessage} from "element-plus";
import {RefreshLeft, Search, Star, User} from "@element-plus/icons-vue";
import axios from "../../utils/http.js"
import router from "../../router.js";


const route = useRoute()  // 获取当前路由信息
const courseData = ref({
  id: '',
  courseName: '',
  courseCode: '',
  rating: ''
});
const recommendCourseDataList = ref([{
  id: '',
  courseName: '',
  courseCode: '',
  rating: ''
}])
const recommendCourseDataByUserList = ref([{
  id: '',
  courseName: '',
  courseCode: '',
  rating: ''
}])

const loading = ref(false);
const hasMore = ref(true);
// 输入的搜索文字
const searchText = ref('');
//贴子列表表格数据
const postListData = ref([{
  id: '',
  title: '',
  mentionedCourseName: '',
  mentionedCourseCode: '',
  content: '',
  imagePathList: '',
  publishName: '',
  publishSno: '',
  publishTime: '',
  commentCount: '',
  type: '',
  rating: ''
}]);

const byType = ref('time'); // 默认选择"最新"

//标记数据是否已初始化
const dataInitialized = ref(false);





const fetchData = async () => {
  if (!courseData.value || !courseData.value.courseCode) {
    console.log("courseCode未就绪，跳过数据获取");
    return;
  }
  loading.value = true;
  try {
    const response = await axios.get('/page/page/course/byType/' + courseData.value.courseCode + '/' + byType.value + '/' + 3);//开始只加载三条
    if (response.data.code === 200) {
      console.log(response.data.data)
      postListData.value = response.data.data;
    } else {
      ElMessage.error("加载数据失败")
    }
  } catch (e) {
    ElMessage.error("加载数据失败：" + e)
  } finally {
    loading.value = false;
    hasMore.value=true
  }
}

const fetchRecommendCourseData = async () => {
  try {
    const response = await axios.get('/page/page/recommend/' + courseData.value.courseCode + '/' + 3);//智能推荐默认三条
    if (response.data.code === 200) {
      console.log("response.data.data")
      console.log(response.data.data)
      recommendCourseDataList.value = response.data.data;
    } else {
      ElMessage.error("加载智能推荐数据失败")
    }
  } catch (e) {
    ElMessage.error("加载智能推荐数据失败：" + e)
  }
}

const fetchRecommendCourseDataByUser = async () => {
  try {
    const response = await axios.get('/page/page/recommend/byUser/' + courseData.value.courseCode + '/' + 3);//智能推荐默认三条
    if (response.data.code === 200) {
      console.log("response.data.data")
      console.log(response.data.data)
      recommendCourseDataByUserList.value = response.data.data;
    } else {
      ElMessage.error("加载智能推荐数据失败")
    }
  } catch (e) {
    ElMessage.error("加载智能推荐数据失败：" + e)
  }
}



watch(
    () => route.query.c,  // 监听路由中c参数的变化
    (newValue) => {
      if (newValue) {  // 如果t有值（说明是新提交后跳转过来的）
        try {
          // 将JSON字符串解析回对象
          courseData.value = JSON.parse(newValue);
          dataInitialized.value = true;

          fetchData(); // 重新加载数据
          fetchRecommendCourseData();
          fetchRecommendCourseDataByUser();
        } catch (e) {
          ElMessage.error("课程数据解析失败：" + e)
        }
      }
    },
    { immediate: true } // 立即执行一次
)

onMounted(() => {
  if (dataInitialized.value) {
    fetchData();
  } else {
    // 如果没有路由参数，等待下一个tick再检查
    nextTick(() => {
      if (dataInitialized.value) {
        fetchData();
        fetchRecommendCourseData();
        fetchRecommendCourseDataByUser();
      }
    });
  }
})



/**
 * 加载更多的贴子数据
 * @param direction
 */
const loadMore = async () => {
  loading.value = true;
  try {
    const response = await axios.get('/page/page/course/' + searchText.value + '/' + courseData.value.courseCode + '/' + (postListData.value.length + 3));//每次只加载三条
    if (response.data.code === 200 && response.data.data.length === postListData.value.length) {
      hasMore.value = false;
    } else if (response.data.code === 200) {
      postListData.value = response.data.data;
    } else {
      ElMessage.error("加载数据失败")
    }
  } catch (e) {
    ElMessage.error("加载数据失败：" + e)
  } finally {
    loading.value = false;
  }
}

/**
 * 搜索贴子
 */
const searchPage = async () => {
  if (searchText.value === '' || searchText.value.length <= 0) {
    ElMessage.info("请输入搜索内容")
    return;
  }
  try {
    const response = await axios.get('/page/page/search/course/' + searchText.value + '/' + courseData.value.courseCode + '/' + 3)
    postListData.value = response.data.data;
    hasMore.value=true
  } catch (error) {
    console.error('获取数据失败:', error);
  }
}

/**
 * 查看贴子
 */
const viewPostDetail = (page) => {
  console.log(page)
  router.push({ path: 'forumPost', query: { p: page.id } });
}




/**
 * 处理滚动事件
 * @param scrollTop
 * @param scrollHeight
 * @param clientHeight
 */
const handleScroll = ({scrollTop, scrollHeight, clientHeight}) => {
  // 接近底部时自动加载更多
  if (scrollHeight - scrollTop - clientHeight < 50 && !loading.value) {
    loadMore();
  }
};

/**
 * 格式化时间
 * @param timeStr
 * @returns {string}
 */
const formatTime = (timeStr) => {
  return new Date(timeStr).toLocaleDateString();
};

/**
 * 处理类型变化
 * @param value
 */
const handleTypeChange = (value) => {
  console.log("当前选择:", value);
  // 这里可以添加数据加载逻辑
  fetchData();
};

/**
 * 去课程小组
 */
const goCourseGroup = async () => {

  //发送请求查看是否有资格进入专属课程小组
  const response = await axios.get('/grade/grade/check/' + sessionStorage.getItem('account') + '/' + courseData.value.courseCode)
  if (response.data.code === 200) {
    goGroup();
  } else {
    ElMessage.error(response.data.message)
  }
}

const goGroup = () => {
  const json = JSON.stringify(courseData.value.courseCode);
  router.push({path: '/forumHomepage/forumCourseGroup', query: {code: json}});
}

</script>


<style scoped>


.common-layout {
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
}

.post-card {
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
}
.post-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
}
.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}
.post-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}
.post-meta {
  display: flex;
  gap: 15px;
  color: #909399;
  font-size: 14px;
}
.post-content {
  color: #606266;
  line-height: 1.6;
  margin-bottom: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.post-footer {
  display: flex;
  justify-content: space-between;
  align-items: center; /* 确保垂直居中 */
  border-top: 1px solid #EBEEF5;
  padding-top: 15px;
  color: #909399;
}

.post-stats {
  display: flex;
  gap: 20px;
  align-items: center; /* 添加垂直居中对齐 */
}
.tag {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  margin-right: 8px;
}
.tag-course {
  background-color: #ecf5ff;
  color: #409EFF;
  border: 1px solid #d9ecff;
}
.empty-state {
  text-align: center;
  padding: 60px 0;
}

.bottom-right-affix {
  position: fixed;
  right: 300px;
  bottom: 300px;
  z-index: 1000;
}



body {
  background-color: #f5f5f5;
  padding: 20px;
  color: #333;
}

.container {
  max-width: 800px;
  margin: 0 auto;
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.header {
  background: linear-gradient(to right, #e74c3c, #c0392b);
  color: white;
  padding: 20px;
  text-align: center;
}

.header h1 {
  font-size: 24px;
  margin-bottom: 10px;
}





.smart-recommend {
  padding: 15px;
  background: #f8fafc;
  border-radius: 6px;
  margin-bottom: 20px;
}

.recommend-title {
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #eaeaea;
}

.recommend-item {
  margin-bottom: 10px;
  padding: 8px 0;
}

.recommend-desc {
  color: #5c6bc0;
  font-weight: 500;
}

.course-list {
  color: #42b983;
  font-weight: 500;
}

.course-card {
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
}

.course-title {
  color: #1a1a1a;
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
}

.course-id {
  color: #666;
  margin-bottom: 12px;
  font-size: 15px;
}

.rating {
  padding-top: 10px;
  border-top: 1px solid #f0f0f0;
}

.rating-text {
  color: #666;
}

.rating-score {
  color: #ff6b35;
  font-weight: 600;
}
</style>