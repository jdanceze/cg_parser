package soot.shimple.toolkits.scalar;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.PhaseOptions;
import soot.Singletons;
import soot.Unit;
import soot.UnitBoxOwner;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.shimple.ShimpleBody;
import soot.shimple.toolkits.scalar.SEvaluator;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/shimple/toolkits/scalar/SConstantPropagatorAndFolder.class */
public class SConstantPropagatorAndFolder extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(SConstantPropagatorAndFolder.class);
    protected ShimpleBody sb;
    protected boolean debug;

    public SConstantPropagatorAndFolder(Singletons.Global g) {
    }

    public static SConstantPropagatorAndFolder v() {
        return G.v().soot_shimple_toolkits_scalar_SConstantPropagatorAndFolder();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        if (!(b instanceof ShimpleBody)) {
            throw new RuntimeException("SConstantPropagatorAndFolder requires a ShimpleBody.");
        }
        ShimpleBody castBody = (ShimpleBody) b;
        if (!castBody.isSSA()) {
            throw new RuntimeException("ShimpleBody is not in proper SSA form as required by SConstantPropagatorAndFolder. You may need to rebuild it or use ConstantPropagatorAndFolder instead.");
        }
        this.sb = castBody;
        this.debug = Options.v().debug() || castBody.getOptions().debug();
        if (Options.v().verbose()) {
            logger.debug("[" + castBody.getMethod().getName() + "] Propagating and folding constants (SSA)...");
        }
        SCPFAnalysis scpf = new SCPFAnalysis(ExceptionalUnitGraphFactory.createExceptionalUnitGraph(castBody));
        propagateResults(scpf.getResults());
        if (PhaseOptions.getBoolean(options, "prune-cfg")) {
            removeStmts(scpf.getDeadStmts());
            replaceStmts(scpf.getStmtsToReplace());
        }
    }

    protected void propagateResults(Map<Local, Constant> localToConstant) {
        ShimpleLocalDefs localDefs = new ShimpleLocalDefs(this.sb);
        ShimpleLocalUses localUses = new ShimpleLocalUses(this.sb);
        for (Local local : this.sb.getLocals()) {
            Constant constant = localToConstant.get(local);
            if (!(constant instanceof SEvaluator.MetaConstant)) {
                DefinitionStmt stmt = (DefinitionStmt) localDefs.getDefsOf(local).get(0);
                ValueBox defSrcBox = stmt.getRightOpBox();
                Value defSrcOld = defSrcBox.getValue();
                if (defSrcBox.canContainValue(constant)) {
                    defSrcBox.setValue(constant);
                    if (defSrcOld instanceof UnitBoxOwner) {
                        ((UnitBoxOwner) defSrcOld).clearUnitBoxes();
                    }
                } else if (this.debug) {
                    logger.warn("Couldn't propagate constant " + constant + " to box " + defSrcBox.getValue() + " in unit " + stmt);
                }
                for (UnitValueBoxPair pair : localUses.getUsesOf(local)) {
                    ValueBox useBox = pair.getValueBox();
                    if (useBox.canContainValue(constant)) {
                        useBox.setValue(constant);
                    } else if (this.debug) {
                        logger.warn("Couldn't propagate constant " + constant + " to box " + useBox.getValue() + " in unit " + pair.getUnit());
                    }
                }
            }
        }
    }

    protected void removeStmts(List<IfStmt> deadStmts) {
        Chain<Unit> units = this.sb.getUnits();
        for (IfStmt dead : deadStmts) {
            units.remove(dead);
            dead.clearUnitBoxes();
        }
    }

    protected void replaceStmts(Map<Stmt, GotoStmt> stmtsToReplace) {
        Chain<Unit> units = this.sb.getUnits();
        for (Map.Entry<Stmt, GotoStmt> e : stmtsToReplace.entrySet()) {
            units.swapWith(e.getKey(), e.getValue());
        }
    }
}
