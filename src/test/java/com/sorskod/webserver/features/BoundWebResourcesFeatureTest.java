package com.sorskod.webserver.features;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.Response;

/**
 * @author Aleksandar Babic
 */
public class BoundWebResourcesFeatureTest {

  private Injector injector;
  private FeatureContext featureContext;

  @Before
  public void setUp() throws Exception {
    injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(Resource1.class);
        bind(Resource2.class);
      }
    });

    featureContext = Mockito.mock(FeatureContext.class);
    Mockito.when(featureContext.register(Matchers.any(Object.class))).thenReturn(featureContext);
  }

  @Test
  public void configure() throws Exception {
    Feature feature = new BoundWebResourcesFeature(injector);

    feature.configure(featureContext);

    Mockito.verify(featureContext, Mockito.times(2)).register(Matchers.any(Object.class));
  }


  @Path("/res1")
  private static class Resource1 {
    @GET
    public Response get() { return Response.ok().build(); }
  }

  @Path("/res2")
  private static class Resource2 {
    @GET
    public Response get() { return Response.ok().build(); }
  }
}