package com.fox.platform.contentserv.dom.ent;

import lombok.Data;

/**
 * POJO class to match the Json object
 * @author diego.chavarria
 *
 */
@Data
public class Domain {

    private Integer id;
    private String  name;
    private String  languageId;
    private String  networkId;
    private String  countryId;
}
