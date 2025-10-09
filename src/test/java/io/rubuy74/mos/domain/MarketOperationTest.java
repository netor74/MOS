package io.rubuy74.mos.domain;

import io.rubuy74.mos.domain.database.MarketRequest;
import io.rubuy74.mos.domain.database.OperationType;
import io.rubuy74.mos.dto.EventDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MarketOperationTest {
    private static final String EVENT_ID = "e1";
    private static final String EVENT_NAME = "Match";
    private static final LocalDate EVENT_DATE = LocalDate.parse("2025-12-01");
    private static final String MARKET_ID = "m1";
    private static final String MARKET_NAME = "Market 1";

    @Test
    void constructor_ShouldSetFields_WhenValidArgsProvided() {
        EventDTO eventDTO = new EventDTO(EVENT_ID, EVENT_NAME, EVENT_DATE);
        MarketRequest req = new MarketRequest(MARKET_ID, MARKET_NAME, eventDTO, new ArrayList<>());
        MarketOperation op = new MarketOperation(req, OperationType.ADD);

        assertAll(
            () -> assertThat(op.getMarketRequest()).isSameAs(req),
            () -> assertThat(op.getOperationType()).isEqualTo(OperationType.ADD),
            () -> assertThat(op.toString()).contains("operationType=ADD")
        );
    }

    static Stream<Arguments> invalidConstructorArgs() {
        return Stream.of(
            Arguments.of(null, OperationType.ADD,"Market Request is null"),
            Arguments.of(new MarketRequest(MARKET_ID, MARKET_NAME, new EventDTO(EVENT_ID, EVENT_NAME, EVENT_DATE), new ArrayList<>()), null, "OperationType is null")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidConstructorArgs")
    void constructor_ShouldThrow_WhenAnyArgumentIsNull(MarketRequest req, OperationType op, String expectedMessage) {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new MarketOperation(req, op));
        assertThat(thrown.getMessage()).isEqualTo(expectedMessage);
    }
}
