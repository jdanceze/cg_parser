package soot.jbco.jimpleTransformations;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.Trap;
import soot.Unit;
import soot.UnitBox;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.Rand;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jbco/jimpleTransformations/GotoInstrumenter.class */
public class GotoInstrumenter extends BodyTransformer implements IJbcoTransform {
    private int trapsAdded = 0;
    private int gotosInstrumented = 0;
    private static final int MAX_TRIES_TO_GET_REORDER_COUNT = 10;
    private static final Logger logger = LoggerFactory.getLogger(GotoInstrumenter.class);
    public static final String name = "jtp.jbco_gia";
    public static final String[] dependencies = {name};
    private static final UnitBox[] EMPTY_UNIT_BOX_ARRAY = new UnitBox[0];

    @Override // soot.jbco.IJbcoTransform
    public String getName() {
        return name;
    }

    @Override // soot.jbco.IJbcoTransform
    public String[] getDependencies() {
        return (String[]) Arrays.copyOf(dependencies, dependencies.length);
    }

    @Override // soot.jbco.IJbcoTransform
    public void outputSummary() {
        logger.info("Instrumented {} GOTOs, added {} traps.", Integer.valueOf(this.gotosInstrumented), Integer.valueOf(this.trapsAdded));
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        Unit trapEndUnit;
        Stmt newGotoStmt;
        if ("<init>".equals(body.getMethod().getName()) || "<clinit>".equals(body.getMethod().getName())) {
            if (isVerbose()) {
                logger.info("Skipping {} method GOTO instrumentation as it is constructor/initializer.", body.getMethod().getSignature());
            }
        } else if (Main.getWeight(phaseName, body.getMethod().getSignature()) != 0) {
            PatchingChain<Unit> units = body.getUnits();
            int precedingFirstNotIdentityIndex = 0;
            Unit precedingFirstNotIdentity = null;
            Iterator<Unit> it = units.iterator();
            while (it.hasNext()) {
                Unit unit = it.next();
                if (!(unit instanceof IdentityStmt)) {
                    break;
                }
                precedingFirstNotIdentity = unit;
                precedingFirstNotIdentityIndex++;
            }
            int unitsLeft = units.size() - precedingFirstNotIdentityIndex;
            if (unitsLeft < 8) {
                if (isVerbose()) {
                    logger.info("Skipping {} method GOTO instrumentation as it is too small.", body.getMethod().getSignature());
                    return;
                }
                return;
            }
            int tries = 0;
            int unitsQuantityToReorder = 0;
            while (tries < 10) {
                unitsQuantityToReorder = Rand.getInt(unitsLeft - 2) + 1;
                tries++;
                Unit selectedUnit = (Unit) Iterables.get(units, precedingFirstNotIdentityIndex + unitsQuantityToReorder);
                if (!isExceptionCaught(selectedUnit, units, body.getTraps())) {
                    break;
                }
            }
            if (tries >= 10) {
                return;
            }
            if (isVerbose()) {
                logger.info("Adding GOTOs to \"{}\".", body.getMethod().getName());
            }
            Unit first = precedingFirstNotIdentity == null ? units.getFirst() : precedingFirstNotIdentity;
            Unit firstReorderingUnit = units.getSuccOf((PatchingChain<Unit>) first);
            Unit reorderingUnit = firstReorderingUnit;
            for (int reorder = 0; reorder < unitsQuantityToReorder; reorder++) {
                UnitBox[] pointingToReorderingUnit = (UnitBox[]) reorderingUnit.getBoxesPointingToThis().toArray(EMPTY_UNIT_BOX_ARRAY);
                for (UnitBox element : pointingToReorderingUnit) {
                    reorderingUnit.removeBoxPointingToThis(element);
                }
                Unit nextReorderingUnit = units.getSuccOf((PatchingChain<Unit>) reorderingUnit);
                units.remove(reorderingUnit);
                units.add((PatchingChain<Unit>) reorderingUnit);
                for (UnitBox element2 : pointingToReorderingUnit) {
                    reorderingUnit.addBoxPointingToThis(element2);
                }
                reorderingUnit = nextReorderingUnit;
            }
            Unit firstReorderingNotGotoStmt = first instanceof GotoStmt ? ((GotoStmt) first).getTargetBox().getUnit() : firstReorderingUnit;
            GotoStmt gotoFirstReorderingNotGotoStmt = Jimple.v().newGotoStmt(firstReorderingNotGotoStmt);
            units.insertBeforeNoRedirect(gotoFirstReorderingNotGotoStmt, (GotoStmt) reorderingUnit);
            if (units.getLast().fallsThrough()) {
                if (reorderingUnit instanceof GotoStmt) {
                    newGotoStmt = Jimple.v().newGotoStmt(((GotoStmt) reorderingUnit).getTargetBox().getUnit());
                } else {
                    newGotoStmt = Jimple.v().newGotoStmt(reorderingUnit);
                }
                Stmt gotoStmt = newGotoStmt;
                units.add((PatchingChain<Unit>) gotoStmt);
            }
            this.gotosInstrumented++;
            Unit secondReorderedUnit = units.getSuccOf((PatchingChain<Unit>) firstReorderingNotGotoStmt);
            if (secondReorderedUnit == null || (secondReorderedUnit.equals(units.getLast()) && (secondReorderedUnit instanceof IdentityStmt))) {
                if (firstReorderingNotGotoStmt instanceof IdentityStmt) {
                    if (isVerbose()) {
                        logger.info("Skipping adding try-catch block at \"{}\".", body.getMethod().getSignature());
                        return;
                    }
                    return;
                }
                secondReorderedUnit = firstReorderingNotGotoStmt;
            }
            RefType throwable = Scene.v().getRefType("java.lang.Throwable");
            Local caughtExceptionLocal = Jimple.v().newLocal("jbco_gi_caughtExceptionLocal", throwable);
            body.getLocals().add(caughtExceptionLocal);
            Unit caughtExceptionHandler = Jimple.v().newIdentityStmt(caughtExceptionLocal, Jimple.v().newCaughtExceptionRef());
            units.add((PatchingChain<Unit>) caughtExceptionHandler);
            units.add((PatchingChain<Unit>) Jimple.v().newThrowStmt(caughtExceptionLocal));
            Iterator<Unit> reorderedUnitsIterator = units.iterator(secondReorderedUnit, units.getPredOf((PatchingChain<Unit>) caughtExceptionHandler));
            Unit next = reorderedUnitsIterator.next();
            while (true) {
                trapEndUnit = next;
                if (!(trapEndUnit instanceof IdentityStmt) || !reorderedUnitsIterator.hasNext()) {
                    break;
                }
                next = reorderedUnitsIterator.next();
            }
            body.getTraps().add(Jimple.v().newTrap(throwable.getSootClass(), units.getPredOf((PatchingChain<Unit>) firstReorderingNotGotoStmt), units.getSuccOf((PatchingChain<Unit>) trapEndUnit), caughtExceptionHandler));
            this.trapsAdded++;
        }
    }

    private static boolean isExceptionCaught(Unit unit, Chain<Unit> units, Chain<Trap> traps) {
        for (Trap trap : traps) {
            Unit end = trap.getEndUnit();
            if (end.equals(unit)) {
                return true;
            }
            Iterator<Unit> unitsInTryIterator = units.iterator(trap.getBeginUnit(), units.getPredOf(end));
            if (Iterators.contains(unitsInTryIterator, unit)) {
                return true;
            }
        }
        return false;
    }
}
