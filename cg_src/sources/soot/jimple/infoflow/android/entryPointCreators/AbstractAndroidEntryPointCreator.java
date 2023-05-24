package soot.jimple.infoflow.android.entryPointCreators;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import soot.Local;
import soot.SootClass;
import soot.SootMethod;
import soot.UnitPatchingChain;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.manifest.IManifestHandler;
import soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator;
import soot.jimple.infoflow.util.SystemClassHandler;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/entryPointCreators/AbstractAndroidEntryPointCreator.class */
public abstract class AbstractAndroidEntryPointCreator extends BaseEntryPointCreator {
    protected AndroidEntryPointUtils entryPointUtils = null;
    protected IManifestHandler manifest;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AbstractAndroidEntryPointCreator.class.desiredAssertionStatus();
    }

    public AbstractAndroidEntryPointCreator(IManifestHandler manifest) {
        this.manifest = manifest;
    }

    @Override // soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator, soot.jimple.infoflow.entryPointCreators.IEntryPointCreator
    public SootMethod createDummyMain() {
        this.entryPointUtils = new AndroidEntryPointUtils();
        return super.createDummyMain();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Stmt searchAndBuildMethod(String subsignature, SootClass currentClass, Local classLocal) {
        return searchAndBuildMethod(subsignature, currentClass, classLocal, Collections.emptySet());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Stmt searchAndBuildMethod(String subsignature, SootClass currentClass, Local classLocal, Set<SootClass> parentClasses) {
        SootMethod method;
        if (currentClass == null || classLocal == null || (method = findMethod(currentClass, subsignature)) == null || AndroidEntryPointConstants.isLifecycleClass(method.getDeclaringClass().getName()) || SystemClassHandler.v().isClassInSystemPackage(method.getDeclaringClass().getName())) {
            return null;
        }
        if (!$assertionsDisabled && !method.isStatic() && classLocal == null) {
            throw new AssertionError("Class local was null for non-static method " + method.getSignature());
        }
        return buildMethodCall(method, classLocal, parentClasses);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean createPlainMethodCall(Local classLocal, SootMethod currentMethod) {
        if (AndroidEntryPointConstants.getServiceLifecycleMethods().contains(currentMethod.getSubSignature())) {
            return false;
        }
        NopStmt beforeStmt = Jimple.v().newNopStmt();
        NopStmt thenStmt = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) beforeStmt);
        createIfStmt(thenStmt);
        buildMethodCall(currentMethod, classLocal);
        this.body.getUnits().add((UnitPatchingChain) thenStmt);
        createIfStmt(beforeStmt);
        return true;
    }

    public void setEntryPointUtils(AndroidEntryPointUtils entryPointUtils) {
        this.entryPointUtils = entryPointUtils;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void createClassInstances(Collection<SootClass> classes) {
        Local l;
        for (SootClass callbackClass : classes) {
            NopStmt thenStmt = Jimple.v().newNopStmt();
            createIfStmt(thenStmt);
            if (this.localVarsForClasses.get(callbackClass) == null && (l = generateClassConstructor(callbackClass)) != null) {
                this.localVarsForClasses.put(callbackClass, l);
            }
            this.body.getUnits().add((UnitPatchingChain) thenStmt);
        }
    }
}
