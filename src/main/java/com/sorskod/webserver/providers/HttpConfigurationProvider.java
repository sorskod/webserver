package com.sorskod.webserver.providers;

import org.eclipse.jetty.server.HttpConfiguration;

import javax.inject.Provider;

/**
 * @author Aleksandar Babic
 */
public class HttpConfigurationProvider implements Provider<HttpConfiguration> {

  @Override
  public HttpConfiguration get() {
    HttpConfiguration configuration = new HttpConfiguration();
    configuration.setSendXPoweredBy(false);
    configuration.setSendServerVersion(false);
    return configuration;
  }
}
