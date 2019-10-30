import rootLayout from '../layouts/RootLayout.vue';

import pageIndex from '../pages/Index.vue';

const routes = [
  {
    path: '/',
    component: rootLayout,
    children: [
      {
        path: '',
        name: 'root',
        component: pageIndex,
      },
    ],
  },
];

// Always leave this as last one
if (process.env.MODE !== 'ssr') {
  routes.push({
    path: '*',
    component: () => import('pages/Error404.vue'),
  });
}

export default routes;
