package soot.jimple.parser.analysis;

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
import soot.jimple.parser.node.Switch;
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
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/analysis/Analysis.class */
public interface Analysis extends Switch {
    Object getIn(Node node);

    void setIn(Node node, Object obj);

    Object getOut(Node node);

    void setOut(Node node, Object obj);

    void caseStart(Start start);

    void caseAFile(AFile aFile);

    void caseAAbstractModifier(AAbstractModifier aAbstractModifier);

    void caseAFinalModifier(AFinalModifier aFinalModifier);

    void caseANativeModifier(ANativeModifier aNativeModifier);

    void caseAPublicModifier(APublicModifier aPublicModifier);

    void caseAProtectedModifier(AProtectedModifier aProtectedModifier);

    void caseAPrivateModifier(APrivateModifier aPrivateModifier);

    void caseAStaticModifier(AStaticModifier aStaticModifier);

    void caseASynchronizedModifier(ASynchronizedModifier aSynchronizedModifier);

    void caseATransientModifier(ATransientModifier aTransientModifier);

    void caseAVolatileModifier(AVolatileModifier aVolatileModifier);

    void caseAStrictfpModifier(AStrictfpModifier aStrictfpModifier);

    void caseAEnumModifier(AEnumModifier aEnumModifier);

    void caseAAnnotationModifier(AAnnotationModifier aAnnotationModifier);

    void caseAClassFileType(AClassFileType aClassFileType);

    void caseAInterfaceFileType(AInterfaceFileType aInterfaceFileType);

    void caseAExtendsClause(AExtendsClause aExtendsClause);

    void caseAImplementsClause(AImplementsClause aImplementsClause);

    void caseAFileBody(AFileBody aFileBody);

    void caseASingleNameList(ASingleNameList aSingleNameList);

    void caseAMultiNameList(AMultiNameList aMultiNameList);

    void caseAClassNameSingleClassNameList(AClassNameSingleClassNameList aClassNameSingleClassNameList);

    void caseAClassNameMultiClassNameList(AClassNameMultiClassNameList aClassNameMultiClassNameList);

    void caseAFieldMember(AFieldMember aFieldMember);

    void caseAMethodMember(AMethodMember aMethodMember);

    void caseAVoidType(AVoidType aVoidType);

    void caseANovoidType(ANovoidType aNovoidType);

    void caseASingleParameterList(ASingleParameterList aSingleParameterList);

    void caseAMultiParameterList(AMultiParameterList aMultiParameterList);

    void caseAParameter(AParameter aParameter);

    void caseAThrowsClause(AThrowsClause aThrowsClause);

    void caseABooleanBaseTypeNoName(ABooleanBaseTypeNoName aBooleanBaseTypeNoName);

    void caseAByteBaseTypeNoName(AByteBaseTypeNoName aByteBaseTypeNoName);

    void caseACharBaseTypeNoName(ACharBaseTypeNoName aCharBaseTypeNoName);

    void caseAShortBaseTypeNoName(AShortBaseTypeNoName aShortBaseTypeNoName);

    void caseAIntBaseTypeNoName(AIntBaseTypeNoName aIntBaseTypeNoName);

    void caseALongBaseTypeNoName(ALongBaseTypeNoName aLongBaseTypeNoName);

    void caseAFloatBaseTypeNoName(AFloatBaseTypeNoName aFloatBaseTypeNoName);

    void caseADoubleBaseTypeNoName(ADoubleBaseTypeNoName aDoubleBaseTypeNoName);

    void caseANullBaseTypeNoName(ANullBaseTypeNoName aNullBaseTypeNoName);

    void caseABooleanBaseType(ABooleanBaseType aBooleanBaseType);

    void caseAByteBaseType(AByteBaseType aByteBaseType);

    void caseACharBaseType(ACharBaseType aCharBaseType);

    void caseAShortBaseType(AShortBaseType aShortBaseType);

    void caseAIntBaseType(AIntBaseType aIntBaseType);

    void caseALongBaseType(ALongBaseType aLongBaseType);

    void caseAFloatBaseType(AFloatBaseType aFloatBaseType);

    void caseADoubleBaseType(ADoubleBaseType aDoubleBaseType);

    void caseANullBaseType(ANullBaseType aNullBaseType);

    void caseAClassNameBaseType(AClassNameBaseType aClassNameBaseType);

    void caseABaseNonvoidType(ABaseNonvoidType aBaseNonvoidType);

    void caseAQuotedNonvoidType(AQuotedNonvoidType aQuotedNonvoidType);

    void caseAIdentNonvoidType(AIdentNonvoidType aIdentNonvoidType);

    void caseAFullIdentNonvoidType(AFullIdentNonvoidType aFullIdentNonvoidType);

    void caseAArrayBrackets(AArrayBrackets aArrayBrackets);

    void caseAEmptyMethodBody(AEmptyMethodBody aEmptyMethodBody);

    void caseAFullMethodBody(AFullMethodBody aFullMethodBody);

    void caseADeclaration(ADeclaration aDeclaration);

    void caseAUnknownJimpleType(AUnknownJimpleType aUnknownJimpleType);

    void caseANonvoidJimpleType(ANonvoidJimpleType aNonvoidJimpleType);

    void caseALocalName(ALocalName aLocalName);

    void caseASingleLocalNameList(ASingleLocalNameList aSingleLocalNameList);

    void caseAMultiLocalNameList(AMultiLocalNameList aMultiLocalNameList);

    void caseALabelStatement(ALabelStatement aLabelStatement);

    void caseABreakpointStatement(ABreakpointStatement aBreakpointStatement);

    void caseAEntermonitorStatement(AEntermonitorStatement aEntermonitorStatement);

    void caseAExitmonitorStatement(AExitmonitorStatement aExitmonitorStatement);

    void caseATableswitchStatement(ATableswitchStatement aTableswitchStatement);

    void caseALookupswitchStatement(ALookupswitchStatement aLookupswitchStatement);

    void caseAIdentityStatement(AIdentityStatement aIdentityStatement);

    void caseAIdentityNoTypeStatement(AIdentityNoTypeStatement aIdentityNoTypeStatement);

    void caseAAssignStatement(AAssignStatement aAssignStatement);

    void caseAIfStatement(AIfStatement aIfStatement);

    void caseAGotoStatement(AGotoStatement aGotoStatement);

    void caseANopStatement(ANopStatement aNopStatement);

    void caseARetStatement(ARetStatement aRetStatement);

    void caseAReturnStatement(AReturnStatement aReturnStatement);

    void caseAThrowStatement(AThrowStatement aThrowStatement);

    void caseAInvokeStatement(AInvokeStatement aInvokeStatement);

    void caseALabelName(ALabelName aLabelName);

    void caseACaseStmt(ACaseStmt aCaseStmt);

    void caseAConstantCaseLabel(AConstantCaseLabel aConstantCaseLabel);

    void caseADefaultCaseLabel(ADefaultCaseLabel aDefaultCaseLabel);

    void caseAGotoStmt(AGotoStmt aGotoStmt);

    void caseACatchClause(ACatchClause aCatchClause);

    void caseANewExpression(ANewExpression aNewExpression);

    void caseACastExpression(ACastExpression aCastExpression);

    void caseAInstanceofExpression(AInstanceofExpression aInstanceofExpression);

    void caseAInvokeExpression(AInvokeExpression aInvokeExpression);

    void caseAReferenceExpression(AReferenceExpression aReferenceExpression);

    void caseABinopExpression(ABinopExpression aBinopExpression);

    void caseAUnopExpression(AUnopExpression aUnopExpression);

    void caseAImmediateExpression(AImmediateExpression aImmediateExpression);

    void caseASimpleNewExpr(ASimpleNewExpr aSimpleNewExpr);

    void caseAArrayNewExpr(AArrayNewExpr aArrayNewExpr);

    void caseAMultiNewExpr(AMultiNewExpr aMultiNewExpr);

    void caseAArrayDescriptor(AArrayDescriptor aArrayDescriptor);

    void caseAReferenceVariable(AReferenceVariable aReferenceVariable);

    void caseALocalVariable(ALocalVariable aLocalVariable);

    void caseABinopBoolExpr(ABinopBoolExpr aBinopBoolExpr);

    void caseAUnopBoolExpr(AUnopBoolExpr aUnopBoolExpr);

    void caseANonstaticInvokeExpr(ANonstaticInvokeExpr aNonstaticInvokeExpr);

    void caseAStaticInvokeExpr(AStaticInvokeExpr aStaticInvokeExpr);

    void caseADynamicInvokeExpr(ADynamicInvokeExpr aDynamicInvokeExpr);

    void caseABinopExpr(ABinopExpr aBinopExpr);

    void caseAUnopExpr(AUnopExpr aUnopExpr);

    void caseASpecialNonstaticInvoke(ASpecialNonstaticInvoke aSpecialNonstaticInvoke);

    void caseAVirtualNonstaticInvoke(AVirtualNonstaticInvoke aVirtualNonstaticInvoke);

    void caseAInterfaceNonstaticInvoke(AInterfaceNonstaticInvoke aInterfaceNonstaticInvoke);

    void caseAUnnamedMethodSignature(AUnnamedMethodSignature aUnnamedMethodSignature);

    void caseAMethodSignature(AMethodSignature aMethodSignature);

    void caseAArrayReference(AArrayReference aArrayReference);

    void caseAFieldReference(AFieldReference aFieldReference);

    void caseAIdentArrayRef(AIdentArrayRef aIdentArrayRef);

    void caseAQuotedArrayRef(AQuotedArrayRef aQuotedArrayRef);

    void caseALocalFieldRef(ALocalFieldRef aLocalFieldRef);

    void caseASigFieldRef(ASigFieldRef aSigFieldRef);

    void caseAFieldSignature(AFieldSignature aFieldSignature);

    void caseAFixedArrayDescriptor(AFixedArrayDescriptor aFixedArrayDescriptor);

    void caseASingleArgList(ASingleArgList aSingleArgList);

    void caseAMultiArgList(AMultiArgList aMultiArgList);

    void caseALocalImmediate(ALocalImmediate aLocalImmediate);

    void caseAConstantImmediate(AConstantImmediate aConstantImmediate);

    void caseAIntegerConstant(AIntegerConstant aIntegerConstant);

    void caseAFloatConstant(AFloatConstant aFloatConstant);

    void caseAStringConstant(AStringConstant aStringConstant);

    void caseAClzzConstant(AClzzConstant aClzzConstant);

    void caseANullConstant(ANullConstant aNullConstant);

    void caseAAndBinop(AAndBinop aAndBinop);

    void caseAOrBinop(AOrBinop aOrBinop);

    void caseAXorBinop(AXorBinop aXorBinop);

    void caseAModBinop(AModBinop aModBinop);

    void caseACmpBinop(ACmpBinop aCmpBinop);

    void caseACmpgBinop(ACmpgBinop aCmpgBinop);

    void caseACmplBinop(ACmplBinop aCmplBinop);

    void caseACmpeqBinop(ACmpeqBinop aCmpeqBinop);

    void caseACmpneBinop(ACmpneBinop aCmpneBinop);

    void caseACmpgtBinop(ACmpgtBinop aCmpgtBinop);

    void caseACmpgeBinop(ACmpgeBinop aCmpgeBinop);

    void caseACmpltBinop(ACmpltBinop aCmpltBinop);

    void caseACmpleBinop(ACmpleBinop aCmpleBinop);

    void caseAShlBinop(AShlBinop aShlBinop);

    void caseAShrBinop(AShrBinop aShrBinop);

    void caseAUshrBinop(AUshrBinop aUshrBinop);

    void caseAPlusBinop(APlusBinop aPlusBinop);

    void caseAMinusBinop(AMinusBinop aMinusBinop);

    void caseAMultBinop(AMultBinop aMultBinop);

    void caseADivBinop(ADivBinop aDivBinop);

    void caseALengthofUnop(ALengthofUnop aLengthofUnop);

    void caseANegUnop(ANegUnop aNegUnop);

    void caseAQuotedClassName(AQuotedClassName aQuotedClassName);

    void caseAIdentClassName(AIdentClassName aIdentClassName);

    void caseAFullIdentClassName(AFullIdentClassName aFullIdentClassName);

    void caseAQuotedName(AQuotedName aQuotedName);

    void caseAIdentName(AIdentName aIdentName);

    void caseTIgnored(TIgnored tIgnored);

    void caseTAbstract(TAbstract tAbstract);

    void caseTFinal(TFinal tFinal);

    void caseTNative(TNative tNative);

    void caseTPublic(TPublic tPublic);

    void caseTProtected(TProtected tProtected);

    void caseTPrivate(TPrivate tPrivate);

    void caseTStatic(TStatic tStatic);

    void caseTSynchronized(TSynchronized tSynchronized);

    void caseTTransient(TTransient tTransient);

    void caseTVolatile(TVolatile tVolatile);

    void caseTStrictfp(TStrictfp tStrictfp);

    void caseTEnum(TEnum tEnum);

    void caseTAnnotation(TAnnotation tAnnotation);

    void caseTClass(TClass tClass);

    void caseTInterface(TInterface tInterface);

    void caseTVoid(TVoid tVoid);

    void caseTBoolean(TBoolean tBoolean);

    void caseTByte(TByte tByte);

    void caseTShort(TShort tShort);

    void caseTChar(TChar tChar);

    void caseTInt(TInt tInt);

    void caseTLong(TLong tLong);

    void caseTFloat(TFloat tFloat);

    void caseTDouble(TDouble tDouble);

    void caseTNullType(TNullType tNullType);

    void caseTUnknown(TUnknown tUnknown);

    void caseTExtends(TExtends tExtends);

    void caseTImplements(TImplements tImplements);

    void caseTBreakpoint(TBreakpoint tBreakpoint);

    void caseTCase(TCase tCase);

    void caseTCatch(TCatch tCatch);

    void caseTCmp(TCmp tCmp);

    void caseTCmpg(TCmpg tCmpg);

    void caseTCmpl(TCmpl tCmpl);

    void caseTDefault(TDefault tDefault);

    void caseTEntermonitor(TEntermonitor tEntermonitor);

    void caseTExitmonitor(TExitmonitor tExitmonitor);

    void caseTGoto(TGoto tGoto);

    void caseTIf(TIf tIf);

    void caseTInstanceof(TInstanceof tInstanceof);

    void caseTInterfaceinvoke(TInterfaceinvoke tInterfaceinvoke);

    void caseTLengthof(TLengthof tLengthof);

    void caseTLookupswitch(TLookupswitch tLookupswitch);

    void caseTNeg(TNeg tNeg);

    void caseTNew(TNew tNew);

    void caseTNewarray(TNewarray tNewarray);

    void caseTNewmultiarray(TNewmultiarray tNewmultiarray);

    void caseTNop(TNop tNop);

    void caseTRet(TRet tRet);

    void caseTReturn(TReturn tReturn);

    void caseTSpecialinvoke(TSpecialinvoke tSpecialinvoke);

    void caseTStaticinvoke(TStaticinvoke tStaticinvoke);

    void caseTDynamicinvoke(TDynamicinvoke tDynamicinvoke);

    void caseTTableswitch(TTableswitch tTableswitch);

    void caseTThrow(TThrow tThrow);

    void caseTThrows(TThrows tThrows);

    void caseTVirtualinvoke(TVirtualinvoke tVirtualinvoke);

    void caseTNull(TNull tNull);

    void caseTFrom(TFrom tFrom);

    void caseTTo(TTo tTo);

    void caseTWith(TWith tWith);

    void caseTCls(TCls tCls);

    void caseTComma(TComma tComma);

    void caseTLBrace(TLBrace tLBrace);

    void caseTRBrace(TRBrace tRBrace);

    void caseTSemicolon(TSemicolon tSemicolon);

    void caseTLBracket(TLBracket tLBracket);

    void caseTRBracket(TRBracket tRBracket);

    void caseTLParen(TLParen tLParen);

    void caseTRParen(TRParen tRParen);

    void caseTColon(TColon tColon);

    void caseTDot(TDot tDot);

    void caseTQuote(TQuote tQuote);

    void caseTColonEquals(TColonEquals tColonEquals);

    void caseTEquals(TEquals tEquals);

    void caseTAnd(TAnd tAnd);

    void caseTOr(TOr tOr);

    void caseTXor(TXor tXor);

    void caseTMod(TMod tMod);

    void caseTCmpeq(TCmpeq tCmpeq);

    void caseTCmpne(TCmpne tCmpne);

    void caseTCmpgt(TCmpgt tCmpgt);

    void caseTCmpge(TCmpge tCmpge);

    void caseTCmplt(TCmplt tCmplt);

    void caseTCmple(TCmple tCmple);

    void caseTShl(TShl tShl);

    void caseTShr(TShr tShr);

    void caseTUshr(TUshr tUshr);

    void caseTPlus(TPlus tPlus);

    void caseTMinus(TMinus tMinus);

    void caseTMult(TMult tMult);

    void caseTDiv(TDiv tDiv);

    void caseTQuotedName(TQuotedName tQuotedName);

    void caseTFullIdentifier(TFullIdentifier tFullIdentifier);

    void caseTIdentifier(TIdentifier tIdentifier);

    void caseTAtIdentifier(TAtIdentifier tAtIdentifier);

    void caseTBoolConstant(TBoolConstant tBoolConstant);

    void caseTIntegerConstant(TIntegerConstant tIntegerConstant);

    void caseTFloatConstant(TFloatConstant tFloatConstant);

    void caseTStringConstant(TStringConstant tStringConstant);

    void caseEOF(EOF eof);
}
