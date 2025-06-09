<template>
  <div class="content-wrapper">
    <div class="query-wrapper">
      <el-form ref="queryFormRef" :model="queryParams">
        <el-row :gutter="15">
          <el-col :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="父权限" name="parentId" prop="parentId">
              <var-field-input
                v-model="queryParams.parentId"
                :recordMeta="{ name: 'parentId', title: '父权限', dataType: 'String', selectable: true, options: parentIdOptions }"/>
            </el-form-item>
          </el-col>
          <el-col :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="权限类型" name="type" prop="type">
              <var-field-input
                v-model="queryParams.type"
                :recordMeta="{ name: 'type', title: '权限类型', dataType: 'Integer', selectable: true, options: typeOptions }"/>
            </el-form-item>
          </el-col>
          <el-col :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="权限名称" name="name" prop="name">
              <var-field-input
                v-model="queryParams.name"
                :recordMeta="{ name: 'name', title: '权限名称', dataType: 'String', selectable: false }"/>
            </el-form-item>
          </el-col>
          <el-col :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="权限编码" name="code" prop="code">
              <var-field-input
                v-model="queryParams.code"
                :recordMeta="{ name: 'code', title: '权限编码', dataType: 'String', selectable: false }"/>
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
            <el-table-column prop="parentId" label="父权限编号" align="center">
              <template #default="scope">
                {{ parentIdOptions.find((item: any) => item.value === scope.row.parentId)?.label || '--' }}
              </template>
            </el-table-column>
            <el-table-column prop="type" label="权限类型" align="center">
              <template #default="scope">
                {{ typeOptions.find((item: any) => item.value === scope.row.type).label }}
              </template>
            </el-table-column>
            <el-table-column prop="name" label="权限名称" align="center">
            </el-table-column>
            <el-table-column prop="code" label="权限编码" align="center" width="180">
            </el-table-column>
            <el-table-column prop="path" label="访问路径" align="center" width="250">
            </el-table-column>
            <el-table-column prop="sequence" label="排序序号" align="center">
            </el-table-column>
            <el-table-column prop="icon" label="图标" align="center">
              <template #default="scope">
                <el-icon><component :is="scope.row.icon"/></el-icon>
              </template>
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
    <modal-form ref="modalFormRef" :hide-id-field="true" title="权限信息" @handleAdd="handleAdd" @handleEdit="handleEdit" />
  </div>
</template>

<script setup lang="ts">
import { computed, h, onMounted, ref, unref } from "vue";
import { Delete, ArrowDown, Plus, RefreshRight, Search } from "@element-plus/icons-vue";
import modalForm from "@/components/manage/modalform.vue";
import varFieldInput from "@/components/manage/varfieldinput.vue";
import useCrudActions from "@/hooks/useCrudActions.ts";
import request from "@/utils/request.ts";

const queryFormRef = ref<any>();
const modalFormRef = ref<any>();
// TODO 自定义查询表达项
const queryParams = ref({
  parentId: "",
  type: null,
  name: "",
  code: "",
  path: "",
  icon: "",
  description: "",
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

const parentIdOptions = ref<any>([]);
const getParentIdOptions = () => {
  request.get("/manage/permission/listParent", {
    pageSize: 1000
  }).then((data: any) => {
    parentIdOptions.value = data.records.map((item: any) => {
      return {
        label: item.name,
        value: item.id
      };
    });
  });
};
const typeOptions = ref<any>([
  {label: "菜单", value: 1},
  {label: "按钮", value: 2},
  {label: "API", value: 3},
]);
const iconOptions = ref<any>([
  {label: "课程图标", value: "folderOpened"},
  {label: "考试图标", value: "document"},
  {label: "系统图标", value: "setting"},
]);
// TODO 自定义需要编辑的项
const recordMetas = [
  {
    name: "id",
    title: "编号",
    dataType: "Integer",
    selectable: false,
  },
  {
    name: "parentId",
    title: "父权限",
    dataType: "Integer",
    selectable: true,
    options: parentIdOptions,
    required: false
  },
  {
    name: "type",
    title: "权限类型",
    dataType: "Integer",
    selectable: true,
    options: typeOptions
  },
  {
    name: "name",
    title: "权限名称",
    dataType: "String",
    selectable: false,
  },
  {
    name: "code",
    title: "权限编码",
    dataType: "String",
    selectable: false,
  },
  {
    name: "path",
    title: "访问路径",
    dataType: "String",
    selectable: false,
  },
  {
    name: "icon",
    title: "图标",
    dataType: "String",
    selectable: true,
    options: iconOptions,
    required: false
  },
  {
    name: "sequence",
    title: "排序序号",
    dataType: "Integer",
    selectable: false
  },
  {
    name: "description",
    title: "权限描述",
    dataType: "BigString",
    selectable: false,
    required: false
  },
];
const urls = {
  list: "/manage/permission/list",
  detail: "/manage/permission/queryById",
  add: "/manage/permission/add",
  edit: "/manage/permission/edit",
  delete: "/manage/permission/deleteById",
  batchDelete: "/manage/permission/batchDeleteById"
};
let { tableData, pagination, handleList, handleAdd, handleEdit, handleDelete, handleBatchDelete } =
  useCrudActions(urls, queryParams);
onMounted(() => {
  getParentIdOptions();
  setTimeout(() => {
    handleList();
  }, 1000);
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
  modalFormRef.value.currentRecord = {
    sequence: 0
  };
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
</script>

<style scoped>
.query-wrapper .el-form-item {
  margin-bottom: 5px;
}
.el-dropdown {
  vertical-align: middle !important;
}
</style>
