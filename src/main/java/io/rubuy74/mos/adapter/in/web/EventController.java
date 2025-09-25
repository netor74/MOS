package io.rubuy74.mos.adapter.in.web;

import io.rubuy74.mos.application.MarketListProcessor;
import io.rubuy74.mos.domain.Event;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final MarketListProcessor marketListProcessor;

    public EventController(MarketListProcessor marketListProcessor) {
        this.marketListProcessor = marketListProcessor;
    }

    @GetMapping
    public List<Event> getEvents() {
        return this.marketListProcessor.list();
    }
}
