package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.types.SemanticException;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.SubtypeSet;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ExceptionChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Term_c.class */
public abstract class Term_c extends Node_c implements Term {
    protected boolean reachable;
    protected SubtypeSet exceptions;

    public abstract Term entry();

    public abstract List acceptCFG(CFGBuilder cFGBuilder, List list);

    public Term_c(Position pos) {
        super(pos);
    }

    @Override // polyglot.ast.Term
    public boolean reachable() {
        return this.reachable;
    }

    @Override // polyglot.ast.Term
    public Term reachable(boolean reachability) {
        if (this.reachable == reachability) {
            return this;
        }
        Term_c t = (Term_c) copy();
        t.reachable = reachability;
        return t;
    }

    public static Term listEntry(List l, Term alt) {
        Term c = (Term) CollectionUtil.firstOrElse(l, alt);
        return c != alt ? c.entry() : alt;
    }

    @Override // polyglot.ast.Term
    public SubtypeSet exceptions() {
        return this.exceptions;
    }

    @Override // polyglot.ast.Term
    public Term exceptions(SubtypeSet exceptions) {
        Term_c n = (Term_c) copy();
        n.exceptions = new SubtypeSet(exceptions);
        return n;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
        Term t = (Term) super.exceptionCheck(ec);
        return t.exceptions(ec.throwsSet());
    }
}
