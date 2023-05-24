package soot.jimple.toolkits.reflection;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.Local;
import soot.LocalGenerator;
import soot.PatchingChain;
import soot.PhaseOptions;
import soot.PrimType;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.ClassConstant;
import soot.jimple.FieldRef;
import soot.jimple.GotoStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.infoflow.results.xml.XmlConstants;
import soot.jimple.toolkits.reflection.ReflectionTraceInfo;
import soot.jimple.toolkits.scalar.CopyPropagator;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.jimple.toolkits.scalar.NopEliminator;
import soot.options.CGOptions;
import soot.options.Options;
import soot.rtlib.tamiflex.DefaultHandler;
import soot.rtlib.tamiflex.IUnexpectedReflectiveCallHandler;
import soot.rtlib.tamiflex.OpaquePredicate;
import soot.rtlib.tamiflex.ReflectiveCalls;
import soot.rtlib.tamiflex.SootSig;
import soot.rtlib.tamiflex.UnexpectedReflectiveCall;
import soot.toolkits.scalar.UnusedLocalEliminator;
import soot.util.Chain;
import soot.util.HashChain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/reflection/ReflectiveCallsInliner.class */
public class ReflectiveCallsInliner extends SceneTransformer {
    private static final String ALREADY_CHECKED_FIELDNAME = "SOOT$Reflection$alreadyChecked";
    private static final List<String> fieldSets = Arrays.asList("set", "setBoolean", "setByte", "setChar", "setInt", "setLong", "setFloat", "setDouble", "setShort");
    private static final List<String> fieldGets = Arrays.asList("get", "getBoolean", "getByte", "getChar", "getInt", "getLong", "getFloat", "getDouble", "getShort");
    private static final boolean useCaching = false;
    private ReflectionTraceInfo RTI;
    private SootMethodRef UNINTERPRETED_METHOD;
    private boolean initialized = false;
    private int callSiteId;
    private int callNum;
    private SootClass reflectiveCallsClass;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$toolkits$reflection$ReflectionTraceInfo$Kind;

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$toolkits$reflection$ReflectionTraceInfo$Kind() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$toolkits$reflection$ReflectionTraceInfo$Kind;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[ReflectionTraceInfo.Kind.valuesCustom().length];
        try {
            iArr2[ReflectionTraceInfo.Kind.ClassForName.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[ReflectionTraceInfo.Kind.ClassNewInstance.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[ReflectionTraceInfo.Kind.ConstructorNewInstance.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[ReflectionTraceInfo.Kind.FieldGet.ordinal()] = 6;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[ReflectionTraceInfo.Kind.FieldSet.ordinal()] = 5;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[ReflectionTraceInfo.Kind.MethodInvoke.ordinal()] = 4;
        } catch (NoSuchFieldError unused6) {
        }
        $SWITCH_TABLE$soot$jimple$toolkits$reflection$ReflectionTraceInfo$Kind = iArr2;
        return iArr2;
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> options) {
        if (!this.initialized) {
            CGOptions cgOptions = new CGOptions(PhaseOptions.v().getPhaseOptions("cg"));
            this.RTI = new ReflectionTraceInfo(cgOptions.reflection_log());
            Scene scene = Scene.v();
            scene.getSootClass(SootSig.class.getName()).setApplicationClass();
            scene.getSootClass(UnexpectedReflectiveCall.class.getName()).setApplicationClass();
            scene.getSootClass(IUnexpectedReflectiveCallHandler.class.getName()).setApplicationClass();
            scene.getSootClass(DefaultHandler.class.getName()).setApplicationClass();
            scene.getSootClass(OpaquePredicate.class.getName()).setApplicationClass();
            scene.getSootClass(ReflectiveCalls.class.getName()).setApplicationClass();
            SootClass reflectiveCallsClass = new SootClass("soot.rtlib.tamiflex.ReflectiveCallsWrapper", 1);
            scene.addClass(reflectiveCallsClass);
            reflectiveCallsClass.setApplicationClass();
            this.reflectiveCallsClass = reflectiveCallsClass;
            this.UNINTERPRETED_METHOD = scene.makeMethodRef(scene.getSootClass("soot.rtlib.tamiflex.OpaquePredicate"), "getFalse", Collections.emptyList(), BooleanType.v(), true);
            initializeReflectiveCallsTable();
            this.callSiteId = 0;
            this.callNum = 0;
            this.initialized = true;
        }
        boolean validate = Options.v().validate();
        for (SootMethod m : this.RTI.methodsContainingReflectiveCalls()) {
            Body b = m.retrieveActiveBody();
            Set<String> classForNameClassNames = this.RTI.classForNameClassNames(m);
            if (!classForNameClassNames.isEmpty()) {
                inlineRelectiveCalls(m, classForNameClassNames, ReflectionTraceInfo.Kind.ClassForName);
                if (validate) {
                    b.validate();
                }
            }
            Set<String> classNewInstanceClassNames = this.RTI.classNewInstanceClassNames(m);
            if (!classNewInstanceClassNames.isEmpty()) {
                inlineRelectiveCalls(m, classNewInstanceClassNames, ReflectionTraceInfo.Kind.ClassNewInstance);
                if (validate) {
                    b.validate();
                }
            }
            Set<String> constructorNewInstanceSignatures = this.RTI.constructorNewInstanceSignatures(m);
            if (!constructorNewInstanceSignatures.isEmpty()) {
                inlineRelectiveCalls(m, constructorNewInstanceSignatures, ReflectionTraceInfo.Kind.ConstructorNewInstance);
                if (validate) {
                    b.validate();
                }
            }
            Set<String> methodInvokeSignatures = this.RTI.methodInvokeSignatures(m);
            if (!methodInvokeSignatures.isEmpty()) {
                inlineRelectiveCalls(m, methodInvokeSignatures, ReflectionTraceInfo.Kind.MethodInvoke);
                if (validate) {
                    b.validate();
                }
            }
            Set<String> fieldSetSignatures = this.RTI.fieldSetSignatures(m);
            if (!fieldSetSignatures.isEmpty()) {
                inlineRelectiveCalls(m, fieldSetSignatures, ReflectionTraceInfo.Kind.FieldSet);
                if (validate) {
                    b.validate();
                }
            }
            Set<String> fieldGetSignatures = this.RTI.fieldGetSignatures(m);
            if (!fieldGetSignatures.isEmpty()) {
                inlineRelectiveCalls(m, fieldGetSignatures, ReflectionTraceInfo.Kind.FieldGet);
                if (validate) {
                    b.validate();
                }
            }
            cleanup(b);
        }
    }

    private void cleanup(Body b) {
        CopyPropagator.v().transform(b);
        DeadAssignmentEliminator.v().transform(b);
        UnusedLocalEliminator.v().transform(b);
        NopEliminator.v().transform(b);
    }

    private void initializeReflectiveCallsTable() {
        Jimple jimp = Jimple.v();
        Scene scene = Scene.v();
        SootClass reflCallsClass = scene.getSootClass("soot.rtlib.tamiflex.ReflectiveCalls");
        Body body = reflCallsClass.getMethodByName("<clinit>").retrieveActiveBody();
        LocalGenerator localGen = scene.createLocalGenerator(body);
        Chain<Unit> hashChain = new HashChain<>();
        SootClass sootClassSet = scene.getSootClass("java.util.Set");
        RefType refTypeSet = sootClassSet.getType();
        SootMethodRef addMethodRef = sootClassSet.getMethodByName("add").makeRef();
        int callSiteId = 0;
        for (SootMethod m : this.RTI.methodsContainingReflectiveCalls()) {
            if (!this.RTI.classForNameClassNames(m).isEmpty()) {
                SootFieldRef fieldRef = scene.makeFieldRef(reflCallsClass, "classForName", refTypeSet, true);
                Local setLocal = localGen.generateLocal(refTypeSet);
                hashChain.add(jimp.newAssignStmt(setLocal, jimp.newStaticFieldRef(fieldRef)));
                for (String className : this.RTI.classForNameClassNames(m)) {
                    InterfaceInvokeExpr invokeExpr = jimp.newInterfaceInvokeExpr(setLocal, addMethodRef, StringConstant.v(String.valueOf(callSiteId) + className));
                    hashChain.add(jimp.newInvokeStmt(invokeExpr));
                }
                callSiteId++;
            }
            if (!this.RTI.classNewInstanceClassNames(m).isEmpty()) {
                SootFieldRef fieldRef2 = scene.makeFieldRef(reflCallsClass, "classNewInstance", refTypeSet, true);
                Local setLocal2 = localGen.generateLocal(refTypeSet);
                hashChain.add(jimp.newAssignStmt(setLocal2, jimp.newStaticFieldRef(fieldRef2)));
                for (String className2 : this.RTI.classNewInstanceClassNames(m)) {
                    InterfaceInvokeExpr invokeExpr2 = jimp.newInterfaceInvokeExpr(setLocal2, addMethodRef, StringConstant.v(String.valueOf(callSiteId) + className2));
                    hashChain.add(jimp.newInvokeStmt(invokeExpr2));
                }
                callSiteId++;
            }
            if (!this.RTI.constructorNewInstanceSignatures(m).isEmpty()) {
                SootFieldRef fieldRef3 = scene.makeFieldRef(reflCallsClass, "constructorNewInstance", refTypeSet, true);
                Local setLocal3 = localGen.generateLocal(refTypeSet);
                hashChain.add(jimp.newAssignStmt(setLocal3, jimp.newStaticFieldRef(fieldRef3)));
                for (String constrSig : this.RTI.constructorNewInstanceSignatures(m)) {
                    InterfaceInvokeExpr invokeExpr3 = jimp.newInterfaceInvokeExpr(setLocal3, addMethodRef, StringConstant.v(String.valueOf(callSiteId) + constrSig));
                    hashChain.add(jimp.newInvokeStmt(invokeExpr3));
                }
                callSiteId++;
            }
            if (!this.RTI.methodInvokeSignatures(m).isEmpty()) {
                SootFieldRef fieldRef4 = scene.makeFieldRef(reflCallsClass, "methodInvoke", refTypeSet, true);
                Local setLocal4 = localGen.generateLocal(refTypeSet);
                hashChain.add(jimp.newAssignStmt(setLocal4, jimp.newStaticFieldRef(fieldRef4)));
                for (String methodSig : this.RTI.methodInvokeSignatures(m)) {
                    InterfaceInvokeExpr invokeExpr4 = jimp.newInterfaceInvokeExpr(setLocal4, addMethodRef, StringConstant.v(String.valueOf(callSiteId) + methodSig));
                    hashChain.add(jimp.newInvokeStmt(invokeExpr4));
                }
                callSiteId++;
            }
        }
        PatchingChain<Unit> units = body.getUnits();
        units.insertAfter((Chain<Chain<Unit>>) hashChain, (Chain<Unit>) units.getPredOf((PatchingChain<Unit>) units.getLast()));
        if (Options.v().validate()) {
            body.validate();
        }
    }

    private void addCaching() {
        ReflectionTraceInfo.Kind[] valuesCustom;
        Scene scene = Scene.v();
        BooleanType bt = BooleanType.v();
        scene.getSootClass("java.lang.reflect.Method").addField(scene.makeSootField(ALREADY_CHECKED_FIELDNAME, bt));
        scene.getSootClass("java.lang.reflect.Constructor").addField(scene.makeSootField(ALREADY_CHECKED_FIELDNAME, bt));
        scene.getSootClass("java.lang.Class").addField(scene.makeSootField(ALREADY_CHECKED_FIELDNAME, bt));
        for (ReflectionTraceInfo.Kind k : ReflectionTraceInfo.Kind.valuesCustom()) {
            addCaching(k);
        }
    }

    private void addCaching(ReflectionTraceInfo.Kind kind) {
        SootClass c;
        String methodName;
        Scene scene = Scene.v();
        switch ($SWITCH_TABLE$soot$jimple$toolkits$reflection$ReflectionTraceInfo$Kind()[kind.ordinal()]) {
            case 1:
                return;
            case 2:
                c = scene.getSootClass("java.lang.Class");
                methodName = "knownClassNewInstance";
                break;
            case 3:
                c = scene.getSootClass("java.lang.reflect.Constructor");
                methodName = "knownConstructorNewInstance";
                break;
            case 4:
                c = scene.getSootClass("java.lang.reflect.Method");
                methodName = "knownMethodInvoke";
                break;
            default:
                throw new IllegalStateException("unknown kind: " + kind);
        }
        SootMethod m = scene.getSootClass("soot.rtlib.tamiflex.ReflectiveCalls").getMethodByName(methodName);
        JimpleBody body = (JimpleBody) m.retrieveActiveBody();
        Chain<Unit> units = body.getUnits();
        Unit firstStmt = units.getPredOf(body.getFirstNonIdentityStmt());
        Chain<Unit> newUnits = new HashChain<>();
        BooleanType bt = BooleanType.v();
        Jimple jimp = Jimple.v();
        InstanceFieldRef fieldRef = jimp.newInstanceFieldRef(body.getParameterLocal(m.getParameterCount() - 1), scene.makeFieldRef(c, ALREADY_CHECKED_FIELDNAME, bt, false));
        LocalGenerator localGen = scene.createLocalGenerator(body);
        Local alreadyCheckedLocal = localGen.generateLocal(bt);
        newUnits.add(jimp.newAssignStmt(alreadyCheckedLocal, fieldRef));
        Stmt jumpTarget = jimp.newNopStmt();
        newUnits.add(jimp.newIfStmt(jimp.newEqExpr(alreadyCheckedLocal, IntConstant.v(0)), jumpTarget));
        newUnits.add(jimp.newReturnVoidStmt());
        newUnits.add(jumpTarget);
        newUnits.add(jimp.newAssignStmt(jimp.newInstanceFieldRef(body.getParameterLocal(m.getParameterCount() - 1), scene.makeFieldRef(c, ALREADY_CHECKED_FIELDNAME, bt, false)), IntConstant.v(1)));
        units.insertAfter((Chain<Chain<Unit>>) newUnits, (Chain<Unit>) firstStmt);
        if (Options.v().validate()) {
            body.validate();
        }
    }

    private void inlineRelectiveCalls(SootMethod m, Set<String> targets, ReflectionTraceInfo.Kind callKind) {
        Body b = m.retrieveActiveBody();
        Chain<Unit> units = b.getUnits();
        Scene scene = Scene.v();
        LocalGenerator localGen = scene.createLocalGenerator(b);
        Jimple jimp = Jimple.v();
        Iterator<Unit> iter = units.snapshotIterator();
        while (iter.hasNext()) {
            Stmt s = (Stmt) iter.next();
            Chain<Unit> newUnits = new HashChain<>();
            if (s.containsInvokeExpr()) {
                InvokeExpr ie = s.getInvokeExpr();
                boolean found = false;
                Type fieldSetGetType = null;
                if (callKind == ReflectionTraceInfo.Kind.ClassForName && ("<java.lang.Class: java.lang.Class forName(java.lang.String)>".equals(ie.getMethodRef().getSignature()) || "<java.lang.Class: java.lang.Class forName(java.lang.String,boolean,java.lang.ClassLoader)>".equals(ie.getMethodRef().getSignature()))) {
                    found = true;
                    Value classNameValue = ie.getArg(0);
                    newUnits.add(jimp.newInvokeStmt(jimp.newStaticInvokeExpr(scene.getMethod("<soot.rtlib.tamiflex.ReflectiveCalls: void knownClassForName(int,java.lang.String)>").makeRef(), IntConstant.v(this.callSiteId), classNameValue)));
                } else if (callKind == ReflectionTraceInfo.Kind.ClassNewInstance && "<java.lang.Class: java.lang.Object newInstance()>".equals(ie.getMethodRef().getSignature())) {
                    found = true;
                    Local classLocal = (Local) ((InstanceInvokeExpr) ie).getBase();
                    newUnits.add(jimp.newInvokeStmt(jimp.newStaticInvokeExpr(scene.getMethod("<soot.rtlib.tamiflex.ReflectiveCalls: void knownClassNewInstance(int,java.lang.Class)>").makeRef(), IntConstant.v(this.callSiteId), classLocal)));
                } else if (callKind == ReflectionTraceInfo.Kind.ConstructorNewInstance && "<java.lang.reflect.Constructor: java.lang.Object newInstance(java.lang.Object[])>".equals(ie.getMethodRef().getSignature())) {
                    found = true;
                    Local constrLocal = (Local) ((InstanceInvokeExpr) ie).getBase();
                    newUnits.add(jimp.newInvokeStmt(jimp.newStaticInvokeExpr(scene.getMethod("<soot.rtlib.tamiflex.ReflectiveCalls: void knownConstructorNewInstance(int,java.lang.reflect.Constructor)>").makeRef(), IntConstant.v(this.callSiteId), constrLocal)));
                } else if (callKind == ReflectionTraceInfo.Kind.MethodInvoke && "<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>".equals(ie.getMethodRef().getSignature())) {
                    found = true;
                    Local methodLocal = (Local) ((InstanceInvokeExpr) ie).getBase();
                    Value recv = ie.getArg(0);
                    newUnits.add(jimp.newInvokeStmt(jimp.newStaticInvokeExpr(scene.getMethod("<soot.rtlib.tamiflex.ReflectiveCalls: void knownMethodInvoke(int,java.lang.Object,java.lang.reflect.Method)>").makeRef(), IntConstant.v(this.callSiteId), recv, methodLocal)));
                } else if (callKind == ReflectionTraceInfo.Kind.FieldSet) {
                    SootMethod sootMethod = ie.getMethodRef().resolve();
                    if ("java.lang.reflect.Field".equals(sootMethod.getDeclaringClass().getName()) && fieldSets.contains(sootMethod.getName())) {
                        found = true;
                        fieldSetGetType = sootMethod.getParameterType(1);
                        Value recv2 = ie.getArg(0);
                        Value field = ((InstanceInvokeExpr) ie).getBase();
                        newUnits.add(jimp.newInvokeStmt(jimp.newStaticInvokeExpr(scene.getMethod("<soot.rtlib.tamiflex.ReflectiveCalls: void knownFieldSet(int,java.lang.Object,java.lang.reflect.Field)>").makeRef(), IntConstant.v(this.callSiteId), recv2, field)));
                    }
                } else if (callKind == ReflectionTraceInfo.Kind.FieldGet) {
                    SootMethod sootMethod2 = ie.getMethodRef().resolve();
                    if ("java.lang.reflect.Field".equals(sootMethod2.getDeclaringClass().getName()) && fieldGets.contains(sootMethod2.getName())) {
                        found = true;
                        fieldSetGetType = sootMethod2.getReturnType();
                        Value recv3 = ie.getArg(0);
                        Value field2 = ((InstanceInvokeExpr) ie).getBase();
                        newUnits.add(jimp.newInvokeStmt(jimp.newStaticInvokeExpr(scene.getMethod("<soot.rtlib.tamiflex.ReflectiveCalls: void knownFieldSet(int,java.lang.Object,java.lang.reflect.Field)>").makeRef(), IntConstant.v(this.callSiteId), recv3, field2)));
                    }
                }
                if (found) {
                    NopStmt endLabel = jimp.newNopStmt();
                    for (String target : targets) {
                        NopStmt jumpTarget = jimp.newNopStmt();
                        Local predLocal = localGen.generateLocal(BooleanType.v());
                        newUnits.add(jimp.newAssignStmt(predLocal, jimp.newStaticInvokeExpr(this.UNINTERPRETED_METHOD)));
                        newUnits.add(jimp.newIfStmt(jimp.newEqExpr(IntConstant.v(0), predLocal), jumpTarget));
                        SootMethod newMethod = createNewMethod(callKind, target, fieldSetGetType);
                        List<Value> args = new LinkedList<>();
                        switch ($SWITCH_TABLE$soot$jimple$toolkits$reflection$ReflectionTraceInfo$Kind()[callKind.ordinal()]) {
                            case 1:
                            case 2:
                                break;
                            case 3:
                                args.add(ie.getArgs().get(0));
                                break;
                            case 4:
                                args.add(ie.getArgs().get(0));
                                args.add(ie.getArgs().get(1));
                                break;
                            case 5:
                                args.add(ie.getArgs().get(0));
                                args.add(ie.getArgs().get(1));
                                break;
                            case 6:
                                args.add(ie.getArgs().get(0));
                                break;
                            default:
                                throw new IllegalStateException();
                        }
                        Local retLocal = localGen.generateLocal(newMethod.getReturnType());
                        newUnits.add(jimp.newAssignStmt(retLocal, jimp.newStaticInvokeExpr(newMethod.makeRef(), args)));
                        if (s instanceof AssignStmt) {
                            AssignStmt assignStmt = (AssignStmt) s;
                            newUnits.add(jimp.newAssignStmt(assignStmt.getLeftOp(), retLocal));
                        }
                        GotoStmt gotoStmt = jimp.newGotoStmt(endLabel);
                        newUnits.add(gotoStmt);
                        newUnits.add(jumpTarget);
                    }
                    Unit end = newUnits.getLast();
                    units.insertAfter((Chain<Chain<Unit>>) newUnits, (Chain<Unit>) s);
                    units.remove(s);
                    units.insertAfter(s, (Stmt) end);
                    units.insertAfter(endLabel, (NopStmt) s);
                } else {
                    continue;
                }
            }
        }
        this.callSiteId++;
    }

    private SootMethod createNewMethod(ReflectionTraceInfo.Kind callKind, String target, Type fieldSetGetType) {
        Type returnType;
        Local freshLocal;
        Value replacement;
        FieldRef fieldRef;
        FieldRef fieldRef2;
        InvokeExpr invokeExpr;
        List<Type> parameterTypes = new LinkedList<>();
        switch ($SWITCH_TABLE$soot$jimple$toolkits$reflection$ReflectionTraceInfo$Kind()[callKind.ordinal()]) {
            case 1:
                returnType = RefType.v("java.lang.Class");
                break;
            case 2:
                returnType = Scene.v().getObjectType();
                break;
            case 3:
                Type returnType2 = Scene.v().getObjectType();
                parameterTypes.add(ArrayType.v(returnType2, 1));
                returnType = returnType2;
                break;
            case 4:
                Type returnType3 = Scene.v().getObjectType();
                parameterTypes.add(returnType3);
                parameterTypes.add(ArrayType.v(returnType3, 1));
                returnType = returnType3;
                break;
            case 5:
                returnType = VoidType.v();
                parameterTypes.add(Scene.v().getObjectType());
                parameterTypes.add(fieldSetGetType);
                break;
            case 6:
                returnType = fieldSetGetType;
                parameterTypes.add(Scene.v().getObjectType());
                break;
            default:
                throw new IllegalStateException();
        }
        Jimple jimp = Jimple.v();
        Scene scene = Scene.v();
        StringBuilder sb = new StringBuilder("reflectiveCall");
        int i = this.callNum;
        this.callNum = i + 1;
        SootMethod newMethod = scene.makeSootMethod(sb.append(i).toString(), parameterTypes, returnType, 9);
        Body newBody = jimp.newBody(newMethod);
        newMethod.setActiveBody(newBody);
        this.reflectiveCallsClass.addMethod(newMethod);
        PatchingChain<Unit> newUnits = newBody.getUnits();
        LocalGenerator localGen = scene.createLocalGenerator(newBody);
        Local[] paramLocals = null;
        switch ($SWITCH_TABLE$soot$jimple$toolkits$reflection$ReflectionTraceInfo$Kind()[callKind.ordinal()]) {
            case 1:
                freshLocal = localGen.generateLocal(RefType.v("java.lang.Class"));
                replacement = ClassConstant.v(target.replace('.', '/'));
                break;
            case 2:
                RefType targetType = RefType.v(target);
                freshLocal = localGen.generateLocal(targetType);
                replacement = jimp.newNewExpr(targetType);
                break;
            case 3:
                SootMethod constructor = scene.getMethod(target);
                paramLocals = new Local[constructor.getParameterCount()];
                if (constructor.getParameterCount() > 0) {
                    ArrayType arrayType = ArrayType.v(Scene.v().getObjectType(), 1);
                    Local argArrayLocal = localGen.generateLocal(arrayType);
                    newUnits.add((PatchingChain<Unit>) jimp.newIdentityStmt(argArrayLocal, jimp.newParameterRef(arrayType, 0)));
                    int i2 = 0;
                    for (Type paramType : constructor.getParameterTypes()) {
                        paramLocals[i2] = localGen.generateLocal(paramType);
                        unboxParameter(argArrayLocal, i2, paramLocals, paramType, newUnits, localGen);
                        i2++;
                    }
                }
                RefType targetType2 = constructor.getDeclaringClass().getType();
                freshLocal = localGen.generateLocal(targetType2);
                replacement = jimp.newNewExpr(targetType2);
                break;
            case 4:
                SootMethod method = scene.getMethod(target);
                RefType objectType = Scene.v().getObjectType();
                Local recvObject = localGen.generateLocal(objectType);
                newUnits.add((PatchingChain<Unit>) jimp.newIdentityStmt(recvObject, jimp.newParameterRef(objectType, 0)));
                paramLocals = new Local[method.getParameterCount()];
                if (method.getParameterCount() > 0) {
                    ArrayType arrayType2 = ArrayType.v(Scene.v().getObjectType(), 1);
                    Local argArrayLocal2 = localGen.generateLocal(arrayType2);
                    newUnits.add((PatchingChain<Unit>) jimp.newIdentityStmt(argArrayLocal2, jimp.newParameterRef(arrayType2, 1)));
                    int i3 = 0;
                    for (Type paramType2 : method.getParameterTypes()) {
                        paramLocals[i3] = localGen.generateLocal(paramType2);
                        unboxParameter(argArrayLocal2, i3, paramLocals, paramType2, newUnits, localGen);
                        i3++;
                    }
                }
                freshLocal = localGen.generateLocal(method.getDeclaringClass().getType());
                replacement = jimp.newCastExpr(recvObject, method.getDeclaringClass().getType());
                break;
            case 5:
            case 6:
                RefType objectType2 = Scene.v().getObjectType();
                Local recvObject2 = localGen.generateLocal(objectType2);
                newUnits.add((PatchingChain<Unit>) jimp.newIdentityStmt(recvObject2, jimp.newParameterRef(objectType2, 0)));
                RefType fieldClassType = scene.getField(target).getDeclaringClass().getType();
                freshLocal = localGen.generateLocal(fieldClassType);
                replacement = jimp.newCastExpr(recvObject2, fieldClassType);
                break;
            default:
                throw new InternalError("Unknown kind of reflective call " + callKind);
        }
        AssignStmt replStmt = jimp.newAssignStmt(freshLocal, replacement);
        newUnits.add((PatchingChain<Unit>) replStmt);
        Local retLocal = localGen.generateLocal(returnType);
        switch ($SWITCH_TABLE$soot$jimple$toolkits$reflection$ReflectionTraceInfo$Kind()[callKind.ordinal()]) {
            case 1:
                newUnits.add((PatchingChain<Unit>) jimp.newAssignStmt(retLocal, freshLocal));
                break;
            case 2:
                newUnits.add((PatchingChain<Unit>) jimp.newInvokeStmt(jimp.newSpecialInvokeExpr(freshLocal, scene.makeMethodRef(scene.getSootClass(target), "<init>", Collections.emptyList(), VoidType.v(), false))));
                newUnits.add((PatchingChain<Unit>) jimp.newAssignStmt(retLocal, freshLocal));
                break;
            case 3:
                newUnits.add((PatchingChain<Unit>) jimp.newInvokeStmt(jimp.newSpecialInvokeExpr(freshLocal, scene.getMethod(target).makeRef(), Arrays.asList(paramLocals))));
                newUnits.add((PatchingChain<Unit>) jimp.newAssignStmt(retLocal, freshLocal));
                break;
            case 4:
                SootMethod method2 = scene.getMethod(target);
                if (method2.isStatic()) {
                    invokeExpr = jimp.newStaticInvokeExpr(method2.makeRef(), Arrays.asList(paramLocals));
                } else {
                    invokeExpr = jimp.newVirtualInvokeExpr(freshLocal, method2.makeRef(), Arrays.asList(paramLocals));
                }
                if (VoidType.v().equals(method2.getReturnType())) {
                    newUnits.add((PatchingChain<Unit>) jimp.newInvokeStmt(invokeExpr));
                    newUnits.add((PatchingChain<Unit>) jimp.newAssignStmt(retLocal, NullConstant.v()));
                    break;
                } else {
                    newUnits.add((PatchingChain<Unit>) jimp.newAssignStmt(retLocal, invokeExpr));
                    break;
                }
            case 5:
                Local value = localGen.generateLocal(fieldSetGetType);
                newUnits.insertBeforeNoRedirect(jimp.newIdentityStmt(value, jimp.newParameterRef(fieldSetGetType, 1)), replStmt);
                SootField field = scene.getField(target);
                Local boxedOrCasted = localGen.generateLocal(field.getType());
                insertCastOrUnboxingCode(boxedOrCasted, value, newUnits);
                if (field.isStatic()) {
                    fieldRef2 = jimp.newStaticFieldRef(field.makeRef());
                } else {
                    fieldRef2 = jimp.newInstanceFieldRef(freshLocal, field.makeRef());
                }
                newUnits.add((PatchingChain<Unit>) jimp.newAssignStmt(fieldRef2, boxedOrCasted));
                break;
            case 6:
                SootField field2 = scene.getField(target);
                Local value2 = localGen.generateLocal(field2.getType());
                if (field2.isStatic()) {
                    fieldRef = jimp.newStaticFieldRef(field2.makeRef());
                } else {
                    fieldRef = jimp.newInstanceFieldRef(freshLocal, field2.makeRef());
                }
                newUnits.add((PatchingChain<Unit>) jimp.newAssignStmt(value2, fieldRef));
                insertCastOrBoxingCode(retLocal, value2, newUnits);
                break;
        }
        if (!VoidType.v().equals(returnType)) {
            newUnits.add((PatchingChain<Unit>) jimp.newReturnStmt(retLocal));
        }
        if (Options.v().validate()) {
            newBody.validate();
        }
        cleanup(newBody);
        return newMethod;
    }

    private void insertCastOrUnboxingCode(Local lhs, Local rhs, Chain<Unit> newUnits) {
        Type lhsType = lhs.getType();
        if (lhsType instanceof PrimType) {
            Type rhsType = rhs.getType();
            if (rhsType instanceof PrimType) {
                newUnits.add(Jimple.v().newAssignStmt(lhs, Jimple.v().newCastExpr(rhs, lhsType)));
                return;
            }
            RefType boxedType = (RefType) rhsType;
            SootMethodRef ref = Scene.v().makeMethodRef(boxedType.getSootClass(), String.valueOf(lhsType.toString()) + XmlConstants.Attributes.value, Collections.emptyList(), lhsType, false);
            newUnits.add(Jimple.v().newAssignStmt(lhs, Jimple.v().newVirtualInvokeExpr(rhs, ref)));
        }
    }

    private void insertCastOrBoxingCode(Local lhs, Local rhs, Chain<Unit> newUnits) {
        Type lhsType = lhs.getType();
        if (lhsType instanceof RefLikeType) {
            Type rhsType = rhs.getType();
            if (rhsType instanceof RefLikeType) {
                newUnits.add(Jimple.v().newAssignStmt(lhs, Jimple.v().newCastExpr(rhs, lhsType)));
                return;
            }
            RefType boxedType = ((PrimType) rhsType).boxedType();
            SootMethodRef ref = Scene.v().makeMethodRef(boxedType.getSootClass(), "valueOf", Collections.singletonList(rhsType), boxedType, true);
            newUnits.add(Jimple.v().newAssignStmt(lhs, Jimple.v().newStaticInvokeExpr(ref, rhs)));
        }
    }

    private void unboxParameter(Local argsArrayLocal, int paramIndex, Local[] paramLocals, Type paramType, Chain<Unit> newUnits, LocalGenerator localGen) {
        Jimple jimp = Jimple.v();
        if (paramType instanceof PrimType) {
            RefType boxedType = ((PrimType) paramType).boxedType();
            Local boxedLocal = localGen.generateLocal(Scene.v().getObjectType());
            newUnits.add(jimp.newAssignStmt(boxedLocal, jimp.newArrayRef(argsArrayLocal, IntConstant.v(paramIndex))));
            Local castedLocal = localGen.generateLocal(boxedType);
            newUnits.add(jimp.newAssignStmt(castedLocal, jimp.newCastExpr(boxedLocal, boxedType)));
            newUnits.add(jimp.newAssignStmt(paramLocals[paramIndex], jimp.newVirtualInvokeExpr(castedLocal, Scene.v().makeMethodRef(boxedType.getSootClass(), paramType + XmlConstants.Attributes.value, Collections.emptyList(), paramType, false))));
            return;
        }
        Local boxedLocal2 = localGen.generateLocal(Scene.v().getObjectType());
        newUnits.add(jimp.newAssignStmt(boxedLocal2, jimp.newArrayRef(argsArrayLocal, IntConstant.v(paramIndex))));
        Local castedLocal2 = localGen.generateLocal(paramType);
        newUnits.add(jimp.newAssignStmt(castedLocal2, jimp.newCastExpr(boxedLocal2, paramType)));
        newUnits.add(jimp.newAssignStmt(paramLocals[paramIndex], castedLocal2));
    }
}
