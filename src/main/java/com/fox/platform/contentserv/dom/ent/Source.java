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
public class Source {
    private String id;
    private String code;
    private String name;
    private String publishDate;

    @JsonProperty(value="lastupdate")
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
