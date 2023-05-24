package soot.jimple.infoflow.entryPointCreators;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LocalGenerator;
import soot.LongType;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.DoubleConstant;
import soot.jimple.EqExpr;
import soot.jimple.FloatConstant;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.infoflow.util.SystemClassHandler;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/entryPointCreators/BaseEntryPointCreator.class */
public abstract class BaseEntryPointCreator implements IEntryPointCreator {
    private List<String> substituteClasses;
    protected Value intCounter;
    protected int conditionCounter;
    static final /* synthetic */ boolean $assertionsDisabled;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected Map<SootClass, Local> localVarsForClasses = new HashMap();
    private final Set<SootClass> failedClasses = new HashSet();
    private boolean substituteCallParams = false;
    private boolean allowSelfReferences = false;
    private boolean ignoreSystemClassParams = true;
    private boolean allowNonPublicConstructors = false;
    private final Set<SootMethod> failedMethods = new HashSet();
    protected String dummyClassName = "dummyMainClass";
    protected String dummyMethodName = "dummyMainMethod";
    protected boolean shallowMode = false;
    protected boolean overwriteDummyMainMethod = false;
    protected boolean warnOnConstructorLoop = false;
    protected SootMethod mainMethod = null;
    protected Body body = null;
    protected LocalGenerator generator = null;

    protected abstract SootMethod createDummyMainInternal();

    static {
        $assertionsDisabled = !BaseEntryPointCreator.class.desiredAssertionStatus();
    }

    public Set<SootClass> getFailedClasses() {
        return new HashSet(this.failedClasses);
    }

    public Set<SootMethod> getFailedMethods() {
        return new HashSet(this.failedMethods);
    }

    @Override // soot.jimple.infoflow.entryPointCreators.IEntryPointCreator
    public void setSubstituteCallParams(boolean b) {
        this.substituteCallParams = b;
    }

    @Override // soot.jimple.infoflow.entryPointCreators.IEntryPointCreator
    public void setSubstituteClasses(List<String> l) {
        this.substituteClasses = l;
    }

    @Override // soot.jimple.infoflow.entryPointCreators.IEntryPointCreator
    public SootMethod createDummyMain() {
        if (this.substituteCallParams) {
            for (String className : this.substituteClasses) {
                Scene.v().forceResolve(className, 3).setApplicationClass();
            }
        }
        createAdditionalFields();
        createAdditionalMethods();
        createEmptyMainMethod();
        this.body = this.mainMethod.getActiveBody();
        Body body = this.mainMethod.getActiveBody();
        this.generator = Scene.v().createLocalGenerator(body);
        this.conditionCounter = 0;
        this.intCounter = this.generator.generateLocal(IntType.v());
        body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(this.intCounter, IntConstant.v(this.conditionCounter)));
        SootMethod m = createDummyMainInternal();
        m.addTag(SimulatedCodeElementTag.TAG);
        return m;
    }

    protected SootClass getOrCreateDummyMainClass() {
        SootClass mainClass = Scene.v().getSootClassUnsafe(this.dummyClassName);
        if (mainClass == null) {
            mainClass = Scene.v().makeSootClass(this.dummyClassName);
            mainClass.setResolvingLevel(3);
            Scene.v().addClass(mainClass);
        }
        return mainClass;
    }

    protected void createEmptyMainMethod() {
        int methodIndex = 0;
        String methodName = this.dummyMethodName;
        SootClass mainClass = getOrCreateDummyMainClass();
        if (!this.overwriteDummyMainMethod) {
            while (mainClass.declaresMethodByName(methodName)) {
                int i = methodIndex;
                methodIndex++;
                methodName = String.valueOf(this.dummyMethodName) + "_" + i;
            }
        }
        Type stringArrayType = ArrayType.v(RefType.v("java.lang.String"), 1);
        this.mainMethod = mainClass.getMethodByNameUnsafe(methodName);
        if (this.mainMethod != null) {
            mainClass.removeMethod(this.mainMethod);
            this.mainMethod = null;
        }
        this.mainMethod = Scene.v().makeSootMethod(methodName, Collections.singletonList(stringArrayType), VoidType.v());
        Body body = Jimple.v().newBody();
        body.setMethod(this.mainMethod);
        this.mainMethod.setActiveBody(body);
        mainClass.addMethod(this.mainMethod);
        mainClass.setApplicationClass();
        this.mainMethod.setModifiers(9);
        LocalGenerator lg = Scene.v().createLocalGenerator(body);
        Local paramLocal = lg.generateLocal(stringArrayType);
        body.getUnits().addFirst((UnitPatchingChain) Jimple.v().newIdentityStmt(paramLocal, Jimple.v().newParameterRef(stringArrayType, 0)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void createAdditionalFields() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void createAdditionalMethods() {
    }

    protected String getNonCollidingFieldName(String baseName) {
        String fieldName = baseName;
        int fieldIdx = 0;
        SootClass mainClass = getOrCreateDummyMainClass();
        while (mainClass.declaresFieldByName(fieldName)) {
            int i = fieldIdx;
            fieldIdx++;
            fieldName = String.valueOf(baseName) + "_" + i;
        }
        return fieldName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Stmt buildMethodCall(SootMethod methodToCall, Local classLocal) {
        return buildMethodCall(methodToCall, classLocal, Collections.emptySet());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Stmt buildMethodCall(SootMethod methodToCall, Local classLocal, Set<SootClass> parentClasses) {
        InvokeExpr invokeExpr;
        AssignStmt newInvokeStmt;
        if (methodToCall == null) {
            return null;
        }
        if (classLocal == null && !methodToCall.isStatic()) {
            this.logger.warn("Cannot call method {}, because there is no local for base object: {}", methodToCall, methodToCall.getDeclaringClass());
            this.failedMethods.add(methodToCall);
            return null;
        }
        Jimple jimple = Jimple.v();
        List<Value> args = new LinkedList<>();
        if (methodToCall.getParameterCount() > 0) {
            for (Type tp : methodToCall.getParameterTypes()) {
                Set<SootClass> constructionStack = new HashSet<>();
                if (!this.allowSelfReferences) {
                    constructionStack.add(methodToCall.getDeclaringClass());
                }
                args.add(getValueForType(tp, constructionStack, parentClasses));
            }
            if (methodToCall.isStatic()) {
                invokeExpr = jimple.newStaticInvokeExpr(methodToCall.makeRef(), args);
            } else if (!$assertionsDisabled && classLocal == null) {
                throw new AssertionError("Class local method was null for non-static method call");
            } else {
                if (methodToCall.isConstructor()) {
                    invokeExpr = jimple.newSpecialInvokeExpr(classLocal, methodToCall.makeRef(), args);
                } else if (methodToCall.getDeclaringClass().isInterface()) {
                    invokeExpr = jimple.newInterfaceInvokeExpr(classLocal, methodToCall.makeRef(), args);
                } else {
                    invokeExpr = jimple.newVirtualInvokeExpr(classLocal, methodToCall.makeRef(), args);
                }
            }
        } else if (methodToCall.isStatic()) {
            invokeExpr = jimple.newStaticInvokeExpr(methodToCall.makeRef());
        } else if (!$assertionsDisabled && classLocal == null) {
            throw new AssertionError("Class local method was null for non-static method call");
        } else {
            if (methodToCall.isConstructor()) {
                invokeExpr = jimple.newSpecialInvokeExpr(classLocal, methodToCall.makeRef());
            } else if (methodToCall.getDeclaringClass().isInterface()) {
                invokeExpr = jimple.newInterfaceInvokeExpr(classLocal, methodToCall.makeRef(), args);
            } else {
                invokeExpr = jimple.newVirtualInvokeExpr(classLocal, methodToCall.makeRef());
            }
        }
        if (!(methodToCall.getReturnType() instanceof VoidType)) {
            Local returnLocal = this.generator.generateLocal(methodToCall.getReturnType());
            newInvokeStmt = jimple.newAssignStmt(returnLocal, invokeExpr);
        } else {
            newInvokeStmt = jimple.newInvokeStmt(invokeExpr);
        }
        this.body.getUnits().add((UnitPatchingChain) newInvokeStmt);
        for (Value val : args) {
            if ((val instanceof Local) && (val.getType() instanceof RefType) && !parentClasses.contains(((RefType) val.getType()).getSootClass())) {
                this.body.getUnits().add((UnitPatchingChain) jimple.newAssignStmt(val, NullConstant.v()));
                this.localVarsForClasses.remove(((RefType) val.getType()).getSootClass());
            }
        }
        return newInvokeStmt;
    }

    protected Value getValueForType(Type tp, Set<SootClass> constructionStack, Set<SootClass> parentClasses) {
        return getValueForType(tp, constructionStack, parentClasses, null, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Value getValueForType(Type tp, Set<SootClass> constructionStack, Set<SootClass> parentClasses, Set<Local> generatedLocals, boolean ignoreExcludes) {
        Value val;
        if (isSimpleType(tp)) {
            return getSimpleDefaultValue(tp);
        }
        if (tp instanceof RefType) {
            SootClass classToType = ((RefType) tp).getSootClass();
            if (classToType != null) {
                if (parentClasses != null && !parentClasses.isEmpty()) {
                    for (SootClass parent : parentClasses) {
                        if (isCompatible(parent, classToType) && (val = this.localVarsForClasses.get(parent)) != null) {
                            return val;
                        }
                    }
                }
                if (!ignoreExcludes && this.ignoreSystemClassParams && SystemClassHandler.v().isClassInSystemPackage(classToType.getName())) {
                    return NullConstant.v();
                }
                Value val2 = generateClassConstructor(classToType, constructionStack, parentClasses, generatedLocals);
                if (val2 == null) {
                    return NullConstant.v();
                }
                if (generatedLocals != null && (val2 instanceof Local)) {
                    generatedLocals.add((Local) val2);
                }
                return val2;
            }
            throw new RuntimeException("Should never see me");
        } else if (tp instanceof ArrayType) {
            Value arrVal = buildArrayOfType((ArrayType) tp, constructionStack, parentClasses, generatedLocals);
            if (arrVal == null) {
                this.logger.warn("Array parameter substituted by null");
                return NullConstant.v();
            }
            return arrVal;
        } else {
            this.logger.warn("Unsupported parameter type: {}", tp.toString());
            return null;
        }
    }

    private Value buildArrayOfType(ArrayType tp, Set<SootClass> constructionStack, Set<SootClass> parentClasses, Set<Local> generatedLocals) {
        Value singleElement = getValueForType(tp.getElementType(), constructionStack, parentClasses);
        Local local = this.generator.generateLocal(tp);
        NewArrayExpr newArrayExpr = Jimple.v().newNewArrayExpr(tp.getElementType(), IntConstant.v(1));
        AssignStmt assignArray = Jimple.v().newAssignStmt(local, newArrayExpr);
        this.body.getUnits().add((UnitPatchingChain) assignArray);
        AssignStmt assign = Jimple.v().newAssignStmt(Jimple.v().newArrayRef(local, IntConstant.v(0)), singleElement);
        this.body.getUnits().add((UnitPatchingChain) assign);
        return local;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Local generateClassConstructor(SootClass createdClass) {
        return generateClassConstructor(createdClass, new HashSet(), Collections.emptySet(), null);
    }

    protected Local generateClassConstructor(SootClass createdClass, Set<SootClass> parentClasses) {
        return generateClassConstructor(createdClass, new HashSet(), parentClasses, null);
    }

    protected boolean acceptClass(SootClass clazz) {
        if (!clazz.getName().equals("android.view.View")) {
            if (clazz.isPhantom() || clazz.isPhantomClass()) {
                this.logger.warn("Cannot generate constructor for phantom class {}", clazz.getName());
                return false;
            }
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Local generateClassConstructor(SootClass createdClass, Set<SootClass> constructionStack, Set<SootClass> parentClasses, Set<Local> tempLocals) {
        SootClass sootClass;
        InvokeExpr vInvokeExpr;
        if (createdClass == null || this.failedClasses.contains(createdClass)) {
            return null;
        }
        Local existingLocal = this.localVarsForClasses.get(createdClass);
        if (existingLocal != null) {
            return existingLocal;
        }
        if (!acceptClass(createdClass)) {
            this.failedClasses.add(createdClass);
            return null;
        }
        Jimple jimple = Jimple.v();
        if (isSimpleType(createdClass.getType())) {
            Local varLocal = this.generator.generateLocal(createdClass.getType());
            AssignStmt aStmt = jimple.newAssignStmt(varLocal, getSimpleDefaultValue(createdClass.getType()));
            this.body.getUnits().add((UnitPatchingChain) aStmt);
            return varLocal;
        }
        boolean isInnerClass = createdClass.getName().contains("$");
        if (isInnerClass) {
            sootClass = Scene.v().getSootClassUnsafe(createdClass.getName().substring(0, createdClass.getName().lastIndexOf("$")));
        } else {
            sootClass = null;
        }
        SootClass outerClass = sootClass;
        if (constructionStack != null && !constructionStack.add(createdClass)) {
            if (this.warnOnConstructorLoop) {
                this.logger.warn("Ran into a constructor generation loop for class " + createdClass + ", substituting with null...");
            }
            Local tempLocal = this.generator.generateLocal(RefType.v(createdClass));
            AssignStmt assignStmt = jimple.newAssignStmt(tempLocal, NullConstant.v());
            this.body.getUnits().add((UnitPatchingChain) assignStmt);
            return tempLocal;
        } else if (createdClass.isInterface() || createdClass.isAbstract()) {
            return generateSubstitutedClassConstructor(createdClass, constructionStack, parentClasses);
        } else {
            List<SootMethod> constructors = new ArrayList<>();
            for (SootMethod currentMethod : createdClass.getMethods()) {
                if (currentMethod.isConstructor() && (this.allowNonPublicConstructors || (!currentMethod.isPrivate() && !currentMethod.isProtected()))) {
                    constructors.add(currentMethod);
                }
            }
            Collections.sort(constructors, new Comparator<SootMethod>() { // from class: soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator.1
                @Override // java.util.Comparator
                public int compare(SootMethod o1, SootMethod o2) {
                    if ((o1.isPrivate() || o1.isProtected()) != (o2.isPrivate() || o2.isProtected())) {
                        return (o1.isPrivate() || o1.isProtected()) ? 1 : -1;
                    } else if (o1.getParameterCount() == o2.getParameterCount()) {
                        int o1Prims = 0;
                        int o2Prims = 0;
                        for (int i = 0; i < o1.getParameterCount(); i++) {
                            if (o1.getParameterType(i) instanceof PrimType) {
                                o1Prims++;
                            }
                        }
                        for (int i2 = 0; i2 < o2.getParameterCount(); i2++) {
                            if (o2.getParameterType(i2) instanceof PrimType) {
                                o2Prims++;
                            }
                        }
                        return o1Prims - o2Prims;
                    } else {
                        return o1.getParameterCount() - o2.getParameterCount();
                    }
                }
            });
            if (!constructors.isEmpty()) {
                SootMethod currentMethod2 = constructors.remove(0);
                List<Value> params = new LinkedList<>();
                for (Type type : currentMethod2.getParameterTypes()) {
                    Set<SootClass> newStack = new HashSet<>(constructionStack == null ? Collections.emptySet() : constructionStack);
                    SootClass typeClass = type instanceof RefType ? ((RefType) type).getSootClass() : null;
                    if (typeClass != null && isInnerClass && typeClass == outerClass && this.localVarsForClasses.containsKey(outerClass)) {
                        params.add(this.localVarsForClasses.get(outerClass));
                    } else if (this.shallowMode) {
                        if (isSimpleType(type)) {
                            params.add(getSimpleDefaultValue(type));
                        } else {
                            params.add(NullConstant.v());
                        }
                    } else {
                        Value val = getValueForType(type, newStack, parentClasses, tempLocals, false);
                        params.add(val);
                    }
                }
                NewExpr newExpr = jimple.newNewExpr(RefType.v(createdClass));
                Local tempLocal2 = this.generator.generateLocal(RefType.v(createdClass));
                AssignStmt assignStmt2 = jimple.newAssignStmt(tempLocal2, newExpr);
                this.body.getUnits().add((UnitPatchingChain) assignStmt2);
                if (params.isEmpty() || params.contains(null)) {
                    vInvokeExpr = jimple.newSpecialInvokeExpr(tempLocal2, currentMethod2.makeRef());
                } else {
                    vInvokeExpr = jimple.newSpecialInvokeExpr(tempLocal2, currentMethod2.makeRef(), params);
                }
                this.body.getUnits().add((UnitPatchingChain) jimple.newInvokeStmt(vInvokeExpr));
                if (tempLocals != null) {
                    tempLocals.add(tempLocal2);
                }
                return tempLocal2;
            }
            this.failedClasses.add(createdClass);
            return null;
        }
    }

    private Local generateSubstitutedClassConstructor(SootClass createdClass, Set<SootClass> constructionStack, Set<SootClass> parentClasses) {
        List<SootClass> classes;
        Local cons;
        if (!this.substituteCallParams) {
            this.logger.warn("Cannot create valid constructor for {}, because it is {} and cannot substitute with subclass", createdClass, createdClass.isInterface() ? "an interface" : createdClass.isAbstract() ? Jimple.ABSTRACT : "");
            this.failedClasses.add(createdClass);
            return null;
        }
        if (createdClass.isInterface()) {
            classes = Scene.v().getActiveHierarchy().getImplementersOf(createdClass);
        } else {
            classes = Scene.v().getActiveHierarchy().getSubclassesOf(createdClass);
        }
        for (SootClass sClass : classes) {
            if (this.substituteClasses.contains(sClass.toString()) && (cons = generateClassConstructor(sClass, constructionStack, parentClasses, null)) != null) {
                return cons;
            }
        }
        this.logger.warn("Cannot create valid constructor for {}, because it is {} and cannot substitute with subclass", createdClass, createdClass.isInterface() ? "an interface" : createdClass.isAbstract() ? Jimple.ABSTRACT : "");
        this.failedClasses.add(createdClass);
        return null;
    }

    protected static boolean isSimpleType(Type t) {
        if (t instanceof PrimType) {
            return true;
        }
        if (t instanceof RefType) {
            RefType rt = (RefType) t;
            if (rt.getSootClass().getName().equals("java.lang.String")) {
                return true;
            }
            return false;
        }
        return false;
    }

    protected Value getSimpleDefaultValue(Type t) {
        if (t == RefType.v("java.lang.String")) {
            return StringConstant.v("");
        }
        if (t instanceof CharType) {
            return IntConstant.v(0);
        }
        if (t instanceof ByteType) {
            return IntConstant.v(0);
        }
        if (t instanceof ShortType) {
            return IntConstant.v(0);
        }
        if (t instanceof IntType) {
            return IntConstant.v(0);
        }
        if (t instanceof FloatType) {
            return FloatConstant.v(0.0f);
        }
        if (t instanceof LongType) {
            return LongConstant.v(0L);
        }
        if (t instanceof DoubleType) {
            return DoubleConstant.v(Const.default_value_double);
        }
        if (t instanceof BooleanType) {
            return IntConstant.v(0);
        }
        return NullConstant.v();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SootMethod findMethod(SootClass currentClass, String subsignature) {
        SootMethod m = currentClass.getMethodUnsafe(subsignature);
        if (m != null) {
            return m;
        }
        if (currentClass.hasSuperclass()) {
            return findMethod(currentClass.getSuperclass(), subsignature);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isCompatible(SootClass actual, SootClass expected) {
        return Scene.v().getOrMakeFastHierarchy().canStoreType(actual.getType(), expected.getType());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void eliminateSelfLoops() {
        Iterator<Unit> unitIt = this.body.getUnits().iterator();
        while (unitIt.hasNext()) {
            Unit u = unitIt.next();
            if (u instanceof IfStmt) {
                IfStmt ifStmt = (IfStmt) u;
                if (ifStmt.getTarget() == ifStmt) {
                    unitIt.remove();
                }
            }
        }
    }

    public void setDummyClassName(String dummyClassName) {
        this.dummyClassName = dummyClassName;
    }

    public void setDummyMethodName(String dummyMethodName) {
        this.dummyMethodName = dummyMethodName;
    }

    public void setAllowSelfReferences(boolean value) {
        this.allowSelfReferences = value;
    }

    public void setShallowMode(boolean shallowMode) {
        this.shallowMode = shallowMode;
    }

    public boolean getShallowMode() {
        return this.shallowMode;
    }

    public void setIgnoreSystemClassParams(boolean ignoreSystemClassParams) {
        this.ignoreSystemClassParams = ignoreSystemClassParams;
    }

    public void setAllowNonPublicConstructors(boolean allowNonPublicConstructors) {
        this.allowNonPublicConstructors = allowNonPublicConstructors;
    }

    public void setOverwriteDummyMainMethod(boolean overwriteDummyMainValue) {
        this.overwriteDummyMainMethod = overwriteDummyMainValue;
    }

    public boolean getOverwriteDummyMainMethod() {
        return this.overwriteDummyMainMethod;
    }

    public void setWarnOnConstructorLoop(boolean warnOnConstructorLoop) {
        this.warnOnConstructorLoop = warnOnConstructorLoop;
    }

    public boolean getWarnOnConstructorLoop() {
        return this.warnOnConstructorLoop;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void reset() {
        this.localVarsForClasses.clear();
        this.conditionCounter = 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void createIfStmt(Unit target) {
        if (target == null) {
            return;
        }
        Jimple jimple = Jimple.v();
        Value value = this.intCounter;
        int i = this.conditionCounter;
        this.conditionCounter = i + 1;
        EqExpr cond = jimple.newEqExpr(value, IntConstant.v(i));
        IfStmt ifStmt = jimple.newIfStmt(cond, target);
        this.body.getUnits().add((UnitPatchingChain) ifStmt);
    }

    @Override // soot.jimple.infoflow.entryPointCreators.IEntryPointCreator
    public SootMethod getGeneratedMainMethod() {
        return this.mainMethod;
    }
}
