package io.rubuy74.mos.config;

import io.rubuy74.mos.adapter.out.database.EventService;
import io.rubuy74.mos.adapter.out.database.SelectionService;
import io.rubuy74.mos.application.MarketChangeProcessor;
import io.rubuy74.mos.application.MarketListProcessor;
import io.rubuy74.mos.port.in.MarketChangeHandler;
import io.rubuy74.mos.port.in.MarketListHandler;
import io.rubuy74.mos.port.out.MarketChangePublisher;
import org.springframework.context.annotation.Bean;

public class BeanConfig {
    @Bean
    MarketChangeHandler marketChangeHandler(MarketChangePublisher marketChangePublisher, EventService eventService,  SelectionService selectionService) {
        return new MarketChangeProcessor(marketChangePublisher,eventService,selectionService);
    }

    @Bean
    MarketListHandler marketListHandler(EventService eventService) {
        return new MarketListProcessor(eventService);
    }

}
