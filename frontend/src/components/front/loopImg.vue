<template>
  <el-carousel :interval="4000" type="card" height="400px">
    <el-carousel-item v-for="(item, index) in props.imgs" :key="index" style="display: flex; justify-content: center">
      <ak-image :url="item" :width="800" :height="400" :preview="false" />
    </el-carousel-item>
  </el-carousel>
</template>

<script setup lang="ts">
import akImage from "@/components/akimage.vue";

const props = defineProps({
  imgs: {
    type: Array,
    default: []
  }
});
</script>

<style scoped>
:deep(.slick-slide) {
  text-align: center;
  overflow: hidden;
}
:deep(.slick-arrow.custom-slick-arrow) {
  width: 50px;
  height: 50px;
  font-size: 50px;
  color: black;
  transition: ease all 0.3s;
  opacity: 0.5;
  z-index: 1;
}
:deep(.slick-arrow.custom-slick-arrow:before) {
  display: none;
}
:deep(.slick-arrow.custom-slick-arrow:hover) {
  opacity: 0.8;
}
</style>