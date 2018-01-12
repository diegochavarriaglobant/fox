package com.fox.platform.contentserv.dom.ent;

import lombok.Data;

/**
 * POJO class to match the Json object
 * @author diego.chavarria
 *
 */
@Data
public class Shards {
    private Integer total;
    private Integer successful;
    private Integer failed;
}
