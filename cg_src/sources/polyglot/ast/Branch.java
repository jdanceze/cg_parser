package polyglot.ast;

import polyglot.util.Enum;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Branch.class */
public interface Branch extends Stmt {
    public static final Kind BREAK = new Kind(Jimple.BREAK);
    public static final Kind CONTINUE = new Kind("continue");

    Kind kind();

    Branch kind(Kind kind);

    String label();

    Branch label(String str);

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Branch$Kind.class */
    public static class Kind extends Enum {
        public Kind(String name) {
            super(name);
        }
    }
}
