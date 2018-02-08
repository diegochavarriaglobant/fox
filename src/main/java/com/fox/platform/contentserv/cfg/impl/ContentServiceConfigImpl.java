package com.fox.platform.contentserv.cfg.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fox.platform.contentserv.cfg.ContentServiceConfig;
import com.fox.platform.lib.cfg.impl.MergeHttpServerOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;

/**
 * Class that represents the configuration of the Content Service
 * 
 * @author diego.chavarria
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentServiceConfigImpl implements ContentServiceConfig {

  public static final String CONFIG_FIELD = "contentService";
  private static final String ADDRESS_PROXY = "omnix_service";
  private static final int DEFAULT_PORT = 8081;
  private static final String DEFAULT_API_PATH = "/channel";
  private static final int DEFAULT_PORT_OMNIX_SERVICE = 443;
  private static final String DEFAULT_HOST_OMNIX_SERVICE =
      "search-omnix-services-sh2266ar6ket7lqcnhj3dpzccu.us-east-1.es.amazonaws.com";
  private static final String DEFAULT_URI_OMNIX_SERVICE = "/omnix_es/contentObjects/_search";
  private static final String DEFAULT_QUERY_OMNIX_SERVICE =
      "{\"query\":{ \"bool\":{\"must\":[{\"term\": {\"type.description\": \"olympicschannel\"}},{\"nested\": {\"path\": \"groups\",\"query\": {\"bool\": {\"must\": [{\"nested\": {\"path\": \"groups.feeds\",\"query\": { \"match\": {\"groups.feeds.countryId\": \"{countryParam}\" } } } }] } }, \"inner_hits\": {} } }]}},\"sort\": [{\"groups.fields.id.raw\": { \"nested_path\": \"groups\", \"order\": \"asc\"}}]}";
  private static final String DEFAULT_PATH = "/";
  private static final HttpMethod DEFAULT_API_METHOD = HttpMethod.POST;

  private String apiPath;
  private int portOmnixService;
  private String hostOmnixService;
  private String uriOmnixService;
  private MergeHttpServerOptions httpServerOptions;
  private String queryOmnixService;
  private String addressProxy;
  private String defaultPath;
  private HttpMethod defaultApiMethod;
  private ProxyVMSConfigImpl proxyVMS;

  public ContentServiceConfigImpl() {

    this.apiPath = DEFAULT_API_PATH;
    this.portOmnixService = DEFAULT_PORT_OMNIX_SERVICE;
    this.hostOmnixService = DEFAULT_HOST_OMNIX_SERVICE;
    this.uriOmnixService = DEFAULT_URI_OMNIX_SERVICE;
    this.httpServerOptions = new MergeHttpServerOptions();
    this.httpServerOptions.setPort(DEFAULT_PORT);
    this.queryOmnixService = DEFAULT_QUERY_OMNIX_SERVICE;
    this.addressProxy = ADDRESS_PROXY;
    this.defaultPath = DEFAULT_PATH;
    this.defaultApiMethod = DEFAULT_API_METHOD;
    this.proxyVMS = new ProxyVMSConfigImpl();

  }

  /**
   * Api path
   * 
   * @return
   */
  public String getApiPath() {
    return apiPath;
  }

  public void setApiPath(String apiPath) {
    this.apiPath = apiPath;
  }

  /**
   * Port Omnix Service
   * 
   * @return
   */
  public int getPortOmnixService() {
    return portOmnixService;
  }

  public void setPortOmnixService(int portOmnixService) {
    this.portOmnixService = portOmnixService;
  }

  /**
   * Host Omnix Service
   * 
   * @return
   */
  public String getHostOmnixService() {
    return hostOmnixService;
  }

  public void setHostOmnixService(String hostOmnixService) {
    this.hostOmnixService = hostOmnixService;
  }


  /**
   * Uri Omnix Service
   * 
   * @return
   */
  public String getUriOmnixService() {
    return uriOmnixService;
  }

  public void setUriOmnixService(String uriOmnixService) {
    this.uriOmnixService = uriOmnixService;
  }

  /**
   * Query Omnix Service
   * 
   * @return
   */
  public String getQueryOmnixService() {
    return queryOmnixService;
  }

  public void setQueryOmnixService(String queryOmnixService) {
    this.queryOmnixService = queryOmnixService;
  }


  /**
   * Address proxy
   * 
   * @return
   */
  public String getAddressProxy() {
    return addressProxy;
  }

  public void setAddressProxy(String addressProxy) {
    this.addressProxy = addressProxy;
  }


  /**
   * Default path
   * 
   * @return
   */
  public String getDefaultPath() {
    return defaultPath;
  }

  public void setDefaultPath(String defaultPath) {
    this.defaultPath = defaultPath;
  }

  /**
   * Default Path Method
   * 
   * @return
   */
  public HttpMethod getDefaultApiMethod() {
    return defaultApiMethod;
  }

  public void setDefaultApiMethod(HttpMethod defaultApiMethod) {
    this.defaultApiMethod = defaultApiMethod;
  }


  @Override
  public HttpServerOptions getHttpServerOptions() {
    return httpServerOptions;
  }

  public void setHttpServerOptions(MergeHttpServerOptions httpServerOptions) {
    this.httpServerOptions = httpServerOptions;
  }

  public ProxyVMSConfigImpl getProxyVMS() {
    return proxyVMS;
  }

  public void setProxyVMS(ProxyVMSConfigImpl proxyVMS) {
    this.proxyVMS = proxyVMS;
  }

  @Override
  public void merge(ContentServiceConfig other) {

    this.setApiPath(other.getApiPath());
    this.setPortOmnixService(other.getPortOmnixService());
    this.setHostOmnixService(other.getHostOmnixService());
    this.setUriOmnixService(other.getUriOmnixService());
    this.setQueryOmnixService(other.getQueryOmnixService());
    this.setAddressProxy(other.getAddressProxy());
    this.setDefaultPath(other.getDefaultPath());
    this.setDefaultApiMethod(other.getDefaultApiMethod());
    httpServerOptions.merge(other.getHttpServerOptions());
    proxyVMS.merge((ProxyVMSConfigImpl) other.getProxyVMS());

  }
}
