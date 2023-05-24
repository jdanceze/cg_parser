package soot.jimple;

import java.util.Iterator;
import soot.Local;
import soot.SideEffectTester;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/NaiveSideEffectTester.class */
public class NaiveSideEffectTester implements SideEffectTester {
    @Override // soot.SideEffectTester
    public void newMethod(SootMethod m) {
    }

    @Override // soot.SideEffectTester
    public boolean unitCanReadFrom(Unit u, Value v) {
        Stmt s = (Stmt) u;
        if (v instanceof Constant) {
            return false;
        }
        if (v instanceof Expr) {
            throw new RuntimeException("can't deal with expr");
        }
        if (s.containsInvokeExpr() && !(v instanceof Local)) {
            return true;
        }
        Iterator useIt = u.getUseBoxes().iterator();
        while (useIt.hasNext()) {
            Value use = (Value) useIt.next();
            if (use.equivTo(v)) {
                return true;
            }
            for (ValueBox valueBox : v.getUseBoxes()) {
                if (use.equivTo(valueBox)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // soot.SideEffectTester
    public boolean unitCanWriteTo(Unit u, Value v) {
        Stmt s = (Stmt) u;
        if (v instanceof Constant) {
            return false;
        }
        if (v instanceof Expr) {
            throw new RuntimeException("can't deal with expr");
        }
        if (s.containsInvokeExpr() && !(v instanceof Local)) {
            return true;
        }
        for (ValueBox valueBox : u.getDefBoxes()) {
            Value def = valueBox.getValue();
            for (ValueBox valueBox2 : v.getUseBoxes()) {
                Value use = valueBox2.getValue();
                if (def.equivTo(use)) {
                    return true;
                }
            }
            if (def.equivTo(v)) {
                return true;
            }
            if ((v instanceof InstanceFieldRef) && (def instanceof InstanceFieldRef) && ((InstanceFieldRef) v).getField() == ((InstanceFieldRef) def).getField()) {
                return true;
            }
        }
        return false;
    }
}
