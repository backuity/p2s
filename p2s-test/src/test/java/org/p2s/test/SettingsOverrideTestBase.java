package org.p2s.test;

import org.junit.Test;
import org.p2s.SettingsFactory;
import some.other.pkg.SomeSettings;

import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public abstract class SettingsOverrideTestBase {

    public abstract SettingsFactory<?> from(String name);

    @Test
    public void overrideProperty() {
        SomeSettings settings = from("camel-case-to-dot-case")
                .override("activate", "false")
                .override("timeout", "987654")
                .override("the.surname", "janine")
                .load(SomeSettings.class);

        assertThat(settings.activate()).isFalse();
        assertThat(settings.theSurname()).isEqualTo("janine");
        assertThat(settings.timeout()).isEqualTo(Optional.of(987654));
    }

    @Test
    public void overrideUnexistingPropertyShouldFail() {
        try {
            from("camel-case-to-dot-case")
                    .override("activate", "false")
                    .override("wrong.timeout", "987654")
                    .load(SomeSettings.class);
            fail("Setting override should fail");
        } catch(RuntimeException e) {
            assertThat(e).hasMessage("Setting 'wrong.timeout' does not override anything.");
        }
    }
}
