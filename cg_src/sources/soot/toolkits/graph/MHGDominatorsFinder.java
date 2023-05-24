package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/MHGDominatorsFinder.class */
public class MHGDominatorsFinder<N> implements DominatorsFinder<N> {
    protected final DirectedGraph<N> graph;
    protected final Set<N> heads;
    protected final Map<N, BitSet> nodeToFlowSet;
    protected final Map<N, Integer> nodeToIndex;
    protected final Map<Integer, N> indexToNode;
    protected int lastIndex = 0;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !MHGDominatorsFinder.class.desiredAssertionStatus();
    }

    public MHGDominatorsFinder(DirectedGraph<N> graph) {
        this.graph = graph;
        this.heads = new HashSet(graph.getHeads());
        int size = (graph.size() * 2) + 1;
        this.nodeToFlowSet = new HashMap(size, 0.7f);
        this.nodeToIndex = new HashMap(size, 0.7f);
        this.indexToNode = new HashMap(size, 0.7f);
        doAnalysis();
    }

    protected void doAnalysis() {
        boolean changed;
        DirectedGraph<N> graph = this.graph;
        BitSet fullSet = new BitSet(graph.size());
        fullSet.flip(0, graph.size());
        for (N o : graph) {
            if (this.heads.contains(o)) {
                BitSet self = new BitSet();
                self.set(indexOf(o));
                this.nodeToFlowSet.put(o, self);
            } else {
                this.nodeToFlowSet.put(o, fullSet);
            }
        }
        do {
            changed = false;
            for (N o2 : graph) {
                if (!this.heads.contains(o2)) {
                    BitSet predsIntersect = (BitSet) fullSet.clone();
                    for (N next : graph.getPredsOf(o2)) {
                        predsIntersect.and(getDominatorsBitSet(next));
                    }
                    BitSet oldSet = getDominatorsBitSet(o2);
                    predsIntersect.set(indexOf(o2));
                    if (!predsIntersect.equals(oldSet)) {
                        this.nodeToFlowSet.put(o2, predsIntersect);
                        changed = true;
                    }
                }
            }
        } while (changed);
    }

    protected BitSet getDominatorsBitSet(N node) {
        BitSet bitSet = this.nodeToFlowSet.get(node);
        if ($assertionsDisabled || bitSet != null) {
            return bitSet;
        }
        throw new AssertionError("Node " + node + " is not in the graph!");
    }

    protected int indexOfAssert(N o) {
        Integer index = this.nodeToIndex.get(o);
        if ($assertionsDisabled || index != null) {
            return index.intValue();
        }
        throw new AssertionError("Node " + o + " is not in the graph!");
    }

    protected int indexOf(N o) {
        Integer index = this.nodeToIndex.get(o);
        if (index == null) {
            index = Integer.valueOf(this.lastIndex);
            this.nodeToIndex.put(o, index);
            this.indexToNode.put(index, o);
            this.lastIndex++;
        }
        return index.intValue();
    }

    @Override // soot.toolkits.graph.DominatorsFinder
    public DirectedGraph<N> getGraph() {
        return this.graph;
    }

    @Override // soot.toolkits.graph.DominatorsFinder
    public List<N> getDominators(N node) {
        List<N> result = new ArrayList<>();
        BitSet bitSet = getDominatorsBitSet(node);
        int nextSetBit = bitSet.nextSetBit(0);
        while (true) {
            int i = nextSetBit;
            if (i < 0) {
                break;
            }
            result.add(this.indexToNode.get(Integer.valueOf(i)));
            if (i == Integer.MAX_VALUE) {
                break;
            }
            nextSetBit = bitSet.nextSetBit(i + 1);
        }
        return result;
    }

    @Override // soot.toolkits.graph.DominatorsFinder
    public N getImmediateDominator(N node) {
        if (this.heads.contains(node)) {
            return null;
        }
        BitSet doms = (BitSet) getDominatorsBitSet(node).clone();
        doms.clear(indexOfAssert(node));
        int nextSetBit = doms.nextSetBit(0);
        while (true) {
            int i = nextSetBit;
            if (i >= 0) {
                N dominator = this.indexToNode.get(Integer.valueOf(i));
                if (isDominatedByAll((MHGDominatorsFinder<N>) dominator, doms) && dominator != null) {
                    return dominator;
                }
                if (i != Integer.MAX_VALUE) {
                    nextSetBit = doms.nextSetBit(i + 1);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    private boolean isDominatedByAll(N node, BitSet doms) {
        BitSet s1 = getDominatorsBitSet(node);
        int nextSetBit = doms.nextSetBit(0);
        while (true) {
            int i = nextSetBit;
            if (i >= 0) {
                if (!s1.get(i)) {
                    return false;
                }
                if (i != Integer.MAX_VALUE) {
                    nextSetBit = doms.nextSetBit(i + 1);
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    @Override // soot.toolkits.graph.DominatorsFinder
    public boolean isDominatedBy(N node, N dominator) {
        return getDominatorsBitSet(node).get(indexOfAssert(dominator));
    }

    @Override // soot.toolkits.graph.DominatorsFinder
    public boolean isDominatedByAll(N node, Collection<N> dominators) {
        BitSet s1 = getDominatorsBitSet(node);
        for (N n : dominators) {
            if (!s1.get(indexOfAssert(n))) {
                return false;
            }
        }
        return true;
    }
}
