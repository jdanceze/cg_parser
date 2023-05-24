package polyglot.ast;

import polyglot.util.CodeWriter;
import polyglot.util.Copy;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Ext.class */
public interface Ext extends Copy {
    Node node();

    void init(Node node);

    Ext ext();

    Ext ext(Ext ext);

    void dump(CodeWriter codeWriter);
}
