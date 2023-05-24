package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.Environment;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/nativemethods/JavaLangClassLoaderNative.class */
public class JavaLangClassLoaderNative extends NativeMethodClass {
    public JavaLangClassLoaderNative(NativeHelper helper) {
        super(helper);
    }

    @Override // soot.jimple.toolkits.pointer.nativemethods.NativeMethodClass
    public void simulateMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        String subSignature = method.getSubSignature();
        if (subSignature.equals("java.lang.Class defineClass0(java.lang.String,byte[],int,int,java.lang.security.ProtectionDomain)")) {
            java_lang_ClassLoader_defineClass0(method, thisVar, returnVar, params);
        } else if (subSignature.equals("java.lang.Class findBootstrapClass(java.lang.String)")) {
            java_lang_ClassLoader_findBootstrapClass(method, thisVar, returnVar, params);
        } else if (subSignature.equals("java.lang.Class findLoadedClass(java.lang.String)")) {
            java_lang_ClassLoader_findLoadedClass(method, thisVar, returnVar, params);
        } else if (subSignature.equals("java.lang.ClassLoader getCallerClassLoader()")) {
            java_lang_ClassLoader_getCallerClassLoader(method, thisVar, returnVar, params);
        } else {
            defaultMethod(method, thisVar, returnVar, params);
        }
    }

    public void java_lang_ClassLoader_defineClass0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        this.helper.assignObjectTo(returnVar, Environment.v().getClassObject());
    }

    public void java_lang_ClassLoader_findBootstrapClass(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        this.helper.assignObjectTo(returnVar, Environment.v().getClassObject());
    }

    public void java_lang_ClassLoader_findLoadedClass(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        this.helper.assignObjectTo(returnVar, Environment.v().getClassObject());
    }

    public void java_lang_ClassLoader_getCallerClassLoader(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        this.helper.assignObjectTo(returnVar, Environment.v().getClassLoaderObject());
    }
}
