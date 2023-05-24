package soot.jimple.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.Immediate;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.NullType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.SootResolver;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPatchingChain;
import soot.UnknownType;
import soot.Value;
import soot.VoidType;
import soot.jimple.BinopExpr;
import soot.jimple.ClassConstant;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.ReturnStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StringConstant;
import soot.jimple.UnopExpr;
import soot.jimple.parser.analysis.DepthFirstAdapter;
import soot.jimple.parser.node.AAbstractModifier;
import soot.jimple.parser.node.AAndBinop;
import soot.jimple.parser.node.AAnnotationModifier;
import soot.jimple.parser.node.AArrayDescriptor;
import soot.jimple.parser.node.AArrayNewExpr;
import soot.jimple.parser.node.AArrayReference;
import soot.jimple.parser.node.AAssignStatement;
import soot.jimple.parser.node.ABaseNonvoidType;
import soot.jimple.parser.node.ABinopBoolExpr;
import soot.jimple.parser.node.ABinopExpr;
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
import soot.jimple.parser.node.ADeclaration;
import soot.jimple.parser.node.ADivBinop;
import soot.jimple.parser.node.ADoubleBaseType;
import soot.jimple.parser.node.ADoubleBaseTypeNoName;
import soot.jimple.parser.node.ADynamicInvokeExpr;
import soot.jimple.parser.node.AEntermonitorStatement;
import soot.jimple.parser.node.AEnumModifier;
import soot.jimple.parser.node.AExitmonitorStatement;
import soot.jimple.parser.node.AFieldMember;
import soot.jimple.parser.node.AFieldSignature;
import soot.jimple.parser.node.AFile;
import soot.jimple.parser.node.AFinalModifier;
import soot.jimple.parser.node.AFloatBaseType;
import soot.jimple.parser.node.AFloatBaseTypeNoName;
import soot.jimple.parser.node.AFloatConstant;
import soot.jimple.parser.node.AFullIdentNonvoidType;
import soot.jimple.parser.node.AFullMethodBody;
import soot.jimple.parser.node.AGotoStatement;
import soot.jimple.parser.node.AIdentNonvoidType;
import soot.jimple.parser.node.AIdentityNoTypeStatement;
import soot.jimple.parser.node.AIdentityStatement;
import soot.jimple.parser.node.AIfStatement;
import soot.jimple.parser.node.AInstanceofExpression;
import soot.jimple.parser.node.AIntBaseType;
import soot.jimple.parser.node.AIntBaseTypeNoName;
import soot.jimple.parser.node.AIntegerConstant;
import soot.jimple.parser.node.AInterfaceFileType;
import soot.jimple.parser.node.AInterfaceNonstaticInvoke;
import soot.jimple.parser.node.AInvokeStatement;
import soot.jimple.parser.node.ALabelStatement;
import soot.jimple.parser.node.ALengthofUnop;
import soot.jimple.parser.node.ALocalFieldRef;
import soot.jimple.parser.node.ALocalImmediate;
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
import soot.jimple.parser.node.AMultiNewExpr;
import soot.jimple.parser.node.AMultiParameterList;
import soot.jimple.parser.node.ANativeModifier;
import soot.jimple.parser.node.ANegUnop;
import soot.jimple.parser.node.ANonstaticInvokeExpr;
import soot.jimple.parser.node.ANopStatement;
import soot.jimple.parser.node.ANovoidType;
import soot.jimple.parser.node.ANullBaseType;
import soot.jimple.parser.node.ANullBaseTypeNoName;
import soot.jimple.parser.node.ANullConstant;
import soot.jimple.parser.node.AOrBinop;
import soot.jimple.parser.node.APlusBinop;
import soot.jimple.parser.node.APrivateModifier;
import soot.jimple.parser.node.AProtectedModifier;
import soot.jimple.parser.node.APublicModifier;
import soot.jimple.parser.node.AQuotedNonvoidType;
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
import soot.jimple.parser.node.AUnopExpr;
import soot.jimple.parser.node.AUnopExpression;
import soot.jimple.parser.node.AUshrBinop;
import soot.jimple.parser.node.AVirtualNonstaticInvoke;
import soot.jimple.parser.node.AVoidType;
import soot.jimple.parser.node.AVolatileModifier;
import soot.jimple.parser.node.AXorBinop;
import soot.jimple.parser.node.Node;
import soot.jimple.parser.node.PModifier;
import soot.jimple.parser.node.Start;
import soot.jimple.parser.node.TAtIdentifier;
import soot.jimple.parser.node.TFloatConstant;
import soot.jimple.parser.node.TFullIdentifier;
import soot.jimple.parser.node.TIdentifier;
import soot.jimple.parser.node.TIntegerConstant;
import soot.jimple.parser.node.TQuotedName;
import soot.jimple.parser.node.TStringConstant;
import soot.jimple.parser.node.Token;
import soot.util.StringTools;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/Walker.class */
public class Walker extends DepthFirstAdapter {
    private static final Logger logger = LoggerFactory.getLogger(Walker.class);
    boolean debug;
    LinkedList mProductions;
    SootClass mSootClass;
    Map<String, Local> mLocals;
    Value mValue;
    Map<Object, Unit> mLabelToStmtMap;
    Map<String, List> mLabelToPatchList;
    protected final SootResolver mResolver;

    public Walker(SootResolver resolver) {
        this.debug = false;
        this.mProductions = new LinkedList();
        this.mSootClass = null;
        this.mLocals = null;
        this.mValue = IntConstant.v(1);
        this.mResolver = resolver;
        if (this.debug) {
            this.mProductions = new LinkedList() { // from class: soot.jimple.parser.Walker.1
                @Override // java.util.LinkedList, java.util.Deque
                public Object removeLast() {
                    Object o = super.removeLast();
                    if (Walker.this.debug) {
                        Walker.logger.debug("popped: " + o);
                    }
                    return o;
                }
            };
        }
    }

    public Walker(SootClass sc, SootResolver resolver) {
        this.debug = false;
        this.mProductions = new LinkedList();
        this.mSootClass = null;
        this.mLocals = null;
        this.mValue = IntConstant.v(1);
        this.mSootClass = sc;
        this.mResolver = resolver;
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outStart(Start node) {
        SootClass sootClass = (SootClass) this.mProductions.removeLast();
    }

    public SootClass getSootClass() {
        if (this.mSootClass == null) {
            throw new RuntimeException("did not parse class yet....");
        }
        return this.mSootClass;
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void inAFile(AFile node) {
        if (this.debug) {
            logger.debug("reading class " + node.getClassName());
        }
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter, soot.jimple.parser.analysis.AnalysisAdapter, soot.jimple.parser.analysis.Analysis
    public void caseAFile(AFile node) {
        inAFile(node);
        Object[] temp = node.getModifier().toArray();
        for (Object element : temp) {
            ((PModifier) element).apply(this);
        }
        if (node.getFileType() != null) {
            node.getFileType().apply(this);
        }
        if (node.getClassName() != null) {
            node.getClassName().apply(this);
        }
        String className = (String) this.mProductions.removeLast();
        if (this.mSootClass == null) {
            this.mSootClass = new SootClass(className);
            this.mSootClass.setResolvingLevel(3);
        } else if (!this.mSootClass.getName().equals(className)) {
            throw new RuntimeException("Invalid SootClass for this JimpleAST. The SootClass provided is of type: >" + this.mSootClass.getName() + "< whereas this parse tree is for type: >" + className + "<");
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

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAFile(AFile node) {
        List<String> implementsList = null;
        String superClass = null;
        if (node.getImplementsClause() != null) {
            implementsList = (List) this.mProductions.removeLast();
        }
        if (node.getExtendsClause() != null) {
            superClass = (String) this.mProductions.removeLast();
        }
        String classType = (String) this.mProductions.removeLast();
        node.getModifier().size();
        int modifierFlags = processModifiers(node.getModifier());
        if (classType.equals("interface")) {
            modifierFlags |= 512;
        }
        this.mSootClass.setModifiers(modifierFlags);
        if (superClass != null) {
            this.mSootClass.setSuperclass(this.mResolver.makeClassRef(superClass));
        }
        if (implementsList != null) {
            for (String str : implementsList) {
                SootClass interfaceClass = this.mResolver.makeClassRef(str);
                this.mSootClass.addInterface(interfaceClass);
            }
        }
        this.mProductions.addLast(this.mSootClass);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAFieldMember(AFieldMember node) {
        String name = (String) this.mProductions.removeLast();
        Type type = (Type) this.mProductions.removeLast();
        int modifier = processModifiers(node.getModifier());
        SootField f = Scene.v().makeSootField(name, type, modifier);
        this.mSootClass.addField(f);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAMethodMember(AMethodMember node) {
        List parameterList;
        SootMethod method;
        List<SootClass> throwsClause = null;
        JimpleBody methodBody = null;
        if (node.getMethodBody() instanceof AFullMethodBody) {
            methodBody = (JimpleBody) this.mProductions.removeLast();
        }
        if (node.getThrowsClause() != null) {
            throwsClause = (List) this.mProductions.removeLast();
        }
        if (node.getParameterList() != null) {
            parameterList = (List) this.mProductions.removeLast();
        } else {
            parameterList = new ArrayList();
        }
        Object o = this.mProductions.removeLast();
        String name = (String) o;
        Type type = (Type) this.mProductions.removeLast();
        int modifier = processModifiers(node.getModifier());
        if (throwsClause != null) {
            method = Scene.v().makeSootMethod(name, parameterList, type, modifier, throwsClause);
        } else {
            method = Scene.v().makeSootMethod(name, parameterList, type, modifier);
        }
        this.mSootClass.addMethod(method);
        if (method.isConcrete()) {
            methodBody.setMethod(method);
            method.setActiveBody(methodBody);
        } else if (node.getMethodBody() instanceof AFullMethodBody) {
            throw new RuntimeException("Impossible: !concrete => ! instanceof");
        }
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAVoidType(AVoidType node) {
        this.mProductions.addLast(VoidType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outABaseNonvoidType(ABaseNonvoidType node) {
        Type t = (Type) this.mProductions.removeLast();
        int dim = node.getArrayBrackets().size();
        if (dim > 0) {
            t = ArrayType.v(t, dim);
        }
        this.mProductions.addLast(t);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v13, types: [soot.ArrayType] */
    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAQuotedNonvoidType(AQuotedNonvoidType node) {
        String typeName = (String) this.mProductions.removeLast();
        RefType v = RefType.v(typeName);
        int dim = node.getArrayBrackets().size();
        if (dim > 0) {
            v = ArrayType.v(v, dim);
        }
        this.mProductions.addLast(v);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v13, types: [soot.ArrayType] */
    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAIdentNonvoidType(AIdentNonvoidType node) {
        String typeName = (String) this.mProductions.removeLast();
        RefType v = RefType.v(typeName);
        int dim = node.getArrayBrackets().size();
        if (dim > 0) {
            v = ArrayType.v(v, dim);
        }
        this.mProductions.addLast(v);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v13, types: [soot.ArrayType] */
    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAFullIdentNonvoidType(AFullIdentNonvoidType node) {
        String typeName = (String) this.mProductions.removeLast();
        RefType v = RefType.v(typeName);
        int dim = node.getArrayBrackets().size();
        if (dim > 0) {
            v = ArrayType.v(v, dim);
        }
        this.mProductions.addLast(v);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outABooleanBaseTypeNoName(ABooleanBaseTypeNoName node) {
        this.mProductions.addLast(BooleanType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAByteBaseTypeNoName(AByteBaseTypeNoName node) {
        this.mProductions.addLast(ByteType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outACharBaseTypeNoName(ACharBaseTypeNoName node) {
        this.mProductions.addLast(CharType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAShortBaseTypeNoName(AShortBaseTypeNoName node) {
        this.mProductions.addLast(ShortType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAIntBaseTypeNoName(AIntBaseTypeNoName node) {
        this.mProductions.addLast(IntType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outALongBaseTypeNoName(ALongBaseTypeNoName node) {
        this.mProductions.addLast(LongType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAFloatBaseTypeNoName(AFloatBaseTypeNoName node) {
        this.mProductions.addLast(FloatType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outADoubleBaseTypeNoName(ADoubleBaseTypeNoName node) {
        this.mProductions.addLast(DoubleType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outANullBaseTypeNoName(ANullBaseTypeNoName node) {
        this.mProductions.addLast(NullType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outABooleanBaseType(ABooleanBaseType node) {
        this.mProductions.addLast(BooleanType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAByteBaseType(AByteBaseType node) {
        this.mProductions.addLast(ByteType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outACharBaseType(ACharBaseType node) {
        this.mProductions.addLast(CharType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAShortBaseType(AShortBaseType node) {
        this.mProductions.addLast(ShortType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAIntBaseType(AIntBaseType node) {
        this.mProductions.addLast(IntType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outALongBaseType(ALongBaseType node) {
        this.mProductions.addLast(LongType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAFloatBaseType(AFloatBaseType node) {
        this.mProductions.addLast(FloatType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outADoubleBaseType(ADoubleBaseType node) {
        this.mProductions.addLast(DoubleType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outANullBaseType(ANullBaseType node) {
        this.mProductions.addLast(NullType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAClassNameBaseType(AClassNameBaseType node) {
        String type = (String) this.mProductions.removeLast();
        if (type.equals("int")) {
            throw new RuntimeException();
        }
        this.mProductions.addLast(RefType.v(type));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void inAFullMethodBody(AFullMethodBody node) {
        this.mLocals = new HashMap();
        this.mLabelToStmtMap = new HashMap();
        this.mLabelToPatchList = new HashMap();
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAFullMethodBody(AFullMethodBody node) {
        JimpleBody jBody = Jimple.v().newBody();
        if (node.getCatchClause() != null) {
            int size = node.getCatchClause().size();
            for (int i = 0; i < size; i++) {
                jBody.getTraps().addFirst((Trap) this.mProductions.removeLast());
            }
        }
        if (node.getStatement() != null) {
            int size2 = node.getStatement().size();
            Unit lastStmt = null;
            for (int i2 = 0; i2 < size2; i2++) {
                Object o = this.mProductions.removeLast();
                if (o instanceof Unit) {
                    jBody.getUnits().addFirst((UnitPatchingChain) ((Unit) o));
                    lastStmt = (Unit) o;
                } else if (o instanceof String) {
                    if (lastStmt == null) {
                        throw new RuntimeException("impossible");
                    }
                    this.mLabelToStmtMap.put(o, lastStmt);
                } else {
                    throw new RuntimeException("impossible");
                }
            }
        }
        if (node.getDeclaration() != null) {
            int size3 = node.getDeclaration().size();
            for (int i3 = 0; i3 < size3; i3++) {
                List<Local> localList = (List) this.mProductions.removeLast();
                jBody.getLocals().addAll(localList);
            }
        }
        for (String label : this.mLabelToPatchList.keySet()) {
            Unit target = this.mLabelToStmtMap.get(label);
            for (UnitBox box : this.mLabelToPatchList.get(label)) {
                box.setUnit(target);
            }
        }
        this.mProductions.addLast(jBody);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outANovoidType(ANovoidType node) {
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outASingleParameterList(ASingleParameterList node) {
        ArrayList arrayList = new ArrayList();
        arrayList.add((Type) this.mProductions.removeLast());
        this.mProductions.addLast(arrayList);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAMultiParameterList(AMultiParameterList node) {
        List<Type> l = (List) this.mProductions.removeLast();
        l.add(0, (Type) this.mProductions.removeLast());
        this.mProductions.addLast(l);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outASingleArgList(ASingleArgList node) {
        ArrayList arrayList = new ArrayList();
        arrayList.add((Value) this.mProductions.removeLast());
        this.mProductions.addLast(arrayList);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAMultiArgList(AMultiArgList node) {
        List<Value> l = (List) this.mProductions.removeLast();
        l.add(0, (Value) this.mProductions.removeLast());
        this.mProductions.addLast(l);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAClassNameSingleClassNameList(AClassNameSingleClassNameList node) {
        ArrayList arrayList = new ArrayList();
        arrayList.add((String) this.mProductions.removeLast());
        this.mProductions.addLast(arrayList);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAClassNameMultiClassNameList(AClassNameMultiClassNameList node) {
        List<String> l = (List) this.mProductions.removeLast();
        l.add(0, (String) this.mProductions.removeLast());
        this.mProductions.addLast(l);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAClassFileType(AClassFileType node) {
        this.mProductions.addLast("class");
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAInterfaceFileType(AInterfaceFileType node) {
        this.mProductions.addLast("interface");
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outACatchClause(ACatchClause node) {
        UnitBox withUnit = Jimple.v().newStmtBox(null);
        addBoxToPatch((String) this.mProductions.removeLast(), withUnit);
        UnitBox toUnit = Jimple.v().newStmtBox(null);
        addBoxToPatch((String) this.mProductions.removeLast(), toUnit);
        UnitBox fromUnit = Jimple.v().newStmtBox(null);
        addBoxToPatch((String) this.mProductions.removeLast(), fromUnit);
        String exceptionName = (String) this.mProductions.removeLast();
        Trap trap = Jimple.v().newTrap(this.mResolver.makeClassRef(exceptionName), fromUnit, toUnit, withUnit);
        this.mProductions.addLast(trap);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outADeclaration(ADeclaration node) {
        List<String> localNameList = (List) this.mProductions.removeLast();
        Type type = (Type) this.mProductions.removeLast();
        ArrayList arrayList = new ArrayList();
        for (String str : localNameList) {
            Local l = Jimple.v().newLocal(str, type);
            this.mLocals.put(l.getName(), l);
            arrayList.add(l);
        }
        this.mProductions.addLast(arrayList);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAUnknownJimpleType(AUnknownJimpleType node) {
        this.mProductions.addLast(UnknownType.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outASingleLocalNameList(ASingleLocalNameList node) {
        ArrayList arrayList = new ArrayList();
        arrayList.add((String) this.mProductions.removeLast());
        this.mProductions.addLast(arrayList);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAMultiLocalNameList(AMultiLocalNameList node) {
        List<String> l = (List) this.mProductions.removeLast();
        l.add(0, (String) this.mProductions.removeLast());
        this.mProductions.addLast(l);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outALabelStatement(ALabelStatement node) {
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outABreakpointStatement(ABreakpointStatement node) {
        this.mProductions.addLast(Jimple.v().newBreakpointStmt());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAEntermonitorStatement(AEntermonitorStatement node) {
        Value op = (Value) this.mProductions.removeLast();
        this.mProductions.addLast(Jimple.v().newEnterMonitorStmt(op));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAExitmonitorStatement(AExitmonitorStatement node) {
        Value op = (Value) this.mProductions.removeLast();
        this.mProductions.addLast(Jimple.v().newExitMonitorStmt(op));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outACaseStmt(ACaseStmt node) {
        String labelName = (String) this.mProductions.removeLast();
        UnitBox box = Jimple.v().newStmtBox(null);
        addBoxToPatch(labelName, box);
        Value labelValue = null;
        if (node.getCaseLabel() instanceof AConstantCaseLabel) {
            labelValue = (Value) this.mProductions.removeLast();
        }
        if (labelValue == null) {
            this.mProductions.addLast(box);
            return;
        }
        Object[] valueTargetPair = {labelValue, box};
        this.mProductions.addLast(valueTargetPair);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outATableswitchStatement(ATableswitchStatement node) {
        List<UnitBox> targets = new ArrayList<>();
        UnitBox defaultTarget = null;
        int lowIndex = 0;
        int highIndex = 0;
        if (node.getCaseStmt() != null) {
            int size = node.getCaseStmt().size();
            for (int i = 0; i < size; i++) {
                Object valueTargetPair = this.mProductions.removeLast();
                if (valueTargetPair instanceof UnitBox) {
                    if (defaultTarget != null) {
                        throw new RuntimeException("error: can't ;have more than 1 default stmt");
                    }
                    defaultTarget = (UnitBox) valueTargetPair;
                } else {
                    Object[] pair = (Object[]) valueTargetPair;
                    if ((i == 0 && defaultTarget == null) || (i == 1 && defaultTarget != null)) {
                        highIndex = ((IntConstant) pair[0]).value;
                    }
                    if (i == size - 1) {
                        lowIndex = ((IntConstant) pair[0]).value;
                    }
                    targets.add(0, (UnitBox) pair[1]);
                }
            }
            Value key = (Value) this.mProductions.removeLast();
            this.mProductions.addLast(Jimple.v().newTableSwitchStmt(key, lowIndex, highIndex, targets, defaultTarget));
            return;
        }
        throw new RuntimeException("error: switch stmt has no case stmts");
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outALookupswitchStatement(ALookupswitchStatement node) {
        List<IntConstant> lookupValues = new ArrayList<>();
        List<UnitBox> targets = new ArrayList<>();
        UnitBox defaultTarget = null;
        if (node.getCaseStmt() != null) {
            int size = node.getCaseStmt().size();
            for (int i = 0; i < size; i++) {
                Object valueTargetPair = this.mProductions.removeLast();
                if (valueTargetPair instanceof UnitBox) {
                    if (defaultTarget != null) {
                        throw new RuntimeException("error: can't ;have more than 1 default stmt");
                    }
                    defaultTarget = (UnitBox) valueTargetPair;
                } else {
                    Object[] pair = (Object[]) valueTargetPair;
                    lookupValues.add(0, (IntConstant) pair[0]);
                    targets.add(0, (UnitBox) pair[1]);
                }
            }
            Value key = (Value) this.mProductions.removeLast();
            this.mProductions.addLast(Jimple.v().newLookupSwitchStmt(key, lookupValues, targets, defaultTarget));
            return;
        }
        throw new RuntimeException("error: switch stmt has no case stmts");
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAIdentityStatement(AIdentityStatement node) {
        Value ref;
        Type identityRefType = (Type) this.mProductions.removeLast();
        String atClause = (String) this.mProductions.removeLast();
        Value local = this.mLocals.get(this.mProductions.removeLast());
        if (atClause.startsWith("@this")) {
            ref = Jimple.v().newThisRef((RefType) identityRefType);
        } else if (atClause.startsWith("@parameter")) {
            int index = Integer.parseInt(atClause.substring(10, atClause.length() - 1));
            ref = Jimple.v().newParameterRef(identityRefType, index);
        } else {
            throw new RuntimeException("shouldn't @caughtexception be handled by outAIdentityNoTypeStatement: got" + atClause);
        }
        this.mProductions.addLast(Jimple.v().newIdentityStmt(local, ref));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAIdentityNoTypeStatement(AIdentityNoTypeStatement node) {
        this.mProductions.removeLast();
        Value local = this.mLocals.get(this.mProductions.removeLast());
        this.mProductions.addLast(Jimple.v().newIdentityStmt(local, Jimple.v().newCaughtExceptionRef()));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAAssignStatement(AAssignStatement node) {
        Object removeLast = this.mProductions.removeLast();
        Value rvalue = (Value) removeLast;
        Value variable = (Value) this.mProductions.removeLast();
        this.mProductions.addLast(Jimple.v().newAssignStmt(variable, rvalue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAIfStatement(AIfStatement node) {
        String targetLabel = (String) this.mProductions.removeLast();
        Value condition = (Value) this.mProductions.removeLast();
        UnitBox box = Jimple.v().newStmtBox(null);
        IfStmt newIfStmt = Jimple.v().newIfStmt(condition, box);
        addBoxToPatch(targetLabel, box);
        this.mProductions.addLast(newIfStmt);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAReturnStatement(AReturnStatement node) {
        ReturnStmt newReturnVoidStmt;
        if (node.getImmediate() != null) {
            Immediate v = (Immediate) this.mProductions.removeLast();
            newReturnVoidStmt = Jimple.v().newReturnStmt(v);
        } else {
            newReturnVoidStmt = Jimple.v().newReturnVoidStmt();
        }
        this.mProductions.addLast(newReturnVoidStmt);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAGotoStatement(AGotoStatement node) {
        String targetLabel = (String) this.mProductions.removeLast();
        UnitBox box = Jimple.v().newStmtBox(null);
        GotoStmt newGotoStmt = Jimple.v().newGotoStmt(box);
        addBoxToPatch(targetLabel, box);
        this.mProductions.addLast(newGotoStmt);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outANopStatement(ANopStatement node) {
        this.mProductions.addLast(Jimple.v().newNopStmt());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outARetStatement(ARetStatement node) {
        throw new RuntimeException("ret not yet implemented.");
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAThrowStatement(AThrowStatement node) {
        Value op = (Value) this.mProductions.removeLast();
        this.mProductions.addLast(Jimple.v().newThrowStmt(op));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAInvokeStatement(AInvokeStatement node) {
        Value op = (Value) this.mProductions.removeLast();
        this.mProductions.addLast(Jimple.v().newInvokeStmt(op));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAConstantCaseLabel(AConstantCaseLabel node) {
        String s = (String) this.mProductions.removeLast();
        int sign = 1;
        if (node.getMinus() != null) {
            sign = -1;
        }
        if (s.endsWith("L")) {
            this.mProductions.addLast(LongConstant.v(sign * Long.parseLong(s.substring(0, s.length() - 1))));
        } else if (s.equals("2147483648")) {
            this.mProductions.addLast(IntConstant.v(sign * Integer.MIN_VALUE));
        } else {
            this.mProductions.addLast(IntConstant.v(sign * Integer.parseInt(s)));
        }
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outALocalImmediate(ALocalImmediate node) {
        String local = (String) this.mProductions.removeLast();
        Local l = this.mLocals.get(local);
        if (l == null) {
            throw new RuntimeException("did not find local: " + local);
        }
        this.mProductions.addLast(l);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outANullConstant(ANullConstant node) {
        this.mProductions.addLast(NullConstant.v());
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAIntegerConstant(AIntegerConstant node) {
        String s = (String) this.mProductions.removeLast();
        StringBuffer buf = new StringBuffer();
        if (node.getMinus() != null) {
            buf.append('-');
        }
        buf.append(s);
        String s2 = buf.toString();
        if (s2.endsWith("L")) {
            this.mProductions.addLast(LongConstant.v(Long.parseLong(s2.substring(0, s2.length() - 1))));
        } else if (s2.equals("2147483648")) {
            this.mProductions.addLast(IntConstant.v(Integer.MIN_VALUE));
        } else {
            this.mProductions.addLast(IntConstant.v(Integer.parseInt(s2)));
        }
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAStringConstant(AStringConstant node) {
        String s = (String) this.mProductions.removeLast();
        this.mProductions.addLast(StringConstant.v(s));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAClzzConstant(AClzzConstant node) {
        String s = (String) this.mProductions.removeLast();
        this.mProductions.addLast(ClassConstant.v(s));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v32, types: [soot.jimple.DoubleConstant] */
    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAFloatConstant(AFloatConstant node) {
        FloatConstant v;
        String s = (String) this.mProductions.removeLast();
        boolean isDouble = true;
        float value = 0.0f;
        double dvalue = 0.0d;
        if (s.endsWith("f") || s.endsWith("F")) {
            isDouble = false;
        }
        if (s.charAt(0) == '#') {
            if (s.charAt(1) == '-') {
                if (isDouble) {
                    dvalue = Double.NEGATIVE_INFINITY;
                } else {
                    value = Float.NEGATIVE_INFINITY;
                }
            } else if (s.charAt(1) == 'I') {
                if (isDouble) {
                    dvalue = Double.POSITIVE_INFINITY;
                } else {
                    value = Float.POSITIVE_INFINITY;
                }
            } else if (isDouble) {
                dvalue = Double.NaN;
            } else {
                value = Float.NaN;
            }
        } else {
            StringBuffer buf = new StringBuffer();
            if (node.getMinus() != null) {
                buf.append('-');
            }
            buf.append(s);
            String s2 = buf.toString();
            if (isDouble) {
                dvalue = Double.parseDouble(s2);
            } else {
                value = Float.parseFloat(s2);
            }
        }
        if (isDouble) {
            v = DoubleConstant.v(dvalue);
        } else {
            v = FloatConstant.v(value);
        }
        this.mProductions.addLast(v);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outABinopExpr(ABinopExpr node) {
        Value right = (Value) this.mProductions.removeLast();
        BinopExpr expr = (BinopExpr) this.mProductions.removeLast();
        Value left = (Value) this.mProductions.removeLast();
        expr.setOp1(left);
        expr.setOp2(right);
        this.mProductions.addLast(expr);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outABinopBoolExpr(ABinopBoolExpr node) {
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAUnopExpression(AUnopExpression node) {
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAAndBinop(AAndBinop node) {
        this.mProductions.addLast(Jimple.v().newAndExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAOrBinop(AOrBinop node) {
        this.mProductions.addLast(Jimple.v().newOrExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAXorBinop(AXorBinop node) {
        this.mProductions.addLast(Jimple.v().newXorExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAModBinop(AModBinop node) {
        this.mProductions.addLast(Jimple.v().newRemExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outACmpBinop(ACmpBinop node) {
        this.mProductions.addLast(Jimple.v().newCmpExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outACmpgBinop(ACmpgBinop node) {
        this.mProductions.addLast(Jimple.v().newCmpgExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outACmplBinop(ACmplBinop node) {
        this.mProductions.addLast(Jimple.v().newCmplExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outACmpeqBinop(ACmpeqBinop node) {
        this.mProductions.addLast(Jimple.v().newEqExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outACmpneBinop(ACmpneBinop node) {
        this.mProductions.addLast(Jimple.v().newNeExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outACmpgtBinop(ACmpgtBinop node) {
        this.mProductions.addLast(Jimple.v().newGtExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outACmpgeBinop(ACmpgeBinop node) {
        this.mProductions.addLast(Jimple.v().newGeExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outACmpltBinop(ACmpltBinop node) {
        this.mProductions.addLast(Jimple.v().newLtExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outACmpleBinop(ACmpleBinop node) {
        this.mProductions.addLast(Jimple.v().newLeExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAShlBinop(AShlBinop node) {
        this.mProductions.addLast(Jimple.v().newShlExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAShrBinop(AShrBinop node) {
        this.mProductions.addLast(Jimple.v().newShrExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAUshrBinop(AUshrBinop node) {
        this.mProductions.addLast(Jimple.v().newUshrExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAPlusBinop(APlusBinop node) {
        this.mProductions.addLast(Jimple.v().newAddExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAMinusBinop(AMinusBinop node) {
        this.mProductions.addLast(Jimple.v().newSubExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAMultBinop(AMultBinop node) {
        this.mProductions.addLast(Jimple.v().newMulExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outADivBinop(ADivBinop node) {
        this.mProductions.addLast(Jimple.v().newDivExpr(this.mValue, this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAThrowsClause(AThrowsClause node) {
        List<String> l = (List) this.mProductions.removeLast();
        ArrayList arrayList = new ArrayList(l.size());
        for (String className : l) {
            arrayList.add(this.mResolver.makeClassRef(className));
        }
        this.mProductions.addLast(arrayList);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outALocalVariable(ALocalVariable node) {
        String local = (String) this.mProductions.removeLast();
        Local l = this.mLocals.get(local);
        if (l == null) {
            throw new RuntimeException("did not find local: " + local);
        }
        this.mProductions.addLast(l);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAArrayReference(AArrayReference node) {
        Value immediate = (Value) this.mProductions.removeLast();
        String identifier = (String) this.mProductions.removeLast();
        Local l = this.mLocals.get(identifier);
        if (l == null) {
            throw new RuntimeException("did not find local: " + identifier);
        }
        this.mProductions.addLast(Jimple.v().newArrayRef(l, immediate));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outALocalFieldRef(ALocalFieldRef node) {
        SootFieldRef field = (SootFieldRef) this.mProductions.removeLast();
        String local = (String) this.mProductions.removeLast();
        Local l = this.mLocals.get(local);
        if (l == null) {
            throw new RuntimeException("did not find local: " + local);
        }
        this.mProductions.addLast(Jimple.v().newInstanceFieldRef(l, field));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outASigFieldRef(ASigFieldRef node) {
        SootFieldRef field = (SootFieldRef) this.mProductions.removeLast();
        this.mProductions.addLast(Jimple.v().newStaticFieldRef(Scene.v().makeFieldRef(field.declaringClass(), field.name(), field.type(), true)));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAFieldSignature(AFieldSignature node) {
        String fieldName = (String) this.mProductions.removeLast();
        Type t = (Type) this.mProductions.removeLast();
        String className = (String) this.mProductions.removeLast();
        SootClass cl = this.mResolver.makeClassRef(className);
        SootFieldRef field = Scene.v().makeFieldRef(cl, fieldName, t, false);
        this.mProductions.addLast(field);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outACastExpression(ACastExpression node) {
        Value val = (Value) this.mProductions.removeLast();
        Type type = (Type) this.mProductions.removeLast();
        this.mProductions.addLast(Jimple.v().newCastExpr(val, type));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAInstanceofExpression(AInstanceofExpression node) {
        Type nonvoidType = (Type) this.mProductions.removeLast();
        Value immediate = (Value) this.mProductions.removeLast();
        this.mProductions.addLast(Jimple.v().newInstanceOfExpr(immediate, nonvoidType));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAUnopExpr(AUnopExpr node) {
        Value v = (Value) this.mProductions.removeLast();
        UnopExpr expr = (UnopExpr) this.mProductions.removeLast();
        expr.setOp(v);
        this.mProductions.addLast(expr);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outALengthofUnop(ALengthofUnop node) {
        this.mProductions.addLast(Jimple.v().newLengthExpr(this.mValue));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outANegUnop(ANegUnop node) {
        this.mProductions.addLast(Jimple.v().newNegExpr(this.mValue));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outANonstaticInvokeExpr(ANonstaticInvokeExpr node) {
        List args;
        SpecialInvokeExpr newInterfaceInvokeExpr;
        if (node.getArgList() != null) {
            args = (List) this.mProductions.removeLast();
        } else {
            args = new ArrayList();
        }
        SootMethodRef method = (SootMethodRef) this.mProductions.removeLast();
        String local = (String) this.mProductions.removeLast();
        Local l = this.mLocals.get(local);
        if (l == null) {
            throw new RuntimeException("did not find local: " + local);
        }
        Node invokeType = node.getNonstaticInvoke();
        if (invokeType instanceof ASpecialNonstaticInvoke) {
            newInterfaceInvokeExpr = Jimple.v().newSpecialInvokeExpr(l, method, args);
        } else if (invokeType instanceof AVirtualNonstaticInvoke) {
            newInterfaceInvokeExpr = Jimple.v().newVirtualInvokeExpr(l, method, args);
        } else if (this.debug && !(invokeType instanceof AInterfaceNonstaticInvoke)) {
            throw new RuntimeException("expected interface invoke.");
        } else {
            newInterfaceInvokeExpr = Jimple.v().newInterfaceInvokeExpr(l, method, args);
        }
        this.mProductions.addLast(newInterfaceInvokeExpr);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAStaticInvokeExpr(AStaticInvokeExpr node) {
        List args;
        if (node.getArgList() != null) {
            args = (List) this.mProductions.removeLast();
        } else {
            args = new ArrayList();
        }
        SootMethodRef method = (SootMethodRef) this.mProductions.removeLast();
        this.mProductions.addLast(Jimple.v().newStaticInvokeExpr(Scene.v().makeMethodRef(method.declaringClass(), method.name(), method.parameterTypes(), method.returnType(), true), args));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outADynamicInvokeExpr(ADynamicInvokeExpr node) {
        List<Value> bsmArgs;
        List<Value> dynArgs;
        if (node.getStaticargs() != null) {
            bsmArgs = (List) this.mProductions.removeLast();
        } else {
            bsmArgs = Collections.emptyList();
        }
        SootMethodRef bsmMethodRef = (SootMethodRef) this.mProductions.removeLast();
        if (node.getDynargs() != null) {
            dynArgs = (List) this.mProductions.removeLast();
        } else {
            dynArgs = Collections.emptyList();
        }
        SootMethodRef dynMethodRef = (SootMethodRef) this.mProductions.removeLast();
        this.mProductions.addLast(Jimple.v().newDynamicInvokeExpr(bsmMethodRef, bsmArgs, dynMethodRef, dynArgs));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAUnnamedMethodSignature(AUnnamedMethodSignature node) {
        List parameterList = new ArrayList();
        if (node.getParameterList() != null) {
            parameterList = (List) this.mProductions.removeLast();
        }
        Type type = (Type) this.mProductions.removeLast();
        String name = (String) this.mProductions.removeLast();
        SootClass sootClass = this.mResolver.makeClassRef(SootClass.INVOKEDYNAMIC_DUMMY_CLASS_NAME);
        SootMethodRef sootMethod = Scene.v().makeMethodRef(sootClass, name, parameterList, type, false);
        this.mProductions.addLast(sootMethod);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAMethodSignature(AMethodSignature node) {
        List parameterList = new ArrayList();
        if (node.getParameterList() != null) {
            parameterList = (List) this.mProductions.removeLast();
        }
        String methodName = (String) this.mProductions.removeLast();
        Type type = (Type) this.mProductions.removeLast();
        String className = (String) this.mProductions.removeLast();
        SootClass sootClass = this.mResolver.makeClassRef(className);
        SootMethodRef sootMethod = Scene.v().makeMethodRef(sootClass, methodName, parameterList, type, false);
        this.mProductions.addLast(sootMethod);
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outASimpleNewExpr(ASimpleNewExpr node) {
        this.mProductions.addLast(Jimple.v().newNewExpr((RefType) this.mProductions.removeLast()));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAArrayNewExpr(AArrayNewExpr node) {
        Value size = (Value) this.mProductions.removeLast();
        Type type = (Type) this.mProductions.removeLast();
        this.mProductions.addLast(Jimple.v().newNewArrayExpr(type, size));
    }

    @Override // soot.jimple.parser.analysis.DepthFirstAdapter
    public void outAMultiNewExpr(AMultiNewExpr node) {
        LinkedList arrayDesc = node.getArrayDescriptor();
        int descCnt = arrayDesc.size();
        List sizes = new LinkedList();
        Iterator it = arrayDesc.iterator();
        while (it.hasNext()) {
            AArrayDescriptor o = (AArrayDescriptor) it.next();
            if (o.getImmediate() == null) {
                break;
            }
            sizes.add(0, this.mProductions.removeLast());
        }
        Type type = (Type) this.mProductions.removeLast();
        ArrayType arrayType = ArrayType.v(type, descCnt);
        this.mProductions.addLast(Jimple.v().newNewMultiArrayExpr(arrayType, sizes));
    }

    @Override // soot.jimple.parser.analysis.AnalysisAdapter
    public void defaultCase(Node node) {
        if ((node instanceof TQuotedName) || (node instanceof TFullIdentifier) || (node instanceof TIdentifier) || (node instanceof TStringConstant) || (node instanceof TIntegerConstant) || (node instanceof TFloatConstant) || (node instanceof TAtIdentifier)) {
            if (this.debug) {
                logger.debug("Default case -pushing token:" + ((Token) node).getText());
            }
            String tokenString = ((Token) node).getText();
            if ((node instanceof TStringConstant) || (node instanceof TQuotedName)) {
                tokenString = tokenString.substring(1, tokenString.length() - 1);
            } else if (node instanceof TFullIdentifier) {
                Scene.v();
                tokenString = Scene.unescapeName(tokenString);
            }
            if ((node instanceof TIdentifier) || (node instanceof TFullIdentifier) || (node instanceof TQuotedName) || (node instanceof TStringConstant)) {
                try {
                    tokenString = StringTools.getUnEscapedStringOf(tokenString);
                } catch (RuntimeException e) {
                    logger.debug("Invalid escaped string: " + tokenString);
                }
            }
            this.mProductions.addLast(tokenString);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int processModifiers(List l) {
        int modifier = 0;
        for (Object t : l) {
            if (t instanceof AAbstractModifier) {
                modifier |= 1024;
            } else if (t instanceof AFinalModifier) {
                modifier |= 16;
            } else if (t instanceof ANativeModifier) {
                modifier |= 256;
            } else if (t instanceof APublicModifier) {
                modifier |= 1;
            } else if (t instanceof AProtectedModifier) {
                modifier |= 4;
            } else if (t instanceof APrivateModifier) {
                modifier |= 2;
            } else if (t instanceof AStaticModifier) {
                modifier |= 8;
            } else if (t instanceof ASynchronizedModifier) {
                modifier |= 32;
            } else if (t instanceof ATransientModifier) {
                modifier |= 128;
            } else if (t instanceof AVolatileModifier) {
                modifier |= 64;
            } else if (t instanceof AStrictfpModifier) {
                modifier |= 2048;
            } else if (t instanceof AEnumModifier) {
                modifier |= 16384;
            } else if (t instanceof AAnnotationModifier) {
                modifier |= 8192;
            } else {
                throw new RuntimeException("Impossible: modifier unknown - Have you added a new modifier and not updated this file?");
            }
        }
        return modifier;
    }

    private void addBoxToPatch(String aLabelName, UnitBox aUnitBox) {
        List<UnitBox> patchList = this.mLabelToPatchList.get(aLabelName);
        if (patchList == null) {
            patchList = new ArrayList<>();
            this.mLabelToPatchList.put(aLabelName, patchList);
        }
        patchList.add(aUnitBox);
    }
}
