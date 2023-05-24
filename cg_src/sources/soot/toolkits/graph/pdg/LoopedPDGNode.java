package soot.toolkits.graph.pdg;

import soot.toolkits.graph.pdg.PDGNode;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/pdg/LoopedPDGNode.class */
public class LoopedPDGNode extends PDGNode {
    protected PDGNode m_header;
    protected PDGNode m_body;

    public LoopedPDGNode(Region obj, PDGNode.Type t, PDGNode c) {
        super(obj, t);
        this.m_header = null;
        this.m_body = null;
        this.m_header = c;
    }

    public PDGNode getHeader() {
        return this.m_header;
    }

    public void setBody(PDGNode b) {
        this.m_body = b;
    }

    public PDGNode getBody() {
        return this.m_body;
    }
}
