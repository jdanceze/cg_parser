package java_cup;

import java.util.Enumeration;
import java.util.Hashtable;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/symbol_set.class */
public class symbol_set {
    protected Hashtable _all;

    public symbol_set() {
        this._all = new Hashtable(11);
    }

    public symbol_set(symbol_set other) throws internal_error {
        this._all = new Hashtable(11);
        not_null(other);
        this._all = (Hashtable) other._all.clone();
    }

    public Enumeration all() {
        return this._all.elements();
    }

    public int size() {
        return this._all.size();
    }

    protected void not_null(Object obj) throws internal_error {
        if (obj == null) {
            throw new internal_error("Null object used in set operation");
        }
    }

    public boolean contains(symbol sym) {
        return this._all.containsKey(sym.name());
    }

    public boolean is_subset_of(symbol_set other) throws internal_error {
        not_null(other);
        Enumeration e = all();
        while (e.hasMoreElements()) {
            if (!other.contains((symbol) e.nextElement())) {
                return false;
            }
        }
        return true;
    }

    public boolean is_superset_of(symbol_set other) throws internal_error {
        not_null(other);
        return other.is_subset_of(this);
    }

    public boolean add(symbol sym) throws internal_error {
        not_null(sym);
        Object previous = this._all.put(sym.name(), sym);
        return previous == null;
    }

    public void remove(symbol sym) throws internal_error {
        not_null(sym);
        this._all.remove(sym.name());
    }

    public boolean add(symbol_set other) throws internal_error {
        boolean result = false;
        not_null(other);
        Enumeration e = other.all();
        while (e.hasMoreElements()) {
            result = add((symbol) e.nextElement()) || result;
        }
        return result;
    }

    public void remove(symbol_set other) throws internal_error {
        not_null(other);
        Enumeration e = other.all();
        while (e.hasMoreElements()) {
            remove((symbol) e.nextElement());
        }
    }

    public boolean equals(symbol_set other) {
        if (other == null || other.size() != size()) {
            return false;
        }
        try {
            return is_subset_of(other);
        } catch (internal_error e) {
            e.crash();
            return false;
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof symbol_set)) {
            return false;
        }
        return equals((symbol_set) other);
    }

    public int hashCode() {
        int result = 0;
        Enumeration e = all();
        for (int cnt = 0; e.hasMoreElements() && cnt < 5; cnt++) {
            result ^= ((symbol) e.nextElement()).hashCode();
        }
        return result;
    }

    public String toString() {
        String result = "{";
        boolean comma_flag = false;
        Enumeration e = all();
        while (e.hasMoreElements()) {
            if (comma_flag) {
                result = String.valueOf(result) + ", ";
            } else {
                comma_flag = true;
            }
            result = String.valueOf(result) + ((symbol) e.nextElement()).name();
        }
        return String.valueOf(result) + "}";
    }
}
