<template>
  <el-menu
    active-text-color="#ffd04b"
    background-color="#2d3a4b"
    class="el-menu-vertical-demo"
    :default-active="route.path"
    text-color="#fff"
    router
  >
    <el-menu-item index="/home">
      <el-icon><home-filled /></el-icon>
      <span>首页</span>
    </el-menu-item>

    <el-menu-item index="/user">
      <el-icon><user /></el-icon>
      <span>工作台</span>
    </el-menu-item>

    <el-menu-item index="/appoint">
      <el-icon><tickets /></el-icon>
      <span>预约管理</span>
    </el-menu-item>
    <el-menu-item index="/site">
      <el-icon><Filter /></el-icon>
      <span>场地管理</span>
    </el-menu-item>
      <el-sub-menu index="3">
      <template #title >
        <el-icon><ShoppingCart /></el-icon>
        <span>商品类别管理</span>
      </template>
      <el-menu-item index="bigType">
        <el-icon><management /></el-icon>
        <span>商品大类管理</span>
      </el-menu-item>
      <el-menu-item index="smallType">
        <el-icon><management /></el-icon>
        <span>商品小类管理</span>
      </el-menu-item>
    </el-sub-menu>
    <el-menu-item index="/product">
      <el-icon><Box /></el-icon>
      <span>商品管理</span>
    </el-menu-item>
    <el-sub-menu index="11">
      <template #title >
        <el-icon><management /></el-icon>
        <span>系统管理</span>
      </template>
      <el-menu-item index="/modifyPassword">
        <el-icon><edit /></el-icon>
        <span>修改密码</span>
      </el-menu-item>
      <el-menu-item >
        <el-icon @click="logout"><switch-button /></el-icon>
        <span @click="logout">安全退出</span>
      </el-menu-item>
    </el-sub-menu>


  </el-menu>
</template>

<script setup>
import {HomeFilled,User,Management,Tickets,Edit,SwitchButton,Filter,Box,ShoppingCart} from '@element-plus/icons-vue'

import {useStore} from 'vuex'
import { useRoute } from 'vue-router'
const route = useRoute()
const store=useStore();


const logout=()=>{
  store.dispatch('logout')
}

</script>

<style scoped>

</style>