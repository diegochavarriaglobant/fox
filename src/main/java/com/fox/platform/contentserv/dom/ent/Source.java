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
public class Source {
  private String id;
  private String code;
  private String name;
  private String publishDate;

  @JsonProperty(value = "lastupdate")
  private String lastUpdate;

  private Boolean isSocialPost;
  private Type type;
  private Domain domain;
  private Show show;
  private String updatedOn;
  private List<Groups> groups;
  private List<Feeds> feeds;
  private List<Categories> categories;


}
