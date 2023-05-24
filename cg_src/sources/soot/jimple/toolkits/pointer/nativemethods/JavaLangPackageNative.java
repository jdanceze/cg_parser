package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.Environment;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/nativemethods/JavaLangPackageNative.class */
public class JavaLangPackageNative extends NativeMethodClass {
    public JavaLangPackageNative(NativeHelper helper) {
        super(helper);
    }

    @Override // soot.jimple.toolkits.pointer.nativemethods.NativeMethodClass
    public void simulateMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        String subSignature = method.getSubSignature();
        if (subSignature.equals("java.lang.String getSystemPackage0(java.lang.String)")) {
            java_lang_Package_getSystemPackage0(method, thisVar, returnVar, params);
        } else if (subSignature.equals("java.lang.String[] getSystemPackages0()")) {
            java_lang_Package_getSystemPackages0(method, thisVar, returnVar, params);
        } else {
            defaultMethod(method, thisVar, returnVar, params);
        }
    }

    public void java_lang_Package_getSystemPackage0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        this.helper.assignObjectTo(returnVar, Environment.v().getStringObject());
    }

    public void java_lang_Package_getSystemPackages0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        this.helper.assignObjectTo(returnVar, Environment.v().getStringObject());
    }
}
