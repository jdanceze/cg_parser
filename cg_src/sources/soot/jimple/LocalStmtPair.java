package soot.jimple;

import soot.Local;
/* loaded from: gencallgraphv3.jar:soot/jimple/LocalStmtPair.class */
public class LocalStmtPair {
    Local local;
    Stmt stmt;

    public LocalStmtPair(Local local, Stmt stmt) {
        this.local = local;
        this.stmt = stmt;
    }

    public boolean equals(Object other) {
        if ((other instanceof LocalStmtPair) && ((LocalStmtPair) other).local == this.local && ((LocalStmtPair) other).stmt == this.stmt) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (this.local.hashCode() * 101) + this.stmt.hashCode() + 17;
    }
}
