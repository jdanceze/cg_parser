package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/graph/EndpointPairIterator.class */
public abstract class EndpointPairIterator<N> extends AbstractIterator<EndpointPair<N>> {
    private final BaseGraph<N> graph;
    private final Iterator<N> nodeIterator;
    protected N node;
    protected Iterator<N> successorIterator;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <N> EndpointPairIterator<N> of(BaseGraph<N> graph) {
        return graph.isDirected() ? new Directed(graph) : new Undirected(graph);
    }

    private EndpointPairIterator(BaseGraph<N> graph) {
        this.node = null;
        this.successorIterator = ImmutableSet.of().iterator();
        this.graph = graph;
        this.nodeIterator = graph.nodes().iterator();
    }

    protected final boolean advance() {
        Preconditions.checkState(!this.successorIterator.hasNext());
        if (!this.nodeIterator.hasNext()) {
            return false;
        }
        this.node = this.nodeIterator.next();
        this.successorIterator = this.graph.successors((BaseGraph<N>) this.node).iterator();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/graph/EndpointPairIterator$Directed.class */
    public static final class Directed<N> extends EndpointPairIterator<N> {
        private Directed(BaseGraph<N> graph) {
            super(graph);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.AbstractIterator
        public EndpointPair<N> computeNext() {
            while (!this.successorIterator.hasNext()) {
                if (!advance()) {
                    return endOfData();
                }
            }
            return EndpointPair.ordered(this.node, this.successorIterator.next());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/graph/EndpointPairIterator$Undirected.class */
    public static final class Undirected<N> extends EndpointPairIterator<N> {
        private Set<N> visitedNodes;

        private Undirected(BaseGraph<N> graph) {
            super(graph);
            this.visitedNodes = Sets.newHashSetWithExpectedSize(graph.nodes().size());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.AbstractIterator
        public EndpointPair<N> computeNext() {
            while (true) {
                if (this.successorIterator.hasNext()) {
                    N otherNode = this.successorIterator.next();
                    if (!this.visitedNodes.contains(otherNode)) {
                        return EndpointPair.unordered(this.node, otherNode);
                    }
                } else {
                    this.visitedNodes.add(this.node);
                    if (!advance()) {
                        this.visitedNodes = null;
                        return endOfData();
                    }
                }
            }
        }
    }
}
