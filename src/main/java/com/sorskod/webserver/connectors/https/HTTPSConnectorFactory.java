package com.sorskod.webserver.connectors.https;

import com.sorskod.webserver.Configurator;
import com.sorskod.webserver.annotations.SecureConnector;
import com.sorskod.webserver.connectors.ConnectorFactory;

import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import javax.inject.Inject;

/**
 * @author Aleksandar Babic
 */
public class HTTPSConnectorFactory implements ConnectorFactory {

  private final Configurator configurator;

  @Inject
  public HTTPSConnectorFactory(@SecureConnector Configurator configurator) {
    this.configurator = configurator;
  }


  @Override
  public ServerConnector get(Server server) {
    // TODO: Finish
    HttpConfiguration config = new HttpConfiguration();
    config.setSecurePort(configurator.getPort());
    config.setSecureScheme(HttpScheme.HTTPS.asString());
    HttpConnectionFactory connectionFactory = new HttpConnectionFactory(config);

    final ServerConnector connector = new ServerConnector(server, connectionFactory);
    connector.setPort(configurator.getPort());

    return connector;
  }
}
