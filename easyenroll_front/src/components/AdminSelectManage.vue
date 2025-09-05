<template>
  <div class="action-footer">
    <el-button type="primary" size="large" round class="submit-btn" @click="easySelect()">一键选课</el-button>
  </div>
  <div class="container">
    <el-container class="main-container">
      <!-- 学生选择区域 -->
      <el-aside class="selection-panel">
        <div class="panel-header">
          <span class="panel-title">学生列表</span>
          <el-button type="primary" size="large" round @click="batchStudent">批量选择学生</el-button>
        </div>
        <el-table :data="selectedStudents" stripe style="width: 100%" v-if="selectedStudents.length > 0">
          <el-table-column prop="name" label="姓名" />
          <el-table-column prop="sno" label="学号"  />
          <el-table-column prop="college" label="学院"  />
          <el-table-column prop="major" label="专业" />
          <el-table-column fixed="right" label="操作"  align="center">
            <template #default="scope">
              <el-button link type="danger" @click="removeStudent(scope.$index)">移除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty :image-size="200" v-else style="margin-top: 175px" description='您还未选择任何学生'/>
      </el-aside>

      <!-- 课程选择区域 -->
      <el-aside class="selection-panel">
        <div class="panel-header">
          <span class="panel-title">课程列表</span>
          <el-button type="primary" size="large" round @click="batchCourse()">批量选择课程</el-button>
        </div>
        <el-table :data="selectedCourses" stripe style="width: 100%" v-if="selectedCourses.length > 0">
          <el-table-column prop="name" label="课程名"/>
          <el-table-column prop="courseCode" label="课程代码"  />
          <el-table-column prop="courseType" label="课程类型"  />
          <el-table-column prop="college" label="所属学院"  />
          <el-table-column prop="semester" label="开设学期"  />
          <el-table-column fixed="right" label="操作"  align="center">
            <template #default="scope">
              <el-button link type="danger" @click="removeCourse(scope.$index)">移除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty :image-size="200" v-else style="margin-top: 175px" description='您还未选择任何课程'/>


      </el-aside>
    </el-container>
    <el-dialog v-model="BatchStudentVisible" title="批量选择学生" width="1000" align-center @closed="saveSelectedStudents">
      <div>
        <!-- 这里可以放置学生列表内容 -->
        <el-table
            :data="studentTableData"
            stripe style="width: 100%"
            @selection-change="handleSelectionChangeStudent"
        >
          <el-table-column type="selection" width="55" />  <!-- 复选框列 -->
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
            :current-page="currentPage"
            :page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            @current-change="handlePageChange"
            style="margin-top: 20px; justify-content: center;"
        />
        <el-button type="primary" size="large" round @click="saveSelectedStudents()">提交</el-button>
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
      </div>
    </el-dialog>

    <el-dialog v-model="BatchCourseVisible" title="批量选择课程" width="1000" align-center @closed="saveSelectedCourses">
      <div>
        <!-- 这里可以放置课程列表内容 -->
        <el-table :data="courseTableData" stripe style="width: 100%" @selection-change="handleSelectionChangeCourse">
          <el-table-column type="selection" width="55" />  <!-- 复选框列 -->
          <el-table-column prop="name" label="课程名" wixdth="180" />
          <el-table-column prop="courseCode" label="课程代码" wixdth="180" />
          <el-table-column prop="courseType" label="课程类型" wixdth="180" />
          <el-table-column prop="college" label="所属学院" wixdth="180" />
          <el-table-column prop="semester" label="开设学期" wixdth="180" />
          <el-table-column fixed="right" label="操作" min-width="180" align="center">
            <template #default="scope">
              <el-button link type="primary" @click="detailCourse(scope.row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页条 -->
        <el-pagination
            :current-page="currentPageCourse"
            :page-size="pageSizeCourse"
            :total="totalCourse"
            layout="prev, pager, next"
            @current-change="handlePageChangeCourse"
            style="margin-top: 20px; justify-content: center;"
        />
        <el-button type="primary" size="large" round @click="saveSelectedCourses()">提交</el-button>
        <!-- 详情页弹窗 -->
        <el-dialog v-model="CourseDetailDialogVisible" title="课程详情信息" width="1000" align-center>
          <div>
            <el-form :model="courseData" label-position="left" label-width="110px" :disabled="true">
              <el-form-item label="课程名" >
                <el-input v-model="courseData.name" />
              </el-form-item>
              <el-form-item label="课程代码" >
                <el-input v-model="courseData.courseCode" />
              </el-form-item>
              <el-form-item label="课程类型" >
                <el-input v-model="courseData.courseType" />
              </el-form-item>
              <el-form-item label="所属学院" >
                <el-input v-model="courseData.college" />
              </el-form-item>
              <el-form-item label="开设学期" >
                <el-input v-model="courseData.semester" />
              </el-form-item>
              <el-form-item label="学分" >
                <el-input v-model="courseData.credit" />
              </el-form-item>
              <el-form-item label="课程容量" >
                <el-input v-model="courseData.capacity" />
              </el-form-item>
              <el-form-item label="剩余名额" >
                <el-input v-model="courseData.remain" />
              </el-form-item>
              <el-form-item label="是否需要抢选" >
                <el-input v-model="courseData.isSeckill" />
              </el-form-item>
              <el-form-item label="任课教师" >
                <el-input v-model="courseData.teacherName" />
              </el-form-item>
              <el-form-item label="上课时间" >
                <el-input v-model="courseData.classTime" />
              </el-form-item>
              <el-form-item label="上课地点" >
                <el-input v-model="courseData.classLocation" />
              </el-form-item>
              <el-form-item label="周次范围" >
                <el-input v-model="courseData.weekRange" />
              </el-form-item>
              <el-form-item label="前置课程" >
                <el-input v-model="courseData.preCourseCode" />
              </el-form-item>
              <el-form-item label="课程描述" >
                <el-input v-model="courseData.description" />
              </el-form-item>
            </el-form>
          </div>
          <template #footer>
            <div>
              <el-button type="primary" @click="CourseDetailDialogVisible = false">
                确认
              </el-button>
            </div>
          </template>
        </el-dialog>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import axios from "../utils/http.js";
import {ElMessage, ElPagination} from "element-plus";

export default {
  name: 'AdminSelectManage',
  components: {ElPagination},
  setup() {
    // 响应式数据定义在这里
    const studentTableData = ref([
      { name: '', sno: '', college: '', major: '' } // 初始空数据，可留空或设为 []
    ]);
    const courseTableData = ref([
      {name: '', courseCode: '', courseType: '', college: '', semester: ''}
    ])

    const studentData = ref(null);
    const courseData = ref(null);

    //学生列表分页相关
    const currentPage = ref(1);
    const pageSize = ref(10);
    const total = ref(0);
    //课程列表分页相关
    const currentPageCourse = ref(1)
    const pageSizeCourse = ref(10)
    const totalCourse = ref(0)

    //弹出详情页
    const StudentDetailDialogVisible = ref(false);
    const CourseDetailDialogVisible = ref(false);

    //批量选择窗口弹出
    const BatchStudentVisible = ref(false);
    const BatchCourseVisible = ref(false);

    //存储最终选中的学生
    const selectedStudents = ref([]);
    //临时存储弹窗中选中的学生
    const tempSelectedStudents = ref([]);
    //存储最终选中的课程
    const selectedCourses = ref([]);
    //临时存储弹窗中选中的课程
    const tempSelectedCourses = ref([]);

    // ✅ 方法定义
    const fetchData = async () => {
      try {
        const response = await axios.get('/stu/student', {
          params: {
            page: currentPage.value,
            size: pageSize.value
          }
        });
        console.log("API Response:", response);
        // ✅ 确保 response.data.data 是数组，response.data.message 是总数
        studentTableData.value = response.data.data || []; // 防止 undefined
        total.value = Number(response.data.message) || 0;
        console.log("Total:", total.value);
      } catch (error) {
        console.error('获取数据失败:', error);
      }
    };
    const fetchCourseData = async () => {
      try {
        const response = await axios.get('/cou/course/noseckill', {
          params: {
            page: currentPage.value,
            size: pageSize.value
          }
        })
        console.log(response);
        courseTableData.value = response.data.data;
        total.value = Number(response.data.message);
        console.log("Total:", total.value, "Type:", typeof total.value);
      } catch (error) {
        console.error('获取数据失败:', error);
      }
    }

    //分页相关
    const handlePageChange = (page) => {
      currentPage.value = page;
      fetchData();
    };
    const handlePageChangeCourse = (page) => {
      currentPageCourse.value = page
      fetchCourseData()
    }

    //详情按钮
    const detailStudent = (rowData) => {
      StudentDetailDialogVisible.value = true;
      studentData.value = rowData;
    };
    const detailCourse = (rowData) => {
      //打开一个弹窗，用courseTableData传数据
      CourseDetailDialogVisible.value = true;
      console.log(rowData)
      courseData.value = rowData
    }

    //批量选择按钮
    const batchStudent = () => {
      BatchStudentVisible.value = true;
    }
    const batchCourse = () => {
      BatchCourseVisible.value = true;
    }

    /**
     * 处理复选框选择变化
     */
    const handleSelectionChangeStudent = (selection) => {
      tempSelectedStudents.value = selection;
    }
    const handleSelectionChangeCourse = (selection) => {
      tempSelectedCourses.value = selection;
    }

    /**
     * 提交选中的
     */
    const saveSelectedStudents = () => {
      selectedStudents.value = [...tempSelectedStudents.value];
      BatchStudentVisible.value = false;
    }
    const saveSelectedCourses = () => {
      selectedCourses.value = [...tempSelectedCourses.value];
      BatchCourseVisible.value = false;
    }

    /**
     * 移除单个
     */
    const removeStudent = (index) => {
      selectedStudents.value.splice(index, 1);  // 移除指定位置的学生
    };
    const removeCourse = (index) => {
      selectedCourses.value.splice(index, 1);  // 移除指定位置的学生
    };

    /**
     *  一键选课anniu
     */
    const easySelect = async () => {
      console.log("selectedStudents")
      console.log(selectedStudents.value)
      console.log("selectedCourses")
      console.log(selectedCourses.value)
      if (selectedStudents.value.length === 0 || selectedCourses.value.length === 0) {
        ElMessage("请选择学生或课程");
        return;
      }
      try {
        const response = await axios.post('/sel/selection/batch', {
          selectedStudents: selectedStudents.value,
          selectedCourses: selectedCourses.value
        }, {
          headers: {
            'Content-Type': 'application/json'
          }
        })
        if (response.data.code === 200) {
          ElMessage.success("选课成功");
        } else if (response.data.message === '部分学生尚未满足所选课程的前置课程要求') {
          ElMessage.error("部分学生尚未满足所选课程的前置课程要求");
        } else if (response.data.message === '选课组合中存在时间排布冲突') {
          ElMessage.error("选课组合中存在时间排布冲突");
        } else if (response.data.message === '系统错误') {
          ElMessage.error("系统错误");
        }
      } catch (error) {
        window.alert("系统异常");
      }
    }

    // ✅ 组件挂载时加载数据
    onMounted(() => {
      fetchData();
      fetchCourseData()
    });

    // ✅ 必须返回模板中用到的所有响应式数据和方法
    return {
      studentTableData,
      courseTableData,
      currentPage,
      pageSize,
      total,
      currentPageCourse,
      pageSizeCourse,
      totalCourse,
      StudentDetailDialogVisible,
      CourseDetailDialogVisible,
      BatchStudentVisible,
      BatchCourseVisible,
      studentData,
      courseData,
      selectedStudents,
      selectedCourses,
      fetchData,
      fetchCourseData,
      handlePageChange,
      handlePageChangeCourse,
      detailStudent,
      detailCourse,
      batchStudent,
      batchCourse,
      handleSelectionChangeStudent,
      handleSelectionChangeCourse,
      saveSelectedStudents,
      saveSelectedCourses,
      removeStudent,
      removeCourse,
      easySelect
    };
  }
};
</script>

<style scoped>
.container {
  height: 79vh;
  display: flex;
  flex-direction: column;
}

.main-container {
  flex: 1;
  padding: 16px;
  justify-content: space-around;
}

.selection-panel {
  width: 40vw !important;
  min-width: 500px;
  background: white;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  margin: 0 16px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.panel-header {
  padding: 12px 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #dcdfe6;
}

.panel-title {
  font-weight: 500;
}

.scroll-area {
  flex: 1;
  padding: 0 16px;
}

.content-space {
  width: 100%;
  padding: 8px 0;
}

.action-footer {
  padding: 16px;
  text-align: center;
  border-top: 1px solid #dcdfe6;
}

.submit-btn {
  width: 200px;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .main-container {
    flex-direction: column;
    align-items: center;
  }

  .selection-panel {
    width: 80vw !important;
    margin-bottom: 16px;
  }
}
</style>