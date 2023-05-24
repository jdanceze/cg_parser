package org.objectweb.asm;
/* loaded from: gencallgraphv3.jar:asm-9.4.jar:org/objectweb/asm/Edge.class */
final class Edge {
    static final int JUMP = 0;
    static final int EXCEPTION = Integer.MAX_VALUE;
    final int info;
    final Label successor;
    Edge nextEdge;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Edge(int info, Label successor, Edge nextEdge) {
        this.info = info;
        this.successor = successor;
        this.nextEdge = nextEdge;
    }
}
