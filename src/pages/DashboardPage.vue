<!-- src/pages/DashboardPage.vue -->
<template>
  <q-page class="q-pa-md">
    <div class="text-h5 q-mb-md">
      Финансовая статистика
    </div>

    <div v-if="isLoading" class="flex flex-center q-pa-xl">
      <q-spinner color="primary" size="3em" />
    </div>

    <template v-else>
      <!-- Сводка -->
      <div class="row q-col-gutter-md q-mb-lg">
        <div class="col-12 col-md-4">
          <q-card class="bg-positive text-white">
            <q-card-section>
              <div class="text-subtitle2">Доходы</div>
              <div class="text-h5">{{ formatMoney(totalIncomes) }}</div>
            </q-card-section>
          </q-card>
        </div>
        <div class="col-12 col-md-4">
          <q-card class="bg-negative text-white">
            <q-card-section>
              <div class="text-subtitle2">Расходы</div>
              <div class="text-h5">{{ formatMoney(totalExpenses) }}</div>
            </q-card-section>
          </q-card>
        </div>
        <div class="col-12 col-md-4">
          <q-card :class="balance >= 0 ? 'bg-blue text-white' : 'bg-orange text-white'">
            <q-card-section>
              <div class="text-subtitle2">Баланс</div>
              <div class="text-h5">{{ formatMoney(balance) }}</div>
            </q-card-section>
          </q-card>
        </div>
      </div>

      <!-- Графики -->
      <div class="row q-col-gutter-md">
        <!-- Линейный график: динамика расходов по дням -->
        <div class="col-12 col-md-6">
          <q-card>
            <q-card-section>
              <div class="text-h6 q-mb-sm">Динамика расходов за 7 дней</div>
              <div style="height: 300px">
                <LineChart
                  :labels="dailyData.labels"
                  :expense-data="dailyData.expenses"
                  :income-data="dailyData.incomes"
                />
              </div>
            </q-card-section>
          </q-card>
        </div>

        <!-- Круговая диаграмма: распределение расходов по категориям -->
        <div class="col-12 col-md-6">
          <q-card>
            <q-card-section>
              <div class="text-h6 q-mb-sm">Расходы по категориям</div>
              <div style="height: 300px">
                <PieChart
                  v-if="categoryData.labels.length > 0"
                  :labels="categoryData.labels"
                  :data="categoryData.amounts"
                />
                <div v-else class="flex flex-center full-height text-grey">
                  Нет данных о расходах
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>
      </div>
    </template>
  </q-page>
</template>

<script lang="ts" setup>
import { useTransactionAnalytics } from 'src/composables/useTransactionAnalytics';
import LineChart from 'src/components/LineChart.vue';
import PieChart from 'src/components/PieChart.vue';

const {
  dailyData,
  categoryData,
  totalExpenses,
  totalIncomes,
  balance,
  isLoading,
} = useTransactionAnalytics();

const formatMoney = (amount: number) => {
  return `${amount.toFixed(2)} ₽`;
};
</script>

<style lang="scss" scoped>
</style>
