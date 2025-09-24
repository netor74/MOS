package io.rubuy74.mos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Market {

    @JsonProperty
    @Id
    public String id;

    @JsonProperty
    public String name;

    @JsonProperty
    @OneToMany(cascade = CascadeType.ALL)
    public List<Selection> selections;

    public Market(String id, String name, List<Selection> selections) {
        this.id = id;
        this.name = name;
        this.selections = selections;
    }

    public Market() {

    }
}
