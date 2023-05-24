package soot.jimple.internal;

import java.util.List;
import soot.Unit;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JLengthExpr.class */
public class JLengthExpr extends AbstractLengthExpr implements ConvertToBaf {
    public JLengthExpr(Value op) {
        super(Jimple.v().newImmediateBox(op));
    }

    @Override // soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        ((ConvertToBaf) getOp()).convertToBaf(context, out);
        Unit u = Baf.v().newArrayLengthInst();
        u.addAllTagsOf(context.getCurrentUnit());
        out.add(u);
    }

    @Override // soot.jimple.internal.AbstractUnopExpr, soot.Value
    public Object clone() {
        return new JLengthExpr(Jimple.cloneIfNecessary(getOp()));
    }
}
