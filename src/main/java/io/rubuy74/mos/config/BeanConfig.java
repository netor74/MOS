package io.rubuy74.mos.config;

import io.rubuy74.mos.application.MarketChangeProcessor;
import io.rubuy74.mos.port.in.MarketChangeHandler;
import io.rubuy74.mos.port.out.MarketChangeLogger;
import io.rubuy74.mos.port.out.MarketChangePublisher;
import org.springframework.context.annotation.Bean;

public class BeanConfig {
    @Bean
    MarketChangeHandler marketChangeHandler(MarketChangeLogger marketChangeLogger, MarketChangePublisher marketChangePublisher) {
        return new MarketChangeProcessor(marketChangeLogger,marketChangePublisher);
    }

}
