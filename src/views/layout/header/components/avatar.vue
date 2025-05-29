<template>

    <el-dropdown>
    <span class="el-dropdown-link">
      <el-avatar shape="square" :size="40" :src="squareUrl"></el-avatar>
    </span>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="logout">安全退出</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>


</template>

<script setup>
import {ref} from 'vue'
import {useStore} from 'vuex'
import logoutIconSrc from '@/assets/exit.png';
const store=useStore();
const squareUrl=ref(logoutIconSrc)

const logout=()=>{
  store.dispatch('logout')
}
</script>

<style scoped>

</style>