package org.p2s;

import java.util.List;

class SettingsClass {
    private String interfaceName;
    private String packageName;
    private String simpleClassName;
    private List<Setting> settings;

    public SettingsClass(String packageName, String interfaceName, String simpleClassName, List<Setting> settings) {
        this.packageName = packageName;
        this.interfaceName = interfaceName;
        this.simpleClassName = simpleClassName;
        this.settings = settings;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public String getClassName() {
        return packageName + "." + simpleClassName;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SettingsClass that = (SettingsClass) o;

        if (!packageName.equals(that.packageName)) return false;
        if (!simpleClassName.equals(that.simpleClassName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = packageName.hashCode();
        result = 31 * result + simpleClassName.hashCode();
        return result;
    }
}
