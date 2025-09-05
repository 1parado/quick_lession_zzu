// 从vue包中导入createApp函数，用于创建Vue应用实例
import { createApp } from 'vue'
// 导入全局样式文件，是应用的主CSS文件，会影响整个应用
import './style.css'
// 导入根组件App，所有其他组件都是它的子组件
import App from './App.vue'
// 导入路由配置，router.js中通常定义了应用的所有路由规则
import router from './router'
// 导入 Element Plus 组件库
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
// 导入 Element Plus 的默认样式文件
import 'element-plus/dist/index.css'

import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}


app.use(router)
app.use(ElementPlus, {
    locale: zhCn,
})

app.mount('#app')
