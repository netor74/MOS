package io.rubuy74.mos.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EventTest {
    private static final String EVENT_ID = "1";
    private static final String EVENT_NAME = "Name";
    private static final LocalDate EVENT_DATE = LocalDate.parse("2025-12-01");

    @Test
    void constructor_ShouldSetFields_WhenValidArgsProvided() {
        LocalDate date = EVENT_DATE;
        Event event = new Event(EVENT_ID, EVENT_NAME, date);

        assertAll(
            () -> assertThat(event.getId()).isEqualTo(EVENT_ID),
            () -> assertThat(event.getName()).isEqualTo(EVENT_NAME),
            () -> assertThat(event.getDate()).isEqualTo(date),
            () -> assertThat(event.toString()).contains("id=" + EVENT_ID).contains(EVENT_NAME)
        );
    }

    static Stream<Arguments> invalidConstructorArgs() {
        return Stream.of(
                Arguments.of(null, EVENT_NAME, EVENT_DATE, "Event id is null"),
                Arguments.of(EVENT_ID, null, EVENT_DATE, "Event name is null"),
                Arguments.of(EVENT_ID, EVENT_NAME, null, "Event date is null"),
                Arguments.of("", EVENT_NAME, EVENT_DATE, "Event id is empty"),
                Arguments.of(EVENT_ID, "", EVENT_DATE, "Event name is empty"),
                Arguments.of(" ", EVENT_NAME, EVENT_DATE, "Event id is empty"),
                Arguments.of(EVENT_ID, " ", EVENT_DATE, "Event name is empty")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidConstructorArgs")
    void constructor_ShouldThrow_WhenAnyArgumentIsNull(String id, String name, LocalDate date, String expectedMessage) {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Event(id, name, date));
        assertThat(thrown.getMessage()).isEqualTo(expectedMessage);
    }

}


