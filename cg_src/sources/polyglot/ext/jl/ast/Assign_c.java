package polyglot.ext.jl.ast;

import java.util.LinkedList;
import java.util.List;
import polyglot.ast.Assign;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Term;
import polyglot.ast.Variable;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Assign_c.class */
public abstract class Assign_c extends Expr_c implements Assign {
    protected Expr left;
    protected Assign.Operator op;
    protected Expr right;

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public abstract Term entry();

    protected abstract void acceptCFGAssign(CFGBuilder cFGBuilder);

    protected abstract void acceptCFGOpAssign(CFGBuilder cFGBuilder);

    public Assign_c(Position pos, Expr left, Assign.Operator op, Expr right) {
        super(pos);
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Precedence precedence() {
        return Precedence.ASSIGN;
    }

    @Override // polyglot.ast.Assign
    public Expr left() {
        return this.left;
    }

    public Assign left(Expr left) {
        Assign_c n = (Assign_c) copy();
        n.left = left;
        return n;
    }

    @Override // polyglot.ast.Assign
    public Assign.Operator operator() {
        return this.op;
    }

    @Override // polyglot.ast.Assign
    public Assign operator(Assign.Operator op) {
        Assign_c n = (Assign_c) copy();
        n.op = op;
        return n;
    }

    @Override // polyglot.ast.Assign
    public Expr right() {
        return this.right;
    }

    @Override // polyglot.ast.Assign
    public Assign right(Expr right) {
        Assign_c n = (Assign_c) copy();
        n.right = right;
        return n;
    }

    protected Assign_c reconstruct(Expr left, Expr right) {
        if (left != this.left || right != this.right) {
            Assign_c n = (Assign_c) copy();
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

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        Type t = this.left.type();
        Type s = this.right.type();
        TypeSystem ts = tc.typeSystem();
        if (!(this.left instanceof Variable)) {
            throw new SemanticException("Target of assignment must be a variable.", position());
        }
        if (this.op == Assign.ASSIGN) {
            if (!ts.isImplicitCastValid(s, t) && !ts.equals(s, t) && !ts.numericConversionValid(t, this.right.constantValue())) {
                throw new SemanticException(new StringBuffer().append("Cannot assign ").append(s).append(" to ").append(t).append(".").toString(), position());
            }
            return type(t);
        } else if (this.op == Assign.ADD_ASSIGN) {
            if (ts.equals(t, ts.String()) && ts.canCoerceToString(s, tc.context())) {
                return type(ts.String());
            }
            if (t.isNumeric() && s.isNumeric()) {
                return type(ts.promote(t, s));
            }
            throw new SemanticException(new StringBuffer().append("The ").append(this.op).append(" operator must have ").append("numeric or String operands.").toString(), position());
        } else if (this.op == Assign.SUB_ASSIGN || this.op == Assign.MUL_ASSIGN || this.op == Assign.DIV_ASSIGN || this.op == Assign.MOD_ASSIGN) {
            if (t.isNumeric() && s.isNumeric()) {
                return type(ts.promote(t, s));
            }
            throw new SemanticException(new StringBuffer().append("The ").append(this.op).append(" operator must have ").append("numeric operands.").toString(), position());
        } else if (this.op == Assign.BIT_AND_ASSIGN || this.op == Assign.BIT_OR_ASSIGN || this.op == Assign.BIT_XOR_ASSIGN) {
            if (t.isBoolean() && s.isBoolean()) {
                return type(ts.Boolean());
            }
            if (ts.isImplicitCastValid(t, ts.Long()) && ts.isImplicitCastValid(s, ts.Long())) {
                return type(ts.promote(t, s));
            }
            throw new SemanticException(new StringBuffer().append("The ").append(this.op).append(" operator must have ").append("integral or boolean operands.").toString(), position());
        } else if (this.op == Assign.SHL_ASSIGN || this.op == Assign.SHR_ASSIGN || this.op == Assign.USHR_ASSIGN) {
            if (ts.isImplicitCastValid(t, ts.Long()) && ts.isImplicitCastValid(s, ts.Long())) {
                return type(ts.promote(t));
            }
            throw new SemanticException(new StringBuffer().append("The ").append(this.op).append(" operator must have ").append("integral operands.").toString(), position());
        } else {
            throw new InternalCompilerError(new StringBuffer().append("Unrecognized assignment operator ").append(this.op).append(".").toString());
        }
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        if (child == this.right) {
            TypeSystem ts = av.typeSystem();
            if (ts.numericConversionValid(this.left.type(), child.constantValue())) {
                return child.type();
            }
            return this.left.type();
        }
        return child.type();
    }

    @Override // polyglot.ast.Assign
    public boolean throwsArithmeticException() {
        return this.op == Assign.DIV_ASSIGN || this.op == Assign.MOD_ASSIGN;
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
        w.allowBreak(2, Instruction.argsep);
        printSubExpr(this.right, false, w, tr);
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public void dump(CodeWriter w) {
        super.dump(w);
        w.allowBreak(4, Instruction.argsep);
        w.begin(0);
        w.write(new StringBuffer().append("(operator ").append(this.op).append(")").toString());
        w.end();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        if (operator() == Assign.ASSIGN) {
            acceptCFGAssign(v);
        } else {
            acceptCFGOpAssign(v);
        }
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public List throwTypes(TypeSystem ts) {
        List l = new LinkedList();
        if (throwsArithmeticException()) {
            l.add(ts.ArithmeticException());
        }
        return l;
    }
}
