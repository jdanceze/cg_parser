package soot.jimple.spark.builder;

import soot.AnySubType;
import soot.ArrayType;
import soot.PointsToAnalysis;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.ContextVarNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.VarNode;
import soot.toolkits.scalar.Pair;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/builder/GlobalNodeFactory.class */
public class GlobalNodeFactory {
    protected final RefType rtObject = Scene.v().getObjectType();
    protected final RefType rtClassLoader = RefType.v("java.lang.ClassLoader");
    protected final RefType rtString = RefType.v("java.lang.String");
    protected final RefType rtThread = RefType.v("java.lang.Thread");
    protected final RefType rtThreadGroup = RefType.v("java.lang.ThreadGroup");
    protected final RefType rtThrowable = Scene.v().getBaseExceptionType();
    protected PAG pag;

    public GlobalNodeFactory(PAG pag) {
        this.pag = pag;
    }

    public final Node caseDefaultClassLoader() {
        AllocNode a = this.pag.makeAllocNode(PointsToAnalysis.DEFAULT_CLASS_LOADER, AnySubType.v(this.rtClassLoader), null);
        VarNode v = this.pag.makeGlobalVarNode(PointsToAnalysis.DEFAULT_CLASS_LOADER_LOCAL, this.rtClassLoader);
        this.pag.addEdge(a, v);
        return v;
    }

    public final Node caseMainClassNameString() {
        AllocNode a = this.pag.makeAllocNode(PointsToAnalysis.MAIN_CLASS_NAME_STRING, this.rtString, null);
        VarNode v = this.pag.makeGlobalVarNode(PointsToAnalysis.MAIN_CLASS_NAME_STRING_LOCAL, this.rtString);
        this.pag.addEdge(a, v);
        return v;
    }

    public final Node caseMainThreadGroup() {
        AllocNode threadGroupNode = this.pag.makeAllocNode(PointsToAnalysis.MAIN_THREAD_GROUP_NODE, this.rtThreadGroup, null);
        VarNode threadGroupNodeLocal = this.pag.makeGlobalVarNode(PointsToAnalysis.MAIN_THREAD_GROUP_NODE_LOCAL, this.rtThreadGroup);
        this.pag.addEdge(threadGroupNode, threadGroupNodeLocal);
        return threadGroupNodeLocal;
    }

    public final Node casePrivilegedActionException() {
        AllocNode a = this.pag.makeAllocNode(PointsToAnalysis.PRIVILEGED_ACTION_EXCEPTION, AnySubType.v(RefType.v("java.security.PrivilegedActionException")), null);
        VarNode v = this.pag.makeGlobalVarNode(PointsToAnalysis.PRIVILEGED_ACTION_EXCEPTION_LOCAL, RefType.v("java.security.PrivilegedActionException"));
        this.pag.addEdge(a, v);
        return v;
    }

    public final Node caseCanonicalPath() {
        AllocNode a = this.pag.makeAllocNode(PointsToAnalysis.CANONICAL_PATH, this.rtString, null);
        VarNode v = this.pag.makeGlobalVarNode(PointsToAnalysis.CANONICAL_PATH_LOCAL, this.rtString);
        this.pag.addEdge(a, v);
        return v;
    }

    public final Node caseMainThread() {
        AllocNode threadNode = this.pag.makeAllocNode(PointsToAnalysis.MAIN_THREAD_NODE, this.rtThread, null);
        VarNode threadNodeLocal = this.pag.makeGlobalVarNode(PointsToAnalysis.MAIN_THREAD_NODE_LOCAL, this.rtThread);
        this.pag.addEdge(threadNode, threadNodeLocal);
        return threadNodeLocal;
    }

    public final Node caseFinalizeQueue() {
        return this.pag.makeGlobalVarNode(PointsToAnalysis.FINALIZE_QUEUE, this.rtObject);
    }

    public final Node caseArgv() {
        ArrayType strArray = ArrayType.v(this.rtString, 1);
        AllocNode argv = this.pag.makeAllocNode(PointsToAnalysis.STRING_ARRAY_NODE, strArray, null);
        VarNode sanl = this.pag.makeGlobalVarNode(PointsToAnalysis.STRING_ARRAY_NODE_LOCAL, strArray);
        AllocNode stringNode = this.pag.makeAllocNode(PointsToAnalysis.STRING_NODE, this.rtString, null);
        VarNode stringNodeLocal = this.pag.makeGlobalVarNode(PointsToAnalysis.STRING_NODE_LOCAL, this.rtString);
        this.pag.addEdge(argv, sanl);
        this.pag.addEdge(stringNode, stringNodeLocal);
        this.pag.addEdge(stringNodeLocal, this.pag.makeFieldRefNode(sanl, ArrayElement.v()));
        return sanl;
    }

    public final Node caseNewInstance(VarNode cls) {
        if (cls instanceof ContextVarNode) {
            cls = this.pag.findLocalVarNode(cls.getVariable());
        }
        VarNode local = this.pag.makeGlobalVarNode(cls, this.rtObject);
        for (SootClass cl : Scene.v().dynamicClasses()) {
            AllocNode site = this.pag.makeAllocNode(new Pair(cls, cl), cl.getType(), null);
            this.pag.addEdge(site, local);
        }
        return local;
    }

    public Node caseThrow() {
        VarNode ret = this.pag.makeGlobalVarNode(PointsToAnalysis.EXCEPTION_NODE, this.rtThrowable);
        ret.setInterProcTarget();
        ret.setInterProcSource();
        return ret;
    }
}
