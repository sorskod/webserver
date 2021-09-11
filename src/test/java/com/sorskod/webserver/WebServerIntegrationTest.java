package com.sorskod.webserver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.multibindings.ProvidesIntoSet;
import com.sorskod.webserver.annotations.DefaultConnector;
import com.sorskod.webserver.connectors.http.HTTPConnectorModule;
import com.sorskod.webserver.entities.Error;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Aleksandar Babic
 */
public class WebServerIntegrationTest {

  private final static Logger LOGGER = LoggerFactory.getLogger(WebServerIntegrationTest.class);

  static Injector injector;
  static Server server;
  static Client client;

  @BeforeClass
  public static void initialize() throws Exception {
    injector = Guice.createInjector(new TestModule(), new WebServerModule());

    client = ClientBuilder.newClient();
    server = injector.getInstance(Server.class);
    server.start();
  }

  @AfterClass
  public static void tearDown() throws Exception {
    injector.getInstance(Server.class).stop();
  }

  @Test
  public void testGet() throws Exception {
    WebTarget webTarget = client.target(server.getURI().toString());

    Invocation.Builder invocationBuilder
        = webTarget.request(MediaType.APPLICATION_JSON);

    Response response = invocationBuilder.get();
    Map<String, String> entity = response.readEntity(new GenericType<Map<String, String>>() {});

    assertEquals(OK.getStatusCode(), response.getStatus());
    assertEquals("Hello.", entity.get("message"));
  }

  @Test
  public void testError() throws Exception {
    WebTarget webTarget = client.target(server.getURI().toString());
    Invocation.Builder invocationBuilder = webTarget.path("/runtime-error")
        .request(MediaType.APPLICATION_JSON);

    Response response = invocationBuilder.get();
    Map<String, Error> entity = response.readEntity(new GenericType<Map<String, Error>>() {});

    assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), entity.get("error").getCode());
    assertNotNull(entity.get("error").getMessage());
  }

  @Test
  public void testBadRequest() throws Exception {
    WebTarget webTarget = client.target(server.getURI().toString());
    Invocation.Builder invocationBuilder = webTarget.path("/badrequest-error")
        .request(MediaType.APPLICATION_JSON);

    Response response = invocationBuilder.get();
    Map<String, Error> entity = response.readEntity(new GenericType<Map<String, Error>>() {});

    assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
    assertEquals(BAD_REQUEST.getStatusCode(), entity.get("error").getCode());
    assertNotNull(entity.get("error").getMessage());
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Testable application
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  @Singleton
  static class MessengerService {
    String getMessage() {
      return "Hello.";
    }
  }


  static class SubmitBody {
    @JsonProperty
    private String body;

    @JsonCreator
    public SubmitBody(@JsonProperty(value = "body", required = true) String body) {
      this.body = body;
    }
  }

  @Path("/")
  @Produces("application/json")
  @Consumes("application/json")
  public interface RootResource {
    @GET
    Response get();

    @GET
    @Path("/runtime-error")
    Response runtimeError();

    @GET
    @Path("/badrequest-error")
    Response badRequestError();

    @POST
    Response create(SubmitBody body);
  }

  @Singleton
  public static class RootResourceImpl implements RootResource {

    private final MessengerService service;

    @Inject
    public RootResourceImpl(MessengerService service) {
      this.service = service;
    }

    public Response get() {
      return Response.ok(Collections.singletonMap("message", service.getMessage())).build();
    }

    @Override
    public Response runtimeError() {
      throw new RuntimeException("Some random error.");
    }

    @Override
    public Response badRequestError() {
      throw new BadRequestException("Some random BadRequestException.");
    }

    @Override
    public Response create(SubmitBody body) {
      return Response.accepted().entity(body).build();
    }
  }

  private static class TestModule extends AbstractModule {

    @Override
    protected void configure() {
      install(new HTTPConnectorModule());
      bind(RootResource.class).to(RootResourceImpl.class).asEagerSingleton();
    }

    @DefaultConnector
    @Provides
    Configurator configurator() {
      return () -> 0; // random port
    }

    @ProvidesIntoSet
    Feature customFeature() {
      return (context) -> {
        context.register(xPoweredByFilter());
        context.register(requestLoggerFilter());

        return true;
      };
    }

    ContainerResponseFilter xPoweredByFilter() {
      return (req, res) -> res.getHeaders().add("X-Powered-By", "UnitTest-Webserver (1.0-SNAPSHOT)");
    }

    ContainerRequestFilter requestLoggerFilter() {
      return (req) -> LOGGER.info("Request: {}", req.getUriInfo().getRequestUri().getPath());
    }
  }
}
