<template>
  <!-- 上传图片预览组件 -->
  <el-image preview-teleported :preview-src-list="preview ? [serverUrl] : []" :src="serverUrl" :style="{ width: width + 'px', height: height + 'px'}"/>
</template>

<script setup lang="ts">
import {computed} from "vue";

const props = defineProps({
  url: {
    type: String
  },
  width: {
    type: [Number, String],
    default: 50
  },
  height: {
    type: [Number, String],
    default: 50
  },
  preview: {
    type: Boolean,
    default: true
  }
});
const serverUrl = computed(() => {
  const token = localStorage.getItem("token");
  return "/api/manage/common/preview?path=" + props.url + "&Authorization=" + token;
});

</script>

<style scoped></style>
