package soot.jimple.parser.parser;

import soot.jimple.parser.analysis.AnalysisAdapter;
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
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/parser/TokenIndex.class */
public class TokenIndex extends AnalysisAdapter {
    int index;

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTAbstract(TAbstract node) {
        this.index = 0;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTFinal(TFinal node) {
        this.index = 1;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTNative(TNative node) {
        this.index = 2;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTPublic(TPublic node) {
        this.index = 3;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTProtected(TProtected node) {
        this.index = 4;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTPrivate(TPrivate node) {
        this.index = 5;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTStatic(TStatic node) {
        this.index = 6;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTSynchronized(TSynchronized node) {
        this.index = 7;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTTransient(TTransient node) {
        this.index = 8;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTVolatile(TVolatile node) {
        this.index = 9;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTStrictfp(TStrictfp node) {
        this.index = 10;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTEnum(TEnum node) {
        this.index = 11;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTAnnotation(TAnnotation node) {
        this.index = 12;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTClass(TClass node) {
        this.index = 13;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTInterface(TInterface node) {
        this.index = 14;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTVoid(TVoid node) {
        this.index = 15;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTBoolean(TBoolean node) {
        this.index = 16;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTByte(TByte node) {
        this.index = 17;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTShort(TShort node) {
        this.index = 18;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTChar(TChar node) {
        this.index = 19;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTInt(TInt node) {
        this.index = 20;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTLong(TLong node) {
        this.index = 21;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTFloat(TFloat node) {
        this.index = 22;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTDouble(TDouble node) {
        this.index = 23;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTNullType(TNullType node) {
        this.index = 24;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTUnknown(TUnknown node) {
        this.index = 25;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTExtends(TExtends node) {
        this.index = 26;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTImplements(TImplements node) {
        this.index = 27;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTBreakpoint(TBreakpoint node) {
        this.index = 28;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTCase(TCase node) {
        this.index = 29;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTCatch(TCatch node) {
        this.index = 30;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTCmp(TCmp node) {
        this.index = 31;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTCmpg(TCmpg node) {
        this.index = 32;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTCmpl(TCmpl node) {
        this.index = 33;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTDefault(TDefault node) {
        this.index = 34;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTEntermonitor(TEntermonitor node) {
        this.index = 35;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTExitmonitor(TExitmonitor node) {
        this.index = 36;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTGoto(TGoto node) {
        this.index = 37;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTIf(TIf node) {
        this.index = 38;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTInstanceof(TInstanceof node) {
        this.index = 39;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTInterfaceinvoke(TInterfaceinvoke node) {
        this.index = 40;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTLengthof(TLengthof node) {
        this.index = 41;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTLookupswitch(TLookupswitch node) {
        this.index = 42;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTNeg(TNeg node) {
        this.index = 43;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTNew(TNew node) {
        this.index = 44;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTNewarray(TNewarray node) {
        this.index = 45;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTNewmultiarray(TNewmultiarray node) {
        this.index = 46;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTNop(TNop node) {
        this.index = 47;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTRet(TRet node) {
        this.index = 48;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTReturn(TReturn node) {
        this.index = 49;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTSpecialinvoke(TSpecialinvoke node) {
        this.index = 50;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTStaticinvoke(TStaticinvoke node) {
        this.index = 51;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTDynamicinvoke(TDynamicinvoke node) {
        this.index = 52;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTTableswitch(TTableswitch node) {
        this.index = 53;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTThrow(TThrow node) {
        this.index = 54;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTThrows(TThrows node) {
        this.index = 55;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTVirtualinvoke(TVirtualinvoke node) {
        this.index = 56;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTNull(TNull node) {
        this.index = 57;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTFrom(TFrom node) {
        this.index = 58;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTTo(TTo node) {
        this.index = 59;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTWith(TWith node) {
        this.index = 60;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTCls(TCls node) {
        this.index = 61;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTComma(TComma node) {
        this.index = 62;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTLBrace(TLBrace node) {
        this.index = 63;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTRBrace(TRBrace node) {
        this.index = 64;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTSemicolon(TSemicolon node) {
        this.index = 65;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTLBracket(TLBracket node) {
        this.index = 66;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTRBracket(TRBracket node) {
        this.index = 67;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTLParen(TLParen node) {
        this.index = 68;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTRParen(TRParen node) {
        this.index = 69;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTColon(TColon node) {
        this.index = 70;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTDot(TDot node) {
        this.index = 71;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTQuote(TQuote node) {
        this.index = 72;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTColonEquals(TColonEquals node) {
        this.index = 73;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTEquals(TEquals node) {
        this.index = 74;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTAnd(TAnd node) {
        this.index = 75;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTOr(TOr node) {
        this.index = 76;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTXor(TXor node) {
        this.index = 77;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTMod(TMod node) {
        this.index = 78;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTCmpeq(TCmpeq node) {
        this.index = 79;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTCmpne(TCmpne node) {
        this.index = 80;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTCmpgt(TCmpgt node) {
        this.index = 81;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTCmpge(TCmpge node) {
        this.index = 82;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTCmplt(TCmplt node) {
        this.index = 83;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTCmple(TCmple node) {
        this.index = 84;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTShl(TShl node) {
        this.index = 85;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTShr(TShr node) {
        this.index = 86;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTUshr(TUshr node) {
        this.index = 87;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTPlus(TPlus node) {
        this.index = 88;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTMinus(TMinus node) {
        this.index = 89;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTMult(TMult node) {
        this.index = 90;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTDiv(TDiv node) {
        this.index = 91;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTQuotedName(TQuotedName node) {
        this.index = 92;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTFullIdentifier(TFullIdentifier node) {
        this.index = 93;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTIdentifier(TIdentifier node) {
        this.index = 94;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTAtIdentifier(TAtIdentifier node) {
        this.index = 95;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTBoolConstant(TBoolConstant node) {
        this.index = 96;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTIntegerConstant(TIntegerConstant node) {
        this.index = 97;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTFloatConstant(TFloatConstant node) {
        this.index = 98;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseTStringConstant(TStringConstant node) {
        this.index = 99;
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseEOF(EOF node) {
        this.index = 100;
    }
}
