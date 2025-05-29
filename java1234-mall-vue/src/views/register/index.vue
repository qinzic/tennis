<template>
  <div class="register-container">
    <div class="register-form">
      <h3 class="title">管理员注册</h3>
      <el-form ref="registerForm" :model="form" :rules="registerRules">
        <el-form-item prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户名..."></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" placeholder="请输入密码..." :type="passwordType"></el-input>
          <svg-icon 
          :icon="passwordType === 'password' ? 'eye' : 'eye-open'"
          @click="changeType"
        ></svg-icon>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="form.confirmPassword" placeholder="请再次输入密码..." :type="confirmPasswordType"></el-input>
          <svg-icon 
            :icon="confirmPasswordType === 'password' ? 'eye' : 'eye-open'"
            @click="changeConfirmPasswordType"
          ></svg-icon>
        </el-form-item>
        <el-button type="primary" class="register-button" @click="handleRegister">注册</el-button>
      </el-form>
      <div class="switch-container">
        <p @click="goToLogin">已有账号？<span>登录</span></p>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref } from 'vue'
 // import {Edit} from '@element-plus/icons-vue'
  // import axios from 'axios'
  import  axios from '@/util/axios'
  import {ElMessage} from 'element-plus'
  import {getServerUrl} from "@/config/sys";
  import router from '@/router'

const form = ref({
  userName: '',
  password: '',
  confirmPassword: ''
});

const registerRules = ref({
  userName: [
    { 
      required: true, 
      message: '请输入用户名!', 
      trigger: 'blur' 
    }],
  password: [
    { 
      required: true, 
      message: '请输入密码!', 
      trigger: 'blur' 
    }],
  confirmPassword: [
    {
      required: true,
      validator: (rule, value) => {
        if (value !== form.value.password) {
          return Promise.reject('两次输入密码不一致');
        }
        return Promise.resolve();
      },
      trigger: ['blur', 'input'], 
    },
  ],
});

const registerForm=ref(null);


// 提交注册逻辑
const handleRegister = async () => {
  try {
    // 触发验证逻辑
    registerForm.value.validate(async (valid) => {
      if (valid) {
        const response = await axios.post('/user/register', form.value);
        if (response.data.code === 0) {
          ElMessage.success('注册成功');
          router.push('/login');
        } else {
          ElMessage.error(response.data.msg);
        }
      }
    });
  } catch (error) {
    console.error('Registration error:', error);
    ElMessage.error('系统运行出错，请联系管理员');
  }
};

const goToLogin = () => {
  router.push('/login');
};
const passwordType = ref('password')
  const changeType = () => {
    if (passwordType.value === 'password') {
      passwordType.value = 'text'
    } else {
      passwordType.value = 'password'
    }
  }
  const confirmPasswordType = ref('password');
const changeConfirmPasswordType = () => {
  if (confirmPasswordType.value === 'password') {
    confirmPasswordType.value = 'text';
  } else {
    confirmPasswordType.value = 'password';
  }
}
</script>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;
$cursor: #fff;

.register-container {
  height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .register-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;

    ::v-deep .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      color: #454545;
    }

    ::v-deep .el-input {
      display: inline-block;
      height: 47px;
      width: 85%;

      input {
        background: transparent;
        border: 0px;
        -webkit-appearance: none;
        border-radius: 0px;
        padding: 12px 5px 12px 15px;
        color: $light_gray;
        height: 47px;
        caret-color: $cursor;
      }
    }

    .register-button {
      width: 100%;
      box-sizing: border-box;
    }
  }
  .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }

  .tips {
    font-size: 16px;
    line-height: 28px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .switch-container {
    text-align: center;
    margin-top: 20px;
    cursor: pointer;
    color: $light_gray;

    span {
      color: #007bff;
      text-decoration: underline;
    }
    &:hover {
      color: white;
    }
  }
}
</style>
