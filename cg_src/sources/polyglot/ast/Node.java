package polyglot.ast;

import java.util.List;
import polyglot.types.Type;
import polyglot.util.CodeWriter;
import polyglot.util.Copy;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.NodeVisitor;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Node.class */
public interface Node extends JL, Copy {
    Node del(JL jl);

    JL del();

    Node ext(Ext ext);

    Ext ext();

    Node ext(int i, Ext ext);

    Ext ext(int i);

    Position position();

    Node position(Position position);

    Node visit(NodeVisitor nodeVisitor);

    Node visitEdge(Node node, NodeVisitor nodeVisitor);

    Node visitChild(Node node, NodeVisitor nodeVisitor);

    List visitList(List list, NodeVisitor nodeVisitor);

    Type childExpectedType(Expr expr, AscriptionVisitor ascriptionVisitor);

    void dump(CodeWriter codeWriter);
}
