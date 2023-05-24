package soot.options;

import java.util.Map;
import soot.PhaseOptions;
/* loaded from: gencallgraphv3.jar:soot/options/LCMOptions.class */
public class LCMOptions {
    private Map<String, String> options;
    public static final int safety_safe = 1;
    public static final int safety_medium = 2;
    public static final int safety_unsafe = 3;

    public LCMOptions(Map<String, String> options) {
        this.options = options;
    }

    public boolean enabled() {
        return PhaseOptions.getBoolean(this.options, "enabled");
    }

    public boolean unroll() {
        return PhaseOptions.getBoolean(this.options, "unroll");
    }

    public boolean naive_side_effect() {
        return PhaseOptions.getBoolean(this.options, "naive-side-effect");
    }

    public int safety() {
        String s = PhaseOptions.getString(this.options, "safety");
        if (s == null || s.isEmpty() || s.equalsIgnoreCase("safe")) {
            return 1;
        }
        if (s.equalsIgnoreCase("medium")) {
            return 2;
        }
        if (s.equalsIgnoreCase("unsafe")) {
            return 3;
        }
        throw new RuntimeException(String.format("Invalid value %s of phase option safety", s));
    }
}
