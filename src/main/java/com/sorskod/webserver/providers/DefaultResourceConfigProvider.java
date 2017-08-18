package com.sorskod.webserver.providers;

import org.glassfish.jersey.server.ResourceConfig;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.ws.rs.core.Feature;

/**
 * Default RC provider is about to instantiate {@link ResourceConfig}
 * with all Guice bound JAX-RS {@link Feature} instances
 *
 * @author Aleksandar Babic
 */
public class DefaultResourceConfigProvider implements Provider<ResourceConfig> {

  private final Set<Feature> features;

  @Inject
  public DefaultResourceConfigProvider(Set<Feature> features) {
    this.features = features;
  }

  @Override
  public ResourceConfig get() {
    ResourceConfig config = new ResourceConfig();
    features.forEach(config::register);
    return config;
  }
}
