<template>
  <div class="content-wrapper">
    <div class="query-wrapper">
      <el-form ref="queryFormRef" :model="queryParams">
        <el-row :gutter="15">
          <el-col v-if="isAdminGlobal" :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="教师" name="teacherId" prop="teacherId">
              <var-field-input
                v-model="queryParams.teacherId"
                :recordMeta="{ name: 'teacherId', title: '教师', dataType: 'Integer', selectable: true, options: teacherIdOptions }"/>
            </el-form-item>
          </el-col>
          <el-col :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="课程" name="courseId" prop="courseId">
              <var-field-input
                v-model="queryParams.courseId"
                :recordMeta="{ name: 'courseId', title: '课程', dataType: 'Integer', selectable: true, options: courseIdOptions }"/>
            </el-form-item>
          </el-col>
          <el-col :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="名称" name="name" prop="name">
              <var-field-input
                v-model="queryParams.name"
                :recordMeta="{ name: 'name', title: '名称', dataType: 'String', selectable: false }"/>
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
        <el-button v-if="isTeacherGlobal" type="primary" :icon="h(Plus)" max-height="350" @click="handleClickAdd">添加</el-button>
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
            <el-table-column v-if="isAdminGlobal" prop="teacherId" label="教师编号" align="center">
              <template #default="scope">
                {{ teacherIdOptions.find((item: any) => item.value === scope.row.teacherId).label }}
              </template>
            </el-table-column>
            <el-table-column prop="courseId" label="课程编号" align="center">
              <template #default="scope">
                {{ courseIdOptions.find((item: any) => item.value === scope.row.courseId).label }}
              </template>
            </el-table-column>
            <el-table-column prop="name" label="名称" align="center">
            </el-table-column>
            <el-table-column prop="startTime" label="开始时间" align="center">
            </el-table-column>
            <el-table-column prop="endTime" label="结束时间" align="center">
            </el-table-column>
            <el-table-column prop="description" label="说明" align="center">
            </el-table-column>
            <el-table-column prop="state" label="状态" align="center">
              <template #default="scope">
                {{ stateOptions.find((item: any) => item.value === scope.row.state).label }}
              </template>
            </el-table-column>
            <el-table-column prop="createdTime" label="创建时间" align="center">
            </el-table-column>
            <el-table-column prop="updatedTime" label="更新时间" align="center">
            </el-table-column>
            <el-table-column fixed="right" label="操作" align="center" width="230">
              <template #default="scope">
                <el-link v-if="scope.row.state === 1" :underline="false" @click="() => handleCheck(scope.row)">批改试卷</el-link>
                <el-divider v-if="scope.row.state === 1" direction="vertical" />
                <el-popconfirm v-if="scope.row.state === 0"
                    title="确定发布？"
                    ok-text="确定"
                    cancel-text="取消"
                    @confirm="() => handlePublish(scope.row)"
                >
                  <template #reference>
                    <el-link :underline="false">发布</el-link>
                  </template>
                </el-popconfirm>
                <el-divider v-if="scope.row.state === 0" direction="vertical" />
                <el-link v-if="scope.row.state === 0" :underline="false" @click="() => handleClickEdit(scope.row)">编辑</el-link>
                <el-divider v-if="scope.row.state === 0" direction="vertical" />
                <el-popconfirm v-if="scope.row.state === 0"
                  title="确定删除？"
                  ok-text="确定"
                  cancel-text="取消"
                  @confirm="() => handleDelete(scope.row.id)"
                >
                  <template #reference>
                    <el-link :underline="false">删除</el-link>
                  </template>
                </el-popconfirm>
                <el-divider v-if="scope.row.state === 0" direction="vertical" />
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
    <modal-form ref="modalFormRef" :hide-id-field="true" title="考试信息" @handleAdd="handleAdd" @handleEdit="handleEdit" />
  </div>
</template>

<script setup lang="ts">
import { computed, h, onMounted, ref, unref } from "vue";
import { Delete, ArrowDown, Plus, RefreshRight, Search } from "@element-plus/icons-vue";
import modalForm from "@/components/manage/modalform.vue";
import varFieldInput from "@/components/manage/varfieldinput.vue";
import useCrudActions from "@/hooks/useCrudActions.ts";
import request from "@/utils/request.ts";
import {isAdminGlobal, isTeacherGlobal, userGlobal} from "@/utils/user.ts";
import {ElMessage} from "element-plus";
import router from "@/router";

const queryFormRef = ref<any>();
const modalFormRef = ref<any>();
// TODO 自定义查询表达项
const queryParams = ref({
  teacherId: null,
  courseId: null,
  name: "",
  startTime: [],
  endTime: [],
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

const stateOptions = ref<any>([
  {label: "已创建", value: 0},
  {label: "已发布", value: 1}
]);
const teacherIdOptions = ref<any>([]);
const getTeacherIdOptions = () => {
  request.get("/manage/user/list", {
    role: "teacher",
    pageSize: 1000
  }).then((data: any) => {
    teacherIdOptions.value = data.records.map((item: any) => {
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
    name: "courseId",
    title: "课程",
    dataType: "Integer",
    selectable: true,
    options: courseIdOptions
  },
  {
    name: "name",
    title: "名称",
    dataType: "String",
    selectable: false,
  },
  {
    name: "startTime",
    title: "开始时间",
    dataType: "Date",
    selectable: false,
  },
  {
    name: "endTime",
    title: "结束时间",
    dataType: "Date",
    selectable: false,
  },
  {
    name: "description",
    title: "说明",
    dataType: "BigString",
    selectable: false,
  },
];
const urls = {
  list: "/manage/exam/list",
  detail: "/manage/exam/queryById",
  add: "/manage/exam/add",
  edit: "/manage/exam/edit",
  delete: "/manage/exam/deleteById",
  batchDelete: "/manage/exam/batchDeleteById"
};
let { tableData, pagination, handleList, handleAdd, handleEdit, handleDelete, handleBatchDelete } =
  useCrudActions(urls, queryParams);
onMounted(() => {
  getTeacherIdOptions();
  getCourseIdOptions();
  setTimeout(() => {
    if (!isAdminGlobal) {
      queryParams.value.teacherId = userGlobal.id;
    }
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
const handlePublish = (record: any) => {
  request.get("/manage/studentCourse/list", {
    courseId: record.courseId
  }).then((data: any) => {
    if (data.records.length <= 0) {
      ElMessage.error("还未有同学选该门课程");
      return;
    }
    const postData = new URLSearchParams();
    postData.append("examId", record.id);
    postData.append("studentIds", data.records.map((item: any) => item.studentId).join(","));
    request.post("/manage/studentExam/batchAdd", postData).then(() => {
      ElMessage.success("发布成功");
    })
  });
};
const handleCheck = (record: any) => {
  router.push({path: "/manage/my_exam", query: {"examId": record.id}});
};

// TODO 添加
</script>

<style scoped>
.query-wrapper .el-form-item {
  margin-bottom: 5px;
}
.el-dropdown {
  vertical-align: middle !important;
}
</style>
