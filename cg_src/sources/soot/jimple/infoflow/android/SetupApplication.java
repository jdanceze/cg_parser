package soot.jimple.infoflow.android;

import android.hardware.Camera;
import heros.solver.Pair;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;
import soot.G;
import soot.Main;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.infoflow.AbstractInfoflow;
import soot.jimple.infoflow.BackwardsInfoflow;
import soot.jimple.infoflow.IInfoflow;
import soot.jimple.infoflow.Infoflow;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.callbacks.AbstractCallbackAnalyzer;
import soot.jimple.infoflow.android.callbacks.AndroidCallbackDefinition;
import soot.jimple.infoflow.android.callbacks.DefaultCallbackAnalyzer;
import soot.jimple.infoflow.android.callbacks.FastCallbackAnalyzer;
import soot.jimple.infoflow.android.callbacks.filters.AlienFragmentFilter;
import soot.jimple.infoflow.android.callbacks.filters.AlienHostComponentFilter;
import soot.jimple.infoflow.android.callbacks.filters.ApplicationCallbackFilter;
import soot.jimple.infoflow.android.callbacks.filters.UnreachableConstructorFilter;
import soot.jimple.infoflow.android.callbacks.xml.CollectedCallbacks;
import soot.jimple.infoflow.android.callbacks.xml.CollectedCallbacksSerializer;
import soot.jimple.infoflow.android.config.SootConfigForAndroid;
import soot.jimple.infoflow.android.data.AndroidMemoryManager;
import soot.jimple.infoflow.android.data.AndroidMethod;
import soot.jimple.infoflow.android.data.parsers.PermissionMethodParser;
import soot.jimple.infoflow.android.entryPointCreators.AndroidEntryPointCreator;
import soot.jimple.infoflow.android.entryPointCreators.components.ComponentEntryPointCollection;
import soot.jimple.infoflow.android.iccta.IccInstrumenter;
import soot.jimple.infoflow.android.manifest.IAndroidApplication;
import soot.jimple.infoflow.android.manifest.IManifestHandler;
import soot.jimple.infoflow.android.manifest.ProcessManifest;
import soot.jimple.infoflow.android.resources.ARSCFileParser;
import soot.jimple.infoflow.android.resources.LayoutFileParser;
import soot.jimple.infoflow.android.resources.controls.AndroidLayoutControl;
import soot.jimple.infoflow.android.results.xml.InfoflowResultsSerializer;
import soot.jimple.infoflow.android.source.AccessPathBasedSourceSinkManager;
import soot.jimple.infoflow.android.source.ConfigurationBasedCategoryFilter;
import soot.jimple.infoflow.android.source.UnsupportedSourceSinkFormatException;
import soot.jimple.infoflow.android.source.parsers.xml.XMLSourceSinkParser;
import soot.jimple.infoflow.cfg.BiDirICFGFactory;
import soot.jimple.infoflow.cfg.LibraryClassPatcher;
import soot.jimple.infoflow.config.IInfoflowConfig;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.FlowDroidMemoryManager;
import soot.jimple.infoflow.entryPointCreators.SimulatedCodeElementTag;
import soot.jimple.infoflow.handlers.PostAnalysisHandler;
import soot.jimple.infoflow.handlers.PreAnalysisHandler;
import soot.jimple.infoflow.handlers.ResultsAvailableHandler;
import soot.jimple.infoflow.handlers.TaintPropagationHandler;
import soot.jimple.infoflow.ipc.IIPCManager;
import soot.jimple.infoflow.memory.FlowDroidMemoryWatcher;
import soot.jimple.infoflow.memory.FlowDroidTimeoutWatcher;
import soot.jimple.infoflow.memory.IMemoryBoundedSolver;
import soot.jimple.infoflow.results.InfoflowPerformanceData;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.rifl.RIFLSourceSinkDefinitionProvider;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.infoflow.solver.memory.IMemoryManager;
import soot.jimple.infoflow.solver.memory.IMemoryManagerFactory;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider;
import soot.jimple.infoflow.sourcesSinks.definitions.MethodSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager;
import soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper;
import soot.jimple.infoflow.taintWrappers.ITaintWrapperDataFlowAnalysis;
import soot.jimple.infoflow.util.SystemClassHandler;
import soot.jimple.infoflow.values.IValueProvider;
import soot.options.Options;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/SetupApplication.class */
public class SetupApplication implements ITaintWrapperDataFlowAnalysis {
    private final Logger logger;
    protected ISourceSinkDefinitionProvider sourceSinkProvider;
    protected MultiMap<SootClass, AndroidCallbackDefinition> callbackMethods;
    protected MultiMap<SootClass, SootClass> fragmentClasses;
    protected InfoflowAndroidConfiguration config;
    protected Set<SootClass> entrypoints;
    protected Set<String> callbackClasses;
    protected AndroidEntryPointCreator entryPointCreator;
    protected IccInstrumenter iccInstrumenter;
    protected ARSCFileParser resources;
    protected IManifestHandler manifest;
    protected IValueProvider valueProvider;
    protected final boolean forceAndroidJar;
    protected ITaintPropagationWrapper taintWrapper;
    protected ISourceSinkManager sourceSinkManager;
    protected IInfoflowConfig sootConfig;
    protected BiDirICFGFactory cfgFactory;
    protected IIPCManager ipcManager;
    protected Set<Stmt> collectedSources;
    protected Set<Stmt> collectedSinks;
    protected String callbackFile;
    protected SootClass scView;
    protected Set<PreAnalysisHandler> preprocessors;
    protected Set<ResultsAvailableHandler> resultsAvailableHandlers;
    protected TaintPropagationHandler taintPropagationHandler;
    protected TaintPropagationHandler aliasPropagationHandler;
    protected IInPlaceInfoflow infoflow;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$android$InfoflowAndroidConfiguration$CallbackAnalyzer;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$CallgraphAlgorithm;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/SetupApplication$IInPlaceInfoflow.class */
    public interface IInPlaceInfoflow extends IInfoflow {
        void runAnalysis(ISourceSinkManager iSourceSinkManager, SootMethod sootMethod);
    }

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$android$InfoflowAndroidConfiguration$CallbackAnalyzer() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$android$InfoflowAndroidConfiguration$CallbackAnalyzer;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[InfoflowAndroidConfiguration.CallbackAnalyzer.valuesCustom().length];
        try {
            iArr2[InfoflowAndroidConfiguration.CallbackAnalyzer.Default.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[InfoflowAndroidConfiguration.CallbackAnalyzer.Fast.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$android$InfoflowAndroidConfiguration$CallbackAnalyzer = iArr2;
        return iArr2;
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

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/SetupApplication$MultiRunResultAggregator.class */
    public static class MultiRunResultAggregator implements ResultsAvailableHandler {
        private final InfoflowResults aggregatedResults;
        private InfoflowResults lastResults = null;
        private IInfoflowCFG lastICFG = null;

        public MultiRunResultAggregator(boolean pathAgnosticResults) {
            this.aggregatedResults = new InfoflowResults(pathAgnosticResults);
        }

        @Override // soot.jimple.infoflow.handlers.ResultsAvailableHandler
        public void onResultsAvailable(IInfoflowCFG cfg, InfoflowResults results) {
            this.aggregatedResults.addAll(results);
            this.lastResults = results;
            this.lastICFG = cfg;
        }

        public InfoflowResults getAggregatedResults() {
            return this.aggregatedResults;
        }

        public InfoflowResults getLastResults() {
            return this.lastResults;
        }

        public void clearLastResults() {
            this.lastResults = null;
            this.lastICFG = null;
        }

        public IInfoflowCFG getLastICFG() {
            return this.lastICFG;
        }
    }

    public SetupApplication(InfoflowAndroidConfiguration config) {
        this(config, (IIPCManager) null);
    }

    public SetupApplication(String androidJar, String apkFileLocation) {
        this(getConfig(androidJar, apkFileLocation));
    }

    public SetupApplication(String androidJar, String apkFileLocation, IIPCManager ipcManager) {
        this(getConfig(androidJar, apkFileLocation), ipcManager);
    }

    private static InfoflowAndroidConfiguration getConfig(String androidJar, String apkFileLocation) {
        InfoflowAndroidConfiguration config = new InfoflowAndroidConfiguration();
        config.getAnalysisFileConfig().setTargetAPKFile(apkFileLocation);
        config.getAnalysisFileConfig().setAndroidPlatformDir(androidJar);
        return config;
    }

    public SetupApplication(InfoflowAndroidConfiguration config, IIPCManager ipcManager) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.callbackMethods = new HashMultiMap();
        this.fragmentClasses = new HashMultiMap();
        this.config = new InfoflowAndroidConfiguration();
        this.entrypoints = null;
        this.callbackClasses = null;
        this.entryPointCreator = null;
        this.iccInstrumenter = null;
        this.resources = null;
        this.manifest = null;
        this.valueProvider = null;
        this.sourceSinkManager = null;
        this.sootConfig = new SootConfigForAndroid();
        this.cfgFactory = null;
        this.ipcManager = null;
        this.collectedSources = null;
        this.collectedSinks = null;
        this.callbackFile = "AndroidCallbacks.txt";
        this.scView = null;
        this.preprocessors = new HashSet();
        this.resultsAvailableHandlers = new HashSet();
        this.taintPropagationHandler = null;
        this.aliasPropagationHandler = null;
        this.infoflow = null;
        this.config = config;
        this.ipcManager = ipcManager;
        if (config.getSootIntegrationMode() == InfoflowConfiguration.SootIntegrationMode.CreateNewInstance) {
            String platformDir = config.getAnalysisFileConfig().getAndroidPlatformDir();
            if (platformDir == null || platformDir.isEmpty()) {
                throw new RuntimeException("Android platform directory not specified");
            }
            File f = new File(platformDir);
            this.forceAndroidJar = f.isFile();
            return;
        }
        this.forceAndroidJar = false;
    }

    public Collection<? extends ISourceSinkDefinition> getSinks() {
        if (this.sourceSinkProvider == null) {
            return null;
        }
        return this.sourceSinkProvider.getSinks();
    }

    public Set<Stmt> getCollectedSinks() {
        return this.collectedSinks;
    }

    public void printSinks() {
        if (this.sourceSinkProvider == null) {
            this.logger.error("Sinks not calculated yet");
            return;
        }
        this.logger.info("Sinks:");
        for (ISourceSinkDefinition am : getSinks()) {
            this.logger.info(String.format("- %s", am.toString()));
        }
        this.logger.info("End of Sinks");
    }

    public Collection<? extends ISourceSinkDefinition> getSources() {
        if (this.sourceSinkProvider == null) {
            return null;
        }
        return this.sourceSinkProvider.getSources();
    }

    public Set<Stmt> getCollectedSources() {
        return this.collectedSources;
    }

    public void printSources() {
        if (this.sourceSinkProvider == null) {
            this.logger.error("Sources not calculated yet");
            return;
        }
        this.logger.info("Sources:");
        for (ISourceSinkDefinition am : getSources()) {
            this.logger.info(String.format("- %s", am.toString()));
        }
        this.logger.info("End of Sources");
    }

    public Set<SootClass> getEntrypointClasses() {
        return this.entrypoints;
    }

    public void printEntrypoints() {
        if (this.entrypoints == null) {
            this.logger.error("Entry points not initialized");
            return;
        }
        this.logger.info("Classes containing entry points:");
        for (SootClass sc : this.entrypoints) {
            this.logger.info("\t" + sc.getName());
        }
        this.logger.info("End of Entrypoints");
    }

    public void setCallbackClasses(Set<String> callbackClasses) {
        this.callbackClasses = callbackClasses;
    }

    public Set<String> getCallbackClasses() {
        return this.callbackClasses;
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintWrapperDataFlowAnalysis
    public void setTaintWrapper(ITaintPropagationWrapper taintWrapper) {
        this.taintWrapper = taintWrapper;
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintWrapperDataFlowAnalysis
    public ITaintPropagationWrapper getTaintWrapper() {
        return this.taintWrapper;
    }

    protected void parseAppResources() throws IOException, XmlPullParserException {
        File targetAPK = new File(this.config.getAnalysisFileConfig().getTargetAPKFile());
        if (!targetAPK.exists()) {
            throw new RuntimeException(String.format("Target APK file %s does not exist", targetAPK.getCanonicalPath()));
        }
        long beforeARSC = System.nanoTime();
        this.resources = new ARSCFileParser();
        this.resources.parse(targetAPK.getAbsolutePath());
        this.logger.info("ARSC file parsing took " + ((System.nanoTime() - beforeARSC) / 1.0E9d) + " seconds");
        this.manifest = createManifestParser(targetAPK);
        SystemClassHandler.v().setExcludeSystemComponents(this.config.getIgnoreFlowsInSystemPackages());
        Set<String> entryPoints = this.manifest.getEntryPointClasses();
        this.entrypoints = new HashSet(entryPoints.size());
        for (String className : entryPoints) {
            SootClass sc = Scene.v().getSootClassUnsafe(className);
            if (sc != null) {
                this.entrypoints.add(sc);
            }
        }
    }

    protected IManifestHandler createManifestParser(File targetAPK) throws IOException, XmlPullParserException {
        return new ProcessManifest(targetAPK, this.resources);
    }

    private void calculateCallbacks(ISourceSinkDefinitionProvider sourcesAndSinks) throws IOException, XmlPullParserException {
        calculateCallbacks(sourcesAndSinks, null);
    }

    private void calculateCallbacks(ISourceSinkDefinitionProvider sourcesAndSinks, SootClass entryPoint) throws IOException, XmlPullParserException {
        CollectedCallbacks callbacks;
        LayoutFileParser lfp = null;
        InfoflowAndroidConfiguration.CallbackConfiguration callbackConfig = this.config.getCallbackConfig();
        if (callbackConfig.getEnableCallbacks()) {
            String callbackFile = callbackConfig.getCallbacksFile();
            if (callbackFile != null && !callbackFile.isEmpty()) {
                File cbFile = new File(callbackFile);
                if (cbFile.exists() && (callbacks = CollectedCallbacksSerializer.deserialize(callbackConfig)) != null) {
                    this.entrypoints = callbacks.getEntryPoints();
                    this.fragmentClasses = callbacks.getFragmentClasses();
                    this.callbackMethods = callbacks.getCallbackMethods();
                    createMainMethod(entryPoint);
                    constructCallgraphInternal();
                    createSourceSinkProvider(entryPoint, null);
                    return;
                }
            }
            if (this.callbackClasses != null && this.callbackClasses.isEmpty()) {
                this.logger.warn("Callback definition file is empty, disabling callbacks");
            } else {
                lfp = createLayoutFileParser();
                switch ($SWITCH_TABLE$soot$jimple$infoflow$android$InfoflowAndroidConfiguration$CallbackAnalyzer()[callbackConfig.getCallbackAnalyzer().ordinal()]) {
                    case 1:
                        calculateCallbackMethods(lfp, entryPoint);
                        break;
                    case 2:
                        calculateCallbackMethodsFast(lfp, entryPoint);
                        break;
                    default:
                        throw new RuntimeException("Unknown callback analyzer");
                }
            }
        } else if (this.config.getSootIntegrationMode().needsToBuildCallgraph()) {
            createMainMethod(entryPoint);
            constructCallgraphInternal();
        }
        this.logger.info("Entry point calculation done.");
        createSourceSinkProvider(entryPoint, lfp);
    }

    protected void createSourceSinkProvider(SootClass entryPoint, LayoutFileParser lfp) {
        Set<AndroidCallbackDefinition> callbacks;
        if (this.sourceSinkProvider != null) {
            if (entryPoint == null) {
                callbacks = this.callbackMethods.values();
            } else {
                callbacks = this.callbackMethods.get(entryPoint);
            }
            this.sourceSinkManager = createSourceSinkManager(lfp, callbacks);
        }
    }

    protected LayoutFileParser createLayoutFileParser() {
        return new LayoutFileParser(this.manifest.getPackageName(), this.resources);
    }

    protected ISourceSinkManager createSourceSinkManager(LayoutFileParser lfp, Set<AndroidCallbackDefinition> callbacks) {
        AccessPathBasedSourceSinkManager sourceSinkManager = new AccessPathBasedSourceSinkManager(this.sourceSinkProvider.getSources(), this.sourceSinkProvider.getSinks(), callbacks, this.config, lfp == null ? null : lfp.getUserControlsByID());
        sourceSinkManager.setAppPackageName(this.manifest.getPackageName());
        sourceSinkManager.setResourcePackages(this.resources.getPackages());
        return sourceSinkManager;
    }

    protected void constructCallgraphInternal() {
        if (this.config.getSootIntegrationMode() == InfoflowConfiguration.SootIntegrationMode.UseExistingCallgraph) {
            if (!Scene.v().hasCallGraph()) {
                throw new RuntimeException("FlowDroid is configured to use an existing callgraph, but there is none");
            }
            return;
        }
        if (this.config.getIccConfig().isIccEnabled()) {
            if (this.iccInstrumenter == null) {
                this.iccInstrumenter = createIccInstrumenter();
            }
            this.iccInstrumenter.onBeforeCallgraphConstruction();
        }
        for (PreAnalysisHandler handler : this.preprocessors) {
            handler.onBeforeCallgraphConstruction();
        }
        releaseCallgraph();
        if (this.config.getSootIntegrationMode() == InfoflowConfiguration.SootIntegrationMode.UseExistingInstance) {
            configureCallgraph();
        }
        this.logger.info("Constructing the callgraph...");
        PackManager.v().getPack("cg").apply();
        if (this.iccInstrumenter != null) {
            this.iccInstrumenter.onAfterCallgraphConstruction();
        }
        for (PreAnalysisHandler handler2 : this.preprocessors) {
            handler2.onAfterCallgraphConstruction();
        }
        Scene.v().getOrMakeFastHierarchy();
    }

    protected IccInstrumenter createIccInstrumenter() {
        IccInstrumenter iccInstrumenter = new IccInstrumenter(this.config.getIccConfig().getIccModel(), this.entryPointCreator.getGeneratedMainMethod().getDeclaringClass(), this.entryPointCreator.getComponentToEntryPointInfo());
        return iccInstrumenter;
    }

    private void calculateCallbackMethods(LayoutFileParser lfp, SootClass component) throws IOException {
        AbstractCallbackAnalyzer defaultCallbackAnalyzer;
        InfoflowAndroidConfiguration.CallbackConfiguration callbackConfig = this.config.getCallbackConfig();
        if (this.config.getSootIntegrationMode().needsToBuildCallgraph()) {
            releaseCallgraph();
        }
        PackManager.v().getPack("wjtp").remove("wjtp.lfp");
        PackManager.v().getPack("wjtp").remove("wjtp.ajc");
        Set<SootClass> entryPointClasses = getComponentsToAnalyze(component);
        if (this.callbackClasses == null) {
            defaultCallbackAnalyzer = new DefaultCallbackAnalyzer(this.config, entryPointClasses, this.callbackMethods, this.callbackFile);
        } else {
            defaultCallbackAnalyzer = new DefaultCallbackAnalyzer(this.config, entryPointClasses, this.callbackMethods, this.callbackClasses);
        }
        AbstractCallbackAnalyzer jimpleClass = defaultCallbackAnalyzer;
        if (this.valueProvider != null) {
            jimpleClass.setValueProvider(this.valueProvider);
        }
        jimpleClass.addCallbackFilter(new AlienHostComponentFilter(this.entrypoints));
        jimpleClass.addCallbackFilter(new ApplicationCallbackFilter(this.entrypoints));
        jimpleClass.addCallbackFilter(new UnreachableConstructorFilter());
        jimpleClass.collectCallbackMethods();
        lfp.parseLayoutFile(this.config.getAnalysisFileConfig().getTargetAPKFile());
        FlowDroidMemoryWatcher memoryWatcher = null;
        FlowDroidTimeoutWatcher timeoutWatcher = null;
        if (jimpleClass instanceof IMemoryBoundedSolver) {
            memoryWatcher = createCallbackMemoryWatcher(jimpleClass);
            timeoutWatcher = createCallbackTimeoutWatcher(callbackConfig, jimpleClass);
        }
        int depthIdx = 0;
        boolean hasChanged = true;
        boolean isInitial = true;
        while (true) {
            try {
                if (!hasChanged) {
                    break;
                }
                try {
                    hasChanged = false;
                    if (!(jimpleClass instanceof IMemoryBoundedSolver) || !((IMemoryBoundedSolver) jimpleClass).isKilled()) {
                        createMainMethod(component);
                        int numPrevEdges = 0;
                        if (Scene.v().hasCallGraph()) {
                            numPrevEdges = Scene.v().getCallGraph().size();
                        }
                        if ((jimpleClass instanceof IMemoryBoundedSolver) && ((IMemoryBoundedSolver) jimpleClass).isKilled()) {
                            this.logger.warn("Callback calculation aborted due to timeout");
                            break;
                        }
                        if (!isInitial) {
                            releaseCallgraph();
                            PackManager.v().getPack("wjtp").remove("wjtp.lfp");
                        }
                        isInitial = false;
                        constructCallgraphInternal();
                        if (!Scene.v().hasCallGraph()) {
                            throw new RuntimeException("No callgraph in Scene even after creating one. That's very sad and should never happen.");
                        }
                        lfp.parseLayoutFileDirect(this.config.getAnalysisFileConfig().getTargetAPKFile());
                        PackManager.v().getPack("wjtp").apply();
                        if ((jimpleClass instanceof IMemoryBoundedSolver) && ((IMemoryBoundedSolver) jimpleClass).isKilled()) {
                            this.logger.warn("Aborted callback collection because of low memory");
                            break;
                        }
                        if (numPrevEdges < Scene.v().getCallGraph().size()) {
                            hasChanged = true;
                        }
                        if (this.callbackMethods.putAll(jimpleClass.getCallbackMethods())) {
                            hasChanged = true;
                        }
                        if (this.entrypoints.addAll(jimpleClass.getDynamicManifestComponents())) {
                            hasChanged = true;
                        }
                        if (collectXmlBasedCallbackMethods(lfp, jimpleClass)) {
                            hasChanged = true;
                        }
                        if (callbackConfig.getMaxCallbacksPerComponent() > 0) {
                            Iterator<SootClass> componentIt = this.callbackMethods.keySet().iterator();
                            while (componentIt.hasNext()) {
                                SootClass callbackComponent = componentIt.next();
                                if (this.callbackMethods.get(callbackComponent).size() > callbackConfig.getMaxCallbacksPerComponent()) {
                                    componentIt.remove();
                                    jimpleClass.excludeEntryPoint(callbackComponent);
                                }
                            }
                        }
                        depthIdx++;
                        if (callbackConfig.getMaxAnalysisCallbackDepth() > 0) {
                            if (depthIdx >= callbackConfig.getMaxAnalysisCallbackDepth()) {
                                break;
                            }
                        }
                        if (this.config.getSootIntegrationMode() == InfoflowConfiguration.SootIntegrationMode.UseExistingCallgraph) {
                            break;
                        }
                    } else {
                        break;
                    }
                } catch (Exception ex) {
                    this.logger.error("Could not calculate callback methods", (Throwable) ex);
                    throw ex;
                }
            } finally {
                if (timeoutWatcher != null) {
                    timeoutWatcher.stop();
                }
                if (memoryWatcher != null) {
                    memoryWatcher.close();
                }
            }
        }
        AlienFragmentFilter fragmentFilter = new AlienFragmentFilter(invertMap(this.fragmentClasses));
        fragmentFilter.reset();
        Iterator<Pair<K, V>> it = this.callbackMethods.iterator();
        while (it.hasNext()) {
            Pair<SootClass, AndroidCallbackDefinition> pair = (Pair) it.next();
            if (!fragmentFilter.accepts(pair.getO1(), pair.getO2().getTargetMethod())) {
                it.remove();
            } else if (!fragmentFilter.accepts(pair.getO1(), pair.getO2().getTargetMethod().getDeclaringClass())) {
                it.remove();
            }
        }
        if (callbackConfig.getMaxCallbacksPerComponent() > 0) {
            Iterator<SootClass> componentIt2 = this.callbackMethods.keySet().iterator();
            while (componentIt2.hasNext()) {
                if (this.callbackMethods.get(componentIt2.next()).size() > callbackConfig.getMaxCallbacksPerComponent()) {
                    componentIt2.remove();
                }
            }
        }
        PackManager.v().getPack("wjtp").remove("wjtp.lfp");
        PackManager.v().getPack("wjtp").remove("wjtp.ajc");
        boolean abortedEarly = false;
        if ((jimpleClass instanceof IMemoryBoundedSolver) && ((IMemoryBoundedSolver) jimpleClass).isKilled()) {
            this.logger.warn("Callback analysis aborted early due to time or memory exhaustion");
            abortedEarly = true;
        }
        if (!abortedEarly) {
            this.logger.info("Callback analysis terminated normally");
        }
        if (callbackConfig.isSerializeCallbacks()) {
            CollectedCallbacks callbacks = new CollectedCallbacks(entryPointClasses, this.callbackMethods, this.fragmentClasses);
            CollectedCallbacksSerializer.serialize(callbacks, callbackConfig);
        }
    }

    protected FlowDroidMemoryWatcher createCallbackMemoryWatcher(AbstractCallbackAnalyzer jimpleClass) {
        FlowDroidMemoryWatcher memoryWatcher = new FlowDroidMemoryWatcher(this.config.getMemoryThreshold());
        memoryWatcher.addSolver((IMemoryBoundedSolver) jimpleClass);
        return memoryWatcher;
    }

    protected FlowDroidTimeoutWatcher createCallbackTimeoutWatcher(InfoflowAndroidConfiguration.CallbackConfiguration callbackConfig, AbstractCallbackAnalyzer analyzer) {
        if (callbackConfig.getCallbackAnalysisTimeout() > 0) {
            FlowDroidTimeoutWatcher timeoutWatcher = new FlowDroidTimeoutWatcher(callbackConfig.getCallbackAnalysisTimeout());
            timeoutWatcher.addSolver((IMemoryBoundedSolver) analyzer);
            timeoutWatcher.start();
            return timeoutWatcher;
        }
        return null;
    }

    private <K, V> MultiMap<K, V> invertMap(MultiMap<V, K> original) {
        MultiMap<K, V> newTag = new HashMultiMap<>();
        for (V key : original.keySet()) {
            for (K value : original.get(key)) {
                newTag.put(value, key);
            }
        }
        return newTag;
    }

    protected void releaseCallgraph() {
        if (this.config.getSootIntegrationMode() == InfoflowConfiguration.SootIntegrationMode.UseExistingCallgraph) {
            return;
        }
        Scene.v().releaseCallGraph();
        Scene.v().releasePointsToAnalysis();
        Scene.v().releaseReachableMethods();
        G.v().resetSpark();
    }

    private boolean collectXmlBasedCallbackMethods(LayoutFileParser lfp, AbstractCallbackAnalyzer jimpleClass) {
        SootMethod smViewOnClick = Scene.v().grabMethod("<android.view.View$OnClickListener: void onClick(android.view.View)>");
        boolean hasNewCallback = false;
        for (SootClass callbackClass : jimpleClass.getLayoutClasses().keySet()) {
            if (!jimpleClass.isExcludedEntryPoint(callbackClass)) {
                Set<Integer> classIds = jimpleClass.getLayoutClasses().get(callbackClass);
                for (Integer classId : classIds) {
                    ARSCFileParser.AbstractResource resource = this.resources.findResource(classId.intValue());
                    if (resource instanceof ARSCFileParser.StringResource) {
                        String layoutFileName = ((ARSCFileParser.StringResource) resource).getValue();
                        Set<String> callbackMethods = lfp.getCallbackMethods().get(layoutFileName);
                        if (callbackMethods != null) {
                            for (String methodName : callbackMethods) {
                                String subSig = "void " + methodName + "(android.view.View)";
                                SootClass sootClass = callbackClass;
                                while (true) {
                                    SootClass currentClass = sootClass;
                                    SootMethod callbackMethod = currentClass.getMethodUnsafe(subSig);
                                    if (callbackMethod != null) {
                                        if (this.callbackMethods.put(callbackClass, new AndroidCallbackDefinition(callbackMethod, smViewOnClick, AndroidCallbackDefinition.CallbackType.Widget))) {
                                            hasNewCallback = true;
                                        }
                                    } else {
                                        SootClass sclass = currentClass.getSuperclassUnsafe();
                                        if (sclass == null) {
                                            this.logger.error(String.format("Callback method %s not found in class %s", methodName, callbackClass.getName()));
                                            break;
                                        }
                                        sootClass = sclass;
                                    }
                                }
                            }
                        }
                        Set<SootClass> fragments = lfp.getFragments().get(layoutFileName);
                        if (fragments != null) {
                            for (SootClass fragment : fragments) {
                                if (this.fragmentClasses.put(callbackClass, fragment)) {
                                    hasNewCallback = true;
                                }
                            }
                        }
                        Set<AndroidLayoutControl> controls = lfp.getUserControls().get(layoutFileName);
                        if (controls != null) {
                            for (AndroidLayoutControl lc : controls) {
                                if (!SystemClassHandler.v().isClassInSystemPackage(lc.getViewClass().getName())) {
                                    hasNewCallback |= registerCallbackMethodsForView(callbackClass, lc);
                                }
                            }
                        }
                    } else {
                        this.logger.error("Unexpected resource type for layout class");
                    }
                }
            }
        }
        if (this.fragmentClasses.putAll(jimpleClass.getFragmentClasses())) {
            hasNewCallback = true;
        }
        return hasNewCallback;
    }

    private void calculateCallbackMethodsFast(LayoutFileParser lfp, SootClass component) throws IOException {
        AbstractCallbackAnalyzer fastCallbackAnalyzer;
        releaseCallgraph();
        createMainMethod(component);
        constructCallgraphInternal();
        Set<SootClass> entryPointClasses = getComponentsToAnalyze(component);
        if (this.callbackClasses == null) {
            fastCallbackAnalyzer = new FastCallbackAnalyzer(this.config, entryPointClasses, this.callbackFile);
        } else {
            fastCallbackAnalyzer = new FastCallbackAnalyzer(this.config, entryPointClasses, this.callbackClasses);
        }
        AbstractCallbackAnalyzer jimpleClass = fastCallbackAnalyzer;
        if (this.valueProvider != null) {
            jimpleClass.setValueProvider(this.valueProvider);
        }
        jimpleClass.collectCallbackMethods();
        this.callbackMethods.putAll(jimpleClass.getCallbackMethods());
        this.entrypoints.addAll(jimpleClass.getDynamicManifestComponents());
        lfp.parseLayoutFileDirect(this.config.getAnalysisFileConfig().getTargetAPKFile());
        collectXmlBasedCallbackMethods(lfp, jimpleClass);
        releaseCallgraph();
        createMainMethod(component);
        constructCallgraphInternal();
    }

    private boolean registerCallbackMethodsForView(SootClass callbackClass, AndroidLayoutControl lc) {
        SootMethod parentMethod;
        if (SystemClassHandler.v().isClassInSystemPackage(callbackClass.getName())) {
            return false;
        }
        if (this.scView == null) {
            this.scView = Scene.v().getSootClass("android.view.View");
        }
        if (!Scene.v().getOrMakeFastHierarchy().canStoreType(lc.getViewClass().getType(), this.scView.getType())) {
            return false;
        }
        SootClass sc = lc.getViewClass();
        Map<String, SootMethod> systemMethods = new HashMap<>(10000);
        for (SootClass parentClass : Scene.v().getActiveHierarchy().getSuperclassesOf(sc)) {
            if (parentClass.getName().startsWith("android.")) {
                for (SootMethod sm : parentClass.getMethods()) {
                    if (!sm.isConstructor()) {
                        systemMethods.put(sm.getSubSignature(), sm);
                    }
                }
            }
        }
        boolean changed = false;
        for (SootMethod sm2 : sc.getMethods()) {
            if (!sm2.isConstructor() && (parentMethod = systemMethods.get(sm2.getSubSignature())) != null) {
                changed |= this.callbackMethods.put(callbackClass, new AndroidCallbackDefinition(sm2, parentMethod, AndroidCallbackDefinition.CallbackType.Widget));
            }
        }
        return changed;
    }

    private void createMainMethod(SootClass component) {
        if (this.config.getSootIntegrationMode() == InfoflowConfiguration.SootIntegrationMode.UseExistingCallgraph) {
            return;
        }
        this.entryPointCreator = createEntryPointCreator(component);
        SootMethod dummyMainMethod = this.entryPointCreator.createDummyMain();
        Scene.v().setEntryPoints(Collections.singletonList(dummyMainMethod));
        if (!dummyMainMethod.getDeclaringClass().isInScene()) {
            Scene.v().addClass(dummyMainMethod.getDeclaringClass());
        }
        dummyMainMethod.getDeclaringClass().setApplicationClass();
    }

    public ISourceSinkManager getSourceSinkManager() {
        return this.sourceSinkManager;
    }

    private String getClasspath() {
        String androidJar = this.config.getAnalysisFileConfig().getAndroidPlatformDir();
        String apkFileLocation = this.config.getAnalysisFileConfig().getTargetAPKFile();
        String additionalClasspath = this.config.getAnalysisFileConfig().getAdditionalClasspath();
        String classpath = this.forceAndroidJar ? androidJar : Scene.v().getAndroidJarPath(androidJar, apkFileLocation);
        if (additionalClasspath != null && !additionalClasspath.isEmpty()) {
            classpath = String.valueOf(classpath) + File.pathSeparator + additionalClasspath;
        }
        this.logger.debug("soot classpath: " + classpath);
        return classpath;
    }

    private void initializeSoot() {
        this.logger.info("Initializing Soot...");
        String androidJar = this.config.getAnalysisFileConfig().getAndroidPlatformDir();
        String apkFileLocation = this.config.getAnalysisFileConfig().getTargetAPKFile();
        G.reset();
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_allow_phantom_refs(true);
        if (this.config.getWriteOutputFiles()) {
            Options.v().set_output_format(1);
        } else {
            Options.v().set_output_format(12);
        }
        Options.v().set_whole_program(true);
        Options.v().set_process_dir(Collections.singletonList(apkFileLocation));
        if (this.forceAndroidJar) {
            Options.v().set_force_android_jar(androidJar);
        } else {
            Options.v().set_android_jars(androidJar);
        }
        Options.v().set_src_prec(6);
        Options.v().set_keep_offset(false);
        Options.v().set_keep_line_number(this.config.getEnableLineNumbers());
        Options.v().set_throw_analysis(3);
        Options.v().set_process_multiple_dex(this.config.getMergeDexFiles());
        Options.v().set_ignore_resolution_errors(true);
        if (this.config.getEnableOriginalNames()) {
            Options.v().setPhaseOption("jb", "use-original-names:true");
        }
        if (this.sootConfig != null) {
            this.sootConfig.setSootOptions(Options.v(), this.config);
        }
        Options.v().set_soot_classpath(getClasspath());
        Main.v().autoSetOptions();
        configureCallgraph();
        this.logger.info("Loading dex files...");
        Scene.v().loadNecessaryClasses();
        PackManager.v().getPack("wjpp").apply();
        LibraryClassPatcher patcher = getLibraryClassPatcher();
        patcher.patchLibraries();
    }

    protected LibraryClassPatcher getLibraryClassPatcher() {
        return new LibraryClassPatcher();
    }

    protected void configureCallgraph() {
        switch ($SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$CallgraphAlgorithm()[this.config.getCallgraphAlgorithm().ordinal()]) {
            case 1:
            case 5:
                Options.v().setPhaseOption("cg.spark", Camera.Parameters.FLASH_MODE_ON);
                break;
            case 2:
                Options.v().setPhaseOption("cg.cha", Camera.Parameters.FLASH_MODE_ON);
                break;
            case 3:
                Options.v().setPhaseOption("cg.spark", Camera.Parameters.FLASH_MODE_ON);
                Options.v().setPhaseOption("cg.spark", "vta:true");
                break;
            case 4:
                Options.v().setPhaseOption("cg.spark", Camera.Parameters.FLASH_MODE_ON);
                Options.v().setPhaseOption("cg.spark", "rta:true");
                Options.v().setPhaseOption("cg.spark", "on-fly-cg:false");
                break;
            case 6:
                Options.v().setPhaseOption("cg.spark", Camera.Parameters.FLASH_MODE_ON);
                AbstractInfoflow.setGeomPtaSpecificOptions();
                break;
            default:
                throw new RuntimeException("Invalid callgraph algorithm");
        }
        if (this.config.getEnableReflection()) {
            Options.v().setPhaseOption("cg", "types-for-invoke:true");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/SetupApplication$InPlaceInfoflow.class */
    public class InPlaceInfoflow extends Infoflow implements IInPlaceInfoflow {
        public InPlaceInfoflow(String androidPath, boolean forceAndroidJar, BiDirICFGFactory icfgFactory, Collection<SootMethod> additionalEntryPointMethods) {
            super(androidPath, forceAndroidJar, icfgFactory);
            this.additionalEntryPointMethods = additionalEntryPointMethods;
        }

        @Override // soot.jimple.infoflow.android.SetupApplication.IInPlaceInfoflow
        public void runAnalysis(ISourceSinkManager sourcesSinks, SootMethod entryPoint) {
            this.dummyMainMethod = entryPoint;
            super.runAnalysis(sourcesSinks);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.jimple.infoflow.AbstractInfoflow
        public boolean isUserCodeClass(String className) {
            String packageName = String.valueOf(SetupApplication.this.manifest.getPackageName()) + ".";
            return super.isUserCodeClass(className) || className.startsWith(packageName);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/SetupApplication$InPlaceBackwardsInfoflow.class */
    public class InPlaceBackwardsInfoflow extends BackwardsInfoflow implements IInPlaceInfoflow {
        public InPlaceBackwardsInfoflow(String androidPath, boolean forceAndroidJar, BiDirICFGFactory icfgFactory, Collection<SootMethod> additionalEntryPointMethods) {
            super(androidPath, forceAndroidJar, icfgFactory);
            this.additionalEntryPointMethods = additionalEntryPointMethods;
        }

        @Override // soot.jimple.infoflow.android.SetupApplication.IInPlaceInfoflow
        public void runAnalysis(ISourceSinkManager sourcesSinks, SootMethod entryPoint) {
            this.dummyMainMethod = entryPoint;
            super.runAnalysis(sourcesSinks);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.jimple.infoflow.AbstractInfoflow
        public boolean isUserCodeClass(String className) {
            String packageName = String.valueOf(SetupApplication.this.manifest.getPackageName()) + ".";
            return super.isUserCodeClass(className) || className.startsWith(packageName);
        }
    }

    public void constructCallgraph() {
        boolean oldRunAnalysis = this.config.isTaintAnalysisEnabled();
        try {
            try {
                this.config.setTaintAnalysisEnabled(false);
                if (!this.config.getSootIntegrationMode().needsToBuildCallgraph()) {
                    throw new RuntimeException("FlowDroid is configured to use an existing callgraph. Please change this option before trying to create a new callgraph.");
                }
                runInfoflow((ISourceSinkDefinitionProvider) null);
            } catch (RuntimeException ex) {
                this.logger.error("Could not construct callgraph", (Throwable) ex);
                throw ex;
            }
        } finally {
            this.config.setTaintAnalysisEnabled(oldRunAnalysis);
        }
    }

    public InfoflowResults runInfoflow(Set<AndroidMethod> sources, Set<AndroidMethod> sinks) throws IOException, XmlPullParserException {
        final Set<ISourceSinkDefinition> sourceDefs = new HashSet<>(sources.size());
        final Set<ISourceSinkDefinition> sinkDefs = new HashSet<>(sinks.size());
        for (AndroidMethod am : sources) {
            sourceDefs.add(new MethodSourceSinkDefinition(am));
        }
        for (AndroidMethod am2 : sinks) {
            sinkDefs.add(new MethodSourceSinkDefinition(am2));
        }
        ISourceSinkDefinitionProvider parser = new ISourceSinkDefinitionProvider() { // from class: soot.jimple.infoflow.android.SetupApplication.1
            @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider
            public Set<ISourceSinkDefinition> getSources() {
                return sourceDefs;
            }

            @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider
            public Set<ISourceSinkDefinition> getSinks() {
                return sinkDefs;
            }

            @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider
            public Set<ISourceSinkDefinition> getAllMethods() {
                Set<ISourceSinkDefinition> sourcesSinks = new HashSet<>(sourceDefs.size() + sinkDefs.size());
                sourcesSinks.addAll(sourceDefs);
                sourcesSinks.addAll(sinkDefs);
                return sourcesSinks;
            }
        };
        return runInfoflow(parser);
    }

    public InfoflowResults runInfoflow(String sourceSinkFile) throws IOException, XmlPullParserException {
        if (sourceSinkFile != null && !sourceSinkFile.isEmpty()) {
            this.config.getAnalysisFileConfig().setSourceSinkFile(sourceSinkFile);
        }
        return runInfoflow();
    }

    public InfoflowResults runInfoflow() throws IOException, XmlPullParserException {
        ISourceSinkDefinitionProvider parser;
        String sourceSinkFile = this.config.getAnalysisFileConfig().getSourceSinkFile();
        if (sourceSinkFile == null || sourceSinkFile.isEmpty()) {
            throw new RuntimeException("No source/sink file specified for the data flow analysis");
        }
        String fileExtension = sourceSinkFile.substring(sourceSinkFile.lastIndexOf(".")).toLowerCase();
        try {
            if (fileExtension.equals(".xml")) {
                parser = XMLSourceSinkParser.fromFile(sourceSinkFile, new ConfigurationBasedCategoryFilter(this.config.getSourceSinkConfig()));
            } else if (fileExtension.equals(".txt")) {
                parser = PermissionMethodParser.fromFile(sourceSinkFile);
            } else if (fileExtension.equals(".rifl")) {
                parser = new RIFLSourceSinkDefinitionProvider(sourceSinkFile);
            } else {
                throw new UnsupportedSourceSinkFormatException("The Inputfile isn't a .txt or .xml file.");
            }
            return runInfoflow(parser);
        } catch (SAXException ex) {
            throw new IOException("Could not read XML file", ex);
        }
    }

    public InfoflowResults runInfoflow(ISourceSinkDefinitionProvider sourcesAndSinks) {
        this.collectedSources = this.config.getLogSourcesAndSinks() ? new HashSet() : null;
        this.collectedSinks = this.config.getLogSourcesAndSinks() ? new HashSet() : null;
        this.sourceSinkProvider = sourcesAndSinks;
        this.infoflow = null;
        if (this.config.getSourceSinkConfig().getEnableLifecycleSources() && this.config.getIccConfig().isIccEnabled()) {
            this.logger.warn("ICC model specified, automatically disabling lifecycle sources");
            this.config.getSourceSinkConfig().setEnableLifecycleSources(false);
        }
        if (this.config.getSootIntegrationMode() == InfoflowConfiguration.SootIntegrationMode.CreateNewInstance) {
            G.reset();
            initializeSoot();
        }
        try {
            parseAppResources();
            MultiRunResultAggregator resultAggregator = new MultiRunResultAggregator(this.config.getPathAgnosticResults());
            if (this.entrypoints == null || this.entrypoints.isEmpty()) {
                this.logger.warn("No entry points");
                return null;
            }
            if (this.config.getOneComponentAtATime()) {
                List<SootClass> entrypointWorklist = new ArrayList<>(this.entrypoints);
                while (!entrypointWorklist.isEmpty()) {
                    SootClass entrypoint = entrypointWorklist.remove(0);
                    processEntryPoint(sourcesAndSinks, resultAggregator, entrypointWorklist.size(), entrypoint);
                }
            } else {
                processEntryPoint(sourcesAndSinks, resultAggregator, -1, null);
            }
            serializeResults(resultAggregator.getAggregatedResults(), resultAggregator.getLastICFG());
            this.infoflow = null;
            resultAggregator.clearLastResults();
            return resultAggregator.getAggregatedResults();
        } catch (IOException | XmlPullParserException e) {
            this.logger.error("Parse app resource failed", (Throwable) e);
            throw new RuntimeException("Parse app resource failed", e);
        }
    }

    protected void processEntryPoint(ISourceSinkDefinitionProvider sourcesAndSinks, MultiRunResultAggregator resultAggregator, int numEntryPoints, SootClass entrypoint) {
        long beforeEntryPoint = System.nanoTime();
        resultAggregator.clearLastResults();
        long callbackDuration = System.nanoTime();
        try {
            if (this.config.getOneComponentAtATime()) {
                calculateCallbacks(sourcesAndSinks, entrypoint);
            } else {
                calculateCallbacks(sourcesAndSinks);
            }
            long callbackDuration2 = Math.round((System.nanoTime() - callbackDuration) / 1.0E9d);
            this.logger.info(String.format("Collecting callbacks and building a callgraph took %d seconds", Integer.valueOf((int) callbackDuration2)));
            Collection<? extends ISourceSinkDefinition> sources = getSources();
            Collection<? extends ISourceSinkDefinition> sinks = getSinks();
            String apkFileLocation = this.config.getAnalysisFileConfig().getTargetAPKFile();
            if (this.config.getOneComponentAtATime()) {
                Logger logger = this.logger;
                Object[] objArr = new Object[6];
                objArr[0] = apkFileLocation;
                objArr[1] = Integer.valueOf(this.entrypoints.size() - numEntryPoints);
                objArr[2] = Integer.valueOf(this.entrypoints.size());
                objArr[3] = entrypoint;
                objArr[4] = Integer.valueOf(sources == null ? 0 : sources.size());
                objArr[5] = Integer.valueOf(sinks == null ? 0 : sinks.size());
                logger.info("Running data flow analysis on {} (component {}/{}: {}) with {} sources and {} sinks...", objArr);
            } else {
                Logger logger2 = this.logger;
                Object[] objArr2 = new Object[3];
                objArr2[0] = apkFileLocation;
                objArr2[1] = Integer.valueOf(sources == null ? 0 : sources.size());
                objArr2[2] = Integer.valueOf(sinks == null ? 0 : sinks.size());
                logger2.info("Running data flow analysis on {} with {} sources and {} sinks...", objArr2);
            }
            if (this.config.getOneComponentAtATime() && this.config.getSootIntegrationMode().needsToBuildCallgraph()) {
                createMainMethod(entrypoint);
                constructCallgraphInternal();
            }
            this.infoflow = createInfoflow();
            this.infoflow.addResultsAvailableHandler(resultAggregator);
            this.infoflow.runAnalysis(this.sourceSinkManager, this.entryPointCreator.getGeneratedMainMethod());
            if (this.config.getLogSourcesAndSinks() && this.infoflow.getCollectedSources() != null) {
                this.collectedSources.addAll(this.infoflow.getCollectedSources());
            }
            if (this.config.getLogSourcesAndSinks() && this.infoflow.getCollectedSinks() != null) {
                this.collectedSinks.addAll(this.infoflow.getCollectedSinks());
            }
            int resCount = resultAggregator.getLastResults() == null ? 0 : resultAggregator.getLastResults().size();
            if (this.config.getOneComponentAtATime()) {
                this.logger.info("Found {} leaks for component {}", Integer.valueOf(resCount), entrypoint);
            } else {
                this.logger.info("Found {} leaks", Integer.valueOf(resCount));
            }
            InfoflowResults lastResults = resultAggregator.getLastResults();
            if (lastResults != null) {
                InfoflowPerformanceData perfData = lastResults.getPerformanceData();
                if (perfData == null) {
                    InfoflowPerformanceData infoflowPerformanceData = new InfoflowPerformanceData();
                    perfData = infoflowPerformanceData;
                    lastResults.setPerformanceData(infoflowPerformanceData);
                }
                perfData.setCallgraphConstructionSeconds((int) callbackDuration2);
                perfData.setTotalRuntimeSeconds((int) Math.round((System.nanoTime() - beforeEntryPoint) / 1.0E9d));
            }
            this.callbackMethods.clear();
            this.fragmentClasses.clear();
            for (ResultsAvailableHandler handler : this.resultsAvailableHandlers) {
                handler.onResultsAvailable(resultAggregator.getLastICFG(), resultAggregator.getLastResults());
            }
        } catch (IOException | XmlPullParserException e) {
            this.logger.error("Callgraph construction failed: " + e.getMessage(), (Throwable) e);
            throw new RuntimeException("Callgraph construction failed", e);
        }
    }

    protected void serializeResults(InfoflowResults results, IInfoflowCFG cfg) {
        String resultsFile;
        if (results != null && !results.isEmpty() && (resultsFile = this.config.getAnalysisFileConfig().getOutputFile()) != null && !resultsFile.isEmpty()) {
            InfoflowResultsSerializer serializer = new InfoflowResultsSerializer(cfg, this.config);
            try {
                serializer.serialize(results, resultsFile);
            } catch (IOException ex) {
                System.err.println("Could not write data flow results to file: " + ex.getMessage());
                ex.printStackTrace();
            } catch (XMLStreamException ex2) {
                System.err.println("Could not write data flow results to file: " + ex2.getMessage());
                ex2.printStackTrace();
            }
        }
    }

    private IInPlaceInfoflow createInfoflow() {
        ComponentEntryPointCollection entryPoints;
        if (this.config.getSootIntegrationMode().needsToBuildCallgraph()) {
            if (this.entryPointCreator == null) {
                throw new RuntimeException("No entry point available");
            }
            if (this.entryPointCreator.getComponentToEntryPointInfo() == null) {
                throw new RuntimeException("No information about component entry points available");
            }
        }
        Collection<SootMethod> lifecycleMethods = Collections.emptySet();
        if (this.entryPointCreator != null && (entryPoints = this.entryPointCreator.getComponentToEntryPointInfo()) != null) {
            lifecycleMethods = entryPoints.getLifecycleMethods();
        }
        IInPlaceInfoflow info = createInfoflowInternal(lifecycleMethods);
        if (this.ipcManager != null) {
            info.setIPCManager(this.ipcManager);
        }
        info.setConfig(this.config);
        info.setSootConfig(this.sootConfig);
        info.setTaintWrapper(this.taintWrapper);
        info.setTaintPropagationHandler(this.taintPropagationHandler);
        info.setAliasPropagationHandler(this.aliasPropagationHandler);
        info.setMemoryManagerFactory(new IMemoryManagerFactory() { // from class: soot.jimple.infoflow.android.SetupApplication.2
            @Override // soot.jimple.infoflow.solver.memory.IMemoryManagerFactory
            public IMemoryManager<Abstraction, Unit> getMemoryManager(boolean tracingEnabled, FlowDroidMemoryManager.PathDataErasureMode erasePathData) {
                return new AndroidMemoryManager(tracingEnabled, erasePathData, SetupApplication.this.entrypoints);
            }
        });
        info.setMemoryManagerFactory(null);
        info.setPostProcessors(Collections.singleton(new PostAnalysisHandler() { // from class: soot.jimple.infoflow.android.SetupApplication.3
            @Override // soot.jimple.infoflow.handlers.PostAnalysisHandler
            public InfoflowResults onResultsAvailable(InfoflowResults results, IInfoflowCFG cfg) {
                InfoflowAndroidConfiguration.IccConfiguration iccConfig = SetupApplication.this.config.getIccConfig();
                iccConfig.isIccResultsPurifyEnabled();
                return results;
            }
        }));
        return info;
    }

    protected IInPlaceInfoflow createInfoflowInternal(Collection<SootMethod> lifecycleMethods) {
        String androidJar = this.config.getAnalysisFileConfig().getAndroidPlatformDir();
        if (this.config.getDataFlowDirection() == InfoflowConfiguration.DataFlowDirection.Backwards) {
            return new InPlaceBackwardsInfoflow(androidJar, this.forceAndroidJar, this.cfgFactory, lifecycleMethods);
        }
        return new InPlaceInfoflow(androidJar, this.forceAndroidJar, this.cfgFactory, lifecycleMethods);
    }

    private AndroidEntryPointCreator createEntryPointCreator(SootClass component) {
        Set<SootClass> components = getComponentsToAnalyze(component);
        if (this.entryPointCreator == null) {
            this.entryPointCreator = createEntryPointCreator(components);
        } else {
            this.entryPointCreator.removeGeneratedMethods(false);
            this.entryPointCreator.reset();
        }
        MultiMap<SootClass, SootMethod> callbackMethodSigs = new HashMultiMap<>();
        if (component == null) {
            for (SootClass sc : this.callbackMethods.keySet()) {
                Set<AndroidCallbackDefinition> callbackDefs = this.callbackMethods.get(sc);
                if (callbackDefs != null) {
                    for (AndroidCallbackDefinition cd : callbackDefs) {
                        callbackMethodSigs.put(sc, cd.getTargetMethod());
                    }
                }
            }
        } else {
            for (SootClass sc2 : components) {
                Set<AndroidCallbackDefinition> callbackDefs2 = this.callbackMethods.get(sc2);
                if (callbackDefs2 != null) {
                    for (AndroidCallbackDefinition cd2 : callbackDefs2) {
                        callbackMethodSigs.put(sc2, cd2.getTargetMethod());
                    }
                }
            }
        }
        this.entryPointCreator.setCallbackFunctions(callbackMethodSigs);
        this.entryPointCreator.setFragments(this.fragmentClasses);
        this.entryPointCreator.setComponents(components);
        return this.entryPointCreator;
    }

    protected AndroidEntryPointCreator createEntryPointCreator(Set<SootClass> components) {
        return new AndroidEntryPointCreator(this.manifest, components);
    }

    private Set<SootClass> getComponentsToAnalyze(SootClass component) {
        String applicationName;
        if (component == null) {
            return this.entrypoints;
        }
        Set<SootClass> components = new HashSet<>(2);
        components.add(component);
        IAndroidApplication app = this.manifest.getApplication();
        if (app != null && (applicationName = app.getName()) != null && !applicationName.isEmpty()) {
            components.add(Scene.v().getSootClassUnsafe(applicationName));
        }
        return components;
    }

    public SootMethod getDummyMainMethod() {
        return this.entryPointCreator.getGeneratedMainMethod();
    }

    public IInfoflowConfig getSootConfig() {
        return this.sootConfig;
    }

    public void setSootConfig(IInfoflowConfig config) {
        this.sootConfig = config;
    }

    public void setIcfgFactory(BiDirICFGFactory factory) {
        this.cfgFactory = factory;
    }

    public InfoflowAndroidConfiguration getConfig() {
        return this.config;
    }

    public void setCallbackFile(String callbackFile) {
        this.callbackFile = callbackFile;
    }

    public void addPreprocessor(PreAnalysisHandler preprocessor) {
        this.preprocessors.add(preprocessor);
    }

    public void addResultsAvailableHandler(ResultsAvailableHandler handler) {
        this.resultsAvailableHandlers.add(handler);
    }

    public void clearResultsAvailableHandlers() {
        this.resultsAvailableHandlers.clear();
    }

    public void abortAnalysis() {
        if (this.infoflow != null) {
            this.infoflow.abortAnalysis();
        }
    }

    public void setIpcManager(IIPCManager ipcManager) {
        this.ipcManager = ipcManager;
    }

    public void setTaintPropagationHandler(TaintPropagationHandler taintPropagationHandler) {
        this.taintPropagationHandler = taintPropagationHandler;
    }

    public void setAliasPropagationHandler(TaintPropagationHandler aliasPropagationHandler) {
        this.aliasPropagationHandler = aliasPropagationHandler;
    }

    public void setValueProvider(IValueProvider valueProvider) {
        this.valueProvider = valueProvider;
    }

    public void removeSimulatedCodeElements() {
        Iterator<SootClass> scIt = Scene.v().getClasses().iterator();
        while (scIt.hasNext()) {
            SootClass sc = scIt.next();
            if (sc.hasTag(SimulatedCodeElementTag.TAG_NAME)) {
                scIt.remove();
            } else {
                Iterator<SootMethod> smIt = sc.getMethods().iterator();
                while (smIt.hasNext()) {
                    SootMethod sm = smIt.next();
                    if (sm.hasTag(SimulatedCodeElementTag.TAG_NAME)) {
                        smIt.remove();
                    }
                }
                Iterator<SootField> sfIt = sc.getFields().iterator();
                while (sfIt.hasNext()) {
                    SootField sf = sfIt.next();
                    if (sf.hasTag(SimulatedCodeElementTag.TAG_NAME)) {
                        sfIt.remove();
                    }
                }
            }
        }
    }
}
