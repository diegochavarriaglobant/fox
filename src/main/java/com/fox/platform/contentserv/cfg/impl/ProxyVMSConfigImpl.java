package com.fox.platform.contentserv.cfg.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fox.platform.contentserv.cfg.ContentProxyConfig;
import com.fox.platform.lib.cbr.cfg.SwitchCircuitBreakerConfig;
import com.fox.platform.lib.cbr.cfg.impl.SwitchCircuitBreakerConfigImpl;

/**
 * Class that manages the configuration of the microservices, this is the "proxyVMS" object in the
 * configuration object
 * 
 * @author h.gonzalez
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProxyVMSConfigImpl implements ContentProxyConfig {

  private static final String DEFAULT_ADDRESS = "ProxyVMSAddr";

  private String address;
  private SwitchCircuitBreakerConfigImpl circuitBreaker;
  private VMSEndpointConfigImpl endpoint;

  public ProxyVMSConfigImpl() {
    circuitBreaker = new SwitchCircuitBreakerConfigImpl();
    address = DEFAULT_ADDRESS;
    endpoint = new VMSEndpointConfigImpl();
  }

  public void setCircuitBreaker(SwitchCircuitBreakerConfigImpl circuitBreaker) {
    this.circuitBreaker = circuitBreaker;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setEndpoint(VMSEndpointConfigImpl endpoint) {
    this.endpoint = endpoint;
  }

  @Override
  public SwitchCircuitBreakerConfig getCircuitBreaker() {
    return circuitBreaker;
  }

  @Override
  public String getAddress() {
    return address;
  }

  @Override
  public VMSEndpointConfigImpl getEndpoint() {
    return endpoint;
  }

  public void merge(ProxyVMSConfigImpl other) {
    this.setAddress(other.getAddress());
    circuitBreaker.merge(other.getCircuitBreaker());
    endpoint.merge(other.getEndpoint());
  }

}
