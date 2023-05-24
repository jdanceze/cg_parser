package soot.jimple.toolkits.annotation.callgraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Body;
import soot.G;
import soot.MethodOrMethodContext;
import soot.MethodToContexts;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.VoidType;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.options.CGGOptions;
import soot.options.Options;
import soot.toolkits.graph.interaction.InteractionHandler;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/callgraph/CallGraphGrapher.class */
public class CallGraphGrapher extends SceneTransformer {
    private static final Logger logger = LoggerFactory.getLogger(CallGraphGrapher.class);
    private MethodToContexts methodToContexts;
    private CallGraph cg;
    private boolean showLibMeths;
    private SootMethod nextMethod;

    public CallGraphGrapher(Singletons.Global g) {
    }

    public static CallGraphGrapher v() {
        return G.v().soot_jimple_toolkits_annotation_callgraph_CallGraphGrapher();
    }

    private ArrayList<MethInfo> getTgtMethods(SootMethod method, boolean recurse) {
        if (!method.hasActiveBody()) {
            return new ArrayList<>();
        }
        Body b = method.getActiveBody();
        ArrayList<MethInfo> list = new ArrayList<>();
        Iterator sIt = b.getUnits().iterator();
        while (sIt.hasNext()) {
            Stmt s = (Stmt) sIt.next();
            Iterator edges = this.cg.edgesOutOf(s);
            while (edges.hasNext()) {
                Edge e = edges.next();
                SootMethod sm = e.tgt();
                if (sm.getDeclaringClass().isLibraryClass()) {
                    if (isShowLibMeths()) {
                        if (recurse) {
                            list.add(new MethInfo(sm, hasTgtMethods(sm) | hasSrcMethods(sm), e.kind()));
                        } else {
                            list.add(new MethInfo(sm, true, e.kind()));
                        }
                    }
                } else if (recurse) {
                    list.add(new MethInfo(sm, hasTgtMethods(sm) | hasSrcMethods(sm), e.kind()));
                } else {
                    list.add(new MethInfo(sm, true, e.kind()));
                }
            }
        }
        return list;
    }

    private boolean hasTgtMethods(SootMethod meth) {
        ArrayList<MethInfo> list = getTgtMethods(meth, false);
        if (!list.isEmpty()) {
            return true;
        }
        return false;
    }

    private boolean hasSrcMethods(SootMethod meth) {
        ArrayList<MethInfo> list = getSrcMethods(meth, false);
        if (list.size() > 1) {
            return true;
        }
        return false;
    }

    private ArrayList<MethInfo> getSrcMethods(SootMethod method, boolean recurse) {
        ArrayList<MethInfo> list = new ArrayList<>();
        for (MethodOrMethodContext momc : this.methodToContexts.get(method)) {
            Iterator callerEdges = this.cg.edgesInto(momc);
            while (callerEdges.hasNext()) {
                Edge callEdge = callerEdges.next();
                SootMethod methodCaller = callEdge.src();
                if (methodCaller.getDeclaringClass().isLibraryClass()) {
                    if (isShowLibMeths()) {
                        if (recurse) {
                            list.add(new MethInfo(methodCaller, hasTgtMethods(methodCaller) | hasSrcMethods(methodCaller), callEdge.kind()));
                        } else {
                            list.add(new MethInfo(methodCaller, true, callEdge.kind()));
                        }
                    }
                } else if (recurse) {
                    list.add(new MethInfo(methodCaller, hasTgtMethods(methodCaller) | hasSrcMethods(methodCaller), callEdge.kind()));
                } else {
                    list.add(new MethInfo(methodCaller, true, callEdge.kind()));
                }
            }
        }
        return list;
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map options) {
        CGGOptions opts = new CGGOptions(options);
        if (opts.show_lib_meths()) {
            setShowLibMeths(true);
        }
        this.cg = Scene.v().getCallGraph();
        if (Options.v().interactive_mode()) {
            reset();
        }
    }

    public void reset() {
        if (this.methodToContexts == null) {
            this.methodToContexts = new MethodToContexts(Scene.v().getReachableMethods().listener());
        }
        if (Scene.v().hasCallGraph()) {
            SootClass sc = Scene.v().getMainClass();
            SootMethod sm = getFirstMethod(sc);
            ArrayList<MethInfo> tgts = getTgtMethods(sm, true);
            ArrayList<MethInfo> srcs = getSrcMethods(sm, true);
            CallGraphInfo info = new CallGraphInfo(sm, tgts, srcs);
            InteractionHandler.v().handleCallGraphStart(info, this);
        }
    }

    private SootMethod getFirstMethod(SootClass sc) {
        ArrayList paramTypes = new ArrayList();
        paramTypes.add(ArrayType.v(RefType.v("java.lang.String"), 1));
        SootMethod sm = sc.getMethodUnsafe("main", paramTypes, VoidType.v());
        if (sm != null) {
            return sm;
        }
        return sc.getMethods().get(0);
    }

    public void handleNextMethod() {
        if (!getNextMethod().hasActiveBody()) {
            return;
        }
        ArrayList<MethInfo> tgts = getTgtMethods(getNextMethod(), true);
        ArrayList<MethInfo> srcs = getSrcMethods(getNextMethod(), true);
        CallGraphInfo info = new CallGraphInfo(getNextMethod(), tgts, srcs);
        InteractionHandler.v().handleCallGraphPart(info);
    }

    public void setNextMethod(SootMethod m) {
        this.nextMethod = m;
    }

    public SootMethod getNextMethod() {
        return this.nextMethod;
    }

    public void setShowLibMeths(boolean b) {
        this.showLibMeths = b;
    }

    public boolean isShowLibMeths() {
        return this.showLibMeths;
    }
}
