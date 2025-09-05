<template>
  <div class="common-layout">
    <el-container>
      <el-aside width="200px" :style="{ width: sidebarWidth }">
        <div>
          <el-radio-group v-model="isCollapse" style="margin-bottom: 20px">
            <el-radio-button :value=!isCollapse :style="{display: !isCollapse ? 'none' : 'inline-block'}" >展开</el-radio-button>
            <el-radio-button :value=!isCollapse :style="{display: isCollapse ? 'none' : 'inline-block'}">折叠</el-radio-button>
          </el-radio-group>
          <el-menu
              default-active="2"
              class="el-menu-vertical-demo"
              :collapse="isCollapse"
              @open="handleOpen"
              @close="handleClose"
          >
            <el-sub-menu index="1" v-if="(role === 'ADMIN')  ? true : false">
              <template #title>
                <el-icon><Suitcase /></el-icon>
                <span>OA系统</span>
              </template>
              <!--       <el-menu-item-group>
                     <template #title><span>学生管理</span></template>
                     </el-menu-item-group>-->
              <el-menu-item index="1-1" @click="router.push('studentManage')"><el-icon><Mouse /></el-icon>学生管理</el-menu-item>
              <el-menu-item index="1-2" @click="router.push('teacherManage')"><el-icon><Mouse /></el-icon>教师管理</el-menu-item>
              <el-menu-item index="1-3" @click="router.push('courseManage')"><el-icon><Mouse /></el-icon>课程管理</el-menu-item>
              <el-menu-item index="1-4" @click="router.push('announcementManage')"><el-icon><Mouse /></el-icon>公告管理</el-menu-item>
              <el-menu-item index="1-5" @click="router.push('taskManage')"><el-icon><Mouse /></el-icon>选课任务管理</el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="2" v-if="(role === 'STUDENT')  ? true : false ">
              <template #title>
                <el-icon><Memo /></el-icon>
                <span>教务系统</span>
              </template>
              <el-sub-menu index="2-1">
                <template #title><span><el-icon><Mouse /></el-icon>学生选课中心</span></template>
                <el-menu-item index="2-1-1" @click="router.push('selectCourseCenter')">选课</el-menu-item>
                <el-menu-item index="2-1-2" @click="router.push('selectCourseSeckill')">限时选课</el-menu-item>
              </el-sub-menu>
              <el-menu-item index="2-2" @click="router.push('courseTable')"><el-icon><Mouse /></el-icon>个人课表</el-menu-item>
              <el-menu-item index="2-3" @click="router.push('examStudentPlan')"><el-icon><Mouse /></el-icon>考试安排</el-menu-item>
              <el-menu-item index="2-4" @click="router.push('studentGrade')"><el-icon><Mouse /></el-icon>个人成绩</el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="3" v-if="(role === 'ADMIN')  ? true : false ">
              <template #title>
                <el-icon><Memo /></el-icon>
                <span>教务系统（管理端）</span>
              </template>
              <el-menu-item index="3-1" @click="router.push('adminSelectCourse')"><el-icon><Mouse /></el-icon>学生选课查询</el-menu-item>
              <el-menu-item index="3-2" @click="router.push('adminSelectManage')"><el-icon><Mouse /></el-icon>学生选课管理</el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="4" v-if="(role === 'TEACHER')  ? true : false ">
              <template #title>
                <el-icon><Memo /></el-icon>
                <span>教务系统（教师端）</span>
              </template>
              <el-menu-item index="4-1" @click="router.push('teacherCourseTable')"><el-icon><Mouse /></el-icon>个人课表</el-menu-item>
              <el-menu-item index="4-2" @click="router.push('teacherCourseManage')"><el-icon><Mouse /></el-icon>课程管理</el-menu-item>
              <el-menu-item index="4-3" @click="router.push('teacherAnnouncementManage')"><el-icon><Mouse /></el-icon>公告管理</el-menu-item>
              <el-menu-item index="4-4" @click="router.push('examManage')"><el-icon><Mouse /></el-icon>考试管理</el-menu-item>
              <el-menu-item index="4-5" @click="router.push('examGradeManage')"><el-icon><Mouse /></el-icon>成绩管理</el-menu-item>
            </el-sub-menu>

            <el-menu-item index="5" @click="router.push('studentPayment')" v-if="(role === 'STUDENT')  ? true : false ">
              <el-icon><Magnet /></el-icon>
              <template #title>缴费系统（学生端）</template>
            </el-menu-item>

            <el-menu-item index="6" @click="router.push('adminPayment')" v-if="(role === 'ADMIN')  ? true : false ">
              <el-icon><Magnet /></el-icon>
              <template #title>缴费系统（管理端）</template>
            </el-menu-item>

          </el-menu>

        </div>
      </el-aside>
      <el-container>
        <el-header :style="{left: sidebarWidth}">
          <div style="
            display: flex;
            justify-content: flex-end;
            align-items: center;
            height: 100%;
            width: 100%;
            padding-right: 20px;
  ">
            <el-button type="primary" plain @click="router.push('/forumHomepage')" style="margin-right: 20px" v-if="(role === 'ADMIN')  ? false : true " >进入论坛</el-button>

            <div class="btn" @click="router.push('/')">回到主页</div>&nbsp;&nbsp;&nbsp;&nbsp;

            <el-popover
                placement="bottom"
                trigger="hover"
                :width="150"
            >
              <template #reference>
                <div class="btn">{{ name }}</div>
              </template>

              <div style="text-align: center; padding: 5px 0;">
                <el-button
                    type="text"
                    style="width: 100%; padding: 8px 0;"
                    @click="showInfo()"
                >
                  个人中心
                </el-button>
                <el-divider style="margin: 5px 0" />
                <el-button
                    type="text"
                    style="width: 100%; padding: 8px 0;"
                    @click="PasswordDialogVisible = true"
                >
                  修改密码
                </el-button>
              </div>
            </el-popover>

            &nbsp;&nbsp;&nbsp;&nbsp;
            <div class="btn" @click="logout()">退出登录</div>
          </div>
        </el-header>

        <el-main :style="{ left: sidebarWidth }">
          <router-view></router-view>
        </el-main>

      </el-container>
    </el-container>
  </div>

  <!-- 点击姓名弹出学生个人信息弹出 -->
  <el-dialog v-model="StudentDialogVisible" title="个人信息" width="1000">
    <div>
      <el-form :model="info" label-position="left" label-width="80px" :disabled="true">
        <el-form-item label="姓名" >
          <el-input v-model="info.name" />
        </el-form-item>
        <el-form-item label="学号" >
          <el-input v-model="info.sno" />
        </el-form-item>
        <el-form-item label="学院" >
          <el-input v-model="info.college" />
        </el-form-item>
        <el-form-item label="专业" >
          <el-input v-model="info.major" />
        </el-form-item>
        <el-form-item label="性别" >
          <el-input v-model="info.gender" />
        </el-form-item>
        <el-form-item label="年龄" >
          <el-input v-model="info.age" />
        </el-form-item>
        <el-form-item label="学分" >
          <el-input v-model="info.credit" />
        </el-form-item>
        <el-form-item label="联系方式" >
          <el-input v-model="info.phone" />
        </el-form-item>
        <el-form-item label="入学时间" >
          <el-input v-model="info.inputTime" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="isUpdatePhone = true">
          更改联系方式
        </el-button>
        <el-button type="primary" @click="StudentDialogVisible = false">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 点击姓名弹出教师个人信息弹出 -->
  <el-dialog v-model="TeacherDialogVisible" title="个人信息" width="500">
    <div>
      <el-form :model="info" label-position="left" label-width="80px" :disabled="true">
        <el-form-item label="姓名" >
          <el-input v-model="info.name" />
        </el-form-item>
        <el-form-item label="工号" >
          <el-input v-model="info.tno" />
        </el-form-item>
        <el-form-item label="学院" >
          <el-input v-model="info.college" />
        </el-form-item>
        <el-form-item label="职称" >
          <el-input v-model="info.title" />
        </el-form-item>
        <el-form-item label="性别" >
          <el-input v-model="info.gender" />
        </el-form-item>
        <el-form-item label="年龄" >
          <el-input v-model="info.age" />
        </el-form-item>
        <el-form-item label="联系方式" >
          <el-input v-model="info.phone" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="TeacherDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="isUpdatePhone = true">
          更改联系方式
        </el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 点击按钮“更改联系方式”弹出 -->
  <el-dialog v-model="isUpdatePhone" title="新的联系方式" width="400" align-center  >
    <div>
      <!--<el-form :model="newPhone" label-position="left" label-width="0px" >-->
      <el-form
          ref="phoneFormRef"
          style="max-width: 600px"
          :model="newPhone"
          status-icon
          :rules="phoneRules"
          label-width="auto"
          class="demo-ruleForm"
      >
        <el-form-item prop="phone">
          <el-input v-model="newPhone.phone" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="isUpdatePhone = false">
          取消
        </el-button>
        <el-button type="primary" @click="updatePhone()">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 点击按钮“修改密码”弹出 -->
  <el-dialog v-model="PasswordDialogVisible" title="新的密码" width="400" align-center  >
    <div>
      <el-form
          ref="passwordFormRef"
          style="max-width: 600px"
          :model="newPassword"
          status-icon
          label-width="auto"
          class="demo-ruleForm"
      >
        <el-form-item>
          <el-input v-model="newPassword.password" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="PasswordDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="updatePassword()">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>




</template>

<script lang="ts" setup>
import {computed, onMounted, reactive, ref} from 'vue'
import axios from '../utils/http.js';
import router from "../router";
import {ElMessage, FormRules} from "element-plus";


// 组件挂载时自动加载数据
onMounted( () => {
  fetchData()
  fetchStudent()
})

const account = sessionStorage.getItem('account');
const role = sessionStorage.getItem('role');
const token = sessionStorage.getItem('token');
const name = ref('');

//决定是否弹出个人信息弹框
const StudentDialogVisible = ref(false)
const TeacherDialogVisible = ref(false)
//决定是否弹出更改联系方式弹窗
const isUpdatePhone = ref(false);
//决定是否弹出更改密码弹窗
const PasswordDialogVisible = ref(false);

//从后端查到的个人信息（教师或者学生）
const info = ref([]);

//新手机号
const newPhone = ref({
  phone: ''
});

//新密码
const newPassword = ref({
  password: ''
})

//联系方式表单的引用
const phoneFormRef = ref(null) // 添加这行

//密码表单的引用
const passwordFormRef = ref(null) // 添加这行

// 验证规则
const phoneRules = {
  phone: [
    {
      pattern: /^1[3-9]\d{9}$/,  // 匹配中国大陆手机号（11位，1开头，第二位3-9）
      message: '请输入正确的手机号',
      trigger: 'blur'
    }
  ],
}
/**
 *  更新手机号
 */
const updatePhone = async () => {
  try {
    console.log(newPhone.value)
    console.log(newPhone.value.phone)
    //发送手机号到后端去修改DB
    //如果验证不通过，不去发送
    await phoneFormRef.value.validate()

    const response = await axios.post('/auth/user/updatePhone/' + account + '/' + role, newPhone.value.phone);
    console.log(response)
    if (response.data.code === 200) {
      ElMessage.success("更新成功")
      fetchData();
      newPhone.value.phone = '';
      isUpdatePhone.value = false;
    } else {
      ElMessage.error("更新失败")
      newPhone.value.phone = '';
    }
  } catch (error) {
    ElMessage.error("格式有误")
    return
  }
}

/**
 *  修改密码
 */
const updatePassword = async () => {
  try {
    //发送密码到后端去修改DB
    console.log(newPassword.value.password)
    const response = await axios.put('/auth/user/updatePassword/' + account + '/' + role, newPassword.value.password,{
      headers: {
        'Content-Type': 'text/plain'  // 明确指定内容类型
      }
    });
    console.log(response)
    if (response.data.code === 200) {
      ElMessage.success("更新成功")
      fetchData();
      newPassword.value.password = '';
      PasswordDialogVisible.value = false;
    } else {
      ElMessage.error("更新失败")
      newPassword.value.password = '';
    }
  } catch (error) {
    ElMessage.error("error")
    return
  }
}

/**
 * 退出登录
 */
const logout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('role')
  router.push('/login');
}

/**
 * 展示个人信息
 */
const showInfo = () => {
  if (role === 'STUDENT') {
    StudentDialogVisible.value = true;
  } else if (role === 'TEACHER') {
    TeacherDialogVisible.value = true;
  }
}

/**
 * 初始化
 */
const fetchData = async () => {
  try {
    //把角色和账号一同发回
    const response =  await axios.get('/auth/user/' + account + '/' + role);
    console.log(response)
    if (response.data.code === 200) {
      //显示到页面上
      name.value = response.data.data.name;
      info.value = response.data.data;
      console.log('info.value')
      console.log(info.value)
    } else {
      console.error('API 返回错误:', response.data.message);
    }
  } catch (error) {
    console.error('请求失败:', error);
    name.value = '未知用户'; // 提供默认值
  }
}

const fetchStudent = async () => {
  try {
    const response = await axios.get('/stu/student/getid/' + sessionStorage.getItem('account'))
    console.log(response.data.data);
    if (response.data.code === 200) {
      sessionStorage.setItem('studentId', response.data.data)
      localStorage.setItem('studentId', response.data.data)
    } else {
      console.log("获取学生id失败");
    }
  } catch (error) {
    console.error('获取学生id失败:', error);
  }
}


//侧边栏折叠状态
const isCollapse = ref(false)
const sidebarWidth = computed(() => isCollapse.value ? '64px' : '200px')
const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
</script>

<style>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
/* 悬停效果 */
.btn {
  display: inline-block;
  cursor: pointer;  /* 手型指针 */
}
.btn:hover {
  color: #409eff;  /* 悬停变蓝色 */
}
</style>