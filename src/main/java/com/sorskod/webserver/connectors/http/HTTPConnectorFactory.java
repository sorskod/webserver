package com.sorskod.webserver.connectors.http;

import com.sorskod.webserver.Configurator;
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

  @Inject
  public HTTPConnectorFactory(@DefaultConnector Configurator configurator) {
    this.configurator = configurator;
  }


  public ServerConnector get(Server server) {

    HttpConfiguration config = new HttpConfiguration();
    HttpConnectionFactory connectionFactory = new HttpConnectionFactory(config);

    final ServerConnector connector = new ServerConnector(server, connectionFactory);
    connector.setPort(configurator.getPort());

    return connector;
  }


}
