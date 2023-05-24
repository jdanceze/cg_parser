package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.Environment;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/nativemethods/JavaLangObjectNative.class */
public class JavaLangObjectNative extends NativeMethodClass {
    public JavaLangObjectNative(NativeHelper helper) {
        super(helper);
    }

    @Override // soot.jimple.toolkits.pointer.nativemethods.NativeMethodClass
    public void simulateMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        String subSignature = method.getSubSignature();
        if (subSignature.equals("java.lang.Class getClass()")) {
            java_lang_Object_getClass(method, thisVar, returnVar, params);
        } else if (subSignature.equals("java.lang.Object clone()")) {
            java_lang_Object_clone(method, thisVar, returnVar, params);
        } else {
            defaultMethod(method, thisVar, returnVar, params);
        }
    }

    public void java_lang_Object_getClass(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        this.helper.assignObjectTo(returnVar, Environment.v().getClassObject());
    }

    public void java_lang_Object_clone(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        if (thisVar == null) {
            throw new RuntimeException("Need a 'this' variable to perform a clone()");
        }
        ReferenceVariable newVar = this.helper.cloneObject(thisVar);
        this.helper.assign(returnVar, newVar);
    }
}
