package io.rubuy74.mos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.rubuy74.mos.utils.ValidatorUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Selection {
    @Id
    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private Double odd;

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

    public Selection() {}

    public Selection(String id, String name, Double odd) {
        ValidatorUtils.checkArgument(id == null,    "Selection id is null","create_selection");
        ValidatorUtils.checkArgument(name == null,  "Selection name is null","create_selection");
        ValidatorUtils.checkArgument(odd == null,   "Selection odd is null","create_selection");
        ValidatorUtils.checkArgument(id.isBlank(),    "Selection id is empty","create_selection");
        ValidatorUtils.checkArgument(name.isBlank(),  "Selection name is empty","create_selection");
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
