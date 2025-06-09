<template>
  <div id="nav-bar">
    <!-- 侧边栏 -->
    <div id="logo"></div>
    <el-menu
      style="border: none"
      mode="vertical"
      active-text-color="#ffd04b"
      background-color="#001529"
      text-color="#fff"
      :default-openeds="state.openKeys"
      :default-active="state.selectedKey"
      :unique-opened="true"
    >
      <el-sub-menu v-for="item in items" :index="item.index" :hidden="item.hidden">
        <template #title>
          <div class="menu-title">
            <el-icon><component :is="item.icon"/></el-icon>
            <span>{{ item.label }}</span>
          </div>
        </template>
        <template v-for="child in item.children">
          <el-menu-item
             v-if="!child.hidden"
             :index="child.index"
             @click="() => handleMenuItemClick(child)"
          >
             <span>{{ child.label }}</span>
          </el-menu-item>
        </template>
      </el-sub-menu>
    </el-menu>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watch } from "vue";
import routeHistoryStore from "@/store/routeHistoryStore.ts";
import router from "@/router";
import request from "@/utils/request.ts";
import {userGlobal} from "@/utils/user.ts";

const state: { openKeys: Array<any>; selectedKey: any } = reactive({
  openKeys: [],
  selectedKey: null
});
const items = ref([
  {
    index: "/manage",
    label: "主页",
    title: "主页",
    icon: "menu",
    children: [
      {
        index: "/manage/index",
        label: "欢迎页",
        title: "欢迎页"
      }
    ]
  }
]);
watch(
  () => routeHistoryStore.state.activeKey,
  () => {
    state.selectedKey = routeHistoryStore.state.activeKey;
    state.openKeys = [
      routeHistoryStore.state.activeKey?.substring(0, routeHistoryStore.state.activeKey?.lastIndexOf("/"))
    ];
  }
);
onMounted(() => {
  routeHistoryStore.state.activeKey = router.currentRoute.value.path;
  if (routeHistoryStore.state.activeKey !== "/manage/index") {
    routeHistoryStore.handleAddTab({
      index: "/manage/index",
      title: "欢迎页"
    });
  }
  routeHistoryStore.handleAddTab({
    index: router.currentRoute.value.path,
    title: router.currentRoute.value.name
  });
  request.get("/manage/role/list", {code: userGlobal.role}).then((data: any) => {
    const roleId = data.records[0].id;
    request.get("/manage/rolePermission/list", {roleId: roleId, pageSize: 1000}).then((rolePermissionData: any) => {
      request.get("/manage/permission/list", {id: rolePermissionData.records.map((item: any) => item.permissionId).join(","), pageSize: 1000, order: "sequence"}).then((permissionData: any) => {
        const childPermission = permissionData.records.filter((item:any) => item.parentId !== null);
        const parentPermission = permissionData.records.filter((item:any) => item.parentId === null).map((item: any) => {
          return {
            index: "/manage" + item.path,
            label: item.name,
            title: item.name,
            icon: item.icon,
            children: [
              ...childPermission.filter((item1:any) => item1.parentId === item.id).map((item1:any) => {
                return {
                  index: "/manage" + item1.path,
                  label: item1.name,
                  title: item1.name
                };
              })
            ]
          };
        });
        items.value.push(...parentPermission);
      });
    });
  });
});
const handleMenuItemClick = (item: any) => {
  const { index, title } = item;
  routeHistoryStore.handleAddTab({
    index: index,
    title: title
  });
  router.push({ path: index });
};
</script>

<style scoped>
#nav-bar {
  background-color: #001529;
  height: 100vh;
  min-height: 650px;
  padding-top: 16px;
  box-sizing: border-box;

  #logo {
    height: 50px;
    margin: 0 16px;
    background: rgba(255, 255, 255, 0.3);
  }
}
</style>
