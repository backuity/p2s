package org.p2s;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class SettingsFactory {

    private Properties properties;
    private String source;

    private SettingsFactory(Properties properties, String source) {
        this.properties = properties;
        this.source = source;
    }

    public SettingsFactory withFallback(String key, String value) {
        if( !this.properties.containsKey(key) ) {
            this.properties.setProperty(key, value);
        }
        return this;
    }

    public SettingsFactory withFallback(String fallbackPropertiesClasspath) {
        return withFallback(loadPropertiesFromClassPath(fallbackPropertiesClasspath), fallbackPropertiesClasspath);
    }

    public SettingsFactory withFallback(Properties fallbackProperties, String fallbackSource) {
        if( ! fallbackSource.isEmpty() ) {
            this.source += " with fallback " + fallbackSource;
        }

        Properties mergedProperties = new Properties();
        mergedProperties.putAll(fallbackProperties);
        mergedProperties.putAll(this.properties);
        this.properties = mergedProperties;
        return this;
    }

    public SettingsFactory override(String key, String value) {
        if (properties.getProperty(key) == null) {
            throw new RuntimeException("Setting '" + key + "' does not override anything.");
        }
        properties.setProperty(key, value);
        return this;
    }

    public <T> T load(Class<T> clazz) {
        Object settings;
        Class<?> settingsPropertiesClass;
        try {
            settingsPropertiesClass = SettingsFactory.class.getClassLoader().loadClass(clazz.getCanonicalName() + "Properties");
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load " + clazz, e);
        }

        try {
            settings = settingsPropertiesClass.getConstructor(Properties.class).newInstance(properties);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot load properties " + source + ", " +
                    e.getTargetException().getMessage(), e.getTargetException());
        } catch (Exception e) {
            throw new IllegalStateException("Cannot construct " + settingsPropertiesClass, e);
        }
        return (T) settings;
    }

    public static SettingsFactory from(String propertiesClassPath) {
        return new SettingsFactory(loadPropertiesFromClassPath(propertiesClassPath), propertiesClassPath);
    }

    public static <T> T loadFromProperties(String classpath, Class<T> clazz, SettingOverride ... overrides) {
        SettingsFactory factory = from(classpath);

        for( SettingOverride override : overrides ) {
            factory.override(override.getKey(), override.getValue());
        }

        return factory.load(clazz);
    }

    private static Properties loadPropertiesFromClassPath(String classpath) {
        try {
            try( InputStream is = SettingsFactory.class.getClassLoader().getResourceAsStream(classpath)) {
                if( is == null ) {
                    throw new RuntimeException("Cannot find " + classpath + " in the classpath");
                }
                Properties properties = new Properties();
                properties.load(is);
                return properties;
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load " + classpath, e);
        }
    }
}
