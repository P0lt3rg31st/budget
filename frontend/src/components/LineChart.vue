<!-- src/components/LineChart.vue -->
<template>
  <Line :data="chartData" :options="chartOptions" />
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { Line } from 'vue-chartjs';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler,
} from 'chart.js';
import type { ChartData, ChartOptions } from 'chart.js';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler
);

interface Props {
  labels: string[];
  expenseData: number[];
  incomeData?: number[];
}

const props = defineProps<Props>();

const chartData = computed<ChartData<'line'>>(() => {
  const datasets: ChartData<'line'>['datasets'] = [
    {
      label: 'Расходы',
      data: props.expenseData,
      borderColor: '#f44336',
      backgroundColor: 'rgba(244, 67, 54, 0.1)',
      fill: true,
      tension: 0.3,
    },
  ];

  if (props.incomeData) {
    datasets.push({
      label: 'Доходы',
      data: props.incomeData,
      borderColor: '#4caf50',
      backgroundColor: 'rgba(76, 175, 80, 0.1)',
      fill: true,
      tension: 0.3,
    });
  }

  return {
    labels: props.labels,
    datasets,
  };
});

const chartOptions: ChartOptions<'line'> = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'top',
    },
    tooltip: {
      mode: 'index',
      intersect: false,
    },
  },
  scales: {
    y: {
      beginAtZero: true,
      ticks: {
        callback: (value) => `${value} ₽`,
      },
    },
  },
};
</script>
