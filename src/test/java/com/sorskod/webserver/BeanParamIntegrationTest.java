package com.sorskod.webserver;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.sorskod.webserver.annotations.DefaultConnector;
import com.sorskod.webserver.connectors.http.HTTPConnectorModule;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Aleksandar Babic
 */
public class BeanParamIntegrationTest {

  static Injector injector;
  static Server server;
  static Client client;

  public static class Filter {
    @QueryParam("author") String author;
  }

  @Path("/")
  public static class BookResource {
    @GET
    @Path("/book")
    public Response getBook(@BeanParam Filter filter) {
      Map<String, String> book = new HashMap<>();
      book.put("name", "Java Performance");
      book.put("author", "Scott Oaks");
      return Response.ok().entity(book).build();
    }
  }

  @BeforeClass
  public static void initialize() throws Exception {
    injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(BookResource.class);
      }

      @DefaultConnector
      @Provides
      Configurator configurator() {
        return () -> 0; // port
      }
    }, new WebServerModule(), new HTTPConnectorModule());

    client = ClientBuilder.newClient();
    server = injector.getInstance(Server.class);
    server.start();
  }

  @AfterClass
  public static void tearDown() throws Exception {
    injector.getInstance(Server.class).stop();
  }

  @Test
  public void testBeanParameter() throws Exception {
    WebTarget webTarget = client.target(server.getURI().toString());
    Invocation.Builder invocationBuilder = webTarget.path("/book")
        .queryParam("author", "Scott Oaks")
        .request(MediaType.APPLICATION_JSON);

    Response response = invocationBuilder.get();
    Map<String, String> entity = response.readEntity(new GenericType<Map<String, String>>() {});

    assertThat(response.getStatus(), equalTo(OK.getStatusCode()));
    assertThat(entity.get("author"), equalTo("Scott Oaks"));
  }
}
