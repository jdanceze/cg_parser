package soot.jimple.toolkits.thread;

import soot.SootMethod;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/IThreadLocalObjectsAnalysis.class */
public interface IThreadLocalObjectsAnalysis {
    boolean isObjectThreadLocal(Value value, SootMethod sootMethod);
}
