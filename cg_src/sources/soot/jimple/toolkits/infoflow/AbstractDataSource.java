package soot.jimple.toolkits.infoflow;

import java.util.Collections;
import java.util.List;
import soot.NullType;
import soot.Type;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/infoflow/AbstractDataSource.class */
public class AbstractDataSource implements Value {
    Object sourcename;

    public AbstractDataSource(Object sourcename) {
        this.sourcename = sourcename;
    }

    @Override // soot.Value
    public List<ValueBox> getUseBoxes() {
        return Collections.emptyList();
    }

    @Override // soot.Value
    public Object clone() {
        return new AbstractDataSource(this.sourcename);
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object c) {
        return this.sourcename instanceof Value ? (c instanceof AbstractDataSource) && ((Value) this.sourcename).equivTo(((AbstractDataSource) c).sourcename) : (c instanceof AbstractDataSource) && ((AbstractDataSource) c).sourcename.equals(this.sourcename);
    }

    public boolean equals(Object c) {
        return (c instanceof AbstractDataSource) && ((AbstractDataSource) c).sourcename.equals(this.sourcename);
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        if (this.sourcename instanceof Value) {
            return ((Value) this.sourcename).equivHashCode();
        }
        return this.sourcename.hashCode();
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
    }

    @Override // soot.Value
    public Type getType() {
        return NullType.v();
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        throw new RuntimeException("Not Implemented");
    }

    public String toString() {
        return "sourceof<" + this.sourcename.toString() + ">";
    }
}
