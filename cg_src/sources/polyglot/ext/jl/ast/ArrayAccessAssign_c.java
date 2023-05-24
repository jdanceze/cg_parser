package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.List;
import polyglot.ast.ArrayAccess;
import polyglot.ast.ArrayAccessAssign;
import polyglot.ast.Assign;
import polyglot.ast.Expr;
import polyglot.ast.Term;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/ArrayAccessAssign_c.class */
public class ArrayAccessAssign_c extends Assign_c implements ArrayAccessAssign {
    public ArrayAccessAssign_c(Position pos, ArrayAccess left, Assign.Operator op, Expr right) {
        super(pos, left, op, right);
    }

    @Override // polyglot.ext.jl.ast.Assign_c, polyglot.ast.Assign
    public Assign left(Expr left) {
        ArrayAccessAssign_c n = (ArrayAccessAssign_c) super.left(left);
        n.assertLeftType();
        return n;
    }

    private void assertLeftType() {
        if (!(left() instanceof ArrayAccess)) {
            throw new InternalCompilerError("left expression of an ArrayAccessAssign must be an array access");
        }
    }

    @Override // polyglot.ext.jl.ast.Assign_c, polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return left().entry();
    }

    @Override // polyglot.ext.jl.ast.Assign_c
    protected void acceptCFGAssign(CFGBuilder v) {
        ArrayAccess a = (ArrayAccess) left();
        v.visitCFG(a.array(), a.index().entry());
        v.visitCFG(a.index(), right().entry());
        v.visitCFG(right(), this);
    }

    @Override // polyglot.ext.jl.ast.Assign_c
    protected void acceptCFGOpAssign(CFGBuilder v) {
        ArrayAccess a = (ArrayAccess) left();
        v.visitCFG(a.array(), a.index().entry());
        v.visitCFG(a.index(), a);
        v.visitThrow(a);
        v.edge(a, right().entry());
        v.visitCFG(right(), this);
    }

    @Override // polyglot.ext.jl.ast.Assign_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public List throwTypes(TypeSystem ts) {
        List l = new ArrayList(super.throwTypes(ts));
        if (throwsArrayStoreException()) {
            l.add(ts.ArrayStoreException());
        }
        l.add(ts.NullPointerException());
        l.add(ts.OutOfBoundsException());
        return l;
    }

    @Override // polyglot.ast.ArrayAccessAssign
    public boolean throwsArrayStoreException() {
        return this.op == Assign.ASSIGN && this.left.type().isReference();
    }
}
