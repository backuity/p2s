package org.p2s;

public class Setting {
    private String name, type;
    private boolean isOptional;

    public Setting(String name, String type, boolean isOptional) {
        this.name = name;
        this.type = type;
        this.isOptional = isOptional;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public static SettingOverride override(String key, String value) {
        return new SettingOverride(key, value);
    }
}
