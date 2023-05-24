package java_cup;

import java.util.Enumeration;
import java.util.Hashtable;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/terminal.class */
public class terminal extends symbol {
    private int _precedence_num;
    private int _precedence_side;
    protected static Hashtable _all = new Hashtable();
    protected static Hashtable _all_by_index = new Hashtable();
    protected static int next_index = 0;
    public static terminal EOF = new terminal("EOF");
    public static terminal error = new terminal("error");

    public terminal(String nm, String tp, int precedence_side, int precedence_num) {
        super(nm, tp);
        Object conflict = _all.put(nm, this);
        if (conflict != null) {
            new internal_error("Duplicate terminal (" + nm + ") created").crash();
        }
        int i = next_index;
        next_index = i + 1;
        this._index = i;
        this._precedence_num = precedence_num;
        this._precedence_side = precedence_side;
        _all_by_index.put(new Integer(this._index), this);
    }

    public terminal(String nm, String tp) {
        this(nm, tp, -1, -1);
    }

    public terminal(String nm) {
        this(nm, null);
    }

    public static void clear() {
        _all.clear();
        _all_by_index.clear();
        next_index = 0;
        EOF = new terminal("EOF");
        error = new terminal("error");
    }

    public static Enumeration all() {
        return _all.elements();
    }

    public static terminal find(String with_name) {
        if (with_name == null) {
            return null;
        }
        return (terminal) _all.get(with_name);
    }

    public static terminal find(int indx) {
        Integer the_indx = new Integer(indx);
        return (terminal) _all_by_index.get(the_indx);
    }

    public static int number() {
        return _all.size();
    }

    @Override // java_cup.symbol
    public boolean is_non_term() {
        return false;
    }

    @Override // java_cup.symbol
    public String toString() {
        return String.valueOf(super.toString()) + "[" + index() + "]";
    }

    public int precedence_num() {
        return this._precedence_num;
    }

    public int precedence_side() {
        return this._precedence_side;
    }

    public void set_precedence(int p, int new_prec) {
        this._precedence_side = p;
        this._precedence_num = new_prec;
    }
}
