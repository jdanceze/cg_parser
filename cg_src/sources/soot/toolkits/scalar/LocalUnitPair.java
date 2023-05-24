package soot.toolkits.scalar;

import soot.Local;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/LocalUnitPair.class */
public class LocalUnitPair {
    Local local;
    Unit unit;

    public LocalUnitPair(Local local, Unit unit) {
        this.local = local;
        this.unit = unit;
    }

    public boolean equals(Object other) {
        if (other instanceof LocalUnitPair) {
            LocalUnitPair temp = (LocalUnitPair) other;
            return temp.local == this.local && temp.unit == this.unit;
        }
        return false;
    }

    public int hashCode() {
        return (this.local.hashCode() * 101) + this.unit.hashCode() + 17;
    }

    public Local getLocal() {
        return this.local;
    }

    public Unit getUnit() {
        return this.unit;
    }
}
