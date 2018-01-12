package com.fox.platform.contentserv.dom.ent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * POJO class to match the Json object
 * @author diego.chavarria
 *
 */
@Data
public class Show {

    private String id;
    private String name;
    private String code;
    private String publishDate;

    @JsonProperty(value="lastupdate")
    private String lastUpdate;

    private String programmingSerie;
    private ShowType type;
    private Paradigm paradigm;
}
