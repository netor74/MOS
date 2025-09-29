package io.rubuy74.mos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;

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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Market> markets = new java.util.ArrayList<>();

    public Event(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
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
