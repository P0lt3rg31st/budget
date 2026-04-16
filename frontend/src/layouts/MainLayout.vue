<template>
  <q-layout view="hHh Lpr lFf">

    <q-header elevated class="bg-primary">
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="menu"
          aria-label="Menu"
          @click="toggleLeftDrawer"
        />

        <q-toolbar-title class="text-weight-bold">
          Финансовый дневник
        </q-toolbar-title>
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      bordered
      side="left"
    >
      <q-list>
        <q-item-label header>
          &nbsp;
        </q-item-label>

        <q-item clickable v-ripple to="/" exact>
          <q-item-section avatar>
            <q-icon name="bar_chart" />
          </q-item-section>
          <q-item-section>
            <q-item-label>Аналитика</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable v-ripple to="/accounts" exact>
          <q-item-section avatar>
            <q-icon name="credit_card" />
          </q-item-section>
          <q-item-section>
            <q-item-label>Счета</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable v-ripple to="/categories" exact>
          <q-item-section avatar>
            <q-icon name="list" />
          </q-item-section>
          <q-item-section>
            <q-item-label>Категории</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable v-ripple to="/transactions" exact>
          <q-item-section avatar>
            <q-icon name="swap_horiz" />
          </q-item-section>
          <q-item-section>
            <q-item-label>Транзакции</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable v-ripple @click="logout">
          <q-item-section avatar>
            <q-icon name="logout" />
          </q-item-section>
          <q-item-section>
            <q-item-label>Выйти</q-item-label>
          </q-item-section>
        </q-item>

      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useAuthStore } from 'src/stores/auth'

export default defineComponent({
  name: 'MainLayout',

  setup() {
    const leftDrawerOpen = ref(false)
    const authStore = useAuthStore()

    const toggleLeftDrawer = (): void => {
      leftDrawerOpen.value = !leftDrawerOpen.value
    }

    const logout = (): void => {
      authStore.logout()
    }

    return {
      leftDrawerOpen,
      toggleLeftDrawer,
      logout
    }
  }
})
</script>
