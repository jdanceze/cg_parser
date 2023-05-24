package soot.jimple.infoflow;

import java.util.Set;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.infoflow.AbstractInfoflow;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.aliasing.BackwardsFlowSensitiveAliasStrategy;
import soot.jimple.infoflow.aliasing.IAliasingStrategy;
import soot.jimple.infoflow.aliasing.NullAliasStrategy;
import soot.jimple.infoflow.cfg.BiDirICFGFactory;
import soot.jimple.infoflow.codeOptimization.AddNopStmt;
import soot.jimple.infoflow.codeOptimization.ICodeOptimizer;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.globalTaints.GlobalTaintManager;
import soot.jimple.infoflow.memory.IMemoryBoundedSolver;
import soot.jimple.infoflow.nativeCallHandler.BackwardNativeCallHandler;
import soot.jimple.infoflow.problems.BackwardsAliasProblem;
import soot.jimple.infoflow.problems.BackwardsInfoflowProblem;
import soot.jimple.infoflow.problems.rules.BackwardPropagationRuleManagerFactory;
import soot.jimple.infoflow.results.BackwardsInfoflowResults;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.solver.IInfoflowSolver;
import soot.jimple.infoflow.solver.cfg.BackwardsInfoflowCFG;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.infoflow.solver.executors.InterruptableExecutor;
import soot.jimple.infoflow.solver.memory.IMemoryManager;
import soot.jimple.infoflow.sourcesSinks.manager.IReversibleSourceSinkManager;
import soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/BackwardsInfoflow.class */
public class BackwardsInfoflow extends AbstractInfoflow {
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$AliasingAlgorithm;

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$AliasingAlgorithm() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$AliasingAlgorithm;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[InfoflowConfiguration.AliasingAlgorithm.valuesCustom().length];
        try {
            iArr2[InfoflowConfiguration.AliasingAlgorithm.FlowSensitive.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[InfoflowConfiguration.AliasingAlgorithm.Lazy.ordinal()] = 4;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[InfoflowConfiguration.AliasingAlgorithm.None.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[InfoflowConfiguration.AliasingAlgorithm.PtsBased.ordinal()] = 2;
        } catch (NoSuchFieldError unused4) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$AliasingAlgorithm = iArr2;
        return iArr2;
    }

    public BackwardsInfoflow() {
        this.config.setDataFlowDirection(InfoflowConfiguration.DataFlowDirection.Backwards);
        setNativeCallHandler(new BackwardNativeCallHandler());
    }

    public BackwardsInfoflow(String androidPath, boolean forceAndroidJar) {
        super(null, androidPath, forceAndroidJar);
        this.config.setDataFlowDirection(InfoflowConfiguration.DataFlowDirection.Backwards);
        setNativeCallHandler(new BackwardNativeCallHandler());
    }

    public BackwardsInfoflow(String androidPath, boolean forceAndroidJar, BiDirICFGFactory icfgFactory) {
        super(icfgFactory, androidPath, forceAndroidJar);
        this.config.setDataFlowDirection(InfoflowConfiguration.DataFlowDirection.Backwards);
        setNativeCallHandler(new BackwardNativeCallHandler());
    }

    @Override // soot.jimple.infoflow.AbstractInfoflow
    protected IAliasingStrategy createAliasAnalysis(ISourceSinkManager sourcesSinks, IInfoflowCFG iCfg, InterruptableExecutor executor, IMemoryManager<Abstraction, Unit> memoryManager) {
        IAliasingStrategy aliasingStrategy;
        switch ($SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$AliasingAlgorithm()[getConfig().getAliasingAlgorithm().ordinal()]) {
            case 1:
                InfoflowManager aliasManager = new InfoflowManager(this.config, (IInfoflowSolver) null, iCfg, sourcesSinks, this.taintWrapper, this.hierarchy, this.manager);
                BackwardsAliasProblem aliasProblem = new BackwardsAliasProblem(aliasManager);
                InfoflowConfiguration.SolverConfiguration solverConfig = this.config.getSolverConfiguration();
                IInfoflowSolver aliasSolver = createDataFlowSolver(executor, aliasProblem, solverConfig);
                aliasSolver.setMemoryManager(memoryManager);
                aliasSolver.setPredecessorShorteningMode(pathConfigToShorteningMode(this.manager.getConfig().getPathConfiguration()));
                aliasSolver.setMaxJoinPointAbstractions(solverConfig.getMaxJoinPointAbstractions());
                aliasSolver.setMaxCalleesPerCallSite(solverConfig.getMaxCalleesPerCallSite());
                aliasSolver.setMaxAbstractionPathLength(solverConfig.getMaxAbstractionPathLength());
                aliasSolver.setSolverId(false);
                aliasProblem.setTaintPropagationHandler(this.aliasPropagationHandler);
                aliasProblem.setTaintWrapper(this.taintWrapper);
                if (this.nativeCallHandler != null) {
                    aliasProblem.setNativeCallHandler(this.nativeCallHandler);
                }
                this.memoryWatcher.addSolver((IMemoryBoundedSolver) aliasSolver);
                aliasingStrategy = new BackwardsFlowSensitiveAliasStrategy(this.manager, aliasSolver);
                break;
            case 2:
            default:
                throw new RuntimeException("Unsupported aliasing algorithm: " + getConfig().getAliasingAlgorithm());
            case 3:
                aliasingStrategy = new NullAliasStrategy();
                break;
        }
        return aliasingStrategy;
    }

    @Override // soot.jimple.infoflow.AbstractInfoflow
    protected InfoflowManager initializeInfoflowManager(ISourceSinkManager sourcesSinks, IInfoflowCFG iCfg, GlobalTaintManager globalTaintManager) {
        return new InfoflowManager(this.config, (IInfoflowSolver) null, new BackwardsInfoflowCFG(iCfg), sourcesSinks, this.taintWrapper, this.hierarchy, globalTaintManager);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.AbstractInfoflow
    public BackwardsInfoflowProblem createInfoflowProblem(Abstraction zeroValue) {
        return new BackwardsInfoflowProblem(this.manager, zeroValue, this.ruleManagerFactory);
    }

    @Override // soot.jimple.infoflow.AbstractInfoflow
    protected AbstractInfoflow.SourceSinkState scanStmtForSourcesSinks(ISourceSinkManager sourcesSinks, Stmt s) {
        IReversibleSourceSinkManager ssm = (IReversibleSourceSinkManager) sourcesSinks;
        if (ssm.getInverseSinkInfo(s, this.manager) != null) {
            return AbstractInfoflow.SourceSinkState.SOURCE;
        }
        if (ssm.getInverseSourceInfo(s, this.manager, null) != null) {
            return AbstractInfoflow.SourceSinkState.SINK;
        }
        return AbstractInfoflow.SourceSinkState.NEITHER;
    }

    @Override // soot.jimple.infoflow.AbstractInfoflow
    protected InfoflowResults createResultsObject() {
        return new BackwardsInfoflowResults();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.AbstractInfoflow
    public BackwardPropagationRuleManagerFactory initializeRuleManagerFactory() {
        return new BackwardPropagationRuleManagerFactory();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.AbstractInfoflow
    public void performCodeInstrumentation(InfoflowManager dceManager, Set<SootMethod> excludedMethods) {
        super.performCodeInstrumentation(dceManager, excludedMethods);
        ICodeOptimizer nopStmt = new AddNopStmt();
        nopStmt.initialize(this.config);
        nopStmt.run(dceManager, excludedMethods, null, null);
    }
}
