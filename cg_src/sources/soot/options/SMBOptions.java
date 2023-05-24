package soot.options;

import java.util.Map;
import soot.PhaseOptions;
/* loaded from: gencallgraphv3.jar:soot/options/SMBOptions.class */
public class SMBOptions {
    private Map<String, String> options;
    public static final int allowed_modifier_changes_unsafe = 1;
    public static final int allowed_modifier_changes_safe = 2;
    public static final int allowed_modifier_changes_none = 3;

    public SMBOptions(Map<String, String> options) {
        this.options = options;
    }

    public boolean enabled() {
        return PhaseOptions.getBoolean(this.options, "enabled");
    }

    public boolean insert_null_checks() {
        return PhaseOptions.getBoolean(this.options, "insert-null-checks");
    }

    public boolean insert_redundant_casts() {
        return PhaseOptions.getBoolean(this.options, "insert-redundant-casts");
    }

    public int allowed_modifier_changes() {
        String s = PhaseOptions.getString(this.options, "allowed-modifier-changes");
        if (s == null || s.isEmpty() || s.equalsIgnoreCase("unsafe")) {
            return 1;
        }
        if (s.equalsIgnoreCase("safe")) {
            return 2;
        }
        if (s.equalsIgnoreCase("none")) {
            return 3;
        }
        throw new RuntimeException(String.format("Invalid value %s of phase option allowed-modifier-changes", s));
    }
}
