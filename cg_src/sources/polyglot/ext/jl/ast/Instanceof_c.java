package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Expr;
import polyglot.ast.Instanceof;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Instanceof_c.class */
public class Instanceof_c extends Expr_c implements Instanceof {
    protected Expr expr;
    protected TypeNode compareType;

    public Instanceof_c(Position pos, Expr expr, TypeNode compareType) {
        super(pos);
        this.expr = expr;
        this.compareType = compareType;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Precedence precedence() {
        return Precedence.INSTANCEOF;
    }

    @Override // polyglot.ast.Instanceof
    public Expr expr() {
        return this.expr;
    }

    @Override // polyglot.ast.Instanceof
    public Instanceof expr(Expr expr) {
        Instanceof_c n = (Instanceof_c) copy();
        n.expr = expr;
        return n;
    }

    @Override // polyglot.ast.Instanceof
    public TypeNode compareType() {
        return this.compareType;
    }

    @Override // polyglot.ast.Instanceof
    public Instanceof compareType(TypeNode compareType) {
        Instanceof_c n = (Instanceof_c) copy();
        n.compareType = compareType;
        return n;
    }

    protected Instanceof_c reconstruct(Expr expr, TypeNode compareType) {
        if (expr != this.expr || compareType != this.compareType) {
            Instanceof_c n = (Instanceof_c) copy();
            n.expr = expr;
            n.compareType = compareType;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Expr expr = (Expr) visitChild(this.expr, v);
        TypeNode compareType = (TypeNode) visitChild(this.compareType, v);
        return reconstruct(expr, compareType);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        if (!this.compareType.type().isReference()) {
            throw new SemanticException("Type operand of \"instanceof\" must be a reference type.", this.compareType.position());
        }
        if (!ts.isCastValid(this.expr.type(), this.compareType.type())) {
            throw new SemanticException("Expression operand incompatible with type in \"instanceof\".", this.expr.position());
        }
        return type(ts.Boolean());
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        TypeSystem ts = av.typeSystem();
        if (child == this.expr) {
            return ts.Object();
        }
        return child.type();
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.expr).append(" instanceof ").append(this.compareType).toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        printSubExpr(this.expr, w, tr);
        w.write(" instanceof ");
        print(this.compareType, w, tr);
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.expr.entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        v.visitCFG(this.expr, this);
        return succs;
    }
}
