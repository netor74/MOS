package io.rubuy74.mos.converter;

import io.rubuy74.mos.domain.MarketOperation;
import io.rubuy74.mos.domain.database.MarketRequest;
import io.rubuy74.mos.domain.database.OperationType;
import io.rubuy74.mos.utils.ValidatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MarketOperationJSONConverter {
    private static final Logger logger = LoggerFactory.getLogger(MarketOperationJSONConverter.class);

    @SuppressWarnings("unchecked")
    public static MarketOperation fromJson(LinkedHashMap<String, Object> rawPayload) {
        MarketOperation marketOperation = new MarketOperation();
        MarketRequest marketRequest = new MarketRequest();

        List<String> attributeList = List.of("marketRequest", "event", "selection");
        List<String> errorMessages = ValidatorUtils.checkAttributeList(rawPayload,attributeList);
        if(!errorMessages.isEmpty()) {
            errorMessages.forEach((errorMessage) -> {
                logger.error("operation=deserialize_market_operation, msg={}", errorMessage);
            });
            return null;
        }

        Map<String, Object> marketRequestMap = (Map<String, Object>) rawPayload.get("marketRequest");
        Map<String, Object> eventMap = (Map<String, Object>) marketRequestMap.get("event");

        // add event to marketRequest
        marketRequest.eventDTO = EventDTOJSONConverter.fromJson(eventMap);
        marketRequest.marketId = (String)marketRequestMap.get("marketId");
        marketRequest.marketName = (String)marketRequestMap.get("marketName");

        List<Map<String, Object>> selectionsMap = (List<Map<String, Object>>) marketRequestMap.get("selections");

        // add selections to marketRequest
        marketRequest.selections = selectionsMap.stream().map(SelectionJSONConverter::fromJson).toList();
        marketOperation.setMarketRequest(marketRequest);
        marketOperation.setOperationType(OperationType.valueOf((String) marketRequestMap.get("operationType")));
        return marketOperation;
    }
}
