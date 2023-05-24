package jasmin;

import android.provider.ContactsContract;
import jas.Base64;
import jas.GenericAttr;
import jas.VisibilityAnnotationAttr;
import java.util.Stack;
import java_cup.runtime.Symbol;
import java_cup.runtime.lr_parser;
import javassist.compiler.TokenId;
import org.apache.tools.ant.taskdefs.modules.Jmod;
import org.hamcrest.generator.qdox.parser.impl.Parser;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jasmin/parser$CUP$parser$actions.class */
class parser$CUP$parser$actions {
    short access_val;
    public Scanner scanner;
    public ClassFile classFile;
    private final parser parser;
    final /* synthetic */ parser this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public parser$CUP$parser$actions(parser this$0, parser parser) {
        this.this$0 = this$0;
        this.parser = parser;
    }

    public final Symbol CUP$parser$do_action_part00000000(int CUP$parser$act_num, lr_parser CUP$parser$parser, Stack CUP$parser$stack, int CUP$parser$top) throws Exception {
        switch (CUP$parser$act_num) {
            case 0:
                Symbol CUP$parser$result = this.parser.getSymbolFactory().newSymbol("jas_file", 22, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 12), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result;
            case 1:
                int i = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i2 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Object start_val = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Symbol CUP$parser$result2 = this.parser.getSymbolFactory().newSymbol("$START", 0, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), start_val);
                CUP$parser$parser.done_parsing();
                return CUP$parser$result2;
            case 2:
                int i3 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i4 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String w = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                this.classFile.setSource(w);
                Symbol CUP$parser$result3 = this.parser.getSymbolFactory().newSymbol("source_spec", 36, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result3;
            case 3:
                Symbol CUP$parser$result4 = this.parser.getSymbolFactory().newSymbol("source_spec", 36, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result4;
            case 4:
                int i5 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i6 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                Short a = (Short) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i7 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i8 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String name = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                this.classFile.setClass(name, (short) (a.intValue() | 32));
                Symbol CUP$parser$result5 = this.parser.getSymbolFactory().newSymbol("class_spec", 10, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result5;
            case 5:
                int i9 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i10 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                Short a2 = (Short) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i11 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i12 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String name2 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                this.classFile.setClass(name2, (short) (a2.intValue() | 512));
                Symbol CUP$parser$result6 = this.parser.getSymbolFactory().newSymbol("class_spec", 10, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result6;
            case 6:
                int i13 = ((Symbol) CUP$parser$stack.peek()).left;
                int i14 = ((Symbol) CUP$parser$stack.peek()).right;
                String w2 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                String RESULT = ScannerUtils.convertDots(w2);
                Symbol CUP$parser$result7 = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), RESULT);
                return CUP$parser$result7;
            case 7:
                Symbol CUP$parser$result8 = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "from");
                return CUP$parser$result8;
            case 8:
                Symbol CUP$parser$result9 = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "to");
                return CUP$parser$result9;
            case 9:
                Symbol CUP$parser$result10 = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "using");
                return CUP$parser$result10;
            case 10:
                Symbol CUP$parser$result11 = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "is");
                return CUP$parser$result11;
            case 11:
                Symbol CUP$parser$result12 = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "method");
                return CUP$parser$result12;
            case 12:
                Symbol CUP$parser$result13 = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), Jimple.LOOKUPSWITCH);
                return CUP$parser$result13;
            case 13:
                Symbol CUP$parser$result14 = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), Jimple.TABLESWITCH);
                return CUP$parser$result14;
            case 14:
                Symbol CUP$parser$result15 = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "default");
                return CUP$parser$result15;
            case 15:
                int i15 = ((Symbol) CUP$parser$stack.peek()).left;
                int i16 = ((Symbol) CUP$parser$stack.peek()).right;
                String i17 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                Symbol CUP$parser$result16 = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), i17);
                return CUP$parser$result16;
            case 16:
                Symbol CUP$parser$result17 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "aaload");
                return CUP$parser$result17;
            case 17:
                Symbol CUP$parser$result18 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "aastore");
                return CUP$parser$result18;
            case 18:
                Symbol CUP$parser$result19 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "aconst_null");
                return CUP$parser$result19;
            case 19:
                Symbol CUP$parser$result20 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "aload");
                return CUP$parser$result20;
            case 20:
                Symbol CUP$parser$result21 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "aload_0");
                return CUP$parser$result21;
            case 21:
                Symbol CUP$parser$result22 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "aload_1");
                return CUP$parser$result22;
            case 22:
                Symbol CUP$parser$result23 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "aload_2");
                return CUP$parser$result23;
            case 23:
                Symbol CUP$parser$result24 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "aload_3");
                return CUP$parser$result24;
            case 24:
                Symbol CUP$parser$result25 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "anewarray");
                return CUP$parser$result25;
            case 25:
                Symbol CUP$parser$result26 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "areturn");
                return CUP$parser$result26;
            case 26:
                Symbol CUP$parser$result27 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "arraylength");
                return CUP$parser$result27;
            case 27:
                Symbol CUP$parser$result28 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "astore");
                return CUP$parser$result28;
            case 28:
                Symbol CUP$parser$result29 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "astore_0");
                return CUP$parser$result29;
            case 29:
                Symbol CUP$parser$result30 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "astore_1");
                return CUP$parser$result30;
            case 30:
                Symbol CUP$parser$result31 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "astore_2");
                return CUP$parser$result31;
            case 31:
                Symbol CUP$parser$result32 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "astore_3");
                return CUP$parser$result32;
            case 32:
                Symbol CUP$parser$result33 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "athrow");
                return CUP$parser$result33;
            case 33:
                Symbol CUP$parser$result34 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "baload");
                return CUP$parser$result34;
            case 34:
                Symbol CUP$parser$result35 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "bastore");
                return CUP$parser$result35;
            case 35:
                Symbol CUP$parser$result36 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "bipush");
                return CUP$parser$result36;
            case 36:
                Symbol CUP$parser$result37 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), Jimple.BREAKPOINT);
                return CUP$parser$result37;
            case 37:
                Symbol CUP$parser$result38 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "caload");
                return CUP$parser$result38;
            case 38:
                Symbol CUP$parser$result39 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "castore");
                return CUP$parser$result39;
            case 39:
                Symbol CUP$parser$result40 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "checkcast");
                return CUP$parser$result40;
            case 40:
                Symbol CUP$parser$result41 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "d2f");
                return CUP$parser$result41;
            case 41:
                Symbol CUP$parser$result42 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "d2i");
                return CUP$parser$result42;
            case 42:
                Symbol CUP$parser$result43 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "d2l");
                return CUP$parser$result43;
            case 43:
                Symbol CUP$parser$result44 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dadd");
                return CUP$parser$result44;
            case 44:
                Symbol CUP$parser$result45 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "daload");
                return CUP$parser$result45;
            case 45:
                Symbol CUP$parser$result46 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dastore");
                return CUP$parser$result46;
            case 46:
                Symbol CUP$parser$result47 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dcmpg");
                return CUP$parser$result47;
            case 47:
                Symbol CUP$parser$result48 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dcmpl");
                return CUP$parser$result48;
            case 48:
                Symbol CUP$parser$result49 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dconst_0");
                return CUP$parser$result49;
            case 49:
                Symbol CUP$parser$result50 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dconst_1");
                return CUP$parser$result50;
            case 50:
                Symbol CUP$parser$result51 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ddiv");
                return CUP$parser$result51;
            case 51:
                Symbol CUP$parser$result52 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dload");
                return CUP$parser$result52;
            case 52:
                Symbol CUP$parser$result53 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dload_0");
                return CUP$parser$result53;
            case 53:
                Symbol CUP$parser$result54 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dload_1");
                return CUP$parser$result54;
            case 54:
                Symbol CUP$parser$result55 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dload_2");
                return CUP$parser$result55;
            case 55:
                Symbol CUP$parser$result56 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dload_3");
                return CUP$parser$result56;
            case 56:
                Symbol CUP$parser$result57 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dmul");
                return CUP$parser$result57;
            case 57:
                Symbol CUP$parser$result58 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dneg");
                return CUP$parser$result58;
            case 58:
                Symbol CUP$parser$result59 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "drem");
                return CUP$parser$result59;
            case 59:
                Symbol CUP$parser$result60 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dreturn");
                return CUP$parser$result60;
            case 60:
                Symbol CUP$parser$result61 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dstore");
                return CUP$parser$result61;
            case 61:
                Symbol CUP$parser$result62 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dstore_0");
                return CUP$parser$result62;
            case 62:
                Symbol CUP$parser$result63 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dstore_1");
                return CUP$parser$result63;
            case 63:
                Symbol CUP$parser$result64 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dstore_2");
                return CUP$parser$result64;
            case 64:
                Symbol CUP$parser$result65 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dstore_3");
                return CUP$parser$result65;
            case 65:
                Symbol CUP$parser$result66 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dsub");
                return CUP$parser$result66;
            case 66:
                Symbol CUP$parser$result67 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dup");
                return CUP$parser$result67;
            case 67:
                Symbol CUP$parser$result68 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dup2");
                return CUP$parser$result68;
            case 68:
                Symbol CUP$parser$result69 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dup2_x1");
                return CUP$parser$result69;
            case 69:
                Symbol CUP$parser$result70 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dup2_x2");
                return CUP$parser$result70;
            case 70:
                Symbol CUP$parser$result71 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dup_x1");
                return CUP$parser$result71;
            case 71:
                Symbol CUP$parser$result72 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "dup_x2");
                return CUP$parser$result72;
            case 72:
                Symbol CUP$parser$result73 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "f2d");
                return CUP$parser$result73;
            case 73:
                Symbol CUP$parser$result74 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "f2i");
                return CUP$parser$result74;
            case 74:
                Symbol CUP$parser$result75 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "f2l");
                return CUP$parser$result75;
            case 75:
                Symbol CUP$parser$result76 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fadd");
                return CUP$parser$result76;
            case 76:
                Symbol CUP$parser$result77 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "faload");
                return CUP$parser$result77;
            case 77:
                Symbol CUP$parser$result78 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fastore");
                return CUP$parser$result78;
            case 78:
                Symbol CUP$parser$result79 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fcmpg");
                return CUP$parser$result79;
            case 79:
                Symbol CUP$parser$result80 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fcmpl");
                return CUP$parser$result80;
            case 80:
                Symbol CUP$parser$result81 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fconst_0");
                return CUP$parser$result81;
            case 81:
                Symbol CUP$parser$result82 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fconst_1");
                return CUP$parser$result82;
            case 82:
                Symbol CUP$parser$result83 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fconst_2");
                return CUP$parser$result83;
            case 83:
                Symbol CUP$parser$result84 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fdiv");
                return CUP$parser$result84;
            case 84:
                Symbol CUP$parser$result85 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fload");
                return CUP$parser$result85;
            case 85:
                Symbol CUP$parser$result86 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fload_0");
                return CUP$parser$result86;
            case 86:
                Symbol CUP$parser$result87 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fload_1");
                return CUP$parser$result87;
            case 87:
                Symbol CUP$parser$result88 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fload_2");
                return CUP$parser$result88;
            case 88:
                Symbol CUP$parser$result89 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fload_3");
                return CUP$parser$result89;
            case 89:
                Symbol CUP$parser$result90 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fmul");
                return CUP$parser$result90;
            case 90:
                Symbol CUP$parser$result91 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fneg");
                return CUP$parser$result91;
            case 91:
                Symbol CUP$parser$result92 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "frem");
                return CUP$parser$result92;
            case 92:
                Symbol CUP$parser$result93 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "freturn");
                return CUP$parser$result93;
            case 93:
                Symbol CUP$parser$result94 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fstore");
                return CUP$parser$result94;
            case 94:
                Symbol CUP$parser$result95 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fstore_0");
                return CUP$parser$result95;
            case 95:
                Symbol CUP$parser$result96 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fstore_1");
                return CUP$parser$result96;
            case 96:
                Symbol CUP$parser$result97 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fstore_2");
                return CUP$parser$result97;
            case 97:
                Symbol CUP$parser$result98 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fstore_3");
                return CUP$parser$result98;
            case 98:
                Symbol CUP$parser$result99 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "fsub");
                return CUP$parser$result99;
            case 99:
                Symbol CUP$parser$result100 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "getfield");
                return CUP$parser$result100;
            case 100:
                Symbol CUP$parser$result101 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "getstatic");
                return CUP$parser$result101;
            case 101:
                Symbol CUP$parser$result102 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), Jimple.GOTO);
                return CUP$parser$result102;
            case 102:
                Symbol CUP$parser$result103 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "goto_w");
                return CUP$parser$result103;
            case 103:
                Symbol CUP$parser$result104 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "i2d");
                return CUP$parser$result104;
            case 104:
                Symbol CUP$parser$result105 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "i2f");
                return CUP$parser$result105;
            case 105:
                Symbol CUP$parser$result106 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "i2l");
                return CUP$parser$result106;
            case 106:
                Symbol CUP$parser$result107 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iadd");
                return CUP$parser$result107;
            case 107:
                Symbol CUP$parser$result108 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iaload");
                return CUP$parser$result108;
            case 108:
                Symbol CUP$parser$result109 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iand");
                return CUP$parser$result109;
            case 109:
                Symbol CUP$parser$result110 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iastore");
                return CUP$parser$result110;
            case 110:
                Symbol CUP$parser$result111 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iconst_0");
                return CUP$parser$result111;
            case 111:
                Symbol CUP$parser$result112 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iconst_1");
                return CUP$parser$result112;
            case 112:
                Symbol CUP$parser$result113 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iconst_2");
                return CUP$parser$result113;
            case 113:
                Symbol CUP$parser$result114 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iconst_3");
                return CUP$parser$result114;
            case 114:
                Symbol CUP$parser$result115 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iconst_4");
                return CUP$parser$result115;
            case 115:
                Symbol CUP$parser$result116 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iconst_5");
                return CUP$parser$result116;
            case 116:
                Symbol CUP$parser$result117 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iconst_m1");
                return CUP$parser$result117;
            case 117:
                Symbol CUP$parser$result118 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "idiv");
                return CUP$parser$result118;
            case 118:
                Symbol CUP$parser$result119 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "if_acmpeq");
                return CUP$parser$result119;
            case 119:
                Symbol CUP$parser$result120 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "if_acmpne");
                return CUP$parser$result120;
            case 120:
                Symbol CUP$parser$result121 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "if_icmpeq");
                return CUP$parser$result121;
            case 121:
                Symbol CUP$parser$result122 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "if_icmpge");
                return CUP$parser$result122;
            case 122:
                Symbol CUP$parser$result123 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "if_icmpgt");
                return CUP$parser$result123;
            case 123:
                Symbol CUP$parser$result124 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "if_icmple");
                return CUP$parser$result124;
            case 124:
                Symbol CUP$parser$result125 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "if_icmplt");
                return CUP$parser$result125;
            case 125:
                Symbol CUP$parser$result126 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "if_icmpne");
                return CUP$parser$result126;
            case 126:
                Symbol CUP$parser$result127 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ifeq");
                return CUP$parser$result127;
            case 127:
                Symbol CUP$parser$result128 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ifge");
                return CUP$parser$result128;
            case 128:
                Symbol CUP$parser$result129 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ifgt");
                return CUP$parser$result129;
            case 129:
                Symbol CUP$parser$result130 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ifle");
                return CUP$parser$result130;
            case 130:
                Symbol CUP$parser$result131 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iflt");
                return CUP$parser$result131;
            case 131:
                Symbol CUP$parser$result132 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ifne");
                return CUP$parser$result132;
            case 132:
                Symbol CUP$parser$result133 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ifnonnull");
                return CUP$parser$result133;
            case 133:
                Symbol CUP$parser$result134 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ifnull");
                return CUP$parser$result134;
            case 134:
                Symbol CUP$parser$result135 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iinc");
                return CUP$parser$result135;
            case 135:
                Symbol CUP$parser$result136 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iload");
                return CUP$parser$result136;
            case 136:
                Symbol CUP$parser$result137 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iload_0");
                return CUP$parser$result137;
            case 137:
                Symbol CUP$parser$result138 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iload_1");
                return CUP$parser$result138;
            case 138:
                Symbol CUP$parser$result139 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iload_2");
                return CUP$parser$result139;
            case 139:
                Symbol CUP$parser$result140 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iload_3");
                return CUP$parser$result140;
            case 140:
                Symbol CUP$parser$result141 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "imul");
                return CUP$parser$result141;
            case 141:
                Symbol CUP$parser$result142 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ineg");
                return CUP$parser$result142;
            case 142:
                Symbol CUP$parser$result143 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), Jimple.INSTANCEOF);
                return CUP$parser$result143;
            case 143:
                Symbol CUP$parser$result144 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "int2byte");
                return CUP$parser$result144;
            case 144:
                Symbol CUP$parser$result145 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "int2char");
                return CUP$parser$result145;
            case 145:
                Symbol CUP$parser$result146 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "int2short");
                return CUP$parser$result146;
            case 146:
                Symbol CUP$parser$result147 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "i2b");
                return CUP$parser$result147;
            case 147:
                Symbol CUP$parser$result148 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "i2c");
                return CUP$parser$result148;
            case 148:
                Symbol CUP$parser$result149 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "i2s");
                return CUP$parser$result149;
            case 149:
                Symbol CUP$parser$result150 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "invokeinterface");
                return CUP$parser$result150;
            case 150:
                Symbol CUP$parser$result151 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "invokenonvirtual");
                return CUP$parser$result151;
            case 151:
                Symbol CUP$parser$result152 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "invokespecial");
                return CUP$parser$result152;
            case 152:
                Symbol CUP$parser$result153 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "invokestatic");
                return CUP$parser$result153;
            case 153:
                Symbol CUP$parser$result154 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "invokevirtual");
                return CUP$parser$result154;
            case 154:
                Symbol CUP$parser$result155 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "invokedynamic");
                return CUP$parser$result155;
            case 155:
                Symbol CUP$parser$result156 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ior");
                return CUP$parser$result156;
            case 156:
                Symbol CUP$parser$result157 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "irem");
                return CUP$parser$result157;
            case 157:
                Symbol CUP$parser$result158 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ireturn");
                return CUP$parser$result158;
            case 158:
                Symbol CUP$parser$result159 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ishl");
                return CUP$parser$result159;
            case 159:
                Symbol CUP$parser$result160 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ishr");
                return CUP$parser$result160;
            case 160:
                Symbol CUP$parser$result161 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "istore");
                return CUP$parser$result161;
            case 161:
                Symbol CUP$parser$result162 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "istore_0");
                return CUP$parser$result162;
            case 162:
                Symbol CUP$parser$result163 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "istore_1");
                return CUP$parser$result163;
            case 163:
                Symbol CUP$parser$result164 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "istore_2");
                return CUP$parser$result164;
            case 164:
                Symbol CUP$parser$result165 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "istore_3");
                return CUP$parser$result165;
            case 165:
                Symbol CUP$parser$result166 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "isub");
                return CUP$parser$result166;
            case 166:
                Symbol CUP$parser$result167 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "iushr");
                return CUP$parser$result167;
            case 167:
                Symbol CUP$parser$result168 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ixor");
                return CUP$parser$result168;
            case 168:
                Symbol CUP$parser$result169 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "jsr");
                return CUP$parser$result169;
            case 169:
                Symbol CUP$parser$result170 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "jsr_w");
                return CUP$parser$result170;
            case 170:
                Symbol CUP$parser$result171 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "l2d");
                return CUP$parser$result171;
            case 171:
                Symbol CUP$parser$result172 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "l2f");
                return CUP$parser$result172;
            case 172:
                Symbol CUP$parser$result173 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "l2i");
                return CUP$parser$result173;
            case 173:
                Symbol CUP$parser$result174 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ladd");
                return CUP$parser$result174;
            case 174:
                Symbol CUP$parser$result175 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "laload");
                return CUP$parser$result175;
            case 175:
                Symbol CUP$parser$result176 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "land");
                return CUP$parser$result176;
            case 176:
                Symbol CUP$parser$result177 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lastore");
                return CUP$parser$result177;
            case 177:
                Symbol CUP$parser$result178 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lcmp");
                return CUP$parser$result178;
            case 178:
                Symbol CUP$parser$result179 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lconst_0");
                return CUP$parser$result179;
            case 179:
                Symbol CUP$parser$result180 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lconst_1");
                return CUP$parser$result180;
            case 180:
                Symbol CUP$parser$result181 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ldc");
                return CUP$parser$result181;
            case 181:
                Symbol CUP$parser$result182 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ldc_w");
                return CUP$parser$result182;
            case 182:
                Symbol CUP$parser$result183 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ldc2_w");
                return CUP$parser$result183;
            case 183:
                Symbol CUP$parser$result184 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ldiv");
                return CUP$parser$result184;
            case 184:
                Symbol CUP$parser$result185 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lload");
                return CUP$parser$result185;
            case 185:
                Symbol CUP$parser$result186 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lload_0");
                return CUP$parser$result186;
            case 186:
                Symbol CUP$parser$result187 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lload_1");
                return CUP$parser$result187;
            case 187:
                Symbol CUP$parser$result188 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lload_2");
                return CUP$parser$result188;
            case 188:
                Symbol CUP$parser$result189 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lload_3");
                return CUP$parser$result189;
            case 189:
                Symbol CUP$parser$result190 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lmul");
                return CUP$parser$result190;
            case 190:
                Symbol CUP$parser$result191 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lneg");
                return CUP$parser$result191;
            case 191:
                Symbol CUP$parser$result192 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), Jimple.LOOKUPSWITCH);
                return CUP$parser$result192;
            case 192:
                Symbol CUP$parser$result193 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lor");
                return CUP$parser$result193;
            case 193:
                Symbol CUP$parser$result194 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lrem");
                return CUP$parser$result194;
            case 194:
                Symbol CUP$parser$result195 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lreturn");
                return CUP$parser$result195;
            case 195:
                Symbol CUP$parser$result196 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lshl");
                return CUP$parser$result196;
            case 196:
                Symbol CUP$parser$result197 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lshr");
                return CUP$parser$result197;
            case 197:
                Symbol CUP$parser$result198 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lstore");
                return CUP$parser$result198;
            case 198:
                Symbol CUP$parser$result199 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lstore_0");
                return CUP$parser$result199;
            case 199:
                Symbol CUP$parser$result200 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lstore_1");
                return CUP$parser$result200;
            case 200:
                Symbol CUP$parser$result201 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lstore_2");
                return CUP$parser$result201;
            case 201:
                Symbol CUP$parser$result202 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lstore_3");
                return CUP$parser$result202;
            case 202:
                Symbol CUP$parser$result203 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lsub");
                return CUP$parser$result203;
            case 203:
                Symbol CUP$parser$result204 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lushr");
                return CUP$parser$result204;
            case 204:
                Symbol CUP$parser$result205 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "lxor");
                return CUP$parser$result205;
            case 205:
                Symbol CUP$parser$result206 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "monitorenter");
                return CUP$parser$result206;
            case 206:
                Symbol CUP$parser$result207 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "monitorexit");
                return CUP$parser$result207;
            case 207:
                Symbol CUP$parser$result208 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "multianewarray");
                return CUP$parser$result208;
            case 208:
                Symbol CUP$parser$result209 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "new");
                return CUP$parser$result209;
            case 209:
                Symbol CUP$parser$result210 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), Jimple.NEWARRAY);
                return CUP$parser$result210;
            case 210:
                Symbol CUP$parser$result211 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), Jimple.NOP);
                return CUP$parser$result211;
            case 211:
                Symbol CUP$parser$result212 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "pop");
                return CUP$parser$result212;
            case 212:
                Symbol CUP$parser$result213 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "pop2");
                return CUP$parser$result213;
            case 213:
                Symbol CUP$parser$result214 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "putfield");
                return CUP$parser$result214;
            case 214:
                Symbol CUP$parser$result215 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "putstatic");
                return CUP$parser$result215;
            case 215:
                Symbol CUP$parser$result216 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), Jimple.RET);
                return CUP$parser$result216;
            case 216:
                Symbol CUP$parser$result217 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "ret_w");
                return CUP$parser$result217;
            case 217:
                Symbol CUP$parser$result218 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "return");
                return CUP$parser$result218;
            case 218:
                Symbol CUP$parser$result219 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "saload");
                return CUP$parser$result219;
            case 219:
                Symbol CUP$parser$result220 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "sastore");
                return CUP$parser$result220;
            case 220:
                Symbol CUP$parser$result221 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "sipush");
                return CUP$parser$result221;
            case 221:
                Symbol CUP$parser$result222 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "swap");
                return CUP$parser$result222;
            case 222:
                Symbol CUP$parser$result223 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), Jimple.TABLESWITCH);
                return CUP$parser$result223;
            case 223:
                Symbol CUP$parser$result224 = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "wide");
                return CUP$parser$result224;
            case 224:
                this.access_val = (short) 0;
                Symbol CUP$parser$result225 = this.parser.getSymbolFactory().newSymbol("NT$0", 89, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result225;
            case 225:
                Short sh = (Short) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Short RESULT2 = new Short(this.access_val);
                Symbol CUP$parser$result226 = this.parser.getSymbolFactory().newSymbol("access", 88, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), RESULT2);
                return CUP$parser$result226;
            case 226:
                Symbol CUP$parser$result227 = this.parser.getSymbolFactory().newSymbol("access_list", 8, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result227;
            case sym.i_ixor /* 227 */:
                Symbol CUP$parser$result228 = this.parser.getSymbolFactory().newSymbol("access_list", 8, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result228;
            case sym.i_jsr /* 228 */:
                Symbol CUP$parser$result229 = this.parser.getSymbolFactory().newSymbol("access_items", 7, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result229;
            case sym.i_jsr_w /* 229 */:
                Symbol CUP$parser$result230 = this.parser.getSymbolFactory().newSymbol("access_items", 7, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result230;
            case sym.i_l2d /* 230 */:
                this.access_val = (short) (this.access_val | 1);
                Symbol CUP$parser$result231 = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result231;
            case sym.i_l2f /* 231 */:
                this.access_val = (short) (this.access_val | 2);
                Symbol CUP$parser$result232 = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result232;
            case 232:
                this.access_val = (short) (this.access_val | 4);
                Symbol CUP$parser$result233 = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result233;
            case 233:
                this.access_val = (short) (this.access_val | 8);
                Symbol CUP$parser$result234 = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result234;
            case 234:
                this.access_val = (short) (this.access_val | 16);
                Symbol CUP$parser$result235 = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result235;
            case 235:
                this.access_val = (short) (this.access_val | 32);
                Symbol CUP$parser$result236 = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result236;
            case 236:
                this.access_val = (short) (this.access_val | 64);
                Symbol CUP$parser$result237 = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result237;
            case 237:
                this.access_val = (short) (this.access_val | 128);
                Symbol CUP$parser$result238 = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result238;
            case 238:
                this.access_val = (short) (this.access_val | 256);
                Symbol CUP$parser$result239 = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result239;
            case 239:
                this.access_val = (short) (this.access_val | 512);
                Symbol CUP$parser$result240 = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result240;
            case 240:
                this.access_val = (short) (this.access_val | 1024);
                Symbol CUP$parser$result241 = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result241;
            case sym.i_ldc_w /* 241 */:
                this.access_val = (short) (this.access_val | 2048);
                Symbol CUP$parser$result242 = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result242;
            case 242:
                this.access_val = (short) (this.access_val | 8192);
                Symbol CUP$parser$result243 = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result243;
            case 243:
                this.access_val = (short) (this.access_val | 16384);
                Symbol CUP$parser$result244 = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result244;
            case 244:
                int i18 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i19 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String name3 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                this.classFile.setSuperClass(name3);
                Symbol CUP$parser$result245 = this.parser.getSymbolFactory().newSymbol("super_spec", 40, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result245;
            case 245:
                this.classFile.setNoSuperClass();
                Symbol CUP$parser$result246 = this.parser.getSymbolFactory().newSymbol("super_spec", 40, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result246;
            case 246:
                Symbol CUP$parser$result247 = this.parser.getSymbolFactory().newSymbol("impls", 19, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result247;
            case 247:
                Symbol CUP$parser$result248 = this.parser.getSymbolFactory().newSymbol("impls", 19, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result248;
            case 248:
                Symbol CUP$parser$result249 = this.parser.getSymbolFactory().newSymbol("implements_list", 20, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result249;
            case 249:
                Symbol CUP$parser$result250 = this.parser.getSymbolFactory().newSymbol("implements_list", 20, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result250;
            case 250:
                int i20 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i21 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String name4 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                this.classFile.addInterface(name4);
                Symbol CUP$parser$result251 = this.parser.getSymbolFactory().newSymbol("implements_spec", 21, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result251;
            case 251:
                int i22 = ((Symbol) CUP$parser$stack.peek()).left;
                int i23 = ((Symbol) CUP$parser$stack.peek()).right;
                Object dep_attr = ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.addClassDeprAttr(dep_attr);
                Symbol CUP$parser$result252 = this.parser.getSymbolFactory().newSymbol("class_depr_attr", 71, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result252;
            case 252:
                int i24 = ((Symbol) CUP$parser$stack.peek()).left;
                int i25 = ((Symbol) CUP$parser$stack.peek()).right;
                Object sig_attr = ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.addClassSigAttr(sig_attr);
                Symbol CUP$parser$result253 = this.parser.getSymbolFactory().newSymbol("class_sig_attr", 72, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result253;
            case sym.i_lrem /* 253 */:
                int i26 = ((Symbol) CUP$parser$stack.peek()).left;
                int i27 = ((Symbol) CUP$parser$stack.peek()).right;
                Object annot_attr = ((Symbol) CUP$parser$stack.peek()).value;
                if (annot_attr != null) {
                    if (((VisibilityAnnotationAttr) annot_attr).getKind().equals("RuntimeVisible")) {
                        this.classFile.addClassAnnotAttrVisible(annot_attr);
                    } else {
                        this.classFile.addClassAnnotAttrInvisible(annot_attr);
                    }
                }
                Symbol CUP$parser$result254 = this.parser.getSymbolFactory().newSymbol("class_annotation_attr", 79, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result254;
            case 254:
                int i28 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i29 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Object annot_attr2 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i30 = ((Symbol) CUP$parser$stack.peek()).left;
                int i31 = ((Symbol) CUP$parser$stack.peek()).right;
                Object annot_attr22 = ((Symbol) CUP$parser$stack.peek()).value;
                if (((VisibilityAnnotationAttr) annot_attr2).getKind().equals("RuntimeVisible")) {
                    this.classFile.addClassAnnotAttrVisible(annot_attr2);
                } else {
                    this.classFile.addClassAnnotAttrInvisible(annot_attr2);
                }
                if (((VisibilityAnnotationAttr) annot_attr22).getKind().equals("RuntimeVisible")) {
                    this.classFile.addClassAnnotAttrVisible(annot_attr22);
                } else {
                    this.classFile.addClassAnnotAttrInvisible(annot_attr22);
                }
                Symbol CUP$parser$result255 = this.parser.getSymbolFactory().newSymbol("class_annotation_attr", 79, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result255;
            case 255:
                Symbol CUP$parser$result256 = this.parser.getSymbolFactory().newSymbol("fields", 17, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result256;
            case 256:
                Symbol CUP$parser$result257 = this.parser.getSymbolFactory().newSymbol("fields", 17, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result257;
            case 257:
                Symbol CUP$parser$result258 = this.parser.getSymbolFactory().newSymbol("field_list", 15, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result258;
            case 258:
                Symbol CUP$parser$result259 = this.parser.getSymbolFactory().newSymbol("field_list", 15, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result259;
            case 259:
                Symbol CUP$parser$result260 = this.parser.getSymbolFactory().newSymbol("field_item", 58, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result260;
            case 260:
                Symbol CUP$parser$result261 = this.parser.getSymbolFactory().newSymbol("field_item", 58, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result261;
            case 261:
                int i32 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7)).left;
                int i33 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7)).right;
                Short a3 = (Short) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7)).value;
                int i34 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).left;
                int i35 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).right;
                String name5 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).value;
                int i36 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).left;
                int i37 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).right;
                String sig = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).value;
                int i38 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).left;
                int i39 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).right;
                Object v = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).value;
                int i40 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i41 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                Object dep_attr2 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i42 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i43 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Object sig_attr2 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i44 = ((Symbol) CUP$parser$stack.peek()).left;
                int i45 = ((Symbol) CUP$parser$stack.peek()).right;
                Object vis_annot_attr = ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.addField((short) a3.intValue(), name5, sig, v, dep_attr2, sig_attr2, vis_annot_attr, null);
                Symbol CUP$parser$result262 = this.parser.getSymbolFactory().newSymbol("field_spec", 16, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 8), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result262;
            case 262:
                int i46 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 8)).left;
                int i47 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 8)).right;
                Short a4 = (Short) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 8)).value;
                int i48 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7)).left;
                int i49 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7)).right;
                String name6 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7)).value;
                int i50 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).left;
                int i51 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).right;
                String sig2 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).value;
                int i52 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).left;
                int i53 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).right;
                Object v2 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).value;
                int i54 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i55 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                Object dep_attr3 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i56 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i57 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Object sig_attr3 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i58 = ((Symbol) CUP$parser$stack.peek()).left;
                int i59 = ((Symbol) CUP$parser$stack.peek()).right;
                Object vis_annot_attr2 = ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.addField((short) a4.intValue(), name6, sig2, v2, "synth", dep_attr3, sig_attr3, vis_annot_attr2, null);
                Symbol CUP$parser$result263 = this.parser.getSymbolFactory().newSymbol("field_spec", 16, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 9), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result263;
            case 263:
                int i60 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 8)).left;
                int i61 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 8)).right;
                Short a5 = (Short) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 8)).value;
                int i62 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7)).left;
                int i63 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7)).right;
                String name7 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7)).value;
                int i64 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).left;
                int i65 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).right;
                String sig3 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).value;
                int i66 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).left;
                int i67 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).right;
                Object v3 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).value;
                int i68 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
                int i69 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
                Object dep_attr4 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
                int i70 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i71 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                Object sig_attr4 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i72 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i73 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Object vis_annot_attr1 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i74 = ((Symbol) CUP$parser$stack.peek()).left;
                int i75 = ((Symbol) CUP$parser$stack.peek()).right;
                Object vis_annot_attr22 = ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.addField((short) a5.intValue(), name7, sig3, v3, dep_attr4, sig_attr4, vis_annot_attr1, vis_annot_attr22);
                Symbol CUP$parser$result264 = this.parser.getSymbolFactory().newSymbol("field_spec", 16, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 9), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result264;
            case 264:
                int i76 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 9)).left;
                int i77 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 9)).right;
                Short a6 = (Short) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 9)).value;
                int i78 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 8)).left;
                int i79 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 8)).right;
                String name8 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 8)).value;
                int i80 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7)).left;
                int i81 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7)).right;
                String sig4 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7)).value;
                int i82 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).left;
                int i83 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).right;
                Object v4 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).value;
                int i84 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
                int i85 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
                Object dep_attr5 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
                int i86 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i87 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                Object sig_attr5 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i88 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i89 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Object vis_annot_attr12 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i90 = ((Symbol) CUP$parser$stack.peek()).left;
                int i91 = ((Symbol) CUP$parser$stack.peek()).right;
                Object vis_annot_attr23 = ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.addField((short) a6.intValue(), name8, sig4, v4, "synth", dep_attr5, sig_attr5, vis_annot_attr12, vis_annot_attr23);
                Symbol CUP$parser$result265 = this.parser.getSymbolFactory().newSymbol("field_spec", 16, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 10), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result265;
            case 265:
                int i92 = ((Symbol) CUP$parser$stack.peek()).left;
                int i93 = ((Symbol) CUP$parser$stack.peek()).right;
                Object v5 = ((Symbol) CUP$parser$stack.peek()).value;
                Symbol CUP$parser$result266 = this.parser.getSymbolFactory().newSymbol("optional_default", 2, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), v5);
                return CUP$parser$result266;
            case 266:
                Symbol CUP$parser$result267 = this.parser.getSymbolFactory().newSymbol("optional_default", 2, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result267;
            case 267:
                int i94 = ((Symbol) CUP$parser$stack.peek()).left;
                int i95 = ((Symbol) CUP$parser$stack.peek()).right;
                Integer i96 = (Integer) ((Symbol) CUP$parser$stack.peek()).value;
                Symbol CUP$parser$result268 = this.parser.getSymbolFactory().newSymbol("item", 3, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), i96);
                return CUP$parser$result268;
            case 268:
                int i97 = ((Symbol) CUP$parser$stack.peek()).left;
                int i98 = ((Symbol) CUP$parser$stack.peek()).right;
                Number n = (Number) ((Symbol) CUP$parser$stack.peek()).value;
                Symbol CUP$parser$result269 = this.parser.getSymbolFactory().newSymbol("item", 3, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), n);
                return CUP$parser$result269;
            case 269:
                int i99 = ((Symbol) CUP$parser$stack.peek()).left;
                int i100 = ((Symbol) CUP$parser$stack.peek()).right;
                String s = (String) ((Symbol) CUP$parser$stack.peek()).value;
                Symbol CUP$parser$result270 = this.parser.getSymbolFactory().newSymbol("item", 3, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), s);
                return CUP$parser$result270;
            case 270:
                Symbol CUP$parser$result271 = this.parser.getSymbolFactory().newSymbol("methods", 33, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result271;
            case 271:
                Symbol CUP$parser$result272 = this.parser.getSymbolFactory().newSymbol("methods", 33, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result272;
            case 272:
                Symbol CUP$parser$result273 = this.parser.getSymbolFactory().newSymbol("method_list", 31, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result273;
            case 273:
                Symbol CUP$parser$result274 = this.parser.getSymbolFactory().newSymbol("method_list", 31, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result274;
            case 274:
                Symbol CUP$parser$result275 = this.parser.getSymbolFactory().newSymbol("method_item", 54, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result275;
            case 275:
                Symbol CUP$parser$result276 = this.parser.getSymbolFactory().newSymbol("method_item", 54, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result276;
            case 276:
                Symbol CUP$parser$result277 = this.parser.getSymbolFactory().newSymbol("method_spec", 32, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result277;
            case 277:
                Symbol CUP$parser$result278 = this.parser.getSymbolFactory().newSymbol("method_spec", 32, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result278;
            case 278:
                int i101 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i102 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                Short i103 = (Short) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i104 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i105 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String name9 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                String[] split = ScannerUtils.splitMethodSignature(name9);
                this.classFile.newMethod(split[0], split[1], i103.intValue());
                Symbol CUP$parser$result279 = this.parser.getSymbolFactory().newSymbol("defmethod", 12, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result279;
            case 279:
                this.classFile.endMethod();
                Symbol CUP$parser$result280 = this.parser.getSymbolFactory().newSymbol("endmethod", 14, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result280;
            case 280:
                Symbol CUP$parser$result281 = this.parser.getSymbolFactory().newSymbol("statements", 38, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result281;
            case 281:
                Symbol CUP$parser$result282 = this.parser.getSymbolFactory().newSymbol("statements", 38, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result282;
            case 282:
                this.classFile.setLine(this.scanner.token_line_num);
                Symbol CUP$parser$result283 = this.parser.getSymbolFactory().newSymbol("NT$1", 90, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result283;
            case 283:
                Object RESULT3 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                Symbol CUP$parser$result284 = this.parser.getSymbolFactory().newSymbol("statement", 37, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), RESULT3);
                return CUP$parser$result284;
            case 284:
                Symbol CUP$parser$result285 = this.parser.getSymbolFactory().newSymbol("stmnt", 39, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result285;
            case 285:
                Symbol CUP$parser$result286 = this.parser.getSymbolFactory().newSymbol("stmnt", 39, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result286;
            case 286:
                Symbol CUP$parser$result287 = this.parser.getSymbolFactory().newSymbol("stmnt", 39, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result287;
            case 287:
                Symbol CUP$parser$result288 = this.parser.getSymbolFactory().newSymbol("stmnt", 39, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result288;
            case Parser.IMPLEMENTS /* 288 */:
                Symbol CUP$parser$result289 = this.parser.getSymbolFactory().newSymbol("stmnt", 39, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result289;
            case Parser.SUPER /* 289 */:
                int i106 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i107 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String label = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                this.classFile.plantLabel(label);
                Symbol CUP$parser$result290 = this.parser.getSymbolFactory().newSymbol("label", 23, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result290;
            case Parser.DEFAULT /* 290 */:
                Symbol CUP$parser$result291 = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result291;
            case Parser.BRACEOPEN /* 291 */:
                Symbol CUP$parser$result292 = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result292;
            case Parser.BRACECLOSE /* 292 */:
                Symbol CUP$parser$result293 = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result293;
            case Parser.SQUAREOPEN /* 293 */:
                Symbol CUP$parser$result294 = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result294;
            case Parser.SQUARECLOSE /* 294 */:
                Symbol CUP$parser$result295 = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result295;
            case Parser.PARENOPEN /* 295 */:
                Symbol CUP$parser$result296 = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result296;
            case Parser.PARENCLOSE /* 296 */:
                Symbol CUP$parser$result297 = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result297;
            case Parser.LESSTHAN /* 297 */:
                Symbol CUP$parser$result298 = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result298;
            case Parser.GREATERTHAN /* 298 */:
                Symbol CUP$parser$result299 = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result299;
            case Parser.LESSEQUALS /* 299 */:
                Symbol CUP$parser$result300 = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result300;
            default:
                throw new Exception("Invalid action number " + CUP$parser$act_num + "found in internal parse table");
        }
    }

    public final Symbol CUP$parser$do_action_part00000001(int CUP$parser$act_num, lr_parser CUP$parser$parser, Stack CUP$parser$stack, int CUP$parser$top) throws Exception {
        switch (CUP$parser$act_num) {
            case 300:
                Symbol CUP$parser$result = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result;
            case 301:
                Symbol CUP$parser$result2 = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result2;
            case 302:
                Symbol CUP$parser$result3 = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result3;
            case 303:
                this.classFile.addMethSynthAttr();
                Symbol CUP$parser$result4 = this.parser.getSymbolFactory().newSymbol("meth_synth_attr", 68, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result4;
            case 304:
                this.classFile.addMethDeprAttr();
                Symbol CUP$parser$result5 = this.parser.getSymbolFactory().newSymbol("meth_depr_attr", 69, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result5;
            case 305:
                int i = ((Symbol) CUP$parser$stack.peek()).left;
                int i2 = ((Symbol) CUP$parser$stack.peek()).right;
                String s = (String) ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.addMethSigAttr(s);
                Symbol CUP$parser$result6 = this.parser.getSymbolFactory().newSymbol("meth_sig_attr", 70, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result6;
            case 306:
                int i3 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).left;
                int i4 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).right;
                Object tval = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).value;
                int i5 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i6 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                Object list = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                Object RESULT = this.classFile.makeVisibilityAnnotation(tval, list);
                if (tval.equals("RuntimeVisible")) {
                    this.classFile.addMethAnnotAttrVisible(RESULT);
                } else {
                    this.classFile.addMethAnnotAttrInvisible(RESULT);
                }
                Symbol CUP$parser$result7 = this.parser.getSymbolFactory().newSymbol("meth_annotation_attr", 78, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), RESULT);
                return CUP$parser$result7;
            case 307:
                int i7 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).left;
                int i8 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).right;
                Object kind = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).value;
                int i9 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i10 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                Object list2 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                Object RESULT2 = this.classFile.makeParameterVisibilityAnnotation(kind, list2);
                if (kind.equals("RuntimeVisible")) {
                    this.classFile.addMethParamAnnotAttrVisible(RESULT2);
                } else {
                    this.classFile.addMethParamAnnotAttrInvisible(RESULT2);
                }
                Symbol CUP$parser$result8 = this.parser.getSymbolFactory().newSymbol("meth_param_annotation_attr", 82, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5), (Symbol) CUP$parser$stack.peek(), RESULT2);
                return CUP$parser$result8;
            case 308:
                int i11 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i12 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Object list3 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i13 = ((Symbol) CUP$parser$stack.peek()).left;
                int i14 = ((Symbol) CUP$parser$stack.peek()).right;
                Object elem = ((Symbol) CUP$parser$stack.peek()).value;
                Object RESULT3 = this.classFile.mergeNewAnnotAttr(list3, elem);
                Symbol CUP$parser$result9 = this.parser.getSymbolFactory().newSymbol("annotation_attr_list", 83, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), RESULT3);
                return CUP$parser$result9;
            case 309:
                int i15 = ((Symbol) CUP$parser$stack.peek()).left;
                int i16 = ((Symbol) CUP$parser$stack.peek()).right;
                Object elem2 = ((Symbol) CUP$parser$stack.peek()).value;
                Object RESULT4 = this.classFile.makeNewAnnotAttrList(elem2);
                Symbol CUP$parser$result10 = this.parser.getSymbolFactory().newSymbol("annotation_attr_list", 83, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), RESULT4);
                return CUP$parser$result10;
            case 310:
                int i17 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i18 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                Object def = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                this.classFile.addMethAnnotDefault(def);
                Symbol CUP$parser$result11 = this.parser.getSymbolFactory().newSymbol("meth_annotation_default_attr", 81, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result11;
            case 311:
                int i19 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i20 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String w = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i21 = ((Symbol) CUP$parser$stack.peek()).left;
                int i22 = ((Symbol) CUP$parser$stack.peek()).right;
                String v = (String) ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.addSootCodeAttr(w, v);
                Symbol CUP$parser$result12 = this.parser.getSymbolFactory().newSymbol("code_attr_expr", 50, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result12;
            case 312:
                int i23 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7)).left;
                int i24 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7)).right;
                Integer reg = (Integer) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7)).value;
                int i25 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).left;
                int i26 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).right;
                String name = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).value;
                int i27 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).left;
                int i28 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).right;
                String sig = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).value;
                int i29 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i30 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String slab = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i31 = ((Symbol) CUP$parser$stack.peek()).left;
                int i32 = ((Symbol) CUP$parser$stack.peek()).right;
                String elab = (String) ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.addVar(slab, elab, name, sig, reg.intValue());
                Symbol CUP$parser$result13 = this.parser.getSymbolFactory().newSymbol("var_expr", 49, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result13;
            case 313:
                int i33 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
                int i34 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
                Integer reg2 = (Integer) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
                int i35 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i36 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String name2 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i37 = ((Symbol) CUP$parser$stack.peek()).left;
                int i38 = ((Symbol) CUP$parser$stack.peek()).right;
                String sig2 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.addVar(null, null, name2, sig2, reg2.intValue());
                Symbol CUP$parser$result14 = this.parser.getSymbolFactory().newSymbol("var_expr", 49, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result14;
            case 314:
                int i39 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i40 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String w2 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i41 = ((Symbol) CUP$parser$stack.peek()).left;
                int i42 = ((Symbol) CUP$parser$stack.peek()).right;
                Integer v2 = (Integer) ((Symbol) CUP$parser$stack.peek()).value;
                if (w2.equals("locals") || w2.equals("vars")) {
                    this.classFile.setVarSize((short) v2.intValue());
                } else if (w2.equals("stack")) {
                    this.classFile.setStackSize((short) v2.intValue());
                } else {
                    this.classFile.report_error(".limit expected \"stack\" or \"locals\", but got " + w2);
                }
                Symbol CUP$parser$result15 = this.parser.getSymbolFactory().newSymbol("limit_expr", 24, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result15;
            case 315:
                int i43 = ((Symbol) CUP$parser$stack.peek()).left;
                int i44 = ((Symbol) CUP$parser$stack.peek()).right;
                Integer v3 = (Integer) ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.addLine(v3.intValue());
                Symbol CUP$parser$result16 = this.parser.getSymbolFactory().newSymbol("line_expr", 43, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result16;
            case 316:
                int i45 = ((Symbol) CUP$parser$stack.peek()).left;
                int i46 = ((Symbol) CUP$parser$stack.peek()).right;
                String s2 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.addThrow(s2);
                Symbol CUP$parser$result17 = this.parser.getSymbolFactory().newSymbol("throws_expr", 48, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result17;
            case 317:
                int i47 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).left;
                int i48 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).right;
                String aclass = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).value;
                int i49 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).left;
                int i50 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).right;
                String fromlab = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).value;
                int i51 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i52 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String tolab = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i53 = ((Symbol) CUP$parser$stack.peek()).left;
                int i54 = ((Symbol) CUP$parser$stack.peek()).right;
                String branchlab = (String) ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.addCatch(aclass, fromlab, tolab, branchlab);
                Symbol CUP$parser$result18 = this.parser.getSymbolFactory().newSymbol("catch_expr", 9, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result18;
            case 318:
                int i55 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i56 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String name3 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i57 = ((Symbol) CUP$parser$stack.peek()).left;
                int i58 = ((Symbol) CUP$parser$stack.peek()).right;
                Integer v4 = (Integer) ((Symbol) CUP$parser$stack.peek()).value;
                this.scanner.dict.put(name3, v4);
                Symbol CUP$parser$result19 = this.parser.getSymbolFactory().newSymbol("set_expr", 34, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result19;
            case 319:
                int i59 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i60 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String name4 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i61 = ((Symbol) CUP$parser$stack.peek()).left;
                int i62 = ((Symbol) CUP$parser$stack.peek()).right;
                String v5 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                this.scanner.dict.put(name4, v5);
                Symbol CUP$parser$result20 = this.parser.getSymbolFactory().newSymbol("set_expr", 34, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result20;
            case 320:
                int i63 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i64 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String name5 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i65 = ((Symbol) CUP$parser$stack.peek()).left;
                int i66 = ((Symbol) CUP$parser$stack.peek()).right;
                Number v6 = (Number) ((Symbol) CUP$parser$stack.peek()).value;
                this.scanner.dict.put(name5, v6);
                Symbol CUP$parser$result21 = this.parser.getSymbolFactory().newSymbol("set_expr", 34, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result21;
            case 321:
                int i67 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i68 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String name6 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i69 = ((Symbol) CUP$parser$stack.peek()).left;
                int i70 = ((Symbol) CUP$parser$stack.peek()).right;
                String v7 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                this.scanner.dict.put(name6, v7);
                Symbol CUP$parser$result22 = this.parser.getSymbolFactory().newSymbol("set_expr", 34, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result22;
            case 322:
                Symbol CUP$parser$result23 = this.parser.getSymbolFactory().newSymbol("instruction", 18, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result23;
            case 323:
                Symbol CUP$parser$result24 = this.parser.getSymbolFactory().newSymbol("instruction", 18, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result24;
            case 324:
                int i71 = ((Symbol) CUP$parser$stack.peek()).left;
                int i72 = ((Symbol) CUP$parser$stack.peek()).right;
                String i73 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.plant(i73);
                Symbol CUP$parser$result25 = this.parser.getSymbolFactory().newSymbol("simple_instruction", 35, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result25;
            case 325:
                int i74 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i75 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String i76 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i77 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i78 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Integer n1 = (Integer) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i79 = ((Symbol) CUP$parser$stack.peek()).left;
                int i80 = ((Symbol) CUP$parser$stack.peek()).right;
                Integer n2 = (Integer) ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.plant(i76, n1.intValue(), n2.intValue());
                Symbol CUP$parser$result26 = this.parser.getSymbolFactory().newSymbol("simple_instruction", 35, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result26;
            case 326:
                int i81 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i82 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String i83 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i84 = ((Symbol) CUP$parser$stack.peek()).left;
                int i85 = ((Symbol) CUP$parser$stack.peek()).right;
                Integer n = (Integer) ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.plant(i83, n.intValue());
                Symbol CUP$parser$result27 = this.parser.getSymbolFactory().newSymbol("simple_instruction", 35, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result27;
            case 327:
                int i86 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i87 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String i88 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i89 = ((Symbol) CUP$parser$stack.peek()).left;
                int i90 = ((Symbol) CUP$parser$stack.peek()).right;
                Number n3 = (Number) ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.plant(i88, n3);
                Symbol CUP$parser$result28 = this.parser.getSymbolFactory().newSymbol("simple_instruction", 35, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result28;
            case 328:
                int i91 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i92 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String i93 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i94 = ((Symbol) CUP$parser$stack.peek()).left;
                int i95 = ((Symbol) CUP$parser$stack.peek()).right;
                String n4 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.plant(i93, n4);
                Symbol CUP$parser$result29 = this.parser.getSymbolFactory().newSymbol("simple_instruction", 35, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result29;
            case 329:
                int i96 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i97 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String i98 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i99 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i100 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String n5 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i101 = ((Symbol) CUP$parser$stack.peek()).left;
                int i102 = ((Symbol) CUP$parser$stack.peek()).right;
                Integer n22 = (Integer) ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.plant(i98, n5, n22.intValue());
                Symbol CUP$parser$result30 = this.parser.getSymbolFactory().newSymbol("simple_instruction", 35, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result30;
            case 330:
                int i103 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i104 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String i105 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i106 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i107 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String n12 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i108 = ((Symbol) CUP$parser$stack.peek()).left;
                int i109 = ((Symbol) CUP$parser$stack.peek()).right;
                String n23 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.plant(i105, n12, n23);
                Symbol CUP$parser$result31 = this.parser.getSymbolFactory().newSymbol("simple_instruction", 35, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result31;
            case 331:
                int i110 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
                int i111 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
                String i112 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
                int i113 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i114 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String n13 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i115 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i116 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String n24 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i117 = ((Symbol) CUP$parser$stack.peek()).left;
                int i118 = ((Symbol) CUP$parser$stack.peek()).right;
                String n32 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.plant(i112, n13, n24, n32);
                Symbol CUP$parser$result32 = this.parser.getSymbolFactory().newSymbol("simple_instruction", 35, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result32;
            case 332:
                int i119 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i120 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String i121 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i122 = ((Symbol) CUP$parser$stack.peek()).left;
                int i123 = ((Symbol) CUP$parser$stack.peek()).right;
                String n6 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.plantString(i121, n6);
                Symbol CUP$parser$result33 = this.parser.getSymbolFactory().newSymbol("simple_instruction", 35, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result33;
            case 333:
                Symbol CUP$parser$result34 = this.parser.getSymbolFactory().newSymbol("complex_instruction", 11, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result34;
            case 334:
                Symbol CUP$parser$result35 = this.parser.getSymbolFactory().newSymbol("complex_instruction", 11, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result35;
            case 335:
                Symbol CUP$parser$result36 = this.parser.getSymbolFactory().newSymbol(ContactsContract.ContactsColumns.LOOKUP_KEY, 25, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result36;
            case 336:
                this.classFile.newLookupswitch();
                Symbol CUP$parser$result37 = this.parser.getSymbolFactory().newSymbol("lookup_args", 26, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result37;
            case 337:
                Symbol CUP$parser$result38 = this.parser.getSymbolFactory().newSymbol("lookup_list_t", 29, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result38;
            case 338:
                Symbol CUP$parser$result39 = this.parser.getSymbolFactory().newSymbol("lookup_list_t", 29, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result39;
            case 339:
                Symbol CUP$parser$result40 = this.parser.getSymbolFactory().newSymbol("lookup_list", 30, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result40;
            case TokenId.THROW /* 340 */:
                Symbol CUP$parser$result41 = this.parser.getSymbolFactory().newSymbol("lookup_list", 30, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result41;
            case TokenId.THROWS /* 341 */:
                int i124 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
                int i125 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
                Integer i126 = (Integer) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
                int i127 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i128 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String w3 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                this.classFile.addLookupswitch(i126.intValue(), w3);
                Symbol CUP$parser$result42 = this.parser.getSymbolFactory().newSymbol("lookup_entry", 28, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result42;
            case TokenId.TRANSIENT /* 342 */:
                int i129 = ((Symbol) CUP$parser$stack.peek()).left;
                int i130 = ((Symbol) CUP$parser$stack.peek()).right;
                String w4 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.endLookupswitch(w4);
                Symbol CUP$parser$result43 = this.parser.getSymbolFactory().newSymbol("lookup_default", 27, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result43;
            case TokenId.TRY /* 343 */:
                Symbol CUP$parser$result44 = this.parser.getSymbolFactory().newSymbol("table", 41, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result44;
            case TokenId.VOID /* 344 */:
                int i131 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i132 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Integer low = (Integer) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                this.classFile.newTableswitch(low.intValue());
                Symbol CUP$parser$result45 = this.parser.getSymbolFactory().newSymbol("table_args", 42, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result45;
            case TokenId.VOLATILE /* 345 */:
                int i133 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i134 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                Integer low2 = (Integer) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i135 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i136 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Integer high = (Integer) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                this.classFile.newTableswitch(low2.intValue(), high.intValue());
                Symbol CUP$parser$result46 = this.parser.getSymbolFactory().newSymbol("table_args", 42, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result46;
            case TokenId.WHILE /* 346 */:
                Symbol CUP$parser$result47 = this.parser.getSymbolFactory().newSymbol("table_list_t", 46, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result47;
            case TokenId.STRICT /* 347 */:
                Symbol CUP$parser$result48 = this.parser.getSymbolFactory().newSymbol("table_list_t", 46, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result48;
            case 348:
                Symbol CUP$parser$result49 = this.parser.getSymbolFactory().newSymbol("table_list", 47, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result49;
            case 349:
                Symbol CUP$parser$result50 = this.parser.getSymbolFactory().newSymbol("table_list", 47, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result50;
            case TokenId.NEQ /* 350 */:
                int i137 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i138 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String w5 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                this.classFile.addTableswitch(w5);
                Symbol CUP$parser$result51 = this.parser.getSymbolFactory().newSymbol("table_entry", 45, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result51;
            case TokenId.MOD_E /* 351 */:
                int i139 = ((Symbol) CUP$parser$stack.peek()).left;
                int i140 = ((Symbol) CUP$parser$stack.peek()).right;
                String w6 = (String) ((Symbol) CUP$parser$stack.peek()).value;
                this.classFile.endTableswitch(w6);
                Symbol CUP$parser$result52 = this.parser.getSymbolFactory().newSymbol("table_default", 44, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result52;
            case TokenId.AND_E /* 352 */:
                Symbol CUP$parser$result53 = this.parser.getSymbolFactory().newSymbol("class_attrs", 51, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result53;
            case TokenId.MUL_E /* 353 */:
                Symbol CUP$parser$result54 = this.parser.getSymbolFactory().newSymbol("class_attrs", 51, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result54;
            case TokenId.PLUS_E /* 354 */:
                Symbol CUP$parser$result55 = this.parser.getSymbolFactory().newSymbol("class_attr_list", 52, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result55;
            case TokenId.MINUS_E /* 355 */:
                Symbol CUP$parser$result56 = this.parser.getSymbolFactory().newSymbol("class_attr_list", 52, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result56;
            case TokenId.DIV_E /* 356 */:
                int i141 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i142 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String w7 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i143 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i144 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String v8 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                byte[] data = Base64.decode(v8.toCharArray());
                this.classFile.addGenericAttrToClass(new GenericAttr(w7, data));
                Symbol CUP$parser$result57 = this.parser.getSymbolFactory().newSymbol("class_attr_spec", 53, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result57;
            case TokenId.LE /* 357 */:
                Symbol CUP$parser$result58 = this.parser.getSymbolFactory().newSymbol("method_attrs", 55, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result58;
            case TokenId.EQ /* 358 */:
                Symbol CUP$parser$result59 = this.parser.getSymbolFactory().newSymbol("method_attr_list", 56, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result59;
            case TokenId.GE /* 359 */:
                Symbol CUP$parser$result60 = this.parser.getSymbolFactory().newSymbol("method_attr_list", 56, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result60;
            case TokenId.EXOR_E /* 360 */:
                int i145 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i146 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String w8 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i147 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i148 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String v9 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                byte[] data2 = Base64.decode(v9.toCharArray());
                this.classFile.addGenericAttrToMethod(w8, data2);
                Symbol CUP$parser$result61 = this.parser.getSymbolFactory().newSymbol("method_attr_spec", 57, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result61;
            case TokenId.OR_E /* 361 */:
                Symbol CUP$parser$result62 = this.parser.getSymbolFactory().newSymbol("field_attrs", 59, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result62;
            case TokenId.PLUSPLUS /* 362 */:
                Symbol CUP$parser$result63 = this.parser.getSymbolFactory().newSymbol("field_attr_list", 60, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result63;
            case TokenId.MINUSMINUS /* 363 */:
                Symbol CUP$parser$result64 = this.parser.getSymbolFactory().newSymbol("field_attr_list", 60, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result64;
            case TokenId.LSHIFT /* 364 */:
                int i149 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i150 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String w9 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i151 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i152 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String v10 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                byte[] data3 = Base64.decode(v10.toCharArray());
                this.classFile.addGenericAttrToField(w9, data3);
                Symbol CUP$parser$result65 = this.parser.getSymbolFactory().newSymbol("field_attr_spec", 61, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result65;
            case TokenId.LSHIFT_E /* 365 */:
                this.classFile.addInnerClassAttr();
                Symbol CUP$parser$result66 = this.parser.getSymbolFactory().newSymbol("inner_class_attr", 62, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result66;
            case TokenId.RSHIFT /* 366 */:
                Symbol CUP$parser$result67 = this.parser.getSymbolFactory().newSymbol("inner_class_attr", 62, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result67;
            case TokenId.RSHIFT_E /* 367 */:
                Symbol CUP$parser$result68 = this.parser.getSymbolFactory().newSymbol("inner_class_attr_list", 63, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result68;
            case TokenId.OROR /* 368 */:
                Symbol CUP$parser$result69 = this.parser.getSymbolFactory().newSymbol("inner_class_attr_list", 63, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result69;
            case TokenId.ANDAND /* 369 */:
                int i153 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).left;
                int i154 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).right;
                String a = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).value;
                int i155 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).left;
                int i156 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).right;
                String b = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).value;
                int i157 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).left;
                int i158 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).right;
                String c = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).value;
                int i159 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
                int i160 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
                Short d = (Short) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
                this.classFile.addInnerClassSpec(a, b, c, (short) d.intValue());
                Symbol CUP$parser$result70 = this.parser.getSymbolFactory().newSymbol("inner_class_attr_spec", 64, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result70;
            case TokenId.ARSHIFT /* 370 */:
                this.classFile.endInnerClassAttr();
                Symbol CUP$parser$result71 = this.parser.getSymbolFactory().newSymbol("end_inner_class_attr", 65, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result71;
            case TokenId.ARSHIFT_E /* 371 */:
                this.classFile.addClassSynthAttr();
                Symbol CUP$parser$result72 = this.parser.getSymbolFactory().newSymbol("synth_attr", 66, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result72;
            case 372:
                Symbol CUP$parser$result73 = this.parser.getSymbolFactory().newSymbol("synth_attr", 66, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result73;
            case 373:
                int i161 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
                int i162 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
                String c2 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
                int i163 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i164 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String m = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i165 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i166 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String s3 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                this.classFile.addEnclMethAttr(c2, m, s3);
                Symbol CUP$parser$result74 = this.parser.getSymbolFactory().newSymbol("encl_meth_attr", 67, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), null);
                return CUP$parser$result74;
            case 374:
                Symbol CUP$parser$result75 = this.parser.getSymbolFactory().newSymbol("encl_meth_attr", 67, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result75;
            case 375:
                Symbol CUP$parser$result76 = this.parser.getSymbolFactory().newSymbol("deprecated_attr", 4, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), Jmod.ResolutionWarningReason.DEPRECATED);
                return CUP$parser$result76;
            case 376:
                Symbol CUP$parser$result77 = this.parser.getSymbolFactory().newSymbol("deprecated_attr", 4, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result77;
            case 377:
                int i167 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i168 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String sig3 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Symbol CUP$parser$result78 = this.parser.getSymbolFactory().newSymbol("signature_attr", 5, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol) CUP$parser$stack.peek(), sig3);
                return CUP$parser$result78;
            case 378:
                Symbol CUP$parser$result79 = this.parser.getSymbolFactory().newSymbol("signature_attr", 5, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result79;
            case 379:
                Symbol CUP$parser$result80 = this.parser.getSymbolFactory().newSymbol("visibility_type", 73, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "RuntimeVisible");
                return CUP$parser$result80;
            case 380:
                Symbol CUP$parser$result81 = this.parser.getSymbolFactory().newSymbol("visibility_type", 73, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "RuntimeInvisible");
                return CUP$parser$result81;
            case 381:
                Symbol CUP$parser$result82 = this.parser.getSymbolFactory().newSymbol("param_visibility_type", 84, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "RuntimeVisibleParameter");
                return CUP$parser$result82;
            case 382:
                Symbol CUP$parser$result83 = this.parser.getSymbolFactory().newSymbol("param_visibility_type", 84, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), "RuntimeInvisibleParameter");
                return CUP$parser$result83;
            case 383:
                int i169 = ((Symbol) CUP$parser$stack.peek()).left;
                int i170 = ((Symbol) CUP$parser$stack.peek()).right;
                Object attr = ((Symbol) CUP$parser$stack.peek()).value;
                Symbol CUP$parser$result84 = this.parser.getSymbolFactory().newSymbol("annotation_attr_opt", 85, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), attr);
                return CUP$parser$result84;
            case 384:
                Symbol CUP$parser$result85 = this.parser.getSymbolFactory().newSymbol("annotation_attr_opt", 85, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result85;
            case 385:
                int i171 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).left;
                int i172 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).right;
                Object tval2 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).value;
                int i173 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
                int i174 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
                Object list4 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
                Object RESULT5 = this.classFile.makeVisibilityAnnotation(tval2, list4);
                Symbol CUP$parser$result86 = this.parser.getSymbolFactory().newSymbol("annotation_attr", 74, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5), (Symbol) CUP$parser$stack.peek(), RESULT5);
                return CUP$parser$result86;
            case 386:
                int i175 = ((Symbol) CUP$parser$stack.peek()).left;
                int i176 = ((Symbol) CUP$parser$stack.peek()).right;
                Object list5 = ((Symbol) CUP$parser$stack.peek()).value;
                Symbol CUP$parser$result87 = this.parser.getSymbolFactory().newSymbol("annotation_list_opt", 86, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), list5);
                return CUP$parser$result87;
            case 387:
                Symbol CUP$parser$result88 = this.parser.getSymbolFactory().newSymbol("annotation_list_opt", 86, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result88;
            case 388:
                int i177 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i178 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Object list6 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i179 = ((Symbol) CUP$parser$stack.peek()).left;
                int i180 = ((Symbol) CUP$parser$stack.peek()).right;
                Object elem3 = ((Symbol) CUP$parser$stack.peek()).value;
                Object RESULT6 = this.classFile.mergeNewAnnotation(list6, elem3);
                Symbol CUP$parser$result89 = this.parser.getSymbolFactory().newSymbol("annotation_list", 75, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), RESULT6);
                return CUP$parser$result89;
            case 389:
                int i181 = ((Symbol) CUP$parser$stack.peek()).left;
                int i182 = ((Symbol) CUP$parser$stack.peek()).right;
                Object elem4 = ((Symbol) CUP$parser$stack.peek()).value;
                Object RESULT7 = this.classFile.makeNewAnnotationList(elem4);
                Symbol CUP$parser$result90 = this.parser.getSymbolFactory().newSymbol("annotation_list", 75, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), RESULT7);
                return CUP$parser$result90;
            case 390:
                int i183 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).left;
                int i184 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).right;
                String annotation_type = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).value;
                int i185 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
                int i186 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
                Object elems = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
                Object RESULT8 = this.classFile.makeAnnotation(annotation_type, elems);
                Symbol CUP$parser$result91 = this.parser.getSymbolFactory().newSymbol(Jimple.ANNOTATION, 76, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6), (Symbol) CUP$parser$stack.peek(), RESULT8);
                return CUP$parser$result91;
            case 391:
                int i187 = ((Symbol) CUP$parser$stack.peek()).left;
                int i188 = ((Symbol) CUP$parser$stack.peek()).right;
                Object list7 = ((Symbol) CUP$parser$stack.peek()).value;
                Symbol CUP$parser$result92 = this.parser.getSymbolFactory().newSymbol("elem_val_pair_list_opt", 87, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), list7);
                return CUP$parser$result92;
            case 392:
                Symbol CUP$parser$result93 = this.parser.getSymbolFactory().newSymbol("elem_val_pair_list_opt", 87, (Symbol) CUP$parser$stack.peek(), (Object) null);
                return CUP$parser$result93;
            case 393:
                int i189 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i190 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Object list8 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                int i191 = ((Symbol) CUP$parser$stack.peek()).left;
                int i192 = ((Symbol) CUP$parser$stack.peek()).right;
                Object elem5 = ((Symbol) CUP$parser$stack.peek()).value;
                Object RESULT9 = this.classFile.mergeNewElemValPair(list8, elem5);
                Symbol CUP$parser$result94 = this.parser.getSymbolFactory().newSymbol("elem_val_pair_list", 77, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol) CUP$parser$stack.peek(), RESULT9);
                return CUP$parser$result94;
            case 394:
                int i193 = ((Symbol) CUP$parser$stack.peek()).left;
                int i194 = ((Symbol) CUP$parser$stack.peek()).right;
                Object elem6 = ((Symbol) CUP$parser$stack.peek()).value;
                Object RESULT10 = this.classFile.makeNewElemValPairList(elem6);
                Symbol CUP$parser$result95 = this.parser.getSymbolFactory().newSymbol("elem_val_pair_list", 77, (Symbol) CUP$parser$stack.peek(), (Symbol) CUP$parser$stack.peek(), RESULT10);
                return CUP$parser$result95;
            case 395:
                int i195 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i196 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String name7 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i197 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i198 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Integer val = (Integer) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Object RESULT11 = this.classFile.makeConstantElem(name7, 'I', val);
                Symbol CUP$parser$result96 = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), RESULT11);
                return CUP$parser$result96;
            case 396:
                int i199 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i200 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String name8 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i201 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i202 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Integer val2 = (Integer) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Object RESULT12 = this.classFile.makeConstantElem(name8, 'S', val2);
                Symbol CUP$parser$result97 = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), RESULT12);
                return CUP$parser$result97;
            case 397:
                int i203 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i204 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String name9 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i205 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i206 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Integer val3 = (Integer) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Object RESULT13 = this.classFile.makeConstantElem(name9, 'B', val3);
                Symbol CUP$parser$result98 = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), RESULT13);
                return CUP$parser$result98;
            case 398:
                int i207 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i208 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String name10 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i209 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i210 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Integer val4 = (Integer) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Object RESULT14 = this.classFile.makeConstantElem(name10, 'C', val4);
                Symbol CUP$parser$result99 = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), RESULT14);
                return CUP$parser$result99;
            case 399:
                int i211 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i212 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String name11 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i213 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i214 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Integer val5 = (Integer) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Object RESULT15 = this.classFile.makeConstantElem(name11, 'Z', val5);
                Symbol CUP$parser$result100 = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), RESULT15);
                return CUP$parser$result100;
            case 400:
                int i215 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i216 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String name12 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i217 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i218 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Object val6 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Object RESULT16 = this.classFile.makeConstantElem(name12, 'J', val6);
                Symbol CUP$parser$result101 = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), RESULT16);
                return CUP$parser$result101;
            case 401:
                int i219 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i220 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String name13 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i221 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i222 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Object val7 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Object RESULT17 = this.classFile.makeConstantElem(name13, 'F', val7);
                Symbol CUP$parser$result102 = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), RESULT17);
                return CUP$parser$result102;
            case 402:
                int i223 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i224 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String name14 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i225 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i226 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Object val8 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Object RESULT18 = this.classFile.makeConstantElem(name14, 'D', val8);
                Symbol CUP$parser$result103 = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), RESULT18);
                return CUP$parser$result103;
            case 403:
                int i227 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i228 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String name15 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i229 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i230 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String val9 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Object RESULT19 = this.classFile.makeConstantElem(name15, 's', val9);
                Symbol CUP$parser$result104 = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), RESULT19);
                return CUP$parser$result104;
            case 404:
                int i231 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
                int i232 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
                String name16 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
                int i233 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i234 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String tname = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i235 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i236 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String cname = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Object RESULT20 = this.classFile.makeEnumElem(name16, 'e', tname, cname);
                Symbol CUP$parser$result105 = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5), (Symbol) CUP$parser$stack.peek(), RESULT20);
                return CUP$parser$result105;
            case 405:
                int i237 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                int i238 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                String name17 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                int i239 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int i240 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                String desc = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                Object RESULT21 = this.classFile.makeClassElem(name17, 'c', desc);
                Symbol CUP$parser$result106 = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol) CUP$parser$stack.peek(), RESULT21);
                return CUP$parser$result106;
            case 406:
                int i241 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).left;
                int i242 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).right;
                String name18 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).value;
                int i243 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
                int i244 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
                Object list9 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
                Object RESULT22 = this.classFile.makeArrayElem(name18, '[', list9);
                Symbol CUP$parser$result107 = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7), (Symbol) CUP$parser$stack.peek(), RESULT22);
                return CUP$parser$result107;
            case 407:
                int i245 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).left;
                int i246 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).right;
                String name19 = (String) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).value;
                int i247 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
                int i248 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
                Object attr2 = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
                Object RESULT23 = this.classFile.makeAnnotElem(name19, '@', attr2);
                Symbol CUP$parser$result108 = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 7), (Symbol) CUP$parser$stack.peek(), RESULT23);
                return CUP$parser$result108;
            default:
                throw new Exception("Invalid action number " + CUP$parser$act_num + "found in internal parse table");
        }
    }

    public final Symbol CUP$parser$do_action(int CUP$parser$act_num, lr_parser CUP$parser$parser, Stack CUP$parser$stack, int CUP$parser$top) throws Exception {
        switch (CUP$parser$act_num / 300) {
            case 0:
                return CUP$parser$do_action_part00000000(CUP$parser$act_num, CUP$parser$parser, CUP$parser$stack, CUP$parser$top);
            case 1:
                return CUP$parser$do_action_part00000001(CUP$parser$act_num, CUP$parser$parser, CUP$parser$stack, CUP$parser$top);
            default:
                throw new Exception("Invalid action number found in internal parse table");
        }
    }
}
