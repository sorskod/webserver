package com.sorskod.webserver.connectors.http;

import com.sorskod.webserver.connectors.AbstractConnectorModule;
import com.sorskod.webserver.connectors.ConnectorFactory;

/**
 * @author Aleksandar Babic
 */
public class HTTPConnectorModule extends AbstractConnectorModule {

  @Override
  protected Class<? extends ConnectorFactory> getConnectorFactoryClass() {
    return HTTPConnectorFactory.class;
  }
}
