package soot.jimple.toolkits.thread.mhp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/TopologicalSorter.class */
public class TopologicalSorter {
    Chain chain;
    PegGraph pg;
    LinkedList<Object> sorter = new LinkedList<>();
    List<Object> visited = new ArrayList();

    public TopologicalSorter(Chain chain, PegGraph pg) {
        this.chain = chain;
        this.pg = pg;
        go();
    }

    private void go() {
        for (Object node : this.chain) {
            dfsVisit(node);
        }
    }

    private void dfsVisit(Object m) {
        if (this.visited.contains(m)) {
            return;
        }
        this.visited.add(m);
        for (Object target : this.pg.getSuccsOf(m)) {
            dfsVisit(target);
        }
        this.sorter.addFirst(m);
    }

    public List<Object> sorter() {
        return this.sorter;
    }
}
