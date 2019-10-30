package com.betfair.esa.client.cache.market;

public interface BatchMarketsChangeListener extends java.util.EventListener {
    void batchMarketsChange(BatchMarketChangeEvent batchMarketChangeEvent);
}
