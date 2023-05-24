package soot.jimple.toolkits.thread.mhp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.jimple.toolkits.thread.mhp.stmt.BeginStmt;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.jimple.toolkits.thread.mhp.stmt.JoinStmt;
import soot.jimple.toolkits.thread.mhp.stmt.MonitorEntryStmt;
import soot.jimple.toolkits.thread.mhp.stmt.NotifiedEntryStmt;
import soot.jimple.toolkits.thread.mhp.stmt.NotifyAllStmt;
import soot.jimple.toolkits.thread.mhp.stmt.NotifyStmt;
import soot.jimple.toolkits.thread.mhp.stmt.StartStmt;
import soot.jimple.toolkits.thread.mhp.stmt.WaitingStmt;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/MhpAnalysis.class */
class MhpAnalysis {
    private PegGraph g;
    private final Map<Object, FlowSet> unitToGen;
    private final Map<Object, FlowSet> unitToKill;
    private final Map<Object, FlowSet> unitToM;
    private final Map<Object, FlowSet> unitToOut;
    private final Map<Object, FlowSet> notifySucc;
    private final Map<String, FlowSet> monitor;
    private final Map<JPegStmt, Set<JPegStmt>> notifyPred;
    FlowSet fullSet = new ArraySparseSet();
    LinkedList<Object> workList = new LinkedList<>();

    /* JADX WARN: Multi-variable type inference failed */
    MhpAnalysis(PegGraph g) {
        this.g = g;
        int size = g.size();
        Map startToThread = g.getStartToThread();
        this.unitToGen = new HashMap((size * 2) + 1, 0.7f);
        this.unitToKill = new HashMap((size * 2) + 1, 0.7f);
        this.unitToM = new HashMap((size * 2) + 1, 0.7f);
        this.unitToOut = new HashMap((size * 2) + 1, 0.7f);
        this.notifySucc = new HashMap((size * 2) + 1, 0.7f);
        this.notifyPred = new HashMap((size * 2) + 1, 0.7f);
        this.monitor = g.getMonitor();
        Iterator it = g.iterator();
        while (it.hasNext()) {
            Object stmt = it.next();
            FlowSet genSet = new ArraySparseSet();
            FlowSet killSet = new ArraySparseSet();
            FlowSet mSet = new ArraySparseSet();
            FlowSet outSet = new ArraySparseSet();
            FlowSet notifySuccSet = new ArraySparseSet();
            this.unitToGen.put(stmt, genSet);
            this.unitToKill.put(stmt, killSet);
            this.unitToM.put(stmt, mSet);
            this.unitToOut.put(stmt, outSet);
            this.notifySucc.put(stmt, notifySuccSet);
        }
        Set keys = startToThread.keySet();
        for (JPegStmt stmt2 : keys) {
            if (!this.workList.contains(stmt2)) {
                this.workList.addLast(stmt2);
            }
        }
        Iterator it2 = g.iterator();
        while (it2.hasNext()) {
            FlowSet genSet2 = new ArraySparseSet();
            FlowSet killSet2 = new ArraySparseSet();
            Object o = it2.next();
            if (o instanceof JPegStmt) {
                JPegStmt s = (JPegStmt) o;
                if (s instanceof JoinStmt) {
                    if (!g.getSpecialJoin().contains(s)) {
                        Chain chain = g.getJoinStmtToThread().get(s);
                        Iterator nodesIt = chain.iterator();
                        if (nodesIt.hasNext()) {
                            while (nodesIt.hasNext()) {
                                killSet2.add(nodesIt.next());
                            }
                        }
                        this.unitToGen.put(s, genSet2);
                        this.unitToKill.put(s, killSet2);
                    }
                } else if ((s instanceof MonitorEntryStmt) || (s instanceof NotifiedEntryStmt)) {
                    g.iterator();
                    killSet2 = this.monitor.containsKey(s.getObject()) ? this.monitor.get(s.getObject()) : killSet2;
                    this.unitToGen.put(s, genSet2);
                    this.unitToKill.put(s, killSet2);
                } else if (s instanceof NotifyAllStmt) {
                    Map<String, FlowSet> waitingNodes = g.getWaitingNodes();
                    if (waitingNodes.containsKey(s.getObject())) {
                        for (Object obj : waitingNodes.get(s.getObject())) {
                            killSet2.add(obj);
                        }
                    }
                    this.unitToGen.put(s, genSet2);
                    this.unitToKill.put(s, killSet2);
                } else if (s instanceof NotifyStmt) {
                    Map<String, FlowSet> waitingNodes2 = g.getWaitingNodes();
                    if (waitingNodes2.containsKey(s.getObject())) {
                        FlowSet<Object> killNodes = waitingNodes2.get(s.getObject());
                        if (killNodes.size() == 1) {
                            for (Object obj2 : killNodes) {
                                killSet2.add(obj2);
                            }
                        }
                    }
                    this.unitToGen.put(s, genSet2);
                    this.unitToKill.put(s, killSet2);
                } else if ((s instanceof StartStmt) && g.getStartToThread().containsKey(s)) {
                    for (PegChain chain2 : g.getStartToThread().get(s)) {
                        for (Object obj3 : chain2.getHeads()) {
                            genSet2.add(obj3);
                        }
                    }
                    this.unitToGen.put(s, genSet2);
                    this.unitToKill.put(s, killSet2);
                }
            }
        }
        doAnalysis();
        long beginTime = System.currentTimeMillis();
        computeMPairs();
        computeMSet();
        long buildPegDuration = System.currentTimeMillis() - beginTime;
        System.err.println("compute parir + mset: " + buildPegDuration);
    }

    protected void doAnalysis() {
        while (this.workList.size() > 0) {
            Object currentObj = this.workList.removeFirst();
            FlowSet killSet = this.unitToKill.get(currentObj);
            FlowSet genSet = this.unitToGen.get(currentObj);
            ArraySparseSet arraySparseSet = new ArraySparseSet();
            FlowSet outSet = this.unitToOut.get(currentObj);
            FlowSet notifySuccSet = this.notifySucc.get(currentObj);
            FlowSet mOld = this.unitToM.get(currentObj);
            FlowSet outOld = outSet.mo2534clone();
            FlowSet notifySuccSetOld = notifySuccSet.mo2534clone();
            ArraySparseSet arraySparseSet2 = new ArraySparseSet();
            waitingPred = null;
            if (!(currentObj instanceof JPegStmt)) {
                for (Object tempStmt : this.g.getPredsOf(currentObj)) {
                    FlowSet out = this.unitToOut.get(tempStmt);
                    if (out != null) {
                        arraySparseSet.union(out);
                    }
                }
                arraySparseSet.union(mOld);
                this.unitToM.put(currentObj, arraySparseSet);
                arraySparseSet.union(genSet, outSet);
                if (killSet.size() > 0) {
                    for (Object tempStmt2 : killSet) {
                        if (outSet.contains(tempStmt2)) {
                            outSet.remove(tempStmt2);
                        }
                    }
                }
                if (!mOld.equals(arraySparseSet)) {
                    for (Object tempM : arraySparseSet) {
                        if (!mOld.contains(tempM) && this.unitToM.containsKey(tempM)) {
                            FlowSet mSetMSym = this.unitToM.get(tempM);
                            if (mSetMSym.size() != 0) {
                                if (!mSetMSym.contains(currentObj)) {
                                    mSetMSym.add(currentObj);
                                }
                            } else {
                                mSetMSym.add(currentObj);
                            }
                        }
                        if (!this.workList.contains(tempM)) {
                            this.workList.addLast(tempM);
                        }
                    }
                }
                if (!outOld.equals(outSet)) {
                    for (Object localSucc : this.g.getSuccsOf(currentObj)) {
                        if (localSucc instanceof JPegStmt) {
                            if (!(((JPegStmt) localSucc) instanceof NotifiedEntryStmt) && !this.workList.contains(localSucc)) {
                                this.workList.addLast(localSucc);
                            }
                        } else if (!this.workList.contains(localSucc)) {
                            this.workList.addLast(localSucc);
                        }
                    }
                }
            } else {
                JPegStmt currentNode = (JPegStmt) currentObj;
                currentNode.getTags().get(0);
                if ((currentNode instanceof NotifyStmt) || (currentNode instanceof NotifyAllStmt)) {
                    Map<String, FlowSet> waitingNodes = this.g.getWaitingNodes();
                    if (waitingNodes.containsKey(currentNode.getObject())) {
                        FlowSet<JPegStmt> waitingNodeSet = waitingNodes.get(currentNode.getObject());
                        for (JPegStmt tempNode : waitingNodeSet) {
                            if (mOld.contains(tempNode)) {
                                List<JPegStmt> waitingSuccList = this.g.getSuccsOf(tempNode);
                                for (JPegStmt waitingSucc : waitingSuccList) {
                                    notifySuccSet.add(waitingSucc);
                                    if (waitingSucc instanceof NotifiedEntryStmt) {
                                        FlowSet notifySet = this.notifySucc.get(currentNode);
                                        notifySet.add(waitingSucc);
                                        this.notifySucc.put(currentNode, notifySet);
                                        if (this.notifyPred.containsKey(waitingSucc)) {
                                            Set<JPegStmt> notifyPredSet = this.notifyPred.get(waitingSucc);
                                            notifyPredSet.add(currentNode);
                                            this.notifyPred.put(waitingSucc, notifyPredSet);
                                        } else {
                                            Set<JPegStmt> notifyPredSet2 = new HashSet<>();
                                            notifyPredSet2.add(currentNode);
                                            this.notifyPred.put(waitingSucc, notifyPredSet2);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        throw new RuntimeException("Fail to find waiting node for: " + currentObj);
                    }
                }
                if (!notifySuccSetOld.equals(notifySuccSet)) {
                    for (Object notifySuccNode : notifySuccSet) {
                        if (!this.workList.contains(notifySuccNode)) {
                            this.workList.addLast(notifySuccNode);
                        }
                    }
                }
                if (currentNode instanceof NotifiedEntryStmt) {
                    for (JPegStmt waitingPred : this.g.getPredsOf(currentNode)) {
                        if ((waitingPred instanceof WaitingStmt) && waitingPred.getObject().equals(currentNode.getObject()) && waitingPred.getCaller().equals(currentNode.getCaller())) {
                            break;
                        }
                    }
                    Map<String, FlowSet> waitingNodes2 = this.g.getWaitingNodes();
                    FlowSet<JPegStmt> notifyEntrySet = new ArraySparseSet();
                    if (waitingNodes2.containsKey(currentNode.getObject())) {
                        FlowSet<Object> waitingNodesSet = waitingNodes2.get(currentNode.getObject());
                        for (Object obj : waitingNodesSet) {
                            List<JPegStmt> waitingNodesSucc = this.g.getSuccsOf(obj);
                            for (JPegStmt notifyEntry : waitingNodesSucc) {
                                if (notifyEntry instanceof NotifiedEntryStmt) {
                                    notifyEntrySet.add(notifyEntry);
                                }
                            }
                        }
                    }
                    for (JPegStmt notifyEntry2 : notifyEntrySet) {
                        waitingPredNode = null;
                        for (JPegStmt waitingPredNode : this.g.getPredsOf(notifyEntry2)) {
                            if ((waitingPredNode instanceof WaitingStmt) && waitingPredNode.getObject().equals(currentNode.getObject()) && waitingPredNode.getCaller().equals(currentNode.getCaller())) {
                                break;
                            }
                        }
                        if (this.unitToM.containsKey(waitingPredNode)) {
                            FlowSet mWaitingPredM = this.unitToM.get(waitingPredNode);
                            if (mWaitingPredM.contains(waitingPred)) {
                                Map<String, Set<JPegStmt>> notifyAll = this.g.getNotifyAll();
                                if (notifyAll.containsKey(currentNode.getObject())) {
                                    Set notifyAllSet = notifyAll.get(currentNode.getObject());
                                    for (JPegStmt notifyAllStmt : notifyAllSet) {
                                        if (this.unitToM.containsKey(waitingPred)) {
                                            FlowSet mWaitingPredN = this.unitToM.get(waitingPred);
                                            if (mWaitingPredM.contains(notifyAllStmt) && mWaitingPredN.contains(notifyAllStmt)) {
                                                arraySparseSet2.add(notifyEntry2);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                ArraySparseSet arraySparseSet3 = new ArraySparseSet();
                if (currentNode instanceof NotifiedEntryStmt) {
                    if (!this.unitToOut.containsKey(waitingPred)) {
                        throw new RuntimeException("unitToOut does not contains " + waitingPred);
                    }
                    ArraySparseSet arraySparseSet4 = new ArraySparseSet();
                    Set notifyPredSet3 = this.notifyPred.get(currentNode);
                    if (notifyPredSet3 != null) {
                        for (JPegStmt notifyPred : notifyPredSet3) {
                            FlowSet outWaitingPredTemp = this.unitToOut.get(notifyPred);
                            outWaitingPredTemp.copy(arraySparseSet3);
                        }
                        FlowSet outWaitingPredSet = this.unitToOut.get(waitingPred);
                        arraySparseSet3.intersection(outWaitingPredSet, arraySparseSet4);
                        arraySparseSet4.union(arraySparseSet2, arraySparseSet);
                    }
                } else if (currentNode instanceof BeginStmt) {
                    arraySparseSet = new ArraySparseSet();
                    Map<JPegStmt, List> startToThread = this.g.getStartToThread();
                    Set<JPegStmt> keySet = startToThread.keySet();
                    for (JPegStmt tempStmt3 : keySet) {
                        for (PegChain pegChain : startToThread.get(tempStmt3)) {
                            List beginNodes = pegChain.getHeads();
                            if (beginNodes.contains(currentNode)) {
                                for (Object startPred : this.unitToOut.get(tempStmt3)) {
                                    arraySparseSet.add(startPred);
                                }
                            }
                        }
                    }
                    for (JPegStmt tempStmt4 : startToThread.keySet()) {
                        for (Chain chain : startToThread.get(tempStmt4)) {
                            if (chain.contains(currentNode)) {
                                for (Object stmt : chain) {
                                    if (arraySparseSet.contains(stmt)) {
                                        arraySparseSet.remove((ArraySparseSet) stmt);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (!(currentNode instanceof NotifiedEntryStmt)) {
                        for (Object tempStmt5 : this.g.getPredsOf(currentNode)) {
                            FlowSet out2 = this.unitToOut.get(tempStmt5);
                            if (out2 != null) {
                                arraySparseSet.union(out2);
                            }
                        }
                    }
                }
                arraySparseSet.union(mOld);
                this.unitToM.put(currentNode, arraySparseSet);
                if ((currentNode instanceof NotifyStmt) || (currentNode instanceof NotifyAllStmt)) {
                    notifySuccSet.copy(genSet);
                    this.unitToGen.put(currentNode, genSet);
                }
                arraySparseSet.union(genSet, outSet);
                if (killSet.size() > 0) {
                    for (Object tempStmt6 : killSet) {
                        if (outSet.contains(tempStmt6)) {
                            outSet.remove(tempStmt6);
                        }
                    }
                }
                if (!mOld.equals(arraySparseSet)) {
                    for (Object tempM2 : arraySparseSet) {
                        if (!mOld.contains(tempM2)) {
                            if (!this.unitToM.containsKey(tempM2)) {
                                throw new RuntimeException("unitToM does not contain: " + tempM2);
                            }
                            FlowSet mSetMSym2 = this.unitToM.get(tempM2);
                            if (mSetMSym2.size() != 0) {
                                if (!mSetMSym2.contains(currentNode)) {
                                    mSetMSym2.add(currentNode);
                                }
                            } else {
                                mSetMSym2.add(currentNode);
                            }
                        }
                        if (!this.workList.contains(tempM2)) {
                            this.workList.addLast(tempM2);
                        }
                    }
                }
                if (!outOld.equals(outSet)) {
                    for (Object localSucc2 : this.g.getSuccsOf(currentNode)) {
                        if (localSucc2 instanceof JPegStmt) {
                            if (!(((JPegStmt) localSucc2) instanceof NotifiedEntryStmt) && !this.workList.contains(localSucc2)) {
                                this.workList.addLast(localSucc2);
                            }
                        } else if (!this.workList.contains(localSucc2)) {
                            this.workList.addLast(localSucc2);
                        }
                    }
                    if (currentNode instanceof StartStmt) {
                        Map<JPegStmt, List> startToThread2 = this.g.getStartToThread();
                        if (startToThread2.containsKey(currentNode)) {
                            for (Chain chain2 : startToThread2.get(currentNode)) {
                                Iterator chainIt = chain2.iterator();
                                while (true) {
                                    if (!chainIt.hasNext()) {
                                        break;
                                    }
                                    Object tempStmt7 = chainIt.next();
                                    if ((tempStmt7 instanceof JPegStmt) && (((JPegStmt) tempStmt7) instanceof BeginStmt)) {
                                        if (!this.workList.contains(tempStmt7)) {
                                            this.workList.addLast(tempStmt7);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected Object entryInitialFlow() {
        return new ArraySparseSet();
    }

    protected Object newInitialFlow() {
        return this.fullSet.mo2534clone();
    }

    protected Map<Object, FlowSet> getUnitToM() {
        return this.unitToM;
    }

    private void computeMPairs() {
        Set<Set<Object>> mSetPairs = new HashSet<>();
        Set maps = this.unitToM.entrySet();
        for (Map.Entry<Object, FlowSet> entry : maps) {
            Object obj = entry.getKey();
            FlowSet fs = entry.getValue();
            for (Object m : fs) {
                Set<Object> pair = new HashSet<>();
                pair.add(obj);
                pair.add(m);
                if (!mSetPairs.contains(pair)) {
                    mSetPairs.add(pair);
                }
            }
        }
        System.err.println("Number of pairs: " + mSetPairs.size());
    }

    private void computeMSet() {
        long min = 0;
        long max = 0;
        long nodes = 0;
        long totalNodes = 0;
        Set maps = this.unitToM.entrySet();
        boolean first = true;
        for (Map.Entry<Object, FlowSet> entry : maps) {
            entry.getKey();
            FlowSet fs = entry.getValue();
            if (fs.size() > 0) {
                totalNodes += fs.size();
                nodes++;
                if (fs.size() > max) {
                    max = fs.size();
                }
                if (first) {
                    min = fs.size();
                    first = false;
                } else if (fs.size() < min) {
                    min = fs.size();
                }
            }
        }
        System.err.println("average: " + (totalNodes / nodes));
        System.err.println("min: " + min);
        System.err.println("max: " + max);
    }
}
