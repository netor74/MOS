package io.rubuy74.mos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.List;

public class MarketRequest {
    @JsonProperty
    String marketId;

    @JsonProperty
    String marketName;

    @JsonProperty
    Event event;

    @JsonProperty
    Timestamp timestamp;

    @JsonProperty
    List<Selection> selections;

    public MarketRequest(String marketId, String marketName,Event event, List<Selection> selections) {
        this.marketId = marketId;
        this.marketName = marketName;
        this.event = event;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.selections = selections;
    }
    @Override
    public String toString() {
        return "MarketRequest{" +
                "marketId='" + marketId + '\'' +
                ", marketName='" + marketName + '\'' +
                ", event=" + event +
                ", selections=" + selections +
                "}";
    }
}
