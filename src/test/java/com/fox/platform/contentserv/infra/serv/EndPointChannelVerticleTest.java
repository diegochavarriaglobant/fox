package com.fox.platform.contentserv.infra.serv;

import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.net.ServerSocket;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fox.platform.contentserv.cfg.ContentServiceConfig;
import com.fox.platform.contentserv.cfg.ContentServiceConfigTest;
import com.fox.platform.contentserv.cfg.impl.ContentServiceConfigImpl;
import com.fox.platform.lib.cfg.ConfigLibFactory;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.impl.MimeMapping;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

@RunWith(VertxUnitRunner.class)
public class EndPointChannelVerticleTest {

  private ContentServiceConfig contentServiceConfig;
  private static final String JSON_MIMETYPE = MimeMapping.getMimeTypeForExtension("json");
  private static Logger logger = LoggerFactory.getLogger(EndPointChannelVerticleTest.class);
  private static final String URL_DEFAULT_CONFIG = "/default-config.json";
  private static final String REQUEST_URL = "localhost";
  private static final String ERROR_MESSAGE = "Deployment failed!";

  private ContentServiceConfigTest contentServiceConfigTest = new ContentServiceConfigTest();

  Vertx vertx;
  int port;

  @Before
  public void before(TestContext context) throws IOException {
    ServerSocket socket = new ServerSocket(8081);
    port = socket.getLocalPort();
    socket.close();
    JsonObject configFile = contentServiceConfigTest.loadResource(URL_DEFAULT_CONFIG);
    DeploymentOptions options = new DeploymentOptions().setConfig(configFile);

    vertx = Vertx.vertx();
    vertx.deployVerticle(EndPointChannelVerticle.class.getName(), options, result -> {
      if (result.succeeded()) {
        vertx.deployVerticle(ProxyChannelVerticle.class.getName(), options, res -> {
          if (result.succeeded()) {
            context.async().complete();
          } else
            logger.error(ERROR_MESSAGE, result.cause());
        });
        context.async().complete();
      } else
        logger.error(ERROR_MESSAGE, result.cause());
    });
  }

  @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void callEndPointChannelVerticle(TestContext context) {
    Async async = context.async();
    HttpClient client = vertx.createHttpClient();
    client.getNow(port, REQUEST_URL, "/", response -> {
      response.bodyHandler(body -> {
        context.assertEquals("<h1>Hello from my first Vert.x 3 application</h1>", body.toString());
        client.close();
        async.complete();
      });
    });
  }

  @Test
  public void callEndPointChannelVerticlePath(TestContext context) {
    Async async = context.async();
    WebClient client = WebClient.create(vertx);
    client.post(port, REQUEST_URL, "/channel?countryId=MX").ssl(false)
        .putHeader(HttpHeaders.CONTENT_TYPE.toString(), JSON_MIMETYPE).send(response -> {
          if (response.succeeded()) {
            HttpResponse<Buffer> data = response.result();
            context.assertNotNull(data.body().toJsonObject());
            client.close();
            async.complete();
          }
        });
  }


  @Test
  public void proxyChannelVerticleProcess() throws Exception {
    ProxyChannelVerticle verticle = new ProxyChannelVerticle();
    JsonObject configFile = contentServiceConfigTest.loadResource(URL_DEFAULT_CONFIG);
    contentServiceConfig =
        (ContentServiceConfig) ConfigLibFactory.FACTORY.createServiceConfig(configFile,
            ContentServiceConfigImpl.CONFIG_FIELD, ContentServiceConfigImpl.class);
    JsonObject jsonObject =
        verticle.processQuery(contentServiceConfig.getQueryOmnixService(), "MX");
    assertNotNull(jsonObject);

  }


}
