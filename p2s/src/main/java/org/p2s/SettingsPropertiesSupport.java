package org.p2s;

import java.util.Optional;
import java.util.Properties;

public class SettingsPropertiesSupport {

    protected String loadMandatory(String name, Properties properties) {
        String prop = properties.getProperty(name);
        checkProperty(name, prop);
        return prop;
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

    // Integer
    // ------------------------------------------------------------------------------

    protected Integer loadMandatoryInteger(String name, Properties properties) {
        return parseInteger(name, loadMandatory(name, properties));
    }

    private Integer parseInteger(String name, String value) {
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException e) {
            throw new RuntimeException("Property " + name + " (= " + value + ") is not a valid integer");
        }
    }

    protected Optional<Integer> loadOptionalInteger(String name, Properties properties) {
        return loadOptional(name, properties).map( s -> parseInteger(name, s));
    }

    // Long
    // ------------------------------------------------------------------------------

    protected Long loadMandatoryLong(String name, Properties properties) {
        return parseLong(name, loadMandatory(name, properties));
    }

    private Long parseLong(String name, String value) {
        try {
            return Long.parseLong(value);
        } catch(NumberFormatException e) {
            throw new RuntimeException("Property " + name + " (= " + value + ") is not a valid long");
        }
    }

    protected Optional<Long> loadOptionalLong(String name, Properties properties) {
        return loadOptional(name, properties).map( s -> parseLong(name, s));
    }

    // Boolean
    // ------------------------------------------------------------------------------

    protected Boolean loadMandatoryBoolean(String name, Properties properties) {
        return parseBoolean(name, loadMandatory(name, properties));
    }

    private Boolean parseBoolean(String name, String value) {
        try {
            return Boolean.parseBoolean(value);
        } catch(NumberFormatException e) {
            throw new RuntimeException("Property " + name + " (= " + value + ") is not a valid boolean");
        }
    }

    protected Optional<Boolean> loadOptionalBoolean(String name, Properties properties) {
        return loadOptional(name, properties).map( s -> parseBoolean(name, s));
    }
}
