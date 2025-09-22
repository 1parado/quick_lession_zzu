
<template>
  <div class="common-layout">
    <el-container>
      <el-header style="margin-top: -7px">
        <div style="
            display: flex;
            justify-content: flex-end;
            align-items: center;
            height: 100%;
            width: 100%;
            padding-right: 20px;
  ">
          <div class="btn" @click="router.push('/aiChat')">问问AI</div>&nbsp;&nbsp;&nbsp;&nbsp;
          <div class="btn" @click="router.push('/forumHomepage')">论坛主页</div>&nbsp;&nbsp;&nbsp;&nbsp;
          <div class="btn" @click="router.push('/forumHomepage/forumCourseList')">课程列表</div>&nbsp;&nbsp;&nbsp;&nbsp;
          <div class="btn" @click="publishPost()">我要发帖</div>&nbsp;&nbsp;&nbsp;&nbsp;
          <div class="btn" @click="router.push('/')">退出论坛</div>&nbsp;&nbsp;&nbsp;&nbsp;
        </div>
      </el-header>

      <el-container>
        <el-aside width="400px"></el-aside>
        <el-main >
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>


<script setup>

import router from "../../router.js";
import {ElMessage} from "element-plus";

/**
 * 我要发贴按钮
 */
const publishPost = () => {
  // 判断当前用户是否是教师
  if (sessionStorage.getItem('role') === 'TEACHER') {
    ElMessage.warning("抱歉，根据相关规定，您无发布权限")
    return;
  }

  router.push('/forumHomepage/forumPublish');
}

</script>


<style scoped>
/* 悬停效果 */
.btn {
  display: inline-block;
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.btn:hover {
  color: #409eff;
  background-color: rgba(64, 158, 255, 0.1); /* 添加背景色变化 */
}

.el-header {
  background-color: #f8f9fa; /* 浅灰色背景 */
  border-bottom: 1px solid #eaeaea; /* 底部边框线 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05); /* 轻微阴影 */
}
</style>