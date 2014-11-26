package some.other.pkg;

import org.p2s.Settings;

public class Outer {

    @Settings
    public interface InnerSettings {
        Long connectionTimeout();
        String url();
    }

    public static class Other {
        @Settings
        public interface InnerInnerSettings {
            Long connectionTimeout();
            String url();
        }
    }
}
