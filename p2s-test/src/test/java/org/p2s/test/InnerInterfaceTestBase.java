package org.p2s.test;

import org.junit.Test;
import org.p2s.SettingsFactory;
import some.other.pkg.Outer;

import static org.fest.assertions.Assertions.assertThat;

public abstract class InnerInterfaceTestBase {

    public abstract SettingsFactory<?> from(String name);

    @Test
    public void innerSettings() {
        Outer.InnerSettings settings = from("inner-setting").load(Outer.InnerSettings.class);
        assertThat(settings.connectionTimeout()).isEqualTo(12345);
        assertThat(settings.url()).isEqualTo("http://localhost:8080");
    }

    @Test
    public void innerInnerSettings() {
        Outer.Other.InnerInnerSettings settings = from("inner-setting").load(Outer.Other.InnerInnerSettings.class);
        assertThat(settings.connectionTimeout()).isEqualTo(12345);
        assertThat(settings.url()).isEqualTo("http://localhost:8080");
    }
}
