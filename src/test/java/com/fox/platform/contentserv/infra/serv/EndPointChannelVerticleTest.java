package com.fox.platform.contentserv.infra.serv;

import com.fox.platform.contentserv.cfg.ContentServiceConfig;
import com.fox.platform.contentserv.cfg.impl.ContentServiceConfigImpl;
import com.fox.platform.lib.cfg.ConfigLibFactory;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;

public class EndPointChannelVerticleTest {

    private ContentServiceConfig contentServiceConfig;

    private static Logger logger = LoggerFactory.getLogger(EndPointChannelVerticleTest.class);

    private static final String URL_DEFAULT_CONFIG = "/default-config.json";

    @Test
    public void loadConfigFile(){
        JsonObject configFile = loadResource(URL_DEFAULT_CONFIG);
        assertNotNull(configFile);
        assertTrue(!configFile.isEmpty());
    }

    @Test
    public void loadConfig(){

        JsonObject configFile = loadResource(URL_DEFAULT_CONFIG);
        contentServiceConfig = (ContentServiceConfig) ConfigLibFactory.FACTORY.createServiceConfig(
                configFile,
                ContentServiceConfigImpl.CONFIG_FIELD,
                ContentServiceConfigImpl.class
        );
        assertNotNull(contentServiceConfig);
    }


    private JsonObject loadResource(String resourcePath){
        try{
            InputStream in = this.getClass().getResourceAsStream(resourcePath);
            String jsonFile =  IOUtils.toString(in,"UTF-8");
            return new JsonObject(jsonFile);
        } catch(Exception ex){
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }


}
