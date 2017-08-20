package com.sorskod.webserver.providers;

import com.google.inject.Provider;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.inject.Inject;

/**
 * @author Aleksandar Babic
 */
public class ServletHolderProvider implements Provider<ServletHolder> {

  private final ResourceConfig resourceConfig;

  @Inject
  public ServletHolderProvider(ResourceConfig resourceConfig) {
    this.resourceConfig = resourceConfig;
  }


  public ServletHolder get() {
    ServletHolder servletHolder = new ServletHolder(new ServletContainer(resourceConfig));
    servletHolder.setInitOrder(0);
    return servletHolder;
  }
}
