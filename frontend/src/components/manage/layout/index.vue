<template>
  <!-- 布局页面，将侧边栏和顶部导航栏固定，中间内容区域可滚动 -->
  <div id="layout">
    <el-container>
      <el-aside style="width: 200px">
        <aside-menu></aside-menu>
      </el-aside>
      <el-container>
        <el-main>
          <main-header></main-header>
          <main-content></main-content>
        </el-main>
        <el-backtop target=".el-main"></el-backtop>
      </el-container>
    </el-container>
    <el-button
      v-if="!isExamPage"
      class="back-top-btn"
      type="danger"
      circle
      @click="router.push({path: '/manage/ai-chat'})"
    >
      <el-icon :size="45"><ChatLineRound /></el-icon>
    </el-button>
  </div>
</template>

<script setup lang="ts">
import "@/style/global.less";
import AsideMenu from "@/components/manage/layout/asideMenu.vue";
import MainHeader from "@/components/manage/layout/mainHeader.vue";
import MainContent from "@/components/manage/layout/mainContent.vue";
import router from "@/router";
import { useRoute } from "vue-router";
import { computed } from "vue";

const route = useRoute();
const isExamPage = computed(() => route.path.startsWith("/manage/answer/"));
</script>

<style scoped>
.el-main {
  padding: 0;
}
.back-top-btn {
  position: fixed;
  right: 50px;
  bottom: 100px;
  width: 45px;
  height: 45px;
  z-index: 1000;
}
</style>
