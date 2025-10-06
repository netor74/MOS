package io.rubuy74.mos.adapter.out.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rubuy74.mos.domain.database.MarketOperationResult;
import io.rubuy74.mos.port.out.MarketChangePublisher;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class KafkaMarketMetadataProducer implements MarketChangePublisher {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMarketMetadataProducer.class);
    private static final String topic = "market-change-status";
    private static final ObjectMapper mapper = new ObjectMapper() ;

    private Properties getKafkaProperties() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("acks", "1");
        properties.put("retries", 3);
        properties.put("key.serializer", StringSerializer.class.getName() );
        properties.put("value.serializer", ByteArraySerializer.class.getName()  );
        return properties;
    }

    @Override
    public void publish(MarketOperationResult marketOperationResult) {
        byte[] payload;
        try {
            payload = mapper.writeValueAsBytes(marketOperationResult);
        } catch (Exception e) {
            logger.error("Failed to serialize MarketOperation to JSON: {}", e.getMessage(), e);
            return;
        }

        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(getKafkaProperties());
        ProducerRecord<String, byte[]> producerRecord = new ProducerRecord<>(
                topic,
                marketOperationResult.resultType().toString(),
                payload
        );

        producer.send(producerRecord, (metadata, exception) -> {
            if (exception != null) {
                logger.error("Failed to send MarketOperation to Kafka", exception);
            } else  {
                logger.info(
                        "Send record to market-changes-status: {} \n Status({}): {}",
                        metadata,
                        marketOperationResult.resultType(),
                        marketOperationResult.message()
                );
            }

        });
    }
}
