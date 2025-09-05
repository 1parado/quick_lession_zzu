
<template>
  <el-table :data="announcementTableData" stripe style="width: 100%" v-if="announcementTableData.length > 0" >
    <el-table-column prop="publicName" label="编辑人" wixdth="180" />
    <el-table-column label="预计发布时间" wixdth="180" >
      <template #default="scope">
        <!-- 使用之前定义的 formatTime 函数格式化时间 -->
        {{ formatTime(scope.row.publishTime) }}
      </template>
    </el-table-column>
    <el-table-column prop="title" label="公告题目" wixdth="180" />
    <el-table-column fixed="right" label="操作" min-width="180" align="center">
      <template #default="scope">
        <el-button link type="primary" @click="detailAnnouncement(scope.row)">详情</el-button>
        <el-button link type="primary" @click="updateAnnouncement(scope.row)">修改</el-button>
        <el-button link type="primary" @click="deleteAnnouncement(scope.row.id)">删除</el-button>
        <el-button type="primary" plain @click="sendAnnouncement(scope.row)">发布</el-button>
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
      v-if="announcementTableData.length > 0"
  />

  <el-empty :image-size="200" v-else style="margin-top: 175px" description='暂无草稿箱公告'/>

  <!-- 详情页弹窗 -->
  <el-dialog v-model="AnnouncementDetailDialogVisible" title="公告详细信息" width="1000">
    <div>
      <el-form :model="announcementData" label-position="left" label-width="100px" :disabled="true">
        <el-form-item label="编辑人" >
          <el-input v-model="announcementData.publicName" />
        </el-form-item>
        <el-form-item label="预计发布时间" >
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

  <!-- 修改页弹窗 -->
  <el-dialog v-model="AnnouncementDetailUpdateVisible" title="修改公告信息" width="1000">
    <div>
      <el-form :model="announcementData" :rules="rules" ref="updateFormRef" label-position="left" label-width="100px" >
        <el-form-item label="编辑人" prop="publicName" >
          <el-input v-model="announcementData.publicName" disabled />
        </el-form-item>
        <el-form-item label="预计发布时间" prop="publishTime"  >
          <el-date-picker
              style="width: 1000px"
              v-model="announcementData.publishTime"
              type="datetime"
              placeholder="选择发布时间"
              :disabled-date="disabledDate"
          />
        </el-form-item>
        <el-form-item label="公告标题" prop="title" >
          <el-input v-model="announcementData.title" />
        </el-form-item>
        <el-form-item label="公告内容" prop="content" >
          <el-input v-model="announcementData.content" />
        </el-form-item>
        <el-form-item label="公告状态" prop="status" >
          <el-input v-model="announcementData.status" disabled />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="AnnouncementDetailUpdateVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="updateAnnouncementRel()">
          修改
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

// 禁用今天之前的日期
const disabledDate = (time) => {
  return time.getTime() < Date.now() - 86400000
}

//一个公告的信息
const announcementData = ref()

//分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

//决定是否弹出公告详情页
const AnnouncementDetailDialogVisible = ref(false);
//决定是否弹出公告修改页
const AnnouncementDetailUpdateVisible = ref(false);

// 定义表单引用
const updateFormRef = ref(null)

// 验证规则
const rules = {
  publishTime: [
  ],
  content: [
    { required: true, message: '请输入公告内容', trigger: 'blur' }
  ]
}

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
    const response = await axios.get('/ann/announcement/teacher/' + 1 + '/' + sessionStorage.getItem('account'), {
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
 * 删除公告
 * @param id
 */
const deleteAnnouncement = async (id) => {
  try {
    //先询问是否删除
    //到已删除状态
    if (window.confirm('是否删除')) {
      const response = await axios.delete('/ann/announcement/' + id)
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
 * 发布公告
 */
const sendAnnouncement = async (rowData) => {
  announcementData.value = rowData;
  try {
    //先询问是否发布
    //到已发布状态
    if (window.confirm('是否发布')) {
      const response = await axios.put('/ann/announcement/send', announcementData.value)
      console.log(response);
      if (response.data.code === 200) {
        ElMessage.success("发布成功")
        fetchData();
      } else {
        ElMessage.error("发布失败")
      }
    }
  } catch (error) {
    console.log(error)
  }
}

/**
 * 修改，打开弹窗
 * @param rowData
 */
const updateAnnouncement = (rowData) => {
  AnnouncementDetailUpdateVisible.value = true;
  announcementData.value = rowData
}

/**
 * 修改，真正修改
 */
const updateAnnouncementRel = async () => {
  //将studentData打包送给后端
  // 验证表单
  await updateFormRef.value.validate()

  try {
    const response = await axios.put('/ann/announcement', announcementData.value)
    console.log(response);
    if (response.data.code === 200) {
      ElMessage.success("更新成功")
      fetchData();
      AnnouncementDetailUpdateVisible.value = false;
    } else {
      ElMessage.error("更新失败")
      fetchData();
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