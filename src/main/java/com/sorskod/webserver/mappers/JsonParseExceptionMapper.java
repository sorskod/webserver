package com.sorskod.webserver.mappers;

import com.fasterxml.jackson.core.JsonParseException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author Aleksandar Babic
 */
public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException>, ErrorableResponse {

  @Override
  public Response toResponse(JsonParseException exception) {
    return buildResponse(Response.Status.BAD_REQUEST, exception.getOriginalMessage());
  }
}
