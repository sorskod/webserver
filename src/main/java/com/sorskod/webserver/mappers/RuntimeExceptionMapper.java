package com.sorskod.webserver.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author Aleksandar Babic <salebab@gmail.com>
 */
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException>, ErrorableResponse {
  @Override
  public Response toResponse(RuntimeException exception) {
    return buildResponse(Response.Status.INTERNAL_SERVER_ERROR, exception.getMessage());
  }
}
