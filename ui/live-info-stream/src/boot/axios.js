import axios from 'axios';

export default async ({ Vue }) => {
  axios.defaults.baseURL = process.env.LIVESTREAM_API_ROOT;
  Vue.prototype.$axios = axios;
};
