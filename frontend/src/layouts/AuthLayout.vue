<template>
    <q-layout view="hHh Lpr lFf">
  
      <q-header elevated class="bg-primary">
        <q-toolbar>
          <q-toolbar-title class="text-weight-bold">
            Финансовый дневник
          </q-toolbar-title>
        </q-toolbar>
      </q-header>

      <q-page-container>
        <router-view />
      </q-page-container>
    </q-layout>
  </template>
  
  <script lang="ts">
  import { defineComponent, ref } from 'vue'
  
  export default defineComponent({
    name: 'AuthLayout'
  })
  </script>
