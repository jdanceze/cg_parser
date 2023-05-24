package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.util.FastStack;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/StronglyConnectedComponentsFast.class */
public class StronglyConnectedComponentsFast<N> {
    protected final List<List<N>> componentList = new ArrayList();
    protected final List<List<N>> trueComponentList = new ArrayList();
    protected int index = 0;
    protected Map<N, Integer> indexForNode;
    protected Map<N, Integer> lowlinkForNode;
    protected FastStack<N> s;
    protected DirectedGraph<N> g;

    public StronglyConnectedComponentsFast(DirectedGraph<N> g) {
        this.g = g;
        this.s = new FastStack<>();
        this.indexForNode = new HashMap();
        this.lowlinkForNode = new HashMap();
        for (N node : g) {
            if (!this.indexForNode.containsKey(node)) {
                if (g.size() > 1000) {
                    iterate(node);
                } else {
                    recurse(node);
                }
            }
        }
        this.indexForNode = null;
        this.lowlinkForNode = null;
        this.s = null;
        this.g = null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void recurse(N v) {
        N v2;
        this.indexForNode.put(v, Integer.valueOf(this.index));
        Map<N, Integer> map = this.lowlinkForNode;
        int i = this.index;
        int lowLinkForNodeV = i;
        map.put(v, Integer.valueOf(i));
        this.index++;
        this.s.push(v);
        for (N succ : this.g.getSuccsOf(v)) {
            Integer indexForNodeSucc = this.indexForNode.get(succ);
            if (indexForNodeSucc == null) {
                recurse(succ);
                Map<N, Integer> map2 = this.lowlinkForNode;
                int min = Math.min(lowLinkForNodeV, this.lowlinkForNode.get(succ).intValue());
                lowLinkForNodeV = min;
                map2.put(v, Integer.valueOf(min));
            } else if (this.s.contains(succ)) {
                Map<N, Integer> map3 = this.lowlinkForNode;
                int min2 = Math.min(lowLinkForNodeV, indexForNodeSucc.intValue());
                lowLinkForNodeV = min2;
                map3.put(v, Integer.valueOf(min2));
            }
        }
        if (lowLinkForNodeV == this.indexForNode.get(v).intValue()) {
            List<N> scc = new ArrayList<>();
            do {
                v2 = this.s.pop();
                scc.add(v2);
            } while (v != v2);
            this.componentList.add(scc);
            if (scc.size() > 1) {
                this.trueComponentList.add(scc);
                return;
            }
            N n = scc.get(0);
            if (this.g.getSuccsOf(n).contains(n)) {
                this.trueComponentList.add(scc);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void iterate(N x) {
        N v2;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList.add(x);
        while (!arrayList.isEmpty()) {
            Object remove = arrayList.remove(0);
            boolean hasChildren = false;
            boolean isForward = false;
            if (!this.indexForNode.containsKey(remove)) {
                this.indexForNode.put(remove, Integer.valueOf(this.index));
                this.lowlinkForNode.put(remove, Integer.valueOf(this.index));
                this.index++;
                this.s.push(remove);
                isForward = true;
            }
            Iterator it = this.g.getSuccsOf(remove).iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Object next = it.next();
                Integer indexForNodeSucc = this.indexForNode.get(next);
                if (indexForNodeSucc == null) {
                    arrayList.add(0, next);
                    hasChildren = true;
                    break;
                } else if (!isForward) {
                    int lowLinkForNodeV = this.lowlinkForNode.get(remove).intValue();
                    this.lowlinkForNode.put(remove, Integer.valueOf(Math.min(lowLinkForNodeV, this.lowlinkForNode.get(next).intValue())));
                } else if (isForward && this.s.contains(next)) {
                    int lowLinkForNodeV2 = this.lowlinkForNode.get(remove).intValue();
                    this.lowlinkForNode.put(remove, Integer.valueOf(Math.min(lowLinkForNodeV2, indexForNodeSucc.intValue())));
                }
            }
            if (hasChildren) {
                arrayList2.add(0, remove);
            } else {
                if (!arrayList2.isEmpty()) {
                    arrayList.add(0, arrayList2.remove(0));
                }
                int lowLinkForNodeV3 = this.lowlinkForNode.get(remove).intValue();
                if (lowLinkForNodeV3 == this.indexForNode.get(remove).intValue()) {
                    List<N> scc = new ArrayList<>();
                    do {
                        v2 = this.s.pop();
                        scc.add(v2);
                    } while (remove != v2);
                    this.componentList.add(scc);
                    if (scc.size() > 1) {
                        this.trueComponentList.add(scc);
                    } else {
                        N n = scc.get(0);
                        if (this.g.getSuccsOf(n).contains(n)) {
                            this.trueComponentList.add(scc);
                        }
                    }
                }
            }
        }
    }

    public List<List<N>> getComponents() {
        return this.componentList;
    }

    public List<List<N>> getTrueComponents() {
        return this.trueComponentList;
    }
}
