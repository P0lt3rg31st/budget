// src/services/transactions.api.ts
import api from './api';
import { useMutation, useQuery, useQueryClient } from '@tanstack/vue-query';
import type {
  Transaction,
  TransactionCreateRequest,
  TransactionUpdateRequest,
  TransactionListResponse,
  FlowType,
  ListTransactionsParams,
} from 'src/types/transaction';
import { Ref } from 'vue';

export const transactionsApi = {
  list: async (params?: ListTransactionsParams): Promise<TransactionListResponse> => {
    const { data } = await api.get<TransactionListResponse>('/api/v1/transactions', { params });
    return data;
  },

  getById: async (id: number): Promise<Transaction> => {
    const { data } = await api.get<Transaction>(`/api/v1/transactions/${id}`);
    return data;
  },

  create: async (request: TransactionCreateRequest): Promise<Transaction> => {
    const { data } = await api.post<Transaction>('/api/v1/transactions', request);
    return data;
  },

  update: async (id: number, request: TransactionUpdateRequest): Promise<Transaction> => {
    const { data } = await api.patch<Transaction>(`/api/v1/transactions/${id}`, request);
    return data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/api/v1/transactions/${id}`);
  },
};

export const useTransactionsQuery = (params?: Ref<ListTransactionsParams>) => {
  return useQuery({
    queryKey: ['transactions', params],
    queryFn: () => transactionsApi.list(params?.value),
  });
};

export const useTransactionQuery = (id: number) => {
  return useQuery({
    queryKey: ['transaction', id],
    queryFn: () => transactionsApi.getById(id),
    enabled: !!id,
  });
};

export const useCreateTransactionMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: transactionsApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['transactions'] });
    },
  });
};

export const useUpdateTransactionMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, request }: { id: number; request: TransactionUpdateRequest }) =>
      transactionsApi.update(id, request),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['transactions'] });
      queryClient.invalidateQueries({ queryKey: ['transaction'] });
    },
  });
};

export const useDeleteTransactionMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: transactionsApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['transactions'] });
    },
  });
};
