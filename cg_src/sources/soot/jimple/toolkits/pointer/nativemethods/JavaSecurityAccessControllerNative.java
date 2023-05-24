package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.Environment;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/nativemethods/JavaSecurityAccessControllerNative.class */
public class JavaSecurityAccessControllerNative extends NativeMethodClass {
    public JavaSecurityAccessControllerNative(NativeHelper helper) {
        super(helper);
    }

    @Override // soot.jimple.toolkits.pointer.nativemethods.NativeMethodClass
    public void simulateMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        String subSignature = method.getSubSignature();
        if (subSignature.equals("java.lang.Object doPrivileged(java.security.PrivilegedAction)")) {
            java_security_AccessController_doPrivileged(method, thisVar, returnVar, params);
        } else if (subSignature.equals("java.lang.Object doPrivileged(java.security.PrivilegedExceptionAction)")) {
            java_security_AccessController_doPrivileged(method, thisVar, returnVar, params);
        } else if (subSignature.equals("java.lang.Object doPrivileged(java.security.PrivilegedAction,java.security.AccessControlContext)")) {
            java_security_AccessController_doPrivileged(method, thisVar, returnVar, params);
        } else if (subSignature.equals("java.lang.Object doPrivileged(java.security.PrivilegedExceptionAction,java.security.AccessControlContext)")) {
            java_security_AccessController_doPrivileged(method, thisVar, returnVar, params);
        } else if (subSignature.equals("java.security.AccessControlContext getStackAccessControlContext()")) {
            java_security_AccessController_getStackAccessControlContext(method, thisVar, returnVar, params);
        } else if (subSignature.equals("java.security.AccessControlContext getInheritedAccessControlContext()")) {
            java_security_AccessController_getInheritedAccessControlContext(method, thisVar, returnVar, params);
        } else {
            defaultMethod(method, thisVar, returnVar, params);
        }
    }

    public void java_security_AccessController_doPrivileged(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        this.helper.throwException(Environment.v().getPrivilegedActionExceptionObject());
    }

    public void java_security_AccessController_getStackAccessControlContext(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        this.helper.assignObjectTo(returnVar, Environment.v().getAccessControlContext());
    }

    public void java_security_AccessController_getInheritedAccessControlContext(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        this.helper.assignObjectTo(returnVar, Environment.v().getAccessControlContext());
    }
}
