// src/services/categories.api.ts
import api from './api';
import { useMutation, useQuery, useQueryClient } from '@tanstack/vue-query';
import type { Category, CategoryCreateRequest, CategoryUpdateRequest, CategoryListResponse, CategoryType } from 'src/types/category';
import { Ref } from 'vue';

export interface ListCategoriesParams {
  type?: CategoryType | undefined;
  from?: number | undefined;
  size?: number | undefined;
}

export const categoriesApi = {
  list: async (params?: ListCategoriesParams): Promise<CategoryListResponse> => {
    const { data } = await api.get<CategoryListResponse>('/api/v1/categories', { params });
    return data;
  },

  getById: async (id: number): Promise<Category> => {
    const { data } = await api.get<Category>(`/api/v1/categories/${id}`);
    return data;
  },

  create: async (request: CategoryCreateRequest): Promise<Category> => {
    const { data } = await api.post<Category>('/api/v1/categories', request);
    return data;
  },

  update: async (id: number, request: CategoryUpdateRequest): Promise<Category> => {
    const { data } = await api.patch<Category>(`/api/v1/categories/${id}`, request);
    return data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/api/v1/categories/${id}`);
  },
};

export const useCategoriesQuery = (params?: Ref<ListCategoriesParams>) => {
  return useQuery({
    queryKey: ['categories', params],
    queryFn: () => categoriesApi.list(params?.value),
  });
};

export const useCategoryQuery = (id: number) => {
  return useQuery({
    queryKey: ['category', id],
    queryFn: () => categoriesApi.getById(id),
    enabled: !!id,
  });
};

export const useCreateCategoryMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: categoriesApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['categories'] });
    },
  });
};

export const useUpdateCategoryMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, request }: { id: number; request: CategoryUpdateRequest }) =>
      categoriesApi.update(id, request),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['categories'] });
      queryClient.invalidateQueries({ queryKey: ['category'] });
    },
  });
};

export const useDeleteCategoryMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: categoriesApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['categories'] });
    },
  });
};
