<template>
  <q-layout view="hHh lpR fFf">
    <q-header elevated>
      <q-toolbar>
        <img src="../statics/horse.png" style="height: 40px;" />
        <q-toolbar-title>Horse racing stream</q-toolbar-title>
        <q-chip square>
          <transition mode="out-in" enter-active-class="animated rubberBand">
            <q-avatar size="1.6rem"
                      :key="onlineUserCount"
                      color="secondary"
                      text-color="white">{{ onlineUserCount }}</q-avatar>
          </transition>
          Spectators online
        </q-chip>
        <q-space />
        <q-btn
          flat
          dense
          round
          @click="leftDrawerOpen = !leftDrawerOpen"
          icon="menu"
          aria-label="Menu"
        />
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      side="right"
      bordered
      content-class="bg-grey-2"
    >
      <q-list>
        <q-item-label header>Essential Links</q-item-label>
        <q-item clickable tag="a" target="_blank" href="https://github.com/martinserts/live-info-stream">
          <q-item-section avatar>
            <q-icon name="code" />
          </q-item-section>
          <q-item-section>
            <q-item-label>Github</q-item-label>
            <q-item-label caption>github.com/martinserts/live-info-stream</q-item-label>
          </q-item-section>
        </q-item>
        <q-item clickable tag="a" target="_blank" href="mailto:martins.erts@gmail.com">
          <q-item-section avatar>
            <q-icon name="email" />
          </q-item-section>
          <q-item-section>
            <q-item-label>Contact</q-item-label>
            <q-item-label caption>martins.erts@gmail.com</q-item-label>
          </q-item-section>
        </q-item>
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script>
import 'animate.css/animate.min.css';
import { mapState } from 'vuex';

export default {
  name: 'RootLayout',

  data() {
    return {
      leftDrawerOpen: false,
    };
  },
  computed: {
    ...mapState('livedata', {
      onlineUserCount: state => state.onlineUserCount,
    }),
  },
};
</script>
