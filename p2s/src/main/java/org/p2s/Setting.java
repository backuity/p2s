package org.p2s;

public class Setting {
    private final boolean isNestedType;
    private String name, type;
    private boolean isOptional;

    public Setting(String name, String type, boolean isOptional, boolean isNestedType) {
        this.name = name;
        this.type = type;
        this.isOptional = isOptional;
        this.isNestedType = isNestedType;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public boolean isNestedType() {
        return isNestedType;
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
