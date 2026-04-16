
import { getRouter } from 'src/router/router';
import { useAuthStore } from '../stores/auth';
import api from 'src/services/api';


// добавляем токен
api.interceptors.request.use((config) => {
  const authStore = useAuthStore();
  const token = authStore.accessToken;
  
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// обрабатываем 401
api.interceptors.response.use(
  (response) => response,
  async (error) => {

    if (error.response?.status === 401) {
      const authStore = useAuthStore();
      authStore.logout();
      getRouter().push('/auth/login');
    }
    return Promise.reject(error);
  }
);
