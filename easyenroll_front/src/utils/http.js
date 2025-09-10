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
        // 请求成功的处理
        return response;
    },
    error => {
        // 修复这里：检查 error.response 是否存在
        if (error.response) {
            // 服务器返回了错误响应
            const status = error.response.status;
            const message = error.response.data?.message || '请求失败';

            console.error(`请求错误 ${status}: ${message}`);

            // 可以根据不同的状态码进行不同的处理
            switch (status) {
                case 401:
                    console.error('未授权，请重新登录');
                    break;
                case 403:
                    console.error('禁止访问');
                    break;
                case 404:
                    console.error('请求的资源不存在');
                    break;
                case 500:
                    console.error('服务器内部错误');
                    break;
                default:
                    console.error('其他错误');
            }
        } else if (error.request) {
            // 请求已发出但没有收到响应
            console.error('网络错误，请检查网络连接');
        } else {
            // 其他错误
            console.error('请求配置错误:', error.message);
        }
        return Promise.reject(error)
    }
)


export default service