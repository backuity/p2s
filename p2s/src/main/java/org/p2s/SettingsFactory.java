package org.p2s;

import java.lang.reflect.InvocationTargetException;

abstract class SettingsFactory<P extends DotCaseProperties<P>> {

    private P properties;
    private String source;

    protected SettingsFactory(P properties, String source) {
        this.properties = properties;
        this.source = source;
    }

    public SettingsFactory<P> withFallback(String key, String value) {
        if( !this.properties.containsKey(key) ) {
            this.properties.setProperty(key, value);
        }
        return this;
    }

    public SettingsFactory<P> withFallback(P fallbackProperties, String fallbackSource) {
        if( ! fallbackSource.isEmpty() ) {
            this.source += " with fallback " + fallbackSource;
        }

        this.properties = this.properties.withFallback(fallbackProperties);
        return this;
    }

    public SettingsFactory<P> override(String key, String value) {
        if (!properties.containsKey(key)) {
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
            settings = settingsPropertiesClass.getConstructor(DotCaseProperties.class).newInstance(properties);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot load properties " + source + ", " +
                    e.getTargetException().getMessage(), e.getTargetException());
        } catch (Exception e) {
            throw new IllegalStateException("Cannot construct " + settingsPropertiesClass, e);
        }
        return (T) settings;
    }
}
