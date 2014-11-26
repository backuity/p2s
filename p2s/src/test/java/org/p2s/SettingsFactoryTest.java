package org.p2s;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class SettingsFactoryTest {

    public static class A {
        public static class B {
            public interface SomeInterface {}
        }
    }

    @Test
    public void propertyClassNameTest() {
        assertThat(SettingsFactory.propertyClassName(A.B.SomeInterface.class)).isEqualTo(
                "org.p2s.SettingsFactoryTest$A$B$SomeInterfaceProperties");
    }
}
