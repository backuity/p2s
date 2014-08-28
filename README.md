# Properties To Settings

Generate at compile time settings classes that can load properties.
 
Here is an example from the processor-test module:
     
     import org.p2s.Settings;
     import java.util.Optional;
     
     @Settings
     public interface SomeSettings {
     
         Optional<Integer> timeout();
         String name();
         String theSurname();
     }

You can then automatically load properties file :

     name = John Doe
     timeout = 1234
     the.surname = Toto
     
With the following code :
     
     SomeSettings settings = SettingsFactory.loadFromProperties(
        "your-properties-from-classpath.properties", SomeSettings.class);
     
     assertEquals("Toto", settings.theSurname());
     assertEquals(Optional.of(1234), settings.timeout());
     
     
## Maven     
     
Add the following to your pom :
 
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
             <version>1.0</version>
         </dependency>
     </dependencies>