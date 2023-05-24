package scm;

import java.util.Hashtable;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/Symbol.class */
class Symbol implements Obj {
    static Hashtable internset = new Hashtable();
    String name;

    Symbol(String s) {
        this.name = s;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Symbol intern(String s) {
        Symbol symbol = (Symbol) internset.get(s);
        Symbol ret = symbol;
        if (symbol == null) {
            ret = new Symbol(s);
            internset.put(s, ret);
        }
        return ret;
    }

    @Override // scm.Obj
    public Obj eval(Env e) {
        return e.lookup(this);
    }

    public String toString() {
        return this.name;
    }
}
