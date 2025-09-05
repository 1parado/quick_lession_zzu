import axios from 'axios'
import router from "../router.js";
import {ElMessage} from "element-plus";

// axios.create()：创建一个自定义的 Axios 实例，
const service = axios.create({
    // 所有请求会自动在前面添加 /api 前缀
    baseURL: '/api',
    // 请求超过 5 秒无响应会自动取消
    timeout: 5000
})

// 请求拦截器
service.interceptors.request.use(
    (config) => {
        //发送请求前拦截，对请求进行各种处理
        const token = sessionStorage.getItem('token')

        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`
        }
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

// 响应拦截器
service.interceptors.response.use(
    response => {
        if (response.data.code === 401 || response.data.code === 402 ) {
            window.alert(response.data.message);
            sessionStorage.removeItem('token')
            //去重新登录
            router.push('/login')
        }
        return response
    },
    (error) => {
        // 对响应错误做统一处理（如 401 跳转登录）
        if (error.response.status === 401) {
            // Token过期或无效，跳转到登录页
            sessionStorage.removeItem('token')
            router.push('/login')
        }
        return Promise.reject(error)
    }
)


export default service