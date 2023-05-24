package soot.jimple.toolkits.annotation.liveness;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.ValueBox;
import soot.jimple.Stmt;
import soot.tagkit.ColorTag;
import soot.tagkit.StringTag;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.scalar.LiveLocals;
import soot.toolkits.scalar.SimpleLiveLocals;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/liveness/LiveVarsTagger.class */
public class LiveVarsTagger extends BodyTransformer {
    public LiveVarsTagger(Singletons.Global g) {
    }

    public static LiveVarsTagger v() {
        return G.v().soot_jimple_toolkits_annotation_liveness_LiveVarsTagger();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map options) {
        LiveLocals sll = new SimpleLiveLocals(ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b));
        Iterator it = b.getUnits().iterator();
        while (it.hasNext()) {
            Stmt s = (Stmt) it.next();
            for (Local v : sll.getLiveLocalsAfter(s)) {
                s.addTag(new StringTag("Live Variable: " + v, "Live Variable"));
                for (ValueBox use : s.getUseBoxes()) {
                    if (use.getValue().equals(v)) {
                        use.addTag(new ColorTag(1, "Live Variable"));
                    }
                }
                for (ValueBox def : s.getDefBoxes()) {
                    if (def.getValue().equals(v)) {
                        def.addTag(new ColorTag(1, "Live Variable"));
                    }
                }
            }
        }
    }
}
