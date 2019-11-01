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
      <q-card :class="headerClass">
        <q-card-section>
          <div class="text-h6">{{ item.name }}</div>
          <div class="text-subtitle2">{{ item.event.name }}</div>
        </q-card-section>
        <q-card-section>
          <q-list dense :class="headerClass">
            <runner v-for="(r, index) in item.runners"
                    :key="r.id"
                    :item="r"
                    :avatarColor="avatarColor"
                    :avatarBackground="avatarBackground"
                    :progressColor="progressColor"
                    volumeBackground="grey-7"
                    volumeColor="black"
                    :tradedVolume="tradedVolume"
                    :index="index + 1" />
          </q-list>
        </q-card-section>
        <q-card-actions v-if="isMarketClosed"
                        align="right">
          <q-btn flat @click="removeMarket">Remove market</q-btn>
        </q-card-actions>
      </q-card>
    </q-expansion-item>
    <q-separator />
  </div>
</template>

<script>
import TimeAgo from 'javascript-time-ago';
import { canonical } from 'javascript-time-ago/gradation';
import en from 'javascript-time-ago/locale/en';
import runnerComponent from 'src/components/index/Runner';

TimeAgo.addLocale(en);

const statusConfig = {
  OPEN: {
    icon: 'schedule',
    header: '',
    avatarColor: 'white',
    avatarBackground: 'brown-5',
    progressColor: 'deep-orange-4',
  },
  SUSPENDED: {
    icon: 'pause_circle_outline',
    header: 'bg-deep-orange-4',
    avatarColor: 'white',
    avatarBackground: 'brown-6',
    progressColor: 'deep-orange-10',
  },
  CLOSED: {
    icon: 'close',
    header: 'bg-grey-5',
    avatarColor: 'white',
    avatarBackground: 'brown-6',
    progressColor: 'deep-orange-10',
  },
  INACTIVE: {
    icon: 'remove_circle_outline',
    header: 'bg-grey-5',
    avatarColor: 'white',
    avatarBackground: 'brown-6',
    progressColor: 'deep-orange-10',
  },
  INPLAY: {
    icon: 'speed',
    header: 'bg-yellow-2',
    avatarColor: 'white',
    avatarBackground: 'brown-6',
    progressColor: 'deep-orange-5',
  },
};

export default {
  name: 'Market',
  components: {
    runner: runnerComponent,
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
    };
  },
  computed: {
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
    avatarColor() {
      return this.statusConfigDetails.avatarColor;
    },
    avatarBackground() {
      return this.statusConfigDetails.avatarBackground;
    },
    progressColor() {
      return this.statusConfigDetails.progressColor;
    },
    icon() {
      return this.statusConfigDetails.icon;
    },
    tradedVolume() {
      return this.item.tradedVolume;
    },
    formattedTradedVolume() {
      return `$ ${this.tradedVolume.toLocaleString('en-GB',
        {
          minimumFractionDigits: 2,
        })}`;
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
  methods: {
    removeMarket() {
      this.$store.commit('livedata/removeMarket', this.item.id);
    },
  },
};
</script>
