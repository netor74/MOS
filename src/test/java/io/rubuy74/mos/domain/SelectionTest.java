package io.rubuy74.mos.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SelectionTest {
    private static final String SELECTION_ID = "s1";
    private static final String SELECTION_NAME = "Home";
    private static final double SELECTION_ODD = 1.5;

    @Test
    void constructor_ShouldSetFields_WhenValidArgsProvided() {
        Selection selection = new Selection(SELECTION_ID, SELECTION_NAME, SELECTION_ODD);
        assertAll(
            () -> assertThat(selection).isNotNull(),
            () -> assertThat(selection.toString()).contains(SELECTION_NAME)
        );
    }

    static Stream<Arguments> invalidConstructorArgs() {
        return Stream.of(
            Arguments.of(null, SELECTION_NAME, SELECTION_ODD,"Selection id is null"),
            Arguments.of(SELECTION_ID, null, SELECTION_ODD,"Selection name is null"),
            Arguments.of(SELECTION_ID, SELECTION_NAME, null,"Selection odd is null"),
            Arguments.of("", SELECTION_NAME, SELECTION_ODD,"Selection id is empty"),
            Arguments.of(SELECTION_ID,"", SELECTION_ODD,"Selection name is empty"),
            Arguments.of(" ", SELECTION_NAME, SELECTION_ODD,"Selection id is empty"),
            Arguments.of(SELECTION_ID," ", SELECTION_ODD,"Selection name is empty")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidConstructorArgs")
    void constructor_ShouldThrow_WhenAnyArgumentIsInvalid(String id, String name, Double odd, String expectedMessage) {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Selection(id, name, odd));
        assertThat(thrown.getMessage()).isEqualTo(expectedMessage);
    }
}


