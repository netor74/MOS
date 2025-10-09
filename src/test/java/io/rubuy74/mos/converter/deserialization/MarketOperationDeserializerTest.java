package io.rubuy74.mos.converter.deserialization;

import io.rubuy74.mos.domain.MarketOperation;
import io.rubuy74.mos.domain.database.OperationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MarketOperationDeserializerTest {

    @Test
    void deserialize_ShouldReturnMarketOperation_WhenValidPayload() {
        LinkedHashMap<String, Object> rawPayload = new LinkedHashMap<>();
        LinkedHashMap<String, Object> marketRequestMap = new LinkedHashMap<>();
        LinkedHashMap<String, Object> eventMap = new LinkedHashMap<>();
        LinkedHashMap<String, Object> selectionMap = new LinkedHashMap<>();

        selectionMap.put("id", "s1");
        selectionMap.put("name", "Selection 1");
        selectionMap.put("odd", 1.5);

        eventMap.put("id", "e1");
        eventMap.put("name", "Event 1");
        eventMap.put("date", "2026-01-01");

        marketRequestMap.put("marketId", "m1");
        marketRequestMap.put("marketName", "Market 1");
        marketRequestMap.put("event", eventMap);
        marketRequestMap.put("selections", List.of(selectionMap));

        rawPayload.put("marketRequest", marketRequestMap);
        rawPayload.put("operationType", "ADD");

        MarketOperation result = MarketOperationDeserializer.deserialize(rawPayload);

        assertAll(
                () -> assertThat(result.getOperationType()).isEqualTo(OperationType.ADD),
                () -> assertThat(result.getMarketRequest().marketId).isEqualTo("m1"),
                () -> assertThat(result.getMarketRequest().marketName).isEqualTo("Market 1"),
                () -> assertThat(result.getMarketRequest().eventDTO.getId()).isEqualTo("e1"),
                () -> assertThat(result.getMarketRequest().selections).hasSize(1)
        );
    }

    static Stream<Arguments> invalidPayloads() {
        LinkedHashMap<String, Object> validPayload = new LinkedHashMap<>();
        LinkedHashMap<String, Object> marketRequestMap = new LinkedHashMap<>();
        LinkedHashMap<String, Object> eventMap = new LinkedHashMap<>();
        LinkedHashMap<String, Object> selectionMap = new LinkedHashMap<>();
        selectionMap.put("id", "s1");
        selectionMap.put("name", "Selection 1");
        selectionMap.put("odd", 1.5);
        eventMap.put("id", "e1");
        eventMap.put("name", "Event 1");
        eventMap.put("date", "2026-01-01");
        marketRequestMap.put("marketId", "m1");
        marketRequestMap.put("marketName", "Market 1");
        marketRequestMap.put("event", eventMap);
        marketRequestMap.put("selections", List.of(selectionMap));
        validPayload.put("marketRequest", marketRequestMap);
        validPayload.put("operationType", "ADD");

        return Stream.of(
                Arguments.of(null, "MarketOperation payload is null"),
                Arguments.of(new LinkedHashMap<>() {{
                    put("marketRequest", marketRequestMap);
                }}, "attribute 'operationType' doesn't exist"),
                Arguments.of(new LinkedHashMap<>() {{
                    put("marketRequest", marketRequestMap);
                    put("operationType", "INVALID_OP");
                }}, "No enum constant io.rubuy74.mos.domain.database.OperationType.INVALID_OP")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPayloads")
    void deserialize_ShouldThrowException_WhenPayloadIsInvalid(LinkedHashMap<String, Object> payload, String expectedMessage) {
        Throwable thrown = assertThrows(Throwable.class, () -> MarketOperationDeserializer.deserialize(payload));
        assertThat(thrown.getMessage()).contains(expectedMessage);
    }
}