import { createApp } from "vue";
import router from "./router";
import App from "./App.vue";
import ElementUI from "element-plus";
import "element-plus/dist/index.css";
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
import { VueEditor } from "vue3-editor";

const app = createApp(App);
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
app.component("VueEditor", VueEditor);
app.use(ElementUI).use(router).mount("#app");
