package soot.jimple.internal;

import java.util.Collections;
import java.util.List;
import soot.RefType;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.jimple.ExprSwitch;
import soot.jimple.NewExpr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractNewExpr.class */
public abstract class AbstractNewExpr implements NewExpr {
    protected RefType type;

    @Override // soot.Value
    public abstract Object clone();

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        if (o instanceof AbstractNewExpr) {
            AbstractNewExpr ae = (AbstractNewExpr) o;
            return this.type.equals(ae.type);
        }
        return false;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return this.type.hashCode();
    }

    public String toString() {
        return "new " + this.type.toString();
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        up.literal("new ");
        up.type(this.type);
    }

    @Override // soot.jimple.NewExpr
    public RefType getBaseType() {
        return this.type;
    }

    @Override // soot.jimple.NewExpr
    public void setBaseType(RefType type) {
        this.type = type;
    }

    @Override // soot.jimple.NewExpr, soot.Value
    public Type getType() {
        return this.type;
    }

    @Override // soot.Value
    public List<ValueBox> getUseBoxes() {
        return Collections.emptyList();
    }

    @Override // soot.jimple.NewExpr, soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseNewExpr(this);
    }
}
