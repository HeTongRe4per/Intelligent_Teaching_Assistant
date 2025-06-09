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
            <el-form-item label="考试" name="examId" prop="examId">
              <var-field-input
                v-model="queryParams.examId"
                :recordMeta="{ name: 'examId', title: '考试', dataType: 'Integer', selectable: true, options: examIdOptions }"/>
            </el-form-item>
          </el-col>
          <el-col :xs="8" :sm="8" :md="8" :lg="6" :xl="6">
            <el-form-item label="题型" name="type" prop="type">
              <var-field-input
                v-model="queryParams.type"
                :recordMeta="{ name: 'type', title: '题型', dataType: 'Integer', selectable: true, options: typeOptions }"/>
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
            <el-table-column prop="examId" label="考试编号" align="center">
              <template #default="scope">
                {{ examIdOptions.find((item: any) => item.value === scope.row.examId).label }}
              </template>
            </el-table-column>
            <el-table-column prop="type" label="题型" align="center">
              <template #default="scope">
                {{ typeOptions.find((item: any) => item.value === scope.row.type).label }}
              </template>
            </el-table-column>
            <el-table-column prop="content" label="试题内容" align="center" width="420">
              <template #default="scope">
                <div v-if="scope.row.type === 1">
                  <p>{{scope.row.content.title}}</p>
                  <p>A：{{scope.row.content.itemA}}</p>
                  <p>B：{{scope.row.content.itemB}}</p>
                  <p>C：{{scope.row.content.itemC}}</p>
                  <p>D：{{scope.row.content.itemD}}</p>
                  <p>正确答案：{{scope.row.content.answer}}</p>
                </div>
                <div v-else>
                  <p>{{scope.row.content.title}}</p>
                  <p>参考答案：{{scope.row.content.answer}}</p>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="score" label="分值" align="center">
            </el-table-column>
            <el-table-column prop="sequence" label="排序序号" align="center">
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
    <modal-form ref="modalFormRef" :hide-id-field="true" title="试题信息" @handleAdd="handleAdd" @handleEdit="handleEdit" />
  </div>
</template>

<script setup lang="ts">
import {computed, h, onMounted, ref, unref, watch} from "vue";
import { Delete, ArrowDown, Plus, RefreshRight, Search } from "@element-plus/icons-vue";
import modalForm from "@/components/manage/modalform.vue";
import varFieldInput from "@/components/manage/varfieldinput.vue";
import useCrudActions from "@/hooks/useCrudActions.ts";
import request from "@/utils/request.ts";
import {isAdminGlobal, isTeacherGlobal, userGlobal} from "@/utils/user.ts";

const queryFormRef = ref<any>();
const modalFormRef = ref<any>();
// TODO 自定义查询表达项
const queryParams = ref({
  teacherId: null,
  examId: null,
  type: null,
  content: "",
  score: null,
  sequence: null,
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
const typeOptions = ref<any>([
  {label: "单选", value: 1},
  {label: "简答", value: 2},
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
    name: "examId",
    title: "考试",
    dataType: "Integer",
    selectable: true,
    options: examIdOptions
  },
  {
    name: "type",
    title: "题型",
    dataType: "Integer",
    selectable: true,
    options: typeOptions
  },
  {
    name: "content",
    subName: "title",
    isSubField: true,
    title: "题目",
    dataType: "String",
    selectable: false,
  },
  {
    name: "content",
    subName: "itemA",
    isSubField: true,
    title: "选项A",
    dataType: "String",
    selectable: false,
  },
  {
    name: "content",
    subName: "itemB",
    isSubField: true,
    title: "选项B",
    dataType: "String",
    selectable: false,
  },
  {
    name: "content",
    subName: "itemC",
    isSubField: true,
    title: "选项C",
    dataType: "String",
    selectable: false,
  },
  {
    name: "content",
    subName: "itemD",
    isSubField: true,
    title: "选项D",
    dataType: "String",
    selectable: false,
  },
  {
    name: "content",
    subName: "answer",
    title: "答案",
    isSubField: true,
    selectable: true,
    options: [
      {
        label: "A",
        value: "A"
      },
      {
        label: "B",
        value: "B"
      },
      {
        label: "C",
        value: "C"
      },
      {
        label: "D",
        value: "D"
      }
    ]
  },
  {
    name: "score",
    title: "分数",
    dataType: "Integer",
    selectable: false,
  },
  {
    name: "sequence",
    title: "排序序号",
    dataType: "Integer",
    selectable: false,
  },
];
const urls = {
  list: "/manage/examQuestion/list",
  detail: "/manage/examQuestion/queryById",
  add: "/manage/examQuestion/add",
  edit: "/manage/examQuestion/edit",
  delete: "/manage/examQuestion/deleteById",
  batchDelete: "/manage/examQuestion/batchDeleteById"
};
let { tableData, pagination, handleList, handleAdd, handleEdit, handleDelete, handleBatchDelete } =
  useCrudActions(urls, queryParams);
watch(() => modalFormRef.value?.currentRecord, () => {
  if (modalFormRef.value.currentRecord.type === 2) {
    modalFormRef.value.recordMetas = [
      {
        name: "id",
        title: "编号",
        dataType: "Integer",
        selectable: false,
      },
      {
        name: "examId",
        title: "考试",
        dataType: "Integer",
        selectable: true,
        options: examIdOptions
      },
      {
        name: "type",
        title: "类型",
        dataType: "Integer",
        selectable: true,
        options: typeOptions
      },
      {
        name: "content",
        subName: "title",
        isSubField: true,
        title: "题目",
        dataType: "String",
        selectable: false,
      },
      {
        name: "content",
        subName: "answer",
        isSubField: true,
        title: "参考答案",
        dataType: "BigString",
        selectable: false,
      },
      {
        name: "score",
        title: "分数",
        dataType: "Integer",
        selectable: false,
      }
    ];
  } else {
    modalFormRef.value.recordMetas = recordMetas;
  }
}, {deep: true});
onMounted(() => {
  getTeacherIdOptions();
  getExamIdOptions();
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
  modalFormRef.value.currentRecord = {
    type: 1,
    content: {},
    sequence: 1
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
