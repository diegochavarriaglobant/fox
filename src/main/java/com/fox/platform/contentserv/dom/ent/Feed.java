package com.fox.platform.contentserv.dom.ent;

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
public class Feed {
  private Integer took;

  @JsonProperty(value = "timed_out")
  private Boolean timedOut;

  @JsonProperty(value = "_shards")
  private Shards shards;

  private Hits hits;
}
