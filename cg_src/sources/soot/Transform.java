package soot;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.options.Options;
import soot.util.PhaseDumper;
/* loaded from: gencallgraphv3.jar:soot/Transform.class */
public class Transform implements HasPhaseOptions {
    private static final Logger logger = LoggerFactory.getLogger(Transform.class);
    private final boolean DEBUG;
    private String declaredOpts;
    private String defaultOpts;
    final String phaseName;
    final Transformer t;

    public Transform(String phaseName, Transformer t) {
        this.DEBUG = Options.v().dump_body().contains(phaseName);
        this.phaseName = phaseName;
        this.t = t;
    }

    @Override // soot.HasPhaseOptions
    public String getPhaseName() {
        return this.phaseName;
    }

    public Transformer getTransformer() {
        return this.t;
    }

    @Override // soot.HasPhaseOptions
    public String getDeclaredOptions() {
        if (this.declaredOpts != null) {
            return this.declaredOpts;
        }
        return Options.getDeclaredOptionsForPhase(this.phaseName);
    }

    @Override // soot.HasPhaseOptions
    public String getDefaultOptions() {
        if (this.defaultOpts != null) {
            return this.defaultOpts;
        }
        return Options.getDefaultOptionsForPhase(this.phaseName);
    }

    public void setDeclaredOptions(String options) {
        this.declaredOpts = options;
    }

    public void setDefaultOptions(String options) {
        this.defaultOpts = options;
    }

    public void apply() {
        Map<String, String> options = PhaseOptions.v().getPhaseOptions(this.phaseName);
        if (PhaseOptions.getBoolean(options, "enabled") && Options.v().verbose()) {
            logger.debug("Applying phase " + this.phaseName + " to the scene.");
        }
        if (this.DEBUG) {
            PhaseDumper.v().dumpBefore(getPhaseName());
        }
        ((SceneTransformer) this.t).transform(this.phaseName, options);
        if (this.DEBUG) {
            PhaseDumper.v().dumpAfter(getPhaseName());
        }
    }

    public void apply(Body b) {
        if (b == null) {
            return;
        }
        Map<String, String> options = PhaseOptions.v().getPhaseOptions(this.phaseName);
        if (PhaseOptions.getBoolean(options, "enabled") && Options.v().verbose()) {
            logger.debug("Applying phase " + this.phaseName + " to " + b.getMethod() + ".");
        }
        if (this.DEBUG) {
            PhaseDumper.v().dumpBefore(b, getPhaseName());
        }
        ((BodyTransformer) this.t).transform(b, this.phaseName, options);
        if (this.DEBUG) {
            PhaseDumper.v().dumpAfter(b, getPhaseName());
        }
    }

    public String toString() {
        return this.phaseName;
    }
}
