package soot.jimple.toolkits.thread.mhp;

import heros.util.SootThreadGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Kind;
import soot.PointsToAnalysis;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.spark.ondemand.DemandCSPointsTo;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.PAG;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.thread.AbstractRuntimeThread;
import soot.jimple.toolkits.thread.mhp.findobject.AllocNodesFinder;
import soot.jimple.toolkits.thread.mhp.findobject.MultiRunStatementsFinder;
import soot.jimple.toolkits.thread.mhp.pegcallgraph.PegCallGraph;
import soot.options.SparkOptions;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/SynchObliviousMhpAnalysis.class */
public class SynchObliviousMhpAnalysis implements MhpTester, Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SynchObliviousMhpAnalysis.class);
    boolean optionThreaded = false;
    List<AbstractRuntimeThread> threadList = new ArrayList();
    boolean optionPrintDebug = false;
    Thread self = null;

    public SynchObliviousMhpAnalysis() {
        buildThreadList();
    }

    protected void buildThreadList() {
        if (this.optionThreaded) {
            if (this.self != null) {
                return;
            }
            this.self = new Thread(new SootThreadGroup(), this);
            this.self.start();
            return;
        }
        run();
    }

    @Override // java.lang.Runnable
    public void run() {
        SootMethod mainMethod = Scene.v().getMainClass().getMethodByName("main");
        PointsToAnalysis pta = Scene.v().getPointsToAnalysis();
        if (pta instanceof DemandCSPointsTo) {
            DemandCSPointsTo demandCSPointsTo = (DemandCSPointsTo) pta;
            pta = demandCSPointsTo.getPAG();
        }
        if (!(pta instanceof PAG)) {
            throw new RuntimeException("You must use Spark for points-to analysis when computing MHP information!");
        }
        PAG pag = (PAG) pta;
        SparkOptions so = pag.getOpts();
        if (so.rta()) {
            throw new RuntimeException("MHP cannot be calculated using RTA due to incomplete call graph");
        }
        CallGraph callGraph = Scene.v().getCallGraph();
        PegCallGraph pecg = new PegCallGraph(callGraph);
        AllocNodesFinder anf = new AllocNodesFinder(pecg, callGraph, (PAG) pta);
        Set<AllocNode> multiRunAllocNodes = anf.getMultiRunAllocNodes();
        Set<SootMethod> multiCalledMethods = anf.getMultiCalledMethods();
        StartJoinFinder sjf = new StartJoinFinder(callGraph, (PAG) pta);
        Map<Stmt, List<AllocNode>> startToAllocNodes = sjf.getStartToAllocNodes();
        Map<Stmt, List<SootMethod>> startToRunMethods = sjf.getStartToRunMethods();
        Map<Stmt, SootMethod> startToContainingMethod = sjf.getStartToContainingMethod();
        Map<Stmt, Stmt> startToJoin = sjf.getStartToJoin();
        List<AbstractRuntimeThread> runAtOnceCandidates = new ArrayList<>();
        int threadNum = 0;
        for (Map.Entry<Stmt, List<SootMethod>> e : startToRunMethods.entrySet()) {
            Stmt startStmt = e.getKey();
            List<SootMethod> runMethods = e.getValue();
            List threadAllocNodes = startToAllocNodes.get(e.getKey());
            AbstractRuntimeThread thread = new AbstractRuntimeThread();
            thread.setStartStmt(startStmt);
            for (SootMethod method : runMethods) {
                if (!thread.containsMethod(method)) {
                    thread.addMethod(method);
                    thread.addRunMethod(method);
                }
            }
            for (int methodNum = 0; methodNum < thread.methodCount(); methodNum++) {
                for (SootMethod method2 : pecg.getSuccsOf(thread.getMethod(methodNum))) {
                    boolean ignoremethod = true;
                    Iterator edgeInIt = callGraph.edgesInto(method2);
                    while (edgeInIt.hasNext()) {
                        Edge edge = edgeInIt.next();
                        if (edge.kind() != Kind.THREAD && edge.kind() != Kind.EXECUTOR && edge.kind() != Kind.ASYNCTASK && thread.containsMethod(edge.src())) {
                            ignoremethod = false;
                        }
                    }
                    if (!ignoremethod && !thread.containsMethod(method2)) {
                        thread.addMethod(method2);
                    }
                }
            }
            this.threadList.add(thread);
            if (this.optionPrintDebug) {
                System.out.println(thread.toString());
            }
            boolean mayStartMultipleThreadObjects = threadAllocNodes.size() > 1 || so.types_for_sites();
            if (!mayStartMultipleThreadObjects && multiRunAllocNodes.contains(threadAllocNodes.iterator().next())) {
                mayStartMultipleThreadObjects = true;
            }
            if (mayStartMultipleThreadObjects) {
                thread.setStartStmtHasMultipleReachingObjects();
            }
            SootMethod startStmtMethod = startToContainingMethod.get(startStmt);
            thread.setStartStmtMethod(startStmtMethod);
            boolean mayBeRunMultipleTimes = multiCalledMethods.contains(startStmtMethod);
            if (!mayBeRunMultipleTimes) {
                UnitGraph graph = new CompleteUnitGraph(startStmtMethod.getActiveBody());
                MultiRunStatementsFinder finder = new MultiRunStatementsFinder(graph, startStmtMethod, multiCalledMethods, callGraph);
                FlowSet multiRunStatements = finder.getMultiRunStatements();
                if (multiRunStatements.contains(startStmt)) {
                    mayBeRunMultipleTimes = true;
                }
            }
            if (mayBeRunMultipleTimes) {
                thread.setStartStmtMayBeRunMultipleTimes();
            }
            if (mayBeRunMultipleTimes && startToJoin.containsKey(startStmt)) {
                thread.setJoinStmt(startToJoin.get(startStmt));
                mayBeRunMultipleTimes = false;
                List<SootMethod> containingMethodCalls = new ArrayList<>();
                containingMethodCalls.add(startStmtMethod);
                for (int methodNum2 = 0; methodNum2 < containingMethodCalls.size(); methodNum2++) {
                    Iterator succMethodsIt = pecg.getSuccsOf(containingMethodCalls.get(methodNum2)).iterator();
                    while (true) {
                        if (succMethodsIt.hasNext()) {
                            SootMethod method3 = (SootMethod) succMethodsIt.next();
                            if (method3 == startStmtMethod) {
                                mayBeRunMultipleTimes = true;
                                thread.setStartMethodIsReentrant();
                                thread.setRunsMany();
                                break;
                            } else if (!containingMethodCalls.contains(method3)) {
                                containingMethodCalls.add(method3);
                            }
                        }
                    }
                }
                if (!mayBeRunMultipleTimes) {
                    runAtOnceCandidates.add(thread);
                }
            }
            if (this.optionPrintDebug) {
                System.out.println("Start Stmt " + startStmt.toString() + " mayStartMultipleThreadObjects=" + mayStartMultipleThreadObjects + " mayBeRunMultipleTimes=" + mayBeRunMultipleTimes);
            }
            if (mayStartMultipleThreadObjects && mayBeRunMultipleTimes) {
                this.threadList.add(thread);
                thread.setRunsMany();
                if (this.optionPrintDebug) {
                    System.out.println(thread.toString());
                }
            } else {
                thread.setRunsOnce();
            }
            threadNum++;
        }
        AbstractRuntimeThread mainThread = new AbstractRuntimeThread();
        this.threadList.add(mainThread);
        mainThread.setRunsOnce();
        mainThread.addMethod(mainMethod);
        mainThread.addRunMethod(mainMethod);
        mainThread.setIsMainThread();
        for (int methodNum3 = 0; methodNum3 < mainThread.methodCount(); methodNum3++) {
            for (SootMethod method4 : pecg.getSuccsOf(mainThread.getMethod(methodNum3))) {
                boolean ignoremethod2 = true;
                Iterator edgeInIt2 = callGraph.edgesInto(method4);
                while (edgeInIt2.hasNext()) {
                    if (edgeInIt2.next().kind() != Kind.THREAD) {
                        ignoremethod2 = false;
                    }
                }
                if (!ignoremethod2 && !mainThread.containsMethod(method4)) {
                    mainThread.addMethod(method4);
                }
            }
        }
        if (this.optionPrintDebug) {
            logger.debug(mainThread.toString());
        }
        boolean addedNew = true;
        while (addedNew) {
            addedNew = false;
            ListIterator<AbstractRuntimeThread> it = runAtOnceCandidates.listIterator();
            while (it.hasNext()) {
                AbstractRuntimeThread someThread = it.next();
                SootMethod someStartMethod = someThread.getStartStmtMethod();
                if (mayHappenInParallelInternal(someStartMethod, someStartMethod)) {
                    this.threadList.add(someThread);
                    someThread.setStartMethodMayHappenInParallel();
                    someThread.setRunsMany();
                    it.remove();
                    if (this.optionPrintDebug) {
                        logger.debug(someThread.toString());
                    }
                    addedNew = true;
                }
            }
        }
        for (AbstractRuntimeThread someThread2 : runAtOnceCandidates) {
            someThread2.setRunsOneAtATime();
        }
    }

    @Override // soot.jimple.toolkits.thread.mhp.MhpTester
    public boolean mayHappenInParallel(SootMethod m1, Unit u1, SootMethod m2, Unit u2) {
        if (this.optionThreaded) {
            if (this.self == null) {
                return true;
            }
            logger.debug("[mhp] waiting for analysis thread to finish");
            try {
                this.self.join();
            } catch (InterruptedException e) {
                return true;
            }
        }
        return mayHappenInParallelInternal(m1, m2);
    }

    @Override // soot.jimple.toolkits.thread.mhp.MhpTester
    public boolean mayHappenInParallel(SootMethod m1, SootMethod m2) {
        if (this.optionThreaded) {
            if (this.self == null) {
                return true;
            }
            logger.debug("[mhp] waiting for thread to finish");
            try {
                this.self.join();
            } catch (InterruptedException e) {
                return true;
            }
        }
        return mayHappenInParallelInternal(m1, m2);
    }

    private boolean mayHappenInParallelInternal(SootMethod m1, SootMethod m2) {
        if (this.threadList == null) {
            return true;
        }
        int size = this.threadList.size();
        for (int i = 0; i < size; i++) {
            if (this.threadList.get(i).containsMethod(m1)) {
                for (int j = 0; j < size; j++) {
                    if (this.threadList.get(j).containsMethod(m2) && i != j) {
                        return true;
                    }
                }
                continue;
            }
        }
        return false;
    }

    @Override // soot.jimple.toolkits.thread.mhp.MhpTester
    public void printMhpSummary() {
        if (this.optionThreaded) {
            if (this.self == null) {
                return;
            }
            logger.debug("[mhp] waiting for thread to finish");
            try {
                this.self.join();
            } catch (InterruptedException e) {
                return;
            }
        }
        List<AbstractRuntimeThread> threads = new ArrayList<>();
        int size = this.threadList.size();
        logger.debug("[mhp]");
        for (int i = 0; i < size; i++) {
            if (!threads.contains(this.threadList.get(i))) {
                logger.debug("[mhp] " + this.threadList.get(i).toString().replaceAll("\n", "\n[mhp] ").replaceAll(">,", ">\n[mhp]  "));
                logger.debug("[mhp]");
            }
            threads.add(this.threadList.get(i));
        }
    }

    public List<SootClass> getThreadClassList() {
        if (this.optionThreaded) {
            if (this.self == null) {
                return null;
            }
            logger.debug("[mhp] waiting for thread to finish");
            try {
                this.self.join();
            } catch (InterruptedException e) {
                return null;
            }
        }
        if (this.threadList == null) {
            return null;
        }
        List<SootClass> threadClasses = new ArrayList<>();
        int size = this.threadList.size();
        for (int i = 0; i < size; i++) {
            AbstractRuntimeThread thread = this.threadList.get(i);
            Iterator<Object> threadRunMethodIt = thread.getRunMethods().iterator();
            while (threadRunMethodIt.hasNext()) {
                SootClass threadClass = ((SootMethod) threadRunMethodIt.next()).getDeclaringClass();
                if (!threadClasses.contains(threadClass) && threadClass.isApplicationClass()) {
                    threadClasses.add(threadClass);
                }
            }
        }
        return threadClasses;
    }

    @Override // soot.jimple.toolkits.thread.mhp.MhpTester
    public List<AbstractRuntimeThread> getThreads() {
        if (this.optionThreaded) {
            if (this.self == null) {
                return null;
            }
            logger.debug("[mhp] waiting for thread to finish");
            try {
                this.self.join();
            } catch (InterruptedException e) {
                return null;
            }
        }
        if (this.threadList == null) {
            return null;
        }
        List<AbstractRuntimeThread> threads = new ArrayList<>();
        int size = this.threadList.size();
        for (int i = 0; i < size; i++) {
            if (!threads.contains(this.threadList.get(i))) {
                threads.add(this.threadList.get(i));
            }
        }
        return threads;
    }
}
