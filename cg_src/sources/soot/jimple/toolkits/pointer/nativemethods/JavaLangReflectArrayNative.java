package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/nativemethods/JavaLangReflectArrayNative.class */
public class JavaLangReflectArrayNative extends NativeMethodClass {
    public JavaLangReflectArrayNative(NativeHelper helper) {
        super(helper);
    }

    @Override // soot.jimple.toolkits.pointer.nativemethods.NativeMethodClass
    public void simulateMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        String subSignature = method.getSubSignature();
        if (subSignature.equals("java.lang.Object get(java.lang.Object,int)")) {
            java_lang_reflect_Array_get(method, thisVar, returnVar, params);
        } else if (subSignature.equals("void set(java.lang.Object,int,java.lang.Object)")) {
            java_lang_reflect_Array_set(method, thisVar, returnVar, params);
        } else if (subSignature.equals("java.lang.Object newArray(java.lang.Class,int)")) {
            java_lang_reflect_Array_newArray(method, thisVar, returnVar, params);
        } else if (subSignature.equals("java.lang.Object multiNewArray(java.lang.Class,int[])")) {
            java_lang_reflect_Array_multiNewArray(method, thisVar, returnVar, params);
        } else {
            defaultMethod(method, thisVar, returnVar, params);
        }
    }

    public void java_lang_reflect_Array_get(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        throw new NativeMethodNotSupportedException(method);
    }

    public void java_lang_reflect_Array_set(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        throw new NativeMethodNotSupportedException(method);
    }

    public void java_lang_reflect_Array_newArray(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        throw new NativeMethodNotSupportedException(method);
    }

    public void java_lang_reflect_Array_multiNewArray(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        throw new NativeMethodNotSupportedException(method);
    }
}
