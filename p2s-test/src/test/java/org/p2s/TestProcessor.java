package org.p2s;

import some.other.pkg.SomeSettings;

public class TestProcessor {
    public static void main(String[] args) {
        SomeSettings settings = SettingsFactory.loadFromProperties("camel-case-to-dot-case.properties", SomeSettings.class);
        System.out.println( "name = " + settings.name() );
        System.out.println( "timeout = " + settings.timeout() );
    }
}
