package soot.jimple.toolkits.callgraph;

import soot.Local;
import soot.SootMethod;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.ConstantArrayAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/InvokeCallSite.class */
public class InvokeCallSite extends AbstractCallSite {
    public static final int MUST_BE_NULL = 0;
    public static final int MUST_NOT_BE_NULL = 1;
    public static final int MAY_BE_NULL = -1;
    private final InstanceInvokeExpr iie;
    private final Local argArray;
    private final Local base;
    private final int nullnessCode;
    private final ConstantArrayAnalysis.ArrayTypes reachingTypes;

    public InvokeCallSite(Stmt stmt, SootMethod container, InstanceInvokeExpr iie, Local base) {
        this(stmt, container, iie, base, (Local) null, 0);
    }

    public InvokeCallSite(Stmt stmt, SootMethod container, InstanceInvokeExpr iie, Local base, Local argArray, int nullnessCode) {
        super(stmt, container);
        this.iie = iie;
        this.base = base;
        this.argArray = argArray;
        this.nullnessCode = nullnessCode;
        this.reachingTypes = null;
    }

    public InvokeCallSite(Stmt stmt, SootMethod container, InstanceInvokeExpr iie, Local base, ConstantArrayAnalysis.ArrayTypes reachingArgTypes, int nullnessCode) {
        super(stmt, container);
        this.iie = iie;
        this.base = base;
        this.argArray = null;
        this.nullnessCode = nullnessCode;
        this.reachingTypes = reachingArgTypes;
    }

    @Deprecated
    public Stmt stmt() {
        return this.stmt;
    }

    @Deprecated
    public SootMethod container() {
        return this.container;
    }

    public InstanceInvokeExpr iie() {
        return this.iie;
    }

    public Local base() {
        return this.base;
    }

    public Local argArray() {
        return this.argArray;
    }

    public int nullnessCode() {
        return this.nullnessCode;
    }

    public ConstantArrayAnalysis.ArrayTypes reachingTypes() {
        return this.reachingTypes;
    }

    @Override // soot.jimple.toolkits.callgraph.AbstractCallSite
    public String toString() {
        return this.stmt.toString();
    }
}
