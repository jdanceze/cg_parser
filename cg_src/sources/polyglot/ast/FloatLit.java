package polyglot.ast;

import polyglot.util.Enum;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/FloatLit.class */
public interface FloatLit extends Lit {
    public static final Kind FLOAT = new Kind(Jimple.FLOAT);
    public static final Kind DOUBLE = new Kind("double");

    Kind kind();

    FloatLit kind(Kind kind);

    double value();

    FloatLit value(double d);

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/FloatLit$Kind.class */
    public static class Kind extends Enum {
        public Kind(String name) {
            super(name);
        }
    }
}
