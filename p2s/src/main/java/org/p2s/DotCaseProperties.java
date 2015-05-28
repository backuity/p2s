package org.p2s;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public abstract class DotCaseProperties<T extends DotCaseProperties> {
    public abstract Optional<String> getProperty(String name);
    public abstract boolean hasProperty(String name);
    public abstract List<String> getProperties(String name);

    public abstract <T> List<T> getProperties(String name, Function<DotCaseProperties<?>, T> producer);

    public boolean containsKey(String key) {
        return getProperty(key).isPresent();
    }

    public abstract void setProperty(String key, String value);

    /** @return a new value computed by merging this value with `other`, with
     *          keys in this value "winning" over the other one
     */
    public abstract T withFallback(T other);

    // Conversions
    // ==============================================================================

    public <T> List<T> getProperties(String name, Class<T> clazz) {
        return getProperties(name).stream().map( value -> parse(name, value, clazz)).collect(toList());
    }

    public String loadMandatory(String name) {
        Optional<String> value = getProperty(name);
        if( ! value.isPresent() ) {
            throw new RuntimeException("Cannot find mandatory setting " + name);
        } else {
            return value.get();
        }
    }

    public <T> T loadMandatory(String name, Class<T> clazz) {
        return parse(name, loadMandatory(name), clazz);
    }

    public <T> Optional<T> loadOptional(String name, Class<T> clazz) {
        return  getProperty(name).map( value -> parse(name, value, clazz));
    }

    public <T> Optional<T> loadNestedOptional(String name, Function<DotCaseProperties<?>,T> factory) {
        if( hasProperty(name) ) {
            return Optional.of(factory.apply(this));
        } else {
            return Optional.empty();
        }
    }

    public static <T> T parse(String name, String value, Class<T> clazz) {
        if (clazz == String.class) {
            return (T)value;
        } else if( clazz == Integer.class || clazz == Integer.TYPE ){
            return (T)parseInteger(name, value);
        } else if( clazz == Long.class || clazz == Long.TYPE){
            return (T)parseLong(name, value);
        } else if( clazz == Boolean.class || clazz == Boolean.TYPE) {
            return (T)parseBoolean(name, value);
        } else {
            throw new UnsupportedOperationException("Property " + name +
                    " (= " + value + ") has an unsupported type " + clazz);
        }
    }

    public static Integer parseInteger(String name, String value) {
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException e) {
            throw new RuntimeException("Property " + name + " (= " + value + ") is not a valid integer");
        }
    }

    public static Long parseLong(String name, String value) {
        try {
            return Long.parseLong(value);
        } catch(NumberFormatException e) {
            throw new RuntimeException("Property " + name + " (= " + value + ") is not a valid long");
        }
    }

    public static Boolean parseBoolean(String name, String value) {
        if( "true".equalsIgnoreCase(value) ) {
            return true;
        } else if( "false".equalsIgnoreCase(value) ) {
            return false;
        } else {
            throw new RuntimeException("Property " + name +
                    " (= " + value + ") is not a valid boolean " +
                    "(expected 'true' or 'false')");
        }
    }
}
