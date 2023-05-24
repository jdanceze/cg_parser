package soot.toolkits.scalar;

import soot.PointsToAnalysis;
import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/Pair.class */
public class Pair<T, U> {
    protected T o1;
    protected U o2;

    public Pair() {
        this.o1 = null;
        this.o2 = null;
    }

    public Pair(T o1, U o2) {
        this.o1 = o1;
        this.o2 = o2;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.o1 == null ? 0 : this.o1.hashCode());
        return (31 * result) + (this.o2 == null ? 0 : this.o2.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Pair<?, ?> other = (Pair) obj;
        if (this.o1 == null) {
            if (other.o1 != null) {
                return false;
            }
        } else if (!this.o1.equals(other.o1)) {
            return false;
        }
        if (this.o2 == null) {
            if (other.o2 != null) {
                return false;
            }
            return true;
        } else if (!this.o2.equals(other.o2)) {
            return false;
        } else {
            return true;
        }
    }

    public String toString() {
        return "Pair " + this.o1 + "," + this.o2;
    }

    public boolean isParameter() {
        return (this.o1 instanceof SootMethod) && (this.o2 instanceof Integer);
    }

    public boolean isThisParameter() {
        return (this.o1 instanceof SootMethod) && PointsToAnalysis.THIS_NODE.equals(this.o2);
    }

    public T getO1() {
        return this.o1;
    }

    public U getO2() {
        return this.o2;
    }

    public void setO1(T no1) {
        this.o1 = no1;
    }

    public void setO2(U no2) {
        this.o2 = no2;
    }

    public void setPair(T no1, U no2) {
        this.o1 = no1;
        this.o2 = no2;
    }
}
