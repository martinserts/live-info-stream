import Vue from 'vue';

function onUnknownCommand() {
}

function onServerTime(context, { instant }) {
  const UpdateInterval = 60 * 1000;

  context.commit('updateTimeDiff', instant);
  context.commit('updateTime');
  context.commit('setSyncTimer',
    setInterval(() => { context.commit('updateTime'); }, UpdateInterval));
}

function onTradedVolumeChanged(context, data) {
  context.commit('setTradedVolume', data);
}

function onInPlayChanged(context, data) {
  context.commit('setInPlay', data);
}

function onMarketStatusChanged(context, data) {
  context.commit('setMarketStatus', data);
}

function onRunnerPriceChanged(context, data) {
  context.commit('setRunnerPrice', data);
}

function onRunnerStatusChanged(context, data) {
  context.commit('setRunnerStatus', data);
}

function mapCommand(name) {
  switch (name) {
    case 'serverTime': return onServerTime;
    case 'tradedVolumeChanged': return onTradedVolumeChanged;
    case 'inPlayChanged': return onInPlayChanged;
    case 'marketStatusChanged': return onMarketStatusChanged;
    case 'runnerPriceChanged': return onRunnerPriceChanged;
    case 'runnerStatusChanged': return onRunnerStatusChanged;
    default: return onUnknownCommand;
  }
}

export function SOCKET_ONOPEN({ commit }, ev) {
  Vue.prototype.$socket = ev.currentTarget;
  commit('setConnected', true);
}

export function SOCKET_ONCLOSE({ commit }) {
  commit('setConnected', false);
}

export function SOCKET_ONERROR() {
}

export function SOCKET_ONMESSAGE(context, { data }) {
  const msg = JSON.parse(data);
  mapCommand(msg.command)(context, msg);
}

export function SOCKET_RECONNECT() {
}

export function SOCKET_RECONNECT_ERROR() {
}
