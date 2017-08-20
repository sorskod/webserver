package com.sorskod.webserver;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MultibindingsScanner;
import com.google.inject.multibindings.ProvidesIntoSet;
import com.google.inject.servlet.ServletModule;
import com.sorskod.webserver.annotations.BaseConfiguration;
import com.sorskod.webserver.features.BoundWebResourcesFeature;
import com.sorskod.webserver.providers.*;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Provider;
import javax.ws.rs.core.Feature;

/**
 * @author Aleksandar Babic
 */
public class WebServerModule extends AbstractModule {

  protected void configure() {
    install(MultibindingsScanner.asModule());
    install(new ServletModule());

    bind(ServletHolder.class)
        .toProvider(ServletHolderProvider.class)
        .asEagerSingleton();

    bind(Handler.class)
        .toProvider(ServletContextHandlerProvider.class)
        .asEagerSingleton();

    bind(Server.class)
        .toProvider(JettyServerProvider.class)
        .asEagerSingleton();

    bind(ResourceConfig.class)
        .toProvider(resourceConfigProvider())
        .asEagerSingleton();

    bind(HttpConfiguration.class)
        .annotatedWith(BaseConfiguration.class)
        .toProvider(HttpConfigurationProvider.class)
        .asEagerSingleton();

  }

  /**
   * Defined class that is used as a provider for {@link ResourceConfig}
   *
   * @return {@link ResourceConfig} provider
   */
  protected Class<? extends Provider<ResourceConfig>> resourceConfigProvider() {
    return DefaultResourceConfigProvider.class;
  }

  @ProvidesIntoSet
  Class<? extends Feature> jacksonFeature() {
    return JacksonFeature.class;
  }

  @ProvidesIntoSet
  Class<? extends Feature> boundWebResourcesFeature() {
    return BoundWebResourcesFeature.class;
  }
}
