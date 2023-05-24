package soot.jbco.bafTransformations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import soot.Body;
import soot.BodyTransformer;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.PatchingChain;
import soot.PrimType;
import soot.RefType;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.baf.Baf;
import soot.baf.DupInst;
import soot.baf.FieldArgInst;
import soot.baf.IdentityInst;
import soot.baf.IncInst;
import soot.baf.InstanceCastInst;
import soot.baf.InstanceOfInst;
import soot.baf.LoadInst;
import soot.baf.MethodArgInst;
import soot.baf.NewArrayInst;
import soot.baf.NewInst;
import soot.baf.NewMultiArrayInst;
import soot.baf.NoArgInst;
import soot.baf.OpTypeArgInst;
import soot.baf.PopInst;
import soot.baf.PrimitiveCastInst;
import soot.baf.PushInst;
import soot.baf.ReturnInst;
import soot.baf.SpecialInvokeInst;
import soot.baf.StoreInst;
import soot.baf.SwapInst;
import soot.baf.TargetArgInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.Rand;
import soot.jimple.Constant;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.StringConstant;
import soot.toolkits.graph.BriefUnitGraph;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jbco/bafTransformations/FindDuplicateSequences.class */
public class FindDuplicateSequences extends BodyTransformer implements IJbcoTransform {
    int[] totalcounts = new int[512];
    public static String[] dependancies = {"bb.jbco_j2bl", "bb.jbco_rds", "bb.jbco_ful", "bb.lp"};
    public static String name = "bb.jbco_rds";

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
        out.println("Duplicate Sequences:");
        for (int count = this.totalcounts.length - 1; count >= 0; count--) {
            if (this.totalcounts[count] > 0) {
                out.println("\t" + count + " total: " + this.totalcounts[count]);
            }
        }
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
        if (weight == 0) {
            return;
        }
        if (output) {
            out.println("Checking " + b.getMethod().getName() + " for duplicate sequences..");
        }
        List<Unit> illegalUnits = new ArrayList<>();
        List<Unit> seenUnits = new ArrayList<>();
        List<Unit> workList = new ArrayList<>();
        PatchingChain<Unit> units = b.getUnits();
        BriefUnitGraph bug = new BriefUnitGraph(b);
        workList.addAll(bug.getHeads());
        while (workList.size() > 0) {
            Unit u = workList.remove(0);
            if (!seenUnits.contains(u)) {
                if (u instanceof NewInst) {
                    RefType t = ((NewInst) u).getBaseType();
                    List<Unit> tmpWorkList = new ArrayList<>();
                    tmpWorkList.add(u);
                    while (tmpWorkList.size() > 0) {
                        Unit v = tmpWorkList.remove(0);
                        if (v instanceof SpecialInvokeInst) {
                            SpecialInvokeInst si = (SpecialInvokeInst) v;
                            if (si.getMethodRef().getSignature().indexOf("void <init>") < 0 || si.getMethodRef().declaringClass() != t.getSootClass()) {
                                tmpWorkList.addAll(bug.getSuccsOf(v));
                            }
                        } else {
                            tmpWorkList.addAll(bug.getSuccsOf(v));
                        }
                        illegalUnits.add(v);
                    }
                }
                seenUnits.add(u);
                workList.addAll(bug.getSuccsOf(u));
            }
        }
        int controlLocalIndex = 0;
        int longestSeq = (units.size() / 2) - 1;
        if (longestSeq > 20) {
            longestSeq = 20;
        }
        Collection<Local> bLocals = b.getLocals();
        int[] counts = new int[longestSeq + 1];
        Map<Local, Local> bafToJLocals = Main.methods2Baf2JLocals.get(b.getMethod());
        boolean changed = true;
        Map<Unit, Stack<Type>> stackHeightsBefore = null;
        for (int count = longestSeq; count > 2; count--) {
            Unit[] uArry = (Unit[]) units.toArray(new Unit[units.size()]);
            if (uArry.length <= 0) {
                return;
            }
            List<List<Unit>> candidates = new ArrayList<>();
            List<Unit> unitIDs = new ArrayList<>();
            if (changed) {
                stackHeightsBefore = StackTypeHeightCalculator.calculateStackHeights(b, bafToJLocals);
                bug = StackTypeHeightCalculator.bug;
                changed = false;
            }
            for (int i = 0; i < uArry.length; i++) {
                unitIDs.add(uArry[i]);
                if (i + count <= uArry.length) {
                    List<Unit> seq = new ArrayList<>();
                    for (int j = 0; j < count; j++) {
                        Unit u2 = uArry[i + j];
                        if ((u2 instanceof IdentityInst) || (u2 instanceof ReturnInst) || illegalUnits.contains(u2)) {
                            break;
                        }
                        if (j > 0) {
                            List<Unit> preds = bug.getPredsOf(u2);
                            if (preds.size() > 0) {
                                int found = 0;
                                for (Unit p : preds) {
                                    int jj = 0;
                                    while (true) {
                                        if (jj < count) {
                                            if (p != uArry[i + jj]) {
                                                jj++;
                                            } else {
                                                found++;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (found < preds.size()) {
                                    break;
                                }
                            } else {
                                continue;
                            }
                        }
                        seq.add(u2);
                    }
                    if (seq.size() == count && seq.get(seq.size() - 1).fallsThrough()) {
                        candidates.add(seq);
                    }
                }
            }
            Map<List<Unit>, List<List<Unit>>> selected = new HashMap<>();
            for (int i2 = 0; i2 < candidates.size(); i2++) {
                List<Unit> seq2 = candidates.get(i2);
                List<List<Unit>> matches = new ArrayList<>();
                for (int j2 = 0; j2 < uArry.length - count; j2++) {
                    if (!overlap(uArry, seq2, j2, count)) {
                        boolean found2 = false;
                        for (int k = 0; k < count; k++) {
                            Unit u3 = seq2.get(k);
                            found2 = false;
                            Unit v2 = uArry[j2 + k];
                            if (!equalUnits(u3, v2, b) || illegalUnits.contains(v2)) {
                                break;
                            }
                            if (k > 0) {
                                List<Unit> preds2 = bug.getPredsOf(v2);
                                if (preds2.size() > 0) {
                                    int fcount = 0;
                                    for (Unit p2 : preds2) {
                                        int jj2 = 0;
                                        while (true) {
                                            if (jj2 < count) {
                                                if (p2 != uArry[j2 + jj2]) {
                                                    jj2++;
                                                } else {
                                                    fcount++;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (fcount < preds2.size()) {
                                        break;
                                    }
                                }
                            }
                            if (!stackHeightsBefore.get(u3).equals(stackHeightsBefore.get(v2))) {
                                break;
                            }
                            found2 = true;
                        }
                        if (found2) {
                            List<Unit> foundSeq = new ArrayList<>();
                            for (int m = 0; m < count; m++) {
                                foundSeq.add(uArry[j2 + m]);
                            }
                            matches.add(foundSeq);
                        }
                    }
                }
                if (matches.size() > 0) {
                    boolean done = false;
                    for (int x = 0; x < seq2.size(); x++) {
                        if (!unitIDs.contains(seq2.get(x))) {
                            done = true;
                        } else {
                            unitIDs.remove(seq2.get(x));
                        }
                    }
                    if (!done) {
                        List<List<Unit>> matches2 = cullOverlaps(b, unitIDs, matches);
                        if (matches2.size() > 0) {
                            selected.put(seq2, matches2);
                        }
                    }
                }
            }
            if (selected.size() > 0) {
                for (List<Unit> key : selected.keySet()) {
                    List<List<Unit>> avalues = selected.get(key);
                    if (avalues.size() >= 1 && Rand.getInt(10) > weight) {
                        changed = true;
                        Local controlLocal = Baf.v().newLocal("controlLocalfordups" + controlLocalIndex, IntType.v());
                        bLocals.add(controlLocal);
                        int i3 = controlLocalIndex;
                        controlLocalIndex++;
                        bafToJLocals.put(controlLocal, Jimple.v().newLocal("controlLocalfordups" + i3, IntType.v()));
                        int size = key.size();
                        counts[size] = counts[size] + avalues.size();
                        List<Unit> jumps = new ArrayList<>();
                        Unit first = key.get(0);
                        Unit store = Baf.v().newStoreInst(IntType.v(), controlLocal);
                        units.insertBefore(store, first);
                        Unit pushUnit = Baf.v().newPushInst(IntConstant.v(0));
                        units.insertBefore(pushUnit, store);
                        int index = 1;
                        for (List<Unit> next : avalues) {
                            Unit jump = units.getSuccOf((PatchingChain<Unit>) next.get(next.size() - 1));
                            Unit firstt = next.get(0);
                            Unit storet = (Unit) store.clone();
                            units.insertBefore(storet, firstt);
                            int i4 = index;
                            index++;
                            Unit pushUnit2 = Baf.v().newPushInst(IntConstant.v(i4));
                            units.insertBefore(pushUnit2, storet);
                            Unit goUnit = Baf.v().newGotoInst(first);
                            units.insertAfter(goUnit, storet);
                            jumps.add(jump);
                        }
                        Unit insertAfter = key.get(key.size() - 1);
                        Unit swUnit = Baf.v().newTableSwitchInst(units.getSuccOf((PatchingChain<Unit>) insertAfter), 1, jumps.size(), jumps);
                        units.insertAfter(swUnit, insertAfter);
                        Unit loadUnit = Baf.v().newLoadInst(IntType.v(), controlLocal);
                        units.insertAfter(loadUnit, insertAfter);
                        for (List<Unit> next2 : avalues) {
                            units.removeAll(next2);
                        }
                    }
                }
            }
        }
        boolean dupsExist = false;
        if (output) {
            System.out.println("Duplicate Sequences for " + b.getMethod().getName());
        }
        for (int count2 = longestSeq; count2 >= 0; count2--) {
            if (counts[count2] > 0) {
                if (output) {
                    out.println(String.valueOf(count2) + " total: " + counts[count2]);
                }
                dupsExist = true;
                int[] iArr = this.totalcounts;
                int i5 = count2;
                iArr[i5] = iArr[i5] + counts[count2];
            }
        }
        if (dupsExist) {
            if (debug) {
                StackTypeHeightCalculator.calculateStackHeights(b);
            }
        } else if (output) {
            out.println("\tnone");
        }
    }

    private boolean equalUnits(Object o1, Object o2, Body b) {
        if (o1.getClass() != o2.getClass()) {
            return false;
        }
        List<Trap> l1 = getTrapsForUnit(o1, b);
        List<Trap> l2 = getTrapsForUnit(o2, b);
        if (l1.size() != l2.size()) {
            return false;
        }
        for (int i = 0; i < l1.size(); i++) {
            if (l1.get(i) != l2.get(i)) {
                return false;
            }
        }
        if (o1 instanceof NoArgInst) {
            return true;
        }
        if (o1 instanceof TargetArgInst) {
            return o1 instanceof OpTypeArgInst ? ((TargetArgInst) o1).getTarget() == ((TargetArgInst) o2).getTarget() && ((OpTypeArgInst) o1).getOpType() == ((OpTypeArgInst) o2).getOpType() : ((TargetArgInst) o1).getTarget() == ((TargetArgInst) o2).getTarget();
        } else if (o1 instanceof OpTypeArgInst) {
            return ((OpTypeArgInst) o1).getOpType() == ((OpTypeArgInst) o2).getOpType();
        } else if (o1 instanceof MethodArgInst) {
            return ((MethodArgInst) o1).getMethod() == ((MethodArgInst) o2).getMethod();
        } else if (o1 instanceof FieldArgInst) {
            return ((FieldArgInst) o1).getField() == ((FieldArgInst) o2).getField();
        } else if (o1 instanceof PrimitiveCastInst) {
            return ((PrimitiveCastInst) o1).getFromType() == ((PrimitiveCastInst) o2).getFromType() && ((PrimitiveCastInst) o1).getToType() == ((PrimitiveCastInst) o2).getToType();
        } else if (o1 instanceof DupInst) {
            return compareDups(o1, o2);
        } else {
            if (o1 instanceof LoadInst) {
                return ((LoadInst) o1).getLocal() == ((LoadInst) o2).getLocal();
            } else if (o1 instanceof StoreInst) {
                return ((StoreInst) o1).getLocal() == ((StoreInst) o2).getLocal();
            } else if (o1 instanceof PushInst) {
                return equalConstants(((PushInst) o1).getConstant(), ((PushInst) o2).getConstant());
            } else {
                if ((o1 instanceof IncInst) && equalConstants(((IncInst) o1).getConstant(), ((IncInst) o2).getConstant())) {
                    return ((IncInst) o1).getLocal() == ((IncInst) o2).getLocal();
                } else if (o1 instanceof InstanceCastInst) {
                    return equalTypes(((InstanceCastInst) o1).getCastType(), ((InstanceCastInst) o2).getCastType());
                } else {
                    if (o1 instanceof InstanceOfInst) {
                        return equalTypes(((InstanceOfInst) o1).getCheckType(), ((InstanceOfInst) o2).getCheckType());
                    }
                    if (o1 instanceof NewArrayInst) {
                        return equalTypes(((NewArrayInst) o1).getBaseType(), ((NewArrayInst) o2).getBaseType());
                    }
                    if (o1 instanceof NewInst) {
                        return equalTypes(((NewInst) o1).getBaseType(), ((NewInst) o2).getBaseType());
                    }
                    return o1 instanceof NewMultiArrayInst ? equalTypes(((NewMultiArrayInst) o1).getBaseType(), ((NewMultiArrayInst) o2).getBaseType()) && ((NewMultiArrayInst) o1).getDimensionCount() == ((NewMultiArrayInst) o2).getDimensionCount() : o1 instanceof PopInst ? ((PopInst) o1).getWordCount() == ((PopInst) o2).getWordCount() : (o1 instanceof SwapInst) && ((SwapInst) o1).getFromType() == ((SwapInst) o2).getFromType() && ((SwapInst) o1).getToType() == ((SwapInst) o2).getToType();
                }
            }
        }
    }

    private List<Trap> getTrapsForUnit(Object o, Body b) {
        ArrayList<Trap> list = new ArrayList<>();
        Chain<Trap> traps = b.getTraps();
        if (traps.size() != 0) {
            PatchingChain<Unit> units = b.getUnits();
            for (Trap t : traps) {
                Iterator<Unit> tit = units.iterator(t.getBeginUnit(), t.getEndUnit());
                while (true) {
                    if (tit.hasNext()) {
                        if (tit.next() == o) {
                            list.add(t);
                            break;
                        }
                    }
                }
            }
        }
        return list;
    }

    private boolean overlap(Object[] units, List<?> list, int idx, int count) {
        if (idx < 0 || list == null || list.size() == 0) {
            return false;
        }
        Object first = list.get(0);
        Object last = list.get(list.size() - 1);
        for (int i = idx; i < idx + count; i++) {
            if (i < units.length && (first == units[i] || last == units[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean equalConstants(Constant c1, Constant c2) {
        Type t = c1.getType();
        if (t != c2.getType()) {
            return false;
        }
        return t instanceof IntType ? ((IntConstant) c1).value == ((IntConstant) c2).value : t instanceof FloatType ? ((FloatConstant) c1).value == ((FloatConstant) c2).value : t instanceof LongType ? ((LongConstant) c1).value == ((LongConstant) c2).value : t instanceof DoubleType ? ((DoubleConstant) c1).value == ((DoubleConstant) c2).value : ((c1 instanceof StringConstant) && (c2 instanceof StringConstant)) ? ((StringConstant) c1).value == ((StringConstant) c2).value : (c1 instanceof NullConstant) && (c2 instanceof NullConstant);
    }

    private boolean compareDups(Object o1, Object o2) {
        DupInst d1 = (DupInst) o1;
        DupInst d2 = (DupInst) o2;
        List<Type> l1 = d1.getOpTypes();
        List<Type> l2 = d2.getOpTypes();
        for (int k = 0; k < 2; k++) {
            if (k == 1) {
                l1 = d1.getUnderTypes();
                l2 = d2.getUnderTypes();
            }
            if (l1.size() != l2.size()) {
                return false;
            }
            for (int i = 0; i < l1.size(); i++) {
                if (l1.get(i) != l2.get(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean equalTypes(Type t1, Type t2) {
        if (!(t1 instanceof RefType)) {
            return (t1 instanceof PrimType) && (t2 instanceof PrimType) && t1.getClass() == t2.getClass();
        } else if (t2 instanceof RefType) {
            RefType rt1 = (RefType) t1;
            RefType rt2 = (RefType) t2;
            return rt1.compareTo(rt2) == 0;
        } else {
            return false;
        }
    }

    private static List<List<Unit>> cullOverlaps(Body b, List<Unit> ids, List<List<Unit>> matches) {
        List<List<Unit>> newMatches = new ArrayList<>();
        for (int i = 0; i < matches.size(); i++) {
            List<Unit> match = matches.get(i);
            Iterator<Unit> it = match.iterator();
            boolean clean = true;
            while (true) {
                if (it.hasNext()) {
                    if (!ids.contains(it.next())) {
                        clean = false;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (clean) {
                List<UnitBox> targs = b.getUnitBoxes(true);
                for (int j = 0; j < targs.size() && clean; j++) {
                    Unit u = targs.get(j).getUnit();
                    Iterator<Unit> it2 = match.iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            if (u == it2.next()) {
                                clean = false;
                                break;
                            }
                        }
                    }
                }
            }
            if (clean) {
                for (Unit unit : match) {
                    ids.remove(unit);
                }
                newMatches.add(match);
            }
        }
        return newMatches;
    }
}
