package heros.fieldsens.structs;

import heros.fieldsens.AccessPath;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.Resolver;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/structs/WrappedFact.class */
public class WrappedFact<Field, Fact, Stmt, Method> {
    private final Fact fact;
    private final AccessPath<Field> accessPath;
    private final Resolver<Field, Fact, Stmt, Method> resolver;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WrappedFact.class.desiredAssertionStatus();
    }

    public WrappedFact(Fact fact, AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {
        if (!$assertionsDisabled && fact == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && accessPath == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && resolver == null) {
            throw new AssertionError();
        }
        this.fact = fact;
        this.accessPath = accessPath;
        this.resolver = resolver;
    }

    public Fact getFact() {
        return this.fact;
    }

    public WrappedFact<Field, Fact, Stmt, Method> applyDelta(AccessPath.Delta<Field> delta) {
        return new WrappedFact<>(this.fact, delta.applyTo(this.accessPath), this.resolver);
    }

    public AccessPath<Field> getAccessPath() {
        return this.accessPath;
    }

    public WrappedFact<Field, Fact, Stmt, Method> applyConstraint(FlowFunction.Constraint<Field> constraint, Fact zeroValue) {
        if (this.fact.equals(zeroValue)) {
            return this;
        }
        return new WrappedFact<>(this.fact, constraint.applyToAccessPath(this.accessPath), this.resolver);
    }

    public String toString() {
        String result = this.fact.toString() + this.accessPath;
        if (this.resolver != null) {
            result = result + this.resolver.toString();
        }
        return result;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.accessPath == null ? 0 : this.accessPath.hashCode());
        return (31 * ((31 * result) + (this.fact == null ? 0 : this.fact.hashCode()))) + (this.resolver == null ? 0 : this.resolver.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        WrappedFact other = (WrappedFact) obj;
        if (this.accessPath == null) {
            if (other.accessPath != null) {
                return false;
            }
        } else if (!this.accessPath.equals(other.accessPath)) {
            return false;
        }
        if (this.fact == null) {
            if (other.fact != null) {
                return false;
            }
        } else if (!this.fact.equals(other.fact)) {
            return false;
        }
        if (this.resolver == null) {
            if (other.resolver != null) {
                return false;
            }
            return true;
        } else if (!this.resolver.equals(other.resolver)) {
            return false;
        } else {
            return true;
        }
    }

    public Resolver<Field, Fact, Stmt, Method> getResolver() {
        return this.resolver;
    }
}
