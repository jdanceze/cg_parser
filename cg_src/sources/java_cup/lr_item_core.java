package java_cup;

import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/lr_item_core.class */
public class lr_item_core {
    protected production _the_production;
    protected int _dot_pos;
    protected int _core_hash_cache;
    protected symbol _symbol_after_dot;

    public lr_item_core(production prod, int pos) throws internal_error {
        this._symbol_after_dot = null;
        if (prod == null) {
            throw new internal_error("Attempt to create an lr_item_core with a null production");
        }
        this._the_production = prod;
        if (pos < 0 || pos > this._the_production.rhs_length()) {
            throw new internal_error("Attempt to create an lr_item_core with a bad dot position");
        }
        this._dot_pos = pos;
        this._core_hash_cache = (13 * this._the_production.hashCode()) + pos;
        if (this._dot_pos < this._the_production.rhs_length()) {
            production_part part = this._the_production.rhs(this._dot_pos);
            if (!part.is_action()) {
                this._symbol_after_dot = ((symbol_part) part).the_symbol();
            }
        }
    }

    public lr_item_core(production prod) throws internal_error {
        this(prod, 0);
    }

    public production the_production() {
        return this._the_production;
    }

    public int dot_pos() {
        return this._dot_pos;
    }

    public boolean dot_at_end() {
        return this._dot_pos >= this._the_production.rhs_length();
    }

    public symbol symbol_after_dot() {
        return this._symbol_after_dot;
    }

    public non_terminal dot_before_nt() {
        symbol sym = symbol_after_dot();
        if (sym != null && sym.is_non_term()) {
            return (non_terminal) sym;
        }
        return null;
    }

    public lr_item_core shift_core() throws internal_error {
        if (dot_at_end()) {
            throw new internal_error("Attempt to shift past end of an lr_item_core");
        }
        return new lr_item_core(this._the_production, this._dot_pos + 1);
    }

    public boolean core_equals(lr_item_core other) {
        return other != null && this._the_production.equals(other._the_production) && this._dot_pos == other._dot_pos;
    }

    public boolean equals(lr_item_core other) {
        return core_equals(other);
    }

    public boolean equals(Object other) {
        if (!(other instanceof lr_item_core)) {
            return false;
        }
        return equals((lr_item_core) other);
    }

    public int core_hashCode() {
        return this._core_hash_cache;
    }

    public int hashCode() {
        return this._core_hash_cache;
    }

    protected int obj_hash() {
        return super.hashCode();
    }

    public String to_simple_string() throws internal_error {
        String result;
        String str;
        if (this._the_production.lhs() != null && this._the_production.lhs().the_symbol() != null && this._the_production.lhs().the_symbol().name() != null) {
            result = this._the_production.lhs().the_symbol().name();
        } else {
            result = "$$NULL$$";
        }
        String result2 = String.valueOf(result) + " ::= ";
        for (int i = 0; i < this._the_production.rhs_length(); i++) {
            if (i == this._dot_pos) {
                result2 = String.valueOf(result2) + "(*) ";
            }
            if (this._the_production.rhs(i) == null) {
                str = String.valueOf(result2) + "$$NULL$$ ";
            } else {
                production_part part = this._the_production.rhs(i);
                if (part == null) {
                    str = String.valueOf(result2) + "$$NULL$$ ";
                } else if (part.is_action()) {
                    str = String.valueOf(result2) + "{ACTION} ";
                } else if (((symbol_part) part).the_symbol() != null && ((symbol_part) part).the_symbol().name() != null) {
                    str = String.valueOf(result2) + ((symbol_part) part).the_symbol().name() + Instruction.argsep;
                } else {
                    str = String.valueOf(result2) + "$$NULL$$ ";
                }
            }
            result2 = str;
        }
        if (this._dot_pos == this._the_production.rhs_length()) {
            result2 = String.valueOf(result2) + "(*) ";
        }
        return result2;
    }

    public String toString() {
        try {
            return to_simple_string();
        } catch (internal_error e) {
            e.crash();
            return null;
        }
    }
}
