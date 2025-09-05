<template>
  <div class="leaderboard-container">
    <el-card class="leaderboard-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <h2>🏆 排行榜</h2>
          <el-button type="primary" :icon="Refresh" @click="refreshData">刷新数据</el-button>
        </div>
      </template>

      <!-- 顶部前三名展示 -->
      <div v-if="leaderboard.length >= 3" class="top-three">
        <div class="top-item second">
          <div class="crown">🥈</div>
          <div class="top-content">
            <span class="rank">2</span>
            <div class="info">
              <div class="name">{{ leaderboard[1].name }}</div>
              <div class="score">{{ leaderboard[1].score }}分</div>
            </div>
          </div>
        </div>

        <div class="top-item first">
          <div class="crown">🥇</div>
          <div class="top-content">
            <span class="rank">1</span>
            <div class="info">
              <div class="name">{{ leaderboard[0].name }}</div>
              <div class="score">{{ leaderboard[0].score }}分</div>
            </div>
          </div>
        </div>

        <div class="top-item third">
          <div class="crown">🥉</div>
          <div class="top-content">
            <span class="rank">3</span>
            <div class="info">
              <div class="name">{{ leaderboard[2].name }}</div>
              <div class="score">{{ leaderboard[2].score }}分</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 排行榜列表 -->
      <el-table :data="leaderboard" stripe style="width: 100%" class="leaderboard-table">
        <el-table-column type="index" label="排名" width="80">
          <template #default="scope">
            <span v-if="scope.$index < 3" class="top-rank">{{ scope.$index + 1 }}</span>
            <span v-else>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名" width="120"></el-table-column>
        <el-table-column prop="score" label="分数" sortable width="100">
          <template #default="scope">
            <el-tag :type="getScoreTagType(scope.row.score)">
              {{ scope.row.score }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="学号">
          <template #default="scope">
            {{ scope.row.value }}
          </template>
        </el-table-column>
      </el-table>

      <!-- 个人排名信息 -->
      <el-card class="personal-card" shadow="never">
        <div class="personal-info">
          <h3>我的排名</h3>
          <div class="stats">
            <div class="stat-item">
              <div class="label">排名</div>
              <div class="value" :class="{ 'highlight': myData.rank > 0 }">
                {{ myData.rank > 0 ? `第${myData.rank}名` : '未上榜' }}
              </div>
            </div>
            <div class="stat-item">
              <div class="label">分数</div>
              <div class="value" :class="{ 'highlight': myData.score }">
                {{ myData.score || '暂无' }}
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from '../utils/http.js';
import { ElMessage } from "element-plus";
import { Refresh } from '@element-plus/icons-vue'

const leaderboard = ref([]);
const myData = ref({
  rank: -1,
  score: null,
})

onMounted(async () => {
  fetchData();
  fetchOne();
});

const fetchData = async () => {
  //获取topN
  try {
    const response = await axios.get("/leaderboard/leaderboard/topN/" + 10);
    if (response.data.code === 200) {
      leaderboard.value = response.data.data;
    } else {
      ElMessage.error("获取排行榜失败");
    }
  } catch (e) {
    ElMessage.error("获取排行榜失败:" + e);
  }
}

const fetchOne = async () => {
  try {
    const response = await axios.get("/leaderboard/leaderboard/" + sessionStorage.getItem('account'));
    if (response.data.code === 200) {
      myData.value = {
        rank: Number(response.data.data.rank) || -1,
        score: response.data.data.score
      };
      console.log('我的数据:', myData.value);
    } else {
      ElMessage.error("获取排行榜失败");
    }
  } catch (e) {
    ElMessage.error("获取自身排行榜数据失败")
  }
}

const refreshData = () => {
  fetchData();
  fetchOne();
  ElMessage.success('数据已刷新');
}

const getScoreTagType = (score) => {
  if (score >= 90) return 'success';
  if (score >= 80) return 'warning';
  return 'danger';
}
</script>

<style scoped>
.leaderboard-container {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.leaderboard-card {
  border-radius: 12px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  margin: 0;
  color: #303133;
}

/* 前三名样式 */
.top-three {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 30px;
  padding: 20px 0;
}

.top-item {
  width: 180px;
  padding: 20px;
  border-radius: 12px;
  text-align: center;
  transition: transform 0.3s ease;
}

.top-item:hover {
  transform: translateY(-5px);
}

.first {
  background: linear-gradient(135deg, #fff9e6 0%, #ffedb3 100%);
  order: 2;
  box-shadow: 0 4px 12px rgba(255, 197, 0, 0.2);
}

.second {
  background: linear-gradient(135deg, #f0f0f0 0%, #e0e0e0 100%);
  order: 1;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.third {
  background: linear-gradient(135deg, #f9f0e6 0%, #e9d5b9 100%);
  order: 3;
  box-shadow: 0 4px 12px rgba(205, 127, 50, 0.2);
}

.crown {
  font-size: 32px;
  margin-bottom: 10px;
}

.top-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.rank {
  font-size: 24px;
  font-weight: bold;
  color: #606266;
  margin-bottom: 8px;
}

.info .name {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 5px;
  color: #303133;
}

.info .score {
  font-size: 18px;
  font-weight: bold;
  color: #409EFF;
}

/* 表格样式 */
.leaderboard-table {
  margin: 20px 0;
}

.leaderboard-table :deep(.el-table__row) {
  transition: background-color 0.3s;
}

.leaderboard-table :deep(.el-table__row:hover) {
  background-color: #f5f7fa !important;
}

.top-rank {
  display: inline-block;
  width: 24px;
  height: 24px;
  line-height: 24px;
  text-align: center;
  border-radius: 50%;
  background-color: #409EFF;
  color: white;
  font-weight: bold;
}

/* 个人卡片样式 */
.personal-card {
  margin-top: 20px;
  background-color: #f9fafc;
  border: 1px solid #e6e8eb;
}

.personal-info h3 {
  margin: 0 0 15px 0;
  color: #303133;
  font-size: 18px;
}

.stats {
  display: flex;
  gap: 30px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.value {
  font-size: 20px;
  font-weight: bold;
  color: #606266;
}

.value.highlight {
  color: #409EFF;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .top-three {
    flex-direction: column;
    align-items: center;
  }

  .top-item {
    width: 100%;
    max-width: 300px;
  }

  .first {
    order: 1;
  }

  .stats {
    flex-direction: column;
    gap: 15px;
  }
}
</style>