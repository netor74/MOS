package io.rubuy74.mos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
public class Event {

    @Id
    @JsonProperty
    public String id;

    @JsonProperty
    public String name;

    @JsonProperty
    public String date;

    @JsonProperty
    @OneToMany(cascade = CascadeType.ALL)
    public List<Market> markets;

    public Event(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.markets = List.of();
    }


    public Event() {

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
