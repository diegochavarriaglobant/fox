package com.fox.platform.contentserv.infra.serv;

import com.fox.platform.contentserv.infra.handler.HandlerChannel;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;

public class EndPointChannelVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(EndPointChannelVerticle.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new EndPointChannelVerticle(), ar -> {
            if (ar.failed()) {
                ar.cause().printStackTrace();
            }
        });
    }

    @Override
    public void start(Future<Void> fut) {

        try {

            LOG.info("Start Http Server at port : {8081}");

            // Create a router object.
            Router router = Router.router(vertx);

            router.route().handler(BodyHandler.create());
            // Bind "/" to our hello message.
            router.route("/").handler(routingContext -> {
                HttpServerResponse response = routingContext.response();
                response
                        .putHeader("content-type", "text/html")
                        .end("<h1>Hello from my first Vert.x 3 application</h1>");
            });


            router.post("/channel").handler(this::handlePostChannel);

            // Create the HTTP server and pass the "accept" method to the request handler.
            vertx
                    .createHttpServer()
                    .requestHandler(router::accept)
                    .listen(
                            // Retrieve the port from the configuration,
                            // default to 8090.
                            config().getInteger("http.port", 8081)//,
                            //next::handle
                    );

        } catch (final Exception ex) {
            LOG.error("Unable to start operation ", ex);
        }

    }

    /**
     *
     * @param routingContext
     */
    private void handlePostChannel(RoutingContext routingContext) {
        try {
            LOG.info("Connected handlePostChannel ... ");
            vertx.eventBus().send(HandlerChannel.ADDRESS, "Pong", res -> {

               /*if(res.succeeded()) {
                    routingContext.response().setStatusCode(200);
                    routingContext.response().end(res.result().body().toString());*/
                    WebClient client = WebClient.create(vertx);
                    HandlerChannel.handleEventBusResponse(routingContext, client);
                /*}
                else{
                    LOG.error("Unable to handleEventBusResponse operation ", res.cause());
                }*/
            });
        } catch (Exception ex) {
            routingContext.response().setStatusCode(500).putHeader("Content-Type","text/plain").end(ex.getMessage());
            LOG.error("Unable to handlePostChannel operation ", ex);
        }

    }
}
