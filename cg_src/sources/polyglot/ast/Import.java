package polyglot.ast;

import polyglot.util.Enum;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Import.class */
public interface Import extends Node {
    public static final Kind CLASS = new Kind("class");
    public static final Kind PACKAGE = new Kind("package");

    String name();

    Import name(String str);

    Kind kind();

    Import kind(Kind kind);

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Import$Kind.class */
    public static class Kind extends Enum {
        public Kind(String name) {
            super(name);
        }
    }
}
