<template>
  <!-- 顶部导航栏 -->
  <div id="header">
    <router-link to="/index">
      <el-link style="margin-left: 25px; font-size: 25px; line-height: 25px; color: white;" :underline="false">springboot-front</el-link>
    </router-link>
    <el-input v-model="keyword" placeholder="请输入关键字" style="width: 450px">
      <template #prefix>
        <i class="el-input__icon el-icon-search"></i>
      </template>
    </el-input>
    <el-dropdown v-if="usernameGlobal">
      <div id="avatar">
        <span class="username">{{ usernameGlobal }}</span>
        <el-avatar :size="30">
          <ak-image v-if="userGlobal.avatar" :url="userGlobal.avatar" :preview="false" :width="30" :height="30" />
          <el-icon v-else>
            <User />
          </el-icon>
        </el-avatar>
      </div>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item :key="1">
            <router-link to="/my">
              <el-link type="primary" :underline="false">个人中心</el-link>
            </router-link>
          </el-dropdown-item>
          <el-dropdown-item :key="2">
            <el-link type="primary" :underline="false" @click="handleLogout">退出</el-link>
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
    <router-link v-else to="/login">
      <el-link style="font-size: 20px; color: white;margin-right: 25px;" :underline="false">登录</el-link>
    </router-link>
  </div>
</template>

<script setup lang="ts">
import {ref} from "@vue/reactivity";
import {userGlobal, usernameGlobal} from "@/utils/user.ts";
import akImage from "@/components/akimage.vue";

const keyword = ref("");

const handleLogout = () => {
  // 退出登录
  localStorage.removeItem("token");
  localStorage.removeItem("user");
  window.location.href = "/login";
};
</script>

<style lang="less" scoped>
#header {
  background: #065298;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: white;
  height: 64px;

  .el-dropdown > div {
    min-width: 75px;
    margin-right: 50px;
    display: flex;
    align-items: center;
    justify-content: space-between;

    span {
      color: white;
      font-size: 15px;
      margin-left: 8px;
    }
  }
}

div:focus-visible {
  outline: none !important;
}
</style>
