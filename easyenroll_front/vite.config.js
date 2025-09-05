// 从vite包中导入 defineConfig 函数， 提供了配置的类型提示和智能感知
import { defineConfig } from 'vite'
// 导入官方 Vue 插件
import vue from '@vitejs/plugin-vue'
// 导入 Node.js 的 path 模块，用于处理文件和目录路径
import path from 'path'

// https://vite.dev/config/
export default defineConfig({
  // 使用 Vue 插件，这使 Vite 能够正确处理 .vue 单文件组件
  plugins: [vue()],

  // 开发服务器配置
  server: {
    // 代理配置
    proxy: {
      // 配置 '/api' 路径的代理
      '/api': {
        // 目标服务器地址
        // 所有以 '/api' 开头的请求将被代理到 http://localhost:8088  后台的服务接口
        target: 'http://localhost:8088',
        changeOrigin: true,
        // 路径重写函数
        // 这里移除了 '/api' 前缀，所以实际请求的路径是 http://localhost:8080/xxx 而不是 http://localhost:8080/api/xxx
        rewrite: (path) => path.replace(/^\/api/, '')

        //加上api访问后端接口，不加api就是前端路由
        //并且后端也无需加api
      }
    }
  }
})
