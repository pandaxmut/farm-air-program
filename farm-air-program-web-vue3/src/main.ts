import './assets/base.scss'
import 'element-plus/dist/index.css'
import '@kangc/v-md-editor/lib/style/base-editor.css'
import '@kangc/v-md-editor/lib/theme/style/vuepress.css'
import '@kangc/v-md-editor/lib/style/preview-html.css'
import '@kangc/v-md-editor/lib/plugins/mermaid/mermaid.css'

import App from './App.vue'
import router from './router'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// markdown

import VueMarkdownEditor from '@kangc/v-md-editor';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import VMdPreviewHtml from '@kangc/v-md-editor/lib/preview-html';
import Prism from 'prismjs';

VueMarkdownEditor.use(vuepressTheme, {
  Prism,
});



const app = createApp(App);

app.use(VueMarkdownEditor);
app.use(VMdPreviewHtml);

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

app.use(pinia);

app.use(router);
app.use(ElementPlus);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.mount('#app');