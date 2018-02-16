package com.fox.platform.contentserv.cfg;


import com.fox.platform.lib.cfg.ServiceConfig;
import io.vertx.core.http.HttpMethod;

/**
 * Inbterface that extends the behaviour of the ServiceConfig
 * 
 * @author diego.chavarria
 *
 */
public interface ContentServiceConfig extends ServiceConfig {
  /**
   * Api path
   * 
   * @return
   */
  public String getApiPath();

  /**
   * Port Omnix Service
   * 
   * @return
   */
  public int getPortOmnixService();

  /**
   * Uri Omnix Service
   * 
   * @return
   */
  public String getUriOmnixService();

  /**
   * Query Omnix Service
   * 
   * @return
   */
  public String getQueryOmnixService();

  /**
   * Address proxy
   * 
   * @return
   */
  public String getAddressProxy();

  /**
   * Host Omnix Service
   * 
   * @return
   */
  public String getHostOmnixService();

  /**
   * Default Path
   * 
   * @return
   */
  public String getDefaultPath();

  /**
   * Default API Method
   * 
   * @return
   */
  public HttpMethod getDefaultApiMethod();

  public ContentProxyConfig getProxyVMS();

  public void merge(ContentServiceConfig other);



}
