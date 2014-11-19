package org.p2s;

import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class JulDotCasePropertiesTest {

    @Test
    public void listsMustBeOrdered() {
        Properties p = new Properties();
        p.put("my.list.1", "banana");
        p.put("my.list.2", "apple");
        p.put("my.list.3", "pear");

        p.put("another.list.1", "one");
        p.put("another.list.100", "hundred");
        p.put("another.list.20", "twenty");

        JulDotCaseProperties dc = new JulDotCaseProperties(p);

        assertThat(dc.getProperties("my.list")).containsSequence(
                "banana", "apple", "pear");
        assertThat(dc.getProperties("another.list")).containsSequence(
                "one", "twenty", "hundred");
    }

    @Test
    public void nonParseableId() {
        Properties p = new Properties();
        p.put("my.list.1", "banana");
        p.put("my.list.1+", "banana");
        JulDotCaseProperties dc = new JulDotCaseProperties(p);
        try {
            dc.getProperties("my.list");
            fail("Should fail");
        } catch(IllegalStateException e) {
            assertThat(e).hasMessage("Cannot parse id 1+ of list key my.list.1+");
        }
    }

    @Test
    public void intList() {
        Properties p = new Properties();
        p.put("int.list.1", "12");
        p.put("int.list.2", "42");
        p.put("int.list.3", "27");

        JulDotCaseProperties dc = new JulDotCaseProperties(p);
        assertThat(dc.getProperties("int.list", Integer.class)).containsSequence(12, 42, 27);
    }

    @Test
    public void booleanList() {
        Properties p = new Properties();
        p.put("boolean.list.1", "false");
        p.put("boolean.list.2", "true");

        JulDotCaseProperties dc = new JulDotCaseProperties(p);

        assertThat(dc.getProperties("boolean.list", Boolean.class)).containsSequence(false, true);
    }

    @Test
    public void missingKeyShouldReturnAnEmptyList() {
        JulDotCaseProperties dc = new JulDotCaseProperties(new Properties());
        assertThat(dc.getProperties("missing")).isEmpty();
    }

    public static class Address {
        public String city, street;

        public Address(String city, String street) {
            this.city = city;
            this.street = street;
        }
    }

    @Test
    public void customList() {
        Properties p = new Properties();
        p.put("addresses.1.city", "Marseille");
        p.put("addresses.1.street", "Rue du bourg");

        p.put("addresses.2.city", "NYC");
        p.put("addresses.2.street", "1st avenue");

        JulDotCaseProperties dc = new JulDotCaseProperties(p);
        List<Address> addresses = dc.getProperties("addresses", conf -> new Address(conf.loadMandatory("city"), conf.loadMandatory("street")));
        assertThat(addresses.get(0).city).isEqualTo("Marseille");
        assertThat(addresses.get(0).street).isEqualTo("Rue du bourg");
        assertThat(addresses.get(1).city).isEqualTo("NYC");
        assertThat(addresses.get(1).street).isEqualTo("1st avenue");
    }
}
