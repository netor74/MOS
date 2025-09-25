package io.rubuy74.mos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Map;

@Entity
public class Selection {

    @Id
    @JsonProperty
    public String id;

    @JsonProperty
    public String name;

    @JsonProperty
    public Double odd;

    public static Selection fromJson(Map<String,Object> rawPayload) {
        String id = (String) rawPayload.get("id");
        String name = (String) rawPayload.get("name");
        Double odd = (Double) rawPayload.get("odd");
        return new Selection(id, name, odd);
    }

    public Selection(String id, String name, Double odd) {
        this.id = id;
        this.name = name;
        this.odd = odd;
    }

    public Selection() {

    }

    @Override
    public String toString() {
        return "Selection{" +
                "id:" + id
                + ", name:" + name
                + ", odd:" + odd
                ;
    }
}
