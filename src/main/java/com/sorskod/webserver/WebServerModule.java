package com.sorskod.webserver;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MultibindingsScanner;
import com.google.inject.multibindings.ProvidesIntoSet;
import com.google.inject.servlet.ServletModule;
import com.sorskod.webserver.annotations.BaseConfiguration;
import com.sorskod.webserver.connectors.ConnectorFactory;
import com.sorskod.webserver.features.BoundWebResourcesFeature;
import com.sorskod.webserver.features.JsonJacksonFeature;
import com.sorskod.webserver.mappers.RuntimeExceptionMapper;
import com.sorskod.webserver.mappers.WebApplicationExceptionMapper;
import com.sorskod.webserver.providers.DefaultResourceConfigProvider;
import com.sorskod.webserver.providers.HttpConfigurationProvider;
import com.sorskod.webserver.providers.JettyServerProvider;
import com.sorskod.webserver.providers.ServletContextHandlerProvider;
import com.sorskod.webserver.providers.ServletHolderProvider;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.Set;

import javax.inject.Provider;
import javax.ws.rs.core.Feature;

/**
 * @author Aleksandar Babic
 */
public class WebServerModule extends AbstractModule {

  protected void configure() {
    install(new ServletModule());

    requireBinding(Key.get(new TypeLiteral<Set<ConnectorFactory>>() {}));

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
    return JsonJacksonFeature.class;
  }

  @ProvidesIntoSet
  Class<? extends Feature> boundWebResourcesFeature() {
    return BoundWebResourcesFeature.class;
  }

  @ProvidesIntoSet
  Feature exceptionMappersFeature() {
    return (context) -> {
      context.register(RuntimeExceptionMapper.class);
      context.register(WebApplicationExceptionMapper.class);
      return true;
    };
  }
}
