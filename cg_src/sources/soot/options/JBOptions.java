package soot.options;

import java.util.Map;
import soot.PhaseOptions;
/* loaded from: gencallgraphv3.jar:soot/options/JBOptions.class */
public class JBOptions {
    private Map<String, String> options;

    public JBOptions(Map<String, String> options) {
        this.options = options;
    }

    public boolean enabled() {
        return PhaseOptions.getBoolean(this.options, "enabled");
    }

    public boolean use_original_names() {
        return PhaseOptions.getBoolean(this.options, "use-original-names");
    }

    public boolean preserve_source_annotations() {
        return PhaseOptions.getBoolean(this.options, "preserve-source-annotations");
    }

    public boolean stabilize_local_names() {
        return PhaseOptions.getBoolean(this.options, "stabilize-local-names");
    }

    public boolean model_lambdametafactory() {
        return PhaseOptions.getBoolean(this.options, "model-lambdametafactory");
    }
}
