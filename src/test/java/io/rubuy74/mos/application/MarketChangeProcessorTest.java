package io.rubuy74.mos.application;

import io.rubuy74.mos.adapter.out.database.EventService;
import io.rubuy74.mos.adapter.out.database.SelectionService;
import io.rubuy74.mos.domain.Event;
import io.rubuy74.mos.domain.Market;
import io.rubuy74.mos.domain.MarketOperation;
import io.rubuy74.mos.domain.Selection;
import io.rubuy74.mos.domain.internal.MarketRequest;
import io.rubuy74.mos.domain.internal.OperationType;
import io.rubuy74.mos.dto.EventDTO;
import io.rubuy74.mos.port.out.EventRepository;
import io.rubuy74.mos.port.out.MarketChangePublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MarketChangeProcessorTest {

    private MarketChangePublisher publisher;
    private EventRepository eventRepository;
    private EventService eventService;
    private SelectionService selectionService;

    private MarketChangeProcessor processor;

    @BeforeEach
    void setUp() throws Exception {
        publisher = mock(MarketChangePublisher.class);
        eventRepository = mock(EventRepository.class);
        eventService = new EventService();
        Field field = EventService.class.getDeclaredField("eventRepository");
        field.setAccessible(true);
        field.set(eventService, eventRepository);
        selectionService = mock(SelectionService.class);
        processor = new MarketChangeProcessor(publisher, eventService, selectionService);
    }

    private MarketOperation buildOperation(OperationType type, String eventId, String marketId, String marketName, List<Selection> selections) {
        EventDTO eventDTO = new EventDTO(eventId, "Event Name", LocalDate.parse("2025-10-06").atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
        MarketRequest request = new MarketRequest(marketId, marketName, eventDTO, selections);
        return new MarketOperation("test-request-id", request, type);
    }

    @Test
    void add_createsEventWhenMissingAndPublishesSuccess() {
        when(eventRepository.findById("e1")).thenReturn(Optional.empty());
        Selection s = new Selection("s1", "Sel 1", 1.5);
        when(selectionService.getManagedSelections(List.of(s))).thenReturn(List.of(s));

        MarketOperation op = buildOperation(OperationType.ADD, "e1", "m1", "Market 1", List.of(s));
        processor.handle(op);

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventRepository).save(eventCaptor.capture());
        Event saved = eventCaptor.getValue();
        assertEquals("e1", saved.getId());
        assertEquals(1, saved.getMarkets().size());
        assertEquals("m1", saved.getMarkets().get(0).getId());

        verify(publisher, atLeastOnce()).publish(any());
    }

    @Test
    void add_addsMarketToExistingEventWhenNotPresent() {
        Event existing = new Event("e1", "Event Name", LocalDate.parse("2025-10-06").atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
        when(eventRepository.findById("e1")).thenReturn(Optional.of(existing));
        Selection s = new Selection("s1", "Sel 1", 1.5);
        when(selectionService.getManagedSelections(List.of(s))).thenReturn(List.of(s));

        MarketOperation op = buildOperation(OperationType.ADD, "e1", "m1", "Market 1", List.of(s));
        processor.handle(op);

        assertEquals(1, existing.getMarkets().size());
        assertEquals("m1", existing.getMarkets().get(0).getId());
        verify(eventRepository).save(existing);
        verify(publisher, atLeastOnce()).publish(any());
    }

    @Test
    void edit_updatesExistingMarket() {
        Selection s = new Selection("s1", "Sel 1", 1.5);
        Selection s2 = new Selection("s2", "Sel 2", 2.5);
        Market market = new Market("m1", "Old Name", List.of(s));
        Event existing = new Event(
                "e1",
                "Event Name",
                LocalDate.parse("2028-10-06")
                        .atStartOfDay(ZoneOffset.UTC)
                        .toInstant()
                        .toEpochMilli()
        );
        existing.getMarkets().add(market);
        when(eventRepository.findById("e1")).thenReturn(Optional.of(existing));
        when(selectionService.getManagedSelections(List.of(s2))).thenReturn(List.of(s2));

        MarketOperation op = buildOperation(OperationType.EDIT, "e1", "m1", "New Name", List.of(s2));
        processor.handle(op);

        assertEquals("New Name", market.getName());
        assertEquals(1, market.getSelections().size());
        assertEquals("s2", market.getSelections().get(0).getId());
        verify(eventRepository).save(existing);
        verify(publisher, atLeastOnce()).publish(any());
    }

    @Test
    void delete_removesExistingMarket() {
        Selection s = new Selection("s1", "Sel 1", 1.5);
        Market market = new Market("m1", "Market 1", List.of(s));
        Event existing = new Event("e1", "Event Name", LocalDate.parse("2025-10-06").atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
        existing.getMarkets().add(market);
        when(eventRepository.findById("e1")).thenReturn(Optional.of(existing));

        MarketOperation op = buildOperation(OperationType.DELETE, "e1", "m1", "Market 1", List.of());
        processor.handle(op);

        assertTrue(existing.getMarkets().isEmpty());
        verify(eventRepository).save(existing);
        verify(publisher, atLeastOnce()).publish(any());
    }
}
