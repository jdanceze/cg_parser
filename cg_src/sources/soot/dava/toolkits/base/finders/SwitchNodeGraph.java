package soot.dava.toolkits.base.finders;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import soot.toolkits.graph.DirectedGraph;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/finders/SwitchNodeGraph.class */
class SwitchNodeGraph implements DirectedGraph {
    private LinkedList body;
    private final HashMap binding = new HashMap();
    private final LinkedList heads = new LinkedList();
    private final LinkedList tails = new LinkedList();

    public SwitchNodeGraph(List body) {
        this.body = new LinkedList(body);
        for (Object o : body) {
            SwitchNode sn = (SwitchNode) o;
            this.binding.put(sn.get_AugStmt().bsuccs.get(0), sn);
            sn.reset();
        }
        for (Object o2 : body) {
            ((SwitchNode) o2).setup_Graph(this.binding);
        }
        for (Object o3 : body) {
            SwitchNode sn2 = (SwitchNode) o3;
            if (sn2.get_Preds().isEmpty()) {
                this.heads.add(sn2);
            }
            if (sn2.get_Succs().isEmpty()) {
                this.tails.add(sn2);
            }
        }
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public int size() {
        return this.body.size();
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List getHeads() {
        return this.heads;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List getTails() {
        return this.tails;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List getPredsOf(Object o) {
        return ((SwitchNode) o).get_Preds();
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List getSuccsOf(Object o) {
        return ((SwitchNode) o).get_Succs();
    }

    @Override // soot.toolkits.graph.DirectedGraph, java.lang.Iterable
    public Iterator iterator() {
        return this.body.iterator();
    }

    public List getBody() {
        return this.body;
    }
}
