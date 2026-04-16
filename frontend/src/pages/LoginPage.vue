<!-- src/pages/LoginPage.vue -->
<template>
    <q-page class="q-pa-md">
        <div class="login-page full-height flex flex-center">
            <q-card class="q-pa-lg" style="width: 100%; max-width: 400px">
                <q-card-section class="text-center">
                    <div class="text-h5 q-mb-md">Вход в систему</div>
                </q-card-section>

                <q-card-section>
                    <q-form @submit="onSubmit" class="">
                        <q-input
                            v-model="form.email"
                            label="Email"
                            type="email"
                            bg-color="white"
                            :rules="[val => !!val || 'Email обязателен', val => /.+@.+\..+/.test(val) || 'Некорректный email']"
                        />

                        <q-input
                            v-model="form.password"
                            label="Пароль"
                            type="password"
                            :rules="[val => !!val || 'Пароль обязателен']"
                        />

                        <q-btn
                            :loading="loginMutation.isPending.value"
                            label="Войти"
                            type="submit"
                            color="primary"
                            class="full-width"
                        />

                        <q-banner
                        v-if="loginMutation.error.value"
                        class="bg-red-1 text-red-9 full-width rounded-borders"
                        >
                        {{ loginMutation.error.value.message }}
                        </q-banner>
                    </q-form>
                </q-card-section>

                <q-card-actions align="center">
                    <router-link to="/auth/register" class="text-primary">
                        Нет аккаунта? Зарегистрироваться
                    </router-link>
                </q-card-actions>
            </q-card>
        </div>
    </q-page>
  </template>

  <script setup lang="ts">
  import { ref } from 'vue';
  import { useRouter, useRoute } from 'vue-router';
  import { useAuthStore } from 'src/stores/auth';
  import { useLoginMutation } from 'src/services/auth.api';
  import { useQuasar } from 'quasar';

  const router = useRouter();
  const route = useRoute();
  const $q = useQuasar();

  console.log('$q available:', !!$q) // true
  console.log('$q.notify type:', typeof $q.notify) // 'function'

  const authStore = useAuthStore();
  const loginMutation = useLoginMutation();

  const form = ref({
    email: '',
    password: '',
  });

  const onSubmit = async () => {
    try {
      const response = await loginMutation.mutateAsync(form.value);
      authStore.setAuth(response);

      $q.notify({
        type: 'positive',
        message: 'Успешный вход!',
      });

      // Редирект на страницу, с которой перенаправили, или на главную
      const redirect = route.query.redirect as string || '/';
      router.replace(redirect);
    } catch (error: any) {
      console.error('Login error:', error);
    }
  };
  </script>

<style scoped>
.login-page {
    min-height: 100vh;
}
</style>
