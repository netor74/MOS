package io.rubuy74.mos.utils;

import io.rubuy74.mos.adapter.out.database.SelectionService;
import io.rubuy74.mos.domain.Market;
import io.rubuy74.mos.domain.MarketOperation;
import io.rubuy74.mos.domain.Selection;

import java.util.List;

public class MarketCreator {

    public static Market createMarket(SelectionService selectionService, MarketOperation marketOperation) {
        String marketId = marketOperation.getMarketRequest().getMarketId();
        String marketName = marketOperation.getMarketRequest().getMarketName();
        List<Selection> detachedSelections = marketOperation.getMarketRequest().getSelections();
        List<Selection> marketSelections = selectionService.getManagedSelections(detachedSelections);

        return new Market(marketId,marketName,marketSelections);
    }
}
