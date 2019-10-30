<template>
  <q-page>
    <q-list bordered class="rounded-borders">
      <market v-for="m in orderedMarkets"
              :key="m.id"
              :item="m"
              :now="syncedNow"
              />
    </q-list>
    <q-page-scroller position="bottom-right" :scroll-offset="150" :offset="[18, 18]">
      <q-btn fab icon="keyboard_arrow_up" color="accent" ></q-btn>
    </q-page-scroller>
  </q-page>
</template>

<script>
import { mapState } from 'vuex';
import marketComponent from 'src/components/index/Market';

export default {
  name: 'PageIndex',
  components: {
    market: marketComponent,
  },
  data() {
    return {
    };
  },
  computed: {
    ...mapState('livedata', {
      orderedMarkets: state => state.orderedMarkets,
      syncedNow: state => state.syncedNow,
    }),
  },
  mounted() {
    this.loadData();
  },
  methods: {
    loadData() {
      this.$axios.get('markets/list')
        .then(({ data }) => {
          this.$store.commit('livedata/setMarkets', data);
        }).catch((error) => {
          this.$q.notify({
            message: error,
            position: 'top-right',
          });
        });
    },
  },
};
</script>
