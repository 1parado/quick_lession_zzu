<template>
  <div class="login-container">
    <el-container style="height: 100vh; overflow: hidden;">
      <el-header class="header">
        <div class="header-content">校园管理系统</div>
      </el-header>
      <el-container class="main-container">
        <el-aside width="60%" class="left-section">
          <img
              src="\src\assets\login.jpeg"
              alt="校园场景"
              class="school-image"
          >
        </el-aside>
        <el-main class="right-section">
          <div class="right-content">
            <div class="welcome-texts">
              <h1>欢迎登录</h1>
              <p>学生一站式服务大厅</p>
            </div>
            <div class="form-container">
              <el-form
                  ref="loginFormRef"
                  :model="loginForm"
                  :rules="rules"
                  label-width="80px"
                  class="login-form"
              >
                <el-form-item label="账号" prop="account">
                  <el-input
                      v-model="loginForm.account"
                      placeholder="请输入账号"
                      prefix-icon="el-icon-user"
                  />
                </el-form-item>
                <el-form-item label="密码" prop="password">
                  <el-input
                      v-model="loginForm.password"
                      type="password"
                      placeholder="请输入密码"
                      prefix-icon="el-icon-lock"
                      show-password
                  />
                </el-form-item>

                <el-form-item class="form-actions">
                  <el-button
                      type="primary"
                      @click="handleLogin()"
                      class="login-btn"
                  >
                    登录
                  </el-button>
                  <el-button
                      @click="resetForm()"
                      class="reset-btn"
                  >
                    重置
                  </el-button>
                </el-form-item>
              </el-form>
            </div>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>


<script setup>
import axios from '../utils/http.js';
import router from "../router.js";
import {ref} from "vue";
import { reactive } from 'vue'


// 定义响应式表单对象数据
//ref函数，将一个普通 JavaScript 值（如字符串、数字、对象等）转换为响应式数据
//使 Vue 能够追踪它的变化，并在数据变化时自动更新视图
const loginForm = ref({
  account: '',  // 绑定用户名输入框
  password: ''   // 绑定密码输入框
});

//async 异步ajax请求
const handleLogin = async () => {
  try {
    //await 等待结果返回
    const response = await axios.post('/auth/login', loginForm.value);
    if (response.data.code === 200) {
      console.log(response)
      sessionStorage.setItem('token',response.data.data.token);
      localStorage.setItem('token',response.data.data.token);

      //解析token，拿到role
      const base64Url = response.data.data.token.split('.')[1]
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
      const jsonPayload = decodeURIComponent(
          atob(base64)
              .split('')
              .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
              .join('')
      )
      console.log(JSON.parse(jsonPayload));//这个{sub: '20221614001', iat: 1754142603, exp: 1754229003, role: 'STUDENT'}
      sessionStorage.setItem('role',JSON.parse(jsonPayload).role);
      localStorage.setItem('role',JSON.parse(jsonPayload).role);
      sessionStorage.setItem('account',JSON.parse(jsonPayload).sub);
      localStorage.setItem('account',JSON.parse(jsonPayload).sub);

      console.log(sessionStorage.getItem('token'))
      //路由跳转
      router.push('/');
    } else {
      console.log(response)
      window.alert('用户名或密码错误')
    }
  } catch (err) {
    err.value = '用户名或密码错误';
    window.alert(err);
  }
};

// 验证规则
const rules = reactive({
  account: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
})

const resetForm = () => {
  loginForm.value.account = '';
  loginForm.value.password = '';
}
</script>

<style scoped>
/* 全局滚动控制 */
html, body, #app {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
  overflow: hidden !important;
}

.login-container {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB',
  'Microsoft YaHei', Arial, sans-serif;
  height: 96vh;
  width: 99vw;
  overflow: hidden !important;
}

/* Header样式 */
.header {
  background-color: #ffffff;
  color: #409eff;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  border-bottom: 1px solid #e6e6e6;
  height: 60px;
  line-height: 60px;
  position: fixed;
  width: 100%;
  z-index: 100;
}

.header-content {
  font-size: 20px;
  font-weight: bold;
}

/* 主内容区 - 修复滚动问题 */
.main-container {
  height: calc(100vh - 60px);
  width: 100vw;
  margin-top: 60px;
  overflow: hidden !important;
  display: flex;
}

/* 左侧区域 - 完全禁用滚动 */
.left-section {
  background-color: #f8fafc;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden !important;
  height: 100%;
  padding: 0 !important;  /* 移除内边距 */
  margin: 0 !important;   /* 移除外边距 */
  border: none !important;
  outline: none !important;
  box-shadow: none !important;
}

.school-image {
  display: block;        /* 消除图片底部间隙 */
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center center; /* 确保图片居中显示 */
}

/* 右侧区域 - 精确控制滚动 */
.right-section {
  padding: 0;
  height: 100%;
  overflow: hidden !important;
}

.right-content {
  height: 100%;
  width: 100%;
  overflow-y: auto; /* 只允许垂直滚动 */
  overflow-x: hidden !important; /* 禁用水平滚动 */
  padding: 0px;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #ffffff;
}

/* 欢迎文字 */
.welcome-texts {
  text-align: center;
  margin-bottom: 70px;
  width: 100%;
  max-width: 400px;
}

.welcome-texts h1 {
  font-size: 38px;
  color: #303133;
  margin-bottom: 15px;
  font-weight: 600; /* 加粗 */
}

.welcome-texts p {
  font-size: 28px;
  color: #909399;
  font-weight: 500; /* 中等加粗 */
}

/* 表单容器 */
.form-container {
  background: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  width: 100%;
  max-width: 450px; /* 稍微加宽 */
}

/* 其他样式保持不变 */
.login-form {
  margin-top: 20px;
}

.form-actions {
  margin-top: 30px;
  display: flex;
  justify-content: space-between;
}

.login-btn {
  width: 48%;
  background-color: #409eff;
  border-color: #409eff;
}

.reset-btn {
  width: 48%;
}

:deep(.el-input__inner) {
  height: 40px;
  line-height: 40px;
  border-color: #dcdfe6;
}

:deep(.el-form-item__label) {
  color: #606266;
  font-weight: normal;
}

:deep(.el-button) {
  height: 40px;
}
</style>