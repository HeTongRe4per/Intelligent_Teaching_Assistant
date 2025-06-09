<template>
  <div class="content-wrapper">
    <div class="query-wrapper">
      <el-form ref="queryFormRef" :model="queryParams">
        <el-row :gutter="15">
          <el-col :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="用户名" name="username" prop="username">
              <var-field-input
                v-model="queryParams.username"
                :recordMeta="{ name: 'username', title: '用户名', dataType: 'String', selectable: false }"/>
            </el-form-item>
          </el-col>
          <el-col :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="真实姓名" name="realName" prop="realName">
              <var-field-input
                v-model="queryParams.realName"
                :recordMeta="{ name: 'realName', title: '真实姓名', dataType: 'String', selectable: false }"/>
            </el-form-item>
          </el-col>
          <el-col :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="手机号码" name="phone" prop="phone">
              <var-field-input
                v-model="queryParams.phone"
                :recordMeta="{ name: 'phone', title: '手机号码', dataType: 'String', selectable: false }"/>
            </el-form-item>
          </el-col>
          <el-col v-if="currentRole == 'normal'" :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="学号" name="stuNum" prop="stuNum">
              <var-field-input
                v-model="queryParams.stuNum"
                :recordMeta="{ name: 'stuNum', title: '学号', dataType: 'String', selectable: false }"/>
            </el-form-item>
          </el-col>
          <el-col v-if="currentRole == 'normal'" :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="年级" name="grade" prop="grade">
              <var-field-input
                v-model="queryParams.grade"
                :recordMeta="{ name: 'grade', title: '年级', dataType: 'String', selectable: true, options: gradeOptions }"/>
            </el-form-item>
          </el-col>
          <el-col v-if="currentRole == 'normal'" :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="班级" name="clazz" prop="clazz">
              <var-field-input
                v-model="queryParams.clazz"
                :recordMeta="{ name: 'clazz', title: '班级', dataType: 'String', selectable: false }"/>
            </el-form-item>
          </el-col>
          <el-col v-if="currentRole == 'teacher'" :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="部门" name="department" prop="department">
              <var-field-input
                v-model="queryParams.department"
                :recordMeta="{ name: 'department', title: '部门', dataType: 'String', selectable: false }"/>
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
            <el-table-column prop="username" label="用户名" align="center">
            </el-table-column>
            <el-table-column prop="realName" label="真实姓名" align="center">
            </el-table-column>
            <el-table-column prop="email" label="电子邮箱" align="center">
            </el-table-column>
            <el-table-column prop="phone" label="手机号码" align="center">
            </el-table-column>
            <el-table-column prop="avatar" label="头像URL" align="center">
              <template #default="scope">
                <ak-image :url="scope.row.avatar" />
              </template>
            </el-table-column>
            <el-table-column v-if="currentRole == 'normal'" prop="stuNum" label="学号" align="center">
            </el-table-column>
            <el-table-column v-if="currentRole == 'normal'" prop="grade" label="年级" align="center">
              <template #default="scope">
                {{ gradeOptions.find((item: any) => item.value === scope.row.grade)?.label }}
              </template>
            </el-table-column>
            <el-table-column v-if="currentRole == 'normal'" prop="clazz" label="班级" align="center">
            </el-table-column>
            <el-table-column v-if="currentRole == 'teacher'" prop="department" label="部门" align="center">
            </el-table-column>
            <el-table-column fixed="right" label="操作" align="center" width="230">
              <template #default="scope">
                <el-link :underline="false" @click="() => handleClickEditRole(scope.row)">更改角色</el-link>
                <el-divider direction="vertical" />
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
    <modal-form ref="modalFormRef" :hide-id-field="true" title="用户信息" @handleAdd="handleAdd" @handleEdit="handleEdit" />
  </div>
</template>

<script setup lang="ts">
import {computed, h, onMounted, ref, unref, watch} from "vue";
import { Delete, ArrowDown, Plus, RefreshRight, Search } from "@element-plus/icons-vue";
import modalForm from "@/components/manage/modalform.vue";
import varFieldInput from "@/components/manage/varfieldinput.vue";
import useCrudActions from "@/hooks/useCrudActions.ts";
import akImage from "@/components/akimage.vue";
import router from "@/router";

const queryFormRef = ref<any>();
const modalFormRef = ref<any>();
const currentRole = ref("");
// TODO 自定义查询表达项
const queryParams = ref({
  username: "",
  password: "",
  realName: "",
  email: "",
  phone: "",
  avatar: "",
  role: "",
  stuNum: "",
  grade: "",
  clazz: "",
  department: "",
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

const roleOptions = ref<any>([
  {label: "学生", value: "normal"},
  {label: "教师", value: "teacher"},
  {label: "系统管理员", value: "admin"},
]);
const gradeOptions = ref<any>([
  {label: "21级", value: "grade21"},
  {label: "22级", value: "grade22"},
  {label: "23级", value: "grade23"},
  {label: "24级", value: "grade24"},
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
    name: "username",
    title: "用户名",
    dataType: "String",
    selectable: false,
  },
  {
    name: "password",
    title: "密码",
    dataType: "String",
    selectable: false,
  },
  {
    name: "realName",
    title: "真实姓名",
    dataType: "String",
    selectable: false,
  },
  {
    name: "email",
    title: "电子邮箱",
    dataType: "String",
    selectable: false,
  },
  {
    name: "phone",
    title: "手机号码",
    dataType: "String",
    selectable: false,
  },
  {
    name: "avatar",
    title: "头像URL",
    dataType: "Picture",
    selectable: false,
  }
];
const urls = {
  list: "/manage/user/list",
  detail: "/manage/user/queryById",
  add: "/manage/user/add",
  edit: "/manage/user/edit",
  delete: "/manage/user/deleteById",
  batchDelete: "/manage/user/batchDeleteById"
};
let { tableData, pagination, handleList, handleAdd, handleEdit, handleDelete, handleBatchDelete } =
  useCrudActions(urls, queryParams);
watch(
    () => router.currentRoute.value.params.role,
    () => {
      const role = router.currentRoute.value.params.role+"";
      currentRole.value = role;
      queryParams.value.role = role;
      handleList();
    }
);
onMounted(() => {
  setTimeout(() => {
    const role = router.currentRoute.value.params.role+"";
    currentRole.value = role;
    queryParams.value.role = role;
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
  if (currentRole.value === "normal") {
    modalFormRef.value.recordMetas = [
      ... recordMetas,
      {
        name: "stuNum",
        title: "学号",
        dataType: "String",
        selectable: false,
      },
      {
        name: "grade",
        title: "年级",
        dataType: "String",
        selectable: true,
        options: gradeOptions
      },
      {
        name: "clazz",
        title: "班级",
        dataType: "String",
        selectable: false,
      }
    ];
  } else if (currentRole.value === "teacher") {
    modalFormRef.value.recordMetas = [
      ... recordMetas,
      {
        name: "department",
        title: "部门",
        dataType: "String",
        selectable: false,
      }
    ];
  } else {
    modalFormRef.value.recordMetas = recordMetas;
  }
  modalFormRef.value.currentRecord = {
    role: currentRole.value
  };
};
const handleClickEditRole = (record: any) => {
  modalFormRef.value.editable = true;
  modalFormRef.value.isEdit = true;
  modalFormRef.value.visible = true;
  modalFormRef.value.recordMetas = [
    {
      name: "role",
      title: "角色",
      dataType: "String",
      selectable: true,
      options: roleOptions
    }
  ];
  modalFormRef.value.currentRecord = record;
};
const handleClickEdit = (record: any) => {
  modalFormRef.value.editable = true;
  modalFormRef.value.isEdit = true;
  modalFormRef.value.visible = true;
  if (currentRole.value === "normal") {
    modalFormRef.value.recordMetas = [
      ... recordMetas,
      {
        name: "stuNum",
        title: "学号",
        dataType: "String",
        selectable: false,
      },
      {
        name: "grade",
        title: "年级",
        dataType: "String",
        selectable: true,
        options: gradeOptions
      },
      {
        name: "clazz",
        title: "班级",
        dataType: "String",
        selectable: false,
      }
    ];
  } else if (currentRole.value === "teacher") {
    modalFormRef.value.recordMetas = [
      ... recordMetas,
      {
        name: "department",
        title: "部门",
        dataType: "String",
        selectable: false,
      }
    ];
  } else {
    modalFormRef.value.recordMetas = recordMetas;
  }
  modalFormRef.value.currentRecord = record;
};
const handleClickDetail = (record: any) => {
  modalFormRef.value.editable = false;
  modalFormRef.value.isEdit = false;
  modalFormRef.value.visible = true;
  if (currentRole.value === "normal") {
    modalFormRef.value.recordMetas = [
      ... recordMetas,
      {
        name: "stuNum",
        title: "学号",
        dataType: "String",
        selectable: false,
      },
      {
        name: "grade",
        title: "年级",
        dataType: "String",
        selectable: true,
        options: gradeOptions
      },
      {
        name: "clazz",
        title: "班级",
        dataType: "String",
        selectable: false,
      }
    ];
  } else if (currentRole.value === "teacher") {
    modalFormRef.value.recordMetas = [
      ... recordMetas,
      {
        name: "department",
        title: "部门",
        dataType: "String",
        selectable: false,
      }
    ];
  } else {
    modalFormRef.value.recordMetas = recordMetas;
  }
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
