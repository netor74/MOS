package io.rubuy74.mos.application;

import io.rubuy74.mos.domain.MarketOperation;
import io.rubuy74.mos.port.in.MarketChangeHandler;
import io.rubuy74.mos.port.out.MarketChangeLogger;
import io.rubuy74.mos.port.out.MarketChangePublisher;

public class MarketChangeProcessor implements MarketChangeHandler {

    private MarketChangeLogger marketChangeLogger;
    private MarketChangePublisher marketChangePublisher;
    public MarketChangeProcessor(MarketChangeLogger marketChangeLogger, MarketChangePublisher marketChangePublisher) {
        this.marketChangeLogger = marketChangeLogger;
        this.marketChangePublisher = marketChangePublisher;
    }

    @Override
    public void handle(MarketOperation marketOperation) {
        // TODO: perform business model based on operation && log it && send its metadata to a kafka topic
    }
}