package soot.jimple;

import java.util.Collections;
import java.util.List;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/ParameterRef.class */
public class ParameterRef implements IdentityRef {
    int n;
    Type paramType;

    public ParameterRef(Type paramType, int number) {
        this.n = number;
        this.paramType = paramType;
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        return (o instanceof ParameterRef) && this.n == ((ParameterRef) o).n && this.paramType.equals(((ParameterRef) o).paramType);
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return (this.n * 101) + (this.paramType.hashCode() * 17);
    }

    @Override // soot.Value
    public Object clone() {
        return new ParameterRef(this.paramType, this.n);
    }

    public String toString() {
        return "@parameter" + this.n + ": " + this.paramType;
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        up.identityRef(this);
    }

    public int getIndex() {
        return this.n;
    }

    public void setIndex(int index) {
        this.n = index;
    }

    @Override // soot.Value
    public final List<ValueBox> getUseBoxes() {
        return Collections.emptyList();
    }

    @Override // soot.Value
    public Type getType() {
        return this.paramType;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((RefSwitch) sw).caseParameterRef(this);
    }
}
