export default {
  isConnected: false,
  serverTimeDiff: 0,
  syncTimer: null,
  syncedNow: Date.now(), // Updated via timer
  markets: {},
  orderedMarkets: [],
  onlineUserCount: 0,
};
