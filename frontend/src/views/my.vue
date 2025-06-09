<template>
  <div style="max-height: 80vh">
    <div class="profile-container">
      <ak-image class="avatar" v-if="userGlobal.avatar" :url="userGlobal.avatar" :width="200" :height="200" />
      <h1>用户名：{{ usernameGlobal }}</h1>
      <h1>真实姓名：{{ userGlobal.realName || '--' }}</h1>
      <h1>电子邮箱：{{ userGlobal.email || '--' }}</h1>
      <h1>手机号码：{{ userGlobal.phone || '--' }}</h1>
      <h1 v-if="isNormalGlobal">学号：{{ userGlobal.stuNum || '--' }}</h1>
      <h1 v-if="isNormalGlobal">年级：{{ gradeOptions.find((item: any) => item.value === userGlobal.grade)?.label || '--' }}</h1>
      <h1 v-if="isNormalGlobal">班级：{{ userGlobal.clazz || '--' }}</h1>
      <h1 v-if="isTeacherGlobal">部门：{{ userGlobal.department || '--' }}</h1>
      <div class="btn-group">
        <el-button type="warning" @click="handleClickEdit">编辑</el-button>
        <el-button type="danger" @click="handleClickChangePass">修改密码</el-button>
        <el-button type="success" @click="back">返回</el-button>
      </div>
    </div>
    <modal-form ref="modalFormRef" @handleEdit="handleEdit" />
    <password-box ref="passBoxRef" :userId="userGlobal.id" />
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import {isNormalGlobal, isTeacherGlobal, userGlobal, usernameGlobal} from "@/utils/user.ts";
import modalForm from "@/components/manage/modalform.vue";
import passwordBox from "@/components/passwordbox.vue";
import akImage from "@/components/akimage.vue";
import useCrudActions from "@/hooks/useCrudActions.ts";

const modalFormRef = ref<any>();
const passBoxRef = ref<any>();
const urls = {
  edit: "/manage/user/edit"
};
const gradeOptions = ref<any>([
  {label: "21级", value: "grade21"},
  {label: "22级", value: "grade22"},
  {label: "23级", value: "grade23"},
  {label: "24级", value: "grade24"},
]);
const recordMetas = [
  {
    name: "id",
    title: "编号",
    dataType: "Integer",
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
const {handleEdit} = useCrudActions(urls, {});
const handleClickEdit = () => {
  modalFormRef.value.editable = true;
  modalFormRef.value.isEdit = true;
  modalFormRef.value.visible = true;
  if (isNormalGlobal) {
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
  } else if (isTeacherGlobal) {
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
  modalFormRef.value.currentRecord = userGlobal;
};
const handleClickChangePass = () => {
  passBoxRef.value.visible = true;
};
const back = () => {
  window.history.back();
};
</script>

<style scoped>
.profile-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 50px;
}

.avatar {
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 30px;
}

.btn-group {
  margin: 5px 0px;
}
</style>
