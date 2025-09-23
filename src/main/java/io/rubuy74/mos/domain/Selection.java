package io.rubuy74.mos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Selection {
    @JsonProperty
    String id;

    @JsonProperty
    String name;

    @JsonProperty
    Double odd;

    public Selection(String id, String name, Double odd) {
        this.id = id;
        this.name = name;
        this.odd = odd;
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
