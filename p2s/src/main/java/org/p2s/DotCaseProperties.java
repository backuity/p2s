package org.p2s;

import java.util.Optional;

public interface DotCaseProperties<T extends DotCaseProperties> {
    Optional<String> getProperty(String name);

    default boolean containsKey(String key) {
        return getProperty(key).isPresent();
    }

    void setProperty(String key, String value);

    /** @return a new value computed by merging this value with `other`, with
     *          keys in this value "winning" over the other one
     */
    T withFallback(T other);
}
