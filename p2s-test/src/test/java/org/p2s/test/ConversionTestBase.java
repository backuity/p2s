package org.p2s.test;

import org.junit.Test;
import org.p2s.SettingsFactory;
import some.other.pkg.ConversionSettings;
import some.other.pkg.SomeSettings;

import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public abstract class ConversionTestBase {

    public abstract SettingsFactory<?> from(String name);

    @Test
    public void loadOptionalInteger() {
        ConversionSettings settings = from("camel-case-to-dot-case").load(ConversionSettings.class);
        assertThat(settings.timeout()).isEqualTo(Optional.of(1234));
    }

    @Test
    public void loadPrimitiveInteger() {
        ConversionSettings settings = from("camel-case-to-dot-case").load(ConversionSettings.class);
        assertThat(settings.primitiveTimeout()).isEqualTo(1234);
    }

    @Test
    public void loadLong() {
        ConversionSettings settings = from("camel-case-to-dot-case").load(ConversionSettings.class);
        assertThat(settings.timestamp()).isEqualTo(123456789012345L);
    }

    @Test
    public void loadPrimitiveLong() {
        ConversionSettings settings = from("camel-case-to-dot-case").load(ConversionSettings.class);
        assertThat(settings.primitiveTimestamp()).isEqualTo(123456789012345L);
    }

    @Test
    public void loadBoolean() {
        ConversionSettings settings = from("camel-case-to-dot-case").load(ConversionSettings.class);
        assertThat(settings.activate()).isTrue();
    }

    @Test
    public void loadPrimitiveBoolean() {
        ConversionSettings settings = from("camel-case-to-dot-case").load(ConversionSettings.class);
        assertThat(settings.primitiveActivate()).isTrue();
    }

    @Test
    public void unparseableIntegerShouldFailWhenLoading() {
        try {
            from("invalid-integer").load(SomeSettings.class);
            fail("invalid-integer should not be loaded");
        } catch (RuntimeException e) {
            assertThat(e.getMessage())
                    .contains("Cannot load properties")
                    .contains("invalid-integer")
                    .contains("Property timeout (= 1234m) is not a valid integer");
        }
    }

    @Test
    public void unparseableLongShouldFailWhenLoading() {
        try {
            from("invalid-long").load(SomeSettings.class);
            fail("invalid-long should not be loaded");
        } catch (RuntimeException e) {
            assertThat(e.getMessage())
                    .contains("Cannot load properties")
                    .contains("invalid-long")
                    .contains("Property timestamp (= 1234567m) is not a valid long");
        }
    }

    @Test
    public void unparseableBooleanShouldFailWhenLoading() {
        try {
            from("invalid-boolean").load(SomeSettings.class);
            fail("invalid-boolean should not be loaded");
        } catch (RuntimeException e) {
            assertThat(e.getMessage())
                    .contains("Cannot load properties")
                    .contains("invalid-boolean")
                    .contains("Property activate (= trux) is not a valid boolean");
        }
    }
}
