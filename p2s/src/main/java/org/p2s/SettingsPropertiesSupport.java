package org.p2s;

import java.util.Optional;
import java.util.Properties;

public class SettingsPropertiesSupport {

    protected String loadMandatory(String name, Properties properties) {
        String prop = properties.getProperty(name);
        checkProperty(name, prop);
        return prop;
    }

    protected Integer loadMandatoryInt(String name, Properties properties) {
        return parseInt(name, loadMandatory(name, properties));
    }

    private Integer parseInt(String name, String value) {
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException e) {
            throw new RuntimeException("Property " + name + " (= " + value + ") is not a valid integer");
        }
    }

    private void checkProperty(String name, String value) {
        if( value == null ) {
            throw new RuntimeException("Cannot find mandatory setting " + name);
        }
    }

    protected Optional<String> loadOptional(String name, Properties properties) {
        String prop = properties.getProperty(name);
        return Optional.ofNullable(prop);
    }

    protected Optional<Integer> loadOptionalInt(String name, Properties properties) {
        return loadOptional(name, properties).map( s -> parseInt(name, s));
    }
}
