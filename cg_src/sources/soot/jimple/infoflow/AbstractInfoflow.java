package soot.jimple.infoflow;

import android.hardware.Camera;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.FastHierarchy;
import soot.G;
import soot.MethodOrMethodContext;
import soot.PackManager;
import soot.PatchingChain;
import soot.PointsToAnalysis;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.aliasing.Aliasing;
import soot.jimple.infoflow.aliasing.IAliasingStrategy;
import soot.jimple.infoflow.cfg.BiDirICFGFactory;
import soot.jimple.infoflow.cfg.DefaultBiDiICFGFactory;
import soot.jimple.infoflow.cfg.LibraryClassPatcher;
import soot.jimple.infoflow.codeOptimization.DeadCodeEliminator;
import soot.jimple.infoflow.codeOptimization.ICodeOptimizer;
import soot.jimple.infoflow.config.IInfoflowConfig;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AbstractionAtSink;
import soot.jimple.infoflow.data.FlowDroidMemoryManager;
import soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder;
import soot.jimple.infoflow.data.pathBuilders.IPathBuilderFactory;
import soot.jimple.infoflow.entryPointCreators.DefaultEntryPointCreator;
import soot.jimple.infoflow.entryPointCreators.IEntryPointCreator;
import soot.jimple.infoflow.globalTaints.GlobalTaintManager;
import soot.jimple.infoflow.handlers.PostAnalysisHandler;
import soot.jimple.infoflow.handlers.PreAnalysisHandler;
import soot.jimple.infoflow.handlers.ResultsAvailableHandler;
import soot.jimple.infoflow.handlers.ResultsAvailableHandler2;
import soot.jimple.infoflow.handlers.TaintPropagationHandler;
import soot.jimple.infoflow.ipc.DefaultIPCManager;
import soot.jimple.infoflow.ipc.IIPCManager;
import soot.jimple.infoflow.memory.FlowDroidMemoryWatcher;
import soot.jimple.infoflow.memory.IMemoryBoundedSolver;
import soot.jimple.infoflow.memory.ISolverTerminationReason;
import soot.jimple.infoflow.memory.reasons.AbortRequestedReason;
import soot.jimple.infoflow.nativeCallHandler.INativeCallHandler;
import soot.jimple.infoflow.problems.AbstractInfoflowProblem;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.IPropagationRuleManagerFactory;
import soot.jimple.infoflow.results.InfoflowPerformanceData;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.results.ResultSinkInfo;
import soot.jimple.infoflow.results.ResultSourceInfo;
import soot.jimple.infoflow.solver.IInfoflowSolver;
import soot.jimple.infoflow.solver.PredecessorShorteningMode;
import soot.jimple.infoflow.solver.SolverPeerGroup;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.infoflow.solver.executors.InterruptableExecutor;
import soot.jimple.infoflow.solver.fastSolver.InfoflowSolver;
import soot.jimple.infoflow.solver.memory.DefaultMemoryManagerFactory;
import soot.jimple.infoflow.solver.memory.IMemoryManager;
import soot.jimple.infoflow.solver.memory.IMemoryManagerFactory;
import soot.jimple.infoflow.sourcesSinks.manager.DefaultSourceSinkManager;
import soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager;
import soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper;
import soot.jimple.infoflow.threading.DefaultExecutorFactory;
import soot.jimple.infoflow.threading.IExecutorFactory;
import soot.jimple.infoflow.util.SootMethodRepresentationParser;
import soot.jimple.infoflow.util.SystemClassHandler;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.jimple.toolkits.pointer.DumbPointerAnalysis;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/AbstractInfoflow.class */
public abstract class AbstractInfoflow implements IInfoflow {
    protected final Logger logger;
    protected InfoflowResults results;
    protected InfoflowManager manager;
    protected SolverPeerGroup solverPeerGroup;
    protected IPathBuilderFactory pathBuilderFactory;
    protected InfoflowConfiguration config;
    protected ITaintPropagationWrapper taintWrapper;
    protected INativeCallHandler nativeCallHandler;
    protected IIPCManager ipcManager;
    protected final BiDirICFGFactory icfgFactory;
    protected Collection<? extends PreAnalysisHandler> preProcessors;
    protected Collection<? extends PostAnalysisHandler> postProcessors;
    protected final String androidPath;
    protected final boolean forceAndroidJar;
    protected IInfoflowConfig sootConfig;
    protected FastHierarchy hierarchy;
    protected IMemoryManagerFactory memoryManagerFactory;
    protected IExecutorFactory executorFactory;
    protected IPropagationRuleManagerFactory ruleManagerFactory;
    protected Set<Stmt> collectedSources;
    protected Set<Stmt> collectedSinks;
    protected SootMethod dummyMainMethod;
    protected Collection<SootMethod> additionalEntryPointMethods;
    protected boolean throwExceptions;
    protected Set<ResultsAvailableHandler> onResultsAvailable;
    protected TaintPropagationHandler taintPropagationHandler;
    protected TaintPropagationHandler aliasPropagationHandler;
    protected FlowDroidMemoryWatcher memoryWatcher;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$CallgraphAlgorithm;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$DataFlowSolver;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$AbstractInfoflow$SourceSinkState;
    static final /* synthetic */ boolean $assertionsDisabled;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$PathReconstructionMode;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/AbstractInfoflow$SourceSinkState.class */
    public enum SourceSinkState {
        SOURCE,
        SINK,
        NEITHER;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static SourceSinkState[] valuesCustom() {
            SourceSinkState[] valuesCustom = values();
            int length = valuesCustom.length;
            SourceSinkState[] sourceSinkStateArr = new SourceSinkState[length];
            System.arraycopy(valuesCustom, 0, sourceSinkStateArr, 0, length);
            return sourceSinkStateArr;
        }
    }

    protected abstract AbstractInfoflowProblem createInfoflowProblem(Abstraction abstraction);

    protected abstract SourceSinkState scanStmtForSourcesSinks(ISourceSinkManager iSourceSinkManager, Stmt stmt);

    protected abstract InfoflowResults createResultsObject();

    protected abstract IAliasingStrategy createAliasAnalysis(ISourceSinkManager iSourceSinkManager, IInfoflowCFG iInfoflowCFG, InterruptableExecutor interruptableExecutor, IMemoryManager<Abstraction, Unit> iMemoryManager);

    protected abstract InfoflowManager initializeInfoflowManager(ISourceSinkManager iSourceSinkManager, IInfoflowCFG iInfoflowCFG, GlobalTaintManager globalTaintManager);

    protected abstract IPropagationRuleManagerFactory initializeRuleManagerFactory();

    static {
        $assertionsDisabled = !AbstractInfoflow.class.desiredAssertionStatus();
    }

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$CallgraphAlgorithm() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$CallgraphAlgorithm;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[InfoflowConfiguration.CallgraphAlgorithm.valuesCustom().length];
        try {
            iArr2[InfoflowConfiguration.CallgraphAlgorithm.AutomaticSelection.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[InfoflowConfiguration.CallgraphAlgorithm.CHA.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[InfoflowConfiguration.CallgraphAlgorithm.GEOM.ordinal()] = 6;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[InfoflowConfiguration.CallgraphAlgorithm.OnDemand.ordinal()] = 7;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[InfoflowConfiguration.CallgraphAlgorithm.RTA.ordinal()] = 4;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[InfoflowConfiguration.CallgraphAlgorithm.SPARK.ordinal()] = 5;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            iArr2[InfoflowConfiguration.CallgraphAlgorithm.VTA.ordinal()] = 3;
        } catch (NoSuchFieldError unused7) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$CallgraphAlgorithm = iArr2;
        return iArr2;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$DataFlowSolver() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$DataFlowSolver;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[InfoflowConfiguration.DataFlowSolver.valuesCustom().length];
        try {
            iArr2[InfoflowConfiguration.DataFlowSolver.ContextFlowSensitive.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[InfoflowConfiguration.DataFlowSolver.FlowInsensitive.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[InfoflowConfiguration.DataFlowSolver.GarbageCollecting.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$DataFlowSolver = iArr2;
        return iArr2;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$AbstractInfoflow$SourceSinkState() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$AbstractInfoflow$SourceSinkState;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[SourceSinkState.valuesCustom().length];
        try {
            iArr2[SourceSinkState.NEITHER.ordinal()] = 3;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[SourceSinkState.SINK.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[SourceSinkState.SOURCE.ordinal()] = 1;
        } catch (NoSuchFieldError unused3) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$AbstractInfoflow$SourceSinkState = iArr2;
        return iArr2;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$PathReconstructionMode() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$PathReconstructionMode;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[InfoflowConfiguration.PathReconstructionMode.valuesCustom().length];
        try {
            iArr2[InfoflowConfiguration.PathReconstructionMode.Fast.ordinal()] = 2;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[InfoflowConfiguration.PathReconstructionMode.NoPaths.ordinal()] = 1;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[InfoflowConfiguration.PathReconstructionMode.Precise.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$PathReconstructionMode = iArr2;
        return iArr2;
    }

    public AbstractInfoflow() {
        this(null, "", false);
    }

    public AbstractInfoflow(BiDirICFGFactory icfgFactory, String androidPath, boolean forceAndroidJar) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.results = null;
        this.config = new InfoflowConfiguration();
        this.ipcManager = new DefaultIPCManager(new ArrayList());
        this.preProcessors = Collections.emptyList();
        this.postProcessors = Collections.emptyList();
        this.memoryManagerFactory = new DefaultMemoryManagerFactory();
        this.executorFactory = new DefaultExecutorFactory();
        this.ruleManagerFactory = initializeRuleManagerFactory();
        this.onResultsAvailable = new HashSet();
        this.taintPropagationHandler = null;
        this.aliasPropagationHandler = null;
        this.memoryWatcher = null;
        if (icfgFactory == null) {
            DefaultBiDiICFGFactory factory = new DefaultBiDiICFGFactory();
            factory.setIsAndroid((androidPath == null || androidPath.isEmpty()) ? false : true);
            this.icfgFactory = factory;
        } else {
            this.icfgFactory = icfgFactory;
        }
        this.androidPath = androidPath;
        this.forceAndroidJar = forceAndroidJar;
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public InfoflowConfiguration getConfig() {
        return this.config;
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void setConfig(InfoflowConfiguration config) {
        this.config = config;
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintWrapperDataFlowAnalysis
    public void setTaintWrapper(ITaintPropagationWrapper wrapper) {
        this.taintWrapper = wrapper;
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void setNativeCallHandler(INativeCallHandler handler) {
        this.nativeCallHandler = handler;
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintWrapperDataFlowAnalysis
    public ITaintPropagationWrapper getTaintWrapper() {
        return this.taintWrapper;
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void setPreProcessors(Collection<? extends PreAnalysisHandler> preprocessors) {
        this.preProcessors = preprocessors;
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void setPostProcessors(Collection<? extends PostAnalysisHandler> postprocessors) {
        this.postProcessors = postprocessors;
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void computeInfoflow(String appPath, String libPath, IEntryPointCreator entryPointCreator, List<String> sources, List<String> sinks) {
        computeInfoflow(appPath, libPath, entryPointCreator, new DefaultSourceSinkManager(sources, sinks));
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void computeInfoflow(String appPath, String libPath, Collection<String> entryPoints, Collection<String> sources, Collection<String> sinks) {
        computeInfoflow(appPath, libPath, new DefaultEntryPointCreator(entryPoints), new DefaultSourceSinkManager(sources, sinks));
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void computeInfoflow(String libPath, String appPath, String entryPoint, Collection<String> sources, Collection<String> sinks) {
        computeInfoflow(appPath, libPath, entryPoint, new DefaultSourceSinkManager(sources, sinks));
    }

    private String appendClasspath(String appPath, String libPath) {
        String s = (appPath == null || appPath.isEmpty()) ? "" : appPath;
        if (libPath != null && !libPath.isEmpty()) {
            if (!s.isEmpty()) {
                s = String.valueOf(s) + File.pathSeparator;
            }
            s = String.valueOf(s) + libPath;
        }
        return s;
    }

    protected void initializeSoot(String appPath, String libPath, Collection<String> classes) {
        initializeSoot(appPath, libPath, classes, "");
    }

    protected void initializeSoot(String appPath, String libPath, Collection<String> classes, String extraSeed) {
        String[] split;
        if (this.config.getSootIntegrationMode().needsToInitializeSoot()) {
            this.logger.info("Resetting Soot...");
            G.reset();
            Options.v().set_no_bodies_for_excluded(true);
            Options.v().set_allow_phantom_refs(true);
            if (this.config.getWriteOutputFiles()) {
                Options.v().set_output_format(1);
            } else {
                Options.v().set_output_format(12);
            }
            if (this.config.getCallgraphAlgorithm() == InfoflowConfiguration.CallgraphAlgorithm.OnDemand) {
                Options.v().set_soot_classpath(libPath);
                if (appPath != null) {
                    List<String> processDirs = new LinkedList<>();
                    for (String ap : appPath.split(File.pathSeparator)) {
                        processDirs.add(ap);
                    }
                    Options.v().set_process_dir(processDirs);
                }
            } else {
                Options.v().set_soot_classpath(appendClasspath(appPath, libPath));
            }
            Options.v().setPhaseOption("jb.ulp", "off");
            setSourcePrec();
        }
        if (this.config.getSootIntegrationMode().needsToBuildCallgraph()) {
            switch ($SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$CallgraphAlgorithm()[this.config.getCallgraphAlgorithm().ordinal()]) {
                case 1:
                    if (extraSeed == null || extraSeed.isEmpty()) {
                        setSparkOptions();
                        break;
                    } else {
                        setChaOptions();
                        break;
                    }
                    break;
                case 2:
                    setChaOptions();
                    break;
                case 3:
                    Options.v().setPhaseOption("cg.spark", Camera.Parameters.FLASH_MODE_ON);
                    Options.v().setPhaseOption("cg.spark", "vta:true");
                    Options.v().setPhaseOption("cg.spark", "string-constants:true");
                    break;
                case 4:
                    Options.v().setPhaseOption("cg.spark", Camera.Parameters.FLASH_MODE_ON);
                    Options.v().setPhaseOption("cg.spark", "rta:true");
                    Options.v().setPhaseOption("cg.spark", "on-fly-cg:false");
                    Options.v().setPhaseOption("cg.spark", "string-constants:true");
                    break;
                case 5:
                    setSparkOptions();
                    break;
                case 6:
                    setSparkOptions();
                    setGeomPtaSpecificOptions();
                    break;
                case 7:
                    break;
                default:
                    throw new RuntimeException("Invalid callgraph algorithm");
            }
            if (this.config.getCallgraphAlgorithm() != InfoflowConfiguration.CallgraphAlgorithm.OnDemand) {
                Options.v().set_whole_program(true);
                Options.v().setPhaseOption("cg", "trim-clinit:false");
                if (this.config.getEnableReflection()) {
                    Options.v().setPhaseOption("cg", "types-for-invoke:true");
                }
            }
        }
        if (this.config.getSootIntegrationMode().needsToInitializeSoot()) {
            if (this.sootConfig != null) {
                this.sootConfig.setSootOptions(Options.v(), this.config);
            }
            for (String className : classes) {
                Scene.v().addBasicClass(className, 3);
            }
            Scene.v().loadNecessaryClasses();
            this.logger.info("Basic class loading done.");
            boolean hasClasses = false;
            for (String className2 : classes) {
                SootClass c = Scene.v().forceResolve(className2, 3);
                if (c != null) {
                    c.setApplicationClass();
                    if (!c.isPhantomClass() && !c.isPhantom()) {
                        hasClasses = true;
                    }
                }
            }
            if (!hasClasses) {
                this.logger.error("Only phantom classes loaded, skipping analysis...");
            }
        }
    }

    protected void setSourcePrec() {
        if (!this.androidPath.isEmpty()) {
            Options.v().set_src_prec(6);
            if (this.forceAndroidJar) {
                Options.v().set_force_android_jar(this.androidPath);
                return;
            } else {
                Options.v().set_android_jars(this.androidPath);
                return;
            }
        }
        Options.v().set_src_prec(4);
    }

    private void setChaOptions() {
        Options.v().setPhaseOption("cg.cha", Camera.Parameters.FLASH_MODE_ON);
    }

    private void setSparkOptions() {
        Options.v().setPhaseOption("cg.spark", Camera.Parameters.FLASH_MODE_ON);
        Options.v().setPhaseOption("cg.spark", "string-constants:true");
    }

    public static void setGeomPtaSpecificOptions() {
        Options.v().setPhaseOption("cg.spark", "geom-pta:true");
        Options.v().setPhaseOption("cg.spark", "geom-encoding:Geom");
        Options.v().setPhaseOption("cg.spark", "geom-worklist:PQ");
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void setSootConfig(IInfoflowConfig config) {
        this.sootConfig = config;
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void setIPCManager(IIPCManager ipcManager) {
        this.ipcManager = ipcManager;
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void setPathBuilderFactory(IPathBuilderFactory factory) {
        this.pathBuilderFactory = factory;
    }

    protected void constructCallgraph() {
        if (this.config.getSootIntegrationMode().needsToBuildCallgraph()) {
            if (this.ipcManager != null) {
                this.ipcManager.updateJimpleForICC();
            }
            for (PreAnalysisHandler tr : this.preProcessors) {
                tr.onBeforeCallgraphConstruction();
            }
            LibraryClassPatcher patcher = getLibraryClassPatcher();
            patcher.patchLibraries();
            for (SootClass sc : Scene.v().getClasses()) {
                if (sc.resolvingLevel() == 0) {
                    sc.setResolvingLevel(3);
                    sc.setPhantomClass();
                }
            }
            if (this.config.getCallgraphAlgorithm() != InfoflowConfiguration.CallgraphAlgorithm.OnDemand && !Scene.v().hasCallGraph()) {
                PackManager.v().getPack("wjpp").apply();
                PackManager.v().getPack("cg").apply();
            }
        }
        this.hierarchy = Scene.v().getOrMakeFastHierarchy();
        if (this.config.getSootIntegrationMode().needsToBuildCallgraph()) {
            for (PreAnalysisHandler tr2 : this.preProcessors) {
                tr2.onAfterCallgraphConstruction();
            }
        }
    }

    protected LibraryClassPatcher getLibraryClassPatcher() {
        return new LibraryClassPatcher();
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void setMemoryManagerFactory(IMemoryManagerFactory factory) {
        this.memoryManagerFactory = factory;
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void computeInfoflow(String appPath, String libPath, IEntryPointCreator entryPointCreator, ISourceSinkManager sourcesSinks) {
        if (sourcesSinks == null) {
            this.logger.error("Sources are empty!");
            return;
        }
        if (this.config.getSootIntegrationMode() != InfoflowConfiguration.SootIntegrationMode.UseExistingInstance) {
            initializeSoot(appPath, libPath, entryPointCreator.getRequiredClasses());
        }
        this.dummyMainMethod = entryPointCreator.createDummyMain();
        this.additionalEntryPointMethods = entryPointCreator.getAdditionalMethods();
        Scene.v().setEntryPoints(Collections.singletonList(this.dummyMainMethod));
        runAnalysis(sourcesSinks, null);
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void computeInfoflow(String appPath, String libPath, String entryPoint, ISourceSinkManager sourcesSinks) {
        if (sourcesSinks == null) {
            this.logger.error("No source/sink manager specified");
            return;
        }
        initializeSoot(appPath, libPath, SootMethodRepresentationParser.v().parseClassNames(Collections.singletonList(entryPoint), false).keySet(), entryPoint);
        if (!Scene.v().containsMethod(entryPoint)) {
            this.logger.error("Entry point not found: " + entryPoint);
            return;
        }
        SootMethod ep = Scene.v().getMethod(entryPoint);
        if (ep.isConcrete()) {
            ep.retrieveActiveBody();
            this.dummyMainMethod = null;
            Scene.v().setEntryPoints(Collections.singletonList(ep));
            Options.v().set_main_class(ep.getDeclaringClass().getName());
            Set<String> seeds = Collections.emptySet();
            if (entryPoint != null && !entryPoint.isEmpty()) {
                seeds = Collections.singleton(entryPoint);
            }
            this.ipcManager.updateJimpleForICC();
            runAnalysis(sourcesSinks, seeds);
            return;
        }
        this.logger.debug("Skipping non-concrete method " + ep);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void runAnalysis(ISourceSinkManager sourcesSinks) {
        runAnalysis(sourcesSinks, null);
    }

    protected void runAnalysis(ISourceSinkManager sourcesSinks, Set<String> additionalSeeds) {
        InfoflowPerformanceData performanceData = createPerformanceDataClass();
        try {
            this.results = createResultsObject();
            this.results.setPerformanceData(performanceData);
            checkAndFixConfiguration();
            this.config.printSummary();
            if (this.memoryWatcher != null) {
                this.memoryWatcher.clearSolvers();
                this.memoryWatcher = null;
            }
            this.memoryWatcher = new FlowDroidMemoryWatcher(this.results, this.config.getMemoryThreshold());
            Abstraction.initialize(this.config);
            long beforeCallgraph = System.nanoTime();
            constructCallgraph();
            performanceData.setCallgraphConstructionSeconds((int) Math.round((System.nanoTime() - beforeCallgraph) / 1.0E9d));
            this.logger.info(String.format(Locale.getDefault(), "Callgraph construction took %d seconds", Integer.valueOf(performanceData.getCallgraphConstructionSeconds())));
            if (sourcesSinks != null) {
                sourcesSinks.initialize();
            }
            if (this.config.getCodeEliminationMode() != InfoflowConfiguration.CodeEliminationMode.NoCodeElimination) {
                long currentMillis = System.nanoTime();
                eliminateDeadCode(sourcesSinks);
                this.logger.info("Dead code elimination took " + ((System.nanoTime() - currentMillis) / 1.0E9d) + " seconds");
            }
            if (this.config.getEnableReflection()) {
                releaseCallgraph();
                constructCallgraph();
            }
            if (this.config.getCallgraphAlgorithm() != InfoflowConfiguration.CallgraphAlgorithm.OnDemand) {
                this.logger.info("Callgraph has {} edges", Integer.valueOf(Scene.v().getCallGraph().size()));
            }
            IInfoflowCFG iCfg = this.icfgFactory.buildBiDirICFG(this.config.getCallgraphAlgorithm(), this.config.getEnableExceptionTracking());
            if (this.config.isTaintAnalysisEnabled()) {
                runTaintAnalysis(sourcesSinks, additionalSeeds, iCfg, performanceData);
            }
            performanceData.setTotalRuntimeSeconds((int) Math.round((System.nanoTime() - beforeCallgraph) / 1.0E9d));
            performanceData.updateMaxMemoryConsumption(getUsedMemory());
            this.logger.info(String.format("Data flow solver took %d seconds. Maximum memory consumption: %d MB", Integer.valueOf(performanceData.getTotalRuntimeSeconds()), Integer.valueOf(performanceData.getMaxMemoryConsumption())));
            for (ResultsAvailableHandler handler : this.onResultsAvailable) {
                handler.onResultsAvailable(iCfg, this.results);
            }
            if (this.config.getWriteOutputFiles()) {
                PackManager.v().writeOutput();
            }
        } catch (Exception ex) {
            StringWriter stacktrace = new StringWriter();
            PrintWriter pw = new PrintWriter(stacktrace);
            ex.printStackTrace(pw);
            if (this.results != null) {
                this.results.addException(String.valueOf(ex.getClass().getName()) + ": " + ex.getMessage() + "\n" + stacktrace.toString());
            }
            this.logger.error("Exception during data flow analysis", (Throwable) ex);
            if (this.throwExceptions) {
                throw ex;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:219:0x0869 A[Catch: all -> 0x0878, TryCatch #2 {all -> 0x0878, blocks: (B:48:0x0216, B:50:0x0220, B:52:0x022a, B:53:0x0236, B:55:0x0240, B:57:0x024d, B:58:0x0259, B:60:0x0291, B:59:0x0277, B:64:0x029f, B:69:0x02f4, B:65:0x02aa, B:67:0x02c8, B:68:0x02d9, B:71:0x02fe, B:73:0x0306, B:99:0x0388, B:123:0x0405, B:125:0x0440, B:126:0x044d, B:128:0x0454, B:129:0x0461, B:131:0x0499, B:132:0x04a1, B:134:0x04a9, B:135:0x04af, B:136:0x04c9, B:138:0x04d1, B:140:0x04d9, B:141:0x04dc, B:143:0x04e7, B:146:0x04fc, B:148:0x0504, B:151:0x0518, B:153:0x0520, B:154:0x052f, B:155:0x0530, B:157:0x0562, B:158:0x056e, B:160:0x0575, B:161:0x05b9, B:163:0x05d8, B:164:0x05e1, B:168:0x0615, B:172:0x0633, B:174:0x064d, B:176:0x0655, B:177:0x0668, B:179:0x0670, B:180:0x0680, B:182:0x06ab, B:183:0x06b0, B:185:0x06dc, B:186:0x06fa, B:189:0x0711, B:191:0x071e, B:192:0x0725, B:194:0x0765, B:195:0x0788, B:197:0x0797, B:198:0x079e, B:217:0x085a, B:219:0x0869, B:200:0x07a8, B:201:0x07b9, B:202:0x07ce, B:204:0x07e1, B:208:0x080a, B:210:0x0818, B:212:0x0820, B:213:0x0834, B:215:0x083c, B:216:0x084c, B:205:0x07f2, B:207:0x07fc, B:171:0x062c, B:167:0x0609, B:150:0x050c), top: B:303:0x0216, inners: #0, #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:223:0x087f  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x0889  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x0893  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x08dc  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x08f0  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x08fa  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x0904  */
    /* JADX WARN: Removed duplicated region for block: B:268:0x094d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void runTaintAnalysis(soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager r9, java.util.Set<java.lang.String> r10, soot.jimple.infoflow.solver.cfg.IInfoflowCFG r11, soot.jimple.infoflow.results.InfoflowPerformanceData r12) {
        /*
            Method dump skipped, instructions count: 2860
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.infoflow.AbstractInfoflow.runTaintAnalysis(soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager, java.util.Set, soot.jimple.infoflow.solver.cfg.IInfoflowCFG, soot.jimple.infoflow.results.InfoflowPerformanceData):void");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IInfoflowSolver createDataFlowSolver(InterruptableExecutor executor, AbstractInfoflowProblem problem, InfoflowConfiguration.SolverConfiguration solverConfig) {
        switch ($SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$DataFlowSolver()[solverConfig.getDataFlowSolver().ordinal()]) {
            case 1:
                this.logger.info("Using context- and flow-sensitive solver");
                return new InfoflowSolver(problem, executor);
            case 2:
                this.logger.info("Using context-sensitive, but flow-insensitive solver");
                return new soot.jimple.infoflow.solver.fastSolver.flowInsensitive.InfoflowSolver(problem, executor);
            case 3:
                this.logger.info("Using garbage-collecting solver");
                IInfoflowSolver solver = new soot.jimple.infoflow.solver.gcSolver.InfoflowSolver(problem, executor);
                this.solverPeerGroup.addSolver(solver);
                solver.setPeerGroup(this.solverPeerGroup);
                return solver;
            default:
                throw new RuntimeException("Unsupported data flow solver");
        }
    }

    private int scanMethodForSourcesSinks(ISourceSinkManager sourcesSinks, AbstractInfoflowProblem forwardProblem, SootMethod m) {
        if (getConfig().getLogSourcesAndSinks() && this.collectedSources == null) {
            this.collectedSources = new HashSet();
            this.collectedSinks = new HashSet();
        }
        int sinkCount = 0;
        if (m.hasActiveBody()) {
            if (!isValidSeedMethod(m)) {
                return 0;
            }
            PatchingChain<Unit> units = m.getActiveBody().getUnits();
            Iterator<Unit> it = units.iterator();
            while (it.hasNext()) {
                Unit u = it.next();
                Stmt s = (Stmt) u;
                switch ($SWITCH_TABLE$soot$jimple$infoflow$AbstractInfoflow$SourceSinkState()[scanStmtForSourcesSinks(sourcesSinks, s).ordinal()]) {
                    case 1:
                        forwardProblem.addInitialSeeds(s, Collections.singleton(forwardProblem.zeroValue()));
                        if (!getConfig().getLogSourcesAndSinks()) {
                            break;
                        } else {
                            this.collectedSources.add(s);
                            break;
                        }
                    case 2:
                        if (getConfig().getLogSourcesAndSinks()) {
                            this.collectedSinks.add(s);
                        }
                        sinkCount++;
                        break;
                }
            }
        }
        return sinkCount;
    }

    protected Collection<SootMethod> getMethodsForSeeds(IInfoflowCFG icfg) {
        List<SootMethod> seeds = new LinkedList<>();
        if (Scene.v().hasCallGraph()) {
            ReachableMethods reachableMethods = Scene.v().getReachableMethods();
            reachableMethods.update();
            Iterator<MethodOrMethodContext> iter = reachableMethods.listener();
            while (iter.hasNext()) {
                SootMethod sm = iter.next().method();
                if (isValidSeedMethod(sm)) {
                    seeds.add(sm);
                }
            }
        } else {
            long beforeSeedMethods = System.nanoTime();
            Set<SootMethod> doneSet = new HashSet<>();
            for (SootMethod sm2 : Scene.v().getEntryPoints()) {
                getMethodsForSeedsIncremental(sm2, doneSet, seeds, icfg);
            }
            this.logger.info("Collecting seed methods took {} seconds", Double.valueOf((System.nanoTime() - beforeSeedMethods) / 1.0E9d));
        }
        return seeds;
    }

    private void getMethodsForSeedsIncremental(SootMethod sm, Set<SootMethod> doneSet, List<SootMethod> seeds, IInfoflowCFG icfg) {
        if (!$assertionsDisabled && !Scene.v().hasFastHierarchy()) {
            throw new AssertionError();
        }
        if (!sm.isConcrete() || !sm.getDeclaringClass().isApplicationClass() || !doneSet.add(sm)) {
            return;
        }
        seeds.add(sm);
        Iterator<Unit> it = sm.retrieveActiveBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt stmt = (Stmt) u;
            if (stmt.containsInvokeExpr()) {
                for (SootMethod callee : icfg.getCalleesOfCallAt(stmt)) {
                    if (isValidSeedMethod(callee)) {
                        getMethodsForSeedsIncremental(callee, doneSet, seeds, icfg);
                    }
                }
            }
        }
    }

    protected boolean isValidSeedMethod(SootMethod sm) {
        if (sm == this.dummyMainMethod) {
            return false;
        }
        if (this.dummyMainMethod != null && sm.getDeclaringClass() == this.dummyMainMethod.getDeclaringClass()) {
            return false;
        }
        String className = sm.getDeclaringClass().getName();
        if (this.config.getIgnoreFlowsInSystemPackages() && SystemClassHandler.v().isClassInSystemPackage(className) && !isUserCodeClass(className)) {
            return false;
        }
        if (this.config.getExcludeSootLibraryClasses() && sm.getDeclaringClass().isLibraryClass()) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isUserCodeClass(String className) {
        return false;
    }

    protected void eliminateDeadCode(ISourceSinkManager sourcesSinks) {
        PointsToAnalysis newPta;
        InfoflowManager dceManager = new InfoflowManager(this.config, null, this.icfgFactory.buildBiDirICFG(this.config.getCallgraphAlgorithm(), this.config.getEnableExceptionTracking()));
        Scene scene = Scene.v();
        PointsToAnalysis pta = scene.getPointsToAnalysis();
        Set<SootMethod> excludedMethods = new HashSet<>();
        if (this.additionalEntryPointMethods != null) {
            excludedMethods.addAll(this.additionalEntryPointMethods);
        }
        excludedMethods.addAll(Scene.v().getEntryPoints());
        ICodeOptimizer dce = new DeadCodeEliminator();
        dce.initialize(this.config);
        dce.run(dceManager, excludedMethods, sourcesSinks, this.taintWrapper);
        if (pta != null && !(pta instanceof DumbPointerAnalysis) && ((newPta = scene.getPointsToAnalysis()) == null || (newPta instanceof DumbPointerAnalysis))) {
            scene.setPointsToAnalysis(pta);
        }
        performCodeInstrumentation(dceManager, excludedMethods);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void performCodeInstrumentation(InfoflowManager dceManager, Set<SootMethod> excludedMethods) {
    }

    protected IInfoflowSolver createDataFlowSolver(InterruptableExecutor executor, AbstractInfoflowProblem problem) {
        InfoflowConfiguration.SolverConfiguration solverConfig = this.config.getSolverConfiguration();
        IInfoflowSolver solver = createDataFlowSolver(executor, problem, solverConfig);
        solver.setSolverId(true);
        solver.setPredecessorShorteningMode(pathConfigToShorteningMode(this.manager.getConfig().getPathConfiguration()));
        solver.setMaxJoinPointAbstractions(solverConfig.getMaxJoinPointAbstractions());
        solver.setMaxCalleesPerCallSite(solverConfig.getMaxCalleesPerCallSite());
        solver.setMaxAbstractionPathLength(solverConfig.getMaxAbstractionPathLength());
        return solver;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PredecessorShorteningMode pathConfigToShorteningMode(InfoflowConfiguration.PathConfiguration pathConfiguration) {
        if (this.pathBuilderFactory.supportsPathReconstruction()) {
            switch ($SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$PathReconstructionMode()[pathConfiguration.getPathReconstructionMode().ordinal()]) {
                case 1:
                    return PredecessorShorteningMode.AlwaysShorten;
                case 2:
                    return PredecessorShorteningMode.ShortenIfEqual;
                case 3:
                    return PredecessorShorteningMode.NeverShorten;
                default:
                    throw new RuntimeException("Unknown path reconstruction mode");
            }
        }
        return PredecessorShorteningMode.AlwaysShorten;
    }

    protected IMemoryManager<Abstraction, Unit> createMemoryManager() {
        FlowDroidMemoryManager.PathDataErasureMode erasureMode;
        if (this.memoryManagerFactory == null) {
            return null;
        }
        if (this.config.getPathConfiguration().mustKeepStatements()) {
            erasureMode = FlowDroidMemoryManager.PathDataErasureMode.EraseNothing;
        } else if (this.pathBuilderFactory.supportsPathReconstruction()) {
            erasureMode = FlowDroidMemoryManager.PathDataErasureMode.EraseNothing;
        } else if (this.pathBuilderFactory.isContextSensitive()) {
            erasureMode = FlowDroidMemoryManager.PathDataErasureMode.KeepOnlyContextData;
        } else {
            erasureMode = FlowDroidMemoryManager.PathDataErasureMode.EraseAll;
        }
        IMemoryManager<Abstraction, Unit> memoryManager = this.memoryManagerFactory.getMemoryManager(false, erasureMode);
        return memoryManager;
    }

    protected void releaseCallgraph() {
        Scene.v().releaseCallGraph();
        Scene.v().releasePointsToAnalysis();
        Scene.v().releaseReachableMethods();
        G.v().resetSpark();
    }

    private void checkAndFixConfiguration() {
        InfoflowConfiguration.AccessPathConfiguration accessPathConfig = this.config.getAccessPathConfiguration();
        if (this.config.getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && accessPathConfig.getAccessPathLength() == 0) {
            throw new RuntimeException("Static field tracking must be disabled if the access path length is zero");
        }
        if (this.config.getSolverConfiguration().getDataFlowSolver() == InfoflowConfiguration.DataFlowSolver.FlowInsensitive) {
            this.config.setFlowSensitiveAliasing(false);
            this.config.setEnableTypeChecking(false);
            this.logger.warn("Disabled flow-sensitive aliasing because we are running with a flow-insensitive data flow solver");
        }
    }

    protected InfoflowPerformanceData createPerformanceDataClass() {
        return new InfoflowPerformanceData();
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void setExecutorFactory(IExecutorFactory executorFactory) {
        this.executorFactory = executorFactory;
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void setPropagationRuleManagerFactory(IPropagationRuleManagerFactory ruleManagerFactory) {
        this.ruleManagerFactory = ruleManagerFactory;
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public Set<Stmt> getCollectedSources() {
        return this.collectedSources;
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public Set<Stmt> getCollectedSinks() {
        return this.collectedSinks;
    }

    private int getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return (int) Math.round((runtime.totalMemory() - runtime.freeMemory()) / 1000000.0d);
    }

    private void initializeIncrementalResultReporting(TaintPropagationResults propagationResults, final IAbstractionPathBuilder builder) {
        this.memoryWatcher.addSolver(builder);
        this.results = createResultsObject();
        propagationResults.addResultAvailableHandler(new TaintPropagationResults.OnTaintPropagationResultAdded() { // from class: soot.jimple.infoflow.AbstractInfoflow.3
            @Override // soot.jimple.infoflow.problems.TaintPropagationResults.OnTaintPropagationResultAdded
            public boolean onResultAvailable(AbstractionAtSink abs) {
                builder.addResultAvailableHandler(new IAbstractionPathBuilder.OnPathBuilderResultAvailable() { // from class: soot.jimple.infoflow.AbstractInfoflow.3.1
                    @Override // soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder.OnPathBuilderResultAvailable
                    public void onResultAvailable(ResultSourceInfo source, ResultSinkInfo sink) {
                        for (ResultsAvailableHandler handler : AbstractInfoflow.this.onResultsAvailable) {
                            if (handler instanceof ResultsAvailableHandler2) {
                                ResultsAvailableHandler2 handler2 = (ResultsAvailableHandler2) handler;
                                handler2.onSingleResultAvailable(source, sink);
                            }
                        }
                        AbstractInfoflow.this.results.addResult(sink, source);
                    }
                });
                builder.computeTaintPaths(Collections.singleton(abs));
                return true;
            }
        });
    }

    private void removeEntailedAbstractions(Set<AbstractionAtSink> res) {
        Iterator<AbstractionAtSink> absAtSinkIt = res.iterator();
        while (absAtSinkIt.hasNext()) {
            AbstractionAtSink curAbs = absAtSinkIt.next();
            Iterator<AbstractionAtSink> it = res.iterator();
            while (true) {
                if (it.hasNext()) {
                    AbstractionAtSink checkAbs = it.next();
                    if (checkAbs != curAbs && checkAbs.getSinkStmt() == curAbs.getSinkStmt() && checkAbs.getAbstraction().isImplicit() == curAbs.getAbstraction().isImplicit() && checkAbs.getAbstraction().getSourceContext() == curAbs.getAbstraction().getSourceContext() && checkAbs.getAbstraction().getTurnUnit() == curAbs.getAbstraction().getTurnUnit() && checkAbs.getAbstraction().getAccessPath().entails(curAbs.getAbstraction().getAccessPath())) {
                        absAtSinkIt.remove();
                        break;
                    }
                }
            }
        }
    }

    protected Aliasing createAliasController(IAliasingStrategy aliasingStrategy) {
        return new Aliasing(aliasingStrategy, this.manager);
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void addResultsAvailableHandler(ResultsAvailableHandler handler) {
        this.onResultsAvailable.add(handler);
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void setTaintPropagationHandler(TaintPropagationHandler handler) {
        this.taintPropagationHandler = handler;
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void setAliasPropagationHandler(TaintPropagationHandler handler) {
        this.aliasPropagationHandler = handler;
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void removeResultsAvailableHandler(ResultsAvailableHandler handler) {
        this.onResultsAvailable.remove(handler);
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public InfoflowResults getResults() {
        return this.results;
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public boolean isResultAvailable() {
        if (this.results == null) {
            return false;
        }
        return true;
    }

    @Override // soot.jimple.infoflow.IInfoflow
    public void abortAnalysis() {
        ISolverTerminationReason reason = new AbortRequestedReason();
        if (this.manager != null) {
            IInfoflowSolver forwardSolver = this.manager.getForwardSolver();
            if (forwardSolver instanceof IMemoryBoundedSolver) {
                IMemoryBoundedSolver boundedSolver = (IMemoryBoundedSolver) forwardSolver;
                boundedSolver.forceTerminate(reason);
            }
            IInfoflowSolver backwardSolver = this.manager.getBackwardSolver();
            if (backwardSolver instanceof IMemoryBoundedSolver) {
                IMemoryBoundedSolver boundedSolver2 = (IMemoryBoundedSolver) backwardSolver;
                boundedSolver2.forceTerminate(reason);
            }
        }
        if (this.memoryWatcher != null) {
            this.memoryWatcher.forceTerminate(reason);
        }
    }

    public void setThrowExceptions(boolean b) {
        this.throwExceptions = b;
    }

    protected void onBeforeTaintPropagation(IInfoflowSolver forwardSolver, IInfoflowSolver backwardSolver) {
    }

    protected void onTaintPropagationCompleted(IInfoflowSolver forwardSolver, IInfoflowSolver backwardSolver) {
    }

    protected IAbstractionPathBuilder createPathBuilder(InterruptableExecutor executor) {
        return this.pathBuilderFactory.createPathBuilder(this.manager, executor);
    }
}
