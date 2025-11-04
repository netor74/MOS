package io.rubuy74.mos.adapter.out.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rubuy74.mos.domain.internal.MarketOperationResult;
import io.rubuy74.mos.port.out.MarketChangePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaMarketMetadataProducer implements MarketChangePublisher {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMarketMetadataProducer.class);
    private static final String TOPIC = "market-change-status";
    private final ObjectMapper mapper;
    private final KafkaTemplate<String,byte[]> kafkaTemplate;

    public KafkaMarketMetadataProducer(ObjectMapper mapper, KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.mapper = mapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(MarketOperationResult marketOperation) {
        byte[] payload;
        try {
            payload = mapper.writeValueAsBytes(marketOperation);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to serialize MarketOperationResult to JSON: " + e.getMessage(), e);
        }

        try {
            kafkaTemplate.send(TOPIC, payload)
                .whenComplete((result, e) -> {
                    if (e != null) {
                        logger.error("operation=send_market_operation_result," +
                                "msg=Failed to send MarketOperationResult message, " +
                                "error:{}", e.getMessage(), e);
                    } else  {
                        logger.info("operation=send_market_operation_result," +
                                "msg=Sent MarketOperationResult to market-changes-status: " +
                                "payload={}", marketOperation.toString());
                    }
                });
        } catch (Exception e) {
            logger.error("operation=send_market_operation_result, " +
                    "msg:Caught Exception while sending MarketOperationResult message, " +
                    "error: {}", e.getMessage(), e);
        }
    }
}
