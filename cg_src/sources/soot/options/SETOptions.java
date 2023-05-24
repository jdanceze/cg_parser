package soot.options;

import java.util.Map;
import soot.PhaseOptions;
/* loaded from: gencallgraphv3.jar:soot/options/SETOptions.class */
public class SETOptions {
    private Map<String, String> options;

    public SETOptions(Map<String, String> options) {
        this.options = options;
    }

    public boolean enabled() {
        return PhaseOptions.getBoolean(this.options, "enabled");
    }

    public boolean naive() {
        return PhaseOptions.getBoolean(this.options, "naive");
    }
}
