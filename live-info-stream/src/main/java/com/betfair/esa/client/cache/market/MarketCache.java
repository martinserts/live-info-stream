package com.betfair.esa.client.cache.market;

import com.betfair.esa.client.protocol.ChangeMessage;
import com.betfair.esa.swagger.model.MarketChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Thread safe cache of markets
 */
public class MarketCache {
    private static final Logger LOG = LoggerFactory.getLogger(MarketCache.class);

    private Map<String, Market> markets = new ConcurrentHashMap<>();
    //whether markets are automatically removed on close (default is True)
    private boolean isMarketRemovedOnClose;
    //conflation indicates slow consumption
    private int conflatedCount;

    private CopyOnWriteArrayList<MarketChangeListener> marketChangeListeners = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<BatchMarketsChangeListener> batchMarketChangeListeners = new CopyOnWriteArrayList<>();

    public MarketCache() {
        this.isMarketRemovedOnClose = true;
    }

    public void onMarketChange(ChangeMessage<MarketChange> changeMessage) {
        if (changeMessage.isStartOfNewSubscription()){
            //clear cache
            markets.clear();
        }
        if (changeMessage.getItems() != null){
            //lazy build events
            List<MarketChangeEvent> batch = (batchMarketChangeListeners.size() == 0) ? null : new ArrayList<>(changeMessage.getItems().size());

            for (MarketChange marketChange : changeMessage.getItems()) {
                Market market = onMarketChange(marketChange);

                if (isMarketRemovedOnClose && market.isClosed()) {
                    //remove on close
                    markets.remove(market.getMarketId());
                }

                //lazy build events
                if (batch != null || marketChangeListeners.size() != 0) {
                    MarketChangeEvent marketChangeEvent = new MarketChangeEvent(this);
                    marketChangeEvent.setChange(marketChange);
                    marketChangeEvent.setMarket(market);
                    if (marketChangeListeners != null){
                        dispatchMarketChanged(marketChangeEvent);
                    }
                    if (batch != null){
                        batch.add(marketChangeEvent);
                    }
                }
            }
            if (batch != null){
                dispatchBatchMarketChanged(new BatchMarketChangeEvent(batch));
            }
        }
    }

    private Market onMarketChange(MarketChange marketChange) {
        if (Boolean.TRUE.equals(marketChange.isCon())) {
            conflatedCount++;
        }
        Market market = markets.computeIfAbsent(marketChange.getId(), k -> new Market(this, k));
        market.onMarketChange(marketChange);
        return market;
    }

    public int getConflatedCount() {
        return conflatedCount;
    }

    void setConflatedCount(int conflatedCount) {
        this.conflatedCount = conflatedCount;
    }

    public boolean isMarketRemovedOnClose() {
        return isMarketRemovedOnClose;
    }

    public void setMarketRemovedOnClose(boolean marketRemovedOnClose) {
        isMarketRemovedOnClose = marketRemovedOnClose;
    }

    public Market getMarket(String marketId){
        //queries by market id - the result is invariant for the lifetime of the market.
        return markets.get(marketId);
    }

    public Iterable<Market> getMarkets(){
        //all the cached markets
        return markets.values();
    }

    public int getCount(){
        //market count
        return markets.size();
    }

    // Event for each market change

    private void dispatchMarketChanged(MarketChangeEvent marketChangeEvent){
        try {
            marketChangeListeners.forEach(l->l.marketChange(marketChangeEvent));
        } catch (Exception e) {
            LOG.error("Exception from event listener", e);
        }
    }

    public void addMarketChangeListener(MarketChangeListener marketChangeListener){
        marketChangeListeners.add(marketChangeListener);
    }

    public void removeMarketChangeListener(MarketChangeListener marketChangeListener){
        marketChangeListeners.remove(marketChangeListener);
    }

    // Event for each batch of market changes
    // (note to be truly atomic you will want to set to merge segments otherwise an event could be segmented)

    private void dispatchBatchMarketChanged(BatchMarketChangeEvent batchMarketChangeEvent){
        try {
            batchMarketChangeListeners.forEach(l -> l.batchMarketsChange(batchMarketChangeEvent));
        } catch (Exception e) {
            LOG.error("Exception from batch event listener", e);
        }
    }

    public void addBatchMarketChangeListener(BatchMarketsChangeListener batchMarketChangeListener){
        batchMarketChangeListeners.add(batchMarketChangeListener);
    }

    public void removeBatchMarketChangeListener(BatchMarketsChangeListener batchMarketChangeListener){
        batchMarketChangeListeners.remove(batchMarketChangeListener);
    }
}


