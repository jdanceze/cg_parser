package soot.jimple.infoflow.android.entryPointCreators.components;

import soot.SootClass;
import soot.SootMethod;
import soot.UnitPatchingChain;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;
import soot.jimple.infoflow.android.entryPointCreators.AndroidEntryPointConstants;
import soot.jimple.infoflow.android.manifest.IManifestHandler;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/entryPointCreators/components/ContentProviderEntryPointCreator.class */
public class ContentProviderEntryPointCreator extends AbstractComponentEntryPointCreator {
    public ContentProviderEntryPointCreator(SootClass component, SootClass applicationClass, IManifestHandler manifest) {
        super(component, applicationClass, manifest);
    }

    @Override // soot.jimple.infoflow.android.entryPointCreators.components.AbstractComponentEntryPointCreator
    protected void generateComponentLifecycle() {
        UnitPatchingChain units = this.body.getUnits();
        NopStmt startWhileStmt = Jimple.v().newNopStmt();
        NopStmt endWhileStmt = Jimple.v().newNopStmt();
        units.add((UnitPatchingChain) startWhileStmt);
        createIfStmt(endWhileStmt);
        addCallbackMethods();
        NopStmt beforeCallbacksStmt = Jimple.v().newNopStmt();
        units.add((UnitPatchingChain) beforeCallbacksStmt);
        for (String methodSig : AndroidEntryPointConstants.getContentproviderLifecycleMethods()) {
            SootMethod sm = findMethod(this.component, methodSig);
            if (sm != null && !sm.getSubSignature().equals(AndroidEntryPointConstants.CONTENTPROVIDER_ONCREATE)) {
                NopStmt afterMethodStmt = Jimple.v().newNopStmt();
                createIfStmt(afterMethodStmt);
                buildMethodCall(sm, this.thisLocal);
                units.add((UnitPatchingChain) afterMethodStmt);
            }
        }
        createIfStmt(beforeCallbacksStmt);
        units.add((UnitPatchingChain) endWhileStmt);
    }
}
