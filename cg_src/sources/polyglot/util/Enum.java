package polyglot.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/Enum.class */
public class Enum implements Serializable {
    private String name;
    private static Map cache = new HashMap();

    /* JADX INFO: Access modifiers changed from: protected */
    public Enum(String name) {
        this.name = name;
        if (intern() != this) {
            throw new InternalCompilerError(new StringBuffer().append("Duplicate enum \"").append(name).append("\"").toString());
        }
    }

    private Enum() {
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public boolean equals(Object o) {
        return this == o;
    }

    public String toString() {
        return this.name;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/Enum$EnumKey.class */
    public static class EnumKey {
        Enum e;

        EnumKey(Enum e) {
            this.e = e;
        }

        public boolean equals(Object o) {
            return (o instanceof EnumKey) && this.e.name.equals(((EnumKey) o).e.name) && this.e.getClass() == ((EnumKey) o).e.getClass();
        }

        public int hashCode() {
            return this.e.getClass().hashCode() ^ this.e.name.hashCode();
        }

        public String toString() {
            return this.e.toString();
        }
    }

    public Enum intern() {
        EnumKey k = new EnumKey(this);
        Enum e = (Enum) cache.get(k);
        if (e == null) {
            cache.put(k, this);
            return this;
        }
        return e;
    }
}
