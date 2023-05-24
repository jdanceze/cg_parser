package soot.javaToJimple;

import polyglot.ast.Assert;
import polyglot.ast.ClassDecl;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.visit.NodeVisitor;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/AssertStmtChecker.class */
public class AssertStmtChecker extends NodeVisitor {
    private boolean hasAssert = false;

    public boolean isHasAssert() {
        return this.hasAssert;
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
        if (n instanceof Assert) {
            this.hasAssert = true;
        }
        return enter(n);
    }
}
