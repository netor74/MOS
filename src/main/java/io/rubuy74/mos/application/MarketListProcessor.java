package io.rubuy74.mos.application;

import io.rubuy74.mos.adapter.out.database.EventService;
import io.rubuy74.mos.domain.Event;
import io.rubuy74.mos.port.in.MarketListHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MarketListProcessor implements MarketListHandler {

    private final EventService eventService;

    public MarketListProcessor(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public List<Event> list() {
        return eventService.getEvents();
    }
}
