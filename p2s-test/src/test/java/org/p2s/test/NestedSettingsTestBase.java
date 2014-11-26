package org.p2s.test;

import org.junit.Test;
import org.p2s.SettingsFactory;
import some.other.pkg.ParentSettings;

import static org.fest.assertions.Assertions.assertThat;

public abstract class NestedSettingsTestBase {

    public abstract SettingsFactory<?> from(String name);

    @Test
    public void nestedSettings() {
        ParentSettings settings = from("parents").load(ParentSettings.class);

        assertThat(settings.mother().firstName()).isEqualTo("Alice");
        assertThat(settings.mother().lastName()).isEqualTo("Watt");
        assertThat(settings.mother().age().intValue()).isEqualTo(31);
        assertThat(settings.mother().address().street()).isEqualTo("3 rue de la paix");
        assertThat(settings.mother().address().city()).isEqualTo("Toulon");

        assertThat(settings.father().firstName()).isEqualTo("Cab");
        assertThat(settings.father().lastName()).isEqualTo("Calloway");
        assertThat(settings.father().age().intValue()).isEqualTo(42);
        assertThat(settings.father().address().street()).isEqualTo("28 av. de la republique");
        assertThat(settings.father().address().city()).isEqualTo("Lausanne");
    }
}
