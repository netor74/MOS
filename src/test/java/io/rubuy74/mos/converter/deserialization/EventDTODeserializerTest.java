package io.rubuy74.mos.converter.deserialization;

import io.rubuy74.mos.dto.EventDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EventDTODeserializerTest {

    @Test
    void deserialize_ShouldReturnEventDTO_WhenValidPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", "123");
        payload.put("name", "Test Event");
        payload.put("date", "2026-01-01");

        EventDTO eventDTO = EventDTODeserializer.deserialize(payload);

        assertAll(
                () -> assertThat(eventDTO.getId()).isEqualTo("123"),
                () -> assertThat(eventDTO.getName()).isEqualTo("Test Event"),
                () -> assertThat(eventDTO.getDate()).isEqualTo(LocalDate.of(2026, 1, 1))
        );
    }

    @Test
    void deserialize_ShouldThrowException_WhenDateIsInThePast() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", "123");
        payload.put("name", "Past Event");
        payload.put("date", "2024-01-01");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> EventDTODeserializer.deserialize(payload));
        assertThat(thrown.getMessage()).isEqualTo("Event DTO Date is invalid");
    }

    static Stream<Arguments> invalidPayloads() {
        return Stream.of(
                Arguments.of(null, "EventDTO payload is null"),
                Arguments.of(new HashMap<>() {{
                    put("id", "123");
                    put("date", "2026-01-01");
                }}, "attribute 'name' doesn't exist"),
                Arguments.of(new HashMap<>() {{
                    put("id", "123");
                    put("name", "Test Event");
                    put("date", "invalid-date");
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