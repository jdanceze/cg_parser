package soot;

import java.util.Collections;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:soot/BodyTransformer.class */
public abstract class BodyTransformer extends Transformer {
    private static final Map<String, String> enabledOnlyMap = Collections.singletonMap("enabled", "true");

    protected abstract void internalTransform(Body body, String str, Map<String, String> map);

    public final void transform(Body b, String phaseName, Map<String, String> options) {
        if (PhaseOptions.getBoolean(options, "enabled")) {
            internalTransform(b, phaseName, options);
        }
    }

    public final void transform(Body b, String phaseName) {
        internalTransform(b, phaseName, enabledOnlyMap);
    }

    public final void transform(Body b) {
        transform(b, "");
    }
}
