package soot.jimple.toolkits.annotation.defs;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.tagkit.LinkTag;
import soot.toolkits.scalar.LocalDefs;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/defs/ReachingDefsTagger.class */
public class ReachingDefsTagger extends BodyTransformer {
    public ReachingDefsTagger(Singletons.Global g) {
    }

    public static ReachingDefsTagger v() {
        return G.v().soot_jimple_toolkits_annotation_defs_ReachingDefsTagger();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        LocalDefs ld = G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs(b);
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit s = it.next();
            for (ValueBox vbox : s.getUseBoxes()) {
                Value v = vbox.getValue();
                if (v instanceof Local) {
                    Local l = (Local) v;
                    for (Unit next : ld.getDefsOfAt(l, s)) {
                        String info = l + " has reaching def: " + next;
                        String className = b.getMethod().getDeclaringClass().getName();
                        s.addTag(new LinkTag(info, next, className, "Reaching Defs"));
                    }
                }
            }
        }
    }
}
