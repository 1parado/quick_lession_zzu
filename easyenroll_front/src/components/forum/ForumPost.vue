<template>
  <div class="common-layout">
    <el-container>
      <el-main>
        <div class="post-header">
          <h1 class="post-title">{{ pageData.title }}</h1>
          <div class="post-meta">
            <span class="author">作者: {{ pageData.publishName }} ({{ pageData.publishSno }})</span>
            <span class="publish-time">发布时间: {{ formatTime(pageData.publishTime) }}</span>
          </div>
          <div class="course-tag" v-if="pageData.mentionedCourseName">
            相关课程: {{ pageData.mentionedCourseName }} ({{ pageData.mentionedCourseCode }})
          </div>
          <div class="rating">
            <el-rate
                :model-value="Number(pageData.rating) || 0"
                disabled
                show-score
                text-color="#ff9900"
                score-template="{value} 分">
            </el-rate>
          </div>
        </div>
        <div class="post-content" style="white-space: pre-wrap;">
          {{ pageData.content }}
        </div>
        <div class="comments-section">
          <h3 class="comments-header">评论 ({{ pageData.commentCount }})</h3>

          <!-- 评论输入框 -->
          <div class="comment-input-area">
            <el-input
                v-model="newCommentContent"
                :rows="3"
                type="textarea"
                placeholder="写下你的评论..."
                maxlength="300"
                show-word-limit
            />
            <div class="comment-actions">
              <el-button type="primary" @click="submitComment">发表评论</el-button>
            </div>
          </div>

          <!-- 评论列表 -->
          <div style="height: 650px; overflow: hidden;">
            <el-scrollbar height="100%">
              <div class="comments-list">
                <div
                    v-for="comment in commentDataList.filter(item => item.relatedCommentId === null)"
                    :key="comment.id"
                    class="comment-item"
                    :class="{ 'is-reply': comment.relatedCommentId !== 0 }"
                >
                  <div class="comment-avatar">{{ comment.reviewerName.charAt(0) }}</div>
                  <div class="comment-content">
                    <div class="comment-header">

                      <span class="comment-author">{{ comment.reviewerName }} ({{ comment.reviewerNo }})
                        <span v-if="comment.reviewerNo.toString().substring(0, 4) === '6666'" class="teacher-badge">教师</span>
                      </span>

                      <div class="comment-actions">
                        <span class="like-action" @click="likeComment(comment)">
                          <el-icon><Star /></el-icon>
                            {{ comment.likeCount }}
                        </span>
                        <span class="reply-action" @click="startReply(comment.id)">
                          回复
                        </span>
                        <span
                            v-if="Number(comment.reviewerNo) === Number(no)"
                            class="delete-action"
                            @click="deleteComment(comment)"
                        >
                          删除
                        </span>
                      </div>

                    </div>

                    <div class="comment-text">{{ comment.content }}</div>

                    <!-- 显示回复给谁 -->
                    <div v-if="comment.relatedCommentId !== 0 && comment.relatedCommentId !== null" class="reply-to">
                      回复给: {{ getReplyToName(comment.relatedCommentId) }}
                    </div>

                    <!-- 回复输入框 -->
                    <div v-if="replyingTo === comment.id" class="reply-input-area">
                      <el-input
                          v-model="replyContent"
                          :rows="2"
                          type="textarea"
                          placeholder="写下你的回复..."
                          maxlength="200"
                          show-word-limit
                      />
                      <div class="reply-actions">
                        <el-button size="small" @click="cancelReply">取消</el-button>
                        <el-button type="primary" size="small" @click="submitReply(comment.id)">回复</el-button>
                      </div>
                    </div>

                    <!-- 显示子回复 -->
                    <div v-if="hasReplies(comment.id)" class="replies-container">
                      <div
                          v-for="reply in getReplies(comment.id)"
                          :key="reply.id"
                          class="comment-item is-reply"
                      >
                        <div class="comment-avatar">{{ reply.reviewerName.charAt(0) }}</div>
                        <div class="comment-content">
                          <div class="comment-header">
                            <span class="comment-author">{{ reply.reviewerName }} ({{ reply.reviewerNo }})
                              <span v-if="comment.reviewerNo.toString().substring(0, 4) === '6666'" class="teacher-badge">教师</span>
                            </span>
                            <div class="comment-actions">
                      <span class="like-action" @click="likeComment(reply)">
                        <el-icon><Star /></el-icon>
                        {{ reply.likeCount }}
                      </span>
                              <span
                                  v-if="Number(reply.reviewerNo) === Number(no)"
                                  class="delete-action"
                                  @click="deleteComment(reply)"
                              >
                        删除
                      </span>
                            </div>
                          </div>
                          <div class="comment-text">{{ reply.content }}</div>
                          <div class="reply-to" v-if="reply.relatedCommentId !== 0 && reply.relatedCommentId !== null">
                            回复给: {{ getReplyToName(reply.relatedCommentId) }}
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- 加载更多提示 -->
                <div v-if="loading" class="loading-more">
                  <el-icon class="is-loading"><Loading /></el-icon>
                  加载中...
                </div>
                <div v-else-if="noMore" class="no-more">
                  没有更多评论了
                </div>

              </div>
            </el-scrollbar>
          </div>
        </div>
      </el-main>
      <el-aside width="400px">
        <div class="aside-header">
          <el-button
              plain
              @click="goBack()"
              style="padding: 8px 15px;"
          >
            <el-icon><Back /></el-icon>&nbsp;
            返回
          </el-button>
          <el-button
              type="danger"
              plain
              @click="deletePost()"
              style="padding: 8px 15px;"
              v-if="showDeleteButton"
          >
            <el-icon><Delete /></el-icon>&nbsp;
            删除
          </el-button>
        </div>

        <div class="author-info">
          <div class="author-avatar">{{ pageData.publishName.charAt(0) }}</div>
          <div>
            <div style="font-weight: 500;">{{ pageData.publishName }}</div>
            <div style="font-size: 14px; color: #666;">学号: {{ pageData.publishSno }}</div>
          </div>
        </div>
        <el-divider />
        <div>
          <h4>帖子信息</h4>
          <p style="margin-top: 10px;">发布时间: {{ formatTime(pageData.publishTime) }}</p>
          <p>评论数: {{ pageData.commentCount }}</p>
          <p v-if="pageData.mentionedCourseName">相关课程: {{ pageData.mentionedCourseName }}</p>
        </div>
      </el-aside>
    </el-container>
  </div>
</template>

<script setup>
import {computed, nextTick, onMounted, ref, watch} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {useRoute} from "vue-router";
import axios from "../../utils/http.js"
import router from "../../router.js";

const pageData = ref({
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
})

// 评论数据列表
const commentDataList = ref([])
// 标记数据是否已初始化
const dataInitialized = ref(false);
const route = useRoute()  // 获取当前路由信息
// 登录者学号/工号
const no = ref('')

// 新评论内容
const newCommentContent = ref('')
// 回复内容
const replyContent = ref('')
// 当前正在回复的评论ID
const replyingTo = ref(null)
// 加载状态
const loading = ref(false)
// 是否没有更多评论
const noMore = ref(false)


const fetchData = async () => {
  try {
    const response = await axios.get('/page/page/post/' + pageData.value.id);
    if (response.data.code === 200) {
      pageData.value = response.data.data;
    } else {
      ElMessage.error("数据获取失败");
    }
  } catch (e) {
    ElMessage.error("数据获取失败：" + e)
  }
}

const fetchCommentData = async () => {
  try {
    const response = await axios.get(`/page/page/post/comment/${pageData.value.id}`);
    if (response.data.code === 200) {
      commentDataList.value = response.data.data
      // 如果返回数据少于pageSize，说明没有更多数据了

    } else {
      ElMessage.error("数据获取失败");
    }
  } catch (e) {
    ElMessage.error("数据获取失败：" + e)
  }
}

watch(
    () => route.query.p,  // 监听路由中p参数的变化
    (newValue) => {
      if (newValue) {  // 如果p有值（说明是新提交后跳转过来的）
        try {
          // 将JSON字符串解析回对象
          pageData.value.id = JSON.parse(newValue);
          dataInitialized.value = true;

          fetchData(); // 重新加载数据
          fetchCommentData();
        } catch (e) {
          ElMessage.error("贴子数据解析失败：" + e)
        }
      }
    },
    { immediate: true } // 立即执行一次
)

onMounted(() => {
  if (typeof sessionStorage !== 'undefined') {
    no.value = sessionStorage.getItem('account') || ''
  }

  // 检查是否有路由参数
  if (route.query.p) {
    try {
      pageData.value.id = JSON.parse(route.query.p);
      dataInitialized.value = true;
      fetchData();
      fetchCommentData();
    } catch (e) {
      ElMessage.error("贴子数据解析失败：" + e)
    }
  } else {
    // 如果没有路由参数，等待下一个tick再检查
    nextTick(() => {
      if (dataInitialized.value) {
        fetchData();
        fetchCommentData();
      }
    });
  }
})

/**
 * 加载更多评论
 */
/*const loadMore = async () => {
  if (loading.value || noMore.value) {
    return;
  }

  loading.value = true;

  try {
    const nextPage = currentPage.value + 1;
    const url = `/page/page/post/comment/${pageData.value.id}/${nextPage}/${pageSize.value}`;

    const response = await axios.get(url);

    if (response.data.code === 200) {
      const newComments = response.data.data;

      commentDataList.value = [...commentDataList.value, ...newComments];
      currentPage.value = nextPage;

      if (newComments.length < pageSize.value) {
        noMore.value = true;
      }
    } else {
      ElMessage.error("加载更多数据失败");
    }
  } catch (e) {
    ElMessage.error("加载更多数据失败：" + e.message);
  } finally {
    loading.value = false;
  }
}*/

/**
 * 提交评论
 */
const submitComment = async () => {
  if (!newCommentContent.value.trim()) {
    ElMessage.warning('评论内容不能为空');
    return;
  }

  try {
    const response = await axios.post('/page/page/post/comment', {
      content: newCommentContent.value,
      reviewerNo: sessionStorage.getItem('account'),
      relatedPostId: pageData.value.id
    })
    if (response.data.code === 200) {
      ElMessage.success("评论成功");
      newCommentContent.value = '';

      // 重新获取评论数据
      await fetchCommentData();
      // 更新评论计数
      pageData.value.commentCount += 1;

    } else {
      ElMessage.error("评论失败")
    }
  } catch (e) {
    ElMessage.error("评论失败：" + e)
  }
}

/**
 * 开始回复评论
 */
const startReply = (id) => {
  replyingTo.value = id;
  replyContent.value = '';
}

/**
 * 取消回复
 */
const cancelReply = () => {
  replyingTo.value = null;
  replyContent.value = '';
}

/**
 * 提交回复
 */
const submitReply = async (commentId) => {
  if (!replyContent.value.trim()) {
    ElMessage.warning('回复内容不能为空');
    return;
  }

  try {
    const response = await axios.post('/page/page/post/comment/reply', {
      content: replyContent.value,
      reviewerNo: sessionStorage.getItem('account'),
      relatedPostId: pageData.value.id,
      relatedCommentId: commentId
    })

    if (response.data.code === 200) {
      replyingTo.value = null;
      replyContent.value = '';

      // 重新获取评论数据
      await fetchCommentData();
      // 更新评论计数
      pageData.value.commentCount += 1;
      ElMessage.success("回复成功");

    } else {
      ElMessage.error("回复失败")
    }
  } catch (e) {
    ElMessage.error("回复失败：" + e)
  }
}

/**
 * 获取被回复者的名称
 */
const getReplyToName = (commentId) => {
  const comment = commentDataList.value.find(c => c.id === commentId);
  return comment ? comment.reviewerName : '未知用户';
}

/**
 * 检查是否有回复
 */
const hasReplies = (commentId) => {
  return commentDataList.value.some(c => c.relatedCommentId === commentId);
}

/**
 * 获取特定评论的所有回复
 */
const getReplies = (commentId) => {
  return commentDataList.value.filter(c => c.relatedCommentId === commentId);
}

/**
 * 点赞评论
 */
const likeComment = async (comment) => {

  try {
    const response = await axios.post("/page/page/post/comment/like", {
      likerNo: sessionStorage.getItem('account'),
      commentId: comment.id
    })
    if (response.data.code === 200 && response.data.data === '点赞成功') {
      comment.likeCount += 1;
      ElMessage.success('已点赞');
    } else if (response.data.code === 200 && response.data.data === '取消点赞成功') {
      comment.likeCount -= 1;
      ElMessage.success('已取消点赞');
    } else {
      ElMessage.error("点赞操作失败");
    }
  } catch (e) {
    ElMessage.error("点赞失败")
  }
}

/**
 * 删除评论
 */
const deleteComment = async (comment) => {
  ElMessageBox.confirm('确定要删除这条评论吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {


    const response = await axios.delete('/page/page/post/comment/' + comment.id + '/' + sessionStorage.getItem('account') + '/' + comment.reviewerNo + '/' + comment.relatedPostId)
    if (response.data.code === 200) {
      ElMessage.success("删除成功")
      // 重新获取评论数据
      await fetchCommentData();
      // 更新评论计数
      pageData.value.commentCount -= 1;


    }
  }).catch(() => {
    // 用户取消删除
  });
}

/**
 * 删除贴子
 */
const deletePost = async () => {
  try {
    const response = await axios.delete('/page/page/post/' + pageData.value.id + '/' + pageData.value.publishSno + '/' + no.value);
    if (response.data.code === 200) {
      ElMessage.success("删除成功")
      goBack();
    } else {
      ElMessage.error("删除失败")
    }

  } catch (e) {
    ElMessage.error("删除失败：" + e)
  }
}

/**
 * 返回
 */
const goBack = () => {
  // 设置返回标记，让列表页面知道这是从详情页返回的
  window.history.length > 1 ? router.go(-1) : router.push('/forumHomepage')
}

/**
 * 格式化时间
 * @param timeStr
 * @returns {string}
 */
const formatTime = (timeStr) => {
  return new Date(timeStr).toLocaleDateString();
};

/**
 * 计算属性来判断是否显示删除按钮
 */
const showDeleteButton = computed(() => {
  if (!pageData.value.publishSno || !no.value) return false;

  // 转换为字符串并去除前后空格
  const publishSno = String(pageData.value.publishSno).trim();
  const currentNo = String(no.value).trim();

  return publishSno === currentNo;
});
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
  background-color: #f5f7fa;
  color: #333;
  line-height: 1.6;
}
.post-header {
  border-bottom: 1px solid #eee;
  padding-bottom: 16px;
  margin-bottom: 20px;
}
.post-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #1f2f3d;
}
.post-meta {
  display: flex;
  align-items: center;
  color: #87909e;
  font-size: 14px;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 8px;
}
.post-content {
  font-size: 16px;
  line-height: 1.8;
  margin-bottom: 24px;
  color: #2c3e50;
}
.course-tag {
  display: inline-block;
  background: #ecf5ff;
  color: #409eff;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 12px;
  margin-right: 8px;
}
.rating {
  display: flex;
  align-items: center;
  margin: 10px 0;
}
.comments-section {
  margin-top: 30px;
}
.comments-header {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
  color: #1f2f3d;
}

/* 评论输入区域 */
.comment-input-area {
  margin-bottom: 20px;
}
.comment-actions {
  margin-top: 10px;
  display: flex;
  justify-content: flex-end;
}

/* 评论列表 */
.comments-list {
  padding: 10px 0;
}
.comment-item {
  display: flex;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}
.comment-item.is-reply {
  margin-left: 15px;
  margin-right: 15px;
  padding: 10px 0;
  border-bottom-color: #f5f5f5;
}
.comment-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #409eff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-right: 12px;
  flex-shrink: 0;
}
.comment-content {
  flex: 1;
}
.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}
.comment-author {
  font-weight: 500;
  color: #1f2f3d;
}
.comment-actions {
  display: flex;
  gap: 15px;
  font-size: 13px;
  color: #909399;
}
.comment-actions span {
  cursor: pointer;
  display: flex;
  align-items: center;
}
.comment-actions span:hover {
  color: #409eff;
}
.comment-text {
  font-size: 14px;
  line-height: 1.6;
  color: #606266;
  margin-bottom: 10px;
}

/* 回复输入区域 */
.reply-input-area {
  margin-top: 10px;
}
.reply-actions {
  margin-top: 10px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 加载更多 */
.loading-more, .no-more {
  text-align: center;
  padding: 15px;
  color: #909399;
  font-size: 14px;
}
.loading-more {
  display: flex;
  align-items: center;
  justify-content: center;
}

.author-info {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}
.author-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #409eff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-right: 12px;
}

.aside-header {
  padding: 15px 0;
  border-bottom: 1px solid #eee;
  margin-bottom: 15px;
}

.back-button {
  justify-content: flex-start; /* 左对齐 */
  border-color: #dcdfe6; /* 更柔和的边框颜色 */
  color: #606266; /* 文字颜色 */
  margin-bottom: 15px;
}

.back-button:hover {
  border-color: #409eff; /* 悬停时显示主题色 */
  color: #409eff;
  background-color: #f5f7fa;
}

.reply-to {
  font-size: 12px;
  color: #909399;
  margin-bottom: 8px;
}

.teacher-badge {
  background: #409EFF;
  color: white;
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 4px;
  margin-left: 8px;
}
</style>