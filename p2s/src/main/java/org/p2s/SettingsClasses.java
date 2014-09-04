package org.p2s;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SettingsClasses {

    private Map<String, SettingsClass> classes = new HashMap<>();

    public void add(SettingsClass clazz) {
        classes.put(clazz.getClassName(), clazz);
    }

    public boolean contains(String fqn) {
        return classes.containsKey(fqn);
    }

    public Set<SettingsClass> getClasses() {
        return new HashSet<>(classes.values());
    }
}
