package com.sorskod.webserver.providers;

import com.google.inject.servlet.GuiceFilter;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.EnumSet;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.DispatcherType;

/**
 * @author Aleksandar Babic
 */
public class ServletContextHandlerProvider implements Provider<Handler> {

  private final ServletHolder servletHolder;

  @Inject
  public ServletContextHandlerProvider(ServletHolder servletHolder) {
    this.servletHolder = servletHolder;
  }

  public Handler get() {
    ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
    handler.setContextPath("/");
    handler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
    handler.addServlet(servletHolder, "/*");

    return handler;
  }
}
