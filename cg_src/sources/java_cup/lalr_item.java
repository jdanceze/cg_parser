package java_cup;

import java.util.Stack;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/lalr_item.class */
public class lalr_item extends lr_item_core {
    protected terminal_set _lookahead;
    protected Stack _propagate_items;
    protected boolean needs_propagation;

    public lalr_item(production prod, int pos, terminal_set look) throws internal_error {
        super(prod, pos);
        this._lookahead = look;
        this._propagate_items = new Stack();
        this.needs_propagation = true;
    }

    public lalr_item(production prod, terminal_set look) throws internal_error {
        this(prod, 0, look);
    }

    public lalr_item(production prod) throws internal_error {
        this(prod, 0, new terminal_set());
    }

    public terminal_set lookahead() {
        return this._lookahead;
    }

    public Stack propagate_items() {
        return this._propagate_items;
    }

    public void add_propagate(lalr_item prop_to) {
        this._propagate_items.push(prop_to);
        this.needs_propagation = true;
    }

    public void propagate_lookaheads(terminal_set incoming) throws internal_error {
        boolean change = false;
        if (!this.needs_propagation && (incoming == null || incoming.empty())) {
            return;
        }
        if (incoming != null) {
            change = lookahead().add(incoming);
        }
        if (change || this.needs_propagation) {
            this.needs_propagation = false;
            for (int i = 0; i < propagate_items().size(); i++) {
                ((lalr_item) propagate_items().elementAt(i)).propagate_lookaheads(lookahead());
            }
        }
    }

    public lalr_item shift() throws internal_error {
        if (dot_at_end()) {
            throw new internal_error("Attempt to shift past end of an lalr_item");
        }
        lalr_item result = new lalr_item(the_production(), dot_pos() + 1, new terminal_set(lookahead()));
        add_propagate(result);
        return result;
    }

    public terminal_set calc_lookahead(terminal_set lookahead_after) throws internal_error {
        if (dot_at_end()) {
            throw new internal_error("Attempt to calculate a lookahead set with a completed item");
        }
        terminal_set result = new terminal_set();
        for (int pos = dot_pos() + 1; pos < the_production().rhs_length(); pos++) {
            production_part part = the_production().rhs(pos);
            if (!part.is_action()) {
                symbol sym = ((symbol_part) part).the_symbol();
                if (!sym.is_non_term()) {
                    result.add((terminal) sym);
                    return result;
                }
                result.add(((non_terminal) sym).first_set());
                if (!((non_terminal) sym).nullable()) {
                    return result;
                }
            }
        }
        result.add(lookahead_after);
        return result;
    }

    public boolean lookahead_visible() throws internal_error {
        if (dot_at_end()) {
            return true;
        }
        for (int pos = dot_pos() + 1; pos < the_production().rhs_length(); pos++) {
            production_part part = the_production().rhs(pos);
            if (!part.is_action()) {
                symbol sym = ((symbol_part) part).the_symbol();
                if (!sym.is_non_term() || !((non_terminal) sym).nullable()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean equals(lalr_item other) {
        if (other == null) {
            return false;
        }
        return super.equals((lr_item_core) other);
    }

    @Override // java_cup.lr_item_core
    public boolean equals(Object other) {
        if (!(other instanceof lalr_item)) {
            return false;
        }
        return equals((lalr_item) other);
    }

    @Override // java_cup.lr_item_core
    public int hashCode() {
        return super.hashCode();
    }

    @Override // java_cup.lr_item_core
    public String toString() {
        String result;
        String result2 = String.valueOf("") + "[";
        String result3 = String.valueOf(String.valueOf(result2) + super.toString()) + ", ";
        if (lookahead() != null) {
            String result4 = String.valueOf(result3) + "{";
            for (int t = 0; t < terminal.number(); t++) {
                if (lookahead().contains(t)) {
                    result4 = String.valueOf(result4) + terminal.find(t).name() + Instruction.argsep;
                }
            }
            result = String.valueOf(result4) + "}";
        } else {
            result = String.valueOf(result3) + "NULL LOOKAHEAD!!";
        }
        return String.valueOf(result) + "]";
    }
}
