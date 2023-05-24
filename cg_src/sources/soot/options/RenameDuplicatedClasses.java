package soot.options;

import java.util.Map;
import soot.PhaseOptions;
/* loaded from: gencallgraphv3.jar:soot/options/RenameDuplicatedClasses.class */
public class RenameDuplicatedClasses {
    private Map<String, String> options;

    public RenameDuplicatedClasses(Map<String, String> options) {
        this.options = options;
    }

    public boolean enabled() {
        return PhaseOptions.getBoolean(this.options, "enabled");
    }

    public String fixed_class_names() {
        return PhaseOptions.getString(this.options, "fcn fixed-class-names");
    }
}
