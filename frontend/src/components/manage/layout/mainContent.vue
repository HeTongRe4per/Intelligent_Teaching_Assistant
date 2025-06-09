<template>
  <router-view v-slot="{ Component }">
    <keep-alive>
      <component :key="new Date().getTime()" :is="Component" />
    </keep-alive>
  </router-view>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import routeHistoryStore from "@/store/routeHistoryStore.ts";
import { onMounted } from "vue";

const router = useRouter();
onMounted(() => {
  routeHistoryStore.state.activeKey = router.currentRoute.value.path;
  routeHistoryStore.handleAddTab({
    index: router.currentRoute.value.path,
    title: router.currentRoute.value.name
  });
});
</script>

<style scoped></style>
