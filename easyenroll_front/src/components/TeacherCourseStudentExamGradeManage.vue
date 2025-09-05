
<template>

  <el-breadcrumb :separator-icon="ArrowRight">
    <el-breadcrumb-item :to="{ path: '/' }">主页</el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: '/examGradeManage'}">成绩管理</el-breadcrumb-item>
    <el-breadcrumb-item>学生列表</el-breadcrumb-item>
  </el-breadcrumb>

  <el-dropdown
      ref="dropdown"
      :visible="dropdownVisible"
      @visible-change="handleVisibleChange"
      trigger="click"
      :hide-on-click="false"
  >
    <el-button type="primary" plain  style="margin-left: 1500px;margin-bottom: 0px;margin-top: 20px" >
      导入学生列表<el-icon class="el-icon--right"><arrow-down />
    </el-icon></el-button>
    <template #dropdown>
      <el-dropdown-menu
          style="width: 300px; padding: 10px;"
          @click.stop
      >
        <el-dropdown-item @click="downloadTemplate" @click.stop>
          <el-button type="primary" style="width: 100%">下载导入模板</el-button>
        </el-dropdown-item>
        <el-dropdown-item @click.stop>
          <el-upload
              ref="upload"
              class="upload-dropdown"
              :action="uploadUrl"
              :limit="1"
              :on-exceed="handleExceed"
              :auto-upload="false"
              :show-file-list="true"
              :on-change="handleFileChange"
              :file-list="fileList"
              accept=".xlsx,.xls"
              @click.stop
          >
            <template #trigger>
              <el-button
                  type="primary"
                  style="width: 100%; margin-bottom: 10px"
                  @click.stop="handleSelectFile"
              >
                选择文件
              </el-button>
            </template>
            <div style="margin-top: 10px; text-align: center;">
              <el-button
                  type="success"
                  @click.stop="handleImport"
                  style="width: 100%"
              >
                导入学生数据
              </el-button>
            </div>
            <template #tip>
              <div class="el-upload__tip" style="font-size: 12px; color: #909399; margin-top: 10px;">
                仅支持.xlsx/.xls格式文件，每次只能上传一个文件
              </div>
            </template>
          </el-upload>
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>

  <el-table :data="studentTableData" stripe style="width: 100%; margin-top: 30px" v-if="studentTableData.length > 0">
    <el-table-column prop="name" label="姓名" wixdth="180" />
    <el-table-column prop="sno" label="学号" wixdth="180" />
    <el-table-column prop="grade" label="成绩" wixdth="180" />
    <el-table-column prop="state" label="状态">
      <template #default="scope">
        <span :class="'status-' + getStatusClass(scope.row.state)">
          {{ getStatusText(scope.row.state) }}
        </span>
      </template>
    </el-table-column>
    <el-table-column fixed="right" label="操作" min-width="180" align="center">
      <template #default="scope">
        <el-button link type="primary" @click="detailStudent(scope.row)">详情</el-button>
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
      v-if="studentTableData.length > 0"
  />
  <el-empty :image-size="200" v-else style="margin-top: 175px" description='暂无数据'/>


  <!-- 详情页弹窗 -->
  <el-dialog v-model="StudentDetailDialogVisible" title="学生详情信息" width="1000">
    <div>
      <el-form :model="studentData" label-position="left" label-width="80px" :disabled="true">
        <el-form-item label="姓名" >
          <el-input v-model="studentData.name" />
        </el-form-item>
        <el-form-item label="学号" >
          <el-input v-model="studentData.sno" />
        </el-form-item>
        <el-form-item label="学院" >
          <el-input v-model="studentData.college" />
        </el-form-item>
        <el-form-item label="专业" >
          <el-input v-model="studentData.major" />
        </el-form-item>
        <el-form-item label="性别" >
          <el-input v-model="studentData.gender" />
        </el-form-item>
        <el-form-item label="年龄" >
          <el-input v-model="studentData.age" />
        </el-form-item>
        <el-form-item label="学分" >
          <el-input v-model="studentData.credit" />
        </el-form-item>
        <el-form-item label="联系方式" >
          <el-input v-model="studentData.phone" />
        </el-form-item>
        <el-form-item label="入学时间" >
          <el-input v-model="studentData.inputTime" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button type="primary" @click="StudentDetailDialogVisible = false">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>

</template>


<script lang="ts" setup>
import {onMounted, ref} from "vue";
import axios from '../utils/http.js';
import {ElMessage, ElPagination, genFileId, type UploadProps, type UploadRawFile} from 'element-plus';
import {ArrowRight} from "@element-plus/icons-vue";
import {useRoute} from "vue-router";

const route = useRoute();
const courseId = route.query.id; // 获取 id
console.log(courseId)

//学生表格数据
const studentTableData = ref([{
  name: '',
  sno: '',
  college: '',
  major: '',
}])

//一个学生的信息
const studentData = ref()

//分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

//决定是否弹出学生详情页
const StudentDetailDialogVisible = ref(false);

// 导入相关
const uploadUrl = ref('') // 设置为空，因为我们不使用默认的上传方式
const fileList = ref([]) // 用于存储选择的文件
const selectedFile = ref(null) // 存储当前选择的文件
const upload = ref();
const dropdown = ref(null)
const dropdownVisible = ref(false)

/**
 * 初始化加载数据
 */
onMounted(() => {
  fetchData()
})

/**
 * 获取数据
 */
const fetchData = async () => {
  try {
    const response = await axios.get('/stu/student/exam/' + courseId, {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })
    console.log(response);
    studentTableData.value = response.data.data;
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
 * 详情,打开弹窗
 * @param rowData
 */
const detailStudent = (rowData) => {
  //打开一个弹窗，用studentTableData传数据
  StudentDetailDialogVisible.value = true;
  console.log(rowData)
  studentData.value = rowData
}


// 获取状态文本
const getStatusText = (state) => {
  const statusMap = {
    1: '正常',
    0: '缺考',
    2: '作弊'
  }
  return statusMap[state] || '未知状态'
}

// 获取状态对应的CSS类名
const getStatusClass = (state) => {
  const classMap = {
    1: 'normal',
    0: 'absent',
    2: 'cheat'
  }
  return classMap[state] || 'unknown'
}

const handleVisibleChange = (visible) => {
  dropdownVisible.value = visible
}

/**
 * 下载模板
 */
const downloadTemplate = async () => {
  try {
    const response = await axios.get('/exc/template/student/grade', {
      responseType: 'blob'
    });
    //文件名
    let fileName = '学生成绩导入模板.xlsx';

    //创建Blob对象
    const blob = new Blob([response.data], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })

    //创建下载链接并触发下载
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a')
    a.href = url;
    a.download = fileName;
    document.body.appendChild(a);
    a.click();
  } catch (error) {
    ElMessage.error('下载模板失败');
  }
};

const handleExceed: UploadProps['onExceed'] = (files) => {
  upload.value!.clearFiles()
  const file = files[0] as UploadRawFile
  file.uid = genFileId()
  upload.value!.handleStart(file)
}

const handleFileChange = (file, fileList) => {
  fileList.value = [file]; // 替换为数组形式
  if (fileList.length > 1) {
    fileList.splice(0, 1)
  }
  selectedFile.value = file.raw
}

const handleSelectFile = () => {
  upload.value.$el.querySelector('input[type="file"]').click()
}

const handleImport = async () => {
  await submitUpload()
}

/**
 * 学生成绩导入
 */
const submitUpload = async () => {
  if (!selectedFile.value) {
    ElMessage.warning('请先选择文件')
    return
  }

  const formData = new FormData()
  formData.append('file', selectedFile.value)

  try {
    const response = await axios.post('/exc/import/student/grade', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    if (response.data.code === 200) {
      ElMessage.success('导入成功')
      fetchData() // 刷新表格数据

      // 完全重置上传状态
      upload.value.clearFiles(); // 清除上传组件内部文件列表
      fileList.value = []; // 清空文件列表
      selectedFile.value = null; // 清空选择的文件

      // 关闭下拉菜单
      dropdownVisible.value = false;

    } else {
      ElMessage.error(response.data.message)
    }
  } catch (error) {
    console.error('导入失败:', error)
    ElMessage.error('导入失败:!!!')
    console.log(error.response?.data?.message || error.message)
  }
};
</script>


<style scoped>
.status-normal {
  color: #67c23a;
  font-weight: 500;
}
.status-absent {
  color: #909399;
  font-weight: 500;
}
.status-cheat {
  color: #e6a23c;
  font-weight: 500;
}
.status-unknown {
  color: #f56c6c;
  font-weight: 500;
}
</style>