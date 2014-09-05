Properties To Settings [![Build Status](https://travis-ci.org/backuity/p2s.png?branch=master)](https://travis-ci.org/backuity/p2s)
======================


Generate at compile time settings classes that can load properties.
 
Here is an example from the processor-test module:
  
```java     
 import org.p2s.Settings;
 import java.util.Optional;
 
 @Settings
 public interface SomeSettings {
 
     Optional<Integer> timeout();
     String name();
     String theSurname();
 }
 ```

You can then automatically load properties file :

     name = John Doe
     timeout = 1234
     the.surname = Toto
     
With the following code :
    
```java
SomeSettings settings = SettingsFactory.loadFromProperties(
    "your-properties-from-classpath.properties", SomeSettings.class);

assertEquals("Toto", settings.theSurname());
assertEquals(Optional.of(1234), settings.timeout());
```     

## Nested types

You can use nested settings interfaces :

```java
@Settings
public interface ParentSettings {
    PersonSettings mother();
    PersonSettings father();
}

public interface PersonSettings {
    String firstName();
    String lastName();
    Integer age();
    AddressSettings address();
}

public interface AddressSettings {
    String city();
    String street();
}
```

Note : Optional nested types are not yet supported.


## Override settings

```java
SomeSettings settings = SettingsFactory.loadFromProperties(
    "your-properties-from-classpath.properties", SomeSettings.class,
    Setting.override("the.surname", "Fantastic"),
    Setting.override("timeout", "789"));
     
assertEquals("Fantastic", settings.theSurname());
assertEquals(Optional.of(789), settings.timeout());     
```
     
## Maven     
     
Add the following to your pom :
 
 ```xml
 <build>
     <plugins>
         <plugin>
             <groupId>org.apache.maven.plugins</groupId>
             <artifactId>maven-compiler-plugin</artifactId>
             <version>3.0</version>
             <configuration>
                <annotationProcessors>
                    <annotationProcessor>org.p2s.SettingsProcessor</annotationProcessor>
                </annotationProcessors>
             </configuration>
         </plugin>
     </plugins>
 </build>

 <dependencies>
     <dependency>
         <groupId>org.backuity.p2s</groupId>
         <artifactId>p2s</artifactId>
         <version>1.3</version>
     </dependency>
 </dependencies>
```


## Release

See <http://central.sonatype.org/pages/apache-maven.html>.

    cd p2s

    # deploy a snapshot
    mvn clean deploy
    
    # perform a release - NOTE : you need to have a published gpg key installed on your system    
    # update the version manually - commit push and tag
    mvn clean deploy -P release
    
    # promote to central (through the web interface)

## Licence

The project is licenced under the Apache 2 licence.
