package com.sorskod.webserver.entities;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Aleksandar Babic
 */
public class ErrorTest {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Test
  public void itShouldSerialize() throws Exception {
    Error error = new Error(404, "Not Found");
    String serialized = OBJECT_MAPPER.writeValueAsString(error);
    Assert.assertEquals("{\"code\":404,\"message\":\"Not Found\"}", serialized);
  }

  @Test
  public void itShouldWrap() throws Exception {
    Error error = new Error(404, "Not Found");
    String serialized = OBJECT_MAPPER.writeValueAsString(error.wrapped());
    Assert.assertEquals("{\"error\":{\"code\":404,\"message\":\"Not Found\"}}", serialized);
  }
}