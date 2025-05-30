import { createRouter, createWebHashHistory } from "vue-router";

const routes = [

  {
    path: "/",
    name: "首页",
    component: () =>import( "../views/layout"),
    redirect:'/home',
    children:[
      {
        path: "/home",
        name: "首页",
        component: () =>
          import( "../views/home/index"),
      },
      {
        path: "/user",
        name: "工作台",
        component: () =>
          import( "../views/user"),
      },
      {
        path: "/appoint",
        name: "预约管理",
        component: () =>
          import( "../views/appoint"),
      },
            {
        path: "/site",
        name: "场地管理",
        component: () =>
          import( "../views/site"),
      },
      {
        path: "/modifyPassword",
        name: "修改密码",
        component: () =>
          import( "../views/modifyPassword"),
      }
    ]
  },
  {
    path: "/login",
    name: "Login",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import( "../views/login"),
  },
  {
    path: "/register",
    name: "Register",
    component: () => import("../views/register"), // 注册页面
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
