package soot.jimple.toolkits.invoke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.Pack;
import soot.PackManager;
import soot.PhaseOptions;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.ExplicitEdgesPred;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.Targets;
import soot.jimple.toolkits.callgraph.TopologicalOrderer;
import soot.options.Options;
import soot.tagkit.Host;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/invoke/StaticInliner.class */
public class StaticInliner extends SceneTransformer {
    private static final Logger logger = LoggerFactory.getLogger(StaticInliner.class);
    private final HashMap<SootMethod, Integer> methodToOriginalSize = new HashMap<>();

    public StaticInliner(Singletons.Global g) {
    }

    public static StaticInliner v() {
        return G.v().soot_jimple_toolkits_invoke_StaticInliner();
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> options) {
        int inlinedSize;
        Filter explicitInvokesFilter = new Filter(new ExplicitEdgesPred());
        if (Options.v().verbose()) {
            logger.debug("[" + phaseName + "] Inlining methods...");
        }
        computeAverageMethodSizeAndSaveOriginalSizes();
        String modifierOptions = PhaseOptions.getString(options, "allowed-modifier-changes");
        ArrayList<Host[]> sitesToInline = new ArrayList<>();
        CallGraph cg = Scene.v().getCallGraph();
        TopologicalOrderer orderer = new TopologicalOrderer(cg);
        orderer.go();
        List<SootMethod> order = orderer.order();
        ListIterator<SootMethod> it = order.listIterator(order.size());
        while (it.hasPrevious()) {
            SootMethod container = it.previous();
            if (container.isConcrete() && this.methodToOriginalSize.containsKey(container) && explicitInvokesFilter.wrap(cg.edgesOutOf(container)).hasNext()) {
                Iterator it2 = new ArrayList(container.retrieveActiveBody().getUnits()).iterator();
                while (it2.hasNext()) {
                    Unit u = (Unit) it2.next();
                    Stmt s = (Stmt) u;
                    if (s.containsInvokeExpr()) {
                        Targets targets = new Targets(explicitInvokesFilter.wrap(cg.edgesOutOf(s)));
                        if (targets.hasNext()) {
                            SootMethod target = (SootMethod) targets.next();
                            if (!targets.hasNext() && target.isConcrete() && target.getDeclaringClass().isApplicationClass() && InlinerSafetyManager.ensureInlinability(target, s, container, modifierOptions)) {
                                sitesToInline.add(new Host[]{target, s, container});
                            }
                        }
                    }
                }
            }
        }
        float expansionFactor = PhaseOptions.getFloat(options, "expansion-factor");
        int maxContainerSize = PhaseOptions.getInt(options, "max-container-size");
        int maxInlineeSize = PhaseOptions.getInt(options, "max-inlinee-size");
        Pack jbPack = PhaseOptions.getBoolean(options, "rerun-jb") ? PackManager.v().getPack("jb") : null;
        Iterator<Host[]> it3 = sitesToInline.iterator();
        while (it3.hasNext()) {
            Host[] site = it3.next();
            SootMethod inlinee = (SootMethod) site[0];
            int inlineeSize = inlinee.retrieveActiveBody().getUnits().size();
            SootMethod container2 = (SootMethod) site[2];
            int containerSize = container2.retrieveActiveBody().getUnits().size();
            if (inlineeSize <= maxInlineeSize && (inlinedSize = inlineeSize + containerSize) <= maxContainerSize && inlinedSize <= expansionFactor * this.methodToOriginalSize.get(container2).intValue()) {
                Stmt invokeStmt = (Stmt) site[1];
                if (InlinerSafetyManager.ensureInlinability(inlinee, invokeStmt, container2, modifierOptions)) {
                    SiteInliner.inlineSite(inlinee, invokeStmt, container2, options);
                    if (jbPack != null) {
                        jbPack.apply(container2.getActiveBody());
                    }
                }
            }
        }
    }

    private void computeAverageMethodSizeAndSaveOriginalSizes() {
        for (SootClass c : Scene.v().getApplicationClasses()) {
            Iterator<SootMethod> methodsIt = c.methodIterator();
            while (methodsIt.hasNext()) {
                SootMethod m = methodsIt.next();
                if (m.isConcrete()) {
                    int size = m.retrieveActiveBody().getUnits().size();
                    this.methodToOriginalSize.put(m, Integer.valueOf(size));
                }
            }
        }
    }
}
