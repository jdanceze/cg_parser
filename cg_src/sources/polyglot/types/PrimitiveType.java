package polyglot.types;

import polyglot.util.Enum;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/PrimitiveType.class */
public interface PrimitiveType extends Type, Named {
    public static final Kind VOID = new Kind(Jimple.VOID);
    public static final Kind BOOLEAN = new Kind("boolean");
    public static final Kind BYTE = new Kind("byte");
    public static final Kind CHAR = new Kind("char");
    public static final Kind SHORT = new Kind("short");
    public static final Kind INT = new Kind("int");
    public static final Kind LONG = new Kind("long");
    public static final Kind FLOAT = new Kind(Jimple.FLOAT);
    public static final Kind DOUBLE = new Kind("double");

    Kind kind();

    String wrapperTypeString(TypeSystem typeSystem);

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/PrimitiveType$Kind.class */
    public static class Kind extends Enum {
        public Kind(String name) {
            super(name);
        }
    }
}
