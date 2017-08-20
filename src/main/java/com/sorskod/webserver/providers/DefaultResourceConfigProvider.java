package com.sorskod.webserver.providers;

import com.google.inject.Injector;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.Feature;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Default RC provider is about to instantiate {@link ResourceConfig}
 * with all Guice bound JAX-RS {@link Feature} instances
 *
 * @author Aleksandar Babic
 */
public class DefaultResourceConfigProvider implements Provider<ResourceConfig> {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultResourceConfigProvider.class);

  private final Set<Feature> features;
  private final Set<Class<? extends Feature>> featureClasses;
  private final Injector injector;

  @Inject
  public DefaultResourceConfigProvider(Set<Feature> features,
                                       Set<Class<? extends Feature>> featureClasses,
                                       Injector injector) {
    this.features = features;
    this.featureClasses = featureClasses;
    this.injector = injector;
  }

  @Override
  public ResourceConfig get() {
    ResourceConfig config = new ResourceConfig();

    // Still considering to disable auto discovery...
    //config.property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);

    Stream.concat(features.stream(), featureClasses.stream().map(injector::getInstance))
        .peek(f -> LOGGER.info("Register feature: {}", f))
        .forEach(config::register);

    return config;
  }
}
