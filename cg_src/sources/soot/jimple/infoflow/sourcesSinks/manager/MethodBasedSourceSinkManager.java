package soot.jimple.infoflow.sourcesSinks.manager;

import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.AccessPath;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/manager/MethodBasedSourceSinkManager.class */
public abstract class MethodBasedSourceSinkManager implements ISourceSinkManager {
    static final /* synthetic */ boolean $assertionsDisabled;

    public abstract SourceInfo getSourceMethodInfo(SootMethod sootMethod);

    public abstract SinkInfo getSinkMethodInfo(SootMethod sootMethod);

    static {
        $assertionsDisabled = !MethodBasedSourceSinkManager.class.desiredAssertionStatus();
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager
    public SourceInfo getSourceInfo(Stmt sCallSite, InfoflowManager manager) {
        if ($assertionsDisabled || sCallSite != null) {
            if (!sCallSite.containsInvokeExpr()) {
                return null;
            }
            return getSourceMethodInfo(sCallSite.getInvokeExpr().getMethod());
        }
        throw new AssertionError();
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager
    public SinkInfo getSinkInfo(Stmt sCallSite, InfoflowManager manager, AccessPath ap) {
        if ($assertionsDisabled || sCallSite != null) {
            if (!sCallSite.containsInvokeExpr()) {
                return null;
            }
            return getSinkMethodInfo(sCallSite.getInvokeExpr().getMethod());
        }
        throw new AssertionError();
    }

    protected boolean typeIsString(Type type) {
        return (type instanceof RefType) && ((RefType) type).getSootClass().getName().equals("java.lang.String");
    }
}
