package polyglot.ext.jl.ast;

import polyglot.ast.AmbAssign;
import polyglot.ast.ArrayAccess;
import polyglot.ast.Assign;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.Local;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.types.SemanticException;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.CFGBuilder;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/AmbAssign_c.class */
public class AmbAssign_c extends Assign_c implements AmbAssign {
    public AmbAssign_c(Position pos, Expr left, Assign.Operator op, Expr right) {
        super(pos, left, op, right);
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
        v.visitCFG(right(), this);
    }

    @Override // polyglot.ext.jl.ast.Assign_c
    protected void acceptCFGOpAssign(CFGBuilder v) {
        v.edge(left(), right().entry());
        v.visitCFG(right(), this);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
        Assign n = (Assign) super.disambiguate(ar);
        if (n.left() instanceof Local) {
            return ar.nodeFactory().LocalAssign(n.position(), (Local) left(), operator(), right());
        }
        if (n.left() instanceof Field) {
            return ar.nodeFactory().FieldAssign(n.position(), (Field) left(), operator(), right());
        }
        if (n.left() instanceof ArrayAccess) {
            return ar.nodeFactory().ArrayAccessAssign(n.position(), (ArrayAccess) left(), operator(), right());
        }
        throw new SemanticException("Could not disambiguate left side of assignment!", n.position());
    }
}
