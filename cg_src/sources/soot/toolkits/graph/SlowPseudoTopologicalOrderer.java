package soot.toolkits.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import soot.G;
import soot.Singletons;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/SlowPseudoTopologicalOrderer.class */
public class SlowPseudoTopologicalOrderer<N> implements Orderer<N> {
    private boolean mIsReversed;

    public SlowPseudoTopologicalOrderer(Singletons.Global g) {
        this.mIsReversed = false;
    }

    public static SlowPseudoTopologicalOrderer v() {
        return G.v().soot_toolkits_graph_SlowPseudoTopologicalOrderer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/toolkits/graph/SlowPseudoTopologicalOrderer$AbstractOrderBuilder.class */
    public static abstract class AbstractOrderBuilder<N> {
        protected final Map<N, Color> stmtToColor = new HashMap();
        protected final LinkedList<N> order = new LinkedList<>();
        protected final DirectedGraph<N> graph;
        protected final boolean reverse;

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:soot/toolkits/graph/SlowPseudoTopologicalOrderer$AbstractOrderBuilder$Color.class */
        public enum Color {
            WHITE,
            GRAY,
            BLACK;

            /* renamed from: values  reason: to resolve conflict with enum method */
            public static Color[] valuesCustom() {
                Color[] valuesCustom = values();
                int length = valuesCustom.length;
                Color[] colorArr = new Color[length];
                System.arraycopy(valuesCustom, 0, colorArr, 0, length);
                return colorArr;
            }
        }

        protected abstract void visitNode(N n);

        protected AbstractOrderBuilder(DirectedGraph<N> g, boolean reverse) {
            this.graph = g;
            this.reverse = reverse;
        }

        public LinkedList<N> computeOrder() {
            for (N s : this.graph) {
                this.stmtToColor.put(s, Color.WHITE);
            }
            for (N s2 : this.graph) {
                if (this.stmtToColor.get(s2) == Color.WHITE) {
                    visitNode(s2);
                }
            }
            return this.order;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/toolkits/graph/SlowPseudoTopologicalOrderer$ForwardOrderBuilder.class */
    private static class ForwardOrderBuilder<N> extends AbstractOrderBuilder<N> {
        private final HashMap<N, List<N>> succsMap;
        private List<N> reverseOrder;

        public ForwardOrderBuilder(DirectedGraph<N> graph, boolean reverse) {
            super(graph, reverse);
            this.succsMap = new HashMap<>();
        }

        @Override // soot.toolkits.graph.SlowPseudoTopologicalOrderer.AbstractOrderBuilder
        public LinkedList<N> computeOrder() {
            this.reverseOrder = new ReverseOrderBuilder(this.graph).computeOrder();
            return super.computeOrder();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // soot.toolkits.graph.SlowPseudoTopologicalOrderer.AbstractOrderBuilder
        protected void visitNode(N startStmt) {
            LinkedList<N> stmtStack = new LinkedList<>();
            LinkedList<Integer> indexStack = new LinkedList<>();
            this.stmtToColor.put(startStmt, AbstractOrderBuilder.Color.GRAY);
            stmtStack.addLast(startStmt);
            indexStack.addLast(-1);
            while (!stmtStack.isEmpty()) {
                int toVisitIndex = indexStack.removeLast().intValue();
                N toVisitNode = stmtStack.getLast();
                int toVisitIndex2 = toVisitIndex + 1;
                indexStack.addLast(Integer.valueOf(toVisitIndex2));
                if (toVisitIndex2 >= this.graph.getSuccsOf(toVisitNode).size()) {
                    if (this.reverse) {
                        this.order.addLast(toVisitNode);
                    } else {
                        this.order.addFirst(toVisitNode);
                    }
                    this.stmtToColor.put(toVisitNode, AbstractOrderBuilder.Color.BLACK);
                    stmtStack.removeLast();
                    indexStack.removeLast();
                } else {
                    LinkedList orderedSuccs = this.succsMap.get(toVisitNode);
                    if (orderedSuccs == null) {
                        orderedSuccs = new LinkedList();
                        this.succsMap.put(toVisitNode, orderedSuccs);
                        List<N> allsuccs = this.graph.getSuccsOf(toVisitNode);
                        for (int i = 0; i < allsuccs.size(); i++) {
                            N cur = allsuccs.get(i);
                            int j = 0;
                            while (j < orderedSuccs.size()) {
                                N comp = orderedSuccs.get(j);
                                if (this.reverseOrder.indexOf(cur) < this.reverseOrder.indexOf(comp)) {
                                    break;
                                }
                                j++;
                            }
                            orderedSuccs.add(j, cur);
                        }
                    }
                    N childNode = orderedSuccs.get(toVisitIndex2);
                    if (this.stmtToColor.get(childNode) == AbstractOrderBuilder.Color.WHITE) {
                        this.stmtToColor.put(childNode, AbstractOrderBuilder.Color.GRAY);
                        stmtStack.addLast(childNode);
                        indexStack.addLast(-1);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/toolkits/graph/SlowPseudoTopologicalOrderer$ReverseOrderBuilder.class */
    public static class ReverseOrderBuilder<N> extends AbstractOrderBuilder<N> {
        public ReverseOrderBuilder(DirectedGraph<N> graph) {
            super(graph, false);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // soot.toolkits.graph.SlowPseudoTopologicalOrderer.AbstractOrderBuilder
        protected void visitNode(N startStmt) {
            LinkedList linkedList = new LinkedList();
            LinkedList<Integer> indexStack = new LinkedList<>();
            this.stmtToColor.put(startStmt, AbstractOrderBuilder.Color.GRAY);
            linkedList.addLast(startStmt);
            indexStack.addLast(-1);
            while (!linkedList.isEmpty()) {
                int toVisitIndex = indexStack.removeLast().intValue();
                Object last = linkedList.getLast();
                int toVisitIndex2 = toVisitIndex + 1;
                indexStack.addLast(Integer.valueOf(toVisitIndex2));
                if (toVisitIndex2 >= this.graph.getPredsOf(last).size()) {
                    if (this.reverse) {
                        this.order.addLast(last);
                    } else {
                        this.order.addFirst(last);
                    }
                    this.stmtToColor.put(last, AbstractOrderBuilder.Color.BLACK);
                    linkedList.removeLast();
                    indexStack.removeLast();
                } else {
                    Object obj = this.graph.getPredsOf(last).get(toVisitIndex2);
                    if (this.stmtToColor.get(obj) == AbstractOrderBuilder.Color.WHITE) {
                        this.stmtToColor.put(obj, AbstractOrderBuilder.Color.GRAY);
                        linkedList.addLast(obj);
                        indexStack.addLast(-1);
                    }
                }
            }
        }
    }

    public SlowPseudoTopologicalOrderer() {
        this.mIsReversed = false;
    }

    @Override // soot.toolkits.graph.Orderer
    public List<N> newList(DirectedGraph<N> g, boolean reverse) {
        this.mIsReversed = reverse;
        return new ForwardOrderBuilder(g, reverse).computeOrder();
    }

    public SlowPseudoTopologicalOrderer(boolean isReversed) {
        this.mIsReversed = false;
        this.mIsReversed = isReversed;
    }

    @Deprecated
    public List<N> newList(DirectedGraph<N> g) {
        return new ForwardOrderBuilder(g, this.mIsReversed).computeOrder();
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
