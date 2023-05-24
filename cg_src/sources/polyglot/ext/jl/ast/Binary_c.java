package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.List;
import polyglot.ast.Binary;
import polyglot.ast.BooleanLit;
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
import soot.coffi.Instruction;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Binary_c.class */
public class Binary_c extends Expr_c implements Binary {
    protected Expr left;
    protected Binary.Operator op;
    protected Expr right;
    protected Precedence precedence;

    public Binary_c(Position pos, Expr left, Binary.Operator op, Expr right) {
        super(pos);
        this.left = left;
        this.op = op;
        this.right = right;
        this.precedence = op.precedence();
    }

    @Override // polyglot.ast.Binary
    public Expr left() {
        return this.left;
    }

    @Override // polyglot.ast.Binary
    public Binary left(Expr left) {
        Binary_c n = (Binary_c) copy();
        n.left = left;
        return n;
    }

    @Override // polyglot.ast.Binary
    public Binary.Operator operator() {
        return this.op;
    }

    @Override // polyglot.ast.Binary
    public Binary operator(Binary.Operator op) {
        Binary_c n = (Binary_c) copy();
        n.op = op;
        return n;
    }

    @Override // polyglot.ast.Binary
    public Expr right() {
        return this.right;
    }

    @Override // polyglot.ast.Binary
    public Binary right(Expr right) {
        Binary_c n = (Binary_c) copy();
        n.right = right;
        return n;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Precedence precedence() {
        return this.precedence;
    }

    @Override // polyglot.ast.Binary
    public Binary precedence(Precedence precedence) {
        Binary_c n = (Binary_c) copy();
        n.precedence = precedence;
        return n;
    }

    protected Binary_c reconstruct(Expr left, Expr right) {
        if (left != this.left || right != this.right) {
            Binary_c n = (Binary_c) copy();
            n.left = left;
            n.right = right;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Expr left = (Expr) visitChild(this.left, v);
        Expr right = (Expr) visitChild(this.right, v);
        return reconstruct(left, right);
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public boolean isConstant() {
        return this.left.isConstant() && this.right.isConstant();
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Object constantValue() {
        Object lv = this.left.constantValue();
        Object rv = this.right.constantValue();
        if (!isConstant()) {
            return null;
        }
        if (this.op == Binary.ADD && ((lv instanceof String) || (rv instanceof String))) {
            if (lv == null) {
                lv = Jimple.NULL;
            }
            if (rv == null) {
                rv = Jimple.NULL;
            }
            return new StringBuffer().append(lv.toString()).append(rv.toString()).toString();
        } else if (this.op == Binary.EQ && (lv instanceof String) && (rv instanceof String)) {
            return Boolean.valueOf(((String) lv).intern() == ((String) rv).intern());
        } else if (this.op == Binary.NE && (lv instanceof String) && (rv instanceof String)) {
            return Boolean.valueOf(((String) lv).intern() != ((String) rv).intern());
        } else {
            if (lv instanceof Character) {
                lv = new Integer(((Character) lv).charValue());
            }
            if (rv instanceof Character) {
                rv = new Integer(((Character) rv).charValue());
            }
            try {
                if ((lv instanceof Number) && (rv instanceof Number)) {
                    if ((lv instanceof Double) || (rv instanceof Double)) {
                        double l = ((Number) lv).doubleValue();
                        double r = ((Number) rv).doubleValue();
                        if (this.op == Binary.ADD) {
                            return new Double(l + r);
                        }
                        if (this.op == Binary.SUB) {
                            return new Double(l - r);
                        }
                        if (this.op == Binary.MUL) {
                            return new Double(l * r);
                        }
                        if (this.op == Binary.DIV) {
                            return new Double(l / r);
                        }
                        if (this.op == Binary.MOD) {
                            return new Double(l % r);
                        }
                        if (this.op == Binary.EQ) {
                            return Boolean.valueOf(l == r);
                        } else if (this.op == Binary.NE) {
                            return Boolean.valueOf(l != r);
                        } else if (this.op == Binary.LT) {
                            return Boolean.valueOf(l < r);
                        } else if (this.op == Binary.LE) {
                            return Boolean.valueOf(l <= r);
                        } else if (this.op == Binary.GE) {
                            return Boolean.valueOf(l >= r);
                        } else if (this.op == Binary.GT) {
                            return Boolean.valueOf(l > r);
                        } else {
                            return null;
                        }
                    } else if ((lv instanceof Float) || (rv instanceof Float)) {
                        float l2 = ((Number) lv).floatValue();
                        float r2 = ((Number) rv).floatValue();
                        if (this.op == Binary.ADD) {
                            return new Float(l2 + r2);
                        }
                        if (this.op == Binary.SUB) {
                            return new Float(l2 - r2);
                        }
                        if (this.op == Binary.MUL) {
                            return new Float(l2 * r2);
                        }
                        if (this.op == Binary.DIV) {
                            return new Float(l2 / r2);
                        }
                        if (this.op == Binary.MOD) {
                            return new Float(l2 % r2);
                        }
                        if (this.op == Binary.EQ) {
                            return Boolean.valueOf(l2 == r2);
                        } else if (this.op == Binary.NE) {
                            return Boolean.valueOf(l2 != r2);
                        } else if (this.op == Binary.LT) {
                            return Boolean.valueOf(l2 < r2);
                        } else if (this.op == Binary.LE) {
                            return Boolean.valueOf(l2 <= r2);
                        } else if (this.op == Binary.GE) {
                            return Boolean.valueOf(l2 >= r2);
                        } else if (this.op == Binary.GT) {
                            return Boolean.valueOf(l2 > r2);
                        } else {
                            return null;
                        }
                    } else {
                        if ((lv instanceof Long) && (rv instanceof Number)) {
                            long l3 = ((Long) lv).longValue();
                            long r3 = ((Number) rv).longValue();
                            if (this.op == Binary.SHL) {
                                return new Long(l3 << ((int) r3));
                            }
                            if (this.op == Binary.SHR) {
                                return new Long(l3 >> ((int) r3));
                            }
                            if (this.op == Binary.USHR) {
                                return new Long(l3 >>> ((int) r3));
                            }
                        }
                        if ((lv instanceof Long) || (rv instanceof Long)) {
                            long l4 = ((Number) lv).longValue();
                            long r4 = ((Number) rv).longValue();
                            if (this.op == Binary.ADD) {
                                return new Long(l4 + r4);
                            }
                            if (this.op == Binary.SUB) {
                                return new Long(l4 - r4);
                            }
                            if (this.op == Binary.MUL) {
                                return new Long(l4 * r4);
                            }
                            if (this.op == Binary.DIV) {
                                return new Long(l4 / r4);
                            }
                            if (this.op == Binary.MOD) {
                                return new Long(l4 % r4);
                            }
                            if (this.op == Binary.EQ) {
                                return Boolean.valueOf(l4 == r4);
                            } else if (this.op == Binary.NE) {
                                return Boolean.valueOf(l4 != r4);
                            } else if (this.op == Binary.LT) {
                                return Boolean.valueOf(l4 < r4);
                            } else if (this.op == Binary.LE) {
                                return Boolean.valueOf(l4 <= r4);
                            } else if (this.op == Binary.GE) {
                                return Boolean.valueOf(l4 >= r4);
                            } else if (this.op == Binary.GT) {
                                return Boolean.valueOf(l4 > r4);
                            } else if (this.op == Binary.BIT_AND) {
                                return new Long(l4 & r4);
                            } else {
                                if (this.op == Binary.BIT_OR) {
                                    return new Long(l4 | r4);
                                }
                                if (this.op == Binary.BIT_XOR) {
                                    return new Long(l4 ^ r4);
                                }
                                return null;
                            }
                        }
                        int l5 = ((Number) lv).intValue();
                        int r5 = ((Number) rv).intValue();
                        if (this.op == Binary.ADD) {
                            return new Integer(l5 + r5);
                        }
                        if (this.op == Binary.SUB) {
                            return new Integer(l5 - r5);
                        }
                        if (this.op == Binary.MUL) {
                            return new Integer(l5 * r5);
                        }
                        if (this.op == Binary.DIV) {
                            return new Integer(l5 / r5);
                        }
                        if (this.op == Binary.MOD) {
                            return new Integer(l5 % r5);
                        }
                        if (this.op == Binary.EQ) {
                            return Boolean.valueOf(l5 == r5);
                        } else if (this.op == Binary.NE) {
                            return Boolean.valueOf(l5 != r5);
                        } else if (this.op == Binary.LT) {
                            return Boolean.valueOf(l5 < r5);
                        } else if (this.op == Binary.LE) {
                            return Boolean.valueOf(l5 <= r5);
                        } else if (this.op == Binary.GE) {
                            return Boolean.valueOf(l5 >= r5);
                        } else if (this.op == Binary.GT) {
                            return Boolean.valueOf(l5 > r5);
                        } else if (this.op == Binary.BIT_AND) {
                            return new Integer(l5 & r5);
                        } else {
                            if (this.op == Binary.BIT_OR) {
                                return new Integer(l5 | r5);
                            }
                            if (this.op == Binary.BIT_XOR) {
                                return new Integer(l5 ^ r5);
                            }
                            if (this.op == Binary.SHL) {
                                return new Integer(l5 << r5);
                            }
                            if (this.op == Binary.SHR) {
                                return new Integer(l5 >> r5);
                            }
                            if (this.op == Binary.USHR) {
                                return new Integer(l5 >>> r5);
                            }
                            return null;
                        }
                    }
                } else if ((lv instanceof Boolean) && (rv instanceof Boolean)) {
                    boolean l6 = ((Boolean) lv).booleanValue();
                    boolean r6 = ((Boolean) rv).booleanValue();
                    if (this.op == Binary.EQ) {
                        return Boolean.valueOf(l6 == r6);
                    } else if (this.op == Binary.NE) {
                        return Boolean.valueOf(l6 != r6);
                    } else if (this.op == Binary.BIT_AND) {
                        return Boolean.valueOf(l6 & r6);
                    } else {
                        if (this.op == Binary.BIT_OR) {
                            return Boolean.valueOf(l6 | r6);
                        }
                        if (this.op == Binary.BIT_XOR) {
                            return Boolean.valueOf(l6 ^ r6);
                        }
                        if (this.op == Binary.COND_AND) {
                            return Boolean.valueOf(l6 && r6);
                        } else if (this.op == Binary.COND_OR) {
                            return Boolean.valueOf(l6 || r6);
                        } else {
                            return null;
                        }
                    }
                } else {
                    return null;
                }
            } catch (ArithmeticException e) {
                return null;
            }
        }
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        Type l = this.left.type();
        Type r = this.right.type();
        TypeSystem ts = tc.typeSystem();
        if (this.op == Binary.GT || this.op == Binary.LT || this.op == Binary.GE || this.op == Binary.LE) {
            if (!l.isNumeric()) {
                throw new SemanticException(new StringBuffer().append("The ").append(this.op).append(" operator must have numeric operands.").toString(), this.left.position());
            }
            if (!r.isNumeric()) {
                throw new SemanticException(new StringBuffer().append("The ").append(this.op).append(" operator must have numeric operands.").toString(), this.right.position());
            }
            return type(ts.Boolean());
        } else if (this.op == Binary.EQ || this.op == Binary.NE) {
            if (!ts.isCastValid(l, r) && !ts.isCastValid(r, l)) {
                throw new SemanticException(new StringBuffer().append("The ").append(this.op).append(" operator must have operands of similar type.").toString(), position());
            }
            return type(ts.Boolean());
        } else if (this.op == Binary.COND_OR || this.op == Binary.COND_AND) {
            if (!l.isBoolean()) {
                throw new SemanticException(new StringBuffer().append("The ").append(this.op).append(" operator must have boolean operands.").toString(), this.left.position());
            }
            if (!r.isBoolean()) {
                throw new SemanticException(new StringBuffer().append("The ").append(this.op).append(" operator must have boolean operands.").toString(), this.right.position());
            }
            return type(ts.Boolean());
        } else if (this.op == Binary.ADD && (ts.equals(l, ts.String()) || ts.equals(r, ts.String()))) {
            if (!ts.canCoerceToString(r, tc.context())) {
                throw new SemanticException(new StringBuffer().append("Cannot coerce an expression of type ").append(r).append(" to a String.").toString(), this.right.position());
            }
            if (!ts.canCoerceToString(l, tc.context())) {
                throw new SemanticException(new StringBuffer().append("Cannot coerce an expression of type ").append(l).append(" to a String.").toString(), this.left.position());
            }
            return precedence(Precedence.STRING_ADD).type(ts.String());
        } else if ((this.op == Binary.BIT_AND || this.op == Binary.BIT_OR || this.op == Binary.BIT_XOR) && l.isBoolean() && r.isBoolean()) {
            return type(ts.Boolean());
        } else {
            if (this.op == Binary.ADD) {
                if (!l.isNumeric()) {
                    throw new SemanticException(new StringBuffer().append("The ").append(this.op).append(" operator must have numeric or String operands.").toString(), this.left.position());
                }
                if (!r.isNumeric()) {
                    throw new SemanticException(new StringBuffer().append("The ").append(this.op).append(" operator must have numeric or String operands.").toString(), this.right.position());
                }
            }
            if (this.op == Binary.BIT_AND || this.op == Binary.BIT_OR || this.op == Binary.BIT_XOR) {
                if (!ts.isImplicitCastValid(l, ts.Long())) {
                    throw new SemanticException(new StringBuffer().append("The ").append(this.op).append(" operator must have numeric or boolean operands.").toString(), this.left.position());
                }
                if (!ts.isImplicitCastValid(r, ts.Long())) {
                    throw new SemanticException(new StringBuffer().append("The ").append(this.op).append(" operator must have numeric or boolean operands.").toString(), this.right.position());
                }
            }
            if (this.op == Binary.SUB || this.op == Binary.MUL || this.op == Binary.DIV || this.op == Binary.MOD) {
                if (!l.isNumeric()) {
                    throw new SemanticException(new StringBuffer().append("The ").append(this.op).append(" operator must have numeric operands.").toString(), this.left.position());
                }
                if (!r.isNumeric()) {
                    throw new SemanticException(new StringBuffer().append("The ").append(this.op).append(" operator must have numeric operands.").toString(), this.right.position());
                }
            }
            if (this.op == Binary.SHL || this.op == Binary.SHR || this.op == Binary.USHR) {
                if (!ts.isImplicitCastValid(l, ts.Long())) {
                    throw new SemanticException(new StringBuffer().append("The ").append(this.op).append(" operator must have numeric operands.").toString(), this.left.position());
                }
                if (!ts.isImplicitCastValid(r, ts.Long())) {
                    throw new SemanticException(new StringBuffer().append("The ").append(this.op).append(" operator must have numeric operands.").toString(), this.right.position());
                }
            }
            if (this.op == Binary.SHL || this.op == Binary.SHR || this.op == Binary.USHR) {
                return type(ts.promote(l));
            }
            return type(ts.promote(l, r));
        }
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        Expr expr;
        if (child == this.left) {
            expr = this.right;
        } else if (child == this.right) {
            expr = this.left;
        } else {
            return child.type();
        }
        TypeSystem ts = av.typeSystem();
        try {
            if (this.op == Binary.EQ || this.op == Binary.NE) {
                if ((child.type().isReference() || child.type().isNull()) && (expr.type().isReference() || expr.type().isNull())) {
                    return ts.leastCommonAncestor(child.type(), expr.type());
                }
                if (child.type().isBoolean() && expr.type().isBoolean()) {
                    return ts.Boolean();
                }
                if (child.type().isNumeric() && expr.type().isNumeric()) {
                    return ts.promote(child.type(), expr.type());
                }
                if (child.type().isImplicitCastValid(expr.type())) {
                    return expr.type();
                }
                return child.type();
            } else if (this.op == Binary.ADD && ts.equals(this.type, ts.String())) {
                return ts.String();
            } else {
                if (this.op == Binary.GT || this.op == Binary.LT || this.op == Binary.GE || this.op == Binary.LE) {
                    if (child.type().isNumeric() && expr.type().isNumeric()) {
                        return ts.promote(child.type(), expr.type());
                    }
                    return child.type();
                } else if (this.op == Binary.COND_OR || this.op == Binary.COND_AND) {
                    return ts.Boolean();
                } else {
                    if (this.op == Binary.BIT_AND || this.op == Binary.BIT_OR || this.op == Binary.BIT_XOR) {
                        if (expr.type().isBoolean()) {
                            return ts.Boolean();
                        }
                        if (child.type().isNumeric() && expr.type().isNumeric()) {
                            return ts.promote(child.type(), expr.type());
                        }
                        return child.type();
                    } else if (this.op == Binary.ADD || this.op == Binary.SUB || this.op == Binary.MUL || this.op == Binary.DIV || this.op == Binary.MOD) {
                        if (child.type().isNumeric() && expr.type().isNumeric()) {
                            Type t = ts.promote(child.type(), expr.type());
                            if (ts.isImplicitCastValid(t, av.toType())) {
                                return t;
                            }
                            return av.toType();
                        }
                        return child.type();
                    } else if (this.op == Binary.SHL || this.op == Binary.SHR || this.op == Binary.USHR) {
                        if (child.type().isNumeric() && expr.type().isNumeric()) {
                            if (child == this.left) {
                                Type t2 = ts.promote(child.type());
                                if (ts.isImplicitCastValid(t2, av.toType())) {
                                    return t2;
                                }
                                return av.toType();
                            }
                            return ts.promote(child.type());
                        }
                        return child.type();
                    } else {
                        return child.type();
                    }
                }
            }
        } catch (SemanticException e) {
            return child.type();
        }
    }

    @Override // polyglot.ast.Binary
    public boolean throwsArithmeticException() {
        return this.op == Binary.DIV || this.op == Binary.MOD;
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.left).append(Instruction.argsep).append(this.op).append(Instruction.argsep).append(this.right).toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        printSubExpr(this.left, true, w, tr);
        w.write(Instruction.argsep);
        w.write(this.op.toString());
        w.allowBreak((type() == null || type().isPrimitive()) ? 2 : 0, Instruction.argsep);
        printSubExpr(this.right, false, w, tr);
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public void dump(CodeWriter w) {
        super.dump(w);
        if (this.type != null) {
            w.allowBreak(4, Instruction.argsep);
            w.begin(0);
            w.write(new StringBuffer().append("(type ").append(this.type).append(")").toString());
            w.end();
        }
        w.allowBreak(4, Instruction.argsep);
        w.begin(0);
        w.write(new StringBuffer().append("(operator ").append(this.op).append(")").toString());
        w.end();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.left.entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        if (this.op == Binary.COND_AND || this.op == Binary.COND_OR) {
            if (this.left instanceof BooleanLit) {
                BooleanLit b = (BooleanLit) this.left;
                if ((b.value() && this.op == Binary.COND_OR) || (!b.value() && this.op == Binary.COND_AND)) {
                    v.visitCFG(this.left, this);
                } else {
                    v.visitCFG(this.left, this.right.entry());
                    v.visitCFG(this.right, this);
                }
            } else {
                if (this.op == Binary.COND_AND) {
                    v.visitCFG(this.left, FlowGraph.EDGE_KEY_TRUE, this.right.entry(), FlowGraph.EDGE_KEY_FALSE, this);
                } else {
                    v.visitCFG(this.left, FlowGraph.EDGE_KEY_FALSE, this.right.entry(), FlowGraph.EDGE_KEY_TRUE, this);
                }
                v.visitCFG(this.right, FlowGraph.EDGE_KEY_TRUE, this, FlowGraph.EDGE_KEY_FALSE, this);
            }
        } else if (this.left.type().isBoolean() && this.right.type().isBoolean()) {
            v.visitCFG(this.left, FlowGraph.EDGE_KEY_TRUE, this.right.entry(), FlowGraph.EDGE_KEY_FALSE, this.right.entry());
            v.visitCFG(this.right, FlowGraph.EDGE_KEY_TRUE, this, FlowGraph.EDGE_KEY_FALSE, this);
        } else {
            v.visitCFG(this.left, this.right.entry());
            v.visitCFG(this.right, this);
        }
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public List throwTypes(TypeSystem ts) {
        if (throwsArithmeticException()) {
            return Collections.singletonList(ts.ArithmeticException());
        }
        return Collections.EMPTY_LIST;
    }
}
