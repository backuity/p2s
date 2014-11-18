package org.p2s;

import org.junit.Test;
import some.other.pkg.SomeSettings;

import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public abstract class SettingsTestBase {

    public abstract SettingsFactory<?> from(String name);

    @Test
    public void missingFileShouldFail() {
        try {
            from("unknown-file").load(SomeSettings.class);
            fail("unknown-file should not be found");
        } catch (RuntimeException e) {
            assertThat(e.getMessage())
                    .contains("Cannot find")
                    .contains("unknown-file")
                    .contains("in the classpath");
        }
    }

    @Test
    public void camelCaseSettingShouldBeConvertedToDotCaseProperty() {
        SomeSettings settings = from("camel-case-to-dot-case").load(SomeSettings.class);
        assertThat(settings.theSurname()).isEqualTo("Toto");
    }

    @Test
    public void missingMandatorySettingShouldFailWhenLoading() {
        try {
            from("missing-mandatory-setting").load(SomeSettings.class);
            fail("missing-mandatory-setting.properties should not be loaded");
        } catch (RuntimeException e) {
            assertThat(e.getMessage())
                    .contains("Cannot load properties")
                    .contains("missing-mandatory-setting")
                    .contains("Cannot find mandatory setting name");
        }
    }
}
