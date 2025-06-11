<template>
  <div class="content-wrapper">
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
            <el-table-column prop="studentId" label="学生编号" align="center">
              <template #default="scope">
                {{ studentIdOptions.find((item: any) => item.value === scope.row.studentId).label }}
              </template>
            </el-table-column>
            <el-table-column prop="examId" label="考试编号" align="center">
              <template #default="scope">
                {{ examIdOptions.find((item: any) => item.value === scope.row.examId).label }}
              </template>
            </el-table-column>
            <el-table-column prop="score" label="考试成绩" align="center">
            </el-table-column>
            <el-table-column prop="switchCount" label="切屏次数" align="center" >
            </el-table-column>
            <el-table-column prop="startTime" label="开始时间" align="center">
            </el-table-column>
            <el-table-column prop="endTime" label="结束时间" align="center">
            </el-table-column>
            <el-table-column prop="state" label="状态" align="center">
              <template #default="scope">
                {{ stateOptions.find((item: any) => item.value === scope.row.state).label }}
              </template>
            </el-table-column>
            <el-table-column fixed="right" label="操作" align="center" width="230">
              <template #default="scope">
                <el-popconfirm v-if="scope.row.state === 0 && scope.row.studentId === userGlobal.id"
                    title="确定开始答题？"
                    ok-text="确定"
                    cancel-text="取消"
                    @confirm="() => handleAnswer(scope.row)"
                >
                  <template #reference>
                    <el-link :underline="false">开始答题</el-link>
                  </template>
                </el-popconfirm>
                <el-divider v-if="scope.row.state === 0 && scope.row.studentId === userGlobal.id" direction="vertical" />
                <el-link v-if="scope.row.state !== 0" :underline="false" @click="() => handleAnswer(scope.row)">查看答题</el-link>
                <el-divider v-if="scope.row.state !== 0" direction="vertical" />
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
    <modal-form ref="modalFormRef" :hide-id-field="true" title="学生考试成绩信息" @handleAdd="handleAdd" @handleEdit="handleEdit" />
  </div>
</template>

<script setup lang="ts">
import { computed, h, onMounted, ref, unref } from "vue";
import { Delete, ArrowDown, Plus } from "@element-plus/icons-vue";
import modalForm from "@/components/manage/modalform.vue";
import useCrudActions from "@/hooks/useCrudActions.ts";
import request from "@/utils/request.ts";
import router from "@/router";
import {isNormalGlobal, isTeacherGlobal, userGlobal} from "@/utils/user.ts";

const modalFormRef = ref<any>();
// TODO 自定义查询表达项
const queryParams = ref({
  studentId: null,
  examId: null,
  answers: "",
  score: null,
  startTime: [],
  endTime: [],
  state: null,
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
const examIdOptions = ref<any>([]);
const getExamIdOptions = () => {
  request.get("/manage/exam/list", {
    pageSize: 1000
  }).then((data: any) => {
    examIdOptions.value = data.records.map((item: any) => {
      return {
        label: item.name,
        value: item.id
      };
    });
  });
};
const stateOptions = ref<any>([
  {label: "未提交", value: 0},
  {label: "待批改", value: 1},
  {label: "已完成", value: 2},
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
    name: "studentId",
    title: "学生",
    dataType: "Integer",
    selectable: true,
    options: studentIdOptions
  },
  {
    name: "examId",
    title: "考试",
    dataType: "Integer",
    selectable: true,
    options: examIdOptions
  },
  {
    name: "score",
    title: "考试成绩",
    dataType: "Integer",
    selectable: false,
  },
  {
    name: "switchCount",
    title: "切屏次数",
    dataType: "Integer",
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
    name: "state",
    title: "状态",
    dataType: "Integer",
    selectable: true,
    options: stateOptions
  },
];
const urls = {
  list: "/manage/studentExam/list",
  detail: "/manage/studentExam/queryById",
  add: "/manage/studentExam/add",
  edit: "/manage/studentExam/edit",
  delete: "/manage/studentExam/deleteById",
  batchDelete: "/manage/studentExam/batchDeleteById"
};
let { tableData, pagination, handleList, handleAdd, handleEdit, handleBatchDelete } =
  useCrudActions(urls, queryParams);
onMounted(() => {
  getStudentIdOptions();
  getExamIdOptions();
  setTimeout(() => {
    if (isNormalGlobal) {
      queryParams.value.studentId = userGlobal.id;
      handleList();
    } else {
      const examId: any = router.currentRoute.value.query.examId;
      console.log(examId);
      queryParams.value.examId = examId;
      handleList();
    }
  }, 500);
});
const handleClickAdd = () => {
  modalFormRef.value.editable = true;
  modalFormRef.value.isEdit = false;
  modalFormRef.value.visible = true;
  modalFormRef.value.recordMetas = recordMetas;
  modalFormRef.value.currentRecord = {};
};
const handleClickDetail = (record: any) => {
  modalFormRef.value.editable = false;
  modalFormRef.value.isEdit = false;
  modalFormRef.value.visible = true;
  modalFormRef.value.recordMetas = recordMetas;
  modalFormRef.value.currentRecord = record;
};
const handleAnswer = (record: any) => {
  router.push({path: `/manage/answer/${record.id}`});
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
