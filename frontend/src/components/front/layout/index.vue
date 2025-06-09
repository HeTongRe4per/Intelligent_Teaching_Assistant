<template>
  <div id="layout">
    <el-row has-sider>
      <el-col :span="24">
        <el-row>
          <el-col :span="24">
            <main-header></main-header>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <main-content style="min-height: 500px;"></main-content>
          </el-col>
        </el-row>
        <el-row style="display: flex; justify-content: center; font-size: 18px"> springboot-front ©2024 created by cwq</el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import "@/style/global.less";
import mainHeader from "@/components/front/layout/mainHeader.vue";
import mainContent from "@/components/front/layout/mainContent.vue";
</script>

<style scoped>
#layout {
  min-width: 800px;
}
</style>
