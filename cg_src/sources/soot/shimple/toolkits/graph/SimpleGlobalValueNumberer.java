package soot.shimple.toolkits.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.shimple.toolkits.graph.ValueGraph;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.CompleteBlockGraph;
/* loaded from: gencallgraphv3.jar:soot/shimple/toolkits/graph/SimpleGlobalValueNumberer.class */
public class SimpleGlobalValueNumberer implements GlobalValueNumberer {
    protected BlockGraph cfg;
    protected ValueGraph vg;
    protected Set<Partition> partitions = new HashSet();
    protected Map<ValueGraph.Node, Partition> nodeToPartition = new HashMap();
    protected int currentPartitionNumber = 0;
    protected List<Partition> newPartitions;

    public SimpleGlobalValueNumberer(BlockGraph cfg) {
        this.cfg = cfg;
        this.vg = new ValueGraph(cfg);
        initPartition();
        iterPartition();
    }

    public static void main(String[] args) {
        Scene.v().loadClassAndSupport(args[0]);
        SootClass sc = Scene.v().getSootClass(args[0]);
        SootMethod sm = sc.getMethod(args[1]);
        Body b = sm.retrieveActiveBody();
        ShimpleBody sb = Shimple.v().newBody(b);
        CompleteBlockGraph cfg = new CompleteBlockGraph(sb);
        SimpleGlobalValueNumberer sgvn = new SimpleGlobalValueNumberer(cfg);
        System.out.println(sgvn);
    }

    @Override // soot.shimple.toolkits.graph.GlobalValueNumberer
    public int getGlobalValueNumber(Local local) {
        ValueGraph.Node node = this.vg.getNode(local);
        return this.nodeToPartition.get(node).getPartitionNumber();
    }

    @Override // soot.shimple.toolkits.graph.GlobalValueNumberer
    public boolean areEqual(Local local1, Local local2) {
        ValueGraph.Node node1 = this.vg.getNode(local1);
        ValueGraph.Node node2 = this.vg.getNode(local2);
        return this.nodeToPartition.get(node1) == this.nodeToPartition.get(node2);
    }

    protected void initPartition() {
        Map<String, Partition> labelToPartition = new HashMap<>();
        for (ValueGraph.Node node : this.vg.getTopNodes()) {
            String label = node.getLabel();
            Partition partition = labelToPartition.get(label);
            if (partition == null) {
                partition = new Partition();
                this.partitions.add(partition);
                labelToPartition.put(label, partition);
            }
            partition.add(node);
            this.nodeToPartition.put(node, partition);
        }
    }

    protected void iterPartition() {
        this.newPartitions = new ArrayList();
        for (Partition partition : this.partitions) {
            processPartition(partition);
        }
        this.partitions.addAll(this.newPartitions);
    }

    protected void processPartition(Partition partition) {
        int size = partition.size();
        if (size == 0) {
            return;
        }
        ValueGraph.Node firstNode = (ValueGraph.Node) partition.get(0);
        Partition newPartition = new Partition();
        boolean processNewPartition = false;
        for (int i = 1; i < size; i++) {
            ValueGraph.Node node = (ValueGraph.Node) partition.get(i);
            if (!childrenAreInSamePartition(firstNode, node)) {
                partition.remove(i);
                size--;
                newPartition.add(node);
                this.newPartitions.add(newPartition);
                this.nodeToPartition.put(node, newPartition);
                processNewPartition = true;
            }
        }
        if (processNewPartition) {
            processPartition(newPartition);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x00fe, code lost:
        r11 = r11 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected boolean childrenAreInSamePartition(soot.shimple.toolkits.graph.ValueGraph.Node r5, soot.shimple.toolkits.graph.ValueGraph.Node r6) {
        /*
            Method dump skipped, instructions count: 266
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.shimple.toolkits.graph.SimpleGlobalValueNumberer.childrenAreInSamePartition(soot.shimple.toolkits.graph.ValueGraph$Node, soot.shimple.toolkits.graph.ValueGraph$Node):boolean");
    }

    public String toString() {
        StringBuffer tmp = new StringBuffer();
        Body b = this.cfg.getBody();
        for (Local local : b.getLocals()) {
            tmp.append(local + "\t" + getGlobalValueNumber(local) + "\n");
        }
        return tmp.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/shimple/toolkits/graph/SimpleGlobalValueNumberer$Partition.class */
    public class Partition extends ArrayList {
        protected int partitionNumber;

        protected Partition() {
            int i = SimpleGlobalValueNumberer.this.currentPartitionNumber;
            SimpleGlobalValueNumberer.this.currentPartitionNumber = i + 1;
            this.partitionNumber = i;
        }

        protected int getPartitionNumber() {
            return this.partitionNumber;
        }
    }
}
