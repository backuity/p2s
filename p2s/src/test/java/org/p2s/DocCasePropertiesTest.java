package org.p2s;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DocCasePropertiesTest {
    @Test
    public void parsePrimitiveInt() {
        assertThat(DotCaseProperties.parse("some.int", "12", Integer.TYPE)).isEqualTo(12);
    }
    @Test
    public void parsePrimitiveLong() {
        assertThat(DotCaseProperties.parse("some.long", "1234567890", Long.TYPE)).isEqualTo(1234567890);
    }
    @Test
    public void parsePrimitiveBoolean() {
        assertThat(DotCaseProperties.parse("some.boolean", "true", Boolean.TYPE)).isEqualTo(true);
    }
}
