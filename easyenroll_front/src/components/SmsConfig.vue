<template>

  <el-button type="primary" plain @click="openInsertTemplateDialog()" style="margin-bottom: 50px">新增模板</el-button><br/>
  <el-button type="primary" plain @click="openTeacherTemplateDialog()" style="margin-bottom: 50px">选择教师短信模板</el-button><br/>
  <el-button type="primary" plain @click="openStudentTemplateDialog()" style="margin-bottom: 150px">选择学生短信模板</el-button><br/>

  <el-button type="primary" plain @click="DraftAnnouncementDialogVisible = true" style="margin-bottom: 150px">设置短信提前通知时间</el-button><br/>

  自动短信功能是否启用 <el-switch v-model="smsSwitch" @change="smsSwitchChange()" />

  <!-- 新增模板窗口 -->
  <el-dialog v-model="InsertTemplateDialogVisible" title="新增模板" width="1000">
    <div>
      <el-form
          :model="templateData"
          label-position="left"
          :rules="rules"
          ref="insertFormRef"
          label-width="80px" >

        <el-form-item label="模板类型" prop="type">
          <el-select v-model="templateData.type" placeholder="请选择模板类型" style="width: 100%">
            <el-option label="教师模板" value="TeacherTemplate" />
            <el-option label="学生模板" value="StudentTemplate" />
          </el-select>
        </el-form-item>

        <el-form-item label="模板内容" prop="component">
          <el-input type="textarea" v-model="templateData.component" />
        </el-form-item>

        <el-form-item label="内容提醒">
          <el-input type="textarea" readonly v-model="pointComponent">
          </el-input>
        </el-form-item>

      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="InsertTemplateDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="insertTemplate()">
          新增
        </el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 学生模板选择窗口 -->
  <el-dialog v-model="StudentTemplateDialogVisible" title="学生模板选择" width="1000">
    <div>
      <el-table :data="studentTemplateTableData" stripe style="width: 100%" v-if="studentTemplateTableData.length > 0">
        <el-table-column prop="component" label="模板内容" wixdth="180" />
        <el-table-column prop="status" label="选中状态" wixdth="180" >
          <template #default="{ row }">
            <span :style="{ color: row.status === 1 ? 'green' : 'gray' }">
              {{ row.status === 1 ? "已选中" : "未选中" }}
            </span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" min-width="180" align="center">
          <template #default="scope">
            <el-button link type="primary" @click="selectStudentTemplate(scope.row)">选择</el-button>
            <el-button link type="primary" @click="openUpdateStudentTemplateDialogVisible(scope.row)">修改</el-button>
            <el-button link type="primary" @click="deleteStudentTemplate(scope.row.id)" >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty :image-size="200" v-else style="margin-top: 0px"/>
    </div>
  </el-dialog>

  <!-- 教师模板选择窗口 -->
  <el-dialog v-model="TeacherTemplateDialogVisible" title="教师模板选择" width="1000">
    <div>
      <el-table :data="teacherTemplateTableData" stripe style="width: 100%" v-if="teacherTemplateTableData.length > 0">
        <el-table-column prop="component" label="模板内容" wixdth="180" />
        <el-table-column prop="status" label="选中状态" wixdth="180" >
          <template #default="{ row }">
            <span :style="{ color: row.status === 1 ? 'green' : 'gray' }">
              {{ row.status === 1 ? "已选中" : "未选中" }}
            </span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" min-width="180" align="center">
          <template #default="scope">
            <el-button link type="primary" @click="selectTeacherTemplate(scope.row)">选择</el-button>
            <el-button link type="primary" @click="openUpdateTeacherTemplateDialogVisible(scope.row)">修改</el-button>
            <el-button link type="primary" @click="deleteTeacherTemplate(scope.row.id)" >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty :image-size="200" v-else style="margin-top: 0px"/>
    </div>
  </el-dialog>

  <!-- 学生模板修改窗口 -->
  <el-dialog v-model="UpdateStudentTemplateDialogVisible" title="修改学生模板" width="1000">
    <div>
      <el-form
          :model="templateData"
          label-position="left"
          label-width="80px" >

        <el-form-item label="模板类型" prop="type">
          <el-input v-model="templateData.type" readonly />
        </el-form-item>

        <el-form-item label="模板内容" prop="component">
          <el-input type="textarea" v-model="templateData.component" />
        </el-form-item>

        <el-form-item label="内容提醒">
          <el-input type="textarea" readonly v-model="pointComponent">
          </el-input>
        </el-form-item>

      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="UpdateStudentTemplateDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="updateStudentTemplate(templateData)">
          修改
        </el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 教师模板修改窗口 -->
  <el-dialog v-model="UpdateTeacherTemplateDialogVisible" title="修改教师模板" width="1000">
    <div>
      <el-form
          :model="templateData"
          label-position="left"
          label-width="80px" >

        <el-form-item label="模板类型" prop="type">
          <el-input v-model="templateData.type" readonly />
        </el-form-item>

        <el-form-item label="模板内容" prop="component">
          <el-input type="textarea" v-model="templateData.component" />
        </el-form-item>

        <el-form-item label="内容提醒">
          <el-input type="textarea" readonly v-model="pointComponent">
          </el-input>
        </el-form-item>

      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="UpdateTeacherTemplateDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="updateTeacherTemplate(templateData)">
          修改
        </el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 草稿箱公告表格窗口 -->
  <el-dialog v-model="DraftAnnouncementDialogVisible" title="公告草稿箱" width="1000">
    <div>
      <el-table :data="announcementTableData" stripe style="width: 100%" v-if="announcementTableData.length > 0">
        <el-table-column prop="publicName" label="编辑人" wixdth="180" />
        <el-table-column label="预计发布时间" wixdth="180" >
          <template #default="scope">
            <!-- 使用之前定义的 formatTime 函数格式化时间 -->
            {{ formatTime(scope.row.publishTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="title" label="公告题目" wixdth="180" />
        <!-- 提前发布时间 -->
        <el-table-column label="提前通知时间" prop="time"/>
        <el-table-column fixed="right" label="操作" min-width="180" align="center">
          <template #default="scope">
            <el-button link type="primary" @click="SetTimeDialogVisible = true; currentRow = scope.row; newTime.time = scope.row.time || 0">设置</el-button>
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
      <el-empty :image-size="200" v-else style="margin-top: 0px"/>
    </div>
  </el-dialog>

  <!-- 修改提前发布时间的窗口 -->
  <el-dialog v-model="SetTimeDialogVisible" title="设置提前通知时间" width="400" align-center  >
    <div>
      <el-form
          ref="timeFormRef"
          style="max-width: 600px"
          :model="newTime"
          status-icon
          :rules="timeRules"
          label-width="auto"
          class="demo-ruleForm"
      >
        <el-form-item prop="time">
          <el-input v-model="newTime.time" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="SetTimeDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="setTime()">
            确认
        </el-button>
      </div>
    </template>
  </el-dialog>

</template>


<script setup>

import {onMounted, ref} from "vue";
import axios from "../utils/http.js"
import {ElMessage, ElPagination} from "element-plus";

// 短信功能的开关
const smsSwitch = ref(true)

//设置提前发布时间时的当前行
const currentRow = ref();

//一个模板数据
const templateData = ref();

//一个学生模板表格数据
const studentTemplateTableData = ref([{
  type: '',
  component: ''
}]);

//一个教师模板表格数据
const teacherTemplateTableData = ref([{
  type: '',
  component: ''
}]);

//提醒数据
const pointComponent = ref("{{name}}表示短信接收者；\n" +
    "{{time}}表示短信发布时间；\n" +
    "{{}}")

//公告表格数据
const announcementTableData = ref([{
  publicName: '',
  publishTime: '',
  title: '',
  time: ''
}])

//新的提前发布时间
const newTime = ref({
  time: 0
});

//分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 决定是否弹出新增模板窗口
const InsertTemplateDialogVisible = ref(false);
// 决定是否弹出学生模板窗口
const StudentTemplateDialogVisible = ref(false);
// 决定是否弹出教师模板窗口
const TeacherTemplateDialogVisible = ref(false);
// 决定是否弹出修改学生模板窗口
const UpdateStudentTemplateDialogVisible = ref(false);
// 决定是否弹出修改教师模板窗口
const UpdateTeacherTemplateDialogVisible = ref(false);
// 决定是否弹出草稿箱公告窗口
const DraftAnnouncementDialogVisible = ref(false);
//决定是否弹出设置提前发布时间的窗口
const SetTimeDialogVisible = ref(false);

//定义新增表单引用
const insertFormRef = ref(null)
//设置提前发布时间表单的引用
const timeFormRef = ref(null) // 添加这行


// 验证规则
const timeRules = {
  time: [
    { required: true, message: "时间必须填写", trigger: "blur" },
    {
      validator: (rule, value, callback) => {
        const numValue = Number(value);
        if (isNaN(numValue)){
          callback(new Error("必须是数字"));
        } else if (!Number.isInteger(numValue)) {
          callback(new Error("必须是整数"));
        } else if (numValue < 0 || numValue > 120) {
          callback(new Error("必须介于 0 和 120 之间"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
}
// 验证规则
const rules = {
  type: [
    { required: true, message: '请选择是否需要抢选', trigger: 'blur' }
  ]
};

/**
 * 页码变化处理
 * @param page
 */
const handlePageChange = (page) => {
  currentPage.value = page
  fetchDraftData()
}

/**
 * 定义时间格式化函数
 * @param time
 * @returns {string}
 */
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
 * onMounted
 */
onMounted(() => {
  fetchStudentData()
  fetchTeacherData()
  fetchDraftData()
})

/**
 * 初始化草稿箱公告数据
 */
/**
 * 获取数据
 */
const fetchDraftData = async () => {
  try {
    const response = await axios.get('/template/template/draft', {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })
    if (response.data.code === 200) {
      console.log(response);
      announcementTableData.value = response.data.data;
      total.value = Number(response.data.message);
      console.log("Total:", total.value, "Type:", typeof total.value);
    }
  } catch (error) {
    console.error('获取数据失败:', error);
  }
}

/**
 * 初始化学生模板数据
 */
const fetchStudentData = async () => {
  try {
    const studentResponse = await axios.get('/template/template/student')
    if (studentResponse.data.code === 200) {
      studentTemplateTableData.value = studentResponse.data.data;
    } else {
      ElMessage.error("学生模板数据获取失败")
    }
  }catch (e) {
    ElMessage.error(e);
  }
}

/**
 * 初始化教师模板数据
 */
const fetchTeacherData = async () => {
  try {
    const teacherResponse = await axios.get('/template/template/teacher')
    if (teacherResponse.data.code === 200) {
      teacherTemplateTableData.value = teacherResponse.data.data;
    } else {
      ElMessage.error("学生模板数据获取失败")
    }
  }catch (e) {
    ElMessage.error(e);
  }
}

/**
 * 打开新增模板窗口
 */
const openInsertTemplateDialog = () => {
  InsertTemplateDialogVisible.value = true;
  init();
}

/**
 * 打开教师模板窗口
 */
const openTeacherTemplateDialog = () => {
  TeacherTemplateDialogVisible.value = true;
}

/**
 * 打开学生模板窗口
 */
const openStudentTemplateDialog = () => {
  StudentTemplateDialogVisible.value = true;
}

/**
 * 新增模板
 */
const insertTemplate = async () => {
  try {
    const response = await axios.post('/template/template', templateData.value, {
      headers: {
        'Content-Type': 'application/json'
      }
    })
    if (response.data.code === 200) {
      ElMessage.success("新增成功")
      fetchStudentData();
      fetchTeacherData();
    } else {
      ElMessage.error("新增失败")
    }
  } catch (e) {
    ElMessage.error(e);
  }
}

/**
 * 专门用于新增的初始化方法
 */
const init = () => {
  templateData.value = {
    type: '',
    status: 0,
    component: ''
  }
}

/**
 * 选中学生模板
 */
//若未选中则使用按id排序第一个合适的模板
const selectStudentTemplate = async (row) => {
  studentTemplateTableData.value.forEach(item => {
    item.status = 0;
  });
  row.status = 1;

  //发送请求，改变选中状态
  try {
    const response = await axios.put('/template/template/student/status', row)
    if (response.data.code === 200) {
      ElMessage.success("选中成功")
      fetchStudentData()
    } else {
      ElMessage.error("选中失败");
    }
  } catch (e) {
    ElMessage.error(e);
  }
}

/**
 * 选中教师模板
 */
const selectTeacherTemplate = async (row) => {
  teacherTemplateTableData.value.forEach(item => {
    item.status = 0;
  });
  row.status = 1;
  //发送请求，改变选中状态
  try {
    const response = await axios.put('/template/template/teacher/status', row)
    if (response.data.code === 200) {
      ElMessage.success("选中成功")
      fetchTeacherData()
    } else {
      ElMessage.error("选中失败");
    }
  } catch (e) {
    ElMessage.error(e);
  }
}

/**
 * 打开学生模板修改窗口
 */
const openUpdateStudentTemplateDialogVisible = (row) => {
  templateData.value = row;
  UpdateStudentTemplateDialogVisible.value = true;
}

/**
 * 打开教师模板修改窗口
 */
const openUpdateTeacherTemplateDialogVisible = (row) => {
  templateData.value = row;
  UpdateTeacherTemplateDialogVisible.value = true;
}

/**
 * 修改学生模板
 */
const updateStudentTemplate = async (row) => {
  try {
    const response = await axios.put('/template/template/student', row)
    if (response.data.code === 200) {
      ElMessage.success("修改成功")
      fetchStudentData()
    } else {
      ElMessage.error("修改失败");
    }
  } catch (e) {
    ElMessage.error(e);
  }
}

/**
 * 修改教师模板
 */
const updateTeacherTemplate = async (row) => {
  try {
    const response = await axios.put('/template/template/teacher', row)
    if (response.data.code === 200) {
      ElMessage.success("修改成功")
      fetchTeacherData()
    } else {
      ElMessage.error("修改失败");
    }
  } catch (e) {
    ElMessage.error(e);
  }
}

/**
 * 删除学生模板
 */
const deleteStudentTemplate = async (id) => {
  try {
    const response = await axios.delete('/template/template/student/' + id)
    if (response.data.code === 200) {
      ElMessage.success("删除成功")
      fetchStudentData()
    } else {
      ElMessage.error("删除失败");
    }
  } catch (e) {
    ElMessage.error(e);
  }
}

/**
 * 删除教师模板
 */
const deleteTeacherTemplate = async (id) => {
  try {
    const response = await axios.delete('/template/template/teacher/' + id)
    if (response.data.code === 200) {
      ElMessage.success("删除成功")
      fetchTeacherData()
    } else {
      ElMessage.error("删除失败");
    }
  } catch (e) {
    ElMessage.error(e);
  }
}

/**
 * 设置提前发布时间
 */
const setTime = async () => {
  try {
    // 验证表单
    await timeFormRef.value.validate();
    console.log(currentRow)
    const response = await axios.put('/template/template/time/' + newTime.value.time, currentRow.value)
    if (response.data.code === 200) {
      currentRow.value.time = newTime.value.time;
      ElMessage.success("设置成功")
      SetTimeDialogVisible.value = false;
      fetchDraftData();
    }
  } catch (e) {
    ElMessage.error(e)
  }
}

/**
 * 启用与禁用自动短信功能
 */
const smsSwitchChange = async () => {
  console.log(smsSwitch.value)
  try {
    const response = await axios.get('/template/template/smsSwitch/' + smsSwitch.value)
    if (response.data.code === 200) {
      ElMessage.success("设置成功")
    } else {
      ElMessage.error("设置失败")
    }
  } catch (e) {
    ElMessage.error(e);
  }
}

</script>



<style scoped>

</style>