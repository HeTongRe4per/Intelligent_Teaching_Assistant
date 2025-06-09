<template>
  <div class="centered-form">
    <div class="form-container">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="auto">
        <el-form-item label="用户名" prop="username" name="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password" name="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="账户类型" prop="role" name="role">
          <el-select v-model="form.role" placeholder="请选择账户类型">
            <el-option v-for="option in roleOptions" :value="option.value" :label="option.label"></el-option>
          </el-select>
        </el-form-item>
        <!-- 登录与注册按钮 -->
        <el-space>
          <el-button type="primary" @click="() => handleSubmit('login')">登录</el-button>
          <el-button @click="() => handleSubmit('register')">注册</el-button>
        </el-space>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import request from "@/utils/request.ts";
import { ElMessage } from "element-plus";

const formRef = ref<any>();
const form = ref({
  username: "",
  password: "",
  role: "normal"
});
const roleOptions = ref<any>([
  {label: "学生", value: "normal"},
  {label: "教师", value: "teacher"},
  {label: "系统管理员", value: "admin"},
]);
const rules = {
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
  role: [{ required: true, message: "请选择账户类型", trigger: "change" }]
};
const handleSubmit = (type: string) => {
  if (formRef.value) {
    formRef.value
      .validate()
      .then(() => {
        const formCopy = Object.assign({}, form.value);
        // 验证通过后的处理逻辑
        if ("register" === type) {
          request.post("/register", formCopy).then(() => {
            window.location.reload();
          });
        } else {
          formCopy.username = formCopy.role + "::" + formCopy.username;
          request
            .get("/login", formCopy)
            .then((data: any) => {
              localStorage.setItem("token", data + "");
              request.get("/manage/getUser", {}).then((data: any) => {
                localStorage.setItem("user", JSON.stringify(data));
                if ("admin" === data.role) {
                  window.location.href = "/manage/index";
                } else {
                  window.location.href = "/index";
                }
              });
            })
            .catch((error: string) => {
              ElMessage.error(error);
            });
        }
      })
      .catch((err: any) => {
        console.log("表单验证失败:", err);
      });
  }
};
</script>

<style scoped>
.centered-form {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}

.form-container {
  width: 100%;
  max-width: 600px;
}
</style>
