package soot.baf.internal;

import java.util.Collections;
import java.util.List;
import soot.Local;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BafLocal.class */
public class BafLocal implements Local {
    String name;
    Type type;
    int fixedHashCode;
    boolean isHashCodeChosen;
    private Local originalLocal;
    private int number = 0;

    public BafLocal(String name, Type t) {
        this.name = name;
        this.type = t;
    }

    @Override // soot.Value
    public Object clone() {
        BafLocal baf = new BafLocal(this.name, this.type);
        baf.originalLocal = this.originalLocal;
        return baf;
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        return equals(o);
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return (this.name.hashCode() * 101) + (this.type.hashCode() * 17);
    }

    public Local getOriginalLocal() {
        return this.originalLocal;
    }

    public void setOriginalLocal(Local l) {
        this.originalLocal = l;
    }

    @Override // soot.Local
    public String getName() {
        return this.name;
    }

    @Override // soot.Local
    public void setName(String name) {
        this.name = name;
    }

    @Override // soot.Value
    public Type getType() {
        return this.type;
    }

    @Override // soot.Local
    public void setType(Type t) {
        this.type = t;
    }

    public String toString() {
        return getName();
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        up.local(this);
    }

    @Override // soot.Value
    public List<ValueBox> getUseBoxes() {
        return Collections.emptyList();
    }

    @Override // soot.util.Switchable
    public void apply(Switch s) {
        throw new RuntimeException("invalid case switch");
    }

    @Override // soot.util.Numberable
    public final int getNumber() {
        return this.number;
    }

    @Override // soot.util.Numberable
    public final void setNumber(int number) {
        this.number = number;
    }

    @Override // soot.Local
    public boolean isStackLocal() {
        return true;
    }
}
