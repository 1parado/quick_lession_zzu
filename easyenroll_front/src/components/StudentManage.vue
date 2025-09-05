<template>
  <!-- 新增按钮 -->
  <el-button type="primary" plain @click="openInsertDialog()" style="margin-bottom: 30px">新增学生</el-button>

  <el-input v-model="searchText" style="width: 240px;margin-left: 20px;margin-bottom: 30px" placeholder="请输入学生姓名" />
  <el-button style="margin-left: 20px;margin-bottom: 30px" type="primary" @click="searchStudent()" round>搜索</el-button>

  <el-dropdown
      ref="dropdown"
      :visible="dropdownVisible"
      @visible-change="handleVisibleChange"
      trigger="click"
      :hide-on-click="false"
  >
    <el-button type="primary" plain  style="margin-left: 970px;margin-bottom: 30px" >
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

  <el-button type="primary" plain @click="exportStudentList()" style="margin-left: 15px;margin-bottom: 30px" >导出学生列表</el-button>

  <el-table :data="studentTableData" stripe style="width: 100%" v-if="studentTableData.length > 0">
    <el-table-column prop="name" label="姓名" wixdth="180" />
    <el-table-column prop="sno" label="学号" wixdth="180" />
    <el-table-column prop="college" label="学院" wixdth="180" />
    <el-table-column prop="major" label="专业" wixdth="180" />
    <el-table-column fixed="right" label="操作" min-width="180" align="center">
      <template #default="scope">
        <el-button link type="primary" @click="detailStudent(scope.row)">详情</el-button>
        <el-button link type="primary" @click="updateStudent(scope.row)">修改</el-button>
        <el-button link type="primary" @click="deleteStudent(scope.row.id)" >删除</el-button>
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
  <el-empty :image-size="200" v-else style="margin-top: 175px"/>
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
  <!-- 修改页弹窗 -->
  <el-dialog v-model="StudentDetailUpdateVisible" title="修改学生信息" width="1000">
    <div>
      <el-form :model="studentData" :rules="rules" ref="updateFormRef" label-position="left" label-width="80px" >
        <el-form-item label="姓名" prop="name" >
          <el-input v-model="studentData.name" />
        </el-form-item>
        <el-form-item label="学号" prop="sno">
          <el-input v-model="studentData.sno" />
        </el-form-item>
        <el-form-item label="学院" prop="college">
          <el-input v-model="studentData.college" />
        </el-form-item>
        <el-form-item label="专业" prop="major">
          <el-input v-model="studentData.major" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-input v-model="studentData.gender" />
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="studentData.age" />
        </el-form-item>
        <el-form-item label="学分" prop="credit">
          <el-input v-model="studentData.credit" />
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="studentData.phone" />
        </el-form-item>
        <el-form-item label="入学时间" prop="inputTime">
          <el-input v-model="studentData.inputTime" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="StudentDetailUpdateVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="updateStudentRel()">
          修改
        </el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 新增页弹窗 -->
  <el-dialog v-model="StudentInsertDialogVisible" title="新增学生" width="1000">
    <div>
      <el-form :model="studentData" :rules="rules" ref="insertFormRef" label-position="left" label-width="80px" >
        <el-form-item label="姓名" prop="name" >
          <el-input v-model="studentData.name" />
        </el-form-item>
        <el-form-item label="学号" prop="sno">
          <el-input v-model="studentData.sno" />
        </el-form-item>
        <el-form-item label="学院" prop="college">
          <el-input v-model="studentData.college" />
        </el-form-item>
        <el-form-item label="专业" prop="major">
          <el-input v-model="studentData.major" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-input v-model="studentData.gender" />
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="studentData.age" />
        </el-form-item>
        <el-form-item label="学分" prop="credit">
          <el-input v-model="studentData.credit" />
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="studentData.phone" />
        </el-form-item>
        <el-form-item label="入学时间" prop="inputTime">
          <el-input v-model="studentData.inputTime" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="StudentInsertDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="insertStudent()">
          新增
        </el-button>
      </div>
    </template>
  </el-dialog>

</template>


<script lang="ts" setup>
import {onMounted, ref} from "vue";
import axios from '../utils/http.js';
import {ElMessage, ElPagination} from 'element-plus';
import { genFileId } from 'element-plus'
import type { UploadProps, UploadRawFile } from 'element-plus'

// 输入的搜索文字
const searchText = ref('');

//学生表格数据
const studentTableData = ref([{
  name: '',
  sno: '',
  college: '',
  major: '',
}])

//一个学生的信息
const studentData = ref()

// 新增一个专门用于新增的初始化方法
const initStudentForCreate = () => {
  studentData.value = {
    name: '',
    sno: '',
    college: '',
    major: '',
    gender: '',
    age: '',
    credit: '',
    phone: '',
    inputTime: ''
  }
}

// 新增按钮的点击事件
const openInsertDialog = () => {
  StudentInsertDialogVisible.value = true
  initStudentForCreate() // 仅在这里初始化
}

//分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

//决定是否弹出学生详情页
const StudentDetailDialogVisible = ref(false);
//决定是否弹出学生修改页
const StudentDetailUpdateVisible = ref(false);
//决定是否弹出学生增加页
const StudentInsertDialogVisible = ref(false);

// 定义表单引用
const updateFormRef = ref(null)
const insertFormRef = ref(null);

//导入相关
const uploadUrl = ref('') // 设置为空，因为我们不使用默认的上传方式
const fileList = ref([]) // 用于存储选择的文件
const selectedFile = ref(null) // 存储当前选择的文件
const upload = ref();
const dropdown = ref(null)
const dropdownVisible = ref(false)



// 验证规则
const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 10, message: '长度在2到10个字符', trigger: 'blur' }
  ],
  sno: [
    { required: true, message: '请输入学号', trigger: 'blur' },
    { pattern: /^\d{11}$/, message: '学号必须是11位数字', trigger: 'blur' }
  ],
  college: [
    { required: true, message: '所属学院不能为空', trigger: 'blur' },
    {
      pattern: /^[\u4e00-\u9fa5]{1,10}学院$/,
      message: '学院名称格式应为“xx学院”，如“软件学院”',
      trigger: 'blur'
    }
  ],
  major: [
    { required: true, message: '请输入专业名称', trigger: 'blur' }
  ],
  gender: [
  ],
  age: [
    {
      type: 'number',
      message: '年龄必须是数字',
      trigger: 'blur',
      transform: value => Number(value)
    }
  ],
  credit: [
    { required: true, message: '请输入学分', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        const num = Number(value)
        if (isNaN(num)) {
          callback(new Error('请输入数字'))
        } else if (num < 0 || num > 100) {
          callback(new Error('学分必须在0-100之间'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  inputTime: [
    { required: true, message: '请选择入学时间', trigger: 'blur' },
    {
      pattern: /^\d{4}年\d{1,2}月$/,
      message: '格式应为"xxxx年x月"或"xxxx年xx月"',
      trigger: 'blur'
    }
  ]
}

/**
 * 初始化加载数据
 */
onMounted(() => {
  fetchData()
  document.addEventListener('click', (e) => {
    if (dropdownVisible.value && !dropdown.value?.$el.contains(e.target)) {
      dropdownVisible.value = false
    }
  })
})

/**
 * 获取数据
 */
const fetchData = async () => {
  try {
    const response = await axios.get('/stu/student', {
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
 * 搜索学生
 */
const searchStudent = async () => {
  if (searchText.value === '' || searchText.value.length <= 0) {
    fetchData();
    return;
  }
  try {
    const response = await axios.get('/stu/student/search/' + searchText.value, {
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
 * 删除学生
 * @param id
 */
const deleteStudent = async (id) => {
  try {
    //先询问是否删除
    if(window.confirm('是否删除')){
      const response = await axios.delete('/stu/student/' + id)
      console.log(response);
      if (response.data.code === 200){
        ElMessage.success("删除成功")
        fetchData();
      } else {
        ElMessage.error("删除失败")
      }
    }
  } catch (error) {
    console.log(error)
    ElMessage.error("删除失败：" + error)
  }
}

/**
 * 修改，打开弹窗
 * @param rowData
 */
const updateStudent = (rowData) => {
  StudentDetailUpdateVisible.value = true;
  studentData.value = rowData
}

/**
 * 修改，真正修改
 */
const updateStudentRel = async () => {
  //将studentData打包送给后端
  // 验证表单
  await updateFormRef.value.validate()

  try {
    const response = await axios.put('/stu/student', studentData.value)
    console.log(response);
    if (response.data.code === 200){
      ElMessage.success("更新成功")
      fetchData();
      StudentDetailUpdateVisible.value = false;
    } else if (response.data.message === '学号重复') {
      ElMessage.error("学号重复")
      fetchData();
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
const detailStudent = (rowData) => {
  //打开一个弹窗，用studentTableData传数据
  StudentDetailDialogVisible.value = true;
  console.log(rowData)
  studentData.value = rowData
}

/**
 * 新增
 */
const insertStudent = async () => {
  // 验证表单
  await insertFormRef.value.validate()

  console.log(studentData.value)
  try {
      const response = await axios.post('/stu/student', studentData.value,  {
        headers: {
          'Content-Type': 'application/json'
        }
      })
      //console.log(response);
      if (response.data.code === 200){
        window.alert("新增成功")
        fetchData();
        StudentInsertDialogVisible.value = false;
      } else if (response.data.message === '学号重复') {
        ElMessage.error("学号重复")
        fetchData();
      } else {
        ElMessage.error("新增失败")
        fetchData();
      }
  } catch (error) {
    console.log(error)
  }
}

/**
 * 导出学生列表
 */
const exportStudentList = async () => {
  try {
    //响应类型为blob
    const response = await axios.get('/exc/export/student', {
      responseType: "blob"
    })
    //文件名
    let fileName = '学生列表_' + new Date() + ".xlsx";

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

  } catch (e) {
    ElMessage.error('下载文件失败');
  }
}

/**
 * 学生导入
 */
const submitUpload = async () => {
  if (!selectedFile.value) {
    ElMessage.warning('请先选择文件')
    return
  }

  const formData = new FormData()
  formData.append('file', selectedFile.value)

  try {
    const response = await axios.post('/exc/import/student', formData, {
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
      ElMessage.error('导入失败')
      console.log(response.data.message)
    }
  } catch (error) {
    console.error('导入失败:', error)
    ElMessage.error('导入失败:!!!')
    console.log(error.response?.data?.message || error.message)
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


/**
 * 下载模板
 */
const downloadTemplate = async () => {
  try {
    const response = await axios.get('/exc/template/student', {
      responseType: 'blob'
    });
    //文件名
    let fileName = '学生导入模板.xlsx';

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

const handleVisibleChange = (visible) => {
  dropdownVisible.value = visible
}

const handleSelectFile = () => {
  upload.value.$el.querySelector('input[type="file"]').click()
}

const handleImport = async () => {
  await submitUpload()
}


</script>


<style scoped>
.upload-dropdown {
  padding: 10px;
}
.upload-dropdown :deep(.el-upload-list) {
  max-height: 150px;
  overflow-y: auto;
  margin-top: 10px;
}
</style>