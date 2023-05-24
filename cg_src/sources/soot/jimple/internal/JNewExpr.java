package soot.jimple.internal;

import java.util.List;
import soot.RefType;
import soot.Unit;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.JimpleToBafContext;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JNewExpr.class */
public class JNewExpr extends AbstractNewExpr implements ConvertToBaf {
    public JNewExpr(RefType type) {
        this.type = type;
    }

    @Override // soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        Unit u = Baf.v().newNewInst(getBaseType());
        u.addAllTagsOf(context.getCurrentUnit());
        out.add(u);
    }

    @Override // soot.jimple.internal.AbstractNewExpr, soot.Value
    public Object clone() {
        return new JNewExpr(this.type);
    }
}
