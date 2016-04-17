# BugVM-Surefire-Provider

The BugVM surefire provider is a means of running unit tests compiled by
BugVM on the console, in the iOS Simulator or on an iOS device.

## Using BugVM Surefire Provider

### Compile and install this plugin

Compile bugvm-surefire-provider and install into your local maven repository
with `mvn install`.

### Example test class:

```java
import static org.junit.Assert.*;

import org.junit.Test;

public class TestClass {

    @Test
    public void testTest() throws Exception {
        System.err.println("Running testTest");
        assertTrue(1 == 1);
    }
}
```

### Example pom.xml

```xml
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>2.17</version>
        <dependencies>
          <dependency>
            <groupId>com.bugvm</groupId>
            <artifactId>bugvm-surefire-provider</artifactId>
            <version>1.0.0-SNAPSHOT</version>
          </dependency>
        </dependencies>
      </plugin>
    </plugins>
  </build>  
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
```

### Running

mvn test

## How does it work?

The BugVM surefire provider scans your project for JUnit3/4 tests. The test
classes are compiled into native code with BugVM and executed on the
simulator or a real device. Results are transferred back using GSON over a TCP
connection.

## Configuration

The provider can be configured using system properties. The following system
properties are supported:

* `bugvm.test.enableDebugLogging` -- Set to `true` to enable debug logging
  output from the provider.
* `bugvm.test.enableServerLogging` -- Set to `true` to enable debug logging
  output from the test server executable.
* `bugvm.test.propertiesFile` -- Properties file to read in. If not set the
  provider will try with `bugvm.test.properties` and then
  `bugvm.properties`.
* `bugvm.test.configFile` -- Config file to read in. If not set the provider
  will try with `bugvm.test.xml` and then `bugvm.xml`.
* `bugvm.test.os` -- Sets the OS to test on. If not set the OS will be read
  from the config file. The final fallback is to build for the current host
  OS.
* `bugvm.test.arch` -- Sets the CPU architecture to test on. If not set the
  architecture will be read from the config file. The final fallback is to
  build for the current host architecture.

These properties can either be specified on the `mvn` command line when
running the tests, e.g.:

```
mvn -Dbugvm.test.enableDebugLogging=true -Dbugvm.test.os=ios -Dbugvm.test.arch=x86 clean test
```

Or be set in the provider coniguration in the `pom.xml`, e.g.:

```xml
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>2.17</version>
    <dependencies>
      <dependency>
        <groupId>com.bugvm</groupId>
        <artifactId>bugvm-surefire-provider</artifactId>
        <version>1.0.0-SNAPSHOT</version>
      </dependency>
    </dependencies>
    <configuration>
      <systemPropertyVariables>
        <bugvm.test.enableDebugLogging>true</bugvm.test.enableDebugLogging>
        <bugvm.test.os>ios</bugvm.test.os>
        <bugvm.test.arch>x86</bugvm.test.arch>
      </systemPropertyVariables>
    </configuration>
  </plugin>
```
