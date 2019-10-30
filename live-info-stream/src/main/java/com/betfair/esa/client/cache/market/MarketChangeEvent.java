package com.betfair.esa.client.cache.market;

import com.betfair.esa.swagger.model.MarketChange;

import java.util.EventObject;

public class MarketChangeEvent extends EventObject {
    //the raw change message that was just applied
    private MarketChange change;
    //the market changed - this is reference invariant
    private Market market;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MarketChangeEvent(Object source) {
        super(source);
    }

    public MarketChange getChange() {
        return change;
    }

    void setChange(MarketChange change) {
        this.change = change;
    }

    public Market getMarket() {
        return market;
    }

    void setMarket(Market market) {
        this.market = market;
    }

    public MarketSnap getSnap() {
        return market.getSnap();
    }
}
