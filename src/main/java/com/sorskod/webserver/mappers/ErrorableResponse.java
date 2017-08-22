package com.sorskod.webserver.mappers;

import com.sorskod.webserver.entities.Error;

import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Aleksandar Babic <salebab@gmail.com>
 */
public interface ErrorableResponse {

  /**
   * Builds an error response from status and message.
   *
   * @param status  Response status
   * @param message Error message
   * @return response
   */
  default Response buildResponse(Response.Status status, String message) {
    return buildResponse(status, message, null);
  }

  /**
   * Builds an error response from status, message with details
   *
   * @param status  Response status
   * @param message Error message
   * @param details Details as Map
   * @return response
   */
  default Response buildResponse(Response.Status status, String message, Map<String, Object> details) {
    return Response.status(status)
        .type(MediaType.APPLICATION_JSON_TYPE)
        .entity(new Error(status.getStatusCode(), message, details).wrapped())
        .build();
  }
}
