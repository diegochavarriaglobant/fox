package com.fox.platform.contentserv.dom.ent;

import java.util.List;
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
public class Groups {
  private String id;
  private Boolean enabled;
  private String priority;
  private List<Feeds> feeds;
  private Fields fields;

}
