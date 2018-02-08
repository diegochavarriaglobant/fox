package com.fox.platform.contentserv.infra.serv;

import com.fox.platform.contentserv.dom.ent.Feed;
import com.fox.platform.contentserv.exc.ContentServiceException;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class ProxyChannelVerticle extends ContentVerticle {

  private static final Logger logger = LoggerFactory.getLogger(ProxyChannelVerticle.class);

  private static final String ERROR_MESSAGE = "Unable to onMessage operation";


  @Override
  public void start(Future<Void> future) throws Exception {
    try {
      Future<Void> startFuture = Future.future();
      super.start(startFuture);
      startFuture.setHandler(res -> {
        if (res.succeeded()) {
          vertx.eventBus().consumer(contentServiceConfig.getAddressProxy(), this::onMessage);
          future.complete();
        } else
          future.fail(ERROR_MESSAGE);
      });

    } catch (ContentServiceException e) {
      future.fail(e);
      logger.error(ERROR_MESSAGE, e);
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
      JsonObject jsonObject =
          processQuery(contentServiceConfig.getQueryOmnixService(), message.body());
      client
          .post(contentServiceConfig.getPortOmnixService(),
              contentServiceConfig.getHostOmnixService(), contentServiceConfig.getUriOmnixService())
          .ssl(true).sendJson(jsonObject, response -> {
            if (response.succeeded()) {
              message.reply(processResponse(response.result()));
            } else {
              logger.error("Unable to handleEventBusResponse operation",
                  response.cause().getStackTrace().toString());
              message.fail(response.hashCode(), response.cause().getMessage());
            }
          });
    } catch (Exception ex) {
      logger.error("Unable to onMessage operation ", ex);
      message.fail(ex.hashCode(), ex.getCause().getMessage());
    }
  }

  /**
   * Method that process the Query for the omnix service
   * 
   * @param query
   * @param countryIdParam
   * @return
   */
  public JsonObject processQuery(String query, String countryIdParam) {
    try {
      query = query.replace("{countryParam}", countryIdParam);
      return new JsonObject(query);

    } catch (ContentServiceException ex) {
      logger.error("Unable to processQuery operation ", ex);
      return null;
    }
  }

  /**
   * Method that process the response of omnix service
   * 
   * @param data
   * @return
   */
  public JsonArray processResponse(HttpResponse<Buffer> data) {
    JsonArray responseJson = new JsonArray();
    Feed feed = data.bodyAsJsonObject().mapTo(Feed.class);
    feed.getHits().getHits().stream().forEach(
        hits -> hits.getInnerHits().getGroups().getHits().getHits().stream().forEach(hit -> {
          hit.getSource().getFields().toString();
          responseJson.add(JsonObject.mapFrom(hit.getSource().getFields()));
        }));

    return responseJson;

  }
}
