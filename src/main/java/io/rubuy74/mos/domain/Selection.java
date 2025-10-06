package io.rubuy74.mos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Map;

@Entity
public class Selection {
    @Id
    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private Double odd;

    public Selection() {}

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getOdd() {
        return odd;
    }

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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
                .add("id",id)
                .add("name",name)
                .add("odd",odd)
                .toString();
    }
}
