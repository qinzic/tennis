<template>
  <el-card>
    <el-row :gutter="20" class="header">
      <el-col :span="7">
        <el-input placeholder="请输入用户昵称..." clearable v-model="queryForm.query"></el-input>
      </el-col>
      <el-button type="primary" :icon="Search" @click="initReservationList">搜索</el-button>
    </el-row>

    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column prop="venueName" label="场地名称" width="200" fixed />

      <el-table-column prop="nickName" label="用户昵称" width="150" />

      <el-table-column prop="avatarUrl" label="用户头像" width="120">
        <template v-slot="scope">
          <img :src="scope.row.avatarUrl" width="50" height="50" />
        </template>
      </el-table-column>

      <el-table-column prop="reservationTime" label="申请时间" width="180" />

      <!-- 新增时间段列 -->
      <el-table-column label="预约时间段" width="180">
        <template v-slot="scope">
          <!-- 显示预约的时间段字符串，如 "08:00 - 10:00" -->
          {{ scope.row.timeSlotStart }} - {{ scope.row.timeSlotEnd }}
        </template>
      </el-table-column>

      <!-- 预约状态列 -->
      <el-table-column prop="reservationStatus" label="预约状态" width="150">
        <template v-slot="scope">
          <el-tag :type="getStatusType(scope.row.reservationStatus)">
            {{ getStatusText(scope.row.reservationStatus) }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="action" label="操作" width="350" fixed="right" align="center">
        <template v-slot="scope">
          <el-button
            type="success"
            :disabled="scope.row.actionDisabled"
            @click="handleApproveReservation(scope.row)"
          >
            批准
          </el-button>
          <el-button
            type="danger"
            :disabled="scope.row.actionDisabled"
            @click="handleRejectReservation(scope.row)"
          >
            拒绝
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:currentPage="queryForm.pageNum"
      :page-sizes="[10, 20, 30, 40, 50]"
      :page-size="queryForm.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </el-card>
</template>

<script setup>
import { ref } from 'vue';
import { Search } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import axios from '@/util/axios';

const queryForm = ref({
  query: '',
  pageNum: 1,
  pageSize: 10,
});

const total = ref(0);
const tableData = ref([]);

// 预约列表初始化（含时间段信息）
const initReservationList = async () => {
  const res = await axios.post('admin/reservation/list', queryForm.value);
  if (res.data.code === 0) {
    tableData.value = res.data.reservationList.map(item => {
      // 找出对应的预约时间段
      const matchedSlot = item.timeSlots?.find(ts => ts.id === item.timeSlotId);
      return {
        ...item,
        actionDisabled: item.reservationStatus !== 0, // 审核中才可操作
        timeSlotStart: matchedSlot ? matchedSlot.startTime : '',
        timeSlotEnd: matchedSlot ? matchedSlot.endTime : '',
      };
    });
    total.value = res.data.total;
  } else {
    ElMessage.error(res.data.msg || '获取预约列表失败');
  }
};

initReservationList();

const handleSizeChange = (pageSize) => {
  queryForm.value.pageNum = 1;
  queryForm.value.pageSize = pageSize;
  initReservationList();
};

const handleCurrentChange = (pageNum) => {
  queryForm.value.pageNum = pageNum;
  initReservationList();
};

const getStatusText = (status) => {
  switch (status) {
    case 0: return '待审核';
    case 1: return '已批准';
    case 2: return '已拒绝';
    default: return '未知';
  }
};

const getStatusType = (status) => {
  switch (status) {
    case 0: return 'warning';
    case 1: return 'success';
    case 2: return 'danger';
    default: return 'info';
  }
};

// 批准预约，同时减少对应时间段剩余容量
const handleApproveReservation = async (row) => {
  const res = await axios.post('admin/reservation/approve', { id: row.id });
  if (res.data.code === 0) {
    ElMessage.success('批准成功！');
    row.reservationStatus = 1;
    row.actionDisabled = true;
  } else {
    ElMessage.error(res.data.msg || '操作失败');
  }
};

// 拒绝预约，不影响容量
const handleRejectReservation = async (row) => {
  const res = await axios.post('admin/reservation/reject', { id: row.id });
  if (res.data.code === 0) {
    ElMessage.success('拒绝成功！');
    row.reservationStatus = 2;
    row.actionDisabled = true;
  } else {
    ElMessage.error(res.data.msg || '操作失败');
  }
};
</script>

<style scoped>
.header {
  padding-bottom: 16px;
  box-sizing: border-box;
}

.el-pagination {
  padding-top: 15px;
  box-sizing: border-box;
}

.el-table .cell {
  text-align: center;
}
</style>
