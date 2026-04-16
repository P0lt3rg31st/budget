export interface Account {
  id: number;
  name: string;
  currency: string;
  archived: boolean;
  createdAt: string;
}

export interface AccountCreateRequest {
  name: string;
  currency: string;
}

export interface AccountUpdateRequest {
  name?: string;
  archived?: boolean;
}

export interface AccountListResponse {
  items: Account[];
}
