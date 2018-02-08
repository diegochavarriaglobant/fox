package com.fox.platform.contentserv.cfg;

import com.fox.platform.lib.cfg.EndpointConfig;

public interface VMSEndpointConfig extends EndpointConfig {
  public String getUrlPathByCountry(String country);
}
