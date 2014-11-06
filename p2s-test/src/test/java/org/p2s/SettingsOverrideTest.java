package org.p2s;

import org.junit.Test;
import some.other.pkg.SomeSettings;

import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class SettingsOverrideTest {

    @Test
    public void overrideProperty() {
        SomeSettings settings = SettingsFactory.loadFromProperties(
                "camel-case-to-dot-case.properties", SomeSettings.class,
                Setting.override("activate", "false"),
                Setting.override("timeout", "987654"),
                Setting.override("the.surname", "janine"));

        assertThat(settings.activate()).isFalse();
        assertThat(settings.theSurname()).isEqualTo("janine");
        assertThat(settings.timeout()).isEqualTo(Optional.of(987654));

        settings = SettingsFactory.from("camel-case-to-dot-case.properties")
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
            SettingsFactory.loadFromProperties(
                    "camel-case-to-dot-case.properties", SomeSettings.class,
                    Setting.override("activate", "false"),
                    Setting.override("wrong.timeout", "987654"));
            fail("Setting override should fail");
        } catch(RuntimeException e) {
            assertThat(e).hasMessage("Setting 'wrong.timeout' does not override anything.");
        }
    }
}
