<template>
  <el-config-provider :locale="zhCn">
    <router-view :to="{ path: '/index' }" />
  </el-config-provider>
</template>

<script setup lang="ts">
import { zhCn } from "element-plus/es/locale/index";
</script>

<style scoped></style>
