// src/services/accounts.api.ts
import api from './api';
import { useMutation, useQuery, useQueryClient } from '@tanstack/vue-query';
import type { Account, AccountCreateRequest, AccountUpdateRequest, AccountListResponse } from 'src/types/account';
import { Ref } from 'vue';

export interface ListAccountsParams {
  from?: number | undefined;
  size?: number | undefined;
}

export const accountsApi = {
  list: async (params?: ListAccountsParams): Promise<AccountListResponse> => {
    const { data } = await api.get<AccountListResponse>('/api/v1/accounts', { params });
    return data;
  },

  getById: async (id: number): Promise<Account> => {
    const { data } = await api.get<Account>(`/api/v1/accounts/${id}`);
    return data;
  },

  create: async (request: AccountCreateRequest): Promise<Account> => {
    const { data } = await api.post<Account>('/api/v1/accounts', request);
    return data;
  },

  update: async (id: number, request: AccountUpdateRequest): Promise<Account> => {
    const { data } = await api.patch<Account>(`/api/v1/accounts/${id}`, request);
    return data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/api/v1/accounts/${id}`);
  },
};

export const useAccountsQuery = (params?: Ref<ListAccountsParams>) => {
  return useQuery({
    queryKey: ['accounts', params],
    queryFn: () => accountsApi.list(params?.value),
  });
};

export const useAccountQuery = (id: number) => {
  return useQuery({
    queryKey: ['account', id],
    queryFn: () => accountsApi.getById(id),
    enabled: !!id,
  });
};

export const useCreateAccountMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: accountsApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['accounts'] });
    },
  });
};

export const useUpdateAccountMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, request }: { id: number; request: AccountUpdateRequest }) =>
      accountsApi.update(id, request),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['accounts'] });
      queryClient.invalidateQueries({ queryKey: ['account'] });
    },
  });
};

export const useDeleteAccountMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: accountsApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['accounts'] });
    },
  });
};
