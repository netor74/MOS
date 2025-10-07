package io.rubuy74.mos.utils;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidatorUtilsTest {

    @Test
    void returnsEmptyListWhenAllAttributesPresent() {
        Map<String, Object> payload = Map.of(
                "event", "e1",
                "name", "Event Name",
                "date", "2025-10-06"
        );
        List<String> attributes = List.of("event", "name", "date");

        List<String> result = ValidatorUtils.checkAttributeList(payload, attributes);

        assertTrue(result.isEmpty());
    }

    @Test
    void returnsMessagesForMissingAttributes() {
        Map<String, Object> payload = Map.of(
                "event", "e1"
        );
        List<String> attributes = List.of("event", "name", "date");

        List<String> result = ValidatorUtils.checkAttributeList(payload, attributes);

        assertEquals(2, result.size());
        assertTrue(result.contains("attribute 'name' doesn't exist"));
        assertTrue(result.contains("attribute 'date' doesn't exist"));
    }
}
