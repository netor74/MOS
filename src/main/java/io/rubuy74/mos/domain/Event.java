package io.rubuy74.mos.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Event {
    @JsonProperty
    @Id
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @JsonProperty
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    private final List<Market> markets = new java.util.ArrayList<>();


    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public LocalDate getDate() {
        return date;
    }
    public List<Market> getMarkets() {
        return markets;
    }

    public Event(String id, String name, LocalDate date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }
    public Event() {}

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
                .add("id",id)
                .add("name",name)
                .add("date",date)
                .add("market",markets)
                .toString();
    }
}
