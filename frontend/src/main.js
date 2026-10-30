import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import AuthPlugin from './plugins/AuthPlugin'
import './assets/base.css'

const app = createApp(App)

app.use(router)
app.use(createPinia())
app.use(AuthPlugin)
app.mount('#app')
