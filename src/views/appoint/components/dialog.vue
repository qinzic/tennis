<template>
  <el-dialog
    :model-value="dialogVisible"
    :title="dialogTitle"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="formRef" :model="form" label-width="100px" :rules="rules" class="form-container">

      <!-- 场地名称 -->
      <el-form-item label="场地名称" prop="name" class="form-item">
        <el-input v-model="form.name" style="width: 100%"></el-input>
      </el-form-item>

      <!-- 场地图片 -->
      <el-form-item label="场地图片" prop="image" class="form-item">
        <el-upload
          class="upload-demo"
          action="/upload"
          list-type="picture-card"
          :on-success="handleImageSuccess"
          :before-upload="beforeUpload"
        >
          <i class="el-icon-plus"></i>
        </el-upload>
        
        <span v-if="form.image" class="uploaded-image">
          <img :src="form.image" alt="场地图片" class="image-preview">
        </span>
      </el-form-item>

      <!-- 剩余人数 -->
      <el-form-item label="剩余人数" prop="capacity" class="form-item">
        <el-input v-model="form.capacity" style="width: 100%" type="number"></el-input>
      </el-form-item>

      <!-- 场地描述 -->
      <el-form-item label="场地描述" prop="description" class="form-item">
        <el-input
          v-model="form.description"
          :rows="4"
          type="textarea"
        />
      </el-form-item>

    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleConfirm">确认</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { defineEmits, ref, defineProps, watch } from 'vue'
import axios from "@/util/axios";
import { ElMessage } from "element-plus";

// Props
const props = defineProps({
  dialogTitle: {
    type: String,
    default: '',
    required: true
  },
  dialogValue: {
    type: Object,
    default: () => {}
  }
})

const form = ref({
  id: -1,
  name: '',
  image: '',  // 场地图片路径
  capacity: 0,  // 剩余人数
  description: ''
})

const formRef = ref(null)

const emits = defineEmits(['update:modelValue', 'initVenueList'])

const handleClose = () => {
  formRef.value.resetFields();
  emits('update:modelValue', false)
}

const rules = ref({
  name: [
    {
      required: true,
      message: '请输入场地名称！',
    }
  ],
  capacity: [
    { required: true, message: '请输入剩余人数!' },
    { type: 'number', message: '剩余人数必须是数值类型！', transform: (value) => Number(value) },
  ],
  description: [
    {
      required: true,
      message: '请输入场地描述！',
    },
  ],
})

const handleImageSuccess = (response) => {
  // 假设图片上传成功后，返回的响应包含图片的 URL 地址
  form.value.image = response.url;
  ElMessage.success('图片上传成功');
}

const beforeUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
  if (!isJPG) {
    ElMessage.error('上传图片只能是 JPG 或 PNG 格式!');
  }
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!');
  }
  return isJPG && isLt2M;
}

const handleConfirm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        let result = await axios.post("admin/venue/save", form.value);
        let data = result.data;
        if (data.code === 0) {
          ElMessage.success("场地信息保存成功");
          formRef.value.resetFields();
          emits("initVenueList");
          handleClose();
        } else {
          ElMessage.error(data.msg);
        }
      } catch (err) {
        console.error("error:" + err)
        ElMessage.error('系统运行出错，请联系管理员');
      }
    } else {
      return false;
    }
  })
}

watch(
  () => props.dialogValue,
  () => {
    form.value = props.dialogValue;
  },
  { deep: true, immediate: true }
)
</script>
<style scoped>
/* 使用 Flexbox 布局确保所有表单项都在同一列内 */
.form-container {
  display: flex;
  flex-direction: column; /* 所有表单项垂直排列 */
  gap: 20px;  /* 每个表单项之间的间距 */
  width: 100%;  /* 确保容器宽度充满 */
}

/* 每个表单项 */
.form-item {
  display: flex;
  flex-direction: column;  /* 保证 label 和输入框是竖直排列的 */
  gap: 10px;  /* 控制 label 和输入框之间的间距 */
  width: 100%;  /* 确保每个表单项的宽度填满容器 */
}

/* 上传图片的容器，确保上传按钮和图片显示在同一列 */
.upload-demo {
  width: 100px;
  height: 100px;
  margin-bottom: 10px;
}

/* 显示上传的图片 */
.uploaded-image {
  display: flex;
  justify-content: center;
  align-items: center;
}

.image-preview {
  width: 80px;
  height: 80px;
  object-fit: cover;  /* 确保图片适应框内，避免变形 */
  border-radius: 8px;
}

/* 控制每个 form-item 内部的输入框 */
.el-input {
  width: 100%;
}
</style>



