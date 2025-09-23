package io.rubuy74.mos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class Event {
    @JsonProperty
    String id;

    @JsonProperty
    String name;

    @JsonProperty
    String date;

    @JsonProperty
    List<Market> markets;

    public Event(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.markets = List.of();
    }

    @Override
    public String toString() {
        return "Event{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", date=" + date
                + ", markets=" + markets;
    }
}
