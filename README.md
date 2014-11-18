Properties To Settings [![Build Status](https://travis-ci.org/backuity/p2s.png?branch=master)](https://travis-ci.org/backuity/p2s)
======================


Generate at compile time settings classes (java 8) that can load java properties files or HOCON files.

Add the following dependency to your pom:

```xml
 <dependencies>
     <dependency>
         <groupId>org.backuity.p2s</groupId>
         <artifactId>p2s</artifactId>
         <version>2.0</version>
     </dependency>
 </dependencies>
```

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
SomeSettings settings = JulSettingsFactory.from("your-properties-from-classpath.properties")
                                          .load(SomeSettings.class);

assertEquals("Toto", settings.theSurname());
assertEquals(Optional.of(1234), settings.timeout());
```

## HOCON

To load configuration from an HOCON file add the following dependency to your pom:

```xml
 <dependencies>
     <dependency>
         <groupId>org.backuity.p2s</groupId>
         <artifactId>p2s-hocon</artifactId>
         <version>2.0</version>
     </dependency>
 </dependencies>
```

And use `HoconSettingsFactory` instead of `JulSettingsFactory`.
So given the following HOCON file :

     name = "John Doe"
     timeout = 1234
     the {
       surname = "Toto"
     }

```java
SomeSettings settings = HoconSettingsFactory.from("your-hoconfrom-classpath.conf")
                                            .load(SomeSettings.class);

assertEquals("Toto", settings.theSurname());
assertEquals(Optional.of(1234), settings.timeout());
```

This especially shines when used with nested types or arrays.

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

You can load up a Parent with the following HOCON file :

    mother {
      first { name = "Mary Jane" }
      last { name = "Watson" }
      age = 32
      address {
        city = "Manhattan"
        street = "1st avenue"
      }
    }

    father {
      # ...
    }

Or a properties:

    mother.first.name = Alice
    mother.last.name = Watt
    mother.age = 31
    mother.address.street = 3 rue de la paix
    mother.address.city = Toulon

    father.first.name = Bob
    # ...

Note : Optional nested types are not yet supported.

## Fallback

From properties:

```java
SomeSettings settings = JulSettingsFactory.from("your-properties-from-classpath.properties")
    .withFallback("a-fallback-from-classpath.properties")
    .load(SomeSettings.class);
```

From code:

```java
SomeSettings settings = JulSettingsFactory.from("your-properties-from-classpath.properties")
    .withFallback("the.surname", "Fantastic")
    .withFallback("timeout", "123")
    .load(SomeSettings.class);
```

## Override settings

```java
SomeSettings settings = JulSettingsFactory.from("your-properties-from-classpath.properties")
    .override("the.surname", "Fantastic")
    .override("timeout", "789")
    .load(SomeSettings.class);
     
assertEquals("Fantastic", settings.theSurname());
assertEquals(Optional.of(789), settings.timeout());     
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
