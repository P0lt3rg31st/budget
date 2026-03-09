
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

export const useAuthStore = defineStore('auth', () => {
  const accessToken = ref<string | null>(null);
  const tokenType = ref<string>('Bearer');
  const expiresIn = ref<number>(0);
  const tokenExpiry = ref<number | null>(null);

  const router = useRouter();

  const isAuthenticated = computed(() => {
    if (!accessToken.value || !tokenExpiry.value) return false;
    return Date.now() < tokenExpiry.value;
  });

  const setAuth = (data: {
    tokenType: string;
    accessToken: string;
    expiresIn: number;
  }) => {
    accessToken.value = data.accessToken;
    tokenType.value = data.tokenType;
    expiresIn.value = data.expiresIn;
    tokenExpiry.value = Date.now() + data.expiresIn * 1000;
    
    localStorage.setItem('auth_token', data.accessToken);
    localStorage.setItem('auth_expiry', String(tokenExpiry.value));
  };

  const loadPersistedAuth = () => {
    const token = localStorage.getItem('auth_token');
    const expiry = localStorage.getItem('auth_expiry');
    
    if (token && expiry && Date.now() < Number(expiry)) {
      accessToken.value = token;
      tokenExpiry.value = Number(expiry);
      return true;
    }
    localStorage.removeItem('auth_token');
    localStorage.removeItem('auth_expiry');
    return false;
  };

  const logout = () => {
    accessToken.value = null;
    tokenExpiry.value = null;
    localStorage.removeItem('auth_token');
    localStorage.removeItem('auth_expiry');
    router.push('/login');
  };

  return {
    accessToken,
    tokenType,
    expiresIn,
    isAuthenticated,
    setAuth,
    loadPersistedAuth,
    logout,
  };
});