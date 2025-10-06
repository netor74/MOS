package io.rubuy74.mos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.rubuy74.mos.domain.Event;

import java.time.LocalDate;
import java.util.Map;

public class EventDTO {
    @JsonProperty
    public String id;

    @JsonProperty
    public String name;

    @JsonProperty
    @JsonFormat(pattern = "dd/MM/yyyy")
    public LocalDate date;

    public EventDTO() {}

    public EventDTO(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.date = event.getDate();
    }

    public EventDTO(String id, String name, LocalDate date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
                .add("id",id)
                .add("name", name)
                .add("date",date)
                .toString();
    }
}
