<template>
  <el-card>
    <!-- 搜索框 -->
    <el-row :gutter="20" class="header">
      <el-col :span="7">
        <el-input placeholder="请输入用户昵称..." clearable v-model="queryForm.query"></el-input>
      </el-col>
      <el-button type="primary" :icon="Search" @click="initUserList">搜索</el-button>
    </el-row>

    <!-- 统计信息 -->
    <el-row :gutter="20" class="stats">
      <el-col :span="6">
        <el-card>
          <p>预定数量</p>
          <p>{{ stats.totalOrders }}</p>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <p>待审核</p>
          <p>{{ stats.pendingOrders }}</p>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <p>已批准</p>
          <p>{{ stats.approvedOrders }}</p>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <p>已拒绝</p>
          <p>{{ stats.rejectedOrders }}</p>
        </el-card>
      </el-col>
    </el-row>

    <!-- 用户预约列表 -->
    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column prop="id" label="#ID" width="80" />
      <el-table-column prop="nickName" label="用户昵称" width="200" />
      <el-table-column prop="avatarUrl" label="用户头像" width="200">
        <template v-slot="scope">
          <img :src="scope.row.avatarUrl" width="50" height="50" />
        </template>
      </el-table-column>
      <el-table-column prop="reservationTime" label="申请时间" width="180" />
      <el-table-column prop="reservationStatus" label="预约状态" width="150">
        <template v-slot="scope">
          <el-tag :type="getStatusType(scope.row.reservationStatus)">
            {{ getStatusText(scope.row.reservationStatus) }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:currentPage="queryForm.pageNum"
      :page-sizes="[10, 20, 30, 40, 50]"
      :page-size="queryForm.pageSize"
      :background="true"
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

const queryForm = ref({
  query: '',
  pageNum: 1,
  pageSize: 10,
});

const total = ref(0);
const tableData = ref([]);
const stats = ref({
  totalOrders: 0,
  pendingOrders: 0,
  approvedOrders: 0,
  rejectedOrders: 0,
});

// 初始化用户列表
const initUserList = async () => {
  const res = await axios.post('admin/reservation/list', queryForm.value);
  tableData.value = res.data.reservationList;
  total.value = res.data.total;
  stats.value = res.data.stats; // 假设返回的统计数据有预定数量、待审核、已批准和已拒绝
};

initUserList();

// 分页大小变化时的处理
const handleSizeChange = (pageSize) => {
  queryForm.value.pageNum = 1;
  queryForm.value.pageSize = pageSize;
  initUserList();
};

// 当前页数变化时的处理
const handleCurrentChange = (pageNum) => {
  queryForm.value.pageNum = pageNum;
  initUserList();
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
</script>

<style lang="scss" scoped>
.header {
  padding-bottom: 16px;
  box-sizing: border-box;
}

.stats {
  padding: 16px 0;
}

.el-pagination {
  padding-top: 15px;
  box-sizing: border-box;
}
</style>
