package soot.jimple.toolkits.thread.mhp;

import java.util.List;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.thread.AbstractRuntimeThread;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/MhpTester.class */
public interface MhpTester {
    boolean mayHappenInParallel(SootMethod sootMethod, SootMethod sootMethod2);

    boolean mayHappenInParallel(SootMethod sootMethod, Unit unit, SootMethod sootMethod2, Unit unit2);

    void printMhpSummary();

    List<AbstractRuntimeThread> getThreads();
}
