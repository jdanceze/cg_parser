package soot.jimple.infoflow;

import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.infoflow.AbstractInfoflow;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.aliasing.FlowSensitiveAliasStrategy;
import soot.jimple.infoflow.aliasing.IAliasingStrategy;
import soot.jimple.infoflow.aliasing.LazyAliasingStrategy;
import soot.jimple.infoflow.aliasing.NullAliasStrategy;
import soot.jimple.infoflow.aliasing.PtsBasedAliasStrategy;
import soot.jimple.infoflow.cfg.BiDirICFGFactory;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.globalTaints.GlobalTaintManager;
import soot.jimple.infoflow.memory.IMemoryBoundedSolver;
import soot.jimple.infoflow.nativeCallHandler.DefaultNativeCallHandler;
import soot.jimple.infoflow.problems.AliasProblem;
import soot.jimple.infoflow.problems.InfoflowProblem;
import soot.jimple.infoflow.problems.rules.DefaultPropagationRuleManagerFactory;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.solver.IInfoflowSolver;
import soot.jimple.infoflow.solver.cfg.BackwardsInfoflowCFG;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.infoflow.solver.executors.InterruptableExecutor;
import soot.jimple.infoflow.solver.memory.IMemoryManager;
import soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/Infoflow.class */
public class Infoflow extends AbstractInfoflow {
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

    public Infoflow() {
    }

    public Infoflow(String androidPath, boolean forceAndroidJar) {
        super(null, androidPath, forceAndroidJar);
    }

    public Infoflow(String androidPath, boolean forceAndroidJar, BiDirICFGFactory icfgFactory) {
        super(icfgFactory, androidPath, forceAndroidJar);
        setNativeCallHandler(new DefaultNativeCallHandler());
    }

    @Override // soot.jimple.infoflow.AbstractInfoflow
    protected InfoflowManager initializeInfoflowManager(ISourceSinkManager sourcesSinks, IInfoflowCFG iCfg, GlobalTaintManager globalTaintManager) {
        return new InfoflowManager(this.config, (IInfoflowSolver) null, iCfg, sourcesSinks, this.taintWrapper, this.hierarchy, globalTaintManager);
    }

    @Override // soot.jimple.infoflow.AbstractInfoflow
    protected IAliasingStrategy createAliasAnalysis(ISourceSinkManager sourcesSinks, IInfoflowCFG iCfg, InterruptableExecutor executor, IMemoryManager<Abstraction, Unit> memoryManager) {
        IAliasingStrategy aliasingStrategy;
        switch ($SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$AliasingAlgorithm()[getConfig().getAliasingAlgorithm().ordinal()]) {
            case 1:
                InfoflowManager aliasManager = new InfoflowManager(this.config, (IInfoflowSolver) null, new BackwardsInfoflowCFG(iCfg), sourcesSinks, this.taintWrapper, this.hierarchy, this.manager);
                AliasProblem backProblem = new AliasProblem(aliasManager);
                InfoflowConfiguration.SolverConfiguration solverConfig = this.config.getSolverConfiguration();
                IInfoflowSolver backSolver = createDataFlowSolver(executor, backProblem, solverConfig);
                backSolver.setMemoryManager(memoryManager);
                backSolver.setPredecessorShorteningMode(pathConfigToShorteningMode(this.manager.getConfig().getPathConfiguration()));
                backSolver.setMaxJoinPointAbstractions(solverConfig.getMaxJoinPointAbstractions());
                backSolver.setMaxCalleesPerCallSite(solverConfig.getMaxCalleesPerCallSite());
                backSolver.setMaxAbstractionPathLength(solverConfig.getMaxAbstractionPathLength());
                backSolver.setSolverId(false);
                backProblem.setTaintPropagationHandler(this.aliasPropagationHandler);
                backProblem.setTaintWrapper(this.taintWrapper);
                if (this.nativeCallHandler != null) {
                    backProblem.setNativeCallHandler(this.nativeCallHandler);
                }
                this.memoryWatcher.addSolver((IMemoryBoundedSolver) backSolver);
                aliasingStrategy = new FlowSensitiveAliasStrategy(this.manager, backSolver);
                break;
            case 2:
                aliasingStrategy = new PtsBasedAliasStrategy(this.manager);
                break;
            case 3:
                aliasingStrategy = new NullAliasStrategy();
                break;
            case 4:
                aliasingStrategy = new LazyAliasingStrategy(this.manager);
                break;
            default:
                throw new RuntimeException("Unsupported aliasing algorithm");
        }
        return aliasingStrategy;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.AbstractInfoflow
    public InfoflowProblem createInfoflowProblem(Abstraction zeroValue) {
        return new InfoflowProblem(this.manager, zeroValue, this.ruleManagerFactory);
    }

    @Override // soot.jimple.infoflow.AbstractInfoflow
    protected AbstractInfoflow.SourceSinkState scanStmtForSourcesSinks(ISourceSinkManager sourcesSinks, Stmt s) {
        if (sourcesSinks.getSourceInfo(s, this.manager) != null) {
            return AbstractInfoflow.SourceSinkState.SOURCE;
        }
        if (sourcesSinks.getSinkInfo(s, this.manager, null) != null) {
            return AbstractInfoflow.SourceSinkState.SINK;
        }
        return AbstractInfoflow.SourceSinkState.NEITHER;
    }

    @Override // soot.jimple.infoflow.AbstractInfoflow
    protected InfoflowResults createResultsObject() {
        return new InfoflowResults(this.config.getPathAgnosticResults());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.AbstractInfoflow
    public DefaultPropagationRuleManagerFactory initializeRuleManagerFactory() {
        return new DefaultPropagationRuleManagerFactory();
    }
}
