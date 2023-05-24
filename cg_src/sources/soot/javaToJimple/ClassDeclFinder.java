package soot.javaToJimple;

import java.util.ArrayList;
import polyglot.ast.ClassDecl;
import polyglot.ast.Node;
import polyglot.visit.NodeVisitor;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/ClassDeclFinder.class */
public class ClassDeclFinder extends NodeVisitor {
    private final ArrayList<ClassDecl> declsFound = new ArrayList<>();

    public ArrayList<ClassDecl> declsFound() {
        return this.declsFound;
    }

    @Override // polyglot.visit.NodeVisitor
    public NodeVisitor enter(Node parent, Node n) {
        if (n instanceof ClassDecl) {
            this.declsFound.add((ClassDecl) n);
        }
        return enter(n);
    }
}
