package java_cup;

import java.util.Enumeration;
import java.util.Hashtable;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/lalr_item_set.class */
public class lalr_item_set {
    protected Hashtable _all;
    protected Integer hashcode_cache;

    public lalr_item_set() {
        this._all = new Hashtable(11);
        this.hashcode_cache = null;
    }

    public lalr_item_set(lalr_item_set other) throws internal_error {
        this._all = new Hashtable(11);
        this.hashcode_cache = null;
        not_null(other);
        this._all = (Hashtable) other._all.clone();
    }

    public Enumeration all() {
        return this._all.elements();
    }

    public int size() {
        return this._all.size();
    }

    public boolean contains(lalr_item itm) {
        return this._all.containsKey(itm);
    }

    public lalr_item find(lalr_item itm) {
        return (lalr_item) this._all.get(itm);
    }

    public boolean is_subset_of(lalr_item_set other) throws internal_error {
        not_null(other);
        Enumeration e = all();
        while (e.hasMoreElements()) {
            if (!other.contains((lalr_item) e.nextElement())) {
                return false;
            }
        }
        return true;
    }

    public boolean is_superset_of(lalr_item_set other) throws internal_error {
        not_null(other);
        return other.is_subset_of(this);
    }

    public lalr_item add(lalr_item itm) throws internal_error {
        not_null(itm);
        lalr_item other = (lalr_item) this._all.get(itm);
        if (other != null) {
            other.lookahead().add(itm.lookahead());
            return other;
        }
        this.hashcode_cache = null;
        this._all.put(itm, itm);
        return itm;
    }

    public void remove(lalr_item itm) throws internal_error {
        not_null(itm);
        this.hashcode_cache = null;
        this._all.remove(itm);
    }

    public void add(lalr_item_set other) throws internal_error {
        not_null(other);
        Enumeration e = other.all();
        while (e.hasMoreElements()) {
            add((lalr_item) e.nextElement());
        }
    }

    public void remove(lalr_item_set other) throws internal_error {
        not_null(other);
        Enumeration e = other.all();
        while (e.hasMoreElements()) {
            remove((lalr_item) e.nextElement());
        }
    }

    public lalr_item get_one() throws internal_error {
        Enumeration the_set = all();
        if (the_set.hasMoreElements()) {
            lalr_item result = (lalr_item) the_set.nextElement();
            remove(result);
            return result;
        }
        return null;
    }

    protected void not_null(Object obj) throws internal_error {
        if (obj == null) {
            throw new internal_error("Null object used in set operation");
        }
    }

    public void compute_closure() throws internal_error {
        this.hashcode_cache = null;
        lalr_item_set consider = new lalr_item_set(this);
        while (consider.size() > 0) {
            lalr_item itm = consider.get_one();
            non_terminal nt = itm.dot_before_nt();
            if (nt != null) {
                terminal_set new_lookaheads = itm.calc_lookahead(itm.lookahead());
                boolean need_prop = itm.lookahead_visible();
                Enumeration p = nt.productions();
                while (p.hasMoreElements()) {
                    production prod = (production) p.nextElement();
                    lalr_item new_itm = new lalr_item(prod, new terminal_set(new_lookaheads));
                    lalr_item add_itm = add(new_itm);
                    if (need_prop) {
                        itm.add_propagate(add_itm);
                    }
                    if (add_itm == new_itm) {
                        consider.add(new_itm);
                    }
                }
            }
        }
    }

    public boolean equals(lalr_item_set other) {
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
        if (!(other instanceof lalr_item_set)) {
            return false;
        }
        return equals((lalr_item_set) other);
    }

    public int hashCode() {
        int result = 0;
        if (this.hashcode_cache == null) {
            Enumeration e = all();
            int cnt = 0;
            while (e.hasMoreElements()) {
                result ^= ((lalr_item) e.nextElement()).hashCode();
                cnt++;
            }
            this.hashcode_cache = new Integer(result);
        }
        return this.hashcode_cache.intValue();
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("{\n");
        Enumeration e = all();
        while (e.hasMoreElements()) {
            result.append("  " + ((lalr_item) e.nextElement()) + "\n");
        }
        result.append("}");
        return result.toString();
    }
}
