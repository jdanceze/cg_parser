package soot.jimple.infoflow.android.entryPointCreators.components;

import soot.SootClass;
import soot.UnitPatchingChain;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;
import soot.jimple.infoflow.android.entryPointCreators.AndroidEntryPointConstants;
import soot.jimple.infoflow.android.manifest.IManifestHandler;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/entryPointCreators/components/ServiceConnectionEntryPointCreator.class */
public class ServiceConnectionEntryPointCreator extends AbstractComponentEntryPointCreator {
    public ServiceConnectionEntryPointCreator(SootClass component, SootClass applicationClass, IManifestHandler manifest) {
        super(component, applicationClass, manifest);
    }

    @Override // soot.jimple.infoflow.android.entryPointCreators.components.AbstractComponentEntryPointCreator
    protected void generateComponentLifecycle() {
        searchAndBuildMethod(AndroidEntryPointConstants.SERVICECONNECTION_ONSERVICECONNECTED, this.component, this.thisLocal);
        NopStmt startWhileStmt = Jimple.v().newNopStmt();
        NopStmt endWhileStmt = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) startWhileStmt);
        createIfStmt(endWhileStmt);
        addCallbackMethods();
        this.body.getUnits().add((UnitPatchingChain) endWhileStmt);
        createIfStmt(startWhileStmt);
        searchAndBuildMethod(AndroidEntryPointConstants.SERVICECONNECTION_ONSERVICEDISCONNECTED, this.component, this.thisLocal);
    }
}
