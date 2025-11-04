package io.rubuy74.mos.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.google.common.base.MoreObjects;
import io.rubuy74.mos.utils.ValidatorUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

@Entity
public class Event {

    private static final ZoneId zoneId = ZoneOffset.UTC;

    @JsonProperty
    @Id
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private long date;

    @JsonProperty
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    private final List<Market> markets = new java.util.ArrayList<>();


    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public long getDate() {
        return date;
    }
    public List<Market> getMarkets() {
        return markets;
    }

    public Event() {}

    public Event(String id, String name, long date) {
        ValidatorUtils.checkArgument(id == null,"Event id is null","create_event");
        ValidatorUtils.checkArgument(name == null,"Event name is null","create_event");
        ValidatorUtils.checkArgument(date <= 0,"Event date is null","create_event");
        ValidatorUtils.checkArgument(id.isBlank(),   "Event id is empty","create_event");
        ValidatorUtils.checkArgument(name.isBlank(), "Event name is empty","create_event");
        this.id = id;
        this.name = name;
        this.date = date;
    }

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
