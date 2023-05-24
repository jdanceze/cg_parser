package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Block;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.PrettyPrinter;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Block_c.class */
public class Block_c extends AbstractBlock_c implements Block {
    public Block_c(Position pos, List statements) {
        super(pos, statements);
    }

    @Override // polyglot.ext.jl.ast.AbstractBlock_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write("{");
        w.allowBreak(4, Instruction.argsep);
        super.prettyPrint(w, tr);
        w.allowBreak(0, Instruction.argsep);
        w.write("}");
    }
}
