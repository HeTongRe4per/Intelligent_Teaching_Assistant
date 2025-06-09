<template>
  <div class="item">
    <div class="info">
      <!--  TODO   -->
      <ak-image v-if="currentData?.id" :url="currentData?.img" :width="400" :height="400"/>
      <div class="detail">
        <h1 class="detail-info">名称：{{ currentData?.name }}</h1>
        <div class="btn-group">
          <a-button class="btn" type="primary" @click="">确定</a-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import useCrudActions from "@/hooks/useCrudActions.ts";
import router from "@/router";
import request from "@/utils/request.ts";
import akImage from "@/components/akimage.vue";

const urls = {
  detail: "/manage/video/queryById"
};
let { currentData, handleDetail } = useCrudActions(urls);

let kitchenOptions = ref<any>([]);
onMounted(() => {
  request.get("/manage/kitchen/list", {
    pageSize: 1000
  }).then((data) => {
    kitchenOptions.value = data.records.map((item: any) => {
      return {
        value: item.id,
        label: item.name
      };
    });
  });
});
const stateOptions = ref<any>([
  {label: "空闲", value: 0},
  {label: "预定", value: 1},
  {label: "租用", value: 2},
]);
onMounted(() => {
  const id = router.currentRoute.value.params.id;
  if (id === "") {
    return;
  }
  handleDetail(id);
});
</script>

<style scoped>
.item {
  margin: 4px 0;
  padding: 5px;
  background-color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  .info {
    width: 70%;
    display: flex;
    flex-direction: row;

    .detail {
      display: flex;
      flex-direction: column;
      margin-left: 25px;

      .detail-info {
        text-align: left;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        margin-top: 10px;
        font-size: 18px;
        color: #333;
        font-weight: bold;
      }

      .btn-group {
        display: flex;
        flex-direction: row;
        margin-top: 20px;
      }
    }

  }
}
</style>
