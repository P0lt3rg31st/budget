<!-- src/pages/TransactionFormPage.vue -->
<template>
  <q-page class="q-pa-md">
    <div class="text-h5 q-mb-md">
      {{ isEditMode ? 'Редактирование транзакции' : 'Новая транзакция' }}
    </div>

    <q-form @submit="onSubmit">
      <q-card class="q-pa-lg" style="width: 100%">
        <q-card-section class="text-center">
          <q-select
            v-model="form.accountId"
            :options="accountOptions"
            label="Счёт"
            emit-value
            map-options
            :rules="[val => !!val || 'Счёт обязателен']"
            class="q-mb-md"
          />

          <q-btn-toggle
            v-model="form.type"
            label="Тип транзакции"
            toggle-color="secondary"
            :options="[
              { label: 'Расход', value: 'EXPENSE' },
              { label: 'Доход', value: 'INCOME' }
            ]"
            unelevated
            class="full-width q-mb-md"
            :rules="[(val: string) => !!val || 'Тип обязателен']"
          />

          <q-input
            v-model="form.amount"
            label="Сумма"
            type="number"
            step="0.01"
            :rules="[
              val => !!val || 'Сумма обязательна',
              val => parseFloat(val) > 0 || 'Сумма должна быть больше 0'
            ]"
            class="q-mb-md"
          />

          <q-input
            v-model="form.occurredAt"
            label="Дата"
            type="date"
            :rules="[val => !!val || 'Дата обязательна']"
            class="q-mb-md"
          />

          <q-select
            v-model="form.categoryId"
            :options="categoryOptions"
            label="Категория"
            emit-value
            map-options
            :rules="[val => !!val || 'Категория обязательна']"
            class="q-mb-md"
          />

          <q-input
            v-model="form.counterpartyName"
            label="Контрагент"
            :rules="[val => !val || val.length <= 120 || 'Максимум 120 символов']"
            class="q-mb-md"
          />

          <q-input
            v-model="form.note"
            label="Заметка"
            type="textarea"
            :rules="[val => !val || val.length <= 500 || 'Максимум 500 символов']"
            class="q-mb-md"
          />
        </q-card-section>

        <q-card-section class="text-left">
          <div class="row q-col-gutter-sm q-mt-lg">
            <div class="col">
              <q-btn
                :loading="saveMutation.isPending.value"
                label="Сохранить"
                type="submit"
                color="primary"
                class="q-mr-sm"
              />
              <q-btn
                label="Отмена"
                color="grey"
                @click="cancel"
              />
            </div>
          </div>
        </q-card-section>
      </q-card>

      <q-banner
        v-if="saveMutation.error.value"
        class="bg-red-1 text-red-9 full-width rounded-borders q-mt-lg"
      >
        {{ saveMutation.error.value.message }}
      </q-banner>
    </q-form>
  </q-page>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useQuasar } from 'quasar';
import { date } from 'quasar';
import type { FlowType, TransactionCreateRequest, TransactionUpdateRequest } from 'src/types/transaction';
import {
  useTransactionQuery,
  useCreateTransactionMutation,
  useUpdateTransactionMutation,
} from 'src/services/transactions.api';
import { useAccountsQuery } from 'src/services/accounts.api';
import { useCategoriesQuery } from 'src/services/categories.api';

const router = useRouter();
const route = useRoute();
const $q = useQuasar();

const isEditMode = computed(() => !!route.params.id);

const form = ref({
  accountId: 0 as number | null,
  type: 'EXPENSE' as FlowType,
  amount: '',
  occurredAt: date.formatDate(Date.now(), 'YYYY-MM-DD') as string,
  categoryId: 0 as number | null,
  counterpartyName: '',
  note: '',
});

const transactionId = computed(() => {
  const id = route.params.id as string;
  return id ? parseInt(id, 10) : 0;
});

const { data: existingTransaction } = useTransactionQuery(transactionId.value);
const { data: accountsData } = useAccountsQuery();
const { data: categoriesData } = useCategoriesQuery();

const createMutation = useCreateTransactionMutation();
const updateMutation = useUpdateTransactionMutation();

const saveMutation = computed(() =>
  isEditMode.value ? updateMutation : createMutation
);

const accountOptions = computed(() => {
  const accounts = accountsData.value?.items || [];
  return accounts.map(a => ({ label: a.name, value: a.id }));
});

const categoryOptions = computed(() => {
  const categories = categoriesData.value || [];
  return categories.map(c => ({ label: c.name, value: c.id }));
});

watch(
  existingTransaction,
  (transaction) => {
    if (transaction && isEditMode.value) {
      form.value.accountId = transaction.accountId;
      form.value.type = transaction.type;
      form.value.amount = transaction.amount;
      form.value.occurredAt = date.formatDate(transaction.occurredAt, 'YYYY-MM-DD') as string;
      form.value.categoryId = transaction.categoryId;
      form.value.counterpartyName = transaction.counterpartyName || '';
      form.value.note = transaction.note || '';
    }
  },
  { immediate: true }
);

const onSubmit = async () => {
  try {
    if (isEditMode.value) {
      const request: TransactionUpdateRequest = {
        type: form.value.type,
        categoryId: form.value.categoryId!,
        counterpartyName: form.value.counterpartyName || null,
        note: form.value.note || null,
        occurredAt: form.value.occurredAt,
        amount: form.value.amount,
      };
      await updateMutation.mutateAsync({
        id: transactionId.value,
        request,
      });

      $q.notify({
        type: 'positive',
        message: 'Транзакция успешно обновлена',
      });
    } else {
      const request: TransactionCreateRequest = {
        accountId: form.value.accountId!,
        type: form.value.type,
        categoryId: form.value.categoryId!,
        counterpartyName: form.value.counterpartyName || null,
        note: form.value.note || null,
        occurredAt: form.value.occurredAt,
        amount: form.value.amount,
      };
      await createMutation.mutateAsync(request);

      $q.notify({
        type: 'positive',
        message: 'Транзакция успешно создана',
      });
    }

    router.push({ name: 'transactions' });
  } catch (error) {
    console.error('Save error:', error);
  }
};

const cancel = () => {
  router.push({ name: 'transactions' });
};
</script>

<style scoped>
.transaction-form-page {
  min-height: 100vh;
}
</style>
