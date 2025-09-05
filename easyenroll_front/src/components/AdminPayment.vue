<template>
  <!-- 新增按钮 -->
  <el-button type="primary" plain @click="openInsertDialog()" style="margin-bottom: 30px">新增缴费项目</el-button>

  <el-table :data="paymentTableData" stripe style="width: 100%" v-if="paymentTableData">
    <el-table-column prop="name" label="项目名称" />
    <el-table-column prop="amount" label="待缴费金额" />
    <el-table-column prop="startTime" label="缴费开始时间" >
      <template #default="scope">
        <!-- 使用之前定义的 formatTime 函数格式化时间 -->
        {{ formatTime(scope.row.startTime) }}
      </template>
    </el-table-column>
    <el-table-column prop="endTime" label="缴费结束时间" >
      <template #default="scope">
        <!-- 使用之前定义的 formatTime 函数格式化时间 -->
        {{ formatTime(scope.row.endTime) }}
      </template>
    </el-table-column>
    <el-table-column prop="paidCount" label="已缴费人数" />
    <el-table-column prop="payableCount" label="应缴费人数" />
    <el-table-column fixed="right" label="操作" min-width="180" align="center">
      <template #default="scope">
        <el-button link type="primary" @click="detailPayment(scope.row)">查看</el-button>
        <el-button link type="primary" @click="updatePayment(scope.row)">修改</el-button>
        <el-button link type="primary" @click="deletePayment(scope.row.id)">删除</el-button>
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
      v-if="paymentTableData"
  />
  <el-empty :image-size="200" v-else style="margin-top: 175px"/>

  <!-- 修改页弹窗 -->
  <el-dialog v-model="PaymentUpdateDialogVisible" title="修改缴费项目信息" width="1000">
    <div>
      <el-form :model="paymentData" :rules="rules" ref="updateFormRef" label-position="left" label-width="110px" >
        <el-form-item label="缴费项目名" prop="name" >
          <el-input v-model="paymentData.name" />
        </el-form-item>
        <el-form-item label="需缴费金额" prop="amount">
          <el-input v-model="paymentData.amount" />
        </el-form-item>
        <el-form-item label="缴费开始时间" prop="startTime">
          <el-date-picker
              style="width: 1000px"
              v-model.number="paymentData.startTime"
              type="datetime"
              placeholder="选择开始时间"
          />
        </el-form-item>
        <el-form-item label="缴费结束时间" prop="endTime">
          <el-date-picker
              style="width: 1000px"
              v-model="paymentData.endTime"
              type="datetime"
              placeholder="选择结束时间"
              :disabled-date="disabledDate"
          />
        </el-form-item>
        <el-form-item label="应缴费人数" prop="payableCount">
          <el-input v-model="paymentData.payableCount" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="PaymentUpdateDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="updatePaymentRel()">
          修改
        </el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 新增页弹窗 -->
  <el-dialog v-model="PaymentInsertDialogVisible" title="新增缴费项目" width="1000">
    <div>
      <el-form :model="paymentData" :rules="rules" ref="insertFormRef" label-position="left" label-width="110px" >
        <el-form-item label="缴费项目名" prop="name" >
          <el-input v-model="paymentData.name" />
        </el-form-item>
        <el-form-item label="需缴费金额" prop="amount">
          <el-input v-model="paymentData.amount" />
        </el-form-item>
        <el-form-item label="缴费开始时间" prop="startTime">
          <el-date-picker
              style="width: 1000px"
              v-model="paymentData.startTime"
              type="datetime"
              placeholder="选择开始时间"
          />
        </el-form-item>
        <el-form-item label="缴费结束时间" prop="endTime">
          <el-date-picker
              style="width: 1000px"
              v-model="paymentData.endTime"
              type="datetime"
              placeholder="选择结束时间"
              :disabled-date="disabledDate"
          />
        </el-form-item>
        <el-form-item label="应缴费人数" prop="payableCount">
          <el-input v-model="paymentData.payableCount" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="PaymentInsertDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="insertPayment()">
          新增
        </el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 详细页弹窗 -->
  <el-dialog v-model="PaymentDetailDialogVisible" title="缴费人列表" width="1000">
    <div>
      <el-table :data="studentTableData" stripe style="width: 100%" v-if="studentTableData.length > 0">
        <el-table-column prop="name" label="姓名" wixdth="180" />
        <el-table-column prop="sno" label="学号" wixdth="180" />
        <el-table-column prop="college" label="学院" wixdth="180" />
        <el-table-column prop="major" label="专业" wixdth="180" />
        <el-table-column fixed="right" label="操作" min-width="180" align="center">
          <template #default="scope">
            <el-button link type="primary" @click="detailStudent(scope.row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页条 -->
      <el-pagination
          :current-page="currentPageStudent"
          :page-size="pageSizeStudent"
          :total="totalStudent"
          layout="prev, pager, next"
          @current-change="handlePageChangeStudent"
          style="margin-top: 20px; justify-content: center;"
          v-if="studentTableData.length > 0"
      />
      <el-empty :image-size="200" v-else style="margin-top: 175px"/>
    </div>
  </el-dialog>

  <!-- 学生详情页弹窗 -->
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



<script setup>

import {ElMessage, ElPagination} from "element-plus";
import {onMounted, ref} from "vue";
import axios from "../utils/http.js";

//  缴费项目表格数据
const paymentTableData = ref([{}])

//一个缴费项目的信息
const paymentData = ref()

//一个缴费项目对应的学生表格数据
const studentTableData = ref([{
  name: '',
  sno: '',
  college: '',
  major: '',
}])

//一个学生信息
const studentData = ref()

// 专门用于新增的初始化方法
const initPaymentForCreate = () => {
  paymentData.value = {
    name: '',
    amount: '',
    startTime: '',
    endTime: '',
    payableCount: ''
  }
}

//分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 学生列表分页相关
const currentPageStudent = ref(1)
const pageSizeStudent = ref(10)
const totalStudent = ref(0)


//决定是否弹出缴费项目修改页
const PaymentUpdateDialogVisible = ref(false);
//决定是否弹出缴费项目增加页
const PaymentInsertDialogVisible = ref(false);
//决定是否弹出缴费项目查看页
const PaymentDetailDialogVisible = ref(false);
//决定是否弹出学生详情页
const StudentDetailDialogVisible = ref(false);

// 定义表单引用
const updateFormRef = ref(null)
const insertFormRef = ref(null)

// 验证规则
const rules = {
  name: [
    { required: true, message: '请输入缴费项目名', trigger: 'blur' },
    { min: 2, max: 10, message: '长度在2到10个字符', trigger: 'blur' }
  ],
  amount: [
    { required: true, message: '请输入需缴费金额', trigger: 'blur' },
    {
      type: 'number',
      message: '缴费金额必须是数字',
      trigger: 'blur',
      transform: value => Number(value)
    }
  ],
  startTime: [
    { required: true, message: '请输入缴费开始时间', trigger: 'blur' }
  ],
  endTime: [
    { required: true, message: '请输入缴费结束时间', trigger: 'blur' }
  ],
  payableCount: [
    { required: true, message: '请输入应缴费人数', trigger: 'blur' },
    {
      type: 'number',
      message: '缴费人数必须是数字',
      trigger: 'blur',
      transform: value => Number(value)
    }
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

// 禁用今天之前的日期
const disabledDate = (time) => {
  return time.getTime() < Date.now() - 86400000
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
 * 学生列表页码变化处理
 * @param page
 */
const handlePageChangeStudent = (page) => {
  currentPage.value = page
  fetchData()
}


onMounted(() => {
  fetchData()
})

const fetchData = async () => {
  try {
    //  查询所有的缴费项目
    const response = await axios.get('/pay/payment/all', {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })
    console.log(response)
    if (response.data.code === 200) {
      paymentTableData.value = response.data.data
      total.value = Number(response.data.message);
    } else {
      ElMessage("获取失败")
    }
  } catch (error) {
    window.alert(error)
  }
}

// 新增按钮的点击事件
const openInsertDialog = () => {
  PaymentInsertDialogVisible.value = true
  initPaymentForCreate() // 仅在这里初始化
}

/**
 * 修改，打开弹窗
 * @param rowData
 */
const updatePayment = (rowData) => {
  PaymentUpdateDialogVisible.value = true;
  paymentData.value = rowData
}

/**
 * 修改，真正修改
 */
const updatePaymentRel = async () => {
  //将studentData打包送给后端
  // 验证表单
  await updateFormRef.value.validate()

  try {
    const response = await axios.put('/pay/payment', paymentData.value)
    console.log(response);
    if (response.data.code === 200){
      ElMessage.success("更新成功")
      fetchData();
      PaymentUpdateDialogVisible.value = false;
    }  else {
      window.alert("更新失败")
      fetchData();
    }
  } catch (error) {
    console.log(error)
  }
}

/**
 * 新增
 */
const insertPayment = async () => {
  // 验证表单
  await insertFormRef.value.validate()

  try {
    const response = await axios.post('/pay/payment', paymentData.value,  {
      headers: {
        'Content-Type': 'application/json'
      }
    })

    if (response.data.code === 200){
      ElMessage.success("新增成功")
      fetchData();
      PaymentInsertDialogVisible.value = false;
    } else {
      window.alert("新增失败")
      fetchData();
    }
  } catch (error) {
    console.log(error)
  }
}

/**
 * 查看，打开窗口，获取学生数据
 * @param rowData
 * @returns {Promise<void>}
 */
const detailPayment = async (rowData) => {
  paymentData.value = rowData;

  try {
    //获取学生列表
    const response = await axios.get('/pay/payment/student/' + paymentData.value.id, {
      params: {
        page: currentPageStudent.value,
        size: pageSizeStudent.value
      }
    });
    if (response.data.code === 200) {
      studentTableData.value = response.data.data;
      totalStudent.value = Number(response.data.message);
    } else {
      ElMessage("获取学生列表失败")
    }
  } catch (error) {
    console.log(error)
  }

  PaymentDetailDialogVisible.value = true;
}

/**
 * 学生详情,打开弹窗
 * @param rowData
 */
const detailStudent = (rowData) => {
  //打开一个弹窗，用studentTableData传数据
  StudentDetailDialogVisible.value = true;
  console.log(rowData)
  studentData.value = rowData
}

const deletePayment = async (id) => {
  if (window.confirm("确定删除？")) {
    try {
      const response = await axios.delete('/pay/payment/' + id);
      if (response.data.code === 200) {
        ElMessage.success("删除成功");
        fetchData();
      } else {
        ElMessage.error("删除失败");
        fetchData();
      }
    } catch (e) {
      ElMessage.error("删除失败：" + e)
    }
  }
}


</script>


<style scoped>

</style>