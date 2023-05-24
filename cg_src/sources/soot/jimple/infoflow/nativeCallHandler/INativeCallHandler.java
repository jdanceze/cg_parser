package soot.jimple.infoflow.nativeCallHandler;

import java.util.Set;
import soot.Value;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/nativeCallHandler/INativeCallHandler.class */
public interface INativeCallHandler {
    void initialize(InfoflowManager infoflowManager);

    Set<Abstraction> getTaintedValues(Stmt stmt, Abstraction abstraction, Value[] valueArr);

    boolean supportsCall(Stmt stmt);

    void shutdown();
}
