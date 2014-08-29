package org.p2s;

public class SettingOverride {
    private String key, value;

    SettingOverride(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
