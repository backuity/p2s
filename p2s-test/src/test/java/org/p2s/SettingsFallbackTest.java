package org.p2s;

import org.junit.Test;
import some.other.pkg.SomeSettings;

import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class SettingsFallbackTest {

    @Test
    public void withFallback() {
        try {
            SettingsFactory.loadFromProperties("missing-mandatory-setting.properties", SomeSettings.class);
            fail("missing-mandatory-setting.properties should not be loaded");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).contains("Cannot find mandatory setting name");
        }

        SomeSettings someSettings = SettingsFactory.from("missing-mandatory-setting.properties")
                .withFallback("name", "toto")
                .load(SomeSettings.class);

        assertThat(someSettings.name()).isEqualTo("toto");
        assertThat(someSettings.timeout()).isEqualTo(Optional.of(1234));
    }

    @Test
    public void fallbackShouldNotOverrideExistingValue(){

        SomeSettings someSettings = SettingsFactory.from("missing-mandatory-setting.properties")
                .withFallback("name", "toto")
                .withFallback("timeout", "1111")
                .load(SomeSettings.class);

        assertThat(someSettings.timeout()).isEqualTo(Optional.of(1234));
    }

    @Test
    public void overrideFallback() {
        SomeSettings someSettings = SettingsFactory.from("missing-mandatory-setting.properties")
                .withFallback("name", "toto")
                .override("name", "overriden-name")
                .load(SomeSettings.class);

        assertThat(someSettings.name()).isEqualTo("overriden-name");
    }

    @Test
    public void fallbackFromClasspath() {
        SomeSettings someSettings = SettingsFactory.from("missing-mandatory-setting.properties")
                .withFallback("some-settings-fallback.properties")
                .load(SomeSettings.class);
        assertThat(someSettings.name()).isEqualTo("james");
    }

    @Test
    public void missingMandatoryWithFallback() {
        try {
            SettingsFactory.from("missing-mandatory-setting.properties")
                    .withFallback("parents.properties")
                    .load(SomeSettings.class);
            fail("missing-mandatory-setting.properties should not be loaded");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Cannot load properties " +
                    "missing-mandatory-setting.properties " +
                    "with fallback parents.properties, Cannot find mandatory setting name");
        }
    }
}
