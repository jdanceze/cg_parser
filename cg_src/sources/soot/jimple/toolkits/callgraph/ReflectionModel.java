package soot.jimple.toolkits.callgraph;

import soot.SootMethod;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/ReflectionModel.class */
public interface ReflectionModel {
    void methodInvoke(SootMethod sootMethod, Stmt stmt);

    void classNewInstance(SootMethod sootMethod, Stmt stmt);

    void contructorNewInstance(SootMethod sootMethod, Stmt stmt);

    void classForName(SootMethod sootMethod, Stmt stmt);
}
