package com.sorskod.webserver.connectors.https;

import com.sorskod.webserver.connectors.AbstractConnectorModule;
import com.sorskod.webserver.connectors.ConnectorFactory;

/**
 * @author Aleksandar Babic
 */
public class HTTPSConnectorModule extends AbstractConnectorModule {

  @Override
  protected Class<? extends ConnectorFactory> getConnectorFactoryClass() {
    return HTTPSConnectorFactory.class;
  }
}
