package com.fox.platform.contentserv.cfg;


import com.fox.platform.lib.cfg.ServiceConfig;
import com.fox.platform.lib.cfg.impl.MergeHttpServerOptions;
import io.vertx.core.http.HttpMethod;

/**
 * Inbterface that extends the behaviour of the ServiceConfig
 * @author diego.chavarria
 *
 * */
public interface ContentServiceConfig extends ServiceConfig {

    public String getApiPath();
    public int getPortOmnixService();
    public String getUriOmnixService();
    public String getQueryOmnixService();
    public HttpMethod getChannelHttpMethod();



}
