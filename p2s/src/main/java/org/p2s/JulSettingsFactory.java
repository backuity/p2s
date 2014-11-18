package org.p2s;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JulSettingsFactory extends SettingsFactory<JulDotCaseProperties> {

    protected JulSettingsFactory(JulDotCaseProperties properties, String source) {
        super(properties, source);
    }

    public static JulSettingsFactory from(String propertiesClassPath) {
        return new JulSettingsFactory(loadPropertiesFromClassPath(propertiesClassPath), propertiesClassPath);
    }

    public JulSettingsFactory withFallback(String fallbackPropertiesClasspath) {
        return (JulSettingsFactory) withFallback(loadPropertiesFromClassPath(fallbackPropertiesClasspath), fallbackPropertiesClasspath);
    }

    private static JulDotCaseProperties loadPropertiesFromClassPath(String classpath) {
        try {
            try( InputStream is = JulSettingsFactory.class.getClassLoader().getResourceAsStream(classpath)) {
                if( is == null ) {
                    throw new RuntimeException("Cannot find " + classpath + " in the classpath");
                }
                Properties properties = new Properties();
                properties.load(is);
                return new JulDotCaseProperties(properties);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load " + classpath, e);
        }
    }
}
