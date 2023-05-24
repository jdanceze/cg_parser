package java_cup.runtime;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java_cup.runtime.ComplexSymbolFactory;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/runtime/lr_parser.class */
public abstract class lr_parser {
    public SymbolFactory symbolFactory;
    protected static final int _error_sync_size = 3;
    protected boolean _done_parsing;
    protected int tos;
    protected Symbol cur_token;
    protected Stack stack;
    protected short[][] production_tab;
    protected short[][] action_tab;
    protected short[][] reduce_tab;
    private Scanner _scanner;
    protected Symbol[] lookahead;
    protected int lookahead_pos;

    public abstract short[][] production_table();

    public abstract short[][] action_table();

    public abstract short[][] reduce_table();

    public abstract int start_state();

    public abstract int start_production();

    public abstract int EOF_sym();

    public abstract int error_sym();

    public abstract Symbol do_action(int i, lr_parser lr_parserVar, Stack stack, int i2) throws Exception;

    protected abstract void init_actions() throws Exception;

    @Deprecated
    public lr_parser() {
        this(new DefaultSymbolFactory());
    }

    public lr_parser(SymbolFactory fac) {
        this._done_parsing = false;
        this.stack = new Stack();
        this.symbolFactory = fac;
    }

    @Deprecated
    public lr_parser(Scanner s) {
        this(s, new DefaultSymbolFactory());
    }

    public lr_parser(Scanner s, SymbolFactory symfac) {
        this();
        this.symbolFactory = symfac;
        setScanner(s);
    }

    public SymbolFactory getSymbolFactory() {
        return this.symbolFactory;
    }

    protected int error_sync_size() {
        return 3;
    }

    public void done_parsing() {
        this._done_parsing = true;
    }

    public void setScanner(Scanner s) {
        this._scanner = s;
    }

    public Scanner getScanner() {
        return this._scanner;
    }

    public void user_init() throws Exception {
    }

    public Symbol scan() throws Exception {
        Symbol sym = getScanner().next_token();
        return sym != null ? sym : getSymbolFactory().newSymbol("END_OF_FILE", EOF_sym());
    }

    public void report_fatal_error(String message, Object info) throws Exception {
        done_parsing();
        report_error(message, info);
        throw new Exception("Can't recover from previous error(s)");
    }

    public void report_error(String message, Object info) {
        if (info instanceof ComplexSymbolFactory.ComplexSymbol) {
            ComplexSymbolFactory.ComplexSymbol cs = (ComplexSymbolFactory.ComplexSymbol) info;
            System.err.println(String.valueOf(message) + " for input symbol \"" + cs.getName() + "\" spanning from " + cs.getLeft() + " to " + cs.getRight());
            return;
        }
        System.err.print(message);
        System.err.flush();
        if (info instanceof Symbol) {
            if (((Symbol) info).left != -1) {
                System.err.println(" at character " + ((Symbol) info).left + " of input");
            } else {
                System.err.println("");
            }
        }
    }

    public void syntax_error(Symbol cur_token) {
        report_error("Syntax error", cur_token);
        report_expected_token_ids();
    }

    public Class getSymbolContainer() {
        return null;
    }

    protected void report_expected_token_ids() {
        List<Integer> ids = expected_token_ids();
        LinkedList<String> list = new LinkedList<>();
        for (Integer expected : ids) {
            list.add(symbl_name_from_id(expected.intValue()));
        }
        System.out.println("instead expected token classes are " + list);
    }

    public String symbl_name_from_id(int id) {
        Field[] fields = getSymbolContainer().getFields();
        for (Field f : fields) {
            if (f.getInt(null) != id) {
                continue;
            } else {
                return f.getName();
            }
        }
        return "invalid symbol id";
    }

    public List<Integer> expected_token_ids() {
        List<Integer> ret = new LinkedList<>();
        int parse_state = ((Symbol) this.stack.peek()).parse_state;
        short[] row = this.action_tab[parse_state];
        for (int i = 0; i < row.length; i += 2) {
            if (row[i] != -1 && validate_expected_symbol(row[i])) {
                ret.add(new Integer(row[i]));
            }
        }
        return ret;
    }

    private boolean validate_expected_symbol(int id) {
        try {
            virtual_parse_stack vstack = new virtual_parse_stack(this.stack);
            while (true) {
                int act = get_action(vstack.top(), id);
                if (act == 0) {
                    return false;
                }
                if (act > 0) {
                    vstack.push(act - 1);
                    if (!advance_lookahead()) {
                        return true;
                    }
                } else if ((-act) - 1 == start_production()) {
                    return true;
                } else {
                    short lhs = this.production_tab[(-act) - 1][0];
                    short rhs_size = this.production_tab[(-act) - 1][1];
                    for (int i = 0; i < rhs_size; i++) {
                        vstack.pop();
                    }
                    vstack.push(get_reduce(vstack.top(), lhs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public void unrecovered_syntax_error(Symbol cur_token) throws Exception {
        report_fatal_error("Couldn't repair and continue parse", cur_token);
    }

    protected final short get_action(int state, int sym) {
        short[] row = this.action_tab[state];
        if (row.length < 20) {
            int probe = 0;
            while (probe < row.length) {
                int i = probe;
                int probe2 = probe + 1;
                short tag = row[i];
                if (tag != sym && tag != -1) {
                    probe = probe2 + 1;
                } else {
                    return row[probe2];
                }
            }
            return (short) 0;
        }
        int first = 0;
        int last = ((row.length - 1) / 2) - 1;
        while (first <= last) {
            int probe3 = (first + last) / 2;
            if (sym == row[probe3 * 2]) {
                return row[(probe3 * 2) + 1];
            }
            if (sym > row[probe3 * 2]) {
                first = probe3 + 1;
            } else {
                last = probe3 - 1;
            }
        }
        return row[row.length - 1];
    }

    protected final short get_reduce(int state, int sym) {
        short[] row = this.reduce_tab[state];
        if (row == null) {
            return (short) -1;
        }
        int probe = 0;
        while (probe < row.length) {
            int i = probe;
            int probe2 = probe + 1;
            short tag = row[i];
            if (tag != sym && tag != -1) {
                probe = probe2 + 1;
            } else {
                return row[probe2];
            }
        }
        return (short) -1;
    }

    public Symbol parse() throws Exception {
        Symbol lhs_sym = null;
        this.production_tab = production_table();
        this.action_tab = action_table();
        this.reduce_tab = reduce_table();
        init_actions();
        user_init();
        this.cur_token = scan();
        this.stack.removeAllElements();
        this.stack.push(getSymbolFactory().startSymbol("START", 0, start_state()));
        this.tos = 0;
        this._done_parsing = false;
        while (!this._done_parsing) {
            if (this.cur_token.used_by_parser) {
                throw new Error("Symbol recycling detected (fix your scanner).");
            }
            int act = get_action(((Symbol) this.stack.peek()).parse_state, this.cur_token.sym);
            if (act > 0) {
                this.cur_token.parse_state = act - 1;
                this.cur_token.used_by_parser = true;
                this.stack.push(this.cur_token);
                this.tos++;
                this.cur_token = scan();
            } else if (act < 0) {
                lhs_sym = do_action((-act) - 1, this, this.stack, this.tos);
                short lhs_sym_num = this.production_tab[(-act) - 1][0];
                short handle_size = this.production_tab[(-act) - 1][1];
                for (int i = 0; i < handle_size; i++) {
                    this.stack.pop();
                    this.tos--;
                }
                lhs_sym.parse_state = get_reduce(((Symbol) this.stack.peek()).parse_state, lhs_sym_num);
                lhs_sym.used_by_parser = true;
                this.stack.push(lhs_sym);
                this.tos++;
            } else if (act == 0) {
                syntax_error(this.cur_token);
                if (!error_recovery(false)) {
                    unrecovered_syntax_error(this.cur_token);
                    done_parsing();
                } else {
                    lhs_sym = (Symbol) this.stack.peek();
                }
            }
        }
        return lhs_sym;
    }

    public void debug_message(String mess) {
        System.err.println(mess);
    }

    public void dump_stack() {
        if (this.stack == null) {
            debug_message("# Stack dump requested, but stack is null");
            return;
        }
        debug_message("============ Parse Stack Dump ============");
        for (int i = 0; i < this.stack.size(); i++) {
            debug_message("Symbol: " + ((Symbol) this.stack.elementAt(i)).sym + " State: " + ((Symbol) this.stack.elementAt(i)).parse_state);
        }
        debug_message("==========================================");
    }

    public void debug_reduce(int prod_num, int nt_num, int rhs_size) {
        debug_message("# Reduce with prod #" + prod_num + " [NT=" + nt_num + ", SZ=" + rhs_size + "]");
    }

    public void debug_shift(Symbol shift_tkn) {
        debug_message("# Shift under term #" + shift_tkn.sym + " to state #" + shift_tkn.parse_state);
    }

    public void debug_stack() {
        StringBuffer sb = new StringBuffer("## STACK:");
        for (int i = 0; i < this.stack.size(); i++) {
            Symbol s = (Symbol) this.stack.elementAt(i);
            sb.append(" <state " + s.parse_state + ", sym " + s.sym + ">");
            if (i % 3 == 2 || i == this.stack.size() - 1) {
                debug_message(sb.toString());
                sb = new StringBuffer("         ");
            }
        }
    }

    public Symbol debug_parse() throws Exception {
        Symbol lhs_sym = null;
        this.production_tab = production_table();
        this.action_tab = action_table();
        this.reduce_tab = reduce_table();
        debug_message("# Initializing parser");
        init_actions();
        user_init();
        this.cur_token = scan();
        debug_message("# Current Symbol is #" + this.cur_token.sym);
        this.stack.removeAllElements();
        this.stack.push(getSymbolFactory().startSymbol("START", 0, start_state()));
        this.tos = 0;
        this._done_parsing = false;
        while (!this._done_parsing) {
            if (this.cur_token.used_by_parser) {
                throw new Error("Symbol recycling detected (fix your scanner).");
            }
            int act = get_action(((Symbol) this.stack.peek()).parse_state, this.cur_token.sym);
            if (act > 0) {
                this.cur_token.parse_state = act - 1;
                this.cur_token.used_by_parser = true;
                debug_shift(this.cur_token);
                this.stack.push(this.cur_token);
                this.tos++;
                this.cur_token = scan();
                debug_message("# Current token is " + this.cur_token);
            } else if (act < 0) {
                lhs_sym = do_action((-act) - 1, this, this.stack, this.tos);
                short lhs_sym_num = this.production_tab[(-act) - 1][0];
                short handle_size = this.production_tab[(-act) - 1][1];
                debug_reduce((-act) - 1, lhs_sym_num, handle_size);
                for (int i = 0; i < handle_size; i++) {
                    this.stack.pop();
                    this.tos--;
                }
                int act2 = get_reduce(((Symbol) this.stack.peek()).parse_state, lhs_sym_num);
                debug_message("# Reduce rule: top state " + ((Symbol) this.stack.peek()).parse_state + ", lhs sym " + ((int) lhs_sym_num) + " -> state " + act2);
                lhs_sym.parse_state = act2;
                lhs_sym.used_by_parser = true;
                this.stack.push(lhs_sym);
                this.tos++;
                debug_message("# Goto state #" + act2);
            } else if (act == 0) {
                syntax_error(this.cur_token);
                if (!error_recovery(true)) {
                    unrecovered_syntax_error(this.cur_token);
                    done_parsing();
                } else {
                    lhs_sym = (Symbol) this.stack.peek();
                }
            }
        }
        return lhs_sym;
    }

    protected boolean error_recovery(boolean debug) throws Exception {
        if (debug) {
            debug_message("# Attempting error recovery");
        }
        if (!find_recovery_config(debug)) {
            if (debug) {
                debug_message("# Error recovery fails");
                return false;
            }
            return false;
        }
        read_lookahead();
        while (true) {
            if (debug) {
                debug_message("# Trying to parse ahead");
            }
            if (!try_parse_ahead(debug)) {
                if (this.lookahead[0].sym == EOF_sym()) {
                    if (debug) {
                        debug_message("# Error recovery fails at EOF");
                        return false;
                    }
                    return false;
                }
                if (debug) {
                    debug_message("# Consuming Symbol #" + this.lookahead[0].sym);
                }
                restart_lookahead();
            } else {
                if (debug) {
                    debug_message("# Parse-ahead ok, going back to normal parse");
                }
                parse_lookahead(debug);
                return true;
            }
        }
    }

    protected boolean shift_under_error() {
        return get_action(((Symbol) this.stack.peek()).parse_state, error_sym()) > 0;
    }

    protected boolean find_recovery_config(boolean debug) {
        if (debug) {
            debug_message("# Finding recovery state on stack");
        }
        Symbol right = (Symbol) this.stack.peek();
        Symbol left = right;
        while (!shift_under_error()) {
            if (debug) {
                debug_message("# Pop stack by one, state was # " + ((Symbol) this.stack.peek()).parse_state);
            }
            left = (Symbol) this.stack.pop();
            this.tos--;
            if (this.stack.empty()) {
                if (debug) {
                    debug_message("# No recovery state found on stack");
                    return false;
                }
                return false;
            }
        }
        int act = get_action(((Symbol) this.stack.peek()).parse_state, error_sym());
        if (debug) {
            debug_message("# Recover state found (#" + ((Symbol) this.stack.peek()).parse_state + ")");
            debug_message("# Shifting on error to state #" + (act - 1));
        }
        Symbol error_token = getSymbolFactory().newSymbol("ERROR", error_sym(), left, right);
        error_token.parse_state = act - 1;
        error_token.used_by_parser = true;
        this.stack.push(error_token);
        this.tos++;
        return true;
    }

    protected void read_lookahead() throws Exception {
        this.lookahead = new Symbol[error_sync_size()];
        for (int i = 0; i < error_sync_size(); i++) {
            this.lookahead[i] = this.cur_token;
            this.cur_token = scan();
        }
        this.lookahead_pos = 0;
    }

    protected Symbol cur_err_token() {
        return this.lookahead[this.lookahead_pos];
    }

    protected boolean advance_lookahead() {
        this.lookahead_pos++;
        return this.lookahead_pos < error_sync_size();
    }

    protected void restart_lookahead() throws Exception {
        for (int i = 1; i < error_sync_size(); i++) {
            this.lookahead[i - 1] = this.lookahead[i];
        }
        this.lookahead[error_sync_size() - 1] = this.cur_token;
        this.cur_token = scan();
        this.lookahead_pos = 0;
    }

    protected boolean try_parse_ahead(boolean debug) throws Exception {
        virtual_parse_stack vstack = new virtual_parse_stack(this.stack);
        while (true) {
            int act = get_action(vstack.top(), cur_err_token().sym);
            if (act == 0) {
                return false;
            }
            if (act > 0) {
                vstack.push(act - 1);
                if (debug) {
                    debug_message("# Parse-ahead shifts Symbol #" + cur_err_token().sym + " into state #" + (act - 1));
                }
                if (!advance_lookahead()) {
                    return true;
                }
            } else if ((-act) - 1 == start_production()) {
                if (debug) {
                    debug_message("# Parse-ahead accepts");
                    return true;
                }
                return true;
            } else {
                short lhs = this.production_tab[(-act) - 1][0];
                short rhs_size = this.production_tab[(-act) - 1][1];
                for (int i = 0; i < rhs_size; i++) {
                    vstack.pop();
                }
                if (debug) {
                    debug_message("# Parse-ahead reduces: handle size = " + ((int) rhs_size) + " lhs = #" + ((int) lhs) + " from state #" + vstack.top());
                }
                vstack.push(get_reduce(vstack.top(), lhs));
                if (debug) {
                    debug_message("# Goto state #" + vstack.top());
                }
            }
        }
    }

    protected void parse_lookahead(boolean debug) throws Exception {
        Symbol lhs_sym = null;
        this.lookahead_pos = 0;
        if (debug) {
            debug_message("# Reparsing saved input with actions");
            debug_message("# Current Symbol is #" + cur_err_token().sym);
            debug_message("# Current state is #" + ((Symbol) this.stack.peek()).parse_state);
        }
        while (!this._done_parsing) {
            int act = get_action(((Symbol) this.stack.peek()).parse_state, cur_err_token().sym);
            if (act > 0) {
                cur_err_token().parse_state = act - 1;
                cur_err_token().used_by_parser = true;
                if (debug) {
                    debug_shift(cur_err_token());
                }
                this.stack.push(cur_err_token());
                this.tos++;
                if (!advance_lookahead()) {
                    if (debug) {
                        debug_message("# Completed reparse");
                        return;
                    }
                    return;
                } else if (debug) {
                    debug_message("# Current Symbol is #" + cur_err_token().sym);
                }
            } else if (act < 0) {
                lhs_sym = do_action((-act) - 1, this, this.stack, this.tos);
                short lhs_sym_num = this.production_tab[(-act) - 1][0];
                short handle_size = this.production_tab[(-act) - 1][1];
                if (debug) {
                    debug_reduce((-act) - 1, lhs_sym_num, handle_size);
                }
                for (int i = 0; i < handle_size; i++) {
                    this.stack.pop();
                    this.tos--;
                }
                int act2 = get_reduce(((Symbol) this.stack.peek()).parse_state, lhs_sym_num);
                lhs_sym.parse_state = act2;
                lhs_sym.used_by_parser = true;
                this.stack.push(lhs_sym);
                this.tos++;
                if (debug) {
                    debug_message("# Goto state #" + act2);
                }
            } else if (act == 0) {
                report_fatal_error("Syntax error", lhs_sym);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r0v9, types: [short[], short[][]] */
    public static short[][] unpackFromStrings(String[] sa) {
        StringBuffer sb = new StringBuffer(sa[0]);
        for (int i = 1; i < sa.length; i++) {
            sb.append(sa[i]);
        }
        int size1 = (sb.charAt(0) << 16) | sb.charAt(0 + 1);
        int n = 0 + 2;
        ?? r0 = new short[size1];
        for (int i2 = 0; i2 < size1; i2++) {
            int size2 = (sb.charAt(n) << 16) | sb.charAt(n + 1);
            n += 2;
            r0[i2] = new short[size2];
            for (int j = 0; j < size2; j++) {
                int i3 = n;
                n++;
                r0[i2][j] = (short) (sb.charAt(i3) - 2);
            }
        }
        return r0;
    }
}
