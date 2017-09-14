# webserver 

Guice WebServer Module - backed by latest Jetty, Jersey and Jackson. **Without HK2-Guice bridge.**

Library is useful for lite web services, like simple REST endpoints. JSON output is the only supported. 

[![Build Status](https://travis-ci.org/sorskod/webserver.svg?branch=master)](https://travis-ci.org/sorskod/webserver)

## Dependencies:
- Guice 4.1
- Jetty 9.4.6
- Jersey 2.26
- Jackson 2.8.4

## Snapshot installation

In your POM, add OSS Sonartype repository and following dependency:

```xml
<project>
    <repositories>
        <repository>
            <id>oss-sonatype</id>
            <name>oss-sonatype</name>
            <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
    
    <dependencies>
        <dependency>
          <groupId>com.sorskod.webserver</groupId>
          <artifactId>webserver</artifactId>
          <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>

```

## Usage:

There is a working example in test package. However, here  is short guideline:

1. Bind all JAX-RS resource classes in Guice context. (All classes annotated with `@Path` will be registered in Jersey's context. Jersey's auto-discovery feature is disabled.)
2. Install `WebServerModule`
3. Install `HTTPConnectorModule` or/and `HTTPSConnectorModule`
4. Provide `Configurator` implementation
5. Inject Jetty `Server` and call `start()`

## Example:

```java

public class MyModule extends AbstractModule {
  
  protected void configure() {
    
    // Bind @Path annotated classes
    bind(MyHttpResource.class);
  }
  
  @Provides
  @DefaultConnector
  Configurator configuratorProvider() {
    return () -> 8080; // WebServer port 
  }
  
  @ProvidesIntoSet
  Feature customJaxrsFeature() {
    // Provide custom features, register filters, etc 
    return (context) -> {};
  }
}

Injector injector = Guice.createInjector(new WebServerModule(), 
                                         new HTTPConnectorModule(),
                                         new MyModule());


injector.getInstance(Server.class).start();
```



#### IMPORTANT NOTE 
Library is written for fun and test purposes. It's not complete and fully featured. Pull requests and improvement ideas are welcome.


## TODO:
- Remove HK2 and make Guice as only DI framework 
- HTTPS support and tests
- README & Wiki