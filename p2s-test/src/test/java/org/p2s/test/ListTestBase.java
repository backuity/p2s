package org.p2s.test;

import org.junit.Test;
import org.p2s.SettingsFactory;
import some.other.pkg.ListSettings;

import static org.fest.assertions.Assertions.assertThat;

public abstract class ListTestBase {

    public abstract SettingsFactory<?> from(String name);

    @Test
    public void listSettings() {
        ListSettings settings = from("list").load(ListSettings.class);
        assertThat(settings.empty()).isEmpty();
        assertThat(settings.languages()).containsSequence("fr", "en", "it");
        assertThat(settings.sizes()).containsSequence(12, 200, 3);
        assertThat(settings.addresses().get(0).city()).isEqualTo("Marseille");
        assertThat(settings.addresses().get(0).street()).isEqualTo("Rue du bourg");
        assertThat(settings.addresses().get(1).city()).isEqualTo("NYC");
        assertThat(settings.addresses().get(1).street()).isEqualTo("1st avenue");
    }
}
