package soot.jimple.infoflow.nativeCallHandler;

import java.util.Collections;
import java.util.Set;
import soot.Value;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/nativeCallHandler/DefaultNativeCallHandler.class */
public class DefaultNativeCallHandler extends AbstractNativeCallHandler {
    private static final String SIG_ARRAYCOPY = "<java.lang.System: void arraycopy(java.lang.Object,int,java.lang.Object,int,int)>";
    private static final String SIG_NEW_ARRAY = "<java.lang.reflect.Array: java.lang.Object newArray(java.lang.Class,int)>";
    private static final String SIG_COMPARE_AND_SWAP_OBJECT = "<sun.misc.Unsafe: boolean compareAndSwapObject(java.lang.Object,long,java.lang.Object,java.lang.Object)>";

    @Override // soot.jimple.infoflow.nativeCallHandler.INativeCallHandler
    public Set<Abstraction> getTaintedValues(Stmt call, Abstraction source, Value[] params) {
        if (source.isAbstractionActive()) {
            Value taintedValue = source.getAccessPath().getPlainValue();
            String signature = call.getInvokeExpr().getMethodRef().getSignature();
            switch (signature.hashCode()) {
                case -1003247876:
                    if (signature.equals(SIG_NEW_ARRAY) && params[1].equals(taintedValue) && (call instanceof DefinitionStmt)) {
                        DefinitionStmt defStmt = (DefinitionStmt) call;
                        if (this.manager.getTypeUtils().checkCast(source.getAccessPath(), params[1].getType())) {
                            AccessPath ap = this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), defStmt.getLeftOp(), null, false, true, AccessPath.ArrayTaintType.Length);
                            Abstraction abs = source.deriveNewAbstraction(ap, call);
                            abs.setCorrespondingCallSite(call);
                            return Collections.singleton(abs);
                        }
                        return null;
                    }
                    return null;
                case -505374034:
                    if (signature.equals(SIG_COMPARE_AND_SWAP_OBJECT) && params[3].equals(taintedValue) && this.manager.getTypeUtils().checkCast(source.getAccessPath(), params[3].getType())) {
                        AccessPath ap2 = this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), params[0], null, false, true, AccessPath.ArrayTaintType.Length);
                        Abstraction abs2 = source.deriveNewAbstraction(ap2, call);
                        abs2.setCorrespondingCallSite(call);
                        return Collections.singleton(abs2);
                    }
                    return null;
                case -325913181:
                    if (signature.equals(SIG_ARRAYCOPY) && params[0].equals(taintedValue) && this.manager.getTypeUtils().checkCast(source.getAccessPath(), params[2].getType())) {
                        AccessPath ap3 = this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), params[2], source.getAccessPath().getBaseType(), false);
                        Abstraction abs3 = source.deriveNewAbstraction(ap3, call);
                        abs3.setCorrespondingCallSite(call);
                        return Collections.singleton(abs3);
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
        if (!call.containsInvokeExpr()) {
            return false;
        }
        String sig = call.getInvokeExpr().getMethod().getSignature();
        switch (sig.hashCode()) {
            case -1003247876:
                if (sig.equals(SIG_NEW_ARRAY)) {
                    return true;
                }
                return false;
            case -505374034:
                if (sig.equals(SIG_COMPARE_AND_SWAP_OBJECT)) {
                    return true;
                }
                return false;
            case -325913181:
                if (sig.equals(SIG_ARRAYCOPY)) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }
}
