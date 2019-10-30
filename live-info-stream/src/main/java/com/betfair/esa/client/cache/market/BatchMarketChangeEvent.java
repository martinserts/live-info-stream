package com.betfair.esa.client.cache.market;

import java.util.EventObject;
import java.util.List;

public class BatchMarketChangeEvent extends EventObject {
    private List<MarketChangeEvent> changes;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public BatchMarketChangeEvent(Object source) {
        super(source);
    }

    public List<MarketChangeEvent> getChanges() {
        return changes;
    }

    void setChanges(List<MarketChangeEvent> changes) {
        this.changes = changes;
    }
}
