package org.p2s;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigValueFactory;

import java.util.Optional;

public class HoconDotCaseProperties implements DotCaseProperties<HoconDotCaseProperties> {

    private Config config;

    public HoconDotCaseProperties(Config config) {
        this.config = config;
    }

    @Override
    public Optional<String> getProperty(String name) {
        try {
            return Optional.of(config.getString(name));
        } catch(ConfigException.Missing e) {
            return Optional.empty();
        }
    }

    @Override
    public void setProperty(String key, String value) {
        this.config = config.withValue(key, ConfigValueFactory.fromAnyRef(value));
    }

    @Override
    public HoconDotCaseProperties withFallback(HoconDotCaseProperties other) {
        return new HoconDotCaseProperties(this.config.withFallback(other.config));
    }
}
