package soot.jimple.toolkits.callgraph;

import java.util.ArrayList;
import java.util.List;
import soot.EntryPoints;
import soot.PhaseOptions;
import soot.RadioScenePack;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.options.CGOptions;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/CallGraphPack.class */
public class CallGraphPack extends RadioScenePack {
    public CallGraphPack(String name) {
        super(name);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.RadioScenePack, soot.ScenePack, soot.Pack
    public void internalApply() {
        CGOptions options = new CGOptions(PhaseOptions.v().getPhaseOptions(this));
        if (!Scene.v().hasCustomEntryPoints()) {
            if (!options.implicit_entry()) {
                Scene.v().setEntryPoints(EntryPoints.v().application());
            }
            if (options.all_reachable()) {
                List<SootMethod> entryPoints = new ArrayList<>();
                entryPoints.addAll(EntryPoints.v().all());
                entryPoints.addAll(EntryPoints.v().methodsOfApplicationClasses());
                Scene.v().setEntryPoints(entryPoints);
            }
        }
        super.internalApply();
        if (options.trim_clinit()) {
            ClinitElimTransformer trimmer = new ClinitElimTransformer();
            for (SootClass cl : Scene.v().getClasses(3)) {
                for (SootMethod m : cl.getMethods()) {
                    if (m.isConcrete() && m.hasActiveBody()) {
                        trimmer.transform(m.getActiveBody());
                    }
                }
            }
        }
    }
}
