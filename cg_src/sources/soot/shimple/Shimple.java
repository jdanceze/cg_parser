package soot.shimple;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BriefUnitPrinter;
import soot.G;
import soot.Local;
import soot.PhaseOptions;
import soot.Singletons;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.JimpleBody;
import soot.options.Options;
import soot.shimple.internal.SPhiExpr;
import soot.shimple.internal.SPiExpr;
import soot.toolkits.graph.Block;
import soot.toolkits.scalar.ValueUnitPair;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/shimple/Shimple.class */
public class Shimple {
    private static final Logger logger = LoggerFactory.getLogger(Shimple.class);
    public static final String IFALIAS = "IfAlias";
    public static final String MAYMODIFY = "MayModify";
    public static final String PHI = "Phi";
    public static final String PI = "Pi";
    public static final String PHASE = "shimple";

    public Shimple(Singletons.Global g) {
    }

    public static Shimple v() {
        return G.v().soot_shimple_Shimple();
    }

    public ShimpleBody newBody(SootMethod m) {
        Map<String, String> options = PhaseOptions.v().getPhaseOptions(PHASE);
        return new ShimpleBody(m, options);
    }

    public ShimpleBody newBody(SootMethod m, Map<String, String> options) {
        return new ShimpleBody(m, options);
    }

    public ShimpleBody newBody(Body b) {
        Map<String, String> options = PhaseOptions.v().getPhaseOptions(PHASE);
        return new ShimpleBody(b, options);
    }

    public ShimpleBody newBody(Body b, Map<String, String> options) {
        return new ShimpleBody(b, options);
    }

    public PhiExpr newPhiExpr(Local leftLocal, List<Block> preds) {
        return new SPhiExpr(leftLocal, preds);
    }

    public PiExpr newPiExpr(Local local, Unit predicate, Object targetKey) {
        return new SPiExpr(local, predicate, targetKey);
    }

    public PhiExpr newPhiExpr(List<Value> args, List<Unit> preds) {
        return new SPhiExpr(args, preds);
    }

    public JimpleBody newJimpleBody(ShimpleBody body) {
        return body.toJimpleBody();
    }

    public static boolean isPhiExpr(Value value) {
        return value instanceof PhiExpr;
    }

    public static boolean isPhiNode(Unit unit) {
        return getPhiExpr(unit) != null;
    }

    public static PhiExpr getPhiExpr(Unit unit) {
        if (unit instanceof AssignStmt) {
            Value right = ((AssignStmt) unit).getRightOp();
            if (isPhiExpr(right)) {
                return (PhiExpr) right;
            }
            return null;
        }
        return null;
    }

    public static boolean isPiExpr(Value value) {
        return value instanceof PiExpr;
    }

    public static boolean isPiNode(Unit unit) {
        return getPiExpr(unit) != null;
    }

    public static PiExpr getPiExpr(Unit unit) {
        if (unit instanceof AssignStmt) {
            Value right = ((AssignStmt) unit).getRightOp();
            if (isPiExpr(right)) {
                return (PiExpr) right;
            }
            return null;
        }
        return null;
    }

    public static Local getLhsLocal(Unit unit) {
        if (unit instanceof AssignStmt) {
            AssignStmt assign = (AssignStmt) unit;
            if (assign.getRightOp() instanceof ShimpleExpr) {
                return (Local) assign.getLeftOp();
            }
            return null;
        }
        return null;
    }

    public static void redirectToPreds(Body body, Unit remove) {
        if (remove.getBoxesPointingToThis().isEmpty()) {
            return;
        }
        boolean debug = Options.v().debug();
        if (body instanceof ShimpleBody) {
            debug |= ((ShimpleBody) body).getOptions().debug();
        }
        Chain<Unit> units = body.getUnits();
        Set<Unit> preds = new HashSet<>();
        Set<PhiExpr> phis = new HashSet<>();
        if (!remove.equals(units.getFirst())) {
            Unit possiblePred = units.getPredOf(remove);
            if (possiblePred.fallsThrough()) {
                preds.add(possiblePred);
            }
        }
        for (Unit unit : units) {
            for (UnitBox targetBox : unit.getUnitBoxes()) {
                if (remove.equals(targetBox.getUnit())) {
                    if (targetBox.isBranchTarget()) {
                        preds.add(unit);
                    } else {
                        PhiExpr phiExpr = getPhiExpr(unit);
                        if (phiExpr != null) {
                            phis.add(phiExpr);
                        }
                    }
                }
            }
        }
        if (phis.isEmpty()) {
            if (debug) {
                for (UnitBox u : remove.getBoxesPointingToThis()) {
                    if (!u.isBranchTarget()) {
                        logger.warn("Orphaned UnitBoxes to " + remove + "? Shimple.redirectToPreds is giving up.");
                        return;
                    }
                }
                return;
            }
            return;
        }
        if (preds.isEmpty()) {
            if (debug) {
                logger.warn("Shimple.redirectToPreds found no predecessors for " + remove + " in " + body.getMethod() + ".");
            }
            if (!remove.equals(units.getFirst())) {
                Unit pred = units.getPredOf(remove);
                if (debug) {
                    logger.warn("Falling back to immediate chain predecessor: " + pred + ".");
                }
                preds.add(pred);
            } else if (!remove.equals(units.getLast())) {
                Unit succ = units.getSuccOf(remove);
                if (debug) {
                    logger.warn("Falling back to immediate chain successor: " + succ + ".");
                }
                preds.add(succ);
            } else {
                throw new RuntimeException("Assertion failed.");
            }
        }
        for (PhiExpr phiExpr2 : phis) {
            ValueUnitPair argBox = phiExpr2.getArgBox(remove);
            if (argBox == null) {
                throw new RuntimeException("Assertion failed.");
            }
            Value arg = argBox.getValue();
            phiExpr2.removeArg(argBox);
            for (Unit pred2 : preds) {
                boolean added = phiExpr2.addArg(arg, pred2);
                if (!added) {
                    BriefUnitPrinter prtr = new BriefUnitPrinter(body);
                    prtr.setIndent("");
                    phiExpr2.toString(prtr);
                    logger.warn("Shimple.redirectToPreds failed to add " + arg + " (for predecessor: " + pred2 + ") to " + prtr.toString() + " in " + body.getMethod() + ".");
                }
            }
        }
    }

    public static void redirectPointers(Unit oldLocation, Unit newLocation) {
        Iterator it = new ArrayList(oldLocation.getBoxesPointingToThis()).iterator();
        while (it.hasNext()) {
            UnitBox box = (UnitBox) it.next();
            if (box.getUnit() != oldLocation) {
                throw new RuntimeException("Something weird's happening");
            }
            if (!box.isBranchTarget()) {
                box.setUnit(newLocation);
            }
        }
    }
}
