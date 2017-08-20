package com.sorskod.webserver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.multibindings.ProvidesIntoSet;
import com.sorskod.webserver.annotations.DefaultConnector;
import com.sorskod.webserver.annotations.SecureConnector;
import com.sorskod.webserver.connectors.http.HTTPConnectorModule;
import com.sorskod.webserver.connectors.https.HTTPSConnectorModule;
import org.eclipse.jetty.server.Server;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.Response;
import java.util.Collections;

/**
 * @author Aleksandar Babic
 */
public class WebServerModuleTest {

  private final static Logger LOGGER = LoggerFactory.getLogger(WebServerModuleTest.class);

  Injector injector;

  @Before
  public void initialize() throws Exception {
    injector = Guice.createInjector(new WebServerModule(), new TestModule());
  }

  @Test
  public void testWebServerStart() throws Exception {
    injector.getInstance(Server.class).start();
    injector.getInstance(Server.class).stop();
  }


  public static void main(String[] args) throws Exception {
    WebServerModuleTest test = new WebServerModuleTest();

    test.initialize();
    test.injector.getInstance(Server.class).start();
  }


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

  private class TestModule extends AbstractModule {

    @Override
    protected void configure() {
      install(new HTTPConnectorModule());
      install(new HTTPSConnectorModule());

      bind(RootResource.class).to(RootResourceImpl.class).asEagerSingleton();
    }

    @DefaultConnector
    @Provides
    Configurator configurator() {
      return new Configurator() {
        @Override
        public int getPort() {
          return 8090;
        }
      };
    }

    @SecureConnector
    @Provides
    Configurator secureConfigurator() {
      return new Configurator() {
        @Override
        public int getPort() {
          return 8091;
        }
      };
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
      return (req, res) -> res.getHeaders().add("X-Powered-By", "UnitTest Webserver");
    }

    ContainerRequestFilter requestLoggerFilter() {
      return (req) -> LOGGER.info("Request: {}", req.getUriInfo().getRequestUri().getPath());
    }
  }
}
