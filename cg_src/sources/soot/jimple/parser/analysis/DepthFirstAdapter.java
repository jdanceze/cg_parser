package soot.jimple.parser.analysis;

import java.util.ArrayList;
import java.util.List;
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
import soot.jimple.parser.node.Node;
import soot.jimple.parser.node.PArrayBrackets;
import soot.jimple.parser.node.PArrayDescriptor;
import soot.jimple.parser.node.PCaseStmt;
import soot.jimple.parser.node.PCatchClause;
import soot.jimple.parser.node.PDeclaration;
import soot.jimple.parser.node.PMember;
import soot.jimple.parser.node.PModifier;
import soot.jimple.parser.node.PStatement;
import soot.jimple.parser.node.Start;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/analysis/DepthFirstAdapter.class */
public class DepthFirstAdapter extends AnalysisAdapter {
    public void inStart(Start node) {
        defaultIn(node);
    }

    public void outStart(Start node) {
        defaultOut(node);
    }

    public void defaultIn(Node node) {
    }

    public void defaultOut(Node node) {
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseStart(Start node) {
        inStart(node);
        node.getPFile().apply(this);
        node.getEOF().apply(this);
        outStart(node);
    }

    public void inAFile(AFile node) {
        defaultIn(node);
    }

    public void outAFile(AFile node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAFile(AFile node) {
        inAFile(node);
        List<PModifier> copy = new ArrayList<>(node.getModifier());
        for (PModifier e : copy) {
            e.apply(this);
        }
        if (node.getFileType() != null) {
            node.getFileType().apply(this);
        }
        if (node.getClassName() != null) {
            node.getClassName().apply(this);
        }
        if (node.getExtendsClause() != null) {
            node.getExtendsClause().apply(this);
        }
        if (node.getImplementsClause() != null) {
            node.getImplementsClause().apply(this);
        }
        if (node.getFileBody() != null) {
            node.getFileBody().apply(this);
        }
        outAFile(node);
    }

    public void inAAbstractModifier(AAbstractModifier node) {
        defaultIn(node);
    }

    public void outAAbstractModifier(AAbstractModifier node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAAbstractModifier(AAbstractModifier node) {
        inAAbstractModifier(node);
        if (node.getAbstract() != null) {
            node.getAbstract().apply(this);
        }
        outAAbstractModifier(node);
    }

    public void inAFinalModifier(AFinalModifier node) {
        defaultIn(node);
    }

    public void outAFinalModifier(AFinalModifier node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAFinalModifier(AFinalModifier node) {
        inAFinalModifier(node);
        if (node.getFinal() != null) {
            node.getFinal().apply(this);
        }
        outAFinalModifier(node);
    }

    public void inANativeModifier(ANativeModifier node) {
        defaultIn(node);
    }

    public void outANativeModifier(ANativeModifier node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseANativeModifier(ANativeModifier node) {
        inANativeModifier(node);
        if (node.getNative() != null) {
            node.getNative().apply(this);
        }
        outANativeModifier(node);
    }

    public void inAPublicModifier(APublicModifier node) {
        defaultIn(node);
    }

    public void outAPublicModifier(APublicModifier node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAPublicModifier(APublicModifier node) {
        inAPublicModifier(node);
        if (node.getPublic() != null) {
            node.getPublic().apply(this);
        }
        outAPublicModifier(node);
    }

    public void inAProtectedModifier(AProtectedModifier node) {
        defaultIn(node);
    }

    public void outAProtectedModifier(AProtectedModifier node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAProtectedModifier(AProtectedModifier node) {
        inAProtectedModifier(node);
        if (node.getProtected() != null) {
            node.getProtected().apply(this);
        }
        outAProtectedModifier(node);
    }

    public void inAPrivateModifier(APrivateModifier node) {
        defaultIn(node);
    }

    public void outAPrivateModifier(APrivateModifier node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAPrivateModifier(APrivateModifier node) {
        inAPrivateModifier(node);
        if (node.getPrivate() != null) {
            node.getPrivate().apply(this);
        }
        outAPrivateModifier(node);
    }

    public void inAStaticModifier(AStaticModifier node) {
        defaultIn(node);
    }

    public void outAStaticModifier(AStaticModifier node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAStaticModifier(AStaticModifier node) {
        inAStaticModifier(node);
        if (node.getStatic() != null) {
            node.getStatic().apply(this);
        }
        outAStaticModifier(node);
    }

    public void inASynchronizedModifier(ASynchronizedModifier node) {
        defaultIn(node);
    }

    public void outASynchronizedModifier(ASynchronizedModifier node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseASynchronizedModifier(ASynchronizedModifier node) {
        inASynchronizedModifier(node);
        if (node.getSynchronized() != null) {
            node.getSynchronized().apply(this);
        }
        outASynchronizedModifier(node);
    }

    public void inATransientModifier(ATransientModifier node) {
        defaultIn(node);
    }

    public void outATransientModifier(ATransientModifier node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseATransientModifier(ATransientModifier node) {
        inATransientModifier(node);
        if (node.getTransient() != null) {
            node.getTransient().apply(this);
        }
        outATransientModifier(node);
    }

    public void inAVolatileModifier(AVolatileModifier node) {
        defaultIn(node);
    }

    public void outAVolatileModifier(AVolatileModifier node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAVolatileModifier(AVolatileModifier node) {
        inAVolatileModifier(node);
        if (node.getVolatile() != null) {
            node.getVolatile().apply(this);
        }
        outAVolatileModifier(node);
    }

    public void inAStrictfpModifier(AStrictfpModifier node) {
        defaultIn(node);
    }

    public void outAStrictfpModifier(AStrictfpModifier node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAStrictfpModifier(AStrictfpModifier node) {
        inAStrictfpModifier(node);
        if (node.getStrictfp() != null) {
            node.getStrictfp().apply(this);
        }
        outAStrictfpModifier(node);
    }

    public void inAEnumModifier(AEnumModifier node) {
        defaultIn(node);
    }

    public void outAEnumModifier(AEnumModifier node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAEnumModifier(AEnumModifier node) {
        inAEnumModifier(node);
        if (node.getEnum() != null) {
            node.getEnum().apply(this);
        }
        outAEnumModifier(node);
    }

    public void inAAnnotationModifier(AAnnotationModifier node) {
        defaultIn(node);
    }

    public void outAAnnotationModifier(AAnnotationModifier node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAAnnotationModifier(AAnnotationModifier node) {
        inAAnnotationModifier(node);
        if (node.getAnnotation() != null) {
            node.getAnnotation().apply(this);
        }
        outAAnnotationModifier(node);
    }

    public void inAClassFileType(AClassFileType node) {
        defaultIn(node);
    }

    public void outAClassFileType(AClassFileType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAClassFileType(AClassFileType node) {
        inAClassFileType(node);
        if (node.getTheclass() != null) {
            node.getTheclass().apply(this);
        }
        outAClassFileType(node);
    }

    public void inAInterfaceFileType(AInterfaceFileType node) {
        defaultIn(node);
    }

    public void outAInterfaceFileType(AInterfaceFileType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAInterfaceFileType(AInterfaceFileType node) {
        inAInterfaceFileType(node);
        if (node.getInterface() != null) {
            node.getInterface().apply(this);
        }
        outAInterfaceFileType(node);
    }

    public void inAExtendsClause(AExtendsClause node) {
        defaultIn(node);
    }

    public void outAExtendsClause(AExtendsClause node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAExtendsClause(AExtendsClause node) {
        inAExtendsClause(node);
        if (node.getExtends() != null) {
            node.getExtends().apply(this);
        }
        if (node.getClassName() != null) {
            node.getClassName().apply(this);
        }
        outAExtendsClause(node);
    }

    public void inAImplementsClause(AImplementsClause node) {
        defaultIn(node);
    }

    public void outAImplementsClause(AImplementsClause node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAImplementsClause(AImplementsClause node) {
        inAImplementsClause(node);
        if (node.getImplements() != null) {
            node.getImplements().apply(this);
        }
        if (node.getClassNameList() != null) {
            node.getClassNameList().apply(this);
        }
        outAImplementsClause(node);
    }

    public void inAFileBody(AFileBody node) {
        defaultIn(node);
    }

    public void outAFileBody(AFileBody node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAFileBody(AFileBody node) {
        inAFileBody(node);
        if (node.getLBrace() != null) {
            node.getLBrace().apply(this);
        }
        List<PMember> copy = new ArrayList<>(node.getMember());
        for (PMember e : copy) {
            e.apply(this);
        }
        if (node.getRBrace() != null) {
            node.getRBrace().apply(this);
        }
        outAFileBody(node);
    }

    public void inASingleNameList(ASingleNameList node) {
        defaultIn(node);
    }

    public void outASingleNameList(ASingleNameList node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseASingleNameList(ASingleNameList node) {
        inASingleNameList(node);
        if (node.getName() != null) {
            node.getName().apply(this);
        }
        outASingleNameList(node);
    }

    public void inAMultiNameList(AMultiNameList node) {
        defaultIn(node);
    }

    public void outAMultiNameList(AMultiNameList node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAMultiNameList(AMultiNameList node) {
        inAMultiNameList(node);
        if (node.getName() != null) {
            node.getName().apply(this);
        }
        if (node.getComma() != null) {
            node.getComma().apply(this);
        }
        if (node.getNameList() != null) {
            node.getNameList().apply(this);
        }
        outAMultiNameList(node);
    }

    public void inAClassNameSingleClassNameList(AClassNameSingleClassNameList node) {
        defaultIn(node);
    }

    public void outAClassNameSingleClassNameList(AClassNameSingleClassNameList node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAClassNameSingleClassNameList(AClassNameSingleClassNameList node) {
        inAClassNameSingleClassNameList(node);
        if (node.getClassName() != null) {
            node.getClassName().apply(this);
        }
        outAClassNameSingleClassNameList(node);
    }

    public void inAClassNameMultiClassNameList(AClassNameMultiClassNameList node) {
        defaultIn(node);
    }

    public void outAClassNameMultiClassNameList(AClassNameMultiClassNameList node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAClassNameMultiClassNameList(AClassNameMultiClassNameList node) {
        inAClassNameMultiClassNameList(node);
        if (node.getClassName() != null) {
            node.getClassName().apply(this);
        }
        if (node.getComma() != null) {
            node.getComma().apply(this);
        }
        if (node.getClassNameList() != null) {
            node.getClassNameList().apply(this);
        }
        outAClassNameMultiClassNameList(node);
    }

    public void inAFieldMember(AFieldMember node) {
        defaultIn(node);
    }

    public void outAFieldMember(AFieldMember node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAFieldMember(AFieldMember node) {
        inAFieldMember(node);
        List<PModifier> copy = new ArrayList<>(node.getModifier());
        for (PModifier e : copy) {
            e.apply(this);
        }
        if (node.getType() != null) {
            node.getType().apply(this);
        }
        if (node.getName() != null) {
            node.getName().apply(this);
        }
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        outAFieldMember(node);
    }

    public void inAMethodMember(AMethodMember node) {
        defaultIn(node);
    }

    public void outAMethodMember(AMethodMember node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAMethodMember(AMethodMember node) {
        inAMethodMember(node);
        List<PModifier> copy = new ArrayList<>(node.getModifier());
        for (PModifier e : copy) {
            e.apply(this);
        }
        if (node.getType() != null) {
            node.getType().apply(this);
        }
        if (node.getName() != null) {
            node.getName().apply(this);
        }
        if (node.getLParen() != null) {
            node.getLParen().apply(this);
        }
        if (node.getParameterList() != null) {
            node.getParameterList().apply(this);
        }
        if (node.getRParen() != null) {
            node.getRParen().apply(this);
        }
        if (node.getThrowsClause() != null) {
            node.getThrowsClause().apply(this);
        }
        if (node.getMethodBody() != null) {
            node.getMethodBody().apply(this);
        }
        outAMethodMember(node);
    }

    public void inAVoidType(AVoidType node) {
        defaultIn(node);
    }

    public void outAVoidType(AVoidType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAVoidType(AVoidType node) {
        inAVoidType(node);
        if (node.getVoid() != null) {
            node.getVoid().apply(this);
        }
        outAVoidType(node);
    }

    public void inANovoidType(ANovoidType node) {
        defaultIn(node);
    }

    public void outANovoidType(ANovoidType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseANovoidType(ANovoidType node) {
        inANovoidType(node);
        if (node.getNonvoidType() != null) {
            node.getNonvoidType().apply(this);
        }
        outANovoidType(node);
    }

    public void inASingleParameterList(ASingleParameterList node) {
        defaultIn(node);
    }

    public void outASingleParameterList(ASingleParameterList node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseASingleParameterList(ASingleParameterList node) {
        inASingleParameterList(node);
        if (node.getParameter() != null) {
            node.getParameter().apply(this);
        }
        outASingleParameterList(node);
    }

    public void inAMultiParameterList(AMultiParameterList node) {
        defaultIn(node);
    }

    public void outAMultiParameterList(AMultiParameterList node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAMultiParameterList(AMultiParameterList node) {
        inAMultiParameterList(node);
        if (node.getParameter() != null) {
            node.getParameter().apply(this);
        }
        if (node.getComma() != null) {
            node.getComma().apply(this);
        }
        if (node.getParameterList() != null) {
            node.getParameterList().apply(this);
        }
        outAMultiParameterList(node);
    }

    public void inAParameter(AParameter node) {
        defaultIn(node);
    }

    public void outAParameter(AParameter node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAParameter(AParameter node) {
        inAParameter(node);
        if (node.getNonvoidType() != null) {
            node.getNonvoidType().apply(this);
        }
        outAParameter(node);
    }

    public void inAThrowsClause(AThrowsClause node) {
        defaultIn(node);
    }

    public void outAThrowsClause(AThrowsClause node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAThrowsClause(AThrowsClause node) {
        inAThrowsClause(node);
        if (node.getThrows() != null) {
            node.getThrows().apply(this);
        }
        if (node.getClassNameList() != null) {
            node.getClassNameList().apply(this);
        }
        outAThrowsClause(node);
    }

    public void inABooleanBaseTypeNoName(ABooleanBaseTypeNoName node) {
        defaultIn(node);
    }

    public void outABooleanBaseTypeNoName(ABooleanBaseTypeNoName node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseABooleanBaseTypeNoName(ABooleanBaseTypeNoName node) {
        inABooleanBaseTypeNoName(node);
        if (node.getBoolean() != null) {
            node.getBoolean().apply(this);
        }
        outABooleanBaseTypeNoName(node);
    }

    public void inAByteBaseTypeNoName(AByteBaseTypeNoName node) {
        defaultIn(node);
    }

    public void outAByteBaseTypeNoName(AByteBaseTypeNoName node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAByteBaseTypeNoName(AByteBaseTypeNoName node) {
        inAByteBaseTypeNoName(node);
        if (node.getByte() != null) {
            node.getByte().apply(this);
        }
        outAByteBaseTypeNoName(node);
    }

    public void inACharBaseTypeNoName(ACharBaseTypeNoName node) {
        defaultIn(node);
    }

    public void outACharBaseTypeNoName(ACharBaseTypeNoName node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseACharBaseTypeNoName(ACharBaseTypeNoName node) {
        inACharBaseTypeNoName(node);
        if (node.getChar() != null) {
            node.getChar().apply(this);
        }
        outACharBaseTypeNoName(node);
    }

    public void inAShortBaseTypeNoName(AShortBaseTypeNoName node) {
        defaultIn(node);
    }

    public void outAShortBaseTypeNoName(AShortBaseTypeNoName node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAShortBaseTypeNoName(AShortBaseTypeNoName node) {
        inAShortBaseTypeNoName(node);
        if (node.getShort() != null) {
            node.getShort().apply(this);
        }
        outAShortBaseTypeNoName(node);
    }

    public void inAIntBaseTypeNoName(AIntBaseTypeNoName node) {
        defaultIn(node);
    }

    public void outAIntBaseTypeNoName(AIntBaseTypeNoName node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAIntBaseTypeNoName(AIntBaseTypeNoName node) {
        inAIntBaseTypeNoName(node);
        if (node.getInt() != null) {
            node.getInt().apply(this);
        }
        outAIntBaseTypeNoName(node);
    }

    public void inALongBaseTypeNoName(ALongBaseTypeNoName node) {
        defaultIn(node);
    }

    public void outALongBaseTypeNoName(ALongBaseTypeNoName node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseALongBaseTypeNoName(ALongBaseTypeNoName node) {
        inALongBaseTypeNoName(node);
        if (node.getLong() != null) {
            node.getLong().apply(this);
        }
        outALongBaseTypeNoName(node);
    }

    public void inAFloatBaseTypeNoName(AFloatBaseTypeNoName node) {
        defaultIn(node);
    }

    public void outAFloatBaseTypeNoName(AFloatBaseTypeNoName node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAFloatBaseTypeNoName(AFloatBaseTypeNoName node) {
        inAFloatBaseTypeNoName(node);
        if (node.getFloat() != null) {
            node.getFloat().apply(this);
        }
        outAFloatBaseTypeNoName(node);
    }

    public void inADoubleBaseTypeNoName(ADoubleBaseTypeNoName node) {
        defaultIn(node);
    }

    public void outADoubleBaseTypeNoName(ADoubleBaseTypeNoName node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseADoubleBaseTypeNoName(ADoubleBaseTypeNoName node) {
        inADoubleBaseTypeNoName(node);
        if (node.getDouble() != null) {
            node.getDouble().apply(this);
        }
        outADoubleBaseTypeNoName(node);
    }

    public void inANullBaseTypeNoName(ANullBaseTypeNoName node) {
        defaultIn(node);
    }

    public void outANullBaseTypeNoName(ANullBaseTypeNoName node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseANullBaseTypeNoName(ANullBaseTypeNoName node) {
        inANullBaseTypeNoName(node);
        if (node.getNullType() != null) {
            node.getNullType().apply(this);
        }
        outANullBaseTypeNoName(node);
    }

    public void inABooleanBaseType(ABooleanBaseType node) {
        defaultIn(node);
    }

    public void outABooleanBaseType(ABooleanBaseType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseABooleanBaseType(ABooleanBaseType node) {
        inABooleanBaseType(node);
        if (node.getBoolean() != null) {
            node.getBoolean().apply(this);
        }
        outABooleanBaseType(node);
    }

    public void inAByteBaseType(AByteBaseType node) {
        defaultIn(node);
    }

    public void outAByteBaseType(AByteBaseType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAByteBaseType(AByteBaseType node) {
        inAByteBaseType(node);
        if (node.getByte() != null) {
            node.getByte().apply(this);
        }
        outAByteBaseType(node);
    }

    public void inACharBaseType(ACharBaseType node) {
        defaultIn(node);
    }

    public void outACharBaseType(ACharBaseType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseACharBaseType(ACharBaseType node) {
        inACharBaseType(node);
        if (node.getChar() != null) {
            node.getChar().apply(this);
        }
        outACharBaseType(node);
    }

    public void inAShortBaseType(AShortBaseType node) {
        defaultIn(node);
    }

    public void outAShortBaseType(AShortBaseType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAShortBaseType(AShortBaseType node) {
        inAShortBaseType(node);
        if (node.getShort() != null) {
            node.getShort().apply(this);
        }
        outAShortBaseType(node);
    }

    public void inAIntBaseType(AIntBaseType node) {
        defaultIn(node);
    }

    public void outAIntBaseType(AIntBaseType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAIntBaseType(AIntBaseType node) {
        inAIntBaseType(node);
        if (node.getInt() != null) {
            node.getInt().apply(this);
        }
        outAIntBaseType(node);
    }

    public void inALongBaseType(ALongBaseType node) {
        defaultIn(node);
    }

    public void outALongBaseType(ALongBaseType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseALongBaseType(ALongBaseType node) {
        inALongBaseType(node);
        if (node.getLong() != null) {
            node.getLong().apply(this);
        }
        outALongBaseType(node);
    }

    public void inAFloatBaseType(AFloatBaseType node) {
        defaultIn(node);
    }

    public void outAFloatBaseType(AFloatBaseType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAFloatBaseType(AFloatBaseType node) {
        inAFloatBaseType(node);
        if (node.getFloat() != null) {
            node.getFloat().apply(this);
        }
        outAFloatBaseType(node);
    }

    public void inADoubleBaseType(ADoubleBaseType node) {
        defaultIn(node);
    }

    public void outADoubleBaseType(ADoubleBaseType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseADoubleBaseType(ADoubleBaseType node) {
        inADoubleBaseType(node);
        if (node.getDouble() != null) {
            node.getDouble().apply(this);
        }
        outADoubleBaseType(node);
    }

    public void inANullBaseType(ANullBaseType node) {
        defaultIn(node);
    }

    public void outANullBaseType(ANullBaseType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseANullBaseType(ANullBaseType node) {
        inANullBaseType(node);
        if (node.getNullType() != null) {
            node.getNullType().apply(this);
        }
        outANullBaseType(node);
    }

    public void inAClassNameBaseType(AClassNameBaseType node) {
        defaultIn(node);
    }

    public void outAClassNameBaseType(AClassNameBaseType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAClassNameBaseType(AClassNameBaseType node) {
        inAClassNameBaseType(node);
        if (node.getClassName() != null) {
            node.getClassName().apply(this);
        }
        outAClassNameBaseType(node);
    }

    public void inABaseNonvoidType(ABaseNonvoidType node) {
        defaultIn(node);
    }

    public void outABaseNonvoidType(ABaseNonvoidType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseABaseNonvoidType(ABaseNonvoidType node) {
        inABaseNonvoidType(node);
        if (node.getBaseTypeNoName() != null) {
            node.getBaseTypeNoName().apply(this);
        }
        List<PArrayBrackets> copy = new ArrayList<>(node.getArrayBrackets());
        for (PArrayBrackets e : copy) {
            e.apply(this);
        }
        outABaseNonvoidType(node);
    }

    public void inAQuotedNonvoidType(AQuotedNonvoidType node) {
        defaultIn(node);
    }

    public void outAQuotedNonvoidType(AQuotedNonvoidType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAQuotedNonvoidType(AQuotedNonvoidType node) {
        inAQuotedNonvoidType(node);
        if (node.getQuotedName() != null) {
            node.getQuotedName().apply(this);
        }
        List<PArrayBrackets> copy = new ArrayList<>(node.getArrayBrackets());
        for (PArrayBrackets e : copy) {
            e.apply(this);
        }
        outAQuotedNonvoidType(node);
    }

    public void inAIdentNonvoidType(AIdentNonvoidType node) {
        defaultIn(node);
    }

    public void outAIdentNonvoidType(AIdentNonvoidType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAIdentNonvoidType(AIdentNonvoidType node) {
        inAIdentNonvoidType(node);
        if (node.getIdentifier() != null) {
            node.getIdentifier().apply(this);
        }
        List<PArrayBrackets> copy = new ArrayList<>(node.getArrayBrackets());
        for (PArrayBrackets e : copy) {
            e.apply(this);
        }
        outAIdentNonvoidType(node);
    }

    public void inAFullIdentNonvoidType(AFullIdentNonvoidType node) {
        defaultIn(node);
    }

    public void outAFullIdentNonvoidType(AFullIdentNonvoidType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAFullIdentNonvoidType(AFullIdentNonvoidType node) {
        inAFullIdentNonvoidType(node);
        if (node.getFullIdentifier() != null) {
            node.getFullIdentifier().apply(this);
        }
        List<PArrayBrackets> copy = new ArrayList<>(node.getArrayBrackets());
        for (PArrayBrackets e : copy) {
            e.apply(this);
        }
        outAFullIdentNonvoidType(node);
    }

    public void inAArrayBrackets(AArrayBrackets node) {
        defaultIn(node);
    }

    public void outAArrayBrackets(AArrayBrackets node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAArrayBrackets(AArrayBrackets node) {
        inAArrayBrackets(node);
        if (node.getLBracket() != null) {
            node.getLBracket().apply(this);
        }
        if (node.getRBracket() != null) {
            node.getRBracket().apply(this);
        }
        outAArrayBrackets(node);
    }

    public void inAEmptyMethodBody(AEmptyMethodBody node) {
        defaultIn(node);
    }

    public void outAEmptyMethodBody(AEmptyMethodBody node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAEmptyMethodBody(AEmptyMethodBody node) {
        inAEmptyMethodBody(node);
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        outAEmptyMethodBody(node);
    }

    public void inAFullMethodBody(AFullMethodBody node) {
        defaultIn(node);
    }

    public void outAFullMethodBody(AFullMethodBody node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAFullMethodBody(AFullMethodBody node) {
        inAFullMethodBody(node);
        if (node.getLBrace() != null) {
            node.getLBrace().apply(this);
        }
        List<PDeclaration> copy = new ArrayList<>(node.getDeclaration());
        for (PDeclaration e : copy) {
            e.apply(this);
        }
        List<PStatement> copy2 = new ArrayList<>(node.getStatement());
        for (PStatement e2 : copy2) {
            e2.apply(this);
        }
        List<PCatchClause> copy3 = new ArrayList<>(node.getCatchClause());
        for (PCatchClause e3 : copy3) {
            e3.apply(this);
        }
        if (node.getRBrace() != null) {
            node.getRBrace().apply(this);
        }
        outAFullMethodBody(node);
    }

    public void inADeclaration(ADeclaration node) {
        defaultIn(node);
    }

    public void outADeclaration(ADeclaration node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseADeclaration(ADeclaration node) {
        inADeclaration(node);
        if (node.getJimpleType() != null) {
            node.getJimpleType().apply(this);
        }
        if (node.getLocalNameList() != null) {
            node.getLocalNameList().apply(this);
        }
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        outADeclaration(node);
    }

    public void inAUnknownJimpleType(AUnknownJimpleType node) {
        defaultIn(node);
    }

    public void outAUnknownJimpleType(AUnknownJimpleType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAUnknownJimpleType(AUnknownJimpleType node) {
        inAUnknownJimpleType(node);
        if (node.getUnknown() != null) {
            node.getUnknown().apply(this);
        }
        outAUnknownJimpleType(node);
    }

    public void inANonvoidJimpleType(ANonvoidJimpleType node) {
        defaultIn(node);
    }

    public void outANonvoidJimpleType(ANonvoidJimpleType node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseANonvoidJimpleType(ANonvoidJimpleType node) {
        inANonvoidJimpleType(node);
        if (node.getNonvoidType() != null) {
            node.getNonvoidType().apply(this);
        }
        outANonvoidJimpleType(node);
    }

    public void inALocalName(ALocalName node) {
        defaultIn(node);
    }

    public void outALocalName(ALocalName node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseALocalName(ALocalName node) {
        inALocalName(node);
        if (node.getName() != null) {
            node.getName().apply(this);
        }
        outALocalName(node);
    }

    public void inASingleLocalNameList(ASingleLocalNameList node) {
        defaultIn(node);
    }

    public void outASingleLocalNameList(ASingleLocalNameList node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseASingleLocalNameList(ASingleLocalNameList node) {
        inASingleLocalNameList(node);
        if (node.getLocalName() != null) {
            node.getLocalName().apply(this);
        }
        outASingleLocalNameList(node);
    }

    public void inAMultiLocalNameList(AMultiLocalNameList node) {
        defaultIn(node);
    }

    public void outAMultiLocalNameList(AMultiLocalNameList node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAMultiLocalNameList(AMultiLocalNameList node) {
        inAMultiLocalNameList(node);
        if (node.getLocalName() != null) {
            node.getLocalName().apply(this);
        }
        if (node.getComma() != null) {
            node.getComma().apply(this);
        }
        if (node.getLocalNameList() != null) {
            node.getLocalNameList().apply(this);
        }
        outAMultiLocalNameList(node);
    }

    public void inALabelStatement(ALabelStatement node) {
        defaultIn(node);
    }

    public void outALabelStatement(ALabelStatement node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseALabelStatement(ALabelStatement node) {
        inALabelStatement(node);
        if (node.getLabelName() != null) {
            node.getLabelName().apply(this);
        }
        if (node.getColon() != null) {
            node.getColon().apply(this);
        }
        outALabelStatement(node);
    }

    public void inABreakpointStatement(ABreakpointStatement node) {
        defaultIn(node);
    }

    public void outABreakpointStatement(ABreakpointStatement node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseABreakpointStatement(ABreakpointStatement node) {
        inABreakpointStatement(node);
        if (node.getBreakpoint() != null) {
            node.getBreakpoint().apply(this);
        }
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        outABreakpointStatement(node);
    }

    public void inAEntermonitorStatement(AEntermonitorStatement node) {
        defaultIn(node);
    }

    public void outAEntermonitorStatement(AEntermonitorStatement node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAEntermonitorStatement(AEntermonitorStatement node) {
        inAEntermonitorStatement(node);
        if (node.getEntermonitor() != null) {
            node.getEntermonitor().apply(this);
        }
        if (node.getImmediate() != null) {
            node.getImmediate().apply(this);
        }
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        outAEntermonitorStatement(node);
    }

    public void inAExitmonitorStatement(AExitmonitorStatement node) {
        defaultIn(node);
    }

    public void outAExitmonitorStatement(AExitmonitorStatement node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAExitmonitorStatement(AExitmonitorStatement node) {
        inAExitmonitorStatement(node);
        if (node.getExitmonitor() != null) {
            node.getExitmonitor().apply(this);
        }
        if (node.getImmediate() != null) {
            node.getImmediate().apply(this);
        }
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        outAExitmonitorStatement(node);
    }

    public void inATableswitchStatement(ATableswitchStatement node) {
        defaultIn(node);
    }

    public void outATableswitchStatement(ATableswitchStatement node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseATableswitchStatement(ATableswitchStatement node) {
        inATableswitchStatement(node);
        if (node.getTableswitch() != null) {
            node.getTableswitch().apply(this);
        }
        if (node.getLParen() != null) {
            node.getLParen().apply(this);
        }
        if (node.getImmediate() != null) {
            node.getImmediate().apply(this);
        }
        if (node.getRParen() != null) {
            node.getRParen().apply(this);
        }
        if (node.getLBrace() != null) {
            node.getLBrace().apply(this);
        }
        List<PCaseStmt> copy = new ArrayList<>(node.getCaseStmt());
        for (PCaseStmt e : copy) {
            e.apply(this);
        }
        if (node.getRBrace() != null) {
            node.getRBrace().apply(this);
        }
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        outATableswitchStatement(node);
    }

    public void inALookupswitchStatement(ALookupswitchStatement node) {
        defaultIn(node);
    }

    public void outALookupswitchStatement(ALookupswitchStatement node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseALookupswitchStatement(ALookupswitchStatement node) {
        inALookupswitchStatement(node);
        if (node.getLookupswitch() != null) {
            node.getLookupswitch().apply(this);
        }
        if (node.getLParen() != null) {
            node.getLParen().apply(this);
        }
        if (node.getImmediate() != null) {
            node.getImmediate().apply(this);
        }
        if (node.getRParen() != null) {
            node.getRParen().apply(this);
        }
        if (node.getLBrace() != null) {
            node.getLBrace().apply(this);
        }
        List<PCaseStmt> copy = new ArrayList<>(node.getCaseStmt());
        for (PCaseStmt e : copy) {
            e.apply(this);
        }
        if (node.getRBrace() != null) {
            node.getRBrace().apply(this);
        }
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        outALookupswitchStatement(node);
    }

    public void inAIdentityStatement(AIdentityStatement node) {
        defaultIn(node);
    }

    public void outAIdentityStatement(AIdentityStatement node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAIdentityStatement(AIdentityStatement node) {
        inAIdentityStatement(node);
        if (node.getLocalName() != null) {
            node.getLocalName().apply(this);
        }
        if (node.getColonEquals() != null) {
            node.getColonEquals().apply(this);
        }
        if (node.getAtIdentifier() != null) {
            node.getAtIdentifier().apply(this);
        }
        if (node.getType() != null) {
            node.getType().apply(this);
        }
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        outAIdentityStatement(node);
    }

    public void inAIdentityNoTypeStatement(AIdentityNoTypeStatement node) {
        defaultIn(node);
    }

    public void outAIdentityNoTypeStatement(AIdentityNoTypeStatement node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAIdentityNoTypeStatement(AIdentityNoTypeStatement node) {
        inAIdentityNoTypeStatement(node);
        if (node.getLocalName() != null) {
            node.getLocalName().apply(this);
        }
        if (node.getColonEquals() != null) {
            node.getColonEquals().apply(this);
        }
        if (node.getAtIdentifier() != null) {
            node.getAtIdentifier().apply(this);
        }
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        outAIdentityNoTypeStatement(node);
    }

    public void inAAssignStatement(AAssignStatement node) {
        defaultIn(node);
    }

    public void outAAssignStatement(AAssignStatement node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAAssignStatement(AAssignStatement node) {
        inAAssignStatement(node);
        if (node.getVariable() != null) {
            node.getVariable().apply(this);
        }
        if (node.getEquals() != null) {
            node.getEquals().apply(this);
        }
        if (node.getExpression() != null) {
            node.getExpression().apply(this);
        }
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        outAAssignStatement(node);
    }

    public void inAIfStatement(AIfStatement node) {
        defaultIn(node);
    }

    public void outAIfStatement(AIfStatement node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAIfStatement(AIfStatement node) {
        inAIfStatement(node);
        if (node.getIf() != null) {
            node.getIf().apply(this);
        }
        if (node.getBoolExpr() != null) {
            node.getBoolExpr().apply(this);
        }
        if (node.getGotoStmt() != null) {
            node.getGotoStmt().apply(this);
        }
        outAIfStatement(node);
    }

    public void inAGotoStatement(AGotoStatement node) {
        defaultIn(node);
    }

    public void outAGotoStatement(AGotoStatement node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAGotoStatement(AGotoStatement node) {
        inAGotoStatement(node);
        if (node.getGotoStmt() != null) {
            node.getGotoStmt().apply(this);
        }
        outAGotoStatement(node);
    }

    public void inANopStatement(ANopStatement node) {
        defaultIn(node);
    }

    public void outANopStatement(ANopStatement node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseANopStatement(ANopStatement node) {
        inANopStatement(node);
        if (node.getNop() != null) {
            node.getNop().apply(this);
        }
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        outANopStatement(node);
    }

    public void inARetStatement(ARetStatement node) {
        defaultIn(node);
    }

    public void outARetStatement(ARetStatement node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseARetStatement(ARetStatement node) {
        inARetStatement(node);
        if (node.getRet() != null) {
            node.getRet().apply(this);
        }
        if (node.getImmediate() != null) {
            node.getImmediate().apply(this);
        }
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        outARetStatement(node);
    }

    public void inAReturnStatement(AReturnStatement node) {
        defaultIn(node);
    }

    public void outAReturnStatement(AReturnStatement node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAReturnStatement(AReturnStatement node) {
        inAReturnStatement(node);
        if (node.getReturn() != null) {
            node.getReturn().apply(this);
        }
        if (node.getImmediate() != null) {
            node.getImmediate().apply(this);
        }
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        outAReturnStatement(node);
    }

    public void inAThrowStatement(AThrowStatement node) {
        defaultIn(node);
    }

    public void outAThrowStatement(AThrowStatement node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAThrowStatement(AThrowStatement node) {
        inAThrowStatement(node);
        if (node.getThrow() != null) {
            node.getThrow().apply(this);
        }
        if (node.getImmediate() != null) {
            node.getImmediate().apply(this);
        }
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        outAThrowStatement(node);
    }

    public void inAInvokeStatement(AInvokeStatement node) {
        defaultIn(node);
    }

    public void outAInvokeStatement(AInvokeStatement node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAInvokeStatement(AInvokeStatement node) {
        inAInvokeStatement(node);
        if (node.getInvokeExpr() != null) {
            node.getInvokeExpr().apply(this);
        }
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        outAInvokeStatement(node);
    }

    public void inALabelName(ALabelName node) {
        defaultIn(node);
    }

    public void outALabelName(ALabelName node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseALabelName(ALabelName node) {
        inALabelName(node);
        if (node.getIdentifier() != null) {
            node.getIdentifier().apply(this);
        }
        outALabelName(node);
    }

    public void inACaseStmt(ACaseStmt node) {
        defaultIn(node);
    }

    public void outACaseStmt(ACaseStmt node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseACaseStmt(ACaseStmt node) {
        inACaseStmt(node);
        if (node.getCaseLabel() != null) {
            node.getCaseLabel().apply(this);
        }
        if (node.getColon() != null) {
            node.getColon().apply(this);
        }
        if (node.getGotoStmt() != null) {
            node.getGotoStmt().apply(this);
        }
        outACaseStmt(node);
    }

    public void inAConstantCaseLabel(AConstantCaseLabel node) {
        defaultIn(node);
    }

    public void outAConstantCaseLabel(AConstantCaseLabel node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAConstantCaseLabel(AConstantCaseLabel node) {
        inAConstantCaseLabel(node);
        if (node.getCase() != null) {
            node.getCase().apply(this);
        }
        if (node.getMinus() != null) {
            node.getMinus().apply(this);
        }
        if (node.getIntegerConstant() != null) {
            node.getIntegerConstant().apply(this);
        }
        outAConstantCaseLabel(node);
    }

    public void inADefaultCaseLabel(ADefaultCaseLabel node) {
        defaultIn(node);
    }

    public void outADefaultCaseLabel(ADefaultCaseLabel node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseADefaultCaseLabel(ADefaultCaseLabel node) {
        inADefaultCaseLabel(node);
        if (node.getDefault() != null) {
            node.getDefault().apply(this);
        }
        outADefaultCaseLabel(node);
    }

    public void inAGotoStmt(AGotoStmt node) {
        defaultIn(node);
    }

    public void outAGotoStmt(AGotoStmt node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAGotoStmt(AGotoStmt node) {
        inAGotoStmt(node);
        if (node.getGoto() != null) {
            node.getGoto().apply(this);
        }
        if (node.getLabelName() != null) {
            node.getLabelName().apply(this);
        }
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        outAGotoStmt(node);
    }

    public void inACatchClause(ACatchClause node) {
        defaultIn(node);
    }

    public void outACatchClause(ACatchClause node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseACatchClause(ACatchClause node) {
        inACatchClause(node);
        if (node.getCatch() != null) {
            node.getCatch().apply(this);
        }
        if (node.getName() != null) {
            node.getName().apply(this);
        }
        if (node.getFrom() != null) {
            node.getFrom().apply(this);
        }
        if (node.getFromLabel() != null) {
            node.getFromLabel().apply(this);
        }
        if (node.getTo() != null) {
            node.getTo().apply(this);
        }
        if (node.getToLabel() != null) {
            node.getToLabel().apply(this);
        }
        if (node.getWith() != null) {
            node.getWith().apply(this);
        }
        if (node.getWithLabel() != null) {
            node.getWithLabel().apply(this);
        }
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        outACatchClause(node);
    }

    public void inANewExpression(ANewExpression node) {
        defaultIn(node);
    }

    public void outANewExpression(ANewExpression node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseANewExpression(ANewExpression node) {
        inANewExpression(node);
        if (node.getNewExpr() != null) {
            node.getNewExpr().apply(this);
        }
        outANewExpression(node);
    }

    public void inACastExpression(ACastExpression node) {
        defaultIn(node);
    }

    public void outACastExpression(ACastExpression node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseACastExpression(ACastExpression node) {
        inACastExpression(node);
        if (node.getLParen() != null) {
            node.getLParen().apply(this);
        }
        if (node.getNonvoidType() != null) {
            node.getNonvoidType().apply(this);
        }
        if (node.getRParen() != null) {
            node.getRParen().apply(this);
        }
        if (node.getImmediate() != null) {
            node.getImmediate().apply(this);
        }
        outACastExpression(node);
    }

    public void inAInstanceofExpression(AInstanceofExpression node) {
        defaultIn(node);
    }

    public void outAInstanceofExpression(AInstanceofExpression node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAInstanceofExpression(AInstanceofExpression node) {
        inAInstanceofExpression(node);
        if (node.getImmediate() != null) {
            node.getImmediate().apply(this);
        }
        if (node.getInstanceof() != null) {
            node.getInstanceof().apply(this);
        }
        if (node.getNonvoidType() != null) {
            node.getNonvoidType().apply(this);
        }
        outAInstanceofExpression(node);
    }

    public void inAInvokeExpression(AInvokeExpression node) {
        defaultIn(node);
    }

    public void outAInvokeExpression(AInvokeExpression node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAInvokeExpression(AInvokeExpression node) {
        inAInvokeExpression(node);
        if (node.getInvokeExpr() != null) {
            node.getInvokeExpr().apply(this);
        }
        outAInvokeExpression(node);
    }

    public void inAReferenceExpression(AReferenceExpression node) {
        defaultIn(node);
    }

    public void outAReferenceExpression(AReferenceExpression node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAReferenceExpression(AReferenceExpression node) {
        inAReferenceExpression(node);
        if (node.getReference() != null) {
            node.getReference().apply(this);
        }
        outAReferenceExpression(node);
    }

    public void inABinopExpression(ABinopExpression node) {
        defaultIn(node);
    }

    public void outABinopExpression(ABinopExpression node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseABinopExpression(ABinopExpression node) {
        inABinopExpression(node);
        if (node.getBinopExpr() != null) {
            node.getBinopExpr().apply(this);
        }
        outABinopExpression(node);
    }

    public void inAUnopExpression(AUnopExpression node) {
        defaultIn(node);
    }

    public void outAUnopExpression(AUnopExpression node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAUnopExpression(AUnopExpression node) {
        inAUnopExpression(node);
        if (node.getUnopExpr() != null) {
            node.getUnopExpr().apply(this);
        }
        outAUnopExpression(node);
    }

    public void inAImmediateExpression(AImmediateExpression node) {
        defaultIn(node);
    }

    public void outAImmediateExpression(AImmediateExpression node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAImmediateExpression(AImmediateExpression node) {
        inAImmediateExpression(node);
        if (node.getImmediate() != null) {
            node.getImmediate().apply(this);
        }
        outAImmediateExpression(node);
    }

    public void inASimpleNewExpr(ASimpleNewExpr node) {
        defaultIn(node);
    }

    public void outASimpleNewExpr(ASimpleNewExpr node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseASimpleNewExpr(ASimpleNewExpr node) {
        inASimpleNewExpr(node);
        if (node.getNew() != null) {
            node.getNew().apply(this);
        }
        if (node.getBaseType() != null) {
            node.getBaseType().apply(this);
        }
        outASimpleNewExpr(node);
    }

    public void inAArrayNewExpr(AArrayNewExpr node) {
        defaultIn(node);
    }

    public void outAArrayNewExpr(AArrayNewExpr node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAArrayNewExpr(AArrayNewExpr node) {
        inAArrayNewExpr(node);
        if (node.getNewarray() != null) {
            node.getNewarray().apply(this);
        }
        if (node.getLParen() != null) {
            node.getLParen().apply(this);
        }
        if (node.getNonvoidType() != null) {
            node.getNonvoidType().apply(this);
        }
        if (node.getRParen() != null) {
            node.getRParen().apply(this);
        }
        if (node.getFixedArrayDescriptor() != null) {
            node.getFixedArrayDescriptor().apply(this);
        }
        outAArrayNewExpr(node);
    }

    public void inAMultiNewExpr(AMultiNewExpr node) {
        defaultIn(node);
    }

    public void outAMultiNewExpr(AMultiNewExpr node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAMultiNewExpr(AMultiNewExpr node) {
        inAMultiNewExpr(node);
        if (node.getNewmultiarray() != null) {
            node.getNewmultiarray().apply(this);
        }
        if (node.getLParen() != null) {
            node.getLParen().apply(this);
        }
        if (node.getBaseType() != null) {
            node.getBaseType().apply(this);
        }
        if (node.getRParen() != null) {
            node.getRParen().apply(this);
        }
        List<PArrayDescriptor> copy = new ArrayList<>(node.getArrayDescriptor());
        for (PArrayDescriptor e : copy) {
            e.apply(this);
        }
        outAMultiNewExpr(node);
    }

    public void inAArrayDescriptor(AArrayDescriptor node) {
        defaultIn(node);
    }

    public void outAArrayDescriptor(AArrayDescriptor node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAArrayDescriptor(AArrayDescriptor node) {
        inAArrayDescriptor(node);
        if (node.getLBracket() != null) {
            node.getLBracket().apply(this);
        }
        if (node.getImmediate() != null) {
            node.getImmediate().apply(this);
        }
        if (node.getRBracket() != null) {
            node.getRBracket().apply(this);
        }
        outAArrayDescriptor(node);
    }

    public void inAReferenceVariable(AReferenceVariable node) {
        defaultIn(node);
    }

    public void outAReferenceVariable(AReferenceVariable node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAReferenceVariable(AReferenceVariable node) {
        inAReferenceVariable(node);
        if (node.getReference() != null) {
            node.getReference().apply(this);
        }
        outAReferenceVariable(node);
    }

    public void inALocalVariable(ALocalVariable node) {
        defaultIn(node);
    }

    public void outALocalVariable(ALocalVariable node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseALocalVariable(ALocalVariable node) {
        inALocalVariable(node);
        if (node.getLocalName() != null) {
            node.getLocalName().apply(this);
        }
        outALocalVariable(node);
    }

    public void inABinopBoolExpr(ABinopBoolExpr node) {
        defaultIn(node);
    }

    public void outABinopBoolExpr(ABinopBoolExpr node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseABinopBoolExpr(ABinopBoolExpr node) {
        inABinopBoolExpr(node);
        if (node.getBinopExpr() != null) {
            node.getBinopExpr().apply(this);
        }
        outABinopBoolExpr(node);
    }

    public void inAUnopBoolExpr(AUnopBoolExpr node) {
        defaultIn(node);
    }

    public void outAUnopBoolExpr(AUnopBoolExpr node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAUnopBoolExpr(AUnopBoolExpr node) {
        inAUnopBoolExpr(node);
        if (node.getUnopExpr() != null) {
            node.getUnopExpr().apply(this);
        }
        outAUnopBoolExpr(node);
    }

    public void inANonstaticInvokeExpr(ANonstaticInvokeExpr node) {
        defaultIn(node);
    }

    public void outANonstaticInvokeExpr(ANonstaticInvokeExpr node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseANonstaticInvokeExpr(ANonstaticInvokeExpr node) {
        inANonstaticInvokeExpr(node);
        if (node.getNonstaticInvoke() != null) {
            node.getNonstaticInvoke().apply(this);
        }
        if (node.getLocalName() != null) {
            node.getLocalName().apply(this);
        }
        if (node.getDot() != null) {
            node.getDot().apply(this);
        }
        if (node.getMethodSignature() != null) {
            node.getMethodSignature().apply(this);
        }
        if (node.getLParen() != null) {
            node.getLParen().apply(this);
        }
        if (node.getArgList() != null) {
            node.getArgList().apply(this);
        }
        if (node.getRParen() != null) {
            node.getRParen().apply(this);
        }
        outANonstaticInvokeExpr(node);
    }

    public void inAStaticInvokeExpr(AStaticInvokeExpr node) {
        defaultIn(node);
    }

    public void outAStaticInvokeExpr(AStaticInvokeExpr node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAStaticInvokeExpr(AStaticInvokeExpr node) {
        inAStaticInvokeExpr(node);
        if (node.getStaticinvoke() != null) {
            node.getStaticinvoke().apply(this);
        }
        if (node.getMethodSignature() != null) {
            node.getMethodSignature().apply(this);
        }
        if (node.getLParen() != null) {
            node.getLParen().apply(this);
        }
        if (node.getArgList() != null) {
            node.getArgList().apply(this);
        }
        if (node.getRParen() != null) {
            node.getRParen().apply(this);
        }
        outAStaticInvokeExpr(node);
    }

    public void inADynamicInvokeExpr(ADynamicInvokeExpr node) {
        defaultIn(node);
    }

    public void outADynamicInvokeExpr(ADynamicInvokeExpr node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseADynamicInvokeExpr(ADynamicInvokeExpr node) {
        inADynamicInvokeExpr(node);
        if (node.getDynamicinvoke() != null) {
            node.getDynamicinvoke().apply(this);
        }
        if (node.getStringConstant() != null) {
            node.getStringConstant().apply(this);
        }
        if (node.getDynmethod() != null) {
            node.getDynmethod().apply(this);
        }
        if (node.getFirstl() != null) {
            node.getFirstl().apply(this);
        }
        if (node.getDynargs() != null) {
            node.getDynargs().apply(this);
        }
        if (node.getFirstr() != null) {
            node.getFirstr().apply(this);
        }
        if (node.getBsm() != null) {
            node.getBsm().apply(this);
        }
        if (node.getLParen() != null) {
            node.getLParen().apply(this);
        }
        if (node.getStaticargs() != null) {
            node.getStaticargs().apply(this);
        }
        if (node.getRParen() != null) {
            node.getRParen().apply(this);
        }
        outADynamicInvokeExpr(node);
    }

    public void inABinopExpr(ABinopExpr node) {
        defaultIn(node);
    }

    public void outABinopExpr(ABinopExpr node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseABinopExpr(ABinopExpr node) {
        inABinopExpr(node);
        if (node.getLeft() != null) {
            node.getLeft().apply(this);
        }
        if (node.getBinop() != null) {
            node.getBinop().apply(this);
        }
        if (node.getRight() != null) {
            node.getRight().apply(this);
        }
        outABinopExpr(node);
    }

    public void inAUnopExpr(AUnopExpr node) {
        defaultIn(node);
    }

    public void outAUnopExpr(AUnopExpr node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAUnopExpr(AUnopExpr node) {
        inAUnopExpr(node);
        if (node.getUnop() != null) {
            node.getUnop().apply(this);
        }
        if (node.getImmediate() != null) {
            node.getImmediate().apply(this);
        }
        outAUnopExpr(node);
    }

    public void inASpecialNonstaticInvoke(ASpecialNonstaticInvoke node) {
        defaultIn(node);
    }

    public void outASpecialNonstaticInvoke(ASpecialNonstaticInvoke node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseASpecialNonstaticInvoke(ASpecialNonstaticInvoke node) {
        inASpecialNonstaticInvoke(node);
        if (node.getSpecialinvoke() != null) {
            node.getSpecialinvoke().apply(this);
        }
        outASpecialNonstaticInvoke(node);
    }

    public void inAVirtualNonstaticInvoke(AVirtualNonstaticInvoke node) {
        defaultIn(node);
    }

    public void outAVirtualNonstaticInvoke(AVirtualNonstaticInvoke node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAVirtualNonstaticInvoke(AVirtualNonstaticInvoke node) {
        inAVirtualNonstaticInvoke(node);
        if (node.getVirtualinvoke() != null) {
            node.getVirtualinvoke().apply(this);
        }
        outAVirtualNonstaticInvoke(node);
    }

    public void inAInterfaceNonstaticInvoke(AInterfaceNonstaticInvoke node) {
        defaultIn(node);
    }

    public void outAInterfaceNonstaticInvoke(AInterfaceNonstaticInvoke node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAInterfaceNonstaticInvoke(AInterfaceNonstaticInvoke node) {
        inAInterfaceNonstaticInvoke(node);
        if (node.getInterfaceinvoke() != null) {
            node.getInterfaceinvoke().apply(this);
        }
        outAInterfaceNonstaticInvoke(node);
    }

    public void inAUnnamedMethodSignature(AUnnamedMethodSignature node) {
        defaultIn(node);
    }

    public void outAUnnamedMethodSignature(AUnnamedMethodSignature node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAUnnamedMethodSignature(AUnnamedMethodSignature node) {
        inAUnnamedMethodSignature(node);
        if (node.getCmplt() != null) {
            node.getCmplt().apply(this);
        }
        if (node.getType() != null) {
            node.getType().apply(this);
        }
        if (node.getLParen() != null) {
            node.getLParen().apply(this);
        }
        if (node.getParameterList() != null) {
            node.getParameterList().apply(this);
        }
        if (node.getRParen() != null) {
            node.getRParen().apply(this);
        }
        if (node.getCmpgt() != null) {
            node.getCmpgt().apply(this);
        }
        outAUnnamedMethodSignature(node);
    }

    public void inAMethodSignature(AMethodSignature node) {
        defaultIn(node);
    }

    public void outAMethodSignature(AMethodSignature node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAMethodSignature(AMethodSignature node) {
        inAMethodSignature(node);
        if (node.getCmplt() != null) {
            node.getCmplt().apply(this);
        }
        if (node.getClassName() != null) {
            node.getClassName().apply(this);
        }
        if (node.getFirst() != null) {
            node.getFirst().apply(this);
        }
        if (node.getType() != null) {
            node.getType().apply(this);
        }
        if (node.getMethodName() != null) {
            node.getMethodName().apply(this);
        }
        if (node.getLParen() != null) {
            node.getLParen().apply(this);
        }
        if (node.getParameterList() != null) {
            node.getParameterList().apply(this);
        }
        if (node.getRParen() != null) {
            node.getRParen().apply(this);
        }
        if (node.getCmpgt() != null) {
            node.getCmpgt().apply(this);
        }
        outAMethodSignature(node);
    }

    public void inAArrayReference(AArrayReference node) {
        defaultIn(node);
    }

    public void outAArrayReference(AArrayReference node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAArrayReference(AArrayReference node) {
        inAArrayReference(node);
        if (node.getArrayRef() != null) {
            node.getArrayRef().apply(this);
        }
        outAArrayReference(node);
    }

    public void inAFieldReference(AFieldReference node) {
        defaultIn(node);
    }

    public void outAFieldReference(AFieldReference node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAFieldReference(AFieldReference node) {
        inAFieldReference(node);
        if (node.getFieldRef() != null) {
            node.getFieldRef().apply(this);
        }
        outAFieldReference(node);
    }

    public void inAIdentArrayRef(AIdentArrayRef node) {
        defaultIn(node);
    }

    public void outAIdentArrayRef(AIdentArrayRef node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAIdentArrayRef(AIdentArrayRef node) {
        inAIdentArrayRef(node);
        if (node.getIdentifier() != null) {
            node.getIdentifier().apply(this);
        }
        if (node.getFixedArrayDescriptor() != null) {
            node.getFixedArrayDescriptor().apply(this);
        }
        outAIdentArrayRef(node);
    }

    public void inAQuotedArrayRef(AQuotedArrayRef node) {
        defaultIn(node);
    }

    public void outAQuotedArrayRef(AQuotedArrayRef node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAQuotedArrayRef(AQuotedArrayRef node) {
        inAQuotedArrayRef(node);
        if (node.getQuotedName() != null) {
            node.getQuotedName().apply(this);
        }
        if (node.getFixedArrayDescriptor() != null) {
            node.getFixedArrayDescriptor().apply(this);
        }
        outAQuotedArrayRef(node);
    }

    public void inALocalFieldRef(ALocalFieldRef node) {
        defaultIn(node);
    }

    public void outALocalFieldRef(ALocalFieldRef node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseALocalFieldRef(ALocalFieldRef node) {
        inALocalFieldRef(node);
        if (node.getLocalName() != null) {
            node.getLocalName().apply(this);
        }
        if (node.getDot() != null) {
            node.getDot().apply(this);
        }
        if (node.getFieldSignature() != null) {
            node.getFieldSignature().apply(this);
        }
        outALocalFieldRef(node);
    }

    public void inASigFieldRef(ASigFieldRef node) {
        defaultIn(node);
    }

    public void outASigFieldRef(ASigFieldRef node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseASigFieldRef(ASigFieldRef node) {
        inASigFieldRef(node);
        if (node.getFieldSignature() != null) {
            node.getFieldSignature().apply(this);
        }
        outASigFieldRef(node);
    }

    public void inAFieldSignature(AFieldSignature node) {
        defaultIn(node);
    }

    public void outAFieldSignature(AFieldSignature node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAFieldSignature(AFieldSignature node) {
        inAFieldSignature(node);
        if (node.getCmplt() != null) {
            node.getCmplt().apply(this);
        }
        if (node.getClassName() != null) {
            node.getClassName().apply(this);
        }
        if (node.getFirst() != null) {
            node.getFirst().apply(this);
        }
        if (node.getType() != null) {
            node.getType().apply(this);
        }
        if (node.getFieldName() != null) {
            node.getFieldName().apply(this);
        }
        if (node.getCmpgt() != null) {
            node.getCmpgt().apply(this);
        }
        outAFieldSignature(node);
    }

    public void inAFixedArrayDescriptor(AFixedArrayDescriptor node) {
        defaultIn(node);
    }

    public void outAFixedArrayDescriptor(AFixedArrayDescriptor node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAFixedArrayDescriptor(AFixedArrayDescriptor node) {
        inAFixedArrayDescriptor(node);
        if (node.getLBracket() != null) {
            node.getLBracket().apply(this);
        }
        if (node.getImmediate() != null) {
            node.getImmediate().apply(this);
        }
        if (node.getRBracket() != null) {
            node.getRBracket().apply(this);
        }
        outAFixedArrayDescriptor(node);
    }

    public void inASingleArgList(ASingleArgList node) {
        defaultIn(node);
    }

    public void outASingleArgList(ASingleArgList node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseASingleArgList(ASingleArgList node) {
        inASingleArgList(node);
        if (node.getImmediate() != null) {
            node.getImmediate().apply(this);
        }
        outASingleArgList(node);
    }

    public void inAMultiArgList(AMultiArgList node) {
        defaultIn(node);
    }

    public void outAMultiArgList(AMultiArgList node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAMultiArgList(AMultiArgList node) {
        inAMultiArgList(node);
        if (node.getImmediate() != null) {
            node.getImmediate().apply(this);
        }
        if (node.getComma() != null) {
            node.getComma().apply(this);
        }
        if (node.getArgList() != null) {
            node.getArgList().apply(this);
        }
        outAMultiArgList(node);
    }

    public void inALocalImmediate(ALocalImmediate node) {
        defaultIn(node);
    }

    public void outALocalImmediate(ALocalImmediate node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseALocalImmediate(ALocalImmediate node) {
        inALocalImmediate(node);
        if (node.getLocalName() != null) {
            node.getLocalName().apply(this);
        }
        outALocalImmediate(node);
    }

    public void inAConstantImmediate(AConstantImmediate node) {
        defaultIn(node);
    }

    public void outAConstantImmediate(AConstantImmediate node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAConstantImmediate(AConstantImmediate node) {
        inAConstantImmediate(node);
        if (node.getConstant() != null) {
            node.getConstant().apply(this);
        }
        outAConstantImmediate(node);
    }

    public void inAIntegerConstant(AIntegerConstant node) {
        defaultIn(node);
    }

    public void outAIntegerConstant(AIntegerConstant node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAIntegerConstant(AIntegerConstant node) {
        inAIntegerConstant(node);
        if (node.getMinus() != null) {
            node.getMinus().apply(this);
        }
        if (node.getIntegerConstant() != null) {
            node.getIntegerConstant().apply(this);
        }
        outAIntegerConstant(node);
    }

    public void inAFloatConstant(AFloatConstant node) {
        defaultIn(node);
    }

    public void outAFloatConstant(AFloatConstant node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAFloatConstant(AFloatConstant node) {
        inAFloatConstant(node);
        if (node.getMinus() != null) {
            node.getMinus().apply(this);
        }
        if (node.getFloatConstant() != null) {
            node.getFloatConstant().apply(this);
        }
        outAFloatConstant(node);
    }

    public void inAStringConstant(AStringConstant node) {
        defaultIn(node);
    }

    public void outAStringConstant(AStringConstant node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAStringConstant(AStringConstant node) {
        inAStringConstant(node);
        if (node.getStringConstant() != null) {
            node.getStringConstant().apply(this);
        }
        outAStringConstant(node);
    }

    public void inAClzzConstant(AClzzConstant node) {
        defaultIn(node);
    }

    public void outAClzzConstant(AClzzConstant node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAClzzConstant(AClzzConstant node) {
        inAClzzConstant(node);
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        if (node.getStringConstant() != null) {
            node.getStringConstant().apply(this);
        }
        outAClzzConstant(node);
    }

    public void inANullConstant(ANullConstant node) {
        defaultIn(node);
    }

    public void outANullConstant(ANullConstant node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseANullConstant(ANullConstant node) {
        inANullConstant(node);
        if (node.getNull() != null) {
            node.getNull().apply(this);
        }
        outANullConstant(node);
    }

    public void inAAndBinop(AAndBinop node) {
        defaultIn(node);
    }

    public void outAAndBinop(AAndBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAAndBinop(AAndBinop node) {
        inAAndBinop(node);
        if (node.getAnd() != null) {
            node.getAnd().apply(this);
        }
        outAAndBinop(node);
    }

    public void inAOrBinop(AOrBinop node) {
        defaultIn(node);
    }

    public void outAOrBinop(AOrBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAOrBinop(AOrBinop node) {
        inAOrBinop(node);
        if (node.getOr() != null) {
            node.getOr().apply(this);
        }
        outAOrBinop(node);
    }

    public void inAXorBinop(AXorBinop node) {
        defaultIn(node);
    }

    public void outAXorBinop(AXorBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAXorBinop(AXorBinop node) {
        inAXorBinop(node);
        if (node.getXor() != null) {
            node.getXor().apply(this);
        }
        outAXorBinop(node);
    }

    public void inAModBinop(AModBinop node) {
        defaultIn(node);
    }

    public void outAModBinop(AModBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAModBinop(AModBinop node) {
        inAModBinop(node);
        if (node.getMod() != null) {
            node.getMod().apply(this);
        }
        outAModBinop(node);
    }

    public void inACmpBinop(ACmpBinop node) {
        defaultIn(node);
    }

    public void outACmpBinop(ACmpBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseACmpBinop(ACmpBinop node) {
        inACmpBinop(node);
        if (node.getCmp() != null) {
            node.getCmp().apply(this);
        }
        outACmpBinop(node);
    }

    public void inACmpgBinop(ACmpgBinop node) {
        defaultIn(node);
    }

    public void outACmpgBinop(ACmpgBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseACmpgBinop(ACmpgBinop node) {
        inACmpgBinop(node);
        if (node.getCmpg() != null) {
            node.getCmpg().apply(this);
        }
        outACmpgBinop(node);
    }

    public void inACmplBinop(ACmplBinop node) {
        defaultIn(node);
    }

    public void outACmplBinop(ACmplBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseACmplBinop(ACmplBinop node) {
        inACmplBinop(node);
        if (node.getCmpl() != null) {
            node.getCmpl().apply(this);
        }
        outACmplBinop(node);
    }

    public void inACmpeqBinop(ACmpeqBinop node) {
        defaultIn(node);
    }

    public void outACmpeqBinop(ACmpeqBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseACmpeqBinop(ACmpeqBinop node) {
        inACmpeqBinop(node);
        if (node.getCmpeq() != null) {
            node.getCmpeq().apply(this);
        }
        outACmpeqBinop(node);
    }

    public void inACmpneBinop(ACmpneBinop node) {
        defaultIn(node);
    }

    public void outACmpneBinop(ACmpneBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseACmpneBinop(ACmpneBinop node) {
        inACmpneBinop(node);
        if (node.getCmpne() != null) {
            node.getCmpne().apply(this);
        }
        outACmpneBinop(node);
    }

    public void inACmpgtBinop(ACmpgtBinop node) {
        defaultIn(node);
    }

    public void outACmpgtBinop(ACmpgtBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseACmpgtBinop(ACmpgtBinop node) {
        inACmpgtBinop(node);
        if (node.getCmpgt() != null) {
            node.getCmpgt().apply(this);
        }
        outACmpgtBinop(node);
    }

    public void inACmpgeBinop(ACmpgeBinop node) {
        defaultIn(node);
    }

    public void outACmpgeBinop(ACmpgeBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseACmpgeBinop(ACmpgeBinop node) {
        inACmpgeBinop(node);
        if (node.getCmpge() != null) {
            node.getCmpge().apply(this);
        }
        outACmpgeBinop(node);
    }

    public void inACmpltBinop(ACmpltBinop node) {
        defaultIn(node);
    }

    public void outACmpltBinop(ACmpltBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseACmpltBinop(ACmpltBinop node) {
        inACmpltBinop(node);
        if (node.getCmplt() != null) {
            node.getCmplt().apply(this);
        }
        outACmpltBinop(node);
    }

    public void inACmpleBinop(ACmpleBinop node) {
        defaultIn(node);
    }

    public void outACmpleBinop(ACmpleBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseACmpleBinop(ACmpleBinop node) {
        inACmpleBinop(node);
        if (node.getCmple() != null) {
            node.getCmple().apply(this);
        }
        outACmpleBinop(node);
    }

    public void inAShlBinop(AShlBinop node) {
        defaultIn(node);
    }

    public void outAShlBinop(AShlBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAShlBinop(AShlBinop node) {
        inAShlBinop(node);
        if (node.getShl() != null) {
            node.getShl().apply(this);
        }
        outAShlBinop(node);
    }

    public void inAShrBinop(AShrBinop node) {
        defaultIn(node);
    }

    public void outAShrBinop(AShrBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAShrBinop(AShrBinop node) {
        inAShrBinop(node);
        if (node.getShr() != null) {
            node.getShr().apply(this);
        }
        outAShrBinop(node);
    }

    public void inAUshrBinop(AUshrBinop node) {
        defaultIn(node);
    }

    public void outAUshrBinop(AUshrBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAUshrBinop(AUshrBinop node) {
        inAUshrBinop(node);
        if (node.getUshr() != null) {
            node.getUshr().apply(this);
        }
        outAUshrBinop(node);
    }

    public void inAPlusBinop(APlusBinop node) {
        defaultIn(node);
    }

    public void outAPlusBinop(APlusBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAPlusBinop(APlusBinop node) {
        inAPlusBinop(node);
        if (node.getPlus() != null) {
            node.getPlus().apply(this);
        }
        outAPlusBinop(node);
    }

    public void inAMinusBinop(AMinusBinop node) {
        defaultIn(node);
    }

    public void outAMinusBinop(AMinusBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAMinusBinop(AMinusBinop node) {
        inAMinusBinop(node);
        if (node.getMinus() != null) {
            node.getMinus().apply(this);
        }
        outAMinusBinop(node);
    }

    public void inAMultBinop(AMultBinop node) {
        defaultIn(node);
    }

    public void outAMultBinop(AMultBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAMultBinop(AMultBinop node) {
        inAMultBinop(node);
        if (node.getMult() != null) {
            node.getMult().apply(this);
        }
        outAMultBinop(node);
    }

    public void inADivBinop(ADivBinop node) {
        defaultIn(node);
    }

    public void outADivBinop(ADivBinop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseADivBinop(ADivBinop node) {
        inADivBinop(node);
        if (node.getDiv() != null) {
            node.getDiv().apply(this);
        }
        outADivBinop(node);
    }

    public void inALengthofUnop(ALengthofUnop node) {
        defaultIn(node);
    }

    public void outALengthofUnop(ALengthofUnop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseALengthofUnop(ALengthofUnop node) {
        inALengthofUnop(node);
        if (node.getLengthof() != null) {
            node.getLengthof().apply(this);
        }
        outALengthofUnop(node);
    }

    public void inANegUnop(ANegUnop node) {
        defaultIn(node);
    }

    public void outANegUnop(ANegUnop node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseANegUnop(ANegUnop node) {
        inANegUnop(node);
        if (node.getNeg() != null) {
            node.getNeg().apply(this);
        }
        outANegUnop(node);
    }

    public void inAQuotedClassName(AQuotedClassName node) {
        defaultIn(node);
    }

    public void outAQuotedClassName(AQuotedClassName node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAQuotedClassName(AQuotedClassName node) {
        inAQuotedClassName(node);
        if (node.getQuotedName() != null) {
            node.getQuotedName().apply(this);
        }
        outAQuotedClassName(node);
    }

    public void inAIdentClassName(AIdentClassName node) {
        defaultIn(node);
    }

    public void outAIdentClassName(AIdentClassName node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAIdentClassName(AIdentClassName node) {
        inAIdentClassName(node);
        if (node.getIdentifier() != null) {
            node.getIdentifier().apply(this);
        }
        outAIdentClassName(node);
    }

    public void inAFullIdentClassName(AFullIdentClassName node) {
        defaultIn(node);
    }

    public void outAFullIdentClassName(AFullIdentClassName node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAFullIdentClassName(AFullIdentClassName node) {
        inAFullIdentClassName(node);
        if (node.getFullIdentifier() != null) {
            node.getFullIdentifier().apply(this);
        }
        outAFullIdentClassName(node);
    }

    public void inAQuotedName(AQuotedName node) {
        defaultIn(node);
    }

    public void outAQuotedName(AQuotedName node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAQuotedName(AQuotedName node) {
        inAQuotedName(node);
        if (node.getQuotedName() != null) {
            node.getQuotedName().apply(this);
        }
        outAQuotedName(node);
    }

    public void inAIdentName(AIdentName node) {
        defaultIn(node);
    }

    public void outAIdentName(AIdentName node) {
        defaultOut(node);
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAIdentName(AIdentName node) {
        inAIdentName(node);
        if (node.getIdentifier() != null) {
            node.getIdentifier().apply(this);
        }
        outAIdentName(node);
    }
}
