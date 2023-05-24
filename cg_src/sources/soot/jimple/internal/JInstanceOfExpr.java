package soot.jimple.internal;

import java.util.List;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JInstanceOfExpr.class */
public class JInstanceOfExpr extends AbstractInstanceOfExpr implements ConvertToBaf {
    public JInstanceOfExpr(Value op, Type checkType) {
        super(Jimple.v().newImmediateBox(op), checkType);
    }

    @Override // soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        ((ConvertToBaf) getOp()).convertToBaf(context, out);
        Unit u = Baf.v().newInstanceOfInst(getCheckType());
        u.addAllTagsOf(context.getCurrentUnit());
        out.add(u);
    }

    @Override // soot.jimple.internal.AbstractInstanceOfExpr, soot.Value
    public Object clone() {
        return new JInstanceOfExpr(Jimple.cloneIfNecessary(getOp()), this.checkType);
    }
}
