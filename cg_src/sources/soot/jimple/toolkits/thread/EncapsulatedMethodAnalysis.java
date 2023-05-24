package soot.jimple.toolkits.thread;

import java.util.Iterator;
import soot.IntType;
import soot.RefLikeType;
import soot.Type;
import soot.jimple.FieldRef;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/EncapsulatedMethodAnalysis.class */
public class EncapsulatedMethodAnalysis {
    boolean isMethodPure;
    boolean isMethodConditionallyPure;

    public EncapsulatedMethodAnalysis(UnitGraph g) {
        this.isMethodPure = true;
        this.isMethodConditionallyPure = true;
        Iterator stmtIt = g.iterator();
        while (stmtIt.hasNext()) {
            Stmt s = (Stmt) stmtIt.next();
            if (s.containsFieldRef()) {
                FieldRef ref = s.getFieldRef();
                if ((ref instanceof StaticFieldRef) && (Type.toMachineType(((StaticFieldRef) ref).getType()) instanceof RefLikeType)) {
                    this.isMethodPure = false;
                    this.isMethodConditionallyPure = false;
                    return;
                }
            }
        }
        for (Type paramType : g.getBody().getMethod().getParameterTypes()) {
            if (Type.toMachineType(paramType) != IntType.v()) {
                this.isMethodPure = false;
                return;
            }
        }
    }

    public boolean isPure() {
        return this.isMethodPure;
    }

    public boolean isConditionallyPure() {
        return this.isMethodConditionallyPure;
    }
}
