package soot.options;

import java.util.Map;
import soot.PhaseOptions;
/* loaded from: gencallgraphv3.jar:soot/options/ProfilingOptions.class */
public class ProfilingOptions {
    private Map<String, String> options;

    public ProfilingOptions(Map<String, String> options) {
        this.options = options;
    }

    public boolean enabled() {
        return PhaseOptions.getBoolean(this.options, "enabled");
    }

    public boolean notmainentry() {
        return PhaseOptions.getBoolean(this.options, "notmainentry");
    }
}
