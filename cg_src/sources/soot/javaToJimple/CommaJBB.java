package soot.javaToJimple;

import polyglot.ast.Expr;
import soot.Value;
import soot.javaToJimple.jj.ast.JjComma_c;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/CommaJBB.class */
public class CommaJBB extends AbstractJimpleBodyBuilder {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public Value createAggressiveExpr(Expr expr, boolean redAggr, boolean revIfNec) {
        if (expr instanceof JjComma_c) {
            return getCommaLocal((JjComma_c) expr);
        }
        return ext().createAggressiveExpr(expr, redAggr, revIfNec);
    }

    private Value getCommaLocal(JjComma_c comma) {
        base().createAggressiveExpr(comma.first(), false, false);
        Value val = base().createAggressiveExpr(comma.second(), false, false);
        return val;
    }
}
