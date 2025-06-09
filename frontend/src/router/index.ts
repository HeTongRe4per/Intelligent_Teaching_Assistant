import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";

const history = createWebHistory();
const routers: Array<RouteRecordRaw> = [
  {
    path: "/manage",
    name: "管理后台",
    redirect: "/manage/index",
    component: () => import("@/components/manage/layout/index.vue"),
    children: [
      {
        path: "/manage/index",
        name: "欢迎页",
        component: () => import("@/views/manage/index.vue")
      },
      {
        path: "/manage/my",
        name: "个人信息",
        component: () => import("@/views/my.vue")
      },
      {
        path: "/manage/my_course",
        name: "我的选课",
        component: () => import("@/views/manage/student_course.vue")
      },
      {
        path: "/manage/my_exam",
        name: "我的考试",
        component: () => import("@/views/manage/student_exam.vue")
      },
      {
        path: "/manage/answer/:id",
        name: "考试答题",
        component: () => import("@/views/manage/exam_answer.vue")
      },
      {
        path: "/manage/ai-chat",
        name: "AI助手",
        component: () => import("@/views/manage/ai_chat.vue")
      }
    ]
  },
  {
    path: "/manage/course",
    name: "课程管理",
    redirect: "/manage/course/course",
    component: () => import("@/components/manage/layout/index.vue"),
    children: [
      {
        path: "/manage/course/course",
        name: "课程列表",
        component: () => import("@/views/manage/course.vue")
      },
      {
        path: "/manage/course/course_material",
        name: "课程资料",
        component: () => import("@/views/manage/course_material.vue")
      }
    ]
  },
  {
    path: "/manage/exam",
    name: "考试管理",
    redirect: "/manage/exam/exam",
    component: () => import("@/components/manage/layout/index.vue"),
    children: [
      {
        path: "/manage/exam/exam",
        name: "考试列表",
        component: () => import("@/views/manage/exam.vue")
      },
      {
        path: "/manage/exam/exam_question",
        name: "试题列表",
        component: () => import("@/views/manage/exam_question.vue")
      }
    ]
  },
  {
    path: "/manage/system",
    name: "系统管理",
    redirect: "/manage/system/index",
    component: () => import("@/components/manage/layout/index.vue"),
    children: [
      {
        path: "/manage/system/role",
        name: "角色列表",
        component: () => import("@/views/manage/role.vue")
      },
      {
        path: "/manage/system/permission",
        name: "权限列表",
        component: () => import("@/views/manage/permission.vue")
      },
      {
        path: "/manage/system/user_:role",
        name: "用户列表",
        component: () => import("@/views/manage/user.vue")
      }
    ]
  },
  {
    path: "/login",
    name: "登录",
    component: () => import("@/views/login.vue")
  },
  {
    path: "/:pathMatch(.*)*",
    name: "404",
    redirect: "/manage/index"
  }
];
export default createRouter({
  history,
  routes: routers
});
