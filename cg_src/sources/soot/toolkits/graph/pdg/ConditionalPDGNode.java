package soot.toolkits.graph.pdg;

import soot.toolkits.graph.pdg.PDGNode;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/pdg/ConditionalPDGNode.class */
public class ConditionalPDGNode extends PDGNode {
    public ConditionalPDGNode(Object obj, PDGNode.Type t) {
        super(obj, t);
    }

    public ConditionalPDGNode(PDGNode node) {
        this(node.getNode(), node.getType());
        this.m_dependents.addAll(node.m_dependents);
        this.m_backDependents.addAll(node.m_backDependents);
        this.m_next = node.m_next;
        this.m_prev = node.m_prev;
    }
}
