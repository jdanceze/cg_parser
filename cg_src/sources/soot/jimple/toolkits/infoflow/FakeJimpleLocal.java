package soot.jimple.toolkits.infoflow;

import soot.Local;
import soot.Type;
import soot.jimple.internal.JimpleLocal;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/infoflow/FakeJimpleLocal.class */
public class FakeJimpleLocal extends JimpleLocal {
    Local realLocal;
    Object info;

    public FakeJimpleLocal(String name, Type t, Local realLocal) {
        this(name, t, realLocal, null);
    }

    public FakeJimpleLocal(String name, Type t, Local realLocal, Object info) {
        super(name, t);
        this.realLocal = realLocal;
        this.info = info;
    }

    @Override // soot.jimple.internal.JimpleLocal, soot.EquivTo
    public boolean equivTo(Object o) {
        if (o != null && (o instanceof JimpleLocal)) {
            return (getName() == null || getType() == null) ? getName() != null ? getName().equals(((Local) o).getName()) && ((Local) o).getType() == null : getType() != null ? ((Local) o).getName() == null && getType().equals(((Local) o).getType()) : ((Local) o).getName() == null && ((Local) o).getType() == null : getName().equals(((Local) o).getName()) && getType().equals(((Local) o).getType());
        }
        return false;
    }

    public boolean equals(Object o) {
        return equivTo(o);
    }

    @Override // soot.jimple.internal.JimpleLocal, soot.Value
    public Object clone() {
        return new FakeJimpleLocal(getName(), getType(), this.realLocal, this.info);
    }

    public Local getRealLocal() {
        return this.realLocal;
    }

    public Object getInfo() {
        return this.info;
    }

    public void setInfo(Object o) {
        this.info = o;
    }
}
