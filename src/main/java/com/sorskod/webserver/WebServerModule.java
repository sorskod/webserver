package com.sorskod.webserver;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.servlet.ServletModule;
import com.sorskod.webserver.features.BoundWebResources;
import com.sorskod.webserver.providers.DefaultResourceConfigProvider;
import com.sorskod.webserver.providers.JettyServerProvider;
import com.sorskod.webserver.providers.ServletContextHandlerProvider;
import com.sorskod.webserver.providers.ServletHolderProvider;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Provider;
import javax.ws.rs.core.Feature;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Aleksandar Babic
 */
public class WebServerModule extends AbstractModule {

  protected void configure() {
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


    //Multibinder.newSetBinder(binder(), Feature.class).addBinding().to(BoundWebResources.class);
    //Multibinder.newSetBinder(binder(), Feature.class).addBinding().to(JacksonFeature.class);


    features().forEach(f -> Multibinder.newSetBinder(binder(), Feature.class).addBinding().to(f));
  }

  private Set<Class<? extends Feature>> features() {
    Set<Class<? extends Feature>> features = new HashSet<>(2);
    features.add(JacksonFeature.class);
    features.add(BoundWebResources.class);

    return features;
  }

  /**
   * Defined class that is used as a provider for {@link ResourceConfig}
   *
   * @return {@link ResourceConfig} provider
   */
  protected Class<? extends Provider<ResourceConfig>> resourceConfigProvider() {
    return DefaultResourceConfigProvider.class;
  }
}
