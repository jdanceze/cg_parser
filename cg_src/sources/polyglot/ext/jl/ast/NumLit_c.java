package polyglot.ext.jl.ast;

import polyglot.ast.NumLit;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/NumLit_c.class */
public abstract class NumLit_c extends Lit_c implements NumLit {
    protected long value;

    public NumLit_c(Position pos, long value) {
        super(pos);
        this.value = value;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.NumLit
    public long longValue() {
        return this.value;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public void dump(CodeWriter w) {
        super.dump(w);
        w.allowBreak(4, Instruction.argsep);
        w.begin(0);
        w.write(new StringBuffer().append("(value ").append(this.value).append(")").toString());
        w.end();
    }
}
