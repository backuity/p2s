package some.other.pkg;

import org.p2s.Settings;

@Settings
public interface ParentSettings {
    PersonSettings mother();
    PersonSettings father();
}
