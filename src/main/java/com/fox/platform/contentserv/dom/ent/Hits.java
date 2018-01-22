package com.fox.platform.contentserv.dom.ent;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * POJO class to match the Json object
 * 
 * @author diego.chavarria
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hits {
  private Integer total;

  @JsonProperty(value = "max_score")
  private Integer maxScore;

  @JsonProperty(value = "_shards")
  private Shards shards;

  private List<Hit> hits;
}
