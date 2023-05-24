package java_cup;

import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.commons.cli.HelpFormatter;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/production.class */
public class production {
    protected static Hashtable _all = new Hashtable();
    protected static int next_index;
    protected symbol_part _lhs;
    protected int _rhs_prec;
    protected int _rhs_assoc;
    protected production_part[] _rhs;
    protected int _rhs_length;
    protected action_part _action;
    protected int _index;
    protected int _num_reductions;
    protected boolean _nullable_known;
    protected boolean _nullable;
    protected terminal_set _first_set;

    public production(non_terminal lhs_sym, production_part[] rhs_parts, int rhs_l, String action_str) throws internal_error {
        String action_str2;
        this._rhs_prec = -1;
        this._rhs_assoc = -1;
        this._num_reductions = 0;
        this._nullable_known = false;
        this._nullable = false;
        this._first_set = new terminal_set();
        int rightlen = rhs_l;
        if (rhs_l >= 0) {
            this._rhs_length = rhs_l;
        } else if (rhs_parts != null) {
            this._rhs_length = rhs_parts.length;
        } else {
            this._rhs_length = 0;
        }
        if (lhs_sym == null) {
            throw new internal_error("Attempt to construct a production with a null LHS");
        }
        if (rhs_l > 0) {
            if (rhs_parts[rhs_l - 1].is_action()) {
                rightlen = rhs_l - 1;
            } else {
                rightlen = rhs_l;
            }
        }
        String declare_str = declare_labels(rhs_parts, rightlen, action_str);
        if (action_str == null) {
            action_str2 = declare_str;
        } else {
            action_str2 = String.valueOf(declare_str) + action_str;
        }
        lhs_sym.note_use();
        this._lhs = new symbol_part(lhs_sym);
        this._rhs_length = merge_adjacent_actions(rhs_parts, this._rhs_length);
        action_part tail_action = strip_trailing_action(rhs_parts, this._rhs_length);
        if (tail_action != null) {
            this._rhs_length--;
        }
        this._rhs = new production_part[this._rhs_length];
        for (int i = 0; i < this._rhs_length; i++) {
            this._rhs[i] = rhs_parts[i];
            if (!this._rhs[i].is_action()) {
                ((symbol_part) this._rhs[i]).the_symbol().note_use();
                if (((symbol_part) this._rhs[i]).the_symbol() instanceof terminal) {
                    this._rhs_prec = ((terminal) ((symbol_part) this._rhs[i]).the_symbol()).precedence_num();
                    this._rhs_assoc = ((terminal) ((symbol_part) this._rhs[i]).the_symbol()).precedence_side();
                }
            }
        }
        action_str2 = action_str2 == null ? "" : action_str2;
        if (tail_action != null && tail_action.code_string() != null) {
            action_str2 = String.valueOf(action_str2) + "\t\t" + tail_action.code_string();
        }
        this._action = new action_part(action_str2);
        remove_embedded_actions();
        int i2 = next_index;
        next_index = i2 + 1;
        this._index = i2;
        _all.put(new Integer(this._index), this);
        lhs_sym.add_production(this);
    }

    public production(non_terminal lhs_sym, production_part[] rhs_parts, int rhs_l) throws internal_error {
        this(lhs_sym, rhs_parts, rhs_l, null);
    }

    public production(non_terminal lhs_sym, production_part[] rhs_parts, int rhs_l, String action_str, int prec_num, int prec_side) throws internal_error {
        this(lhs_sym, rhs_parts, rhs_l, action_str);
        set_precedence_num(prec_num);
        set_precedence_side(prec_side);
    }

    public production(non_terminal lhs_sym, production_part[] rhs_parts, int rhs_l, int prec_num, int prec_side) throws internal_error {
        this(lhs_sym, rhs_parts, rhs_l, null);
        set_precedence_num(prec_num);
        set_precedence_side(prec_side);
    }

    public static Enumeration all() {
        return _all.elements();
    }

    public static production find(int indx) {
        return (production) _all.get(new Integer(indx));
    }

    public static void clear() {
        _all.clear();
        next_index = 0;
    }

    public static int number() {
        return _all.size();
    }

    public symbol_part lhs() {
        return this._lhs;
    }

    public int precedence_num() {
        return this._rhs_prec;
    }

    public int precedence_side() {
        return this._rhs_assoc;
    }

    public void set_precedence_num(int prec_num) {
        this._rhs_prec = prec_num;
    }

    public void set_precedence_side(int prec_side) {
        this._rhs_assoc = prec_side;
    }

    public production_part rhs(int indx) throws internal_error {
        if (indx >= 0 && indx < this._rhs_length) {
            return this._rhs[indx];
        }
        throw new internal_error("Index out of range for right hand side of production");
    }

    public int rhs_length() {
        return this._rhs_length;
    }

    public action_part action() {
        return this._action;
    }

    public int index() {
        return this._index;
    }

    public int num_reductions() {
        return this._num_reductions;
    }

    public void note_reduction_use() {
        this._num_reductions++;
    }

    public boolean nullable_known() {
        return this._nullable_known;
    }

    public boolean nullable() {
        return this._nullable;
    }

    public terminal_set first_set() {
        return this._first_set;
    }

    protected static boolean is_id_start(char c) {
        if (c < 'a' || c > 'z') {
            return (c >= 'A' && c <= 'Z') || c == '_';
        }
        return true;
    }

    protected static boolean is_id_char(char c) {
        if (is_id_start(c)) {
            return true;
        }
        return c >= '0' && c <= '9';
    }

    protected String make_declaration(String labelname, String stack_type, int offset) {
        String ret;
        if (emit.lr_values()) {
            if (!emit.locations()) {
                ret = "\t\tint " + labelname + "left = ((java_cup.runtime.Symbol)" + emit.pre("stack") + (offset == 0 ? ".peek()" : ".elementAt(" + emit.pre("top") + HelpFormatter.DEFAULT_OPT_PREFIX + offset + ")") + ").left;\n\t\tint " + labelname + "right = ((java_cup.runtime.Symbol)" + emit.pre("stack") + (offset == 0 ? ".peek()" : ".elementAt(" + emit.pre("top") + HelpFormatter.DEFAULT_OPT_PREFIX + offset + ")") + ").right;\n";
            } else {
                ret = "\t\tLocation " + labelname + "xleft = ((java_cup.runtime.ComplexSymbolFactory.ComplexSymbol)" + emit.pre("stack") + (offset == 0 ? ".peek()" : ".elementAt(" + emit.pre("top") + HelpFormatter.DEFAULT_OPT_PREFIX + offset + ")") + ").xleft;\n\t\tLocation " + labelname + "xright = ((java_cup.runtime.ComplexSymbolFactory.ComplexSymbol)" + emit.pre("stack") + (offset == 0 ? ".peek()" : ".elementAt(" + emit.pre("top") + HelpFormatter.DEFAULT_OPT_PREFIX + offset + ")") + ").xright;\n";
            }
        } else {
            ret = "";
        }
        return String.valueOf(ret) + "\t\t" + stack_type + Instruction.argsep + labelname + " = (" + stack_type + ")((java_cup.runtime.Symbol) " + emit.pre("stack") + (offset == 0 ? ".peek()" : ".elementAt(" + emit.pre("top") + HelpFormatter.DEFAULT_OPT_PREFIX + offset + ")") + ").value;\n";
    }

    protected String declare_labels(production_part[] rhs, int rhs_len, String final_action) {
        String declaration = "";
        for (int pos = 0; pos < rhs_len; pos++) {
            if (!rhs[pos].is_action()) {
                symbol_part part = (symbol_part) rhs[pos];
                String label = part.label();
                String label2 = label;
                if (label != null || emit._xmlactions) {
                    if (label2 == null) {
                        label2 = String.valueOf(part.the_symbol().name()) + pos;
                    }
                    declaration = String.valueOf(declaration) + make_declaration(label2, part.the_symbol().stack_type(), (rhs_len - pos) - 1);
                }
            }
        }
        return declaration;
    }

    protected int merge_adjacent_actions(production_part[] rhs_parts, int len) {
        if (rhs_parts == null || len == 0) {
            return 0;
        }
        int merge_cnt = 0;
        int to_loc = -1;
        for (int from_loc = 0; from_loc < len; from_loc++) {
            if (to_loc < 0 || !rhs_parts[to_loc].is_action() || !rhs_parts[from_loc].is_action()) {
                to_loc++;
                if (to_loc != from_loc) {
                    rhs_parts[to_loc] = null;
                }
            }
            if (to_loc != from_loc) {
                if (rhs_parts[to_loc] != null && rhs_parts[to_loc].is_action() && rhs_parts[from_loc].is_action()) {
                    rhs_parts[to_loc] = new action_part(String.valueOf(((action_part) rhs_parts[to_loc]).code_string()) + ((action_part) rhs_parts[from_loc]).code_string());
                    merge_cnt++;
                } else {
                    rhs_parts[to_loc] = rhs_parts[from_loc];
                }
            }
        }
        return len - merge_cnt;
    }

    protected action_part strip_trailing_action(production_part[] rhs_parts, int len) {
        if (rhs_parts != null && len != 0 && rhs_parts[len - 1].is_action()) {
            action_part result = (action_part) rhs_parts[len - 1];
            rhs_parts[len - 1] = null;
            return result;
        }
        return null;
    }

    protected void remove_embedded_actions() throws internal_error {
        int lastLocation = -1;
        for (int act_loc = 0; act_loc < rhs_length(); act_loc++) {
            if (rhs(act_loc).is_action()) {
                String declare_str = declare_labels(this._rhs, act_loc, "");
                non_terminal new_nt = non_terminal.create_new(null, lhs().the_symbol().stack_type());
                new_nt.is_embedded_action = true;
                new action_production(this, new_nt, null, 0, String.valueOf(declare_str) + ((action_part) rhs(act_loc)).code_string(), lastLocation == -1 ? -1 : act_loc - lastLocation);
                this._rhs[act_loc] = new symbol_part(new_nt);
                lastLocation = act_loc;
            }
        }
    }

    public boolean check_nullable() throws internal_error {
        if (nullable_known()) {
            return nullable();
        }
        if (rhs_length() == 0) {
            return set_nullable(true);
        }
        for (int pos = 0; pos < rhs_length(); pos++) {
            production_part part = rhs(pos);
            if (!part.is_action()) {
                symbol sym = ((symbol_part) part).the_symbol();
                if (!sym.is_non_term()) {
                    return set_nullable(false);
                }
                if (!((non_terminal) sym).nullable()) {
                    return false;
                }
            }
        }
        return set_nullable(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean set_nullable(boolean v) {
        this._nullable_known = true;
        this._nullable = v;
        return v;
    }

    public terminal_set check_first_set() throws internal_error {
        int part = 0;
        while (true) {
            if (part >= rhs_length()) {
                break;
            }
            if (!rhs(part).is_action()) {
                symbol sym = ((symbol_part) rhs(part)).the_symbol();
                if (sym.is_non_term()) {
                    this._first_set.add(((non_terminal) sym).first_set());
                    if (!((non_terminal) sym).nullable()) {
                        break;
                    }
                } else {
                    this._first_set.add((terminal) sym);
                    break;
                }
            }
            part++;
        }
        return first_set();
    }

    public boolean equals(production other) {
        return other != null && other._index == this._index;
    }

    public boolean equals(Object other) {
        if (!(other instanceof production)) {
            return false;
        }
        return equals((production) other);
    }

    public int hashCode() {
        return this._index * 13;
    }

    public String toString() {
        String result;
        try {
            String result2 = "production [" + index() + "]: ";
            String result3 = String.valueOf(String.valueOf(result2) + (lhs() != null ? lhs().toString() : "$$NULL-LHS$$")) + " :: = ";
            for (int i = 0; i < rhs_length(); i++) {
                result3 = String.valueOf(result3) + rhs(i) + Instruction.argsep;
            }
            result = String.valueOf(result3) + ";";
            if (action() != null && action().code_string() != null) {
                result = String.valueOf(result) + " {" + action().code_string() + "}";
            }
            if (nullable_known()) {
                if (nullable()) {
                    result = String.valueOf(result) + "[NULLABLE]";
                } else {
                    result = String.valueOf(result) + "[NOT NULLABLE]";
                }
            }
        } catch (internal_error e) {
            e.crash();
            result = null;
        }
        return result;
    }

    public String to_simple_string() throws internal_error {
        String result = lhs() != null ? lhs().the_symbol().name() : "NULL_LHS";
        String result2 = String.valueOf(result) + " ::= ";
        for (int i = 0; i < rhs_length(); i++) {
            if (!rhs(i).is_action()) {
                result2 = String.valueOf(result2) + ((symbol_part) rhs(i)).the_symbol().name() + Instruction.argsep;
            }
        }
        return result2;
    }
}
