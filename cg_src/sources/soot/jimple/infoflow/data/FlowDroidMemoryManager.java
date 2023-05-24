package soot.jimple.infoflow.data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Unit;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.infoflow.solver.memory.IMemoryManager;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/FlowDroidMemoryManager.class */
public class FlowDroidMemoryManager implements IMemoryManager<Abstraction, Unit> {
    private final Logger logger;
    private ConcurrentMap<AccessPath, AccessPath> apCache;
    private ConcurrentHashMap<AbstractionCacheKey, Abstraction> absCache;
    private AtomicInteger reuseCounter;
    private final boolean tracingEnabled;
    private final PathDataErasureMode erasePathData;
    private boolean useAbstractionCache;

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/FlowDroidMemoryManager$PathDataErasureMode.class */
    public enum PathDataErasureMode {
        EraseNothing,
        KeepOnlyContextData,
        EraseAll;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static PathDataErasureMode[] valuesCustom() {
            PathDataErasureMode[] valuesCustom = values();
            int length = valuesCustom.length;
            PathDataErasureMode[] pathDataErasureModeArr = new PathDataErasureMode[length];
            System.arraycopy(valuesCustom, 0, pathDataErasureModeArr, 0, length);
            return pathDataErasureModeArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/FlowDroidMemoryManager$AbstractionCacheKey.class */
    public static class AbstractionCacheKey {
        private final Abstraction abs;

        public AbstractionCacheKey(Abstraction abs) {
            this.abs = abs;
        }

        public int hashCode() {
            int result = 31 * this.abs.hashCode();
            return (31 * ((31 * ((31 * result) + (this.abs.getPredecessor() == null ? 0 : this.abs.getPredecessor().hashCode()))) + (this.abs.getCurrentStmt() == null ? 0 : this.abs.getCurrentStmt().hashCode()))) + (this.abs.getCorrespondingCallSite() == null ? 0 : this.abs.getCorrespondingCallSite().hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            AbstractionCacheKey other = (AbstractionCacheKey) obj;
            if (!this.abs.equals(other.abs) || this.abs.getPredecessor() != other.abs.getPredecessor() || this.abs.getCurrentStmt() != other.abs.getCurrentStmt() || this.abs.getCorrespondingCallSite() != other.abs.getCorrespondingCallSite()) {
                return false;
            }
            return true;
        }
    }

    public FlowDroidMemoryManager() {
        this(false, PathDataErasureMode.EraseNothing);
    }

    public FlowDroidMemoryManager(boolean tracingEnabled, PathDataErasureMode erasePathData) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.apCache = new ConcurrentHashMap();
        this.absCache = new ConcurrentHashMap<>();
        this.reuseCounter = new AtomicInteger();
        this.useAbstractionCache = false;
        this.tracingEnabled = tracingEnabled;
        this.erasePathData = erasePathData;
        this.logger.info("Initializing FlowDroid memory manager...");
        if (this.tracingEnabled) {
            this.logger.info("FDMM: Tracing enabled. This may negatively affect performance.");
        }
        if (this.erasePathData != PathDataErasureMode.EraseNothing) {
            this.logger.info("FDMM: Path data erasure enabled");
        }
    }

    private AccessPath getCachedAccessPath(AccessPath ap) {
        AccessPath oldAP = this.apCache.putIfAbsent(ap, ap);
        if (oldAP == null) {
            return ap;
        }
        if (this.tracingEnabled && oldAP != ap) {
            this.reuseCounter.incrementAndGet();
        }
        return oldAP;
    }

    private Abstraction getCachedAbstraction(Abstraction abs) {
        Abstraction oldAbs = this.absCache.putIfAbsent(new AbstractionCacheKey(abs), abs);
        if (oldAbs != null && oldAbs != abs && this.tracingEnabled) {
            this.reuseCounter.incrementAndGet();
        }
        return oldAbs;
    }

    public int getReuseCount() {
        return this.reuseCounter.get();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.jimple.infoflow.solver.memory.IMemoryManager
    public Abstraction handleMemoryObject(Abstraction obj) {
        return obj;
    }

    @Override // soot.jimple.infoflow.solver.memory.IMemoryManager
    public Abstraction handleGeneratedMemoryObject(Abstraction input, Abstraction output) {
        Abstraction cachedAbs;
        if (input == output) {
            return output;
        }
        if (input.equals(output) && (output.getCurrentStmt() == null || input.getCurrentStmt() == output.getCurrentStmt())) {
            return input;
        }
        AccessPath newAP = getCachedAccessPath(output.getAccessPath());
        output.setAccessPath(newAP);
        if (this.erasePathData != PathDataErasureMode.EraseNothing) {
            Abstraction predecessor = output.getPredecessor();
            while (true) {
                Abstraction curAbs = predecessor;
                if (curAbs == null || curAbs.getNeighbors() != null) {
                    break;
                }
                Abstraction predPred = curAbs.getPredecessor();
                if (predPred != null && predPred.equals(output)) {
                    output = predPred;
                }
                predecessor = predPred;
            }
        }
        erasePathData(output);
        if (this.useAbstractionCache && (cachedAbs = getCachedAbstraction(output)) != null) {
            return cachedAbs;
        }
        return output;
    }

    protected void erasePathData(Abstraction output) {
        if (this.erasePathData != PathDataErasureMode.EraseNothing) {
            if (this.erasePathData == PathDataErasureMode.EraseAll) {
                output.setCurrentStmt(null);
                output.setCorrespondingCallSite(null);
            } else if (this.erasePathData == PathDataErasureMode.KeepOnlyContextData && output.getCorrespondingCallSite() == output.getCurrentStmt()) {
                output.setCurrentStmt(null);
                output.setCorrespondingCallSite(null);
            } else if (this.erasePathData == PathDataErasureMode.KeepOnlyContextData && output.getCorrespondingCallSite() == null && output.getCurrentStmt() != null && output.getCorrespondingCallSite() == null && output.getCurrentStmt() != null && !output.getCurrentStmt().containsInvokeExpr() && !(output.getCurrentStmt() instanceof ReturnStmt) && !(output.getCurrentStmt() instanceof ReturnVoidStmt)) {
                output.setCurrentStmt(null);
                output.setCorrespondingCallSite(null);
            }
        }
    }

    public void setUseAbstractionCache(boolean useAbstractionCache) {
        this.useAbstractionCache = useAbstractionCache;
    }

    @Override // soot.jimple.infoflow.solver.memory.IMemoryManager
    public boolean isEssentialJoinPoint(Abstraction abs, Unit relatedCallSite) {
        return (relatedCallSite == null || this.erasePathData == PathDataErasureMode.EraseAll) ? false : true;
    }
}
