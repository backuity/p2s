package org.p2s;

import org.junit.Test;
import some.other.pkg.SomeSettings;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SettingsTest {

    @Test
    public void camelCaseSettingShouldBeConvertedToDotCaseProperty() {
        SomeSettings settings = SettingsFactory.loadFromProperties("camel-case-to-dot-case.properties", SomeSettings.class);
        assertEquals("Toto", settings.theSurname());
    }

    @Test
    public void loadOptionalInteger() {
        SomeSettings settings = SettingsFactory.loadFromProperties("camel-case-to-dot-case.properties", SomeSettings.class);
        assertEquals(Optional.of(1234), settings.timeout());
    }

    @Test
    public void loadLong() {
        SomeSettings settings = SettingsFactory.loadFromProperties("camel-case-to-dot-case.properties", SomeSettings.class);
        assertEquals(new Long(123456789012345L), settings.timestamp());
    }

    @Test
    public void loadBoolean() {
        SomeSettings settings = SettingsFactory.loadFromProperties("camel-case-to-dot-case.properties", SomeSettings.class);
        assertEquals(true, settings.activate());
    }

    @Test
    public void overrideProperty() {
        SomeSettings settings = SettingsFactory.loadFromProperties(
                "camel-case-to-dot-case.properties", SomeSettings.class,
                Setting.override("activate", "false"),
                Setting.override("timeout", "987654"),
                Setting.override("the.surname", "janine"));

        assertEquals(false, settings.activate());
        assertEquals("janine", settings.theSurname());
        assertEquals(Optional.of(987654), settings.timeout());
    }

    @Test
    public void unparseableIntegerShouldFailWhenLoading() {
        try {
            SettingsFactory.loadFromProperties("invalid-integer.properties", SomeSettings.class);
            fail("invalid-integer.properties should not be loaded");
        } catch (RuntimeException e) {
            assertEquals("Cannot load properties invalid-integer.properties, " +
                            "Property timeout (= 1234m) is not a valid integer", e.getMessage());
        }
    }

    @Test
    public void missingMandatorySettingShouldFailWhenLoading() {
        try {
            SettingsFactory.loadFromProperties("missing-mandatory-setting.properties", SomeSettings.class);
            fail("missing-mandatory-setting.properties should not be loaded");
        } catch (RuntimeException e) {
            assertEquals("Cannot load properties missing-mandatory-setting.properties, " +
                            "Cannot find mandatory setting name", e.getMessage());
        }
    }
}
