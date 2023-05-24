package soot.javaToJimple;

import polyglot.ast.ClassDecl;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.LocalClassDecl;
import polyglot.ast.MethodDecl;
import polyglot.ast.Node;
import polyglot.visit.NodeVisitor;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/StrictFPPropagator.class */
public class StrictFPPropagator extends NodeVisitor {
    boolean strict;

    public StrictFPPropagator(boolean val) {
        this.strict = false;
        this.strict = val;
    }

    @Override // polyglot.visit.NodeVisitor
    public NodeVisitor enter(Node parent, Node n) {
        if ((n instanceof ClassDecl) && ((ClassDecl) n).flags().isStrictFP()) {
            return new StrictFPPropagator(true);
        }
        if ((n instanceof LocalClassDecl) && ((LocalClassDecl) n).decl().flags().isStrictFP()) {
            return new StrictFPPropagator(true);
        }
        if ((n instanceof MethodDecl) && ((MethodDecl) n).flags().isStrictFP()) {
            return new StrictFPPropagator(true);
        }
        if ((n instanceof ConstructorDecl) && ((ConstructorDecl) n).flags().isStrictFP()) {
            return new StrictFPPropagator(true);
        }
        return this;
    }

    @Override // polyglot.visit.NodeVisitor
    public Node leave(Node old, Node n, NodeVisitor nodeVisitor) {
        if (n instanceof MethodDecl) {
            MethodDecl decl = (MethodDecl) n;
            if (this.strict && !decl.flags().isAbstract() && !decl.flags().isStrictFP()) {
                return decl.flags(decl.flags().StrictFP());
            }
        }
        if (n instanceof ConstructorDecl) {
            ConstructorDecl decl2 = (ConstructorDecl) n;
            if (this.strict && !decl2.flags().isAbstract() && !decl2.flags().isStrictFP()) {
                return decl2.flags(decl2.flags().StrictFP());
            }
        }
        if (n instanceof LocalClassDecl) {
            LocalClassDecl decl3 = (LocalClassDecl) n;
            if (decl3.decl().flags().isStrictFP()) {
                return decl3.decl().flags(decl3.decl().flags().clearStrictFP());
            }
        }
        if (n instanceof ClassDecl) {
            ClassDecl decl4 = (ClassDecl) n;
            if (decl4.flags().isStrictFP()) {
                return decl4.flags(decl4.flags().clearStrictFP());
            }
        }
        return n;
    }
}
