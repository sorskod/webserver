package com.sorskod.webserver.features;

import com.google.inject.Injector;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Scans for all bindings containing {@link Path}, construct them and return as a list
 *
 * @author Aleksandar Babic
 */
public class BoundWebResources implements Feature {

  private final Injector injector;

  @Inject
  public BoundWebResources(Injector injector) {
    this.injector = injector;
  }

  @Override
  public boolean configure(FeatureContext context) {

    injector.getBindings().entrySet()
        .stream()
        .filter(entry -> entry.getKey().getTypeLiteral().getRawType().getAnnotation(Path.class) != null)
        .map(entry -> injector.getInstance(entry.getKey()))
        .forEach(context::register);

    return true;
  }
}
