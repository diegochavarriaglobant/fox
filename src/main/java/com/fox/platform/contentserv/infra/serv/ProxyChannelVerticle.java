package com.fox.platform.contentserv.infra.serv;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class ProxyChannelVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(ProxyChannelVerticle.class);

    @Override
    public void start() throws InterruptedException {

        vertx.eventBus().registerHandler("default.address", new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> message) {
                message.reply("Test");
                ws.writeTextFrame(message.body().toString()); // Echo it back twice
                container.logger().info("Received: " + message.body().toString());
            }
        });

        vertx.eventBus().consumer("com.makingdevs.ping", message ->{
                LOG.info("Message Received:", message.body());
                message.reply("ping");
        });

    }


}
