package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.List;
import polyglot.ast.Assign;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.FieldAssign;
import polyglot.ast.Term;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/FieldAssign_c.class */
public class FieldAssign_c extends Assign_c implements FieldAssign {
    public FieldAssign_c(Position pos, Field left, Assign.Operator op, Expr right) {
        super(pos, left, op, right);
    }

    @Override // polyglot.ext.jl.ast.Assign_c, polyglot.ast.Assign
    public Assign left(Expr left) {
        FieldAssign_c n = (FieldAssign_c) super.left(left);
        n.assertLeftType();
        return n;
    }

    private void assertLeftType() {
        if (!(left() instanceof Field)) {
            throw new InternalCompilerError("left expression of an FieldAssign must be a field");
        }
    }

    @Override // polyglot.ext.jl.ast.Assign_c, polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        Field f = (Field) left();
        if (f.target() instanceof Expr) {
            return ((Expr) f.target()).entry();
        }
        if (operator() != Assign.ASSIGN) {
            return f;
        }
        return right().entry();
    }

    @Override // polyglot.ext.jl.ast.Assign_c
    protected void acceptCFGAssign(CFGBuilder v) {
        Field f = (Field) left();
        if (f.target() instanceof Expr) {
            Expr o = (Expr) f.target();
            v.visitCFG(o, right().entry());
            v.visitCFG(right(), this);
            return;
        }
        v.visitCFG(right(), this);
    }

    @Override // polyglot.ext.jl.ast.Assign_c
    protected void acceptCFGOpAssign(CFGBuilder v) {
        Field f = (Field) left();
        if (f.target() instanceof Expr) {
            Expr o = (Expr) f.target();
            v.visitCFG(o, f);
            v.visitThrow(f);
            v.edge(f, right().entry());
            v.visitCFG(right(), this);
            return;
        }
        v.visitThrow(f);
        v.edge(f, right().entry());
        v.visitCFG(right(), this);
    }

    @Override // polyglot.ext.jl.ast.Assign_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public List throwTypes(TypeSystem ts) {
        List l = new ArrayList(super.throwTypes(ts));
        Field f = (Field) left();
        if (f.target() instanceof Expr) {
            l.add(ts.NullPointerException());
        }
        return l;
    }
}
