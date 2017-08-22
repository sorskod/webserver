package com.sorskod.webserver.entities;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

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
    Assert.assertThat(error.getCode(), equalTo(404));
    Assert.assertThat(error.getMessage(), equalTo("Not Found"));
  }

  @Test
  public void itShouldDeserializeWithDetails() throws Exception {
    String errorJson = "{\"code\":404,\"message\":\"Not Found\",\"details\":{\"value\":1}}";
    Error error = OBJECT_MAPPER.readValue(errorJson, Error.class);

    Assert.assertThat(error.getDetails(), notNullValue());
    Assert.assertThat(error.getDetails().get("value"), equalTo(1));
  }
}