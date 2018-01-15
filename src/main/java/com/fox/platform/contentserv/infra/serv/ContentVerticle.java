package com.fox.platform.contentserv.infra.serv;

import com.fox.platform.contentserv.cfg.ContentServiceConfig;
import com.fox.platform.contentserv.cfg.impl.ContentServiceConfigImpl;
import com.fox.platform.lib.cfg.ConfigLibFactory;
import com.fox.platform.lib.vrt.AbstractConfigurationVerticle;
import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentVerticle extends AbstractConfigurationVerticle {

    private static final Logger logger = LoggerFactory.getLogger(ContentVerticle.class);

    protected ContentServiceConfig contentServiceConfig;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        contentServiceConfig = (ContentServiceConfig) ConfigLibFactory.FACTORY.createServiceConfig(
                config(),
                ContentServiceConfigImpl.CONFIG_FIELD,
                ContentServiceConfigImpl.class
        );

        super.start(startFuture);
    }
}
