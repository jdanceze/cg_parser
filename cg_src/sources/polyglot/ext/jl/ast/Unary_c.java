package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Expr;
import polyglot.ast.IntLit;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Term;
import polyglot.ast.Unary;
import polyglot.ast.Variable;
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
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Unary_c.class */
public class Unary_c extends Expr_c implements Unary {
    protected Unary.Operator op;
    protected Expr expr;

    public Unary_c(Position pos, Unary.Operator op, Expr expr) {
        super(pos);
        this.op = op;
        this.expr = expr;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Precedence precedence() {
        return Precedence.UNARY;
    }

    @Override // polyglot.ast.Unary
    public Expr expr() {
        return this.expr;
    }

    @Override // polyglot.ast.Unary
    public Unary expr(Expr expr) {
        Unary_c n = (Unary_c) copy();
        n.expr = expr;
        return n;
    }

    @Override // polyglot.ast.Unary
    public Unary.Operator operator() {
        return this.op;
    }

    @Override // polyglot.ast.Unary
    public Unary operator(Unary.Operator op) {
        Unary_c n = (Unary_c) copy();
        n.op = op;
        return n;
    }

    protected Unary_c reconstruct(Expr expr) {
        if (expr != this.expr) {
            Unary_c n = (Unary_c) copy();
            n.expr = expr;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Expr expr = (Expr) visitChild(this.expr, v);
        return reconstruct(expr);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        if (this.op == Unary.POST_INC || this.op == Unary.POST_DEC || this.op == Unary.PRE_INC || this.op == Unary.PRE_DEC) {
            if (!this.expr.type().isNumeric()) {
                throw new SemanticException(new StringBuffer().append("Operand of ").append(this.op).append(" operator must be numeric.").toString(), this.expr.position());
            }
            if (!(this.expr instanceof Variable)) {
                throw new SemanticException(new StringBuffer().append("Operand of ").append(this.op).append(" operator must be a variable.").toString(), this.expr.position());
            }
            if (((Variable) this.expr).flags().isFinal()) {
                throw new SemanticException(new StringBuffer().append("Operand of ").append(this.op).append(" operator must be a non-final variable.").toString(), this.expr.position());
            }
            return type(this.expr.type());
        } else if (this.op == Unary.BIT_NOT) {
            if (!ts.isImplicitCastValid(this.expr.type(), ts.Long())) {
                throw new SemanticException(new StringBuffer().append("Operand of ").append(this.op).append(" operator must be numeric.").toString(), this.expr.position());
            }
            return type(ts.promote(this.expr.type()));
        } else if (this.op == Unary.NEG || this.op == Unary.POS) {
            if (!this.expr.type().isNumeric()) {
                throw new SemanticException(new StringBuffer().append("Operand of ").append(this.op).append(" operator must be numeric.").toString(), this.expr.position());
            }
            return type(ts.promote(this.expr.type()));
        } else if (this.op == Unary.NOT) {
            if (!this.expr.type().isBoolean()) {
                throw new SemanticException(new StringBuffer().append("Operand of ").append(this.op).append(" operator must be boolean.").toString(), this.expr.position());
            }
            return type(this.expr.type());
        } else {
            return this;
        }
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        TypeSystem ts = av.typeSystem();
        try {
            if (child == this.expr) {
                if (this.op == Unary.POST_INC || this.op == Unary.POST_DEC || this.op == Unary.PRE_INC || this.op == Unary.PRE_DEC) {
                    if (ts.isImplicitCastValid(child.type(), av.toType())) {
                        return ts.promote(child.type());
                    }
                    return av.toType();
                } else if (this.op == Unary.NEG || this.op == Unary.POS) {
                    if (ts.isImplicitCastValid(child.type(), av.toType())) {
                        return ts.promote(child.type());
                    }
                    return av.toType();
                } else if (this.op == Unary.BIT_NOT) {
                    if (ts.isImplicitCastValid(child.type(), av.toType())) {
                        return ts.promote(child.type());
                    }
                    return av.toType();
                } else if (this.op == Unary.NOT) {
                    return ts.Boolean();
                }
            }
        } catch (SemanticException e) {
        }
        return child.type();
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        if (this.op == Unary.NEG && (this.expr instanceof IntLit) && ((IntLit) this.expr).boundary()) {
            return new StringBuffer().append(this.op.toString()).append(((IntLit) this.expr).positiveToString()).toString();
        }
        if (this.op.isPrefix()) {
            return new StringBuffer().append(this.op.toString()).append(this.expr.toString()).toString();
        }
        return new StringBuffer().append(this.expr.toString()).append(this.op.toString()).toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        if (this.op == Unary.NEG && (this.expr instanceof IntLit) && ((IntLit) this.expr).boundary()) {
            w.write(this.op.toString());
            w.write(((IntLit) this.expr).positiveToString());
        } else if (this.op.isPrefix()) {
            w.write(this.op.toString());
            printSubExpr(this.expr, false, w, tr);
        } else {
            printSubExpr(this.expr, false, w, tr);
            w.write(this.op.toString());
        }
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.expr.entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        if (this.expr.type().isBoolean()) {
            v.visitCFG(this.expr, FlowGraph.EDGE_KEY_TRUE, this, FlowGraph.EDGE_KEY_FALSE, this);
        } else {
            v.visitCFG(this.expr, this);
        }
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public boolean isConstant() {
        return this.expr.isConstant();
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Object constantValue() {
        if (!isConstant()) {
            return null;
        }
        Object v = this.expr.constantValue();
        if (v instanceof Boolean) {
            boolean vv = ((Boolean) v).booleanValue();
            if (this.op == Unary.NOT) {
                return Boolean.valueOf(!vv);
            }
        }
        if (v instanceof Double) {
            double vv2 = ((Double) v).doubleValue();
            if (this.op == Unary.POS) {
                return new Double(vv2);
            }
            if (this.op == Unary.NEG) {
                return new Double(-vv2);
            }
        }
        if (v instanceof Float) {
            float vv3 = ((Float) v).floatValue();
            if (this.op == Unary.POS) {
                return new Float(vv3);
            }
            if (this.op == Unary.NEG) {
                return new Float(-vv3);
            }
        }
        if (v instanceof Long) {
            long vv4 = ((Long) v).longValue();
            if (this.op == Unary.BIT_NOT) {
                return new Long(vv4 ^ (-1));
            }
            if (this.op == Unary.POS) {
                return new Long(vv4);
            }
            if (this.op == Unary.NEG) {
                return new Long(-vv4);
            }
        }
        if (v instanceof Integer) {
            int vv5 = ((Integer) v).intValue();
            if (this.op == Unary.BIT_NOT) {
                return new Integer(vv5 ^ (-1));
            }
            if (this.op == Unary.POS) {
                return new Integer(vv5);
            }
            if (this.op == Unary.NEG) {
                return new Integer(-vv5);
            }
        }
        if (v instanceof Character) {
            char vv6 = ((Character) v).charValue();
            if (this.op == Unary.BIT_NOT) {
                return new Integer(vv6 ^ 65535);
            }
            if (this.op == Unary.POS) {
                return new Integer(vv6);
            }
            if (this.op == Unary.NEG) {
                return new Integer(-vv6);
            }
        }
        if (v instanceof Short) {
            short vv7 = ((Short) v).shortValue();
            if (this.op == Unary.BIT_NOT) {
                return new Integer(vv7 ^ (-1));
            }
            if (this.op == Unary.POS) {
                return new Integer(vv7);
            }
            if (this.op == Unary.NEG) {
                return new Integer(-vv7);
            }
        }
        if (v instanceof Byte) {
            byte vv8 = ((Byte) v).byteValue();
            if (this.op == Unary.BIT_NOT) {
                return new Integer(vv8 ^ (-1));
            }
            if (this.op == Unary.POS) {
                return new Integer(vv8);
            }
            if (this.op == Unary.NEG) {
                return new Integer(-vv8);
            }
            return null;
        }
        return null;
    }
}
