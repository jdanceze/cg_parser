package soot.baf;

import java.util.List;
import soot.SootMethodRef;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/baf/DynamicInvokeInst.class */
public interface DynamicInvokeInst extends MethodArgInst {
    SootMethodRef getBootstrapMethodRef();

    List<Value> getBootstrapArgs();

    int getHandleTag();
}
