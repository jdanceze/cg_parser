package soot.jimple.toolkits.pointer.nativemethods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/nativemethods/NativeMethodClass.class */
public abstract class NativeMethodClass {
    private static final Logger logger = LoggerFactory.getLogger(NativeMethodClass.class);
    private static final boolean DEBUG = false;
    protected NativeHelper helper;

    public abstract void simulateMethod(SootMethod sootMethod, ReferenceVariable referenceVariable, ReferenceVariable referenceVariable2, ReferenceVariable[] referenceVariableArr);

    public NativeMethodClass(NativeHelper helper) {
        this.helper = helper;
    }

    public static void defaultMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
    }
}
