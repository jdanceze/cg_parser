package soot.jimple.toolkits.pointer;

import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.toolkits.graph.BriefUnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/CastCheckEliminatorDumper.class */
public class CastCheckEliminatorDumper extends BodyTransformer {
    public CastCheckEliminatorDumper(Singletons.Global g) {
    }

    public static CastCheckEliminatorDumper v() {
        return G.v().soot_jimple_toolkits_pointer_CastCheckEliminatorDumper();
    }

    public String getDefaultOptions() {
        return "";
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        new CastCheckEliminator(new BriefUnitGraph(b));
    }
}
