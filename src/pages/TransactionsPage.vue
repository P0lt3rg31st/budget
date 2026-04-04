<!-- src/pages/TransactionsPage.vue -->
<template>
  <q-page class="q-pa-md">
    <div class="text-h5 q-mb-md">
      Транзакции
    </div>

    <!-- Фильтры -->
    <div class="q-mb-md">
      <q-card>
        <q-card-section class="q-pa-md">
          <div class="row q-col-gutter-sm">
            <div class="col-12 col-md-4">
              <q-select
                v-model="filterAccountId"
                :options="accountOptions"
                label="Счёт"
                clearable
                emit-value
                map-options
                dense
              />
            </div>
            <div class="col-12 col-md-4">
              <q-select
                v-model="filterType"
                :options="typeOptions"
                label="Тип"
                clearable
                emit-value
                map-options
                dense
              />
            </div>
            <div class="col-12 col-md-4">
              <q-select
                v-model="filterCategoryId"
                :options="categoryOptions"
                label="Категория"
                clearable
                emit-value
                map-options
                dense
              />
            </div>
            <div class="col-12 col-md-4">
              <q-input
                v-model="filterDateFrom"
                type="date"
                label="С даты"
                dense
                clearable
              />
            </div>
            <div class="col-12 col-md-4">
              <q-input
                v-model="filterDateTo"
                type="date"
                label="По дату"
                dense
                clearable
              />
            </div>
          </div>
          <!--
          <div class="row q-mt-sm">
            <q-btn
              label="Применить"
              color="primary"
              size="sm"
              @click="applyFilters"
            />
            <q-btn
              label="Сбросить"
              color="grey"
              size="sm"
              class="q-ml-sm"
              @click="resetFilters"
            />
          </div>
          -->
        </q-card-section>
      </q-card>
    </div>

    <!-- Список транзакций -->
    <q-list bordered separator class="rounded-borders">
      <q-item v-for="transaction in transactions" :key="transaction.id">
        <q-item-section>
          <q-item-label>
            <span class="text-weight-medium">{{ transaction.counterpartyName || 'Без названия' }}</span>
            <q-badge
              :color="transaction.type === 'EXPENSE' ? 'red' : 'green'"
              class="q-ml-sm"
            >
              {{ transaction.type === 'EXPENSE' ? 'Расход' : 'Доход' }}
            </q-badge>
          </q-item-label>
          <q-item-label caption>
            <div class="row q-gutter-sm items-center">
              <span>{{ formatAmount(transaction.amount, transaction.type) }}</span>
              <span>•</span>
              <span>{{ formatDate(transaction.occurredAt) }}</span>
              <span v-if="getCategoryName(transaction.categoryId)">•</span>
              <span v-if="getCategoryName(transaction.categoryId)">
                {{ getCategoryName(transaction.categoryId) }}
              </span>
              <span v-if="transaction.note">•</span>
              <span v-if="transaction.note" class="text-grey">{{ transaction.note }}</span>
            </div>
          </q-item-label>
        </q-item-section>

        <q-item-section side>
          <div class="q-gutter-xs">
            <q-btn
              flat
              round
              dense
              color="primary"
              icon="edit"
              @click="editTransaction(transaction.id)"
            >
              <q-tooltip>Редактировать</q-tooltip>
            </q-btn>
            <q-btn
              flat
              round
              dense
              color="negative"
              icon="delete"
              @click="confirmDelete(transaction)"
            >
              <q-tooltip>Удалить</q-tooltip>
            </q-btn>
          </div>
        </q-item-section>
      </q-item>

      <q-item v-if="!transactions.length && !isLoading">
        <q-item-section class="text-center text-grey">
          Транзакции не найдены
        </q-item-section>
      </q-item>
    </q-list>

    <!-- Пагинация -->
    <div v-if="totalPages > 1" class="flex flex-center q-mt-md">
      <q-pagination
        v-model="currentPage"
        :max="totalPages"
        :max-pages="5"
        direction-links
        boundary-links
        icon-first="keyboard_arrow_left"
        icon-last="keyboard_arrow_right"
        icon-prev="keyboard_arrow_up"
        icon-next="keyboard_arrow_down"
      />
    </div>

    <!-- Кнопка добавить -->
    <div class="q-mt-md">
      <q-btn
        color="primary"
        label="Добавить"
        icon="add"
        @click="addTransaction"
      />
    </div>

    <!-- Диалог подтверждения удаления -->
    <q-dialog v-model="deleteDialog" @hide="resetDelete">
      <q-card style="min-width: 300px">
        <q-card-section>
          <div class="text-h6">Подтверждение удаления</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          Вы действительно хотите удалить транзакцию?
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Отмена" color="primary" v-close-popup />
          <q-btn
            flat
            label="Удалить"
            color="negative"
            :loading="deleteMutation.isPending.value"
            @click="deleteTransaction"
          />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script lang="ts" setup>
import { ref, computed, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useQuasar } from 'quasar';
import type { Transaction, FlowType } from 'src/types/transaction';
import {
  useTransactionsQuery,
  useDeleteTransactionMutation,
} from 'src/services/transactions.api';
import { useAccountsQuery } from 'src/services/accounts.api';
import { useCategoriesQuery } from 'src/services/categories.api';

const router = useRouter();
const $q = useQuasar();

const PAGE_SIZE = 20;

// Фильтры
const filterAccountId = ref<number | null>(null);
const filterType = ref<FlowType | null>(null);
const filterCategoryId = ref<number | null>(null);
const filterDateFrom = ref<string | null>(null);
const filterDateTo = ref<string | null>(null);
const currentPage = ref(1);

const from = computed(() => (currentPage.value - 1) * PAGE_SIZE);

const applyFilters = () => {
  // Фильтры применяются реактивно через queryParams
};

const resetFilters = () => {
  filterAccountId.value = null;
  filterType.value = null;
  filterCategoryId.value = null;
  filterDateFrom.value = null;
  filterDateTo.value = null;
  currentPage.value = 1;
};

// Convert date (YYYY-MM-DD) to ISO-8601 datetime with timezone
const formatDateToIso = (dateStr: string | null): string | undefined => {
  if (!dateStr) return undefined;
  // Create date at midnight and convert to ISO
  return new Date(dateStr).toISOString();
};

const queryParams = computed(() => ({
  accountId: filterAccountId.value || undefined,
  type: filterType.value || undefined,
  categoryId: filterCategoryId.value || undefined,
  occurredFrom: formatDateToIso(filterDateFrom.value),
  occurredTo: formatDateToIso(filterDateTo.value),
  from: from.value || undefined,
  size: PAGE_SIZE || undefined,
}));

const { data: transactionsData, isLoading } = useTransactionsQuery(queryParams);
const { data: accountsData } = useAccountsQuery();
const { data: categoriesData } = useCategoriesQuery();

const transactions = computed<Transaction[]>(() => transactionsData.value?.items || []);
const totalPages = computed(() => {
  const count = transactions.value.length;
  return count > 0 ? Math.ceil(count / PAGE_SIZE) : 1;
});

// Опции для фильтров
const accountOptions = computed(() => {
  const accounts = accountsData.value?.items || [];
  return accounts.map(a => ({ label: a.name, value: a.id }));
});

const categoryOptions = computed(() => {
  const categories = categoriesData.value || [];
  return categories.map(c => ({ label: c.name, value: c.id }));
});

const typeOptions = [
  { label: 'Расход', value: 'EXPENSE' },
  { label: 'Доход', value: 'INCOME' },
];

// Сброс страницы при изменении фильтров
watch([filterAccountId, filterType, filterCategoryId, filterDateFrom, filterDateTo], () => {
  currentPage.value = 1;
});

const deleteMutation = useDeleteTransactionMutation();

const deleteDialog = ref(false);
const transactionToDelete = ref<Transaction | null>(null);

const addTransaction = () => {
  router.push({ name: 'transaction-new' });
};

const editTransaction = (id: number) => {
  router.push({ name: 'transaction-edit', params: { id } });
};

const confirmDelete = (transaction: Transaction) => {
  transactionToDelete.value = transaction;
  deleteDialog.value = true;
};

const resetDelete = () => {
  transactionToDelete.value = null;
};

const deleteTransaction = async () => {
  if (!transactionToDelete.value) return;

  try {
    await deleteMutation.mutateAsync(transactionToDelete.value.id);
    $q.notify({
      type: 'positive',
      message: 'Транзакция успешно удалена',
    });
    deleteDialog.value = false;
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Ошибка при удалении транзакции',
    });
  }
};

const formatAmount = (amount: string, type: FlowType) => {
  const sign = type === 'EXPENSE' ? '-' : '+';
  const formatted = parseFloat(amount).toFixed(2);
  return `${sign} ${formatted}`;
};

const formatDate = (dateString: string) => {
  // Parse ISO-8601 date and format to DD.MM.YYYY HH:mm
  const date = new Date(dateString);
  return date.toLocaleString('ru-RU', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
};

const getCategoryName = (categoryId: number) => {
  const categories = categoriesData.value || [];
  const category = categories.find(c => c.id === categoryId);
  return category?.name || null;
};
</script>

<style lang="scss" scoped>
</style>
