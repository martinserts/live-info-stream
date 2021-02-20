<style>
  .volumeHighlighted {
    color: red;
    font-size: 0.80rem;
  }
</style>

<template>
  <div>
    <q-expansion-item :header-class="headerClass"
                      v-model="isOpen">
      <template v-slot:header>
        <q-item-section avatar>
          <q-icon :name="icon" />
        </q-item-section>
        <q-item-section>
          <q-item-label>{{ item.name }}</q-item-label>
          <q-item-label caption>{{ item.event.name }}</q-item-label>
        </q-item-section>
        <q-item-section side top>
          <q-item-label caption>{{ marketTimeAgo }}</q-item-label>
          <q-item-label caption class="text-weight-bold"
                                :class="{volumeHighlighted: tradedVolumeChanged}">
            {{ formattedTradedVolume }}
          </q-item-label>
        </q-item-section>
      </template>
      <q-separator />
      <q-table dense
               :card-class="headerClass"
               separator="horizontal"
               :data="item.runners"
               :columns="columns"
               :pagination.sync="pagination"
               :row-key="row => row.id">
               <template v-slot:top="props">
                 <div class="col-grow q-table__title">{{ title }}</div>
                 <q-space />
                 <div class="col-auto">
                   <q-tooltip v-if="!chartEnabled">
                     Charts are available 20 minutes before the race
                   </q-tooltip>
                   <q-btn flat
                     label="Detailed charts"
                     icon="show_chart"
                     :disable="!chartEnabled"
                     @click="openCharts">
                   </q-btn>
                 </div>
                 <div v-if="isMarketClosed" class="col-auto">
                   <q-btn flat icon="delete_forever" @click="removeMarket">Remove market</q-btn>
                 </div>
               </template>
               <template v-slot:body-cell-name="props">
                 <q-td :props="props">
                   <span :class="selectionNameClass(props.row.status)">
                     {{ props.value }}
                   </span>
                 </q-td>
               </template>
               <template v-slot:body-cell-place="props">
                 <q-td :props="props">
                   <span :class="selectionPlaceClass(props.value)">
                     {{ props.value }}
                   </span>
                 </q-td>
               </template>
               <template v-slot:body-cell-volume="props">
                 <q-td :props="props">
                   <visualProgress
                     v-if="!isStatusRemoved(props.row.status)"
                     :value="props.value / tradedVolume"
                     :label="formatCcy(props.value)"
                     background="grey-7"
                     labelBackground="black"
                     labelColor="grey-3" />
                 </q-td>
               </template>
               <template v-slot:body-cell-price="props">
                 <q-td :props="props">
                   <visualProgress
                     v-if="!isStatusRemoved(props.row.status)"
                     :value="priceProgress(props.value)"
                     :label="formatDecimal(props.value)"
                     :background="priceColor"
                     labelBackground="white"
                     highlightChanges
                     labelHighlighted="red"
                     labelColor="black" />
                 </q-td>
               </template>
      </q-table>
    </q-expansion-item>
    <q-separator />
  </div>
</template>

<script>
import { date, openURL } from 'quasar';
import TimeAgo from 'javascript-time-ago';
import { canonical } from 'javascript-time-ago/gradation';
import en from 'javascript-time-ago/locale/en';
import visualProgressComponent from 'src/components/index/VisualProgress';
import { isStatusRemoved } from 'src/helpers/status';
import { formatDecimal, formatCcy } from 'src/helpers/currency';

TimeAgo.addLocale(en);

const statusConfig = {
  OPEN: {
    icon: 'schedule',
    header: '',
    priceColor: 'deep-orange-4',
  },
  SUSPENDED: {
    icon: 'pause_circle_outline',
    header: 'bg-deep-orange-4',
    priceColor: 'deep-orange-10',
  },
  CLOSED: {
    icon: 'close',
    header: 'bg-grey-5',
    progressColor: 'deep-orange-10',
  },
  INACTIVE: {
    icon: 'remove_circle_outline',
    header: 'bg-grey-5',
    priceColor: 'deep-orange-10',
  },
  INPLAY: {
    icon: 'speed',
    header: 'bg-yellow-2',
    priceColor: 'deep-orange-5',
  },
};

export default {
  name: 'Market',
  components: {
    visualProgress: visualProgressComponent,
  },
  props: {
    item: Object,
    now: Number,
  },
  data() {
    return {
      tradedVolumeChanged: false,
      tradedVolumeTimer: null,
      isOpen: false,
      columns: [
        {
          name: 'name',
          required: true,
          label: 'Horse name',
          align: 'left',
          field: r => r.name,
          sortable: false,
          headerStyle: 'width: 130px',
        },
        {
          name: 'place',
          required: true,
          label: 'Place',
          align: 'left',
          field: r => (r.status === 'WINNER' ? 'Winner' : r.place),
          sortable: false,
          headerStyle: 'width: 70px',
        },
        {
          name: 'volume',
          required: true,
          label: 'Matched amount',
          align: 'left',
          field: r => r.volume,
          sortable: false,
          headerStyle: 'width: 30%',
        },
        {
          name: 'price',
          required: true,
          label: 'Price',
          align: 'left',
          field: r => r.price,
          sortable: false,
        },
      ],
      pagination: {
        rowsPerPage: 0,
      },
    };
  },
  computed: {
    title() {
      return `${this.item.name} (${this.item.event.name})`;
    },
    adjustedStatus() {
      const isInPlay = this.item.inPlay && this.item.status === 'OPEN';
      return isInPlay ? 'INPLAY' : this.item.status;
    },
    isMarketClosed() {
      return this.adjustedStatus === 'CLOSED';
    },
    statusConfigDetails() {
      return statusConfig[this.adjustedStatus];
    },
    headerClass() {
      return this.statusConfigDetails.header;
    },
    priceColor() {
      return this.statusConfigDetails.priceColor;
    },
    icon() {
      return this.statusConfigDetails.icon;
    },
    tradedVolume() {
      return this.item.tradedVolume;
    },
    formattedTradedVolume() {
      return formatCcy(this.tradedVolume);
    },
    ago() {
      return new TimeAgo();
    },
    agoStyle() {
      return {
        gradation: canonical,
        now: this.now,
      };
    },
    marketTimeAgo() {
      return this.ago.format(this.item.marketTime, this.agoStyle);
    },
    chartEnabled() {
      return date.subtractFromDate(this.item.marketTime, { minutes: 20 }) < this.now;
    },
  },
  watch: {
    tradedVolume() {
      const TradedVolumeAnimationDuration = 3000;

      this.tradedVolumeChanged = true;

      if (this.tradedVolumeTimer) clearTimeout(this.tradedVolumeTimer);
      this.tradedVolumeTimer = setTimeout(() => {
        this.tradedVolumeChanged = false;
      }, TradedVolumeAnimationDuration);
    },
    adjustedStatus(status) {
      if (status === 'INPLAY') this.isOpen = true;
      else if (status === 'CLOSED') this.isOpen = false;
    },
  },
  mounted() {
    this.isOpen = this.adjustedStatus === 'INPLAY';
  },
  methods: {
    removeMarket() {
      this.$store.commit('livedata/removeMarket', this.item.id);
    },
    adjustInterval(price, fromMin, fromMax, toMin, toMax) {
      return toMax - (toMax - toMin) / (fromMax - fromMin) * (price - fromMin);
    },
    priceProgress(price) {
      if (price <= 1) return 0;

      const middleCutoff = 5.0;
      if (price < middleCutoff) {
        return this.adjustInterval(price, 1.0, middleCutoff, 0.5, 1.0);
      }

      const secondCutoff = 20.00;
      if (price < secondCutoff) {
        return this.adjustInterval(price, middleCutoff, secondCutoff, 0.1, 0.5);
      }

      return this.adjustInterval(price, secondCutoff, 1000.0, 0.0, 0.1);
    },
    isStatusRemoved(status) {
      return isStatusRemoved(status);
    },
    formatCcy(amount) {
      return formatCcy(amount);
    },
    formatDecimal(decimal) {
      return formatDecimal(decimal);
    },
    selectionNameClass(status) {
      return {
        'text-bold': status === 'WINNER',
        'text-strike': isStatusRemoved(status),
      };
    },
    selectionPlaceClass(place) {
      if (place) {
        switch (place.substr(0, 1)) {
          case '1': return 'text-weight-bold';
          case '2': return null;
          case '3': return 'text-weight-light';
          default: return 'text-weight-bold';
        }
      } else {
        return null;
      }
    },
    openCharts() {
      const from = date.subtractFromDate(this.item.marketTime, { minutes: 20 });
      const url = process.env.LIVESTREAM_CHARTS_URL
        .replace('$MARKET_ID', this.item.id)
        .replace('$FROM', date.formatDate(from, 'x'))
        .replace('$TO', 'now');
      openURL(url);
    },
  },
};
</script>
