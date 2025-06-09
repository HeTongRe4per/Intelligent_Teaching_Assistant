<template>
  <div class="content-wrapper">
    <div class="query-wrapper">
      <el-form ref="queryFormRef" :model="queryParams">
        <el-row :gutter="15">
                  <el-col :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="角色名称" name="name" prop="name">
              <var-field-input
                v-model="queryParams.name"
                :recordMeta="{ name: 'name', title: '角色名称', dataType: 'String', selectable: false }"/>
            </el-form-item>
          </el-col>
          <el-col :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="角色编码" name="code" prop="code">
              <var-field-input
                v-model="queryParams.code"
                :recordMeta="{ name: 'code', title: '角色编码', dataType: 'String', selectable: false }"/>
            </el-form-item>
          </el-col>
          <el-col :xs="6" :sm="6" :md="6" :lg="6" :xl="6">
            <el-button type="primary" :icon="h(Search)" @click="handleSubmit">查询</el-button>
            <el-button style="margin-left: 10px" :icon="h(RefreshRight)" @click="handleReset">重置</el-button>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="table-wrapper">
      <div class="table-header-wrapper">
        <el-popconfirm
          v-if="selectedRowKeys.length > 0"
          title="确定删除选择行？"
          ok-text="确定"
          cancel-text="取消"
          @confirm="() => handleBatchDelete(selectedRowKeys.join(','))"
        >
          <el-button style="margin-left: 10px" :icon="h(Delete)">删除</el-button>
        </el-popconfirm>
      </div>
      <div class="table-body-wrapper">
        <el-table :data="tableData" :row-key="(row: any) => row.id" @selection-change="tableRowSelection">
          <template #default>
            <el-table-column type="selection" width="55" />
            <el-table-column prop="id" label="编号" align="center">
            </el-table-column>
            <el-table-column prop="name" label="角色名称" align="center">
            </el-table-column>
            <el-table-column prop="code" label="角色编码" align="center">
            </el-table-column>
            <el-table-column prop="createdTime" label="创建时间" align="center">
            </el-table-column>
            <el-table-column prop="updatedTime" label="更新时间" align="center">
            </el-table-column>
            <el-table-column fixed="right" label="操作" align="center" width="230">
              <template #default="scope">
                <el-link :underline="false" @click="() => handleClickEditPermission(scope.row)">设置权限</el-link>
                <el-divider direction="vertical" />
                <el-dropdown>
                  <el-link :underline="false">
                    更多
                    <el-icon>
                      <ArrowDown />
                    </el-icon>
                  </el-link>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item :key="1">
                        <el-link :underline="false" type="primary" @click="() => handleClickDetail(scope.row)">详情</el-link>
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </template>
            </el-table-column>
          </template>
          <template #append>
            <el-pagination
              :current-page="pagination.current"
              :page-size="pagination.pageSize"
              :total="pagination.total"
              :page-sizes="pagination.pageSizeOptions"
              @size-change="pagination.onShowSizeChange"
              @current-change="pagination.onChange"
            />
          </template>
          <template #empty>
            <div class="empty-data">
              <p>暂无数据</p>
            </div>
          </template>
        </el-table>
      </div>
    </div>
    <modal-form ref="modalFormRef" :hide-id-field="true" title="角色信息" @handleAdd="handleAdd" @handleEdit="handleEdit" />
    <permission-box ref="permissionRef" />
  </div>
</template>

<script setup lang="ts">
import { computed, h, onMounted, ref, unref } from "vue";
import { Delete, ArrowDown, RefreshRight, Search } from "@element-plus/icons-vue";
import modalForm from "@/components/manage/modalform.vue";
import varFieldInput from "@/components/manage/varfieldinput.vue";
import useCrudActions from "@/hooks/useCrudActions.ts";
import permissionBox from "@/components/permissionbox.vue";

const permissionRef = ref<any>();
const queryFormRef = ref<any>();
const modalFormRef = ref<any>();
// TODO 自定义查询表达项
const queryParams = ref({
  name: "",
  code: "",
  createdTime: [],
  updatedTime: [],
});
const selectedRowKeys = ref<any>([]);
const tableRowSelection = computed(() => {
  return {
    selectedRowKeys: unref(selectedRowKeys),
    onChange(_selectedRowKeys: string[]) {
      selectedRowKeys.value = _selectedRowKeys;
    }
  };
});

// TODO 自定义需要编辑的项
const recordMetas = [
  {
    name: "id",
    title: "编号",
    dataType: "Integer",
    selectable: false,
  },
  {
    name: "name",
    title: "角色名称",
    dataType: "String",
    selectable: false,
  },
  {
    name: "code",
    title: "角色编码",
    dataType: "String",
    selectable: false,
  },
];
const urls = {
  list: "/manage/role/list",
  detail: "/manage/role/queryById",
  add: "/manage/role/add",
  edit: "/manage/role/edit",
  delete: "/manage/role/deleteById",
  batchDelete: "/manage/role/batchDeleteById"
};
let { tableData, pagination, handleList, handleAdd, handleEdit, handleBatchDelete } =
  useCrudActions(urls, queryParams);
onMounted(() => {
  setTimeout(() => {
    handleList();
  }, 500);
});
const handleSubmit = () => {
  handleList();
};
const handleReset = () => {
  queryFormRef.value.resetFields();
  handleSubmit();
};
const handleClickDetail = (record: any) => {
  modalFormRef.value.editable = false;
  modalFormRef.value.isEdit = false;
  modalFormRef.value.visible = true;
  modalFormRef.value.recordMetas = recordMetas;
  modalFormRef.value.currentRecord = record;
};
const handleClickEditPermission = (record: any) => {
  permissionRef.value.roleId = record.id;
  permissionRef.value.visible = true;
};
</script>

<style scoped>
.query-wrapper .el-form-item {
  margin-bottom: 5px;
}
.el-dropdown {
  vertical-align: middle !important;
}
</style>
