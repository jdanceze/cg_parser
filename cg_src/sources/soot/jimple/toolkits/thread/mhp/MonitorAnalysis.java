package soot.jimple.toolkits.thread.mhp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Timers;
import soot.coffi.Instruction;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.tagkit.Tag;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/MonitorAnalysis.class */
public class MonitorAnalysis extends ForwardFlowAnalysis {
    private static final Logger logger = LoggerFactory.getLogger(MonitorAnalysis.class);
    private PegGraph g;
    private final HashMap<String, FlowSet> monitor;
    private final Vector<Object> nodes;
    private final Vector<Object> valueBefore;
    private final Vector<Object> valueAfter;

    public MonitorAnalysis(PegGraph g) {
        super(g);
        this.monitor = new HashMap<>();
        this.nodes = new Vector<>();
        this.valueBefore = new Vector<>();
        this.valueAfter = new Vector<>();
        this.g = g;
        doAnalysis();
        g.setMonitor(this.monitor);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.ForwardFlowAnalysis, soot.toolkits.scalar.AbstractFlowAnalysis
    public void doAnalysis() {
        LinkedList<Object> changedUnits = new LinkedList<>();
        HashSet<Object> changedUnitsSet = new HashSet<>();
        int numNodes = this.graph.size();
        int numComputations = 0;
        createWorkList(changedUnits, changedUnitsSet);
        for (Object s : this.graph.getHeads()) {
            this.nodes.add(s);
            this.valueBefore.add(entryInitialFlow());
        }
        Object previousAfterFlow = newInitialFlow();
        while (!changedUnits.isEmpty()) {
            Object s2 = changedUnits.removeFirst();
            changedUnitsSet.remove(s2);
            int pos = this.nodes.indexOf(s2);
            copy(this.valueAfter.elementAt(pos), previousAfterFlow);
            List preds = this.graph.getPredsOf(s2);
            Object beforeFlow = this.valueBefore.elementAt(pos);
            if (preds.size() == 1) {
                copy(this.valueAfter.elementAt(this.nodes.indexOf(preds.get(0))), beforeFlow);
            } else if (preds.size() != 0) {
                Iterator predIt = preds.iterator();
                Object obj = predIt.next();
                copy(this.valueAfter.elementAt(this.nodes.indexOf(obj)), beforeFlow);
                while (predIt.hasNext()) {
                    JPegStmt stmt = (JPegStmt) predIt.next();
                    if (!stmt.equals(obj) && this.nodes.indexOf(stmt) >= 0) {
                        Object otherBranchFlow = this.valueAfter.elementAt(this.nodes.indexOf(stmt));
                        merge(beforeFlow, otherBranchFlow, beforeFlow);
                    }
                }
            }
            Object afterFlow = this.valueAfter.elementAt(this.nodes.indexOf(s2));
            flowThrough(beforeFlow, s2, afterFlow);
            this.valueAfter.set(this.nodes.indexOf(s2), afterFlow);
            numComputations++;
            if (!afterFlow.equals(previousAfterFlow)) {
                for (Object succ : this.graph.getSuccsOf(s2)) {
                    if (!changedUnitsSet.contains(succ)) {
                        changedUnits.addLast(succ);
                        changedUnitsSet.add(succ);
                    }
                }
            }
        }
        Timers.v().totalFlowNodes += numNodes;
        Timers.v().totalFlowComputations += numComputations;
    }

    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    protected void merge(Object in1, Object in2, Object out) {
        MonitorSet inSet1 = (MonitorSet) in1;
        MonitorSet inSet2 = (MonitorSet) in2;
        MonitorSet outSet = (MonitorSet) out;
        inSet1.intersection(inSet2, outSet);
    }

    @Override // soot.toolkits.scalar.FlowAnalysis
    protected void flowThrough(Object inValue, Object unit, Object outValue) {
        MonitorSet in = (MonitorSet) inValue;
        MonitorSet out = (MonitorSet) outValue;
        JPegStmt s = (JPegStmt) unit;
        s.getTags().get(0);
        in.copy(out);
        if (in.size() > 0 && !s.getName().equals("waiting") && !s.getName().equals("notified-entry")) {
            updateMonitor(in, unit);
        }
        String objName = s.getObject();
        if (s.getName().equals("entry") || s.getName().equals("exit")) {
            if (out.contains("&")) {
                out.remove("&");
            }
            Object obj = out.getMonitorDepth(objName);
            if (obj == null) {
                if (s.getName().equals("entry")) {
                    out.add(new MonitorDepth(objName, 1));
                }
            } else if (obj instanceof MonitorDepth) {
                MonitorDepth md = (MonitorDepth) obj;
                if (s.getName().equals("entry")) {
                    md.increaseDepth();
                } else if (md.getDepth() > 1) {
                    md.decreaseDepth();
                } else if (md.getDepth() == 1) {
                    out.remove(md);
                } else {
                    throw new RuntimeException("The monitor depth can not be decreased at  " + unit);
                }
            } else {
                throw new RuntimeException("MonitorSet contains non MonitorDepth element!");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(Object source, Object dest) {
        MonitorSet sourceSet = (MonitorSet) source;
        MonitorSet destSet = (MonitorSet) dest;
        sourceSet.copy(destSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public Object entryInitialFlow() {
        return new MonitorSet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public Object newInitialFlow() {
        MonitorSet fullSet = new MonitorSet();
        fullSet.add("&");
        return fullSet;
    }

    private void updateMonitor(MonitorSet ms, Object unit) {
        Iterator it = ms.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof MonitorDepth) {
                MonitorDepth md = (MonitorDepth) obj;
                String objName = md.getObjName();
                if (this.monitor.containsKey(objName)) {
                    if (md.getDepth() > 0) {
                        this.monitor.get(objName).add(unit);
                    }
                } else {
                    FlowSet monitorObjs = new ArraySparseSet();
                    monitorObjs.add(unit);
                    this.monitor.put(objName, monitorObjs);
                }
            }
        }
    }

    private void createWorkList(LinkedList<Object> changedUnits, HashSet<Object> changedUnitsSet) {
        createWorkList(changedUnits, changedUnitsSet, this.g.getMainPegChain());
        Set maps = this.g.getStartToThread().entrySet();
        for (Map.Entry<JPegStmt, List> entry : maps) {
            List<PegChain> runMethodChainList = entry.getValue();
            for (PegChain chain : runMethodChainList) {
                createWorkList(changedUnits, changedUnitsSet, chain);
            }
        }
    }

    public void computeSynchNodes() {
        int num = 0;
        Set maps = this.monitor.entrySet();
        for (Map.Entry<String, FlowSet> entry : maps) {
            FlowSet fs = entry.getValue();
            num += fs.size();
        }
        System.err.println("synch objects: " + num);
    }

    private void createWorkList(LinkedList<Object> changedUnits, HashSet<Object> changedUnitsSet, PegChain chain) {
        Set<Object> gray = new HashSet<>();
        for (Object head : chain.getHeads()) {
            if (!gray.contains(head)) {
                visitNode(gray, head, changedUnits, changedUnitsSet);
            }
        }
    }

    private void visitNode(Set<Object> gray, Object obj, LinkedList<Object> changedUnits, HashSet<Object> changedUnitsSet) {
        gray.add(obj);
        changedUnits.addLast(obj);
        changedUnitsSet.add(obj);
        this.nodes.add(obj);
        this.valueBefore.add(newInitialFlow());
        this.valueAfter.add(newInitialFlow());
        if (this.g.getSuccsOf(obj).size() > 0) {
            for (Object succ : this.graph.getSuccsOf(obj)) {
                if (!gray.contains(succ)) {
                    visitNode(gray, succ, changedUnits, changedUnitsSet);
                }
            }
        }
    }

    public Map<String, FlowSet> getMonitor() {
        return this.monitor;
    }

    public void testMonitor() {
        System.out.println("=====test monitor size: " + this.monitor.size());
        Set maps = this.monitor.entrySet();
        for (Map.Entry<String, FlowSet> entry : maps) {
            String key = entry.getKey();
            System.out.println("---key=  " + key);
            FlowSet<JPegStmt> list = entry.getValue();
            if (list.size() > 0) {
                System.out.println("**set:  " + list.size());
                for (JPegStmt stmt : list) {
                    Tag tag1 = stmt.getTags().get(0);
                    System.out.println(tag1 + Instruction.argsep + stmt);
                }
            }
        }
        System.out.println("=========monitor--ends--------");
    }
}
