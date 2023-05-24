package soot.javaToJimple;

import polyglot.ast.LocalClassDecl;
import polyglot.ast.Node;
import polyglot.types.ClassType;
import polyglot.visit.NodeVisitor;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/LocalClassDeclFinder.class */
public class LocalClassDeclFinder extends NodeVisitor {
    private ClassType typeToFind;
    private LocalClassDecl declFound = null;

    public void typeToFind(ClassType type) {
        this.typeToFind = type;
    }

    public LocalClassDecl declFound() {
        return this.declFound;
    }

    @Override // polyglot.visit.NodeVisitor
    public NodeVisitor enter(Node parent, Node n) {
        if ((n instanceof LocalClassDecl) && ((LocalClassDecl) n).decl().type().equals(this.typeToFind)) {
            this.declFound = (LocalClassDecl) n;
        }
        return enter(n);
    }
}
