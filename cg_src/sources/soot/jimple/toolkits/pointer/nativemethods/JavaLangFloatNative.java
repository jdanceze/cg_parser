package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/nativemethods/JavaLangFloatNative.class */
public class JavaLangFloatNative extends NativeMethodClass {
    public JavaLangFloatNative(NativeHelper helper) {
        super(helper);
    }

    @Override // soot.jimple.toolkits.pointer.nativemethods.NativeMethodClass
    public void simulateMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        method.getSubSignature();
        defaultMethod(method, thisVar, returnVar, params);
    }
}
