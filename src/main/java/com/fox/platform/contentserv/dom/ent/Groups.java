package com.fox.platform.contentserv.dom.ent;

import lombok.Data;

import java.util.List;

/**
 * POJO class to match the Json object
 * @author diego.chavarria
 *
 */
@Data
public class Groups {
    private String id;
    private Boolean enabled;
    private String priority;
    private List<Feeds> feeds;
    private Fields fields;

}
