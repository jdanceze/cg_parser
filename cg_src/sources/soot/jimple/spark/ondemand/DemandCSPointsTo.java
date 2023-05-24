package soot.jimple.spark.ondemand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.AnySubType;
import soot.ArrayType;
import soot.Context;
import soot.Local;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.RefType;
import soot.Scene;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.coffi.Instruction;
import soot.jimple.spark.ondemand.genericutil.ArraySet;
import soot.jimple.spark.ondemand.genericutil.HashSetMultiMap;
import soot.jimple.spark.ondemand.genericutil.ImmutableStack;
import soot.jimple.spark.ondemand.genericutil.Predicate;
import soot.jimple.spark.ondemand.genericutil.Propagator;
import soot.jimple.spark.ondemand.genericutil.Stack;
import soot.jimple.spark.ondemand.pautil.AssignEdge;
import soot.jimple.spark.ondemand.pautil.ContextSensitiveInfo;
import soot.jimple.spark.ondemand.pautil.OTFMethodSCCManager;
import soot.jimple.spark.ondemand.pautil.SootUtil;
import soot.jimple.spark.ondemand.pautil.ValidMatches;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.GlobalVarNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.EmptyPointsToSet;
import soot.jimple.spark.sets.EqualsSupportingPointsToSet;
import soot.jimple.spark.sets.HybridPointsToSet;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetEqualsWrapper;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.VirtualCalls;
import soot.toolkits.scalar.Pair;
import soot.util.dot.DotGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/DemandCSPointsTo.class */
public final class DemandCSPointsTo implements PointsToAnalysis {
    private static final Logger logger;
    public static boolean DEBUG;
    protected static final int DEBUG_NESTING = 15;
    protected static final int DEBUG_PASS = -1;
    protected static final boolean DEBUG_VIRT;
    protected static final int DEFAULT_MAX_PASSES = 10;
    protected static final int DEFAULT_MAX_TRAVERSAL = 75000;
    protected static final boolean DEFAULT_LAZY = true;
    private boolean refineCallGraph;
    protected static final ImmutableStack<Integer> EMPTY_CALLSTACK;
    protected final AllocAndContextCache allocAndContextCache;
    protected Stack<Pair<Integer, ImmutableStack<Integer>>> callGraphStack;
    protected final CallSiteToTargetsMap callSiteToResolvedTargets;
    protected HashMap<List<Object>, Set<SootMethod>> callTargetsArgCache;
    protected final Stack<VarAndContext> contextForAllocsStack;
    protected Map<VarAndContext, Pair<PointsToSetInternal, AllocAndContextSet>> contextsForAllocsCache;
    protected final ContextSensitiveInfo csInfo;
    protected boolean doPointsTo;
    protected FieldCheckHeuristic fieldCheckHeuristic;
    protected HeuristicType heuristicType;
    protected SootUtil.FieldToEdgesMap fieldToLoads;
    protected SootUtil.FieldToEdgesMap fieldToStores;
    protected final int maxNodesPerPass;
    protected final int maxPasses;
    protected int nesting;
    protected int numNodesTraversed;
    protected int numPasses;
    protected final PAG pag;
    protected AllocAndContextSet pointsTo;
    protected final Set<CallSiteAndContext> queriedCallSites;
    protected int recursionDepth;
    protected boolean refiningCallSite;
    protected OTFMethodSCCManager sccManager;
    protected Map<VarContextAndUp, Map<AllocAndContext, CallingContextSet>> upContextCache;
    protected ValidMatches vMatches;
    protected Map<Local, PointsToSet> reachingObjectsCache;
    protected Map<Local, PointsToSet> reachingObjectsCacheNoCGRefinement;
    protected boolean useCache;
    private final boolean lazy;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DemandCSPointsTo.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(DemandCSPointsTo.class);
        DEBUG = false;
        DEBUG_VIRT = DEBUG;
        EMPTY_CALLSTACK = ImmutableStack.emptyStack();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/DemandCSPointsTo$AllocAndContextCache.class */
    public static final class AllocAndContextCache extends HashMap<AllocAndContext, Map<VarNode, CallingContextSet>> {
        protected AllocAndContextCache() {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/DemandCSPointsTo$CallingContextSet.class */
    public static final class CallingContextSet extends ArraySet<ImmutableStack<Integer>> {
        protected CallingContextSet() {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/DemandCSPointsTo$CallSiteAndContext.class */
    public static final class CallSiteAndContext extends Pair<Integer, ImmutableStack<Integer>> {
        public CallSiteAndContext(Integer callSite, ImmutableStack<Integer> callingContext) {
            super(callSite, callingContext);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/DemandCSPointsTo$CallSiteToTargetsMap.class */
    public static final class CallSiteToTargetsMap extends HashSetMultiMap<CallSiteAndContext, SootMethod> {
        protected CallSiteToTargetsMap() {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/DemandCSPointsTo$IncomingEdgeHandler.class */
    public static abstract class IncomingEdgeHandler {
        public abstract void handleAlloc(AllocNode allocNode, VarAndContext varAndContext);

        public abstract void handleMatchSrc(VarNode varNode, PointsToSetInternal pointsToSetInternal, VarNode varNode2, VarNode varNode3, VarAndContext varAndContext, SparkField sparkField, boolean z);

        abstract Object getResult();

        abstract void handleAssignSrc(VarAndContext varAndContext, VarAndContext varAndContext2, AssignEdge assignEdge);

        abstract boolean shouldHandleSrc(VarNode varNode);

        protected IncomingEdgeHandler() {
        }

        boolean terminate() {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/DemandCSPointsTo$VarAndContext.class */
    public static class VarAndContext {
        final ImmutableStack<Integer> context;
        final VarNode var;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !DemandCSPointsTo.class.desiredAssertionStatus();
        }

        public VarAndContext(VarNode var, ImmutableStack<Integer> context) {
            if (!$assertionsDisabled && var == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && context == null) {
                throw new AssertionError();
            }
            this.var = var;
            this.context = context;
        }

        public boolean equals(Object o) {
            if (o != null && o.getClass() == VarAndContext.class) {
                VarAndContext other = (VarAndContext) o;
                return this.var.equals(other.var) && this.context.equals(other.context);
            }
            return false;
        }

        public int hashCode() {
            return this.var.hashCode() + this.context.hashCode();
        }

        public String toString() {
            return this.var + Instruction.argsep + this.context;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/DemandCSPointsTo$VarContextAndUp.class */
    public static final class VarContextAndUp extends VarAndContext {
        final ImmutableStack<Integer> upContext;

        public VarContextAndUp(VarNode var, ImmutableStack<Integer> context, ImmutableStack<Integer> upContext) {
            super(var, context);
            this.upContext = upContext;
        }

        @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.VarAndContext
        public boolean equals(Object o) {
            if (o != null && o.getClass() == VarContextAndUp.class) {
                VarContextAndUp other = (VarContextAndUp) o;
                return this.var.equals(other.var) && this.context.equals(other.context) && this.upContext.equals(other.upContext);
            }
            return false;
        }

        @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.VarAndContext
        public int hashCode() {
            return this.var.hashCode() + this.context.hashCode() + this.upContext.hashCode();
        }

        @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.VarAndContext
        public String toString() {
            return this.var + Instruction.argsep + this.context + " up " + this.upContext;
        }
    }

    public static DemandCSPointsTo makeDefault() {
        return makeWithBudget(DEFAULT_MAX_TRAVERSAL, 10, true);
    }

    public static DemandCSPointsTo makeWithBudget(int maxTraversal, int maxPasses, boolean lazy) {
        PAG pag = (PAG) Scene.v().getPointsToAnalysis();
        ContextSensitiveInfo csInfo = new ContextSensitiveInfo(pag);
        return new DemandCSPointsTo(csInfo, pag, maxTraversal, maxPasses, lazy);
    }

    public DemandCSPointsTo(ContextSensitiveInfo csInfo, PAG pag) {
        this(csInfo, pag, DEFAULT_MAX_TRAVERSAL, 10, true);
    }

    public DemandCSPointsTo(ContextSensitiveInfo csInfo, PAG pag, int maxTraversal, int maxPasses, boolean lazy) {
        this.refineCallGraph = true;
        this.allocAndContextCache = new AllocAndContextCache();
        this.callGraphStack = new Stack<>();
        this.callSiteToResolvedTargets = new CallSiteToTargetsMap();
        this.callTargetsArgCache = new HashMap<>();
        this.contextForAllocsStack = new Stack<>();
        this.contextsForAllocsCache = new HashMap();
        this.nesting = 0;
        this.numPasses = 0;
        this.pointsTo = null;
        this.queriedCallSites = new HashSet();
        this.recursionDepth = -1;
        this.refiningCallSite = false;
        this.upContextCache = new HashMap();
        this.csInfo = csInfo;
        this.pag = pag;
        this.maxPasses = maxPasses;
        this.lazy = lazy;
        this.maxNodesPerPass = maxTraversal / maxPasses;
        this.heuristicType = HeuristicType.INCR;
        this.reachingObjectsCache = new HashMap();
        this.reachingObjectsCacheNoCGRefinement = new HashMap();
        this.useCache = true;
    }

    private void init() {
        this.fieldToStores = SootUtil.storesOnField(this.pag);
        this.fieldToLoads = SootUtil.loadsOnField(this.pag);
        this.vMatches = new ValidMatches(this.pag, this.fieldToStores);
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjects(Local l) {
        if (this.lazy) {
            return new LazyContextSensitivePointsToSet(l, new WrappedPointsToSet((PointsToSetInternal) this.pag.reachingObjects(l)), this);
        }
        return doReachingObjects(l);
    }

    public PointsToSet doReachingObjects(Local l) {
        Map<Local, PointsToSet> cache;
        if (this.fieldToStores == null) {
            init();
        }
        if (this.refineCallGraph) {
            cache = this.reachingObjectsCache;
        } else {
            cache = this.reachingObjectsCacheNoCGRefinement;
        }
        PointsToSet result = cache.get(l);
        if (result == null) {
            result = computeReachingObjects(l);
            if (this.useCache) {
                cache.put(l, result);
            }
        }
        if ($assertionsDisabled || consistentResult(l, result)) {
            return result;
        }
        throw new AssertionError();
    }

    private boolean consistentResult(Local l, PointsToSet result) {
        PointsToSet result2 = computeReachingObjects(l);
        if (!(result instanceof EqualsSupportingPointsToSet) || !(result2 instanceof EqualsSupportingPointsToSet)) {
            return true;
        }
        EqualsSupportingPointsToSet eq1 = (EqualsSupportingPointsToSet) result;
        EqualsSupportingPointsToSet eq2 = (EqualsSupportingPointsToSet) result2;
        return new PointsToSetEqualsWrapper(eq1).equals(new PointsToSetEqualsWrapper(eq2));
    }

    protected PointsToSet computeReachingObjects(Local l) {
        VarNode v = this.pag.findLocalVarNode(l);
        if (v == null) {
            return EmptyPointsToSet.v();
        }
        PointsToSet contextSensitiveResult = computeRefinedReachingObjects(v);
        if (contextSensitiveResult == null) {
            return new WrappedPointsToSet(v.getP2Set());
        }
        return contextSensitiveResult;
    }

    protected PointsToSet computeRefinedReachingObjects(VarNode v) {
        this.fieldCheckHeuristic = HeuristicType.getHeuristic(this.heuristicType, this.pag.getTypeManager(), getMaxPasses());
        this.doPointsTo = true;
        this.numPasses = 0;
        PointsToSet contextSensitiveResult = null;
        do {
            this.numPasses++;
            if (this.numPasses > this.maxPasses) {
                break;
            }
            if (DEBUG) {
                logger.debug("PASS " + this.numPasses);
                logger.debug(new StringBuilder().append(this.fieldCheckHeuristic).toString());
            }
            clearState();
            this.pointsTo = new AllocAndContextSet();
            try {
                refineP2Set(new VarAndContext(v, EMPTY_CALLSTACK), null);
                contextSensitiveResult = this.pointsTo;
            } catch (TerminateEarlyException e) {
                logger.debug(e.getMessage(), (Throwable) e);
            }
        } while (this.fieldCheckHeuristic.runNewPass());
        return contextSensitiveResult;
    }

    protected boolean callEdgeInSCC(AssignEdge assignEdge) {
        boolean sameSCCAlready = false;
        if ($assertionsDisabled || assignEdge.isCallEdge()) {
            if (!(assignEdge.getSrc() instanceof LocalVarNode) || !(assignEdge.getDst() instanceof LocalVarNode)) {
                return false;
            }
            LocalVarNode src = (LocalVarNode) assignEdge.getSrc();
            LocalVarNode dst = (LocalVarNode) assignEdge.getDst();
            if (this.sccManager.inSameSCC(src.getMethod(), dst.getMethod())) {
                sameSCCAlready = true;
            }
            return sameSCCAlready;
        }
        throw new AssertionError();
    }

    protected CallingContextSet checkAllocAndContextCache(AllocAndContext allocAndContext, VarNode targetVar) {
        if (this.allocAndContextCache.containsKey(allocAndContext)) {
            Map<VarNode, CallingContextSet> m = this.allocAndContextCache.get(allocAndContext);
            if (m.containsKey(targetVar)) {
                return m.get(targetVar);
            }
            return null;
        }
        this.allocAndContextCache.put(allocAndContext, new HashMap());
        return null;
    }

    protected PointsToSetInternal checkContextsForAllocsCache(VarAndContext varAndContext, AllocAndContextSet ret, PointsToSetInternal locs) {
        PointsToSetInternal retSet;
        if (this.contextsForAllocsCache.containsKey(varAndContext)) {
            Iterator<AllocAndContext> it = this.contextsForAllocsCache.get(varAndContext).getO2().iterator();
            while (it.hasNext()) {
                AllocAndContext allocAndContext = it.next();
                if (locs.contains(allocAndContext.alloc)) {
                    ret.add(allocAndContext);
                }
            }
            final PointsToSetInternal oldLocs = this.contextsForAllocsCache.get(varAndContext).getO1();
            final PointsToSetInternal newSet = HybridPointsToSet.getFactory().newSet(locs.getType(), this.pag);
            locs.forall(new P2SetVisitor() { // from class: soot.jimple.spark.ondemand.DemandCSPointsTo.1
                @Override // soot.jimple.spark.sets.P2SetVisitor
                public void visit(Node n) {
                    if (!oldLocs.contains(n)) {
                        newSet.add(n);
                    }
                }
            });
            retSet = newSet;
            oldLocs.addAll(newSet, null);
        } else {
            PointsToSetInternal storedSet = HybridPointsToSet.getFactory().newSet(locs.getType(), this.pag);
            storedSet.addAll(locs, null);
            this.contextsForAllocsCache.put(varAndContext, new Pair<>(storedSet, new AllocAndContextSet()));
            retSet = locs;
        }
        return retSet;
    }

    protected boolean checkP2Set(VarNode v, HeuristicType heuristic, Predicate<Set<AllocAndContext>> p2setPred) {
        boolean success;
        this.doPointsTo = true;
        this.fieldCheckHeuristic = HeuristicType.getHeuristic(heuristic, this.pag.getTypeManager(), getMaxPasses());
        this.numPasses = 0;
        while (true) {
            this.numPasses++;
            if (this.numPasses > this.maxPasses) {
                return true;
            }
            if (DEBUG) {
                logger.debug("PASS " + this.numPasses);
                logger.debug(new StringBuilder().append(this.fieldCheckHeuristic).toString());
            }
            clearState();
            this.pointsTo = new AllocAndContextSet();
            try {
                success = refineP2Set(new VarAndContext(v, EMPTY_CALLSTACK), null);
            } catch (TerminateEarlyException e) {
                success = false;
            }
            if (success) {
                if (p2setPred.test(this.pointsTo)) {
                    return false;
                }
            } else if (!this.fieldCheckHeuristic.runNewPass()) {
                return true;
            }
        }
    }

    protected CallingContextSet checkUpContextCache(VarContextAndUp varContextAndUp, AllocAndContext allocAndContext) {
        if (this.upContextCache.containsKey(varContextAndUp)) {
            Map<AllocAndContext, CallingContextSet> allocAndContextMap = this.upContextCache.get(varContextAndUp);
            if (allocAndContextMap.containsKey(allocAndContext)) {
                return allocAndContextMap.get(allocAndContext);
            }
            return null;
        }
        this.upContextCache.put(varContextAndUp, new HashMap());
        return null;
    }

    protected void clearState() {
        this.allocAndContextCache.clear();
        this.callGraphStack.clear();
        this.callSiteToResolvedTargets.clear();
        this.queriedCallSites.clear();
        this.contextsForAllocsCache.clear();
        this.contextForAllocsStack.clear();
        this.upContextCache.clear();
        this.callTargetsArgCache.clear();
        this.sccManager = new OTFMethodSCCManager();
        this.numNodesTraversed = 0;
        this.nesting = 0;
        this.recursionDepth = -1;
    }

    protected Set<VarNode> computeFlowsTo(AllocNode alloc, HeuristicType heuristic) {
        this.fieldCheckHeuristic = HeuristicType.getHeuristic(heuristic, this.pag.getTypeManager(), getMaxPasses());
        this.numPasses = 0;
        Set<VarNode> smallest = null;
        do {
            this.numPasses++;
            if (this.numPasses > this.maxPasses) {
                return smallest;
            }
            if (DEBUG) {
                logger.debug("PASS " + this.numPasses);
                logger.debug(new StringBuilder().append(this.fieldCheckHeuristic).toString());
            }
            clearState();
            Set<VarNode> result = null;
            try {
                result = getFlowsToHelper(new AllocAndContext(alloc, EMPTY_CALLSTACK));
            } catch (TerminateEarlyException e) {
                logger.debug(e.getMessage(), (Throwable) e);
            }
            if (result != null && (smallest == null || result.size() < smallest.size())) {
                smallest = result;
            }
        } while (this.fieldCheckHeuristic.runNewPass());
        return smallest;
    }

    protected void debugPrint(String str) {
        if (this.nesting <= 15) {
            logger.debug(":" + this.nesting + Instruction.argsep + str);
        }
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [soot.jimple.spark.ondemand.DemandCSPointsTo$1Helper] */
    protected void dumpPathForLoc(VarNode v, final AllocNode badLoc, String filePrefix) {
        final HashSet<VarNode> visited = new HashSet<>();
        final DotPointerGraph dotGraph = new DotPointerGraph();
        new Object() { // from class: soot.jimple.spark.ondemand.DemandCSPointsTo.1Helper
            boolean handle(VarNode curNode) {
                if (DemandCSPointsTo.$assertionsDisabled || curNode.getP2Set().contains(badLoc)) {
                    visited.add(curNode);
                    Node[] newEdges = DemandCSPointsTo.this.pag.allocInvLookup(curNode);
                    for (Node node : newEdges) {
                        AllocNode alloc = (AllocNode) node;
                        if (alloc.equals(badLoc)) {
                            dotGraph.addNew(alloc, curNode);
                            return true;
                        }
                    }
                    Iterator<AssignEdge> it = DemandCSPointsTo.this.csInfo.getAssignEdges(curNode).iterator();
                    while (it.hasNext()) {
                        AssignEdge assignEdge = it.next();
                        VarNode other = assignEdge.getSrc();
                        if (other.getP2Set().contains(badLoc) && !visited.contains(other) && handle(other)) {
                            if (assignEdge.isCallEdge()) {
                                dotGraph.addCall(other, curNode, assignEdge.getCallSite());
                                return true;
                            }
                            dotGraph.addAssign(other, curNode);
                            return true;
                        }
                    }
                    Node[] loadEdges = DemandCSPointsTo.this.pag.loadInvLookup(curNode);
                    for (Node node2 : loadEdges) {
                        FieldRefNode frNode = (FieldRefNode) node2;
                        SparkField field = frNode.getField();
                        VarNode base = frNode.getBase();
                        PointsToSetInternal baseP2Set = base.getP2Set();
                        Iterator<Pair<VarNode, VarNode>> it2 = DemandCSPointsTo.this.fieldToStores.get((SootUtil.FieldToEdgesMap) field).iterator();
                        while (it2.hasNext()) {
                            Pair<VarNode, VarNode> store = it2.next();
                            if (store.getO2().getP2Set().hasNonEmptyIntersection(baseP2Set)) {
                                VarNode matchSrc = store.getO1();
                                if (matchSrc.getP2Set().contains(badLoc) && !visited.contains(matchSrc) && handle(matchSrc)) {
                                    dotGraph.addMatch(matchSrc, curNode);
                                    return true;
                                }
                            }
                        }
                    }
                    return false;
                }
                throw new AssertionError();
            }
        }.handle(v);
        dotGraph.dump("tmp/" + filePrefix + v.getNumber() + "_" + badLoc.getNumber() + DotGraph.DOT_EXTENSION);
    }

    protected Collection<AssignEdge> filterAssigns(VarNode v, ImmutableStack<Integer> callingContext, boolean forward, boolean refineVirtCalls) {
        Collection<AssignEdge> realAssigns;
        ArraySet<AssignEdge> assignEdges = forward ? this.csInfo.getAssignEdges(v) : this.csInfo.getAssignBarEdges(v);
        boolean exitNode = forward ? SootUtil.isParamNode(v) : SootUtil.isRetNode(v);
        boolean backward = !forward;
        if (exitNode && !callingContext.isEmpty()) {
            Integer topCallSite = callingContext.peek();
            realAssigns = new ArrayList<>();
            for (AssignEdge assignEdge : assignEdges) {
                if (!$assertionsDisabled && ((!forward || !assignEdge.isParamEdge()) && (!backward || !assignEdge.isReturnEdge()))) {
                    throw new AssertionError(assignEdge);
                }
                Integer assignEdgeCallSite = assignEdge.getCallSite();
                if (!$assertionsDisabled && !this.csInfo.getCallSiteTargets(assignEdgeCallSite).contains(((LocalVarNode) v).getMethod())) {
                    throw new AssertionError(assignEdge);
                }
                if (topCallSite.equals(assignEdgeCallSite) || callEdgeInSCC(assignEdge)) {
                    realAssigns.add(assignEdge);
                }
            }
        } else if (assignEdges.size() > 1) {
            realAssigns = new ArrayList<>();
            for (AssignEdge assignEdge2 : assignEdges) {
                boolean enteringCall = forward ? assignEdge2.isReturnEdge() : assignEdge2.isParamEdge();
                if (enteringCall) {
                    Integer callSite = assignEdge2.getCallSite();
                    if (this.csInfo.isVirtCall(callSite) && refineVirtCalls) {
                        Set<SootMethod> targets = refineCallSite(assignEdge2.getCallSite(), callingContext);
                        LocalVarNode nodeInTargetMethod = forward ? (LocalVarNode) assignEdge2.getSrc() : (LocalVarNode) assignEdge2.getDst();
                        if (targets.contains(nodeInTargetMethod.getMethod())) {
                            realAssigns.add(assignEdge2);
                        }
                    } else {
                        realAssigns.add(assignEdge2);
                    }
                } else {
                    realAssigns.add(assignEdge2);
                }
            }
        } else {
            realAssigns = assignEdges;
        }
        return realAssigns;
    }

    protected AllocAndContextSet findContextsForAllocs(VarAndContext varAndContext, PointsToSetInternal locs) {
        int oldIndex;
        if (this.contextForAllocsStack.contains(varAndContext) && (oldIndex = this.contextForAllocsStack.indexOf(varAndContext)) != this.contextForAllocsStack.size() - 1) {
            if (this.recursionDepth == -1) {
                this.recursionDepth = oldIndex + 1;
                if (DEBUG) {
                    debugPrint("RECURSION depth = " + this.recursionDepth);
                }
            } else if (this.contextForAllocsStack.size() - oldIndex > 5) {
                throw new TerminateEarlyException();
            }
        }
        this.contextForAllocsStack.push(varAndContext);
        final AllocAndContextSet ret = new AllocAndContextSet();
        final PointsToSetInternal realLocs = checkContextsForAllocsCache(varAndContext, ret, locs);
        if (realLocs.isEmpty()) {
            if (DEBUG) {
                debugPrint("cached result " + ret);
            }
            return ret;
        }
        this.nesting++;
        if (DEBUG) {
            debugPrint("finding alloc contexts for " + varAndContext);
        }
        try {
            try {
                Set<VarAndContext> marked = new HashSet<>();
                Stack<VarAndContext> worklist = new Stack<>();
                final Propagator<VarAndContext> p = new Propagator<>(marked, worklist);
                p.prop(varAndContext);
                IncomingEdgeHandler edgeHandler = new IncomingEdgeHandler() { // from class: soot.jimple.spark.ondemand.DemandCSPointsTo.2
                    @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.IncomingEdgeHandler
                    public void handleAlloc(AllocNode allocNode, VarAndContext origVarAndContext) {
                        if (realLocs.contains(allocNode)) {
                            if (DemandCSPointsTo.DEBUG) {
                                DemandCSPointsTo.this.debugPrint("found alloc " + allocNode);
                            }
                            ret.add(new AllocAndContext(allocNode, origVarAndContext.context));
                        }
                    }

                    @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.IncomingEdgeHandler
                    public void handleMatchSrc(VarNode matchSrc, PointsToSetInternal intersection, VarNode loadBase, VarNode storeBase, VarAndContext origVarAndContext, SparkField field, boolean refine) {
                        CallingContextSet matchSrcContexts;
                        if (DemandCSPointsTo.DEBUG) {
                            DemandCSPointsTo.this.debugPrint("handling src " + matchSrc);
                            DemandCSPointsTo.this.debugPrint("intersection " + intersection);
                        }
                        if (!refine) {
                            p.prop(new VarAndContext(matchSrc, DemandCSPointsTo.EMPTY_CALLSTACK));
                            return;
                        }
                        AllocAndContextSet allocContexts = DemandCSPointsTo.this.findContextsForAllocs(new VarAndContext(loadBase, origVarAndContext.context), intersection);
                        if (DemandCSPointsTo.DEBUG) {
                            DemandCSPointsTo.this.debugPrint("alloc contexts " + allocContexts);
                        }
                        Iterator<AllocAndContext> it = allocContexts.iterator();
                        while (it.hasNext()) {
                            AllocAndContext allocAndContext = it.next();
                            if (DemandCSPointsTo.DEBUG) {
                                DemandCSPointsTo.this.debugPrint("alloc and context " + allocAndContext);
                            }
                            if (DemandCSPointsTo.this.fieldCheckHeuristic.validFromBothEnds(field)) {
                                matchSrcContexts = DemandCSPointsTo.this.findUpContextsForVar(allocAndContext, new VarContextAndUp(storeBase, DemandCSPointsTo.EMPTY_CALLSTACK, DemandCSPointsTo.EMPTY_CALLSTACK));
                            } else {
                                matchSrcContexts = DemandCSPointsTo.this.findVarContextsFromAlloc(allocAndContext, storeBase);
                            }
                            Iterator<ImmutableStack<Integer>> it2 = matchSrcContexts.iterator();
                            while (it2.hasNext()) {
                                ImmutableStack<Integer> matchSrcContext = it2.next();
                                p.prop(new VarAndContext(matchSrc, matchSrcContext));
                            }
                        }
                    }

                    @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.IncomingEdgeHandler
                    Object getResult() {
                        return ret;
                    }

                    @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.IncomingEdgeHandler
                    void handleAssignSrc(VarAndContext newVarAndContext, VarAndContext origVarAndContext, AssignEdge assignEdge) {
                        p.prop(newVarAndContext);
                    }

                    @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.IncomingEdgeHandler
                    boolean shouldHandleSrc(VarNode src) {
                        return realLocs.hasNonEmptyIntersection(src.getP2Set());
                    }
                };
                processIncomingEdges(edgeHandler, worklist);
                if (this.recursionDepth != -1) {
                    if (this.contextForAllocsStack.size() > this.recursionDepth) {
                        if (DEBUG) {
                            debugPrint("REMOVING " + varAndContext);
                            debugPrint(this.contextForAllocsStack.toString());
                        }
                        this.contextsForAllocsCache.remove(varAndContext);
                    } else if (!$assertionsDisabled && this.contextForAllocsStack.size() != this.recursionDepth) {
                        throw new AssertionError(String.valueOf(this.recursionDepth) + Instruction.argsep + this.contextForAllocsStack);
                    } else {
                        this.recursionDepth = -1;
                        if (this.contextsForAllocsCache.containsKey(varAndContext)) {
                            this.contextsForAllocsCache.get(varAndContext).getO2().addAll((ArraySet) ret);
                        } else {
                            PointsToSetInternal storedSet = HybridPointsToSet.getFactory().newSet(locs.getType(), this.pag);
                            storedSet.addAll(locs, null);
                            this.contextsForAllocsCache.put(varAndContext, new Pair<>(storedSet, ret));
                        }
                    }
                } else if (this.contextsForAllocsCache.containsKey(varAndContext)) {
                    this.contextsForAllocsCache.get(varAndContext).getO2().addAll((ArraySet) ret);
                } else {
                    PointsToSetInternal storedSet2 = HybridPointsToSet.getFactory().newSet(locs.getType(), this.pag);
                    storedSet2.addAll(locs, null);
                    this.contextsForAllocsCache.put(varAndContext, new Pair<>(storedSet2, ret));
                }
                this.nesting--;
                return ret;
            } catch (CallSiteException e) {
                this.contextsForAllocsCache.remove(varAndContext);
                throw e;
            }
        } finally {
            this.contextForAllocsStack.pop();
        }
    }

    protected CallingContextSet findUpContextsForVar(AllocAndContext allocAndContext, VarContextAndUp varContextAndUp) {
        final AllocNode alloc = allocAndContext.alloc;
        final ImmutableStack<Integer> allocContext = allocAndContext.context;
        CallingContextSet tmpSet = checkUpContextCache(varContextAndUp, allocAndContext);
        if (tmpSet != null) {
            return tmpSet;
        }
        final CallingContextSet ret = new CallingContextSet();
        this.upContextCache.get(varContextAndUp).put(allocAndContext, ret);
        this.nesting++;
        if (DEBUG) {
            debugPrint("finding up context for " + varContextAndUp + " to " + alloc + Instruction.argsep + allocContext);
        }
        try {
            Set<VarAndContext> marked = new HashSet<>();
            Stack<VarAndContext> worklist = new Stack<>();
            final Propagator<VarAndContext> p = new Propagator<>(marked, worklist);
            p.prop(varContextAndUp);
            processIncomingEdges(new IncomingEdgeHandler() { // from class: soot.jimple.spark.ondemand.DemandCSPointsTo.1UpContextEdgeHandler
                @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.IncomingEdgeHandler
                public void handleAlloc(AllocNode allocNode, VarAndContext origVarAndContext) {
                    VarContextAndUp contextAndUp = (VarContextAndUp) origVarAndContext;
                    if (allocNode == alloc) {
                        if (allocContext.topMatches(contextAndUp.context)) {
                            ImmutableStack<Integer> reverse = contextAndUp.upContext.reverse();
                            ImmutableStack<Integer> toAdd = allocContext.popAll(contextAndUp.context).pushAll(reverse);
                            if (DemandCSPointsTo.DEBUG) {
                                DemandCSPointsTo.this.debugPrint("found up context " + toAdd);
                            }
                            ret.add(toAdd);
                        } else if (contextAndUp.context.topMatches(allocContext)) {
                            ImmutableStack<Integer> toAdd2 = contextAndUp.upContext.reverse();
                            if (DemandCSPointsTo.DEBUG) {
                                DemandCSPointsTo.this.debugPrint("found up context " + toAdd2);
                            }
                            ret.add(toAdd2);
                        }
                    }
                }

                @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.IncomingEdgeHandler
                public void handleMatchSrc(VarNode matchSrc, PointsToSetInternal intersection, VarNode loadBase, VarNode storeBase, VarAndContext origVarAndContext, SparkField field, boolean refine) {
                    VarContextAndUp contextAndUp = (VarContextAndUp) origVarAndContext;
                    if (DemandCSPointsTo.DEBUG) {
                        DemandCSPointsTo.this.debugPrint("CHECKING " + alloc);
                    }
                    PointsToSetInternal tmp = HybridPointsToSet.getFactory().newSet(alloc.getType(), DemandCSPointsTo.this.pag);
                    tmp.add(alloc);
                    AllocAndContextSet allocContexts = DemandCSPointsTo.this.findContextsForAllocs(new VarAndContext(matchSrc, DemandCSPointsTo.EMPTY_CALLSTACK), tmp);
                    if (!refine) {
                        if (!allocContexts.isEmpty()) {
                            ret.add(contextAndUp.upContext.reverse());
                        }
                    } else if (!allocContexts.isEmpty()) {
                        Iterator<AllocAndContext> it = allocContexts.iterator();
                        while (it.hasNext()) {
                            AllocAndContext t = it.next();
                            ImmutableStack<Integer> discoveredAllocContext = t.context;
                            if (allocContext.topMatches(discoveredAllocContext)) {
                                ImmutableStack<Integer> trueAllocContext = allocContext.popAll(discoveredAllocContext);
                                AllocAndContextSet allocAndContexts = DemandCSPointsTo.this.findContextsForAllocs(new VarAndContext(storeBase, trueAllocContext), intersection);
                                Iterator<AllocAndContext> it2 = allocAndContexts.iterator();
                                while (it2.hasNext()) {
                                    AllocAndContext allocAndContext2 = it2.next();
                                    if (DemandCSPointsTo.this.fieldCheckHeuristic.validFromBothEnds(field)) {
                                        ret.addAll((ArraySet) DemandCSPointsTo.this.findUpContextsForVar(allocAndContext2, new VarContextAndUp(loadBase, contextAndUp.context, contextAndUp.upContext)));
                                    } else {
                                        CallingContextSet tmpContexts = DemandCSPointsTo.this.findVarContextsFromAlloc(allocAndContext2, loadBase);
                                        Iterator<ImmutableStack<Integer>> it3 = tmpContexts.iterator();
                                        while (it3.hasNext()) {
                                            ImmutableStack<Integer> tmpContext = it3.next();
                                            if (tmpContext.topMatches(contextAndUp.context)) {
                                                ImmutableStack<Integer> reverse = contextAndUp.upContext.reverse();
                                                ImmutableStack<Integer> toAdd = tmpContext.popAll(contextAndUp.context).pushAll(reverse);
                                                ret.add(toAdd);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.IncomingEdgeHandler
                Object getResult() {
                    return ret;
                }

                @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.IncomingEdgeHandler
                void handleAssignSrc(VarAndContext newVarAndContext, VarAndContext origVarAndContext, AssignEdge assignEdge) {
                    VarContextAndUp contextAndUp = (VarContextAndUp) origVarAndContext;
                    ImmutableStack<Integer> upContext = contextAndUp.upContext;
                    ImmutableStack<Integer> newUpContext = upContext;
                    if (assignEdge.isParamEdge() && contextAndUp.context.isEmpty() && upContext.size() < ImmutableStack.getMaxSize()) {
                        newUpContext = DemandCSPointsTo.this.pushWithRecursionCheck(upContext, assignEdge);
                    }
                    p.prop(new VarContextAndUp(newVarAndContext.var, newVarAndContext.context, newUpContext));
                }

                @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.IncomingEdgeHandler
                boolean shouldHandleSrc(VarNode src) {
                    if (src instanceof GlobalVarNode) {
                        throw new TerminateEarlyException();
                    }
                    return src.getP2Set().contains(alloc);
                }
            }, worklist);
            this.nesting--;
            return ret;
        } catch (CallSiteException e) {
            this.upContextCache.remove(varContextAndUp);
            throw e;
        }
    }

    protected CallingContextSet findVarContextsFromAlloc(AllocAndContext allocAndContext, VarNode targetVar) {
        CallingContextSet upContexts;
        CallingContextSet tmpSet = checkAllocAndContextCache(allocAndContext, targetVar);
        if (tmpSet != null) {
            return tmpSet;
        }
        CallingContextSet ret = new CallingContextSet();
        this.allocAndContextCache.get(allocAndContext).put(targetVar, ret);
        try {
            HashSet<VarAndContext> marked = new HashSet<>();
            Stack<VarAndContext> worklist = new Stack<>();
            Propagator<VarAndContext> p = new Propagator<>(marked, worklist);
            AllocNode alloc = allocAndContext.alloc;
            ImmutableStack<Integer> allocContext = allocAndContext.context;
            Node[] newBarNodes = this.pag.allocLookup(alloc);
            for (Node node : newBarNodes) {
                VarNode v = (VarNode) node;
                p.prop(new VarAndContext(v, allocContext));
            }
            while (!worklist.isEmpty()) {
                incrementNodesTraversed();
                VarAndContext curVarAndContext = worklist.pop();
                if (DEBUG) {
                    debugPrint("looking at " + curVarAndContext);
                }
                VarNode curVar = curVarAndContext.var;
                ImmutableStack<Integer> curContext = curVarAndContext.context;
                if (curVar == targetVar) {
                    ret.add(curContext);
                }
                Collection<AssignEdge> assignEdges = filterAssigns(curVar, curContext, false, true);
                for (AssignEdge assignEdge : assignEdges) {
                    VarNode dst = assignEdge.getDst();
                    ImmutableStack<Integer> newContext = curContext;
                    if (assignEdge.isReturnEdge()) {
                        if (!curContext.isEmpty()) {
                            if (!callEdgeInSCC(assignEdge)) {
                                if (!$assertionsDisabled && !assignEdge.getCallSite().equals(curContext.peek())) {
                                    throw new AssertionError(assignEdge + Instruction.argsep + curContext);
                                }
                                newContext = curContext.pop();
                            } else {
                                newContext = popRecursiveCallSites(curContext);
                            }
                        }
                    } else if (assignEdge.isParamEdge()) {
                        if (DEBUG) {
                            debugPrint("entering call site " + assignEdge.getCallSite());
                        }
                        newContext = pushWithRecursionCheck(curContext, assignEdge);
                    }
                    if (assignEdge.isReturnEdge() && curContext.isEmpty() && this.csInfo.isVirtCall(assignEdge.getCallSite())) {
                        Set<SootMethod> targets = refineCallSite(assignEdge.getCallSite(), newContext);
                        if (!targets.contains(((LocalVarNode) assignEdge.getDst()).getMethod())) {
                        }
                    }
                    if (dst instanceof GlobalVarNode) {
                        newContext = EMPTY_CALLSTACK;
                    }
                    p.prop(new VarAndContext(dst, newContext));
                }
                Set<VarNode> matchTargets = this.vMatches.vMatchLookup(curVar);
                Node[] pfTargets = this.pag.storeLookup(curVar);
                for (Node node2 : pfTargets) {
                    FieldRefNode frNode = (FieldRefNode) node2;
                    VarNode storeBase = frNode.getBase();
                    SparkField field = frNode.getField();
                    Iterator<Pair<VarNode, VarNode>> it = this.fieldToLoads.get((SootUtil.FieldToEdgesMap) field).iterator();
                    while (it.hasNext()) {
                        Pair<VarNode, VarNode> load = it.next();
                        VarNode loadBase = load.getO2();
                        PointsToSetInternal loadBaseP2Set = loadBase.getP2Set();
                        PointsToSetInternal storeBaseP2Set = storeBase.getP2Set();
                        VarNode matchTgt = load.getO1();
                        if (matchTargets.contains(matchTgt)) {
                            if (DEBUG) {
                                debugPrint("match source " + matchTgt);
                            }
                            PointsToSetInternal intersection = SootUtil.constructIntersection(storeBaseP2Set, loadBaseP2Set, this.pag);
                            boolean checkField = this.fieldCheckHeuristic.validateMatchesForField(field);
                            if (checkField) {
                                AllocAndContextSet sharedAllocContexts = findContextsForAllocs(new VarAndContext(storeBase, curContext), intersection);
                                Iterator<AllocAndContext> it2 = sharedAllocContexts.iterator();
                                while (it2.hasNext()) {
                                    AllocAndContext curAllocAndContext = it2.next();
                                    if (this.fieldCheckHeuristic.validFromBothEnds(field)) {
                                        upContexts = findUpContextsForVar(curAllocAndContext, new VarContextAndUp(loadBase, EMPTY_CALLSTACK, EMPTY_CALLSTACK));
                                    } else {
                                        upContexts = findVarContextsFromAlloc(curAllocAndContext, loadBase);
                                    }
                                    Iterator<ImmutableStack<Integer>> it3 = upContexts.iterator();
                                    while (it3.hasNext()) {
                                        ImmutableStack<Integer> upContext = it3.next();
                                        p.prop(new VarAndContext(matchTgt, upContext));
                                    }
                                }
                            } else {
                                p.prop(new VarAndContext(matchTgt, EMPTY_CALLSTACK));
                            }
                        }
                    }
                }
            }
            return ret;
        } catch (CallSiteException e) {
            this.allocAndContextCache.remove(allocAndContext);
            throw e;
        }
    }

    protected Set<SootMethod> getCallTargets(PointsToSetInternal p2Set, SootMethod callee, Type receiverType, Set<SootMethod> possibleTargets) {
        List<Object> args = Arrays.asList(p2Set, callee, receiverType, possibleTargets);
        if (this.callTargetsArgCache.containsKey(args)) {
            return this.callTargetsArgCache.get(args);
        }
        Set<Type> types = p2Set.possibleTypes();
        Set<SootMethod> ret = new HashSet<>();
        for (Type type : types) {
            ret.addAll(getCallTargetsForType(type, callee, receiverType, possibleTargets));
        }
        this.callTargetsArgCache.put(args, ret);
        return ret;
    }

    protected Set<SootMethod> getCallTargetsForType(Type type, SootMethod callee, Type receiverType, Set<SootMethod> possibleTargets) {
        if (!this.pag.getTypeManager().castNeverFails(type, receiverType)) {
            return Collections.emptySet();
        }
        if (type instanceof AnySubType) {
            AnySubType any = (AnySubType) type;
            RefType refType = any.getBase();
            if (this.pag.getTypeManager().getFastHierarchy().canStoreType(receiverType, refType) || this.pag.getTypeManager().getFastHierarchy().canStoreType(refType, receiverType)) {
                return possibleTargets;
            }
            return Collections.emptySet();
        }
        if (type instanceof ArrayType) {
            type = Scene.v().getSootClass(Scene.v().getObjectType().toString()).getType();
        }
        SootMethod targetMethod = VirtualCalls.v().resolveNonSpecial((RefType) type, callee.makeRef());
        return Collections.singleton(targetMethod);
    }

    protected Set<VarNode> getFlowsToHelper(AllocAndContext allocAndContext) {
        CallingContextSet upContexts;
        Set<VarNode> ret = new ArraySet<>();
        try {
            HashSet<VarAndContext> marked = new HashSet<>();
            Stack<VarAndContext> worklist = new Stack<>();
            Propagator<VarAndContext> p = new Propagator<>(marked, worklist);
            AllocNode alloc = allocAndContext.alloc;
            ImmutableStack<Integer> allocContext = allocAndContext.context;
            Node[] newBarNodes = this.pag.allocLookup(alloc);
            for (Node node : newBarNodes) {
                VarNode v = (VarNode) node;
                ret.add(v);
                p.prop(new VarAndContext(v, allocContext));
            }
            while (!worklist.isEmpty()) {
                incrementNodesTraversed();
                VarAndContext curVarAndContext = worklist.pop();
                if (DEBUG) {
                    debugPrint("looking at " + curVarAndContext);
                }
                VarNode curVar = curVarAndContext.var;
                ImmutableStack<Integer> curContext = curVarAndContext.context;
                ret.add(curVar);
                Collection<AssignEdge> assignEdges = filterAssigns(curVar, curContext, false, true);
                for (AssignEdge assignEdge : assignEdges) {
                    VarNode dst = assignEdge.getDst();
                    ImmutableStack<Integer> newContext = curContext;
                    if (assignEdge.isReturnEdge()) {
                        if (!curContext.isEmpty()) {
                            if (!callEdgeInSCC(assignEdge)) {
                                if (!$assertionsDisabled && !assignEdge.getCallSite().equals(curContext.peek())) {
                                    throw new AssertionError(assignEdge + Instruction.argsep + curContext);
                                }
                                newContext = curContext.pop();
                            } else {
                                newContext = popRecursiveCallSites(curContext);
                            }
                        }
                    } else if (assignEdge.isParamEdge()) {
                        if (DEBUG) {
                            debugPrint("entering call site " + assignEdge.getCallSite());
                        }
                        newContext = pushWithRecursionCheck(curContext, assignEdge);
                    }
                    if (assignEdge.isReturnEdge() && curContext.isEmpty() && this.csInfo.isVirtCall(assignEdge.getCallSite())) {
                        Set<SootMethod> targets = refineCallSite(assignEdge.getCallSite(), newContext);
                        if (!targets.contains(((LocalVarNode) assignEdge.getDst()).getMethod())) {
                        }
                    }
                    if (dst instanceof GlobalVarNode) {
                        newContext = EMPTY_CALLSTACK;
                    }
                    p.prop(new VarAndContext(dst, newContext));
                }
                Set<VarNode> matchTargets = this.vMatches.vMatchLookup(curVar);
                Node[] pfTargets = this.pag.storeLookup(curVar);
                for (Node node2 : pfTargets) {
                    FieldRefNode frNode = (FieldRefNode) node2;
                    VarNode storeBase = frNode.getBase();
                    SparkField field = frNode.getField();
                    Iterator<Pair<VarNode, VarNode>> it = this.fieldToLoads.get((SootUtil.FieldToEdgesMap) field).iterator();
                    while (it.hasNext()) {
                        Pair<VarNode, VarNode> load = it.next();
                        VarNode loadBase = load.getO2();
                        PointsToSetInternal loadBaseP2Set = loadBase.getP2Set();
                        PointsToSetInternal storeBaseP2Set = storeBase.getP2Set();
                        VarNode matchTgt = load.getO1();
                        if (matchTargets.contains(matchTgt)) {
                            if (DEBUG) {
                                debugPrint("match source " + matchTgt);
                            }
                            PointsToSetInternal intersection = SootUtil.constructIntersection(storeBaseP2Set, loadBaseP2Set, this.pag);
                            boolean checkField = this.fieldCheckHeuristic.validateMatchesForField(field);
                            if (checkField) {
                                AllocAndContextSet sharedAllocContexts = findContextsForAllocs(new VarAndContext(storeBase, curContext), intersection);
                                Iterator<AllocAndContext> it2 = sharedAllocContexts.iterator();
                                while (it2.hasNext()) {
                                    AllocAndContext curAllocAndContext = it2.next();
                                    if (this.fieldCheckHeuristic.validFromBothEnds(field)) {
                                        upContexts = findUpContextsForVar(curAllocAndContext, new VarContextAndUp(loadBase, EMPTY_CALLSTACK, EMPTY_CALLSTACK));
                                    } else {
                                        upContexts = findVarContextsFromAlloc(curAllocAndContext, loadBase);
                                    }
                                    Iterator<ImmutableStack<Integer>> it3 = upContexts.iterator();
                                    while (it3.hasNext()) {
                                        ImmutableStack<Integer> upContext = it3.next();
                                        p.prop(new VarAndContext(matchTgt, upContext));
                                    }
                                }
                            } else {
                                p.prop(new VarAndContext(matchTgt, EMPTY_CALLSTACK));
                            }
                        }
                    }
                }
            }
            return ret;
        } catch (CallSiteException e) {
            this.allocAndContextCache.remove(allocAndContext);
            throw e;
        }
    }

    protected int getMaxPasses() {
        return this.maxPasses;
    }

    protected void incrementNodesTraversed() {
        this.numNodesTraversed++;
        if (this.numNodesTraversed > this.maxNodesPerPass) {
            throw new TerminateEarlyException();
        }
    }

    protected boolean isRecursive(ImmutableStack<Integer> context, AssignEdge assignEdge) {
        boolean sameSCCAlready = callEdgeInSCC(assignEdge);
        if (sameSCCAlready) {
            return true;
        }
        Integer callSite = assignEdge.getCallSite();
        if (context.contains(callSite)) {
            Set<SootMethod> toBeCollapsed = new ArraySet<>();
            int callSiteInd = 0;
            while (callSiteInd < context.size() && !context.get(callSiteInd).equals(callSite)) {
                callSiteInd++;
            }
            while (callSiteInd < context.size()) {
                toBeCollapsed.add(this.csInfo.getInvokingMethod(context.get(callSiteInd)));
                callSiteInd++;
            }
            this.sccManager.makeSameSCC(toBeCollapsed);
            return true;
        }
        return false;
    }

    protected boolean isRecursiveCallSite(Integer callSite) {
        SootMethod invokingMethod = this.csInfo.getInvokingMethod(callSite);
        SootMethod invokedMethod = this.csInfo.getInvokedMethod(callSite);
        return this.sccManager.inSameSCC(invokingMethod, invokedMethod);
    }

    protected Set<VarNode> nodesPropagatedThrough(VarNode source, PointsToSetInternal allocs) {
        Set<VarNode> marked = new HashSet<>();
        Stack<VarNode> worklist = new Stack<>();
        Propagator<VarNode> p = new Propagator<>(marked, worklist);
        p.prop(source);
        while (!worklist.isEmpty()) {
            VarNode curNode = worklist.pop();
            Node[] assignSources = this.pag.simpleInvLookup(curNode);
            for (Node node : assignSources) {
                VarNode assignSrc = (VarNode) node;
                if (assignSrc.getP2Set().hasNonEmptyIntersection(allocs)) {
                    p.prop(assignSrc);
                }
            }
            Set<VarNode> matchSources = this.vMatches.vMatchInvLookup(curNode);
            for (VarNode matchSrc : matchSources) {
                if (matchSrc.getP2Set().hasNonEmptyIntersection(allocs)) {
                    p.prop(matchSrc);
                }
            }
        }
        return marked;
    }

    protected ImmutableStack<Integer> popRecursiveCallSites(ImmutableStack<Integer> context) {
        ImmutableStack<Integer> ret;
        ImmutableStack<Integer> immutableStack = context;
        while (true) {
            ret = immutableStack;
            if (ret.isEmpty() || !isRecursiveCallSite(ret.peek())) {
                break;
            }
            immutableStack = ret.pop();
        }
        return ret;
    }

    protected void processIncomingEdges(IncomingEdgeHandler h, Stack<VarAndContext> worklist) {
        while (!worklist.isEmpty()) {
            incrementNodesTraversed();
            VarAndContext varAndContext = worklist.pop();
            if (DEBUG) {
                debugPrint("looking at " + varAndContext);
            }
            VarNode v = varAndContext.var;
            ImmutableStack<Integer> callingContext = varAndContext.context;
            Node[] newEdges = this.pag.allocInvLookup(v);
            for (Node node : newEdges) {
                AllocNode allocNode = (AllocNode) node;
                h.handleAlloc(allocNode, varAndContext);
                if (h.terminate()) {
                    return;
                }
            }
            Collection<AssignEdge> assigns = filterAssigns(v, callingContext, true, true);
            for (AssignEdge assignEdge : assigns) {
                VarNode src = assignEdge.getSrc();
                if (h.shouldHandleSrc(src)) {
                    ImmutableStack<Integer> newContext = callingContext;
                    if (assignEdge.isParamEdge()) {
                        if (!callingContext.isEmpty()) {
                            if (!callEdgeInSCC(assignEdge)) {
                                if (!$assertionsDisabled && !assignEdge.getCallSite().equals(callingContext.peek())) {
                                    throw new AssertionError(assignEdge + Instruction.argsep + callingContext);
                                }
                                newContext = callingContext.pop();
                            } else {
                                newContext = popRecursiveCallSites(callingContext);
                            }
                        }
                    } else if (assignEdge.isReturnEdge()) {
                        if (DEBUG) {
                            debugPrint("entering call site " + assignEdge.getCallSite());
                        }
                        newContext = pushWithRecursionCheck(callingContext, assignEdge);
                    }
                    if (assignEdge.isParamEdge()) {
                        Integer callSite = assignEdge.getCallSite();
                        if (this.csInfo.isVirtCall(callSite) && !weirdCall(callSite)) {
                            Set<SootMethod> targets = refineCallSite(callSite, newContext);
                            if (DEBUG) {
                                debugPrint(targets.toString());
                            }
                            SootMethod targetMethod = ((LocalVarNode) assignEdge.getDst()).getMethod();
                            if (!targets.contains(targetMethod)) {
                                if (DEBUG) {
                                    debugPrint("skipping call because of call graph");
                                }
                            }
                        }
                    }
                    if (src instanceof GlobalVarNode) {
                        newContext = EMPTY_CALLSTACK;
                    }
                    h.handleAssignSrc(new VarAndContext(src, newContext), varAndContext, assignEdge);
                    if (h.terminate()) {
                        return;
                    }
                }
            }
            Set<VarNode> matchSources = this.vMatches.vMatchInvLookup(v);
            Node[] loads = this.pag.loadInvLookup(v);
            for (Node node2 : loads) {
                FieldRefNode frNode = (FieldRefNode) node2;
                VarNode loadBase = frNode.getBase();
                SparkField field = frNode.getField();
                Iterator<Pair<VarNode, VarNode>> it = this.fieldToStores.get((SootUtil.FieldToEdgesMap) field).iterator();
                while (it.hasNext()) {
                    Pair<VarNode, VarNode> store = it.next();
                    VarNode storeBase = store.getO2();
                    PointsToSetInternal storeBaseP2Set = storeBase.getP2Set();
                    PointsToSetInternal loadBaseP2Set = loadBase.getP2Set();
                    VarNode matchSrc = store.getO1();
                    if (matchSources.contains(matchSrc) && h.shouldHandleSrc(matchSrc)) {
                        if (DEBUG) {
                            debugPrint("match source " + matchSrc);
                        }
                        PointsToSetInternal intersection = SootUtil.constructIntersection(storeBaseP2Set, loadBaseP2Set, this.pag);
                        boolean checkGetfield = this.fieldCheckHeuristic.validateMatchesForField(field);
                        h.handleMatchSrc(matchSrc, intersection, loadBase, storeBase, varAndContext, field, checkGetfield);
                        if (h.terminate()) {
                            return;
                        }
                    }
                }
            }
        }
    }

    protected ImmutableStack<Integer> pushWithRecursionCheck(ImmutableStack<Integer> context, AssignEdge assignEdge) {
        boolean foundRecursion = callEdgeInSCC(assignEdge);
        if (!foundRecursion) {
            Integer callSite = assignEdge.getCallSite();
            if (context.contains(callSite)) {
                if (DEBUG) {
                    debugPrint("RECURSION!!!");
                }
                throw new TerminateEarlyException();
            }
        }
        if (foundRecursion) {
            ImmutableStack<Integer> popped = popRecursiveCallSites(context);
            if (DEBUG) {
                debugPrint("popped stack " + popped);
            }
            return popped;
        }
        return context.push(assignEdge.getCallSite());
    }

    protected boolean refineAlias(VarNode v1, VarNode v2, PointsToSetInternal intersection, HeuristicType heuristic) {
        if (refineAliasInternal(v1, v2, intersection, heuristic) || refineAliasInternal(v2, v1, intersection, heuristic)) {
            return true;
        }
        return false;
    }

    protected boolean refineAliasInternal(VarNode v1, VarNode v2, PointsToSetInternal intersection, HeuristicType heuristic) {
        boolean success;
        this.fieldCheckHeuristic = HeuristicType.getHeuristic(heuristic, this.pag.getTypeManager(), getMaxPasses());
        this.numPasses = 0;
        do {
            this.numPasses++;
            if (this.numPasses > this.maxPasses) {
                return false;
            }
            if (DEBUG) {
                logger.debug("PASS " + this.numPasses);
                logger.debug(new StringBuilder().append(this.fieldCheckHeuristic).toString());
            }
            clearState();
            try {
                AllocAndContextSet allocAndContexts = findContextsForAllocs(new VarAndContext(v1, EMPTY_CALLSTACK), intersection);
                boolean emptyIntersection = true;
                Iterator<AllocAndContext> it = allocAndContexts.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    AllocAndContext allocAndContext = it.next();
                    CallingContextSet upContexts = findUpContextsForVar(allocAndContext, new VarContextAndUp(v2, EMPTY_CALLSTACK, EMPTY_CALLSTACK));
                    if (!upContexts.isEmpty()) {
                        emptyIntersection = false;
                        break;
                    }
                }
                success = emptyIntersection;
            } catch (TerminateEarlyException e) {
                success = false;
            }
            if (success) {
                logger.debug("took " + this.numPasses + " passes");
                return true;
            }
        } while (this.fieldCheckHeuristic.runNewPass());
        return false;
    }

    /* JADX WARN: Type inference failed for: r0v25, types: [soot.jimple.spark.ondemand.DemandCSPointsTo$2Helper] */
    protected Set<SootMethod> refineCallSite(Integer callSite, ImmutableStack<Integer> origContext) {
        CallingContextSet matchSrcContexts;
        CallSiteAndContext callSiteAndContext = new CallSiteAndContext(callSite, origContext);
        if (this.queriedCallSites.contains(callSiteAndContext)) {
            return this.callSiteToResolvedTargets.get(callSiteAndContext);
        }
        if (this.callGraphStack.contains(callSiteAndContext)) {
            return Collections.emptySet();
        }
        this.callGraphStack.push(callSiteAndContext);
        VarNode receiver = this.csInfo.getReceiverForVirtCallSite(callSite);
        Type receiverType = receiver.getType();
        SootMethod invokedMethod = this.csInfo.getInvokedMethod(callSite);
        Set<SootMethod> allTargets = this.csInfo.getCallSiteTargets(callSite);
        if (!this.refineCallGraph) {
            this.callGraphStack.pop();
            return allTargets;
        }
        if (DEBUG_VIRT) {
            debugPrint("refining call to " + invokedMethod + " on " + receiver + Instruction.argsep + origContext);
        }
        final HashSet<VarAndContext> marked = new HashSet<>();
        final Stack<VarAndContext> worklist = new Stack<>();
        ?? r0 = new Object() { // from class: soot.jimple.spark.ondemand.DemandCSPointsTo.2Helper
            void prop(VarAndContext varAndContext) {
                if (marked.add(varAndContext)) {
                    worklist.push(varAndContext);
                }
            }
        };
        r0.prop(new VarAndContext(receiver, origContext));
        while (!worklist.isEmpty()) {
            incrementNodesTraversed();
            VarAndContext curVarAndContext = worklist.pop();
            if (DEBUG_VIRT) {
                debugPrint("virt looking at " + curVarAndContext);
            }
            VarNode curVar = curVarAndContext.var;
            ImmutableStack<Integer> curContext = curVarAndContext.context;
            Node[] newNodes = this.pag.allocInvLookup(curVar);
            for (Node node : newNodes) {
                AllocNode allocNode = (AllocNode) node;
                for (SootMethod method : getCallTargetsForType(allocNode.getType(), invokedMethod, receiverType, allTargets)) {
                    this.callSiteToResolvedTargets.put(callSiteAndContext, method);
                }
            }
            Collection<AssignEdge> assigns = filterAssigns(curVar, curContext, true, true);
            for (AssignEdge assignEdge : assigns) {
                VarNode src = assignEdge.getSrc();
                ImmutableStack<Integer> newContext = curContext;
                if (assignEdge.isParamEdge()) {
                    if (!curContext.isEmpty()) {
                        if (!callEdgeInSCC(assignEdge)) {
                            if (!$assertionsDisabled && !assignEdge.getCallSite().equals(curContext.peek())) {
                                throw new AssertionError();
                            }
                            newContext = curContext.pop();
                        } else {
                            newContext = popRecursiveCallSites(curContext);
                        }
                    } else {
                        this.callSiteToResolvedTargets.putAll(callSiteAndContext, allTargets);
                    }
                } else if (assignEdge.isReturnEdge()) {
                    newContext = pushWithRecursionCheck(curContext, assignEdge);
                } else if (src instanceof GlobalVarNode) {
                    newContext = EMPTY_CALLSTACK;
                }
                r0.prop(new VarAndContext(src, newContext));
            }
            Set<VarNode> matchSources = this.vMatches.vMatchInvLookup(curVar);
            boolean oneMatch = matchSources.size() == 1;
            Node[] loads = this.pag.loadInvLookup(curVar);
            for (Node node2 : loads) {
                FieldRefNode frNode = (FieldRefNode) node2;
                VarNode loadBase = frNode.getBase();
                SparkField field = frNode.getField();
                Iterator<Pair<VarNode, VarNode>> it = this.fieldToStores.get((SootUtil.FieldToEdgesMap) field).iterator();
                while (it.hasNext()) {
                    Pair<VarNode, VarNode> store = it.next();
                    VarNode storeBase = store.getO2();
                    PointsToSetInternal storeBaseP2Set = storeBase.getP2Set();
                    PointsToSetInternal loadBaseP2Set = loadBase.getP2Set();
                    VarNode matchSrc = store.getO1();
                    if (matchSources.contains(matchSrc)) {
                        boolean skipMatch = false;
                        if (oneMatch) {
                            PointsToSetInternal matchSrcPTo = matchSrc.getP2Set();
                            Set<SootMethod> matchSrcCallTargets = getCallTargets(matchSrcPTo, invokedMethod, receiverType, allTargets);
                            if (matchSrcCallTargets.size() <= 1) {
                                skipMatch = true;
                                for (SootMethod method2 : matchSrcCallTargets) {
                                    this.callSiteToResolvedTargets.put(callSiteAndContext, method2);
                                }
                            }
                        }
                        if (!skipMatch) {
                            PointsToSetInternal intersection = SootUtil.constructIntersection(storeBaseP2Set, loadBaseP2Set, this.pag);
                            boolean oldRefining = this.refiningCallSite;
                            int oldNesting = this.nesting;
                            try {
                                try {
                                    this.refiningCallSite = true;
                                    AllocAndContextSet allocContexts = findContextsForAllocs(new VarAndContext(loadBase, curContext), intersection);
                                    this.refiningCallSite = oldRefining;
                                    this.nesting = oldNesting;
                                    Iterator<AllocAndContext> it2 = allocContexts.iterator();
                                    while (it2.hasNext()) {
                                        AllocAndContext allocAndContext = it2.next();
                                        if (this.fieldCheckHeuristic.validFromBothEnds(field)) {
                                            matchSrcContexts = findUpContextsForVar(allocAndContext, new VarContextAndUp(storeBase, EMPTY_CALLSTACK, EMPTY_CALLSTACK));
                                        } else {
                                            matchSrcContexts = findVarContextsFromAlloc(allocAndContext, storeBase);
                                        }
                                        Iterator<ImmutableStack<Integer>> it3 = matchSrcContexts.iterator();
                                        while (it3.hasNext()) {
                                            ImmutableStack<Integer> matchSrcContext = it3.next();
                                            VarAndContext newVarAndContext = new VarAndContext(matchSrc, matchSrcContext);
                                            r0.prop(newVarAndContext);
                                        }
                                    }
                                } catch (CallSiteException e) {
                                    this.callSiteToResolvedTargets.putAll(callSiteAndContext, allTargets);
                                    this.refiningCallSite = oldRefining;
                                    this.nesting = oldNesting;
                                }
                            } catch (Throwable th) {
                                this.refiningCallSite = oldRefining;
                                this.nesting = oldNesting;
                                throw th;
                            }
                        }
                    }
                }
            }
        }
        if (DEBUG_VIRT) {
            debugPrint("call of " + invokedMethod + " on " + receiver + Instruction.argsep + origContext + " goes to " + this.callSiteToResolvedTargets.get(callSiteAndContext));
        }
        this.callGraphStack.pop();
        this.queriedCallSites.add(callSiteAndContext);
        return this.callSiteToResolvedTargets.get(callSiteAndContext);
    }

    protected boolean refineP2Set(VarAndContext varAndContext, final PointsToSetInternal badLocs) {
        this.nesting++;
        if (DEBUG) {
            debugPrint("refining " + varAndContext);
        }
        Set<VarAndContext> marked = new HashSet<>();
        Stack<VarAndContext> worklist = new Stack<>();
        final Propagator<VarAndContext> p = new Propagator<>(marked, worklist);
        p.prop(varAndContext);
        IncomingEdgeHandler edgeHandler = new IncomingEdgeHandler() { // from class: soot.jimple.spark.ondemand.DemandCSPointsTo.3
            boolean success = true;

            @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.IncomingEdgeHandler
            public void handleAlloc(AllocNode allocNode, VarAndContext origVarAndContext) {
                if (DemandCSPointsTo.this.doPointsTo && DemandCSPointsTo.this.pointsTo != null) {
                    DemandCSPointsTo.this.pointsTo.add(new AllocAndContext(allocNode, origVarAndContext.context));
                } else if (badLocs.contains(allocNode)) {
                    this.success = false;
                }
            }

            @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.IncomingEdgeHandler
            public void handleMatchSrc(VarNode matchSrc, PointsToSetInternal intersection, VarNode loadBase, VarNode storeBase, VarAndContext origVarAndContext, SparkField field, boolean refine) {
                CallingContextSet matchSrcContexts;
                AllocAndContextSet allocContexts = DemandCSPointsTo.this.findContextsForAllocs(new VarAndContext(loadBase, origVarAndContext.context), intersection);
                Iterator<AllocAndContext> it = allocContexts.iterator();
                while (it.hasNext()) {
                    AllocAndContext allocAndContext = it.next();
                    if (DemandCSPointsTo.DEBUG) {
                        DemandCSPointsTo.this.debugPrint("alloc and context " + allocAndContext);
                    }
                    if (DemandCSPointsTo.this.fieldCheckHeuristic.validFromBothEnds(field)) {
                        matchSrcContexts = DemandCSPointsTo.this.findUpContextsForVar(allocAndContext, new VarContextAndUp(storeBase, DemandCSPointsTo.EMPTY_CALLSTACK, DemandCSPointsTo.EMPTY_CALLSTACK));
                    } else {
                        matchSrcContexts = DemandCSPointsTo.this.findVarContextsFromAlloc(allocAndContext, storeBase);
                    }
                    Iterator<ImmutableStack<Integer>> it2 = matchSrcContexts.iterator();
                    while (it2.hasNext()) {
                        ImmutableStack<Integer> matchSrcContext = it2.next();
                        if (DemandCSPointsTo.DEBUG) {
                            DemandCSPointsTo.this.debugPrint("match source context " + matchSrcContext);
                        }
                        VarAndContext newVarAndContext = new VarAndContext(matchSrc, matchSrcContext);
                        p.prop(newVarAndContext);
                    }
                }
            }

            @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.IncomingEdgeHandler
            Object getResult() {
                return Boolean.valueOf(this.success);
            }

            @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.IncomingEdgeHandler
            void handleAssignSrc(VarAndContext newVarAndContext, VarAndContext origVarAndContext, AssignEdge assignEdge) {
                p.prop(newVarAndContext);
            }

            @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.IncomingEdgeHandler
            boolean shouldHandleSrc(VarNode src) {
                if (DemandCSPointsTo.this.doPointsTo) {
                    return true;
                }
                return src.getP2Set().hasNonEmptyIntersection(badLocs);
            }

            @Override // soot.jimple.spark.ondemand.DemandCSPointsTo.IncomingEdgeHandler
            boolean terminate() {
                return !this.success;
            }
        };
        processIncomingEdges(edgeHandler, worklist);
        this.nesting--;
        return ((Boolean) edgeHandler.getResult()).booleanValue();
    }

    protected boolean refineP2Set(VarNode v, PointsToSetInternal badLocs, HeuristicType heuristic) {
        boolean success;
        this.doPointsTo = false;
        this.fieldCheckHeuristic = HeuristicType.getHeuristic(heuristic, this.pag.getTypeManager(), getMaxPasses());
        this.numPasses = 0;
        do {
            this.numPasses++;
            if (this.numPasses > this.maxPasses) {
                return false;
            }
            if (DEBUG) {
                logger.debug("PASS " + this.numPasses);
                logger.debug(new StringBuilder().append(this.fieldCheckHeuristic).toString());
            }
            clearState();
            try {
                success = refineP2Set(new VarAndContext(v, EMPTY_CALLSTACK), badLocs);
            } catch (TerminateEarlyException e) {
                success = false;
            }
            if (success) {
                return true;
            }
        } while (this.fieldCheckHeuristic.runNewPass());
        return false;
    }

    protected boolean weirdCall(Integer callSite) {
        SootMethod invokedMethod = this.csInfo.getInvokedMethod(callSite);
        return SootUtil.isThreadStartMethod(invokedMethod) || SootUtil.isNewInstanceMethod(invokedMethod);
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjects(Context c, Local l) {
        throw new UnsupportedOperationException();
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjects(Context c, Local l, SootField f) {
        throw new UnsupportedOperationException();
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjects(Local l, SootField f) {
        throw new UnsupportedOperationException();
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjects(PointsToSet s, SootField f) {
        throw new UnsupportedOperationException();
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjects(SootField f) {
        throw new UnsupportedOperationException();
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjectsOfArrayElement(PointsToSet s) {
        throw new UnsupportedOperationException();
    }

    public PAG getPAG() {
        return this.pag;
    }

    public boolean usesCache() {
        return this.useCache;
    }

    public void enableCache() {
        this.useCache = true;
    }

    public void disableCache() {
        this.useCache = false;
    }

    public void clearCache() {
        this.reachingObjectsCache.clear();
        this.reachingObjectsCacheNoCGRefinement.clear();
    }

    public boolean isRefineCallGraph() {
        return this.refineCallGraph;
    }

    public void setRefineCallGraph(boolean refineCallGraph) {
        this.refineCallGraph = refineCallGraph;
    }

    public HeuristicType getHeuristicType() {
        return this.heuristicType;
    }

    public void setHeuristicType(HeuristicType heuristicType) {
        this.heuristicType = heuristicType;
        clearCache();
    }
}
