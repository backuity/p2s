package some.other.pkg;

import org.p2s.Settings;

import java.util.Optional;

@Settings
public interface ConversionSettings {

    Optional<Integer> timeout();
    String name();
    String theSurname();
    Long timestamp();
    Boolean activate();

    long primitiveTimestamp();
    int primitiveTimeout();
    boolean primitiveActivate();
}
