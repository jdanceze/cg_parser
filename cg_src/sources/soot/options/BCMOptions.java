package soot.options;

import java.util.Map;
import soot.PhaseOptions;
/* loaded from: gencallgraphv3.jar:soot/options/BCMOptions.class */
public class BCMOptions {
    private Map<String, String> options;

    public BCMOptions(Map<String, String> options) {
        this.options = options;
    }

    public boolean enabled() {
        return PhaseOptions.getBoolean(this.options, "enabled");
    }

    public boolean naive_side_effect() {
        return PhaseOptions.getBoolean(this.options, "naive-side-effect");
    }
}
