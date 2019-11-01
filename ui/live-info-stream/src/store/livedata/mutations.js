import { isStatusRemoved } from 'src/helpers/status';

const places = ['1st', '2nd', '3rd'];

function updateRunnersWithPlaces(runners, marketStatus) {
  function fixedPrice(runner) { return isStatusRemoved(runner.status) ? 1000 : runner.price; }

  if (marketStatus === 'OPEN') {
    const sorted = runners.concat();
    sorted.sort((a, b) => fixedPrice(a) - fixedPrice(b));

    runners.forEach((r) => {
      const index = sorted.indexOf(r);
      r.place = (index >= 0 && index < places.length) ? places[index] : '';
    });
  } else {
    runners.forEach((r) => {
      r.place = '';
    });
  }
}

function updateMarketRunnerPlaces(market) {
  updateRunnersWithPlaces(market.runners, market.status);
}

function updateMarket(state, marketId, fn) {
  if (marketId in state.markets) {
    fn(state.markets[marketId]);
  }
}

export function setMarkets(state, marketList) {
  const adjustedList = marketList.map((market) => {
    market.marketTime = new Date(market.marketTime);
    updateMarketRunnerPlaces(market);
    return market;
  });

  state.markets = Object.fromEntries(adjustedList.map(m => [m.id, m]));
  state.orderedMarkets = adjustedList.sort((a, b) => {
    const left = a.marketTime;
    const right = b.marketTime;
    return (left > right) - (left < right);
  });
}

export function setConnected(state, value) {
  state.isConnected = value;
}

export function updateTimeDiff(state, instant) {
  state.serverTimeDiff = new Date(instant) - new Date();
}

export function updateTime(state) {
  state.syncedNow = Date.now() + state.serverTimeDiff;
}

export function setSyncTimer(state, timer) {
  if (state.syncTimer) clearInterval(state.syncTimer);
  state.syncTimer = timer;
}

export function setTradedVolume(state, { marketId, tradedVolume }) {
  updateMarket(state, marketId, (market) => {
    market.tradedVolume = tradedVolume;
  });
}

export function setInPlay(state, { marketId, inPlay }) {
  updateMarket(state, marketId, (market) => {
    market.inPlay = inPlay;
  });
}

export function setMarketStatus(state, { marketId, status }) {
  updateMarket(state, marketId, (market) => {
    market.status = status;
  });
}

export function setRunnerPrice(state, {
  marketId,
  runnerId,
  hc,
  price,
}) {
  updateMarket(state, marketId, (market) => {
    const runner = market.runners.find(r => r.id === runnerId && r.hc === hc);
    if (runner) {
      runner.price = price;
      updateMarketRunnerPlaces(market);
    }
  });
}

export function setRunnerStatus(state, {
  marketId,
  runnerId,
  hc,
  status,
}) {
  updateMarket(state, marketId, (market) => {
    const runner = market.runners.find(r => r.id === runnerId && r.hc === hc);
    if (runner) {
      runner.status = status;
      updateMarketRunnerPlaces(market);
    }
  });
}

export function setRunnerVolume(state, {
  marketId,
  runnerId,
  hc,
  volume,
}) {
  updateMarket(state, marketId, (market) => {
    const runner = market.runners.find(r => r.id === runnerId && r.hc === hc);
    if (runner) {
      runner.volume = volume;
    }
  });
}

export function setOnlineUserCount(state, { userCount }) {
  state.onlineUserCount = userCount;
}

export function removeMarket(state, marketId) {
  const index = state.orderedMarkets.findIndex(m => m.id === marketId);
  if (index >= 0) state.orderedMarkets.splice(index, 1);

  delete state.markets[marketId];
}
