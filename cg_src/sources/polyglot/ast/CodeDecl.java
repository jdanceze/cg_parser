package polyglot.ast;

import polyglot.types.CodeInstance;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/CodeDecl.class */
public interface CodeDecl extends ClassMember {
    Block body();

    CodeDecl body(Block block);

    CodeInstance codeInstance();
}
