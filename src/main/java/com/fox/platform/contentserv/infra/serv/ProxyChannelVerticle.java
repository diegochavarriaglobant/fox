package com.fox.platform.contentserv.infra.serv;

import com.fox.platform.contentserv.cfg.ContentProxyConfig;
import com.fox.platform.contentserv.dep.VMSModule;
import com.fox.platform.contentserv.dom.ent.Feed;
import com.fox.platform.contentserv.exc.ContentServiceException;
import com.fox.platform.contentserv.vo.MPXResponse;
import com.fox.platform.lib.cbr.SwitchCircuitBreaker;
import com.fox.platform.lib.cbr.exc.KillswitchException;
import com.fox.platform.lib.cbr.fac.CircuitBreakerFactory;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import io.vertx.circuitbreaker.CircuitBreakerState;
import io.vertx.core.AsyncResult;
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
  protected ContentProxyConfig proxyVMSConfig;
  private SwitchCircuitBreaker circuitBreaker;
  private WebClient client;
  @Inject
  private CircuitBreakerFactory circuitBreakerFactory;

  @Override
  public void start(Future<Void> future) throws Exception {
    try {
      Future<Void> startFuture = Future.future();
      super.start(startFuture);
      startFuture.setHandler(handler -> {
        if (handler.succeeded()) {
          proxyVMSConfig = contentServiceConfig.getProxyVMS();
          logger.info(
              "Starting ProxyVerticle with config: " + JsonObject.mapFrom(proxyVMSConfig).encode());

          Guice.createInjector(new VMSModule(vertx, proxyVMSConfig)).injectMembers(this);
          circuitBreaker = circuitBreakerFactory.createCircuitBreaker();

          vertx.eventBus().<JsonObject>consumer(contentServiceConfig.getAddressProxy(),
              request -> circuitBreaker
                  .<MPXResponse>executeWithKillswitch(
                      circuitFuture -> executeCall(circuitFuture, request),
                      this::replyOnCircuitOpen, this::replyOnKillswitch)
                  .setHandler(result -> proccessResponse(result, request)));

          future.complete();

        } else {
          logger.error(deploymentID() + " Error launching ProxyChannelVerticle: "
              + handler.cause().getMessage(), handler.cause());
          future.fail(handler.cause());
        }


      });

    } catch (ContentServiceException e) {
      future.fail(e);
      logger.error(ERROR_MESSAGE, e);
    }


  }

  @Override
  public void onConfigChange() {
    super.onConfigChange();
    if (circuitBreaker.getOptions().isKillswitch() != proxyVMSConfig.getCircuitBreaker()
        .isKillswitch()) {
      circuitBreaker.getOptions().setKillswitch(proxyVMSConfig.getCircuitBreaker().isKillswitch());
    }
  }

  /**
   * Perform the request to MPX endpoint uses Vert.x Web Client
   * 
   * @param future object with the result of the call
   * @param request Message receives through event bus
   */
  @Trace(dispatcher = true, async = true)
  private void executeCall(Future<MPXResponse> future, Message<JsonObject> request) {
    try {

      JsonObject data = request.body();
      client = WebClient.create(vertx);
      JsonObject jsonObject =
          processQuery(contentServiceConfig.getQueryOmnixService(), data.getString("country"));
      client
          .post(contentServiceConfig.getPortOmnixService(),
              contentServiceConfig.getHostOmnixService(), contentServiceConfig.getUriOmnixService())
          .ssl(true).sendJson(jsonObject, response -> {
            if (response.succeeded()) {
              request.reply(processResponse(response.result()));
            } else {
              logger.error("Unable to handleEventBusResponse operation",
                  response.cause().getStackTrace().toString());
              request.fail(response.hashCode(), response.cause().getMessage());
            }
          });

    } catch (Exception ex) {
      logger.error("Unable to onMessage operation ", ex);
      request.fail(ex.hashCode(), ex.getCause().getMessage());
    }

  }

  /**
   * Make a MPXResponse when the Circuit Breaker is opened, this reply send an error
   * 
   * @param openCircuitError Fail that causes the Circuit Breaker go to open state
   * @return MPXResponse object, with an error and says that the circuit is open
   */
  private MPXResponse replyOnCircuitOpen(Throwable openCircuitError) {

    NewRelic.incrementCounter(
        "ContentService/ProxyMPX/Counter/Fallback/" + circuitBreaker.state().toString());
    logger.error("Circuit on open state: " + openCircuitError.getClass().getName(),
        openCircuitError);

    MPXResponse temporalStatus =
        new MPXResponse(circuitBreaker.state(), true, openCircuitError.getMessage());

    if (logger.isDebugEnabled()) {
      logger.debug("Create Open Circuit reply : " + temporalStatus.toJson().toString());
    }

    return temporalStatus;
  }

  private MPXResponse replyOnKillswitch() {
    return replyOnCircuitOpen(new KillswitchException());
  }

  /**
   * Build and send a reply to Event Bus with the search response of MPX
   * 
   * @param result response from MPX or Open Circuit Breaker
   * @param request Original message to do the reply
   */
  private void proccessResponse(AsyncResult<MPXResponse> result, Message<JsonObject> request) {
    if (result.succeeded()) {
      MPXResponse mpxResponse = result.result();
      request.reply(mpxResponse.toJson());
    } else {
      MPXResponse mpxError =
          new MPXResponse(CircuitBreakerState.CLOSED, true, result.cause().getMessage());
      request.reply(mpxError.toJson());
    }


  }

  /**
   * Handler method to define the operation to the verticle
   *
   * @param message
   */
  public void onMessage(Message<String> message) {

    try {

      client = WebClient.create(vertx);
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
  public JsonObject processResponse(HttpResponse<Buffer> data) {
    JsonObject responseJson = new JsonObject();
    JsonArray dataJson = new JsonArray();
    Feed feed = data.bodyAsJsonObject().mapTo(Feed.class);
    feed.getHits().getHits().stream().forEach(
        hits -> hits.getInnerHits().getGroups().getHits().getHits().stream().forEach(hit -> {
          hit.getSource().getFields().toString();
          dataJson.add(JsonObject.mapFrom(hit.getSource().getFields()));
        }));

    return responseJson.put("response", dataJson);

  }
}
