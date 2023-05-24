package soot.dava.toolkits.base.misc;

import soot.grimp.internal.GEqExpr;
import soot.grimp.internal.GGeExpr;
import soot.grimp.internal.GGtExpr;
import soot.grimp.internal.GLeExpr;
import soot.grimp.internal.GLtExpr;
import soot.grimp.internal.GNeExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.EqExpr;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.LeExpr;
import soot.jimple.LtExpr;
import soot.jimple.NeExpr;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/misc/ConditionFlipper.class */
public class ConditionFlipper {
    public static ConditionExpr flip(ConditionExpr ce) {
        if (ce instanceof EqExpr) {
            return new GNeExpr(ce.getOp1(), ce.getOp2());
        }
        if (ce instanceof NeExpr) {
            return new GEqExpr(ce.getOp1(), ce.getOp2());
        }
        if (ce instanceof GtExpr) {
            return new GLeExpr(ce.getOp1(), ce.getOp2());
        }
        if (ce instanceof LtExpr) {
            return new GGeExpr(ce.getOp1(), ce.getOp2());
        }
        if (ce instanceof GeExpr) {
            return new GLtExpr(ce.getOp1(), ce.getOp2());
        }
        if (ce instanceof LeExpr) {
            return new GGtExpr(ce.getOp1(), ce.getOp2());
        }
        return null;
    }
}
