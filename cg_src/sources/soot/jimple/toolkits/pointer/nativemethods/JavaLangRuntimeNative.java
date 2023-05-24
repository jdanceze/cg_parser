package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.Environment;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/nativemethods/JavaLangRuntimeNative.class */
public class JavaLangRuntimeNative extends NativeMethodClass {
    public JavaLangRuntimeNative(NativeHelper helper) {
        super(helper);
    }

    @Override // soot.jimple.toolkits.pointer.nativemethods.NativeMethodClass
    public void simulateMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        String subSignature = method.getSubSignature();
        if (subSignature.equals("java.lang.Process execInternal(java.lang.String[],java.lang.String[],java.lang.String)")) {
            java_lang_Runtime_execInternal(method, thisVar, returnVar, params);
        } else {
            defaultMethod(method, thisVar, returnVar, params);
        }
    }

    public void java_lang_Runtime_execInternal(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        this.helper.assignObjectTo(returnVar, Environment.v().getProcessObject());
    }
}
