package polyglot.ext.jl.ast;

import polyglot.ast.Loop;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Loop_c.class */
public abstract class Loop_c extends Stmt_c implements Loop {
    public Loop_c(Position pos) {
        super(pos);
    }

    @Override // polyglot.ast.Loop
    public boolean condIsConstant() {
        return cond().isConstant();
    }

    @Override // polyglot.ast.Loop
    public boolean condIsConstantTrue() {
        return Boolean.TRUE.equals(cond().constantValue());
    }
}
