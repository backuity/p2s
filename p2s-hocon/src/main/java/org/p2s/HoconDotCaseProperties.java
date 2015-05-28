package org.p2s;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigValueFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class HoconDotCaseProperties extends DotCaseProperties<HoconDotCaseProperties> {

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
    public boolean hasProperty(String name) {
        return config.hasPath(name);
    }

    @Override
    public List<String> getProperties(String name) {
        return config.getStringList(name);
    }

    @Override
    public <T> List<T> getProperties(String name, Function<DotCaseProperties<?>, T> producer) {
        return config.getObjectList(name).stream()
                .map( config -> producer.apply(new HoconDotCaseProperties(config.toConfig())))
                .collect(toList());
    }

    @Override
    public <T> List<T> getProperties(String name, Class<T> clazz) {
        if( clazz == String.class ) {
            return (List<T>)getProperties(name);
        } else if( clazz == Integer.class) {
            return (List<T>)config.getIntList(name);
        } else if( clazz == Boolean.class) {
            return (List<T>)config.getBooleanList(name);
        } else if( clazz == Long.class) {
            return (List<T>)config.getLongList(name);
        } else {
            return super.getProperties(name, clazz);
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
