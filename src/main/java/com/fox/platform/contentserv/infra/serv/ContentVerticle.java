package com.fox.platform.contentserv.infra.serv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fox.platform.contentserv.cfg.ContentServiceConfig;
import com.fox.platform.contentserv.cfg.impl.ContentServiceConfigImpl;
import com.fox.platform.lib.cfg.ConfigLibFactory;
import com.fox.platform.lib.vrt.AbstractConfigurationVerticle;
import io.vertx.core.Future;

/**
 * Class that extended AbstractConfiguration Vertical and it loads the initial configuration of the
 * Json file.
 * 
 * @author diego.chavarria
 *
 */
public class ContentVerticle extends AbstractConfigurationVerticle {

  private static final Logger logger = LoggerFactory.getLogger(ContentVerticle.class);

  protected ContentServiceConfig contentServiceConfig;

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    logger.info("Loading initial configuration ...");
    contentServiceConfig =
        (ContentServiceConfig) ConfigLibFactory.FACTORY.createServiceConfig(config(),
            ContentServiceConfigImpl.CONFIG_FIELD, ContentServiceConfigImpl.class);

    super.start(startFuture);
  }
}
