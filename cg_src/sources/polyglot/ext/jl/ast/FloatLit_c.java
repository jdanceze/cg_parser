package polyglot.ext.jl.ast;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import polyglot.ast.FloatLit;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/FloatLit_c.class */
public class FloatLit_c extends Lit_c implements FloatLit {
    protected FloatLit.Kind kind;
    protected double value;

    public FloatLit_c(Position pos, FloatLit.Kind kind, double value) {
        super(pos);
        this.kind = kind;
        this.value = value;
    }

    @Override // polyglot.ast.FloatLit
    public FloatLit.Kind kind() {
        return this.kind;
    }

    @Override // polyglot.ast.FloatLit
    public FloatLit kind(FloatLit.Kind kind) {
        FloatLit_c n = (FloatLit_c) copy();
        n.kind = kind;
        return n;
    }

    @Override // polyglot.ast.FloatLit
    public double value() {
        return this.value;
    }

    @Override // polyglot.ast.FloatLit
    public FloatLit value(double value) {
        FloatLit_c n = (FloatLit_c) copy();
        n.value = value;
        return n;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        if (this.kind == FloatLit.FLOAT) {
            return type(tc.typeSystem().Float());
        }
        if (this.kind == FloatLit.DOUBLE) {
            return type(tc.typeSystem().Double());
        }
        throw new InternalCompilerError(new StringBuffer().append("Unrecognized FloatLit kind ").append(this.kind).toString());
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return Double.toString(this.value);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        if (this.kind == FloatLit.FLOAT) {
            w.write(new StringBuffer().append(Float.toString((float) this.value)).append("F").toString());
        } else if (this.kind == FloatLit.DOUBLE) {
            w.write(Double.toString(this.value));
        } else {
            throw new InternalCompilerError(new StringBuffer().append("Unrecognized FloatLit kind ").append(this.kind).toString());
        }
    }

    @Override // polyglot.ext.jl.ast.Lit_c, polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Object constantValue() {
        if (this.kind == FloatLit.FLOAT) {
            return new Float(this.value);
        }
        return new Double(this.value);
    }

    @Override // polyglot.ext.jl.ast.Lit_c, polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Precedence precedence() {
        if (this.value < Const.default_value_double) {
            return Precedence.UNARY;
        }
        return Precedence.LITERAL;
    }
}
