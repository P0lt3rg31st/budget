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
    ]
  },
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
];

export default routes;
