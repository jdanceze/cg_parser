package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.Environment;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/nativemethods/JavaNetInetAddressImplNative.class */
public class JavaNetInetAddressImplNative extends NativeMethodClass {
    public JavaNetInetAddressImplNative(NativeHelper helper) {
        super(helper);
    }

    @Override // soot.jimple.toolkits.pointer.nativemethods.NativeMethodClass
    public void simulateMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        String subSignature = method.getSubSignature();
        if (subSignature.equals("java.lang.String getLocalHostName()")) {
            java_net_InetAddressImpl_getLocalHostName(method, thisVar, returnVar, params);
        } else if (subSignature.equals("java.lang.String getHostByAddress(int)")) {
            java_net_InetAddressImpl_getHostByAddr(method, thisVar, returnVar, params);
        } else {
            defaultMethod(method, thisVar, returnVar, params);
        }
    }

    public void java_net_InetAddressImpl_getLocalHostName(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        this.helper.assignObjectTo(returnVar, Environment.v().getStringObject());
    }

    public void java_net_InetAddressImpl_getHostByAddr(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        this.helper.assignObjectTo(returnVar, Environment.v().getStringObject());
    }
}
