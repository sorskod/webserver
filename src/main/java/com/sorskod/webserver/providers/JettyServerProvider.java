package com.sorskod.webserver.providers;

import com.sorskod.webserver.connectors.ConnectorFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Set;

/**
 * @author Aleksandar Babic
 */
public class JettyServerProvider implements Provider<Server> {

  private final Handler serverHandler;
  private final Set<ConnectorFactory> connectors;

  @Inject
  public JettyServerProvider(Handler serverHandler, Set<ConnectorFactory> connectors) {
    this.serverHandler = serverHandler;
    this.connectors = connectors;
  }

  public Server get() {
    Server server = new Server();
    server.setHandler(serverHandler);
    server.setConnectors(connectors.stream().map(cf -> cf.get(server)).toArray(Connector[]::new));

    return server;
  }
}
