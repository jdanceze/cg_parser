package soot.jimple.infoflow;

import soot.FastHierarchy;
import soot.Scene;
import soot.jimple.infoflow.aliasing.Aliasing;
import soot.jimple.infoflow.data.AccessPathFactory;
import soot.jimple.infoflow.globalTaints.GlobalTaintManager;
import soot.jimple.infoflow.memory.IMemoryBoundedSolver;
import soot.jimple.infoflow.solver.IInfoflowSolver;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager;
import soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper;
import soot.jimple.infoflow.typing.TypeUtils;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/InfoflowManager.class */
public class InfoflowManager {
    private final InfoflowConfiguration config;
    private IInfoflowSolver forwardSolver;
    private IInfoflowSolver backwardSolver;
    private final IInfoflowCFG icfg;
    private final IInfoflowCFG originalIcfg;
    private final ISourceSinkManager sourceSinkManager;
    private final ITaintPropagationWrapper taintWrapper;
    private final TypeUtils typeUtils;
    private final FastHierarchy hierarchy;
    private final AccessPathFactory accessPathFactory;
    private final GlobalTaintManager globalTaintManager;
    private Aliasing aliasing;

    protected InfoflowManager(InfoflowConfiguration config) {
        this.config = config;
        this.forwardSolver = null;
        this.icfg = null;
        this.originalIcfg = null;
        this.sourceSinkManager = null;
        this.taintWrapper = null;
        this.typeUtils = null;
        this.hierarchy = null;
        this.accessPathFactory = null;
        this.globalTaintManager = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public InfoflowManager(InfoflowConfiguration config, IInfoflowSolver forwardSolver, IInfoflowCFG icfg, ISourceSinkManager sourceSinkManager, ITaintPropagationWrapper taintWrapper, FastHierarchy hierarchy, GlobalTaintManager globalTaintManager) {
        this.config = config;
        this.forwardSolver = forwardSolver;
        this.icfg = icfg;
        this.originalIcfg = null;
        this.sourceSinkManager = sourceSinkManager;
        this.taintWrapper = taintWrapper;
        this.typeUtils = new TypeUtils(this);
        this.hierarchy = hierarchy;
        this.accessPathFactory = new AccessPathFactory(config, this.typeUtils);
        this.globalTaintManager = globalTaintManager;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public InfoflowManager(InfoflowConfiguration config, IInfoflowSolver forwardSolver, IInfoflowCFG icfg, ISourceSinkManager sourceSinkManager, ITaintPropagationWrapper taintWrapper, FastHierarchy hierarchy, InfoflowManager existingManager) {
        this.config = config;
        this.forwardSolver = forwardSolver;
        this.icfg = icfg;
        this.originalIcfg = existingManager.getICFG();
        this.sourceSinkManager = sourceSinkManager;
        this.taintWrapper = taintWrapper;
        this.typeUtils = existingManager.getTypeUtils();
        this.hierarchy = hierarchy;
        this.accessPathFactory = existingManager.getAccessPathFactory();
        this.globalTaintManager = existingManager.getGlobalTaintManager();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public InfoflowManager(InfoflowConfiguration config, IInfoflowSolver forwardSolver, IInfoflowCFG icfg) {
        this.config = config;
        this.forwardSolver = forwardSolver;
        this.icfg = icfg;
        this.originalIcfg = null;
        this.sourceSinkManager = null;
        this.taintWrapper = null;
        this.typeUtils = new TypeUtils(this);
        this.hierarchy = Scene.v().getOrMakeFastHierarchy();
        this.accessPathFactory = new AccessPathFactory(config, this.typeUtils);
        this.globalTaintManager = null;
    }

    public InfoflowConfiguration getConfig() {
        return this.config;
    }

    public void setForwardSolver(IInfoflowSolver solver) {
        this.forwardSolver = solver;
    }

    public IInfoflowSolver getForwardSolver() {
        return this.forwardSolver;
    }

    public IInfoflowSolver getBackwardSolver() {
        return this.backwardSolver;
    }

    public void setBackwardSolver(IInfoflowSolver solver) {
        this.backwardSolver = solver;
    }

    public IInfoflowCFG getICFG() {
        return this.icfg;
    }

    public IInfoflowCFG getOriginalICFG() {
        return this.originalIcfg;
    }

    public ISourceSinkManager getSourceSinkManager() {
        return this.sourceSinkManager;
    }

    public ITaintPropagationWrapper getTaintWrapper() {
        return this.taintWrapper;
    }

    public TypeUtils getTypeUtils() {
        return this.typeUtils;
    }

    public FastHierarchy getHierarchy() {
        return this.hierarchy;
    }

    public AccessPathFactory getAccessPathFactory() {
        return this.accessPathFactory;
    }

    public boolean isAnalysisAborted() {
        if (this.forwardSolver instanceof IMemoryBoundedSolver) {
            return ((IMemoryBoundedSolver) this.forwardSolver).isKilled();
        }
        return false;
    }

    public void cleanup() {
        this.forwardSolver = null;
        this.aliasing = null;
    }

    public void setAliasing(Aliasing aliasing) {
        this.aliasing = aliasing;
    }

    public Aliasing getAliasing() {
        return this.aliasing;
    }

    public GlobalTaintManager getGlobalTaintManager() {
        return this.globalTaintManager;
    }
}
