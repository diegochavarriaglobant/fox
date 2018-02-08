package com.fox.platform.contentserv.infra.serv;


import java.util.Optional;
import com.fox.platform.contentserv.cfg.impl.ContentServiceConfigImpl;
import com.fox.platform.contentserv.exc.ContentServiceException;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.impl.MimeMapping;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * Class that connect omnix service
 * 
 * @author diego.chavarria
 */
public class EndPointChannelVerticle extends ContentVerticle {

  private static final Logger logger = LoggerFactory.getLogger(EndPointChannelVerticle.class);
  private static final String TEXT_MIMETYPE = MimeMapping.getMimeTypeForExtension("text");
  private static final String HTML_MIMETYPE = MimeMapping.getMimeTypeForExtension("html");
  private static final String JSON_MIMETYPE = MimeMapping.getMimeTypeForExtension("json");
  private String country_id = null;

  @Override
  public void start(Future<Void> future) throws Exception {

    Future<Void> startFuture = Future.future();
    super.start(startFuture);

    startFuture.setHandler(handler -> {
      if (handler.succeeded()) {

        if (logger.isInfoEnabled()) {
          logger.info("Start Content Service Http Server with config: "
              + config().getJsonObject(ContentServiceConfigImpl.CONFIG_FIELD).encodePrettily());
        }

        Router router = getRouter();

        vertx.createHttpServer(contentServiceConfig.getHttpServerOptions())
            .requestHandler(router::accept).listen(result -> {
              if (result.succeeded()) {
                future.complete();
              } else {
                future.fail(result.cause());
              }
            });
      } else {
        future.fail(handler.cause());
      }
    });
  }

  /**
   * Get router with the Endpoint configuration so it can route the request for getEvent and
   * getChannel endpoints
   *
   * @return Router
   */
  private Router getRouter() {
    Router router = Router.router(vertx);

    router.route(contentServiceConfig.getDefaultPath()).handler(routingContext -> {
      HttpServerResponse response = routingContext.response();
      response.putHeader(HttpHeaders.CONTENT_TYPE, HTML_MIMETYPE)
          .end("<h1>Hello from my first Vert.x 3 application</h1>");
    });

    router.route(contentServiceConfig.getDefaultApiMethod(), contentServiceConfig.getApiPath())
        .handler(this::handlePostChannel);


    return router;

  }

  /**
   *
   * @param routingContext
   */
  private void handlePostChannel(RoutingContext routingContext) {
    try {
      logger.info("Connected handlePostChannel ... ");
      country_id = Optional.ofNullable(routingContext.request().getParam("countryId"))
          .orElseThrow(() -> new ContentServiceException("Invalid country Id"));
      JsonObject data = new JsonObject();
      data.put("country", country_id);

      vertx.eventBus().send(contentServiceConfig.getAddressProxy(), data, res -> {

        if (res.succeeded()) {
          routingContext.response().setStatusCode(200)
              .putHeader(HttpHeaders.CONTENT_TYPE, JSON_MIMETYPE)
              .end(new JsonObject(res.result().body().toString()).encodePrettily());
        } else {
          logger.error("Unable to handleEventBusResponse operation ", res.cause());
          routingContext.response().setStatusCode(500)
              .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_MIMETYPE)
              .end(res.cause().getStackTrace().toString());
        }
      });
    } catch (Exception ex) {
      routingContext.response().setStatusCode(500)
          .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_MIMETYPE).end(ex.getMessage());
      logger.error("Unable to handlePostChannel operation ", ex);
    }

  }
}
