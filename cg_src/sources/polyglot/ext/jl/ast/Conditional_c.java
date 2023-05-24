package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Conditional;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Term;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.FlowGraph;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Conditional_c.class */
public class Conditional_c extends Expr_c implements Conditional {
    protected Expr cond;
    protected Expr consequent;
    protected Expr alternative;

    public Conditional_c(Position pos, Expr cond, Expr consequent, Expr alternative) {
        super(pos);
        this.cond = cond;
        this.consequent = consequent;
        this.alternative = alternative;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Precedence precedence() {
        return Precedence.CONDITIONAL;
    }

    @Override // polyglot.ast.Conditional
    public Expr cond() {
        return this.cond;
    }

    @Override // polyglot.ast.Conditional
    public Conditional cond(Expr cond) {
        Conditional_c n = (Conditional_c) copy();
        n.cond = cond;
        return n;
    }

    @Override // polyglot.ast.Conditional
    public Expr consequent() {
        return this.consequent;
    }

    @Override // polyglot.ast.Conditional
    public Conditional consequent(Expr consequent) {
        Conditional_c n = (Conditional_c) copy();
        n.consequent = consequent;
        return n;
    }

    @Override // polyglot.ast.Conditional
    public Expr alternative() {
        return this.alternative;
    }

    @Override // polyglot.ast.Conditional
    public Conditional alternative(Expr alternative) {
        Conditional_c n = (Conditional_c) copy();
        n.alternative = alternative;
        return n;
    }

    protected Conditional_c reconstruct(Expr cond, Expr consequent, Expr alternative) {
        if (cond != this.cond || consequent != this.consequent || alternative != this.alternative) {
            Conditional_c n = (Conditional_c) copy();
            n.cond = cond;
            n.consequent = consequent;
            n.alternative = alternative;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Expr cond = (Expr) visitChild(this.cond, v);
        Expr consequent = (Expr) visitChild(this.consequent, v);
        Expr alternative = (Expr) visitChild(this.alternative, v);
        return reconstruct(cond, consequent, alternative);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        if (!ts.equals(this.cond.type(), ts.Boolean())) {
            throw new SemanticException("Condition of ternary expression must be of type boolean.", this.cond.position());
        }
        Expr e1 = this.consequent;
        Expr e2 = this.alternative;
        Type t1 = e1.type();
        Type t2 = e2.type();
        if (ts.equals(t1, t2)) {
            return type(t1);
        }
        if (t1.isNumeric() && t2.isNumeric()) {
            if ((t1.isByte() && t2.isShort()) || (t1.isShort() && t2.isByte())) {
                return type(ts.Short());
            }
            if (t1.isIntOrLess() && t2.isInt() && ts.numericConversionValid(t1, e2.constantValue())) {
                return type(t1);
            }
            if (t2.isIntOrLess() && t1.isInt() && ts.numericConversionValid(t2, e1.constantValue())) {
                return type(t2);
            }
            return type(ts.promote(t1, t2));
        } else if (t1.isNull() && t2.isReference()) {
            return type(t2);
        } else {
            if (t2.isNull() && t1.isReference()) {
                return type(t1);
            }
            if (t1.isReference() && t2.isReference()) {
                if (ts.isImplicitCastValid(t1, t2)) {
                    return type(t2);
                }
                if (ts.isImplicitCastValid(t2, t1)) {
                    return type(t1);
                }
            }
            throw new SemanticException("Could not find a type for ternary conditional expression.", position());
        }
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        TypeSystem ts = av.typeSystem();
        if (child == this.cond) {
            return ts.Boolean();
        }
        if (child == this.consequent || child == this.alternative) {
            return type();
        }
        return child.type();
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.cond).append(" ? ").append(this.consequent).append(" : ").append(this.alternative).toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        printSubExpr(this.cond, false, w, tr);
        w.write(" ? ");
        printSubExpr(this.consequent, false, w, tr);
        w.write(" : ");
        printSubExpr(this.alternative, false, w, tr);
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.cond.entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        v.visitCFG(this.cond, FlowGraph.EDGE_KEY_TRUE, this.consequent.entry(), FlowGraph.EDGE_KEY_FALSE, this.alternative.entry());
        v.visitCFG(this.consequent, this);
        v.visitCFG(this.alternative, this);
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public boolean isConstant() {
        return this.cond.isConstant() && this.consequent.isConstant() && this.alternative.isConstant();
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Object constantValue() {
        Object cond_ = this.cond.constantValue();
        Object then_ = this.consequent.constantValue();
        Object else_ = this.alternative.constantValue();
        if ((cond_ instanceof Boolean) && then_ != null && else_ != null) {
            boolean c = ((Boolean) cond_).booleanValue();
            if (c) {
                return then_;
            }
            return else_;
        }
        return null;
    }
}
