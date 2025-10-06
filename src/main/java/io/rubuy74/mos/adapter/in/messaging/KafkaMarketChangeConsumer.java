package io.rubuy74.mos.adapter.in.messaging;

import io.rubuy74.mos.converter.MarketOperationJSONConverter;
import io.rubuy74.mos.port.in.MarketChangeHandler;
import io.rubuy74.mos.domain.MarketOperation;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;


@Component
public class KafkaMarketChangeConsumer {

    private final MarketChangeHandler marketChangeHandler;
    public KafkaMarketChangeConsumer(MarketChangeHandler marketChangeHandler) {
        this.marketChangeHandler = marketChangeHandler;
    }

    private static final String topic = "market-changes";
    private static final String groupId = "market-changes-group";

    @KafkaListener(topics = topic,groupId = groupId)
    private void handleMessage(LinkedHashMap<String, Object> rawPayload) {
        MarketOperation marketOperation = MarketOperationJSONConverter.fromJson(rawPayload);
        marketChangeHandler.handle(marketOperation);
    }
}
