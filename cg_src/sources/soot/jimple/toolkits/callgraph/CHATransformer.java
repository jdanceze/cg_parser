package soot.jimple.toolkits.callgraph;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.jimple.toolkits.pointer.DumbPointerAnalysis;
import soot.options.CHAOptions;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/CHATransformer.class */
public class CHATransformer extends SceneTransformer {
    private static final Logger logger = LoggerFactory.getLogger(CHATransformer.class);

    public CHATransformer(Singletons.Global g) {
    }

    public static CHATransformer v() {
        return G.v().soot_jimple_toolkits_callgraph_CHATransformer();
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> opts) {
        CHAOptions options = new CHAOptions(opts);
        CallGraphBuilder cg = options.apponly() ? new CallGraphBuilder() : new CallGraphBuilder(DumbPointerAnalysis.v());
        cg.build();
        if (options.verbose()) {
            logger.debug("Number of reachable methods: " + Scene.v().getReachableMethods().size());
        }
    }
}
