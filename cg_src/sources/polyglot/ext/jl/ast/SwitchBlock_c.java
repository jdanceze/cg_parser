package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.SwitchBlock;
import polyglot.types.Context;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/SwitchBlock_c.class */
public class SwitchBlock_c extends AbstractBlock_c implements SwitchBlock {
    public SwitchBlock_c(Position pos, List statements) {
        super(pos, statements);
    }

    @Override // polyglot.ext.jl.ast.AbstractBlock_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Context enterScope(Context c) {
        return c;
    }
}
