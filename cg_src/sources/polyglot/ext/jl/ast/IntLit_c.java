package polyglot.ext.jl.ast;

import polyglot.ast.IntLit;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/IntLit_c.class */
public class IntLit_c extends NumLit_c implements IntLit {
    protected IntLit.Kind kind;

    public IntLit_c(Position pos, IntLit.Kind kind, long value) {
        super(pos, value);
        this.kind = kind;
    }

    @Override // polyglot.ast.IntLit
    public boolean boundary() {
        return (this.kind == IntLit.INT && ((int) this.value) == Integer.MIN_VALUE) || (this.kind == IntLit.LONG && this.value == Long.MIN_VALUE);
    }

    @Override // polyglot.ast.IntLit
    public long value() {
        return longValue();
    }

    @Override // polyglot.ast.IntLit
    public IntLit value(long value) {
        IntLit_c n = (IntLit_c) copy();
        n.value = value;
        return n;
    }

    @Override // polyglot.ast.IntLit
    public IntLit.Kind kind() {
        return this.kind;
    }

    @Override // polyglot.ast.IntLit
    public IntLit kind(IntLit.Kind kind) {
        IntLit_c n = (IntLit_c) copy();
        n.kind = kind;
        return n;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        IntLit.Kind kind = kind();
        if (kind == IntLit.INT) {
            return type(ts.Int());
        }
        if (kind == IntLit.LONG) {
            return type(ts.Long());
        }
        throw new InternalCompilerError(new StringBuffer().append("Unrecognized IntLit kind ").append(kind).toString());
    }

    @Override // polyglot.ast.IntLit
    public String positiveToString() {
        if (kind() == IntLit.LONG) {
            if (boundary()) {
                return "9223372036854775808L";
            }
            return new StringBuffer().append(Long.toString(this.value)).append("L").toString();
        } else if (boundary()) {
            return "2147483648";
        } else {
            return Long.toString(this.value);
        }
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        if (kind() == IntLit.LONG) {
            return new StringBuffer().append(Long.toString(this.value)).append("L").toString();
        }
        return Long.toString(this.value);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write(toString());
    }

    @Override // polyglot.ext.jl.ast.Lit_c, polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Object constantValue() {
        if (kind() == IntLit.LONG) {
            return new Long(this.value);
        }
        return new Integer((int) this.value);
    }

    @Override // polyglot.ext.jl.ast.Lit_c, polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Precedence precedence() {
        if (this.value < 0 && !boundary()) {
            return Precedence.UNARY;
        }
        return Precedence.LITERAL;
    }
}
