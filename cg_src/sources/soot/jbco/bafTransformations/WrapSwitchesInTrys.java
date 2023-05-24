package soot.jbco.bafTransformations;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.Trap;
import soot.Unit;
import soot.baf.Baf;
import soot.baf.TableSwitchInst;
import soot.baf.ThrowInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.BodyBuilder;
import soot.jbco.util.Rand;
import soot.jbco.util.ThrowSet;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jbco/bafTransformations/WrapSwitchesInTrys.class */
public class WrapSwitchesInTrys extends BodyTransformer implements IJbcoTransform {
    int totaltraps = 0;
    public static String[] dependancies = {"bb.jbco_ptss", "bb.jbco_ful", "bb.lp"};
    public static String name = "bb.jbco_ptss";

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
        out.println("Switches wrapped in Tries: " + this.totaltraps);
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Unit succ;
        Object o;
        int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
        if (weight == 0) {
            return;
        }
        int i = 0;
        Unit handler = null;
        Chain<Trap> traps = b.getTraps();
        PatchingChain<Unit> units = b.getUnits();
        Iterator<Unit> it = units.snapshotIterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof TableSwitchInst) {
                TableSwitchInst twi = (TableSwitchInst) u;
                if (!BodyBuilder.isExceptionCaughtAt(units, twi, traps.iterator()) && Rand.getInt(10) <= weight) {
                    if (handler == null) {
                        Iterator<Unit> uit = units.snapshotIterator();
                        while (true) {
                            if (!uit.hasNext()) {
                                break;
                            }
                            Unit uthrow = uit.next();
                            if ((uthrow instanceof ThrowInst) && !BodyBuilder.isExceptionCaughtAt(units, uthrow, traps.iterator())) {
                                handler = uthrow;
                                break;
                            }
                        }
                        if (handler == null) {
                            handler = Baf.v().newThrowInst();
                            units.add((PatchingChain<Unit>) handler);
                        }
                    }
                    int size = 4;
                    Unit succOf = units.getSuccOf((PatchingChain<Unit>) twi);
                    while (true) {
                        succ = succOf;
                        if (!BodyBuilder.isExceptionCaughtAt(units, succ, traps.iterator())) {
                            int i2 = size;
                            size--;
                            if (i2 <= 0 || (o = units.getSuccOf((PatchingChain<Unit>) succ)) == null) {
                                break;
                            }
                            succOf = (Unit) o;
                        } else {
                            break;
                        }
                    }
                    traps.add(Baf.v().newTrap(ThrowSet.getRandomThrowable(), twi, succ, handler));
                    i++;
                }
            }
        }
        this.totaltraps += i;
        if (i > 0 && debug) {
            StackTypeHeightCalculator.calculateStackHeights(b);
        }
    }
}
