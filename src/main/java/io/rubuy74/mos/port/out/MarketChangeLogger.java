package io.rubuy74.mos.port.out;

import io.rubuy74.mos.domain.MarketOperation;

public interface MarketChangeLogger {
    void log(String message, MarketOperation marketOperation);
}
