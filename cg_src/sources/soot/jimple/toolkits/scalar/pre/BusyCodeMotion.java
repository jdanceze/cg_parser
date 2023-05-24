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
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.NaiveSideEffectTester;
import soot.jimple.toolkits.graph.CriticalEdgeRemover;
import soot.jimple.toolkits.pointer.PASideEffectTester;
import soot.jimple.toolkits.scalar.LocalCreation;
import soot.options.BCMOptions;
import soot.options.Options;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;
import soot.util.UnitMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/pre/BusyCodeMotion.class */
public class BusyCodeMotion extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(BusyCodeMotion.class);
    private static final String PREFIX = "$bcm";

    public BusyCodeMotion(Singletons.Global g) {
    }

    public static BusyCodeMotion v() {
        return G.v().soot_jimple_toolkits_scalar_pre_BusyCodeMotion();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> opts) {
        SideEffectTester sideEffect;
        Local helper;
        BCMOptions options = new BCMOptions(opts);
        if (Options.v().verbose()) {
            logger.debug("[" + b.getMethod().getName() + "]     performing Busy Code Motion...");
        }
        CriticalEdgeRemover.v().transform(b, String.valueOf(phaseName) + ".cer");
        UnitGraph graph = new BriefUnitGraph(b);
        Map<Unit, EquivalentValue> unitToEquivRhs = new UnitMap<EquivalentValue>(b, graph.size() + 1, 0.7f) { // from class: soot.jimple.toolkits.scalar.pre.BusyCodeMotion.1
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
        Map<Unit, EquivalentValue> unitToNoExceptionEquivRhs = new UnitMap<EquivalentValue>(b, graph.size() + 1, 0.7f) { // from class: soot.jimple.toolkits.scalar.pre.BusyCodeMotion.2
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // soot.util.UnitMap
            public EquivalentValue mapTo(Unit unit) {
                return SootFilter.equiVal(SootFilter.noExceptionThrowing(SootFilter.binopRhs(unit)));
            }
        };
        Scene sc = Scene.v();
        if (sc.hasCallGraph() && !options.naive_side_effect()) {
            sideEffect = new PASideEffectTester();
        } else {
            sideEffect = new NaiveSideEffectTester();
        }
        sideEffect.newMethod(b.getMethod());
        UpSafetyAnalysis upSafe = new UpSafetyAnalysis(graph, unitToEquivRhs, sideEffect);
        DownSafetyAnalysis downSafe = new DownSafetyAnalysis(graph, unitToNoExceptionEquivRhs, sideEffect);
        EarliestnessComputation earliest = new EarliestnessComputation(graph, upSafe, downSafe, sideEffect);
        LocalCreation localCreation = sc.createLocalCreation(b.getLocals(), PREFIX);
        HashMap<EquivalentValue, Local> expToHelper = new HashMap<>();
        Chain<Unit> unitChain = b.getUnits();
        Iterator<Unit> unitIt = unitChain.snapshotIterator();
        while (unitIt.hasNext()) {
            Unit currentUnit = unitIt.next();
            for (EquivalentValue equiVal : earliest.getFlowBefore(currentUnit)) {
                Local helper2 = expToHelper.get(equiVal);
                if (helper2 == null) {
                    helper2 = localCreation.newLocal(equiVal.getType());
                    expToHelper.put(equiVal, helper2);
                }
                if (currentUnit instanceof IdentityStmt) {
                    currentUnit = getFirstNonIdentityStmt(b);
                }
                Value insertValue = Jimple.cloneIfNecessary(equiVal.getValue());
                Unit firstComp = Jimple.v().newAssignStmt(helper2, insertValue);
                unitChain.insertBefore(firstComp, currentUnit);
            }
        }
        for (Unit currentUnit2 : unitChain) {
            EquivalentValue rhs = unitToEquivRhs.get(currentUnit2);
            if (rhs != null && (helper = expToHelper.get(rhs)) != null) {
                ((AssignStmt) currentUnit2).setRightOp(helper);
            }
        }
        if (Options.v().verbose()) {
            logger.debug("[" + b.getMethod().getName() + "]     Busy Code Motion done!");
        }
    }

    private Unit getFirstNonIdentityStmt(Body b) {
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (!(u instanceof IdentityStmt)) {
                return u;
            }
        }
        return null;
    }
}
