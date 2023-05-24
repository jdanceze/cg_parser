package java_cup;

import java.util.BitSet;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/terminal_set.class */
public class terminal_set {
    public static final terminal_set EMPTY = new terminal_set();
    protected BitSet _elements;

    public terminal_set() {
        this._elements = new BitSet(terminal.number());
    }

    public terminal_set(terminal_set other) throws internal_error {
        not_null(other);
        this._elements = (BitSet) other._elements.clone();
    }

    protected void not_null(Object obj) throws internal_error {
        if (obj == null) {
            throw new internal_error("Null object used in set operation");
        }
    }

    public boolean empty() {
        return equals(EMPTY);
    }

    public boolean contains(terminal sym) throws internal_error {
        not_null(sym);
        return this._elements.get(sym.index());
    }

    public boolean contains(int indx) {
        return this._elements.get(indx);
    }

    public boolean is_subset_of(terminal_set other) throws internal_error {
        not_null(other);
        BitSet copy_other = (BitSet) other._elements.clone();
        copy_other.or(this._elements);
        return copy_other.equals(other._elements);
    }

    public boolean is_superset_of(terminal_set other) throws internal_error {
        not_null(other);
        return other.is_subset_of(this);
    }

    public boolean add(terminal sym) throws internal_error {
        not_null(sym);
        boolean result = this._elements.get(sym.index());
        if (!result) {
            this._elements.set(sym.index());
        }
        return result;
    }

    public void remove(terminal sym) throws internal_error {
        not_null(sym);
        this._elements.clear(sym.index());
    }

    public boolean add(terminal_set other) throws internal_error {
        not_null(other);
        BitSet copy = (BitSet) this._elements.clone();
        this._elements.or(other._elements);
        return !this._elements.equals(copy);
    }

    public boolean intersects(terminal_set other) throws internal_error {
        not_null(other);
        return this._elements.intersects(other._elements);
    }

    public boolean equals(terminal_set other) {
        if (other == null) {
            return false;
        }
        return this._elements.equals(other._elements);
    }

    public boolean equals(Object other) {
        if (!(other instanceof terminal_set)) {
            return false;
        }
        return equals((terminal_set) other);
    }

    public String toString() {
        String result = "{";
        boolean comma_flag = false;
        for (int t = 0; t < terminal.number(); t++) {
            if (this._elements.get(t)) {
                if (comma_flag) {
                    result = String.valueOf(result) + ", ";
                } else {
                    comma_flag = true;
                }
                result = String.valueOf(result) + terminal.find(t).name();
            }
        }
        return String.valueOf(result) + "}";
    }
}
