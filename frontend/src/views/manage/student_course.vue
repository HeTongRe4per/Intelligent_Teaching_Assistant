<template>
  <div class="content-wrapper">
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
            <el-table-column prop="courseId" label="课程编号" align="center">
              <template #default="scope">
                {{ courseIdOptions.find((item: any) => item.value === scope.row.courseId).label }}
              </template>
            </el-table-column>
            <el-table-column prop="createdTime" label="创建时间" align="center">
            </el-table-column>
            <el-table-column prop="updatedTime" label="更新时间" align="center">
            </el-table-column>
            <el-table-column fixed="right" label="操作" align="center" width="230">
              <template #default="scope">
                <el-popconfirm
                  title="确定退课？"
                  ok-text="确定"
                  cancel-text="取消"
                  @confirm="() => handleDelete(scope.row.id)"
                >
                  <template #reference>
                    <el-link :underline="false">退课</el-link>
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
    <modal-form ref="modalFormRef" :hide-id-field="true" title="学生选课信息" @handleAdd="handleAdd" @handleEdit="handleEdit" />
  </div>
</template>

<script setup lang="ts">
import { computed, h, onMounted, ref, unref } from "vue";
import { Delete, ArrowDown } from "@element-plus/icons-vue";
import modalForm from "@/components/manage/modalform.vue";
import useCrudActions from "@/hooks/useCrudActions.ts";
import request from "@/utils/request.ts";
import {userGlobal} from "@/utils/user.ts";

const queryFormRef = ref<any>();
const modalFormRef = ref<any>();
// TODO 自定义查询表达项
const queryParams = ref({
  studentId: null,
  courseId: null,
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

const studentIdOptions = ref<any>([]);
const getStudentIdOptions = () => {
  request.get("/manage/user/list", {
    role: "normal",
    pageSize: 1000
  }).then((data: any) => {
    studentIdOptions.value = data.records.map((item: any) => {
      return {
        label: item.username,
        value: item.id
      };
    });
  });
};
const courseIdOptions = ref<any>([]);
const getCourseIdOptions = () => {
  request.get("/manage/course/list", {
    pageSize: 1000
  }).then((data: any) => {
    courseIdOptions.value = data.records.map((item: any) => {
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
    name: "studentId",
    title: "学生",
    dataType: "Integer",
    selectable: true,
    options: studentIdOptions
  },
  {
    name: "courseId",
    title: "课程",
    dataType: "Integer",
    selectable: true,
    options: courseIdOptions
  },
];
const urls = {
  list: "/manage/studentCourse/list",
  detail: "/manage/studentCourse/queryById",
  add: "/manage/studentCourse/add",
  edit: "/manage/studentCourse/edit",
  delete: "/manage/studentCourse/deleteById",
  batchDelete: "/manage/studentCourse/batchDeleteById"
};
let { tableData, pagination, handleList, handleAdd, handleEdit, handleDelete, handleBatchDelete } =
  useCrudActions(urls, queryParams);
onMounted(() => {
  getStudentIdOptions();
  getCourseIdOptions();
  setTimeout(() => {
    queryParams.value.studentId = userGlobal.id;
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
