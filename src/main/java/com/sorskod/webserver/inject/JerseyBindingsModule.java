package com.sorskod.webserver.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.RequestScoped;
import org.glassfish.jersey.internal.inject.Binding;
import org.glassfish.jersey.internal.inject.ClassBinding;
import org.glassfish.jersey.process.internal.RequestScope;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Aleksandar Babic
 */
public class JerseyBindingsModule extends AbstractModule {

  List<org.glassfish.jersey.internal.inject.Binding> bindings = new ArrayList<>();

  @Override
  protected void configure() {
    bind(RequestScope.class).to(GuiceRequestScope.class).in(Singleton.class);

    bindings.forEach(b -> {

      if(ClassBinding.class.isAssignableFrom(b.getClass())) {
        b.
      }
    });
  }

  public void addBinding(Binding binding) {
    bindings.add(binding);
  }

}
