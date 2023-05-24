package soot.jimple.spark.solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.VarNode;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/solver/TopoSorter.class */
public class TopoSorter {
    protected boolean ignoreTypes;
    protected PAG pag;
    protected int nextFinishNumber = 1;
    protected HashSet<VarNode> visited = new HashSet<>();

    public void sort() {
        Iterator<VarNode> it = this.pag.getVarNodeNumberer().iterator();
        while (it.hasNext()) {
            VarNode v = it.next();
            dfsVisit(v);
        }
        this.visited = null;
    }

    public TopoSorter(PAG pag, boolean ignoreTypes) {
        this.pag = pag;
        this.ignoreTypes = ignoreTypes;
    }

    protected void dfsVisit(VarNode n) {
        if (this.visited.contains(n)) {
            return;
        }
        List<VarNode> stack = new ArrayList<>();
        List<VarNode> all = new ArrayList<>();
        stack.add(n);
        while (!stack.isEmpty()) {
            VarNode s = stack.remove(stack.size() - 1);
            if (this.visited.add(s)) {
                all.add(s);
                Node[] succs = this.pag.simpleLookup(s);
                for (Node element : succs) {
                    if (this.ignoreTypes || this.pag.getTypeManager().castNeverFails(n.getType(), element.getType())) {
                        stack.add((VarNode) element);
                    }
                }
            }
        }
        for (int i = all.size() - 1; i >= 0; i--) {
            int i2 = this.nextFinishNumber;
            this.nextFinishNumber = i2 + 1;
            all.get(i).setFinishingNumber(i2);
        }
    }
}
