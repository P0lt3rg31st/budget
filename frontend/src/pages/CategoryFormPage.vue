<template>
  <q-page class="q-pa-md">
    <!-- <q-card class="q-pa-lg" style="width: 100%; max-width: 400px"> -->
      <!-- <q-card-section class="text-center"> -->
        <div class="text-h5 q-mb-md">
          {{ isEditMode ? 'Редактирование категории' : 'Новая категория' }}
        </div>
      <!-- </q-card-section> -->

      <!-- <q-card-section> -->
        <q-form @submit="onSubmit">
            <q-card class="q-pa-lg" style="width: 100%">
                <q-card-section class="text-center">
                    <q-input
                        v-model="form.name"
                        label="Название"
                        :rules="[val => !!val || 'Название обязательно']"
                        class="q-mb-md"
                    />

                    <q-btn-toggle
                        v-model="form.type"
                        label="Тип категории"
                        toggle-color="secondary"
                        :options="[
                        { label: 'Расход', value: 'EXPENSE' },
                        { label: 'Доход', value: 'INCOME' }
                        ]"
                        unelevated
                        class="full-width q-mb-md"
                        :rules="[(val: string) => !!val || 'Тип обязателен']"
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
      <!-- </q-card-section> -->
    <!-- </q-card> -->
  </q-page>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useQuasar } from 'quasar';
import type { CategoryType, CategoryCreateRequest, CategoryUpdateRequest } from 'src/types/category';
import {
  useCategoryQuery,
  useCreateCategoryMutation,
  useUpdateCategoryMutation,
} from 'src/services/categories.api';

const router = useRouter();
const route = useRoute();
const $q = useQuasar();

const isEditMode = computed(() => !!route.params.id);

const form = ref({
  name: '',
  type: 'EXPENSE' as CategoryType,
});

const categoryId = computed(() => {
  const id = route.params.id as string;
  return id ? parseInt(id, 10) : 0;
});

const { data: existingCategory } = useCategoryQuery(categoryId.value);

const createMutation = useCreateCategoryMutation();
const updateMutation = useUpdateCategoryMutation();

const saveMutation = computed(() =>
  isEditMode.value ? updateMutation : createMutation
);

watch(
  existingCategory,
  (category) => {
    if (category && isEditMode.value) {
      form.value.name = category.name;
      form.value.type = category.type;
    }
  },
  { immediate: true }
);

const onSubmit = async () => {
  try {
    if (isEditMode.value) {
      const request: CategoryUpdateRequest = {
        name: form.value.name,
      };
      await updateMutation.mutateAsync({
        id: categoryId.value,
        request,
      });

      $q.notify({
        type: 'positive',
        message: 'Категория успешно обновлена',
      });
    } else {
      const request: CategoryCreateRequest = {
        name: form.value.name,
        type: form.value.type,
      };
      await createMutation.mutateAsync(request);

      $q.notify({
        type: 'positive',
        message: 'Категория успешно создана',
      });
    }

    router.push({ name: 'categories' });
  } catch (error) {
    console.error('Save error:', error);
  }
};

const cancel = () => {
  router.push({ name: 'categories' });
};
</script>

<style scoped>
.category-form-page {
  min-height: 100vh;
}
</style>
