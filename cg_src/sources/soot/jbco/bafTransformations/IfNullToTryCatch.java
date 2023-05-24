package soot.jbco.bafTransformations;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.BooleanType;
import soot.G;
import soot.JavaBasicTypes;
import soot.PatchingChain;
import soot.RefType;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.baf.Baf;
import soot.baf.IfNonNullInst;
import soot.baf.IfNullInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.BodyBuilder;
import soot.jbco.util.Rand;
import soot.jimple.NullConstant;
/* loaded from: gencallgraphv3.jar:soot/jbco/bafTransformations/IfNullToTryCatch.class */
public class IfNullToTryCatch extends BodyTransformer implements IJbcoTransform {
    int count = 0;
    int totalifs = 0;
    public static String[] dependancies = {"bb.jbco_riitcb", "bb.jbco_ful", "bb.lp"};
    public static String name = "bb.jbco_riitcb";

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
        out.println("If(Non)Nulls changed to traps: " + this.count);
        out.println("Total ifs found: " + this.totalifs);
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
        if (weight == 0) {
            return;
        }
        SootClass exc = G.v().soot_Scene().getSootClass("java.lang.NullPointerException");
        SootClass obj = G.v().soot_Scene().getSootClass(JavaBasicTypes.JAVA_LANG_OBJECT);
        SootMethod toStrg = obj.getMethodByName("toString");
        SootMethod eq = obj.getMethodByName("equals");
        boolean change = false;
        PatchingChain<Unit> units = b.getUnits();
        Iterator<Unit> uit = units.snapshotIterator();
        while (uit.hasNext()) {
            Unit u = uit.next();
            if (BodyBuilder.isBafIf(u)) {
                this.totalifs++;
            }
            if ((u instanceof IfNullInst) && Rand.getInt(10) <= weight) {
                Unit targ = ((IfNullInst) u).getTarget();
                Unit succ = units.getSuccOf((PatchingChain<Unit>) u);
                Unit pop = Baf.v().newPopInst(RefType.v());
                Unit popClone = (Unit) pop.clone();
                units.insertBefore(pop, targ);
                units.insertBefore(Baf.v().newGotoInst(targ), pop);
                if (Rand.getInt(2) == 0) {
                    Unit methCall = Baf.v().newVirtualInvokeInst(toStrg.makeRef());
                    units.insertBefore(methCall, u);
                    if (Rand.getInt(2) == 0) {
                        units.remove(u);
                        units.insertAfter(popClone, methCall);
                    }
                    b.getTraps().add(Baf.v().newTrap(exc, methCall, succ, pop));
                } else {
                    Unit throwu = Baf.v().newThrowInst();
                    units.insertBefore(throwu, u);
                    units.remove(u);
                    units.insertBefore(Baf.v().newPushInst(NullConstant.v()), throwu);
                    Unit ifunit = Baf.v().newIfCmpNeInst(RefType.v(), succ);
                    units.insertBefore(ifunit, throwu);
                    units.insertBefore(Baf.v().newPushInst(NullConstant.v()), throwu);
                    b.getTraps().add(Baf.v().newTrap(exc, throwu, succ, pop));
                }
                this.count++;
                change = true;
            } else if ((u instanceof IfNonNullInst) && Rand.getInt(10) <= weight) {
                Unit targ2 = ((IfNonNullInst) u).getTarget();
                Unit methCall2 = Baf.v().newVirtualInvokeInst(eq.makeRef());
                units.insertBefore(methCall2, u);
                units.insertBefore(Baf.v().newPushInst(NullConstant.v()), methCall2);
                if (Rand.getInt(2) == 0) {
                    units.insertBefore(Baf.v().newPopInst(BooleanType.v()), u);
                    Unit gotoTarg = Baf.v().newGotoInst(targ2);
                    units.insertBefore(gotoTarg, u);
                    Unit pop2 = Baf.v().newPopInst(RefType.v());
                    units.insertAfter(pop2, u);
                    units.remove(u);
                    b.getTraps().addFirst(Baf.v().newTrap(exc, methCall2, gotoTarg, pop2));
                } else {
                    Unit iffalse = Baf.v().newIfEqInst(targ2);
                    units.insertBefore(iffalse, u);
                    units.insertBefore(Baf.v().newPushInst(NullConstant.v()), u);
                    Unit pop3 = Baf.v().newPopInst(RefType.v());
                    units.insertAfter(pop3, u);
                    units.remove(u);
                    b.getTraps().addFirst(Baf.v().newTrap(exc, methCall2, iffalse, pop3));
                }
                this.count++;
                change = true;
            }
        }
        if (change && debug) {
            StackTypeHeightCalculator.calculateStackHeights(b);
        }
    }
}
