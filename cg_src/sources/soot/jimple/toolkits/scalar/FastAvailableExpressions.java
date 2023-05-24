package soot.jimple.toolkits.scalar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.EquivalentValue;
import soot.SideEffectTester;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.util.Chain;
import soot.util.HashChain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/FastAvailableExpressions.class */
public class FastAvailableExpressions implements AvailableExpressions {
    private static final Logger logger = LoggerFactory.getLogger(FastAvailableExpressions.class);
    protected final Map<Unit, List<UnitValueBoxPair>> unitToPairsAfter;
    protected final Map<Unit, List<UnitValueBoxPair>> unitToPairsBefore;
    protected final Map<Unit, Chain<EquivalentValue>> unitToEquivsAfter;
    protected final Map<Unit, Chain<EquivalentValue>> unitToEquivsBefore;

    public FastAvailableExpressions(Body b, SideEffectTester st) {
        if (Options.v().verbose()) {
            logger.debug("[" + b.getMethod().getName() + "] Finding available expressions...");
        }
        Chain<Unit> units = b.getUnits();
        this.unitToPairsAfter = new HashMap((units.size() * 2) + 1, 0.7f);
        this.unitToPairsBefore = new HashMap((units.size() * 2) + 1, 0.7f);
        this.unitToEquivsAfter = new HashMap((units.size() * 2) + 1, 0.7f);
        this.unitToEquivsBefore = new HashMap((units.size() * 2) + 1, 0.7f);
        FastAvailableExpressionsAnalysis analysis = new FastAvailableExpressionsAnalysis(ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b), b.getMethod(), st);
        for (Unit s : units) {
            FlowSet<Value> set = analysis.getFlowBefore(s);
            if ((set instanceof ToppedSet) && ((ToppedSet) set).isTop()) {
                throw new RuntimeException("top! on " + s);
            }
            List<UnitValueBoxPair> pairsBefore = new ArrayList<>();
            Chain<EquivalentValue> equivsBefore = new HashChain<>();
            for (Value v : set) {
                Stmt containingStmt = (Stmt) analysis.rhsToContainingStmt.get(v);
                pairsBefore.add(new UnitValueBoxPair(containingStmt, ((AssignStmt) containingStmt).getRightOpBox()));
                EquivalentValue ev = new EquivalentValue(v);
                if (!equivsBefore.contains(ev)) {
                    equivsBefore.add(ev);
                }
            }
            this.unitToPairsBefore.put(s, pairsBefore);
            this.unitToEquivsBefore.put(s, equivsBefore);
            List<UnitValueBoxPair> pairsAfter = new ArrayList<>();
            Chain<EquivalentValue> equivsAfter = new HashChain<>();
            for (Value v2 : analysis.getFlowAfter(s)) {
                Stmt containingStmt2 = (Stmt) analysis.rhsToContainingStmt.get(v2);
                pairsAfter.add(new UnitValueBoxPair(containingStmt2, ((AssignStmt) containingStmt2).getRightOpBox()));
                EquivalentValue ev2 = new EquivalentValue(v2);
                if (!equivsAfter.contains(ev2)) {
                    equivsAfter.add(ev2);
                }
            }
            this.unitToPairsAfter.put(s, pairsAfter);
            this.unitToEquivsAfter.put(s, equivsAfter);
        }
        if (Options.v().verbose()) {
            logger.debug("[" + b.getMethod().getName() + "]     Found available expressions...");
        }
    }

    @Override // soot.jimple.toolkits.scalar.AvailableExpressions
    public List<UnitValueBoxPair> getAvailablePairsBefore(Unit u) {
        return this.unitToPairsBefore.get(u);
    }

    @Override // soot.jimple.toolkits.scalar.AvailableExpressions
    public Chain<EquivalentValue> getAvailableEquivsBefore(Unit u) {
        return this.unitToEquivsBefore.get(u);
    }

    @Override // soot.jimple.toolkits.scalar.AvailableExpressions
    public List<UnitValueBoxPair> getAvailablePairsAfter(Unit u) {
        return this.unitToPairsAfter.get(u);
    }

    @Override // soot.jimple.toolkits.scalar.AvailableExpressions
    public Chain<EquivalentValue> getAvailableEquivsAfter(Unit u) {
        return this.unitToEquivsAfter.get(u);
    }
}
