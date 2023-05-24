package soot.jimple.infoflow.android.entryPointCreators.components;

import heros.TwoElementSet;
import java.util.Collections;
import java.util.List;
import soot.Local;
import soot.RefType;
import soot.SootClass;
import soot.Type;
import soot.UnitPatchingChain;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.entryPointCreators.AndroidEntryPointConstants;
import soot.jimple.infoflow.android.manifest.IManifestHandler;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/entryPointCreators/components/FragmentEntryPointCreator.class */
public class FragmentEntryPointCreator extends AbstractComponentEntryPointCreator {
    public FragmentEntryPointCreator(SootClass component, SootClass applicationClass, IManifestHandler manifest) {
        super(component, applicationClass, manifest);
    }

    @Override // soot.jimple.infoflow.android.entryPointCreators.components.AbstractComponentEntryPointCreator
    protected void generateComponentLifecycle() {
        Local lcActivity = this.body.getParameterLocal(getDefaultMainMethodParams().size());
        if (!(lcActivity.getType() instanceof RefType)) {
            throw new RuntimeException("Activities must be reference types");
        }
        RefType rtActivity = (RefType) lcActivity.getType();
        SootClass scActivity = rtActivity.getSootClass();
        TwoElementSet<SootClass> classAndFragment = new TwoElementSet<>(this.component, scActivity);
        NopStmt newNopStmt = Jimple.v().newNopStmt();
        createIfStmt(newNopStmt);
        searchAndBuildMethod(AndroidEntryPointConstants.ACTIVITY_ONATTACHFRAGMENT, this.component, this.thisLocal, classAndFragment);
        this.body.getUnits().add((UnitPatchingChain) newNopStmt);
        generateFragmentLifecycle(this.component, this.thisLocal, scActivity);
    }

    private void generateFragmentLifecycle(SootClass currentClass, Local classLocal, SootClass activity) {
        NopStmt endFragmentStmt = Jimple.v().newNopStmt();
        createIfStmt(endFragmentStmt);
        Stmt onAttachStmt = searchAndBuildMethod(AndroidEntryPointConstants.FRAGMENT_ONATTACH, currentClass, classLocal, Collections.singleton(activity));
        if (onAttachStmt == null) {
            UnitPatchingChain units = this.body.getUnits();
            NopStmt newNopStmt = Jimple.v().newNopStmt();
            onAttachStmt = newNopStmt;
            units.add((UnitPatchingChain) newNopStmt);
        }
        Stmt onCreateStmt = searchAndBuildMethod("void onCreate(android.os.Bundle)", currentClass, classLocal);
        if (onCreateStmt == null) {
            this.body.getUnits().add((UnitPatchingChain) Jimple.v().newNopStmt());
        }
        Stmt onCreateViewStmt = searchAndBuildMethod(AndroidEntryPointConstants.FRAGMENT_ONCREATEVIEW, currentClass, classLocal);
        if (onCreateViewStmt == null) {
            UnitPatchingChain units2 = this.body.getUnits();
            NopStmt newNopStmt2 = Jimple.v().newNopStmt();
            onCreateViewStmt = newNopStmt2;
            units2.add((UnitPatchingChain) newNopStmt2);
        }
        Stmt onViewCreatedStmt = searchAndBuildMethod(AndroidEntryPointConstants.FRAGMENT_ONVIEWCREATED, currentClass, classLocal);
        if (onViewCreatedStmt == null) {
            this.body.getUnits().add((UnitPatchingChain) Jimple.v().newNopStmt());
        }
        Stmt onActCreatedStmt = searchAndBuildMethod(AndroidEntryPointConstants.FRAGMENT_ONACTIVITYCREATED, currentClass, classLocal);
        if (onActCreatedStmt == null) {
            this.body.getUnits().add((UnitPatchingChain) Jimple.v().newNopStmt());
        }
        Stmt onStartStmt = searchAndBuildMethod("void onStart()", currentClass, classLocal);
        if (onStartStmt == null) {
            UnitPatchingChain units3 = this.body.getUnits();
            NopStmt newNopStmt3 = Jimple.v().newNopStmt();
            onStartStmt = newNopStmt3;
            units3.add((UnitPatchingChain) newNopStmt3);
        }
        NopStmt newNopStmt4 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt4);
        searchAndBuildMethod("void onResume()", currentClass, classLocal);
        addCallbackMethods();
        searchAndBuildMethod("void onPause()", currentClass, classLocal);
        createIfStmt(newNopStmt4);
        searchAndBuildMethod("void onSaveInstanceState(android.os.Bundle)", currentClass, classLocal);
        searchAndBuildMethod("void onStop()", currentClass, classLocal);
        createIfStmt(onCreateViewStmt);
        createIfStmt(onStartStmt);
        searchAndBuildMethod(AndroidEntryPointConstants.FRAGMENT_ONDESTROYVIEW, currentClass, classLocal);
        createIfStmt(onCreateViewStmt);
        searchAndBuildMethod("void onDestroy()", currentClass, classLocal);
        searchAndBuildMethod(AndroidEntryPointConstants.FRAGMENT_ONDETACH, currentClass, classLocal);
        createIfStmt(onAttachStmt);
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(classLocal, NullConstant.v()));
        this.body.getUnits().add((UnitPatchingChain) endFragmentStmt);
    }

    @Override // soot.jimple.infoflow.android.entryPointCreators.components.AbstractComponentEntryPointCreator
    protected List<Type> getAdditionalMainMethodParams() {
        return Collections.singletonList(RefType.v(AndroidEntryPointConstants.ACTIVITYCLASS));
    }
}
