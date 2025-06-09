<template>
  <div class="content-wrapper">
    <div class="query-wrapper">
      <el-form ref="queryFormRef" :model="queryParams">
        <el-row :gutter="15">
                  <el-col :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="教师" name="teacherId" prop="teacherId">
              <var-field-input
                v-model="queryParams.teacherId"
                :recordMeta="{ name: 'teacherId', title: '教师', dataType: 'Integer', selectable: true, options: teacherIdOptions }"/>
            </el-form-item>
          </el-col>
          <el-col :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="年级" name="grade" prop="grade">
              <var-field-input
                v-model="queryParams.grade"
                :recordMeta="{ name: 'grade', title: '年级', dataType: 'String', selectable: true, options: gradeOptions }"/>
            </el-form-item>
          </el-col>
          <el-col :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="名称" name="name" prop="name">
              <var-field-input
                v-model="queryParams.name"
                :recordMeta="{ name: 'name', title: '名称', dataType: 'String', selectable: false }"/>
            </el-form-item>
          </el-col>
          <el-col :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="代码" name="code" prop="code">
              <var-field-input
                v-model="queryParams.code"
                :recordMeta="{ name: 'code', title: '代码', dataType: 'String', selectable: false }"/>
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
      </div>
      <div class="table-body-wrapper">
        <el-table :data="chunkedData" :show-header="false" :border="false" class="card-table no-hover-table">
          <template #default>
            <el-table-column>
              <template #default="scope">
                <div class="card-row">
                  <el-card v-for="(item, index) in scope.row" :key="index" class="card-item">
                    <template #header>
                      <div class="card-header">
                        <div>{{typeOptions.find((item1: any) => item1.value === item.type).label}} - {{ item.name }}</div>
                        <div>课程代码：{{item.code}}</div>
                      </div>
                    </template>
                    <ak-image :url="item.img" width="100%" :preview="false" style="height: 250px !important;" @click="() => handleClickDetail(item)"/>
<!--                    <div class="card-content">-->
<!--                      &lt;!&ndash; TODO &ndash;&gt;-->
<!--                    </div>-->
                    <template #footer>
                      <el-popconfirm v-if="isNormalGlobal" title="确定删除？" ok-text="确定" cancel-text="取消" @confirm="() => handleSelectCourse(item.id)">
                        <template #reference>
                          <el-link :underline="false">选课</el-link>
                        </template>
                      </el-popconfirm>
                      <el-divider v-if="isNormalGlobal" direction="vertical" />
                      <el-link v-if="!isNormalGlobal" :underline="false" @click="() => handleClickEdit(item)">编辑</el-link>
                      <el-divider v-if="!isNormalGlobal" direction="vertical" />
                      <el-popconfirm v-if="!isNormalGlobal" title="确定删除？" ok-text="确定" cancel-text="取消" @confirm="() => handleDelete(item.id)">
                        <template #reference>
                          <el-link :underline="false">删除</el-link>
                        </template>
                      </el-popconfirm>
                      <el-divider v-if="!isNormalGlobal" direction="vertical" />
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
                              <el-link :underline="false" type="primary" @click="() => handleClickDetail(item)">详情</el-link>
                            </el-dropdown-item>
                          </el-dropdown-menu>
                        </template>
                      </el-dropdown>
                    </template>
                  </el-card>
                </div>
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
    <modal-form ref="modalFormRef" :hide-id-field="true" title="课程信息" @handleAdd="handleAdd" @handleEdit="handleEdit" />
  </div>
</template>

<script setup lang="ts">
import {h, onMounted, reactive, ref, watch} from "vue";
import { Plus, RefreshRight, Search } from "@element-plus/icons-vue";
import modalForm from "@/components/manage/modalform.vue";
import varFieldInput from "@/components/manage/varfieldinput.vue";
import useCrudActions from "@/hooks/useCrudActions.ts";
import request from "@/utils/request.ts";
import akImage from "@/components/akimage.vue";
import {isNormalGlobal} from "@/utils/user.ts";
import { ElMessage } from "element-plus";
import router from "@/router";

const queryFormRef = ref<any>();
const modalFormRef = ref<any>();
// TODO 自定义查询表达项
const queryParams = ref({
  teacherId: null,
  grade: "",
  name: "",
  code: "",
  img: "",
  description: "",
  credit: null,
  createdTime: [],
  updatedTime: [],
});

const teacherIdOptions = ref<any>([]);
const getTeacherIdOptions = () => {
  request.get("/manage/user/list", {
    role: "teacher",
    pageSize: 1000
  }).then((data: any) => {
    teacherIdOptions.value = data.records.map((item: any) => {
      return {
        label: item.realName || item.username,
        value: item.id
      };
    });
  });
};
const typeOptions = ref<any>([
  {label: "必修课", value: "required"},
  {label: "选修课", value: "elective"},
  {label: "实践课", value: "practical"},
  {label: "公共课", value: "public"},
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
    name: "teacherId",
    title: "教师",
    dataType: "Integer",
    selectable: true,
    options: teacherIdOptions
  },
  {
    name: "type",
    title: "类型",
    dataType: "String",
    selectable: true,
    options: typeOptions
  },
  {
    name: "grade",
    title: "年级",
    dataType: "String",
    selectable: true,
    options: gradeOptions
  },
  {
    name: "name",
    title: "名称",
    dataType: "String",
    selectable: false,
  },
  {
    name: "code",
    title: "代码",
    dataType: "String",
    selectable: false,
  },
  {
    name: "img",
    title: "图片",
    dataType: "Picture",
    selectable: false,
  },
  {
    name: "description",
    title: "描述",
    dataType: "BigString",
    selectable: false,
  },
  {
    name: "credit",
    title: "学分",
    dataType: "Integer",
    selectable: false,
  },
];
const urls = {
  list: "/manage/course/list",
  detail: "/manage/course/queryById",
  add: "/manage/course/add",
  edit: "/manage/course/edit",
  delete: "/manage/course/deleteById",
  batchDelete: "/manage/course/batchDeleteById"
};
let { tableData, pagination, handleList, handleAdd, handleEdit, handleDelete, handleBatchDelete } =
  useCrudActions(urls, queryParams);
const chunkedData = reactive<Array<any>>([]);
watch(tableData, () => {
  const chunkSize = 4;
  const result = [];
  for (let i = 0; i < tableData.length; i += chunkSize) {
    result.push(tableData.slice(i, i + chunkSize));
  }
  chunkedData.splice(0, chunkedData.length, ...result);
});
onMounted(() => {
  getTeacherIdOptions();
  setTimeout(() => {
    pagination.value.pageSize = 8;
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
const handleSelectCourse = (id: number) => {
  request.post("/manage/studentCourse/add", {
    courseId: id
  }).then(() => {
    ElMessage.success("选课成功");
    router.push({path: "/manage/my_course"});
  }).catch(() => {
    ElMessage.error("选课失败");
  });
};
</script>

<style>
/* 去除所有悬停效果 */
.no-hover-table .el-table__body tr:hover > td {
  background-color: transparent !important;
}

.query-wrapper .el-form-item {
  margin-bottom: 5px;
}
.el-dropdown {
  vertical-align: middle !important;
}

.card-table {
  border: none;
}

.card-table .el-card__body {
  padding: 0;
}

.card-table::before {
  display: none;
}

.card-row {
  display: flex;
  flex-wrap: wrap;
  margin: 0 -8px;
}

.card-item {
  width: calc(25% - 16px);
  margin: 0 8px 16px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.card-header {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
}

.card-content {
  padding: 10px 0;
  font-size: 14px;
  color: #606266;
}

.card-content p {
  margin: 5px 0;
}
</style>
