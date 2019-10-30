package com.betfair.esa.client.cache.market;

public interface MarketChangeListener extends java.util.EventListener {
    void marketChange(MarketChangeEvent marketChangeEvent);
}
