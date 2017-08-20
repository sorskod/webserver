package com.sorskod.webserver.connectors.http;

import com.sorskod.webserver.Configurator;
import com.sorskod.webserver.annotations.BaseConfiguration;
import com.sorskod.webserver.annotations.DefaultConnector;
import com.sorskod.webserver.connectors.ConnectorFactory;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import javax.inject.Inject;

/**
 * @author Aleksandar Babic
 */
public class HTTPConnectorFactory implements ConnectorFactory {

  private final Configurator configurator;
  private final HttpConfiguration defaultConfig;

  @Inject
  public HTTPConnectorFactory(@DefaultConnector Configurator configurator,
                              @BaseConfiguration HttpConfiguration defaultConfig) {
    this.configurator = configurator;
    this.defaultConfig = defaultConfig;
  }

  public ServerConnector get(Server server) {
    HttpConnectionFactory connectionFactory = new HttpConnectionFactory(new HttpConfiguration(defaultConfig));

    final ServerConnector connector = new ServerConnector(server, connectionFactory);
    connector.setPort(configurator.getPort());

    return connector;
  }
}
