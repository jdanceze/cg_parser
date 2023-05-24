package soot.jbco.jimpleTransformations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.ByteOrderMark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FastHierarchy;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.PatchingChain;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.jbco.IJbcoTransform;
import soot.jbco.util.BodyBuilder;
import soot.jbco.util.Rand;
import soot.jimple.AssignStmt;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jbco/jimpleTransformations/LibraryMethodWrappersBuilder.class */
public class LibraryMethodWrappersBuilder extends SceneTransformer implements IJbcoTransform {
    private static final Logger logger = LoggerFactory.getLogger(LibraryMethodWrappersBuilder.class);
    public static final String name = "wjtp.jbco_blbc";
    public static final String[] dependencies = {name};
    private static final Map<SootClass, Map<SootMethod, SootMethodRef>> libClassesToMethods = new HashMap();
    public static List<SootMethod> builtByMe = new ArrayList();
    private int newmethods = 0;
    private int methodcalls = 0;

    @Override // soot.jbco.IJbcoTransform
    public String[] getDependencies() {
        return (String[]) Arrays.copyOf(dependencies, dependencies.length);
    }

    @Override // soot.jbco.IJbcoTransform
    public String getName() {
        return name;
    }

    @Override // soot.jbco.IJbcoTransform
    public void outputSummary() {
        logger.info("Created {} new methods. Replaced {} method calls.", Integer.valueOf(this.newmethods), Integer.valueOf(this.methodcalls));
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> options) {
        Body body;
        InvokeExpr invokeExpr;
        SootMethod invokedMethod;
        if (isVerbose()) {
            logger.info("Building Library Wrapper Methods...");
        }
        BodyBuilder.retrieveAllBodies();
        Iterator<SootClass> applicationClassesIterator = Scene.v().getApplicationClasses().snapshotIterator();
        while (applicationClassesIterator.hasNext()) {
            SootClass applicationClass = applicationClassesIterator.next();
            if (isVerbose()) {
                logger.info("\tProcessing class {}", applicationClass.getName());
            }
            List<SootMethod> methods = new ArrayList<>(applicationClass.getMethods());
            for (SootMethod method : methods) {
                if (method.isConcrete() && !builtByMe.contains(method) && (body = getBodySafely(method)) != null) {
                    int localName = 0;
                    Unit first = getFirstNotIdentityStmt(body);
                    Iterator<Unit> unitIterator = body.getUnits().snapshotIterator();
                    while (unitIterator.hasNext()) {
                        Unit unit = unitIterator.next();
                        for (ValueBox valueBox : unit.getUseBoxes()) {
                            Value value = valueBox.getValue();
                            if ((value instanceof InvokeExpr) && !(value instanceof SpecialInvokeExpr) && (invokedMethod = getMethodSafely((invokeExpr = (InvokeExpr) value))) != null) {
                                SootMethodRef invokedMethodRef = getNewMethodRef(invokedMethod);
                                if (invokedMethodRef == null) {
                                    invokedMethodRef = buildNewMethod(applicationClass, invokedMethod, invokeExpr);
                                    setNewMethodRef(invokedMethod, invokedMethodRef);
                                    this.newmethods++;
                                }
                                if (isVerbose()) {
                                    logger.info("\t\t\tChanging {} to {}\tUnit: ", invokedMethod.getSignature(), invokedMethodRef.getSignature(), unit);
                                }
                                List<Value> args = invokeExpr.getArgs();
                                List<Type> parameterTypes = invokedMethodRef.parameterTypes();
                                int argsCount = args.size();
                                int paramCount = parameterTypes.size();
                                if ((invokeExpr instanceof InstanceInvokeExpr) || (invokeExpr instanceof StaticInvokeExpr)) {
                                    if (invokeExpr instanceof InstanceInvokeExpr) {
                                        argsCount++;
                                        args.add(((InstanceInvokeExpr) invokeExpr).getBase());
                                    }
                                    while (argsCount < paramCount) {
                                        Type pType = parameterTypes.get(argsCount);
                                        int i = localName;
                                        localName++;
                                        Local newLocal = Jimple.v().newLocal("newLocal" + i, pType);
                                        body.getLocals().add(newLocal);
                                        body.getUnits().insertBeforeNoRedirect(Jimple.v().newAssignStmt(newLocal, getConstantType(pType)), (AssignStmt) first);
                                        args.add(newLocal);
                                        argsCount++;
                                    }
                                    valueBox.setValue(Jimple.v().newStaticInvokeExpr(invokedMethodRef, args));
                                }
                                this.methodcalls++;
                            }
                        }
                    }
                }
            }
        }
        Scene.v().releaseActiveHierarchy();
        Scene.v().setFastHierarchy(new FastHierarchy());
    }

    private SootMethodRef getNewMethodRef(SootMethod method) {
        Map<SootMethod, SootMethodRef> methods = libClassesToMethods.computeIfAbsent(method.getDeclaringClass(), key -> {
            return new HashMap();
        });
        return methods.get(method);
    }

    private void setNewMethodRef(SootMethod sm, SootMethodRef smr) {
        Map<SootMethod, SootMethodRef> methods = libClassesToMethods.computeIfAbsent(sm.getDeclaringClass(), key -> {
            return new HashMap();
        });
        methods.put(sm, smr);
    }

    private SootMethodRef buildNewMethod(SootClass fromC, SootMethod sm, InvokeExpr origIE) {
        SootClass randomClass;
        String methodNewName;
        List<SootClass> availableClasses = getVisibleApplicationClasses(sm);
        int classCount = availableClasses.size();
        if (classCount == 0) {
            throw new RuntimeException("There appears to be no public non-interface Application classes!");
        }
        while (true) {
            int index = Rand.getInt(classCount);
            SootClass sootClass = availableClasses.get(index);
            randomClass = sootClass;
            if (sootClass == fromC && classCount > 1) {
                int index2 = Rand.getInt(classCount);
                randomClass = availableClasses.get(index2);
            }
            List<SootMethod> methods = randomClass.getMethods();
            int index3 = Rand.getInt(methods.size());
            SootMethod randMethod = methods.get(index3);
            methodNewName = randMethod.getName();
            if (!methodNewName.equals("<init>") && !methodNewName.equals("<clinit>")) {
                break;
            }
        }
        List<Type> smParamTypes = new ArrayList<>(sm.getParameterTypes());
        if (!sm.isStatic()) {
            smParamTypes.add(sm.getDeclaringClass().getType());
        }
        int extraParams = 0;
        if (randomClass.declaresMethod(methodNewName, smParamTypes)) {
            int rtmp = Rand.getInt(classCount + 7);
            if (rtmp >= classCount) {
                smParamTypes.add(getPrimType(rtmp - classCount));
            } else {
                smParamTypes.add(availableClasses.get(rtmp).getType());
            }
            extraParams = 0 + 1;
        }
        int mods = (sm.getModifiers() | 8 | 1) & 64511 & ByteOrderMark.UTF_BOM & 65503;
        SootMethod newMethod = Scene.v().makeSootMethod(methodNewName, smParamTypes, sm.getReturnType(), mods);
        randomClass.addMethod(newMethod);
        JimpleBody body = Jimple.v().newBody(newMethod);
        newMethod.setActiveBody(body);
        Chain<Local> locals = body.getLocals();
        PatchingChain<Unit> units = body.getUnits();
        List<Local> args = BodyBuilder.buildParameterLocals(units, locals, smParamTypes);
        while (true) {
            int i = extraParams;
            extraParams--;
            if (i <= 0) {
                break;
            }
            args.remove(args.size() - 1);
        }
        InvokeExpr ie = null;
        if (sm.isStatic()) {
            ie = Jimple.v().newStaticInvokeExpr(sm.makeRef(), args);
        } else {
            Local libObj = args.remove(args.size() - 1);
            if (origIE instanceof InterfaceInvokeExpr) {
                ie = Jimple.v().newInterfaceInvokeExpr(libObj, sm.makeRef(), args);
            } else if (origIE instanceof VirtualInvokeExpr) {
                ie = Jimple.v().newVirtualInvokeExpr(libObj, sm.makeRef(), args);
            }
        }
        if (sm.getReturnType() instanceof VoidType) {
            units.add((PatchingChain<Unit>) Jimple.v().newInvokeStmt(ie));
            units.add((PatchingChain<Unit>) Jimple.v().newReturnVoidStmt());
        } else {
            Local assign = Jimple.v().newLocal("returnValue", sm.getReturnType());
            locals.add(assign);
            units.add((PatchingChain<Unit>) Jimple.v().newAssignStmt(assign, ie));
            units.add((PatchingChain<Unit>) Jimple.v().newReturnStmt(assign));
        }
        if (isVerbose()) {
            logger.info("{} was replaced by {} which calls {}", sm.getName(), newMethod.getName(), ie);
        }
        if (units.size() < 2) {
            logger.warn("THERE AREN'T MANY UNITS IN THIS METHOD {}", units);
        }
        builtByMe.add(newMethod);
        return newMethod.makeRef();
    }

    private static Type getPrimType(int idx) {
        switch (idx) {
            case 0:
                return IntType.v();
            case 1:
                return CharType.v();
            case 2:
                return ByteType.v();
            case 3:
                return LongType.v();
            case 4:
                return BooleanType.v();
            case 5:
                return DoubleType.v();
            case 6:
                return FloatType.v();
            default:
                return IntType.v();
        }
    }

    private static Value getConstantType(Type t) {
        if (t instanceof BooleanType) {
            return IntConstant.v(Rand.getInt(1));
        }
        if (t instanceof IntType) {
            return IntConstant.v(Rand.getInt());
        }
        if (t instanceof CharType) {
            return Jimple.v().newCastExpr(IntConstant.v(Rand.getInt()), CharType.v());
        }
        if (t instanceof ByteType) {
            return Jimple.v().newCastExpr(IntConstant.v(Rand.getInt()), ByteType.v());
        }
        if (t instanceof LongType) {
            return LongConstant.v(Rand.getLong());
        }
        if (t instanceof FloatType) {
            return FloatConstant.v(Rand.getFloat());
        }
        if (t instanceof DoubleType) {
            return DoubleConstant.v(Rand.getDouble());
        }
        return Jimple.v().newCastExpr(NullConstant.v(), t);
    }

    private static Body getBodySafely(SootMethod method) {
        try {
            return method.getActiveBody();
        } catch (Exception exception) {
            logger.warn("Getting Body from SootMethod {} caused exception that was suppressed.", (Throwable) exception);
            return method.retrieveActiveBody();
        }
    }

    private static Unit getFirstNotIdentityStmt(Body body) {
        Iterator<Unit> unitIterator = body.getUnits().snapshotIterator();
        while (unitIterator.hasNext()) {
            Unit unit = unitIterator.next();
            if (!(unit instanceof IdentityStmt)) {
                return unit;
            }
        }
        logger.debug("There are no non-identity units in the method body.");
        return null;
    }

    private static SootMethod getMethodSafely(InvokeExpr invokeExpr) {
        try {
            SootMethod invokedMethod = invokeExpr.getMethod();
            if (invokedMethod == null) {
                return null;
            }
            if ("<init>".equals(invokedMethod.getName()) || "<clinit>".equals(invokedMethod.getName())) {
                logger.debug("Skipping wrapping method {} as it is constructor/initializer.", invokedMethod);
                return null;
            }
            SootClass invokedMethodClass = invokedMethod.getDeclaringClass();
            if (!invokedMethodClass.isLibraryClass()) {
                logger.debug("Skipping wrapping method {} as it is not library one.", invokedMethod);
                return null;
            } else if (invokeExpr.getMethodRef().declaringClass().isInterface() && !invokedMethodClass.isInterface()) {
                logger.debug("Skipping wrapping method {} as original code suppose to execute it on interface {} but resolved code trying to execute it on class {}", invokedMethod, invokeExpr.getMethodRef().declaringClass(), invokedMethodClass);
                return null;
            } else {
                return invokedMethod;
            }
        } catch (RuntimeException exception) {
            logger.debug("Cannot resolve method of InvokeExpr: " + invokeExpr.toString(), (Throwable) exception);
            return null;
        }
    }

    private static List<SootClass> getVisibleApplicationClasses(SootMethod visibleBy) {
        List<SootClass> result = new ArrayList<>();
        Iterator<SootClass> applicationClassesIterator = Scene.v().getApplicationClasses().snapshotIterator();
        while (applicationClassesIterator.hasNext()) {
            SootClass applicationClass = applicationClassesIterator.next();
            if (applicationClass.isConcrete() && !applicationClass.isInterface() && applicationClass.isPublic() && Scene.v().getActiveHierarchy().isVisible(applicationClass, visibleBy)) {
                result.add(applicationClass);
            }
        }
        return result;
    }
}
