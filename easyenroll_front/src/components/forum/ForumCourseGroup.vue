<template>

  <div class="common-layout">
    <el-container>
      <el-main>
        <div class="course-group-container">
          <div class="header">
            <h2>专属课程小组</h2>
            <div>课程代码: {{ courseCode }}</div>
          </div>

          <div class="message-board">
            <!-- 发布留言区域 -->
            <div class="post-message">
              <el-input
                  v-model="newMessage"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入留言内容..."
                  maxlength="500"
                  show-word-limit
              ></el-input>
              <div class="post-actions">
                <el-button
                    type="primary"
                    @click="postMessage"
                    :disabled="!newMessage.trim()"
                >
                  发布留言
                </el-button>
              </div>
            </div>

            <!-- 留言列表 -->
            <div class="messages-list">
              <div v-if="messages.length === 0" class="empty-state">
                <p>暂无留言，快来发表第一条吧~</p>
              </div>

              <div v-else class="message-item" v-for="message in messages" :key="message.id" :class="{'teacher-message': message.type === 0 }" >
                <div class="message-header">
                  <span class="user-avatar">{{ message.name.slice(0, 1) }}</span>
                  <div class="user-info">
                    <span class="user-name">{{ message.name }}({{ message.account }})
                      <span v-if="message.type === 0" class="teacher-badge">教师</span>
                    </span>
                    <span class="message-time">{{ formatTime(message.createTime) }}</span>
                  </div>
                  <el-button
                      v-if="Number(currentUser) === message.account"
                      type="danger"
                      size="small"
                      @click="deleteMessage(message.id)"
                      class="delete-btn"
                  >
                    删除
                  </el-button>
                </div>
                <div class="message-content">{{ message.content }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-main>
      <el-aside width="400px"></el-aside>
    </el-container>
  </div>


</template>

<script setup>
import {ref, watch, onMounted, nextTick} from 'vue'
import {useRoute} from 'vue-router'
import {ElMessage, ElLoading} from 'element-plus'
import axios from "../../utils/http.js"

const route = useRoute()
const courseCode = ref('')
const dataInitialized = ref(false)
const messages = ref([])
const newMessage = ref('')
const currentUser = ref(sessionStorage.getItem('account') || '')

// 格式化时间显示
const formatTime = (timeString) => {
  if (!timeString) return ''
  const date = new Date(timeString)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
}

// 获取留言数据
const fetchData = async () => {
  const loading = ElLoading.service({
    lock: true,
    text: '加载中...',
    background: 'rgba(0, 0, 0, 0.7)',
  })

  try {
    // 这里需要根据你的API调整
    const response = await axios.get(`/message/message/${courseCode.value}`)
    if (response.data.code === 200) {
      messages.value = response.data.data
    } else {
      ElMessage.error("获取留言数据失败")
    }
  } catch (error) {
    ElMessage.error("获取留言数据失败: " + error.message)
  } finally {
    loading.close()
  }
}

// 发布新留言
const postMessage = async () => {
  if (!newMessage.value.trim()) return

  try {
    // 这里需要根据你的API调整
    await axios.post('/message/message', {
      courseCode: courseCode.value,
      content: newMessage.value.trim(),
      account: currentUser.value
    })

    ElMessage.success('留言发布成功')
    newMessage.value = ''
    fetchData() // 重新加载留言列表
  } catch (error) {
    ElMessage.error("发布留言失败: " + error.message)
  }
}

watch(
    () => route.query.code,
    (newValue) => {
      if (newValue) {
        try {
          courseCode.value = JSON.parse(newValue)
          dataInitialized.value = true
          fetchData()
        } catch (e) {
          ElMessage.error("数据解析失败：" + e)
        }
      }
    },
    {immediate: true}
)

onMounted(() => {
  if (dataInitialized.value) {
    fetchData()
  } else {
    nextTick(() => {
      if (dataInitialized.value) {
        fetchData()
      }
    })
  }
})

// 删除留言
const deleteMessage = async (messageId) => {
  try {
    const response = await axios.delete(`/message/message/${messageId}`)
    if (response.data.code === 200) {
      ElMessage.success('留言删除成功')
      fetchData() // 重新加载留言列表
    } else {
      ElMessage.error("留言删除失败")
    }
  } catch (error) {
    ElMessage.error("删除留言失败: " + error.message)
  }
}
</script>

<style scoped>
.course-group-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.header {
  text-align: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.header h2 {
  margin: 0 0 10px 0;
  color: #333;
}

.post-message {
  margin-bottom: 30px;
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.post-actions {
  margin-top: 15px;
  text-align: right;
}

.messages-list {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.empty-state {
  padding: 40px 20px;
  text-align: center;
  color: #999;
}

.message-item {
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.message-item:last-child {
  border-bottom: none;
}

/* 教师留言特殊样式 */
.message-item.teacher-message {
  background-color: #f0f8ff;
  border-left: 4px solid #409EFF;
}

.teacher-badge {
  background: #409EFF;
  color: white;
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 4px;
  margin-left: 8px;
}

/* 教师头像样式 */
.teacher-avatar {
  background-color: #ff9900;
}

.message-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  justify-content: space-between; /* 让内容分布在两端 */
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #409EFF;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-right: 12px;
}

.user-info {
  display: flex;
  flex-direction: column;
  flex-grow: 1; /* 占据剩余空间 */
}

.user-name {
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.message-time {
  font-size: 12px;
  color: #999;
}

.message-content {
  color: #333;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-word;
}

.delete-btn {
  margin-left: 10px;
}
</style>