package soot.jimple.toolkits.annotation.purity;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.options.PurityOptions;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/purity/PurityAnalysis.class */
public class PurityAnalysis extends SceneTransformer {
    private static final Logger logger = LoggerFactory.getLogger(PurityAnalysis.class);

    public PurityAnalysis(Singletons.Global g) {
    }

    public static PurityAnalysis v() {
        return G.v().soot_jimple_toolkits_annotation_purity_PurityAnalysis();
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> options) {
        PurityOptions opts = new PurityOptions(options);
        logger.debug("[AM] Analysing purity");
        Scene sc = Scene.v();
        new PurityInterproceduralAnalysis(sc.getCallGraph(), sc.getEntryPoints().iterator(), opts);
    }
}
