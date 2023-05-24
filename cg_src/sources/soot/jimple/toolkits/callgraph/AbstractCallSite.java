package soot.jimple.toolkits.callgraph;

import soot.SootMethod;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/AbstractCallSite.class */
public class AbstractCallSite {
    protected Stmt stmt;
    protected SootMethod container;

    public AbstractCallSite(Stmt stmt, SootMethod container) {
        this.stmt = stmt;
        this.container = container;
    }

    public Stmt getStmt() {
        return this.stmt;
    }

    public SootMethod getContainer() {
        return this.container;
    }

    public String toString() {
        return this.stmt == null ? "<null>" : this.stmt.toString();
    }
}
