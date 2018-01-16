package com.fox.platform.contentserv.infra.serv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fox.platform.contentserv.dom.ent.Feed;
import com.fox.platform.contentserv.exc.ContentServiceException;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class ProxyChannelVerticle extends ContentVerticle {
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(ProxyChannelVerticle.class);

    @Override
    public void start(Future<Void> future) throws Exception {

        try {
            Future<Void> startFuture = Future.future();
            super.start(startFuture);
            vertx.eventBus().consumer(contentServiceConfig.getAddressProxy(), this::onMessage);
            future.complete();
        } catch (ContentServiceException e) {
            future.complete();
            logger.error("Unable to onMessage operation ", e.getStackTrace());
        }


    }

    /**
     * Handler method to define the operation to the verticle
     *
     * @param message
     */
    public void onMessage(Message<String> message) {

        try {
            WebClient client = WebClient.create(vertx);
            JsonObject jsonObject = processQuery(contentServiceConfig.getQueryOmnixService(), message.body());
            client.post(contentServiceConfig.getPortOmnixService(), contentServiceConfig.getHostOmnixService(), contentServiceConfig.getUriOmnixService())
                    .ssl(true)
                    .putHeader("Content-Length", "0")
                    .putHeader("Content-Type", "application/json")
                    .putHeader("Cache-Control", "no-cache")
                    .sendJson(jsonObject, response -> {
                        if (response.succeeded()) {

                            HttpResponse<Buffer> data = response.result();
                            Feed feed = data.bodyAsJsonObject().mapTo(Feed.class);
                            message.reply(JsonObject.mapFrom(feed.getHits()));

                        } else {
                            logger.error("Unable to handleEventBusResponse operation", response.cause().getStackTrace());
                            message.fail(response.hashCode(), response.cause().getMessage());

                        }
                    });
        } catch (ContentServiceException e) {
            message.fail(e.hashCode(), e.getCause().getMessage());
            logger.error("Unable to onMessage operation ", e.getStackTrace());
        } catch (Exception ex) {
            logger.error("Unable to onMessage operation ", ex.getStackTrace());
            message.fail(ex.hashCode(), ex.getCause().getMessage());
        }

    }

    public JsonObject processQuery(String query, String countryIdParam) {
        try {
            query = query.replace("{countryParam}", countryIdParam);
            return new JsonObject(query);

        } catch (ContentServiceException ex) {
            logger.error("Unable to processQuery operation ", ex.getStackTrace());
            return null;
        }
    }
}
