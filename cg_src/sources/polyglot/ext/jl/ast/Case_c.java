package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Case;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.Local;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.types.FieldInstance;
import polyglot.types.LocalInstance;
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
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Case_c.class */
public class Case_c extends Stmt_c implements Case {
    protected Expr expr;
    protected long value;

    public Case_c(Position pos, Expr expr) {
        super(pos);
        this.expr = expr;
    }

    @Override // polyglot.ast.Case
    public boolean isDefault() {
        return this.expr == null;
    }

    @Override // polyglot.ast.Case
    public Expr expr() {
        return this.expr;
    }

    @Override // polyglot.ast.Case
    public Case expr(Expr expr) {
        Case_c n = (Case_c) copy();
        n.expr = expr;
        return n;
    }

    @Override // polyglot.ast.Case
    public long value() {
        return this.value;
    }

    @Override // polyglot.ast.Case
    public Case value(long value) {
        Case_c n = (Case_c) copy();
        n.value = value;
        return n;
    }

    protected Case_c reconstruct(Expr expr) {
        if (expr != this.expr) {
            Case_c n = (Case_c) copy();
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
        Object o;
        if (this.expr == null) {
            return this;
        }
        TypeSystem ts = tc.typeSystem();
        if (!ts.isImplicitCastValid(this.expr.type(), ts.Int())) {
            throw new SemanticException("Case label must be an byte, char, short, or int.", position());
        }
        if (this.expr instanceof Field) {
            FieldInstance fi = ((Field) this.expr).fieldInstance();
            if (fi == null) {
                throw new InternalCompilerError("Undefined FieldInstance after type-checking.");
            }
            if (!fi.isConstant()) {
                throw new SemanticException("Case label must be an integral constant.", position());
            }
            o = fi.constantValue();
        } else if (this.expr instanceof Local) {
            LocalInstance li = ((Local) this.expr).localInstance();
            if (li == null) {
                throw new InternalCompilerError("Undefined LocalInstance after type-checking.");
            }
            if (!li.isConstant()) {
                return this;
            }
            o = li.constantValue();
        } else {
            o = this.expr.constantValue();
        }
        if ((o instanceof Number) && !(o instanceof Long) && !(o instanceof Float) && !(o instanceof Double)) {
            return value(((Number) o).longValue());
        }
        if (o instanceof Character) {
            return value(((Character) o).charValue());
        }
        throw new SemanticException("Case label must be an integral constant.", position());
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        TypeSystem ts = av.typeSystem();
        if (child == this.expr) {
            return ts.Int();
        }
        return child.type();
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        if (this.expr == null) {
            return "default:";
        }
        return new StringBuffer().append("case ").append(this.expr).append(":").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        if (this.expr == null) {
            w.write("default:");
            return;
        }
        w.write("case ");
        print(this.expr, w, tr);
        w.write(":");
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.expr != null ? this.expr : this;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        if (this.expr != null) {
            v.visitCFG(this.expr, this);
        }
        return succs;
    }
}
