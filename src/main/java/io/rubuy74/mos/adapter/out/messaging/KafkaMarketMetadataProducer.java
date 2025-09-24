package io.rubuy74.mos.adapter.out.messaging;

import io.rubuy74.mos.port.out.MarketChangePublisher;
import io.rubuy74.mos.domain.MarketOperation;
import org.springframework.stereotype.Component;

@Component
public class KafkaMarketMetadataProducer implements MarketChangePublisher {
    @Override
    public void publish(MarketOperation marketOperation) {

    }
    // TODO: create audit logs for sucess of operations (and maybe event listing??)
}
