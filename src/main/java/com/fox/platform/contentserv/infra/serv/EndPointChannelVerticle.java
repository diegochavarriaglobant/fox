package com.fox.platform.contentserv.infra.serv;


import com.fox.platform.contentserv.cfg.impl.ContentServiceConfigImpl;
import com.fox.platform.contentserv.exc.ContentServiceException;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.util.Optional;

/**
 * Class that connect omnix service
 * @author diego.chavarria
 */
public class EndPointChannelVerticle extends ContentVerticle  {

    private static final Logger logger = LoggerFactory.getLogger(EndPointChannelVerticle.class);

    @Override
    public void start(Future<Void> future) throws Exception {

        Future<Void> startFuture = Future.future();
        super.start(startFuture);

        startFuture.setHandler(handler -> {
            if (handler.succeeded()) {

                if (logger.isInfoEnabled()) {
                    logger.info("Start Content Service Http Server with config: " + config()
                            .getJsonObject(ContentServiceConfigImpl.CONFIG_FIELD).encodePrettily());
                }

                Router router = getRouter();

                vertx.createHttpServer(contentServiceConfig.getHttpServerOptions())
                        .requestHandler(router::accept)
                        .listen(
                                result -> {
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
     * Get router with the Endpoint configuration so it can route the request
     * for getEvent and getChannel endpoints
     *
     * @return Router
     */
    private Router getRouter(){
        Router router = Router.router(vertx);

        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("<h1>Hello from my first Vert.x 3 application</h1>");
        });

        router.post(contentServiceConfig.getApiPath())
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
            String countryId = Optional
                    .ofNullable(routingContext.request().getParam("countryId"))
                    .orElseThrow(() -> new ContentServiceException("Invalid country Id"));

            vertx.eventBus().send(contentServiceConfig.getAddressProxy(), countryId, res -> {

               if(res.succeeded()) {
                    routingContext.response().setStatusCode(200);
                    routingContext.response().end(res.result().body().toString());
                }
                else{
                   logger.error("Unable to handleEventBusResponse operation ", res.cause());
                   routingContext.response().setStatusCode(500).putHeader("Content-Type","text/plain").end(res.cause().getStackTrace().toString());
                }
            });
        } catch (Exception ex) {
            routingContext.response().setStatusCode(500).putHeader("Content-Type","text/plain").end(ex.getMessage());
            logger.error("Unable to handlePostChannel operation ", ex);
        }

    }
}
