
<template>
  <el-table :data="announcementTableData" stripe style="width: 100%" v-if="announcementTableData.length > 0">
    <el-table-column prop="publicName" label="发布人" wixdth="180" />
    <el-table-column label="删除时间" wixdth="180" >
      <template #default="scope">
        <!-- 使用之前定义的 formatTime 函数格式化时间 -->
        {{ formatTime(scope.row.publishTime) }}
      </template>
    </el-table-column>
    <el-table-column prop="title" label="公告题目" wixdth="180" />
    <el-table-column fixed="right" label="操作" min-width="180" align="center">
      <template #default="scope">
        <el-button link type="primary" @click="detailAnnouncement(scope.row)">详情</el-button>
        <el-button link type="primary" @click="reAnnouncement(scope.row.id)">恢复</el-button>
        <el-button link type="primary" @click="deleteAnnouncement(scope.row.id)">彻底删除</el-button>
      </template>
    </el-table-column>
  </el-table >
  <!-- 分页条 -->
  <el-pagination
      :current-page="currentPage"
      :page-size="pageSize"
      :total="total"
      layout="prev, pager, next"
      @current-change="handlePageChange"
      style="margin-top: 20px; justify-content: center;"
      v-if="announcementTableData.length > 0"
  />
  <el-empty :image-size="200" v-else style="margin-top: 175px" description='暂无已删除公告'/>

  <!-- 详情页弹窗 -->
  <el-dialog v-model="AnnouncementDetailDialogVisible" title="公告详细信息" width="1000">
    <div>
      <el-form :model="announcementData" label-position="left" label-width="80px" :disabled="true">
        <el-form-item label="发布人" >
          <el-input v-model="announcementData.publicName" />
        </el-form-item>
        <el-form-item label="删除时间" >
          <el-input :value="formatTime(announcementData.publishTime)"  />
        </el-form-item>
        <el-form-item label="公告标题" >
          <el-input v-model="announcementData.title" />
        </el-form-item>
        <el-form-item label="公告内容" >
          <el-input type='textarea' v-model="announcementData.content" />
        </el-form-item>
        <el-form-item label="公告状态" >
          <el-input v-model="announcementData.status" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button type="primary" @click="AnnouncementDetailDialogVisible = false">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>

</template>



<script setup>

import {ElMessage, ElPagination} from "element-plus";
import {onMounted, ref, watch} from "vue";
import axios from '../../utils/http.js';
import {useRoute} from "vue-router";

//公告表格数据
const announcementTableData = ref([{
  publicName: '',
  publishTime: '',
  title: ''
}])


//一个公告的信息
const announcementData = ref()

//分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

//决定是否弹出公告详情页
const AnnouncementDetailDialogVisible = ref(false);


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
 * 初始化加载数据
 */
onMounted(() => {
  fetchData();
})

const route = useRoute()  // 获取当前路由信息
watch(
    () => route.query.t,  // 监听路由中t参数的变化
    (newValue) => {
      if (newValue) {  // 如果t有值（说明是新提交后跳转过来的）
        fetchData()  // 重新加载数据
      }
    }
)

/**
 * 获取数据
 */
const fetchData = async () => {
  try {
    const response = await axios.get('/ann/announcement/teacher/' + 0 + '/' + sessionStorage.getItem('account'), {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })
    console.log(response);
    announcementTableData.value = response.data.data;
    total.value = Number(response.data.message);
    console.log("Total:", total.value, "Type:", typeof total.value);
  } catch (error) {
    console.error('获取数据失败:', error);
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
 * 彻底删除公告
 * @param id
 */
const deleteAnnouncement = async (id) => {
  try {
    //先询问是否删除
    //彻底删除
    if (window.confirm('是否彻底删除')) {
      const response = await axios.delete('/ann/announcement/true/' + id)
      console.log(response);
      if (response.data.code === 200) {
        ElMessage.success("删除成功")
        fetchData();
      } else {
        ElMessage.error("删除失败")
      }
    }
  } catch (error) {
    console.log(error)
  }
}

/**
 * 恢复公告到草稿箱子
 * @param id
 */
const reAnnouncement = async (id) => {
  try {
    //先询问是否恢复
    //到草稿状态
    if (window.confirm('是否恢复')) {
      const response = await axios.post('/ann/announcement/re/' + id)
      console.log(response);
      if (response.data.code === 200) {
        ElMessage.success("恢复成功")
        fetchData();
      } else {
        ElMessage.error("恢复失败")
      }
    }
  } catch (error) {
    console.log(error)
  }
}



/**
 * 详情,打开弹窗
 * @param rowData
 */
const detailAnnouncement = (rowData) => {
  //打开一个弹窗，用studentTableData传数据
  AnnouncementDetailDialogVisible.value = true;
  console.log(rowData)
  announcementData.value = rowData
}

</script>

<style scoped>

</style>