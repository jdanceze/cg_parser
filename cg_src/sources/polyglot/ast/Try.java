package polyglot.ast;

import java.util.List;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Try.class */
public interface Try extends CompoundStmt {
    Block tryBlock();

    Try tryBlock(Block block);

    List catchBlocks();

    Try catchBlocks(List list);

    Block finallyBlock();

    Try finallyBlock(Block block);
}
