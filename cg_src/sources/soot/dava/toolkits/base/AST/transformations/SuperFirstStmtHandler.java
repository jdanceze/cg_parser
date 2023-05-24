package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.G;
import soot.IntType;
import soot.JavaBasicTypes;
import soot.Local;
import soot.LongType;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.VoidType;
import soot.dava.CorruptASTException;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.DecompilationException;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DIntConstant;
import soot.dava.internal.javaRep.DNewInvokeExpr;
import soot.dava.internal.javaRep.DSpecialInvokeExpr;
import soot.dava.internal.javaRep.DStaticInvokeExpr;
import soot.dava.internal.javaRep.DVariableDeclarationStmt;
import soot.dava.internal.javaRep.DVirtualInvokeExpr;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.dava.toolkits.base.AST.traversals.ASTParentNodeFinder;
import soot.dava.toolkits.base.AST.traversals.ASTUsesAndDefs;
import soot.dava.toolkits.base.AST.traversals.AllDefinitionsFinder;
import soot.grimp.internal.GAssignStmt;
import soot.grimp.internal.GCastExpr;
import soot.grimp.internal.GInvokeStmt;
import soot.grimp.internal.GReturnStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.internal.JimpleLocal;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/SuperFirstStmtHandler.class */
public class SuperFirstStmtHandler extends DepthFirstAdapter {
    public final boolean DEBUG = false;
    ASTMethodNode originalASTMethod;
    DavaBody originalDavaBody;
    Unit originalConstructorUnit;
    InstanceInvokeExpr originalConstructorExpr;
    SootMethod originalSootMethod;
    SootClass originalSootClass;
    Map originalPMap;
    List argsOneTypes;
    List argsOneValues;
    List argsTwoValues;
    List argsTwoTypes;
    SootMethod newSootPreInitMethod;
    DavaBody newPreInitDavaBody;
    ASTMethodNode newASTPreInitMethod;
    SootMethod newConstructor;
    DavaBody newConstructorDavaBody;
    ASTMethodNode newASTConstructorMethod;
    List<Local> mustInitialize;
    int mustInitializeIndex;

    public SuperFirstStmtHandler(ASTMethodNode AST) {
        this.DEBUG = false;
        this.argsOneTypes = null;
        this.argsOneValues = null;
        this.argsTwoValues = null;
        this.argsTwoTypes = null;
        this.newSootPreInitMethod = null;
        this.newPreInitDavaBody = null;
        this.newASTPreInitMethod = null;
        this.newConstructor = null;
        this.newConstructorDavaBody = null;
        this.newASTConstructorMethod = null;
        this.mustInitializeIndex = 0;
        this.originalASTMethod = AST;
        initialize();
    }

    public SuperFirstStmtHandler(boolean verbose, ASTMethodNode AST) {
        super(verbose);
        this.DEBUG = false;
        this.argsOneTypes = null;
        this.argsOneValues = null;
        this.argsTwoValues = null;
        this.argsTwoTypes = null;
        this.newSootPreInitMethod = null;
        this.newPreInitDavaBody = null;
        this.newASTPreInitMethod = null;
        this.newConstructor = null;
        this.newConstructorDavaBody = null;
        this.newASTConstructorMethod = null;
        this.mustInitializeIndex = 0;
        this.originalASTMethod = AST;
        initialize();
    }

    public void initialize() {
        this.originalDavaBody = this.originalASTMethod.getDavaBody();
        this.originalConstructorUnit = this.originalDavaBody.get_ConstructorUnit();
        this.originalConstructorExpr = this.originalDavaBody.get_ConstructorExpr();
        if (this.originalConstructorExpr != null) {
            this.argsTwoValues = this.originalConstructorExpr.getArgs();
            this.argsTwoTypes = new ArrayList();
            for (Value val : this.argsTwoValues) {
                Type type = val.getType();
                this.argsTwoTypes.add(type);
            }
        }
        this.originalSootMethod = this.originalDavaBody.getMethod();
        this.originalSootClass = this.originalSootMethod.getDeclaringClass();
        this.originalPMap = this.originalDavaBody.get_ParamMap();
        this.argsOneTypes = this.originalSootMethod.getParameterTypes();
        this.argsOneValues = new ArrayList();
        int count = 0;
        for (Type type2 : this.argsOneTypes) {
            this.argsOneValues.add(this.originalPMap.get(new Integer(count)));
            count++;
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
        for (AugmentedStmt as : node.getStatements()) {
            Unit u = as.get_Stmt();
            if (u == this.originalConstructorUnit) {
                ASTParentNodeFinder parentFinder = new ASTParentNodeFinder();
                this.originalASTMethod.apply(parentFinder);
                Object tempParent = parentFinder.getParentOf(node);
                if (tempParent != this.originalASTMethod) {
                    removeInit();
                    return;
                }
                createSootPreInitMethod();
                createNewASTPreInitMethod(node);
                if (this.newASTPreInitMethod == null || !finalizePreInitMethod()) {
                    removeInit();
                    return;
                }
                createNewConstructor();
                createNewASTConstructor(node);
                if (!createCallToSuper()) {
                    removeInit();
                    return;
                }
                finalizeConstructor();
                if (changeOriginalAST()) {
                    debug("SuperFirstStmtHandler....inASTStatementSeuqneNode", "Added PreInit");
                    G.v().SootMethodAddedByDava = true;
                    G.v().SootMethodsAdded.add(this.newSootPreInitMethod);
                    G.v().SootMethodsAdded.add(this.newConstructor);
                    G.v().SootClassNeedsDavaSuperHandlerClass.add(this.originalSootClass);
                }
            }
        }
    }

    public void removeInit() {
        List<Object> newBody = new ArrayList<>();
        List<Object> subBody = this.originalASTMethod.get_SubBodies();
        if (subBody.size() != 1) {
            return;
        }
        List<ASTNode> oldBody = (List) subBody.get(0);
        for (ASTNode node : oldBody) {
            if (!(node instanceof ASTStatementSequenceNode)) {
                newBody.add(node);
            } else {
                ASTStatementSequenceNode seqNode = (ASTStatementSequenceNode) node;
                List<AugmentedStmt> newStmtList = new ArrayList<>();
                for (AugmentedStmt augStmt : seqNode.getStatements()) {
                    Stmt stmtTemp = augStmt.get_Stmt();
                    if (stmtTemp != this.originalConstructorUnit) {
                        newStmtList.add(augStmt);
                    }
                }
                if (newStmtList.size() != 0) {
                    newBody.add(new ASTStatementSequenceNode(newStmtList));
                }
            }
        }
        this.originalASTMethod.replaceBody(newBody);
    }

    public boolean changeOriginalAST() {
        if (this.originalConstructorExpr == null) {
            return false;
        }
        List thisArgList = new ArrayList();
        thisArgList.addAll(this.argsOneValues);
        DStaticInvokeExpr newInvokeExpr = new DStaticInvokeExpr(this.newSootPreInitMethod.makeRef(), this.argsOneValues);
        thisArgList.add(newInvokeExpr);
        InstanceInvokeExpr tempExpr = new DSpecialInvokeExpr(this.originalConstructorExpr.getBase(), this.newConstructor.makeRef(), thisArgList);
        this.originalDavaBody.set_ConstructorExpr(tempExpr);
        GInvokeStmt s = new GInvokeStmt(tempExpr);
        this.originalDavaBody.set_ConstructorUnit(s);
        this.originalASTMethod.setDeclarations(new ASTStatementSequenceNode(new ArrayList()));
        this.originalASTMethod.replaceBody(new ArrayList());
        return true;
    }

    private SootMethodRef makeMethodRef(String methodName, ArrayList args) {
        SootMethod method = Scene.v().makeSootMethod(methodName, args, RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT));
        method.setDeclaringClass(new SootClass("DavaSuperHandler"));
        return method.makeRef();
    }

    private boolean createCallToSuper() {
        if (this.originalConstructorExpr == null) {
            return false;
        }
        SootClass parentClass = this.originalSootClass.getSuperclass();
        if (!parentClass.declaresMethod("<init>", this.argsTwoTypes)) {
            return false;
        }
        SootMethod superConstructor = parentClass.getMethod("<init>", this.argsTwoTypes);
        List argsForConstructor = new ArrayList();
        int count = 0;
        RefType type = new SootClass("DavaSuperHandler").getType();
        Local jimpleLocal = new JimpleLocal("handler", type);
        ArrayList tempList = new ArrayList();
        tempList.add(IntType.v());
        SootMethodRef getMethodRef = makeMethodRef("get", tempList);
        for (Type tempType : this.argsTwoTypes) {
            DIntConstant arg = DIntConstant.v(count, IntType.v());
            count++;
            List tempArgList = new ArrayList();
            tempArgList.add(arg);
            DVirtualInvokeExpr tempInvokeExpr = new DVirtualInvokeExpr(jimpleLocal, getMethodRef, tempArgList, new HashSet());
            Value toAddExpr = getProperCasting(tempType, tempInvokeExpr);
            if (toAddExpr == null) {
                throw new DecompilationException("UNABLE TO CREATE TOADDEXPR:" + tempType);
            }
            argsForConstructor.add(toAddExpr);
        }
        this.mustInitializeIndex = count;
        DVirtualInvokeExpr virtualInvoke = new DVirtualInvokeExpr(this.originalConstructorExpr.getBase(), superConstructor.makeRef(), argsForConstructor, new HashSet());
        this.newConstructorDavaBody.set_ConstructorExpr(virtualInvoke);
        GInvokeStmt s = new GInvokeStmt(virtualInvoke);
        this.newConstructorDavaBody.set_ConstructorUnit(s);
        return true;
    }

    public Value getProperCasting(Type tempType, DVirtualInvokeExpr tempInvokeExpr) {
        if (tempType instanceof RefType) {
            return new GCastExpr(tempInvokeExpr, tempType);
        }
        if (tempType instanceof PrimType) {
            PrimType t = (PrimType) tempType;
            if (t == BooleanType.v()) {
                Value tempExpr = new GCastExpr(tempInvokeExpr, RefType.v(JavaBasicTypes.JAVA_LANG_BOOLEAN));
                SootMethod tempMethod = Scene.v().makeSootMethod("booleanValue", new ArrayList(), BooleanType.v());
                tempMethod.setDeclaringClass(new SootClass(JavaBasicTypes.JAVA_LANG_BOOLEAN));
                SootMethodRef tempMethodRef = tempMethod.makeRef();
                return new DVirtualInvokeExpr(tempExpr, tempMethodRef, new ArrayList(), new HashSet());
            } else if (t == ByteType.v()) {
                Value tempExpr2 = new GCastExpr(tempInvokeExpr, RefType.v(JavaBasicTypes.JAVA_LANG_BYTE));
                SootMethod tempMethod2 = Scene.v().makeSootMethod("byteValue", new ArrayList(), ByteType.v());
                tempMethod2.setDeclaringClass(new SootClass(JavaBasicTypes.JAVA_LANG_BYTE));
                SootMethodRef tempMethodRef2 = tempMethod2.makeRef();
                return new DVirtualInvokeExpr(tempExpr2, tempMethodRef2, new ArrayList(), new HashSet());
            } else if (t == CharType.v()) {
                Value tempExpr3 = new GCastExpr(tempInvokeExpr, RefType.v(JavaBasicTypes.JAVA_LANG_CHARACTER));
                SootMethod tempMethod3 = Scene.v().makeSootMethod("charValue", new ArrayList(), CharType.v());
                tempMethod3.setDeclaringClass(new SootClass(JavaBasicTypes.JAVA_LANG_CHARACTER));
                SootMethodRef tempMethodRef3 = tempMethod3.makeRef();
                return new DVirtualInvokeExpr(tempExpr3, tempMethodRef3, new ArrayList(), new HashSet());
            } else if (t == DoubleType.v()) {
                Value tempExpr4 = new GCastExpr(tempInvokeExpr, RefType.v(JavaBasicTypes.JAVA_LANG_DOUBLE));
                SootMethod tempMethod4 = Scene.v().makeSootMethod("doubleValue", new ArrayList(), DoubleType.v());
                tempMethod4.setDeclaringClass(new SootClass(JavaBasicTypes.JAVA_LANG_DOUBLE));
                SootMethodRef tempMethodRef4 = tempMethod4.makeRef();
                return new DVirtualInvokeExpr(tempExpr4, tempMethodRef4, new ArrayList(), new HashSet());
            } else if (t == FloatType.v()) {
                Value tempExpr5 = new GCastExpr(tempInvokeExpr, RefType.v(JavaBasicTypes.JAVA_LANG_FLOAT));
                SootMethod tempMethod5 = Scene.v().makeSootMethod("floatValue", new ArrayList(), FloatType.v());
                tempMethod5.setDeclaringClass(new SootClass(JavaBasicTypes.JAVA_LANG_FLOAT));
                SootMethodRef tempMethodRef5 = tempMethod5.makeRef();
                return new DVirtualInvokeExpr(tempExpr5, tempMethodRef5, new ArrayList(), new HashSet());
            } else if (t == IntType.v()) {
                Value tempExpr6 = new GCastExpr(tempInvokeExpr, RefType.v(JavaBasicTypes.JAVA_LANG_INTEGER));
                SootMethod tempMethod6 = Scene.v().makeSootMethod("intValue", new ArrayList(), IntType.v());
                tempMethod6.setDeclaringClass(new SootClass(JavaBasicTypes.JAVA_LANG_INTEGER));
                SootMethodRef tempMethodRef6 = tempMethod6.makeRef();
                return new DVirtualInvokeExpr(tempExpr6, tempMethodRef6, new ArrayList(), new HashSet());
            } else if (t == LongType.v()) {
                Value tempExpr7 = new GCastExpr(tempInvokeExpr, RefType.v(JavaBasicTypes.JAVA_LANG_LONG));
                SootMethod tempMethod7 = Scene.v().makeSootMethod("longValue", new ArrayList(), LongType.v());
                tempMethod7.setDeclaringClass(new SootClass(JavaBasicTypes.JAVA_LANG_LONG));
                SootMethodRef tempMethodRef7 = tempMethod7.makeRef();
                return new DVirtualInvokeExpr(tempExpr7, tempMethodRef7, new ArrayList(), new HashSet());
            } else if (t == ShortType.v()) {
                Value tempExpr8 = new GCastExpr(tempInvokeExpr, RefType.v(JavaBasicTypes.JAVA_LANG_SHORT));
                SootMethod tempMethod8 = Scene.v().makeSootMethod("shortValue", new ArrayList(), ShortType.v());
                tempMethod8.setDeclaringClass(new SootClass(JavaBasicTypes.JAVA_LANG_SHORT));
                SootMethodRef tempMethodRef8 = tempMethod8.makeRef();
                return new DVirtualInvokeExpr(tempExpr8, tempMethodRef8, new ArrayList(), new HashSet());
            } else {
                throw new DecompilationException("Unhandle primType:" + tempType);
            }
        }
        throw new DecompilationException("The type:" + tempType + " was not a reftye or primtype. PLEASE REPORT.");
    }

    private void finalizeConstructor() {
        this.newASTConstructorMethod.setDavaBody(this.newConstructorDavaBody);
        this.newConstructorDavaBody.getUnits().clear();
        this.newConstructorDavaBody.getUnits().addLast((UnitPatchingChain) this.newASTConstructorMethod);
        System.out.println("Setting declaring class of method" + this.newConstructor.getSubSignature());
        this.newConstructor.setDeclaringClass(this.originalSootClass);
    }

    private boolean finalizePreInitMethod() {
        this.newASTPreInitMethod.setDavaBody(this.newPreInitDavaBody);
        this.newPreInitDavaBody.getUnits().clear();
        this.newPreInitDavaBody.getUnits().addLast((UnitPatchingChain) this.newASTPreInitMethod);
        List<Object> subBodies = this.newASTPreInitMethod.get_SubBodies();
        if (subBodies.size() != 1) {
            return false;
        }
        List body = (List) subBodies.get(0);
        Iterator it = body.iterator();
        boolean empty = true;
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ASTNode tempNode = (ASTNode) it.next();
            if (!(tempNode instanceof ASTStatementSequenceNode)) {
                empty = false;
                break;
            }
            List<AugmentedStmt> stmts = ((ASTStatementSequenceNode) tempNode).getStatements();
            Iterator<AugmentedStmt> it2 = stmts.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                AugmentedStmt as = it2.next();
                Stmt s = as.get_Stmt();
                if (!(s instanceof DVariableDeclarationStmt)) {
                    empty = false;
                    break;
                }
            }
            if (!empty) {
                break;
            }
        }
        if (empty) {
            return false;
        }
        createDavaStoreStmts();
        return true;
    }

    public void createNewASTConstructor(ASTStatementSequenceNode initNode) {
        List<Object> newConstructorBody = new ArrayList<>();
        List<AugmentedStmt> newStmts = new ArrayList<>();
        RefType type = new SootClass("DavaSuperHandler").getType();
        Local jimpleLocal = new JimpleLocal("handler", type);
        ArrayList tempList = new ArrayList();
        tempList.add(IntType.v());
        SootMethodRef getMethodRef = makeMethodRef("get", tempList);
        if (this.mustInitialize != null) {
            for (Local initLocal : this.mustInitialize) {
                Type tempType = initLocal.getType();
                DIntConstant arg = DIntConstant.v(this.mustInitializeIndex, IntType.v());
                this.mustInitializeIndex++;
                ArrayList tempArgList = new ArrayList();
                tempArgList.add(arg);
                DVirtualInvokeExpr tempInvokeExpr = new DVirtualInvokeExpr(jimpleLocal, getMethodRef, tempArgList, new HashSet());
                Value toAddExpr = getProperCasting(tempType, tempInvokeExpr);
                if (toAddExpr == null) {
                    throw new DecompilationException("UNABLE TO CREATE TOADDEXPR:" + tempType);
                }
                GAssignStmt assign = new GAssignStmt(initLocal, toAddExpr);
                newStmts.add(new AugmentedStmt(assign));
            }
        }
        Iterator<AugmentedStmt> it = initNode.getStatements().iterator();
        while (it.hasNext()) {
            AugmentedStmt augStmt = it.next();
            Stmt stmtTemp = augStmt.get_Stmt();
            if (stmtTemp == this.originalConstructorUnit) {
                break;
            }
        }
        while (it.hasNext()) {
            newStmts.add(it.next());
        }
        if (newStmts.size() > 0) {
            newConstructorBody.add(new ASTStatementSequenceNode(newStmts));
        }
        List<Object> originalASTMethodSubBodies = this.originalASTMethod.get_SubBodies();
        if (originalASTMethodSubBodies.size() != 1) {
            throw new CorruptASTException("size of ASTMethodNode subBody not 1");
        }
        List<Object> oldASTBody = (List) originalASTMethodSubBodies.get(0);
        Iterator<Object> itOld = oldASTBody.iterator();
        boolean sanity = false;
        while (true) {
            if (!itOld.hasNext()) {
                break;
            }
            ASTNode tempNode = (ASTNode) itOld.next();
            if ((tempNode instanceof ASTStatementSequenceNode) && ((ASTStatementSequenceNode) tempNode).getStatements().equals(initNode.getStatements())) {
                sanity = true;
                break;
            }
        }
        if (!sanity) {
            throw new DecompilationException("never found the init node");
        }
        while (itOld.hasNext()) {
            newConstructorBody.add(itOld.next());
        }
        List<AugmentedStmt> newConstructorDeclarations = new ArrayList<>();
        for (AugmentedStmt as : this.originalASTMethod.getDeclarations().getStatements()) {
            DVariableDeclarationStmt varDecStmt = (DVariableDeclarationStmt) as.get_Stmt();
            newConstructorDeclarations.add(new AugmentedStmt((DVariableDeclarationStmt) varDecStmt.clone()));
        }
        Object newDecs = new ASTStatementSequenceNode(new ArrayList());
        if (newConstructorDeclarations.size() > 0) {
            newDecs = new ASTStatementSequenceNode(newConstructorDeclarations);
            newConstructorBody.add(0, newDecs);
        }
        this.newASTConstructorMethod = new ASTMethodNode(newConstructorBody);
        this.newASTConstructorMethod.setDeclarations(newDecs);
    }

    private void createNewConstructor() {
        List args = new ArrayList();
        args.addAll(this.argsOneTypes);
        RefType type = new SootClass("DavaSuperHandler").getType();
        args.add(type);
        this.newConstructor = Scene.v().makeSootMethod("<init>", args, IntType.v());
        this.newConstructor.setDeclaringClass(this.originalSootClass);
        this.newConstructor.setModifiers(1);
        this.newConstructorDavaBody = Dava.v().newBody(this.newConstructor);
        Map tempMap = new HashMap();
        int count = 0;
        for (Type type2 : this.argsOneTypes) {
            tempMap.put(new Integer(count), this.originalPMap.get(new Integer(count)));
            count++;
        }
        tempMap.put(new Integer(this.argsOneTypes.size()), "handler");
        this.newConstructorDavaBody.set_ParamMap(tempMap);
        this.newConstructor.setActiveBody(this.newConstructorDavaBody);
    }

    private void createNewASTPreInitMethod(ASTStatementSequenceNode initNode) {
        List<Object> newPreinitBody = new ArrayList<>();
        List<Object> originalASTMethodSubBodies = this.originalASTMethod.get_SubBodies();
        if (originalASTMethodSubBodies.size() != 1) {
            throw new CorruptASTException("size of ASTMethodNode subBody not 1");
        }
        List<Object> oldASTBody = (List) originalASTMethodSubBodies.get(0);
        Iterator<Object> it = oldASTBody.iterator();
        boolean sanity = false;
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ASTNode tempNode = (ASTNode) it.next();
            if (tempNode instanceof ASTStatementSequenceNode) {
                if (((ASTStatementSequenceNode) tempNode).getStatements().equals(initNode.getStatements())) {
                    sanity = true;
                    break;
                }
                newPreinitBody.add(tempNode);
            } else {
                newPreinitBody.add(tempNode);
            }
        }
        if (!sanity) {
            throw new DecompilationException("never found the init node");
        }
        List<AugmentedStmt> newStmts = new ArrayList<>();
        for (AugmentedStmt augStmt : initNode.getStatements()) {
            Stmt stmtTemp = augStmt.get_Stmt();
            if (stmtTemp == this.originalConstructorUnit) {
                break;
            }
            newStmts.add(augStmt);
        }
        if (newStmts.size() > 0) {
            newPreinitBody.add(new ASTStatementSequenceNode(newStmts));
        }
        List<AugmentedStmt> newPreinitDeclarations = new ArrayList<>();
        for (AugmentedStmt as : this.originalASTMethod.getDeclarations().getStatements()) {
            DVariableDeclarationStmt varDecStmt = (DVariableDeclarationStmt) as.get_Stmt();
            newPreinitDeclarations.add(new AugmentedStmt((DVariableDeclarationStmt) varDecStmt.clone()));
        }
        Object newDecs = new ASTStatementSequenceNode(new ArrayList());
        if (newPreinitDeclarations.size() > 0) {
            newDecs = new ASTStatementSequenceNode(newPreinitDeclarations);
            newPreinitBody.remove(0);
            newPreinitBody.add(0, newDecs);
        }
        if (newPreinitBody.size() < 1) {
            this.newASTPreInitMethod = null;
            return;
        }
        this.newASTPreInitMethod = new ASTMethodNode(newPreinitBody);
        this.newASTPreInitMethod.setDeclarations(newDecs);
    }

    private void createSootPreInitMethod() {
        String uniqueName = getUniqueName();
        this.newSootPreInitMethod = Scene.v().makeSootMethod(uniqueName, this.argsOneTypes, new SootClass("DavaSuperHandler").getType());
        this.newSootPreInitMethod.setDeclaringClass(this.originalSootClass);
        this.newSootPreInitMethod.setModifiers(10);
        this.newPreInitDavaBody = Dava.v().newBody(this.newSootPreInitMethod);
        this.newPreInitDavaBody.set_ParamMap(this.originalPMap);
        this.newSootPreInitMethod.setActiveBody(this.newPreInitDavaBody);
    }

    private String getUniqueName() {
        String toReturn = "preInit";
        int counter = 0;
        List methodList = this.originalSootClass.getMethods();
        boolean done = false;
        while (!done) {
            done = true;
            Iterator it = methodList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Object temp = it.next();
                if (temp instanceof SootMethod) {
                    SootMethod method = (SootMethod) temp;
                    String name = method.getName();
                    if (toReturn.compareTo(name) == 0) {
                        counter++;
                        toReturn = "preInit" + counter;
                        done = false;
                        break;
                    }
                } else {
                    throw new DecompilationException("SootClass returned a non SootMethod method");
                }
            }
            Iterator it2 = G.v().SootMethodsAdded.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                SootMethod method2 = it2.next();
                String name2 = method2.getName();
                if (toReturn.compareTo(name2) == 0) {
                    counter++;
                    toReturn = "preInit" + counter;
                    done = false;
                    break;
                }
            }
        }
        return toReturn;
    }

    private void createDavaStoreStmts() {
        List<AugmentedStmt> davaHandlerStmts = new ArrayList<>();
        SootClass sootClass = new SootClass("DavaSuperHandler");
        Type localType = sootClass.getType();
        Local newLocal = new JimpleLocal("handler", localType);
        DVariableDeclarationStmt varStmt = new DVariableDeclarationStmt(localType, this.newPreInitDavaBody);
        varStmt.addLocal(newLocal);
        AugmentedStmt as = new AugmentedStmt(varStmt);
        davaHandlerStmts.add(as);
        DNewInvokeExpr invokeExpr = new DNewInvokeExpr(RefType.v(sootClass), makeMethodRef("DavaSuperHandler", new ArrayList()), new ArrayList());
        GAssignStmt initialization = new GAssignStmt(newLocal, invokeExpr);
        davaHandlerStmts.add(new AugmentedStmt(initialization));
        Iterator typeIt = this.argsTwoTypes.iterator();
        Iterator valIt = this.argsTwoValues.iterator();
        ArrayList tempList = new ArrayList();
        tempList.add(RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT));
        SootMethod method = Scene.v().makeSootMethod("store", tempList, VoidType.v());
        method.setDeclaringClass(sootClass);
        SootMethodRef getMethodRef = method.makeRef();
        while (typeIt.hasNext() && valIt.hasNext()) {
            Type tempType = (Type) typeIt.next();
            Value tempVal = (Value) valIt.next();
            AugmentedStmt toAdd = createStmtAccordingToType(tempType, tempVal, newLocal, getMethodRef);
            davaHandlerStmts.add(toAdd);
        }
        if (typeIt.hasNext() || valIt.hasNext()) {
            throw new DecompilationException("Error creating DavaHandler stmts");
        }
        List<Local> uniqueLocals = addDefsToLiveVariables();
        for (Local local : uniqueLocals) {
            AugmentedStmt toAdd2 = createStmtAccordingToType(local.getType(), local, newLocal, getMethodRef);
            davaHandlerStmts.add(toAdd2);
        }
        this.mustInitialize = uniqueLocals;
        GReturnStmt returnStmt = new GReturnStmt(newLocal);
        davaHandlerStmts.add(new AugmentedStmt(returnStmt));
        ASTStatementSequenceNode addedNode = new ASTStatementSequenceNode(davaHandlerStmts);
        List<Object> subBodies = this.newASTPreInitMethod.get_SubBodies();
        if (subBodies.size() != 1) {
            throw new CorruptASTException("ASTMethodNode does not have one subBody");
        }
        List<Object> body = (List) subBodies.get(0);
        body.add(addedNode);
        this.newASTPreInitMethod.replaceBody(body);
    }

    public AugmentedStmt createStmtAccordingToType(Type tempType, Value tempVal, Local newLocal, SootMethodRef getMethodRef) {
        if (tempType instanceof RefType) {
            return createAugmentedStmtToAdd(newLocal, getMethodRef, tempVal);
        }
        if (tempType instanceof PrimType) {
            PrimType t = (PrimType) tempType;
            ArrayList argList = new ArrayList();
            argList.add(tempVal);
            if (t == BooleanType.v()) {
                ArrayList typeList = new ArrayList();
                typeList.add(IntType.v());
                DNewInvokeExpr argForStore = new DNewInvokeExpr(RefType.v(JavaBasicTypes.JAVA_LANG_BOOLEAN), makeMethodRef("Boolean", typeList), argList);
                return createAugmentedStmtToAdd(newLocal, getMethodRef, argForStore);
            } else if (t == ByteType.v()) {
                ArrayList typeList2 = new ArrayList();
                typeList2.add(ByteType.v());
                DNewInvokeExpr argForStore2 = new DNewInvokeExpr(RefType.v(JavaBasicTypes.JAVA_LANG_BYTE), makeMethodRef("Byte", typeList2), argList);
                return createAugmentedStmtToAdd(newLocal, getMethodRef, argForStore2);
            } else if (t == CharType.v()) {
                ArrayList typeList3 = new ArrayList();
                typeList3.add(CharType.v());
                DNewInvokeExpr argForStore3 = new DNewInvokeExpr(RefType.v(JavaBasicTypes.JAVA_LANG_CHARACTER), makeMethodRef("Character", typeList3), argList);
                return createAugmentedStmtToAdd(newLocal, getMethodRef, argForStore3);
            } else if (t == DoubleType.v()) {
                ArrayList typeList4 = new ArrayList();
                typeList4.add(DoubleType.v());
                DNewInvokeExpr argForStore4 = new DNewInvokeExpr(RefType.v(JavaBasicTypes.JAVA_LANG_DOUBLE), makeMethodRef("Double", typeList4), argList);
                return createAugmentedStmtToAdd(newLocal, getMethodRef, argForStore4);
            } else if (t == FloatType.v()) {
                ArrayList typeList5 = new ArrayList();
                typeList5.add(FloatType.v());
                DNewInvokeExpr argForStore5 = new DNewInvokeExpr(RefType.v(JavaBasicTypes.JAVA_LANG_FLOAT), makeMethodRef("Float", typeList5), argList);
                return createAugmentedStmtToAdd(newLocal, getMethodRef, argForStore5);
            } else if (t == IntType.v()) {
                ArrayList typeList6 = new ArrayList();
                typeList6.add(IntType.v());
                DNewInvokeExpr argForStore6 = new DNewInvokeExpr(RefType.v(JavaBasicTypes.JAVA_LANG_INTEGER), makeMethodRef("Integer", typeList6), argList);
                return createAugmentedStmtToAdd(newLocal, getMethodRef, argForStore6);
            } else if (t == LongType.v()) {
                ArrayList typeList7 = new ArrayList();
                typeList7.add(LongType.v());
                DNewInvokeExpr argForStore7 = new DNewInvokeExpr(RefType.v(JavaBasicTypes.JAVA_LANG_LONG), makeMethodRef("Long", typeList7), argList);
                return createAugmentedStmtToAdd(newLocal, getMethodRef, argForStore7);
            } else if (t == ShortType.v()) {
                ArrayList typeList8 = new ArrayList();
                typeList8.add(ShortType.v());
                DNewInvokeExpr argForStore8 = new DNewInvokeExpr(RefType.v(JavaBasicTypes.JAVA_LANG_SHORT), makeMethodRef("Short", typeList8), argList);
                return createAugmentedStmtToAdd(newLocal, getMethodRef, argForStore8);
            } else {
                throw new DecompilationException("UNHANDLED PRIMTYPE:" + tempType);
            }
        }
        throw new DecompilationException("The type:" + tempType + " is neither a reftype or a primtype");
    }

    private List<Local> addDefsToLiveVariables() {
        AllDefinitionsFinder finder = new AllDefinitionsFinder();
        this.newASTPreInitMethod.apply(finder);
        List<DefinitionStmt> allDefs = finder.getAllDefs();
        List<Local> uniqueLocals = new ArrayList<>();
        List<DefinitionStmt> uniqueLocalDefs = new ArrayList<>();
        for (DefinitionStmt s : allDefs) {
            Value left = s.getLeftOp();
            if (left instanceof Local) {
                if (uniqueLocals.contains(left)) {
                    int index = uniqueLocals.indexOf(left);
                    uniqueLocals.remove(index);
                    uniqueLocalDefs.remove(index);
                } else {
                    uniqueLocals.add((Local) left);
                    uniqueLocalDefs.add(s);
                }
            }
        }
        ASTParentNodeFinder parentFinder = new ASTParentNodeFinder();
        this.newASTPreInitMethod.apply(parentFinder);
        List<DefinitionStmt> toRemoveDefs = new ArrayList<>();
        for (DefinitionStmt s2 : uniqueLocalDefs) {
            Object parent = parentFinder.getParentOf(s2);
            if (parent == null || !(parent instanceof ASTStatementSequenceNode)) {
                toRemoveDefs.add(s2);
            }
            Object grandParent = parentFinder.getParentOf(parent);
            if (grandParent == null || !(grandParent instanceof ASTMethodNode)) {
                toRemoveDefs.add(s2);
            }
        }
        for (DefinitionStmt s3 : toRemoveDefs) {
            int index2 = uniqueLocalDefs.indexOf(s3);
            uniqueLocals.remove(index2);
            uniqueLocalDefs.remove(index2);
        }
        List<DefinitionStmt> toRemoveDefs2 = new ArrayList<>();
        ASTUsesAndDefs uDdU = new ASTUsesAndDefs(this.originalASTMethod);
        this.originalASTMethod.apply(uDdU);
        for (DefinitionStmt s4 : uniqueLocalDefs) {
            Object temp = uDdU.getDUChain(s4);
            if (temp == null) {
                toRemoveDefs2.add(s4);
            } else {
                ArrayList uses = (ArrayList) temp;
                if (uses.size() == 0) {
                    toRemoveDefs2.add(s4);
                }
                Iterator useIt = uses.iterator();
                boolean onlyInConstructorUnit = true;
                while (useIt.hasNext()) {
                    Object tempUse = useIt.next();
                    if (tempUse != this.originalConstructorUnit) {
                        onlyInConstructorUnit = false;
                    }
                }
                if (onlyInConstructorUnit) {
                    toRemoveDefs2.add(s4);
                }
            }
        }
        for (DefinitionStmt s5 : toRemoveDefs2) {
            int index3 = uniqueLocalDefs.indexOf(s5);
            uniqueLocals.remove(index3);
            uniqueLocalDefs.remove(index3);
        }
        return uniqueLocals;
    }

    private AugmentedStmt createAugmentedStmtToAdd(Local newLocal, SootMethodRef getMethodRef, Value tempVal) {
        ArrayList tempArgList = new ArrayList();
        tempArgList.add(tempVal);
        DVirtualInvokeExpr tempInvokeExpr = new DVirtualInvokeExpr(newLocal, getMethodRef, tempArgList, new HashSet());
        GInvokeStmt s = new GInvokeStmt(tempInvokeExpr);
        return new AugmentedStmt(s);
    }

    public void debug(String methodName, String debug) {
    }
}
