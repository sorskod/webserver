package com.sorskod.webserver.features;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.sorskod.webserver.mappers.JsonParseExceptionMapper;
import com.sorskod.webserver.mappers.JsonMappingExceptionMapper;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.internal.InternalProperties;
import org.glassfish.jersey.internal.util.PropertiesHelper;
import org.glassfish.jersey.jackson.internal.FilteringJacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.JacksonFilteringFeature;
import org.glassfish.jersey.message.filtering.EntityFilteringFeature;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

/**
 * Registers Exception Mappers which are returns application/json instead of text/plain for error messages
 *
 * @see org.glassfish.jersey.jackson.JacksonFeature
 * @author Aleksandar Babic
 */
public class JsonJacksonFeature implements Feature {

  private static final String JSON_FEATURE = JsonJacksonFeature.class.getSimpleName();

  @Override
  public boolean configure(FeatureContext context) {
    final Configuration config = context.getConfiguration();

    final String jsonFeature = CommonProperties.getValue(config.getProperties(), config.getRuntimeType(),
        InternalProperties.JSON_FEATURE, JSON_FEATURE, String.class);
    // Other JSON providers registered.
    if (!JSON_FEATURE.equalsIgnoreCase(jsonFeature)) {
      return false;
    }

    // Disable other JSON providers.
    context.property(PropertiesHelper.getPropertyNameForRuntime(InternalProperties.JSON_FEATURE, config.getRuntimeType()),
        JSON_FEATURE);

    // Register Jackson.
    if (!config.isRegistered(JacksonJaxbJsonProvider.class)) {
      // add the Jackson exception mappers with application/json output
      context.register(JsonMappingExceptionMapper.class);
      context.register(JsonParseExceptionMapper.class);

      if (EntityFilteringFeature.enabled(config)) {
        context.register(JacksonFilteringFeature.class);
        context.register(FilteringJacksonJaxbJsonProvider.class, MessageBodyReader.class, MessageBodyWriter.class);
      } else {
        context.register(JacksonJaxbJsonProvider.class, MessageBodyReader.class, MessageBodyWriter.class);
      }
    }

    return true;
  }
}
