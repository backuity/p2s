package org.p2s;

import org.junit.Test;
import some.other.pkg.ParentSettings;

import static org.junit.Assert.assertEquals;

public class UnwrappedSettingsTest {
    @Test
    public void unwrappedSettings() {
        ParentSettings settings = SettingsFactory.loadFromProperties("parents.properties", ParentSettings.class);

        assertEquals("Alice", settings.mother().firstName());
        assertEquals("Watt", settings.mother().lastName());
        assertEquals(31, settings.mother().age().intValue());
        assertEquals("3 rue de la paix", settings.mother().address().street());
        assertEquals("Toulon", settings.mother().address().city());

        assertEquals("Bob", settings.father().firstName());
        assertEquals("Callaway", settings.father().lastName());
        assertEquals(42, settings.father().age().intValue());
        assertEquals("28 av. de la republique", settings.father().address().street());
        assertEquals("Lausanne", settings.father().address().city());
    }
}
