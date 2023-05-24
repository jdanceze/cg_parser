package soot.jimple.infoflow.cfg;

import java.util.Collections;
import net.bytebuddy.implementation.auxiliary.TypeProxy;
import soot.Body;
import soot.IntType;
import soot.JavaBasicTypes;
import soot.JavaMethods;
import soot.Local;
import soot.LocalGenerator;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.UnitPatchingChain;
import soot.VoidType;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NullConstant;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.infoflow.android.entryPointCreators.AndroidEntryPointConstants;
import soot.jimple.infoflow.util.SystemClassHandler;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/cfg/LibraryClassPatcher.class */
public class LibraryClassPatcher {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/cfg/LibraryClassPatcher$IMessageObtainCodeInjector.class */
    public interface IMessageObtainCodeInjector {
        void injectCode(Body body, Local local);
    }

    public void patchLibraries() {
        patchHandlerImplementation();
        patchThreadImplementation();
        patchActivityImplementation();
        patchTimerImplementation();
        patchActivityGetFragmentManager();
        patchMessageObtainImplementation();
    }

    private void patchMessageObtainImplementation() {
        SootClass sc = Scene.v().getSootClassUnsafe("android.os.Message");
        if (sc == null || sc.resolvingLevel() < 2) {
            return;
        }
        sc.setLibraryClass();
        SootMethod smMessageConstructor = Scene.v().grabMethod("<android.os.Message: void <init>()>");
        if (smMessageConstructor == null) {
            return;
        }
        SootField tmp = sc.getFieldUnsafe("int what");
        if (tmp == null) {
            tmp = Scene.v().makeSootField("what", IntType.v());
            sc.addField(tmp);
        }
        final SootField fldWhat = tmp;
        SootField tmp2 = sc.getFieldUnsafe("int arg1");
        if (tmp2 == null) {
            tmp2 = Scene.v().makeSootField("arg1", IntType.v());
            sc.addField(tmp2);
        }
        final SootField fldArg1 = tmp2;
        SootField tmp3 = sc.getFieldUnsafe("int arg2");
        if (tmp3 == null) {
            tmp3 = Scene.v().makeSootField("arg2", IntType.v());
            sc.addField(tmp3);
        }
        final SootField fldArg2 = tmp3;
        SootField tmp4 = sc.getFieldUnsafe("java.lang.Object obj");
        if (tmp4 == null) {
            tmp4 = Scene.v().makeSootField("obj", Scene.v().getObjectType());
            sc.addField(tmp4);
        }
        final SootField fldObj = tmp4;
        SystemClassHandler systemClassHandler = SystemClassHandler.v();
        SootMethod smObtain1 = sc.getMethodUnsafe("android.os.Message obtain(android.os.Handler,int)");
        if (smObtain1 != null && (!smObtain1.hasActiveBody() || systemClassHandler.isStubImplementation(smObtain1.getActiveBody()))) {
            generateMessageObtainMethod(smObtain1, new IMessageObtainCodeInjector() { // from class: soot.jimple.infoflow.cfg.LibraryClassPatcher.1
                @Override // soot.jimple.infoflow.cfg.LibraryClassPatcher.IMessageObtainCodeInjector
                public void injectCode(Body body, Local messageLocal) {
                    body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(messageLocal, fldWhat.makeRef()), body.getParameterLocal(1)));
                }
            });
        }
        SootMethod smObtain2 = sc.getMethodUnsafe("android.os.Message obtain(android.os.Handler,int,int,int,java.lang.Object)");
        if (smObtain2 != null && (!smObtain2.hasActiveBody() || systemClassHandler.isStubImplementation(smObtain2.getActiveBody()))) {
            generateMessageObtainMethod(smObtain2, new IMessageObtainCodeInjector() { // from class: soot.jimple.infoflow.cfg.LibraryClassPatcher.2
                @Override // soot.jimple.infoflow.cfg.LibraryClassPatcher.IMessageObtainCodeInjector
                public void injectCode(Body body, Local messageLocal) {
                    body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(messageLocal, fldWhat.makeRef()), body.getParameterLocal(1)));
                    body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(messageLocal, fldArg1.makeRef()), body.getParameterLocal(2)));
                    body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(messageLocal, fldArg2.makeRef()), body.getParameterLocal(3)));
                    body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(messageLocal, fldObj.makeRef()), body.getParameterLocal(4)));
                }
            });
        }
        SootMethod smObtain3 = sc.getMethodUnsafe("android.os.Message obtain(android.os.Handler,int,int,int)");
        if (smObtain3 != null && (!smObtain3.hasActiveBody() || systemClassHandler.isStubImplementation(smObtain3.getActiveBody()))) {
            generateMessageObtainMethod(smObtain3, new IMessageObtainCodeInjector() { // from class: soot.jimple.infoflow.cfg.LibraryClassPatcher.3
                @Override // soot.jimple.infoflow.cfg.LibraryClassPatcher.IMessageObtainCodeInjector
                public void injectCode(Body body, Local messageLocal) {
                    body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(messageLocal, fldWhat.makeRef()), body.getParameterLocal(1)));
                    body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(messageLocal, fldArg1.makeRef()), body.getParameterLocal(2)));
                    body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(messageLocal, fldArg2.makeRef()), body.getParameterLocal(3)));
                }
            });
        }
        SootMethod smObtain4 = sc.getMethodUnsafe("android.os.Message obtain(android.os.Handler,int,java.lang.Object)");
        if (smObtain4 != null) {
            if (!smObtain4.hasActiveBody() || systemClassHandler.isStubImplementation(smObtain4.getActiveBody())) {
                generateMessageObtainMethod(smObtain4, new IMessageObtainCodeInjector() { // from class: soot.jimple.infoflow.cfg.LibraryClassPatcher.4
                    @Override // soot.jimple.infoflow.cfg.LibraryClassPatcher.IMessageObtainCodeInjector
                    public void injectCode(Body body, Local messageLocal) {
                        body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(messageLocal, fldWhat.makeRef()), body.getParameterLocal(1)));
                        body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(messageLocal, fldObj.makeRef()), body.getParameterLocal(2)));
                    }
                });
            }
        }
    }

    private void generateMessageObtainMethod(SootMethod sm, IMessageObtainCodeInjector injector) {
        RefType tpMessage = RefType.v("android.os.Message");
        sm.getDeclaringClass().setLibraryClass();
        sm.setPhantom(false);
        sm.addTag(new FlowDroidEssentialMethodTag());
        JimpleBody body = Jimple.v().newBody(sm);
        sm.setActiveBody(body);
        body.insertIdentityStmts();
        SootMethod smMessageConstructor = Scene.v().grabMethod("<android.os.Message: void <init>()>");
        LocalGenerator lg = Scene.v().createLocalGenerator(body);
        Local messageLocal = lg.generateLocal(tpMessage);
        body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(messageLocal, Jimple.v().newNewExpr(tpMessage)));
        body.getUnits().add((UnitPatchingChain) Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(messageLocal, smMessageConstructor.makeRef())));
        if (injector != null) {
            injector.injectCode(body, messageLocal);
        }
        body.getUnits().add((UnitPatchingChain) Jimple.v().newReturnStmt(messageLocal));
    }

    private void patchActivityImplementation() {
        SootClass scApplicationHolder = createOrGetApplicationHolder();
        SootClass sc = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.ACTIVITYCLASS);
        if (sc == null || sc.resolvingLevel() < 2 || scApplicationHolder == null) {
            return;
        }
        sc.setLibraryClass();
        SootMethod smRun = sc.getMethodUnsafe("android.app.Application getApplication()");
        if (smRun != null) {
            if (smRun.hasActiveBody() && !SystemClassHandler.v().isStubImplementation(smRun.getActiveBody())) {
                return;
            }
            smRun.setPhantom(false);
            smRun.addTag(new FlowDroidEssentialMethodTag());
            Body b = Jimple.v().newBody(smRun);
            smRun.setActiveBody(b);
            Local thisLocal = Jimple.v().newLocal("this", sc.getType());
            b.getLocals().add(thisLocal);
            b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(thisLocal, Jimple.v().newThisRef(sc.getType())));
            SootFieldRef appStaticFieldRef = scApplicationHolder.getFieldByName("application").makeRef();
            Local targetLocal = Jimple.v().newLocal("retApplication", appStaticFieldRef.type());
            b.getLocals().add(targetLocal);
            b.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(targetLocal, Jimple.v().newStaticFieldRef(appStaticFieldRef)));
            b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnStmt(targetLocal));
        }
    }

    public static SootClass createOrGetApplicationHolder() {
        SootClass scApplicationHolder;
        SootClass scApplication = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.APPLICATIONCLASS);
        if (scApplication == null || scApplication.resolvingLevel() < 2) {
            return null;
        }
        if (!Scene.v().containsClass("il.ac.tau.MyApplicationHolder")) {
            scApplicationHolder = Scene.v().makeSootClass("il.ac.tau.MyApplicationHolder", 1);
            scApplicationHolder.setSuperclass(Scene.v().getSootClass(JavaBasicTypes.JAVA_LANG_OBJECT));
            Scene.v().addClass(scApplicationHolder);
            scApplicationHolder.addField(Scene.v().makeSootField("application", scApplication.getType(), 9));
            scApplicationHolder.validate();
        } else {
            scApplicationHolder = Scene.v().getSootClassUnsafe("il.ac.tau.MyApplicationHolder");
        }
        return scApplicationHolder;
    }

    private void patchThreadImplementation() {
        SootClass sc = Scene.v().getSootClassUnsafe("java.lang.Thread");
        if (sc == null || sc.resolvingLevel() < 2) {
            return;
        }
        sc.setLibraryClass();
        SystemClassHandler systemClassHandler = SystemClassHandler.v();
        SootMethod smRun = sc.getMethodUnsafe(JavaMethods.SIG_RUN);
        if (smRun != null) {
            if (smRun.hasActiveBody() && !systemClassHandler.isStubImplementation(smRun.getActiveBody())) {
                return;
            }
            smRun.addTag(new FlowDroidEssentialMethodTag());
            SootMethod smCons = sc.getMethodUnsafe("void <init>(java.lang.Runnable)");
            if (smCons != null) {
                if (smCons.hasActiveBody() && !systemClassHandler.isStubImplementation(smCons.getActiveBody())) {
                    return;
                }
                smCons.addTag(new FlowDroidEssentialMethodTag());
                SootClass runnable = Scene.v().getSootClassUnsafe("java.lang.Runnable");
                if (runnable == null || runnable.resolvingLevel() < 2) {
                    return;
                }
                int fieldIdx = 0;
                while (sc.getFieldByNameUnsafe(TypeProxy.INSTANCE_FIELD + fieldIdx) != null) {
                    fieldIdx++;
                }
                SootField fldTarget = Scene.v().makeSootField(TypeProxy.INSTANCE_FIELD + fieldIdx, runnable.getType());
                sc.addField(fldTarget);
                patchThreadConstructor(smCons, runnable, fldTarget);
                patchThreadRunMethod(smRun, runnable, fldTarget);
            }
        }
    }

    private void patchThreadRunMethod(SootMethod smRun, SootClass runnable, SootField fldTarget) {
        SootClass sc = smRun.getDeclaringClass();
        sc.setLibraryClass();
        smRun.setPhantom(false);
        Body b = Jimple.v().newBody(smRun);
        smRun.setActiveBody(b);
        Local thisLocal = Jimple.v().newLocal("this", sc.getType());
        b.getLocals().add(thisLocal);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(thisLocal, Jimple.v().newThisRef(sc.getType())));
        Local targetLocal = Jimple.v().newLocal(TypeProxy.INSTANCE_FIELD, runnable.getType());
        b.getLocals().add(targetLocal);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(targetLocal, Jimple.v().newInstanceFieldRef(thisLocal, fldTarget.makeRef())));
        ReturnVoidStmt newReturnVoidStmt = Jimple.v().newReturnVoidStmt();
        b.getUnits().add((UnitPatchingChain) Jimple.v().newIfStmt(Jimple.v().newEqExpr(targetLocal, NullConstant.v()), newReturnVoidStmt));
        b.getUnits().add((UnitPatchingChain) Jimple.v().newInvokeStmt(Jimple.v().newInterfaceInvokeExpr(targetLocal, runnable.getMethod(JavaMethods.SIG_RUN).makeRef())));
        b.getUnits().add((UnitPatchingChain) newReturnVoidStmt);
    }

    private void patchThreadConstructor(SootMethod smCons, SootClass runnable, SootField fldTarget) {
        SootClass sc = smCons.getDeclaringClass();
        sc.setLibraryClass();
        smCons.setPhantom(false);
        Body b = Jimple.v().newBody(smCons);
        smCons.setActiveBody(b);
        Local thisLocal = Jimple.v().newLocal("this", sc.getType());
        b.getLocals().add(thisLocal);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(thisLocal, Jimple.v().newThisRef(sc.getType())));
        Local param0Local = Jimple.v().newLocal("p0", runnable.getType());
        b.getLocals().add(param0Local);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(param0Local, Jimple.v().newParameterRef(runnable.getType(), 0)));
        b.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(thisLocal, fldTarget.makeRef()), param0Local));
        b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnVoidStmt());
    }

    private void patchHandlerImplementation() {
        SootClass sc = Scene.v().getSootClassUnsafe("android.os.Handler");
        if (sc == null || sc.resolvingLevel() < 2) {
            return;
        }
        sc.setLibraryClass();
        SootClass runnable = Scene.v().getSootClassUnsafe("java.lang.Runnable");
        if (runnable == null || runnable.resolvingLevel() < 2) {
            return;
        }
        SootMethod smPost = sc.getMethodUnsafe("boolean post(java.lang.Runnable)");
        SootMethod smPostAtFrontOfQueue = sc.getMethodUnsafe("boolean postAtFrontOfQueue(java.lang.Runnable)");
        SootMethod smPostAtTimeWithToken = sc.getMethodUnsafe("boolean postAtTime(java.lang.Runnable,java.lang.Object,long)");
        SootMethod smPostAtTime = sc.getMethodUnsafe("boolean postAtTime(java.lang.Runnable,long)");
        SootMethod smPostDelayed = sc.getMethodUnsafe("boolean postDelayed(java.lang.Runnable,long)");
        SootMethod smDispatchMessage = sc.getMethodUnsafe("void dispatchMessage(android.os.Message)");
        SystemClassHandler systemClassHandler = SystemClassHandler.v();
        if (smPost != null && (!smPost.hasActiveBody() || systemClassHandler.isStubImplementation(smPost.getActiveBody()))) {
            patchHandlerPostBody(smPost, runnable);
            smPost.addTag(new FlowDroidEssentialMethodTag());
        }
        if (smPostAtFrontOfQueue != null && (!smPostAtFrontOfQueue.hasActiveBody() || systemClassHandler.isStubImplementation(smPostAtFrontOfQueue.getActiveBody()))) {
            patchHandlerPostBody(smPostAtFrontOfQueue, runnable);
            smPostAtFrontOfQueue.addTag(new FlowDroidEssentialMethodTag());
        }
        if (smPostAtTime != null && (!smPostAtTime.hasActiveBody() || systemClassHandler.isStubImplementation(smPostAtTime.getActiveBody()))) {
            patchHandlerPostBody(smPostAtTime, runnable);
            smPostAtTime.addTag(new FlowDroidEssentialMethodTag());
        }
        if (smPostAtTimeWithToken != null && (!smPostAtTimeWithToken.hasActiveBody() || systemClassHandler.isStubImplementation(smPostAtTimeWithToken.getActiveBody()))) {
            patchHandlerPostBody(smPostAtTimeWithToken, runnable);
            smPostAtTimeWithToken.addTag(new FlowDroidEssentialMethodTag());
        }
        if (smPostDelayed != null && (!smPostDelayed.hasActiveBody() || systemClassHandler.isStubImplementation(smPostDelayed.getActiveBody()))) {
            patchHandlerPostBody(smPostDelayed, runnable);
            smPostDelayed.addTag(new FlowDroidEssentialMethodTag());
        }
        if (smDispatchMessage != null) {
            if (!smDispatchMessage.hasActiveBody() || systemClassHandler.isStubImplementation(smDispatchMessage.getActiveBody())) {
                patchHandlerDispatchBody(smDispatchMessage);
                smDispatchMessage.addTag(new FlowDroidEssentialMethodTag());
            }
        }
    }

    private Body patchHandlerDispatchBody(SootMethod method) {
        SootClass sc = method.getDeclaringClass();
        sc.setLibraryClass();
        method.setPhantom(false);
        Body b = Jimple.v().newBody(method);
        method.setActiveBody(b);
        Local thisLocal = Jimple.v().newLocal("this", sc.getType());
        b.getLocals().add(thisLocal);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(thisLocal, Jimple.v().newThisRef(sc.getType())));
        Local firstParam = null;
        for (int i = 0; i < method.getParameterCount(); i++) {
            Local paramLocal = Jimple.v().newLocal("param" + i, method.getParameterType(i));
            b.getLocals().add(paramLocal);
            b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(paramLocal, Jimple.v().newParameterRef(method.getParameterType(i), i)));
            if (i == 0) {
                firstParam = paramLocal;
            }
        }
        b.getUnits().add((UnitPatchingChain) Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(thisLocal, Scene.v().makeMethodRef(sc, "handleMessage", Collections.singletonList(method.getParameterType(0)), VoidType.v(), false), firstParam)));
        b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnVoidStmt());
        return b;
    }

    private Body patchHandlerPostBody(SootMethod method, SootClass runnable) {
        SootClass sc = method.getDeclaringClass();
        sc.setLibraryClass();
        method.setPhantom(false);
        Body b = Jimple.v().newBody(method);
        method.setActiveBody(b);
        Local thisLocal = Jimple.v().newLocal("this", sc.getType());
        b.getLocals().add(thisLocal);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(thisLocal, Jimple.v().newThisRef(sc.getType())));
        Local firstParam = null;
        for (int i = 0; i < method.getParameterCount(); i++) {
            Local paramLocal = Jimple.v().newLocal("param" + i, method.getParameterType(i));
            b.getLocals().add(paramLocal);
            b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(paramLocal, Jimple.v().newParameterRef(method.getParameterType(i), i)));
            if (i == 0) {
                firstParam = paramLocal;
            }
        }
        b.getUnits().add((UnitPatchingChain) Jimple.v().newInvokeStmt(Jimple.v().newInterfaceInvokeExpr(firstParam, Scene.v().makeMethodRef(runnable, "run", Collections.emptyList(), VoidType.v(), false))));
        b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnStmt(IntConstant.v(1)));
        return b;
    }

    private void patchTimerImplementation() {
        SootClass sc = Scene.v().getSootClassUnsafe("java.util.Timer");
        if (sc == null || sc.resolvingLevel() < 2) {
            return;
        }
        sc.setLibraryClass();
        SootMethod smSchedule1 = sc.getMethodUnsafe("void schedule(java.util.TimerTask,long)");
        if (smSchedule1 != null && !smSchedule1.hasActiveBody()) {
            patchTimerScheduleMethod(smSchedule1);
            smSchedule1.addTag(new FlowDroidEssentialMethodTag());
        }
        SootMethod smSchedule2 = sc.getMethodUnsafe("void schedule(java.util.TimerTask,java.util.Date)");
        if (smSchedule2 != null && !smSchedule2.hasActiveBody()) {
            patchTimerScheduleMethod(smSchedule2);
            smSchedule2.addTag(new FlowDroidEssentialMethodTag());
        }
        SootMethod smSchedule3 = sc.getMethodUnsafe("void schedule(java.util.TimerTask,java.util.Date,long)");
        if (smSchedule3 != null && !smSchedule3.hasActiveBody()) {
            patchTimerScheduleMethod(smSchedule3);
            smSchedule3.addTag(new FlowDroidEssentialMethodTag());
        }
        SootMethod smSchedule4 = sc.getMethodUnsafe("void schedule(java.util.TimerTask,long,long)");
        if (smSchedule4 != null && !smSchedule4.hasActiveBody()) {
            patchTimerScheduleMethod(smSchedule4);
            smSchedule4.addTag(new FlowDroidEssentialMethodTag());
        }
        SootMethod smSchedule5 = sc.getMethodUnsafe("void scheduleAtFixedRate(java.util.TimerTask,java.util.Date,long)");
        if (smSchedule5 != null && !smSchedule5.hasActiveBody()) {
            patchTimerScheduleMethod(smSchedule5);
            smSchedule5.addTag(new FlowDroidEssentialMethodTag());
        }
        SootMethod smSchedule6 = sc.getMethodUnsafe("void scheduleAtFixedRate(java.util.TimerTask,long,long)");
        if (smSchedule6 != null && !smSchedule6.hasActiveBody()) {
            patchTimerScheduleMethod(smSchedule6);
            smSchedule6.addTag(new FlowDroidEssentialMethodTag());
        }
    }

    private void patchTimerScheduleMethod(SootMethod method) {
        SootClass sc = method.getDeclaringClass();
        sc.setLibraryClass();
        method.setPhantom(false);
        Body b = Jimple.v().newBody(method);
        method.setActiveBody(b);
        Local thisLocal = Jimple.v().newLocal("this", sc.getType());
        b.getLocals().add(thisLocal);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(thisLocal, Jimple.v().newThisRef(sc.getType())));
        Local firstParam = null;
        for (int i = 0; i < method.getParameterCount(); i++) {
            Local paramLocal = Jimple.v().newLocal("param" + i, method.getParameterType(i));
            b.getLocals().add(paramLocal);
            b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(paramLocal, Jimple.v().newParameterRef(method.getParameterType(i), i)));
            if (i == 0) {
                firstParam = paramLocal;
            }
        }
        SootMethod runMethod = Scene.v().grabMethod("<java.util.TimerTask: void run()>");
        if (runMethod != null) {
            b.getUnits().add((UnitPatchingChain) Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(firstParam, runMethod.makeRef())));
        }
        b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnVoidStmt());
    }

    private void patchActivityGetFragmentManager() {
        SootClass sc = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.ACTIVITYCLASS);
        if (sc == null || sc.resolvingLevel() < 2) {
            return;
        }
        sc.setLibraryClass();
        SootMethod smGetFM = sc.getMethodUnsafe("android.app.FragmentManager getFragmentManager()");
        if (smGetFM == null || smGetFM.hasActiveBody()) {
            return;
        }
        Body b = Jimple.v().newBody(smGetFM);
        smGetFM.setActiveBody(b);
        Local thisLocal = Jimple.v().newLocal("this", sc.getType());
        b.getLocals().add(thisLocal);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(thisLocal, Jimple.v().newThisRef(sc.getType())));
        SootClass scFragmentTransaction = Scene.v().forceResolve("android.app.FragmentManager", 2);
        Local retLocal = Jimple.v().newLocal("retFragMan", Scene.v().getSootClassUnsafe("android.app.FragmentManager").getType());
        b.getLocals().add(retLocal);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(retLocal, Jimple.v().newNewExpr(scFragmentTransaction.getType())));
        b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnStmt(retLocal));
    }
}
