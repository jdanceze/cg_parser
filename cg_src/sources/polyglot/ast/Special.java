package polyglot.ast;

import polyglot.util.Enum;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Special.class */
public interface Special extends Expr {
    public static final Kind SUPER = new Kind("super");
    public static final Kind THIS = new Kind("this");

    Kind kind();

    Special kind(Kind kind);

    TypeNode qualifier();

    Special qualifier(TypeNode typeNode);

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Special$Kind.class */
    public static class Kind extends Enum {
        public Kind(String name) {
            super(name);
        }
    }
}
