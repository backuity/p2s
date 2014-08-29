package some.other.pkg;

import org.p2s.Settings;

import java.util.Optional;

@Settings
public interface SomeSettings {

    Optional<Integer> timeout();
    String name();
    String theSurname();
    Long timestamp();
    Boolean activate();
}
