package com.sorskod.webserver.inject;

import com.google.inject.Injector;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.internal.inject.InjectionManagerFactory;

/**
 * @author Aleksandar Babic
 */
public class GuiceInjectionManagerFactory implements InjectionManagerFactory {
  @Override
  public InjectionManager create(Object injector) {
    return new GuiceInjectionManager((Injector) injector);
  }
}
