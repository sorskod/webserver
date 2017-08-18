package com.sorskod.webserver.connectors;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

/**
 * @author Aleksandar Babic
 */
public interface ConnectorFactory {
  ServerConnector get(Server server);
}
