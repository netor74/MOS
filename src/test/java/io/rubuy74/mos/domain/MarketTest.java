package io.rubuy74.mos.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MarketTest {
    private static final String MARKET_ID = "m1";
    private static final String MARKET_NAME = "Market 1";
    private static final String SELECTION_ID = "s1";
    private static final String SELECTION_NAME = "Sel 1";
    private static final double SELECTION_ODD = 2.0;

    @Test
    void constructor_ShouldSetFields_WhenValidArgsProvided() {
        List<Selection> selectionList = List.of(new Selection(SELECTION_ID, SELECTION_NAME, SELECTION_ODD));
        Market market = new Market(MARKET_ID, MARKET_NAME, selectionList);

        assertAll(
            () -> assertThat(market).isNotNull(),
            () -> assertThat(market.toString()).contains("Market")
        );
    }

    static Stream<Arguments> invalidConstructorArgs() {
        return Stream.of(
            Arguments.of(null, MARKET_NAME, List.of(), "Market id is null"),
            Arguments.of(MARKET_ID, null, List.of(), "Market name is null"),
            Arguments.of(MARKET_ID, MARKET_NAME, null, "Market selections are null"),
            Arguments.of("", MARKET_NAME, List.of(), "Market id is empty"),
            Arguments.of(MARKET_ID, "", List.of(), "Market name is empty"),
            Arguments.of(" ", MARKET_NAME, List.of(), "Market id is empty"),
            Arguments.of(MARKET_ID, " ", List.of(),"Market name is empty")
        );
    }
    @ParameterizedTest
    @MethodSource("invalidConstructorArgs")
    void constructor_ShouldThrow_WhenAnyArgumentIsInvalid(String id, String name, List<Selection> selections, String expectedMessage) {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Market(id, name, selections));
        assertThat(thrown.getMessage()).isEqualTo(expectedMessage);
    }
}


