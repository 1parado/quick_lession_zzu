<template>
  <el-button type="primary" plain @click="openInsertDialog()">新增公告</el-button>
  <br/>
  <br/>
  <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
    <el-tab-pane label="最新公告" name="ALL"></el-tab-pane>
    <el-tab-pane label="已发布" name="PUBLISHED"></el-tab-pane>
    <el-tab-pane label="草稿箱" name="DRAFT"></el-tab-pane>
    <el-tab-pane label="已删除" name="DELETED"></el-tab-pane>
  </el-tabs>
  <div><router-view></router-view></div>

  <el-dialog v-model="AnnouncementInsertDialogVisible" title="新增公告" width="1000">
    <div>
      <el-form :model="announcementData" :rules="rules" ref="insertFormRef" label-position="left" label-width="80px" >
        <el-form-item label="标题" prop="title" >
          <el-input v-model="announcementData.title" placeholder="请输入标题，可不填" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="announcementData.content" placeholder="请输入内容，必填"/>
        </el-form-item>
        <el-form-item label="发布时间" prop="publishTime">
          <el-date-picker
              style="width: 1000px"
              v-model="announcementData.publishTime"
              type="datetime"
              placeholder="选择发布时间"
              :disabled-date="disabledDate"
          />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="AnnouncementInsertDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="sendAnnouncement()">
          发送
        </el-button>
        <el-button type="primary" @click="insertAnnouncement()">
          保存到草稿箱
        </el-button>
      </div>
    </template>
  </el-dialog>



</template>

<script lang="ts" setup>
import {onMounted, ref, watch} from 'vue'
import router from "../router";
import axios from '../utils/http.js';

import {ElMessage, TabsPaneContext} from 'element-plus'

const role = sessionStorage.getItem('role');
const account = sessionStorage.getItem('account');

const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab);
  console.log(tab.paneName)
  // 根据标签name跳转对应路由
  switch(tab.paneName) {
    case 'ALL':
      router.push('/teacherAnnouncementManage')  // 跳转到已发布页面路由
      break
    case 'PUBLISHED':
      router.push('publishedTeacher')  // 跳转到已发布页面路由
      break
    case 'DRAFT':
      router.push('draftTeacher')      // 跳转到草稿箱页面路由
      break
    case 'DELETED':
      router.push('deletedTeacher')    // 跳转到已删除页面路由
      break
  }
}

//一个公告的信息
const announcementData = ref();

// 专门用于新增的初始化方法
const initAnnouncementForCreate = () => {
  announcementData.value = {
    title: '',
    content: '',
    publishTime: ''
  }
}


// 禁用今天之前的日期
const disabledDate = (time: Date) => {
  return time.getTime() < Date.now() - 86400000
}

//决定是否弹出公告增加页
const AnnouncementInsertDialogVisible = ref(false);

// 修改新增按钮的点击事件
const openInsertDialog = () => {
  AnnouncementInsertDialogVisible.value = true
  initAnnouncementForCreate() // 仅在这里初始化
}

const insertFormRef = ref(null)

// 验证规则
const rules = {
  title: [
  ],
  content: [
    { required: true, message: '请输入内容', trigger: 'blur' }
  ],
  publishTime: [//发布时间不必须，默认立即发送，可定时发送
  ]
}

/**
 * 初始化activeName时，根据当前路由匹配对应标签
 *
 */
const getActiveNameByRoute = () => {
  const path = router.currentRoute.value.path
  if (path.includes('draft')) return 'DRAFT'
  if (path.includes('published')) return 'PUBLISHED'
  if (path.includes('deleted')) return 'DELETED'
  return 'ALL' // 默认选中"全部公告"
}

const activeName = ref(getActiveNameByRoute())

// 监听路由变化，自动更新activeName
watch(
    () => router.currentRoute.value.path,
    (newPath) => {
      activeName.value = getActiveNameByRoute()
    }
)

/**
 * 新增
 * 发布则新增跳转到已发布页面
 * 保存则跳转到草稿箱页面
 */
const insertAnnouncement = async () => {
  // 验证表单
  try {
    await insertFormRef.value.validate()
  } catch (error) {
    window.alert("格式有误")
    return;
  }
  try {
    const response = await axios.post('/ann/announcement/'+ account + '/' + 1, announcementData.value,  {
      headers: {
        'Content-Type': 'application/json'
      }
    })
    console.log(response);
    if (response.data.code === 200){
      ElMessage.success("保存成功")
      AnnouncementInsertDialogVisible.value = false;
      //router.push('draft');
      router.push({ path: 'draftTeacher', query: { t: Date.now() } });
    } else {
      ElMessage.error("保存失败")
    }
  } catch (error) {
    console.log(error)
  }
}

const sendAnnouncement = async () => {
  try {
    await insertFormRef.value.validate()
  } catch (error) {
    window.alert("格式有误")
    return;
  }
  try {
    // 创建发送数据的副本
    const sendData = {
      ...announcementData.value,
      //... 创建了 announcementData.value 的浅拷贝
      // 覆盖publishTime为当前时间
      publishTime: new Date().toISOString() // 或者使用其他你需要的日期格式
    }

    const response = await axios.post('/ann/announcement/'+ account + '/' + 2, sendData,  {
      headers: {
        'Content-Type': 'application/json'
      }
    })
    console.log(response);
    if (response.data.code === 200){
      ElMessage.success("发布成功")
      AnnouncementInsertDialogVisible.value = false;
      //router.push('published');
      router.push({ path: 'publishedTeacher', query: { t: Date.now() } });
    } else {
      ElMessage.error("发布失败")
    }
  } catch (error) {
    console.log(error)
  }
}


</script>

<style>
.demo-tabs > .el-tabs__content {
  padding: 5px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
</style>