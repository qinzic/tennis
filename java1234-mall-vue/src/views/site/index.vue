<template>
  <el-card>
    <el-row :gutter="20" class="header">
      <el-col :span="7">
        <el-input v-model="queryForm.query" placeholder="请输入场地名称..." clearable />
      </el-col>
      <el-button type="primary" :icon="Search" @click="getVenueList">搜索</el-button>
      <el-button type="success" @click="openAddDialog">新增场地</el-button>
    </el-row>

    <el-table :data="venueList" style="width: 100%" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="venueName" label="场地名称" width="180" />
      <el-table-column prop="imageUrl" label="场地图片" width="150">
        <template v-slot="scope">
          <img :src="scope.row.imageUrl" width="60" height="60" />
        </template>
      </el-table-column>
      <el-table-column label="容量（剩余/总）" width="200">
        <template v-slot="scope">
          <!-- 显示每个时间段容量 -->
          <div v-for="ts in scope.row.timeSlots" :key="ts.id" style="margin-bottom:6px;">
            <strong>{{ ts.startTime }} - {{ ts.endTime }}:</strong>
            {{ ts.remainingCapacity }} / {{ ts.capacity }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template v-slot="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '启用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" fixed="right" width="220" align="center">
        <template v-slot="scope">
          <el-button type="primary" @click="openEditDialog(scope.row)">编辑</el-button>
          <el-button type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="pagination"
      v-model:currentPage="queryForm.pageNum"
      :page-sizes="[5, 10, 20]"
      :page-size="queryForm.pageSize"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="场地名称">
          <el-input v-model="form.venueName" />
        </el-form-item>

        <el-form-item label="上传图片">
          <el-upload
            class="upload-demo"
            action="/admin/upload"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
          >
            <el-button type="primary">点击上传</el-button>
          </el-upload>
          <div v-if="form.imageUrl" style="margin-top: 10px;">
            <img :src="form.imageUrl" width="100" height="100" />
          </div>
        </el-form-item>

        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择">
            <el-option label="启用" :value="1" />
            <el-option label="停用" :value="0" />
          </el-select>
        </el-form-item>

        <!-- 时间段编辑 -->
        <el-form-item label="时间段及容量" style="width:100%">
          <div v-for="(slot, idx) in form.timeSlots" :key="idx" style="margin-bottom:10px; display:flex; align-items:center;">
            <el-time-picker
              v-model="slot.startTime"
              placeholder="开始时间"
              value-format="HH:mm"
              :picker-options="{ selectableRange: ['00:00:00', '23:59:59'] }"
              style="width:120px; margin-right:10px;"
            />
            <el-time-picker
              v-model="slot.endTime"
              placeholder="结束时间"
              value-format="HH:mm"
              :picker-options="{ selectableRange: ['00:00:00', '23:59:59'] }"
              style="width:120px; margin-right:10px;"
            />
            <el-input-number v-model.number="slot.capacity" :min="1" label="容量" style="margin-right:10px; width:100px;" />
            <el-button @click="removeTimeSlot(idx)" >删除</el-button>
          </div>
          <el-button type="primary" style=" margin-left:10px;" @click="addTimeSlot">添加时间段</el-button>
          <div style="margin-top:6px; font-size:12px; color:#999;">
            每个时间段设置固定容量，预约时会减少对应时间段的剩余容量，容量每日重置。
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确认</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
const dialogVisible = ref(false);

import { ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Search } from '@element-plus/icons-vue';
import axios from '@/util/axios';

const queryForm = ref({
  query: '',
  pageNum: 1,
  pageSize: 10,
});

const total = ref(0);
const venueList = ref([]);

const getVenueList = async () => {
const res = await axios.post('/admin/venue/list', queryForm.value);
  if (res.data.code === 0) {
    venueList.value = res.data.data.venueList.map(item => ({
      ...item,
      timeSlots: item.timeSlots || [], // 后端返回时间段数组 [{id, startTime, endTime, capacity, remainingCapacity}]
    }));
    total.value = res.data.data.total;
  } else {
    ElMessage.error(res.data.msg || '获取场地列表失败');
  }
   // 模拟测试数据
   /* venueList.value = [
    {
      id: 1,
      venueName: '网球场 A',
      imageUrl: 'https://via.placeholder.com/60x60.png?text=A',
      status: 1,
      createTime: '2025-05-30 14:00:00',
      timeSlots: [
        { id: 1, startTime: '08:00', endTime: '10:00', capacity: 10, remainingCapacity: 6 },
        { id: 2, startTime: '10:00', endTime: '12:00', capacity: 10, remainingCapacity: 3 },
      ],
    },
    {
      id: 2,
      venueName: '篮球场 B',
      imageUrl: 'https://via.placeholder.com/60x60.png?text=B',
      status: 0,
      createTime: '2025-05-28 09:30:00',
      timeSlots: [
        { id: 1, startTime: '09:00', endTime: '11:00', capacity: 20, remainingCapacity: 12 },
        { id: 2, startTime: '13:00', endTime: '15:00', capacity: 20, remainingCapacity: 20 },
      ],
    }
  ];
  total.value = venueList.value.length;*/
};

const handleSizeChange = (size) => {
  queryForm.value.pageSize = size;
  getVenueList();
};

const handleCurrentChange = (page) => {
  queryForm.value.pageNum = page;
  getVenueList();
};


const dialogTitle = ref('新增场地');
const form = ref({
  id: null,
  venueName: '',
  imageUrl: '',
  status: 1,
  timeSlots: [], // 时间段数组
});

const handleUploadSuccess = (res) => {
  if (res.code === 0) {
    form.value.imageUrl = res.data.url;
    ElMessage.success('上传成功');
  } else {
    ElMessage.error(res.msg || '上传失败');
  }
};

const beforeUpload = (file) => {
  const isImg = file.type.startsWith('image/');
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isImg) ElMessage.error('只能上传图片文件');
  if (!isLt2M) ElMessage.error('图片大小不能超过 2MB');
  return isImg && isLt2M;
};

const openAddDialog = () => {
  dialogTitle.value = '新增场地';
  form.value = {
    id: null,
    venueName: '',
    imageUrl: '',
    status: 1,
    timeSlots: [
      { startTime: '08:00', endTime: '10:00', capacity: 10 },
      { startTime: '10:00', endTime: '12:00', capacity: 10 },
    ], // 默认空时间段示例
  };
  dialogVisible.value = true;
};

const openEditDialog = (row) => {
  dialogTitle.value = '编辑场地';
  // 需要把时间段格式化成合适格式（字符串）
  form.value = {
    id: row.id,
    venueName: row.venueName,
    imageUrl: row.imageUrl,
    status: row.status,
    timeSlots: (row.timeSlots || []).map(ts => ({
      id: ts.id || null,
      startTime: ts.startTime,
      endTime: ts.endTime,
      capacity: ts.capacity,
      remainingCapacity: ts.remainingCapacity,
    })),
  };
  dialogVisible.value = true;
};

const addTimeSlot = () => {
  form.value.timeSlots = [
    ...form.value.timeSlots,
    { startTime: '', endTime: '', capacity: 1 }
  ];
};

const removeTimeSlot = (index) => {
  form.value.timeSlots = form.value.timeSlots.filter((_, i) => i !== index);
};


const handleSubmit = async () => {
  if (!form.value.venueName.trim()) {
    ElMessage.warning('请输入场地名称');
    return;
  }
  if (form.value.timeSlots.length === 0) {
    ElMessage.warning('请至少添加一个时间段');
    return;
  }
  for (const ts of form.value.timeSlots) {
    if (!ts.startTime || !ts.endTime) {
      ElMessage.warning('所有时间段必须填写完整时间');
      return;
    }
    if (ts.capacity <= 0) {
      ElMessage.warning('容量必须大于0');
      return;
    }
  }
  const url = form.value.id ? '/admin/venue/update' : '/admin/venue/add';
  // 后端需接收时间段数组字段timeSlots
  const postData = {
    id: form.value.id,
    venueName: form.value.venueName,
    imageUrl: form.value.imageUrl,
    status: form.value.status,
    timeSlots: form.value.timeSlots,
  };
  const res = await axios.post(url, postData);
  if (res.data.code === 0) {
    ElMessage.success('操作成功');
    dialogVisible.value = false;
    getVenueList();
  } else {
    ElMessage.error(res.data.msg || '操作失败');
  }
};

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确认删除该场地？', '警告', { type: 'warning' });
    const res = await axios.post('/admin/venue/delete', { id });
    if (res.data.code === 0) {
      ElMessage.success('删除成功');
      getVenueList();
    } else {
      ElMessage.error(res.data.msg || '删除失败');
    }
  } catch {
    // 取消操作无事发生
  }
};

getVenueList();
</script>

<style scoped>
.header {
  padding-bottom: 16px;
}
.pagination {
  margin-top: 16px;
  text-align: right;
}
</style>
