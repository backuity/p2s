package org.p2s;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigUtil;

public class HoconSettingsFactory extends SettingsFactory<HoconDotCaseProperties> {

    protected HoconSettingsFactory(HoconDotCaseProperties properties, String source) {
        super(properties, source);
    }

    public static HoconSettingsFactory from(String fromClassPath) {
        if( HoconSettingsFactory.class.getClassLoader().getResourceAsStream(fromClassPath) == null ) {
            throw new RuntimeException("Cannot find " + fromClassPath + " in the classpath");
        }
        Config config = ConfigFactory.load(HoconSettingsFactory.class.getClassLoader(), fromClassPath);
        return new HoconSettingsFactory(new HoconDotCaseProperties(config), fromClassPath);
    }
}
