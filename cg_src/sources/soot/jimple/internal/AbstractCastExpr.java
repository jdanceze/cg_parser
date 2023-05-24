package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.ArrayType;
import soot.RefType;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.grimp.PrecedenceTest;
import soot.jimple.CastExpr;
import soot.jimple.ConvertToBaf;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractCastExpr.class */
public abstract class AbstractCastExpr implements CastExpr, ConvertToBaf {
    protected final ValueBox opBox;
    protected Type type;

    @Override // soot.Value
    public abstract Object clone();

    AbstractCastExpr(Value op, Type type) {
        this(Jimple.v().newImmediateBox(op), type);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractCastExpr(ValueBox opBox, Type type) {
        this.opBox = opBox;
        this.type = type;
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        if (o instanceof AbstractCastExpr) {
            AbstractCastExpr ace = (AbstractCastExpr) o;
            return this.opBox.getValue().equivTo(ace.opBox.getValue()) && this.type.equals(ace.type);
        }
        return false;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return (this.opBox.getValue().equivHashCode() * 101) + this.type.hashCode() + 17;
    }

    public String toString() {
        return "(" + this.type.toString() + ") " + this.opBox.getValue().toString();
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        up.literal("(");
        up.type(this.type);
        up.literal(") ");
        boolean needsBrackets = PrecedenceTest.needsBrackets(this.opBox, this);
        if (needsBrackets) {
            up.literal("(");
        }
        this.opBox.toString(up);
        if (needsBrackets) {
            up.literal(")");
        }
    }

    @Override // soot.jimple.CastExpr
    public Value getOp() {
        return this.opBox.getValue();
    }

    @Override // soot.jimple.CastExpr
    public void setOp(Value op) {
        this.opBox.setValue(op);
    }

    @Override // soot.jimple.CastExpr
    public ValueBox getOpBox() {
        return this.opBox;
    }

    @Override // soot.Value
    public final List<ValueBox> getUseBoxes() {
        List<ValueBox> list = new ArrayList<>(this.opBox.getValue().getUseBoxes());
        list.add(this.opBox);
        return list;
    }

    @Override // soot.jimple.CastExpr
    public Type getCastType() {
        return this.type;
    }

    @Override // soot.jimple.CastExpr
    public void setCastType(Type castType) {
        this.type = castType;
    }

    @Override // soot.jimple.CastExpr, soot.Value
    public Type getType() {
        return this.type;
    }

    @Override // soot.jimple.CastExpr, soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseCastExpr(this);
    }

    @Override // soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        Unit u;
        ((ConvertToBaf) getOp()).convertToBaf(context, out);
        Type toType = getCastType();
        if ((toType instanceof ArrayType) || (toType instanceof RefType)) {
            u = Baf.v().newInstanceCastInst(toType);
        } else {
            Type fromType = getOp().getType();
            if (!fromType.equals(toType)) {
                u = Baf.v().newPrimitiveCastInst(fromType, toType);
            } else {
                u = Baf.v().newNopInst();
            }
        }
        out.add(u);
        u.addAllTagsOf(context.getCurrentUnit());
    }
}
