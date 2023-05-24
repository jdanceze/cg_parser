package soot.dexpler;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootMethodRef;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.LengthExpr;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.toolkits.scalar.LocalCreation;
import soot.jimple.toolkits.scalar.UnreachableCodeEliminator;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.scalar.LocalDefs;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexNullArrayRefTransformer.class */
public class DexNullArrayRefTransformer extends BodyTransformer {
    public static DexNullArrayRefTransformer v() {
        return new DexNullArrayRefTransformer();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        ExceptionalUnitGraph g = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(body, DalvikThrowAnalysis.v());
        LocalDefs defs = G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs(g);
        LocalCreation lc = Scene.v().createLocalCreation(body.getLocals(), "ex");
        boolean changed = false;
        Iterator<Unit> unitIt = body.getUnits().snapshotIterator();
        while (unitIt.hasNext()) {
            Stmt s = (Stmt) unitIt.next();
            if (s.containsArrayRef()) {
                if (isAlwaysNullBefore(s, (Local) s.getArrayRef().getBase(), defs)) {
                    createThrowStmt(body, s, lc);
                    changed = true;
                }
            } else if (s instanceof AssignStmt) {
                AssignStmt ass = (AssignStmt) s;
                Value rightOp = ass.getRightOp();
                if (rightOp instanceof LengthExpr) {
                    LengthExpr l = (LengthExpr) ass.getRightOp();
                    Value base = l.getOp();
                    if (base instanceof IntConstant) {
                        IntConstant ic = (IntConstant) base;
                        if (ic.value == 0) {
                            createThrowStmt(body, s, lc);
                            changed = true;
                        }
                    } else if (base == NullConstant.v() || isAlwaysNullBefore(s, (Local) base, defs)) {
                        createThrowStmt(body, s, lc);
                        changed = true;
                    }
                }
            }
        }
        if (changed) {
            UnreachableCodeEliminator.v().transform(body);
        }
    }

    private boolean isAlwaysNullBefore(Stmt s, Local base, LocalDefs defs) {
        List<Unit> baseDefs = defs.getDefsOfAt(base, s);
        if (baseDefs.isEmpty()) {
            return true;
        }
        for (Unit u : baseDefs) {
            if (!(u instanceof DefinitionStmt)) {
                return false;
            }
            DefinitionStmt defStmt = (DefinitionStmt) u;
            if (defStmt.getRightOp() != NullConstant.v()) {
                return false;
            }
        }
        return true;
    }

    private void createThrowStmt(Body body, Unit oldStmt, LocalCreation lc) {
        RefType tp = RefType.v("java.lang.NullPointerException");
        Local lcEx = lc.newLocal(tp);
        SootMethodRef constructorRef = Scene.v().makeConstructorRef(tp.getSootClass(), Collections.singletonList(RefType.v("java.lang.String")));
        body.getUnits().insertBefore(Jimple.v().newAssignStmt(lcEx, Jimple.v().newNewExpr(tp)), (AssignStmt) oldStmt);
        body.getUnits().insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(lcEx, constructorRef, Collections.singletonList(StringConstant.v("Invalid array reference replaced by Soot")))), (InvokeStmt) oldStmt);
        body.getUnits().swapWith(oldStmt, (Unit) Jimple.v().newThrowStmt(lcEx));
    }
}
