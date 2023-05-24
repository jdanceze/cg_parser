package soot.javaToJimple;

import java.util.ArrayList;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassLit;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.visit.NodeVisitor;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/ClassLiteralChecker.class */
public class ClassLiteralChecker extends NodeVisitor {
    private final ArrayList<Node> list = new ArrayList<>();

    public ArrayList<Node> getList() {
        return this.list;
    }

    @Override // polyglot.visit.NodeVisitor
    public Node override(Node parent, Node n) {
        if ((n instanceof ClassDecl) || ((n instanceof New) && ((New) n).anonType() != null)) {
            return n;
        }
        return null;
    }

    @Override // polyglot.visit.NodeVisitor
    public NodeVisitor enter(Node parent, Node n) {
        if (n instanceof ClassLit) {
            ClassLit lit = (ClassLit) n;
            if (!lit.typeNode().type().isPrimitive()) {
                this.list.add(n);
            }
        }
        return enter(n);
    }
}
