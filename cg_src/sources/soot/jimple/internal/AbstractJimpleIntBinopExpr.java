package soot.jimple.internal;

import java.util.List;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.ConvertToBaf;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractJimpleIntBinopExpr.class */
public abstract class AbstractJimpleIntBinopExpr extends AbstractIntBinopExpr implements ConvertToBaf {
    protected abstract Unit makeBafInst(Type type);

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractJimpleIntBinopExpr(Value op1, Value op2) {
        super(Jimple.v().newArgBox(op1), Jimple.v().newArgBox(op2));
    }

    @Override // soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        ((ConvertToBaf) getOp1()).convertToBaf(context, out);
        ((ConvertToBaf) getOp2()).convertToBaf(context, out);
        Unit u = makeBafInst(getOp1().getType());
        out.add(u);
        u.addAllTagsOf(context.getCurrentUnit());
    }
}
