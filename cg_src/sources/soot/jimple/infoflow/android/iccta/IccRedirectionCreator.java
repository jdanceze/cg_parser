package soot.jimple.infoflow.android.iccta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.Local;
import soot.LocalGenerator;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.entryPointCreators.components.ActivityEntryPointInfo;
import soot.jimple.infoflow.android.entryPointCreators.components.ComponentEntryPointCollection;
import soot.jimple.infoflow.android.entryPointCreators.components.ServiceEntryPointInfo;
import soot.jimple.infoflow.entryPointCreators.SimulatedCodeElementTag;
import soot.jimple.infoflow.util.SystemClassHandler;
import soot.tagkit.Tag;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
import soot.util.NumberedString;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/IccRedirectionCreator.class */
public class IccRedirectionCreator {
    private static int num = 0;
    private static final Logger logger = LoggerFactory.getLogger(IccRedirectionCreator.class);
    protected final SootClass dummyMainClass;
    protected final ComponentEntryPointCollection componentToEntryPoint;
    private final RefType INTENT_TYPE = RefType.v("android.content.Intent");
    private final RefType IBINDER_TYPE = RefType.v("android.os.IBinder");
    protected final Map<String, SootMethod> source2RedirectMethod = new HashMap();
    protected final MultiMap<Body, Unit> instrumentedUnits = new HashMultiMap();
    protected IRedirectorCallInserted instrumentationCallback = null;
    protected final NumberedString subsigStartActivityForResult = Scene.v().getSubSigNumberer().findOrAdd("void startActivityForResult(android.content.Intent,int)");

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/IccRedirectionCreator$IRedirectorCallInserted.class */
    public interface IRedirectorCallInserted {
        void onRedirectorCallInserted(IccLink iccLink, Stmt stmt, SootMethod sootMethod);
    }

    public IccRedirectionCreator(SootClass dummyMainClass, ComponentEntryPointCollection componentToEntryPoint) {
        this.componentToEntryPoint = componentToEntryPoint;
        this.dummyMainClass = dummyMainClass;
    }

    public void redirectToDestination(IccLink link) {
        SootMethod redirectSM;
        if (link.getDestinationC().isPhantom() || SystemClassHandler.v().isClassInSystemPackage(link.getFromSM().getDeclaringClass().getName()) || (redirectSM = getRedirectMethod(link)) == null) {
            return;
        }
        insertRedirectMethodCallAfterIccMethod(link, redirectSM);
    }

    protected SootMethod getRedirectMethod(IccLink link) {
        SootClass instrumentedDestinationSC = link.getDestinationC();
        if (!this.componentToEntryPoint.hasEntryPointForComponent(instrumentedDestinationSC)) {
            return null;
        }
        SootMethod redirectMethod = this.source2RedirectMethod.get(link.toString());
        if (redirectMethod == null) {
            String source = link.toString();
            Stmt stmt = (Stmt) link.getFromU();
            if (stmt.containsInvokeExpr()) {
                if (stmt.getInvokeExpr().getMethod().getName().equals("startActivityForResult")) {
                    Value expr = stmt.getInvokeExprBox().getValue();
                    if (expr instanceof InstanceInvokeExpr) {
                        InstanceInvokeExpr iiexpr = (InstanceInvokeExpr) expr;
                        Type tp = iiexpr.getBase().getType();
                        if (tp instanceof RefType) {
                            RefType rt = (RefType) tp;
                            redirectMethod = generateRedirectMethodForStartActivityForResult(rt.getSootClass(), instrumentedDestinationSC);
                            if (redirectMethod == null) {
                                return null;
                            }
                        }
                    }
                } else if (stmt.getInvokeExpr().getMethod().getName().equals("bindService")) {
                    Value v = stmt.getInvokeExpr().getArg(1);
                    if (v.getType() instanceof RefType) {
                        RefType rt2 = (RefType) v.getType();
                        redirectMethod = generateRedirectMethodForBindService(rt2.getSootClass(), instrumentedDestinationSC);
                        if (redirectMethod == null) {
                            return null;
                        }
                    }
                } else {
                    redirectMethod = generateRedirectMethod(instrumentedDestinationSC);
                    if (redirectMethod == null) {
                        return null;
                    }
                }
            }
            if (redirectMethod == null) {
                throw new RuntimeException("Wrong IccLink [" + link.toString() + "]");
            }
            this.source2RedirectMethod.put(source, redirectMethod);
        }
        return redirectMethod;
    }

    protected SootMethod generateRedirectMethodForStartActivityForResult(SootClass originActivity, SootClass destComp) {
        StringBuilder sb = new StringBuilder("redirector");
        int i = num;
        num = i + 1;
        String newSM_name = sb.append(i).toString();
        List<Type> newSM_parameters = new ArrayList<>();
        newSM_parameters.add(originActivity.getType());
        newSM_parameters.add(this.INTENT_TYPE);
        SootMethod newSM = Scene.v().makeSootMethod(newSM_name, newSM_parameters, VoidType.v(), 9);
        this.dummyMainClass.addMethod(newSM);
        JimpleBody b = Jimple.v().newBody(newSM);
        newSM.setActiveBody(b);
        newSM.addTag(SimulatedCodeElementTag.TAG);
        LocalGenerator lg = Scene.v().createLocalGenerator(b);
        Local originActivityParameterLocal = lg.generateLocal(originActivity.getType());
        b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(originActivityParameterLocal, Jimple.v().newParameterRef(originActivity.getType(), 0)));
        Local intentParameterLocal = lg.generateLocal(this.INTENT_TYPE);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(intentParameterLocal, Jimple.v().newParameterRef(this.INTENT_TYPE, 1)));
        Local componentLocal = lg.generateLocal(destComp.getType());
        ActivityEntryPointInfo entryPointInfo = (ActivityEntryPointInfo) this.componentToEntryPoint.get(destComp);
        SootMethod targetDummyMain = this.componentToEntryPoint.getEntryPoint(destComp);
        if (targetDummyMain == null) {
            throw new RuntimeException(String.format("Destination component %s has no dummy main method", destComp.getName()));
        }
        b.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(componentLocal, Jimple.v().newStaticInvokeExpr(targetDummyMain.makeRef(), Collections.singletonList(intentParameterLocal))));
        Value arIntentLocal = lg.generateLocal(this.INTENT_TYPE);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(arIntentLocal, Jimple.v().newInstanceFieldRef(componentLocal, entryPointInfo.getResultIntentField().makeRef())));
        SootMethod method = originActivity.getMethodUnsafe("void onActivityResult(int,int,android.content.Intent)");
        if (method != null) {
            List<Value> args = new ArrayList<>();
            args.add(IntConstant.v(-1));
            args.add(IntConstant.v(-1));
            args.add(arIntentLocal);
            b.getUnits().add((UnitPatchingChain) Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(originActivityParameterLocal, method.makeRef(), args)));
        }
        b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnVoidStmt());
        return newSM;
    }

    protected SootMethod generateRedirectMethod(SootClass wrapper) {
        SootMethod targetDummyMain = this.componentToEntryPoint.getEntryPoint(wrapper);
        if (targetDummyMain == null) {
            logger.warn("Destination component {} has no dummy main method", wrapper.getName());
            return null;
        }
        StringBuilder sb = new StringBuilder("redirector");
        int i = num;
        num = i + 1;
        String newSM_name = sb.append(i).toString();
        SootMethod newSM = Scene.v().makeSootMethod(newSM_name, Collections.singletonList(this.INTENT_TYPE), VoidType.v(), 9);
        newSM.addTag(SimulatedCodeElementTag.TAG);
        this.dummyMainClass.addMethod(newSM);
        JimpleBody b = Jimple.v().newBody(newSM);
        newSM.setActiveBody(b);
        LocalGenerator lg = Scene.v().createLocalGenerator(b);
        Local intentParameterLocal = lg.generateLocal(this.INTENT_TYPE);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(intentParameterLocal, Jimple.v().newParameterRef(this.INTENT_TYPE, 0)));
        b.getUnits().add((UnitPatchingChain) Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(targetDummyMain.makeRef(), Collections.singletonList(intentParameterLocal))));
        b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnVoidStmt());
        return newSM;
    }

    protected SootMethod generateRedirectMethodForStartActivity(SootClass wrapper) {
        SootMethod targetDummyMain = this.componentToEntryPoint.getEntryPoint(wrapper);
        if (targetDummyMain == null) {
            logger.warn("Destination component {} has no dummy main method", wrapper.getName());
            return null;
        }
        StringBuilder sb = new StringBuilder("redirector");
        int i = num;
        num = i + 1;
        String newSM_name = sb.append(i).toString();
        SootMethod newSM = Scene.v().makeSootMethod(newSM_name, Collections.singletonList(this.INTENT_TYPE), VoidType.v(), 9);
        newSM.addTag(SimulatedCodeElementTag.TAG);
        this.dummyMainClass.addMethod(newSM);
        JimpleBody b = Jimple.v().newBody(newSM);
        newSM.setActiveBody(b);
        LocalGenerator lg = Scene.v().createLocalGenerator(b);
        Local intentParameterLocal = lg.generateLocal(this.INTENT_TYPE);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(intentParameterLocal, Jimple.v().newParameterRef(this.INTENT_TYPE, 0)));
        b.getUnits().add((UnitPatchingChain) Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(targetDummyMain.makeRef(), Collections.singletonList(intentParameterLocal))));
        b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnVoidStmt());
        return newSM;
    }

    protected SootMethod generateRedirectMethodForBindService(SootClass serviceConnection, SootClass destComp) {
        InvokeExpr invoke;
        ServiceEntryPointInfo entryPointInfo = (ServiceEntryPointInfo) this.componentToEntryPoint.get(destComp);
        if (entryPointInfo == null) {
            logger.warn("Destination component {} has no dummy main method", destComp.getName());
            return null;
        }
        SootMethod targetDummyMain = entryPointInfo.getEntryPoint();
        if (targetDummyMain == null) {
            logger.warn("Destination component {} has no dummy main method", destComp.getName());
            return null;
        }
        StringBuilder sb = new StringBuilder("redirector");
        int i = num;
        num = i + 1;
        String newSM_name = sb.append(i).toString();
        List<Type> newSM_parameters = new ArrayList<>();
        newSM_parameters.add(serviceConnection.getType());
        newSM_parameters.add(this.INTENT_TYPE);
        SootMethod newSM = Scene.v().makeSootMethod(newSM_name, newSM_parameters, VoidType.v(), 9);
        newSM.addTag(SimulatedCodeElementTag.TAG);
        this.dummyMainClass.addMethod(newSM);
        JimpleBody b = Jimple.v().newBody(newSM);
        newSM.setActiveBody(b);
        LocalGenerator lg = Scene.v().createLocalGenerator(b);
        Local originActivityParameterLocal = lg.generateLocal(serviceConnection.getType());
        b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(originActivityParameterLocal, Jimple.v().newParameterRef(serviceConnection.getType(), 0)));
        Local intentParameterLocal = lg.generateLocal(this.INTENT_TYPE);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(intentParameterLocal, Jimple.v().newParameterRef(this.INTENT_TYPE, 1)));
        Local componentLocal = lg.generateLocal(destComp.getType());
        b.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(componentLocal, Jimple.v().newStaticInvokeExpr(targetDummyMain.makeRef(), Collections.singletonList(intentParameterLocal))));
        Value ibinderLocal = lg.generateLocal(this.IBINDER_TYPE);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(ibinderLocal, Jimple.v().newInstanceFieldRef(componentLocal, entryPointInfo.getBinderField().makeRef())));
        List<Type> paramTypes = new ArrayList<>();
        paramTypes.add(RefType.v("android.content.ComponentName"));
        paramTypes.add(RefType.v("android.os.IBinder"));
        SootMethod method = serviceConnection.getMethod("onServiceConnected", paramTypes);
        Value iLocal1 = lg.generateLocal(RefType.v("android.content.ComponentName"));
        b.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(iLocal1, NullConstant.v()));
        List<Value> args = new ArrayList<>();
        args.add(iLocal1);
        args.add(ibinderLocal);
        SootClass sc = Scene.v().getSootClass(originActivityParameterLocal.getType().toString());
        if (sc.isInterface()) {
            invoke = Jimple.v().newInterfaceInvokeExpr(originActivityParameterLocal, method.makeRef(), args);
        } else {
            invoke = Jimple.v().newVirtualInvokeExpr(originActivityParameterLocal, method.makeRef(), args);
        }
        b.getUnits().add((UnitPatchingChain) Jimple.v().newInvokeStmt(invoke));
        b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnVoidStmt());
        return newSM;
    }

    protected SootMethod generateRedirectMethodForContentProvider(Stmt iccStmt, SootClass destProvider) {
        SootMethod iccMethod = iccStmt.getInvokeExpr().getMethod();
        StringBuilder sb = new StringBuilder("redirector");
        int i = num;
        num = i + 1;
        String newSM_name = sb.append(i).toString();
        SootMethod newSM = Scene.v().makeSootMethod(newSM_name, iccMethod.getParameterTypes(), iccMethod.getReturnType(), 9);
        newSM.addTag(SimulatedCodeElementTag.TAG);
        this.dummyMainClass.addMethod(newSM);
        JimpleBody b = Jimple.v().newBody(newSM);
        newSM.setActiveBody(b);
        LocalGenerator lg = Scene.v().createLocalGenerator(b);
        List<Local> locals = new ArrayList<>();
        for (int i2 = 0; i2 < iccMethod.getParameterCount(); i2++) {
            Type type = iccMethod.getParameterType(i2);
            Local local = lg.generateLocal(type);
            locals.add(local);
            b.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(local, Jimple.v().newParameterRef(type, i2)));
        }
        Local al = lg.generateLocal(destProvider.getType());
        b.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(al, Jimple.v().newNewExpr(destProvider.getType())));
        List<Type> parameters = new ArrayList<>();
        List<Value> args = new ArrayList<>();
        SootMethod method = destProvider.getMethod("<init>", parameters, VoidType.v());
        b.getUnits().add((UnitPatchingChain) Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(al, method.makeRef(), args)));
        Local rtLocal = lg.generateLocal(iccMethod.getReturnType());
        iccMethod.getParameterTypes();
        SootMethod method2 = destProvider.getMethodByName(iccMethod.getName());
        InvokeExpr invoke = Jimple.v().newVirtualInvokeExpr(al, method2.makeRef(), locals);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(rtLocal, invoke));
        b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnStmt(rtLocal));
        return newSM;
    }

    protected void insertRedirectMethodCallAfterIccMethod(IccLink link, SootMethod redirectMethod) {
        Stmt fromStmt = (Stmt) link.getFromU();
        if (fromStmt == null || !fromStmt.containsInvokeExpr()) {
            return;
        }
        SootMethod callee = fromStmt.getInvokeExpr().getMethod();
        List<Value> args = new ArrayList<>();
        if (callee.getNumberedSubSignature().equals(this.subsigStartActivityForResult)) {
            InstanceInvokeExpr iiexpr = (InstanceInvokeExpr) fromStmt.getInvokeExpr();
            args.add(iiexpr.getBase());
            args.add(iiexpr.getArg(0));
        } else if (fromStmt.toString().contains("bindService")) {
            Value arg0 = fromStmt.getInvokeExpr().getArg(0);
            Value arg1 = fromStmt.getInvokeExpr().getArg(1);
            args.add(arg1);
            args.add(arg0);
        } else if (fromStmt.getInvokeExpr().getArgCount() == 0) {
            return;
        } else {
            Value arg02 = fromStmt.getInvokeExpr().getArg(0);
            args.add(arg02);
        }
        if (redirectMethod == null) {
            return;
        }
        InvokeStmt newInvokeStmt = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(redirectMethod.makeRef(), args));
        Body body = link.getFromSM().retrieveActiveBody();
        PatchingChain<Unit> units = body.getUnits();
        copyTags(link.getFromU(), newInvokeStmt);
        newInvokeStmt.addTag(SimulatedCodeElementTag.TAG);
        units.insertAfter(newInvokeStmt, (InvokeStmt) link.getFromU());
        this.instrumentedUnits.put(body, newInvokeStmt);
        if (this.instrumentationCallback != null) {
            this.instrumentationCallback.onRedirectorCallInserted(link, newInvokeStmt, redirectMethod);
        }
        NumberedString subsig = Scene.v().getSubSigNumberer().find("android.content.Intent createChooser(android.content.Intent,java.lang.CharSequence)");
        SootClass clazz = Scene.v().getSootClassUnsafe("android.content.Intent");
        if (subsig != null && clazz != null) {
            Iterator<Unit> iter = units.snapshotIterator();
            while (iter.hasNext()) {
                Stmt stmt = (Stmt) iter.next();
                if (stmt.containsInvokeExpr()) {
                    InvokeExpr expr = stmt.getInvokeExpr();
                    SootMethodRef mr = expr.getMethodRef();
                    if (mr.getDeclaringClass().equals(clazz) && mr.getSubSignature().equals(subsig)) {
                        List<ValueBox> vbs = stmt.getUseAndDefBoxes();
                        Unit assignU = Jimple.v().newAssignStmt(vbs.get(0).getValue(), vbs.get(1).getValue());
                        copyTags(stmt, assignU);
                        assignU.addTag(SimulatedCodeElementTag.TAG);
                        units.insertAfter(assignU, (Unit) stmt);
                        this.instrumentedUnits.put(body, assignU);
                    }
                }
            }
        }
    }

    protected static void copyTags(Unit from, Unit to) {
        List<Tag> tags = from.getTags();
        for (Tag tag : tags) {
            to.removeTag(tag.getName());
            to.addTag(tag);
        }
    }

    public void undoInstrumentation() {
        for (SootMethod sm : this.source2RedirectMethod.values()) {
            sm.getDeclaringClass().removeMethod(sm);
        }
        for (Body body : this.instrumentedUnits.keySet()) {
            for (Unit u : this.instrumentedUnits.get(body)) {
                body.getUnits().remove(u);
            }
        }
        this.instrumentedUnits.clear();
        this.source2RedirectMethod.clear();
    }

    public void setInstrumentationCallback(IRedirectorCallInserted instrumentationCallback) {
        this.instrumentationCallback = instrumentationCallback;
    }
}
