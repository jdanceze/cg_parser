package soot.options;

import java.util.Map;
import soot.PhaseOptions;
/* loaded from: gencallgraphv3.jar:soot/options/AETOptions.class */
public class AETOptions {
    private Map<String, String> options;
    public static final int kind_optimistic = 1;
    public static final int kind_pessimistic = 2;

    public AETOptions(Map<String, String> options) {
        this.options = options;
    }

    public boolean enabled() {
        return PhaseOptions.getBoolean(this.options, "enabled");
    }

    public int kind() {
        String s = PhaseOptions.getString(this.options, "kind");
        if (s == null || s.isEmpty() || s.equalsIgnoreCase("optimistic")) {
            return 1;
        }
        if (s.equalsIgnoreCase("pessimistic")) {
            return 2;
        }
        throw new RuntimeException(String.format("Invalid value %s of phase option kind", s));
    }
}
