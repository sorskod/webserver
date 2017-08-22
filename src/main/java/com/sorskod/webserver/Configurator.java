package com.sorskod.webserver;

/**
 * @author Aleksandar Babic
 */
public interface Configurator {

  /**
   * @return The configured port for the connector or 0 if any available
   * port may be used.
   *
   * @see org.eclipse.jetty.server.NetworkConnector#getPort()
   */
  int getPort();

  /**
   *
   * @return The hostname representing the interface to which
   * this connector will bind, or null for all interfaces.
   *
   * @see org.eclipse.jetty.server.NetworkConnector#getHost()
   */
  default String getHost() {
    return null;
  }
}
