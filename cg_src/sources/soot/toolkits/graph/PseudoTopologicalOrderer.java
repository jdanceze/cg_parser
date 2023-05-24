package soot.toolkits.graph;

import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/PseudoTopologicalOrderer.class */
public class PseudoTopologicalOrderer<N> implements Orderer<N> {
    public static final boolean REVERSE = true;
    private boolean mIsReversed;

    /* loaded from: gencallgraphv3.jar:soot/toolkits/graph/PseudoTopologicalOrderer$ReverseOrderBuilder.class */
    private static class ReverseOrderBuilder<N> {
        private final DirectedGraph<N> graph;
        private final int graphSize;
        private final int[] indexStack;
        private final N[] stmtStack;
        private final Set<N> visited;
        private final N[] order;
        private int orderLength;

        public ReverseOrderBuilder(DirectedGraph<N> g) {
            this.graph = g;
            int n = g.size();
            this.graphSize = n;
            this.visited = Collections.newSetFromMap(new IdentityHashMap((n * 2) + 1));
            this.indexStack = new int[n];
            this.stmtStack = (N[]) new Object[n];
            this.order = (N[]) new Object[n];
            this.orderLength = 0;
        }

        public List<N> computeOrder(boolean reverse) {
            for (N s : this.graph) {
                if (this.visited.add(s)) {
                    visitNode(s);
                }
                if (this.orderLength == this.graphSize) {
                    break;
                }
            }
            if (reverse) {
                reverseArray(this.order);
            }
            return Arrays.asList(this.order);
        }

        private void visitNode(N startStmt) {
            this.stmtStack[0] = startStmt;
            int last = 0 + 1;
            this.indexStack[0] = -1;
            while (last > 0) {
                int[] iArr = this.indexStack;
                int i = last - 1;
                int toVisitIndex = iArr[i] + 1;
                iArr[i] = toVisitIndex;
                N toVisitNode = this.stmtStack[last - 1];
                List<N> succs = this.graph.getSuccsOf(toVisitNode);
                if (toVisitIndex >= succs.size()) {
                    N[] nArr = this.order;
                    int i2 = this.orderLength;
                    this.orderLength = i2 + 1;
                    nArr[i2] = toVisitNode;
                    last--;
                } else {
                    N childNode = succs.get(toVisitIndex);
                    if (this.visited.add(childNode)) {
                        this.stmtStack[last] = childNode;
                        int i3 = last;
                        last++;
                        this.indexStack[i3] = -1;
                    }
                }
            }
        }

        private static <T> void reverseArray(T[] tArr) {
            int max = tArr.length >> 1;
            int i = 0;
            int j = tArr.length - 1;
            while (i < max) {
                T temp = tArr[i];
                tArr[i] = tArr[j];
                tArr[j] = temp;
                i++;
                j--;
            }
        }
    }

    public PseudoTopologicalOrderer() {
        this.mIsReversed = false;
    }

    @Override // soot.toolkits.graph.Orderer
    public List<N> newList(DirectedGraph<N> g, boolean reverse) {
        this.mIsReversed = reverse;
        return new ReverseOrderBuilder(g).computeOrder(!reverse);
    }

    @Deprecated
    public PseudoTopologicalOrderer(boolean isReversed) {
        this.mIsReversed = false;
        this.mIsReversed = isReversed;
    }

    @Deprecated
    public List<N> newList(DirectedGraph<N> g) {
        return new ReverseOrderBuilder(g).computeOrder(!this.mIsReversed);
    }

    @Deprecated
    public void setReverseOrder(boolean isReversed) {
        this.mIsReversed = isReversed;
    }

    @Deprecated
    public boolean isReverseOrder() {
        return this.mIsReversed;
    }
}
