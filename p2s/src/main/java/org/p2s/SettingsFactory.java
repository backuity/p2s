package org.p2s;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class SettingsFactory {

    public static <T> T loadFromProperties(String classpath, Class<T> clazz, SettingOverride ... overrides) {
        Object settings;
        Class<?> settingsPropertiesClass;
        try {
            settingsPropertiesClass = SettingsFactory.class.getClassLoader().loadClass(clazz.getCanonicalName() + "Properties");
            settings = settingsPropertiesClass.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load " + clazz, e);
        }            Properties properties = loadPropertiesFromClassPath(classpath);
        try {
            for( SettingOverride override : overrides ) {
                properties.setProperty(override.getKey(), override.getValue());
            }
            settingsPropertiesClass.getMethod("loadProperties", Properties.class ).invoke(settings, properties);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot load properties " + classpath + ", " + e.getTargetException().getMessage(), e.getTargetException());
        } catch (Exception e) {
            throw new IllegalStateException("Cannot invoke loadProperties method on " + settings, e);
        }
        return (T) settings;
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
