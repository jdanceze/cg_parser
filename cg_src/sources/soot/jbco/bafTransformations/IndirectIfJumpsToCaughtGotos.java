package soot.jbco.bafTransformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.ByteType;
import soot.IntType;
import soot.IntegerType;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.SootField;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.baf.Baf;
import soot.baf.GotoInst;
import soot.baf.IdentityInst;
import soot.baf.JSRInst;
import soot.baf.TargetArgInst;
import soot.baf.ThrowInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.jimpleTransformations.FieldRenamer;
import soot.jbco.util.BodyBuilder;
import soot.jbco.util.Rand;
import soot.jbco.util.ThrowSet;
import soot.jimple.IntConstant;
import soot.jimple.NullConstant;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jbco/bafTransformations/IndirectIfJumpsToCaughtGotos.class */
public class IndirectIfJumpsToCaughtGotos extends BodyTransformer implements IJbcoTransform {
    int count = 0;
    private static final Logger logger = LoggerFactory.getLogger(IndirectIfJumpsToCaughtGotos.class);
    public static String[] dependancies = {"bb.jbco_iii", "bb.jbco_ful", "bb.lp"};
    public static String name = "bb.jbco_iii";

    @Override // soot.jbco.IJbcoTransform
    public String[] getDependencies() {
        return dependancies;
    }

    @Override // soot.jbco.IJbcoTransform
    public String getName() {
        return name;
    }

    @Override // soot.jbco.IJbcoTransform
    public void outputSummary() {
        out.println("Indirected Ifs through Traps: " + this.count);
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
        if (weight == 0) {
            return;
        }
        PatchingChain<Unit> units = b.getUnits();
        Unit nonTrap = findNonTrappedUnit(units, b.getTraps());
        if (nonTrap == null) {
            Unit last = null;
            nonTrap = Baf.v().newNopInst();
            Iterator<Unit> it = units.iterator();
            while (it.hasNext()) {
                Unit u = it.next();
                if ((u instanceof IdentityInst) && (((IdentityInst) u).getLeftOp() instanceof Local)) {
                    last = u;
                } else if (last != null) {
                    units.insertAfter(nonTrap, last);
                } else {
                    units.addFirst((PatchingChain<Unit>) nonTrap);
                }
            }
        }
        Stack<Type> stack = StackTypeHeightCalculator.getAfterStack(b, nonTrap);
        ArrayList<Unit> addedUnits = new ArrayList<>();
        Iterator<Unit> it2 = units.snapshotIterator();
        while (it2.hasNext()) {
            Unit u2 = it2.next();
            if (isIf(u2) && Rand.getInt(10) <= weight) {
                TargetArgInst ifu = (TargetArgInst) u2;
                Unit newTarg = Baf.v().newGotoInst(ifu.getTarget());
                units.add((PatchingChain<Unit>) newTarg);
                ifu.setTarget(newTarg);
                addedUnits.add(newTarg);
            }
        }
        if (addedUnits.size() <= 0) {
            return;
        }
        Unit nop = Baf.v().newNopInst();
        units.add((PatchingChain<Unit>) nop);
        ArrayList<Unit> toinsert = new ArrayList<>();
        SootField field = null;
        try {
            field = FieldRenamer.v().getRandomOpaques()[Rand.getInt(2)];
        } catch (NullPointerException npe) {
            logger.debug(npe.getMessage(), (Throwable) npe);
        }
        if (field != null && Rand.getInt(3) > 0) {
            toinsert.add(Baf.v().newStaticGetInst(field.makeRef()));
            if (field.getType() instanceof IntegerType) {
                toinsert.add(Baf.v().newIfGeInst(units.getSuccOf((PatchingChain<Unit>) nonTrap)));
            } else {
                SootMethod boolInit = ((RefType) field.getType()).getSootClass().getMethod("boolean booleanValue()");
                toinsert.add(Baf.v().newVirtualInvokeInst(boolInit.makeRef()));
                toinsert.add(Baf.v().newIfGeInst(units.getSuccOf((PatchingChain<Unit>) nonTrap)));
            }
        } else {
            toinsert.add(Baf.v().newPushInst(IntConstant.v(BodyBuilder.getIntegerNine())));
            toinsert.add(Baf.v().newPrimitiveCastInst(IntType.v(), ByteType.v()));
            toinsert.add(Baf.v().newPushInst(IntConstant.v(Rand.getInt() % 2 == 0 ? 9 : 3)));
            toinsert.add(Baf.v().newRemInst(ByteType.v()));
            toinsert.add(Baf.v().newIfEqInst(units.getSuccOf((PatchingChain<Unit>) nonTrap)));
        }
        ArrayList<Unit> toinserttry = new ArrayList<>();
        while (stack.size() > 0) {
            toinserttry.add(Baf.v().newPopInst(stack.pop()));
        }
        toinserttry.add(Baf.v().newPushInst(NullConstant.v()));
        Unit handler = Baf.v().newThrowInst();
        int rand = Rand.getInt(toinserttry.size());
        while (true) {
            int i = rand;
            rand++;
            if (i >= toinserttry.size()) {
                break;
            }
            toinsert.add(toinserttry.get(0));
            toinserttry.remove(0);
        }
        if (toinserttry.size() > 0) {
            toinserttry.add(Baf.v().newGotoInst(handler));
            toinsert.add(Baf.v().newGotoInst(toinserttry.get(0)));
            units.insertBefore((List<ArrayList<Unit>>) toinserttry, (ArrayList<Unit>) nop);
        }
        toinsert.add(handler);
        units.insertAfter((List<ArrayList<Unit>>) toinsert, (ArrayList<Unit>) nonTrap);
        b.getTraps().add(Baf.v().newTrap(ThrowSet.getRandomThrowable(), addedUnits.get(0), nop, handler));
        this.count += addedUnits.size();
        if (addedUnits.size() > 0 && debug) {
            StackTypeHeightCalculator.calculateStackHeights(b);
        }
    }

    private Unit findNonTrappedUnit(PatchingChain<Unit> units, Chain<Trap> traps) {
        int intrap = 0;
        ArrayList<Unit> untrapped = new ArrayList<>();
        Iterator<Unit> it = units.snapshotIterator();
        while (it.hasNext()) {
            Unit u = it.next();
            for (Trap t : traps) {
                if (u == t.getBeginUnit()) {
                    intrap++;
                }
                if (u == t.getEndUnit()) {
                    intrap--;
                }
            }
            if (intrap == 0) {
                untrapped.add(u);
            }
        }
        Unit result = null;
        if (untrapped.size() > 0) {
            int count = 0;
            while (result == null && count < 10) {
                count++;
                result = untrapped.get(Rand.getInt(999999) % untrapped.size());
                if (!result.fallsThrough() || units.getSuccOf((PatchingChain<Unit>) result) == null || (units.getSuccOf((PatchingChain<Unit>) result) instanceof ThrowInst)) {
                    result = null;
                }
            }
        }
        return result;
    }

    private boolean isIf(Unit u) {
        return (!(u instanceof TargetArgInst) || (u instanceof GotoInst) || (u instanceof JSRInst)) ? false : true;
    }
}
