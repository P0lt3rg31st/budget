export type CategoryType = 'EXPENSE' | 'INCOME';

export interface Category {
  id: number;
  name: string;
  type: CategoryType;
  archived: boolean;
  createdAt: string;
}

export interface CategoryCreateRequest {
  name: string;
  type: CategoryType;
}

export interface CategoryUpdateRequest {
  name?: string;
  archived?: boolean;
}

export interface CategoryListResponse {
  items: Category[];
}
