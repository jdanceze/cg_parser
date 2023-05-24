package soot.jimple.internal;

import java.util.Collections;
import java.util.List;
import soot.Local;
import soot.Scene;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.JimpleToBafContext;
import soot.jimple.JimpleValueSwitch;
import soot.util.Numberer;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JimpleLocal.class */
public class JimpleLocal implements Local, ConvertToBaf {
    protected String name;
    protected Type type;
    private volatile int number = 0;

    public JimpleLocal(String name, Type type) {
        setName(name);
        setType(type);
        addToNumberer();
    }

    protected void addToNumberer() {
        Numberer<Local> numberer = Scene.v().getLocalNumberer();
        if (numberer != null) {
            numberer.add(this);
        }
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        return equals(o);
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        int result = (31 * 1) + (this.name == null ? 0 : this.name.hashCode());
        return (31 * result) + (this.type == null ? 0 : this.type.hashCode());
    }

    @Override // soot.Value
    public Object clone() {
        JimpleLocal local = new JimpleLocal(null, this.type);
        local.name = this.name;
        return local;
    }

    @Override // soot.Local
    public String getName() {
        return this.name;
    }

    @Override // soot.Local
    public void setName(String name) {
        this.name = name == null ? null : name.intern();
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
    public final List<ValueBox> getUseBoxes() {
        return Collections.emptyList();
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((JimpleValueSwitch) sw).caseLocal(this);
    }

    @Override // soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        Unit u = Baf.v().newLoadInst(getType(), context.getBafLocalOfJimpleLocal(this));
        u.addAllTagsOf(context.getCurrentUnit());
        out.add(u);
    }

    @Override // soot.util.Numberable
    public final int getNumber() {
        return this.number;
    }

    @Override // soot.util.Numberable
    public void setNumber(int number) {
        this.number = number;
    }

    @Override // soot.Local
    public boolean isStackLocal() {
        String n = getName();
        return n != null && n.charAt(0) == '$';
    }
}
