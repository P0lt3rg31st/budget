// src/composables/useTransactionAnalytics.ts
import { computed } from 'vue';
import { useTransactionsQuery } from 'src/services/transactions.api';
import { useCategoriesQuery } from 'src/services/categories.api';
import type { Transaction } from 'src/types/transaction';

interface DailyData {
  labels: string[];
  expenses: number[];
  incomes: number[];
}

interface CategoryData {
  labels: string[];
  amounts: number[];
}

/**
 * Composable для аналитики транзакций.
 * Загружает транзакции за последние 7 дней и агрегирует данные для графиков.
 */
export function useTransactionAnalytics() {
  // Вычисляем диапазон дат: последние 7 дней
  const now = new Date();
  const endDate = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59);
  const startDate = new Date(endDate);
  startDate.setDate(startDate.getDate() - 6);
  startDate.setHours(0, 0, 0, 0);

  const queryParams = computed(() => ({
    occurredFrom: startDate.toISOString(),
    occurredTo: endDate.toISOString(),
    size: 1000, // Загружаем все транзакции за период
  }));

  const { data: transactionsData, isLoading: isTransactionsLoading } =
    useTransactionsQuery(queryParams);

  const { data: categoriesData, isLoading: isCategoriesLoading } = useCategoriesQuery();

  const transactions = computed(() => transactionsData.value?.items || []);
  const categories = computed(() => categoriesData.value || []);

  const isLoading = computed(() => isTransactionsLoading.value || isCategoriesLoading.value);

  /**
   * Агрегация расходов/доходов по дням для линейного графика.
   */
  const dailyData = computed<DailyData>(() => {
    const days = 7;
    const labels: string[] = [];
    const expenses: number[] = [];
    const incomes: number[] = [];

    for (let i = days - 1; i >= 0; i--) {
      const date = new Date();
      date.setDate(date.getDate() - i);
      date.setHours(0, 0, 0, 0);

      const nextDate = new Date(date);
      nextDate.setDate(nextDate.getDate() + 1);

      const dayStart = date.toISOString();
      const dayEnd = nextDate.toISOString();

      // Фильтруем транзакции за этот день
      const dayTransactions = transactions.value.filter((t) => {
        const txDate = new Date(t.occurredAt);
        return txDate >= date && txDate < nextDate;
      });

      // Суммируем расходы и доходы
      const dayExpense = dayTransactions
        .filter((t) => t.type === 'EXPENSE')
        .reduce((sum, t) => sum + parseFloat(t.amount), 0);

      const dayIncome = dayTransactions
        .filter((t) => t.type === 'INCOME')
        .reduce((sum, t) => sum + parseFloat(t.amount), 0);

      // Форматируем метку даты (DD.MM)
      const label = date.toLocaleDateString('ru-RU', {
        day: '2-digit',
        month: '2-digit',
      });

      labels.push(label);
      expenses.push(Math.round(dayExpense * 100) / 100);
      incomes.push(Math.round(dayIncome * 100) / 100);
    }

    return { labels, expenses, incomes };
  });

  /**
   * Агрегация расходов по категориям для круговой диаграммы.
   */
  const categoryData = computed<CategoryData>(() => {
    const expenseTransactions = transactions.value.filter((t) => t.type === 'EXPENSE');

    // Группируем по categoryId
    const categoryMap = new Map<number, number>();

    expenseTransactions.forEach((t) => {
      const amount = parseFloat(t.amount);
      const current = categoryMap.get(t.categoryId) || 0;
      categoryMap.set(t.categoryId, current + amount);
    });

    // Преобразуем в массив с названиями категорий
    const labels: string[] = [];
    const amounts: number[] = [];

    categoryMap.forEach((amount, categoryId) => {
      const category = categories.value.find((c) => c.id === categoryId);
      labels.push(category?.name || `Категория #${categoryId}`);
      amounts.push(Math.round(amount * 100) / 100);
    });

    // Сортируем по убыванию
    const combined = labels.map((label, i) => ({ label, amount: amounts[i] }));
    combined.sort((a, b) => b.amount - a.amount);

    return {
      labels: combined.map((c) => c.label),
      amounts: combined.map((c) => c.amount),
    };
  });

  /**
   * Общая сумма расходов за период.
   */
  const totalExpenses = computed(() => {
    return transactions.value
      .filter((t) => t.type === 'EXPENSE')
      .reduce((sum, t) => sum + parseFloat(t.amount), 0);
  });

  /**
   * Общая сумма доходов за период.
   */
  const totalIncomes = computed(() => {
    return transactions.value
      .filter((t) => t.type === 'INCOME')
      .reduce((sum, t) => sum + parseFloat(t.amount), 0);
  });

  /**
   * Баланс (доходы - расходы).
   */
  const balance = computed(() => totalIncomes.value - totalExpenses.value);

  return {
    dailyData,
    categoryData,
    totalExpenses,
    totalIncomes,
    balance,
    isLoading,
  };
}
