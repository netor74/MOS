package io.rubuy74.mos.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class EventDTO {
    @JsonProperty
    public String id;

    @JsonProperty
    public String name;

    @JsonProperty
    public String date;

    public static EventDTO fromJson(Map<String,Object> rawPayload) {
        String id = (String) rawPayload.get("id");
        String name = (String) rawPayload.get("name");
        String date = (String) rawPayload.get("date");

        return new EventDTO(id,name,date);
    }

    public EventDTO() {}

    public EventDTO(Event event) {
        this.id = event.id;
        this.name = event.name;
        this.date = event.date;
    }

    public EventDTO(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Event{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", date=" + date + '}';
    }
}
