package io.rubuy74.mos.domain.http;

import io.rubuy74.mos.domain.market.MarketOperation;

public class MarketOperationResult {
    private final ResultType resultType;
    private final String message;
    private final MarketOperation marketOperation;

    public String getMessage() {
        return message;
    }
    public ResultType getResultType() {
        return resultType;
    }
    public MarketOperation getMarketOperation() {
        return marketOperation;
    }


    public MarketOperationResult(ResultType resultType, String message, MarketOperation marketOperation) {
        this.message = message;
        this.marketOperation = marketOperation;
        this.resultType = resultType;
    }
}
