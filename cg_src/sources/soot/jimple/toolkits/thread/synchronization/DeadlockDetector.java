package soot.jimple.toolkits.thread.synchronization;

import android.net.wifi.WifiConfiguration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.EquivalentValue;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.Unit;
import soot.Value;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.sets.HashPointsToSet;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.TransitiveTargets;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.HashMutableDirectedGraph;
import soot.toolkits.graph.HashMutableEdgeLabelledDirectedGraph;
import soot.toolkits.graph.MutableDirectedGraph;
import soot.toolkits.graph.MutableEdgeLabelledDirectedGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/synchronization/DeadlockDetector.class */
public class DeadlockDetector {
    private static final Logger logger = LoggerFactory.getLogger(DeadlockDetector.class);
    boolean optionPrintDebug;
    boolean optionRepairDeadlock;
    boolean optionAllowSelfEdges;
    List<CriticalSection> criticalSections;
    TransitiveTargets tt;

    public DeadlockDetector(boolean optionPrintDebug, boolean optionRepairDeadlock, boolean optionAllowSelfEdges, List<CriticalSection> criticalSections) {
        this.optionPrintDebug = optionPrintDebug;
        this.optionRepairDeadlock = optionRepairDeadlock;
        this.optionAllowSelfEdges = optionAllowSelfEdges && !optionRepairDeadlock;
        this.criticalSections = criticalSections;
        this.tt = new TransitiveTargets(Scene.v().getCallGraph(), new Filter(new CriticalSectionVisibleEdgesPred(null)));
    }

    public MutableDirectedGraph<CriticalSectionGroup> detectComponentBasedDeadlock() {
        MutableDirectedGraph<CriticalSectionGroup> lockOrder;
        int iteration = 0;
        do {
            iteration++;
            logger.debug("[DeadlockDetector] Deadlock Iteration #" + iteration);
            boolean foundDeadlock = false;
            lockOrder = new HashMutableDirectedGraph<>();
            Iterator<CriticalSection> deadlockIt1 = this.criticalSections.iterator();
            while (deadlockIt1.hasNext() && !foundDeadlock) {
                CriticalSection tn1 = deadlockIt1.next();
                if (tn1.setNumber > 0) {
                    if (!lockOrder.containsNode(tn1.group)) {
                        lockOrder.addNode(tn1.group);
                    }
                    if (tn1.transitiveTargets == null) {
                        tn1.transitiveTargets = new HashSet<>();
                        Iterator<Unit> it = tn1.invokes.iterator();
                        while (it.hasNext()) {
                            Unit tn1Invoke = it.next();
                            Iterator<MethodOrMethodContext> targetIt = this.tt.iterator(tn1Invoke);
                            while (targetIt.hasNext()) {
                                tn1.transitiveTargets.add(targetIt.next());
                            }
                        }
                    }
                    Iterator<CriticalSection> deadlockIt2 = this.criticalSections.iterator();
                    while (deadlockIt2.hasNext() && (!this.optionRepairDeadlock || !foundDeadlock)) {
                        CriticalSection tn2 = deadlockIt2.next();
                        if (tn2.setNumber > 0 && (tn2.setNumber != tn1.setNumber || this.optionAllowSelfEdges)) {
                            if (!lockOrder.containsNode(tn2.group)) {
                                lockOrder.addNode(tn2.group);
                            }
                            if (tn1.transitiveTargets.contains(tn2.method)) {
                                if (this.optionPrintDebug) {
                                    logger.debug(WifiConfiguration.GroupCipher.varName + tn1.setNumber + " before group" + tn2.setNumber + ": outer: " + tn1.name + " inner: " + tn2.name);
                                }
                                List<CriticalSectionGroup> afterTn2 = new ArrayList<>();
                                afterTn2.addAll(lockOrder.getSuccsOf(tn2.group));
                                for (int i = 0; i < afterTn2.size(); i++) {
                                    for (CriticalSectionGroup o : lockOrder.getSuccsOf(afterTn2.get(i))) {
                                        if (!afterTn2.contains(o)) {
                                            afterTn2.add(o);
                                        }
                                    }
                                }
                                if (afterTn2.contains(tn1.group)) {
                                    if (!this.optionRepairDeadlock) {
                                        logger.debug("[DeadlockDetector]  DEADLOCK HAS BEEN DETECTED: not correcting");
                                        foundDeadlock = true;
                                    } else {
                                        logger.debug("[DeadlockDetector]  DEADLOCK HAS BEEN DETECTED: merging group" + tn1.setNumber + " and group" + tn2.setNumber + " and restarting deadlock detection");
                                        if (this.optionPrintDebug) {
                                            logger.debug("tn1.setNumber was " + tn1.setNumber + " and tn2.setNumber was " + tn2.setNumber);
                                            logger.debug("tn1.group.size was " + tn1.group.criticalSections.size() + " and tn2.group.size was " + tn2.group.criticalSections.size());
                                            logger.debug("tn1.group.num was  " + tn1.group.num() + " and tn2.group.num was  " + tn2.group.num());
                                        }
                                        tn1.group.mergeGroups(tn2.group);
                                        if (this.optionPrintDebug) {
                                            logger.debug("tn1.setNumber is  " + tn1.setNumber + " and tn2.setNumber is  " + tn2.setNumber);
                                            logger.debug("tn1.group.size is  " + tn1.group.criticalSections.size() + " and tn2.group.size is  " + tn2.group.criticalSections.size());
                                        }
                                        foundDeadlock = true;
                                    }
                                }
                                lockOrder.addEdge(tn1.group, tn2.group);
                            }
                        }
                    }
                }
            }
            if (!foundDeadlock) {
                break;
            }
        } while (this.optionRepairDeadlock);
        return lockOrder;
    }

    public MutableEdgeLabelledDirectedGraph<Integer, CriticalSection> detectLocksetDeadlock(Map<Value, Integer> lockToLockNum, List<PointsToSetInternal> lockPTSets) {
        MutableEdgeLabelledDirectedGraph<Integer, CriticalSection> lockOrder;
        HashMutableEdgeLabelledDirectedGraph<Integer, CriticalSection> permanentOrder = new HashMutableEdgeLabelledDirectedGraph<>();
        int iteration = 0;
        do {
            iteration++;
            logger.debug("[DeadlockDetector] Deadlock Iteration #" + iteration);
            boolean foundDeadlock = false;
            lockOrder = permanentOrder.m3032clone();
            Iterator<CriticalSection> deadlockIt1 = this.criticalSections.iterator();
            while (deadlockIt1.hasNext() && !foundDeadlock) {
                CriticalSection tn1 = deadlockIt1.next();
                if (tn1.group != null) {
                    for (EquivalentValue lockEqVal : tn1.lockset) {
                        Value lock = lockEqVal.getValue();
                        if (!lockOrder.containsNode(lockToLockNum.get(lock))) {
                            lockOrder.addNode(lockToLockNum.get(lock));
                        }
                    }
                    if (tn1.transitiveTargets == null) {
                        tn1.transitiveTargets = new HashSet<>();
                        Iterator<Unit> it = tn1.invokes.iterator();
                        while (it.hasNext()) {
                            Unit tn1Invoke = it.next();
                            Iterator<MethodOrMethodContext> targetIt = this.tt.iterator(tn1Invoke);
                            while (targetIt.hasNext()) {
                                tn1.transitiveTargets.add(targetIt.next());
                            }
                        }
                    }
                    Iterator<CriticalSection> deadlockIt2 = this.criticalSections.iterator();
                    while (deadlockIt2.hasNext() && !foundDeadlock) {
                        CriticalSection tn2 = deadlockIt2.next();
                        if (tn2.group != null) {
                            for (EquivalentValue lockEqVal2 : tn2.lockset) {
                                Value lock2 = lockEqVal2.getValue();
                                if (!lockOrder.containsNode(lockToLockNum.get(lock2))) {
                                    lockOrder.addNode(lockToLockNum.get(lock2));
                                }
                            }
                            if (tn1.transitiveTargets.contains(tn2.method) && !foundDeadlock) {
                                logger.debug("[DeadlockDetector] locks in " + tn1.name + " before locks in " + tn2.name + ": outer: " + tn1.name + " inner: " + tn2.name);
                                for (EquivalentValue lock2EqVal : tn2.lockset) {
                                    Value lock22 = lock2EqVal.getValue();
                                    Integer lock2Num = lockToLockNum.get(lock22);
                                    List<Integer> afterTn2 = new ArrayList<>();
                                    afterTn2.addAll(lockOrder.getSuccsOf(lock2Num));
                                    ListIterator<Integer> lit = afterTn2.listIterator();
                                    while (lit.hasNext()) {
                                        Integer to = lit.next();
                                        List<CriticalSection> labels = lockOrder.getLabelsForEdges(lock2Num, to);
                                        boolean keep = false;
                                        if (labels != null) {
                                            Iterator<CriticalSection> it2 = labels.iterator();
                                            while (true) {
                                                if (!it2.hasNext()) {
                                                    break;
                                                }
                                                CriticalSection labelTn = it2.next();
                                                boolean tnsShareAStaticLock = false;
                                                for (EquivalentValue tn1LockEqVal : tn1.lockset) {
                                                    Integer tn1LockNum = lockToLockNum.get(tn1LockEqVal.getValue());
                                                    if (tn1LockNum.intValue() < 0) {
                                                        for (EquivalentValue labelTnLockEqVal : labelTn.lockset) {
                                                            if (Objects.equals(lockToLockNum.get(labelTnLockEqVal.getValue()), tn1LockNum)) {
                                                                tnsShareAStaticLock = true;
                                                            }
                                                        }
                                                    }
                                                }
                                                if (!tnsShareAStaticLock) {
                                                    keep = true;
                                                    break;
                                                }
                                            }
                                        }
                                        if (!keep) {
                                            lit.remove();
                                        }
                                    }
                                    Iterator<EquivalentValue> it3 = tn1.lockset.iterator();
                                    while (true) {
                                        if (!it3.hasNext()) {
                                            break;
                                        }
                                        EquivalentValue lock1EqVal = it3.next();
                                        Value lock1 = lock1EqVal.getValue();
                                        Integer lock1Num = lockToLockNum.get(lock1);
                                        if ((!Objects.equals(lock1Num, lock2Num) || lock1Num.intValue() > 0) && afterTn2.contains(lock1Num)) {
                                            if (!this.optionRepairDeadlock) {
                                                logger.debug("[DeadlockDetector] DEADLOCK HAS BEEN DETECTED: not correcting");
                                                foundDeadlock = true;
                                            } else {
                                                logger.debug("[DeadlockDetector] DEADLOCK HAS BEEN DETECTED while inspecting " + lock1Num + " (" + lock1 + ") and " + lock2Num + " (" + lock22 + ") ");
                                                DeadlockAvoidanceEdge dae = new DeadlockAvoidanceEdge(tn1.method.getDeclaringClass());
                                                EquivalentValue daeEqVal = new EquivalentValue(dae);
                                                Integer daeNum = Integer.valueOf(-lockPTSets.size());
                                                permanentOrder.addNode(daeNum);
                                                lockToLockNum.put(dae, daeNum);
                                                PointsToSetInternal dummyLockPT = new HashPointsToSet(lock1.getType(), (PAG) Scene.v().getPointsToAnalysis());
                                                lockPTSets.add(dummyLockPT);
                                                for (EquivalentValue lockEqVal3 : tn1.lockset) {
                                                    Integer lockNum = lockToLockNum.get(lockEqVal3.getValue());
                                                    if (!permanentOrder.containsNode(lockNum)) {
                                                        permanentOrder.addNode(lockNum);
                                                    }
                                                    permanentOrder.addEdge(daeNum, lockNum, tn1);
                                                }
                                                tn1.lockset.add(daeEqVal);
                                                List<CriticalSection> forwardLabels = lockOrder.getLabelsForEdges(lock1Num, lock2Num);
                                                if (forwardLabels != null) {
                                                    for (CriticalSection tn : forwardLabels) {
                                                        if (!tn.lockset.contains(daeEqVal)) {
                                                            for (EquivalentValue lockEqVal4 : tn.lockset) {
                                                                Integer lockNum2 = lockToLockNum.get(lockEqVal4.getValue());
                                                                if (!permanentOrder.containsNode(lockNum2)) {
                                                                    permanentOrder.addNode(lockNum2);
                                                                }
                                                                permanentOrder.addEdge(daeNum, lockNum2, tn);
                                                            }
                                                            tn.lockset.add(daeEqVal);
                                                        }
                                                    }
                                                }
                                                List<CriticalSection> backwardLabels = lockOrder.getLabelsForEdges(lock2Num, lock1Num);
                                                if (backwardLabels != null) {
                                                    for (CriticalSection tn3 : backwardLabels) {
                                                        if (!tn3.lockset.contains(daeEqVal)) {
                                                            for (EquivalentValue lockEqVal5 : tn3.lockset) {
                                                                Integer lockNum3 = lockToLockNum.get(lockEqVal5.getValue());
                                                                if (!permanentOrder.containsNode(lockNum3)) {
                                                                    permanentOrder.addNode(lockNum3);
                                                                }
                                                                permanentOrder.addEdge(daeNum, lockNum3, tn3);
                                                            }
                                                            tn3.lockset.add(daeEqVal);
                                                            logger.debug("[DeadlockDetector]   Adding deadlock avoidance edge between " + tn1.name + " and " + tn3.name);
                                                        }
                                                    }
                                                    logger.debug("[DeadlockDetector]   Restarting deadlock detection");
                                                }
                                                foundDeadlock = true;
                                            }
                                        }
                                        if (!Objects.equals(lock1Num, lock2Num)) {
                                            lockOrder.addEdge(lock1Num, lock2Num, tn1);
                                        }
                                    }
                                    if (foundDeadlock) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!foundDeadlock) {
                break;
            }
        } while (this.optionRepairDeadlock);
        return lockOrder;
    }

    public void reorderLocksets(Map<Value, Integer> lockToLockNum, MutableEdgeLabelledDirectedGraph<Integer, CriticalSection> lockOrder) {
        for (CriticalSection tn : this.criticalSections) {
            HashMutableDirectedGraph<Integer> visibleOrder = new HashMutableDirectedGraph<>();
            if (tn.group != null) {
                for (CriticalSection otherTn : this.criticalSections) {
                    boolean tnsShareAStaticLock = false;
                    for (EquivalentValue tnLockEqVal : tn.lockset) {
                        Integer tnLockNum = lockToLockNum.get(tnLockEqVal.getValue());
                        if (tnLockNum.intValue() < 0) {
                            if (otherTn.group != null) {
                                for (EquivalentValue otherTnLockEqVal : otherTn.lockset) {
                                    if (Objects.equals(lockToLockNum.get(otherTnLockEqVal.getValue()), tnLockNum)) {
                                        tnsShareAStaticLock = true;
                                    }
                                }
                            } else {
                                tnsShareAStaticLock = true;
                            }
                        }
                    }
                    if (!tnsShareAStaticLock || tn == otherTn) {
                        DirectedGraph<Integer> orderings = lockOrder.getEdgesForLabel(otherTn);
                        for (Integer node1 : orderings) {
                            if (!visibleOrder.containsNode(node1)) {
                                visibleOrder.addNode(node1);
                            }
                            for (Integer node2 : orderings.getSuccsOf(node1)) {
                                if (!visibleOrder.containsNode(node2)) {
                                    visibleOrder.addNode(node2);
                                }
                                visibleOrder.addEdge(node1, node2);
                            }
                        }
                    }
                }
                logger.debug("VISIBLE ORDER FOR " + tn.name);
                visibleOrder.printGraph();
                List<EquivalentValue> newLockset = new ArrayList<>();
                for (EquivalentValue lockEqVal : tn.lockset) {
                    Value lockToInsert = lockEqVal.getValue();
                    Integer lockNumToInsert = lockToLockNum.get(lockToInsert);
                    for (int i = 0; i < newLockset.size(); i++) {
                        EquivalentValue existingLockEqVal = newLockset.get(i);
                        Value existingLock = existingLockEqVal.getValue();
                        Integer existingLockNum = lockToLockNum.get(existingLock);
                        if (!visibleOrder.containsEdge(lockNumToInsert, existingLockNum) && lockNumToInsert.intValue() >= existingLockNum.intValue()) {
                        }
                        newLockset.add(i, lockEqVal);
                    }
                    newLockset.add(i, lockEqVal);
                }
                logger.debug("reordered from " + LockAllocator.locksetToLockNumString(tn.lockset, lockToLockNum) + " to " + LockAllocator.locksetToLockNumString(newLockset, lockToLockNum));
                tn.lockset = newLockset;
            }
        }
    }
}
