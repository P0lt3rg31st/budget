<template>
  <q-page class="q-pa-md">
    <div class="text-h5 q-mb-md">
      Категории расходов и доходов
    </div>

    <!-- Фильтр по типу -->
    <div class="q-mb-md">
      <q-btn-toggle
        v-model="filterType"
        toggle-color="secondary"
        :options="[
          { label: 'Все', value: null },
          { label: 'Расходы', value: 'EXPENSE' },
          { label: 'Доходы', value: 'INCOME' }
        ]"
        unelevated
      />
    </div>

    <!-- Список категорий -->
    <q-list bordered separator class="rounded-borders">
      <q-item v-for="category in categories" :key="category.id">
        <q-item-section>
          <q-item-label>{{ category.name }}</q-item-label>
          <q-item-label caption>
            <q-badge :color="category.type === 'EXPENSE' ? 'red' : 'green'">
              {{ category.type === 'EXPENSE' ? 'Расход' : 'Доход' }}
            </q-badge>
            <q-badge v-if="category.archived" color="grey" class="q-ml-xs">
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
              @click="editCategory(category.id)"
            >
              <q-tooltip>Редактировать</q-tooltip>
            </q-btn>
            <q-btn
              flat
              round
              dense
              color="negative"
              icon="delete"
              @click="confirmDelete(category)"
            >
              <q-tooltip>Удалить</q-tooltip>
            </q-btn>
          </div>
        </q-item-section>
      </q-item>

      <q-item v-if="!categories.length && !isLoading">
        <q-item-section class="text-center text-grey">
          Категории не найдены
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
        @click="addCategory"
      />
    </div>

    <!-- Диалог подтверждения удаления -->
    <q-dialog v-model="deleteDialog" @hide="resetDelete">
      <q-card style="min-width: 300px">
        <q-card-section>
          <div class="text-h6">Подтверждение удаления</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          Вы действительно хотите удалить категорию "{{ categoryToDelete?.name }}"?
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Отмена" color="primary" v-close-popup />
          <q-btn
            flat
            label="Удалить"
            color="negative"
            :loading="deleteMutation.isPending.value"
            @click="deleteCategory"
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
import type { Category, CategoryType } from 'src/types/category';
import {
  useCategoriesQuery,
  useDeleteCategoryMutation,
} from 'src/services/categories.api';

const router = useRouter();
const $q = useQuasar();

const PAGE_SIZE = 20;
const filterType = ref<CategoryType | null>(null);
const currentPage = ref(1);

const from = computed(() => (currentPage.value - 1) * PAGE_SIZE);

const queryParams = computed(() => ({
  type: filterType.value || undefined,
  from: from.value || undefined,
  size: PAGE_SIZE || undefined,
}));

const { data, isLoading } = useCategoriesQuery(queryParams);

const categories = computed<Category[]>(() => data.value || []);
const totalPages = computed(() => {
  const count = categories.value.length;
  return count > 0 ? Math.ceil(count / PAGE_SIZE) : 1;
});

const deleteMutation = useDeleteCategoryMutation();

const deleteDialog = ref(false);
const categoryToDelete = ref<Category | null>(null);

const addCategory = () => {
  router.push({ name: 'category-new' });
};

const editCategory = (id: number) => {
  router.push({ name: 'category-edit', params: { id } });
};

const confirmDelete = (category: Category) => {
  categoryToDelete.value = category;
  deleteDialog.value = true;
};

const resetDelete = () => {
  categoryToDelete.value = null;
};

const deleteCategory = async () => {
  if (!categoryToDelete.value) return;

  try {
    await deleteMutation.mutateAsync(categoryToDelete.value.id);
    $q.notify({
      type: 'positive',
      message: 'Категория успешно удалена',
    });
    deleteDialog.value = false;
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Ошибка при удалении категории',
    });
  }
};
</script>

<style lang="scss" scoped>
</style>
