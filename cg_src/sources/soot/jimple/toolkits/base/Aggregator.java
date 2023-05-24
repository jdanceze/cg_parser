package soot.jimple.toolkits.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import soot.Timers;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.MonitorStmt;
import soot.jimple.Stmt;
import soot.jimple.StmtBody;
import soot.options.Options;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.graph.PseudoTopologicalOrderer;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/base/Aggregator.class */
public class Aggregator extends BodyTransformer {
    private static final Logger logger;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Aggregator.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(Aggregator.class);
    }

    public Aggregator(Singletons.Global g) {
    }

    public static Aggregator v() {
        return G.v().soot_jimple_toolkits_base_Aggregator();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        StmtBody body = (StmtBody) b;
        boolean time = Options.v().time();
        if (time) {
            Timers.v().aggregationTimer.start();
        }
        Map<ValueBox, Zone> boxToZone = new HashMap<>((body.getUnits().size() * 2) + 1, 0.7f);
        Zonation zonation = new Zonation(body);
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Zone zone = zonation.getZoneOf(u);
            for (ValueBox box : u.getUseAndDefBoxes()) {
                boxToZone.put(box, zone);
            }
        }
        boolean onlyStackVars = PhaseOptions.getBoolean(options, "only-stack-locals");
        int aggregateCount = Options.v().verbose() ? 1 : 0;
        do {
            if (aggregateCount != 0) {
                logger.debug("[" + body.getMethod().getName() + "] Aggregating iteration " + aggregateCount + "...");
                aggregateCount++;
            }
        } while (internalAggregate(body, boxToZone, onlyStackVars));
        if (time) {
            Timers.v().aggregationTimer.end();
        }
    }

    private static boolean internalAggregate(StmtBody body, Map<ValueBox, Zone> boxToZone, boolean onlyStackVars) {
        List<Unit> path;
        boolean hadAggregation = false;
        Chain<Unit> units = body.getUnits();
        ExceptionalUnitGraph graph = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(body);
        LocalDefs localDefs = G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs(graph);
        LocalUses localUses = LocalUses.Factory.newLocalUses(body, localDefs);
        for (Unit u : new PseudoTopologicalOrderer().newList(graph, false)) {
            if (u instanceof AssignStmt) {
                AssignStmt s = (AssignStmt) u;
                Value lhs = s.getLeftOp();
                if (lhs instanceof Local) {
                    Local lhsLocal = (Local) lhs;
                    if (!onlyStackVars || lhsLocal.isStackLocal()) {
                        List<UnitValueBoxPair> lu = localUses.getUsesOf(s);
                        if (lu.size() != 1) {
                            continue;
                        } else {
                            UnitValueBoxPair usepair = lu.get(0);
                            Unit usepairUnit = usepair.unit;
                            ValueBox usepairValueBox = usepair.valueBox;
                            if (localDefs.getDefsOfAt(lhsLocal, usepairUnit).size() == 1 && boxToZone.get(s.getRightOpBox()) == boxToZone.get(usepairValueBox) && (path = graph.getExtendedBasicBlockPathBetween(s, usepairUnit)) != null) {
                                boolean propagatingInvokeExpr = false;
                                boolean propagatingFieldRef = false;
                                boolean propagatingArrayRef = false;
                                ArrayList<FieldRef> fieldRefList = new ArrayList<>();
                                HashSet<Value> localsUsed = new HashSet<>();
                                for (ValueBox vb : s.getUseBoxes()) {
                                    Value v = vb.getValue();
                                    if (v instanceof Local) {
                                        localsUsed.add(v);
                                    } else if (v instanceof InvokeExpr) {
                                        propagatingInvokeExpr = true;
                                    } else if (v instanceof ArrayRef) {
                                        propagatingArrayRef = true;
                                    } else if (v instanceof FieldRef) {
                                        propagatingFieldRef = true;
                                        fieldRefList.add((FieldRef) v);
                                    }
                                }
                                Iterator<Unit> pathIt = path.iterator();
                                if (!$assertionsDisabled && !pathIt.hasNext()) {
                                    throw new AssertionError();
                                }
                                pathIt.next();
                                while (true) {
                                    if (pathIt.hasNext()) {
                                        Stmt between = (Stmt) pathIt.next();
                                        if (between != usepairUnit) {
                                            if (!propagatingInvokeExpr || !(between instanceof MonitorStmt)) {
                                                for (ValueBox vb2 : between.getDefBoxes()) {
                                                    Value v2 = vb2.getValue();
                                                    if (!localsUsed.contains(v2)) {
                                                        if (v2 instanceof FieldRef) {
                                                            if (!propagatingInvokeExpr) {
                                                                if (propagatingFieldRef) {
                                                                    Iterator<FieldRef> it = fieldRefList.iterator();
                                                                    while (it.hasNext()) {
                                                                        FieldRef fieldRef = it.next();
                                                                        if (isSameField((FieldRef) v2, fieldRef)) {
                                                                            break;
                                                                        }
                                                                    }
                                                                    continue;
                                                                } else {
                                                                    continue;
                                                                }
                                                            }
                                                        } else if (!(v2 instanceof ArrayRef)) {
                                                            continue;
                                                        } else if (!propagatingInvokeExpr && !propagatingArrayRef) {
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (propagatingInvokeExpr || propagatingFieldRef || propagatingArrayRef) {
                                            for (ValueBox box : between.getUseBoxes()) {
                                                if (between != usepairUnit || box != usepairValueBox) {
                                                    Value v3 = box.getValue();
                                                    if (!(v3 instanceof InvokeExpr) && (!propagatingInvokeExpr || (!(v3 instanceof FieldRef) && !(v3 instanceof ArrayRef)))) {
                                                    }
                                                }
                                            }
                                            continue;
                                        }
                                    } else {
                                        Value aggregatee = s.getRightOp();
                                        if (usepairValueBox.canContainValue(aggregatee)) {
                                            boolean wasSimpleCopy = isSimpleCopy(usepairUnit);
                                            usepairValueBox.setValue(aggregatee);
                                            units.remove(s);
                                            if (wasSimpleCopy) {
                                                usepairUnit.addAllTagsOf(s);
                                            }
                                            hadAggregation = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    continue;
                }
            }
        }
        return hadAggregation;
    }

    private static boolean isSameField(FieldRef ref1, FieldRef ref2) {
        return ref1 == ref2 || ref1.getFieldRef().equals(ref2.getFieldRef());
    }

    private static boolean isSimpleCopy(Unit u) {
        if (!(u instanceof DefinitionStmt)) {
            return false;
        }
        DefinitionStmt defstmt = (DefinitionStmt) u;
        return (defstmt.getRightOp() instanceof Local) && (defstmt.getLeftOp() instanceof Local);
    }
}
