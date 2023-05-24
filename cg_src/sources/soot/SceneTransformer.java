package soot;

import java.util.HashMap;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:soot/SceneTransformer.class */
public abstract class SceneTransformer extends Transformer {
    protected abstract void internalTransform(String str, Map<String, String> map);

    public final void transform(String phaseName, Map<String, String> options) {
        if (!PhaseOptions.getBoolean(options, "enabled")) {
            return;
        }
        internalTransform(phaseName, options);
    }

    public final void transform(String phaseName) {
        HashMap<String, String> dummyOptions = new HashMap<>();
        dummyOptions.put("enabled", "true");
        transform(phaseName, dummyOptions);
    }

    public final void transform() {
        transform("");
    }
}
