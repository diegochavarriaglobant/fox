package com.fox.platform.contentserv.cfg;


import com.fox.platform.lib.cfg.ServiceConfig;

/**
 * Inbterface that extends the behaviour of the ServiceConfig
 * @author diego.chavarria
 *
 * */
public interface ContentServiceConfig extends ServiceConfig {
    /**
     * Api path
     * @return
     */
    public String getApiPath();

    /**
     * Port Omnix Service
     * @return
     */
    public int getPortOmnixService();

    /**
     * Uri Omnix Service
     * @return
     */
    public String getUriOmnixService();

    /**
     * Query Omnix Service
     * @return
     */
    public String getQueryOmnixService();

    /**
     * Address proxy
     * @return
     */
    public String getAddressProxy();

    /**
     * Host Omnix Service
     * @return
     */
    public String getHostOmnixService();



}
