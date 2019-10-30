import VueNativeSock from 'vue-native-websocket';

export default async ({ Vue, store }) => {
  Vue.use(VueNativeSock, process.env.LIVESTREAM_WS_ROOT, {
    store,
    reconnection: true,
    passToStoreHandler: (eventName, ev) => {
      store.dispatch(`livedata/${eventName.toUpperCase()}`, ev);
    },
  });
};
