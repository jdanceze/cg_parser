package soot.options;

import java.util.Map;
import soot.PhaseOptions;
/* loaded from: gencallgraphv3.jar:soot/options/FRWOptions.class */
public class FRWOptions {
    private Map<String, String> options;

    public FRWOptions(Map<String, String> options) {
        this.options = options;
    }

    public boolean enabled() {
        return PhaseOptions.getBoolean(this.options, "enabled");
    }

    public int threshold() {
        return PhaseOptions.getInt(this.options, "threshold");
    }
}
