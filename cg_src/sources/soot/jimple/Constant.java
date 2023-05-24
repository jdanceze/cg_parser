package soot.jimple;

import java.util.Collections;
import java.util.List;
import soot.Immediate;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
/* loaded from: gencallgraphv3.jar:soot/jimple/Constant.class */
public abstract class Constant implements Value, ConvertToBaf, Immediate {
    @Override // soot.Value
    public final List<ValueBox> getUseBoxes() {
        return Collections.emptyList();
    }

    @Override // soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        Unit u = Baf.v().newPushInst(this);
        u.addAllTagsOf(context.getCurrentUnit());
        out.add(u);
    }

    @Override // soot.Value
    public Object clone() {
        throw new RuntimeException();
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object c) {
        return equals(c);
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return hashCode();
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        up.constant(this);
    }
}
