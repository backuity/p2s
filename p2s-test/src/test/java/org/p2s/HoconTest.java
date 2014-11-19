package org.p2s;

import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class HoconTest {

    private static HoconSettingsFactory from(String name) {
        return HoconSettingsFactory.from("hocon/" + name + ".conf");
    }

    public static class Conversion extends ConversionTestBase {
        @Override public HoconSettingsFactory from(String name) { return HoconTest.from(name); }
    }

    public static class Unwrapped extends UnwrappedSettingsTestBase {
        @Override public HoconSettingsFactory from(String name) { return HoconTest.from(name); }
    }

    public static class List extends ListTestBase {
        @Override public SettingsFactory<?> from(String name) { return HoconTest.from(name); }
    }

    public static class Settings extends SettingsTestBase {
        @Override public SettingsFactory<?> from(String name) { return HoconTest.from(name); }
    }

    public static class SettingsOverride extends SettingsOverrideTestBase {
        @Override public SettingsFactory<?> from(String name) { return HoconTest.from(name); }
    }

    public static class Fallback extends FallbackTestBase {
        @Override public SettingsFactory<?> from(String name) { return HoconTest.from(name); }
    }
}
