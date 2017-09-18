package com.sorskod.webserver.providers;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletProperties;

import java.util.EnumSet;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.DispatcherType;

/**
 * @author Aleksandar Babic
 */
public class ServletContextHandlerProvider implements Provider<Handler> {

  private final ServletHolder servletHolder;
  private final Injector injector;

  @Inject
  public ServletContextHandlerProvider(ServletHolder servletHolder, Injector injector) {
    this.servletHolder = servletHolder;
    this.injector = injector;
  }

  public Handler get() {
    ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
    handler.setContextPath("/");
    handler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
    handler.addServlet(servletHolder, "/*");
    handler.setAttribute(ServletProperties.SERVICE_LOCATOR, injector);
    return handler;
  }
}
