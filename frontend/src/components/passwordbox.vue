<template>
  <el-dialog v-model="visible" :width="800" title="修改密码" destroy-on-close closable @close="visible = false">
    <el-form
      class="modal-form"
      ref="passFormRef"
      :inline="false"
      :model="currentForm"
      :rules="rules"
      :label-width="200"
      :gutter="20"
      :style="{ padding: '50px 50px' }"
    >
      <el-form-item label="旧密码" prop="oldPassword">
        <el-input type="password" v-model="currentForm.oldPassword" placeholder="请输入旧密码" clearable></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input type="password" v-model="currentForm.confirmPassword" placeholder="请输入旧密码" clearable></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input type="password" v-model="currentForm.newPassword" placeholder="请输入新密码" clearable></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="visible = false">关闭</el-button>
        <el-button type="primary" @click="handleConfirm">确认</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import request from "@/utils/request";
import { ref } from "vue";
import { ElMessage } from "element-plus";

const passFormRef = ref();
const visible = ref<Boolean>(false);
const currentForm = ref<any>({});
const rules: Record<string, any> = {
  oldPassword: [{ required: true, message: "请输入旧密码", trigger: "blur" }],
  confirmPassword: [
    { required: true, message: "请再次输入新密码", trigger: "blur" },
    {
      validator: (rule: any, value: any, callback: any) => {
        if (value !== currentForm.value.oldPassword) {
          callback(new Error("两次输入的密码不一致"));
        } else {
          callback();
        }
      },
      trigger: "blur"
    }
  ],
  newPassword: [{ required: true, message: "请输入新密码", trigger: "blur" }]
};
const props = defineProps({
  url: {
    type: String,
    default: "/manage/changePass"
  },
  userId: {
    type: Number
  }
});
const handleConfirm = () => {
  const data = new URLSearchParams();
  data.append("userId", props.userId + "");
  data.append("oldPassword", currentForm.value.oldPassword);
  data.append("newPassword", currentForm.value.newPassword);
  passFormRef.value.validate().then(() => {
    request
      .post(props.url, data)
      .then(() => {
        ElMessage.success("修改成功");
        visible.value = false;
      })
      .catch((err) => {
        ElMessage.error(err);
      });
  });
};
defineExpose({
  visible
});
</script>

<style scoped></style>
