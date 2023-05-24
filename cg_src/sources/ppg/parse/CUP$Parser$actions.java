package ppg.parse;

import java.util.Stack;
import java.util.Vector;
import java_cup.runtime.Symbol;
import java_cup.runtime.lr_parser;
import ppg.atoms.Nonterminal;
import ppg.atoms.Precedence;
import ppg.atoms.PrecedenceModifier;
import ppg.atoms.Production;
import ppg.atoms.SemanticAction;
import ppg.atoms.SymbolList;
import ppg.cmds.Command;
import ppg.cmds.DropCmd;
import ppg.cmds.ExtendCmd;
import ppg.cmds.NewProdCmd;
import ppg.cmds.OverrideCmd;
import ppg.cmds.TransferCmd;
import ppg.code.ActionCode;
import ppg.code.InitCode;
import ppg.code.ParserCode;
import ppg.code.ScanCode;
import ppg.lex.Token;
import ppg.spec.CUPSpec;
import ppg.spec.PPGSpec;
import ppg.spec.Spec;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/parse/CUP$Parser$actions.class */
class CUP$Parser$actions {
    private final Parser parser;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CUP$Parser$actions(Parser parser) {
        this.parser = parser;
    }

    public final Symbol CUP$Parser$do_action(int CUP$Parser$act_num, lr_parser CUP$Parser$parser, Stack CUP$Parser$stack, int CUP$Parser$top) throws Exception {
        Command RESULT;
        switch (CUP$Parser$act_num) {
            case 0:
                int i = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i2 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Spec start_val = (Spec) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                Symbol CUP$Parser$result = new Symbol(0, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, start_val);
                CUP$Parser$parser.done_parsing();
                return CUP$Parser$result;
            case 1:
                int i3 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i4 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Spec s = (Spec) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Parser.setProgramNode(s);
                Symbol CUP$Parser$result2 = new Symbol(12, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, (Object) null);
                return CUP$Parser$result2;
            case 2:
                int i5 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i6 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Spec s2 = (Spec) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Parser.setProgramNode(s2);
                Symbol CUP$Parser$result3 = new Symbol(12, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, (Object) null);
                return CUP$Parser$result3;
            case 3:
                int i7 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 7)).left;
                int i8 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 7)).right;
                String inc = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 7)).value;
                int i9 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 6)).left;
                int i10 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 6)).right;
                String pkg = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 6)).value;
                int i11 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).left;
                int i12 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).right;
                Vector imp = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).value;
                int i13 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 4)).left;
                int i14 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 4)).right;
                Vector code = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 4)).value;
                int i15 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left;
                int i16 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).right;
                Vector sym_list = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).value;
                int i17 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                int i18 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                Vector prec_list = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                int i19 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i20 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Vector start = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                int i21 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i22 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Vector cmd_list = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Spec RESULT2 = new PPGSpec(inc, pkg, imp, code, sym_list, prec_list, start, cmd_list);
                Symbol CUP$Parser$result4 = new Symbol(13, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 7)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT2);
                return CUP$Parser$result4;
            case 4:
                int i23 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i24 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Token a = (Token) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                String RESULT3 = (String) a.getValue();
                Symbol CUP$Parser$result5 = new Symbol(16, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT3);
                return CUP$Parser$result5;
            case 5:
                int i25 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i26 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Vector sym_list2 = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result6 = new Symbol(50, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, sym_list2);
                return CUP$Parser$result6;
            case 6:
                Symbol CUP$Parser$result7 = new Symbol(50, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, (Object) null);
                return CUP$Parser$result7;
            case 7:
                int i27 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i28 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Vector p_l = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result8 = new Symbol(23, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, p_l);
                return CUP$Parser$result8;
            case 8:
                Symbol CUP$Parser$result9 = new Symbol(23, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, (Object) null);
                return CUP$Parser$result9;
            case 9:
                int i29 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i30 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                String start2 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                Vector v = new Vector();
                v.addElement(start2);
                Symbol CUP$Parser$result10 = new Symbol(24, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, v);
                return CUP$Parser$result10;
            case 10:
                int i31 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i32 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Symbol CUP$Parser$result11 = new Symbol(24, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value);
                return CUP$Parser$result11;
            case 11:
                int i33 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i34 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Vector list = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                int i35 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i36 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Vector elt = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                list.addAll(elt);
                Symbol CUP$Parser$result12 = new Symbol(26, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, list);
                return CUP$Parser$result12;
            case 12:
                int i37 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i38 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Vector elt2 = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result13 = new Symbol(26, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, elt2);
                return CUP$Parser$result13;
            case 13:
                int i39 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                int i40 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                String start3 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                int i41 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i42 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                String method = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                Vector m = new Vector();
                m.addElement(start3);
                m.addElement(method);
                Symbol CUP$Parser$result14 = new Symbol(25, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 4)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, m);
                return CUP$Parser$result14;
            case 14:
                int i43 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i44 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Vector b = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                int i45 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i46 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Command a2 = (Command) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                b.addElement(a2);
                Symbol CUP$Parser$result15 = new Symbol(19, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, b);
                return CUP$Parser$result15;
            case 15:
                Vector RESULT4 = new Vector();
                Symbol CUP$Parser$result16 = new Symbol(19, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT4);
                return CUP$Parser$result16;
            case 16:
                int i47 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i48 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Object mod = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                int i49 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i50 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Production p = (Production) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                if (mod == null) {
                    RESULT = new NewProdCmd(p);
                } else if (mod.equals("extend")) {
                    RESULT = new ExtendCmd(p);
                } else {
                    RESULT = new OverrideCmd(p);
                }
                Symbol CUP$Parser$result17 = new Symbol(28, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                return CUP$Parser$result17;
            case 17:
                int i51 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i52 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Command RESULT5 = new DropCmd((Production) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value);
                Symbol CUP$Parser$result18 = new Symbol(28, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT5);
                return CUP$Parser$result18;
            case 18:
                int i53 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i54 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Vector nt_list = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                Command RESULT6 = new DropCmd(nt_list);
                Symbol CUP$Parser$result19 = new Symbol(28, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT6);
                return CUP$Parser$result19;
            case 19:
                int i55 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i56 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                String nt = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                int i57 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i58 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Vector tlist = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Command RESULT7 = new TransferCmd(nt, tlist);
                Symbol CUP$Parser$result20 = new Symbol(28, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT7);
                return CUP$Parser$result20;
            case 20:
                int i59 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                int i60 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                Vector list2 = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                int i61 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i62 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String id = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                list2.addElement(id);
                Symbol CUP$Parser$result21 = new Symbol(27, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, list2);
                return CUP$Parser$result21;
            case 21:
                int i63 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i64 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String id2 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Vector list3 = new Vector();
                list3.addElement(id2);
                Symbol CUP$Parser$result22 = new Symbol(27, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, list3);
                return CUP$Parser$result22;
            case 22:
                int i65 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i66 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Token a3 = (Token) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Object RESULT8 = (String) a3.getValue();
                Symbol CUP$Parser$result23 = new Symbol(1, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT8);
                return CUP$Parser$result23;
            case 23:
                int i67 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i68 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Token a4 = (Token) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Object RESULT9 = (String) a4.getValue();
                Symbol CUP$Parser$result24 = new Symbol(1, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT9);
                return CUP$Parser$result24;
            case 24:
                Symbol CUP$Parser$result25 = new Symbol(1, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, (Object) null);
                return CUP$Parser$result25;
            case 25:
                int i69 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).left;
                int i70 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).right;
                Vector tl = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).value;
                int i71 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left;
                int i72 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).right;
                String nt2 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).value;
                int i73 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i74 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Vector rhs = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                tl.addElement(new Production(new Nonterminal(nt2), rhs));
                Symbol CUP$Parser$result26 = new Symbol(22, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, tl);
                return CUP$Parser$result26;
            case 26:
                int i75 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left;
                int i76 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).right;
                String nt3 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).value;
                int i77 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i78 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Vector rhs2 = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                Vector v2 = new Vector();
                v2.addElement(new Production(new Nonterminal(nt3), rhs2));
                Symbol CUP$Parser$result27 = new Symbol(22, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 4)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, v2);
                return CUP$Parser$result27;
            case 27:
                int i79 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i80 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Vector p_list = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                int i81 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i82 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                p_list.addElement((Production) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value);
                Symbol CUP$Parser$result28 = new Symbol(30, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, p_list);
                return CUP$Parser$result28;
            case 28:
                int i83 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i84 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Production p2 = (Production) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Vector p_list2 = new Vector();
                p_list2.addElement(p2);
                Symbol CUP$Parser$result29 = new Symbol(30, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, p_list2);
                return CUP$Parser$result29;
            case 29:
                int i85 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left;
                int i86 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).right;
                String lhs_id = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).value;
                int i87 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i88 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Vector rhs3 = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                Production RESULT10 = new Production(new Nonterminal(lhs_id), rhs3);
                Symbol CUP$Parser$result30 = new Symbol(29, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT10);
                return CUP$Parser$result30;
            case 30:
                int i89 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i90 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String id3 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result31 = new Symbol(31, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id3);
                return CUP$Parser$result31;
            case 31:
                int i91 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i92 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String id4 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result32 = new Symbol(32, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id4);
                return CUP$Parser$result32;
            case 32:
                int i93 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                int i94 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                Vector r_list = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                int i95 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i96 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Object r = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                r_list.addElement(r);
                Symbol CUP$Parser$result33 = new Symbol(21, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, r_list);
                return CUP$Parser$result33;
            case 33:
                int i97 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i98 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Object r2 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Vector r_list2 = new Vector();
                r_list2.addElement(r2);
                Symbol CUP$Parser$result34 = new Symbol(21, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, r_list2);
                return CUP$Parser$result34;
            case 34:
                int i99 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                int i100 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                Vector p3 = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                int i101 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i102 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String term_name = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                p3.add(new PrecedenceModifier(term_name));
                Symbol CUP$Parser$result35 = new Symbol(3, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, p3);
                return CUP$Parser$result35;
            case 35:
                int i103 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i104 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Symbol CUP$Parser$result36 = new Symbol(3, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value);
                return CUP$Parser$result36;
            case 36:
                int i105 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i106 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Vector a5 = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                int i107 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i108 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                a5.addElement(((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value);
                Symbol CUP$Parser$result37 = new Symbol(20, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, a5);
                return CUP$Parser$result37;
            case 37:
                Vector RESULT11 = new Vector();
                Symbol CUP$Parser$result38 = new Symbol(20, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT11);
                return CUP$Parser$result38;
            case 38:
                int i109 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i110 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                String symid = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                int i111 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i112 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String labid = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Object RESULT12 = new Nonterminal(symid, labid);
                Symbol CUP$Parser$result39 = new Symbol(2, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT12);
                return CUP$Parser$result39;
            case 39:
                int i113 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i114 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String code_str = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Object RESULT13 = new SemanticAction(code_str);
                Symbol CUP$Parser$result40 = new Symbol(2, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT13);
                return CUP$Parser$result40;
            case 40:
                int i115 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i116 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String labid2 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result41 = new Symbol(34, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, labid2);
                return CUP$Parser$result41;
            case 41:
                Symbol CUP$Parser$result42 = new Symbol(34, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, (Object) null);
                return CUP$Parser$result42;
            case 42:
                int i117 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i118 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String the_id = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result43 = new Symbol(33, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, the_id);
                return CUP$Parser$result43;
            case 43:
                int i119 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i120 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String id5 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result44 = new Symbol(37, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id5);
                return CUP$Parser$result44;
            case 44:
                Symbol CUP$Parser$result45 = new Symbol(7, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, (Object) null);
                return CUP$Parser$result45;
            case 45:
                Symbol CUP$Parser$result46 = new Symbol(7, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, (Object) null);
                return CUP$Parser$result46;
            case 46:
                Symbol CUP$Parser$result47 = new Symbol(4, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, (Object) null);
                return CUP$Parser$result47;
            case 47:
                Symbol CUP$Parser$result48 = new Symbol(4, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, (Object) null);
                return CUP$Parser$result48;
            case 48:
                int i121 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 6)).left;
                int i122 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 6)).right;
                String pkg2 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 6)).value;
                int i123 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).left;
                int i124 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).right;
                Vector imp2 = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).value;
                int i125 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 4)).left;
                int i126 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 4)).right;
                Vector code2 = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 4)).value;
                int i127 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left;
                int i128 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).right;
                Vector sym_list3 = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).value;
                int i129 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                int i130 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                Vector prec_list2 = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                int i131 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i132 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                String start4 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                int i133 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i134 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Vector prod_list = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Spec RESULT14 = new CUPSpec(pkg2, imp2, code2, sym_list3, prec_list2, start4, prod_list);
                Symbol CUP$Parser$result49 = new Symbol(14, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 6)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT14);
                return CUP$Parser$result49;
            case 49:
                int i135 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i136 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                String m_id = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                Symbol CUP$Parser$result50 = new Symbol(38, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, m_id);
                return CUP$Parser$result50;
            case 50:
                Symbol CUP$Parser$result51 = new Symbol(38, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, (Object) null);
                return CUP$Parser$result51;
            case 51:
                int i137 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i138 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Vector i_list = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                int i139 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i140 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String i141 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                i_list.addElement(i141);
                Symbol CUP$Parser$result52 = new Symbol(47, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, i_list);
                return CUP$Parser$result52;
            case 52:
                Vector RESULT15 = new Vector();
                Symbol CUP$Parser$result53 = new Symbol(47, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT15);
                return CUP$Parser$result53;
            case 53:
                int i142 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i143 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                String i144 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                Symbol CUP$Parser$result54 = new Symbol(40, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, i144);
                return CUP$Parser$result54;
            case 54:
                int i145 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i146 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Vector c_parts = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                int i147 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i148 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Object c = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                c_parts.addElement(c);
                Symbol CUP$Parser$result55 = new Symbol(51, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, c_parts);
                return CUP$Parser$result55;
            case 55:
                Vector RESULT16 = new Vector();
                Symbol CUP$Parser$result56 = new Symbol(51, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT16);
                return CUP$Parser$result56;
            case 56:
                int i149 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i150 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Object a6 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result57 = new Symbol(6, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, a6);
                return CUP$Parser$result57;
            case 57:
                int i151 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i152 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Symbol CUP$Parser$result58 = new Symbol(6, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value);
                return CUP$Parser$result58;
            case 58:
                int i153 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i154 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Object i155 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result59 = new Symbol(6, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, i155);
                return CUP$Parser$result59;
            case 59:
                int i156 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i157 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Object s3 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result60 = new Symbol(6, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, s3);
                return CUP$Parser$result60;
            case 60:
                int i158 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i159 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                String user_code = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                Object RESULT17 = new ActionCode(user_code);
                Symbol CUP$Parser$result61 = new Symbol(5, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT17);
                return CUP$Parser$result61;
            case 61:
                int i160 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i161 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                String user_code2 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                Object RESULT18 = new ParserCode(null, "", user_code2);
                Symbol CUP$Parser$result62 = new Symbol(8, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT18);
                return CUP$Parser$result62;
            case 62:
                int i162 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left;
                int i163 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).right;
                String classname = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).value;
                int i164 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                int i165 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                String ei = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                int i166 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i167 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                String user_code3 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                Object RESULT19 = new ParserCode(classname, ei, user_code3);
                Symbol CUP$Parser$result63 = new Symbol(8, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 4)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT19);
                return CUP$Parser$result63;
            case 63:
                Symbol CUP$Parser$result64 = new Symbol(17, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, "");
                return CUP$Parser$result64;
            case 64:
                int i168 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                int i169 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                String e = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                int i170 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i171 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String qid = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                String RESULT20 = new StringBuffer().append(e).append(" extends ").append(qid).toString();
                Symbol CUP$Parser$result65 = new Symbol(17, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT20);
                return CUP$Parser$result65;
            case 65:
                int i172 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                int i173 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                String e2 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                int i174 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i175 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String qid2 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                String RESULT21 = new StringBuffer().append(e2).append(" implements ").append(qid2).toString();
                Symbol CUP$Parser$result66 = new Symbol(17, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT21);
                return CUP$Parser$result66;
            case 66:
                int i176 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i177 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                String user_code4 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                Object RESULT22 = new InitCode(user_code4);
                Symbol CUP$Parser$result67 = new Symbol(9, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT22);
                return CUP$Parser$result67;
            case 67:
                int i178 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i179 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                String user_code5 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                Object RESULT23 = new ScanCode(user_code5);
                Symbol CUP$Parser$result68 = new Symbol(10, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT23);
                return CUP$Parser$result68;
            case 68:
                int i180 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i181 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Vector s_list = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                int i182 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i183 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                SymbolList s4 = (SymbolList) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                s_list.addElement(s4);
                Symbol CUP$Parser$result69 = new Symbol(49, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, s_list);
                return CUP$Parser$result69;
            case 69:
                int i184 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i185 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                SymbolList s5 = (SymbolList) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Vector s_list2 = new Vector();
                s_list2.addElement(s5);
                Symbol CUP$Parser$result70 = new Symbol(49, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, s_list2);
                return CUP$Parser$result70;
            case 70:
                int i186 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i187 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                String type = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                int i188 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i189 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Vector term = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                SymbolList RESULT24 = new SymbolList(0, type, term);
                Symbol CUP$Parser$result71 = new Symbol(57, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT24);
                return CUP$Parser$result71;
            case 71:
                int i190 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i191 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Vector term2 = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                SymbolList RESULT25 = new SymbolList(0, null, term2);
                Symbol CUP$Parser$result72 = new Symbol(57, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT25);
                return CUP$Parser$result72;
            case 72:
                int i192 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i193 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                String type2 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                int i194 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i195 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Vector non_term = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                SymbolList RESULT26 = new SymbolList(1, type2, non_term);
                Symbol CUP$Parser$result73 = new Symbol(57, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT26);
                return CUP$Parser$result73;
            case 73:
                int i196 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i197 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Vector non_term2 = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                SymbolList RESULT27 = new SymbolList(1, null, non_term2);
                Symbol CUP$Parser$result74 = new Symbol(57, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT27);
                return CUP$Parser$result74;
            case 74:
                int i198 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i199 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Symbol CUP$Parser$result75 = new Symbol(55, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value);
                return CUP$Parser$result75;
            case 75:
                int i200 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i201 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Symbol CUP$Parser$result76 = new Symbol(56, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value);
                return CUP$Parser$result76;
            case 76:
                int i202 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                int i203 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                Vector tn_list = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                int i204 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i205 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String n_id = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                tn_list.addElement(n_id);
                Symbol CUP$Parser$result77 = new Symbol(48, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, tn_list);
                return CUP$Parser$result77;
            case 77:
                int i206 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i207 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String n_id2 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Vector tn_list2 = new Vector();
                tn_list2.addElement(n_id2);
                Symbol CUP$Parser$result78 = new Symbol(48, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, tn_list2);
                return CUP$Parser$result78;
            case 78:
                int i208 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                int i209 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                Vector ntn_list = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                int i210 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i211 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String n_id3 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                ntn_list.addElement(n_id3);
                Symbol CUP$Parser$result79 = new Symbol(46, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ntn_list);
                return CUP$Parser$result79;
            case 79:
                int i212 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i213 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String n_id4 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Vector nnt_list = new Vector();
                nnt_list.addElement(n_id4);
                Symbol CUP$Parser$result80 = new Symbol(46, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, nnt_list);
                return CUP$Parser$result80;
            case 80:
                int i214 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i215 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Vector p_l2 = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result81 = new Symbol(54, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, p_l2);
                return CUP$Parser$result81;
            case 81:
                Vector RESULT28 = new Vector();
                Symbol CUP$Parser$result82 = new Symbol(54, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT28);
                return CUP$Parser$result82;
            case 82:
                int i216 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i217 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Vector p_l3 = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                int i218 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i219 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                p_l3.addElement(((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value);
                Symbol CUP$Parser$result83 = new Symbol(53, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, p_l3);
                return CUP$Parser$result83;
            case 83:
                int i220 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i221 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Object p4 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Vector p_l4 = new Vector();
                p_l4.addElement(p4);
                Symbol CUP$Parser$result84 = new Symbol(53, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, p_l4);
                return CUP$Parser$result84;
            case 84:
                int i222 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i223 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Vector t_list = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                Object RESULT29 = new Precedence(0, t_list);
                Symbol CUP$Parser$result85 = new Symbol(11, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT29);
                return CUP$Parser$result85;
            case 85:
                int i224 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i225 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Vector t_list2 = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                Object RESULT30 = new Precedence(1, t_list2);
                Symbol CUP$Parser$result86 = new Symbol(11, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT30);
                return CUP$Parser$result86;
            case 86:
                int i226 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i227 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                Vector t_list3 = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                Object RESULT31 = new Precedence(2, t_list3);
                Symbol CUP$Parser$result87 = new Symbol(11, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT31);
                return CUP$Parser$result87;
            case 87:
                int i228 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                int i229 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                Vector t_list4 = (Vector) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                int i230 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i231 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String t = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                t_list4.addElement(t);
                Symbol CUP$Parser$result88 = new Symbol(52, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list4);
                return CUP$Parser$result88;
            case 88:
                int i232 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i233 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String t2 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Vector t_list5 = new Vector();
                t_list5.addElement(t2);
                Symbol CUP$Parser$result89 = new Symbol(52, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list5);
                return CUP$Parser$result89;
            case 89:
                int i234 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i235 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String sym = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result90 = new Symbol(35, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, sym);
                return CUP$Parser$result90;
            case 90:
                int i236 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i237 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String sym2 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result91 = new Symbol(36, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, sym2);
                return CUP$Parser$result91;
            case 91:
                int i238 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                int i239 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                String start_name = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                Symbol CUP$Parser$result92 = new Symbol(42, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, start_name);
                return CUP$Parser$result92;
            case 92:
                Symbol CUP$Parser$result93 = new Symbol(42, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, (Object) null);
                return CUP$Parser$result93;
            case 93:
                int i240 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                int i241 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                String m_id2 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                int i242 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i243 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String r_id = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                String RESULT32 = new StringBuffer().append(m_id2).append(".").append(r_id).toString();
                Symbol CUP$Parser$result94 = new Symbol(41, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT32);
                return CUP$Parser$result94;
            case 94:
                int i244 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i245 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String r_id2 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result95 = new Symbol(41, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, r_id2);
                return CUP$Parser$result95;
            case 95:
                int i246 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                int i247 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                String m_id3 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                String RESULT33 = new StringBuffer().append(m_id3).append(".*").toString();
                Symbol CUP$Parser$result96 = new Symbol(39, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT33);
                return CUP$Parser$result96;
            case 96:
                int i248 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i249 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String m_id4 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result97 = new Symbol(39, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, m_id4);
                return CUP$Parser$result97;
            case 97:
                int i250 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i251 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String m_id5 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result98 = new Symbol(43, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, m_id5);
                return CUP$Parser$result98;
            case 98:
                int i252 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                int i253 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                String t_id = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                String RESULT34 = new StringBuffer().append(t_id).append("[]").toString();
                Symbol CUP$Parser$result99 = new Symbol(43, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT34);
                return CUP$Parser$result99;
            case 99:
                int i254 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i255 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String id6 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result100 = new Symbol(45, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id6);
                return CUP$Parser$result100;
            case 100:
                int i256 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i257 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                String id7 = (String) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                Symbol CUP$Parser$result101 = new Symbol(44, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id7);
                return CUP$Parser$result101;
            case 101:
                int i258 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i259 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Token id8 = (Token) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                String RESULT35 = (String) id8.getValue();
                Symbol CUP$Parser$result102 = new Symbol(15, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT35);
                return CUP$Parser$result102;
            case 102:
                int i260 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                int i261 = ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                Token c2 = (Token) ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                String RESULT36 = (String) c2.getValue();
                Symbol CUP$Parser$result103 = new Symbol(18, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT36);
                return CUP$Parser$result103;
            default:
                throw new Exception("Invalid action number found in internal parse table");
        }
    }
}
