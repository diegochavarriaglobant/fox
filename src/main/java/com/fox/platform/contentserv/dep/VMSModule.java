package com.fox.platform.contentserv.dep;

import com.fox.platform.contentserv.cfg.ContentProxyConfig;
import com.fox.platform.lib.cbr.cfg.SwitchCircuitBreakerConfig;
import com.fox.platform.lib.cbr.fac.CircuitBreakerFactory;
import com.fox.platform.lib.cbr.fac.CircuitBreakerFactoryImpl;
import com.fox.platform.lib.cfg.EndpointConfig;
import com.fox.platform.lib.fac.WebClientFactory;
import com.fox.platform.lib.fac.WebClientFactoryImpl;
import com.google.inject.AbstractModule;
import io.vertx.core.Vertx;

/**
 * Class to Binder the Dependicies to Repository
 * 
 * @author jorge.garrido
 */
public class VMSModule extends AbstractModule {
  private final ContentProxyConfig vmsProxyConfig;
  private final Vertx vertx;


  public VMSModule(Vertx vertx, ContentProxyConfig vmsProxyConfig) {
    this.vmsProxyConfig = vmsProxyConfig;
    this.vertx = vertx;

  }

  @Override
  protected void configure() {
    bind(Vertx.class).toInstance(vertx);
    bind(ContentProxyConfig.class).toInstance(vmsProxyConfig);
    bind(EndpointConfig.class).toInstance(vmsProxyConfig.getEndpoint());
    bind(WebClientFactory.class).to(WebClientFactoryImpl.class);
    bind(SwitchCircuitBreakerConfig.class).toInstance(vmsProxyConfig.getCircuitBreaker());
    bind(CircuitBreakerFactory.class).to(CircuitBreakerFactoryImpl.class);
  }

}
