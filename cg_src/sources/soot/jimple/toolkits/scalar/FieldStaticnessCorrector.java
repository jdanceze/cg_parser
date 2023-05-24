package soot.jimple.toolkits.scalar;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.ConflictingFieldRefException;
import soot.G;
import soot.Singletons;
import soot.SootField;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/FieldStaticnessCorrector.class */
public class FieldStaticnessCorrector extends AbstractStaticnessCorrector {
    public FieldStaticnessCorrector(Singletons.Global g) {
    }

    public static FieldStaticnessCorrector v() {
        return G.v().soot_jimple_toolkits_scalar_FieldStaticnessCorrector();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        SootField fld;
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof AssignStmt) {
                AssignStmt assignStmt = (AssignStmt) u;
                if (assignStmt.containsFieldRef()) {
                    FieldRef ref = assignStmt.getFieldRef();
                    if (isTypeLoaded(ref.getFieldRef().type())) {
                        try {
                            if ((ref instanceof InstanceFieldRef) && (fld = ref.getField()) != null && fld.isStatic()) {
                                if (assignStmt.getLeftOp() == ref) {
                                    assignStmt.setLeftOp(Jimple.v().newStaticFieldRef(ref.getField().makeRef()));
                                } else if (assignStmt.getRightOp() == ref) {
                                    assignStmt.setRightOp(Jimple.v().newStaticFieldRef(ref.getField().makeRef()));
                                }
                            }
                        } catch (ConflictingFieldRefException e) {
                        }
                    }
                }
            }
        }
    }
}
