package io.rubuy74.mos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.rubuy74.mos.domain.database.MarketRequest;
import io.rubuy74.mos.domain.database.OperationType;
import io.rubuy74.mos.utils.ValidatorUtils;

public class MarketOperation {
    @JsonProperty
    private MarketRequest marketRequest;
    @JsonProperty
    private OperationType operationType;

    public MarketRequest getMarketRequest() {
        return marketRequest;
    }
    public void setMarketRequest(MarketRequest marketRequest) {
        this.marketRequest = marketRequest;
    }
    public OperationType getOperationType() {
        return operationType;
    }
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public MarketOperation() {}
    public MarketOperation(MarketRequest marketRequest, OperationType operationType) {
        ValidatorUtils.checkArgument(marketRequest == null,"Market Request is null","create_market_operation");
        ValidatorUtils.checkArgument(operationType == null,"OperationType is null","create_market_operation");
        this.marketRequest = marketRequest;
        this.operationType = operationType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
                .add("marketRequest", marketRequest)
                .add("operationType", operationType).toString();
    }
}
