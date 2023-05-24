package soot.toDex;

import java.util.Collections;
import java.util.List;
import soot.Local;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.jimple.JimpleValueSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/toDex/TemporaryRegisterLocal.class */
public class TemporaryRegisterLocal implements Local {
    private static final long serialVersionUID = 1;
    private Type type;

    public TemporaryRegisterLocal(Type regType) {
        setType(regType);
    }

    @Override // soot.Value
    public Local clone() {
        throw new RuntimeException("Not implemented");
    }

    @Override // soot.Value
    public final List<ValueBox> getUseBoxes() {
        return Collections.emptyList();
    }

    @Override // soot.Value
    public Type getType() {
        return this.type;
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        throw new RuntimeException("Not implemented.");
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((JimpleValueSwitch) sw).caseLocal(this);
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        return equals(o);
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        int result = (31 * 1) + (this.type == null ? 0 : this.type.hashCode());
        return result;
    }

    @Override // soot.util.Numberable
    public void setNumber(int number) {
        throw new RuntimeException("Not implemented.");
    }

    @Override // soot.util.Numberable
    public int getNumber() {
        throw new RuntimeException("Not implemented.");
    }

    @Override // soot.Local
    public String getName() {
        throw new RuntimeException("Not implemented.");
    }

    @Override // soot.Local
    public void setName(String name) {
        throw new RuntimeException("Not implemented.");
    }

    @Override // soot.Local
    public void setType(Type t) {
        this.type = t;
    }

    @Override // soot.Local
    public boolean isStackLocal() {
        throw new RuntimeException("Not implemented.");
    }
}
