import type { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
        {
            path: '',
            name: 'index',
            component: () => import('../pages/DashboardPage.vue')
        },
        {
            path: 'accounts',
            name: 'accounts',
            component: () => import('../pages/AccountsPage.vue')
        },
        {
            path: 'categories',
            name: 'categories',
            component: () => import('../pages/CategoriesPage.vue')
        },
        {
            path: 'categories/new',
            name: 'category-new',
            component: () => import('../pages/CategoryFormPage.vue')
        },
        {
            path: 'categories/:id/edit',
            name: 'category-edit',
            component: () => import('../pages/CategoryFormPage.vue')
        },
        {
            path: 'accounts/new',
            name: 'account-new',
            component: () => import('../pages/AccountFormPage.vue')
        },
        {
            path: 'accounts/:id/edit',
            name: 'account-edit',
            component: () => import('../pages/AccountFormPage.vue')
        },
        {
            path: 'transactions',
            name: 'transactions',
            component: () => import('../pages/TransactionsPage.vue')
        },
        {
            path: 'transactions/new',
            name: 'transaction-new',
            component: () => import('../pages/TransactionFormPage.vue')
        },
        {
            path: 'transactions/:id/edit',
            name: 'transaction-edit',
            component: () => import('../pages/TransactionFormPage.vue')
        },
    ]
  },
  {
    path: '/auth',
    component: () => import('layouts/AuthLayout.vue'),
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import('../pages/LoginPage.vue')
      },
      {
        path: 'register',
        name: 'register',
        component: () => import('../pages/RegisterPage.vue')
      }
    ]
  },
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
];

export default routes;
