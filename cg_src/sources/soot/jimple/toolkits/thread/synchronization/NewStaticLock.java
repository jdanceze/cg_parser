package soot.jimple.toolkits.thread.synchronization;

import java.util.Collections;
import java.util.List;
import soot.NullType;
import soot.SootClass;
import soot.Type;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/synchronization/NewStaticLock.class */
public class NewStaticLock implements Value {
    SootClass sc;
    static int nextidnum = 1;
    int idnum;

    public NewStaticLock(SootClass sc) {
        this.sc = sc;
        int i = nextidnum;
        nextidnum = i + 1;
        this.idnum = i;
    }

    public SootClass getLockClass() {
        return this.sc;
    }

    @Override // soot.Value
    public List<ValueBox> getUseBoxes() {
        return Collections.emptyList();
    }

    @Override // soot.Value
    public Object clone() {
        return new NewStaticLock(this.sc);
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object c) {
        return equals(c);
    }

    public boolean equals(Object c) {
        return (c instanceof NewStaticLock) && ((NewStaticLock) c).idnum == this.idnum;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return hashCode();
    }

    public int hashCode() {
        return this.idnum;
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
        return "<new static lock in " + this.sc.toString() + ">";
    }
}
