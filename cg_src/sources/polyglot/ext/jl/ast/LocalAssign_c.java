package polyglot.ext.jl.ast;

import polyglot.ast.Assign;
import polyglot.ast.Expr;
import polyglot.ast.Local;
import polyglot.ast.LocalAssign;
import polyglot.ast.Term;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/LocalAssign_c.class */
public class LocalAssign_c extends Assign_c implements LocalAssign {
    public LocalAssign_c(Position pos, Local left, Assign.Operator op, Expr right) {
        super(pos, left, op, right);
    }

    @Override // polyglot.ext.jl.ast.Assign_c, polyglot.ast.Assign
    public Assign left(Expr left) {
        LocalAssign_c n = (LocalAssign_c) super.left(left);
        n.assertLeftType();
        return n;
    }

    private void assertLeftType() {
        if (!(left() instanceof Local)) {
            throw new InternalCompilerError("left expression of an LocalAssign must be a local");
        }
    }

    @Override // polyglot.ext.jl.ast.Assign_c, polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        if (operator() != Assign.ASSIGN) {
            return left();
        }
        return right().entry();
    }

    @Override // polyglot.ext.jl.ast.Assign_c
    protected void acceptCFGAssign(CFGBuilder v) {
        Local local = (Local) left();
        v.visitCFG(right(), this);
    }

    @Override // polyglot.ext.jl.ast.Assign_c
    protected void acceptCFGOpAssign(CFGBuilder v) {
        Local l = (Local) left();
        v.visitThrow(l);
        v.edge(l, right().entry());
        v.visitCFG(right(), this);
    }
}
