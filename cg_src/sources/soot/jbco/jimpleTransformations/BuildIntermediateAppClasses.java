package soot.jbco.jimpleTransformations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import soot.Body;
import soot.FastHierarchy;
import soot.Hierarchy;
import soot.JavaMethods;
import soot.Local;
import soot.PatchingChain;
import soot.PrimType;
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
import soot.dotnet.members.DotnetMethod;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.BodyBuilder;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NullConstant;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.ThisRef;
import soot.options.Options;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jbco/jimpleTransformations/BuildIntermediateAppClasses.class */
public class BuildIntermediateAppClasses extends SceneTransformer implements IJbcoTransform {
    private static int newclasses = 0;
    private static int newmethods = 0;
    public static String[] dependancies = {"wjtp.jbco_bapibm"};
    public static String name = "wjtp.jbco_bapibm";

    @Override // soot.jbco.IJbcoTransform
    public void outputSummary() {
        out.println("New buffer classes created: " + newclasses);
        out.println("New buffer class methods created: " + newmethods);
    }

    @Override // soot.jbco.IJbcoTransform
    public String[] getDependencies() {
        return dependancies;
    }

    @Override // soot.jbco.IJbcoTransform
    public String getName() {
        return name;
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> options) {
        SootMethod newSuperInit;
        if (output) {
            out.println("Building Intermediate Classes...");
        }
        BodyBuilder.retrieveAllBodies();
        Iterator<SootClass> it = Scene.v().getApplicationClasses().snapshotIterator();
        while (it.hasNext()) {
            List<SootMethod> initMethodsToRewrite = new ArrayList<>();
            Map<String, SootMethod> methodsToAdd = new HashMap<>();
            SootClass sc = it.next();
            SootClass originalSuperclass = sc.getSuperclass();
            if (output) {
                out.println("Processing " + sc.getName() + " with super " + originalSuperclass.getName());
            }
            Iterator<SootMethod> methodIterator = sc.methodIterator();
            while (methodIterator.hasNext()) {
                SootMethod method = methodIterator.next();
                if (method.isConcrete()) {
                    try {
                        method.getActiveBody();
                    } catch (Exception e) {
                        if (method.retrieveActiveBody() == null) {
                            throw new RuntimeException(String.valueOf(method.getSignature()) + " has no body. This was not expected dude.");
                        }
                    }
                    String subSig = method.getSubSignature();
                    if (!subSig.equals(Options.v().src_prec() == 7 ? DotnetMethod.MAIN_METHOD_SIGNATURE : JavaMethods.SIG_MAIN) || !method.isPublic() || !method.isStatic()) {
                        if (subSig.indexOf("init>(") > 0) {
                            if (subSig.startsWith("void <init>(")) {
                                initMethodsToRewrite.add(method);
                            }
                        } else {
                            Scene.v().releaseActiveHierarchy();
                            findAccessibleInSuperClassesBySubSig(sc, subSig).ifPresent(m -> {
                                methodsToAdd.put(subSig, m);
                            });
                        }
                    }
                }
            }
            if (methodsToAdd.size() > 0) {
                String fullName = ClassRenamer.v().getOrAddNewName(ClassRenamer.getPackageName(sc.getName()), null);
                if (output) {
                    out.println("\tBuilding " + fullName);
                }
                SootClass mediatingClass = new SootClass(fullName, sc.getModifiers() & (-17));
                Main.IntermediateAppClasses.add(mediatingClass);
                mediatingClass.setSuperclass(originalSuperclass);
                Scene.v().addClass(mediatingClass);
                mediatingClass.setApplicationClass();
                mediatingClass.setInScene(true);
                ThisRef thisRef = new ThisRef(mediatingClass.getType());
                for (String subSig2 : methodsToAdd.keySet()) {
                    SootMethod originalSuperclassMethod = methodsToAdd.get(subSig2);
                    List<Type> paramTypes = originalSuperclassMethod.getParameterTypes();
                    Type returnType = originalSuperclassMethod.getReturnType();
                    List<SootClass> exceptions = originalSuperclassMethod.getExceptions();
                    int modifiers = originalSuperclassMethod.getModifiers() & (-1025) & (-257);
                    String newMethodName = MethodRenamer.v().getNewName();
                    SootMethod newMethod = Scene.v().makeSootMethod(newMethodName, paramTypes, returnType, modifiers, exceptions);
                    mediatingClass.addMethod(newMethod);
                    Body body = Jimple.v().newBody(newMethod);
                    newMethod.setActiveBody(body);
                    Chain<Local> locals = body.getLocals();
                    PatchingChain<Unit> units = body.getUnits();
                    BodyBuilder.buildThisLocal(units, thisRef, locals);
                    BodyBuilder.buildParameterLocals(units, locals, paramTypes);
                    if (returnType instanceof VoidType) {
                        units.add((PatchingChain<Unit>) Jimple.v().newReturnVoidStmt());
                    } else if (returnType instanceof PrimType) {
                        units.add((PatchingChain<Unit>) Jimple.v().newReturnStmt(IntConstant.v(0)));
                    } else {
                        units.add((PatchingChain<Unit>) Jimple.v().newReturnStmt(NullConstant.v()));
                    }
                    newmethods++;
                    SootMethod newMethod2 = Scene.v().makeSootMethod(originalSuperclassMethod.getName(), paramTypes, returnType, modifiers, exceptions);
                    mediatingClass.addMethod(newMethod2);
                    Body body2 = Jimple.v().newBody(newMethod2);
                    newMethod2.setActiveBody(body2);
                    Chain<Local> locals2 = body2.getLocals();
                    PatchingChain<Unit> units2 = body2.getUnits();
                    Local ths = BodyBuilder.buildThisLocal(units2, thisRef, locals2);
                    List<Local> args = BodyBuilder.buildParameterLocals(units2, locals2, paramTypes);
                    SootMethodRef superclassMethodRef = originalSuperclassMethod.makeRef();
                    if (returnType instanceof VoidType) {
                        units2.add((PatchingChain<Unit>) Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(ths, superclassMethodRef, args)));
                        units2.add((PatchingChain<Unit>) Jimple.v().newReturnVoidStmt());
                    } else {
                        Local loc = Jimple.v().newLocal("retValue", returnType);
                        body2.getLocals().add(loc);
                        units2.add((PatchingChain<Unit>) Jimple.v().newAssignStmt(loc, Jimple.v().newSpecialInvokeExpr(ths, superclassMethodRef, args)));
                        units2.add((PatchingChain<Unit>) Jimple.v().newReturnStmt(loc));
                    }
                    newmethods++;
                }
                sc.setSuperclass(mediatingClass);
                int i = initMethodsToRewrite.size();
                while (true) {
                    int i2 = i;
                    i--;
                    if (i2 <= 0) {
                        break;
                    }
                    SootMethod im = initMethodsToRewrite.remove(i);
                    Body b = im.getActiveBody();
                    Local thisLocal = b.getThisLocal();
                    Iterator<Unit> uIt = b.getUnits().snapshotIterator();
                    while (uIt.hasNext()) {
                        for (ValueBox valueBox : uIt.next().getUseBoxes()) {
                            Value v = valueBox.getValue();
                            if (v instanceof SpecialInvokeExpr) {
                                SpecialInvokeExpr sie = (SpecialInvokeExpr) v;
                                SootMethodRef smr = sie.getMethodRef();
                                if (sie.getBase().equivTo(thisLocal) && smr.declaringClass().getName().equals(originalSuperclass.getName()) && smr.getSubSignature().getString().startsWith("void <init>")) {
                                    if (!mediatingClass.declaresMethod("<init>", smr.parameterTypes())) {
                                        List<Type> paramTypes2 = smr.parameterTypes();
                                        newSuperInit = Scene.v().makeSootMethod("<init>", paramTypes2, smr.returnType());
                                        mediatingClass.addMethod(newSuperInit);
                                        JimpleBody body3 = Jimple.v().newBody(newSuperInit);
                                        newSuperInit.setActiveBody(body3);
                                        PatchingChain<Unit> initUnits = body3.getUnits();
                                        Collection<Local> locals3 = body3.getLocals();
                                        initUnits.add((PatchingChain<Unit>) Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(BodyBuilder.buildThisLocal(initUnits, thisRef, locals3), smr, BodyBuilder.buildParameterLocals(initUnits, locals3, paramTypes2))));
                                        initUnits.add((PatchingChain<Unit>) Jimple.v().newReturnVoidStmt());
                                    } else {
                                        newSuperInit = mediatingClass.getMethod("<init>", smr.parameterTypes());
                                    }
                                    sie.setMethodRef(newSuperInit.makeRef());
                                }
                            }
                        }
                    }
                }
            }
        }
        newclasses = Main.IntermediateAppClasses.size();
        Scene.v().releaseActiveHierarchy();
        Scene.v().getActiveHierarchy();
        Scene.v().setFastHierarchy(new FastHierarchy());
    }

    private Optional<SootMethod> findAccessibleInSuperClassesBySubSig(SootClass base, String subSig) {
        Hierarchy hierarchy = Scene.v().getActiveHierarchy();
        for (SootClass superClass : hierarchy.getSuperclassesOfIncluding(base.getSuperclass())) {
            if (superClass.isLibraryClass() && superClass.declaresMethod(subSig)) {
                SootMethod method = superClass.getMethod(subSig);
                if (hierarchy.isVisible(base, method)) {
                    return Optional.of(method);
                }
            }
        }
        return Optional.empty();
    }
}
