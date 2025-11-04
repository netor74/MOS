package io.rubuy74.mos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rubuy74.mos.utils.ValidatorUtils;
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

    public Market() {}

    public Market(String id, String name, List<Selection> selections) {
        ValidatorUtils.checkArgument(id == null,        "Market id is null","create_market");
        ValidatorUtils.checkArgument(name == null,      "Market name is null","create_market");
        ValidatorUtils.checkArgument(selections == null,"Market selections are null","create_market");
        ValidatorUtils.checkArgument(id.isBlank(),   "Market id is empty","create_market");
        ValidatorUtils.checkArgument(name.isBlank(), "Market name is empty","create_market");
        this.id = id;
        this.name = name;
        this.selections = new java.util.ArrayList<>(selections);
    }
}
