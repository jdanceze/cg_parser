package soot.jimple.infoflow.nativeCallHandler;

import java.util.Collections;
import java.util.Set;
import soot.Value;
import soot.jimple.Stmt;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/nativeCallHandler/BackwardNativeCallHandler.class */
public class BackwardNativeCallHandler extends AbstractNativeCallHandler {
    private static final String SIG_ARRAYCOPY = "<java.lang.System: void arraycopy(java.lang.Object,int,java.lang.Object,int,int)>";
    private static final String SIG_COMPARE_AND_SWAP_OBJECT = "<sun.misc.Unsafe: boolean compareAndSwapObject(java.lang.Object,long,java.lang.Object,java.lang.Object)>";

    @Override // soot.jimple.infoflow.nativeCallHandler.INativeCallHandler
    public Set<Abstraction> getTaintedValues(Stmt call, Abstraction source, Value[] params) {
        if (source.isAbstractionActive()) {
            Value taintedValue = source.getAccessPath().getPlainValue();
            String signature = call.getInvokeExpr().getMethod().getSignature();
            switch (signature.hashCode()) {
                case -505374034:
                    if (signature.equals(SIG_COMPARE_AND_SWAP_OBJECT) && params[0].equals(taintedValue) && this.manager.getTypeUtils().checkCast(source.getAccessPath(), params[0].getType())) {
                        AccessPath ap = this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), params[3], null, false, true, AccessPath.ArrayTaintType.Length);
                        Abstraction abs = source.deriveNewAbstraction(ap, call);
                        abs.setCorrespondingCallSite(call);
                        return Collections.singleton(abs);
                    }
                    return null;
                case -325913181:
                    if (signature.equals(SIG_ARRAYCOPY) && params[2].equals(taintedValue) && this.manager.getTypeUtils().checkCast(source.getAccessPath(), params[0].getType())) {
                        AccessPath ap2 = this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), params[0], source.getAccessPath().getBaseType(), false);
                        Abstraction abs2 = source.deriveNewAbstraction(ap2, call);
                        abs2.setCorrespondingCallSite(call);
                        return Collections.singleton(abs2);
                    }
                    return null;
                default:
                    return null;
            }
        }
        return null;
    }

    @Override // soot.jimple.infoflow.nativeCallHandler.INativeCallHandler
    public boolean supportsCall(Stmt call) {
        return call.containsInvokeExpr() && call.getInvokeExpr().getMethod().getSignature().equals(SIG_ARRAYCOPY);
    }
}
