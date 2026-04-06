<template>
  <q-item>
    <q-item-section avatar class="top-aligned">
      <q-checkbox
        :model-value="todo.done"
        @update:model-value="toggle"
        color="primary"
      />
    </q-item-section>

    <q-item-section>
      <q-item-label :class="{ 'text-decoration-line-through text-grey-6': todo.done }">
        {{ todo.title }}
      </q-item-label>
      <q-item-label caption lines="2">
        {{ todo.description || '\u00A0' }}
      </q-item-label>
    </q-item-section>

    <q-item-section side>
      <q-btn
        flat
        round
        dense
        icon="delete"
        color="negative"
        @click="remove"
      />
    </q-item-section>
  </q-item>

</template>

<script lang="ts">
import { defineComponent } from 'vue'
import type {PropType} from 'vue'
import type { ITodoItem } from '../types/ITodoItem'
import { useTodoStore } from '../stores/todoStore'
import { Dialog } from 'quasar';

export default defineComponent({
  name: 'TodoItem',

  props: {
    todo: {
      type: Object as PropType<ITodoItem>,
      required: true
    }
  },

  setup(props) {
    const todoStore = useTodoStore()

    const toggle = (): void => {
      todoStore.toggleTodo(props.todo.id)
  }

    const remove = (): void => {
      Dialog.create({
        title: 'Подтверждение',
        message: `Удалить задачу "${props.todo.title}"?`,
        persistent: true,
        ok: {
          label: 'Удалить',
          color: 'negative'
        },
        cancel: {
          label: 'Отмена',
          flat: true
        }
      }).onOk(() => {
        todoStore.removeTodo(props.todo.id)
      });
    };

    return {
      toggle,
      remove
    };
  }
})
</script>

<style land="scss" scoped>

.text-decoration-line-through {
    text-decoration: line-through;
}

.top-aligned {
    align-items: flex-start;
    margin-top: -12px;
}

</style>