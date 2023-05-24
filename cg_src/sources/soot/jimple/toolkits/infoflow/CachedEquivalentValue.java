package soot.jimple.toolkits.infoflow;

import java.util.WeakHashMap;
import soot.EquivalentValue;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/infoflow/CachedEquivalentValue.class */
public class CachedEquivalentValue extends EquivalentValue {
    protected int code;
    protected WeakHashMap<Value, Boolean> isEquivalent;

    public CachedEquivalentValue(Value e) {
        super(e);
        this.code = Integer.MAX_VALUE;
        this.isEquivalent = new WeakHashMap<>();
    }

    @Override // soot.EquivalentValue
    public int hashCode() {
        if (this.code == Integer.MAX_VALUE) {
            this.code = super.hashCode();
        }
        return this.code;
    }

    @Override // soot.EquivalentValue
    public boolean equals(Object o) {
        if (getClass() != o.getClass()) {
            return false;
        }
        EquivalentValue ev = (EquivalentValue) o;
        Value v = ev.getValue();
        Boolean b = this.isEquivalent.get(v);
        if (b == null) {
            b = Boolean.valueOf(super.equals(o));
            this.isEquivalent.put(v, b);
        }
        return b.booleanValue();
    }
}
