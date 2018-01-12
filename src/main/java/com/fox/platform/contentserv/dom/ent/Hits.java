package com.fox.platform.contentserv.dom.ent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * POJO class to match the Json object
 * @author diego.chavarria
 *
 */
@Data
public class Hits {
    private Integer total;

    @JsonProperty(value="max_score")
    private Integer maxScore;

    @JsonProperty(value="_shards")
    private Shards shards;

    private List<Hit> hits;
}
