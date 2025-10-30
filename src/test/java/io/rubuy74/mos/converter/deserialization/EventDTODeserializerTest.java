package io.rubuy74.mos.converter.deserialization;

import io.rubuy74.mos.dto.EventDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EventDTODeserializerTest {

    private static final long EVENT_DATE = LocalDate
            .parse("2023-12-01")
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli();
    private static final long VALID_EVENT_DATE = LocalDate
            .parse("2123-12-01")
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli();

    @Test
    void deserialize_ShouldReturnEventDTO_WhenValidPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", "123");
        payload.put("name", "Test Event");
        payload.put("date", VALID_EVENT_DATE);

        EventDTO eventDTO = EventDTODeserializer.deserialize(payload);

        assertAll(
                () -> assertThat(eventDTO.getId()).isEqualTo("123"),
                () -> assertThat(eventDTO.getName()).isEqualTo("Test Event"),
                () -> assertThat(eventDTO.getEpochMilliseconds()).isEqualTo(LocalDate.of(2123, 12, 1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli())
        );
    }

    @Test
    void deserialize_ShouldThrowException_WhenDateIsInThePast() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", "123");
        payload.put("name", "Past Event");
        payload.put("date", EVENT_DATE);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> EventDTODeserializer.deserialize(payload));
        assertThat(thrown.getMessage()).isEqualTo("Event DTO Date is invalid");
    }

    static Stream<Arguments> invalidPayloads() {
        return Stream.of(
                Arguments.of(null, "EventDTO payload is null"),
                Arguments.of(new HashMap<>() {{
                    put("id", "123");
                    put("date", VALID_EVENT_DATE);
                }}, "attribute 'name' doesn't exist"),
                Arguments.of(new HashMap<>() {{
                    put("id", "123");
                    put("name", "Test Event");
                    put("date", Long.valueOf("1231231"));
                }}, "Event DTO Date is invalid")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPayloads")
    void deserialize_ShouldThrowException_WhenPayloadIsInvalid(Map<String, Object> payload, String expectedMessage) {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> EventDTODeserializer.deserialize(payload));
        assertThat(thrown.getMessage()).contains(expectedMessage);
    }
}