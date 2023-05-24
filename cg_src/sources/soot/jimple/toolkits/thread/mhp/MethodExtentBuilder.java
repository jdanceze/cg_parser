package soot.jimple.toolkits.thread.mhp;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.Body;
import soot.SootMethod;
import soot.Value;
import soot.jimple.InvokeExpr;
import soot.jimple.MonitorStmt;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.thread.mhp.pegcallgraph.CheckRecursiveCalls;
import soot.jimple.toolkits.thread.mhp.pegcallgraph.PegCallGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/MethodExtentBuilder.class */
public class MethodExtentBuilder {
    private final Set<Object> methodsNeedingInlining = new HashSet();

    public MethodExtentBuilder(Body unitBody, PegCallGraph pcg, CallGraph cg) {
        build(pcg, cg);
        new CheckRecursiveCalls(pcg, this.methodsNeedingInlining);
        propagate(pcg);
    }

    public Set<Object> getMethodsNeedingInlining() {
        return this.methodsNeedingInlining;
    }

    private void build(PegCallGraph pcg, CallGraph cg) {
        Iterator it = pcg.iterator();
        while (it.hasNext()) {
            SootMethod method = (SootMethod) it.next();
            computeForMethodInlining(method, cg);
        }
    }

    private void computeForMethodInlining(SootMethod targetMethod, CallGraph cg) {
        if (targetMethod.isSynchronized()) {
            this.methodsNeedingInlining.add(targetMethod);
            return;
        }
        Body mBody = targetMethod.getActiveBody();
        Iterator bodyIt = mBody.getUnits().iterator();
        while (bodyIt.hasNext()) {
            Stmt stmt = (Stmt) bodyIt.next();
            if (stmt instanceof MonitorStmt) {
                this.methodsNeedingInlining.add(targetMethod);
                return;
            } else if (stmt.containsInvokeExpr()) {
                Value invokeExpr = stmt.getInvokeExpr();
                SootMethod method = ((InvokeExpr) invokeExpr).getMethod();
                String name = method.getName();
                if (name.equals("wait") || name.equals("notify") || name.equals("notifyAll") || ((name.equals("start") || name.equals("join") || name.equals("suspend") || name.equals("resume") || name.equals("destroy") || name.equals("stop")) && method.getDeclaringClass().getName().equals("java.lang.Thread"))) {
                    this.methodsNeedingInlining.add(targetMethod);
                    return;
                } else if (method.isConcrete() && !method.getDeclaringClass().isLibraryClass()) {
                    cg.edgesOutOf(stmt);
                    TargetMethodsFinder tmd = new TargetMethodsFinder();
                    for (SootMethod target : tmd.find(stmt, cg, true, false)) {
                        if (target.isSynchronized()) {
                            this.methodsNeedingInlining.add(targetMethod);
                            return;
                        }
                    }
                    continue;
                }
            }
        }
    }

    protected void propagate(PegCallGraph cg) {
        Set<Object> gray = new HashSet<>();
        Iterator it = cg.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (!this.methodsNeedingInlining.contains(o) && !gray.contains(o) && visitNode(o, gray, cg)) {
                this.methodsNeedingInlining.add(o);
            }
        }
    }

    private boolean visitNode(Object o, Set<Object> gray, PegCallGraph cg) {
        gray.add(o);
        for (Object child : cg.getSuccsOf(o)) {
            if (this.methodsNeedingInlining.contains(child)) {
                gray.add(child);
                return true;
            } else if (!gray.contains(child) && visitNode(child, gray, cg)) {
                this.methodsNeedingInlining.add(child);
                return true;
            }
        }
        return false;
    }
}
