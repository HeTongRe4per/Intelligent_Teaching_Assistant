<template>
  <div class="content-wrapper">
    <div class="query-wrapper">
      <el-form ref="queryFormRef" :model="queryParams">
        <el-row :gutter="15">
                  <el-col :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="用户" name="userId" prop="userId">
              <var-field-input
                v-model="queryParams.userId"
                :recordMeta="{ name: 'userId', title: '用户', dataType: 'Integer', selectable: true, options: userIdOptions }"/>
            </el-form-item>
          </el-col>
          <el-col :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="角色" name="roleId" prop="roleId">
              <var-field-input
                v-model="queryParams.roleId"
                :recordMeta="{ name: 'roleId', title: '角色', dataType: 'Integer', selectable: true, options: roleIdOptions }"/>
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
        <el-button type="primary" :icon="h(Plus)" max-height="350" @click="handleClickAdd">添加</el-button>
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
            <el-table-column prop="userId" label="用户编号" align="center">
              <template #default="scope">
                {{ userIdOptions.find((item: any) => item.value === scope.row.userId).label }}
              </template>
            </el-table-column>
            <el-table-column prop="roleId" label="角色编号" align="center">
              <template #default="scope">
                {{ roleIdOptions.find((item: any) => item.value === scope.row.roleId).label }}
              </template>
            </el-table-column>
            <el-table-column prop="createdTime" label="创建时间" align="center">
            </el-table-column>
            <el-table-column prop="updatedTime" label="更新时间" align="center">
            </el-table-column>
            <el-table-column fixed="right" label="操作" align="center" width="230">
              <template #default="scope">
                <el-link :underline="false" @click="() => handleClickEdit(scope.row)">编辑</el-link>
                <el-divider direction="vertical" />
                <el-popconfirm
                  title="确定删除？"
                  ok-text="确定"
                  cancel-text="取消"
                  @confirm="() => handleDelete(scope.row.id)"
                >
                  <template #reference>
                    <el-link :underline="false">删除</el-link>
                  </template>
                </el-popconfirm>
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
    <modal-form ref="modalFormRef" :hide-id-field="true" title="用户角色关联表信息" @handleAdd="handleAdd" @handleEdit="handleEdit" />
  </div>
</template>

<script setup lang="ts">
import { computed, h, onMounted, ref, unref } from "vue";
import { Delete, ArrowDown, Plus, RefreshRight, Search } from "@element-plus/icons-vue";
import modalForm from "@/components/manage/modalform.vue";
import varFieldInput from "@/components/manage/varfieldinput.vue";
import useCrudActions from "@/hooks/useCrudActions.ts";
import request from "@/utils/request.ts";
import akImage from "@/components/akimage.vue";

const queryFormRef = ref<any>();
const modalFormRef = ref<any>();
// TODO 自定义查询表达项
const queryParams = ref({
  userId: null,
  roleId: null,
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

const userIdOptions = ref<any>([]);
const getUserIdOptions = () => {
  request.get("/manage/user/list", {
    pageSize: 1000
  }).then((data: any) => {
    userIdOptions.value = data.records.map((item: any) => {
      return {
        label: item.username,
        value: item.id
      };
    });
  });
};
const roleIdOptions = ref<any>([]);
const getRoleIdOptions = () => {
  request.get("/manage/role/list", {
    pageSize: 1000
  }).then((data: any) => {
    roleIdOptions.value = data.records.map((item: any) => {
      return {
        label: item.name,
        value: item.id
      };
    });
  });
};
// TODO 自定义需要编辑的项
const recordMetas = [
  {
    name: "id",
    title: "编号",
    dataType: "Integer",
    selectable: false,
  },
  {
    name: "userId",
    title: "用户",
    dataType: "Integer",
    selectable: true,
    options: userIdOptions
  },
  {
    name: "roleId",
    title: "角色",
    dataType: "Integer",
    selectable: true,
    options: roleIdOptions
  },
];
const urls = {
  list: "/manage/userRole/list",
  detail: "/manage/userRole/queryById",
  add: "/manage/userRole/add",
  edit: "/manage/userRole/edit",
  delete: "/manage/userRole/deleteById",
  batchDelete: "/manage/userRole/batchDeleteById"
};
let { tableData, pagination, handleList, handleAdd, handleEdit, handleDelete, handleBatchDelete } =
  useCrudActions(urls, queryParams);
onMounted(() => {
  getUserIdOptions();
  getRoleIdOptions();
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
const handleClickAdd = () => {
  modalFormRef.value.editable = true;
  modalFormRef.value.isEdit = false;
  modalFormRef.value.visible = true;
  modalFormRef.value.recordMetas = recordMetas;
  modalFormRef.value.currentRecord = {};
};
const handleClickEdit = (record: any) => {
  modalFormRef.value.editable = true;
  modalFormRef.value.isEdit = true;
  modalFormRef.value.visible = true;
  modalFormRef.value.recordMetas = recordMetas;
  modalFormRef.value.currentRecord = record;
};
const handleClickDetail = (record: any) => {
  modalFormRef.value.editable = false;
  modalFormRef.value.isEdit = false;
  modalFormRef.value.visible = true;
  modalFormRef.value.recordMetas = recordMetas;
  modalFormRef.value.currentRecord = record;
};
const handleClickDownload = (path: string) => {
  request.download("/manage/common/preview?path=" + path).then((data: any) => {
    const url = window.URL.createObjectURL(data);
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', path.substring(path.lastIndexOf("/") + 1));
    document.body.appendChild(link);
    link.click();
  })
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
