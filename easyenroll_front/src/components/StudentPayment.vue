<template>

  <el-button type="primary" plain @click="PaymentRecordDialogVisible = true" style="margin-bottom: 30px">缴费记录</el-button>

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
    <el-table-column label="缴费状态">
      <template #default="scope">
        <el-tag :type="getStatusType(scope.row)" size="small">
          {{ getStatusText(scope.row) }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column fixed="right" label="操作" min-width="180" align="center">
      <template #default="scope">
        <el-button
            link
            type="primary"
            @click="pay(scope.row)"
            :disabled="!isPaymentAvailable(scope.row)"
        >
          缴费
        </el-button>
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

  <!-- 历史缴费记录页弹窗 -->
  <el-dialog v-model="PaymentRecordDialogVisible" title="历史缴费记录" width="1000">
    <el-table :data="payedData" stripe style="width: 100%" v-if="payedData.length > 0">
      <el-table-column prop="name" label="项目名称" />
      <el-table-column prop="amount" label="缴费金额" />
      <el-table-column prop="payTime" label="缴费时间" >
        <template #default="scope">
          <!-- 使用之前定义的 formatTime 函数格式化时间 -->
          {{ formatTime(scope.row.payTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="payMethod" label="缴费方式" />
    </el-table>
    <!-- 分页条 -->
    <el-pagination
        :current-page="currentPagePayedData"
        :page-size="pageSizePayedData"
        :total="totalPayedData"
        layout="prev, pager, next"
        @current-change="handlePageChangePayedData"
        style="margin-top: 20px; justify-content: center;"
        v-if="payedData.length > 0"
    />
    <el-empty :image-size="200" v-else style="margin-top: 175px"/>
    <template #footer>
      <div>
        <el-button type="primary" @click="PaymentRecordDialogVisible = false">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>


<script setup>

import {ElMessage, ElPagination} from "element-plus";
import {onMounted, ref} from "vue";
import axios from "../utils/http.js"
import {useRoute} from "vue-router";


//  待缴费表格数据
const paymentTableData = ref([{}])

//  缴费记录数据
const payedData = ref([{}])

//  缴费记录分页相关
const currentPagePayedData = ref(1)
const pageSizePayedData = ref(10)
const totalPayedData = ref(0)

//分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

//  决定是否打开历史缴费记录页面
const PaymentRecordDialogVisible = ref(false);

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

// 获取当前时间
const getCurrentTime = () => new Date()

// 判断缴费状态
const getPaymentStatus = (row) => {
  const now = getCurrentTime()
  const startTime = new Date(row.startTime)
  const endTime = new Date(row.endTime)

  if (now < startTime) {
    return 'not_started' // 未开始
  } else if (now > endTime) {
    return 'expired' // 已过期
  } else {
    return 'available' // 可缴费
  }
}

// 获取状态文本
const getStatusText = (row) => {
  const status = getPaymentStatus(row)
  switch(status) {
    case 'not_started': return '未开始，请关注学校动态'
    case 'available': return '缴费通道开放中...'
    case 'expired': return '已超时，请联系管理员'
    default: return ''
  }
}

// 获取状态标签类型
const getStatusType = (row) => {
  const status = getPaymentStatus(row)
  switch(status) {
    case 'not_started': return 'info'
    case 'available': return 'success'
    case 'expired': return 'danger'
    default: return ''
  }
}

// 判断是否可以缴费
const isPaymentAvailable = (row) => {
  return getPaymentStatus(row) === 'available'
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
 * 缴费记录页码变化处理
 * @param page
 */
const handlePageChangePayedData = (page) => {
  currentPagePayedData.value = page
  fetchPayedData()
}

onMounted(() => {
  const route = useRoute();
  const out_trade_no = route.query.out_trade_no;
  if (out_trade_no) {
    fetchPayResult(out_trade_no)
  }
  fetchData()
  fetchPayedData()
})

let status = ref();

const fetchPayResult = async (out_trade_no) => {
  const response = await axios.get('/pay/alipay/' + out_trade_no);
}

const fetchData = async () => {
  try {
    //  查询 未缴费  的缴费项目，满足时间要求,不满足时间的不显示缴费按钮，并显示过期标签
    const response = await axios.get('/pay/payment/' + sessionStorage.getItem('studentId'), {
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

const fetchPayedData = async () => {
  try {
    //  查询 当前学生 已缴费  的缴费项目
    const response = await axios.get('/pay/payment/payed/' + sessionStorage.getItem('studentId'), {
      params: {
        page: currentPagePayedData.value,
        size: pageSizePayedData.value
      }
    })
    console.log(response)
    if (response.data.code === 200) {
      payedData.value = response.data.data
      totalPayedData.value = Number(response.data.message);
    } else {
      ElMessage("获取失败")
    }
  } catch (error) {
    window.alert(error)
  }
}

const pay = async (row) => {
 const response = await axios.post('/alipay/pay', {
   id: row.id,
   name: row.name,
   amount: row.amount,
   startTime: row.startTime,
   endTime: row.endTime,
   payerId: sessionStorage.getItem('account'),
   payMethod: '支付宝',
   payTime: new Date(),
   payStatus: 0
 }, {
   headers: {
     'Content-Type': 'application/json',
     'Authorization': 'Bearer ' + sessionStorage.getItem('token') // 确保传递了token
   }
 })
  console.log(response)

  // 创建一个新窗口或iframe来提交支付宝表单
  /*const newWindow = window.open('', '_blank');
  newWindow.document.write(response.data);
  newWindow.document.close();*/

  // 直接替换当前页面
  document.open();
  document.write(response.data);
  document.close();

}


</script>



<style scoped>

</style>