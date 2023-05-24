package javassist.compiler;

import java.util.HashMap;
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/compiler/KeywordTable.class */
public final class KeywordTable extends HashMap<String, Integer> {
    private static final long serialVersionUID = 1;

    public int lookup(String name) {
        if (containsKey(name)) {
            return get(name).intValue();
        }
        return -1;
    }

    public void append(String name, int t) {
        put(name, Integer.valueOf(t));
    }
}
