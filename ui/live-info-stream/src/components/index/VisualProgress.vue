<template>
  <q-linear-progress size="25px"
                     :value="value"
                     :color="background">
    <div class="absolute-full flex flex-center">
      <q-badge :color="labelBackground" :text-color="badgeColor">
        <span :class="{'text-weight-bold': labelChanged}">{{ label }}</span>
      </q-badge>
    </div>
  </q-linear-progress>
</template>

<script>
export default {
  name: 'VisualProgres',
  props: {
    value: Number,
    label: String,
    background: String,
    labelBackground: String,
    labelColor: String,
    labelHighlighted: String,
    highlightChanges: Boolean,
  },
  data() {
    return {
      labelChanged: false,
      labelTimer: null,
    };
  },
  computed: {
    badgeColor() {
      return this.labelChanged ? this.labelHighlighted : this.labelColor;
    },
  },
  watch: {
    label() {
      if (this.highlightChanges) {
        const LabelAnimationDuration = 3000;

        this.labelChanged = true;

        if (this.labelTimer) clearTimeout(this.labelTimer);
        this.labelTimer = setTimeout(() => {
          this.labelChanged = false;
        }, LabelAnimationDuration);
      }
    },
  },
};
</script>
