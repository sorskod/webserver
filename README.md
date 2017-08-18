# webserver
Guice WebServer Module - backed by latest Jetty, Jersey and Jackson.

Library is useful for lite web services, like simple REST endpoints. JSON output is the only supported. 


## Dependencies:
- Guice 4.1
- Jetty 9.4.6
- Jersey 2.25.1
- Jackson 2.8.4

## Usage:

There is a working example in test package. However, here  is short guideline:

1. Bind all JAX-RS resource classes in Guice context. (All classes annotated with `@Path` will be registered in Jersey's context. Jersey's auto-discovery feature is disabled.)
2. Install `WebServerModule`
3. Install `HTTPConnectorModule` or/and `HTTPSConnectorModule`
4. Provide `Configurator` implementation
5. Inject Jetty `Server` and call `start()`

#### IMPORTANT NOTE 
Library is written for fun and test purposes. It's not complete and fully featured. Pull requests and improvement ideas are welcome.