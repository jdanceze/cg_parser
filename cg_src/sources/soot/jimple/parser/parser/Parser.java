package soot.jimple.parser.parser;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import soot.jimple.parser.analysis.Analysis;
import soot.jimple.parser.analysis.AnalysisAdapter;
import soot.jimple.parser.lexer.Lexer;
import soot.jimple.parser.lexer.LexerException;
import soot.jimple.parser.node.AAbstractModifier;
import soot.jimple.parser.node.AAndBinop;
import soot.jimple.parser.node.AAnnotationModifier;
import soot.jimple.parser.node.AArrayBrackets;
import soot.jimple.parser.node.AArrayDescriptor;
import soot.jimple.parser.node.AArrayNewExpr;
import soot.jimple.parser.node.AArrayReference;
import soot.jimple.parser.node.AAssignStatement;
import soot.jimple.parser.node.ABaseNonvoidType;
import soot.jimple.parser.node.ABinopBoolExpr;
import soot.jimple.parser.node.ABinopExpr;
import soot.jimple.parser.node.ABinopExpression;
import soot.jimple.parser.node.ABooleanBaseType;
import soot.jimple.parser.node.ABooleanBaseTypeNoName;
import soot.jimple.parser.node.ABreakpointStatement;
import soot.jimple.parser.node.AByteBaseType;
import soot.jimple.parser.node.AByteBaseTypeNoName;
import soot.jimple.parser.node.ACaseStmt;
import soot.jimple.parser.node.ACastExpression;
import soot.jimple.parser.node.ACatchClause;
import soot.jimple.parser.node.ACharBaseType;
import soot.jimple.parser.node.ACharBaseTypeNoName;
import soot.jimple.parser.node.AClassFileType;
import soot.jimple.parser.node.AClassNameBaseType;
import soot.jimple.parser.node.AClassNameMultiClassNameList;
import soot.jimple.parser.node.AClassNameSingleClassNameList;
import soot.jimple.parser.node.AClzzConstant;
import soot.jimple.parser.node.ACmpBinop;
import soot.jimple.parser.node.ACmpeqBinop;
import soot.jimple.parser.node.ACmpgBinop;
import soot.jimple.parser.node.ACmpgeBinop;
import soot.jimple.parser.node.ACmpgtBinop;
import soot.jimple.parser.node.ACmplBinop;
import soot.jimple.parser.node.ACmpleBinop;
import soot.jimple.parser.node.ACmpltBinop;
import soot.jimple.parser.node.ACmpneBinop;
import soot.jimple.parser.node.AConstantCaseLabel;
import soot.jimple.parser.node.AConstantImmediate;
import soot.jimple.parser.node.ADeclaration;
import soot.jimple.parser.node.ADefaultCaseLabel;
import soot.jimple.parser.node.ADivBinop;
import soot.jimple.parser.node.ADoubleBaseType;
import soot.jimple.parser.node.ADoubleBaseTypeNoName;
import soot.jimple.parser.node.ADynamicInvokeExpr;
import soot.jimple.parser.node.AEmptyMethodBody;
import soot.jimple.parser.node.AEntermonitorStatement;
import soot.jimple.parser.node.AEnumModifier;
import soot.jimple.parser.node.AExitmonitorStatement;
import soot.jimple.parser.node.AExtendsClause;
import soot.jimple.parser.node.AFieldMember;
import soot.jimple.parser.node.AFieldReference;
import soot.jimple.parser.node.AFieldSignature;
import soot.jimple.parser.node.AFile;
import soot.jimple.parser.node.AFileBody;
import soot.jimple.parser.node.AFinalModifier;
import soot.jimple.parser.node.AFixedArrayDescriptor;
import soot.jimple.parser.node.AFloatBaseType;
import soot.jimple.parser.node.AFloatBaseTypeNoName;
import soot.jimple.parser.node.AFloatConstant;
import soot.jimple.parser.node.AFullIdentClassName;
import soot.jimple.parser.node.AFullIdentNonvoidType;
import soot.jimple.parser.node.AFullMethodBody;
import soot.jimple.parser.node.AGotoStatement;
import soot.jimple.parser.node.AGotoStmt;
import soot.jimple.parser.node.AIdentArrayRef;
import soot.jimple.parser.node.AIdentClassName;
import soot.jimple.parser.node.AIdentName;
import soot.jimple.parser.node.AIdentNonvoidType;
import soot.jimple.parser.node.AIdentityNoTypeStatement;
import soot.jimple.parser.node.AIdentityStatement;
import soot.jimple.parser.node.AIfStatement;
import soot.jimple.parser.node.AImmediateExpression;
import soot.jimple.parser.node.AImplementsClause;
import soot.jimple.parser.node.AInstanceofExpression;
import soot.jimple.parser.node.AIntBaseType;
import soot.jimple.parser.node.AIntBaseTypeNoName;
import soot.jimple.parser.node.AIntegerConstant;
import soot.jimple.parser.node.AInterfaceFileType;
import soot.jimple.parser.node.AInterfaceNonstaticInvoke;
import soot.jimple.parser.node.AInvokeExpression;
import soot.jimple.parser.node.AInvokeStatement;
import soot.jimple.parser.node.ALabelName;
import soot.jimple.parser.node.ALabelStatement;
import soot.jimple.parser.node.ALengthofUnop;
import soot.jimple.parser.node.ALocalFieldRef;
import soot.jimple.parser.node.ALocalImmediate;
import soot.jimple.parser.node.ALocalName;
import soot.jimple.parser.node.ALocalVariable;
import soot.jimple.parser.node.ALongBaseType;
import soot.jimple.parser.node.ALongBaseTypeNoName;
import soot.jimple.parser.node.ALookupswitchStatement;
import soot.jimple.parser.node.AMethodMember;
import soot.jimple.parser.node.AMethodSignature;
import soot.jimple.parser.node.AMinusBinop;
import soot.jimple.parser.node.AModBinop;
import soot.jimple.parser.node.AMultBinop;
import soot.jimple.parser.node.AMultiArgList;
import soot.jimple.parser.node.AMultiLocalNameList;
import soot.jimple.parser.node.AMultiNameList;
import soot.jimple.parser.node.AMultiNewExpr;
import soot.jimple.parser.node.AMultiParameterList;
import soot.jimple.parser.node.ANativeModifier;
import soot.jimple.parser.node.ANegUnop;
import soot.jimple.parser.node.ANewExpression;
import soot.jimple.parser.node.ANonstaticInvokeExpr;
import soot.jimple.parser.node.ANonvoidJimpleType;
import soot.jimple.parser.node.ANopStatement;
import soot.jimple.parser.node.ANovoidType;
import soot.jimple.parser.node.ANullBaseType;
import soot.jimple.parser.node.ANullBaseTypeNoName;
import soot.jimple.parser.node.ANullConstant;
import soot.jimple.parser.node.AOrBinop;
import soot.jimple.parser.node.AParameter;
import soot.jimple.parser.node.APlusBinop;
import soot.jimple.parser.node.APrivateModifier;
import soot.jimple.parser.node.AProtectedModifier;
import soot.jimple.parser.node.APublicModifier;
import soot.jimple.parser.node.AQuotedArrayRef;
import soot.jimple.parser.node.AQuotedClassName;
import soot.jimple.parser.node.AQuotedName;
import soot.jimple.parser.node.AQuotedNonvoidType;
import soot.jimple.parser.node.AReferenceExpression;
import soot.jimple.parser.node.AReferenceVariable;
import soot.jimple.parser.node.ARetStatement;
import soot.jimple.parser.node.AReturnStatement;
import soot.jimple.parser.node.AShlBinop;
import soot.jimple.parser.node.AShortBaseType;
import soot.jimple.parser.node.AShortBaseTypeNoName;
import soot.jimple.parser.node.AShrBinop;
import soot.jimple.parser.node.ASigFieldRef;
import soot.jimple.parser.node.ASimpleNewExpr;
import soot.jimple.parser.node.ASingleArgList;
import soot.jimple.parser.node.ASingleLocalNameList;
import soot.jimple.parser.node.ASingleNameList;
import soot.jimple.parser.node.ASingleParameterList;
import soot.jimple.parser.node.ASpecialNonstaticInvoke;
import soot.jimple.parser.node.AStaticInvokeExpr;
import soot.jimple.parser.node.AStaticModifier;
import soot.jimple.parser.node.AStrictfpModifier;
import soot.jimple.parser.node.AStringConstant;
import soot.jimple.parser.node.ASynchronizedModifier;
import soot.jimple.parser.node.ATableswitchStatement;
import soot.jimple.parser.node.AThrowStatement;
import soot.jimple.parser.node.AThrowsClause;
import soot.jimple.parser.node.ATransientModifier;
import soot.jimple.parser.node.AUnknownJimpleType;
import soot.jimple.parser.node.AUnnamedMethodSignature;
import soot.jimple.parser.node.AUnopBoolExpr;
import soot.jimple.parser.node.AUnopExpr;
import soot.jimple.parser.node.AUnopExpression;
import soot.jimple.parser.node.AUshrBinop;
import soot.jimple.parser.node.AVirtualNonstaticInvoke;
import soot.jimple.parser.node.AVoidType;
import soot.jimple.parser.node.AVolatileModifier;
import soot.jimple.parser.node.AXorBinop;
import soot.jimple.parser.node.EOF;
import soot.jimple.parser.node.Node;
import soot.jimple.parser.node.PArgList;
import soot.jimple.parser.node.PArrayBrackets;
import soot.jimple.parser.node.PArrayDescriptor;
import soot.jimple.parser.node.PArrayRef;
import soot.jimple.parser.node.PBaseType;
import soot.jimple.parser.node.PBaseTypeNoName;
import soot.jimple.parser.node.PBinop;
import soot.jimple.parser.node.PBinopExpr;
import soot.jimple.parser.node.PBoolExpr;
import soot.jimple.parser.node.PCaseLabel;
import soot.jimple.parser.node.PCaseStmt;
import soot.jimple.parser.node.PCatchClause;
import soot.jimple.parser.node.PClassName;
import soot.jimple.parser.node.PClassNameList;
import soot.jimple.parser.node.PConstant;
import soot.jimple.parser.node.PDeclaration;
import soot.jimple.parser.node.PExpression;
import soot.jimple.parser.node.PExtendsClause;
import soot.jimple.parser.node.PFieldRef;
import soot.jimple.parser.node.PFieldSignature;
import soot.jimple.parser.node.PFile;
import soot.jimple.parser.node.PFileBody;
import soot.jimple.parser.node.PFileType;
import soot.jimple.parser.node.PFixedArrayDescriptor;
import soot.jimple.parser.node.PGotoStmt;
import soot.jimple.parser.node.PImmediate;
import soot.jimple.parser.node.PImplementsClause;
import soot.jimple.parser.node.PInvokeExpr;
import soot.jimple.parser.node.PJimpleType;
import soot.jimple.parser.node.PLabelName;
import soot.jimple.parser.node.PLocalName;
import soot.jimple.parser.node.PLocalNameList;
import soot.jimple.parser.node.PMember;
import soot.jimple.parser.node.PMethodBody;
import soot.jimple.parser.node.PMethodSignature;
import soot.jimple.parser.node.PModifier;
import soot.jimple.parser.node.PName;
import soot.jimple.parser.node.PNameList;
import soot.jimple.parser.node.PNewExpr;
import soot.jimple.parser.node.PNonstaticInvoke;
import soot.jimple.parser.node.PNonvoidType;
import soot.jimple.parser.node.PParameter;
import soot.jimple.parser.node.PParameterList;
import soot.jimple.parser.node.PReference;
import soot.jimple.parser.node.PStatement;
import soot.jimple.parser.node.PThrowsClause;
import soot.jimple.parser.node.PType;
import soot.jimple.parser.node.PUnnamedMethodSignature;
import soot.jimple.parser.node.PUnop;
import soot.jimple.parser.node.PUnopExpr;
import soot.jimple.parser.node.PVariable;
import soot.jimple.parser.node.Start;
import soot.jimple.parser.node.Switchable;
import soot.jimple.parser.node.TAbstract;
import soot.jimple.parser.node.TAnd;
import soot.jimple.parser.node.TAnnotation;
import soot.jimple.parser.node.TAtIdentifier;
import soot.jimple.parser.node.TBoolean;
import soot.jimple.parser.node.TBreakpoint;
import soot.jimple.parser.node.TByte;
import soot.jimple.parser.node.TCase;
import soot.jimple.parser.node.TCatch;
import soot.jimple.parser.node.TChar;
import soot.jimple.parser.node.TClass;
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
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/parser/Parser.class */
public class Parser {
    protected ArrayList<Object> nodeList;
    private final Lexer lexer;
    private int last_pos;
    private int last_line;
    private Token last_token;
    private static final int SHIFT = 0;
    private static final int REDUCE = 1;
    private static final int ACCEPT = 2;
    private static final int ERROR = 3;
    private static int[][][] actionTable;
    private static int[][][] gotoTable;
    private static String[] errorMessages;
    private static int[] errors;
    public final Analysis ignoredTokens = new AnalysisAdapter();
    private final ListIterator<Object> stack = new LinkedList().listIterator();
    private final TokenIndex converter = new TokenIndex();
    private final int[] action = new int[2];

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    protected void filter() throws ParserException, LexerException, IOException {
    }

    private void push(int numstate, ArrayList<Object> listNode, boolean hidden) throws ParserException, LexerException, IOException {
        this.nodeList = listNode;
        if (!hidden) {
            filter();
        }
        if (!this.stack.hasNext()) {
            this.stack.add(new State(numstate, this.nodeList));
            return;
        }
        State s = (State) this.stack.next();
        s.state = numstate;
        s.nodes = this.nodeList;
    }

    private int goTo(int index) {
        int state = state();
        int low = 1;
        int high = gotoTable[index].length - 1;
        int value = gotoTable[index][0][1];
        while (true) {
            if (low <= high) {
                int middle = (low + high) >>> 1;
                if (state < gotoTable[index][middle][0]) {
                    high = middle - 1;
                } else if (state > gotoTable[index][middle][0]) {
                    low = middle + 1;
                } else {
                    value = gotoTable[index][middle][1];
                    break;
                }
            } else {
                break;
            }
        }
        return value;
    }

    private int state() {
        State s = (State) this.stack.previous();
        this.stack.next();
        return s.state;
    }

    private ArrayList<Object> pop() {
        return ((State) this.stack.previous()).nodes;
    }

    private int index(Switchable token) {
        this.converter.index = -1;
        token.apply(this.converter);
        return this.converter.index;
    }

    public Start parse() throws ParserException, LexerException, IOException {
        push(0, null, true);
        List<Node> ign = null;
        while (true) {
            if (index(this.lexer.peek()) == -1) {
                if (ign == null) {
                    ign = new LinkedList<>();
                }
                ign.add(this.lexer.next());
            } else {
                if (ign != null) {
                    this.ignoredTokens.setIn(this.lexer.peek(), ign);
                    ign = null;
                }
                this.last_pos = this.lexer.peek().getPos();
                this.last_line = this.lexer.peek().getLine();
                this.last_token = this.lexer.peek();
                int index = index(this.lexer.peek());
                this.action[0] = actionTable[state()][0][1];
                this.action[1] = actionTable[state()][0][2];
                int low = 1;
                int high = actionTable[state()].length - 1;
                while (true) {
                    if (low <= high) {
                        int middle = (low + high) / 2;
                        if (index < actionTable[state()][middle][0]) {
                            high = middle - 1;
                        } else if (index > actionTable[state()][middle][0]) {
                            low = middle + 1;
                        } else {
                            this.action[0] = actionTable[state()][middle][1];
                            this.action[1] = actionTable[state()][middle][2];
                        }
                    }
                }
                switch (this.action[0]) {
                    case 0:
                        ArrayList<Object> list = new ArrayList<>();
                        list.add(this.lexer.next());
                        push(this.action[1], list, false);
                        continue;
                    case 1:
                        switch (this.action[1]) {
                            case 0:
                                ArrayList<Object> list2 = new0();
                                push(goTo(0), list2, false);
                                continue;
                            case 1:
                                ArrayList<Object> list3 = new1();
                                push(goTo(0), list3, false);
                                continue;
                            case 2:
                                ArrayList<Object> list4 = new2();
                                push(goTo(0), list4, false);
                                continue;
                            case 3:
                                ArrayList<Object> list5 = new3();
                                push(goTo(0), list5, false);
                                continue;
                            case 4:
                                ArrayList<Object> list6 = new4();
                                push(goTo(0), list6, false);
                                continue;
                            case 5:
                                ArrayList<Object> list7 = new5();
                                push(goTo(0), list7, false);
                                continue;
                            case 6:
                                ArrayList<Object> list8 = new6();
                                push(goTo(0), list8, false);
                                continue;
                            case 7:
                                ArrayList<Object> list9 = new7();
                                push(goTo(0), list9, false);
                                continue;
                            case 8:
                                ArrayList<Object> list10 = new8();
                                push(goTo(1), list10, false);
                                continue;
                            case 9:
                                ArrayList<Object> list11 = new9();
                                push(goTo(1), list11, false);
                                continue;
                            case 10:
                                ArrayList<Object> list12 = new10();
                                push(goTo(1), list12, false);
                                continue;
                            case 11:
                                ArrayList<Object> list13 = new11();
                                push(goTo(1), list13, false);
                                continue;
                            case 12:
                                ArrayList<Object> list14 = new12();
                                push(goTo(1), list14, false);
                                continue;
                            case 13:
                                ArrayList<Object> list15 = new13();
                                push(goTo(1), list15, false);
                                continue;
                            case 14:
                                ArrayList<Object> list16 = new14();
                                push(goTo(1), list16, false);
                                continue;
                            case 15:
                                ArrayList<Object> list17 = new15();
                                push(goTo(1), list17, false);
                                continue;
                            case 16:
                                ArrayList<Object> list18 = new16();
                                push(goTo(1), list18, false);
                                continue;
                            case 17:
                                ArrayList<Object> list19 = new17();
                                push(goTo(1), list19, false);
                                continue;
                            case 18:
                                ArrayList<Object> list20 = new18();
                                push(goTo(1), list20, false);
                                continue;
                            case 19:
                                ArrayList<Object> list21 = new19();
                                push(goTo(1), list21, false);
                                continue;
                            case 20:
                                ArrayList<Object> list22 = new20();
                                push(goTo(1), list22, false);
                                continue;
                            case 21:
                                ArrayList<Object> list23 = new21();
                                push(goTo(2), list23, false);
                                continue;
                            case 22:
                                ArrayList<Object> list24 = new22();
                                push(goTo(2), list24, false);
                                continue;
                            case 23:
                                ArrayList<Object> list25 = new23();
                                push(goTo(3), list25, false);
                                continue;
                            case 24:
                                ArrayList<Object> list26 = new24();
                                push(goTo(4), list26, false);
                                continue;
                            case 25:
                                ArrayList<Object> list27 = new25();
                                push(goTo(5), list27, false);
                                continue;
                            case 26:
                                ArrayList<Object> list28 = new26();
                                push(goTo(5), list28, false);
                                continue;
                            case 27:
                                ArrayList<Object> list29 = new27();
                                push(goTo(6), list29, false);
                                continue;
                            case 28:
                                ArrayList<Object> list30 = new28();
                                push(goTo(6), list30, false);
                                continue;
                            case 29:
                                ArrayList<Object> list31 = new29();
                                push(goTo(7), list31, false);
                                continue;
                            case 30:
                                ArrayList<Object> list32 = new30();
                                push(goTo(7), list32, false);
                                continue;
                            case 31:
                                ArrayList<Object> list33 = new31();
                                push(goTo(8), list33, false);
                                continue;
                            case 32:
                                ArrayList<Object> list34 = new32();
                                push(goTo(8), list34, false);
                                continue;
                            case 33:
                                ArrayList<Object> list35 = new33();
                                push(goTo(8), list35, false);
                                continue;
                            case 34:
                                ArrayList<Object> list36 = new34();
                                push(goTo(8), list36, false);
                                continue;
                            case 35:
                                ArrayList<Object> list37 = new35();
                                push(goTo(8), list37, false);
                                continue;
                            case 36:
                                ArrayList<Object> list38 = new36();
                                push(goTo(8), list38, false);
                                continue;
                            case 37:
                                ArrayList<Object> list39 = new37();
                                push(goTo(8), list39, false);
                                continue;
                            case 38:
                                ArrayList<Object> list40 = new38();
                                push(goTo(8), list40, false);
                                continue;
                            case 39:
                                ArrayList<Object> list41 = new39();
                                push(goTo(8), list41, false);
                                continue;
                            case 40:
                                ArrayList<Object> list42 = new40();
                                push(goTo(8), list42, false);
                                continue;
                            case 41:
                                ArrayList<Object> list43 = new41();
                                push(goTo(9), list43, false);
                                continue;
                            case 42:
                                ArrayList<Object> list44 = new42();
                                push(goTo(9), list44, false);
                                continue;
                            case 43:
                                ArrayList<Object> list45 = new43();
                                push(goTo(10), list45, false);
                                continue;
                            case 44:
                                ArrayList<Object> list46 = new44();
                                push(goTo(10), list46, false);
                                continue;
                            case 45:
                                ArrayList<Object> list47 = new45();
                                push(goTo(11), list47, false);
                                continue;
                            case 46:
                                ArrayList<Object> list48 = new46();
                                push(goTo(12), list48, false);
                                continue;
                            case 47:
                                ArrayList<Object> list49 = new47();
                                push(goTo(13), list49, false);
                                continue;
                            case 48:
                                ArrayList<Object> list50 = new48();
                                push(goTo(13), list50, false);
                                continue;
                            case 49:
                                ArrayList<Object> list51 = new49();
                                push(goTo(13), list51, false);
                                continue;
                            case 50:
                                ArrayList<Object> list52 = new50();
                                push(goTo(13), list52, false);
                                continue;
                            case 51:
                                ArrayList<Object> list53 = new51();
                                push(goTo(13), list53, false);
                                continue;
                            case 52:
                                ArrayList<Object> list54 = new52();
                                push(goTo(13), list54, false);
                                continue;
                            case 53:
                                ArrayList<Object> list55 = new53();
                                push(goTo(13), list55, false);
                                continue;
                            case 54:
                                ArrayList<Object> list56 = new54();
                                push(goTo(13), list56, false);
                                continue;
                            case 55:
                                ArrayList<Object> list57 = new55();
                                push(goTo(13), list57, false);
                                continue;
                            case 56:
                                ArrayList<Object> list58 = new56();
                                push(goTo(14), list58, false);
                                continue;
                            case 57:
                                ArrayList<Object> list59 = new57();
                                push(goTo(14), list59, false);
                                continue;
                            case 58:
                                ArrayList<Object> list60 = new58();
                                push(goTo(14), list60, false);
                                continue;
                            case 59:
                                ArrayList<Object> list61 = new59();
                                push(goTo(14), list61, false);
                                continue;
                            case 60:
                                ArrayList<Object> list62 = new60();
                                push(goTo(14), list62, false);
                                continue;
                            case 61:
                                ArrayList<Object> list63 = new61();
                                push(goTo(14), list63, false);
                                continue;
                            case 62:
                                ArrayList<Object> list64 = new62();
                                push(goTo(14), list64, false);
                                continue;
                            case 63:
                                ArrayList<Object> list65 = new63();
                                push(goTo(14), list65, false);
                                continue;
                            case 64:
                                ArrayList<Object> list66 = new64();
                                push(goTo(14), list66, false);
                                continue;
                            case 65:
                                ArrayList<Object> list67 = new65();
                                push(goTo(14), list67, false);
                                continue;
                            case 66:
                                ArrayList<Object> list68 = new66();
                                push(goTo(15), list68, false);
                                continue;
                            case 67:
                                ArrayList<Object> list69 = new67();
                                push(goTo(15), list69, false);
                                continue;
                            case 68:
                                ArrayList<Object> list70 = new68();
                                push(goTo(15), list70, false);
                                continue;
                            case 69:
                                ArrayList<Object> list71 = new69();
                                push(goTo(15), list71, false);
                                continue;
                            case 70:
                                ArrayList<Object> list72 = new70();
                                push(goTo(15), list72, false);
                                continue;
                            case 71:
                                ArrayList<Object> list73 = new71();
                                push(goTo(15), list73, false);
                                continue;
                            case 72:
                                ArrayList<Object> list74 = new72();
                                push(goTo(15), list74, false);
                                continue;
                            case 73:
                                ArrayList<Object> list75 = new73();
                                push(goTo(15), list75, false);
                                continue;
                            case 74:
                                ArrayList<Object> list76 = new74();
                                push(goTo(16), list76, false);
                                continue;
                            case 75:
                                ArrayList<Object> list77 = new75();
                                push(goTo(17), list77, false);
                                continue;
                            case 76:
                                ArrayList<Object> list78 = new76();
                                push(goTo(17), list78, false);
                                continue;
                            case 77:
                                ArrayList<Object> list79 = new77();
                                push(goTo(17), list79, false);
                                continue;
                            case 78:
                                ArrayList<Object> list80 = new78();
                                push(goTo(17), list80, false);
                                continue;
                            case 79:
                                ArrayList<Object> list81 = new79();
                                push(goTo(17), list81, false);
                                continue;
                            case 80:
                                ArrayList<Object> list82 = new80();
                                push(goTo(17), list82, false);
                                continue;
                            case 81:
                                ArrayList<Object> list83 = new81();
                                push(goTo(17), list83, false);
                                continue;
                            case 82:
                                ArrayList<Object> list84 = new82();
                                push(goTo(17), list84, false);
                                continue;
                            case 83:
                                ArrayList<Object> list85 = new83();
                                push(goTo(17), list85, false);
                                continue;
                            case 84:
                                ArrayList<Object> list86 = new84();
                                push(goTo(18), list86, false);
                                continue;
                            case 85:
                                ArrayList<Object> list87 = new85();
                                push(goTo(19), list87, false);
                                continue;
                            case 86:
                                ArrayList<Object> list88 = new86();
                                push(goTo(19), list88, false);
                                continue;
                            case 87:
                                ArrayList<Object> list89 = new87();
                                push(goTo(20), list89, false);
                                continue;
                            case 88:
                                ArrayList<Object> list90 = new88();
                                push(goTo(21), list90, false);
                                continue;
                            case 89:
                                ArrayList<Object> list91 = new89();
                                push(goTo(21), list91, false);
                                continue;
                            case 90:
                                ArrayList<Object> list92 = new90();
                                push(goTo(22), list92, false);
                                continue;
                            case 91:
                                ArrayList<Object> list93 = new91();
                                push(goTo(22), list93, false);
                                continue;
                            case 92:
                                ArrayList<Object> list94 = new92();
                                push(goTo(22), list94, false);
                                continue;
                            case 93:
                                ArrayList<Object> list95 = new93();
                                push(goTo(22), list95, false);
                                continue;
                            case 94:
                                ArrayList<Object> list96 = new94();
                                push(goTo(22), list96, false);
                                continue;
                            case 95:
                                ArrayList<Object> list97 = new95();
                                push(goTo(22), list97, false);
                                continue;
                            case 96:
                                ArrayList<Object> list98 = new96();
                                push(goTo(22), list98, false);
                                continue;
                            case 97:
                                ArrayList<Object> list99 = new97();
                                push(goTo(22), list99, false);
                                continue;
                            case 98:
                                ArrayList<Object> list100 = new98();
                                push(goTo(22), list100, false);
                                continue;
                            case 99:
                                ArrayList<Object> list101 = new99();
                                push(goTo(22), list101, false);
                                continue;
                            case 100:
                                ArrayList<Object> list102 = new100();
                                push(goTo(22), list102, false);
                                continue;
                            case 101:
                                ArrayList<Object> list103 = new101();
                                push(goTo(22), list103, false);
                                continue;
                            case 102:
                                ArrayList<Object> list104 = new102();
                                push(goTo(22), list104, false);
                                continue;
                            case 103:
                                ArrayList<Object> list105 = new103();
                                push(goTo(22), list105, false);
                                continue;
                            case 104:
                                ArrayList<Object> list106 = new104();
                                push(goTo(22), list106, false);
                                continue;
                            case 105:
                                ArrayList<Object> list107 = new105();
                                push(goTo(22), list107, false);
                                continue;
                            case 106:
                                ArrayList<Object> list108 = new106();
                                push(goTo(22), list108, false);
                                continue;
                            case 107:
                                ArrayList<Object> list109 = new107();
                                push(goTo(22), list109, false);
                                continue;
                            case 108:
                                ArrayList<Object> list110 = new108();
                                push(goTo(23), list110, false);
                                continue;
                            case 109:
                                ArrayList<Object> list111 = new109();
                                push(goTo(24), list111, false);
                                continue;
                            case 110:
                                ArrayList<Object> list112 = new110();
                                push(goTo(25), list112, false);
                                continue;
                            case 111:
                                ArrayList<Object> list113 = new111();
                                push(goTo(25), list113, false);
                                continue;
                            case 112:
                                ArrayList<Object> list114 = new112();
                                push(goTo(25), list114, false);
                                continue;
                            case 113:
                                ArrayList<Object> list115 = new113();
                                push(goTo(26), list115, false);
                                continue;
                            case 114:
                                ArrayList<Object> list116 = new114();
                                push(goTo(27), list116, false);
                                continue;
                            case 115:
                                ArrayList<Object> list117 = new115();
                                push(goTo(28), list117, false);
                                continue;
                            case 116:
                                ArrayList<Object> list118 = new116();
                                push(goTo(28), list118, false);
                                continue;
                            case 117:
                                ArrayList<Object> list119 = new117();
                                push(goTo(28), list119, false);
                                continue;
                            case 118:
                                ArrayList<Object> list120 = new118();
                                push(goTo(28), list120, false);
                                continue;
                            case 119:
                                ArrayList<Object> list121 = new119();
                                push(goTo(28), list121, false);
                                continue;
                            case 120:
                                ArrayList<Object> list122 = new120();
                                push(goTo(28), list122, false);
                                continue;
                            case 121:
                                ArrayList<Object> list123 = new121();
                                push(goTo(28), list123, false);
                                continue;
                            case 122:
                                ArrayList<Object> list124 = new122();
                                push(goTo(28), list124, false);
                                continue;
                            case 123:
                                ArrayList<Object> list125 = new123();
                                push(goTo(29), list125, false);
                                continue;
                            case 124:
                                ArrayList<Object> list126 = new124();
                                push(goTo(29), list126, false);
                                continue;
                            case 125:
                                ArrayList<Object> list127 = new125();
                                push(goTo(29), list127, false);
                                continue;
                            case 126:
                                ArrayList<Object> list128 = new126();
                                push(goTo(30), list128, false);
                                continue;
                            case 127:
                                ArrayList<Object> list129 = new127();
                                push(goTo(30), list129, false);
                                continue;
                            case 128:
                                ArrayList<Object> list130 = new128();
                                push(goTo(31), list130, false);
                                continue;
                            case 129:
                                ArrayList<Object> list131 = new129();
                                push(goTo(31), list131, false);
                                continue;
                            case 130:
                                ArrayList<Object> list132 = new130();
                                push(goTo(32), list132, false);
                                continue;
                            case 131:
                                ArrayList<Object> list133 = new131();
                                push(goTo(32), list133, false);
                                continue;
                            case 132:
                                ArrayList<Object> list134 = new132();
                                push(goTo(33), list134, false);
                                continue;
                            case 133:
                                ArrayList<Object> list135 = new133();
                                push(goTo(33), list135, false);
                                continue;
                            case 134:
                                ArrayList<Object> list136 = new134();
                                push(goTo(33), list136, false);
                                continue;
                            case 135:
                                ArrayList<Object> list137 = new135();
                                push(goTo(33), list137, false);
                                continue;
                            case 136:
                                ArrayList<Object> list138 = new136();
                                push(goTo(33), list138, false);
                                continue;
                            case 137:
                                ArrayList<Object> list139 = new137();
                                push(goTo(33), list139, false);
                                continue;
                            case 138:
                                ArrayList<Object> list140 = new138();
                                push(goTo(33), list140, false);
                                continue;
                            case 139:
                                ArrayList<Object> list141 = new139();
                                push(goTo(33), list141, false);
                                continue;
                            case 140:
                                ArrayList<Object> list142 = new140();
                                push(goTo(34), list142, false);
                                continue;
                            case 141:
                                ArrayList<Object> list143 = new141();
                                push(goTo(35), list143, false);
                                continue;
                            case 142:
                                ArrayList<Object> list144 = new142();
                                push(goTo(36), list144, false);
                                continue;
                            case 143:
                                ArrayList<Object> list145 = new143();
                                push(goTo(36), list145, false);
                                continue;
                            case 144:
                                ArrayList<Object> list146 = new144();
                                push(goTo(36), list146, false);
                                continue;
                            case 145:
                                ArrayList<Object> list147 = new145();
                                push(goTo(37), list147, false);
                                continue;
                            case 146:
                                ArrayList<Object> list148 = new146();
                                push(goTo(37), list148, false);
                                continue;
                            case 147:
                                ArrayList<Object> list149 = new147();
                                push(goTo(38), list149, false);
                                continue;
                            case 148:
                                ArrayList<Object> list150 = new148();
                                push(goTo(38), list150, false);
                                continue;
                            case 149:
                                ArrayList<Object> list151 = new149();
                                push(goTo(39), list151, false);
                                continue;
                            case 150:
                                ArrayList<Object> list152 = new150();
                                push(goTo(39), list152, false);
                                continue;
                            case 151:
                                ArrayList<Object> list153 = new151();
                                push(goTo(40), list153, false);
                                continue;
                            case 152:
                                ArrayList<Object> list154 = new152();
                                push(goTo(40), list154, false);
                                continue;
                            case 153:
                                ArrayList<Object> list155 = new153();
                                push(goTo(41), list155, false);
                                continue;
                            case 154:
                                ArrayList<Object> list156 = new154();
                                push(goTo(41), list156, false);
                                continue;
                            case 155:
                                ArrayList<Object> list157 = new155();
                                push(goTo(42), list157, false);
                                continue;
                            case 156:
                                ArrayList<Object> list158 = new156();
                                push(goTo(43), list158, false);
                                continue;
                            case 157:
                                ArrayList<Object> list159 = new157();
                                push(goTo(44), list159, false);
                                continue;
                            case 158:
                                ArrayList<Object> list160 = new158();
                                push(goTo(44), list160, false);
                                continue;
                            case 159:
                                ArrayList<Object> list161 = new159();
                                push(goTo(45), list161, false);
                                continue;
                            case 160:
                                ArrayList<Object> list162 = new160();
                                push(goTo(45), list162, false);
                                continue;
                            case 161:
                                ArrayList<Object> list163 = new161();
                                push(goTo(46), list163, false);
                                continue;
                            case 162:
                                ArrayList<Object> list164 = new162();
                                push(goTo(46), list164, false);
                                continue;
                            case 163:
                                ArrayList<Object> list165 = new163();
                                push(goTo(46), list165, false);
                                continue;
                            case 164:
                                ArrayList<Object> list166 = new164();
                                push(goTo(46), list166, false);
                                continue;
                            case 165:
                                ArrayList<Object> list167 = new165();
                                push(goTo(46), list167, false);
                                continue;
                            case 166:
                                ArrayList<Object> list168 = new166();
                                push(goTo(46), list168, false);
                                continue;
                            case 167:
                                ArrayList<Object> list169 = new167();
                                push(goTo(46), list169, false);
                                continue;
                            case 168:
                                ArrayList<Object> list170 = new168();
                                push(goTo(47), list170, false);
                                continue;
                            case 169:
                                ArrayList<Object> list171 = new169();
                                push(goTo(47), list171, false);
                                continue;
                            case 170:
                                ArrayList<Object> list172 = new170();
                                push(goTo(47), list172, false);
                                continue;
                            case 171:
                                ArrayList<Object> list173 = new171();
                                push(goTo(47), list173, false);
                                continue;
                            case 172:
                                ArrayList<Object> list174 = new172();
                                push(goTo(47), list174, false);
                                continue;
                            case 173:
                                ArrayList<Object> list175 = new173();
                                push(goTo(47), list175, false);
                                continue;
                            case 174:
                                ArrayList<Object> list176 = new174();
                                push(goTo(47), list176, false);
                                continue;
                            case 175:
                                ArrayList<Object> list177 = new175();
                                push(goTo(47), list177, false);
                                continue;
                            case 176:
                                ArrayList<Object> list178 = new176();
                                push(goTo(47), list178, false);
                                continue;
                            case 177:
                                ArrayList<Object> list179 = new177();
                                push(goTo(47), list179, false);
                                continue;
                            case 178:
                                ArrayList<Object> list180 = new178();
                                push(goTo(47), list180, false);
                                continue;
                            case 179:
                                ArrayList<Object> list181 = new179();
                                push(goTo(47), list181, false);
                                continue;
                            case 180:
                                ArrayList<Object> list182 = new180();
                                push(goTo(47), list182, false);
                                continue;
                            case 181:
                                ArrayList<Object> list183 = new181();
                                push(goTo(47), list183, false);
                                continue;
                            case 182:
                                ArrayList<Object> list184 = new182();
                                push(goTo(47), list184, false);
                                continue;
                            case 183:
                                ArrayList<Object> list185 = new183();
                                push(goTo(47), list185, false);
                                continue;
                            case 184:
                                ArrayList<Object> list186 = new184();
                                push(goTo(47), list186, false);
                                continue;
                            case 185:
                                ArrayList<Object> list187 = new185();
                                push(goTo(47), list187, false);
                                continue;
                            case 186:
                                ArrayList<Object> list188 = new186();
                                push(goTo(47), list188, false);
                                continue;
                            case 187:
                                ArrayList<Object> list189 = new187();
                                push(goTo(47), list189, false);
                                continue;
                            case 188:
                                ArrayList<Object> list190 = new188();
                                push(goTo(48), list190, false);
                                continue;
                            case 189:
                                ArrayList<Object> list191 = new189();
                                push(goTo(48), list191, false);
                                continue;
                            case 190:
                                ArrayList<Object> list192 = new190();
                                push(goTo(49), list192, false);
                                continue;
                            case 191:
                                ArrayList<Object> list193 = new191();
                                push(goTo(49), list193, false);
                                continue;
                            case 192:
                                ArrayList<Object> list194 = new192();
                                push(goTo(49), list194, false);
                                continue;
                            case 193:
                                ArrayList<Object> list195 = new193();
                                push(goTo(50), list195, false);
                                continue;
                            case 194:
                                ArrayList<Object> list196 = new194();
                                push(goTo(50), list196, false);
                                continue;
                            case 195:
                                ArrayList<Object> list197 = new195();
                                push(goTo(51), list197, true);
                                continue;
                            case 196:
                                ArrayList<Object> list198 = new196();
                                push(goTo(51), list198, true);
                                continue;
                            case 197:
                                ArrayList<Object> list199 = new197();
                                push(goTo(52), list199, true);
                                continue;
                            case 198:
                                ArrayList<Object> list200 = new198();
                                push(goTo(52), list200, true);
                                continue;
                            case 199:
                                ArrayList<Object> list201 = new199();
                                push(goTo(53), list201, true);
                                continue;
                            case 200:
                                ArrayList<Object> list202 = new200();
                                push(goTo(53), list202, true);
                                continue;
                            case 201:
                                ArrayList<Object> list203 = new201();
                                push(goTo(54), list203, true);
                                continue;
                            case 202:
                                ArrayList<Object> list204 = new202();
                                push(goTo(54), list204, true);
                                continue;
                            case 203:
                                ArrayList<Object> list205 = new203();
                                push(goTo(55), list205, true);
                                continue;
                            case 204:
                                ArrayList<Object> list206 = new204();
                                push(goTo(55), list206, true);
                                continue;
                            case 205:
                                ArrayList<Object> list207 = new205();
                                push(goTo(56), list207, true);
                                continue;
                            case 206:
                                ArrayList<Object> list208 = new206();
                                push(goTo(56), list208, true);
                                continue;
                            case 207:
                                ArrayList<Object> list209 = new207();
                                push(goTo(57), list209, true);
                                continue;
                            case 208:
                                ArrayList<Object> list210 = new208();
                                push(goTo(57), list210, true);
                                continue;
                            case 209:
                                ArrayList<Object> list211 = new209();
                                push(goTo(58), list211, true);
                                continue;
                            case 210:
                                ArrayList<Object> list212 = new210();
                                push(goTo(58), list212, true);
                                continue;
                            default:
                                continue;
                        }
                    case 2:
                        EOF node2 = (EOF) this.lexer.next();
                        PFile node1 = (PFile) pop().get(0);
                        Start node = new Start(node1, node2);
                        return node;
                    case 3:
                        throw new ParserException(this.last_token, "[" + this.last_line + "," + this.last_pos + "] " + errorMessages[errors[this.action[1]]]);
                }
            }
        }
    }

    ArrayList<Object> new0() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode2 = new LinkedList<>();
        PFileType pfiletypeNode3 = (PFileType) nodeArrayList1.get(0);
        PClassName pclassnameNode4 = (PClassName) nodeArrayList2.get(0);
        PFileBody pfilebodyNode7 = (PFileBody) nodeArrayList3.get(0);
        PFile pfileNode1 = new AFile(listNode2, pfiletypeNode3, pclassnameNode4, null, null, pfilebodyNode7);
        nodeList.add(pfileNode1);
        return nodeList;
    }

    ArrayList<Object> new1() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        new LinkedList();
        LinkedList<Object> listNode2 = (LinkedList) nodeArrayList1.get(0);
        if (listNode2 != null) {
            listNode3.addAll(listNode2);
        }
        PFileType pfiletypeNode4 = (PFileType) nodeArrayList2.get(0);
        PClassName pclassnameNode5 = (PClassName) nodeArrayList3.get(0);
        PFileBody pfilebodyNode8 = (PFileBody) nodeArrayList4.get(0);
        PFile pfileNode1 = new AFile(listNode3, pfiletypeNode4, pclassnameNode5, null, null, pfilebodyNode8);
        nodeList.add(pfileNode1);
        return nodeList;
    }

    ArrayList<Object> new2() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode2 = new LinkedList<>();
        PFileType pfiletypeNode3 = (PFileType) nodeArrayList1.get(0);
        PClassName pclassnameNode4 = (PClassName) nodeArrayList2.get(0);
        PExtendsClause pextendsclauseNode5 = (PExtendsClause) nodeArrayList3.get(0);
        PFileBody pfilebodyNode7 = (PFileBody) nodeArrayList4.get(0);
        PFile pfileNode1 = new AFile(listNode2, pfiletypeNode3, pclassnameNode4, pextendsclauseNode5, null, pfilebodyNode7);
        nodeList.add(pfileNode1);
        return nodeList;
    }

    ArrayList<Object> new3() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        new LinkedList();
        LinkedList<Object> listNode2 = (LinkedList) nodeArrayList1.get(0);
        if (listNode2 != null) {
            listNode3.addAll(listNode2);
        }
        PFileType pfiletypeNode4 = (PFileType) nodeArrayList2.get(0);
        PClassName pclassnameNode5 = (PClassName) nodeArrayList3.get(0);
        PExtendsClause pextendsclauseNode6 = (PExtendsClause) nodeArrayList4.get(0);
        PFileBody pfilebodyNode8 = (PFileBody) nodeArrayList5.get(0);
        PFile pfileNode1 = new AFile(listNode3, pfiletypeNode4, pclassnameNode5, pextendsclauseNode6, null, pfilebodyNode8);
        nodeList.add(pfileNode1);
        return nodeList;
    }

    ArrayList<Object> new4() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode2 = new LinkedList<>();
        PFileType pfiletypeNode3 = (PFileType) nodeArrayList1.get(0);
        PClassName pclassnameNode4 = (PClassName) nodeArrayList2.get(0);
        PImplementsClause pimplementsclauseNode6 = (PImplementsClause) nodeArrayList3.get(0);
        PFileBody pfilebodyNode7 = (PFileBody) nodeArrayList4.get(0);
        PFile pfileNode1 = new AFile(listNode2, pfiletypeNode3, pclassnameNode4, null, pimplementsclauseNode6, pfilebodyNode7);
        nodeList.add(pfileNode1);
        return nodeList;
    }

    ArrayList<Object> new5() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        new LinkedList();
        LinkedList<Object> listNode2 = (LinkedList) nodeArrayList1.get(0);
        if (listNode2 != null) {
            listNode3.addAll(listNode2);
        }
        PFileType pfiletypeNode4 = (PFileType) nodeArrayList2.get(0);
        PClassName pclassnameNode5 = (PClassName) nodeArrayList3.get(0);
        PImplementsClause pimplementsclauseNode7 = (PImplementsClause) nodeArrayList4.get(0);
        PFileBody pfilebodyNode8 = (PFileBody) nodeArrayList5.get(0);
        PFile pfileNode1 = new AFile(listNode3, pfiletypeNode4, pclassnameNode5, null, pimplementsclauseNode7, pfilebodyNode8);
        nodeList.add(pfileNode1);
        return nodeList;
    }

    ArrayList<Object> new6() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode2 = new LinkedList<>();
        PFileType pfiletypeNode3 = (PFileType) nodeArrayList1.get(0);
        PClassName pclassnameNode4 = (PClassName) nodeArrayList2.get(0);
        PExtendsClause pextendsclauseNode5 = (PExtendsClause) nodeArrayList3.get(0);
        PImplementsClause pimplementsclauseNode6 = (PImplementsClause) nodeArrayList4.get(0);
        PFileBody pfilebodyNode7 = (PFileBody) nodeArrayList5.get(0);
        PFile pfileNode1 = new AFile(listNode2, pfiletypeNode3, pclassnameNode4, pextendsclauseNode5, pimplementsclauseNode6, pfilebodyNode7);
        nodeList.add(pfileNode1);
        return nodeList;
    }

    ArrayList<Object> new7() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        new LinkedList();
        LinkedList<Object> listNode2 = (LinkedList) nodeArrayList1.get(0);
        if (listNode2 != null) {
            listNode3.addAll(listNode2);
        }
        PFileType pfiletypeNode4 = (PFileType) nodeArrayList2.get(0);
        PClassName pclassnameNode5 = (PClassName) nodeArrayList3.get(0);
        PExtendsClause pextendsclauseNode6 = (PExtendsClause) nodeArrayList4.get(0);
        PImplementsClause pimplementsclauseNode7 = (PImplementsClause) nodeArrayList5.get(0);
        PFileBody pfilebodyNode8 = (PFileBody) nodeArrayList6.get(0);
        PFile pfileNode1 = new AFile(listNode3, pfiletypeNode4, pclassnameNode5, pextendsclauseNode6, pimplementsclauseNode7, pfilebodyNode8);
        nodeList.add(pfileNode1);
        return nodeList;
    }

    ArrayList<Object> new8() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TAbstract tabstractNode2 = (TAbstract) nodeArrayList1.get(0);
        PModifier pmodifierNode1 = new AAbstractModifier(tabstractNode2);
        nodeList.add(pmodifierNode1);
        return nodeList;
    }

    ArrayList<Object> new9() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TFinal tfinalNode2 = (TFinal) nodeArrayList1.get(0);
        PModifier pmodifierNode1 = new AFinalModifier(tfinalNode2);
        nodeList.add(pmodifierNode1);
        return nodeList;
    }

    ArrayList<Object> new10() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TNative tnativeNode2 = (TNative) nodeArrayList1.get(0);
        PModifier pmodifierNode1 = new ANativeModifier(tnativeNode2);
        nodeList.add(pmodifierNode1);
        return nodeList;
    }

    ArrayList<Object> new11() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TPublic tpublicNode2 = (TPublic) nodeArrayList1.get(0);
        PModifier pmodifierNode1 = new APublicModifier(tpublicNode2);
        nodeList.add(pmodifierNode1);
        return nodeList;
    }

    ArrayList<Object> new12() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TProtected tprotectedNode2 = (TProtected) nodeArrayList1.get(0);
        PModifier pmodifierNode1 = new AProtectedModifier(tprotectedNode2);
        nodeList.add(pmodifierNode1);
        return nodeList;
    }

    ArrayList<Object> new13() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TPrivate tprivateNode2 = (TPrivate) nodeArrayList1.get(0);
        PModifier pmodifierNode1 = new APrivateModifier(tprivateNode2);
        nodeList.add(pmodifierNode1);
        return nodeList;
    }

    ArrayList<Object> new14() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TStatic tstaticNode2 = (TStatic) nodeArrayList1.get(0);
        PModifier pmodifierNode1 = new AStaticModifier(tstaticNode2);
        nodeList.add(pmodifierNode1);
        return nodeList;
    }

    ArrayList<Object> new15() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TSynchronized tsynchronizedNode2 = (TSynchronized) nodeArrayList1.get(0);
        PModifier pmodifierNode1 = new ASynchronizedModifier(tsynchronizedNode2);
        nodeList.add(pmodifierNode1);
        return nodeList;
    }

    ArrayList<Object> new16() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TTransient ttransientNode2 = (TTransient) nodeArrayList1.get(0);
        PModifier pmodifierNode1 = new ATransientModifier(ttransientNode2);
        nodeList.add(pmodifierNode1);
        return nodeList;
    }

    ArrayList<Object> new17() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TVolatile tvolatileNode2 = (TVolatile) nodeArrayList1.get(0);
        PModifier pmodifierNode1 = new AVolatileModifier(tvolatileNode2);
        nodeList.add(pmodifierNode1);
        return nodeList;
    }

    ArrayList<Object> new18() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TStrictfp tstrictfpNode2 = (TStrictfp) nodeArrayList1.get(0);
        PModifier pmodifierNode1 = new AStrictfpModifier(tstrictfpNode2);
        nodeList.add(pmodifierNode1);
        return nodeList;
    }

    ArrayList<Object> new19() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TEnum tenumNode2 = (TEnum) nodeArrayList1.get(0);
        PModifier pmodifierNode1 = new AEnumModifier(tenumNode2);
        nodeList.add(pmodifierNode1);
        return nodeList;
    }

    ArrayList<Object> new20() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TAnnotation tannotationNode2 = (TAnnotation) nodeArrayList1.get(0);
        PModifier pmodifierNode1 = new AAnnotationModifier(tannotationNode2);
        nodeList.add(pmodifierNode1);
        return nodeList;
    }

    ArrayList<Object> new21() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TClass tclassNode2 = (TClass) nodeArrayList1.get(0);
        PFileType pfiletypeNode1 = new AClassFileType(tclassNode2);
        nodeList.add(pfiletypeNode1);
        return nodeList;
    }

    ArrayList<Object> new22() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TInterface tinterfaceNode2 = (TInterface) nodeArrayList1.get(0);
        PFileType pfiletypeNode1 = new AInterfaceFileType(tinterfaceNode2);
        nodeList.add(pfiletypeNode1);
        return nodeList;
    }

    ArrayList<Object> new23() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TExtends textendsNode2 = (TExtends) nodeArrayList1.get(0);
        PClassName pclassnameNode3 = (PClassName) nodeArrayList2.get(0);
        PExtendsClause pextendsclauseNode1 = new AExtendsClause(textendsNode2, pclassnameNode3);
        nodeList.add(pextendsclauseNode1);
        return nodeList;
    }

    ArrayList<Object> new24() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TImplements timplementsNode2 = (TImplements) nodeArrayList1.get(0);
        PClassNameList pclassnamelistNode3 = (PClassNameList) nodeArrayList2.get(0);
        PImplementsClause pimplementsclauseNode1 = new AImplementsClause(timplementsNode2, pclassnamelistNode3);
        nodeList.add(pimplementsclauseNode1);
        return nodeList;
    }

    ArrayList<Object> new25() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        TLBrace tlbraceNode2 = (TLBrace) nodeArrayList1.get(0);
        TRBrace trbraceNode4 = (TRBrace) nodeArrayList2.get(0);
        PFileBody pfilebodyNode1 = new AFileBody(tlbraceNode2, listNode3, trbraceNode4);
        nodeList.add(pfilebodyNode1);
        return nodeList;
    }

    ArrayList<Object> new26() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode4 = new LinkedList<>();
        TLBrace tlbraceNode2 = (TLBrace) nodeArrayList1.get(0);
        new LinkedList();
        LinkedList<Object> listNode3 = (LinkedList) nodeArrayList2.get(0);
        if (listNode3 != null) {
            listNode4.addAll(listNode3);
        }
        TRBrace trbraceNode5 = (TRBrace) nodeArrayList3.get(0);
        PFileBody pfilebodyNode1 = new AFileBody(tlbraceNode2, listNode4, trbraceNode5);
        nodeList.add(pfilebodyNode1);
        return nodeList;
    }

    ArrayList<Object> new27() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PName pnameNode2 = (PName) nodeArrayList1.get(0);
        PNameList pnamelistNode1 = new ASingleNameList(pnameNode2);
        nodeList.add(pnamelistNode1);
        return nodeList;
    }

    ArrayList<Object> new28() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        PName pnameNode2 = (PName) nodeArrayList1.get(0);
        TComma tcommaNode3 = (TComma) nodeArrayList2.get(0);
        PNameList pnamelistNode4 = (PNameList) nodeArrayList3.get(0);
        PNameList pnamelistNode1 = new AMultiNameList(pnameNode2, tcommaNode3, pnamelistNode4);
        nodeList.add(pnamelistNode1);
        return nodeList;
    }

    ArrayList<Object> new29() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PClassName pclassnameNode2 = (PClassName) nodeArrayList1.get(0);
        PClassNameList pclassnamelistNode1 = new AClassNameSingleClassNameList(pclassnameNode2);
        nodeList.add(pclassnamelistNode1);
        return nodeList;
    }

    ArrayList<Object> new30() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        PClassName pclassnameNode2 = (PClassName) nodeArrayList1.get(0);
        TComma tcommaNode3 = (TComma) nodeArrayList2.get(0);
        PClassNameList pclassnamelistNode4 = (PClassNameList) nodeArrayList3.get(0);
        PClassNameList pclassnamelistNode1 = new AClassNameMultiClassNameList(pclassnameNode2, tcommaNode3, pclassnamelistNode4);
        nodeList.add(pclassnamelistNode1);
        return nodeList;
    }

    ArrayList<Object> new31() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode2 = new LinkedList<>();
        PType ptypeNode3 = (PType) nodeArrayList1.get(0);
        PName pnameNode4 = (PName) nodeArrayList2.get(0);
        TSemicolon tsemicolonNode5 = (TSemicolon) nodeArrayList3.get(0);
        PMember pmemberNode1 = new AFieldMember(listNode2, ptypeNode3, pnameNode4, tsemicolonNode5);
        nodeList.add(pmemberNode1);
        return nodeList;
    }

    ArrayList<Object> new32() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        new LinkedList();
        LinkedList<Object> listNode2 = (LinkedList) nodeArrayList1.get(0);
        if (listNode2 != null) {
            listNode3.addAll(listNode2);
        }
        PType ptypeNode4 = (PType) nodeArrayList2.get(0);
        PName pnameNode5 = (PName) nodeArrayList3.get(0);
        TSemicolon tsemicolonNode6 = (TSemicolon) nodeArrayList4.get(0);
        PMember pmemberNode1 = new AFieldMember(listNode3, ptypeNode4, pnameNode5, tsemicolonNode6);
        nodeList.add(pmemberNode1);
        return nodeList;
    }

    ArrayList<Object> new33() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode2 = new LinkedList<>();
        PType ptypeNode3 = (PType) nodeArrayList1.get(0);
        PName pnameNode4 = (PName) nodeArrayList2.get(0);
        TLParen tlparenNode5 = (TLParen) nodeArrayList3.get(0);
        TRParen trparenNode7 = (TRParen) nodeArrayList4.get(0);
        PMethodBody pmethodbodyNode9 = (PMethodBody) nodeArrayList5.get(0);
        PMember pmemberNode1 = new AMethodMember(listNode2, ptypeNode3, pnameNode4, tlparenNode5, null, trparenNode7, null, pmethodbodyNode9);
        nodeList.add(pmemberNode1);
        return nodeList;
    }

    ArrayList<Object> new34() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        new LinkedList();
        LinkedList<Object> listNode2 = (LinkedList) nodeArrayList1.get(0);
        if (listNode2 != null) {
            listNode3.addAll(listNode2);
        }
        PType ptypeNode4 = (PType) nodeArrayList2.get(0);
        PName pnameNode5 = (PName) nodeArrayList3.get(0);
        TLParen tlparenNode6 = (TLParen) nodeArrayList4.get(0);
        TRParen trparenNode8 = (TRParen) nodeArrayList5.get(0);
        PMethodBody pmethodbodyNode10 = (PMethodBody) nodeArrayList6.get(0);
        PMember pmemberNode1 = new AMethodMember(listNode3, ptypeNode4, pnameNode5, tlparenNode6, null, trparenNode8, null, pmethodbodyNode10);
        nodeList.add(pmemberNode1);
        return nodeList;
    }

    ArrayList<Object> new35() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode2 = new LinkedList<>();
        PType ptypeNode3 = (PType) nodeArrayList1.get(0);
        PName pnameNode4 = (PName) nodeArrayList2.get(0);
        TLParen tlparenNode5 = (TLParen) nodeArrayList3.get(0);
        PParameterList pparameterlistNode6 = (PParameterList) nodeArrayList4.get(0);
        TRParen trparenNode7 = (TRParen) nodeArrayList5.get(0);
        PMethodBody pmethodbodyNode9 = (PMethodBody) nodeArrayList6.get(0);
        PMember pmemberNode1 = new AMethodMember(listNode2, ptypeNode3, pnameNode4, tlparenNode5, pparameterlistNode6, trparenNode7, null, pmethodbodyNode9);
        nodeList.add(pmemberNode1);
        return nodeList;
    }

    ArrayList<Object> new36() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList7 = pop();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        new LinkedList();
        LinkedList<Object> listNode2 = (LinkedList) nodeArrayList1.get(0);
        if (listNode2 != null) {
            listNode3.addAll(listNode2);
        }
        PType ptypeNode4 = (PType) nodeArrayList2.get(0);
        PName pnameNode5 = (PName) nodeArrayList3.get(0);
        TLParen tlparenNode6 = (TLParen) nodeArrayList4.get(0);
        PParameterList pparameterlistNode7 = (PParameterList) nodeArrayList5.get(0);
        TRParen trparenNode8 = (TRParen) nodeArrayList6.get(0);
        PMethodBody pmethodbodyNode10 = (PMethodBody) nodeArrayList7.get(0);
        PMember pmemberNode1 = new AMethodMember(listNode3, ptypeNode4, pnameNode5, tlparenNode6, pparameterlistNode7, trparenNode8, null, pmethodbodyNode10);
        nodeList.add(pmemberNode1);
        return nodeList;
    }

    ArrayList<Object> new37() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode2 = new LinkedList<>();
        PType ptypeNode3 = (PType) nodeArrayList1.get(0);
        PName pnameNode4 = (PName) nodeArrayList2.get(0);
        TLParen tlparenNode5 = (TLParen) nodeArrayList3.get(0);
        TRParen trparenNode7 = (TRParen) nodeArrayList4.get(0);
        PThrowsClause pthrowsclauseNode8 = (PThrowsClause) nodeArrayList5.get(0);
        PMethodBody pmethodbodyNode9 = (PMethodBody) nodeArrayList6.get(0);
        PMember pmemberNode1 = new AMethodMember(listNode2, ptypeNode3, pnameNode4, tlparenNode5, null, trparenNode7, pthrowsclauseNode8, pmethodbodyNode9);
        nodeList.add(pmemberNode1);
        return nodeList;
    }

    ArrayList<Object> new38() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList7 = pop();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        new LinkedList();
        LinkedList<Object> listNode2 = (LinkedList) nodeArrayList1.get(0);
        if (listNode2 != null) {
            listNode3.addAll(listNode2);
        }
        PType ptypeNode4 = (PType) nodeArrayList2.get(0);
        PName pnameNode5 = (PName) nodeArrayList3.get(0);
        TLParen tlparenNode6 = (TLParen) nodeArrayList4.get(0);
        TRParen trparenNode8 = (TRParen) nodeArrayList5.get(0);
        PThrowsClause pthrowsclauseNode9 = (PThrowsClause) nodeArrayList6.get(0);
        PMethodBody pmethodbodyNode10 = (PMethodBody) nodeArrayList7.get(0);
        PMember pmemberNode1 = new AMethodMember(listNode3, ptypeNode4, pnameNode5, tlparenNode6, null, trparenNode8, pthrowsclauseNode9, pmethodbodyNode10);
        nodeList.add(pmemberNode1);
        return nodeList;
    }

    ArrayList<Object> new39() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList7 = pop();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode2 = new LinkedList<>();
        PType ptypeNode3 = (PType) nodeArrayList1.get(0);
        PName pnameNode4 = (PName) nodeArrayList2.get(0);
        TLParen tlparenNode5 = (TLParen) nodeArrayList3.get(0);
        PParameterList pparameterlistNode6 = (PParameterList) nodeArrayList4.get(0);
        TRParen trparenNode7 = (TRParen) nodeArrayList5.get(0);
        PThrowsClause pthrowsclauseNode8 = (PThrowsClause) nodeArrayList6.get(0);
        PMethodBody pmethodbodyNode9 = (PMethodBody) nodeArrayList7.get(0);
        PMember pmemberNode1 = new AMethodMember(listNode2, ptypeNode3, pnameNode4, tlparenNode5, pparameterlistNode6, trparenNode7, pthrowsclauseNode8, pmethodbodyNode9);
        nodeList.add(pmemberNode1);
        return nodeList;
    }

    ArrayList<Object> new40() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList8 = pop();
        ArrayList<Object> nodeArrayList7 = pop();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        new LinkedList();
        LinkedList<Object> listNode2 = (LinkedList) nodeArrayList1.get(0);
        if (listNode2 != null) {
            listNode3.addAll(listNode2);
        }
        PType ptypeNode4 = (PType) nodeArrayList2.get(0);
        PName pnameNode5 = (PName) nodeArrayList3.get(0);
        TLParen tlparenNode6 = (TLParen) nodeArrayList4.get(0);
        PParameterList pparameterlistNode7 = (PParameterList) nodeArrayList5.get(0);
        TRParen trparenNode8 = (TRParen) nodeArrayList6.get(0);
        PThrowsClause pthrowsclauseNode9 = (PThrowsClause) nodeArrayList7.get(0);
        PMethodBody pmethodbodyNode10 = (PMethodBody) nodeArrayList8.get(0);
        PMember pmemberNode1 = new AMethodMember(listNode3, ptypeNode4, pnameNode5, tlparenNode6, pparameterlistNode7, trparenNode8, pthrowsclauseNode9, pmethodbodyNode10);
        nodeList.add(pmemberNode1);
        return nodeList;
    }

    ArrayList<Object> new41() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TVoid tvoidNode2 = (TVoid) nodeArrayList1.get(0);
        PType ptypeNode1 = new AVoidType(tvoidNode2);
        nodeList.add(ptypeNode1);
        return nodeList;
    }

    ArrayList<Object> new42() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PNonvoidType pnonvoidtypeNode2 = (PNonvoidType) nodeArrayList1.get(0);
        PType ptypeNode1 = new ANovoidType(pnonvoidtypeNode2);
        nodeList.add(ptypeNode1);
        return nodeList;
    }

    ArrayList<Object> new43() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PParameter pparameterNode2 = (PParameter) nodeArrayList1.get(0);
        PParameterList pparameterlistNode1 = new ASingleParameterList(pparameterNode2);
        nodeList.add(pparameterlistNode1);
        return nodeList;
    }

    ArrayList<Object> new44() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        PParameter pparameterNode2 = (PParameter) nodeArrayList1.get(0);
        TComma tcommaNode3 = (TComma) nodeArrayList2.get(0);
        PParameterList pparameterlistNode4 = (PParameterList) nodeArrayList3.get(0);
        PParameterList pparameterlistNode1 = new AMultiParameterList(pparameterNode2, tcommaNode3, pparameterlistNode4);
        nodeList.add(pparameterlistNode1);
        return nodeList;
    }

    ArrayList<Object> new45() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PNonvoidType pnonvoidtypeNode2 = (PNonvoidType) nodeArrayList1.get(0);
        PParameter pparameterNode1 = new AParameter(pnonvoidtypeNode2);
        nodeList.add(pparameterNode1);
        return nodeList;
    }

    ArrayList<Object> new46() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TThrows tthrowsNode2 = (TThrows) nodeArrayList1.get(0);
        PClassNameList pclassnamelistNode3 = (PClassNameList) nodeArrayList2.get(0);
        PThrowsClause pthrowsclauseNode1 = new AThrowsClause(tthrowsNode2, pclassnamelistNode3);
        nodeList.add(pthrowsclauseNode1);
        return nodeList;
    }

    ArrayList<Object> new47() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TBoolean tbooleanNode2 = (TBoolean) nodeArrayList1.get(0);
        PBaseTypeNoName pbasetypenonameNode1 = new ABooleanBaseTypeNoName(tbooleanNode2);
        nodeList.add(pbasetypenonameNode1);
        return nodeList;
    }

    ArrayList<Object> new48() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TByte tbyteNode2 = (TByte) nodeArrayList1.get(0);
        PBaseTypeNoName pbasetypenonameNode1 = new AByteBaseTypeNoName(tbyteNode2);
        nodeList.add(pbasetypenonameNode1);
        return nodeList;
    }

    ArrayList<Object> new49() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TChar tcharNode2 = (TChar) nodeArrayList1.get(0);
        PBaseTypeNoName pbasetypenonameNode1 = new ACharBaseTypeNoName(tcharNode2);
        nodeList.add(pbasetypenonameNode1);
        return nodeList;
    }

    ArrayList<Object> new50() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TShort tshortNode2 = (TShort) nodeArrayList1.get(0);
        PBaseTypeNoName pbasetypenonameNode1 = new AShortBaseTypeNoName(tshortNode2);
        nodeList.add(pbasetypenonameNode1);
        return nodeList;
    }

    ArrayList<Object> new51() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TInt tintNode2 = (TInt) nodeArrayList1.get(0);
        PBaseTypeNoName pbasetypenonameNode1 = new AIntBaseTypeNoName(tintNode2);
        nodeList.add(pbasetypenonameNode1);
        return nodeList;
    }

    ArrayList<Object> new52() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TLong tlongNode2 = (TLong) nodeArrayList1.get(0);
        PBaseTypeNoName pbasetypenonameNode1 = new ALongBaseTypeNoName(tlongNode2);
        nodeList.add(pbasetypenonameNode1);
        return nodeList;
    }

    ArrayList<Object> new53() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TFloat tfloatNode2 = (TFloat) nodeArrayList1.get(0);
        PBaseTypeNoName pbasetypenonameNode1 = new AFloatBaseTypeNoName(tfloatNode2);
        nodeList.add(pbasetypenonameNode1);
        return nodeList;
    }

    ArrayList<Object> new54() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TDouble tdoubleNode2 = (TDouble) nodeArrayList1.get(0);
        PBaseTypeNoName pbasetypenonameNode1 = new ADoubleBaseTypeNoName(tdoubleNode2);
        nodeList.add(pbasetypenonameNode1);
        return nodeList;
    }

    ArrayList<Object> new55() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TNullType tnulltypeNode2 = (TNullType) nodeArrayList1.get(0);
        PBaseTypeNoName pbasetypenonameNode1 = new ANullBaseTypeNoName(tnulltypeNode2);
        nodeList.add(pbasetypenonameNode1);
        return nodeList;
    }

    ArrayList<Object> new56() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TBoolean tbooleanNode2 = (TBoolean) nodeArrayList1.get(0);
        PBaseType pbasetypeNode1 = new ABooleanBaseType(tbooleanNode2);
        nodeList.add(pbasetypeNode1);
        return nodeList;
    }

    ArrayList<Object> new57() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TByte tbyteNode2 = (TByte) nodeArrayList1.get(0);
        PBaseType pbasetypeNode1 = new AByteBaseType(tbyteNode2);
        nodeList.add(pbasetypeNode1);
        return nodeList;
    }

    ArrayList<Object> new58() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TChar tcharNode2 = (TChar) nodeArrayList1.get(0);
        PBaseType pbasetypeNode1 = new ACharBaseType(tcharNode2);
        nodeList.add(pbasetypeNode1);
        return nodeList;
    }

    ArrayList<Object> new59() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TShort tshortNode2 = (TShort) nodeArrayList1.get(0);
        PBaseType pbasetypeNode1 = new AShortBaseType(tshortNode2);
        nodeList.add(pbasetypeNode1);
        return nodeList;
    }

    ArrayList<Object> new60() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TInt tintNode2 = (TInt) nodeArrayList1.get(0);
        PBaseType pbasetypeNode1 = new AIntBaseType(tintNode2);
        nodeList.add(pbasetypeNode1);
        return nodeList;
    }

    ArrayList<Object> new61() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TLong tlongNode2 = (TLong) nodeArrayList1.get(0);
        PBaseType pbasetypeNode1 = new ALongBaseType(tlongNode2);
        nodeList.add(pbasetypeNode1);
        return nodeList;
    }

    ArrayList<Object> new62() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TFloat tfloatNode2 = (TFloat) nodeArrayList1.get(0);
        PBaseType pbasetypeNode1 = new AFloatBaseType(tfloatNode2);
        nodeList.add(pbasetypeNode1);
        return nodeList;
    }

    ArrayList<Object> new63() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TDouble tdoubleNode2 = (TDouble) nodeArrayList1.get(0);
        PBaseType pbasetypeNode1 = new ADoubleBaseType(tdoubleNode2);
        nodeList.add(pbasetypeNode1);
        return nodeList;
    }

    ArrayList<Object> new64() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TNullType tnulltypeNode2 = (TNullType) nodeArrayList1.get(0);
        PBaseType pbasetypeNode1 = new ANullBaseType(tnulltypeNode2);
        nodeList.add(pbasetypeNode1);
        return nodeList;
    }

    ArrayList<Object> new65() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PClassName pclassnameNode2 = (PClassName) nodeArrayList1.get(0);
        PBaseType pbasetypeNode1 = new AClassNameBaseType(pclassnameNode2);
        nodeList.add(pbasetypeNode1);
        return nodeList;
    }

    ArrayList<Object> new66() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        PBaseTypeNoName pbasetypenonameNode2 = (PBaseTypeNoName) nodeArrayList1.get(0);
        PNonvoidType pnonvoidtypeNode1 = new ABaseNonvoidType(pbasetypenonameNode2, listNode3);
        nodeList.add(pnonvoidtypeNode1);
        return nodeList;
    }

    ArrayList<Object> new67() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode4 = new LinkedList<>();
        PBaseTypeNoName pbasetypenonameNode2 = (PBaseTypeNoName) nodeArrayList1.get(0);
        new LinkedList();
        LinkedList<Object> listNode3 = (LinkedList) nodeArrayList2.get(0);
        if (listNode3 != null) {
            listNode4.addAll(listNode3);
        }
        PNonvoidType pnonvoidtypeNode1 = new ABaseNonvoidType(pbasetypenonameNode2, listNode4);
        nodeList.add(pnonvoidtypeNode1);
        return nodeList;
    }

    ArrayList<Object> new68() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        TQuotedName tquotednameNode2 = (TQuotedName) nodeArrayList1.get(0);
        PNonvoidType pnonvoidtypeNode1 = new AQuotedNonvoidType(tquotednameNode2, listNode3);
        nodeList.add(pnonvoidtypeNode1);
        return nodeList;
    }

    ArrayList<Object> new69() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode4 = new LinkedList<>();
        TQuotedName tquotednameNode2 = (TQuotedName) nodeArrayList1.get(0);
        new LinkedList();
        LinkedList<Object> listNode3 = (LinkedList) nodeArrayList2.get(0);
        if (listNode3 != null) {
            listNode4.addAll(listNode3);
        }
        PNonvoidType pnonvoidtypeNode1 = new AQuotedNonvoidType(tquotednameNode2, listNode4);
        nodeList.add(pnonvoidtypeNode1);
        return nodeList;
    }

    ArrayList<Object> new70() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        TIdentifier tidentifierNode2 = (TIdentifier) nodeArrayList1.get(0);
        PNonvoidType pnonvoidtypeNode1 = new AIdentNonvoidType(tidentifierNode2, listNode3);
        nodeList.add(pnonvoidtypeNode1);
        return nodeList;
    }

    ArrayList<Object> new71() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode4 = new LinkedList<>();
        TIdentifier tidentifierNode2 = (TIdentifier) nodeArrayList1.get(0);
        new LinkedList();
        LinkedList<Object> listNode3 = (LinkedList) nodeArrayList2.get(0);
        if (listNode3 != null) {
            listNode4.addAll(listNode3);
        }
        PNonvoidType pnonvoidtypeNode1 = new AIdentNonvoidType(tidentifierNode2, listNode4);
        nodeList.add(pnonvoidtypeNode1);
        return nodeList;
    }

    ArrayList<Object> new72() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        TFullIdentifier tfullidentifierNode2 = (TFullIdentifier) nodeArrayList1.get(0);
        PNonvoidType pnonvoidtypeNode1 = new AFullIdentNonvoidType(tfullidentifierNode2, listNode3);
        nodeList.add(pnonvoidtypeNode1);
        return nodeList;
    }

    ArrayList<Object> new73() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode4 = new LinkedList<>();
        TFullIdentifier tfullidentifierNode2 = (TFullIdentifier) nodeArrayList1.get(0);
        new LinkedList();
        LinkedList<Object> listNode3 = (LinkedList) nodeArrayList2.get(0);
        if (listNode3 != null) {
            listNode4.addAll(listNode3);
        }
        PNonvoidType pnonvoidtypeNode1 = new AFullIdentNonvoidType(tfullidentifierNode2, listNode4);
        nodeList.add(pnonvoidtypeNode1);
        return nodeList;
    }

    ArrayList<Object> new74() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TLBracket tlbracketNode2 = (TLBracket) nodeArrayList1.get(0);
        TRBracket trbracketNode3 = (TRBracket) nodeArrayList2.get(0);
        PArrayBrackets parraybracketsNode1 = new AArrayBrackets(tlbracketNode2, trbracketNode3);
        nodeList.add(parraybracketsNode1);
        return nodeList;
    }

    ArrayList<Object> new75() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TSemicolon tsemicolonNode2 = (TSemicolon) nodeArrayList1.get(0);
        PMethodBody pmethodbodyNode1 = new AEmptyMethodBody(tsemicolonNode2);
        nodeList.add(pmethodbodyNode1);
        return nodeList;
    }

    ArrayList<Object> new76() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        LinkedList<Object> listNode4 = new LinkedList<>();
        LinkedList<Object> listNode5 = new LinkedList<>();
        TLBrace tlbraceNode2 = (TLBrace) nodeArrayList1.get(0);
        TRBrace trbraceNode6 = (TRBrace) nodeArrayList2.get(0);
        PMethodBody pmethodbodyNode1 = new AFullMethodBody(tlbraceNode2, listNode3, listNode4, listNode5, trbraceNode6);
        nodeList.add(pmethodbodyNode1);
        return nodeList;
    }

    ArrayList<Object> new77() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode4 = new LinkedList<>();
        LinkedList<Object> listNode5 = new LinkedList<>();
        LinkedList<Object> listNode6 = new LinkedList<>();
        TLBrace tlbraceNode2 = (TLBrace) nodeArrayList1.get(0);
        new LinkedList();
        LinkedList<Object> listNode3 = (LinkedList) nodeArrayList2.get(0);
        if (listNode3 != null) {
            listNode4.addAll(listNode3);
        }
        TRBrace trbraceNode7 = (TRBrace) nodeArrayList3.get(0);
        PMethodBody pmethodbodyNode1 = new AFullMethodBody(tlbraceNode2, listNode4, listNode5, listNode6, trbraceNode7);
        nodeList.add(pmethodbodyNode1);
        return nodeList;
    }

    ArrayList<Object> new78() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        LinkedList<Object> listNode5 = new LinkedList<>();
        LinkedList<Object> listNode6 = new LinkedList<>();
        TLBrace tlbraceNode2 = (TLBrace) nodeArrayList1.get(0);
        new LinkedList();
        LinkedList<Object> listNode4 = (LinkedList) nodeArrayList2.get(0);
        if (listNode4 != null) {
            listNode5.addAll(listNode4);
        }
        TRBrace trbraceNode7 = (TRBrace) nodeArrayList3.get(0);
        PMethodBody pmethodbodyNode1 = new AFullMethodBody(tlbraceNode2, listNode3, listNode5, listNode6, trbraceNode7);
        nodeList.add(pmethodbodyNode1);
        return nodeList;
    }

    ArrayList<Object> new79() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode4 = new LinkedList<>();
        LinkedList<Object> listNode6 = new LinkedList<>();
        LinkedList<Object> listNode7 = new LinkedList<>();
        TLBrace tlbraceNode2 = (TLBrace) nodeArrayList1.get(0);
        new LinkedList();
        LinkedList<Object> listNode3 = (LinkedList) nodeArrayList2.get(0);
        if (listNode3 != null) {
            listNode4.addAll(listNode3);
        }
        new LinkedList();
        LinkedList<Object> listNode5 = (LinkedList) nodeArrayList3.get(0);
        if (listNode5 != null) {
            listNode6.addAll(listNode5);
        }
        TRBrace trbraceNode8 = (TRBrace) nodeArrayList4.get(0);
        PMethodBody pmethodbodyNode1 = new AFullMethodBody(tlbraceNode2, listNode4, listNode6, listNode7, trbraceNode8);
        nodeList.add(pmethodbodyNode1);
        return nodeList;
    }

    ArrayList<Object> new80() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        LinkedList<Object> listNode4 = new LinkedList<>();
        LinkedList<Object> listNode6 = new LinkedList<>();
        TLBrace tlbraceNode2 = (TLBrace) nodeArrayList1.get(0);
        new LinkedList();
        LinkedList<Object> listNode5 = (LinkedList) nodeArrayList2.get(0);
        if (listNode5 != null) {
            listNode6.addAll(listNode5);
        }
        TRBrace trbraceNode7 = (TRBrace) nodeArrayList3.get(0);
        PMethodBody pmethodbodyNode1 = new AFullMethodBody(tlbraceNode2, listNode3, listNode4, listNode6, trbraceNode7);
        nodeList.add(pmethodbodyNode1);
        return nodeList;
    }

    ArrayList<Object> new81() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode4 = new LinkedList<>();
        LinkedList<Object> listNode5 = new LinkedList<>();
        LinkedList<Object> listNode7 = new LinkedList<>();
        TLBrace tlbraceNode2 = (TLBrace) nodeArrayList1.get(0);
        new LinkedList();
        LinkedList<Object> listNode3 = (LinkedList) nodeArrayList2.get(0);
        if (listNode3 != null) {
            listNode4.addAll(listNode3);
        }
        new LinkedList();
        LinkedList<Object> listNode6 = (LinkedList) nodeArrayList3.get(0);
        if (listNode6 != null) {
            listNode7.addAll(listNode6);
        }
        TRBrace trbraceNode8 = (TRBrace) nodeArrayList4.get(0);
        PMethodBody pmethodbodyNode1 = new AFullMethodBody(tlbraceNode2, listNode4, listNode5, listNode7, trbraceNode8);
        nodeList.add(pmethodbodyNode1);
        return nodeList;
    }

    ArrayList<Object> new82() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        LinkedList<Object> listNode5 = new LinkedList<>();
        LinkedList<Object> listNode7 = new LinkedList<>();
        TLBrace tlbraceNode2 = (TLBrace) nodeArrayList1.get(0);
        new LinkedList();
        LinkedList<Object> listNode4 = (LinkedList) nodeArrayList2.get(0);
        if (listNode4 != null) {
            listNode5.addAll(listNode4);
        }
        new LinkedList();
        LinkedList<Object> listNode6 = (LinkedList) nodeArrayList3.get(0);
        if (listNode6 != null) {
            listNode7.addAll(listNode6);
        }
        TRBrace trbraceNode8 = (TRBrace) nodeArrayList4.get(0);
        PMethodBody pmethodbodyNode1 = new AFullMethodBody(tlbraceNode2, listNode3, listNode5, listNode7, trbraceNode8);
        nodeList.add(pmethodbodyNode1);
        return nodeList;
    }

    ArrayList<Object> new83() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode4 = new LinkedList<>();
        LinkedList<Object> listNode6 = new LinkedList<>();
        LinkedList<Object> listNode8 = new LinkedList<>();
        TLBrace tlbraceNode2 = (TLBrace) nodeArrayList1.get(0);
        new LinkedList();
        LinkedList<Object> listNode3 = (LinkedList) nodeArrayList2.get(0);
        if (listNode3 != null) {
            listNode4.addAll(listNode3);
        }
        new LinkedList();
        LinkedList<Object> listNode5 = (LinkedList) nodeArrayList3.get(0);
        if (listNode5 != null) {
            listNode6.addAll(listNode5);
        }
        new LinkedList();
        LinkedList<Object> listNode7 = (LinkedList) nodeArrayList4.get(0);
        if (listNode7 != null) {
            listNode8.addAll(listNode7);
        }
        TRBrace trbraceNode9 = (TRBrace) nodeArrayList5.get(0);
        PMethodBody pmethodbodyNode1 = new AFullMethodBody(tlbraceNode2, listNode4, listNode6, listNode8, trbraceNode9);
        nodeList.add(pmethodbodyNode1);
        return nodeList;
    }

    ArrayList<Object> new84() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        PJimpleType pjimpletypeNode2 = (PJimpleType) nodeArrayList1.get(0);
        PLocalNameList plocalnamelistNode3 = (PLocalNameList) nodeArrayList2.get(0);
        TSemicolon tsemicolonNode4 = (TSemicolon) nodeArrayList3.get(0);
        PDeclaration pdeclarationNode1 = new ADeclaration(pjimpletypeNode2, plocalnamelistNode3, tsemicolonNode4);
        nodeList.add(pdeclarationNode1);
        return nodeList;
    }

    ArrayList<Object> new85() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TUnknown tunknownNode2 = (TUnknown) nodeArrayList1.get(0);
        PJimpleType pjimpletypeNode1 = new AUnknownJimpleType(tunknownNode2);
        nodeList.add(pjimpletypeNode1);
        return nodeList;
    }

    ArrayList<Object> new86() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PNonvoidType pnonvoidtypeNode2 = (PNonvoidType) nodeArrayList1.get(0);
        PJimpleType pjimpletypeNode1 = new ANonvoidJimpleType(pnonvoidtypeNode2);
        nodeList.add(pjimpletypeNode1);
        return nodeList;
    }

    ArrayList<Object> new87() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PName pnameNode2 = (PName) nodeArrayList1.get(0);
        PLocalName plocalnameNode1 = new ALocalName(pnameNode2);
        nodeList.add(plocalnameNode1);
        return nodeList;
    }

    ArrayList<Object> new88() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PLocalName plocalnameNode2 = (PLocalName) nodeArrayList1.get(0);
        PLocalNameList plocalnamelistNode1 = new ASingleLocalNameList(plocalnameNode2);
        nodeList.add(plocalnamelistNode1);
        return nodeList;
    }

    ArrayList<Object> new89() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        PLocalName plocalnameNode2 = (PLocalName) nodeArrayList1.get(0);
        TComma tcommaNode3 = (TComma) nodeArrayList2.get(0);
        PLocalNameList plocalnamelistNode4 = (PLocalNameList) nodeArrayList3.get(0);
        PLocalNameList plocalnamelistNode1 = new AMultiLocalNameList(plocalnameNode2, tcommaNode3, plocalnamelistNode4);
        nodeList.add(plocalnamelistNode1);
        return nodeList;
    }

    ArrayList<Object> new90() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        PLabelName plabelnameNode2 = (PLabelName) nodeArrayList1.get(0);
        TColon tcolonNode3 = (TColon) nodeArrayList2.get(0);
        PStatement pstatementNode1 = new ALabelStatement(plabelnameNode2, tcolonNode3);
        nodeList.add(pstatementNode1);
        return nodeList;
    }

    ArrayList<Object> new91() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TBreakpoint tbreakpointNode2 = (TBreakpoint) nodeArrayList1.get(0);
        TSemicolon tsemicolonNode3 = (TSemicolon) nodeArrayList2.get(0);
        PStatement pstatementNode1 = new ABreakpointStatement(tbreakpointNode2, tsemicolonNode3);
        nodeList.add(pstatementNode1);
        return nodeList;
    }

    ArrayList<Object> new92() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TEntermonitor tentermonitorNode2 = (TEntermonitor) nodeArrayList1.get(0);
        PImmediate pimmediateNode3 = (PImmediate) nodeArrayList2.get(0);
        TSemicolon tsemicolonNode4 = (TSemicolon) nodeArrayList3.get(0);
        PStatement pstatementNode1 = new AEntermonitorStatement(tentermonitorNode2, pimmediateNode3, tsemicolonNode4);
        nodeList.add(pstatementNode1);
        return nodeList;
    }

    ArrayList<Object> new93() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TExitmonitor texitmonitorNode2 = (TExitmonitor) nodeArrayList1.get(0);
        PImmediate pimmediateNode3 = (PImmediate) nodeArrayList2.get(0);
        TSemicolon tsemicolonNode4 = (TSemicolon) nodeArrayList3.get(0);
        PStatement pstatementNode1 = new AExitmonitorStatement(texitmonitorNode2, pimmediateNode3, tsemicolonNode4);
        nodeList.add(pstatementNode1);
        return nodeList;
    }

    ArrayList<Object> new94() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList8 = pop();
        ArrayList<Object> nodeArrayList7 = pop();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode8 = new LinkedList<>();
        TTableswitch ttableswitchNode2 = (TTableswitch) nodeArrayList1.get(0);
        TLParen tlparenNode3 = (TLParen) nodeArrayList2.get(0);
        PImmediate pimmediateNode4 = (PImmediate) nodeArrayList3.get(0);
        TRParen trparenNode5 = (TRParen) nodeArrayList4.get(0);
        TLBrace tlbraceNode6 = (TLBrace) nodeArrayList5.get(0);
        new LinkedList();
        LinkedList<Object> listNode7 = (LinkedList) nodeArrayList6.get(0);
        if (listNode7 != null) {
            listNode8.addAll(listNode7);
        }
        TRBrace trbraceNode9 = (TRBrace) nodeArrayList7.get(0);
        TSemicolon tsemicolonNode10 = (TSemicolon) nodeArrayList8.get(0);
        PStatement pstatementNode1 = new ATableswitchStatement(ttableswitchNode2, tlparenNode3, pimmediateNode4, trparenNode5, tlbraceNode6, listNode8, trbraceNode9, tsemicolonNode10);
        nodeList.add(pstatementNode1);
        return nodeList;
    }

    ArrayList<Object> new95() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList8 = pop();
        ArrayList<Object> nodeArrayList7 = pop();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode8 = new LinkedList<>();
        TLookupswitch tlookupswitchNode2 = (TLookupswitch) nodeArrayList1.get(0);
        TLParen tlparenNode3 = (TLParen) nodeArrayList2.get(0);
        PImmediate pimmediateNode4 = (PImmediate) nodeArrayList3.get(0);
        TRParen trparenNode5 = (TRParen) nodeArrayList4.get(0);
        TLBrace tlbraceNode6 = (TLBrace) nodeArrayList5.get(0);
        new LinkedList();
        LinkedList<Object> listNode7 = (LinkedList) nodeArrayList6.get(0);
        if (listNode7 != null) {
            listNode8.addAll(listNode7);
        }
        TRBrace trbraceNode9 = (TRBrace) nodeArrayList7.get(0);
        TSemicolon tsemicolonNode10 = (TSemicolon) nodeArrayList8.get(0);
        PStatement pstatementNode1 = new ALookupswitchStatement(tlookupswitchNode2, tlparenNode3, pimmediateNode4, trparenNode5, tlbraceNode6, listNode8, trbraceNode9, tsemicolonNode10);
        nodeList.add(pstatementNode1);
        return nodeList;
    }

    ArrayList<Object> new96() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        PLocalName plocalnameNode2 = (PLocalName) nodeArrayList1.get(0);
        TColonEquals tcolonequalsNode3 = (TColonEquals) nodeArrayList2.get(0);
        TAtIdentifier tatidentifierNode4 = (TAtIdentifier) nodeArrayList3.get(0);
        PType ptypeNode5 = (PType) nodeArrayList4.get(0);
        TSemicolon tsemicolonNode6 = (TSemicolon) nodeArrayList5.get(0);
        PStatement pstatementNode1 = new AIdentityStatement(plocalnameNode2, tcolonequalsNode3, tatidentifierNode4, ptypeNode5, tsemicolonNode6);
        nodeList.add(pstatementNode1);
        return nodeList;
    }

    ArrayList<Object> new97() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        PLocalName plocalnameNode2 = (PLocalName) nodeArrayList1.get(0);
        TColonEquals tcolonequalsNode3 = (TColonEquals) nodeArrayList2.get(0);
        TAtIdentifier tatidentifierNode4 = (TAtIdentifier) nodeArrayList3.get(0);
        TSemicolon tsemicolonNode5 = (TSemicolon) nodeArrayList4.get(0);
        PStatement pstatementNode1 = new AIdentityNoTypeStatement(plocalnameNode2, tcolonequalsNode3, tatidentifierNode4, tsemicolonNode5);
        nodeList.add(pstatementNode1);
        return nodeList;
    }

    ArrayList<Object> new98() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        PVariable pvariableNode2 = (PVariable) nodeArrayList1.get(0);
        TEquals tequalsNode3 = (TEquals) nodeArrayList2.get(0);
        PExpression pexpressionNode4 = (PExpression) nodeArrayList3.get(0);
        TSemicolon tsemicolonNode5 = (TSemicolon) nodeArrayList4.get(0);
        PStatement pstatementNode1 = new AAssignStatement(pvariableNode2, tequalsNode3, pexpressionNode4, tsemicolonNode5);
        nodeList.add(pstatementNode1);
        return nodeList;
    }

    ArrayList<Object> new99() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TIf tifNode2 = (TIf) nodeArrayList1.get(0);
        PBoolExpr pboolexprNode3 = (PBoolExpr) nodeArrayList2.get(0);
        PGotoStmt pgotostmtNode4 = (PGotoStmt) nodeArrayList3.get(0);
        PStatement pstatementNode1 = new AIfStatement(tifNode2, pboolexprNode3, pgotostmtNode4);
        nodeList.add(pstatementNode1);
        return nodeList;
    }

    ArrayList<Object> new100() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PGotoStmt pgotostmtNode2 = (PGotoStmt) nodeArrayList1.get(0);
        PStatement pstatementNode1 = new AGotoStatement(pgotostmtNode2);
        nodeList.add(pstatementNode1);
        return nodeList;
    }

    ArrayList<Object> new101() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TNop tnopNode2 = (TNop) nodeArrayList1.get(0);
        TSemicolon tsemicolonNode3 = (TSemicolon) nodeArrayList2.get(0);
        PStatement pstatementNode1 = new ANopStatement(tnopNode2, tsemicolonNode3);
        nodeList.add(pstatementNode1);
        return nodeList;
    }

    ArrayList<Object> new102() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TRet tretNode2 = (TRet) nodeArrayList1.get(0);
        TSemicolon tsemicolonNode4 = (TSemicolon) nodeArrayList2.get(0);
        PStatement pstatementNode1 = new ARetStatement(tretNode2, null, tsemicolonNode4);
        nodeList.add(pstatementNode1);
        return nodeList;
    }

    ArrayList<Object> new103() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TRet tretNode2 = (TRet) nodeArrayList1.get(0);
        PImmediate pimmediateNode3 = (PImmediate) nodeArrayList2.get(0);
        TSemicolon tsemicolonNode4 = (TSemicolon) nodeArrayList3.get(0);
        PStatement pstatementNode1 = new ARetStatement(tretNode2, pimmediateNode3, tsemicolonNode4);
        nodeList.add(pstatementNode1);
        return nodeList;
    }

    ArrayList<Object> new104() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TReturn treturnNode2 = (TReturn) nodeArrayList1.get(0);
        TSemicolon tsemicolonNode4 = (TSemicolon) nodeArrayList2.get(0);
        PStatement pstatementNode1 = new AReturnStatement(treturnNode2, null, tsemicolonNode4);
        nodeList.add(pstatementNode1);
        return nodeList;
    }

    ArrayList<Object> new105() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TReturn treturnNode2 = (TReturn) nodeArrayList1.get(0);
        PImmediate pimmediateNode3 = (PImmediate) nodeArrayList2.get(0);
        TSemicolon tsemicolonNode4 = (TSemicolon) nodeArrayList3.get(0);
        PStatement pstatementNode1 = new AReturnStatement(treturnNode2, pimmediateNode3, tsemicolonNode4);
        nodeList.add(pstatementNode1);
        return nodeList;
    }

    ArrayList<Object> new106() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TThrow tthrowNode2 = (TThrow) nodeArrayList1.get(0);
        PImmediate pimmediateNode3 = (PImmediate) nodeArrayList2.get(0);
        TSemicolon tsemicolonNode4 = (TSemicolon) nodeArrayList3.get(0);
        PStatement pstatementNode1 = new AThrowStatement(tthrowNode2, pimmediateNode3, tsemicolonNode4);
        nodeList.add(pstatementNode1);
        return nodeList;
    }

    ArrayList<Object> new107() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        PInvokeExpr pinvokeexprNode2 = (PInvokeExpr) nodeArrayList1.get(0);
        TSemicolon tsemicolonNode3 = (TSemicolon) nodeArrayList2.get(0);
        PStatement pstatementNode1 = new AInvokeStatement(pinvokeexprNode2, tsemicolonNode3);
        nodeList.add(pstatementNode1);
        return nodeList;
    }

    ArrayList<Object> new108() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TIdentifier tidentifierNode2 = (TIdentifier) nodeArrayList1.get(0);
        PLabelName plabelnameNode1 = new ALabelName(tidentifierNode2);
        nodeList.add(plabelnameNode1);
        return nodeList;
    }

    ArrayList<Object> new109() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        PCaseLabel pcaselabelNode2 = (PCaseLabel) nodeArrayList1.get(0);
        TColon tcolonNode3 = (TColon) nodeArrayList2.get(0);
        PGotoStmt pgotostmtNode4 = (PGotoStmt) nodeArrayList3.get(0);
        PCaseStmt pcasestmtNode1 = new ACaseStmt(pcaselabelNode2, tcolonNode3, pgotostmtNode4);
        nodeList.add(pcasestmtNode1);
        return nodeList;
    }

    ArrayList<Object> new110() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TCase tcaseNode2 = (TCase) nodeArrayList1.get(0);
        TIntegerConstant tintegerconstantNode4 = (TIntegerConstant) nodeArrayList2.get(0);
        PCaseLabel pcaselabelNode1 = new AConstantCaseLabel(tcaseNode2, null, tintegerconstantNode4);
        nodeList.add(pcaselabelNode1);
        return nodeList;
    }

    ArrayList<Object> new111() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TCase tcaseNode2 = (TCase) nodeArrayList1.get(0);
        TMinus tminusNode3 = (TMinus) nodeArrayList2.get(0);
        TIntegerConstant tintegerconstantNode4 = (TIntegerConstant) nodeArrayList3.get(0);
        PCaseLabel pcaselabelNode1 = new AConstantCaseLabel(tcaseNode2, tminusNode3, tintegerconstantNode4);
        nodeList.add(pcaselabelNode1);
        return nodeList;
    }

    ArrayList<Object> new112() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TDefault tdefaultNode2 = (TDefault) nodeArrayList1.get(0);
        PCaseLabel pcaselabelNode1 = new ADefaultCaseLabel(tdefaultNode2);
        nodeList.add(pcaselabelNode1);
        return nodeList;
    }

    ArrayList<Object> new113() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TGoto tgotoNode2 = (TGoto) nodeArrayList1.get(0);
        PLabelName plabelnameNode3 = (PLabelName) nodeArrayList2.get(0);
        TSemicolon tsemicolonNode4 = (TSemicolon) nodeArrayList3.get(0);
        PGotoStmt pgotostmtNode1 = new AGotoStmt(tgotoNode2, plabelnameNode3, tsemicolonNode4);
        nodeList.add(pgotostmtNode1);
        return nodeList;
    }

    ArrayList<Object> new114() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList9 = pop();
        ArrayList<Object> nodeArrayList8 = pop();
        ArrayList<Object> nodeArrayList7 = pop();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TCatch tcatchNode2 = (TCatch) nodeArrayList1.get(0);
        PClassName pclassnameNode3 = (PClassName) nodeArrayList2.get(0);
        TFrom tfromNode4 = (TFrom) nodeArrayList3.get(0);
        PLabelName plabelnameNode5 = (PLabelName) nodeArrayList4.get(0);
        TTo ttoNode6 = (TTo) nodeArrayList5.get(0);
        PLabelName plabelnameNode7 = (PLabelName) nodeArrayList6.get(0);
        TWith twithNode8 = (TWith) nodeArrayList7.get(0);
        PLabelName plabelnameNode9 = (PLabelName) nodeArrayList8.get(0);
        TSemicolon tsemicolonNode10 = (TSemicolon) nodeArrayList9.get(0);
        PCatchClause pcatchclauseNode1 = new ACatchClause(tcatchNode2, pclassnameNode3, tfromNode4, plabelnameNode5, ttoNode6, plabelnameNode7, twithNode8, plabelnameNode9, tsemicolonNode10);
        nodeList.add(pcatchclauseNode1);
        return nodeList;
    }

    ArrayList<Object> new115() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PNewExpr pnewexprNode2 = (PNewExpr) nodeArrayList1.get(0);
        PExpression pexpressionNode1 = new ANewExpression(pnewexprNode2);
        nodeList.add(pexpressionNode1);
        return nodeList;
    }

    ArrayList<Object> new116() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TLParen tlparenNode2 = (TLParen) nodeArrayList1.get(0);
        PNonvoidType pnonvoidtypeNode3 = (PNonvoidType) nodeArrayList2.get(0);
        TRParen trparenNode4 = (TRParen) nodeArrayList3.get(0);
        PImmediate pimmediateNode5 = (PImmediate) nodeArrayList4.get(0);
        PExpression pexpressionNode1 = new ACastExpression(tlparenNode2, pnonvoidtypeNode3, trparenNode4, pimmediateNode5);
        nodeList.add(pexpressionNode1);
        return nodeList;
    }

    ArrayList<Object> new117() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        PImmediate pimmediateNode2 = (PImmediate) nodeArrayList1.get(0);
        TInstanceof tinstanceofNode3 = (TInstanceof) nodeArrayList2.get(0);
        PNonvoidType pnonvoidtypeNode4 = (PNonvoidType) nodeArrayList3.get(0);
        PExpression pexpressionNode1 = new AInstanceofExpression(pimmediateNode2, tinstanceofNode3, pnonvoidtypeNode4);
        nodeList.add(pexpressionNode1);
        return nodeList;
    }

    ArrayList<Object> new118() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PInvokeExpr pinvokeexprNode2 = (PInvokeExpr) nodeArrayList1.get(0);
        PExpression pexpressionNode1 = new AInvokeExpression(pinvokeexprNode2);
        nodeList.add(pexpressionNode1);
        return nodeList;
    }

    ArrayList<Object> new119() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PReference preferenceNode2 = (PReference) nodeArrayList1.get(0);
        PExpression pexpressionNode1 = new AReferenceExpression(preferenceNode2);
        nodeList.add(pexpressionNode1);
        return nodeList;
    }

    ArrayList<Object> new120() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PBinopExpr pbinopexprNode2 = (PBinopExpr) nodeArrayList1.get(0);
        PExpression pexpressionNode1 = new ABinopExpression(pbinopexprNode2);
        nodeList.add(pexpressionNode1);
        return nodeList;
    }

    ArrayList<Object> new121() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PUnopExpr punopexprNode2 = (PUnopExpr) nodeArrayList1.get(0);
        PExpression pexpressionNode1 = new AUnopExpression(punopexprNode2);
        nodeList.add(pexpressionNode1);
        return nodeList;
    }

    ArrayList<Object> new122() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PImmediate pimmediateNode2 = (PImmediate) nodeArrayList1.get(0);
        PExpression pexpressionNode1 = new AImmediateExpression(pimmediateNode2);
        nodeList.add(pexpressionNode1);
        return nodeList;
    }

    ArrayList<Object> new123() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TNew tnewNode2 = (TNew) nodeArrayList1.get(0);
        PBaseType pbasetypeNode3 = (PBaseType) nodeArrayList2.get(0);
        PNewExpr pnewexprNode1 = new ASimpleNewExpr(tnewNode2, pbasetypeNode3);
        nodeList.add(pnewexprNode1);
        return nodeList;
    }

    ArrayList<Object> new124() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TNewarray tnewarrayNode2 = (TNewarray) nodeArrayList1.get(0);
        TLParen tlparenNode3 = (TLParen) nodeArrayList2.get(0);
        PNonvoidType pnonvoidtypeNode4 = (PNonvoidType) nodeArrayList3.get(0);
        TRParen trparenNode5 = (TRParen) nodeArrayList4.get(0);
        PFixedArrayDescriptor pfixedarraydescriptorNode6 = (PFixedArrayDescriptor) nodeArrayList5.get(0);
        PNewExpr pnewexprNode1 = new AArrayNewExpr(tnewarrayNode2, tlparenNode3, pnonvoidtypeNode4, trparenNode5, pfixedarraydescriptorNode6);
        nodeList.add(pnewexprNode1);
        return nodeList;
    }

    ArrayList<Object> new125() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode7 = new LinkedList<>();
        TNewmultiarray tnewmultiarrayNode2 = (TNewmultiarray) nodeArrayList1.get(0);
        TLParen tlparenNode3 = (TLParen) nodeArrayList2.get(0);
        PBaseType pbasetypeNode4 = (PBaseType) nodeArrayList3.get(0);
        TRParen trparenNode5 = (TRParen) nodeArrayList4.get(0);
        new LinkedList();
        LinkedList<Object> listNode6 = (LinkedList) nodeArrayList5.get(0);
        if (listNode6 != null) {
            listNode7.addAll(listNode6);
        }
        PNewExpr pnewexprNode1 = new AMultiNewExpr(tnewmultiarrayNode2, tlparenNode3, pbasetypeNode4, trparenNode5, listNode7);
        nodeList.add(pnewexprNode1);
        return nodeList;
    }

    ArrayList<Object> new126() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TLBracket tlbracketNode2 = (TLBracket) nodeArrayList1.get(0);
        TRBracket trbracketNode4 = (TRBracket) nodeArrayList2.get(0);
        PArrayDescriptor parraydescriptorNode1 = new AArrayDescriptor(tlbracketNode2, null, trbracketNode4);
        nodeList.add(parraydescriptorNode1);
        return nodeList;
    }

    ArrayList<Object> new127() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TLBracket tlbracketNode2 = (TLBracket) nodeArrayList1.get(0);
        PImmediate pimmediateNode3 = (PImmediate) nodeArrayList2.get(0);
        TRBracket trbracketNode4 = (TRBracket) nodeArrayList3.get(0);
        PArrayDescriptor parraydescriptorNode1 = new AArrayDescriptor(tlbracketNode2, pimmediateNode3, trbracketNode4);
        nodeList.add(parraydescriptorNode1);
        return nodeList;
    }

    ArrayList<Object> new128() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PReference preferenceNode2 = (PReference) nodeArrayList1.get(0);
        PVariable pvariableNode1 = new AReferenceVariable(preferenceNode2);
        nodeList.add(pvariableNode1);
        return nodeList;
    }

    ArrayList<Object> new129() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PLocalName plocalnameNode2 = (PLocalName) nodeArrayList1.get(0);
        PVariable pvariableNode1 = new ALocalVariable(plocalnameNode2);
        nodeList.add(pvariableNode1);
        return nodeList;
    }

    ArrayList<Object> new130() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PBinopExpr pbinopexprNode2 = (PBinopExpr) nodeArrayList1.get(0);
        PBoolExpr pboolexprNode1 = new ABinopBoolExpr(pbinopexprNode2);
        nodeList.add(pboolexprNode1);
        return nodeList;
    }

    ArrayList<Object> new131() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PUnopExpr punopexprNode2 = (PUnopExpr) nodeArrayList1.get(0);
        PBoolExpr pboolexprNode1 = new AUnopBoolExpr(punopexprNode2);
        nodeList.add(pboolexprNode1);
        return nodeList;
    }

    ArrayList<Object> new132() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        PNonstaticInvoke pnonstaticinvokeNode2 = (PNonstaticInvoke) nodeArrayList1.get(0);
        PLocalName plocalnameNode3 = (PLocalName) nodeArrayList2.get(0);
        TDot tdotNode4 = (TDot) nodeArrayList3.get(0);
        PMethodSignature pmethodsignatureNode5 = (PMethodSignature) nodeArrayList4.get(0);
        TLParen tlparenNode6 = (TLParen) nodeArrayList5.get(0);
        TRParen trparenNode8 = (TRParen) nodeArrayList6.get(0);
        PInvokeExpr pinvokeexprNode1 = new ANonstaticInvokeExpr(pnonstaticinvokeNode2, plocalnameNode3, tdotNode4, pmethodsignatureNode5, tlparenNode6, null, trparenNode8);
        nodeList.add(pinvokeexprNode1);
        return nodeList;
    }

    ArrayList<Object> new133() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList7 = pop();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        PNonstaticInvoke pnonstaticinvokeNode2 = (PNonstaticInvoke) nodeArrayList1.get(0);
        PLocalName plocalnameNode3 = (PLocalName) nodeArrayList2.get(0);
        TDot tdotNode4 = (TDot) nodeArrayList3.get(0);
        PMethodSignature pmethodsignatureNode5 = (PMethodSignature) nodeArrayList4.get(0);
        TLParen tlparenNode6 = (TLParen) nodeArrayList5.get(0);
        PArgList parglistNode7 = (PArgList) nodeArrayList6.get(0);
        TRParen trparenNode8 = (TRParen) nodeArrayList7.get(0);
        PInvokeExpr pinvokeexprNode1 = new ANonstaticInvokeExpr(pnonstaticinvokeNode2, plocalnameNode3, tdotNode4, pmethodsignatureNode5, tlparenNode6, parglistNode7, trparenNode8);
        nodeList.add(pinvokeexprNode1);
        return nodeList;
    }

    ArrayList<Object> new134() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TStaticinvoke tstaticinvokeNode2 = (TStaticinvoke) nodeArrayList1.get(0);
        PMethodSignature pmethodsignatureNode3 = (PMethodSignature) nodeArrayList2.get(0);
        TLParen tlparenNode4 = (TLParen) nodeArrayList3.get(0);
        TRParen trparenNode6 = (TRParen) nodeArrayList4.get(0);
        PInvokeExpr pinvokeexprNode1 = new AStaticInvokeExpr(tstaticinvokeNode2, pmethodsignatureNode3, tlparenNode4, null, trparenNode6);
        nodeList.add(pinvokeexprNode1);
        return nodeList;
    }

    ArrayList<Object> new135() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TStaticinvoke tstaticinvokeNode2 = (TStaticinvoke) nodeArrayList1.get(0);
        PMethodSignature pmethodsignatureNode3 = (PMethodSignature) nodeArrayList2.get(0);
        TLParen tlparenNode4 = (TLParen) nodeArrayList3.get(0);
        PArgList parglistNode5 = (PArgList) nodeArrayList4.get(0);
        TRParen trparenNode6 = (TRParen) nodeArrayList5.get(0);
        PInvokeExpr pinvokeexprNode1 = new AStaticInvokeExpr(tstaticinvokeNode2, pmethodsignatureNode3, tlparenNode4, parglistNode5, trparenNode6);
        nodeList.add(pinvokeexprNode1);
        return nodeList;
    }

    ArrayList<Object> new136() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList8 = pop();
        ArrayList<Object> nodeArrayList7 = pop();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TDynamicinvoke tdynamicinvokeNode2 = (TDynamicinvoke) nodeArrayList1.get(0);
        TStringConstant tstringconstantNode3 = (TStringConstant) nodeArrayList2.get(0);
        PUnnamedMethodSignature punnamedmethodsignatureNode4 = (PUnnamedMethodSignature) nodeArrayList3.get(0);
        TLParen tlparenNode5 = (TLParen) nodeArrayList4.get(0);
        TRParen trparenNode7 = (TRParen) nodeArrayList5.get(0);
        PMethodSignature pmethodsignatureNode8 = (PMethodSignature) nodeArrayList6.get(0);
        TLParen tlparenNode9 = (TLParen) nodeArrayList7.get(0);
        TRParen trparenNode11 = (TRParen) nodeArrayList8.get(0);
        PInvokeExpr pinvokeexprNode1 = new ADynamicInvokeExpr(tdynamicinvokeNode2, tstringconstantNode3, punnamedmethodsignatureNode4, tlparenNode5, null, trparenNode7, pmethodsignatureNode8, tlparenNode9, null, trparenNode11);
        nodeList.add(pinvokeexprNode1);
        return nodeList;
    }

    ArrayList<Object> new137() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList9 = pop();
        ArrayList<Object> nodeArrayList8 = pop();
        ArrayList<Object> nodeArrayList7 = pop();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TDynamicinvoke tdynamicinvokeNode2 = (TDynamicinvoke) nodeArrayList1.get(0);
        TStringConstant tstringconstantNode3 = (TStringConstant) nodeArrayList2.get(0);
        PUnnamedMethodSignature punnamedmethodsignatureNode4 = (PUnnamedMethodSignature) nodeArrayList3.get(0);
        TLParen tlparenNode5 = (TLParen) nodeArrayList4.get(0);
        PArgList parglistNode6 = (PArgList) nodeArrayList5.get(0);
        TRParen trparenNode7 = (TRParen) nodeArrayList6.get(0);
        PMethodSignature pmethodsignatureNode8 = (PMethodSignature) nodeArrayList7.get(0);
        TLParen tlparenNode9 = (TLParen) nodeArrayList8.get(0);
        TRParen trparenNode11 = (TRParen) nodeArrayList9.get(0);
        PInvokeExpr pinvokeexprNode1 = new ADynamicInvokeExpr(tdynamicinvokeNode2, tstringconstantNode3, punnamedmethodsignatureNode4, tlparenNode5, parglistNode6, trparenNode7, pmethodsignatureNode8, tlparenNode9, null, trparenNode11);
        nodeList.add(pinvokeexprNode1);
        return nodeList;
    }

    ArrayList<Object> new138() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList9 = pop();
        ArrayList<Object> nodeArrayList8 = pop();
        ArrayList<Object> nodeArrayList7 = pop();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TDynamicinvoke tdynamicinvokeNode2 = (TDynamicinvoke) nodeArrayList1.get(0);
        TStringConstant tstringconstantNode3 = (TStringConstant) nodeArrayList2.get(0);
        PUnnamedMethodSignature punnamedmethodsignatureNode4 = (PUnnamedMethodSignature) nodeArrayList3.get(0);
        TLParen tlparenNode5 = (TLParen) nodeArrayList4.get(0);
        TRParen trparenNode7 = (TRParen) nodeArrayList5.get(0);
        PMethodSignature pmethodsignatureNode8 = (PMethodSignature) nodeArrayList6.get(0);
        TLParen tlparenNode9 = (TLParen) nodeArrayList7.get(0);
        PArgList parglistNode10 = (PArgList) nodeArrayList8.get(0);
        TRParen trparenNode11 = (TRParen) nodeArrayList9.get(0);
        PInvokeExpr pinvokeexprNode1 = new ADynamicInvokeExpr(tdynamicinvokeNode2, tstringconstantNode3, punnamedmethodsignatureNode4, tlparenNode5, null, trparenNode7, pmethodsignatureNode8, tlparenNode9, parglistNode10, trparenNode11);
        nodeList.add(pinvokeexprNode1);
        return nodeList;
    }

    ArrayList<Object> new139() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList10 = pop();
        ArrayList<Object> nodeArrayList9 = pop();
        ArrayList<Object> nodeArrayList8 = pop();
        ArrayList<Object> nodeArrayList7 = pop();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TDynamicinvoke tdynamicinvokeNode2 = (TDynamicinvoke) nodeArrayList1.get(0);
        TStringConstant tstringconstantNode3 = (TStringConstant) nodeArrayList2.get(0);
        PUnnamedMethodSignature punnamedmethodsignatureNode4 = (PUnnamedMethodSignature) nodeArrayList3.get(0);
        TLParen tlparenNode5 = (TLParen) nodeArrayList4.get(0);
        PArgList parglistNode6 = (PArgList) nodeArrayList5.get(0);
        TRParen trparenNode7 = (TRParen) nodeArrayList6.get(0);
        PMethodSignature pmethodsignatureNode8 = (PMethodSignature) nodeArrayList7.get(0);
        TLParen tlparenNode9 = (TLParen) nodeArrayList8.get(0);
        PArgList parglistNode10 = (PArgList) nodeArrayList9.get(0);
        TRParen trparenNode11 = (TRParen) nodeArrayList10.get(0);
        PInvokeExpr pinvokeexprNode1 = new ADynamicInvokeExpr(tdynamicinvokeNode2, tstringconstantNode3, punnamedmethodsignatureNode4, tlparenNode5, parglistNode6, trparenNode7, pmethodsignatureNode8, tlparenNode9, parglistNode10, trparenNode11);
        nodeList.add(pinvokeexprNode1);
        return nodeList;
    }

    ArrayList<Object> new140() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        PImmediate pimmediateNode2 = (PImmediate) nodeArrayList1.get(0);
        PBinop pbinopNode3 = (PBinop) nodeArrayList2.get(0);
        PImmediate pimmediateNode4 = (PImmediate) nodeArrayList3.get(0);
        PBinopExpr pbinopexprNode1 = new ABinopExpr(pimmediateNode2, pbinopNode3, pimmediateNode4);
        nodeList.add(pbinopexprNode1);
        return nodeList;
    }

    ArrayList<Object> new141() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        PUnop punopNode2 = (PUnop) nodeArrayList1.get(0);
        PImmediate pimmediateNode3 = (PImmediate) nodeArrayList2.get(0);
        PUnopExpr punopexprNode1 = new AUnopExpr(punopNode2, pimmediateNode3);
        nodeList.add(punopexprNode1);
        return nodeList;
    }

    ArrayList<Object> new142() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TSpecialinvoke tspecialinvokeNode2 = (TSpecialinvoke) nodeArrayList1.get(0);
        PNonstaticInvoke pnonstaticinvokeNode1 = new ASpecialNonstaticInvoke(tspecialinvokeNode2);
        nodeList.add(pnonstaticinvokeNode1);
        return nodeList;
    }

    ArrayList<Object> new143() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TVirtualinvoke tvirtualinvokeNode2 = (TVirtualinvoke) nodeArrayList1.get(0);
        PNonstaticInvoke pnonstaticinvokeNode1 = new AVirtualNonstaticInvoke(tvirtualinvokeNode2);
        nodeList.add(pnonstaticinvokeNode1);
        return nodeList;
    }

    ArrayList<Object> new144() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TInterfaceinvoke tinterfaceinvokeNode2 = (TInterfaceinvoke) nodeArrayList1.get(0);
        PNonstaticInvoke pnonstaticinvokeNode1 = new AInterfaceNonstaticInvoke(tinterfaceinvokeNode2);
        nodeList.add(pnonstaticinvokeNode1);
        return nodeList;
    }

    ArrayList<Object> new145() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TCmplt tcmpltNode2 = (TCmplt) nodeArrayList1.get(0);
        PType ptypeNode3 = (PType) nodeArrayList2.get(0);
        TLParen tlparenNode4 = (TLParen) nodeArrayList3.get(0);
        TRParen trparenNode6 = (TRParen) nodeArrayList4.get(0);
        TCmpgt tcmpgtNode7 = (TCmpgt) nodeArrayList5.get(0);
        PUnnamedMethodSignature punnamedmethodsignatureNode1 = new AUnnamedMethodSignature(tcmpltNode2, ptypeNode3, tlparenNode4, null, trparenNode6, tcmpgtNode7);
        nodeList.add(punnamedmethodsignatureNode1);
        return nodeList;
    }

    ArrayList<Object> new146() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TCmplt tcmpltNode2 = (TCmplt) nodeArrayList1.get(0);
        PType ptypeNode3 = (PType) nodeArrayList2.get(0);
        TLParen tlparenNode4 = (TLParen) nodeArrayList3.get(0);
        PParameterList pparameterlistNode5 = (PParameterList) nodeArrayList4.get(0);
        TRParen trparenNode6 = (TRParen) nodeArrayList5.get(0);
        TCmpgt tcmpgtNode7 = (TCmpgt) nodeArrayList6.get(0);
        PUnnamedMethodSignature punnamedmethodsignatureNode1 = new AUnnamedMethodSignature(tcmpltNode2, ptypeNode3, tlparenNode4, pparameterlistNode5, trparenNode6, tcmpgtNode7);
        nodeList.add(punnamedmethodsignatureNode1);
        return nodeList;
    }

    ArrayList<Object> new147() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList8 = pop();
        ArrayList<Object> nodeArrayList7 = pop();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TCmplt tcmpltNode2 = (TCmplt) nodeArrayList1.get(0);
        PClassName pclassnameNode3 = (PClassName) nodeArrayList2.get(0);
        TColon tcolonNode4 = (TColon) nodeArrayList3.get(0);
        PType ptypeNode5 = (PType) nodeArrayList4.get(0);
        PName pnameNode6 = (PName) nodeArrayList5.get(0);
        TLParen tlparenNode7 = (TLParen) nodeArrayList6.get(0);
        TRParen trparenNode9 = (TRParen) nodeArrayList7.get(0);
        TCmpgt tcmpgtNode10 = (TCmpgt) nodeArrayList8.get(0);
        PMethodSignature pmethodsignatureNode1 = new AMethodSignature(tcmpltNode2, pclassnameNode3, tcolonNode4, ptypeNode5, pnameNode6, tlparenNode7, null, trparenNode9, tcmpgtNode10);
        nodeList.add(pmethodsignatureNode1);
        return nodeList;
    }

    ArrayList<Object> new148() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList9 = pop();
        ArrayList<Object> nodeArrayList8 = pop();
        ArrayList<Object> nodeArrayList7 = pop();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TCmplt tcmpltNode2 = (TCmplt) nodeArrayList1.get(0);
        PClassName pclassnameNode3 = (PClassName) nodeArrayList2.get(0);
        TColon tcolonNode4 = (TColon) nodeArrayList3.get(0);
        PType ptypeNode5 = (PType) nodeArrayList4.get(0);
        PName pnameNode6 = (PName) nodeArrayList5.get(0);
        TLParen tlparenNode7 = (TLParen) nodeArrayList6.get(0);
        PParameterList pparameterlistNode8 = (PParameterList) nodeArrayList7.get(0);
        TRParen trparenNode9 = (TRParen) nodeArrayList8.get(0);
        TCmpgt tcmpgtNode10 = (TCmpgt) nodeArrayList9.get(0);
        PMethodSignature pmethodsignatureNode1 = new AMethodSignature(tcmpltNode2, pclassnameNode3, tcolonNode4, ptypeNode5, pnameNode6, tlparenNode7, pparameterlistNode8, trparenNode9, tcmpgtNode10);
        nodeList.add(pmethodsignatureNode1);
        return nodeList;
    }

    ArrayList<Object> new149() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PArrayRef parrayrefNode2 = (PArrayRef) nodeArrayList1.get(0);
        PReference preferenceNode1 = new AArrayReference(parrayrefNode2);
        nodeList.add(preferenceNode1);
        return nodeList;
    }

    ArrayList<Object> new150() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PFieldRef pfieldrefNode2 = (PFieldRef) nodeArrayList1.get(0);
        PReference preferenceNode1 = new AFieldReference(pfieldrefNode2);
        nodeList.add(preferenceNode1);
        return nodeList;
    }

    ArrayList<Object> new151() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TIdentifier tidentifierNode2 = (TIdentifier) nodeArrayList1.get(0);
        PFixedArrayDescriptor pfixedarraydescriptorNode3 = (PFixedArrayDescriptor) nodeArrayList2.get(0);
        PArrayRef parrayrefNode1 = new AIdentArrayRef(tidentifierNode2, pfixedarraydescriptorNode3);
        nodeList.add(parrayrefNode1);
        return nodeList;
    }

    ArrayList<Object> new152() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TQuotedName tquotednameNode2 = (TQuotedName) nodeArrayList1.get(0);
        PFixedArrayDescriptor pfixedarraydescriptorNode3 = (PFixedArrayDescriptor) nodeArrayList2.get(0);
        PArrayRef parrayrefNode1 = new AQuotedArrayRef(tquotednameNode2, pfixedarraydescriptorNode3);
        nodeList.add(parrayrefNode1);
        return nodeList;
    }

    ArrayList<Object> new153() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        PLocalName plocalnameNode2 = (PLocalName) nodeArrayList1.get(0);
        TDot tdotNode3 = (TDot) nodeArrayList2.get(0);
        PFieldSignature pfieldsignatureNode4 = (PFieldSignature) nodeArrayList3.get(0);
        PFieldRef pfieldrefNode1 = new ALocalFieldRef(plocalnameNode2, tdotNode3, pfieldsignatureNode4);
        nodeList.add(pfieldrefNode1);
        return nodeList;
    }

    ArrayList<Object> new154() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PFieldSignature pfieldsignatureNode2 = (PFieldSignature) nodeArrayList1.get(0);
        PFieldRef pfieldrefNode1 = new ASigFieldRef(pfieldsignatureNode2);
        nodeList.add(pfieldrefNode1);
        return nodeList;
    }

    ArrayList<Object> new155() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList6 = pop();
        ArrayList<Object> nodeArrayList5 = pop();
        ArrayList<Object> nodeArrayList4 = pop();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TCmplt tcmpltNode2 = (TCmplt) nodeArrayList1.get(0);
        PClassName pclassnameNode3 = (PClassName) nodeArrayList2.get(0);
        TColon tcolonNode4 = (TColon) nodeArrayList3.get(0);
        PType ptypeNode5 = (PType) nodeArrayList4.get(0);
        PName pnameNode6 = (PName) nodeArrayList5.get(0);
        TCmpgt tcmpgtNode7 = (TCmpgt) nodeArrayList6.get(0);
        PFieldSignature pfieldsignatureNode1 = new AFieldSignature(tcmpltNode2, pclassnameNode3, tcolonNode4, ptypeNode5, pnameNode6, tcmpgtNode7);
        nodeList.add(pfieldsignatureNode1);
        return nodeList;
    }

    ArrayList<Object> new156() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TLBracket tlbracketNode2 = (TLBracket) nodeArrayList1.get(0);
        PImmediate pimmediateNode3 = (PImmediate) nodeArrayList2.get(0);
        TRBracket trbracketNode4 = (TRBracket) nodeArrayList3.get(0);
        PFixedArrayDescriptor pfixedarraydescriptorNode1 = new AFixedArrayDescriptor(tlbracketNode2, pimmediateNode3, trbracketNode4);
        nodeList.add(pfixedarraydescriptorNode1);
        return nodeList;
    }

    ArrayList<Object> new157() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PImmediate pimmediateNode2 = (PImmediate) nodeArrayList1.get(0);
        PArgList parglistNode1 = new ASingleArgList(pimmediateNode2);
        nodeList.add(parglistNode1);
        return nodeList;
    }

    ArrayList<Object> new158() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList3 = pop();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        PImmediate pimmediateNode2 = (PImmediate) nodeArrayList1.get(0);
        TComma tcommaNode3 = (TComma) nodeArrayList2.get(0);
        PArgList parglistNode4 = (PArgList) nodeArrayList3.get(0);
        PArgList parglistNode1 = new AMultiArgList(pimmediateNode2, tcommaNode3, parglistNode4);
        nodeList.add(parglistNode1);
        return nodeList;
    }

    ArrayList<Object> new159() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PLocalName plocalnameNode2 = (PLocalName) nodeArrayList1.get(0);
        PImmediate pimmediateNode1 = new ALocalImmediate(plocalnameNode2);
        nodeList.add(pimmediateNode1);
        return nodeList;
    }

    ArrayList<Object> new160() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        PConstant pconstantNode2 = (PConstant) nodeArrayList1.get(0);
        PImmediate pimmediateNode1 = new AConstantImmediate(pconstantNode2);
        nodeList.add(pimmediateNode1);
        return nodeList;
    }

    ArrayList<Object> new161() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TIntegerConstant tintegerconstantNode3 = (TIntegerConstant) nodeArrayList1.get(0);
        PConstant pconstantNode1 = new AIntegerConstant(null, tintegerconstantNode3);
        nodeList.add(pconstantNode1);
        return nodeList;
    }

    ArrayList<Object> new162() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TMinus tminusNode2 = (TMinus) nodeArrayList1.get(0);
        TIntegerConstant tintegerconstantNode3 = (TIntegerConstant) nodeArrayList2.get(0);
        PConstant pconstantNode1 = new AIntegerConstant(tminusNode2, tintegerconstantNode3);
        nodeList.add(pconstantNode1);
        return nodeList;
    }

    ArrayList<Object> new163() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TFloatConstant tfloatconstantNode3 = (TFloatConstant) nodeArrayList1.get(0);
        PConstant pconstantNode1 = new AFloatConstant(null, tfloatconstantNode3);
        nodeList.add(pconstantNode1);
        return nodeList;
    }

    ArrayList<Object> new164() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TMinus tminusNode2 = (TMinus) nodeArrayList1.get(0);
        TFloatConstant tfloatconstantNode3 = (TFloatConstant) nodeArrayList2.get(0);
        PConstant pconstantNode1 = new AFloatConstant(tminusNode2, tfloatconstantNode3);
        nodeList.add(pconstantNode1);
        return nodeList;
    }

    ArrayList<Object> new165() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TStringConstant tstringconstantNode2 = (TStringConstant) nodeArrayList1.get(0);
        PConstant pconstantNode1 = new AStringConstant(tstringconstantNode2);
        nodeList.add(pconstantNode1);
        return nodeList;
    }

    ArrayList<Object> new166() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        TClass tclassNode2 = (TClass) nodeArrayList1.get(0);
        TStringConstant tstringconstantNode3 = (TStringConstant) nodeArrayList2.get(0);
        PConstant pconstantNode1 = new AClzzConstant(tclassNode2, tstringconstantNode3);
        nodeList.add(pconstantNode1);
        return nodeList;
    }

    ArrayList<Object> new167() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TNull tnullNode2 = (TNull) nodeArrayList1.get(0);
        PConstant pconstantNode1 = new ANullConstant(tnullNode2);
        nodeList.add(pconstantNode1);
        return nodeList;
    }

    ArrayList<Object> new168() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TAnd tandNode2 = (TAnd) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new AAndBinop(tandNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new169() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TOr torNode2 = (TOr) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new AOrBinop(torNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new170() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TXor txorNode2 = (TXor) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new AXorBinop(txorNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new171() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TMod tmodNode2 = (TMod) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new AModBinop(tmodNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new172() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TCmp tcmpNode2 = (TCmp) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new ACmpBinop(tcmpNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new173() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TCmpg tcmpgNode2 = (TCmpg) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new ACmpgBinop(tcmpgNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new174() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TCmpl tcmplNode2 = (TCmpl) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new ACmplBinop(tcmplNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new175() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TCmpeq tcmpeqNode2 = (TCmpeq) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new ACmpeqBinop(tcmpeqNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new176() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TCmpne tcmpneNode2 = (TCmpne) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new ACmpneBinop(tcmpneNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new177() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TCmpgt tcmpgtNode2 = (TCmpgt) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new ACmpgtBinop(tcmpgtNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new178() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TCmpge tcmpgeNode2 = (TCmpge) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new ACmpgeBinop(tcmpgeNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new179() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TCmplt tcmpltNode2 = (TCmplt) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new ACmpltBinop(tcmpltNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new180() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TCmple tcmpleNode2 = (TCmple) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new ACmpleBinop(tcmpleNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new181() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TShl tshlNode2 = (TShl) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new AShlBinop(tshlNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new182() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TShr tshrNode2 = (TShr) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new AShrBinop(tshrNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new183() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TUshr tushrNode2 = (TUshr) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new AUshrBinop(tushrNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new184() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TPlus tplusNode2 = (TPlus) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new APlusBinop(tplusNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new185() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TMinus tminusNode2 = (TMinus) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new AMinusBinop(tminusNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new186() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TMult tmultNode2 = (TMult) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new AMultBinop(tmultNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new187() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TDiv tdivNode2 = (TDiv) nodeArrayList1.get(0);
        PBinop pbinopNode1 = new ADivBinop(tdivNode2);
        nodeList.add(pbinopNode1);
        return nodeList;
    }

    ArrayList<Object> new188() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TLengthof tlengthofNode2 = (TLengthof) nodeArrayList1.get(0);
        PUnop punopNode1 = new ALengthofUnop(tlengthofNode2);
        nodeList.add(punopNode1);
        return nodeList;
    }

    ArrayList<Object> new189() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TNeg tnegNode2 = (TNeg) nodeArrayList1.get(0);
        PUnop punopNode1 = new ANegUnop(tnegNode2);
        nodeList.add(punopNode1);
        return nodeList;
    }

    ArrayList<Object> new190() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TQuotedName tquotednameNode2 = (TQuotedName) nodeArrayList1.get(0);
        PClassName pclassnameNode1 = new AQuotedClassName(tquotednameNode2);
        nodeList.add(pclassnameNode1);
        return nodeList;
    }

    ArrayList<Object> new191() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TIdentifier tidentifierNode2 = (TIdentifier) nodeArrayList1.get(0);
        PClassName pclassnameNode1 = new AIdentClassName(tidentifierNode2);
        nodeList.add(pclassnameNode1);
        return nodeList;
    }

    ArrayList<Object> new192() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TFullIdentifier tfullidentifierNode2 = (TFullIdentifier) nodeArrayList1.get(0);
        PClassName pclassnameNode1 = new AFullIdentClassName(tfullidentifierNode2);
        nodeList.add(pclassnameNode1);
        return nodeList;
    }

    ArrayList<Object> new193() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TQuotedName tquotednameNode2 = (TQuotedName) nodeArrayList1.get(0);
        PName pnameNode1 = new AQuotedName(tquotednameNode2);
        nodeList.add(pnameNode1);
        return nodeList;
    }

    ArrayList<Object> new194() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        TIdentifier tidentifierNode2 = (TIdentifier) nodeArrayList1.get(0);
        PName pnameNode1 = new AIdentName(tidentifierNode2);
        nodeList.add(pnameNode1);
        return nodeList;
    }

    ArrayList<Object> new195() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode2 = new LinkedList<>();
        PModifier pmodifierNode1 = (PModifier) nodeArrayList1.get(0);
        if (pmodifierNode1 != null) {
            listNode2.add(pmodifierNode1);
        }
        nodeList.add(listNode2);
        return nodeList;
    }

    ArrayList<Object> new196() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        new LinkedList();
        LinkedList<Object> listNode1 = (LinkedList) nodeArrayList1.get(0);
        PModifier pmodifierNode2 = (PModifier) nodeArrayList2.get(0);
        if (listNode1 != null) {
            listNode3.addAll(listNode1);
        }
        if (pmodifierNode2 != null) {
            listNode3.add(pmodifierNode2);
        }
        nodeList.add(listNode3);
        return nodeList;
    }

    ArrayList<Object> new197() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode2 = new LinkedList<>();
        PMember pmemberNode1 = (PMember) nodeArrayList1.get(0);
        if (pmemberNode1 != null) {
            listNode2.add(pmemberNode1);
        }
        nodeList.add(listNode2);
        return nodeList;
    }

    ArrayList<Object> new198() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        new LinkedList();
        LinkedList<Object> listNode1 = (LinkedList) nodeArrayList1.get(0);
        PMember pmemberNode2 = (PMember) nodeArrayList2.get(0);
        if (listNode1 != null) {
            listNode3.addAll(listNode1);
        }
        if (pmemberNode2 != null) {
            listNode3.add(pmemberNode2);
        }
        nodeList.add(listNode3);
        return nodeList;
    }

    ArrayList<Object> new199() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode2 = new LinkedList<>();
        PArrayBrackets parraybracketsNode1 = (PArrayBrackets) nodeArrayList1.get(0);
        if (parraybracketsNode1 != null) {
            listNode2.add(parraybracketsNode1);
        }
        nodeList.add(listNode2);
        return nodeList;
    }

    ArrayList<Object> new200() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        new LinkedList();
        LinkedList<Object> listNode1 = (LinkedList) nodeArrayList1.get(0);
        PArrayBrackets parraybracketsNode2 = (PArrayBrackets) nodeArrayList2.get(0);
        if (listNode1 != null) {
            listNode3.addAll(listNode1);
        }
        if (parraybracketsNode2 != null) {
            listNode3.add(parraybracketsNode2);
        }
        nodeList.add(listNode3);
        return nodeList;
    }

    ArrayList<Object> new201() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode2 = new LinkedList<>();
        PDeclaration pdeclarationNode1 = (PDeclaration) nodeArrayList1.get(0);
        if (pdeclarationNode1 != null) {
            listNode2.add(pdeclarationNode1);
        }
        nodeList.add(listNode2);
        return nodeList;
    }

    ArrayList<Object> new202() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        new LinkedList();
        LinkedList<Object> listNode1 = (LinkedList) nodeArrayList1.get(0);
        PDeclaration pdeclarationNode2 = (PDeclaration) nodeArrayList2.get(0);
        if (listNode1 != null) {
            listNode3.addAll(listNode1);
        }
        if (pdeclarationNode2 != null) {
            listNode3.add(pdeclarationNode2);
        }
        nodeList.add(listNode3);
        return nodeList;
    }

    ArrayList<Object> new203() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode2 = new LinkedList<>();
        PStatement pstatementNode1 = (PStatement) nodeArrayList1.get(0);
        if (pstatementNode1 != null) {
            listNode2.add(pstatementNode1);
        }
        nodeList.add(listNode2);
        return nodeList;
    }

    ArrayList<Object> new204() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        new LinkedList();
        LinkedList<Object> listNode1 = (LinkedList) nodeArrayList1.get(0);
        PStatement pstatementNode2 = (PStatement) nodeArrayList2.get(0);
        if (listNode1 != null) {
            listNode3.addAll(listNode1);
        }
        if (pstatementNode2 != null) {
            listNode3.add(pstatementNode2);
        }
        nodeList.add(listNode3);
        return nodeList;
    }

    ArrayList<Object> new205() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode2 = new LinkedList<>();
        PCatchClause pcatchclauseNode1 = (PCatchClause) nodeArrayList1.get(0);
        if (pcatchclauseNode1 != null) {
            listNode2.add(pcatchclauseNode1);
        }
        nodeList.add(listNode2);
        return nodeList;
    }

    ArrayList<Object> new206() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        new LinkedList();
        LinkedList<Object> listNode1 = (LinkedList) nodeArrayList1.get(0);
        PCatchClause pcatchclauseNode2 = (PCatchClause) nodeArrayList2.get(0);
        if (listNode1 != null) {
            listNode3.addAll(listNode1);
        }
        if (pcatchclauseNode2 != null) {
            listNode3.add(pcatchclauseNode2);
        }
        nodeList.add(listNode3);
        return nodeList;
    }

    ArrayList<Object> new207() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode2 = new LinkedList<>();
        PCaseStmt pcasestmtNode1 = (PCaseStmt) nodeArrayList1.get(0);
        if (pcasestmtNode1 != null) {
            listNode2.add(pcasestmtNode1);
        }
        nodeList.add(listNode2);
        return nodeList;
    }

    ArrayList<Object> new208() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        new LinkedList();
        LinkedList<Object> listNode1 = (LinkedList) nodeArrayList1.get(0);
        PCaseStmt pcasestmtNode2 = (PCaseStmt) nodeArrayList2.get(0);
        if (listNode1 != null) {
            listNode3.addAll(listNode1);
        }
        if (pcasestmtNode2 != null) {
            listNode3.add(pcasestmtNode2);
        }
        nodeList.add(listNode3);
        return nodeList;
    }

    ArrayList<Object> new209() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode2 = new LinkedList<>();
        PArrayDescriptor parraydescriptorNode1 = (PArrayDescriptor) nodeArrayList1.get(0);
        if (parraydescriptorNode1 != null) {
            listNode2.add(parraydescriptorNode1);
        }
        nodeList.add(listNode2);
        return nodeList;
    }

    ArrayList<Object> new210() {
        ArrayList<Object> nodeList = new ArrayList<>();
        ArrayList<Object> nodeArrayList2 = pop();
        ArrayList<Object> nodeArrayList1 = pop();
        LinkedList<Object> listNode3 = new LinkedList<>();
        new LinkedList();
        LinkedList<Object> listNode1 = (LinkedList) nodeArrayList1.get(0);
        PArrayDescriptor parraydescriptorNode2 = (PArrayDescriptor) nodeArrayList2.get(0);
        if (listNode1 != null) {
            listNode3.addAll(listNode1);
        }
        if (parraydescriptorNode2 != null) {
            listNode3.add(parraydescriptorNode2);
        }
        nodeList.add(listNode3);
        return nodeList;
    }

    /* JADX WARN: Type inference failed for: r0v11, types: [int[][], int[][][]] */
    /* JADX WARN: Type inference failed for: r0v5, types: [int[][], int[][][]] */
    static {
        try {
            DataInputStream s = new DataInputStream(new BufferedInputStream(Parser.class.getResourceAsStream("/parser.dat")));
            int length = s.readInt();
            actionTable = new int[length];
            for (int i = 0; i < actionTable.length; i++) {
                int length2 = s.readInt();
                actionTable[i] = new int[length2][3];
                for (int j = 0; j < actionTable[i].length; j++) {
                    for (int k = 0; k < 3; k++) {
                        actionTable[i][j][k] = s.readInt();
                    }
                }
            }
            int length3 = s.readInt();
            gotoTable = new int[length3];
            for (int i2 = 0; i2 < gotoTable.length; i2++) {
                int length4 = s.readInt();
                gotoTable[i2] = new int[length4][2];
                for (int j2 = 0; j2 < gotoTable[i2].length; j2++) {
                    for (int k2 = 0; k2 < 2; k2++) {
                        gotoTable[i2][j2][k2] = s.readInt();
                    }
                }
            }
            int length5 = s.readInt();
            errorMessages = new String[length5];
            for (int i3 = 0; i3 < errorMessages.length; i3++) {
                int length6 = s.readInt();
                StringBuffer buffer = new StringBuffer();
                for (int j3 = 0; j3 < length6; j3++) {
                    buffer.append(s.readChar());
                }
                errorMessages[i3] = buffer.toString();
            }
            int length7 = s.readInt();
            errors = new int[length7];
            for (int i4 = 0; i4 < errors.length; i4++) {
                errors[i4] = s.readInt();
            }
            s.close();
        } catch (Exception e) {
            throw new RuntimeException("The file \"parser.dat\" is either missing or corrupted.");
        }
    }
}
