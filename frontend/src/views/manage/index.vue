<template>
  <div>
    <h2 style="text-align: center">课程推荐</h2>
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
                <template #footer>
                  <el-popconfirm v-if="isNormalGlobal" title="确定删除？" ok-text="确定" cancel-text="取消" @confirm="() => handleSelectCourse(item.id)">
                    <template #reference>
                      <el-link :underline="false">选课</el-link>
                    </template>
                  </el-popconfirm>
                  <el-divider v-if="isNormalGlobal" direction="vertical" />
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
      <template #empty>
        <div class="empty-data">
          <p>暂无数据</p>
        </div>
      </template>
    </el-table>
    <modal-form ref="modalFormRef" :hide-id-field="true" title="课程信息" />
  </div>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref, watch} from "vue";
import request from "@/utils/request.ts";
import {isNormalGlobal, userGlobal} from "@/utils/user.ts";
import {ElMessage} from "element-plus";
import router from "@/router";
import modalForm from "@/components/manage/modalform.vue";
import akImage from "@/components/akimage.vue";

const tableData = ref<any>([]);
const modalFormRef = ref<any>();
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
const chunkedData = reactive<Array<any>>([]);
watch(tableData, () => {
  const chunkSize = 4;
  const result = [];
  for (let i = 0; i < tableData.value.length; i += chunkSize) {
    result.push(tableData.value.slice(i, i + chunkSize));
  }
  chunkedData.splice(0, chunkedData.length, ...result);
});
onMounted(() => {
  getTeacherIdOptions();
  setTimeout(() => {
    request.get(`/recommend/hybridRecommend?studentId=${userGlobal.id}`).then((data: any) => {
      tableData.value = data;
    })
  }, 500);
});
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

<style scoped>

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
