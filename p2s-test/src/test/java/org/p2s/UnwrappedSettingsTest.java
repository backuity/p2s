package org.p2s;

import org.junit.Test;
import some.other.pkg.ParentSettings;

import static org.fest.assertions.Assertions.assertThat;

public class UnwrappedSettingsTest {
    @Test
    public void unwrappedSettings() {
        ParentSettings settings = SettingsFactory.loadFromProperties("parents.properties", ParentSettings.class);

        assertThat(settings.mother().firstName()).isEqualTo("Alice");
        assertThat(settings.mother().lastName()).isEqualTo("Watt");
        assertThat(settings.mother().age().intValue()).isEqualTo(31);
        assertThat(settings.mother().address().street()).isEqualTo("3 rue de la paix");
        assertThat(settings.mother().address().city()).isEqualTo("Toulon");

        assertThat(settings.father().firstName()).isEqualTo("Bob");
        assertThat(settings.father().lastName()).isEqualTo("Callaway");
        assertThat(settings.father().age().intValue()).isEqualTo(42);
        assertThat(settings.father().address().street()).isEqualTo("28 av. de la republique");
        assertThat(settings.father().address().city()).isEqualTo("Lausanne");
    }
}
