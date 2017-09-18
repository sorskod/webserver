package com.sorskod.webserver.inject;

import org.glassfish.jersey.internal.inject.ForeignDescriptor;
import org.glassfish.jersey.process.internal.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Aleksandar Babic
 */
public class GuiceRequestContext implements RequestContext {

  private static final Logger LOGGER = LoggerFactory.getLogger(GuiceRequestContext.class);

  private final AtomicInteger referenceCounter;
  private final Map<ForeignDescriptor, Object> store;

  public GuiceRequestContext() {
    store = new HashMap<>();
    referenceCounter = new AtomicInteger(1);
  }

  @Override
  public RequestContext getReference() {
    referenceCounter.incrementAndGet();
    return this;
  }

  @Override
  public void release() {
    if (referenceCounter.decrementAndGet() < 1) {
      try {
        store.clear();
      } finally {
        LOGGER.debug("Released scope instance {}", this);
      }
    }
  }

  @SuppressWarnings("unchecked")
  public <T> T put(ForeignDescriptor descriptor, T value) {
    return (T) store.put(descriptor, value);
  }

  public boolean contains(ForeignDescriptor provider) {
    return store.containsKey(provider);
  }

  @SuppressWarnings("unchecked")
  public <T> void remove(ForeignDescriptor descriptor) {
    final T removed = (T) store.remove(descriptor);
    if (removed != null) {
      descriptor.dispose(removed);
    }
  }
}
