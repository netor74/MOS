package io.rubuy74.mos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Market {
    @JsonProperty
    String id;

    @JsonProperty
    String name;

    @JsonProperty
    List<Selection> selections;
}
