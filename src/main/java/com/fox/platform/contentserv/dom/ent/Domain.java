package com.fox.platform.contentserv.dom.ent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * POJO class to match the Json object
 * 
 * @author diego.chavarria
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Domain {

  private Integer id;
  private String name;
  private String languageId;
  private String networkId;
  private String countryId;
}
