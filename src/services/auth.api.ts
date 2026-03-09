// src/services/auth.api.ts
import api from './api';
import { useMutation } from '@tanstack/vue-query';

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface LoginResponse {
  tokenType: string;
  accessToken: string;
  expiresIn: number;
}

export const loginApi = async (credentials: LoginCredentials): Promise<LoginResponse> => {
  const { data } = await api.post<LoginResponse>('/api/v1/auth/login', credentials);
  return data;
};

export const useLoginMutation = () => {
  return useMutation<LoginResponse, Error, LoginCredentials>({
    mutationFn: loginApi,
  });
};
