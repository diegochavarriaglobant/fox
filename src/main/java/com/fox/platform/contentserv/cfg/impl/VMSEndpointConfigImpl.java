package com.fox.platform.contentserv.cfg.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.text.StrSubstitutor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fox.platform.contentserv.cfg.VMSEndpointConfig;
import com.fox.platform.lib.cfg.impl.EndpointConfigImpl;
import io.vertx.core.json.JsonObject;

/**
 * Class that manages the config info to call to MPX Endpoint
 * 
 * @author h.gonzalez
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VMSEndpointConfigImpl extends EndpointConfigImpl implements VMSEndpointConfig {

  private Map<String, String> mapUrlPath;

  public VMSEndpointConfigImpl() {
    super();
    mapUrlPath = new HashMap<>();
  }

  public Map<String, String> getMapUrlPath() {
    return mapUrlPath;
  }

  public void setMapUrlPath(Map<String, String> mapUrlPath) {
    this.mapUrlPath = mapUrlPath;
  }



  @Override
  public String getPath(JsonObject request, String defaultPath) {


    String url = Optional.ofNullable("AR").map(this::getUrlPathByCountry).orElse(defaultPath);
    return new StrSubstitutor(request.getMap()).replace(url);
  }

  @Override
  public String getPath(JsonObject request) {
    return getPath(request, getPath());
  }

  @Override
  public String getUrlPathByCountry(String country) {
    return mapUrlPath.getOrDefault(country, getPath());
  }

  public void merge(VMSEndpointConfigImpl other) {
    super.merge(other);
    this.setMapUrlPath(other.getMapUrlPath());
  }

}

