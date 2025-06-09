<template>
  <div>
    <div class="loop-img">
      <loop-img :imgs="tableData.map((item: any) => item.img)" />
    </div>
    <div class="header-menu">
      <header-menu />
    </div>

    <div class="item-list">
      <div class="item" v-for="item in tableData" :key="item.id">
        <router-link :to="{path: `/item_index/${item.id}`}">
          <div class="mask" />
        </router-link>
        <ak-image :url="item.img" :width="300" :height="150" :preview="false" />
        <div class="info">
          <h1 class="title">{{item.name}}</h1>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import useCrudActions from "@/hooks/useCrudActions.ts";
import loopImg from "@/components/front/loopImg.vue";
import headerMenu from "@/components/front/headerMenu.vue";
import akImage from "@/components/akimage.vue";

const urls = {
  list: "/manage/video/list"
};
const queryParams = ref({
  name: ""
});
let { tableData, handleList } = useCrudActions(urls, queryParams);
onMounted(() => {
  handleList();
});
</script>

<style scoped>
.item-list {
  margin-top: 15px;
  display: flex;
  flex-wrap: wrap;
  padding: 7px;
  background-color: #eee;
  overflow: auto;

  .item {
    width: 24.2%;
    border: 1px solid #ccc;
    margin: 4px 0;
    padding: 5px;
    background-color: #fff;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    position: relative;

    .mask {
      position: absolute;
      width: 100%;
      height: 100%;
      left: 0;
      top: 0;
      opacity: 0.5;
      z-index: 1;
    }

    .info {
      width: 100%;
      display: flex;
      flex-direction: column;

      .title {
        text-align: center;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        margin-top: 10px;
        font-size: 18px;
        color: #333;
        font-weight: bold;
      }

    }
  }
}
</style>
