<template>
  <router-view v-slot="{ Component }">
    <keep-alive>
      <component :key="new Date().getTime()" :is="Component" />
    </keep-alive>
  </router-view>
</template>

<script setup lang="ts">
</script>

<style scoped></style>
