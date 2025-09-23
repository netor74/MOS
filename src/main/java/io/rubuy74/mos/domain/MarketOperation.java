package io.rubuy74.mos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MarketOperation {
    @JsonProperty
    public MarketRequest marketRequest;
    @JsonProperty
    public OperationType operationType;

    public MarketOperation(MarketRequest marketRequest, OperationType operationType) {
        this.marketRequest = marketRequest;
        this.operationType = operationType;
    }

}
