package soot.jimple.infoflow.solver.fastSolver;

import java.lang.ref.WeakReference;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/fastSolver/WeakPathEdge.class */
class WeakPathEdge<N, D> {
    protected final WeakReference<N> target;
    protected final WeakReference<D> dSource;
    protected final WeakReference<D> dTarget;
    protected final int hashCode;

    public WeakPathEdge(D dSource, N target, D dTarget) {
        this.target = target == null ? null : new WeakReference<>(target);
        this.dSource = dSource == null ? null : new WeakReference<>(dSource);
        this.dTarget = dTarget == null ? null : new WeakReference<>(dTarget);
        int result = (31 * 1) + (dSource == null ? 0 : dSource.hashCode());
        this.hashCode = (31 * ((31 * result) + (dTarget == null ? 0 : dTarget.hashCode()))) + (target == null ? 0 : target.hashCode());
    }

    public N getTarget() {
        return this.target.get();
    }

    public D factAtSource() {
        return this.dSource.get();
    }

    public D factAtTarget() {
        return this.dTarget.get();
    }

    public boolean isDead() {
        return this.dSource == null || this.dTarget == null;
    }

    public int hashCode() {
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        WeakPathEdge other = (WeakPathEdge) obj;
        if (this.dSource.get() == null) {
            if (other.dSource.get() != null) {
                return false;
            }
        } else if (!this.dSource.get().equals(other.dSource.get())) {
            return false;
        }
        if (this.dTarget.get() == null) {
            if (other.dTarget.get() != null) {
                return false;
            }
        } else if (!this.dTarget.get().equals(other.dTarget.get())) {
            return false;
        }
        if (this.target.get() == null) {
            if (other.target.get() != null) {
                return false;
            }
            return true;
        } else if (!this.target.get().equals(other.target.get())) {
            return false;
        } else {
            return true;
        }
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("<");
        result.append(this.dSource.get());
        result.append("> -> <");
        result.append(this.target.get().toString());
        result.append(",");
        result.append(this.dTarget.get());
        result.append(">");
        return result.toString();
    }
}
