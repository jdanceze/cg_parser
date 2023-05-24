package soot.jimple.infoflow.android.entryPointCreators.components;

import java.util.Collections;
import java.util.List;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.UnitPatchingChain;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NopStmt;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.entryPointCreators.AndroidEntryPointConstants;
import soot.jimple.infoflow.android.entryPointCreators.AndroidEntryPointUtils;
import soot.jimple.infoflow.android.manifest.IManifestHandler;
import soot.jimple.infoflow.entryPointCreators.SimulatedCodeElementTag;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/entryPointCreators/components/ServiceEntryPointCreator.class */
public class ServiceEntryPointCreator extends AbstractComponentEntryPointCreator {
    protected SootField binderField;

    public ServiceEntryPointCreator(SootClass component, SootClass applicationClass, IManifestHandler manifest) {
        super(component, applicationClass, manifest);
        this.binderField = null;
    }

    @Override // soot.jimple.infoflow.android.entryPointCreators.components.AbstractComponentEntryPointCreator
    protected void generateComponentLifecycle() {
        searchAndBuildMethod("void onCreate()", this.component, this.thisLocal);
        searchAndBuildMethod(AndroidEntryPointConstants.SERVICE_ONSTART1, this.component, this.thisLocal);
        NopStmt beforeStartCommand = Jimple.v().newNopStmt();
        NopStmt afterStartCommand = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) beforeStartCommand);
        createIfStmt(afterStartCommand);
        searchAndBuildMethod(AndroidEntryPointConstants.SERVICE_ONSTART2, this.component, this.thisLocal);
        createIfStmt(beforeStartCommand);
        this.body.getUnits().add((UnitPatchingChain) afterStartCommand);
        NopStmt startWhileStmt = Jimple.v().newNopStmt();
        NopStmt endWhileStmt = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) startWhileStmt);
        createIfStmt(endWhileStmt);
        AndroidEntryPointUtils.ComponentType componentType = this.entryPointUtils.getComponentType(this.component);
        boolean hasAdditionalMethods = false;
        if (componentType == AndroidEntryPointUtils.ComponentType.GCMBaseIntentService) {
            hasAdditionalMethods = false | createSpecialServiceMethodCalls(AndroidEntryPointConstants.getGCMIntentServiceMethods(), AndroidEntryPointConstants.GCMBASEINTENTSERVICECLASS);
        } else if (componentType == AndroidEntryPointUtils.ComponentType.GCMListenerService) {
            hasAdditionalMethods = false | createSpecialServiceMethodCalls(AndroidEntryPointConstants.getGCMListenerServiceMethods(), AndroidEntryPointConstants.GCMLISTENERSERVICECLASS);
        } else if (componentType == AndroidEntryPointUtils.ComponentType.HostApduService) {
            hasAdditionalMethods = false | createSpecialServiceMethodCalls(AndroidEntryPointConstants.getHostApduServiceMethods(), AndroidEntryPointConstants.HOSTAPDUSERVICECLASS);
        }
        addCallbackMethods();
        this.body.getUnits().add((UnitPatchingChain) endWhileStmt);
        if (hasAdditionalMethods) {
            createIfStmt(startWhileStmt);
        }
        searchAndBuildMethod(AndroidEntryPointConstants.SERVICE_ONBIND, this.component, this.thisLocal);
        NopStmt beforemethodsStmt = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) beforemethodsStmt);
        NopStmt startWhile2Stmt = Jimple.v().newNopStmt();
        NopStmt endWhile2Stmt = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) startWhile2Stmt);
        boolean hasAdditionalMethods2 = false;
        if (componentType == AndroidEntryPointUtils.ComponentType.GCMBaseIntentService) {
            for (String sig : AndroidEntryPointConstants.getGCMIntentServiceMethods()) {
                SootMethod sm = findMethod(this.component, sig);
                if (sm != null && !sm.getName().equals(AndroidEntryPointConstants.GCMBASEINTENTSERVICECLASS) && createPlainMethodCall(this.thisLocal, sm)) {
                    hasAdditionalMethods2 = true;
                }
            }
        }
        addCallbackMethods();
        this.body.getUnits().add((UnitPatchingChain) endWhile2Stmt);
        if (hasAdditionalMethods2) {
            createIfStmt(startWhile2Stmt);
        }
        NopStmt newNopStmt = Jimple.v().newNopStmt();
        searchAndBuildMethod(AndroidEntryPointConstants.SERVICE_ONUNBIND, this.component, this.thisLocal);
        createIfStmt(newNopStmt);
        searchAndBuildMethod(AndroidEntryPointConstants.SERVICE_ONREBIND, this.component, this.thisLocal);
        createIfStmt(beforemethodsStmt);
        this.body.getUnits().add((UnitPatchingChain) newNopStmt);
        searchAndBuildMethod("void onDestroy()", this.component, this.thisLocal);
    }

    protected boolean createSpecialServiceMethodCalls(List<String> methodSigs, String parentClass) {
        boolean hasAdditionalMethods = false;
        for (String sig : methodSigs) {
            SootMethod sm = findMethod(this.component, sig);
            if (sm != null && !sm.getDeclaringClass().getName().equals(parentClass) && createPlainMethodCall(this.thisLocal, sm)) {
                hasAdditionalMethods = true;
            }
        }
        return hasAdditionalMethods;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.android.entryPointCreators.components.AbstractComponentEntryPointCreator, soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator
    public void createAdditionalFields() {
        super.createAdditionalFields();
        String fieldName = "ipcIntent";
        int fieldIdx = 0;
        while (this.component.declaresFieldByName(fieldName)) {
            int i = fieldIdx;
            fieldIdx++;
            fieldName = "ipcBinder_" + i;
        }
        this.binderField = Scene.v().makeSootField(fieldName, RefType.v("android.os.IBinder"), 1);
        this.component.addField(this.binderField);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator
    public void createAdditionalMethods() {
        super.createAdditionalMethods();
        instrumentOnBind();
        createGetIntentMethod();
    }

    private void instrumentOnBind() {
        SootMethod sm = this.component.getMethodUnsafe(AndroidEntryPointConstants.SERVICE_ONBIND);
        Type intentType = RefType.v("android.content.Intent");
        Type binderType = RefType.v("android.os.IBinder");
        if (sm == null || !sm.hasActiveBody()) {
            if (sm == null) {
                sm = Scene.v().makeSootMethod("onBind", Collections.singletonList(intentType), binderType, 1);
                this.component.addMethod(sm);
                sm.addTag(SimulatedCodeElementTag.TAG);
            }
            JimpleBody b = Jimple.v().newBody(sm);
            sm.setActiveBody(b);
            b.insertIdentityStmts();
            Local thisLocal = b.getThisLocal();
            Local binderLocal = b.getParameterLocal(0);
            b.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(thisLocal, this.binderField.makeRef()), binderLocal));
            b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnStmt(binderLocal));
            return;
        }
        JimpleBody b2 = (JimpleBody) sm.getActiveBody();
        Stmt firstNonIdentityStmt = b2.getFirstNonIdentityStmt();
        Local thisLocal2 = b2.getThisLocal();
        AssignStmt newAssignStmt = Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(thisLocal2, this.binderField.makeRef()), b2.getParameterLocal(0));
        newAssignStmt.addTag(SimulatedCodeElementTag.TAG);
        b2.getUnits().insertAfter(newAssignStmt, (AssignStmt) firstNonIdentityStmt);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.android.entryPointCreators.components.AbstractComponentEntryPointCreator, soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator
    public void reset() {
        super.reset();
        this.component.removeField(this.binderField);
        this.binderField = null;
    }

    @Override // soot.jimple.infoflow.android.entryPointCreators.components.AbstractComponentEntryPointCreator
    public ComponentEntryPointInfo getComponentInfo() {
        ServiceEntryPointInfo serviceInfo = new ServiceEntryPointInfo(this.mainMethod);
        serviceInfo.setIntentField(this.intentField);
        serviceInfo.setBinderField(this.binderField);
        return serviceInfo;
    }
}
