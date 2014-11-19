package org.p2s.test;

import org.junit.Test;
import org.p2s.SettingsFactory;
import some.other.pkg.SomeSettings;

import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public abstract class FallbackTestBase {

    public abstract SettingsFactory<?> from(String name);

    @Test
    public void withFallback() {
        try {
            from("missing-mandatory-setting").load(SomeSettings.class);
            fail("missing-mandatory-setting should not be loaded");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).contains("Cannot find mandatory setting name");
        }

        SomeSettings someSettings = from("missing-mandatory-setting")
                .withFallback("name", "toto")
                .load(SomeSettings.class);

        assertThat(someSettings.name()).isEqualTo("toto");
        assertThat(someSettings.timeout()).isEqualTo(Optional.of(1234));
    }

    @Test
    public void fallbackShouldNotOverrideExistingValue(){

        SomeSettings someSettings = from("missing-mandatory-setting")
                .withFallback("name", "toto")
                .withFallback("timeout", "1111")
                .load(SomeSettings.class);

        assertThat(someSettings.timeout()).isEqualTo(Optional.of(1234));
    }

    @Test
    public void overrideFallback() {
        SomeSettings someSettings = from("missing-mandatory-setting")
                .withFallback("name", "toto")
                .override("name", "overriden-name")
                .load(SomeSettings.class);

        assertThat(someSettings.name()).isEqualTo("overriden-name");
    }
}
