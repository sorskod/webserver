package com.sorskod.webserver.mappers;

import com.sorskod.webserver.entities.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Map;

/**
 * @author Aleksandar Babic <salebab@gmail.com>
 */
public interface ErrorableResponse {

  Logger LOGGER = LoggerFactory.getLogger(ErrorableResponse.class);

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
    LOGGER.debug("Error Response");
    return Response.status(status)
        .type(MediaType.APPLICATION_JSON_TYPE)
        .entity(Collections.singletonMap("error", new Error(status.getStatusCode(), message, details)))
        .build();
  }

}
