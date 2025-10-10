package io.rubuy74.mos.domain.database;

import io.rubuy74.mos.domain.MarketOperation;

public record MarketOperationResult(ResultType resultType, String message, MarketOperation marketOperation) { }
