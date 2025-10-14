package io.rubuy74.mos.adapter.out.database;

import io.rubuy74.mos.domain.Event;
import io.rubuy74.mos.domain.Market;
import io.rubuy74.mos.domain.MarketOperation;
import io.rubuy74.mos.dto.EventDTO;
import io.rubuy74.mos.port.out.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static io.rubuy74.mos.utils.MarketCreator.createMarket;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(String id) {
        return eventRepository.findById(id);
    }

    public void createEvent(Event event) {
        eventRepository.save(event);
    }

    public void updateEvent(Event event) {
        eventRepository.save(event);
    }

    public void addEvent(SelectionService selectionService, MarketOperation marketOperation, EventDTO eventDTO) {
        Event event = new Event(eventDTO.getId(), eventDTO.getName(),eventDTO.getEpochMilliseconds());
        Market newMarket = createMarket(selectionService,marketOperation);
        event.getMarkets().add(newMarket);
        createEvent(event);
    }

    public void addMarket(SelectionService selectionService,MarketOperation marketOperation, Event event) {
        Market newMarket = createMarket(selectionService,marketOperation);
        event.getMarkets().add(newMarket);
        updateEvent(event);
    }

    public void updateMarket(Event event, Market existingMarket, Market newMarket) {
        existingMarket.setName(newMarket.getName());
        existingMarket.selections = newMarket.selections;
        updateEvent(event);
    }

    public void deleteMarket(Event event, Market existingMarket) {
        event.getMarkets().remove(existingMarket);
        updateEvent(event);
    }
}
