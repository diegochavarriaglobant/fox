package com.fox.platform.contentserv.infra.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fox.platform.contentserv.dom.ent.Feed;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.io.IOException;
import java.util.Optional;

public class HandlerChannel {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    public static final String ADDRESS = "omnix_service";
    private static final Logger LOG = LoggerFactory.getLogger(HandlerChannel.class);

    /**
     *
     * @param routingContext
     * @param client
     */
    public static void handleEventBusResponse(RoutingContext routingContext, WebClient client) {

        try {
            HttpServerResponse response = routingContext.response();

            String countryId = Optional
                    .ofNullable(routingContext.request().getParam("countryId"))
                    .orElseThrow(() -> new Exception("Invalid country Id"));



            String jsonObj = "{\"query\":{ \"bool\":{\"must\":[{\"term\": {\"type.description\": \"olympicschannel\"}}," +
                    "{\"nested\": {\"path\": \"groups\",\"query\": {\"bool\": {\"must\": [{\"nested\": {\"path\": \"groups.feeds\",\"query\": " +
                    "{ \"match\": {\"groups.feeds.countryId\": \"" +
                    countryId +
                    "\" } } } }] } }, \"inner_hits\": {" +
                    "} } }]}},\"sort\": [{\"groups.fields.id.raw\": { \"nested_path\": \"groups\", \"order\": \"asc\"}}]}";
            JsonObject jsonObject = new JsonObject(jsonObj);

            // WebClient client = WebClient.create(vertx);
            client.post(443, "search-omnix-services-sh2266ar6ket7lqcnhj3dpzccu.us-east-1.es.amazonaws.com", "/omnix_es/contentObjects/_search")
                    .ssl(true)
                    .putHeader("Content-Length", "0")
                    .putHeader("Content-Type", "application/json")
                    .putHeader("Cache-Control", "no-cache")
                    .sendJson(jsonObject, ar -> {
                        //.send(ar -> {
                        if (ar.succeeded()) {

                            try {
                                HttpResponse<Buffer> data = ar.result();
                                Buffer body = data.body();
                                Feed feed = JSON_MAPPER.readValue(body.getBytes(), Feed.class);
                                JSON_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                                response.end(JSON_MAPPER.writeValueAsString(feed));
                            } catch (IOException e) {
                                LOG.error("Unable to handleEventBusResponse operation", e.getMessage());
                                response.setStatusCode(500).end(e.getMessage());
                            }
                        } else {
                            LOG.error("Unable to handleEventBusResponse operation", ar.cause().getMessage());
                            response.setStatusCode(500).end(ar.cause().getMessage());

                        }
                    });
            // client.close();

        } catch (Exception ex) {
            LOG.error("Unable to handleEventBusResponse operation ", ex);
        }
    }
}
