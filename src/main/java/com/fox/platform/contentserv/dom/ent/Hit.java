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
public class Hit {
  @JsonProperty(value = "_index")
  private String index;

  @JsonProperty(value = "_type")
  private String type;

  @JsonProperty(value = "_id")
  private String id;

  @JsonProperty(value = "_score")
  private Integer score;

  @JsonProperty(value = "_source")
  private Source source;

  private List<Sort> sort;
}
