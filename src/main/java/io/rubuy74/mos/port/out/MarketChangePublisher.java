package io.rubuy74.mos.port.out;

import io.rubuy74.mos.domain.MarketOperationResult;

public interface MarketChangePublisher {
    void publish(MarketOperationResult marketOperationResult);
}
