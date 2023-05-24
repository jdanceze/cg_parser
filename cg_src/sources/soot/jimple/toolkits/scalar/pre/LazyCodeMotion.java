package soot.jimple.toolkits.scalar.pre;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.EquivalentValue;
import soot.G;
import soot.Local;
import soot.Scene;
import soot.SideEffectTester;
import soot.Singletons;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.NaiveSideEffectTester;
import soot.jimple.toolkits.graph.CriticalEdgeRemover;
import soot.jimple.toolkits.graph.LoopConditionUnroller;
import soot.jimple.toolkits.pointer.PASideEffectTester;
import soot.jimple.toolkits.scalar.LocalCreation;
import soot.options.LCMOptions;
import soot.options.Options;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArrayPackedSet;
import soot.toolkits.scalar.BoundedFlowSet;
import soot.toolkits.scalar.CollectionFlowUniverse;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.FlowUniverse;
import soot.util.Chain;
import soot.util.UnitMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/pre/LazyCodeMotion.class */
public class LazyCodeMotion extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(LazyCodeMotion.class);
    private static final String PREFIX = "$lcm";

    public LazyCodeMotion(Singletons.Global g) {
    }

    public static LazyCodeMotion v() {
        return G.v().soot_jimple_toolkits_scalar_pre_LazyCodeMotion();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> opts) {
        SideEffectTester sideEffect;
        UpSafetyAnalysis upSafe;
        DownSafetyAnalysis downSafe;
        Local helper;
        LCMOptions options = new LCMOptions(opts);
        HashMap<EquivalentValue, Local> expToHelper = new HashMap<>();
        Chain<Unit> unitChain = b.getUnits();
        if (Options.v().verbose()) {
            logger.debug("[" + b.getMethod().getName() + "] Performing Lazy Code Motion...");
        }
        if (options.unroll()) {
            new LoopConditionUnroller().transform(b, String.valueOf(phaseName) + ".lcu");
        }
        CriticalEdgeRemover.v().transform(b, String.valueOf(phaseName) + ".cer");
        UnitGraph graph = new BriefUnitGraph(b);
        Map<Unit, EquivalentValue> unitToEquivRhs = new UnitMap<EquivalentValue>(b, graph.size() + 1, 0.7f) { // from class: soot.jimple.toolkits.scalar.pre.LazyCodeMotion.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // soot.util.UnitMap
            public EquivalentValue mapTo(Unit unit) {
                Value tmp = SootFilter.noInvokeRhs(unit);
                Value tmp2 = SootFilter.binop(tmp);
                if (tmp2 == null) {
                    tmp2 = SootFilter.concreteRef(tmp);
                }
                return SootFilter.equiVal(tmp2);
            }
        };
        Map<Unit, EquivalentValue> unitToNoExceptionEquivRhs = new UnitMap<EquivalentValue>(b, graph.size() + 1, 0.7f) { // from class: soot.jimple.toolkits.scalar.pre.LazyCodeMotion.2
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // soot.util.UnitMap
            public EquivalentValue mapTo(Unit unit) {
                return SootFilter.equiVal(SootFilter.noExceptionThrowing(SootFilter.binopRhs(unit)));
            }
        };
        FlowUniverse<EquivalentValue> universe = new CollectionFlowUniverse<>(unitToEquivRhs.values());
        BoundedFlowSet<EquivalentValue> set = new ArrayPackedSet<>(universe);
        Scene sc = Scene.v();
        if (sc.hasCallGraph() && !options.naive_side_effect()) {
            sideEffect = new PASideEffectTester();
        } else {
            sideEffect = new NaiveSideEffectTester();
        }
        sideEffect.newMethod(b.getMethod());
        if (options.safety() == 1) {
            upSafe = new UpSafetyAnalysis(graph, unitToNoExceptionEquivRhs, sideEffect, set);
        } else {
            upSafe = new UpSafetyAnalysis(graph, unitToEquivRhs, sideEffect, set);
        }
        if (options.safety() == 3) {
            downSafe = new DownSafetyAnalysis(graph, unitToEquivRhs, sideEffect, set);
        } else {
            downSafe = new DownSafetyAnalysis(graph, unitToNoExceptionEquivRhs, sideEffect, set);
            for (Unit currentUnit : unitChain) {
                EquivalentValue rhs = unitToEquivRhs.get(currentUnit);
                if (rhs != null) {
                    downSafe.getFlowBefore(currentUnit).add(rhs);
                }
            }
        }
        EarliestnessComputation earliest = new EarliestnessComputation(graph, upSafe, downSafe, sideEffect, set);
        DelayabilityAnalysis delay = new DelayabilityAnalysis(graph, earliest, unitToEquivRhs, set);
        LatestComputation latest = new LatestComputation(graph, delay, unitToEquivRhs, set);
        NotIsolatedAnalysis notIsolated = new NotIsolatedAnalysis(graph, latest, unitToEquivRhs, set);
        LocalCreation localCreation = sc.createLocalCreation(b.getLocals(), PREFIX);
        Iterator<Unit> unitIt = unitChain.snapshotIterator();
        while (unitIt.hasNext()) {
            Unit currentUnit2 = unitIt.next();
            FlowSet<EquivalentValue> latestSet = latest.getFlowBefore(currentUnit2);
            FlowSet<EquivalentValue> notIsolatedSet = notIsolated.getFlowAfter(currentUnit2);
            FlowSet<EquivalentValue> insertHere = latestSet.mo2534clone();
            insertHere.intersection(notIsolatedSet);
            for (EquivalentValue equiVal : insertHere) {
                Local helper2 = expToHelper.get(equiVal);
                if (helper2 == null) {
                    helper2 = localCreation.newLocal(equiVal.getType());
                    expToHelper.put(equiVal, helper2);
                }
                Value insertValue = Jimple.cloneIfNecessary(equiVal.getValue());
                Unit firstComp = Jimple.v().newAssignStmt(helper2, insertValue);
                unitChain.insertBefore(firstComp, currentUnit2);
            }
        }
        for (Unit currentUnit3 : unitChain) {
            EquivalentValue rhs2 = unitToEquivRhs.get(currentUnit3);
            if (rhs2 != null) {
                FlowSet<EquivalentValue> latestSet2 = latest.getFlowBefore(currentUnit3);
                FlowSet<EquivalentValue> notIsolatedSet2 = notIsolated.getFlowAfter(currentUnit3);
                if (!latestSet2.contains(rhs2) && notIsolatedSet2.contains(rhs2) && (helper = expToHelper.get(rhs2)) != null) {
                    try {
                        ((AssignStmt) currentUnit3).setRightOp(helper);
                    } catch (RuntimeException e) {
                        logger.debug("Error on " + b.getMethod().getName());
                        logger.debug(currentUnit3.toString());
                        logger.debug(String.valueOf(latestSet2));
                        logger.debug(String.valueOf(notIsolatedSet2));
                        throw e;
                    }
                }
            }
        }
        if (Options.v().verbose()) {
            logger.debug("[" + b.getMethod().getName() + "]     Lazy Code Motion done.");
        }
    }
}
