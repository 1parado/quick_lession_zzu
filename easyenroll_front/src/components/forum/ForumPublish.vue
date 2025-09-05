<template>
  <div class="common-layout">
    <el-container>
      <el-main>
        <div class="post-container">
          <h2 class="post-title">发布新帖子</h2>
          <el-form :model="postData" label-width="120px" class="post-form">
            <el-form-item label="帖子标题">
              <el-input
                  v-model="postData.title"
                  placeholder="请输入帖子标题"
                  maxlength="100"
                  show-word-limit
              />
            </el-form-item>

            <el-form-item label="关联课程">
              <el-select
                  v-model="selectedCourse"
                  filterable
                  remote
                  reserve-keyword
                  placeholder="请输入课程名称或代码搜索"
                  :remote-method="searchCourses"
                  :loading="courseLoading"
                  @change="handleCourseSelect"
                  style="width: 100%"
                  value-key="courseCode"
              >
                <el-option
                    v-for="course in courseList"
                    :key="course.courseCode"
                    :label="`${course.name} (${course.courseCode})`"
                    :value="course"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="课程评分">
              <el-rate v-model="postData.rating" :colors="['#99A9BF', '#F7BA2A', '#FF9900']" />
            </el-form-item>

            <!-- 添加课程负载评分 -->
            <el-form-item label="">
              <div class="load-rating-container">
                <el-button
                    v-if="selectedCourse"
                    type="primary"
                    size="small"
                    @click="predictCourseLoad"
                    :loading="predictingLoad"
                    class="predict-btn"
                >
                  {{ predictingLoad ? '预测中...' : 'AI智能评估' }}
                </el-button>

                <!-- AI预测结果提示 -->
                <div v-if="aiSuggestion" class="ai-suggestion">
                  <span class="suggestion-text">
                    AI建议: {{ aiSuggestion.score }}分
                    <span class="confidence">(置信度: {{ (aiSuggestion.confidence * 100).toFixed(1) }}%)</span>
                  </span>
                  <el-button
                      type="success"
                      size="mini"
                      @click="applyAISuggestion"
                      class="apply-btn"
                  >
                    采用建议
                  </el-button>
                </div>
              </div>
            </el-form-item>

            <el-form-item label="帖子内容">
              <el-input
                  v-model="postData.content"
                  type="textarea"
                  :rows="8"
                  placeholder="请输入帖子内容，AI将根据内容智能评估课程负载"
                  maxlength="2000"
                  show-word-limit
                  style="height: 380px"
                  @input="onContentInput"
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="submitPost" :loading="submitting">
                {{ submitting ? '发布中...' : '发布帖子' }}
              </el-button>
              <el-button @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-main>
      <el-aside width="400px">
        <div class="aside-panel">
          <h3>发帖指南</h3>
          <ul>
            <li>标题应简洁明了，概括帖子主要内容</li>
            <li>选择相关课程有助于他人找到您的帖子</li>
            <li>评分应客观公正，基于您的真实体验</li>
            <li>内容应详细具体，避免模糊表述</li>
            <li>请遵守社区规则，文明发帖</li>
          </ul>

          <!-- 添加AI功能说明 -->
          <div class="ai-feature-hint">
            <h4>AI智能评估功能</h4>
            <p>• 输入课程相关内容后，AI会自动分析课程负载</p>
            <p>• 点击"AI智能评估"按钮获取负载评分建议</p>
            <p>• 负载评分范围: 1分(最高负载) - 5分(最低负载)</p>
          </div>
        </div>
      </el-aside>
    </el-container>
  </div>
</template>

<script setup>
import {ref, onMounted, onUnmounted} from "vue";
import { ElMessage } from 'element-plus'
import axios from '../../utils/http.js'
import { debounce } from 'lodash'; // 引入lodash的debounce

// 帖子数据
const postData = ref({
  title: '',
  mentionedCourseName: '',
  mentionedCourseCode: '',
  content: '',
  publishName: '',
  publishSno: '',
  publishTime: '',
  commentCount: 0,
  type: 0,
  rating: 0,
  courseLoadRating: 0 // 添加课程负载评分字段
})

const selectedCourse = ref(null)
const courseList = ref([])
const courseLoading = ref(false)
const submitting = ref(false)
const predictingLoad = ref(false)
const aiSuggestion = ref(null)
let timeoutId = null

// 获取课程列表
const fetchCourses = async () => {
  courseLoading.value = true
  try {
    const response = await axios.get('/cou/course/all')
    if (response.data.code === 200) {
      courseList.value = response.data.data
    } else {
      ElMessage.error("获取课程列表数据失败")
    }
  } catch (error) {
    console.error('获取课程列表失败:', error)
    ElMessage.error('获取课程列表失败')
  } finally {
    courseLoading.value = false
  }
}

// 课程选择处理
const handleCourseSelect = (course) => {
  if (course) {
    postData.value.mentionedCourseName = course.name
    postData.value.mentionedCourseCode = course.courseCode
    selectedCourse.value = course
  } else {
    postData.value.mentionedCourseName = ''
    postData.value.mentionedCourseCode = ''
    selectedCourse.value = null
  }
  // 清空AI建议
  aiSuggestion.value = null
}

// 内容输入处理（防抖）
const onContentInput = () => {
  // 清除之前的定时器
  if (timeoutId) {
    clearTimeout(timeoutId)
  }

  // 设置新的定时器，1秒后自动预测
  timeoutId = setTimeout(() => {
    if (postData.value.content.length > 20 && selectedCourse.value) {
      predictCourseLoad()
    }
  }, 1000)
}

// AI预测课程负载
const predictCourseLoad = async () => {
  if (!selectedCourse.value) {
    ElMessage.warning('请先选择关联课程')
    return
  }

  if (postData.value.content.length < 20) {
    ElMessage.warning('请输入更多课程相关内容（至少20字）')
    return
  }

  predictingLoad.value = true
  try {
    const response = await axios.post('/ai/predict-course-load', {
      content: postData.value.content
    })

    if (response.data.code === 200) {
      aiSuggestion.value = response.data.data
      ElMessage.success('AI评估完成')
    }
  } catch (error) {
    console.error('AI预测失败:', error)
    ElMessage.error('AI评估失败，请稍后重试')
  } finally {
    predictingLoad.value = false
  }
}

// 应用AI建议
const applyAISuggestion = () => {
  if (aiSuggestion.value) {
    postData.value.rating = aiSuggestion.value.score
    ElMessage.info('已采用AI建议评分')
  }
}

// 重置表单
const resetForm = () => {
  postData.value = {
    title: '',
    mentionedCourseName: '',
    mentionedCourseCode: '',
    content: '',
    rating: 0,
    courseLoadRating: 0,
    publishName: '',
    publishSno: sessionStorage.getItem('account'),
    publishTime: '',
    commentCount: 0,
    type: 0
  }
  selectedCourse.value = null
  aiSuggestion.value = null
  if (timeoutId) {
    clearTimeout(timeoutId)
    timeoutId = null
  }
}

// 提交帖子
const submitPost = async () => {
  // 简单验证
  if (!postData.value.title.trim()) {
    ElMessage.warning('请输入帖子标题')
    return
  }

  if (!postData.value.content.trim()) {
    ElMessage.warning('请输入帖子内容')
    return
  }

  // 如果选择了课程但未评分
  if (selectedCourse.value && postData.value.rating === 0) {
    ElMessage.warning('请为关联的课程评分')
    return
  }

  submitting.value = true
  try {
    postData.value.publishSno = sessionStorage.getItem('account')
    const response = await axios.post('/page/page/post/publish', postData.value)

    if (response.data.code === 200) {
      ElMessage.success('帖子发布成功')
      resetForm()
    } else if (response.data.message === '不允许重复评价同一课程') {
      ElMessage.error("不允许重复评价同一课程")
    } else {
      ElMessage.error("发布失败")
    }
  } catch (error) {
    console.error('发布帖子失败:', error)
    ElMessage.error('帖子发布失败，请重试')
  } finally {
    submitting.value = false
  }
}

// 组件卸载时清除定时器
onUnmounted(() => {
  if (timeoutId) {
    clearTimeout(timeoutId)
  }
})

onMounted(() => {
  fetchCourses()
})
</script>

<style scoped>
.common-layout {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.post-container {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.post-title {
  margin-top: 0;
  margin-bottom: 24px;
  color: #303133;
  font-weight: 500;
}

.post-form {
  max-width: 800px;
}

.load-rating-container {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.predict-btn {
  margin-left: 10px;
}

.ai-suggestion {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
  padding: 8px 12px;
  background-color: #f0f9ff;
  border-radius: 4px;
  border-left: 3px solid #409eff;
}

.suggestion-text {
  font-size: 14px;
  color: #606266;
}

.confidence {
  font-size: 12px;
  color: #909399;
}

.apply-btn {
  margin-left: auto;
}

.aside-panel {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  margin-left: 20px;
  height: fit-content;
}

.aside-panel h3 {
  margin-top: 0;
  color: #303133;
  border-bottom: 1px solid #EBEEF5;
  padding-bottom: 10px;
}

.aside-panel ul {
  padding-left: 20px;
  color: #606266;
}

.aside-panel li {
  margin-bottom: 10px;
  line-height: 1.5;
}

.ai-feature-hint {
  margin-top: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  border-left: 3px solid #67c23a;
}

.ai-feature-hint h4 {
  margin-top: 0;
  margin-bottom: 10px;
  color: #303133;
  font-size: 14px;
}

.ai-feature-hint p {
  margin: 5px 0;
  font-size: 13px;
  color: #606266;
  line-height: 1.4;
}

:deep(.el-textarea__inner) {
  resize: vertical;
  min-height: 120px;
}

:deep(.el-rate) {
  margin-top: 8px;
}
</style>