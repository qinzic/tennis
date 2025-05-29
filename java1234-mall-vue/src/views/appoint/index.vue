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
          <img :src="scope.row.userAvatar" width="50" height="50" />
        </template>
      </el-table-column>

      <el-table-column prop="reservationTime" label="申请时间" width="180" />

      <!-- 预约状态列 -->
      <el-table-column prop="reservationStatus" label="预约状态" width="150">
        <template v-slot="scope">
          <el-tag :type="getStatusType(scope.row.reservationStatus)">
            {{ getStatusText(scope.row.reservationStatus) }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="action" label="操作" width="400" fixed="right" align="center">
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
      :small="small"
      :disabled="disabled"
      :background="background"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </el-card>
</template>

<script setup>
import { Search } from '@element-plus/icons-vue';
import { ref } from 'vue';
import axios from '@/util/axios';
import { ElMessage } from 'element-plus';

const queryForm = ref({
  query: '', // 查询条件，修改为用户昵称
  pageNum: 1,
  pageSize: 10,
});

const total = ref(0);
const tableData = ref([]);

// 初始化预约列表
const initReservationList = async () => {
  const res = await axios.post('admin/reservation/list', queryForm.value);
  tableData.value = res.data.reservationList.map((item) => ({
    ...item,
    actionDisabled: false, // 默认操作按钮未禁用
  }));
  total.value = res.data.total;
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

// 获取预约状态对应的文本
const getStatusText = (status) => {
  switch (status) {
    case 0:
      return '待审核';
    case 1:
      return '已批准';
    case 2:
      return '已拒绝';
    default:
      return '未知';
  }
};

// 获取预约状态对应的类型，用于 el-tag 样式
const getStatusType = (status) => {
  switch (status) {
    case 0:
      return 'warning'; // 待审核
    case 1:
      return 'success'; // 已批准
    case 2:
      return 'danger'; // 已拒绝
    default:
      return 'info';
  }
};

// 批准预约
const handleApproveReservation = async (row) => {
  const res = await axios.post('admin/reservation/approve', { id: row.id });
  if (res.data.code === 0) {
    ElMessage.success('批准成功！');
    row.reservationStatus = 1; // 更新状态为已批准
    row.actionDisabled = true; // 禁用操作按钮
  } else {
    ElMessage.error(res.data.msg);
  }
};

// 拒绝预约
const handleRejectReservation = async (row) => {
  const res = await axios.post('admin/reservation/reject', { id: row.id });
  if (res.data.code === 0) {
    ElMessage.success('拒绝成功！');
    row.reservationStatus = 2; // 更新状态为已拒绝
    row.actionDisabled = true; // 禁用操作按钮
  } else {
    ElMessage.error(res.data.msg);
  }
};
</script>

<style lang="scss" scoped>
.header {
  padding-bottom: 16px;
  box-sizing: border-box;
}

.el-pagination {
  padding-top: 15px;
  box-sizing: border-box;
}

/* 给操作列加上额外的右边距离 */
.el-table .cell {
  text-align: center;
}
</style>
