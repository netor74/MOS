package io.rubuy74.mos.domain.market;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rubuy74.mos.domain.Selection;
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
    @ManyToMany
    @JoinTable(
            name = "market_selections",
            joinColumns = @JoinColumn(name = "market_id"),
            inverseJoinColumns = @JoinColumn()
    )
    public List<Selection> selections;

    public Market(String id, String name, List<Selection> selections) {
        this.id = id;
        this.name = name;
        this.selections = new java.util.ArrayList<>(selections);
    }

    public Market() {

    }
}
