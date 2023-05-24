package soot.jimple.infoflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration.class */
public class InfoflowConfiguration {
    private final SourceSinkConfiguration sourceSinkConfig = new SourceSinkConfiguration();
    private boolean pathAgnosticResults = true;
    private int stopAfterFirstKFlows = 0;
    private ImplicitFlowMode implicitFlowMode = ImplicitFlowMode.NoImplicitFlows;
    private boolean enableExceptions = true;
    private boolean enableArrays = true;
    private boolean enableArraySizeTainting = true;
    private boolean flowSensitiveAliasing = true;
    private boolean enableTypeChecking = true;
    private boolean ignoreFlowsInSystemPackages = false;
    private boolean excludeSootLibraryClasses = false;
    private int maxThreadNum = -1;
    private boolean writeOutputFiles = false;
    private boolean logSourcesAndSinks = false;
    private boolean enableReflection = false;
    private boolean enableLineNumbers = false;
    private boolean enableOriginalNames = false;
    private boolean inspectSources = false;
    private boolean inspectSinks = false;
    private PathConfiguration pathConfiguration = new PathConfiguration();
    private OutputConfiguration outputConfiguration = new OutputConfiguration();
    private SolverConfiguration solverConfiguration = new SolverConfiguration();
    private AccessPathConfiguration accessPathConfiguration = new AccessPathConfiguration();
    private CallgraphAlgorithm callgraphAlgorithm = CallgraphAlgorithm.AutomaticSelection;
    private AliasingAlgorithm aliasingAlgorithm = AliasingAlgorithm.FlowSensitive;
    private CodeEliminationMode codeEliminationMode = CodeEliminationMode.PropagateConstants;
    private StaticFieldTrackingMode staticFieldTrackingMode = StaticFieldTrackingMode.ContextFlowSensitive;
    private SootIntegrationMode sootIntegrationMode = SootIntegrationMode.CreateNewInstance;
    private DataFlowDirection dataFlowDirection = DataFlowDirection.Forwards;
    private boolean taintAnalysisEnabled = true;
    private boolean incrementalResultReporting = false;
    private long dataFlowTimeout = 0;
    private double memoryThreshold = 0.9d;
    private boolean oneSourceAtATime = false;
    private int maxAliasingBases = Integer.MAX_VALUE;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$ImplicitFlowMode;
    protected static final Logger logger = LoggerFactory.getLogger(InfoflowConfiguration.class);
    private static boolean oneResultPerAccessPath = false;
    private static boolean mergeNeighbors = false;
    private static String baseDirectory = "";

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$AliasingAlgorithm.class */
    public enum AliasingAlgorithm {
        FlowSensitive,
        PtsBased,
        None,
        Lazy;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static AliasingAlgorithm[] valuesCustom() {
            AliasingAlgorithm[] valuesCustom = values();
            int length = valuesCustom.length;
            AliasingAlgorithm[] aliasingAlgorithmArr = new AliasingAlgorithm[length];
            System.arraycopy(valuesCustom, 0, aliasingAlgorithmArr, 0, length);
            return aliasingAlgorithmArr;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$CallbackSourceMode.class */
    public enum CallbackSourceMode {
        NoParametersAsSources,
        AllParametersAsSources,
        SourceListOnly;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static CallbackSourceMode[] valuesCustom() {
            CallbackSourceMode[] valuesCustom = values();
            int length = valuesCustom.length;
            CallbackSourceMode[] callbackSourceModeArr = new CallbackSourceMode[length];
            System.arraycopy(valuesCustom, 0, callbackSourceModeArr, 0, length);
            return callbackSourceModeArr;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$CallgraphAlgorithm.class */
    public enum CallgraphAlgorithm {
        AutomaticSelection,
        CHA,
        VTA,
        RTA,
        SPARK,
        GEOM,
        OnDemand;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static CallgraphAlgorithm[] valuesCustom() {
            CallgraphAlgorithm[] valuesCustom = values();
            int length = valuesCustom.length;
            CallgraphAlgorithm[] callgraphAlgorithmArr = new CallgraphAlgorithm[length];
            System.arraycopy(valuesCustom, 0, callgraphAlgorithmArr, 0, length);
            return callgraphAlgorithmArr;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$CategoryMode.class */
    public enum CategoryMode {
        Include,
        Exclude;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static CategoryMode[] valuesCustom() {
            CategoryMode[] valuesCustom = values();
            int length = valuesCustom.length;
            CategoryMode[] categoryModeArr = new CategoryMode[length];
            System.arraycopy(valuesCustom, 0, categoryModeArr, 0, length);
            return categoryModeArr;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$CodeEliminationMode.class */
    public enum CodeEliminationMode {
        NoCodeElimination,
        PropagateConstants,
        RemoveSideEffectFreeCode;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static CodeEliminationMode[] valuesCustom() {
            CodeEliminationMode[] valuesCustom = values();
            int length = valuesCustom.length;
            CodeEliminationMode[] codeEliminationModeArr = new CodeEliminationMode[length];
            System.arraycopy(valuesCustom, 0, codeEliminationModeArr, 0, length);
            return codeEliminationModeArr;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$DataFlowDirection.class */
    public enum DataFlowDirection {
        Forwards,
        Backwards;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static DataFlowDirection[] valuesCustom() {
            DataFlowDirection[] valuesCustom = values();
            int length = valuesCustom.length;
            DataFlowDirection[] dataFlowDirectionArr = new DataFlowDirection[length];
            System.arraycopy(valuesCustom, 0, dataFlowDirectionArr, 0, length);
            return dataFlowDirectionArr;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$DataFlowSolver.class */
    public enum DataFlowSolver {
        ContextFlowSensitive,
        FlowInsensitive,
        GarbageCollecting;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static DataFlowSolver[] valuesCustom() {
            DataFlowSolver[] valuesCustom = values();
            int length = valuesCustom.length;
            DataFlowSolver[] dataFlowSolverArr = new DataFlowSolver[length];
            System.arraycopy(valuesCustom, 0, dataFlowSolverArr, 0, length);
            return dataFlowSolverArr;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$LayoutMatchingMode.class */
    public enum LayoutMatchingMode {
        NoMatch,
        MatchAll,
        MatchSensitiveOnly;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static LayoutMatchingMode[] valuesCustom() {
            LayoutMatchingMode[] valuesCustom = values();
            int length = valuesCustom.length;
            LayoutMatchingMode[] layoutMatchingModeArr = new LayoutMatchingMode[length];
            System.arraycopy(valuesCustom, 0, layoutMatchingModeArr, 0, length);
            return layoutMatchingModeArr;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$PathBuildingAlgorithm.class */
    public enum PathBuildingAlgorithm {
        Recursive,
        ContextSensitive,
        ContextInsensitive,
        ContextInsensitiveSourceFinder,
        None;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static PathBuildingAlgorithm[] valuesCustom() {
            PathBuildingAlgorithm[] valuesCustom = values();
            int length = valuesCustom.length;
            PathBuildingAlgorithm[] pathBuildingAlgorithmArr = new PathBuildingAlgorithm[length];
            System.arraycopy(valuesCustom, 0, pathBuildingAlgorithmArr, 0, length);
            return pathBuildingAlgorithmArr;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$SourceSinkFilterMode.class */
    public enum SourceSinkFilterMode {
        UseAllButExcluded,
        UseOnlyIncluded;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static SourceSinkFilterMode[] valuesCustom() {
            SourceSinkFilterMode[] valuesCustom = values();
            int length = valuesCustom.length;
            SourceSinkFilterMode[] sourceSinkFilterModeArr = new SourceSinkFilterMode[length];
            System.arraycopy(valuesCustom, 0, sourceSinkFilterModeArr, 0, length);
            return sourceSinkFilterModeArr;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$StaticFieldTrackingMode.class */
    public enum StaticFieldTrackingMode {
        ContextFlowSensitive,
        ContextFlowInsensitive,
        None;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static StaticFieldTrackingMode[] valuesCustom() {
            StaticFieldTrackingMode[] valuesCustom = values();
            int length = valuesCustom.length;
            StaticFieldTrackingMode[] staticFieldTrackingModeArr = new StaticFieldTrackingMode[length];
            System.arraycopy(valuesCustom, 0, staticFieldTrackingModeArr, 0, length);
            return staticFieldTrackingModeArr;
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$ImplicitFlowMode() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$ImplicitFlowMode;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[ImplicitFlowMode.valuesCustom().length];
        try {
            iArr2[ImplicitFlowMode.AllImplicitFlows.ordinal()] = 3;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[ImplicitFlowMode.ArrayAccesses.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[ImplicitFlowMode.NoImplicitFlows.ordinal()] = 1;
        } catch (NoSuchFieldError unused3) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$ImplicitFlowMode = iArr2;
        return iArr2;
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$SootIntegrationMode.class */
    public enum SootIntegrationMode {
        CreateNewInstance,
        UseExistingInstance,
        UseExistingCallgraph;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static SootIntegrationMode[] valuesCustom() {
            SootIntegrationMode[] valuesCustom = values();
            int length = valuesCustom.length;
            SootIntegrationMode[] sootIntegrationModeArr = new SootIntegrationMode[length];
            System.arraycopy(valuesCustom, 0, sootIntegrationModeArr, 0, length);
            return sootIntegrationModeArr;
        }

        public boolean needsToBuildCallgraph() {
            return this == CreateNewInstance || this == UseExistingInstance;
        }

        public boolean needsToInitializeSoot() {
            return this == CreateNewInstance;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$ImplicitFlowMode.class */
    public enum ImplicitFlowMode {
        NoImplicitFlows,
        ArrayAccesses,
        AllImplicitFlows;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static ImplicitFlowMode[] valuesCustom() {
            ImplicitFlowMode[] valuesCustom = values();
            int length = valuesCustom.length;
            ImplicitFlowMode[] implicitFlowModeArr = new ImplicitFlowMode[length];
            System.arraycopy(valuesCustom, 0, implicitFlowModeArr, 0, length);
            return implicitFlowModeArr;
        }

        public boolean trackArrayAccesses() {
            return this == AllImplicitFlows || this == ArrayAccesses;
        }

        public boolean trackControlFlowDependencies() {
            return this == AllImplicitFlows;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$PathReconstructionMode.class */
    public enum PathReconstructionMode {
        NoPaths,
        Fast,
        Precise;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static PathReconstructionMode[] valuesCustom() {
            PathReconstructionMode[] valuesCustom = values();
            int length = valuesCustom.length;
            PathReconstructionMode[] pathReconstructionModeArr = new PathReconstructionMode[length];
            System.arraycopy(valuesCustom, 0, pathReconstructionModeArr, 0, length);
            return pathReconstructionModeArr;
        }

        public boolean reconstructPaths() {
            return this != NoPaths;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$SourceSinkConfiguration.class */
    public static class SourceSinkConfiguration {
        private CallbackSourceMode callbackSourceMode = CallbackSourceMode.SourceListOnly;
        private boolean enableLifecycleSources = false;
        private LayoutMatchingMode layoutMatchingMode = LayoutMatchingMode.MatchSensitiveOnly;
        private SourceSinkFilterMode sourceFilterMode = SourceSinkFilterMode.UseAllButExcluded;
        private SourceSinkFilterMode sinkFilterMode = SourceSinkFilterMode.UseAllButExcluded;

        public void merge(SourceSinkConfiguration ssConfig) {
            this.callbackSourceMode = ssConfig.callbackSourceMode;
            this.enableLifecycleSources = ssConfig.enableLifecycleSources;
            this.layoutMatchingMode = ssConfig.layoutMatchingMode;
            this.sourceFilterMode = ssConfig.sourceFilterMode;
            this.sinkFilterMode = ssConfig.sinkFilterMode;
        }

        public void setSourceFilterMode(SourceSinkFilterMode sourceFilterMode) {
            this.sourceFilterMode = sourceFilterMode;
        }

        public SourceSinkFilterMode getSinkFilterMode() {
            return this.sinkFilterMode;
        }

        public void setSinkFilterMode(SourceSinkFilterMode sinkFilterMode) {
            this.sinkFilterMode = sinkFilterMode;
        }

        public void setCallbackSourceMode(CallbackSourceMode callbackSourceMode) {
            this.callbackSourceMode = callbackSourceMode;
        }

        public CallbackSourceMode getCallbackSourceMode() {
            return this.callbackSourceMode;
        }

        public void setEnableLifecycleSources(boolean enableLifecycleSources) {
            this.enableLifecycleSources = enableLifecycleSources;
        }

        public boolean getEnableLifecycleSources() {
            return this.enableLifecycleSources;
        }

        public void setLayoutMatchingMode(LayoutMatchingMode mode) {
            this.layoutMatchingMode = mode;
        }

        public LayoutMatchingMode getLayoutMatchingMode() {
            return this.layoutMatchingMode;
        }

        public SourceSinkFilterMode getSourceFilterMode() {
            return this.sourceFilterMode;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.callbackSourceMode == null ? 0 : this.callbackSourceMode.hashCode());
            return (31 * ((31 * ((31 * ((31 * result) + (this.enableLifecycleSources ? 1231 : 1237))) + (this.layoutMatchingMode == null ? 0 : this.layoutMatchingMode.hashCode()))) + (this.sinkFilterMode == null ? 0 : this.sinkFilterMode.hashCode()))) + (this.sourceFilterMode == null ? 0 : this.sourceFilterMode.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            SourceSinkConfiguration other = (SourceSinkConfiguration) obj;
            if (this.callbackSourceMode != other.callbackSourceMode || this.enableLifecycleSources != other.enableLifecycleSources || this.layoutMatchingMode != other.layoutMatchingMode || this.sinkFilterMode != other.sinkFilterMode || this.sourceFilterMode != other.sourceFilterMode) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$PathConfiguration.class */
    public static class PathConfiguration {
        private boolean sequentialPathProcessing = false;
        private PathReconstructionMode pathReconstructionMode = PathReconstructionMode.NoPaths;
        private PathBuildingAlgorithm pathBuildingAlgorithm = PathBuildingAlgorithm.ContextSensitive;
        private int maxCallStackSize = 30;
        private int maxPathLength = 75;
        private int maxPathsPerAbstraction = 15;
        private long pathReconstructionTimeout = 0;
        private int pathReconstructionBatchSize = 5;

        public void merge(PathConfiguration pathConfig) {
            this.sequentialPathProcessing = pathConfig.sequentialPathProcessing;
            this.pathReconstructionMode = pathConfig.pathReconstructionMode;
            this.pathBuildingAlgorithm = pathConfig.pathBuildingAlgorithm;
            this.maxCallStackSize = pathConfig.maxCallStackSize;
            this.maxPathLength = pathConfig.maxPathLength;
            this.maxPathsPerAbstraction = pathConfig.maxPathsPerAbstraction;
            this.pathReconstructionTimeout = pathConfig.pathReconstructionTimeout;
            this.pathReconstructionBatchSize = pathConfig.pathReconstructionBatchSize;
        }

        public boolean getSequentialPathProcessing() {
            return this.sequentialPathProcessing;
        }

        public void setSequentialPathProcessing(boolean sequentialPathProcessing) {
            this.sequentialPathProcessing = sequentialPathProcessing;
        }

        public PathReconstructionMode getPathReconstructionMode() {
            return this.pathReconstructionMode;
        }

        public void setPathReconstructionMode(PathReconstructionMode pathReconstructionMode) {
            this.pathReconstructionMode = pathReconstructionMode;
        }

        public PathBuildingAlgorithm getPathBuildingAlgorithm() {
            return this.pathBuildingAlgorithm;
        }

        public void setPathBuildingAlgorithm(PathBuildingAlgorithm pathBuildingAlgorithm) {
            this.pathBuildingAlgorithm = pathBuildingAlgorithm;
        }

        public void setMaxCallStackSize(int maxCallStackSize) {
            this.maxCallStackSize = maxCallStackSize;
        }

        public int getMaxCallStackSize() {
            return this.maxCallStackSize;
        }

        public int getMaxPathLength() {
            return this.maxPathLength;
        }

        public void setMaxPathLength(int maxPathLength) {
            this.maxPathLength = maxPathLength;
        }

        public int getMaxPathsPerAbstraction() {
            return this.maxPathsPerAbstraction;
        }

        public void setMaxPathsPerAbstraction(int maxPathsPerAbstraction) {
            this.maxPathsPerAbstraction = maxPathsPerAbstraction;
        }

        public long getPathReconstructionTimeout() {
            return this.pathReconstructionTimeout;
        }

        public void setPathReconstructionTimeout(long timeout) {
            this.pathReconstructionTimeout = timeout;
        }

        public int getPathReconstructionBatchSize() {
            return this.pathReconstructionBatchSize;
        }

        public void setPathReconstructionBatchSize(int pathReconstructionBatchSize) {
            this.pathReconstructionBatchSize = pathReconstructionBatchSize;
        }

        public boolean mustKeepStatements() {
            return this.pathReconstructionMode.reconstructPaths() || this.pathBuildingAlgorithm == PathBuildingAlgorithm.ContextSensitive;
        }

        public int hashCode() {
            int result = (31 * 1) + this.maxCallStackSize;
            return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + this.maxPathLength)) + this.maxPathsPerAbstraction)) + (this.pathBuildingAlgorithm == null ? 0 : this.pathBuildingAlgorithm.hashCode()))) + this.pathReconstructionBatchSize)) + (this.pathReconstructionMode == null ? 0 : this.pathReconstructionMode.hashCode()))) + ((int) (this.pathReconstructionTimeout ^ (this.pathReconstructionTimeout >>> 32))))) + (this.sequentialPathProcessing ? 1231 : 1237);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            PathConfiguration other = (PathConfiguration) obj;
            if (this.maxCallStackSize != other.maxCallStackSize || this.maxPathLength != other.maxPathLength || this.maxPathsPerAbstraction != other.maxPathsPerAbstraction || this.pathBuildingAlgorithm != other.pathBuildingAlgorithm || this.pathReconstructionBatchSize != other.pathReconstructionBatchSize || this.pathReconstructionMode != other.pathReconstructionMode || this.pathReconstructionTimeout != other.pathReconstructionTimeout || this.sequentialPathProcessing != other.sequentialPathProcessing) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$OutputConfiguration.class */
    public static class OutputConfiguration {
        private boolean noPassedValues = false;
        private boolean noCallGraphFraction = false;
        private int maxCallersInOutputFile = 5;
        private long resultSerializationTimeout = 0;

        public void merge(OutputConfiguration outputConfig) {
            this.noPassedValues = outputConfig.noPassedValues;
            this.noCallGraphFraction = outputConfig.noCallGraphFraction;
            this.maxCallersInOutputFile = outputConfig.maxCallersInOutputFile;
            this.resultSerializationTimeout = outputConfig.resultSerializationTimeout;
        }

        public boolean getNoPassedValues() {
            return this.noPassedValues;
        }

        public void setNoCallGraphFraction(boolean noCallGraphFraction) {
            this.noCallGraphFraction = noCallGraphFraction;
        }

        public boolean getNoCallGraphFraction() {
            return this.noCallGraphFraction;
        }

        public void setMaxCallersInOutputFile(int maxCallers) {
            this.maxCallersInOutputFile = maxCallers;
        }

        public int getMaxCallersInOutputFile() {
            return this.maxCallersInOutputFile;
        }

        public void setResultSerializationTimeout(long timeout) {
            this.resultSerializationTimeout = timeout;
        }

        public long getResultSerializationTimeout() {
            return this.resultSerializationTimeout;
        }

        public void setNoPassedValues(boolean noPassedValues) {
            this.noPassedValues = noPassedValues;
        }

        public int hashCode() {
            int result = (31 * 1) + this.maxCallersInOutputFile;
            return (31 * ((31 * ((31 * result) + (this.noCallGraphFraction ? 1231 : 1237))) + (this.noPassedValues ? 1231 : 1237))) + ((int) (this.resultSerializationTimeout ^ (this.resultSerializationTimeout >>> 32)));
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            OutputConfiguration other = (OutputConfiguration) obj;
            if (this.maxCallersInOutputFile != other.maxCallersInOutputFile || this.noCallGraphFraction != other.noCallGraphFraction || this.noPassedValues != other.noPassedValues || this.resultSerializationTimeout != other.resultSerializationTimeout) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$SolverConfiguration.class */
    public static class SolverConfiguration {
        private DataFlowSolver dataFlowSolver = DataFlowSolver.ContextFlowSensitive;
        private int maxJoinPointAbstractions = 10;
        private int maxCalleesPerCallSite = 75;
        private int maxAbstractionPathLength = 100;

        public void merge(SolverConfiguration solverConfig) {
            this.dataFlowSolver = solverConfig.dataFlowSolver;
            this.maxJoinPointAbstractions = solverConfig.maxJoinPointAbstractions;
            this.maxCalleesPerCallSite = solverConfig.maxCalleesPerCallSite;
            this.maxAbstractionPathLength = solverConfig.maxAbstractionPathLength;
        }

        public DataFlowSolver getDataFlowSolver() {
            return this.dataFlowSolver;
        }

        public void setDataFlowSolver(DataFlowSolver solver) {
            this.dataFlowSolver = solver;
        }

        public int getMaxJoinPointAbstractions() {
            return this.maxJoinPointAbstractions;
        }

        public void setMaxJoinPointAbstractions(int maxJoinPointAbstractions) {
            this.maxJoinPointAbstractions = maxJoinPointAbstractions;
        }

        public int getMaxCalleesPerCallSite() {
            return this.maxCalleesPerCallSite;
        }

        public void setMaxCalleesPerCallSite(int maxCalleesPerCallSite) {
            this.maxCalleesPerCallSite = maxCalleesPerCallSite;
        }

        public void setSingleJoinPointAbstraction(boolean singleJointAbstraction) {
            this.maxJoinPointAbstractions = singleJointAbstraction ? 1 : 10;
        }

        public int getMaxAbstractionPathLength() {
            return this.maxAbstractionPathLength;
        }

        public void setMaxAbstractionPathLength(int maxAbstractionPathLength) {
            this.maxAbstractionPathLength = maxAbstractionPathLength;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.dataFlowSolver == null ? 0 : this.dataFlowSolver.hashCode());
            return (31 * ((31 * ((31 * result) + this.maxCalleesPerCallSite)) + this.maxJoinPointAbstractions)) + this.maxAbstractionPathLength;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            SolverConfiguration other = (SolverConfiguration) obj;
            if (this.dataFlowSolver != other.dataFlowSolver || this.maxCalleesPerCallSite != other.maxCalleesPerCallSite || this.maxJoinPointAbstractions != other.maxJoinPointAbstractions || this.maxAbstractionPathLength != other.maxAbstractionPathLength) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowConfiguration$AccessPathConfiguration.class */
    public static class AccessPathConfiguration {
        private int accessPathLength = 5;
        private boolean useRecursiveAccessPaths = true;
        private boolean useThisChainReduction = true;
        private boolean useSameFieldReduction = true;

        public void merge(AccessPathConfiguration config) {
            this.accessPathLength = config.accessPathLength;
            this.useRecursiveAccessPaths = config.useRecursiveAccessPaths;
            this.useThisChainReduction = config.useThisChainReduction;
            this.useSameFieldReduction = config.useSameFieldReduction;
        }

        public int getAccessPathLength() {
            return this.accessPathLength;
        }

        public void setAccessPathLength(int accessPathLength) {
            this.accessPathLength = accessPathLength;
        }

        public boolean getUseRecursiveAccessPaths() {
            return this.useRecursiveAccessPaths;
        }

        public void setUseRecursiveAccessPaths(boolean useRecursiveAccessPaths) {
            this.useRecursiveAccessPaths = useRecursiveAccessPaths;
        }

        public boolean getUseThisChainReduction() {
            return this.useThisChainReduction;
        }

        public void setUseThisChainReduction(boolean useThisChainReduction) {
            this.useThisChainReduction = useThisChainReduction;
        }

        public boolean getUseSameFieldReduction() {
            return this.useSameFieldReduction;
        }

        public void setUseSameFieldReduction(boolean useSameFieldReduction) {
            this.useSameFieldReduction = useSameFieldReduction;
        }

        public int hashCode() {
            int result = (31 * 1) + this.accessPathLength;
            return (31 * ((31 * ((31 * result) + (this.useRecursiveAccessPaths ? 1231 : 1237))) + (this.useSameFieldReduction ? 1231 : 1237))) + (this.useThisChainReduction ? 1231 : 1237);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            AccessPathConfiguration other = (AccessPathConfiguration) obj;
            if (this.accessPathLength != other.accessPathLength || this.useRecursiveAccessPaths != other.useRecursiveAccessPaths || this.useSameFieldReduction != other.useSameFieldReduction || this.useThisChainReduction != other.useThisChainReduction) {
                return false;
            }
            return true;
        }
    }

    public void merge(InfoflowConfiguration config) {
        this.stopAfterFirstKFlows = config.stopAfterFirstKFlows;
        this.implicitFlowMode = config.implicitFlowMode;
        this.enableExceptions = config.enableExceptions;
        this.enableArrays = config.enableArrays;
        this.enableArraySizeTainting = config.enableArraySizeTainting;
        this.flowSensitiveAliasing = config.flowSensitiveAliasing;
        this.enableTypeChecking = config.enableTypeChecking;
        this.ignoreFlowsInSystemPackages = config.ignoreFlowsInSystemPackages;
        this.excludeSootLibraryClasses = config.excludeSootLibraryClasses;
        this.maxThreadNum = config.maxThreadNum;
        this.writeOutputFiles = config.writeOutputFiles;
        this.logSourcesAndSinks = config.logSourcesAndSinks;
        this.enableReflection = config.enableReflection;
        this.enableLineNumbers = config.enableLineNumbers;
        this.enableOriginalNames = config.enableOriginalNames;
        this.pathConfiguration.merge(config.pathConfiguration);
        this.outputConfiguration.merge(config.outputConfiguration);
        this.solverConfiguration.merge(config.solverConfiguration);
        this.accessPathConfiguration.merge(config.accessPathConfiguration);
        this.callgraphAlgorithm = config.callgraphAlgorithm;
        this.aliasingAlgorithm = config.aliasingAlgorithm;
        this.codeEliminationMode = config.codeEliminationMode;
        this.staticFieldTrackingMode = config.staticFieldTrackingMode;
        this.sootIntegrationMode = config.sootIntegrationMode;
        this.dataFlowDirection = config.dataFlowDirection;
        this.inspectSources = config.inspectSources;
        this.inspectSinks = config.inspectSinks;
        this.taintAnalysisEnabled = config.writeOutputFiles;
        this.incrementalResultReporting = config.incrementalResultReporting;
        this.dataFlowTimeout = config.dataFlowTimeout;
        this.memoryThreshold = config.memoryThreshold;
        this.oneSourceAtATime = config.oneSourceAtATime;
        baseDirectory = baseDirectory;
    }

    public void setPathAgnosticResults(boolean pathAgnosticResults) {
        this.pathAgnosticResults = pathAgnosticResults;
    }

    public boolean getPathAgnosticResults() {
        return this.pathAgnosticResults;
    }

    public static boolean getOneResultPerAccessPath() {
        return oneResultPerAccessPath;
    }

    public static void setOneResultPerAccessPath(boolean oneResultPerAP) {
        oneResultPerAccessPath = oneResultPerAP;
    }

    public static boolean getMergeNeighbors() {
        return mergeNeighbors;
    }

    public static void setMergeNeighbors(boolean value) {
        mergeNeighbors = value;
    }

    public void setStopAfterFirstKFlows(int stopAfterFirstKFlows) {
        this.stopAfterFirstKFlows = stopAfterFirstKFlows;
    }

    public int getStopAfterFirstKFlows() {
        return this.stopAfterFirstKFlows;
    }

    public void setStopAfterFirstFlow(boolean stopAfterFirstFlow) {
        this.stopAfterFirstKFlows = stopAfterFirstFlow ? 1 : 0;
    }

    public boolean getStopAfterFirstFlow() {
        return this.stopAfterFirstKFlows == 1;
    }

    public void setInspectSources(boolean inspect) {
        this.inspectSources = inspect;
    }

    public boolean getInspectSources() {
        return this.inspectSources;
    }

    public void setInspectSinks(boolean inspect) {
        this.inspectSinks = inspect;
    }

    public boolean getInspectSinks() {
        return this.inspectSinks;
    }

    public void setImplicitFlowMode(ImplicitFlowMode implicitFlowMode) {
        this.implicitFlowMode = implicitFlowMode;
    }

    public ImplicitFlowMode getImplicitFlowMode() {
        return this.implicitFlowMode;
    }

    public void setStaticFieldTrackingMode(StaticFieldTrackingMode staticFieldTrackingMode) {
        this.staticFieldTrackingMode = staticFieldTrackingMode;
    }

    public StaticFieldTrackingMode getStaticFieldTrackingMode() {
        return this.staticFieldTrackingMode;
    }

    public void setSootIntegrationMode(SootIntegrationMode sootIntegrationMode) {
        this.sootIntegrationMode = sootIntegrationMode;
    }

    public SootIntegrationMode getSootIntegrationMode() {
        return this.sootIntegrationMode;
    }

    public void setFlowSensitiveAliasing(boolean flowSensitiveAliasing) {
        this.flowSensitiveAliasing = flowSensitiveAliasing;
    }

    public boolean getFlowSensitiveAliasing() {
        return this.flowSensitiveAliasing;
    }

    public void setEnableExceptionTracking(boolean enableExceptions) {
        this.enableExceptions = enableExceptions;
    }

    public boolean getEnableExceptionTracking() {
        return this.enableExceptions;
    }

    public void setEnableArrayTracking(boolean enableArrays) {
        this.enableArrays = enableArrays;
    }

    public boolean getEnableArrayTracking() {
        return this.enableArrays;
    }

    public void setEnableArraySizeTainting(boolean arrayLengthTainting) {
        this.enableArraySizeTainting = arrayLengthTainting;
    }

    public boolean getEnableArraySizeTainting() {
        return this.enableArraySizeTainting;
    }

    public void setCallgraphAlgorithm(CallgraphAlgorithm algorithm) {
        this.callgraphAlgorithm = algorithm;
    }

    public CallgraphAlgorithm getCallgraphAlgorithm() {
        return this.callgraphAlgorithm;
    }

    public void setAliasingAlgorithm(AliasingAlgorithm algorithm) {
        this.aliasingAlgorithm = algorithm;
    }

    public AliasingAlgorithm getAliasingAlgorithm() {
        return this.aliasingAlgorithm;
    }

    public DataFlowDirection getDataFlowDirection() {
        return this.dataFlowDirection;
    }

    public void setDataFlowDirection(DataFlowDirection direction) {
        this.dataFlowDirection = direction;
    }

    public void setEnableTypeChecking(boolean enableTypeChecking) {
        this.enableTypeChecking = enableTypeChecking;
    }

    public boolean getEnableTypeChecking() {
        return this.enableTypeChecking;
    }

    public void setIgnoreFlowsInSystemPackages(boolean ignoreFlowsInSystemPackages) {
        this.ignoreFlowsInSystemPackages = ignoreFlowsInSystemPackages;
    }

    public boolean getIgnoreFlowsInSystemPackages() {
        return this.ignoreFlowsInSystemPackages;
    }

    public void setExcludeSootLibraryClasses(boolean excludeSootLibraryClasses) {
        this.excludeSootLibraryClasses = excludeSootLibraryClasses;
    }

    public boolean getExcludeSootLibraryClasses() {
        return this.excludeSootLibraryClasses;
    }

    public void setMaxThreadNum(int threadNum) {
        this.maxThreadNum = threadNum;
    }

    public int getMaxThreadNum() {
        return this.maxThreadNum;
    }

    public boolean getWriteOutputFiles() {
        return this.writeOutputFiles;
    }

    public void setWriteOutputFiles(boolean writeOutputFiles) {
        this.writeOutputFiles = writeOutputFiles;
    }

    public void setCodeEliminationMode(CodeEliminationMode mode) {
        this.codeEliminationMode = mode;
    }

    public CodeEliminationMode getCodeEliminationMode() {
        return this.codeEliminationMode;
    }

    public boolean getLogSourcesAndSinks() {
        return this.logSourcesAndSinks;
    }

    public void setLogSourcesAndSinks(boolean logSourcesAndSinks) {
        this.logSourcesAndSinks = logSourcesAndSinks;
    }

    public boolean getEnableReflection() {
        return this.enableReflection;
    }

    public void setEnableReflection(boolean enableReflections) {
        this.enableReflection = enableReflections;
    }

    public boolean getEnableLineNumbers() {
        return this.enableLineNumbers;
    }

    public void setEnableLineNumbers(boolean enableLineNumbers) {
        this.enableLineNumbers = enableLineNumbers;
    }

    public boolean getEnableOriginalNames() {
        return this.enableOriginalNames;
    }

    public void setEnableOriginalNames(boolean enableOriginalNames) {
        this.enableOriginalNames = enableOriginalNames;
    }

    public boolean isTaintAnalysisEnabled() {
        return this.taintAnalysisEnabled;
    }

    public void setTaintAnalysisEnabled(boolean taintAnalysisEnabled) {
        this.taintAnalysisEnabled = taintAnalysisEnabled;
    }

    public boolean getIncrementalResultReporting() {
        return this.incrementalResultReporting;
    }

    public void setIncrementalResultReporting(boolean incrementalReporting) {
        this.incrementalResultReporting = incrementalReporting;
    }

    public long getDataFlowTimeout() {
        return this.dataFlowTimeout;
    }

    public void setDataFlowTimeout(long timeout) {
        this.dataFlowTimeout = timeout;
    }

    public double getMemoryThreshold() {
        return this.memoryThreshold;
    }

    public void setMemoryThreshold(double memoryThreshold) {
        this.memoryThreshold = memoryThreshold;
    }

    public boolean getOneSourceAtATime() {
        return this.oneSourceAtATime;
    }

    public void setOneSourceAtATime(boolean oneSourceAtATime) {
        this.oneSourceAtATime = oneSourceAtATime;
    }

    public PathConfiguration getPathConfiguration() {
        return this.pathConfiguration;
    }

    public OutputConfiguration getOutputConfiguration() {
        return this.outputConfiguration;
    }

    public SolverConfiguration getSolverConfiguration() {
        return this.solverConfiguration;
    }

    public AccessPathConfiguration getAccessPathConfiguration() {
        return this.accessPathConfiguration;
    }

    public static String getBaseDirectory() {
        return baseDirectory;
    }

    public static void setBaseDirectory(String baseDirectory2) {
        baseDirectory = baseDirectory2;
    }

    public void printSummary() {
        if (this.staticFieldTrackingMode == StaticFieldTrackingMode.None) {
            logger.warn("Static field tracking is disabled, results may be incomplete");
        }
        if (!this.flowSensitiveAliasing) {
            logger.warn("Using flow-insensitive alias tracking, results may be imprecise");
        }
        switch ($SWITCH_TABLE$soot$jimple$infoflow$InfoflowConfiguration$ImplicitFlowMode()[this.implicitFlowMode.ordinal()]) {
            case 1:
                logger.info("Implicit flow tracking is NOT enabled");
                break;
            case 2:
                logger.info("Tracking of implicit array accesses is enabled");
                break;
            case 3:
                logger.info("Implicit flow tracking is enabled");
                break;
        }
        if (this.enableExceptions) {
            logger.info("Exceptional flow tracking is enabled");
        } else {
            logger.info("Exceptional flow tracking is NOT enabled");
        }
        logger.info("Running with a maximum access path length of {}", Integer.valueOf(this.accessPathConfiguration.getAccessPathLength()));
        if (this.pathAgnosticResults) {
            logger.info("Using path-agnostic result collection");
        } else {
            logger.info("Using path-sensitive result collection");
        }
        if (this.accessPathConfiguration.useRecursiveAccessPaths) {
            logger.info("Recursive access path shortening is enabled");
        } else {
            logger.info("Recursive access path shortening is NOT enabled");
        }
        logger.info("Taint analysis enabled: " + this.taintAnalysisEnabled);
        if (this.oneSourceAtATime) {
            logger.info("Running with one source at a time");
        }
        logger.info("Using alias algorithm " + this.aliasingAlgorithm);
    }

    public int hashCode() {
        int result = (31 * 1) + (this.accessPathConfiguration == null ? 0 : this.accessPathConfiguration.hashCode());
        int result2 = (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this.aliasingAlgorithm == null ? 0 : this.aliasingAlgorithm.hashCode()))) + (this.callgraphAlgorithm == null ? 0 : this.callgraphAlgorithm.hashCode()))) + (this.codeEliminationMode == null ? 0 : this.codeEliminationMode.hashCode()))) + (this.dataFlowDirection == null ? 0 : this.dataFlowDirection.hashCode()))) + ((int) (this.dataFlowTimeout ^ (this.dataFlowTimeout >>> 32))))) + (this.enableArraySizeTainting ? 1231 : 1237))) + (this.enableArrays ? 1231 : 1237))) + (this.enableExceptions ? 1231 : 1237))) + (this.enableReflection ? 1231 : 1237))) + (this.enableLineNumbers ? 1231 : 1237))) + (this.enableOriginalNames ? 1231 : 1237))) + (this.enableTypeChecking ? 1231 : 1237))) + (this.excludeSootLibraryClasses ? 1231 : 1237))) + (this.flowSensitiveAliasing ? 1231 : 1237))) + (this.ignoreFlowsInSystemPackages ? 1231 : 1237))) + (this.implicitFlowMode == null ? 0 : this.implicitFlowMode.hashCode()))) + (this.incrementalResultReporting ? 1231 : 1237))) + (this.inspectSinks ? 1231 : 1237))) + (this.inspectSources ? 1231 : 1237))) + (this.logSourcesAndSinks ? 1231 : 1237))) + this.maxThreadNum;
        long temp = Double.doubleToLongBits(this.memoryThreshold);
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result2) + ((int) (temp ^ (temp >>> 32))))) + (this.oneSourceAtATime ? 1231 : 1237))) + (this.outputConfiguration == null ? 0 : this.outputConfiguration.hashCode()))) + (this.pathConfiguration == null ? 0 : this.pathConfiguration.hashCode()))) + (this.solverConfiguration == null ? 0 : this.solverConfiguration.hashCode()))) + (this.staticFieldTrackingMode == null ? 0 : this.staticFieldTrackingMode.hashCode()))) + (this.sootIntegrationMode == null ? 0 : this.sootIntegrationMode.hashCode()))) + this.stopAfterFirstKFlows)) + (this.taintAnalysisEnabled ? 1231 : 1237))) + (this.writeOutputFiles ? 1231 : 1237);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        InfoflowConfiguration other = (InfoflowConfiguration) obj;
        if (this.accessPathConfiguration == null) {
            if (other.accessPathConfiguration != null) {
                return false;
            }
        } else if (!this.accessPathConfiguration.equals(other.accessPathConfiguration)) {
            return false;
        }
        if (this.aliasingAlgorithm != other.aliasingAlgorithm || this.callgraphAlgorithm != other.callgraphAlgorithm || this.codeEliminationMode != other.codeEliminationMode || this.dataFlowDirection != other.dataFlowDirection || this.dataFlowTimeout != other.dataFlowTimeout || this.enableArraySizeTainting != other.enableArraySizeTainting || this.enableArrays != other.enableArrays || this.enableExceptions != other.enableExceptions || this.enableReflection != other.enableReflection || this.enableLineNumbers != other.enableLineNumbers || this.enableOriginalNames != other.enableOriginalNames || this.enableTypeChecking != other.enableTypeChecking || this.excludeSootLibraryClasses != other.excludeSootLibraryClasses || this.flowSensitiveAliasing != other.flowSensitiveAliasing || this.ignoreFlowsInSystemPackages != other.ignoreFlowsInSystemPackages || this.implicitFlowMode != other.implicitFlowMode || this.incrementalResultReporting != other.incrementalResultReporting || this.inspectSinks != other.inspectSinks || this.inspectSources != other.inspectSources || this.logSourcesAndSinks != other.logSourcesAndSinks || this.maxThreadNum != other.maxThreadNum || Double.doubleToLongBits(this.memoryThreshold) != Double.doubleToLongBits(other.memoryThreshold) || this.oneSourceAtATime != other.oneSourceAtATime) {
            return false;
        }
        if (this.outputConfiguration == null) {
            if (other.outputConfiguration != null) {
                return false;
            }
        } else if (!this.outputConfiguration.equals(other.outputConfiguration)) {
            return false;
        }
        if (this.pathConfiguration == null) {
            if (other.pathConfiguration != null) {
                return false;
            }
        } else if (!this.pathConfiguration.equals(other.pathConfiguration)) {
            return false;
        }
        if (this.solverConfiguration == null) {
            if (other.solverConfiguration != null) {
                return false;
            }
        } else if (!this.solverConfiguration.equals(other.solverConfiguration)) {
            return false;
        }
        if (this.staticFieldTrackingMode != other.staticFieldTrackingMode || this.sootIntegrationMode != other.sootIntegrationMode || this.stopAfterFirstKFlows != other.stopAfterFirstKFlows || this.taintAnalysisEnabled != other.taintAnalysisEnabled || this.writeOutputFiles != other.writeOutputFiles) {
            return false;
        }
        return true;
    }

    public SourceSinkConfiguration getSourceSinkConfig() {
        return this.sourceSinkConfig;
    }

    public int getMaxAliasingBases() {
        return this.maxAliasingBases;
    }

    public void setMaxAliasingBases(int value) {
        this.maxAliasingBases = value;
    }
}
