package io.rubuy74.mos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.List;

public class MarketRequest {
    @JsonProperty
    public String marketId;

    @JsonProperty
    public String marketName;

    @JsonProperty
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
        return "MarketRequest{" +
                "marketId='" + marketId + '\'' +
                ", marketName='" + marketName + '\'' +
                ", event=" + eventDTO +
                ", selections=" + selections +
                "}";
    }
}
