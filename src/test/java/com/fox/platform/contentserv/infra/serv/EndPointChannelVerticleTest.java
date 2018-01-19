package com.fox.platform.contentserv.infra.serv;

import com.fox.platform.contentserv.cfg.ContentServiceConfig;
import com.fox.platform.contentserv.cfg.impl.ContentServiceConfigImpl;
import com.fox.platform.lib.cfg.ConfigLibFactory;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import io.vertx.ext.unit.TestContext;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

@RunWith(VertxUnitRunner.class)
public class EndPointChannelVerticleTest {

    private ContentServiceConfig contentServiceConfig;

    private static Logger logger = LoggerFactory.getLogger(EndPointChannelVerticleTest.class);

    private static final String URL_DEFAULT_CONFIG = "/default-config.json";

    Vertx vertx;
    int port;

    @Before
    public void before(TestContext context) throws IOException {
        ServerSocket socket = new ServerSocket(8081);
        port = socket.getLocalPort();
        socket.close();
        JsonObject configFile = loadResource(URL_DEFAULT_CONFIG);
        DeploymentOptions options = new DeploymentOptions()
                .setConfig(configFile);

        vertx = Vertx.vertx();
        vertx.deployVerticle(EndPointChannelVerticle.class.getName(), options, context.asyncAssertSuccess());
        vertx.deployVerticle(ProxyChannelVerticle.class.getName(), options);
    }

    @After
    public void after(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void callEndPointChannelVerticle(TestContext context) {
        Async async = context.async();
        HttpClient client = vertx.createHttpClient();
        client.getNow(port, "localhost", "/", response -> {
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
        WebClient client =  WebClient.create(vertx);
        client.post(port, "localhost", "/channel?countryId=MX")
                .ssl(false)
                .putHeader("Content-Length", "0")
                .putHeader("Content-Type", "application/json")
                .putHeader("Cache-Control", "no-cache")
                .send(response -> {
            if (response.succeeded()) {
                HttpResponse<Buffer> data = response.result();
                context.assertNotNull(data.bodyAsJsonObject());
            }
            client.close();
            async.complete();
        });
    }


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

        contentServiceConfig.getAddressProxy();
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
    
    @Test
    public void proxyChannelVerticleProcess() throws Exception{
    	
    	
    	ProxyChannelVerticle verticle = new ProxyChannelVerticle();
    	
    	 JsonObject configFile = loadResource(URL_DEFAULT_CONFIG);
         contentServiceConfig = (ContentServiceConfig) ConfigLibFactory.FACTORY.createServiceConfig(
                 configFile,
                 ContentServiceConfigImpl.CONFIG_FIELD,
                 ContentServiceConfigImpl.class
         );
      	JsonObject jsonObject = verticle.processQuery(contentServiceConfig.getQueryOmnixService(), "MX");
        assertNotNull(jsonObject);
        
    }


}
