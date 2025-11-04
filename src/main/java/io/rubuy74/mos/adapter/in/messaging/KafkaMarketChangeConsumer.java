package io.rubuy74.mos.adapter.in.messaging;

import io.rubuy74.mos.converter.deserialization.MarketOperationDeserializer;
import io.rubuy74.mos.port.in.MarketChangeHandler;
import io.rubuy74.mos.port.out.MarketChangePublisher;
import io.rubuy74.mos.domain.MarketOperation;
import io.rubuy74.mos.domain.internal.MarketOperationResult;
import io.rubuy74.mos.domain.internal.ResultType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;


@Component
public class KafkaMarketChangeConsumer {

    private final MarketChangeHandler marketChangeHandler;
    private final MarketChangePublisher marketChangePublisher;
    public KafkaMarketChangeConsumer(MarketChangeHandler marketChangeHandler, MarketChangePublisher marketChangePublisher) {
        this.marketChangeHandler = marketChangeHandler;
        this.marketChangePublisher = marketChangePublisher;
    }

    private static final String topic = "market-changes";
    private static final String groupId = "market-changes-group";

    @KafkaListener(topics = topic,groupId = groupId)
    private void handleMessage(LinkedHashMap<String, Object> rawPayload) {
        try {
            MarketOperation marketOperation = MarketOperationDeserializer.deserialize(rawPayload);
            marketChangeHandler.handle(marketOperation);
        } catch (IllegalArgumentException e) {
            Object reqIdObj = rawPayload.get("requestId");
            String requestId = (reqIdObj instanceof String) ? (String) reqIdObj : "unknown";
            MarketOperationResult result = new MarketOperationResult(requestId, ResultType.FAILURE, "Deserialization failed: " + e.getMessage(), null);
            marketChangePublisher.publish(result);
            throw e;
        }
    }
}
