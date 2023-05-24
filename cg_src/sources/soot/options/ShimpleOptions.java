package soot.options;

import java.util.Map;
import polyglot.main.Report;
import soot.PhaseOptions;
/* loaded from: gencallgraphv3.jar:soot/options/ShimpleOptions.class */
public class ShimpleOptions {
    private Map<String, String> options;

    public ShimpleOptions(Map<String, String> options) {
        this.options = options;
    }

    public boolean enabled() {
        return PhaseOptions.getBoolean(this.options, "enabled");
    }

    public boolean node_elim_opt() {
        return PhaseOptions.getBoolean(this.options, "node-elim-opt");
    }

    public boolean standard_local_names() {
        return PhaseOptions.getBoolean(this.options, "standard-local-names");
    }

    public boolean extended() {
        return PhaseOptions.getBoolean(this.options, "extended");
    }

    public boolean debug() {
        return PhaseOptions.getBoolean(this.options, Report.debug);
    }
}
