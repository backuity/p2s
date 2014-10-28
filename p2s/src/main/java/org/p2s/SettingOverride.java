package org.p2s;

public class SettingOverride {
    private String key, value;

    /**
     * @param key non null
     * @throws NullPointerException if key is null
     */
    SettingOverride(String key, String value) {
        if( key == null ) throw new NullPointerException("SettingOverride key cannot be null");
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
