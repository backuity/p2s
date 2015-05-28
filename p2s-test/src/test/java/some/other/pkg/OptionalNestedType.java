package some.other.pkg;

import java.util.Optional;

import org.p2s.Settings;

import yet.another.pkg.AddressSettings;

@Settings
public interface OptionalNestedType {
    Optional<AddressSettings> address();
}
