package java_cup;

import android.hardware.Camera;
import java.util.Hashtable;
import java.util.Stack;
import java_cup.runtime.Symbol;
import java_cup.runtime.lr_parser;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/CUP$parser$actions.class */
class CUP$parser$actions {
    protected non_terminal lhs_nt;
    private final parser parser;
    protected final int MAX_RHS = 200;
    protected production_part[] rhs_parts = new production_part[200];
    protected int rhs_pos = 0;
    protected String multipart_name = new String();
    protected Stack multipart_names = new Stack();
    protected Hashtable symbols = new Hashtable();
    protected Hashtable non_terms = new Hashtable();
    protected non_terminal start_nt = null;
    int _cur_prec = 0;
    int _cur_side = -1;
    private int cur_debug_id = 0;

    protected production_part add_lab(production_part part, String lab) throws internal_error {
        return (lab == null || part.is_action()) ? part : new symbol_part(((symbol_part) part).the_symbol(), lab);
    }

    protected void new_rhs() {
        this.rhs_pos = 0;
    }

    protected void add_rhs_part(production_part part) throws Exception {
        if (this.rhs_pos >= 200) {
            throw new Exception("Internal Error: Productions limited to 200 symbols and actions");
        }
        this.rhs_parts[this.rhs_pos] = part;
        this.rhs_pos++;
    }

    protected void update_precedence(int p) {
        this._cur_side = p;
        this._cur_prec++;
    }

    protected void add_precedence(String term) {
        if (term == null) {
            System.err.println("Unable to add precedence to nonexistent terminal");
            return;
        }
        symbol_part sp = (symbol_part) this.symbols.get(term);
        if (sp == null) {
            System.err.println("Could find terminal " + term + " while declaring precedence");
            return;
        }
        symbol sym = sp.the_symbol();
        if (sym instanceof terminal) {
            ((terminal) sym).set_precedence(this._cur_side, this._cur_prec);
        } else {
            System.err.println("Precedence declaration: Can't find terminal " + term);
        }
    }

    public int get_new_debug_id() {
        int i = this.cur_debug_id;
        this.cur_debug_id = i + 1;
        return i;
    }

    public String attach_debug_symbol(int id, String code) {
        if (!this.parser.debugSymbols) {
            return code;
        }
        return "//@@CUPDBG" + id + "\n" + code;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CUP$parser$actions(parser parser) {
        this.parser = parser;
    }

    public final Symbol CUP$parser$do_action(int CUP$parser$act_num, lr_parser CUP$parser$parser, Stack CUP$parser$stack, int CUP$parser$top) throws Exception {
        symbol sym;
        switch (CUP$parser$act_num) {
            case 0:
                int i = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i2 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Object start_val = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Symbol CUP$parser$result = this.parser.getSymbolFactory().newSymbol("$START", 0, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), start_val);
                CUP$parser$parser.done_parsing();
                return CUP$parser$result;
            case 1:
                this.symbols.put("error", new symbol_part(terminal.error));
                this.non_terms.put("$START", non_terminal.START_nt);
                Symbol CUP$parser$result2 = this.parser.getSymbolFactory().newSymbol("NT$0", 46, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result2;
            case 2:
                Symbol CUP$parser$result3 = this.parser.getSymbolFactory().newSymbol("spec", 0, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 8), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result3;
            case 3:
                Symbol CUP$parser$result4 = this.parser.getSymbolFactory().newSymbol("spec", 0, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result4;
            case 4:
                emit.package_name = this.multipart_name;
                this.multipart_name = new String();
                Symbol CUP$parser$result5 = this.parser.getSymbolFactory().newSymbol("NT$1", 47, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result5;
            case 5:
                Object RESULT = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Symbol CUP$parser$result6 = this.parser.getSymbolFactory().newSymbol("package_spec", 1, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), RESULT);
                return CUP$parser$result6;
            case 6:
                Symbol CUP$parser$result7 = this.parser.getSymbolFactory().newSymbol("package_spec", 1, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result7;
            case 7:
                Symbol CUP$parser$result8 = this.parser.getSymbolFactory().newSymbol("import_list", 2, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result8;
            case 8:
                Symbol CUP$parser$result9 = this.parser.getSymbolFactory().newSymbol("import_list", 2, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result9;
            case 9:
                emit.import_list.push(this.multipart_name);
                this.multipart_name = new String();
                Symbol CUP$parser$result10 = this.parser.getSymbolFactory().newSymbol("NT$2", 48, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result10;
            case 10:
                Object RESULT2 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Symbol CUP$parser$result11 = this.parser.getSymbolFactory().newSymbol("import_spec", 13, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), RESULT2);
                return CUP$parser$result11;
            case 11:
                Symbol CUP$parser$result12 = this.parser.getSymbolFactory().newSymbol("class_name", 35, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result12;
            case 12:
                int i3 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i4 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String id = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                emit.parser_class_name = id;
                emit.symbol_const_class_name = String.valueOf(id) + "Sym";
                Symbol CUP$parser$result13 = this.parser.getSymbolFactory().newSymbol("class_name", 35, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result13;
            case 13:
                Symbol CUP$parser$result14 = this.parser.getSymbolFactory().newSymbol("code_part", 5, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result14;
            case 14:
                Symbol CUP$parser$result15 = this.parser.getSymbolFactory().newSymbol("code_part", 5, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result15;
            case 15:
                Symbol CUP$parser$result16 = this.parser.getSymbolFactory().newSymbol("code_part", 5, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result16;
            case 16:
                Symbol CUP$parser$result17 = this.parser.getSymbolFactory().newSymbol("code_part", 5, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result17;
            case 17:
                Symbol CUP$parser$result18 = this.parser.getSymbolFactory().newSymbol("code_parts", 4, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result18;
            case 18:
                Symbol CUP$parser$result19 = this.parser.getSymbolFactory().newSymbol("code_parts", 4, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result19;
            case 19:
                int i5 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i6 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String user_code = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                if (emit.action_code != null) {
                    ErrorManager.getManager().emit_warning("Redundant action code (skipping)");
                } else {
                    emit.action_code = attach_debug_symbol(get_new_debug_id(), user_code);
                }
                Symbol CUP$parser$result20 = this.parser.getSymbolFactory().newSymbol("action_code_part", 3, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result20;
            case 20:
                int i7 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i8 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String user_code2 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                if (emit.parser_code != null) {
                    ErrorManager.getManager().emit_warning("Redundant parser code (skipping)");
                } else {
                    emit.parser_code = attach_debug_symbol(get_new_debug_id(), user_code2);
                }
                Symbol CUP$parser$result21 = this.parser.getSymbolFactory().newSymbol("parser_code_part", 8, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result21;
            case 21:
                int i9 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i10 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String user_code3 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                if (emit.init_code != null) {
                    ErrorManager.getManager().emit_warning("Redundant init code (skipping)");
                } else {
                    emit.init_code = attach_debug_symbol(get_new_debug_id(), user_code3);
                }
                Symbol CUP$parser$result22 = this.parser.getSymbolFactory().newSymbol("init_code", 15, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result22;
            case 22:
                int i11 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i12 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String user_code4 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                if (emit.scan_code != null) {
                    ErrorManager.getManager().emit_warning("Redundant scan code (skipping)");
                } else {
                    emit.scan_code = attach_debug_symbol(get_new_debug_id(), user_code4);
                }
                Symbol CUP$parser$result23 = this.parser.getSymbolFactory().newSymbol("scan_code", 16, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result23;
            case 23:
                Symbol CUP$parser$result24 = this.parser.getSymbolFactory().newSymbol("symbol_list", 9, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result24;
            case 24:
                Symbol CUP$parser$result25 = this.parser.getSymbolFactory().newSymbol("symbol_list", 9, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result25;
            case 25:
                Symbol CUP$parser$result26 = this.parser.getSymbolFactory().newSymbol("symbol", 17, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result26;
            case 26:
                Symbol CUP$parser$result27 = this.parser.getSymbolFactory().newSymbol("symbol", 17, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result27;
            case 27:
                Symbol CUP$parser$result28 = this.parser.getSymbolFactory().newSymbol("symbol", 17, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result28;
            case 28:
                Symbol CUP$parser$result29 = this.parser.getSymbolFactory().newSymbol("symbol", 17, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result29;
            case 29:
                this.multipart_name = new String();
                Symbol CUP$parser$result30 = this.parser.getSymbolFactory().newSymbol("NT$3", 49, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result30;
            case 30:
                Object RESULT3 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Symbol CUP$parser$result31 = this.parser.getSymbolFactory().newSymbol("symbol", 17, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), RESULT3);
                return CUP$parser$result31;
            case 31:
                this.multipart_name = new String();
                Symbol CUP$parser$result32 = this.parser.getSymbolFactory().newSymbol("NT$4", 50, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result32;
            case 32:
                Object RESULT4 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Symbol CUP$parser$result33 = this.parser.getSymbolFactory().newSymbol("symbol", 17, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), RESULT4);
                return CUP$parser$result33;
            case 33:
                this.multipart_name = new String();
                Symbol CUP$parser$result34 = this.parser.getSymbolFactory().newSymbol("NT$5", 51, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result34;
            case 34:
                Object RESULT5 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Symbol CUP$parser$result35 = this.parser.getSymbolFactory().newSymbol("declares_term", 33, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), RESULT5);
                return CUP$parser$result35;
            case 35:
                this.multipart_name = new String();
                Symbol CUP$parser$result36 = this.parser.getSymbolFactory().newSymbol("NT$6", 52, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result36;
            case 36:
                Object RESULT6 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Symbol CUP$parser$result37 = this.parser.getSymbolFactory().newSymbol("declares_non_term", 34, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), RESULT6);
                return CUP$parser$result37;
            case 37:
                Symbol CUP$parser$result38 = this.parser.getSymbolFactory().newSymbol("term_name_list", 19, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result38;
            case 38:
                Symbol CUP$parser$result39 = this.parser.getSymbolFactory().newSymbol("term_name_list", 19, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result39;
            case 39:
                Symbol CUP$parser$result40 = this.parser.getSymbolFactory().newSymbol("non_term_name_list", 20, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result40;
            case 40:
                Symbol CUP$parser$result41 = this.parser.getSymbolFactory().newSymbol("non_term_name_list", 20, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result41;
            case 41:
                Symbol CUP$parser$result42 = this.parser.getSymbolFactory().newSymbol("precedence_list", 29, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result42;
            case 42:
                Symbol CUP$parser$result43 = this.parser.getSymbolFactory().newSymbol("precedence_list", 29, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result43;
            case 43:
                Symbol CUP$parser$result44 = this.parser.getSymbolFactory().newSymbol("precedence_l", 32, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result44;
            case 44:
                Symbol CUP$parser$result45 = this.parser.getSymbolFactory().newSymbol("precedence_l", 32, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result45;
            case 45:
                update_precedence(0);
                Symbol CUP$parser$result46 = this.parser.getSymbolFactory().newSymbol("NT$7", 53, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result46;
            case 46:
                Object RESULT7 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                Symbol CUP$parser$result47 = this.parser.getSymbolFactory().newSymbol("preced", 30, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), RESULT7);
                return CUP$parser$result47;
            case 47:
                update_precedence(1);
                Symbol CUP$parser$result48 = this.parser.getSymbolFactory().newSymbol("NT$8", 54, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result48;
            case 48:
                Object RESULT8 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                Symbol CUP$parser$result49 = this.parser.getSymbolFactory().newSymbol("preced", 30, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), RESULT8);
                return CUP$parser$result49;
            case 49:
                update_precedence(2);
                Symbol CUP$parser$result50 = this.parser.getSymbolFactory().newSymbol("NT$9", 55, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result50;
            case 50:
                Object RESULT9 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                Symbol CUP$parser$result51 = this.parser.getSymbolFactory().newSymbol("preced", 30, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), RESULT9);
                return CUP$parser$result51;
            case 51:
                Symbol CUP$parser$result52 = this.parser.getSymbolFactory().newSymbol("terminal_list", 31, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result52;
            case 52:
                Symbol CUP$parser$result53 = this.parser.getSymbolFactory().newSymbol("terminal_list", 31, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result53;
            case 53:
                int i13 = ((Symbol) CUP$parser$stack.peek()).left;
                int i14 = ((Symbol) CUP$parser$stack.peek()).right;
                String sym2 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                add_precedence(sym2);
                Symbol CUP$parser$result54 = this.parser.getSymbolFactory().newSymbol("terminal_id", 40, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), sym2);
                return CUP$parser$result54;
            case 54:
                int i15 = ((Symbol) CUP$parser$stack.peek()).left;
                int i16 = ((Symbol) CUP$parser$stack.peek()).right;
                String sym3 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                if (this.symbols.get(sym3) == null) {
                    ErrorManager.getManager().emit_error("Terminal \"" + sym3 + "\" has not been declared");
                }
                Symbol CUP$parser$result55 = this.parser.getSymbolFactory().newSymbol("term_id", 41, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), sym3);
                return CUP$parser$result55;
            case 55:
                int i17 = ((Symbol) CUP$parser$stack.peek()).left;
                int i18 = ((Symbol) CUP$parser$stack.peek()).right;
                String start_name = (String) ((Symbol) CUP$parser$stack.peek()).value;
                non_terminal nt = (non_terminal) this.non_terms.get(start_name);
                if (nt == null) {
                    ErrorManager.getManager().emit_error("Start non terminal \"" + start_name + "\" has not been declared");
                } else {
                    this.start_nt = nt;
                    new_rhs();
                    add_rhs_part(add_lab(new symbol_part(this.start_nt), "start_val"));
                    add_rhs_part(new symbol_part(terminal.EOF));
                    if (!emit._xmlactions) {
                        add_rhs_part(new action_part("RESULT = start_val;"));
                    }
                    emit.start_production = new production(non_terminal.START_nt, this.rhs_parts, this.rhs_pos);
                    new_rhs();
                }
                Symbol CUP$parser$result56 = this.parser.getSymbolFactory().newSymbol("NT$10", 56, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result56;
            case 56:
                Object RESULT10 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i19 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i20 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String str = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                Symbol CUP$parser$result57 = this.parser.getSymbolFactory().newSymbol("start_spec", 10, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), RESULT10);
                return CUP$parser$result57;
            case 57:
                Symbol CUP$parser$result58 = this.parser.getSymbolFactory().newSymbol("start_spec", 10, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result58;
            case 58:
                Symbol CUP$parser$result59 = this.parser.getSymbolFactory().newSymbol("production_list", 11, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result59;
            case 59:
                Symbol CUP$parser$result60 = this.parser.getSymbolFactory().newSymbol("production_list", 11, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result60;
            case 60:
                int i21 = ((Symbol) CUP$parser$stack.peek()).left;
                int i22 = ((Symbol) CUP$parser$stack.peek()).right;
                String lhs_id = (String) ((Symbol) CUP$parser$stack.peek()).value;
                this.lhs_nt = (non_terminal) this.non_terms.get(lhs_id);
                if (this.lhs_nt == null && ErrorManager.getManager().getErrorCount() == 0) {
                    ErrorManager.getManager().emit_warning("LHS non terminal \"" + lhs_id + "\" has not been declared");
                }
                new_rhs();
                Symbol CUP$parser$result61 = this.parser.getSymbolFactory().newSymbol("NT$11", 57, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result61;
            case 61:
                Object RESULT11 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
                int i23 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).left;
                int i24 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).right;
                String str2 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).value;
                Symbol CUP$parser$result62 = this.parser.getSymbolFactory().newSymbol("production", 21, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), RESULT11);
                return CUP$parser$result62;
            case 62:
                ErrorManager.getManager().emit_error("Syntax Error");
                Symbol CUP$parser$result63 = this.parser.getSymbolFactory().newSymbol("NT$12", 58, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result63;
            case 63:
                Object RESULT12 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Symbol CUP$parser$result64 = this.parser.getSymbolFactory().newSymbol("production", 21, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), RESULT12);
                return CUP$parser$result64;
            case 64:
                Symbol CUP$parser$result65 = this.parser.getSymbolFactory().newSymbol("rhs_list", 26, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result65;
            case 65:
                Symbol CUP$parser$result66 = this.parser.getSymbolFactory().newSymbol("rhs_list", 26, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result66;
            case 66:
                int i25 = ((Symbol) CUP$parser$stack.peek()).left;
                int i26 = ((Symbol) CUP$parser$stack.peek()).right;
                String term_name = (String) ((Symbol) CUP$parser$stack.peek()).value;
                if (this.lhs_nt != null) {
                    if (term_name == null) {
                        System.err.println("No terminal for contextual precedence");
                        sym = null;
                    } else {
                        sym = ((symbol_part) this.symbols.get(term_name)).the_symbol();
                    }
                    if (sym != null && (sym instanceof terminal)) {
                        new production(this.lhs_nt, this.rhs_parts, this.rhs_pos, ((terminal) sym).precedence_num(), ((terminal) sym).precedence_side());
                        ((symbol_part) this.symbols.get(term_name)).the_symbol().note_use();
                    } else {
                        System.err.println("Invalid terminal " + term_name + " for contextual precedence assignment");
                        new production(this.lhs_nt, this.rhs_parts, this.rhs_pos);
                    }
                    if (this.start_nt == null) {
                        this.start_nt = this.lhs_nt;
                        new_rhs();
                        add_rhs_part(add_lab(new symbol_part(this.start_nt), "start_val"));
                        add_rhs_part(new symbol_part(terminal.EOF));
                        if (!emit._xmlactions) {
                            add_rhs_part(new action_part("RESULT = start_val;"));
                        }
                        if (sym != null && (sym instanceof terminal)) {
                            emit.start_production = new production(non_terminal.START_nt, this.rhs_parts, this.rhs_pos, ((terminal) sym).precedence_num(), ((terminal) sym).precedence_side());
                        } else {
                            emit.start_production = new production(non_terminal.START_nt, this.rhs_parts, this.rhs_pos);
                        }
                        new_rhs();
                    }
                }
                new_rhs();
                Symbol CUP$parser$result67 = this.parser.getSymbolFactory().newSymbol("rhs", 27, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result67;
            case 67:
                if (this.lhs_nt != null) {
                    new production(this.lhs_nt, this.rhs_parts, this.rhs_pos);
                    if (this.start_nt == null) {
                        this.start_nt = this.lhs_nt;
                        new_rhs();
                        add_rhs_part(add_lab(new symbol_part(this.start_nt), "start_val"));
                        add_rhs_part(new symbol_part(terminal.EOF));
                        if (!emit._xmlactions) {
                            add_rhs_part(new action_part("RESULT = start_val;"));
                        }
                        emit.start_production = new production(non_terminal.START_nt, this.rhs_parts, this.rhs_pos);
                        new_rhs();
                    }
                }
                new_rhs();
                Symbol CUP$parser$result68 = this.parser.getSymbolFactory().newSymbol("rhs", 27, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result68;
            case 68:
                Symbol CUP$parser$result69 = this.parser.getSymbolFactory().newSymbol("prod_part_list", 22, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result69;
            case 69:
                Symbol CUP$parser$result70 = this.parser.getSymbolFactory().newSymbol("prod_part_list", 22, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result70;
            case 70:
                int i27 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i28 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String symid = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i29 = ((Symbol) CUP$parser$stack.peek()).left;
                int i30 = ((Symbol) CUP$parser$stack.peek()).right;
                String labid = (String) ((Symbol) CUP$parser$stack.peek()).value;
                production_part symb = (production_part) this.symbols.get(symid);
                if (symb == null) {
                    if (ErrorManager.getManager().getErrorCount() == 0) {
                        ErrorManager.getManager().emit_error("java_cup.runtime.Symbol \"" + symid + "\" has not been declared");
                    }
                } else {
                    add_rhs_part(add_lab(symb, labid));
                }
                Symbol CUP$parser$result71 = this.parser.getSymbolFactory().newSymbol("prod_part", 23, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result71;
            case 71:
                int i31 = ((Symbol) CUP$parser$stack.peek()).left;
                int i32 = ((Symbol) CUP$parser$stack.peek()).right;
                String code_str = (String) ((Symbol) CUP$parser$stack.peek()).value;
                add_rhs_part(new action_part(attach_debug_symbol(get_new_debug_id(), code_str)));
                Symbol CUP$parser$result72 = this.parser.getSymbolFactory().newSymbol("prod_part", 23, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result72;
            case 72:
                int i33 = ((Symbol) CUP$parser$stack.peek()).left;
                int i34 = ((Symbol) CUP$parser$stack.peek()).right;
                String labid2 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                Symbol CUP$parser$result73 = this.parser.getSymbolFactory().newSymbol("opt_label", 39, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), labid2);
                return CUP$parser$result73;
            case 73:
                Symbol CUP$parser$result74 = this.parser.getSymbolFactory().newSymbol("opt_label", 39, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result74;
            case 74:
                int i35 = ((Symbol) CUP$parser$stack.peek()).left;
                int i36 = ((Symbol) CUP$parser$stack.peek()).right;
                String another_id = (String) ((Symbol) CUP$parser$stack.peek()).value;
                this.multipart_name = this.multipart_name.concat("." + another_id);
                Symbol CUP$parser$result75 = this.parser.getSymbolFactory().newSymbol("multipart_id", 12, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result75;
            case 75:
                this.multipart_names.push(this.multipart_name);
                this.multipart_name = "";
                Symbol CUP$parser$result76 = this.parser.getSymbolFactory().newSymbol("NT$13", 59, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result76;
            case 76:
                Object RESULT13 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
                int i37 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i38 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String types = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                this.multipart_name = ((String) this.multipart_names.pop()).concat("<" + types + ">");
                Symbol CUP$parser$result77 = this.parser.getSymbolFactory().newSymbol("multipart_id", 12, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), RESULT13);
                return CUP$parser$result77;
            case 77:
                int i39 = ((Symbol) CUP$parser$stack.peek()).left;
                int i40 = ((Symbol) CUP$parser$stack.peek()).right;
                String an_id = (String) ((Symbol) CUP$parser$stack.peek()).value;
                this.multipart_name = this.multipart_name.concat(an_id);
                Symbol CUP$parser$result78 = this.parser.getSymbolFactory().newSymbol("multipart_id", 12, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result78;
            case 78:
                int i41 = ((Symbol) CUP$parser$stack.peek()).left;
                int i42 = ((Symbol) CUP$parser$stack.peek()).right;
                String arg = (String) ((Symbol) CUP$parser$stack.peek()).value;
                Symbol CUP$parser$result79 = this.parser.getSymbolFactory().newSymbol("typearglist", 43, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), arg);
                return CUP$parser$result79;
            case 79:
                int i43 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i44 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String list = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i45 = ((Symbol) CUP$parser$stack.peek()).left;
                int i46 = ((Symbol) CUP$parser$stack.peek()).right;
                String arg2 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                String RESULT14 = String.valueOf(list) + "," + arg2;
                Symbol CUP$parser$result80 = this.parser.getSymbolFactory().newSymbol("typearglist", 43, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), RESULT14);
                return CUP$parser$result80;
            case 80:
                String RESULT15 = this.multipart_name;
                this.multipart_name = new String();
                Symbol CUP$parser$result81 = this.parser.getSymbolFactory().newSymbol("typearguement", 44, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), RESULT15);
                return CUP$parser$result81;
            case 81:
                int i47 = ((Symbol) CUP$parser$stack.peek()).left;
                int i48 = ((Symbol) CUP$parser$stack.peek()).right;
                String w = (String) ((Symbol) CUP$parser$stack.peek()).value;
                Symbol CUP$parser$result82 = this.parser.getSymbolFactory().newSymbol("typearguement", 44, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), w);
                return CUP$parser$result82;
            case 82:
                Symbol CUP$parser$result83 = this.parser.getSymbolFactory().newSymbol("wildcard", 45, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), " ? ");
                return CUP$parser$result83;
            case 83:
                String RESULT16 = " ? extends " + this.multipart_name;
                this.multipart_name = new String();
                Symbol CUP$parser$result84 = this.parser.getSymbolFactory().newSymbol("wildcard", 45, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), RESULT16);
                return CUP$parser$result84;
            case 84:
                String RESULT17 = " ? super " + this.multipart_name;
                this.multipart_name = new String();
                Symbol CUP$parser$result85 = this.parser.getSymbolFactory().newSymbol("wildcard", 45, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), RESULT17);
                return CUP$parser$result85;
            case 85:
                this.multipart_name = this.multipart_name.concat(".*");
                Symbol CUP$parser$result86 = this.parser.getSymbolFactory().newSymbol("import_id", 14, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result86;
            case 86:
                Symbol CUP$parser$result87 = this.parser.getSymbolFactory().newSymbol("import_id", 14, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result87;
            case 87:
                Symbol CUP$parser$result88 = this.parser.getSymbolFactory().newSymbol("type_id", 18, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result88;
            case 88:
                this.multipart_name = this.multipart_name.concat("[]");
                Symbol CUP$parser$result89 = this.parser.getSymbolFactory().newSymbol("type_id", 18, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result89;
            case 89:
                int i49 = ((Symbol) CUP$parser$stack.peek()).left;
                int i50 = ((Symbol) CUP$parser$stack.peek()).right;
                String term_id = (String) ((Symbol) CUP$parser$stack.peek()).value;
                if (this.symbols.get(term_id) != null) {
                    ErrorManager.getManager().emit_error("java_cup.runtime.Symbol \"" + term_id + "\" has already been declared");
                } else {
                    if (this.multipart_name.equals("")) {
                        this.multipart_name = "Object";
                    }
                    this.symbols.put(term_id, new symbol_part(new terminal(term_id, this.multipart_name)));
                }
                Symbol CUP$parser$result90 = this.parser.getSymbolFactory().newSymbol("new_term_id", 24, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result90;
            case 90:
                int i51 = ((Symbol) CUP$parser$stack.peek()).left;
                int i52 = ((Symbol) CUP$parser$stack.peek()).right;
                String non_term_id = (String) ((Symbol) CUP$parser$stack.peek()).value;
                if (this.symbols.get(non_term_id) != null) {
                    ErrorManager.getManager().emit_error("java_cup.runtime.Symbol \"" + non_term_id + "\" has already been declared");
                } else {
                    if (this.multipart_name.equals("")) {
                        this.multipart_name = "Object";
                    }
                    non_terminal this_nt = new non_terminal(non_term_id, this.multipart_name);
                    this.non_terms.put(non_term_id, this_nt);
                    this.symbols.put(non_term_id, new symbol_part(this_nt));
                }
                Symbol CUP$parser$result91 = this.parser.getSymbolFactory().newSymbol("new_non_term_id", 25, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result91;
            case 91:
                int i53 = ((Symbol) CUP$parser$stack.peek()).left;
                int i54 = ((Symbol) CUP$parser$stack.peek()).right;
                String the_id = (String) ((Symbol) CUP$parser$stack.peek()).value;
                Symbol CUP$parser$result92 = this.parser.getSymbolFactory().newSymbol("nt_id", 36, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), the_id);
                return CUP$parser$result92;
            case 92:
                ErrorManager.getManager().emit_error("Illegal use of reserved word");
                Symbol CUP$parser$result93 = this.parser.getSymbolFactory().newSymbol("nt_id", 36, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ILLEGAL");
                return CUP$parser$result93;
            case 93:
                int i55 = ((Symbol) CUP$parser$stack.peek()).left;
                int i56 = ((Symbol) CUP$parser$stack.peek()).right;
                String the_id2 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                Symbol CUP$parser$result94 = this.parser.getSymbolFactory().newSymbol("symbol_id", 37, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), the_id2);
                return CUP$parser$result94;
            case 94:
                ErrorManager.getManager().emit_error("Illegal use of reserved word");
                Symbol CUP$parser$result95 = this.parser.getSymbolFactory().newSymbol("symbol_id", 37, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ILLEGAL");
                return CUP$parser$result95;
            case 95:
                int i57 = ((Symbol) CUP$parser$stack.peek()).left;
                int i58 = ((Symbol) CUP$parser$stack.peek()).right;
                String the_id3 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                Symbol CUP$parser$result96 = this.parser.getSymbolFactory().newSymbol("label_id", 38, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), the_id3);
                return CUP$parser$result96;
            case 96:
                int i59 = ((Symbol) CUP$parser$stack.peek()).left;
                int i60 = ((Symbol) CUP$parser$stack.peek()).right;
                String the_id4 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                Symbol CUP$parser$result97 = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), the_id4);
                return CUP$parser$result97;
            case 97:
                Symbol CUP$parser$result98 = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "code");
                return CUP$parser$result98;
            case 98:
                Symbol CUP$parser$result99 = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), Camera.Parameters.SCENE_MODE_ACTION);
                return CUP$parser$result99;
            case 99:
                Symbol CUP$parser$result100 = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "parser");
                return CUP$parser$result100;
            case 100:
                Symbol CUP$parser$result101 = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "terminal");
                return CUP$parser$result101;
            case 101:
                Symbol CUP$parser$result102 = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "non");
                return CUP$parser$result102;
            case 102:
                Symbol CUP$parser$result103 = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "nonterminal");
                return CUP$parser$result103;
            case 103:
                Symbol CUP$parser$result104 = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "init");
                return CUP$parser$result104;
            case 104:
                Symbol CUP$parser$result105 = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "scan");
                return CUP$parser$result105;
            case 105:
                Symbol CUP$parser$result106 = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), Jimple.WITH);
                return CUP$parser$result106;
            case 106:
                Symbol CUP$parser$result107 = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "start");
                return CUP$parser$result107;
            case 107:
                Symbol CUP$parser$result108 = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "precedence");
                return CUP$parser$result108;
            case 108:
                Symbol CUP$parser$result109 = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "left");
                return CUP$parser$result109;
            case 109:
                Symbol CUP$parser$result110 = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "right");
                return CUP$parser$result110;
            case 110:
                Symbol CUP$parser$result111 = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "nonassoc");
                return CUP$parser$result111;
            case 111:
                ErrorManager.getManager().emit_error("Illegal use of reserved word");
                Symbol CUP$parser$result112 = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ILLEGAL");
                return CUP$parser$result112;
            case 112:
                Symbol CUP$parser$result113 = this.parser.getSymbolFactory().newSymbol("non_terminal", 7, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result113;
            case 113:
                Symbol CUP$parser$result114 = this.parser.getSymbolFactory().newSymbol("non_terminal", 7, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result114;
            case 114:
                Symbol CUP$parser$result115 = this.parser.getSymbolFactory().newSymbol("opt_semi", 6, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result115;
            case 115:
                Symbol CUP$parser$result116 = this.parser.getSymbolFactory().newSymbol("opt_semi", 6, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result116;
            case 116:
                Symbol CUP$parser$result117 = this.parser.getSymbolFactory().newSymbol("empty", 28, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result117;
            default:
                throw new Exception("Invalid action number found in internal parse table");
        }
    }
}
