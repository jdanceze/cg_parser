package heros.fieldsens.structs;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/structs/FactAtStatement.class */
public class FactAtStatement<Fact, Stmt> {
    public final Fact fact;
    public final Stmt stmt;

    public FactAtStatement(Fact fact, Stmt stmt) {
        this.fact = fact;
        this.stmt = stmt;
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
        FactAtStatement other = (FactAtStatement) obj;
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
