<!-- src/pages/RegisterPage.vue -->
<template>
  <q-page class="q-pa-md">
    <div class="register-page full-height flex flex-center">
      <q-card class="q-pa-lg" style="width: 100%; max-width: 400px">
        <q-card-section class="text-center">
          <div class="text-h5 q-mb-md">Регистрация</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit="onSubmit">
            <q-input
              v-model="form.email"
              label="Email"
              type="email"
              bg-color="white"
              :rules="[
                val => !!val || 'Email обязателен',
                val => /.+@.+\..+/.test(val) || 'Некорректный email'
              ]"
            />

            <q-input
              v-model="form.displayName"
              label="Имя"
              type="text"
              bg-color="white"
              :rules="[val => !!val || 'Имя обязателено']"
            />

            <q-input
              v-model="form.password"
              label="Пароль"
              type="password"
              bg-color="white"
              :rules="[
                val => !!val || 'Пароль обязателен',
                val => val.length >= 8 || 'Пароль должен содержать не менее 8 символов'
              ]"
            />

            <q-input
              v-model="form.passwordConfirm"
              label="Подтверждение пароля"
              type="password"
              bg-color="white"
              :rules="[
                val => !!val || 'Подтверждение пароля обязательно',
                val => val === form.password || 'Пароли не совпадают'
              ]"
            />

            <q-btn
              :loading="registerMutation.isPending.value"
              label="Зарегистрироваться"
              type="submit"
              color="primary"
              class="full-width q-mt-md"
            />

            <q-banner
              v-if="registerMutation.error.value"
              class="bg-red-1 text-red-9 full-width rounded-borders q-mt-md"
            >
              {{ registerMutation.error.value.message }}
            </q-banner>
          </q-form>
        </q-card-section>

        <q-card-actions align="center">
          <router-link to="/auth/login" class="text-primary">
            Уже есть аккаунт? Войти
          </router-link>
        </q-card-actions>
      </q-card>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from 'src/stores/auth';
import { useRegisterMutation } from 'src/services/auth.api';
import { useQuasar } from 'quasar';

const router = useRouter();
const $q = useQuasar();

const authStore = useAuthStore();
const registerMutation = useRegisterMutation();

const form = ref({
  email: '',
  displayName: '',
  password: '',
  passwordConfirm: '',
});

const onSubmit = async () => {
  try {
    await registerMutation.mutateAsync({
      email: form.value.email,
      displayName: form.value.displayName,
      password: form.value.password,
    });

    $q.notify({
      type: 'positive',
      message: 'Регистрация успешна! Теперь вы можете войти.',
    });

    // Редирект на страницу входа
    router.push('/auth/login');
  } catch (error: any) {
    console.error('Register error:', error);
    // Ошибка уже отображается через banner
  }
};
</script>

<style scoped>
.register-page {
  min-height: 100vh;
}
</style>
