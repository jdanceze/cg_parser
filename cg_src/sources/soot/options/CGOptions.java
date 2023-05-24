package soot.options;

import java.util.Map;
import soot.PhaseOptions;
/* loaded from: gencallgraphv3.jar:soot/options/CGOptions.class */
public class CGOptions {
    private Map<String, String> options;
    public static final int library_disabled = 1;
    public static final int library_any_subtype = 2;
    public static final int library_signature_resolution = 3;

    public CGOptions(Map<String, String> options) {
        this.options = options;
    }

    public boolean enabled() {
        return PhaseOptions.getBoolean(this.options, "enabled");
    }

    public boolean safe_forname() {
        return PhaseOptions.getBoolean(this.options, "safe-forname");
    }

    public boolean safe_newinstance() {
        return PhaseOptions.getBoolean(this.options, "safe-newinstance");
    }

    public boolean verbose() {
        return PhaseOptions.getBoolean(this.options, "verbose");
    }

    public boolean all_reachable() {
        return PhaseOptions.getBoolean(this.options, "all-reachable");
    }

    public boolean implicit_entry() {
        return PhaseOptions.getBoolean(this.options, "implicit-entry");
    }

    public boolean trim_clinit() {
        return PhaseOptions.getBoolean(this.options, "trim-clinit");
    }

    public boolean types_for_invoke() {
        return PhaseOptions.getBoolean(this.options, "types-for-invoke");
    }

    public boolean resolve_all_abstract_invokes() {
        return PhaseOptions.getBoolean(this.options, "resolve-all-abstract-invokes");
    }

    public int jdkver() {
        return PhaseOptions.getInt(this.options, "jdkver");
    }

    public String reflection_log() {
        return PhaseOptions.getString(this.options, "reflection-log");
    }

    public String guards() {
        return PhaseOptions.getString(this.options, "guards");
    }

    public int library() {
        String s = PhaseOptions.getString(this.options, "library");
        if (s == null || s.isEmpty() || s.equalsIgnoreCase("disabled")) {
            return 1;
        }
        if (s.equalsIgnoreCase("any-subtype")) {
            return 2;
        }
        if (s.equalsIgnoreCase("signature-resolution")) {
            return 3;
        }
        throw new RuntimeException(String.format("Invalid value %s of phase option library", s));
    }
}
