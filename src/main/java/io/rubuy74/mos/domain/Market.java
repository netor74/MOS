package io.rubuy74.mos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Market {

    @JsonProperty
    @Id
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    @ManyToMany
    @JoinTable(
            name = "market_selections",
            joinColumns = @JoinColumn(name = "market_id"),
            inverseJoinColumns = @JoinColumn()
    )
    public List<Selection> selections;

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
    public List<Selection> getSelections() {
        return selections;
    }

    public Market(String id, String name, List<Selection> selections) {
        this.id = id;
        this.name = name;
        this.selections = new java.util.ArrayList<>(selections);
    }

    public Market() {

    }
}
