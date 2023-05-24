package polyglot.ast;

import polyglot.util.Enum;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/IntLit.class */
public interface IntLit extends NumLit {
    public static final Kind INT = new Kind("int");
    public static final Kind LONG = new Kind("long");

    long value();

    IntLit value(long j);

    Kind kind();

    IntLit kind(Kind kind);

    boolean boundary();

    String positiveToString();

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/IntLit$Kind.class */
    public static class Kind extends Enum {
        public Kind(String name) {
            super(name);
        }
    }
}
