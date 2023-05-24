package soot.jimple.toolkits.pointer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.RefLikeType;
import soot.Scene;
import soot.Singletons;
import soot.Unit;
import soot.Value;
import soot.jimple.IdentityStmt;
import soot.jimple.ParameterRef;
import soot.tagkit.ColorTag;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/ParameterAliasTagger.class */
public class ParameterAliasTagger extends BodyTransformer {
    public ParameterAliasTagger(Singletons.Global g) {
    }

    public static ParameterAliasTagger v() {
        return G.v().soot_jimple_toolkits_pointer_ParameterAliasTagger();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Set<IdentityStmt> parms = new HashSet<>();
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof IdentityStmt) {
                IdentityStmt is = (IdentityStmt) u;
                Value value = is.getRightOpBox().getValue();
                if ((value instanceof ParameterRef) && (((ParameterRef) value).getType() instanceof RefLikeType)) {
                    parms.add(is);
                }
            }
        }
        int colour = 0;
        PointsToAnalysis pa = Scene.v().getPointsToAnalysis();
        while (!parms.isEmpty()) {
            int i = colour;
            colour++;
            fill(parms, parms.iterator().next(), i, pa);
        }
    }

    private void fill(Set<IdentityStmt> parms, IdentityStmt parm, int colour, PointsToAnalysis pa) {
        if (parms.contains(parm)) {
            parm.getRightOpBox().addTag(new ColorTag(colour, "Parameter Alias"));
            parms.remove(parm);
            PointsToSet ps = pa.reachingObjects((Local) parm.getLeftOp());
            Iterator it = new LinkedList(parms).iterator();
            while (it.hasNext()) {
                IdentityStmt is = (IdentityStmt) it.next();
                if (ps.hasNonEmptyIntersection(pa.reachingObjects((Local) is.getLeftOp()))) {
                    fill(parms, is, colour, pa);
                }
            }
        }
    }
}
