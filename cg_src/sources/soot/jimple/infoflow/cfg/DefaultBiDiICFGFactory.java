package soot.jimple.infoflow.cfg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.dexpler.DalvikThrowAnalysis;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;
import soot.jimple.toolkits.ide.icfg.OnTheFlyJimpleBasedICFG;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/cfg/DefaultBiDiICFGFactory.class */
public class DefaultBiDiICFGFactory implements BiDirICFGFactory {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    protected boolean isAndroid = false;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DefaultBiDiICFGFactory.class.desiredAssertionStatus();
    }

    @Override // soot.jimple.infoflow.cfg.BiDirICFGFactory
    public IInfoflowCFG buildBiDirICFG(InfoflowConfiguration.CallgraphAlgorithm callgraphAlgorithm, boolean enableExceptions) {
        if (callgraphAlgorithm == InfoflowConfiguration.CallgraphAlgorithm.OnDemand) {
            long beforeClassLoading = System.nanoTime();
            OnTheFlyJimpleBasedICFG.loadAllClassesOnClassPathToSignatures();
            this.logger.info("Class loading took {} seconds", Double.valueOf((System.nanoTime() - beforeClassLoading) / 1.0E9d));
            long beforeHierarchy = System.nanoTime();
            Scene.v().getOrMakeFastHierarchy();
            if ($assertionsDisabled || Scene.v().hasFastHierarchy()) {
                this.logger.info("Hierarchy building took {} seconds", Double.valueOf((System.nanoTime() - beforeHierarchy) / 1.0E9d));
                long beforeCFG = System.nanoTime();
                IInfoflowCFG cfg = new InfoflowCFG(new OnTheFlyJimpleBasedICFG(Scene.v().getEntryPoints()));
                this.logger.info("CFG generation took {} seconds", Double.valueOf((System.nanoTime() - beforeCFG) / 1.0E9d));
                return cfg;
            }
            throw new AssertionError();
        }
        BiDiInterproceduralCFG<Unit, SootMethod> baseCFG = getBaseCFG(enableExceptions);
        return new InfoflowCFG(baseCFG);
    }

    protected BiDiInterproceduralCFG<Unit, SootMethod> getBaseCFG(boolean enableExceptions) {
        JimpleBasedInterproceduralCFG baseCFG;
        if (this.isAndroid) {
            baseCFG = new JimpleBasedInterproceduralCFG(enableExceptions, true) { // from class: soot.jimple.infoflow.cfg.DefaultBiDiICFGFactory.1
                @Override // soot.jimple.toolkits.ide.icfg.AbstractJimpleBasedICFG
                protected DirectedGraph<Unit> makeGraph(Body body) {
                    return this.enableExceptions ? ExceptionalUnitGraphFactory.createExceptionalUnitGraph(body, DalvikThrowAnalysis.interproc(), true) : new BriefUnitGraph(body);
                }
            };
        } else {
            baseCFG = new JimpleBasedInterproceduralCFG(enableExceptions, true);
        }
        baseCFG.setIncludePhantomCallees(true);
        return baseCFG;
    }

    public void setIsAndroid(boolean isAndroid) {
        this.isAndroid = isAndroid;
    }
}
