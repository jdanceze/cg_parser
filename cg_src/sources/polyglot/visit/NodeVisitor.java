package polyglot.visit;

import polyglot.ast.Node;
import polyglot.util.InternalCompilerError;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/NodeVisitor.class */
public abstract class NodeVisitor {
    public Node override(Node parent, Node n) {
        return override(n);
    }

    public Node override(Node n) {
        return null;
    }

    public NodeVisitor enter(Node parent, Node n) {
        return enter(n);
    }

    public NodeVisitor enter(Node n) {
        return this;
    }

    public Node leave(Node parent, Node old, Node n, NodeVisitor v) {
        return leave(old, n, v);
    }

    public Node leave(Node old, Node n, NodeVisitor v) {
        return n;
    }

    public NodeVisitor begin() {
        return this;
    }

    public void finish() {
    }

    public void finish(Node ast) {
        finish();
    }

    public String toString() {
        return getClass().getName();
    }

    public Node visitEdge(Node parent, Node child) {
        Node n = override(parent, child);
        if (n == null) {
            NodeVisitor v_ = enter(parent, child);
            if (v_ == null) {
                throw new InternalCompilerError("NodeVisitor.enter() returned null.");
            }
            Node n2 = child.visitChildren(v_);
            if (n2 == null) {
                throw new InternalCompilerError("Node_c.visitChildren() returned null.");
            }
            n = leave(parent, child, n2, v_);
            if (n == null) {
                throw new InternalCompilerError("NodeVisitor.leave() returned null.");
            }
        }
        return n;
    }
}
