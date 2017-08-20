package com.sorskod.webserver.connectors;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

/**
 * @author Aleksandar Babic
 */
public interface ConnectorFactory {

  /**
   * Creates {@link ServerConnector} relates to provided {@link Server}
   *
   * @param server Jetty Server
   * @return ServerConnector
   */
  ServerConnector get(Server server);
}
