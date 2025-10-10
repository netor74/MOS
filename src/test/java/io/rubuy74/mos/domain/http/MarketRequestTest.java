package io.rubuy74.mos.domain.http;

import io.rubuy74.mos.domain.database.MarketRequest;
import io.rubuy74.mos.dto.EventDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MarketRequestTest {
    private static final String MARKET_ID = "m1";
    private static final String MARKET_NAME = "Market 1";
    private static final String EVENT_ID = "e1";
    private static final String EVENT_NAME = "Match";
    private static final LocalDate EVENT_DATE = LocalDate.parse("2025-12-01");

    @Test
    void constructor_ShouldSetFieldsAndTimestamp() {
        EventDTO eventDTO = new EventDTO(EVENT_ID, EVENT_NAME, EVENT_DATE);
        MarketRequest req = new MarketRequest(MARKET_ID, MARKET_NAME, eventDTO, new ArrayList<>());
        assertAll(
            () -> assertThat(req.marketId).isEqualTo(MARKET_ID),
            () -> assertThat(req.marketName).isEqualTo(MARKET_NAME),
            () -> assertThat(req.eventDTO).isSameAs(eventDTO),
            () -> assertThat(req.timestamp).isNotNull(),
            () -> assertThat(req.selections).isNotNull()
        );
    }
}


