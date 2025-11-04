package io.rubuy74.mos.port.in;

import io.rubuy74.mos.domain.MarketOperation;

public interface MarketChangeHandler {
    void handle(MarketOperation marketOperation);
}
