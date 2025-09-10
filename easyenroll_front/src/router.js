
// router.js
import {createRouter, createWebHistory} from "vue-router";

import Login from './components/Login.vue'
import Welcome from "./components/Welcome.vue";
import StudentManage from "./components/StudentManage.vue";
import TeacherManage from "./components/TeacherManage.vue";
import CourseManage from "./components/CourseManage.vue";
import AnnouncementManage from "./components/AnnouncementManage.vue";
import TaskManage from "./components/TaskManage.vue";
import SelectCourseCenter from "./components/SelectCourseCenter.vue";
import CourseTable from "./components/CourseTable.vue";
import Default from "./components/Default.vue";
import Published from "./components/AnnouncementSub/Published.vue";
import Draft from "./components/AnnouncementSub/Draft.vue";
import Deleted from "./components/AnnouncementSub/Deleted.vue";
import All from "./components/AnnouncementSub/All.vue";
import SelectCourseSeckill from "./components/SelectCourseSeckill.vue";
import AdminSelectCourse from "./components/AdminSelectCourse.vue";
import AdminSelectManage from "./components/AdminSelectManage.vue";
import TeacherCourseManage from "./components/TeacherCourseManage.vue";
import TeacherCourseStudentManage from "./components/TeacherCourseStudentManage.vue";
import TeacherCourseTable from "./components/TeacherCourseTable.vue";
import TeacherAnnouncementManage from "./components/TeacherAnnouncementManage.vue";
import AllTeacher from "./components/TeacherAnnouncementSub/AllTeacher.vue";
import PublishedTeacher from "./components/TeacherAnnouncementSub/PublishedTeacher.vue";
import DraftTeacher from "./components/TeacherAnnouncementSub/DraftTeacher.vue";
import DeletedTeacher from "./components/TeacherAnnouncementSub/DeletedTeacher.vue";
import StudentPayment from "./components/StudentPayment.vue";
import AdminPayment from "./components/AdminPayment.vue";
import SmsConfig from "./components/SmsConfig.vue";
import ExamManage from "./components/ExamManage.vue";
import ExamCourseStudentManage from "./components/ExamCourseStudentManage.vue";
import ExamStudentPlan from "./components/ExamStudentPlan.vue";
import ExamGradeManage from "./components/ExamGradeManage.vue";
import TeacherCourseStudentExamGradeManage from "./components/TeacherCourseStudentExamGradeManage.vue";
import StudentGrade from "./components/StudentGrade.vue";
import Leaderboard from "./components/Leaderboard.vue";
import ForumHomepage from "./components/forum/ForumHomepage.vue";
import ForumWelcome from "./components/forum/ForumWelcome.vue";
import ForumCourseList from "./components/forum/ForumCourseList.vue";
import ForumCourse from "./components/forum/ForumCourse.vue";
import ForumPost from "./components/forum/ForumPost.vue";
import ForumPublish from "./components/forum/ForumPublish.vue";
import ForumCourseGroup from "./components/forum/ForumCourseGroup.vue";
import AIChat from "./components/ai/AIChat.vue";


const routes = [
    {
        path: '/',
        component: Welcome,
        meta: { requiresAuth: true },
        //当使用 嵌套路由（children） 时，默认情况下，父路由和子路由会 同时渲染
        children: [
            { path: '', component: Default},
            { path: 'studentManage', component: StudentManage, meta: { requiresAuth: true }},
            { path: 'teacherManage', component: TeacherManage, meta: { requiresAuth: true }},
            { path: 'courseManage', component: CourseManage, meta: { requiresAuth: true }},
            { path: 'announcementManage', component: AnnouncementManage, meta: { requiresAuth: true },
                children: [
                    { path: '', component: All, meta: { requiresAuth: true}},
                    { path: '/published', component: Published, meta: { requiresAuth: true}},
                    { path: '/draft', component: Draft, meta: { requiresAuth: true}},
                    { path: '/deleted', component: Deleted, meta: { requiresAuth: true}}
                ]
            },
            { path: 'taskManage', component: TaskManage, meta: { requiresAuth: true }},
            { path: 'selectCourseCenter', component: SelectCourseCenter, meta: { requiresAuth: true }},
            { path: 'selectCourseSeckill', component: SelectCourseSeckill, meta: { requiresAuth: true }},
            { path: 'courseTable', component: CourseTable, meta: { requiresAuth: true }},
            { path: 'adminSelectCourse', component: AdminSelectCourse, meta: { requiresAuth: true }},
            { path: 'adminSelectManage', component: AdminSelectManage, meta: { requiresAuth: true }},
            { path: 'teacherCourseManage', component: TeacherCourseManage, meta: { requiresAuth: true }},
            { path: 'teacherCourseStudentManage', component: TeacherCourseStudentManage, meta: { requiresAuth: true }},
            { path: 'teacherCourseTable', component: TeacherCourseTable, meta: { requiresAuth: true }},
            { path: 'teacherAnnouncementManage', component: TeacherAnnouncementManage, meta: { requiresAuth: true },
                children: [
                    { path: '', component: AllTeacher, meta: { requiresAuth: true}},
                    { path: '/publishedTeacher', component: PublishedTeacher, meta: { requiresAuth: true}},
                    { path: '/draftTeacher', component: DraftTeacher, meta: { requiresAuth: true}},
                    { path: '/deletedTeacher', component: DeletedTeacher, meta: { requiresAuth: true}}
                ]
            },
            { path: 'studentPayment', component: StudentPayment, meta: { requiresAuth: true}},
            { path: 'adminPayment', component: AdminPayment, meta: { requiresAuth: true}},
            { path: 'smsConfig', component: SmsConfig, meta: { requiresAuth: true}},
            { path: 'examManage', component: ExamManage, meta: { requiresAuth: true}},
            { path: 'examCourseStudentManage', component: ExamCourseStudentManage, meta: { requiresAuth: true}},
            { path: 'examStudentPlan', component: ExamStudentPlan, meta: { requiresAuth: true}},
            { path: 'examGradeManage', component: ExamGradeManage, meta: { requiresAuth: true}},
            { path: 'teacherCourseStudentExamGradeManage', component: TeacherCourseStudentExamGradeManage, meta: { requiresAuth: true}},
            { path: 'studentGrade', component: StudentGrade, meta: { requiresAuth: true}},
            { path: 'leaderboard', component: Leaderboard, meta: { requiresAuth: true}},
        ]
    },
    {
        path: '/login',
        component: Login,
        meta: { requiresAuth: false }
    },
    {
        path: '/forumHomepage',
        component: ForumWelcome,
        meta: { requiresAuth: true },
        children: [
            { path: '', component: ForumHomepage, meta: { requiresAuth: true} },
            { path: 'forumCourseList', component: ForumCourseList, meta: { requiresAuth: true} },
            { path: 'forumCourse', component: ForumCourse, meta: { requiresAuth: true} },
            { path: 'forumPost', component: ForumPost, meta: { requiresAuth: true} },
            { path: 'forumPublish', component: ForumPublish, meta: { requiresAuth: true} },
            { path: 'forumCourseGroup', component: ForumCourseGroup, meta: { requiresAuth: true} },
        ]
    }, {
        path: '/aiChat',
        component: AIChat,
        meta: { requiresAuth: true },
    }
]

// 必须创建并导出路由实例
const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),  // 使用 Vite 的环境变量
    routes
})

//配置全局路由守卫，检查访问权限(token)
router.beforeEach((to, from, next) => {
    const token = sessionStorage.getItem('token')
    // 如果访问的是需要认证的页面且role不合法
    // 如果访问的是需要认证的页面且没有token
    if (to.meta.requiresAuth && !token) {
        next('/login')
    } else {
        next()
    }
})

export default router