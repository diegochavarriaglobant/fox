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
public class SourceFields {

  @JsonProperty(value = "auth_level")
  private String authLevel;

  @JsonProperty
  private String id;

  @JsonProperty
  private String logo;

  @JsonProperty
  private String urn;

  @JsonProperty
  private String signal;

  @JsonProperty
  private String enabled;

}
