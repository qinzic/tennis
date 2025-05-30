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

    <!-- ECharts 图表 -->
    <el-card>
      <div ref="chartRef" style="width: 100%; height: 300px;"></div>
    </el-card>

    <!-- 用户预约列表 -->
    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column prop="id" label="#ID" width="150" />
      <el-table-column prop="nickName" label="用户昵称" width="250" />
      <el-table-column prop="avatarUrl" label="用户头像" width="200">
        <template v-slot="scope">
          <img :src="scope.row.avatarUrl" width="50" height="50" />
        </template>
      </el-table-column>
      <el-table-column prop="reservationTime" label="申请时间" width="200" />
      <el-table-column label="预约时间段" width="180">
        <template v-slot="scope">
          {{ scope.row.timeSlotStart }} - {{ scope.row.timeSlotEnd }}
        </template>
      </el-table-column>
      <el-table-column prop="reservationStatus" label="预约状态" width="200" align="center" >
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
import { ref, onMounted, watch } from 'vue';
import * as echarts from 'echarts';
import { Search } from '@element-plus/icons-vue';
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

const chartRef = ref(null);
let chartInstance = null;

// 初始化 ECharts 图表
const initChart = () => {
  if (!chartRef.value) return;

  if (!chartInstance) {
    chartInstance = echarts.init(chartRef.value);
  }

  chartInstance.setOption({
    title: {
      text: '预约状态统计',
      left: 'center',
    },
    tooltip: {
      trigger: 'item',
    },
    legend: {
      bottom: '0%',
      left: 'center',
    },
    series: [
      {
        name: '预约状态',
        type: 'pie',
        radius: '50%',
        data: [
          { value: stats.value.pendingOrders, name: '待审核' },
          { value: stats.value.approvedOrders, name: '已批准' },
          { value: stats.value.rejectedOrders, name: '已拒绝' },
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)',
          },
        },
      },
    ],
  });
};

const initUserList = async () => {
  try {
    const res = await axios.post('admin/reservation/list', queryForm.value);
    tableData.value = res.data.reservationList;
    total.value = res.data.total;
    stats.value = res.data.stats;
    initChart(); // 更新图表数据
  } catch (error) {
    console.error('获取预约列表失败', error);
  }
};

onMounted(() => {
  initUserList();
});

const handleSizeChange = (pageSize) => {
  queryForm.value.pageNum = 1;
  queryForm.value.pageSize = pageSize;
  initUserList();
};

const handleCurrentChange = (pageNum) => {
  queryForm.value.pageNum = pageNum;
  initUserList();
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

// 图表响应窗口大小变化
window.addEventListener('resize', () => {
  chartInstance?.resize();
});
</script>

<style scoped>
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
