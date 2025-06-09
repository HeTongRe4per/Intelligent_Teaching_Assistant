<template>
  <div class="not-found">
    <el-result status="404" title="404" sub-title="对不起，页面不存在。">
      <template #extra>
        <el-button type="primary" @click="toIndex">返回主页</el-button>
      </template>
    </el-result>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import {userGlobal} from "@/utils/user.ts";

const router = useRouter();
const toIndex = () => {
  if (userGlobal.role === "admin") {
    router.push({ path: '/manage/index' });
  } else {
    router.push({ path: '/index' });
  }
}
</script>

<style scoped>
.not-found {
  text-align: center;
  padding: 2rem;
}

h1 {
  margin-bottom: 1rem;
}

img {
  max-width: 100%;
  height: auto;
  margin-bottom: 1rem;
}
</style>
