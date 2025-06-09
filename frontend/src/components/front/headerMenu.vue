<template>
  <el-menu :default-active="current" mode="horizontal" @click="handleItemClick">
    <el-menu-item
      v-for="item in items"
      :index="item.key"
      @click="handleItemClick"
    >
      <span>{{ item.label }}</span>
    </el-menu-item>
  </el-menu>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const current = ref<any>([]);
const items = ref([
  {
    key: "/index",
    label: "主页",
    title: "主页"
  }
]);
onMounted(() => {
  current.value = [router.currentRoute.value.path];
});
const handleItemClick = (e: any) => {
  if (e.index) {
    router.push(e.index);
  }
};
</script>

<style scoped>
</style>
