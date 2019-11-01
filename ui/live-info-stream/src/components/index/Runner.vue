<style>
  .priceHighlighted {
    color: red;
    font-weight: bold;
  }

</style>
<template>
  <q-item clickable>
    <q-item-section avatar :style="{minWidth: '100px'}">
      <q-icon v-if="isRemoved"
              :color="avatarBackground"
              size="md"
              name="delete_forever" />
      <q-icon v-else-if="isWinner"
              :color="avatarBackground"
              size="md"
              name="emoji_events" />
      <q-chip v-else-if="hasPlace" :key="runnerId" :class="placeChipClass">
        <q-avatar :color="avatarBackground"
                  :text-color="avatarColor"
                  size="sm">
          {{ index }}
        </q-avatar>
        {{ place }}
      </q-chip>
      <q-chip v-else :key="runnerId">
        <q-avatar :color="avatarBackground"
                  :text-color="avatarColor"
                  size="sm">
          {{ index }}
        </q-avatar>
      </q-chip>
    </q-item-section>
    <q-item-section>
      {{ item.name }}
      <q-linear-progress size="25px"
                         class="q-mb-xs"
                         :value="volumePercent"
                         :color="volumeBackground">
        <div class="absolute-full flex flex-center">
          <q-badge color="white" :text-color="volumeColor" :label="volumeLabel" />
        </div>
      </q-linear-progress>
      <q-linear-progress :value="progress"
                         :color="progressColor"
                         size="lg">
      </q-linear-progress>
    </q-item-section>
    <q-item-section side :style="{minWidth: '60px'}">
      <q-item-label :class="{priceHighlighted: priceChanged}">{{ formattedPrice }}</q-item-label>
    </q-item-section>
  </q-item>
</template>

<script>
import { isStatusRemoved } from 'src/helpers/status';

export default {
  name: 'Runner',
  props: {
    item: Object,
    index: Number,
    avatarColor: String,
    avatarBackground: String,
    progressColor: String,
    volumeBackground: String,
    volumeColor: String,
    tradedVolume: Number,
  },
  data() {
    return {
      priceChanged: false,
      priceTimer: null,
    };
  },
  computed: {
    runnerId() {
      return this.item.id;
    },
    volume() {
      return this.item.volume;
    },
    volumeLabel() {
      return `$ ${this.volume.toLocaleString('en-GB',
        {
          minimumFractionDigits: 2,
        })}`;
    },
    volumePercent() {
      return this.volume / this.tradedVolume;
    },
    place() {
      return this.item.place;
    },
    hasPlace() {
      return !!this.place;
    },
    placeChipClass() {
      const place = parseInt(this.place.substring(0, 1), 10);
      return `bg-brown-${5 - place}`;
    },
    status() {
      return this.item.status;
    },
    isWinner() {
      return this.status === 'WINNER';
    },
    isRemoved() {
      return isStatusRemoved(this.status);
    },
    price() {
      return this.item.price;
    },
    formattedPrice() {
      return this.price.toLocaleString('en-GB',
        {
          minimumFractionDigits: 2,
        });
    },
    progress() {
      if (this.price <= 1) return 0;

      const middleCutoff = 5.0;
      if (this.price < middleCutoff) {
        return this.adjustInterval(this.price, 1.0, middleCutoff, 0.5, 1.0);
      }

      const secondCutoff = 20.00;
      if (this.price < secondCutoff) {
        return this.adjustInterval(this.price, middleCutoff, secondCutoff, 0.1, 0.5);
      }

      return this.adjustInterval(this.price, secondCutoff, 1000.0, 0.0, 0.1);
    },
  },
  watch: {
    price() {
      const PriceAnimationDuration = 3000;

      this.priceChanged = true;

      if (this.priceTimer) clearTimeout(this.priceTimer);
      this.priceTimer = setTimeout(() => {
        this.priceChanged = false;
      }, PriceAnimationDuration);
    },
  },
  methods: {
    adjustInterval(price, fromMin, fromMax, toMin, toMax) {
      return toMax - (toMax - toMin) / (fromMax - fromMin) * (price - fromMin);
    },
  },
};
</script>
