
import type { Router } from 'vue-router';
import { useAuthStore } from 'src/stores/auth';

export function setupAuthGuard(router: Router) {
  router.beforeEach((to, from, next) => {

    console.log('guard');

    const authStore = useAuthStore();
    
    // Проверяем сохранённый токен при первой загрузке
    if (!authStore.accessToken) {
      authStore.loadPersistedAuth();
    }

    // Публичные маршруты
    const publicRoutes = ['/login', '/register', '/forgot-password'];
    if (publicRoutes.includes(to.path)) {
      if (authStore.isAuthenticated) {
        return next('/'); // Уже авторизован — редирект на главную
      }
      return next();
    }

    // Защищённые маршруты
    if (authStore.isAuthenticated) {
      return next();
    }

    // Не авторизован — на логин с возвратом
    next({ path: '/login', query: { redirect: to.fullPath } });
  });
}
