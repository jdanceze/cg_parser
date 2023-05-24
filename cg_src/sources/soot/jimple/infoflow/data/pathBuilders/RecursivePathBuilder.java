package soot.jimple.infoflow.data.pathBuilders;

import heros.solver.CountingThreadPoolExecutor;
import heros.solver.Pair;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AbstractionAtSink;
import soot.jimple.infoflow.data.SourceContextAndPath;
import soot.jimple.infoflow.memory.IMemoryBoundedSolver;
import soot.jimple.infoflow.memory.ISolverTerminationReason;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.util.FastStack;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/RecursivePathBuilder.class */
public class RecursivePathBuilder extends AbstractAbstractionPathBuilder {
    private final InfoflowResults results;
    private final CountingThreadPoolExecutor executor;
    private static int lastTaskId;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !RecursivePathBuilder.class.desiredAssertionStatus();
        lastTaskId = 0;
    }

    public RecursivePathBuilder(InfoflowManager manager, CountingThreadPoolExecutor executor) {
        super(manager);
        this.executor = executor;
        this.results = new InfoflowResults(manager.getConfig().getPathAgnosticResults());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Set<SourceContextAndPath> getPaths(int taskId, Abstraction curAbs, FastStack<Pair<Stmt, Set<Abstraction>>> callStack) {
        Set<SourceContextAndPath> cacheData = new HashSet<>();
        Pair<Stmt, Set<Abstraction>> stackTop = callStack.isEmpty() ? null : callStack.peek();
        if (stackTop != null) {
            Set<Abstraction> callAbs = stackTop.getO2();
            if (!callAbs.add(curAbs)) {
                return Collections.emptySet();
            }
        }
        if (curAbs.getSourceContext() != null) {
            SourceContextAndPath sourceAndPath = new SourceContextAndPath(this.config, curAbs.getSourceContext().getDefinition(), curAbs.getSourceContext().getAccessPath(), curAbs.getSourceContext().getStmt(), curAbs.getSourceContext().getUserData()).extendPath(curAbs);
            cacheData.add(sourceAndPath);
            if (!$assertionsDisabled && curAbs.getPredecessor() != null) {
                throw new AssertionError();
            }
        } else {
            FastStack<Pair<Stmt, Set<Abstraction>>> newCallStack = new FastStack<>();
            newCallStack.addAll(callStack);
            if (curAbs.getCorrespondingCallSite() != null) {
                newCallStack.push(new Pair<>(curAbs.getCorrespondingCallSite(), Collections.newSetFromMap(new IdentityHashMap())));
            }
            boolean isMethodEnter = curAbs.getCurrentStmt() != null && curAbs.getCurrentStmt().containsInvokeExpr();
            boolean scanPreds = true;
            if (isMethodEnter && !newCallStack.isEmpty()) {
                Pair<Stmt, Set<Abstraction>> newStackTop = newCallStack.isEmpty() ? null : newCallStack.peek();
                if (newStackTop != null && newStackTop.getO1() != null) {
                    if (curAbs.getCurrentStmt() != newStackTop.getO1()) {
                        scanPreds = false;
                    }
                    newCallStack.pop();
                }
            }
            if (scanPreds) {
                for (SourceContextAndPath curScap : getPaths(taskId, curAbs.getPredecessor(), newCallStack)) {
                    SourceContextAndPath extendedPath = curScap.extendPath(curAbs, this.config);
                    if (extendedPath != null) {
                        cacheData.add(extendedPath);
                    }
                }
            }
        }
        if (curAbs.getNeighbors() != null) {
            for (Abstraction nb : curAbs.getNeighbors()) {
                for (SourceContextAndPath path : getPaths(taskId, nb, callStack)) {
                    cacheData.add(path);
                }
            }
        }
        return Collections.unmodifiableSet(cacheData);
    }

    private void computeTaintPathsInternal(Set<AbstractionAtSink> res) {
        this.logger.debug("Running path reconstruction");
        this.logger.info("Obtainted {} connections between sources and sinks", Integer.valueOf(res.size()));
        int curResIdx = 0;
        for (final AbstractionAtSink abs : res) {
            curResIdx++;
            this.logger.info(String.format("Building path %d...", Integer.valueOf(curResIdx)));
            this.executor.execute(new Runnable() { // from class: soot.jimple.infoflow.data.pathBuilders.RecursivePathBuilder.1
                @Override // java.lang.Runnable
                public void run() {
                    FastStack<Pair<Stmt, Set<Abstraction>>> initialStack = new FastStack<>();
                    initialStack.push(new Pair<>(null, Collections.newSetFromMap(new IdentityHashMap())));
                    RecursivePathBuilder recursivePathBuilder = RecursivePathBuilder.this;
                    int i = RecursivePathBuilder.lastTaskId;
                    RecursivePathBuilder.lastTaskId = i + 1;
                    for (SourceContextAndPath context : recursivePathBuilder.getPaths(i, abs.getAbstraction(), initialStack)) {
                        RecursivePathBuilder.this.results.addResult(abs.getSinkDefinition(), abs.getAbstraction().getAccessPath(), abs.getSinkStmt(), context.getDefinition(), context.getAccessPath(), context.getStmt(), context.getUserData(), context.getAbstractionPath(), RecursivePathBuilder.this.manager);
                    }
                }
            });
        }
        try {
            this.executor.awaitCompletion();
        } catch (InterruptedException ex) {
            this.logger.error("Could not wait for path executor completion: {0}", ex.getMessage());
            ex.printStackTrace();
        }
        this.executor.shutdown();
        this.logger.debug("Path reconstruction done.");
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
    public void computeTaintPaths(Set<AbstractionAtSink> res) {
        computeTaintPathsInternal(res);
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
    public InfoflowResults getResults() {
        return this.results;
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
    public void runIncrementalPathCompuation() {
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public void forceTerminate(ISolverTerminationReason reason) {
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public boolean isTerminated() {
        return false;
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public boolean isKilled() {
        return false;
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public void reset() {
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public void addStatusListener(IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification listener) {
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public ISolverTerminationReason getTerminationReason() {
        return null;
    }
}
