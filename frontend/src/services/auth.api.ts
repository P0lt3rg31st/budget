// src/services/auth.api.ts
import api from './api';
import { useMutation } from '@tanstack/vue-query';
import type { LoginCredentials, LoginResponse, UserCreateRequest, UserDto } from 'src/types/auth';

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

export const registerApi = async (request: UserCreateRequest): Promise<UserDto> => {
  const { data } = await api.post<UserDto>('/api/v1/users', request);
  return data;
};

export const useLoginMutation = () => {
  return useMutation<LoginResponse, Error, LoginCredentials>({
    mutationFn: loginApi,
  });
};

export const useRegisterMutation = () => {
  return useMutation<UserDto, Error, UserCreateRequest>({
    mutationFn: registerApi,
  });
};
