package soot.options;

import java.util.Map;
import soot.PhaseOptions;
/* loaded from: gencallgraphv3.jar:soot/options/JJOptions.class */
public class JJOptions {
    private Map<String, String> options;

    public JJOptions(Map<String, String> options) {
        this.options = options;
    }

    public boolean enabled() {
        return PhaseOptions.getBoolean(this.options, "enabled");
    }

    public boolean use_original_names() {
        return PhaseOptions.getBoolean(this.options, "use-original-names");
    }
}
