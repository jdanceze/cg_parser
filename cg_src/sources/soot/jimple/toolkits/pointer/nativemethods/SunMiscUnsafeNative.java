package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/nativemethods/SunMiscUnsafeNative.class */
public class SunMiscUnsafeNative extends NativeMethodClass {
    public SunMiscUnsafeNative(NativeHelper helper) {
        super(helper);
    }

    @Override // soot.jimple.toolkits.pointer.nativemethods.NativeMethodClass
    public void simulateMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        String subSignature = method.getSubSignature();
        if (subSignature.equals("java.lang.Object allocateInstance(java.lang.Class)")) {
            sun_misc_Unsafe_allocateInstance(method, thisVar, returnVar, params);
        } else {
            defaultMethod(method, thisVar, returnVar, params);
        }
    }

    public void sun_misc_Unsafe_allocateInstance(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
        ReferenceVariable instanceVar = this.helper.newInstanceOf(thisVar);
        this.helper.assign(returnVar, instanceVar);
    }
}
