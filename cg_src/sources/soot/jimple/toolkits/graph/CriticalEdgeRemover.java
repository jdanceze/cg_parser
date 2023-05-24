package soot.jimple.toolkits.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Unit;
import soot.UnitBox;
import soot.jimple.Jimple;
import soot.options.Options;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/graph/CriticalEdgeRemover.class */
public class CriticalEdgeRemover extends BodyTransformer {
    private static final Logger logger;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CriticalEdgeRemover.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(CriticalEdgeRemover.class);
    }

    public CriticalEdgeRemover(Singletons.Global g) {
    }

    public static CriticalEdgeRemover v() {
        return G.v().soot_jimple_toolkits_graph_CriticalEdgeRemover();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        if (Options.v().verbose()) {
            logger.debug("[" + b.getMethod().getName() + "]     Removing Critical Edges...");
        }
        removeCriticalEdges(b);
        if (Options.v().verbose()) {
            logger.debug("[" + b.getMethod().getName() + "]     Removing Critical Edges done.");
        }
    }

    private static Unit insertGotoAfter(Chain<Unit> unitChain, Unit node, Unit target) {
        Unit newGoto = Jimple.v().newGotoStmt(target);
        unitChain.insertAfter(newGoto, node);
        return newGoto;
    }

    private static Unit insertGotoBefore(Chain<Unit> unitChain, Unit node, Unit target) {
        Unit newGoto = Jimple.v().newGotoStmt(target);
        unitChain.insertBefore(newGoto, node);
        newGoto.redirectJumpsToThisTo(node);
        return newGoto;
    }

    private static void redirectBranch(Unit node, Unit oldTarget, Unit newTarget) {
        for (UnitBox targetBox : node.getUnitBoxes()) {
            if (targetBox.getUnit() == oldTarget) {
                targetBox.setUnit(newTarget);
            }
        }
    }

    private void removeCriticalEdges(Body b) {
        Unit insertGotoAfter;
        Chain<Unit> unitChain = b.getUnits();
        Map<Unit, List<Unit>> predecessors = new HashMap<>((2 * unitChain.size()) + 1, 0.7f);
        Iterator<Unit> unitIt = unitChain.snapshotIterator();
        while (unitIt.hasNext()) {
            Unit currentUnit = unitIt.next();
            for (UnitBox ub : currentUnit.getUnitBoxes()) {
                Unit target = ub.getUnit();
                List<Unit> predList = predecessors.get(target);
                if (predList == null) {
                    List<Unit> arrayList = new ArrayList<>();
                    predList = arrayList;
                    predecessors.put(target, arrayList);
                }
                predList.add(currentUnit);
            }
        }
        Unit currentUnit2 = null;
        Iterator<Unit> unitIt2 = unitChain.snapshotIterator();
        while (unitIt2.hasNext()) {
            Unit directPredecessor = currentUnit2;
            currentUnit2 = unitIt2.next();
            List<Unit> predList2 = predecessors.get(currentUnit2);
            int nbPreds = predList2 == null ? 0 : predList2.size();
            if (directPredecessor != null && directPredecessor.fallsThrough()) {
                nbPreds++;
            }
            if (nbPreds >= 2) {
                if (!$assertionsDisabled && predList2 == null) {
                    throw new AssertionError();
                }
                if (directPredecessor != null && directPredecessor.fallsThrough()) {
                    directPredecessor = insertGotoAfter(unitChain, directPredecessor, currentUnit2);
                }
                for (Unit predecessor : predList2) {
                    int nbSuccs = predecessor.getUnitBoxes().size() + (predecessor.fallsThrough() ? 1 : 0);
                    if (nbSuccs >= 2) {
                        if (directPredecessor == null) {
                            insertGotoAfter = insertGotoBefore(unitChain, currentUnit2, currentUnit2);
                        } else {
                            insertGotoAfter = insertGotoAfter(unitChain, directPredecessor, currentUnit2);
                        }
                        directPredecessor = insertGotoAfter;
                        redirectBranch(predecessor, currentUnit2, directPredecessor);
                    }
                }
            }
        }
    }
}
