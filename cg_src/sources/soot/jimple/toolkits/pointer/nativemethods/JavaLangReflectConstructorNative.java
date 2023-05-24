package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/nativemethods/JavaLangReflectConstructorNative.class */
public class JavaLangReflectConstructorNative extends NativeMethodClass {
    public JavaLangReflectConstructorNative(NativeHelper helper) {
        super(helper);
    }

    @Override // soot.jimple.toolkits.pointer.nativemethods.NativeMethodClass
    public void simulateMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        String subSignature = method.getSubSignature();
        if (subSignature.equals("java.lang.Object newInstance(java.lang.Object[])")) {
            java_lang_reflect_Constructor_newInstance(method, thisVar, returnVar, params);
        } else {
            defaultMethod(method, thisVar, returnVar, params);
        }
    }

    public void java_lang_reflect_Constructor_newInstance(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        throw new NativeMethodNotSupportedException(method);
    }
}
