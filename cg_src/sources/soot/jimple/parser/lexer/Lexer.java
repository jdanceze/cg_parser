package soot.jimple.parser.lexer;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PushbackReader;
import soot.jimple.parser.node.EOF;
import soot.jimple.parser.node.TAbstract;
import soot.jimple.parser.node.TAnd;
import soot.jimple.parser.node.TAnnotation;
import soot.jimple.parser.node.TAtIdentifier;
import soot.jimple.parser.node.TBoolConstant;
import soot.jimple.parser.node.TBoolean;
import soot.jimple.parser.node.TBreakpoint;
import soot.jimple.parser.node.TByte;
import soot.jimple.parser.node.TCase;
import soot.jimple.parser.node.TCatch;
import soot.jimple.parser.node.TChar;
import soot.jimple.parser.node.TClass;
import soot.jimple.parser.node.TCls;
import soot.jimple.parser.node.TCmp;
import soot.jimple.parser.node.TCmpeq;
import soot.jimple.parser.node.TCmpg;
import soot.jimple.parser.node.TCmpge;
import soot.jimple.parser.node.TCmpgt;
import soot.jimple.parser.node.TCmpl;
import soot.jimple.parser.node.TCmple;
import soot.jimple.parser.node.TCmplt;
import soot.jimple.parser.node.TCmpne;
import soot.jimple.parser.node.TColon;
import soot.jimple.parser.node.TColonEquals;
import soot.jimple.parser.node.TComma;
import soot.jimple.parser.node.TDefault;
import soot.jimple.parser.node.TDiv;
import soot.jimple.parser.node.TDot;
import soot.jimple.parser.node.TDouble;
import soot.jimple.parser.node.TDynamicinvoke;
import soot.jimple.parser.node.TEntermonitor;
import soot.jimple.parser.node.TEnum;
import soot.jimple.parser.node.TEquals;
import soot.jimple.parser.node.TExitmonitor;
import soot.jimple.parser.node.TExtends;
import soot.jimple.parser.node.TFinal;
import soot.jimple.parser.node.TFloat;
import soot.jimple.parser.node.TFloatConstant;
import soot.jimple.parser.node.TFrom;
import soot.jimple.parser.node.TFullIdentifier;
import soot.jimple.parser.node.TGoto;
import soot.jimple.parser.node.TIdentifier;
import soot.jimple.parser.node.TIf;
import soot.jimple.parser.node.TIgnored;
import soot.jimple.parser.node.TImplements;
import soot.jimple.parser.node.TInstanceof;
import soot.jimple.parser.node.TInt;
import soot.jimple.parser.node.TIntegerConstant;
import soot.jimple.parser.node.TInterface;
import soot.jimple.parser.node.TInterfaceinvoke;
import soot.jimple.parser.node.TLBrace;
import soot.jimple.parser.node.TLBracket;
import soot.jimple.parser.node.TLParen;
import soot.jimple.parser.node.TLengthof;
import soot.jimple.parser.node.TLong;
import soot.jimple.parser.node.TLookupswitch;
import soot.jimple.parser.node.TMinus;
import soot.jimple.parser.node.TMod;
import soot.jimple.parser.node.TMult;
import soot.jimple.parser.node.TNative;
import soot.jimple.parser.node.TNeg;
import soot.jimple.parser.node.TNew;
import soot.jimple.parser.node.TNewarray;
import soot.jimple.parser.node.TNewmultiarray;
import soot.jimple.parser.node.TNop;
import soot.jimple.parser.node.TNull;
import soot.jimple.parser.node.TNullType;
import soot.jimple.parser.node.TOr;
import soot.jimple.parser.node.TPlus;
import soot.jimple.parser.node.TPrivate;
import soot.jimple.parser.node.TProtected;
import soot.jimple.parser.node.TPublic;
import soot.jimple.parser.node.TQuote;
import soot.jimple.parser.node.TQuotedName;
import soot.jimple.parser.node.TRBrace;
import soot.jimple.parser.node.TRBracket;
import soot.jimple.parser.node.TRParen;
import soot.jimple.parser.node.TRet;
import soot.jimple.parser.node.TReturn;
import soot.jimple.parser.node.TSemicolon;
import soot.jimple.parser.node.TShl;
import soot.jimple.parser.node.TShort;
import soot.jimple.parser.node.TShr;
import soot.jimple.parser.node.TSpecialinvoke;
import soot.jimple.parser.node.TStatic;
import soot.jimple.parser.node.TStaticinvoke;
import soot.jimple.parser.node.TStrictfp;
import soot.jimple.parser.node.TStringConstant;
import soot.jimple.parser.node.TSynchronized;
import soot.jimple.parser.node.TTableswitch;
import soot.jimple.parser.node.TThrow;
import soot.jimple.parser.node.TThrows;
import soot.jimple.parser.node.TTo;
import soot.jimple.parser.node.TTransient;
import soot.jimple.parser.node.TUnknown;
import soot.jimple.parser.node.TUshr;
import soot.jimple.parser.node.TVirtualinvoke;
import soot.jimple.parser.node.TVoid;
import soot.jimple.parser.node.TVolatile;
import soot.jimple.parser.node.TWith;
import soot.jimple.parser.node.TXor;
import soot.jimple.parser.node.Token;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/lexer/Lexer.class */
public class Lexer {
    protected Token token;
    private PushbackReader in;
    private int line;
    private int pos;
    private boolean cr;
    private boolean eof;
    private static int[][][][] gotoTable;
    private static int[][] accept;
    protected State state = State.INITIAL;
    private final StringBuffer text = new StringBuffer();

    protected void filter() throws LexerException, IOException {
    }

    public Lexer(PushbackReader in) {
        this.in = in;
    }

    public Token peek() throws LexerException, IOException {
        while (this.token == null) {
            this.token = getToken();
            filter();
        }
        return this.token;
    }

    public Token next() throws LexerException, IOException {
        while (this.token == null) {
            this.token = getToken();
            filter();
        }
        Token result = this.token;
        this.token = null;
        return result;
    }

    protected Token getToken() throws IOException, LexerException {
        int dfa_state = 0;
        int start_pos = this.pos;
        int start_line = this.line;
        int accept_state = -1;
        int accept_token = -1;
        int accept_length = -1;
        int accept_pos = -1;
        int accept_line = -1;
        int[][][] gotoTable2 = gotoTable[this.state.id()];
        int[] accept2 = accept[this.state.id()];
        this.text.setLength(0);
        while (true) {
            int c = getChar();
            if (c != -1) {
                switch (c) {
                    case 10:
                        if (this.cr) {
                            this.cr = false;
                            break;
                        } else {
                            this.line++;
                            this.pos = 0;
                            break;
                        }
                    case 11:
                    case 12:
                    default:
                        this.pos++;
                        this.cr = false;
                        break;
                    case 13:
                        this.line++;
                        this.pos = 0;
                        this.cr = true;
                        break;
                }
                this.text.append((char) c);
                do {
                    int oldState = dfa_state < -1 ? (-2) - dfa_state : dfa_state;
                    dfa_state = -1;
                    int[][] tmp1 = gotoTable2[oldState];
                    int low = 0;
                    int high = tmp1.length - 1;
                    while (true) {
                        if (low <= high) {
                            int middle = (low + high) >>> 1;
                            int[] tmp2 = tmp1[middle];
                            if (c < tmp2[0]) {
                                high = middle - 1;
                            } else if (c > tmp2[1]) {
                                low = middle + 1;
                            } else {
                                dfa_state = tmp2[2];
                            }
                        }
                    }
                } while (dfa_state < -1);
            } else {
                dfa_state = -1;
            }
            if (dfa_state >= 0) {
                if (accept2[dfa_state] != -1) {
                    accept_state = dfa_state;
                    accept_token = accept2[dfa_state];
                    accept_length = this.text.length();
                    accept_pos = this.pos;
                    accept_line = this.line;
                }
            } else if (accept_state != -1) {
                switch (accept_token) {
                    case 0:
                        Token token = new0(getText(accept_length), start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token;
                    case 1:
                        Token token2 = new1(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token2;
                    case 2:
                        Token token3 = new2(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token3;
                    case 3:
                        Token token4 = new3(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token4;
                    case 4:
                        Token token5 = new4(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token5;
                    case 5:
                        Token token6 = new5(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token6;
                    case 6:
                        Token token7 = new6(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token7;
                    case 7:
                        Token token8 = new7(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token8;
                    case 8:
                        Token token9 = new8(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token9;
                    case 9:
                        Token token10 = new9(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token10;
                    case 10:
                        Token token11 = new10(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token11;
                    case 11:
                        Token token12 = new11(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token12;
                    case 12:
                        Token token13 = new12(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token13;
                    case 13:
                        Token token14 = new13(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token14;
                    case 14:
                        Token token15 = new14(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token15;
                    case 15:
                        Token token16 = new15(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token16;
                    case 16:
                        Token token17 = new16(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token17;
                    case 17:
                        Token token18 = new17(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token18;
                    case 18:
                        Token token19 = new18(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token19;
                    case 19:
                        Token token20 = new19(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token20;
                    case 20:
                        Token token21 = new20(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token21;
                    case 21:
                        Token token22 = new21(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token22;
                    case 22:
                        Token token23 = new22(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token23;
                    case 23:
                        Token token24 = new23(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token24;
                    case 24:
                        Token token25 = new24(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token25;
                    case 25:
                        Token token26 = new25(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token26;
                    case 26:
                        Token token27 = new26(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token27;
                    case 27:
                        Token token28 = new27(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token28;
                    case 28:
                        Token token29 = new28(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token29;
                    case 29:
                        Token token30 = new29(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token30;
                    case 30:
                        Token token31 = new30(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token31;
                    case 31:
                        Token token32 = new31(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token32;
                    case 32:
                        Token token33 = new32(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token33;
                    case 33:
                        Token token34 = new33(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token34;
                    case 34:
                        Token token35 = new34(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token35;
                    case 35:
                        Token token36 = new35(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token36;
                    case 36:
                        Token token37 = new36(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token37;
                    case 37:
                        Token token38 = new37(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token38;
                    case 38:
                        Token token39 = new38(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token39;
                    case 39:
                        Token token40 = new39(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token40;
                    case 40:
                        Token token41 = new40(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token41;
                    case 41:
                        Token token42 = new41(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token42;
                    case 42:
                        Token token43 = new42(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token43;
                    case 43:
                        Token token44 = new43(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token44;
                    case 44:
                        Token token45 = new44(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token45;
                    case 45:
                        Token token46 = new45(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token46;
                    case 46:
                        Token token47 = new46(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token47;
                    case 47:
                        Token token48 = new47(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token48;
                    case 48:
                        Token token49 = new48(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token49;
                    case 49:
                        Token token50 = new49(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token50;
                    case 50:
                        Token token51 = new50(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token51;
                    case 51:
                        Token token52 = new51(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token52;
                    case 52:
                        Token token53 = new52(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token53;
                    case 53:
                        Token token54 = new53(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token54;
                    case 54:
                        Token token55 = new54(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token55;
                    case 55:
                        Token token56 = new55(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token56;
                    case 56:
                        Token token57 = new56(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token57;
                    case 57:
                        Token token58 = new57(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token58;
                    case 58:
                        Token token59 = new58(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token59;
                    case 59:
                        Token token60 = new59(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token60;
                    case 60:
                        Token token61 = new60(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token61;
                    case 61:
                        Token token62 = new61(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token62;
                    case 62:
                        Token token63 = new62(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token63;
                    case 63:
                        Token token64 = new63(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token64;
                    case 64:
                        Token token65 = new64(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token65;
                    case 65:
                        Token token66 = new65(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token66;
                    case 66:
                        Token token67 = new66(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token67;
                    case 67:
                        Token token68 = new67(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token68;
                    case 68:
                        Token token69 = new68(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token69;
                    case 69:
                        Token token70 = new69(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token70;
                    case 70:
                        Token token71 = new70(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token71;
                    case 71:
                        Token token72 = new71(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token72;
                    case 72:
                        Token token73 = new72(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token73;
                    case 73:
                        Token token74 = new73(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token74;
                    case 74:
                        Token token75 = new74(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token75;
                    case 75:
                        Token token76 = new75(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token76;
                    case 76:
                        Token token77 = new76(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token77;
                    case 77:
                        Token token78 = new77(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token78;
                    case 78:
                        Token token79 = new78(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token79;
                    case 79:
                        Token token80 = new79(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token80;
                    case 80:
                        Token token81 = new80(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token81;
                    case 81:
                        Token token82 = new81(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token82;
                    case 82:
                        Token token83 = new82(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token83;
                    case 83:
                        Token token84 = new83(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token84;
                    case 84:
                        Token token85 = new84(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token85;
                    case 85:
                        Token token86 = new85(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token86;
                    case 86:
                        Token token87 = new86(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token87;
                    case 87:
                        Token token88 = new87(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token88;
                    case 88:
                        Token token89 = new88(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token89;
                    case 89:
                        Token token90 = new89(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token90;
                    case 90:
                        Token token91 = new90(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token91;
                    case 91:
                        Token token92 = new91(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token92;
                    case 92:
                        Token token93 = new92(start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token93;
                    case 93:
                        Token token94 = new93(getText(accept_length), start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token94;
                    case 94:
                        Token token95 = new94(getText(accept_length), start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token95;
                    case 95:
                        Token token96 = new95(getText(accept_length), start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token96;
                    case 96:
                        Token token97 = new96(getText(accept_length), start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token97;
                    case 97:
                        Token token98 = new97(getText(accept_length), start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token98;
                    case 98:
                        Token token99 = new98(getText(accept_length), start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token99;
                    case 99:
                        Token token100 = new99(getText(accept_length), start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token100;
                    case 100:
                        Token token101 = new100(getText(accept_length), start_line + 1, start_pos + 1);
                        pushBack(accept_length);
                        this.pos = accept_pos;
                        this.line = accept_line;
                        return token101;
                }
            } else if (this.text.length() > 0) {
                throw new LexerException("[" + (start_line + 1) + "," + (start_pos + 1) + "] Unknown token: " + ((Object) this.text));
            } else {
                EOF token102 = new EOF(start_line + 1, start_pos + 1);
                return token102;
            }
        }
    }

    Token new0(String text, int line, int pos) {
        return new TIgnored(text, line, pos);
    }

    Token new1(int line, int pos) {
        return new TAbstract(line, pos);
    }

    Token new2(int line, int pos) {
        return new TFinal(line, pos);
    }

    Token new3(int line, int pos) {
        return new TNative(line, pos);
    }

    Token new4(int line, int pos) {
        return new TPublic(line, pos);
    }

    Token new5(int line, int pos) {
        return new TProtected(line, pos);
    }

    Token new6(int line, int pos) {
        return new TPrivate(line, pos);
    }

    Token new7(int line, int pos) {
        return new TStatic(line, pos);
    }

    Token new8(int line, int pos) {
        return new TSynchronized(line, pos);
    }

    Token new9(int line, int pos) {
        return new TTransient(line, pos);
    }

    Token new10(int line, int pos) {
        return new TVolatile(line, pos);
    }

    Token new11(int line, int pos) {
        return new TStrictfp(line, pos);
    }

    Token new12(int line, int pos) {
        return new TEnum(line, pos);
    }

    Token new13(int line, int pos) {
        return new TAnnotation(line, pos);
    }

    Token new14(int line, int pos) {
        return new TClass(line, pos);
    }

    Token new15(int line, int pos) {
        return new TInterface(line, pos);
    }

    Token new16(int line, int pos) {
        return new TVoid(line, pos);
    }

    Token new17(int line, int pos) {
        return new TBoolean(line, pos);
    }

    Token new18(int line, int pos) {
        return new TByte(line, pos);
    }

    Token new19(int line, int pos) {
        return new TShort(line, pos);
    }

    Token new20(int line, int pos) {
        return new TChar(line, pos);
    }

    Token new21(int line, int pos) {
        return new TInt(line, pos);
    }

    Token new22(int line, int pos) {
        return new TLong(line, pos);
    }

    Token new23(int line, int pos) {
        return new TFloat(line, pos);
    }

    Token new24(int line, int pos) {
        return new TDouble(line, pos);
    }

    Token new25(int line, int pos) {
        return new TNullType(line, pos);
    }

    Token new26(int line, int pos) {
        return new TUnknown(line, pos);
    }

    Token new27(int line, int pos) {
        return new TExtends(line, pos);
    }

    Token new28(int line, int pos) {
        return new TImplements(line, pos);
    }

    Token new29(int line, int pos) {
        return new TBreakpoint(line, pos);
    }

    Token new30(int line, int pos) {
        return new TCase(line, pos);
    }

    Token new31(int line, int pos) {
        return new TCatch(line, pos);
    }

    Token new32(int line, int pos) {
        return new TCmp(line, pos);
    }

    Token new33(int line, int pos) {
        return new TCmpg(line, pos);
    }

    Token new34(int line, int pos) {
        return new TCmpl(line, pos);
    }

    Token new35(int line, int pos) {
        return new TDefault(line, pos);
    }

    Token new36(int line, int pos) {
        return new TEntermonitor(line, pos);
    }

    Token new37(int line, int pos) {
        return new TExitmonitor(line, pos);
    }

    Token new38(int line, int pos) {
        return new TGoto(line, pos);
    }

    Token new39(int line, int pos) {
        return new TIf(line, pos);
    }

    Token new40(int line, int pos) {
        return new TInstanceof(line, pos);
    }

    Token new41(int line, int pos) {
        return new TInterfaceinvoke(line, pos);
    }

    Token new42(int line, int pos) {
        return new TLengthof(line, pos);
    }

    Token new43(int line, int pos) {
        return new TLookupswitch(line, pos);
    }

    Token new44(int line, int pos) {
        return new TNeg(line, pos);
    }

    Token new45(int line, int pos) {
        return new TNew(line, pos);
    }

    Token new46(int line, int pos) {
        return new TNewarray(line, pos);
    }

    Token new47(int line, int pos) {
        return new TNewmultiarray(line, pos);
    }

    Token new48(int line, int pos) {
        return new TNop(line, pos);
    }

    Token new49(int line, int pos) {
        return new TRet(line, pos);
    }

    Token new50(int line, int pos) {
        return new TReturn(line, pos);
    }

    Token new51(int line, int pos) {
        return new TSpecialinvoke(line, pos);
    }

    Token new52(int line, int pos) {
        return new TStaticinvoke(line, pos);
    }

    Token new53(int line, int pos) {
        return new TDynamicinvoke(line, pos);
    }

    Token new54(int line, int pos) {
        return new TTableswitch(line, pos);
    }

    Token new55(int line, int pos) {
        return new TThrow(line, pos);
    }

    Token new56(int line, int pos) {
        return new TThrows(line, pos);
    }

    Token new57(int line, int pos) {
        return new TVirtualinvoke(line, pos);
    }

    Token new58(int line, int pos) {
        return new TNull(line, pos);
    }

    Token new59(int line, int pos) {
        return new TFrom(line, pos);
    }

    Token new60(int line, int pos) {
        return new TTo(line, pos);
    }

    Token new61(int line, int pos) {
        return new TWith(line, pos);
    }

    Token new62(int line, int pos) {
        return new TCls(line, pos);
    }

    Token new63(int line, int pos) {
        return new TComma(line, pos);
    }

    Token new64(int line, int pos) {
        return new TLBrace(line, pos);
    }

    Token new65(int line, int pos) {
        return new TRBrace(line, pos);
    }

    Token new66(int line, int pos) {
        return new TSemicolon(line, pos);
    }

    Token new67(int line, int pos) {
        return new TLBracket(line, pos);
    }

    Token new68(int line, int pos) {
        return new TRBracket(line, pos);
    }

    Token new69(int line, int pos) {
        return new TLParen(line, pos);
    }

    Token new70(int line, int pos) {
        return new TRParen(line, pos);
    }

    Token new71(int line, int pos) {
        return new TColon(line, pos);
    }

    Token new72(int line, int pos) {
        return new TDot(line, pos);
    }

    Token new73(int line, int pos) {
        return new TQuote(line, pos);
    }

    Token new74(int line, int pos) {
        return new TColonEquals(line, pos);
    }

    Token new75(int line, int pos) {
        return new TEquals(line, pos);
    }

    Token new76(int line, int pos) {
        return new TAnd(line, pos);
    }

    Token new77(int line, int pos) {
        return new TOr(line, pos);
    }

    Token new78(int line, int pos) {
        return new TXor(line, pos);
    }

    Token new79(int line, int pos) {
        return new TMod(line, pos);
    }

    Token new80(int line, int pos) {
        return new TCmpeq(line, pos);
    }

    Token new81(int line, int pos) {
        return new TCmpne(line, pos);
    }

    Token new82(int line, int pos) {
        return new TCmpgt(line, pos);
    }

    Token new83(int line, int pos) {
        return new TCmpge(line, pos);
    }

    Token new84(int line, int pos) {
        return new TCmplt(line, pos);
    }

    Token new85(int line, int pos) {
        return new TCmple(line, pos);
    }

    Token new86(int line, int pos) {
        return new TShl(line, pos);
    }

    Token new87(int line, int pos) {
        return new TShr(line, pos);
    }

    Token new88(int line, int pos) {
        return new TUshr(line, pos);
    }

    Token new89(int line, int pos) {
        return new TPlus(line, pos);
    }

    Token new90(int line, int pos) {
        return new TMinus(line, pos);
    }

    Token new91(int line, int pos) {
        return new TMult(line, pos);
    }

    Token new92(int line, int pos) {
        return new TDiv(line, pos);
    }

    Token new93(String text, int line, int pos) {
        return new TQuotedName(text, line, pos);
    }

    Token new94(String text, int line, int pos) {
        return new TFullIdentifier(text, line, pos);
    }

    Token new95(String text, int line, int pos) {
        return new TIdentifier(text, line, pos);
    }

    Token new96(String text, int line, int pos) {
        return new TAtIdentifier(text, line, pos);
    }

    Token new97(String text, int line, int pos) {
        return new TBoolConstant(text, line, pos);
    }

    Token new98(String text, int line, int pos) {
        return new TIntegerConstant(text, line, pos);
    }

    Token new99(String text, int line, int pos) {
        return new TFloatConstant(text, line, pos);
    }

    Token new100(String text, int line, int pos) {
        return new TStringConstant(text, line, pos);
    }

    private int getChar() throws IOException {
        if (this.eof) {
            return -1;
        }
        int result = this.in.read();
        if (result == -1) {
            this.eof = true;
        }
        return result;
    }

    private void pushBack(int acceptLength) throws IOException {
        int length = this.text.length();
        for (int i = length - 1; i >= acceptLength; i--) {
            this.eof = false;
            this.in.unread(this.text.charAt(i));
        }
    }

    protected void unread(Token token) throws IOException {
        String text = token.getText();
        int length = text.length();
        for (int i = length - 1; i >= 0; i--) {
            this.eof = false;
            this.in.unread(text.charAt(i));
        }
        this.pos = token.getPos() - 1;
        this.line = token.getLine() - 1;
    }

    private String getText(int acceptLength) {
        StringBuffer s = new StringBuffer(acceptLength);
        for (int i = 0; i < acceptLength; i++) {
            s.append(this.text.charAt(i));
        }
        return s.toString();
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/parser/lexer/Lexer$State.class */
    public static class State {
        public static final State INITIAL = new State(0);
        private int id;

        private State(int id) {
            this.id = id;
        }

        public int id() {
            return this.id;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r0v5, types: [int[][][], int[][][][]] */
    static {
        try {
            DataInputStream s = new DataInputStream(new BufferedInputStream(Lexer.class.getResourceAsStream("/lexer.dat")));
            int length = s.readInt();
            gotoTable = new int[length][];
            for (int i = 0; i < gotoTable.length; i++) {
                int length2 = s.readInt();
                gotoTable[i] = new int[length2];
                for (int j = 0; j < gotoTable[i].length; j++) {
                    int length3 = s.readInt();
                    gotoTable[i][j] = new int[length3][3];
                    for (int k = 0; k < gotoTable[i][j].length; k++) {
                        for (int l = 0; l < 3; l++) {
                            gotoTable[i][j][k][l] = s.readInt();
                        }
                    }
                }
            }
            int length4 = s.readInt();
            accept = new int[length4];
            for (int i2 = 0; i2 < accept.length; i2++) {
                int length5 = s.readInt();
                accept[i2] = new int[length5];
                for (int j2 = 0; j2 < accept[i2].length; j2++) {
                    accept[i2][j2] = s.readInt();
                }
            }
            s.close();
        } catch (Exception e) {
            throw new RuntimeException("The file \"lexer.dat\" is either missing or corrupted.");
        }
    }
}
