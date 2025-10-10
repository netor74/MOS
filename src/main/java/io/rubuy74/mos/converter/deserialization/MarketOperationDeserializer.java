package io.rubuy74.mos.converter.deserialization;

import io.rubuy74.mos.domain.MarketOperation;
import io.rubuy74.mos.domain.database.MarketRequest;
import io.rubuy74.mos.domain.database.OperationType;
import io.rubuy74.mos.utils.ValidatorUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MarketOperationDeserializer {
    private static final List<String> ATTRIBUTE_LIST = List.of("marketRequest", "operationType");
    private static final List<String> MARKET_REQUEST_ATTRIBUTE_LIST = List.of("event", "selections");

    @SuppressWarnings("unchecked")
    public static MarketOperation deserialize(LinkedHashMap<String, Object> rawPayload) {
        MarketOperation marketOperation = new MarketOperation();
        MarketRequest marketRequest = new MarketRequest();

        ValidatorUtils.checkArgument(
                rawPayload == null,
                "MarketOperation payload is null",
                "deserialize_market_operation");
        ValidatorUtils.checkAttributeList(
                rawPayload,
                ATTRIBUTE_LIST
        );
        Map<String, Object> marketRequestMap = (Map<String, Object>) rawPayload.get("marketRequest");
        ValidatorUtils.checkAttributeList(
                marketRequestMap,
                MARKET_REQUEST_ATTRIBUTE_LIST
        );


        Map<String, Object> eventMap = (Map<String, Object>) marketRequestMap.get("event");

        // add event to marketRequest
        marketRequest.eventDTO = EventDTODeserializer.deserialize(eventMap);
        marketRequest.marketId = (String)marketRequestMap.get("marketId");
        marketRequest.marketName = (String)marketRequestMap.get("marketName");

        List<Map<String, Object>> selectionsMap = (List<Map<String, Object>>) marketRequestMap.get("selections");

        // add selections to marketRequest
        marketRequest.selections = selectionsMap.stream().map(SelectionDeserializer::deserialize).toList();
        marketOperation.setMarketRequest(marketRequest);
        marketOperation.setOperationType(OperationType.valueOf((String) rawPayload.get("operationType")));
        return marketOperation;
    }
}
