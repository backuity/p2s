package org.p2s;

import java.util.Optional;
import java.util.Properties;

public class JulDotCaseProperties implements DotCaseProperties<JulDotCaseProperties> {
    private Properties properties;

    public JulDotCaseProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Optional<String> getProperty(String name) {
        return Optional.ofNullable(properties.getProperty(name));
    }

    @Override
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    @Override
    public JulDotCaseProperties withFallback(JulDotCaseProperties other) {
        Properties mergedProperties = new Properties();
        mergedProperties.putAll(other.properties);
        mergedProperties.putAll(this.properties);
        return new JulDotCaseProperties(mergedProperties);
    }
}
