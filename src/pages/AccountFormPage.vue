<template>
  <q-page class="q-pa-md">
    <div class="text-h5 q-mb-md">
      {{ isEditMode ? 'Редактирование счета' : 'Новый счет' }}
    </div>

    <q-form @submit="onSubmit">
      <q-card class="q-pa-lg" style="width: 100%">
        <q-card-section class="text-center">
          <q-input
            v-model="form.name"
            label="Название"
            :rules="[val => !!val || 'Название обязательно']"
            class="q-mb-md"
          />

          <q-select
            v-model="form.currency"
            label="Валюта"
            :options="currencyOptions"
            :rules="[val => !!val || 'Валюта обязательна']"
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
import type { AccountCreateRequest, AccountUpdateRequest } from 'src/types/account';
import {
  useAccountQuery,
  useCreateAccountMutation,
  useUpdateAccountMutation,
} from 'src/services/accounts.api';

const router = useRouter();
const route = useRoute();
const $q = useQuasar();

const currencyOptions = ['RUB', 'USD', 'EUR'];

const isEditMode = computed(() => !!route.params.id);

const form = ref({
  name: '',
  currency: '',
});

const accountId = computed(() => {
  const id = route.params.id as string;
  return id ? parseInt(id, 10) : 0;
});

const { data: existingAccount } = useAccountQuery(accountId.value);

const createMutation = useCreateAccountMutation();
const updateMutation = useUpdateAccountMutation();

const saveMutation = computed(() =>
  isEditMode.value ? updateMutation : createMutation
);

watch(
  existingAccount,
  (account) => {
    if (account && isEditMode.value) {
      form.value.name = account.name;
      form.value.currency = account.currency;
    }
  },
  { immediate: true }
);

const onSubmit = async () => {
  try {
    if (isEditMode.value) {
      const request: AccountUpdateRequest = {
        name: form.value.name,
      };
      await updateMutation.mutateAsync({
        id: accountId.value,
        request,
      });

      $q.notify({
        type: 'positive',
        message: 'Счет успешно обновлен',
      });
    } else {
      const request: AccountCreateRequest = {
        name: form.value.name,
        currency: form.value.currency,
      };
      await createMutation.mutateAsync(request);

      $q.notify({
        type: 'positive',
        message: 'Счет успешно создан',
      });
    }

    router.push({ name: 'accounts' });
  } catch (error) {
    console.error('Save error:', error);
  }
};

const cancel = () => {
  router.push({ name: 'accounts' });
};
</script>

<style scoped>
.account-form-page {
  min-height: 100vh;
}
</style>
