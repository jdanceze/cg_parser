package soot.jimple.infoflow.android.entryPointCreators.components;

import soot.SootClass;
import soot.UnitPatchingChain;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.entryPointCreators.AndroidEntryPointConstants;
import soot.jimple.infoflow.android.manifest.IManifestHandler;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/entryPointCreators/components/BroadcastReceiverEntryPointCreator.class */
public class BroadcastReceiverEntryPointCreator extends AbstractComponentEntryPointCreator {
    public BroadcastReceiverEntryPointCreator(SootClass component, SootClass applicationClass, IManifestHandler manifest) {
        super(component, applicationClass, manifest);
    }

    @Override // soot.jimple.infoflow.android.entryPointCreators.components.AbstractComponentEntryPointCreator
    protected void generateComponentLifecycle() {
        Stmt onReceiveStmt = searchAndBuildMethod(AndroidEntryPointConstants.BROADCAST_ONRECEIVE, this.component, this.thisLocal);
        NopStmt startWhileStmt = Jimple.v().newNopStmt();
        NopStmt endWhileStmt = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) startWhileStmt);
        createIfStmt(endWhileStmt);
        addCallbackMethods();
        this.body.getUnits().add((UnitPatchingChain) endWhileStmt);
        createIfStmt(onReceiveStmt);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator
    public void createAdditionalMethods() {
        super.createAdditionalMethods();
        createGetIntentMethod();
    }
}
