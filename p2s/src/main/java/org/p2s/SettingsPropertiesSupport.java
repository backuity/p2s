package org.p2s;

import java.util.Optional;

public class SettingsPropertiesSupport {

    protected String loadMandatory(String name, DotCaseProperties properties) {
        Optional<String> prop = properties.getProperty(name);
        return checkProperty(name, prop);
    }

    private String checkProperty(String name, Optional<String> value) {
        if( ! value.isPresent() ) {
            throw new RuntimeException("Cannot find mandatory setting " + name);
        } else {
            return value.get();
        }
    }

    protected Optional<String> loadOptional(String name, DotCaseProperties properties) {
        return  properties.getProperty(name);
    }

    // Integer
    // ------------------------------------------------------------------------------

    protected Integer loadMandatoryInteger(String name, DotCaseProperties properties) {
        return parseInteger(name, loadMandatory(name, properties));
    }

    private Integer parseInteger(String name, String value) {
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException e) {
            throw new RuntimeException("Property " + name + " (= " + value + ") is not a valid integer");
        }
    }

    protected Optional<Integer> loadOptionalInteger(String name, DotCaseProperties properties) {
        return loadOptional(name, properties).map( s -> parseInteger(name, s));
    }

    // Long
    // ------------------------------------------------------------------------------

    protected Long loadMandatoryLong(String name, DotCaseProperties properties) {
        return parseLong(name, loadMandatory(name, properties));
    }

    private Long parseLong(String name, String value) {
        try {
            return Long.parseLong(value);
        } catch(NumberFormatException e) {
            throw new RuntimeException("Property " + name + " (= " + value + ") is not a valid long");
        }
    }

    protected Optional<Long> loadOptionalLong(String name, DotCaseProperties properties) {
        return loadOptional(name, properties).map( s -> parseLong(name, s));
    }

    // Boolean
    // ------------------------------------------------------------------------------

    protected Boolean loadMandatoryBoolean(String name, DotCaseProperties properties) {
        return parseBoolean(name, loadMandatory(name, properties));
    }

    private Boolean parseBoolean(String name, String value) {
        if( "true".equalsIgnoreCase(value) ) {
            return true;
        } else if( "false".equalsIgnoreCase(value) ) {
            return false;
        } else {
            throw new RuntimeException("Property " + name + " (= " + value + ") is not a valid boolean " +
                    "(expected 'true' or 'false')");
        }
    }

    protected Optional<Boolean> loadOptionalBoolean(String name, DotCaseProperties properties) {
        return loadOptional(name, properties).map( s -> parseBoolean(name, s));
    }
}
