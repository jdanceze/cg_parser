package soot.jimple.infoflow.android.callbacks;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.Kind;
import soot.MethodOrMethodContext;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.util.SystemClassHandler;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.EdgePredicate;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.Targets;
import soot.util.queue.ChunkedQueue;
import soot.util.queue.QueueReader;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/callbacks/ComponentReachableMethods.class */
public class ComponentReachableMethods {
    private final InfoflowAndroidConfiguration config;
    private final SootClass originalComponent;
    private final Set<MethodOrMethodContext> set = new HashSet();
    private final ChunkedQueue<MethodOrMethodContext> reachables = new ChunkedQueue<>();
    private final QueueReader<MethodOrMethodContext> allReachables = this.reachables.reader();
    private QueueReader<MethodOrMethodContext> unprocessedMethods = this.reachables.reader();

    public ComponentReachableMethods(InfoflowAndroidConfiguration config, SootClass originalComponent, Collection<MethodOrMethodContext> entryPoints) {
        this.config = config;
        this.originalComponent = originalComponent;
        addMethods(entryPoints.iterator());
    }

    private void addMethods(Iterator<MethodOrMethodContext> methods) {
        while (methods.hasNext()) {
            addMethod(methods.next());
        }
    }

    private void addMethod(MethodOrMethodContext m) {
        if (!SystemClassHandler.v().isClassInSystemPackage(m.method().getDeclaringClass().getName()) && this.set.add(m)) {
            this.reachables.add(m);
        }
    }

    public void update() {
        while (this.unprocessedMethods.hasNext()) {
            MethodOrMethodContext m = this.unprocessedMethods.next();
            Filter filter = new Filter(new EdgePredicate() { // from class: soot.jimple.infoflow.android.callbacks.ComponentReachableMethods.1
                @Override // soot.jimple.toolkits.callgraph.EdgePredicate
                public boolean want(Edge e) {
                    if (e.kind() == Kind.CLINIT) {
                        return false;
                    }
                    if (e.kind() != Kind.VIRTUAL) {
                        if (ComponentReachableMethods.this.config.getCallbackConfig().getFilterThreadCallbacks()) {
                            if (e.kind() == Kind.THREAD || e.kind() == Kind.EXECUTOR) {
                                return false;
                            }
                            if (e.tgt().getName().equals("run") && Scene.v().getFastHierarchy().canStoreType(e.tgt().getDeclaringClass().getType(), RefType.v("java.lang.Runnable"))) {
                                return false;
                            }
                            return true;
                        }
                        return true;
                    } else if (!e.src().isStatic() && (e.srcStmt().getInvokeExpr() instanceof InstanceInvokeExpr)) {
                        SootMethod refMethod = e.srcStmt().getInvokeExpr().getMethod();
                        InstanceInvokeExpr iinv = (InstanceInvokeExpr) e.srcStmt().getInvokeExpr();
                        if (iinv.getBase() == e.src().getActiveBody().getThisLocal()) {
                            SootClass calleeClass = refMethod.getDeclaringClass();
                            if (Scene.v().getFastHierarchy().isSubclass(ComponentReachableMethods.this.originalComponent, calleeClass)) {
                                SootClass targetClass = e.getTgt().method().getDeclaringClass();
                                return targetClass == ComponentReachableMethods.this.originalComponent || Scene.v().getFastHierarchy().isSubclass(targetClass, ComponentReachableMethods.this.originalComponent);
                            }
                        }
                        if (SystemClassHandler.v().isClassInSystemPackage(refMethod.getDeclaringClass().getName())) {
                            return false;
                        }
                        return true;
                    } else {
                        return true;
                    }
                }
            });
            Iterator<Edge> targets = filter.wrap(Scene.v().getCallGraph().edgesOutOf(m));
            addMethods(new Targets(targets));
        }
    }

    public QueueReader<MethodOrMethodContext> listener() {
        return this.allReachables.m3089clone();
    }

    public QueueReader<MethodOrMethodContext> newListener() {
        return this.reachables.reader();
    }

    public boolean contains(MethodOrMethodContext m) {
        return this.set.contains(m);
    }

    public int size() {
        return this.set.size();
    }
}
