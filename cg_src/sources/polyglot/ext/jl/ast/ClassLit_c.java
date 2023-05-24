package polyglot.ext.jl.ast;

import polyglot.ast.ClassLit;
import polyglot.ast.Node;
import polyglot.ast.TypeNode;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/ClassLit_c.class */
public class ClassLit_c extends Lit_c implements ClassLit {
    protected TypeNode typeNode;

    public ClassLit_c(Position pos, TypeNode typeNode) {
        super(pos);
        this.typeNode = typeNode;
    }

    @Override // polyglot.ast.ClassLit
    public TypeNode typeNode() {
        return this.typeNode;
    }

    public ClassLit typeNode(TypeNode typeNode) {
        if (this.typeNode == typeNode) {
            return this;
        }
        ClassLit_c n = (ClassLit_c) copy();
        n.typeNode = typeNode;
        return n;
    }

    public Object objValue() {
        return null;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        TypeNode tn = (TypeNode) visitChild(this.typeNode, v);
        return typeNode(tn);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        return type(tc.typeSystem().Class());
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.typeNode.toString()).append(".class").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.begin(0);
        print(this.typeNode, w, tr);
        w.write(".class");
        w.end();
    }

    @Override // polyglot.ext.jl.ast.Lit_c, polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public boolean isConstant() {
        return false;
    }

    @Override // polyglot.ext.jl.ast.Lit_c, polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Object constantValue() {
        return null;
    }
}
