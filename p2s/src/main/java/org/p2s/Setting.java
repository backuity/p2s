package org.p2s;

public class Setting {
    private final boolean isNestedType;
    private String name, type, pkg;
    private boolean isOptional;

    public Setting(String name, String type, String pkg, boolean isOptional, boolean isNestedType) {
        this.name = name;
        this.type = type;
        this.isOptional = isOptional;
        this.isNestedType = isNestedType;
        this.pkg = pkg;
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

    public String getPkg() {
        return pkg;
    }

    public static SettingOverride override(String key, String value) {
        return new SettingOverride(key, value);
    }
}
