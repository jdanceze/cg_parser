package soot.dava.toolkits.base.AST.transformations;

import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.grimp.internal.GAssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.InvokeExpr;
/* compiled from: FinalFieldDefinition.java */
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/MethodCallFinder.class */
class MethodCallFinder extends DepthFirstAdapter {
    GAssignStmt def;
    boolean foundIt;
    boolean anyMethodCalls;

    public MethodCallFinder(GAssignStmt def) {
        this.foundIt = false;
        this.anyMethodCalls = false;
        this.def = def;
    }

    public MethodCallFinder(boolean verbose, GAssignStmt def) {
        super(verbose);
        this.foundIt = false;
        this.anyMethodCalls = false;
        this.def = def;
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outDefinitionStmt(DefinitionStmt s) {
        if ((s instanceof GAssignStmt) && ((GAssignStmt) s).equals(this.def)) {
            this.foundIt = true;
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inInvokeExpr(InvokeExpr ie) {
        if (this.foundIt) {
            this.anyMethodCalls = true;
        }
    }

    public boolean anyMethodCalls() {
        return this.anyMethodCalls;
    }
}
