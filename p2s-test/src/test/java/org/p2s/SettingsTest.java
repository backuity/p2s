package org.p2s;

import org.junit.Test;
import some.other.pkg.SomeSettings;

import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class SettingsTest {

    @Test
    public void camelCaseSettingShouldBeConvertedToDotCaseProperty() {
        SomeSettings settings = SettingsFactory.loadFromProperties("camel-case-to-dot-case.properties", SomeSettings.class);
        assertThat(settings.theSurname()).isEqualTo("Toto");
    }

    @Test
    public void loadOptionalInteger() {
        SomeSettings settings = SettingsFactory.loadFromProperties("camel-case-to-dot-case.properties", SomeSettings.class);
        assertThat(settings.timeout()).isEqualTo(Optional.of(1234));
    }

    @Test
    public void loadLong() {
        SomeSettings settings = SettingsFactory.loadFromProperties("camel-case-to-dot-case.properties", SomeSettings.class);
        assertThat(settings.timestamp()).isEqualTo(123456789012345L);
    }

    @Test
    public void loadBoolean() {
        SomeSettings settings = SettingsFactory.loadFromProperties("camel-case-to-dot-case.properties", SomeSettings.class);
        assertThat(settings.activate()).isTrue();
    }

    @Test
    public void unparseableIntegerShouldFailWhenLoading() {
        try {
            SettingsFactory.loadFromProperties("invalid-integer.properties", SomeSettings.class);
            fail("invalid-integer.properties should not be loaded");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo(
                    "Cannot load properties invalid-integer.properties, " +
                            "Property timeout (= 1234m) is not a valid integer");
        }
    }

    @Test
    public void missingMandatorySettingShouldFailWhenLoading() {
        try {
            SettingsFactory.loadFromProperties("missing-mandatory-setting.properties", SomeSettings.class);
            fail("missing-mandatory-setting.properties should not be loaded");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo(
                    "Cannot load properties missing-mandatory-setting.properties, " +
                            "Cannot find mandatory setting name");
        }
    }
}
