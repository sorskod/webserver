package com.sorskod.webserver.inject;

import org.glassfish.jersey.process.internal.RequestContext;
import org.glassfish.jersey.process.internal.RequestScope;

/**
 * @author Aleksandar Babic
 */
public class GuiceRequestScope extends RequestScope {
  @Override
  public RequestContext createContext() {
    return new GuiceRequestContext();
  }
}
