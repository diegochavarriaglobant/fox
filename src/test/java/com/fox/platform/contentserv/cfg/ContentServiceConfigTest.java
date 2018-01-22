package com.fox.platform.contentserv.cfg;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fox.platform.contentserv.cfg.impl.ContentServiceConfigImpl;
import com.fox.platform.lib.cfg.ConfigLibFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class ContentServiceConfigTest {

  private ContentServiceConfig contentServiceConfig;
  private static Logger logger = LoggerFactory.getLogger(ContentServiceConfigTest.class);
  private static final String URL_DEFAULT_CONFIG = "/default-config.json";

  /**
   * Method that obtains information from default-config.json
   * 
   * @param resourcePath
   * @return
   */
  public JsonObject loadResource(String resourcePath) {
    try {
      InputStream in = this.getClass().getResourceAsStream(resourcePath);
      String jsonFile = IOUtils.toString(in, "UTF-8");
      return new JsonObject(jsonFile);
    } catch (Exception ex) {
      logger.error(ex.getStackTrace().toString());
      return null;
    }
  }

  @Test
  public void loadConfig() {

    JsonObject configFile = loadResource(URL_DEFAULT_CONFIG);
    contentServiceConfig =
        (ContentServiceConfig) ConfigLibFactory.FACTORY.createServiceConfig(configFile,
            ContentServiceConfigImpl.CONFIG_FIELD, ContentServiceConfigImpl.class);

    contentServiceConfig.getAddressProxy();
    assertNotNull(contentServiceConfig);
  }

  @Test
  public void loadConfigFile() {
    JsonObject configFile = loadResource(URL_DEFAULT_CONFIG);
    assertNotNull(configFile);
    assertTrue(!configFile.isEmpty());
  }


}
