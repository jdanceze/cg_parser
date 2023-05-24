package soot.jimple.toolkits.thread.synchronization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.resource.spi.work.WorkException;
import org.apache.commons.cli.HelpFormatter;
import org.apache.tools.tar.TarConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.EquivalentValue;
import soot.G;
import soot.Local;
import soot.PhaseOptions;
import soot.PointsToAnalysis;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.coffi.Instruction;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.Jimple;
import soot.jimple.Ref;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.sets.HashPointsToSet;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.jimple.toolkits.infoflow.ClassInfoFlowAnalysis;
import soot.jimple.toolkits.infoflow.FakeJimpleLocal;
import soot.jimple.toolkits.infoflow.SmartMethodInfoFlowAnalysis;
import soot.jimple.toolkits.pointer.RWSet;
import soot.jimple.toolkits.thread.ThreadLocalObjectsAnalysis;
import soot.jimple.toolkits.thread.mhp.MhpTester;
import soot.jimple.toolkits.thread.mhp.SynchObliviousMhpAnalysis;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.graph.HashMutableEdgeLabelledDirectedGraph;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.LocalDefs;
import soot.util.Chain;
import soot.util.dot.DotGraphConstants;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/synchronization/LockAllocator.class */
public class LockAllocator extends SceneTransformer {
    private static final Logger logger = LoggerFactory.getLogger(LockAllocator.class);
    List<CriticalSection> criticalSections = null;
    CriticalSectionInterferenceGraph interferenceGraph = null;
    DirectedGraph deadlockGraph = null;
    boolean optionOneGlobalLock = false;
    boolean optionStaticLocks = false;
    boolean optionUseLocksets = false;
    boolean optionLeaveOriginalLocks = false;
    boolean optionIncludeEmptyPossibleEdges = false;
    boolean optionAvoidDeadlock = true;
    boolean optionOpenNesting = true;
    boolean optionDoMHP = false;
    boolean optionDoTLO = false;
    boolean optionOnFlyTLO = false;
    boolean optionPrintMhpSummary = true;
    boolean optionPrintGraph = false;
    boolean optionPrintTable = false;
    boolean optionPrintDebug = false;

    public LockAllocator(Singletons.Global g) {
    }

    public static LockAllocator v() {
        return G.v().soot_jimple_toolkits_thread_synchronization_LockAllocator();
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> options) {
        FlowSet fs;
        String lockingScheme = PhaseOptions.getString(options, "locking-scheme");
        if (lockingScheme.equals("fine-grained")) {
            this.optionOneGlobalLock = false;
            this.optionStaticLocks = false;
            this.optionUseLocksets = true;
            this.optionLeaveOriginalLocks = false;
        }
        if (lockingScheme.equals("medium-grained")) {
            this.optionOneGlobalLock = false;
            this.optionStaticLocks = false;
            this.optionUseLocksets = false;
            this.optionLeaveOriginalLocks = false;
        }
        if (lockingScheme.equals("coarse-grained")) {
            this.optionOneGlobalLock = false;
            this.optionStaticLocks = true;
            this.optionUseLocksets = false;
            this.optionLeaveOriginalLocks = false;
        }
        if (lockingScheme.equals("single-static")) {
            this.optionOneGlobalLock = true;
            this.optionStaticLocks = true;
            this.optionUseLocksets = false;
            this.optionLeaveOriginalLocks = false;
        }
        if (lockingScheme.equals("leave-original")) {
            this.optionOneGlobalLock = false;
            this.optionStaticLocks = false;
            this.optionUseLocksets = false;
            this.optionLeaveOriginalLocks = true;
        }
        this.optionAvoidDeadlock = PhaseOptions.getBoolean(options, "avoid-deadlock");
        this.optionOpenNesting = PhaseOptions.getBoolean(options, "open-nesting");
        this.optionDoMHP = PhaseOptions.getBoolean(options, "do-mhp");
        this.optionDoTLO = PhaseOptions.getBoolean(options, "do-tlo");
        this.optionPrintGraph = PhaseOptions.getBoolean(options, "print-graph");
        this.optionPrintTable = PhaseOptions.getBoolean(options, "print-table");
        this.optionPrintDebug = PhaseOptions.getBoolean(options, "print-debug");
        MhpTester mhp = null;
        if (this.optionDoMHP && (Scene.v().getPointsToAnalysis() instanceof PAG)) {
            logger.debug("[wjtp.tn] *** Build May-Happen-in-Parallel Info *** " + new Date());
            mhp = new SynchObliviousMhpAnalysis();
            if (this.optionPrintMhpSummary) {
                mhp.printMhpSummary();
            }
        }
        ThreadLocalObjectsAnalysis tlo = null;
        if (this.optionDoTLO) {
            logger.debug("[wjtp.tn] *** Find Thread-Local Objects *** " + new Date());
            if (mhp != null) {
                tlo = new ThreadLocalObjectsAnalysis(mhp);
            } else {
                tlo = new ThreadLocalObjectsAnalysis(new SynchObliviousMhpAnalysis());
            }
            if (!this.optionOnFlyTLO) {
                tlo.precompute();
                logger.debug("[wjtp.tn] TLO totals (#analyzed/#encountered): " + SmartMethodInfoFlowAnalysis.counter + "/" + ClassInfoFlowAnalysis.methodCount);
            } else {
                logger.debug("[wjtp.tn] TLO so far (#analyzed/#encountered): " + SmartMethodInfoFlowAnalysis.counter + "/" + ClassInfoFlowAnalysis.methodCount);
            }
        }
        Date start = new Date();
        logger.debug("[wjtp.tn] *** Find and Name Transactions *** " + start);
        Map<SootMethod, FlowSet> methodToFlowSet = new HashMap<>();
        Map<SootMethod, ExceptionalUnitGraph> methodToExcUnitGraph = new HashMap<>();
        for (SootClass appClass : Scene.v().getApplicationClasses()) {
            for (SootMethod method : appClass.getMethods()) {
                if (method.isConcrete()) {
                    Body b = method.retrieveActiveBody();
                    ExceptionalUnitGraph eug = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b);
                    methodToExcUnitGraph.put(method, eug);
                    SynchronizedRegionFinder ta = new SynchronizedRegionFinder(eug, b, this.optionPrintDebug, this.optionOpenNesting, tlo);
                    Chain<Unit> units = b.getUnits();
                    Unit lastUnit = units.getLast();
                    methodToFlowSet.put(method, ta.getFlowBefore(lastUnit));
                }
            }
        }
        this.criticalSections = new Vector();
        for (FlowSet fs2 : methodToFlowSet.values()) {
            List fList = fs2.toList();
            for (int i = 0; i < fList.size(); i++) {
                this.criticalSections.add(((SynchronizedRegionFlowPair) fList.get(i)).tn);
            }
        }
        assignNamesToTransactions(this.criticalSections);
        if (this.optionOnFlyTLO) {
            logger.debug("[wjtp.tn] TLO so far (#analyzed/#encountered): " + SmartMethodInfoFlowAnalysis.counter + "/" + ClassInfoFlowAnalysis.methodCount);
        }
        logger.debug("[wjtp.tn] *** Find Transitive Read/Write Sets *** " + new Date());
        PointsToAnalysis pta = Scene.v().getPointsToAnalysis();
        CriticalSectionAwareSideEffectAnalysis tasea = new CriticalSectionAwareSideEffectAnalysis(pta, Scene.v().getCallGraph(), this.optionOpenNesting ? this.criticalSections : null, tlo);
        for (CriticalSection tn : this.criticalSections) {
            Iterator<Unit> it = tn.invokes.iterator();
            while (it.hasNext()) {
                Unit unit = it.next();
                Stmt stmt = (Stmt) unit;
                HashSet uses = new HashSet();
                RWSet stmtRead = tasea.readSet(tn.method, stmt, tn, uses);
                if (stmtRead != null) {
                    tn.read.union(stmtRead);
                }
                RWSet stmtWrite = tasea.writeSet(tn.method, stmt, tn, uses);
                if (stmtWrite != null) {
                    tn.write.union(stmtWrite);
                }
            }
        }
        long longTime = (new Date().getTime() - start.getTime()) / 100;
        float time = ((float) longTime) / 10.0f;
        if (this.optionOnFlyTLO) {
            logger.debug("[wjtp.tn] TLO totals (#analyzed/#encountered): " + SmartMethodInfoFlowAnalysis.counter + "/" + ClassInfoFlowAnalysis.methodCount);
            logger.debug("[wjtp.tn] Time for stages utilizing on-fly TLO: " + time + "s");
        }
        logger.debug("[wjtp.tn] *** Calculate Locking Groups *** " + new Date());
        CriticalSectionInterferenceGraph ig = new CriticalSectionInterferenceGraph(this.criticalSections, mhp, this.optionOneGlobalLock, this.optionLeaveOriginalLocks, this.optionIncludeEmptyPossibleEdges);
        this.interferenceGraph = ig;
        logger.debug("[wjtp.tn] *** Detect the Possibility of Deadlock *** " + new Date());
        DeadlockDetector dd = new DeadlockDetector(this.optionPrintDebug, this.optionAvoidDeadlock, true, this.criticalSections);
        if (!this.optionUseLocksets) {
            this.deadlockGraph = dd.detectComponentBasedDeadlock();
        }
        logger.debug("[wjtp.tn] *** Calculate Locking Objects *** " + new Date());
        if (!this.optionStaticLocks) {
            for (CriticalSection tn2 : this.criticalSections) {
                if (tn2.setNumber > 0) {
                    Iterator<CriticalSectionDataDependency> it2 = tn2.edges.iterator();
                    while (it2.hasNext()) {
                        CriticalSectionDataDependency tdd = it2.next();
                        tn2.group.rwSet.union(tdd.rw);
                    }
                }
            }
        }
        Map<Value, Integer> lockToLockNum = null;
        List<PointsToSetInternal> lockPTSets = null;
        if (this.optionLeaveOriginalLocks) {
            analyzeExistingLocks(this.criticalSections, ig);
        } else if (this.optionStaticLocks) {
            setFlagsForStaticAllocations(ig);
        } else {
            setFlagsForDynamicAllocations(ig);
            lockPTSets = new ArrayList<>();
            lockToLockNum = new HashMap<>();
            findLockableReferences(this.criticalSections, pta, tasea, lockToLockNum, lockPTSets);
            if (this.optionUseLocksets) {
                for (CriticalSection tn3 : this.criticalSections) {
                    if (tn3.group != null) {
                        logger.debug("[wjtp.tn] " + tn3.name + " lockset: " + locksetToLockNumString(tn3.lockset, lockToLockNum) + (tn3.group.useLocksets ? "" : " (placeholder)"));
                    }
                }
            }
        }
        if (this.optionUseLocksets) {
            logger.debug("[wjtp.tn] *** Detect " + (this.optionAvoidDeadlock ? "and Correct " : "") + "the Possibility of Deadlock for Locksets *** " + new Date());
            this.deadlockGraph = dd.detectLocksetDeadlock(lockToLockNum, lockPTSets);
            if (this.optionPrintDebug) {
                ((HashMutableEdgeLabelledDirectedGraph) this.deadlockGraph).printGraph();
            }
            logger.debug("[wjtp.tn] *** Reorder Locksets to Avoid Deadlock *** " + new Date());
            dd.reorderLocksets(lockToLockNum, (HashMutableEdgeLabelledDirectedGraph) this.deadlockGraph);
        }
        logger.debug("[wjtp.tn] *** Print Output and Transform Program *** " + new Date());
        if (this.optionPrintGraph) {
            printGraph(this.criticalSections, ig, lockToLockNum);
        }
        if (this.optionPrintTable) {
            printTable(this.criticalSections, mhp);
            printGroups(this.criticalSections, ig);
        }
        if (!this.optionLeaveOriginalLocks) {
            boolean[] insertedGlobalLock = new boolean[ig.groupCount()];
            insertedGlobalLock[0] = false;
            for (int i2 = 1; i2 < ig.groupCount(); i2++) {
                CriticalSectionGroup tnGroup = ig.groups().get(i2);
                insertedGlobalLock[i2] = !this.optionOneGlobalLock && (tnGroup.useDynamicLock || tnGroup.useLocksets);
            }
            for (SootClass appClass2 : Scene.v().getApplicationClasses()) {
                for (SootMethod method2 : appClass2.getMethods()) {
                    if (method2.isConcrete() && (fs = methodToFlowSet.get(method2)) != null) {
                        LockAllocationBodyTransformer.v().internalTransform(method2.getActiveBody(), fs, ig.groups(), insertedGlobalLock);
                    }
                }
            }
        }
    }

    protected void findLockableReferences(List<CriticalSection> AllTransactions, PointsToAnalysis pta, CriticalSectionAwareSideEffectAnalysis tasea, Map<Value, Integer> lockToLockNum, List<PointsToSetInternal> lockPTSets) {
        PointsToSetInternal lockPT;
        for (CriticalSection tn : AllTransactions) {
            int group = tn.setNumber - 1;
            if (group >= 0 && (tn.group.useDynamicLock || tn.group.useLocksets)) {
                logger.debug("[wjtp.tn] * " + tn.name + " *");
                LockableReferenceAnalysis la = new LockableReferenceAnalysis(new BriefUnitGraph(tn.method.retrieveActiveBody()));
                tn.lockset = la.getLocksetOf(tasea, tn.group.rwSet, tn);
                if (this.optionUseLocksets) {
                    if (tn.lockset == null || tn.lockset.size() <= 0) {
                        tn.group.useLocksets = false;
                        Value newStaticLock = new NewStaticLock(tn.method.getDeclaringClass());
                        EquivalentValue newStaticLockEqVal = new EquivalentValue(newStaticLock);
                        Iterator<CriticalSection> it = tn.group.iterator();
                        while (it.hasNext()) {
                            CriticalSection groupTn = it.next();
                            groupTn.lockset = new ArrayList();
                            groupTn.lockset.add(newStaticLockEqVal);
                        }
                        Integer lockNum = new Integer(-lockPTSets.size());
                        logger.debug("[wjtp.tn] Lock: num " + lockNum + " type " + newStaticLock.getType() + " obj " + newStaticLock);
                        lockToLockNum.put(newStaticLockEqVal, lockNum);
                        lockToLockNum.put(newStaticLock, lockNum);
                        PointsToSetInternal dummyLockPT = new HashPointsToSet(newStaticLock.getType(), (PAG) pta);
                        lockPTSets.add(dummyLockPT);
                    } else {
                        for (EquivalentValue lockEqVal : tn.lockset) {
                            Value lock = lockEqVal.getValue();
                            if (lock instanceof Local) {
                                lockPT = (PointsToSetInternal) pta.reachingObjects((Local) lock);
                            } else if (lock instanceof StaticFieldRef) {
                                lockPT = null;
                            } else if (lock instanceof InstanceFieldRef) {
                                Local base = (Local) ((InstanceFieldRef) lock).getBase();
                                if (base instanceof FakeJimpleLocal) {
                                    lockPT = (PointsToSetInternal) pta.reachingObjects(((FakeJimpleLocal) base).getRealLocal(), ((FieldRef) lock).getField());
                                } else {
                                    lockPT = (PointsToSetInternal) pta.reachingObjects(base, ((FieldRef) lock).getField());
                                }
                            } else if (lock instanceof NewStaticLock) {
                                lockPT = null;
                            } else {
                                lockPT = null;
                            }
                            if (lockPT != null) {
                                boolean foundLock = false;
                                int i = 0;
                                while (true) {
                                    if (i >= lockPTSets.size()) {
                                        break;
                                    }
                                    PointsToSetInternal otherLockPT = lockPTSets.get(i);
                                    if (!lockPT.hasNonEmptyIntersection(otherLockPT)) {
                                        i++;
                                    } else {
                                        logger.debug("[wjtp.tn] Lock: num " + i + " type " + lock.getType() + " obj " + lock);
                                        lockToLockNum.put(lock, new Integer(i));
                                        otherLockPT.addAll(lockPT, null);
                                        foundLock = true;
                                        break;
                                    }
                                }
                                if (!foundLock) {
                                    logger.debug("[wjtp.tn] Lock: num " + lockPTSets.size() + " type " + lock.getType() + " obj " + lock);
                                    lockToLockNum.put(lock, new Integer(lockPTSets.size()));
                                    PointsToSetInternal otherLockPT2 = new HashPointsToSet(lockPT.getType(), (PAG) pta);
                                    lockPTSets.add(otherLockPT2);
                                    otherLockPT2.addAll(lockPT, null);
                                }
                            } else if (lockToLockNum.get(lockEqVal) != null) {
                                Integer lockNum2 = lockToLockNum.get(lockEqVal);
                                logger.debug("[wjtp.tn] Lock: num " + lockNum2 + " type " + lock.getType() + " obj " + lock);
                                lockToLockNum.put(lock, lockNum2);
                            } else {
                                Integer lockNum3 = new Integer(-lockPTSets.size());
                                logger.debug("[wjtp.tn] Lock: num " + lockNum3 + " type " + lock.getType() + " obj " + lock);
                                lockToLockNum.put(lockEqVal, lockNum3);
                                lockToLockNum.put(lock, lockNum3);
                                PointsToSetInternal dummyLockPT2 = new HashPointsToSet(lock.getType(), (PAG) pta);
                                lockPTSets.add(dummyLockPT2);
                            }
                        }
                    }
                } else if (tn.lockset == null || tn.lockset.size() != 1) {
                    tn.lockObject = null;
                    tn.group.useDynamicLock = false;
                    tn.group.lockObject = null;
                } else {
                    tn.lockObject = tn.lockset.get(0);
                    if (tn.group.lockObject == null || (tn.lockObject instanceof Ref)) {
                        tn.group.lockObject = tn.lockObject;
                    }
                }
            }
        }
        if (this.optionUseLocksets) {
            for (int i2 = 0; i2 < lockPTSets.size(); i2++) {
                PointsToSetInternal pts = lockPTSets.get(i2);
                pts.size();
            }
        }
    }

    public void setFlagsForDynamicAllocations(CriticalSectionInterferenceGraph ig) {
        for (int group = 0; group < ig.groupCount() - 1; group++) {
            CriticalSectionGroup tnGroup = ig.groups().get(group + 1);
            if (this.optionUseLocksets) {
                tnGroup.useLocksets = true;
            } else {
                tnGroup.isDynamicLock = tnGroup.rwSet.getGlobals().size() == 0;
                tnGroup.useDynamicLock = true;
                tnGroup.lockObject = null;
            }
            if (tnGroup.rwSet.size() <= 0) {
                if (this.optionUseLocksets) {
                    tnGroup.useLocksets = false;
                } else {
                    tnGroup.isDynamicLock = false;
                    tnGroup.useDynamicLock = false;
                }
            }
        }
    }

    public void setFlagsForStaticAllocations(CriticalSectionInterferenceGraph ig) {
        for (int group = 0; group < ig.groupCount() - 1; group++) {
            CriticalSectionGroup tnGroup = ig.groups().get(group + 1);
            tnGroup.isDynamicLock = false;
            tnGroup.useDynamicLock = false;
            tnGroup.lockObject = null;
        }
    }

    private void analyzeExistingLocks(List<CriticalSection> AllTransactions, CriticalSectionInterferenceGraph ig) {
        List<Unit> rDefs;
        setFlagsForStaticAllocations(ig);
        for (CriticalSection tn : AllTransactions) {
            if (tn.setNumber > 0) {
                LocalDefs ld = G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs(tn.method.retrieveActiveBody());
                if (tn.origLock != null && (tn.origLock instanceof Local) && (rDefs = ld.getDefsOfAt((Local) tn.origLock, tn.entermonitor)) != null) {
                    for (Unit u : rDefs) {
                        Stmt next = (Stmt) u;
                        if (next instanceof DefinitionStmt) {
                            Value rightOp = ((DefinitionStmt) next).getRightOp();
                            if (rightOp instanceof FieldRef) {
                                if (((FieldRef) rightOp).getField().isStatic()) {
                                    tn.group.lockObject = rightOp;
                                } else {
                                    tn.group.isDynamicLock = true;
                                    tn.group.useDynamicLock = true;
                                    tn.group.lockObject = tn.origLock;
                                }
                            } else {
                                tn.group.isDynamicLock = true;
                                tn.group.useDynamicLock = true;
                                tn.group.lockObject = tn.origLock;
                            }
                        } else {
                            tn.group.isDynamicLock = true;
                            tn.group.useDynamicLock = true;
                            tn.group.lockObject = tn.origLock;
                        }
                    }
                }
            }
        }
    }

    public static String locksetToLockNumString(List<EquivalentValue> lockset, Map<Value, Integer> lockToLockNum) {
        if (lockset == null) {
            return Jimple.NULL;
        }
        String ret = "[";
        boolean first = true;
        for (EquivalentValue lockEqVal : lockset) {
            if (!first) {
                ret = String.valueOf(ret) + Instruction.argsep;
            }
            first = false;
            ret = String.valueOf(ret) + lockToLockNum.get(lockEqVal.getValue());
        }
        return String.valueOf(ret) + "]";
    }

    public void assignNamesToTransactions(List<CriticalSection> AllTransactions) {
        List<String> methodNamesTemp = new ArrayList<>();
        for (CriticalSection tn1 : AllTransactions) {
            String mname = tn1.method.getSignature();
            if (!methodNamesTemp.contains(mname)) {
                methodNamesTemp.add(mname);
            }
        }
        String[] methodNames = (String[]) methodNamesTemp.toArray(new String[1]);
        Arrays.sort(methodNames);
        int[][] identMatrix = new int[methodNames.length][(CriticalSection.nextIDNum - methodNames.length) + 2];
        for (int i = 0; i < methodNames.length; i++) {
            identMatrix[i][0] = 0;
            for (int j = 1; j < (CriticalSection.nextIDNum - methodNames.length) + 1; j++) {
                identMatrix[i][j] = 50000;
            }
        }
        for (CriticalSection tn12 : AllTransactions) {
            int methodNum = Arrays.binarySearch(methodNames, tn12.method.getSignature());
            int[] iArr = identMatrix[methodNum];
            iArr[0] = iArr[0] + 1;
            identMatrix[methodNum][identMatrix[methodNum][0]] = tn12.IDNum;
        }
        for (int j2 = 0; j2 < methodNames.length; j2++) {
            identMatrix[j2][0] = 0;
            Arrays.sort(identMatrix[j2]);
        }
        for (CriticalSection tn13 : AllTransactions) {
            int methodNum2 = Arrays.binarySearch(methodNames, tn13.method.getSignature());
            int tnNum = Arrays.binarySearch(identMatrix[methodNum2], tn13.IDNum) - 1;
            tn13.name = "m" + (methodNum2 < 10 ? TarConstants.VERSION_POSIX : methodNum2 < 100 ? WorkException.UNDEFINED : "") + methodNum2 + "n" + (tnNum < 10 ? WorkException.UNDEFINED : "") + tnNum;
        }
    }

    public void printGraph(Collection<CriticalSection> AllTransactions, CriticalSectionInterferenceGraph ig, Map<Value, Integer> lockToLockNum) {
        CriticalSection tnedge;
        String objString;
        String typeString;
        String[] colors = {"black", "blue", "blueviolet", "chartreuse", "crimson", "darkgoldenrod1", "darkseagreen", "darkslategray", "deeppink", "deepskyblue1", "firebrick1", "forestgreen", "gold", "gray80", "navy", "pink", "red", "sienna", "turquoise1", "yellow"};
        Map<Integer, String> lockColors = new HashMap<>();
        int colorNum = 0;
        HashSet<CriticalSection> visited = new HashSet<>();
        logger.debug("[transaction-graph]" + (this.optionUseLocksets ? "" : " strict") + " graph transactions {");
        for (int group = 0; group < ig.groups().size(); group++) {
            boolean printedHeading = false;
            for (CriticalSection tn : AllTransactions) {
                if (tn.setNumber == group + 1) {
                    if (!printedHeading) {
                        if (tn.group.useDynamicLock && tn.group.lockObject != null) {
                            if (tn.group.lockObject.getType() instanceof RefType) {
                                typeString = ((RefType) tn.group.lockObject.getType()).getSootClass().getShortName();
                            } else {
                                typeString = tn.group.lockObject.getType().toString();
                            }
                            logger.debug("[transaction-graph] subgraph cluster_" + (group + 1) + " {\n[transaction-graph] color=blue;\n[transaction-graph] label=\"Lock: a \\n" + typeString + " object\";");
                        } else if (tn.group.useLocksets) {
                            logger.debug("[transaction-graph] subgraph cluster_" + (group + 1) + " {\n[transaction-graph] color=blue;\n[transaction-graph] label=\"Locksets\";");
                        } else {
                            if (tn.group.lockObject == null) {
                                objString = "lockObj" + (group + 1);
                            } else if (tn.group.lockObject instanceof FieldRef) {
                                SootField field = ((FieldRef) tn.group.lockObject).getField();
                                objString = String.valueOf(field.getDeclaringClass().getShortName()) + "." + field.getName();
                            } else {
                                objString = tn.group.lockObject.toString();
                            }
                            logger.debug("[transaction-graph] subgraph cluster_" + (group + 1) + " {\n[transaction-graph] color=blue;\n[transaction-graph] label=\"Lock: \\n" + objString + "\";");
                        }
                        printedHeading = true;
                    }
                    if (Scene.v().getReachableMethods().contains(tn.method)) {
                        logger.debug("[transaction-graph] " + tn.name + " [name=\"" + tn.method.toString() + "\" style=\"setlinewidth(3)\"];");
                    } else {
                        logger.debug("[transaction-graph] " + tn.name + " [name=\"" + tn.method.toString() + "\" color=cadetblue1 style=\"setlinewidth(1)\"];");
                    }
                    if (tn.group.useLocksets) {
                        for (EquivalentValue lockEqVal : tn.lockset) {
                            Integer lockNum = lockToLockNum.get(lockEqVal.getValue());
                            Iterator<CriticalSection> it = tn.group.iterator();
                            while (it.hasNext()) {
                                CriticalSection tn2 = it.next();
                                if (!visited.contains(tn2) && ig.mayHappenInParallel(tn, tn2)) {
                                    for (EquivalentValue lock2EqVal : tn2.lockset) {
                                        Integer lock2Num = lockToLockNum.get(lock2EqVal.getValue());
                                        if (lockNum.intValue() == lock2Num.intValue()) {
                                            if (!lockColors.containsKey(lockNum)) {
                                                lockColors.put(lockNum, colors[colorNum % colors.length]);
                                                colorNum++;
                                            }
                                            String color = lockColors.get(lockNum);
                                            logger.debug("[transaction-graph] " + tn.name + " -- " + tn2.name + " [color=" + color + " style=" + (lockNum.intValue() >= 0 ? DotGraphConstants.NODE_STYLE_DASHED : "solid") + " exactsize=1 style=\"setlinewidth(3)\"];");
                                        }
                                    }
                                }
                            }
                            visited.add(tn);
                        }
                    } else {
                        Iterator<CriticalSectionDataDependency> tnedgeit = tn.edges.iterator();
                        while (tnedgeit.hasNext()) {
                            CriticalSectionDataDependency edge = tnedgeit.next();
                            if (edge.other.setNumber == group + 1) {
                                logger.debug("[transaction-graph] " + tn.name + " -- " + tnedge.name + " [color=" + (edge.size > 0 ? "black" : "cadetblue1") + " style=" + ((tn.setNumber <= 0 || !tn.group.useDynamicLock) ? "solid" : DotGraphConstants.NODE_STYLE_DASHED) + " exactsize=" + edge.size + " style=\"setlinewidth(3)\"];");
                            }
                        }
                    }
                }
            }
            if (printedHeading) {
                logger.debug("[transaction-graph] }");
            }
        }
        boolean printedHeading2 = false;
        for (CriticalSection tn3 : AllTransactions) {
            if (tn3.setNumber == -1) {
                if (!printedHeading2) {
                    logger.debug("[transaction-graph] subgraph lone {\n[transaction-graph] rank=source;");
                    printedHeading2 = true;
                }
                if (Scene.v().getReachableMethods().contains(tn3.method)) {
                    logger.debug("[transaction-graph] " + tn3.name + " [name=\"" + tn3.method.toString() + "\" style=\"setlinewidth(3)\"];");
                } else {
                    logger.debug("[transaction-graph] " + tn3.name + " [name=\"" + tn3.method.toString() + "\" color=cadetblue1 style=\"setlinewidth(1)\"];");
                }
                Iterator<CriticalSectionDataDependency> tnedgeit2 = tn3.edges.iterator();
                while (tnedgeit2.hasNext()) {
                    CriticalSectionDataDependency edge2 = tnedgeit2.next();
                    CriticalSection tnedge2 = edge2.other;
                    if (tnedge2.setNumber != tn3.setNumber || tnedge2.setNumber == -1) {
                        logger.debug("[transaction-graph] " + tn3.name + " -- " + tnedge2.name + " [color=" + (edge2.size > 0 ? "black" : "cadetblue1") + " style=" + ((tn3.setNumber <= 0 || !tn3.group.useDynamicLock) ? "solid" : DotGraphConstants.NODE_STYLE_DASHED) + " exactsize=" + edge2.size + " style=\"setlinewidth(1)\"];");
                    }
                }
            }
        }
        if (printedHeading2) {
            logger.debug("[transaction-graph] }");
        }
        logger.debug("[transaction-graph] }");
    }

    public void printTable(Collection<CriticalSection> AllTransactions, MhpTester mhp) {
        String str;
        logger.debug("[transaction-table] ");
        for (CriticalSection tn : AllTransactions) {
            boolean mhpself = false;
            ReachableMethods rm = Scene.v().getReachableMethods();
            boolean reachable = rm.contains(tn.method);
            if (mhp != null) {
                mhpself = mhp.mayHappenInParallel(tn.method, tn.method);
            }
            logger.debug("[transaction-table] Transaction " + tn.name + (reachable ? " reachable" : " dead") + (mhpself ? " [called from >= 2 threads]" : " [called from <= 1 thread]"));
            logger.debug("[transaction-table] Where: " + tn.method.getDeclaringClass().toString() + ":" + tn.method.toString() + ":  ");
            logger.debug("[transaction-table] Orig : " + tn.origLock);
            logger.debug("[transaction-table] Prep : " + tn.prepStmt);
            logger.debug("[transaction-table] Begin: " + tn.entermonitor);
            logger.debug("[transaction-table] End  : early:" + tn.earlyEnds.toString() + " exc:" + tn.exceptionalEnd + " through:" + tn.end + " \n");
            logger.debug("[transaction-table] Size : " + tn.units.size());
            if (tn.read.size() < 100) {
                logger.debug("[transaction-table] Read : " + tn.read.size() + "\n[transaction-table] " + tn.read.toString().replaceAll("\\[", "     : [").replaceAll("\n", "\n[transaction-table] "));
            } else {
                logger.debug("[transaction-table] Read : " + tn.read.size() + "  \n[transaction-table] ");
            }
            if (tn.write.size() < 100) {
                logger.debug("Write: " + tn.write.size() + "\n[transaction-table] " + tn.write.toString().replaceAll("\\[", "     : [").replaceAll("\n", "\n[transaction-table] "));
            } else {
                logger.debug("Write: " + tn.write.size() + "\n[transaction-table] ");
            }
            logger.debug("Edges: (" + tn.edges.size() + ") ");
            Iterator<CriticalSectionDataDependency> tnedgeit = tn.edges.iterator();
            while (tnedgeit.hasNext()) {
                logger.debug(tnedgeit.next().other.name + Instruction.argsep);
            }
            if (tn.group != null && tn.group.useLocksets) {
                logger.debug("\n[transaction-table] Locks: " + tn.lockset);
            } else {
                Logger logger2 = logger;
                StringBuilder sb = new StringBuilder("\n[transaction-table] Lock : ");
                if (tn.setNumber == -1) {
                    str = HelpFormatter.DEFAULT_OPT_PREFIX;
                } else if (tn.lockObject == null) {
                    str = "Global";
                } else {
                    str = String.valueOf(tn.lockObject.toString()) + (tn.lockObjectArrayIndex == null ? "" : "[" + tn.lockObjectArrayIndex + "]");
                }
                logger2.debug(sb.append(str).toString());
            }
            logger.debug("[transaction-table] Group: " + tn.setNumber + "\n[transaction-table] ");
        }
    }

    public void printGroups(Collection<CriticalSection> AllTransactions, CriticalSectionInterferenceGraph ig) {
        String str;
        logger.debug("[transaction-groups] Group Summaries\n[transaction-groups] ");
        for (int group = 0; group < ig.groupCount() - 1; group++) {
            CriticalSectionGroup tnGroup = ig.groups().get(group + 1);
            if (tnGroup.size() > 0) {
                logger.debug("Group " + (group + 1) + Instruction.argsep);
                Logger logger2 = logger;
                StringBuilder sb = new StringBuilder("Locking: ");
                if (tnGroup.useLocksets) {
                    str = "using ";
                } else {
                    str = (tnGroup.isDynamicLock && tnGroup.useDynamicLock) ? "Dynamic on " : "Static on ";
                }
                logger2.debug(sb.append(str).append(tnGroup.useLocksets ? "locksets" : tnGroup.lockObject == null ? Jimple.NULL : tnGroup.lockObject.toString()).toString());
                logger.debug("\n[transaction-groups]      : ");
                for (CriticalSection tn : AllTransactions) {
                    if (tn.setNumber == group + 1) {
                        logger.debug(tn.name + Instruction.argsep);
                    }
                }
                logger.debug("\n[transaction-groups] " + tnGroup.rwSet.toString().replaceAll("\\[", "     : [").replaceAll("\n", "\n[transaction-groups] "));
            }
        }
        logger.debug("Erasing \n[transaction-groups]      : ");
        for (CriticalSection tn2 : AllTransactions) {
            if (tn2.setNumber == -1) {
                logger.debug(tn2.name + Instruction.argsep);
            }
        }
        logger.debug("\n[transaction-groups] ");
    }

    public CriticalSectionInterferenceGraph getInterferenceGraph() {
        return this.interferenceGraph;
    }

    public DirectedGraph getDeadlockGraph() {
        return this.deadlockGraph;
    }

    public List<CriticalSection> getCriticalSections() {
        return this.criticalSections;
    }
}
