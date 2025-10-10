package io.rubuy74.mos.application;

import io.rubuy74.mos.adapter.out.database.EventService;
import io.rubuy74.mos.domain.Event;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MarketListProcessorTest {

    @Test
    void list_returnsEventsFromService() {
        EventService eventService = mock(EventService.class);
        List<Event> events = List.of(
                new Event("e1", "Event 1", LocalDate.parse("2025-10-06")),
                new Event("e2", "Event 2", LocalDate.parse("2025-10-06"))
        );
        when(eventService.getEvents()).thenReturn(events);

        MarketListProcessor processor = new MarketListProcessor(eventService);
        List<Event> result = processor.list();

        assertEquals(events, result);
        verify(eventService).getEvents();
    }
}
