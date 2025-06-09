<template>
  <!-- 顶部导航栏 -->
  <div id="header">
    <span>学生考试管理系统</span>
    <el-dropdown>
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
            <router-link to="/manage/my">
              <el-link type="primary" :underline="false">个人中心</el-link>
            </router-link>
          </el-dropdown-item>
          <el-dropdown-item v-if="isNormalGlobal" :key="3">
            <router-link to="/manage/my_course">
              <el-link type="primary" :underline="false">我的选课</el-link>
            </router-link>
          </el-dropdown-item>
          <el-dropdown-item v-if="isNormalGlobal" :key="4">
            <router-link to="/manage/my_exam">
              <el-link type="primary" :underline="false">我的考试</el-link>
            </router-link>
          </el-dropdown-item>
          <el-dropdown-item :key="2">
            <el-link type="primary" :underline="false" @click="handleLogout">退出</el-link>
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script setup lang="ts">
import {isNormalGlobal, userGlobal, usernameGlobal} from "@/utils/user.ts";
import akImage from "@/components/akimage.vue";

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

  > span {
    color: white;
    font-size: 15px;
    margin-left: 25px;
  }

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

  .el-dropdown > div:focus-visible {
    outline: none !important;
  }
}
</style>
