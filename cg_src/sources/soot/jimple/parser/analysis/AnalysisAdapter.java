package soot.jimple.parser.analysis;

import java.util.Hashtable;
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
import soot.jimple.parser.node.Start;
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
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/analysis/AnalysisAdapter.class */
public class AnalysisAdapter implements Analysis {
    private Hashtable<Node, Object> in;
    private Hashtable<Node, Object> out;

    @Override // soot.jimple.parser.analysis.Analysis
    public Object getIn(Node node) {
        if (this.in == null) {
            return null;
        }
        return this.in.get(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void setIn(Node node, Object o) {
        if (this.in == null) {
            this.in = new Hashtable<>(1);
        }
        if (o != null) {
            this.in.put(node, o);
        } else {
            this.in.remove(node);
        }
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public Object getOut(Node node) {
        if (this.out == null) {
            return null;
        }
        return this.out.get(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void setOut(Node node, Object o) {
        if (this.out == null) {
            this.out = new Hashtable<>(1);
        }
        if (o != null) {
            this.out.put(node, o);
        } else {
            this.out.remove(node);
        }
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseStart(Start node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAFile(AFile node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAAbstractModifier(AAbstractModifier node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAFinalModifier(AFinalModifier node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseANativeModifier(ANativeModifier node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAPublicModifier(APublicModifier node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAProtectedModifier(AProtectedModifier node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAPrivateModifier(APrivateModifier node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAStaticModifier(AStaticModifier node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseASynchronizedModifier(ASynchronizedModifier node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseATransientModifier(ATransientModifier node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAVolatileModifier(AVolatileModifier node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAStrictfpModifier(AStrictfpModifier node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAEnumModifier(AEnumModifier node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAAnnotationModifier(AAnnotationModifier node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAClassFileType(AClassFileType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAInterfaceFileType(AInterfaceFileType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAExtendsClause(AExtendsClause node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAImplementsClause(AImplementsClause node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAFileBody(AFileBody node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseASingleNameList(ASingleNameList node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAMultiNameList(AMultiNameList node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAClassNameSingleClassNameList(AClassNameSingleClassNameList node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAClassNameMultiClassNameList(AClassNameMultiClassNameList node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAFieldMember(AFieldMember node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAMethodMember(AMethodMember node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAVoidType(AVoidType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseANovoidType(ANovoidType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseASingleParameterList(ASingleParameterList node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAMultiParameterList(AMultiParameterList node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAParameter(AParameter node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAThrowsClause(AThrowsClause node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseABooleanBaseTypeNoName(ABooleanBaseTypeNoName node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAByteBaseTypeNoName(AByteBaseTypeNoName node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseACharBaseTypeNoName(ACharBaseTypeNoName node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAShortBaseTypeNoName(AShortBaseTypeNoName node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAIntBaseTypeNoName(AIntBaseTypeNoName node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseALongBaseTypeNoName(ALongBaseTypeNoName node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAFloatBaseTypeNoName(AFloatBaseTypeNoName node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseADoubleBaseTypeNoName(ADoubleBaseTypeNoName node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseANullBaseTypeNoName(ANullBaseTypeNoName node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseABooleanBaseType(ABooleanBaseType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAByteBaseType(AByteBaseType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseACharBaseType(ACharBaseType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAShortBaseType(AShortBaseType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAIntBaseType(AIntBaseType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseALongBaseType(ALongBaseType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAFloatBaseType(AFloatBaseType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseADoubleBaseType(ADoubleBaseType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseANullBaseType(ANullBaseType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAClassNameBaseType(AClassNameBaseType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseABaseNonvoidType(ABaseNonvoidType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAQuotedNonvoidType(AQuotedNonvoidType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAIdentNonvoidType(AIdentNonvoidType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAFullIdentNonvoidType(AFullIdentNonvoidType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAArrayBrackets(AArrayBrackets node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAEmptyMethodBody(AEmptyMethodBody node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAFullMethodBody(AFullMethodBody node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseADeclaration(ADeclaration node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAUnknownJimpleType(AUnknownJimpleType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseANonvoidJimpleType(ANonvoidJimpleType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseALocalName(ALocalName node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseASingleLocalNameList(ASingleLocalNameList node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAMultiLocalNameList(AMultiLocalNameList node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseALabelStatement(ALabelStatement node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseABreakpointStatement(ABreakpointStatement node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAEntermonitorStatement(AEntermonitorStatement node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAExitmonitorStatement(AExitmonitorStatement node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseATableswitchStatement(ATableswitchStatement node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseALookupswitchStatement(ALookupswitchStatement node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAIdentityStatement(AIdentityStatement node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAIdentityNoTypeStatement(AIdentityNoTypeStatement node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAAssignStatement(AAssignStatement node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAIfStatement(AIfStatement node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAGotoStatement(AGotoStatement node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseANopStatement(ANopStatement node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseARetStatement(ARetStatement node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAReturnStatement(AReturnStatement node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAThrowStatement(AThrowStatement node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAInvokeStatement(AInvokeStatement node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseALabelName(ALabelName node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseACaseStmt(ACaseStmt node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAConstantCaseLabel(AConstantCaseLabel node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseADefaultCaseLabel(ADefaultCaseLabel node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAGotoStmt(AGotoStmt node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseACatchClause(ACatchClause node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseANewExpression(ANewExpression node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseACastExpression(ACastExpression node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAInstanceofExpression(AInstanceofExpression node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAInvokeExpression(AInvokeExpression node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAReferenceExpression(AReferenceExpression node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseABinopExpression(ABinopExpression node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAUnopExpression(AUnopExpression node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAImmediateExpression(AImmediateExpression node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseASimpleNewExpr(ASimpleNewExpr node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAArrayNewExpr(AArrayNewExpr node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAMultiNewExpr(AMultiNewExpr node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAArrayDescriptor(AArrayDescriptor node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAReferenceVariable(AReferenceVariable node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseALocalVariable(ALocalVariable node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseABinopBoolExpr(ABinopBoolExpr node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAUnopBoolExpr(AUnopBoolExpr node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseANonstaticInvokeExpr(ANonstaticInvokeExpr node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAStaticInvokeExpr(AStaticInvokeExpr node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseADynamicInvokeExpr(ADynamicInvokeExpr node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseABinopExpr(ABinopExpr node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAUnopExpr(AUnopExpr node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseASpecialNonstaticInvoke(ASpecialNonstaticInvoke node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAVirtualNonstaticInvoke(AVirtualNonstaticInvoke node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAInterfaceNonstaticInvoke(AInterfaceNonstaticInvoke node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAUnnamedMethodSignature(AUnnamedMethodSignature node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAMethodSignature(AMethodSignature node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAArrayReference(AArrayReference node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAFieldReference(AFieldReference node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAIdentArrayRef(AIdentArrayRef node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAQuotedArrayRef(AQuotedArrayRef node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseALocalFieldRef(ALocalFieldRef node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseASigFieldRef(ASigFieldRef node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAFieldSignature(AFieldSignature node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAFixedArrayDescriptor(AFixedArrayDescriptor node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseASingleArgList(ASingleArgList node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAMultiArgList(AMultiArgList node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseALocalImmediate(ALocalImmediate node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAConstantImmediate(AConstantImmediate node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAIntegerConstant(AIntegerConstant node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAFloatConstant(AFloatConstant node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAStringConstant(AStringConstant node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAClzzConstant(AClzzConstant node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseANullConstant(ANullConstant node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAAndBinop(AAndBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAOrBinop(AOrBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAXorBinop(AXorBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAModBinop(AModBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseACmpBinop(ACmpBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseACmpgBinop(ACmpgBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseACmplBinop(ACmplBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseACmpeqBinop(ACmpeqBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseACmpneBinop(ACmpneBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseACmpgtBinop(ACmpgtBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseACmpgeBinop(ACmpgeBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseACmpltBinop(ACmpltBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseACmpleBinop(ACmpleBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAShlBinop(AShlBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAShrBinop(AShrBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAUshrBinop(AUshrBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAPlusBinop(APlusBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAMinusBinop(AMinusBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAMultBinop(AMultBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseADivBinop(ADivBinop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseALengthofUnop(ALengthofUnop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseANegUnop(ANegUnop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAQuotedClassName(AQuotedClassName node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAIdentClassName(AIdentClassName node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAFullIdentClassName(AFullIdentClassName node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAQuotedName(AQuotedName node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseAIdentName(AIdentName node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTIgnored(TIgnored node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTAbstract(TAbstract node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTFinal(TFinal node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTNative(TNative node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTPublic(TPublic node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTProtected(TProtected node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTPrivate(TPrivate node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTStatic(TStatic node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTSynchronized(TSynchronized node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTTransient(TTransient node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTVolatile(TVolatile node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTStrictfp(TStrictfp node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTEnum(TEnum node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTAnnotation(TAnnotation node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTClass(TClass node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTInterface(TInterface node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTVoid(TVoid node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTBoolean(TBoolean node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTByte(TByte node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTShort(TShort node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTChar(TChar node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTInt(TInt node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTLong(TLong node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTFloat(TFloat node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTDouble(TDouble node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTNullType(TNullType node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTUnknown(TUnknown node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTExtends(TExtends node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTImplements(TImplements node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTBreakpoint(TBreakpoint node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTCase(TCase node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTCatch(TCatch node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTCmp(TCmp node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTCmpg(TCmpg node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTCmpl(TCmpl node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTDefault(TDefault node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTEntermonitor(TEntermonitor node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTExitmonitor(TExitmonitor node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTGoto(TGoto node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTIf(TIf node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTInstanceof(TInstanceof node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTInterfaceinvoke(TInterfaceinvoke node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTLengthof(TLengthof node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTLookupswitch(TLookupswitch node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTNeg(TNeg node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTNew(TNew node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTNewarray(TNewarray node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTNewmultiarray(TNewmultiarray node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTNop(TNop node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTRet(TRet node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTReturn(TReturn node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTSpecialinvoke(TSpecialinvoke node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTStaticinvoke(TStaticinvoke node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTDynamicinvoke(TDynamicinvoke node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTTableswitch(TTableswitch node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTThrow(TThrow node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTThrows(TThrows node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTVirtualinvoke(TVirtualinvoke node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTNull(TNull node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTFrom(TFrom node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTTo(TTo node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTWith(TWith node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTCls(TCls node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTComma(TComma node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTLBrace(TLBrace node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTRBrace(TRBrace node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTSemicolon(TSemicolon node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTLBracket(TLBracket node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTRBracket(TRBracket node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTLParen(TLParen node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTRParen(TRParen node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTColon(TColon node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTDot(TDot node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTQuote(TQuote node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTColonEquals(TColonEquals node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTEquals(TEquals node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTAnd(TAnd node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTOr(TOr node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTXor(TXor node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTMod(TMod node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTCmpeq(TCmpeq node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTCmpne(TCmpne node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTCmpgt(TCmpgt node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTCmpge(TCmpge node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTCmplt(TCmplt node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTCmple(TCmple node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTShl(TShl node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTShr(TShr node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTUshr(TUshr node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTPlus(TPlus node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTMinus(TMinus node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTMult(TMult node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTDiv(TDiv node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTQuotedName(TQuotedName node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTFullIdentifier(TFullIdentifier node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTIdentifier(TIdentifier node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTAtIdentifier(TAtIdentifier node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTBoolConstant(TBoolConstant node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTIntegerConstant(TIntegerConstant node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTFloatConstant(TFloatConstant node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseTStringConstant(TStringConstant node) {
        defaultCase(node);
    }

    @Override // soot.jimple.parser.analysis.Analysis
    public void caseEOF(EOF node) {
        defaultCase(node);
    }

    public void defaultCase(Node node) {
    }
}
