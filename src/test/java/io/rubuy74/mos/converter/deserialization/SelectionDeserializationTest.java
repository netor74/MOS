package io.rubuy74.mos.converter.deserialization;

import io.rubuy74.mos.domain.Selection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SelectionDeserializationTest {

    @Test
    void deserialize_ShouldReturnSelection_WhenValidPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", "456");
        payload.put("name", "Team B");
        payload.put("odd", 2.25);

        Selection selection = SelectionDeserializer.deserialize(payload);

        assertAll(
                () -> assertThat(selection).isNotNull(),
                () -> assertThat(selection.toString()).contains("id=456", "name=Team B", "odd=2.25")
        );
    }

    static Stream<Arguments> invalidPayloads() {
        return Stream.of(
                Arguments.of(null, "Selection payload is null"),
                Arguments.of(new HashMap<>() {{
                    put("id", "456");
                    put("name", "Team B");
                }}, "attribute 'odd' doesn't exist")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPayloads")
    void deserialize_ShouldThrowException_WhenPayloadIsInvalid(Map<String, Object> payload, String expectedMessage) {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> SelectionDeserializer.deserialize(payload));
        assertThat(thrown.getMessage()).contains(expectedMessage);
    }
}