<template>
  <q-page class="q-pa-md">
    <div class="text-h5 q-mb-md">
      Счета
    </div>

    <!-- Список счетов -->
    <q-list bordered separator class="rounded-borders">
      <q-item v-for="account in accounts" :key="account.id">
        <q-item-section>
          <q-item-label>{{ account.name }}</q-item-label>
          <q-item-label caption>
            <q-badge color="blue">
              {{ account.currency }}
            </q-badge>
            <q-badge v-if="account.archived" color="grey" class="q-ml-xs">
              Архив
            </q-badge>
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
              @click="editAccount(account.id)"
            >
              <q-tooltip>Редактировать</q-tooltip>
            </q-btn>
            <q-btn
              flat
              round
              dense
              color="negative"
              icon="delete"
              @click="confirmDelete(account)"
            >
              <q-tooltip>Удалить</q-tooltip>
            </q-btn>
          </div>
        </q-item-section>
      </q-item>

      <q-item v-if="!accounts.length && !isLoading">
        <q-item-section class="text-center text-grey">
          Счета не найдены
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
        @click="addAccount"
      />
    </div>

    <!-- Диалог подтверждения удаления -->
    <q-dialog v-model="deleteDialog" @hide="resetDelete">
      <q-card style="min-width: 300px">
        <q-card-section>
          <div class="text-h6">Подтверждение удаления</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          Вы действительно хотите удалить счет "{{ accountToDelete?.name }}"?
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Отмена" color="primary" v-close-popup />
          <q-btn
            flat
            label="Удалить"
            color="negative"
            :loading="archiveMutation.isPending.value"
            @click="archiveAccount"
          />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useQuasar } from 'quasar';
import type { Account } from 'src/types/account';
import {
  useAccountsQuery,
  useArchiveAccountMutation,
} from 'src/services/accounts.api';

const router = useRouter();
const $q = useQuasar();

const PAGE_SIZE = 20;
const currentPage = ref(1);

const from = computed(() => (currentPage.value - 1) * PAGE_SIZE);

const queryParams = computed(() => ({
  from: from.value || undefined,
  size: PAGE_SIZE || undefined,
}));

const { data, isLoading } = useAccountsQuery(queryParams);

const accounts = computed<Account[]>(() => data.value?.items || []);
const totalPages = computed(() => {
  const count = accounts.value.length;
  return count > 0 ? Math.ceil(count / PAGE_SIZE) : 1;
});

const archiveMutation = useArchiveAccountMutation();

const deleteDialog = ref(false);
const accountToDelete = ref<Account | null>(null);

const addAccount = () => {
  router.push({ name: 'account-new' });
};

const editAccount = (id: number) => {
  router.push({ name: 'account-edit', params: { id } });
};

const confirmDelete = (account: Account) => {
  accountToDelete.value = account;
  deleteDialog.value = true;
};

const resetDelete = () => {
  accountToDelete.value = null;
};

const archiveAccount = async () => {
  if (!accountToDelete.value) return;

  try {
    await archiveMutation.mutateAsync(accountToDelete.value.id);
    $q.notify({
      type: 'positive',
      message: 'Счет успешно удален',
    });
    deleteDialog.value = false;
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Ошибка при удалении счета',
    });
  }
};
</script>

<style lang="scss" scoped>
</style>
