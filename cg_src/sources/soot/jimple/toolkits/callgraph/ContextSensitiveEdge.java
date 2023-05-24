package soot.jimple.toolkits.callgraph;

import soot.Context;
import soot.Kind;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/ContextSensitiveEdge.class */
public interface ContextSensitiveEdge {
    Context srcCtxt();

    SootMethod src();

    Unit srcUnit();

    Stmt srcStmt();

    Context tgtCtxt();

    SootMethod tgt();

    Kind kind();
}
