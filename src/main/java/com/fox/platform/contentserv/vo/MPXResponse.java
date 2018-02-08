package com.fox.platform.contentserv.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.circuitbreaker.CircuitBreakerState;
import io.vertx.core.json.JsonObject;


public class MPXResponse {

  protected JsonObject response;
  private CircuitBreakerState circuitBreakerState;
  private boolean responseWithError;
  private String errorMessage;


  public MPXResponse(JsonObject response) {
    this.response = response;
  }

  public MPXResponse() {
    circuitBreakerState = CircuitBreakerState.CLOSED;
  }

  public MPXResponse(CircuitBreakerState circuitBreakerState, boolean isError,
      String errorMessage) {
    this.circuitBreakerState = circuitBreakerState;
    this.responseWithError = isError;
    this.errorMessage = errorMessage;
  }

  public JsonObject toJson() {
    return JsonObject.mapFrom(this);
  }

  @JsonIgnore
  public boolean isCircuitBreakerClose() {
    return circuitBreakerState == CircuitBreakerState.CLOSED;
  }

  @JsonInclude(JsonInclude.Include.NON_NULL)
  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public void setCircuitState(CircuitBreakerState state) {
    circuitBreakerState = state;
  }

  public CircuitBreakerState getCircuitState() {
    return circuitBreakerState;
  }

  public void setResponseWithError(boolean responseWithError) {
    this.responseWithError = responseWithError;
  }

  public boolean getResponseWithError() {
    return responseWithError;
  }

  @JsonInclude(JsonInclude.Include.NON_NULL)
  public JsonObject getResponse() {
    return response;
  }



}
