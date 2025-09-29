package io.rubuy74.mos.domain.market;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rubuy74.mos.domain.http.MarketRequest;
import io.rubuy74.mos.domain.Selection;
import io.rubuy74.mos.domain.event.EventDTO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MarketOperation {
    @JsonProperty
    public MarketRequest marketRequest;
    @JsonProperty
    public OperationType operationType;

    @SuppressWarnings("unchecked")
    public static MarketOperation fromJson(LinkedHashMap<String, Object> rawPayload) {
        MarketOperation marketOperation = new MarketOperation();
        MarketRequest marketRequest = new MarketRequest();

        Map<String, Object> marketRequestMap = (Map<String, Object>) rawPayload.get("marketRequest");
        Map<String, Object> eventMap = (Map<String, Object>) marketRequestMap.get("event");

        // add event to marketRequest
        marketRequest.eventDTO = EventDTO.fromJson(eventMap);
        marketRequest.marketId = (String)marketRequestMap.get("marketId");
        marketRequest.marketName = (String)marketRequestMap.get("marketName");

        if(marketRequestMap.containsKey("selections")){
            List<Map<String, Object>> selectionsMap = (List<Map<String, Object>>) marketRequestMap.get("selections");

            // add selections to marketRequest
            marketRequest.selections = selectionsMap.stream().map(Selection::fromJson).toList();
        }

        marketOperation.marketRequest = marketRequest;
        marketOperation.operationType = OperationType.valueOf((String) rawPayload.get("operationType"));
        return marketOperation;
    }

    public MarketOperation() {}

    public MarketOperation(
            @JsonProperty("marketRequest") MarketRequest marketRequest,
            @JsonProperty("operationType") OperationType operationType) {
        this.marketRequest = marketRequest;
        this.operationType = operationType;
    }

}
