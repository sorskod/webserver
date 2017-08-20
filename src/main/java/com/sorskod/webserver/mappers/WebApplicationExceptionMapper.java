package com.sorskod.webserver.mappers;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author Aleksandar Babic <salebab@gmail.com>
 */
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException>, ErrorableResponse {

  @Override
  public Response toResponse(WebApplicationException exception) {
    return buildResponse(Response.Status.fromStatusCode(exception.getResponse().getStatus()), exception.getMessage());
  }
}
