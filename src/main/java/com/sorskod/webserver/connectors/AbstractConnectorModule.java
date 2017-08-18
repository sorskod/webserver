package com.sorskod.webserver.connectors;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * @author Aleksandar Babic
 */
public abstract class AbstractConnectorModule extends AbstractModule {

  @Override
  protected void configure() {
    Multibinder<ConnectorFactory> connectorBinder = Multibinder.newSetBinder(binder(), ConnectorFactory.class);
    connectorBinder.addBinding().to(getConnectorFactoryClass());
  }

  protected abstract Class<? extends ConnectorFactory> getConnectorFactoryClass();
}
