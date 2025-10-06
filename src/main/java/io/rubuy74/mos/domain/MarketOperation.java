package io.rubuy74.mos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.rubuy74.mos.domain.database.MarketRequest;
import io.rubuy74.mos.domain.database.OperationType;
import io.rubuy74.mos.dto.EventDTO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
                .add("marketRequest", marketRequest)
                .add("operationType", operationType).toString();
    }
}
