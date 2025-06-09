<template>
  <el-dialog v-model="visible" :width="800" title="设置权限" destroy-on-close closable @close="visible = false">
    <div class="flex gap-2 mt-4">
      <el-check-tag v-for="item in tableData" :checked="rolePermissionTableData.filter((rolePermission: any) => rolePermission.permissionId===item.id).length>0" @change="(status: boolean) => handleChange(item.id, status)">{{ item.name }}</el-check-tag>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="visible = false">关闭</el-button>
        <el-button type="primary" @click="visible = false">确认</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import request from "@/utils/request";
import {ref, watch} from "vue";
import useCrudActions from "@/hooks/useCrudActions.ts";

const rolePermissionTableData = ref<any>([]);
const roleId = ref<any>(null);
const visible = ref<Boolean>(false);
const urls = {
  list: "/manage/permission/list",
  add: "/manage/rolePermission/add",
  delete: "/manage/rolePermission/deleteById"
};
let { tableData, pagination, handleList, handleAdd, handleDelete } = useCrudActions(urls, {});
watch(() => visible.value, () => {
  if (visible.value) {
    pagination.value.pageSize = 1000;
    handleList();
    setTimeout(() => {
      handleListRolePermission();
    }, 500);
  }
});
const handleChange = (id: number, status: boolean) => {
  if (status) {
    handleAdd({roleId: roleId.value, permissionId: id});
  } else {
    handleDelete(rolePermissionTableData.value.find((item:any) => item.permissionId === id)?.id);
  }
  handleListRolePermission();
};
const handleListRolePermission = () => {
  request.get("/manage/rolePermission/list", {roleId: roleId.value, pageSize: 1000}).then((data: any) => {
    rolePermissionTableData.value = data.records;
  });
};
defineExpose({
  roleId,
  visible
});
</script>

<style scoped></style>
