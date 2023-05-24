package soot.baf;

import soot.SootMethod;
import soot.SootMethodRef;
/* loaded from: gencallgraphv3.jar:soot/baf/MethodArgInst.class */
public interface MethodArgInst extends Inst {
    SootMethodRef getMethodRef();

    SootMethod getMethod();
}
