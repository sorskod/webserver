package com.sorskod.webserver.mappers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Collections;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * @author Aleksandar Babic <salebab@gmail.com>
 */
public class JsonMappingExceptionMapper implements ExceptionMapper<JsonMappingException>, ErrorableResponse {
  @Override
  public Response toResponse(JsonMappingException exception) {

    if(exception instanceof UnrecognizedPropertyException) {
      return buildResponse(BAD_REQUEST, "Unrecognized property.", Collections.singletonMap("property_name", ((UnrecognizedPropertyException) exception).getPropertyName()));
    }

    return buildResponse(BAD_REQUEST, "Mapping error - Some fields are missing.");
  }
}
