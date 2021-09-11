package com.sorskod.webserver.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

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

  @Test
  public void itShouldDeserialize() throws Exception {
    String errorJson = "{\"code\":404,\"message\":\"Not Found\"}";
    Error error = OBJECT_MAPPER.readValue(errorJson, Error.class);
    Assert.assertEquals(404, error.getCode());
    Assert.assertEquals("Not Found", error.getMessage());
  }

  @Test
  public void itShouldDeserializeWithDetails() throws Exception {
    String errorJson = "{\"code\":404,\"message\":\"Not Found\",\"details\":{\"value\":1}}";
    Error error = OBJECT_MAPPER.readValue(errorJson, Error.class);

    Assert.assertNotNull(error.getDetails());
    Assert.assertEquals(1, error.getDetails().get("value"));
  }
}