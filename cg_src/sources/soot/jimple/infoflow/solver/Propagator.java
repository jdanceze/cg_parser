package soot.jimple.infoflow.solver;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/Propagator.class */
public class Propagator<D> {
    private final D abstraction;

    public Propagator(D abstraction) {
        this.abstraction = abstraction;
    }

    public D getAbstraction() {
        return this.abstraction;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.abstraction == null ? 0 : this.abstraction.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Propagator other = (Propagator) obj;
        if (this.abstraction == null) {
            if (other.abstraction != null) {
                return false;
            }
            return true;
        } else if (!this.abstraction.equals(other.abstraction)) {
            return false;
        } else {
            return true;
        }
    }
}
