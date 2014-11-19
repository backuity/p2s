package some.other.pkg;

import org.p2s.Settings;
import yet.another.pkg.AddressSettings;

import java.util.List;

@Settings
public interface ListSettings {
    List<String> languages();
    List<Integer> sizes();
    List<Integer> empty();
    List<AddressSettings> addresses();
}
