package io.rubuy74.mos.adapter.out.database;

import io.rubuy74.mos.domain.event.Event;
import io.rubuy74.mos.port.out.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEvent(String id) {
        return eventRepository.findById(id);
    }

    public void createEvent(Event event) {
        eventRepository.save(event);
    }

    public void updateEvent(Event event) {
        eventRepository.save(event);
    }
}
