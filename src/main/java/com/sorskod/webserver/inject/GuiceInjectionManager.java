package com.sorskod.webserver.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.glassfish.jersey.internal.inject.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Aleksandar Babic
 */
public class GuiceInjectionManager implements InjectionManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(GuiceInjectionManager.class);

  private final Injector injector;
  private Injector childInjector;
  private final JerseyBindingsModule module;

  public GuiceInjectionManager(Injector injector) {
    LOGGER.debug("New instance is created.");

    this.injector = injector;
    module = new JerseyBindingsModule();
  }

  @Override
  public void completeRegistration() {
    LOGGER.debug("Registration completed.");

    childInjector = injector.createChildInjector(module);
  }

  @Override
  public void shutdown() {
    LOGGER.debug("Shutdown called.");
  }

  @Override
  public void register(Binding binding) {
    LOGGER.debug("Register binding. Contracts: {}; Implementation type: {}",
        binding.getContracts(), binding.getImplementationType());

    module.addBinding(binding);
  }

  @Override
  public void register(Iterable<Binding> iterable) {
    LOGGER.debug("Register iterable  bindings starts.");
    iterable.forEach(this::register);
    LOGGER.debug("Register iterable  bindings ends.");
  }

  @Override
  public void register(Binder binder) {
    LOGGER.debug("Register iterable  bindings starts.");
    binder.getBindings().forEach(this::register);
    LOGGER.debug("Register iterable  bindings ends.");
  }

  @Override
  public void register(Object o) throws IllegalArgumentException {
    throw new IllegalArgumentException("Not Supported.");
  }

  @Override
  public boolean isRegistrable(Class<?> aClass) {
    LOGGER.debug("Checks is {} registrable", aClass);
    return false;
  }

  @Override
  public <T> T createAndInitialize(Class<T> aClass) {
    LOGGER.debug("Creating and initializing {}", aClass);
    return injector.getInstance(aClass);
  }

  @Override
  public <T> List<ServiceHolder<T>> getAllServiceHolders(Class<T> aClass, Annotation... annotations) {
    LOGGER.debug("Getting all service holders {}", aClass);
    return null;
  }

  @Override
  public <T> T getInstance(Class<T> aClass, Annotation... annotations) {
    return null;
  }

  @Override
  public <T> T getInstance(Class<T> aClass, String s) {
    return null;
  }

  @Override
  public <T> T getInstance(Class<T> contractOrImpl) {
    return childInjector.getInstance(contractOrImpl);
  }

  @Override
  public <T> T getInstance(Type contractOrImpl) {
    return childInjector.getInstance((Class<T>) contractOrImpl);
  }

  @Override
  public Object getInstance(ForeignDescriptor foreignDescriptor) {
    return null;
  }

  @Override
  public ForeignDescriptor createForeignDescriptor(Binding binding) {
    return null;
  }

  @Override
  public <T> List<T> getAllInstances(Type type) {
    LOGGER.debug("Getting all instances of type: {}", type);
    return null;
  }

  @Override
  public void inject(Object o) {
    LOGGER.debug("Inject {}", o);
  }

  @Override
  public void inject(Object o, String classAnalyzer) {

  }

  @Override
  public void preDestroy(Object o) {

  }
}
