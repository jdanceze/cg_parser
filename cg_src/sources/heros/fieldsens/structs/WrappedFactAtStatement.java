package heros.fieldsens.structs;

import heros.fieldsens.AccessPath;
import heros.fieldsens.Resolver;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/structs/WrappedFactAtStatement.class */
public class WrappedFactAtStatement<Field, Fact, Stmt, Method> {
    private WrappedFact<Field, Fact, Stmt, Method> fact;
    private Stmt stmt;

    public WrappedFactAtStatement(Stmt stmt, WrappedFact<Field, Fact, Stmt, Method> fact) {
        this.stmt = stmt;
        this.fact = fact;
    }

    public WrappedFact<Field, Fact, Stmt, Method> getWrappedFact() {
        return this.fact;
    }

    public Fact getFact() {
        return this.fact.getFact();
    }

    public AccessPath<Field> getAccessPath() {
        return this.fact.getAccessPath();
    }

    public Resolver<Field, Fact, Stmt, Method> getResolver() {
        return this.fact.getResolver();
    }

    public Stmt getStatement() {
        return this.stmt;
    }

    public FactAtStatement<Fact, Stmt> getAsFactAtStatement() {
        return new FactAtStatement<>(this.fact.getFact(), this.stmt);
    }

    public boolean canDeltaBeApplied(AccessPath.Delta<Field> delta) {
        return delta.canBeAppliedTo(this.fact.getAccessPath());
    }

    public String toString() {
        return this.fact + " @ " + this.stmt;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.fact == null ? 0 : this.fact.hashCode());
        return (31 * result) + (this.stmt == null ? 0 : this.stmt.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        WrappedFactAtStatement other = (WrappedFactAtStatement) obj;
        if (this.fact == null) {
            if (other.fact != null) {
                return false;
            }
        } else if (!this.fact.equals(other.fact)) {
            return false;
        }
        if (this.stmt == null) {
            if (other.stmt != null) {
                return false;
            }
            return true;
        } else if (!this.stmt.equals(other.stmt)) {
            return false;
        } else {
            return true;
        }
    }
}
