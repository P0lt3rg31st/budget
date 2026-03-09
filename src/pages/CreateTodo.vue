<template>
  <q-page class="q-pa-md">
    <div class="text-h4 text-weight-bold q-mb-md">Добавить дело</div>

    <q-form ref="formRef" @submit.prevent="onSubmit">

      <q-input
        v-model="title"
        label="Что надо сделать?"
        outlined
        clearable
        :rules="[val => !!val || 'Если надо что то сделать, то нельзя ничего не делать!']"
        lazy-rules
        class="q-mb-md"
      />

      <q-input
        v-model="description"
        label="Подробное описание"
        outlined
        type="textarea"
        class="q-mb-md"
      />

      <div class="row q-col-gutter-sm">

        <div class="col">
          <q-btn
            type="submit"
            color="primary"
            icon="add"
            label="Добавить"
            unelevated
            class="q-mr-sm"
            :disable="!isFormValid"
          />
          <q-btn
            label="Отмена"
            color="secondary"
            icon="cancel"
            unelevated
            to="/"
          />
        </div>
      </div>

    </q-form>
  </q-page>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useTodoStore } from '@/stores/todoStore'

const title = ref('')
const description = ref('')
const formRef = ref()

const store = useTodoStore();

const isFormValid = computed(() => {
  return title.value?.trim().length ?? 0 > 0
})

const onSubmit = async () => {

  console.log('submit handler');

  const isValid = await formRef.value.validate()

  if (!isValid) return

  store.addTodo(title.value.trim(), description.value.trim());
  title.value = '';
  description.value = '';

  formRef.value.resetValidation();
}

</script>
