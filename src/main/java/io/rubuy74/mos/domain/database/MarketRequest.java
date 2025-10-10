package io.rubuy74.mos.domain.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.rubuy74.mos.dto.EventDTO;
import io.rubuy74.mos.domain.Selection;

import java.sql.Timestamp;
import java.util.List;

public class MarketRequest {
    @JsonProperty
    public String marketId;

    @JsonProperty
    public String marketName;

    @JsonProperty("event")
    public EventDTO eventDTO;

    @JsonProperty
    public Timestamp timestamp;

    @JsonProperty
    public List<Selection> selections;

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
