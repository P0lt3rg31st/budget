
import { boot } from 'quasar/wrappers';
import { VueQueryPlugin } from '@tanstack/vue-query';
import type { App } from 'vue';

export default boot(({ app }: { app: App }) => {
  app.use(VueQueryPlugin, {
    queryClientConfig: {
      defaultOptions: {
        queries: {
          retry: 1,
          staleTime: 5 * 60 * 1000, // 5 минут
        },
      },
    },
  });
});
