export type FlowType = 'EXPENSE' | 'INCOME';

export interface Transaction {
  id: number;
  accountId: number;
  type: FlowType;
  categoryId: number;
  counterpartyName: string | null;
  note: string | null;
  occurredAt: string;
  amount: string;
  createdAt: string;
  updatedAt?: string | null;
}

export interface TransactionCreateRequest {
  accountId: number;
  type: FlowType;
  categoryId: number;
  counterpartyName?: string | null;
  note?: string | null;
  occurredAt: string;
  amount: string;
}

export interface TransactionUpdateRequest {
  type?: FlowType;
  categoryId?: number;
  counterpartyName?: string | null;
  note?: string | null;
  occurredAt?: string;
  amount?: string;
}

export interface TransactionListResponse {
  items: Transaction[];
}

export interface ListTransactionsParams {
  accountId?: number | undefined;
  type?: FlowType | undefined;
  categoryId?: number | undefined;
  occurredFrom?: string | undefined;
  occurredTo?: string | undefined;
  from?: number | undefined;
  size?: number | undefined;
}
