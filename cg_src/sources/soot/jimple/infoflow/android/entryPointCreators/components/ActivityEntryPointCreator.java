package soot.jimple.infoflow.android.entryPointCreators.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.IntType;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.UnitPatchingChain;
import soot.Value;
import soot.VoidType;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NopStmt;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.entryPointCreators.AndroidEntryPointConstants;
import soot.jimple.infoflow.android.manifest.IManifestHandler;
import soot.jimple.infoflow.cfg.LibraryClassPatcher;
import soot.jimple.infoflow.entryPointCreators.SimulatedCodeElementTag;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/entryPointCreators/components/ActivityEntryPointCreator.class */
public class ActivityEntryPointCreator extends AbstractComponentEntryPointCreator {
    private final MultiMap<SootClass, String> activityLifecycleCallbacks;
    private final Map<SootClass, SootField> callbackClassToField;
    private final Map<SootClass, SootMethod> fragmentToMainMethod;
    protected SootField resultIntentField;

    public ActivityEntryPointCreator(SootClass component, SootClass applicationClass, MultiMap<SootClass, String> activityLifecycleCallbacks, Map<SootClass, SootField> callbackClassToField, Map<SootClass, SootMethod> fragmentToMainMethod, IManifestHandler manifest) {
        super(component, applicationClass, manifest);
        this.resultIntentField = null;
        this.activityLifecycleCallbacks = activityLifecycleCallbacks;
        this.callbackClassToField = callbackClassToField;
        this.fragmentToMainMethod = fragmentToMainMethod;
    }

    @Override // soot.jimple.infoflow.android.entryPointCreators.components.AbstractComponentEntryPointCreator
    protected void generateComponentLifecycle() {
        Set<SootClass> currentClassSet = Collections.singleton(this.component);
        Body body = this.mainMethod.getActiveBody();
        Set<SootClass> referenceClasses = new HashSet<>();
        if (this.applicationClass != null) {
            referenceClasses.add(this.applicationClass);
        }
        if (this.activityLifecycleCallbacks != null) {
            for (SootClass callbackClass : this.activityLifecycleCallbacks.keySet()) {
                referenceClasses.add(callbackClass);
            }
        }
        referenceClasses.add(this.component);
        if (this.applicationClass != null) {
            Local applicationLocal = this.generator.generateLocal(RefType.v(AndroidEntryPointConstants.APPLICATIONCLASS));
            SootClass scApplicationHolder = LibraryClassPatcher.createOrGetApplicationHolder();
            body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(applicationLocal, Jimple.v().newStaticFieldRef(scApplicationHolder.getFieldByName("application").makeRef())));
            this.localVarsForClasses.put(this.applicationClass, applicationLocal);
        }
        for (SootClass sc : this.callbackClassToField.keySet()) {
            Local callbackLocal = this.generator.generateLocal(RefType.v(sc));
            body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(callbackLocal, Jimple.v().newStaticFieldRef(this.callbackClassToField.get(sc).makeRef())));
            this.localVarsForClasses.put(sc, callbackLocal);
        }
        searchAndBuildMethod("void onCreate(android.os.Bundle)", this.component, this.thisLocal);
        for (SootClass callbackClass2 : this.activityLifecycleCallbacks.keySet()) {
            searchAndBuildMethod(AndroidEntryPointConstants.ACTIVITYLIFECYCLECALLBACK_ONACTIVITYCREATED, callbackClass2, this.localVarsForClasses.get(callbackClass2), currentClassSet);
        }
        if (this.fragmentToMainMethod != null && !this.fragmentToMainMethod.isEmpty()) {
            for (SootClass scFragment : this.fragmentToMainMethod.keySet()) {
                SootMethod smFragment = this.fragmentToMainMethod.get(scFragment);
                List<Value> args = new ArrayList<>();
                args.add(this.intentLocal);
                args.add(this.thisLocal);
                body.getUnits().add((UnitPatchingChain) Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(smFragment.makeRef(), args)));
            }
        }
        Stmt onStartStmt = searchAndBuildMethod("void onStart()", this.component, this.thisLocal);
        for (SootClass callbackClass3 : this.activityLifecycleCallbacks.keySet()) {
            Stmt s = searchAndBuildMethod(AndroidEntryPointConstants.ACTIVITYLIFECYCLECALLBACK_ONACTIVITYSTARTED, callbackClass3, this.localVarsForClasses.get(callbackClass3), currentClassSet);
            if (onStartStmt == null) {
                onStartStmt = s;
            }
        }
        if (onStartStmt == null) {
            UnitPatchingChain units = body.getUnits();
            NopStmt newNopStmt = Jimple.v().newNopStmt();
            onStartStmt = newNopStmt;
            units.add((UnitPatchingChain) newNopStmt);
        }
        NopStmt newNopStmt2 = Jimple.v().newNopStmt();
        createIfStmt(newNopStmt2);
        searchAndBuildMethod(AndroidEntryPointConstants.ACTIVITY_ONRESTOREINSTANCESTATE, this.component, this.thisLocal, currentClassSet);
        body.getUnits().add((UnitPatchingChain) newNopStmt2);
        searchAndBuildMethod(AndroidEntryPointConstants.ACTIVITY_ONPOSTCREATE, this.component, this.thisLocal);
        NopStmt newNopStmt3 = Jimple.v().newNopStmt();
        body.getUnits().add((UnitPatchingChain) newNopStmt3);
        searchAndBuildMethod("void onResume()", this.component, this.thisLocal);
        for (SootClass callbackClass4 : this.activityLifecycleCallbacks.keySet()) {
            searchAndBuildMethod(AndroidEntryPointConstants.ACTIVITYLIFECYCLECALLBACK_ONACTIVITYRESUMED, callbackClass4, this.localVarsForClasses.get(callbackClass4), currentClassSet);
        }
        searchAndBuildMethod(AndroidEntryPointConstants.ACTIVITY_ONPOSTRESUME, this.component, this.thisLocal);
        if (this.callbacks != null && !this.callbacks.isEmpty()) {
            NopStmt startWhileStmt = Jimple.v().newNopStmt();
            NopStmt endWhileStmt = Jimple.v().newNopStmt();
            body.getUnits().add((UnitPatchingChain) startWhileStmt);
            createIfStmt(endWhileStmt);
            addCallbackMethods();
            body.getUnits().add((UnitPatchingChain) endWhileStmt);
            createIfStmt(startWhileStmt);
        }
        searchAndBuildMethod("void onPause()", this.component, this.thisLocal);
        for (SootClass callbackClass5 : this.activityLifecycleCallbacks.keySet()) {
            searchAndBuildMethod(AndroidEntryPointConstants.ACTIVITYLIFECYCLECALLBACK_ONACTIVITYPAUSED, callbackClass5, this.localVarsForClasses.get(callbackClass5), currentClassSet);
        }
        searchAndBuildMethod(AndroidEntryPointConstants.ACTIVITY_ONCREATEDESCRIPTION, this.component, this.thisLocal);
        searchAndBuildMethod("void onSaveInstanceState(android.os.Bundle)", this.component, this.thisLocal);
        for (SootClass callbackClass6 : this.activityLifecycleCallbacks.keySet()) {
            searchAndBuildMethod(AndroidEntryPointConstants.ACTIVITYLIFECYCLECALLBACK_ONACTIVITYSAVEINSTANCESTATE, callbackClass6, this.localVarsForClasses.get(callbackClass6), currentClassSet);
        }
        createIfStmt(newNopStmt3);
        Stmt onStop = searchAndBuildMethod("void onStop()", this.component, this.thisLocal);
        boolean hasAppOnStop = false;
        for (SootClass callbackClass7 : this.activityLifecycleCallbacks.keySet()) {
            Stmt onActStoppedStmt = searchAndBuildMethod(AndroidEntryPointConstants.ACTIVITYLIFECYCLECALLBACK_ONACTIVITYSTOPPED, callbackClass7, this.localVarsForClasses.get(callbackClass7), currentClassSet);
            hasAppOnStop |= onActStoppedStmt != null;
        }
        if (hasAppOnStop && onStop != null) {
            createIfStmt(onStop);
        }
        NopStmt stopToDestroyStmt = Jimple.v().newNopStmt();
        createIfStmt(stopToDestroyStmt);
        searchAndBuildMethod(AndroidEntryPointConstants.ACTIVITY_ONRESTART, this.component, this.thisLocal);
        body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(onStartStmt));
        body.getUnits().add((UnitPatchingChain) stopToDestroyStmt);
        searchAndBuildMethod("void onDestroy()", this.component, this.thisLocal);
        for (SootClass callbackClass8 : this.activityLifecycleCallbacks.keySet()) {
            searchAndBuildMethod(AndroidEntryPointConstants.ACTIVITYLIFECYCLECALLBACK_ONACTIVITYDESTROYED, callbackClass8, this.localVarsForClasses.get(callbackClass8), currentClassSet);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.android.entryPointCreators.components.AbstractComponentEntryPointCreator, soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator
    public void createAdditionalFields() {
        super.createAdditionalFields();
        String fieldName = "ipcResultIntent";
        int fieldIdx = 0;
        while (this.component.declaresFieldByName(fieldName)) {
            int i = fieldIdx;
            fieldIdx++;
            fieldName = "ipcResultIntent_" + i;
        }
        this.resultIntentField = Scene.v().makeSootField(fieldName, RefType.v("android.content.Intent"), 1);
        this.resultIntentField.addTag(SimulatedCodeElementTag.TAG);
        this.component.addField(this.resultIntentField);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator
    public void createAdditionalMethods() {
        if (InfoflowAndroidConfiguration.getCreateActivityEntryMethods()) {
            createGetIntentMethod();
            createSetIntentMethod();
            createSetResultMethod();
        }
    }

    private void createSetIntentMethod() {
        if (this.component.declaresMethod("void setIntent(android.content.Intent)")) {
            return;
        }
        Type intentType = RefType.v("android.content.Intent");
        SootMethod sm = Scene.v().makeSootMethod("setIntent", Collections.singletonList(intentType), VoidType.v(), 1);
        this.component.addMethod(sm);
        sm.addTag(SimulatedCodeElementTag.TAG);
        JimpleBody b = Jimple.v().newBody(sm);
        sm.setActiveBody(b);
        b.insertIdentityStmts();
        Local lcIntent = b.getParameterLocal(0);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(b.getThisLocal(), this.intentField.makeRef()), lcIntent));
        b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnVoidStmt());
    }

    private void createSetResultMethod() {
        if (this.component.declaresMethod("void setResult(int,android.content.Intent)")) {
            return;
        }
        Type v = RefType.v("android.content.Intent");
        List<Type> params = new ArrayList<>();
        params.add(IntType.v());
        params.add(v);
        SootMethod sm = Scene.v().makeSootMethod("setResult", params, VoidType.v(), 1);
        this.component.addMethod(sm);
        sm.addTag(SimulatedCodeElementTag.TAG);
        JimpleBody b = Jimple.v().newBody(sm);
        sm.setActiveBody(b);
        b.insertIdentityStmts();
        Local lcIntent = b.getParameterLocal(1);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(b.getThisLocal(), this.resultIntentField.makeRef()), lcIntent));
        b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnVoidStmt());
        SootMethod smSetResult = Scene.v().grabMethod("<android.app.Activity: void setResult(int,android.content.Intent)>");
        if (smSetResult != null && smSetResult.getDeclaringClass().isApplicationClass()) {
            smSetResult.setModifiers(smSetResult.getModifiers() & (-17));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.android.entryPointCreators.components.AbstractComponentEntryPointCreator, soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator
    public void reset() {
        super.reset();
        this.component.removeField(this.resultIntentField);
        this.resultIntentField = null;
    }

    @Override // soot.jimple.infoflow.android.entryPointCreators.components.AbstractComponentEntryPointCreator
    public ComponentEntryPointInfo getComponentInfo() {
        ActivityEntryPointInfo activityInfo = new ActivityEntryPointInfo(this.mainMethod);
        activityInfo.setIntentField(this.intentField);
        activityInfo.setResultIntentField(this.resultIntentField);
        return activityInfo;
    }
}
