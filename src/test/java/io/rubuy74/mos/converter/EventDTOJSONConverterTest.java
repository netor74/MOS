package io.rubuy74.mos.converter;

import io.rubuy74.mos.dto.EventDTO;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EventDTOJSONConverterTest {

    @Test
    void fromJson_returnsNull_whenPayloadIsNull() {
        assertNull(EventDTOJSONConverter.fromJson(null));
    }

    @Test
    void fromJson_returnsNull_whenMandatoryAttributesMissing() {
        Map<String, Object> payload = Map.of(
                "id", "e1",
                "name", "Event Name" // missing "date" and "event"
        );

        assertNull(EventDTOJSONConverter.fromJson(payload));
    }

    @Test
    void fromJson_parsesEventDTO_whenPayloadValid() {
        Map<String, Object> payload = Map.of(
                "id", "e1",
                "event", "ignored",
                "name", "Event Name",
                "date", "2025-10-06"
        );

        EventDTO dto = EventDTOJSONConverter.fromJson(payload);
        assertNotNull(dto);
        assertEquals("e1", dto.id);
        assertEquals("Event Name", dto.name);
        assertEquals("2025-10-06", dto.date.toString());
    }
}
