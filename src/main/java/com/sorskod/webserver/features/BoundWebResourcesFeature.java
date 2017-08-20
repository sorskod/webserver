package com.sorskod.webserver.features;

import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Scans for all bindings containing {@link Path}, instates and registers each in Jersey context.
 *
 * @author Aleksandar Babic
 */
public class BoundWebResourcesFeature implements Feature {

  private final static Logger LOGGER = LoggerFactory.getLogger(BoundWebResourcesFeature.class);
  private final Injector injector;

  @Inject
  public BoundWebResourcesFeature(Injector injector) {
    this.injector = injector;
  }

  @Override
  public boolean configure(FeatureContext context) {

    injector.getBindings().entrySet()
        .stream()
        .filter(entry -> entry.getKey().getTypeLiteral().getRawType().getAnnotation(Path.class) != null)
        .map(entry -> injector.getInstance(entry.getKey()))
        .peek(resource -> LOGGER.info("Register resource: {}", resource))
        .forEach(context::register);

    return true;
  }
}
