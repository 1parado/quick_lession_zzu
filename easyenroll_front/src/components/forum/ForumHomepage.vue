<template>

  <el-affix :offset="800" class="bottom-right-affix">
    <el-button type="primary" :icon="RefreshLeft" size="large" plain circle @click="fetchData()" />
  </el-affix>

  <div class="common-layout">
    <el-container>

      <el-container>
        <el-main >

          <el-scrollbar height="1000px" @scroll="handleScroll" @end-reached="loadMore">

            <el-input v-model="searchText" style="width: 910px;height: 50px;margin-bottom: 45px;margin-top: 30px" placeholder="搜索您感兴趣的内容" />
            <el-button type="primary" plain :icon="Search" style="height: 50px;margin-left: 30px;margin-bottom: 45px;margin-top: 30px;" @click="searchPage()" round></el-button>

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
<!--                    <div class="post-meta">
                      <span>评分: {{ post.rating }}/5</span>
                    </div>-->
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
                  v-if="hasMore"
              >
                加载更多
              </el-button>
              <span v-else>没有更多帖子了</span>
            </div>

          </el-scrollbar>
        </el-main>

        <el-aside width="400px">
          <div class="container" style="margin-top: 20px">
            <div class="header">
              <h1>热议榜</h1>
              <button class="view-list-btn" @click="fetchHotList">刷新榜单</button>
            </div>

            <div class="hot-list">
              <div
                  v-for="(item, index) in hotListData"
                  :key="item.id"
                  class="hot-item"
              >
                <div :class="['rank', index < 3 ? 'rank-top3' : '']">{{ index + 1 }}</div>
                <div class="content">
                  <div class="title" @click="viewPostDetail(item)">{{ item.title }}</div>
                  <div class="heat">热度: {{ item.commentCount }}</div>
                </div>
              </div>
            </div>

            <div class="footer">
              <p>更新时间: <span id="update-time">{{ formatTime(updateTime) }}</span></p>
            </div>
          </div>
        </el-aside>

      </el-container>
    </el-container>
  </div>
</template>



<script setup>

import router from "../../router.js";
import {onMounted, ref} from "vue";
import axios from "../../utils/http.js";
import {ElMessage} from "element-plus";
import {RefreshLeft, Search} from '@element-plus/icons-vue'

/**
 * 变量
 *
 *
 *
 *
 */

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

// 热议数据
const hotListData = ref([]);
// 热议榜更新时间
const updateTime = ref('');


//
const loading = ref(false);
const hasMore = ref(true);

// 输入的搜索文字
const searchText = ref('');

/**
 * 函数（主要）
 *
 *
 *
 *
 */

onMounted(() => {
  fetchData();
  fetchHotList();
})

const fetchData = async () => {
  loading.value = true;
  try {
    const response = await axios.get('/page/page/' + 3);//开始只加载三条
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

const fetchHotList = async () => {
  try {
    const response = await axios.get('/page/page/hotList');
    if (response.data.code === 200) {
      hotListData.value = response.data.data;
      updateTime.value = response.data.message;
    } else {
      ElMessage.error("加载热议榜失败");
    }
  } catch (e) {
    ElMessage.error("加载热议榜失败：" + e);
  }
};

/**
 * 加载更多的贴子数据
 * @param direction
 */
const loadMore = async () => {
  loading.value = true;
  try {
    const response = await axios.get('/page/page/' + searchText.value + '/' + (postListData.value.length + 3));//每次只加载三条
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
 * 查看贴子
 */
const viewPostDetail = (page) => {
  console.log(page)
  router.push({ path: '/forumHomepage/forumPost', query: { p: page.id } });
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
    const response = await axios.get('/page/page/search/' + searchText.value + '/' + 3)
    postListData.value = response.data.data;
    hasMore.value=true
  } catch (error) {
    console.error('获取数据失败:', error);
  }
}


/**
 * 函数（辅助）
 *
 *
 *
 *
 */

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

</script>

<style scoped>
/* 悬停效果 */
.btn {
  display: inline-block;
  cursor: pointer;  /* 手型指针 */
}
.btn:hover {
  color: #409eff;  /* 悬停变蓝色 */
}

.common-layout {
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
}
.el-header {
  background-color: #f5f7fa;
  border-bottom: 1px solid #e6e6e6;
  padding: 0 20px;
}
.btn {
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 4px;
  transition: all 0.3s;
}
.btn:hover {
  color: #409EFF;
  background-color: rgba(64, 158, 255, 0.1);
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

.view-list-btn {
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid white;
  color: white;
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.view-list-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.hot-list {
  padding: 15px;
}

.hot-item {
  display: flex;
  align-items: center;
  padding: 12px 15px;
  border-bottom: 1px solid #eee;
  transition: background-color 0.2s;
}

.hot-item:hover {
  background-color: #f9f9f9;
}

.rank {
  font-size: 18px;
  font-weight: bold;
  color: #e74c3c;
  width: 40px;
  text-align: center;
}

.rank-top3 {
  font-size: 22px;
}

.content {
  flex: 1;
}

.title {
  font-size: 16px;
  margin-bottom: 5px;
  cursor: pointer;
}

.title:hover {
  color: #e74c3c;
}

.heat {
  font-size: 12px;
  color: #888;
}

.footer {
  text-align: center;
  padding: 15px;
  color: #888;
  font-size: 12px;
}

</style>