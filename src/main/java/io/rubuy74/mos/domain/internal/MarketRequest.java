package io.rubuy74.mos.domain.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.rubuy74.mos.dto.EventDTO;
import io.rubuy74.mos.domain.Selection;

import java.sql.Timestamp;
import java.util.List;

public class MarketRequest {
    @JsonProperty
    private String marketId;

    @JsonProperty
    private String marketName;

    @JsonProperty("event")
    private EventDTO eventDTO;

    @JsonProperty
    private Timestamp timestamp;

    @JsonProperty
    private List<Selection> selections;

    public String getMarketId() {
        return marketId;
    }
    public String getMarketName() {
        return marketName;
    }
    public EventDTO getEventDTO() {
        return eventDTO;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public List<Selection> getSelections() {
        return selections;
    }

    public MarketRequest() {}

    public MarketRequest(String marketId, String marketName,EventDTO eventDTO, List<Selection> selections) {
        this.marketId = marketId;
        this.marketName = marketName;
        this.eventDTO = eventDTO;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.selections = selections;
    }
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
                .add("marketId",marketId)
                .add("marketName",marketName)
                .add("event",eventDTO)
                .add("selections",selections)
                .toString();
    }
}
