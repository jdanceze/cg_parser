package soot.jbco.bafTransformations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.BooleanType;
import soot.IntType;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.StmtAddressType;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.baf.Baf;
import soot.baf.GotoInst;
import soot.baf.IdentityInst;
import soot.baf.JSRInst;
import soot.baf.StaticGetInst;
import soot.baf.TargetArgInst;
import soot.baf.VirtualInvokeInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.jimpleTransformations.FieldRenamer;
import soot.jbco.util.Rand;
import soot.jimple.IntConstant;
import soot.jimple.NullConstant;
import soot.toolkits.graph.BriefUnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jbco/bafTransformations/TryCatchCombiner.class */
public class TryCatchCombiner extends BodyTransformer implements IJbcoTransform {
    int totalcount = 0;
    int changedcount = 0;
    private static final Logger logger = LoggerFactory.getLogger(TryCatchCombiner.class);
    public static String[] dependancies = {"bb.jbco_j2bl", "bb.jbco_ctbcb", "bb.jbco_ful", "bb.lp"};
    public static String name = "bb.jbco_ctbcb";

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
        out.println("Total try blocks found: " + this.totalcount);
        out.println("Combined TryCatches: " + this.changedcount);
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Unit first;
        int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
        if (weight == 0) {
            return;
        }
        int trapCount = 0;
        PatchingChain<Unit> units = b.getUnits();
        ArrayList<Unit> headList = new ArrayList<>();
        ArrayList<Trap> trapList = new ArrayList<>();
        for (Trap t : b.getTraps()) {
            this.totalcount++;
            if (isRewritable(t)) {
                headList.add(t.getBeginUnit());
                trapList.add(t);
                trapCount++;
            }
        }
        if (trapCount == 0) {
            return;
        }
        for (int i = 0; i < headList.size(); i++) {
            for (int j = 0; j < headList.size(); j++) {
                if (i != j && headList.get(i) == headList.get(j)) {
                    Trap t2 = trapList.get(i);
                    Unit nop = Baf.v().newNopInst();
                    units.insertBeforeNoRedirect(nop, headList.get(i));
                    headList.set(i, nop);
                    t2.setBeginUnit(nop);
                }
            }
        }
        Unit first2 = null;
        Iterator<Unit> uit = units.iterator();
        while (uit.hasNext()) {
            Unit unit = uit.next();
            if (!(unit instanceof IdentityInst)) {
                break;
            }
            first2 = unit;
        }
        if (first2 == null) {
            first = Baf.v().newNopInst();
            units.insertBefore(first, units.getFirst());
        } else {
            first = units.getSuccOf((PatchingChain<Unit>) first2);
        }
        Collection<Local> locs = b.getLocals();
        Map<Local, Local> bafToJLocals = Main.methods2Baf2JLocals.get(b.getMethod());
        int varCount = trapCount + 1;
        Iterator<Trap> traps = b.getTraps().snapshotIterator();
        while (traps.hasNext()) {
            Trap t3 = traps.next();
            Unit begUnit = t3.getBeginUnit();
            if (isRewritable(t3) && Rand.getInt(10) <= weight) {
                Map<Unit, Stack<Type>> stackHeightsBefore = StackTypeHeightCalculator.calculateStackHeights(b, bafToJLocals);
                boolean badType = false;
                Stack<Type> s = (Stack) stackHeightsBefore.get(begUnit).clone();
                if (s.size() > 0) {
                    int i2 = 0;
                    while (true) {
                        if (i2 < s.size()) {
                            if (!(s.pop() instanceof StmtAddressType)) {
                                i2++;
                            } else {
                                badType = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
                if (!badType) {
                    Local controlLocal = Baf.v().newLocal("controlLocal_tccomb" + trapCount, IntType.v());
                    locs.add(controlLocal);
                    Unit pushZero = Baf.v().newPushInst(IntConstant.v(0));
                    Unit storZero = Baf.v().newStoreInst(IntType.v(), controlLocal);
                    units.insertBeforeNoRedirect((Unit) pushZero.clone(), first);
                    units.insertBeforeNoRedirect((Unit) storZero.clone(), first);
                    BriefUnitGraph graph = new BriefUnitGraph(b);
                    List<Unit> l = graph.getPredsOf(begUnit);
                    units.add((PatchingChain<Unit>) pushZero);
                    units.add((PatchingChain<Unit>) storZero);
                    Stack<Local> varsToLoad = new Stack<>();
                    Stack<Type> s2 = stackHeightsBefore.get(begUnit);
                    if (s2.size() > 0) {
                        for (int i3 = 0; i3 < s2.size(); i3++) {
                            Type type = s2.pop();
                            int i4 = varCount;
                            varCount++;
                            Local varLocal = Baf.v().newLocal("varLocal_tccomb" + i4, type);
                            locs.add(varLocal);
                            varsToLoad.push(varLocal);
                            units.add((PatchingChain<Unit>) Baf.v().newStoreInst(type, varLocal));
                            units.insertBeforeNoRedirect(FixUndefinedLocals.getPushInitializer(varLocal, type), first);
                            units.insertBeforeNoRedirect(Baf.v().newStoreInst(type, varLocal), first);
                        }
                    }
                    units.add((PatchingChain<Unit>) Baf.v().newPushInst(NullConstant.v()));
                    units.add((PatchingChain<Unit>) Baf.v().newGotoInst(begUnit));
                    for (int i5 = 0; i5 < l.size(); i5++) {
                        Unit pred = l.get(i5);
                        if (isIf(pred)) {
                            TargetArgInst ifPred = (TargetArgInst) pred;
                            if (ifPred.getTarget() == begUnit) {
                                ifPred.setTarget(pushZero);
                            }
                            Unit succ = units.getSuccOf((PatchingChain<Unit>) ifPred);
                            if (succ == begUnit) {
                                units.insertAfter(Baf.v().newGotoInst(pushZero), (Unit) ifPred);
                            }
                        } else if ((pred instanceof GotoInst) && ((GotoInst) pred).getTarget() == begUnit) {
                            ((GotoInst) pred).setTarget(pushZero);
                        } else {
                            units.insertAfter(Baf.v().newGotoInst(pushZero), pred);
                        }
                    }
                    Unit handlerUnit = t3.getHandlerUnit();
                    Unit newBeginUnit = Baf.v().newLoadInst(IntType.v(), controlLocal);
                    units.insertBefore(newBeginUnit, begUnit);
                    units.insertBefore(Baf.v().newIfNeInst(handlerUnit), begUnit);
                    units.insertBefore(Baf.v().newPopInst(RefType.v()), begUnit);
                    while (varsToLoad.size() > 0) {
                        Local varLocal2 = varsToLoad.pop();
                        units.insertBefore(Baf.v().newLoadInst(varLocal2.getType(), varLocal2), begUnit);
                    }
                    try {
                        SootField[] f = FieldRenamer.v().getRandomOpaques();
                        if (f[0] != null && f[1] != null) {
                            loadBooleanValue(units, f[0], begUnit);
                            loadBooleanValue(units, f[1], begUnit);
                            units.insertBeforeNoRedirect(Baf.v().newIfCmpEqInst(BooleanType.v(), begUnit), begUnit);
                        }
                    } catch (NullPointerException npe) {
                        logger.debug(npe.getMessage(), (Throwable) npe);
                    }
                    if (Rand.getInt() % 2 == 0) {
                        units.insertBeforeNoRedirect(Baf.v().newPushInst(IntConstant.v(Rand.getInt(3) + 1)), begUnit);
                        units.insertBeforeNoRedirect(Baf.v().newStoreInst(IntType.v(), controlLocal), begUnit);
                    } else {
                        units.insertBeforeNoRedirect(Baf.v().newIncInst(controlLocal, IntConstant.v(Rand.getInt(3) + 1)), begUnit);
                    }
                    trapCount--;
                    t3.setBeginUnit(newBeginUnit);
                    t3.setHandlerUnit(newBeginUnit);
                    this.changedcount++;
                    if (debug) {
                        StackTypeHeightCalculator.printStack(units, StackTypeHeightCalculator.calculateStackHeights(b), false);
                    }
                }
            }
        }
    }

    private void loadBooleanValue(PatchingChain<Unit> units, SootField f, Unit insert) {
        units.insertBefore(Baf.v().newStaticGetInst(f.makeRef()), (StaticGetInst) insert);
        if (f.getType() instanceof RefType) {
            SootMethod boolInit = ((RefType) f.getType()).getSootClass().getMethod("boolean booleanValue()");
            units.insertBefore(Baf.v().newVirtualInvokeInst(boolInit.makeRef()), (VirtualInvokeInst) insert);
        }
    }

    private boolean isIf(Unit u) {
        return (!(u instanceof TargetArgInst) || (u instanceof GotoInst) || (u instanceof JSRInst)) ? false : true;
    }

    private boolean isRewritable(Trap t) {
        if (t.getBeginUnit() == t.getHandlerUnit()) {
            return false;
        }
        SootClass exc = t.getException();
        if (exc.getName().equals("java.lang.Throwable")) {
            return false;
        }
        while (!exc.getName().equals("java.lang.RuntimeException")) {
            if (!exc.hasSuperclass()) {
                return true;
            }
            SootClass superclass = exc.getSuperclass();
            exc = superclass;
            if (superclass == null) {
                return true;
            }
        }
        return false;
    }
}
