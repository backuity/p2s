package org.p2s.test;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.p2s.JulSettingsFactory;
import org.p2s.SettingsFactory;
import some.other.pkg.SomeSettings;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(Enclosed.class)
public class JulTest {
    private static JulSettingsFactory from(String name) {
        return JulSettingsFactory.from("jul/" + name + ".properties");
    }

    public static class Conversion extends ConversionTestBase {
        @Override public SettingsFactory<?> from(String name) { return JulTest.from(name); }
    }

    public static class Unwrapped extends UnwrappedSettingsTestBase {
        @Override public SettingsFactory<?> from(String name) { return JulTest.from(name); }
    }

    public static class Settings extends SettingsTestBase {
        @Override public SettingsFactory<?> from(String name) { return JulTest.from(name); }
    }

    public static class SettingsOverride extends SettingsOverrideTestBase {
        @Override public SettingsFactory<?> from(String name) { return JulTest.from(name); }
    }

    public static class List extends ListTestBase {
        @Override public SettingsFactory<?> from(String name) { return JulTest.from(name); }
    }

    public static class Fallback extends FallbackTestBase {
        @Override public JulSettingsFactory from(String name) { return JulTest.from(name); }

        @Test
        public void fallbackFromClasspath() {
            SomeSettings someSettings = from("missing-mandatory-setting")
                    .withFallback("jul/some-settings-fallback.properties")
                    .load(SomeSettings.class);
            assertThat(someSettings.name()).isEqualTo("james");
        }

        @Test
        public void missingMandatoryWithFallback() {
            try {
                from("missing-mandatory-setting")
                        .withFallback("jul/parents.properties")
                        .load(SomeSettings.class);
                fail("missing-mandatory-setting.properties should not be loaded");
            } catch (RuntimeException e) {
                assertThat(e.getMessage()).isEqualTo("Cannot load properties " +
                        "jul/missing-mandatory-setting.properties " +
                        "with fallback jul/parents.properties, Cannot find mandatory setting name");
            }
        }
    }
}
