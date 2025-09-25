package io.rubuy74.mos.port.out;

import io.rubuy74.mos.domain.MarketOperation;

public interface MarketChangePublisher {
    void publish(String message, MarketOperation marketOperation);
}
