package soot.jimple.toolkits.thread.synchronization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.EquivalentValue;
import soot.Local;
import soot.SootMethod;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.coffi.Instruction;
import soot.jimple.AnyNewExpr;
import soot.jimple.ArrayRef;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.IdentityRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.ParameterRef;
import soot.jimple.Ref;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.toolkits.infoflow.FakeJimpleLocal;
import soot.jimple.toolkits.pointer.CodeBlockRWSet;
import soot.jimple.toolkits.pointer.RWSet;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.BackwardFlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/synchronization/LockableReferenceAnalysis.class */
public class LockableReferenceAnalysis extends BackwardFlowAnalysis<Unit, LocksetFlowInfo> {
    UnitGraph graph;
    SootMethod method;
    CriticalSectionAwareSideEffectAnalysis tasea;
    RWSet contributingRWSet;
    CriticalSection tn;
    Stmt begin;
    boolean lostObjects;
    Map<Ref, EquivalentValue> refToBase;
    Map<Ref, EquivalentValue> refToIndex;
    private static final Logger logger = LoggerFactory.getLogger(LockableReferenceAnalysis.class);
    static Set<SootMethod> analyzing = new HashSet();
    static int groupNum = 1;

    public LockableReferenceAnalysis(UnitGraph g) {
        super(g);
        this.graph = g;
        this.method = g.getBody().getMethod();
        this.contributingRWSet = null;
        this.tn = null;
        this.begin = null;
        this.lostObjects = false;
        this.refToBase = new HashMap();
        this.refToIndex = new HashMap();
    }

    public void printMsg(String msg) {
        logger.debug("[wjtp.tn] ");
        for (int i = 0; i < analyzing.size() - 1; i++) {
            logger.debug("  ");
        }
        logger.debug(msg);
    }

    public List<EquivalentValue> getLocksetOf(CriticalSectionAwareSideEffectAnalysis tasea, RWSet contributingRWSet, CriticalSection tn) {
        List<EquivalentValue> keys;
        analyzing.add(this.method);
        this.tasea = tasea;
        tasea.setExemptTransaction(tn);
        this.contributingRWSet = contributingRWSet;
        this.tn = tn;
        this.begin = tn == null ? null : tn.beginning;
        this.lostObjects = false;
        doAnalysis();
        if (this.lostObjects) {
            printMsg("Failed lockset:");
            analyzing.remove(this.method);
            return null;
        }
        List<EquivalentValue> lockset = new ArrayList<>();
        LocksetFlowInfo resultsInfo = null;
        if (this.begin == null) {
            Iterator<Unit> it = this.graph.iterator();
            while (it.hasNext()) {
                Unit u = it.next();
                resultsInfo = getFlowBefore(u);
            }
        } else {
            resultsInfo = getFlowBefore(this.begin);
        }
        if (resultsInfo == null) {
            analyzing.remove(this.method);
            throw new RuntimeException("Why is getFlowBefore null???");
        }
        Map<EquivalentValue, Integer> results = resultsInfo.groups;
        Map<Integer, List<EquivalentValue>> reversed = new HashMap<>();
        for (Map.Entry<EquivalentValue, Integer> e : results.entrySet()) {
            EquivalentValue key = e.getKey();
            Integer value = e.getValue();
            if (!reversed.containsKey(value)) {
                keys = new ArrayList<>();
                reversed.put(value, keys);
            } else {
                keys = reversed.get(value);
            }
            keys.add(key);
        }
        for (List<EquivalentValue> objects : reversed.values()) {
            EquivalentValue bestLock = null;
            for (EquivalentValue object : objects) {
                if (bestLock == null || (object.getValue() instanceof IdentityRef) || ((object.getValue() instanceof Ref) && !(bestLock instanceof IdentityRef))) {
                    bestLock = object;
                }
            }
            Integer group = results.get(bestLock);
            for (Ref ref : resultsInfo.refToBaseGroup.keySet()) {
                if (group == resultsInfo.refToBaseGroup.get(ref)) {
                    this.refToBase.put(ref, bestLock);
                }
            }
            for (Ref ref2 : resultsInfo.refToIndexGroup.keySet()) {
                if (group == resultsInfo.refToIndexGroup.get(ref2)) {
                    this.refToIndex.put(ref2, bestLock);
                }
            }
            if (group.intValue() >= 0) {
                lockset.add(bestLock);
            }
        }
        if (lockset.size() == 0) {
            printMsg("Empty lockset: S" + lockset.size() + "/G" + reversed.keySet().size() + "/O" + results.keySet().size() + " Method:" + this.method + " Begin:" + this.begin + " Result:" + results + " RW:" + contributingRWSet);
            printMsg("|= results:" + results + " refToBaseGroup:" + resultsInfo.refToBaseGroup);
        } else {
            printMsg("Healthy lockset: S" + lockset.size() + "/G" + reversed.keySet().size() + "/O" + results.keySet().size() + Instruction.argsep + lockset + " refToBase:" + this.refToBase + " refToIndex:" + this.refToIndex);
            printMsg("|= results:" + results + " refToBaseGroup:" + resultsInfo.refToBaseGroup);
        }
        analyzing.remove(this.method);
        return lockset;
    }

    public EquivalentValue baseFor(Ref ref) {
        return this.refToBase.get(ref);
    }

    public EquivalentValue indexFor(Ref ref) {
        return this.refToIndex.get(ref);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(LocksetFlowInfo in1, LocksetFlowInfo in2, LocksetFlowInfo out) {
        LocksetFlowInfo tmpInfo = new LocksetFlowInfo();
        copy(in1, out);
        copy(in2, tmpInfo);
        for (EquivalentValue key : tmpInfo.groups.keySet()) {
            Integer newvalue = tmpInfo.groups.get(key);
            if (!out.groups.containsKey(key)) {
                out.groups.put(key, newvalue);
            } else if (out.groups.get(key) != tmpInfo.groups.get(key)) {
                Integer oldvalue = out.groups.get(key);
                for (Map.Entry<?, Integer> entry : out.groups.entrySet()) {
                    if (entry.getValue() == oldvalue) {
                        entry.setValue(newvalue);
                    }
                }
                for (Map.Entry<?, Integer> entry2 : tmpInfo.groups.entrySet()) {
                    if (entry2.getValue() == oldvalue) {
                        entry2.setValue(newvalue);
                    }
                }
                for (Map.Entry<?, Integer> entry3 : out.refToBaseGroup.entrySet()) {
                    if (entry3.getValue() == oldvalue) {
                        entry3.setValue(newvalue);
                    }
                }
                for (Map.Entry<?, Integer> entry4 : out.refToIndexGroup.entrySet()) {
                    if (entry4.getValue() == oldvalue) {
                        entry4.setValue(newvalue);
                    }
                }
                for (Map.Entry<?, Integer> entry5 : tmpInfo.refToBaseGroup.entrySet()) {
                    if (entry5.getValue() == oldvalue) {
                        entry5.setValue(newvalue);
                    }
                }
                for (Map.Entry<?, Integer> entry6 : tmpInfo.refToIndexGroup.entrySet()) {
                    if (entry6.getValue() == oldvalue) {
                        entry6.setValue(newvalue);
                    }
                }
            }
        }
        for (Ref ref : tmpInfo.refToBaseGroup.keySet()) {
            if (!out.refToBaseGroup.containsKey(ref)) {
                out.refToBaseGroup.put(ref, tmpInfo.refToBaseGroup.get(ref));
            }
        }
        for (Ref ref2 : tmpInfo.refToIndexGroup.keySet()) {
            if (!out.refToIndexGroup.containsKey(ref2)) {
                out.refToIndexGroup.put(ref2, tmpInfo.refToIndexGroup.get(ref2));
            }
        }
    }

    public Integer addFromSubanalysis(LocksetFlowInfo outInfo, LockableReferenceAnalysis la, Stmt stmt, Value lock) {
        Map<EquivalentValue, Integer> out = outInfo.groups;
        InvokeExpr ie = stmt.getInvokeExpr();
        printMsg("Attempting to bring up '" + lock + "' from inner lockset at (" + stmt.hashCode() + ") " + stmt);
        if ((lock instanceof ThisRef) && (ie instanceof InstanceInvokeExpr)) {
            Value use = ((InstanceInvokeExpr) ie).getBase();
            if (!out.containsKey(new EquivalentValue(use))) {
                int newGroup = groupNum;
                groupNum = newGroup + 1;
                out.put(new EquivalentValue(use), Integer.valueOf(newGroup));
                return Integer.valueOf(newGroup);
            }
            return out.get(new EquivalentValue(use));
        } else if (lock instanceof ParameterRef) {
            Value use2 = ie.getArg(((ParameterRef) lock).getIndex());
            if (!out.containsKey(new EquivalentValue(use2))) {
                int newGroup2 = groupNum;
                groupNum = newGroup2 + 1;
                out.put(new EquivalentValue(use2), Integer.valueOf(newGroup2));
                return Integer.valueOf(newGroup2);
            }
            return out.get(new EquivalentValue(use2));
        } else if (lock instanceof StaticFieldRef) {
            if (!out.containsKey(new EquivalentValue(lock))) {
                int newGroup3 = groupNum;
                groupNum = newGroup3 + 1;
                out.put(new EquivalentValue(lock), Integer.valueOf(newGroup3));
                return Integer.valueOf(newGroup3);
            }
            return out.get(new EquivalentValue(lock));
        } else if (lock instanceof InstanceFieldRef) {
            if (((InstanceFieldRef) lock).getBase() instanceof FakeJimpleLocal) {
                ((FakeJimpleLocal) ((InstanceFieldRef) lock).getBase()).setInfo(this);
            }
            EquivalentValue baseEqVal = la.baseFor((Ref) lock);
            if (baseEqVal == null) {
                printMsg("Lost Object from inner Lockset (InstanceFieldRef w/ previously lost base) at " + stmt);
                return 0;
            }
            Value base = baseEqVal.getValue();
            Integer baseGroup = addFromSubanalysis(outInfo, la, stmt, base);
            if (baseGroup.intValue() == 0) {
                printMsg("Lost Object from inner Lockset (InstanceFieldRef w/ newly lost base) at " + stmt);
                return 0;
            }
            outInfo.refToBaseGroup.put((Ref) lock, baseGroup);
            if (!out.containsKey(new EquivalentValue(lock))) {
                int newGroup4 = groupNum;
                groupNum = newGroup4 + 1;
                out.put(new EquivalentValue(lock), Integer.valueOf(newGroup4));
                return Integer.valueOf(newGroup4);
            }
            return out.get(new EquivalentValue(lock));
        } else if (lock instanceof ArrayRef) {
            if (((ArrayRef) lock).getBase() instanceof FakeJimpleLocal) {
                ((FakeJimpleLocal) ((ArrayRef) lock).getBase()).setInfo(this);
            }
            if (((ArrayRef) lock).getIndex() instanceof FakeJimpleLocal) {
                ((FakeJimpleLocal) ((ArrayRef) lock).getIndex()).setInfo(this);
            }
            EquivalentValue baseEqVal2 = la.baseFor((Ref) lock);
            EquivalentValue indexEqVal = la.indexFor((Ref) lock);
            if (baseEqVal2 == null) {
                printMsg("Lost Object from inner Lockset (InstanceFieldRef w/ previously lost base) at " + stmt);
                return 0;
            } else if (indexEqVal == null) {
                printMsg("Lost Object from inner Lockset (InstanceFieldRef w/ previously lost index) at " + stmt);
                return 0;
            } else {
                Value base2 = baseEqVal2.getValue();
                Value index = indexEqVal.getValue();
                Integer baseGroup2 = addFromSubanalysis(outInfo, la, stmt, base2);
                if (baseGroup2.intValue() == 0) {
                    printMsg("Lost Object from inner Lockset (InstanceFieldRef w/ newly lost base) at " + stmt);
                    return 0;
                }
                Integer indexGroup = addFromSubanalysis(outInfo, la, stmt, index);
                if (indexGroup.intValue() == 0) {
                    printMsg("Lost Object from inner Lockset (InstanceFieldRef w/ newly lost index) at " + stmt);
                    return 0;
                }
                outInfo.refToBaseGroup.put((Ref) lock, baseGroup2);
                outInfo.refToIndexGroup.put((Ref) lock, indexGroup);
                if (!out.containsKey(new EquivalentValue(lock))) {
                    int newGroup5 = groupNum;
                    groupNum = newGroup5 + 1;
                    out.put(new EquivalentValue(lock), Integer.valueOf(newGroup5));
                    return Integer.valueOf(newGroup5);
                }
                return out.get(new EquivalentValue(lock));
            }
        } else if (lock instanceof Constant) {
            if (!out.containsKey(new EquivalentValue(lock))) {
                int newGroup6 = groupNum;
                groupNum = newGroup6 + 1;
                out.put(new EquivalentValue(lock), Integer.valueOf(newGroup6));
                return Integer.valueOf(newGroup6);
            }
            return out.get(new EquivalentValue(lock));
        } else {
            printMsg("Lost Object from inner Lockset (unknown or unhandled object type) at " + stmt);
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(LocksetFlowInfo inInfo, Unit u, LocksetFlowInfo outInfo) {
        Value value;
        Value use;
        Integer indexGroup;
        Integer baseGroup;
        Integer baseGroup2;
        copy(inInfo, outInfo);
        Stmt stmt = (Stmt) u;
        Map<EquivalentValue, Integer> out = outInfo.groups;
        if ((this.tn == null || this.tn.units.contains(stmt)) && !this.lostObjects) {
            CodeBlockRWSet stmtRW = null;
            Set<Value> allUses = new HashSet<>();
            RWSet stmtRead = this.tasea.readSet(this.method, stmt, this.tn, allUses);
            if (stmtRead != null) {
                stmtRW = (CodeBlockRWSet) stmtRead;
            }
            RWSet stmtWrite = this.tasea.writeSet(this.method, stmt, this.tn, allUses);
            if (stmtWrite != null) {
                if (stmtRW != null) {
                    stmtRW.union(stmtWrite);
                } else {
                    stmtRW = (CodeBlockRWSet) stmtWrite;
                }
            }
            if (stmtRW != null && stmtRW.hasNonEmptyIntersection(this.contributingRWSet)) {
                ArrayList arrayList = new ArrayList();
                for (Value value2 : allUses) {
                    Value v = value2;
                    if (stmt.containsFieldRef()) {
                        Value fr = stmt.getFieldRef();
                        if ((fr instanceof InstanceFieldRef) && ((InstanceFieldRef) fr).getBase() == v) {
                            v = fr;
                        }
                    }
                    if (stmt.containsArrayRef()) {
                        ArrayRef ar = stmt.getArrayRef();
                        if (ar.getBase() == v) {
                            v = ar;
                        }
                    }
                    RWSet valRW = this.tasea.valueRWSet(v, this.method, stmt, this.tn);
                    if (valRW != null && valRW.hasNonEmptyIntersection(this.contributingRWSet)) {
                        arrayList.add(value2);
                    }
                }
                if (stmt.containsInvokeExpr()) {
                    InvokeExpr ie = stmt.getInvokeExpr();
                    SootMethod called = ie.getMethod();
                    if (called.isConcrete()) {
                        if (called.getDeclaringClass().toString().startsWith("java.util") || called.getDeclaringClass().toString().startsWith("java.lang")) {
                            if (arrayList.size() <= 0) {
                                printMsg("Lost Object at library call at " + stmt);
                                this.lostObjects = true;
                            }
                        } else if (!analyzing.contains(called)) {
                            LockableReferenceAnalysis la = new LockableReferenceAnalysis(new BriefUnitGraph(called.retrieveActiveBody()));
                            List<EquivalentValue> innerLockset = la.getLocksetOf(this.tasea, stmtRW, null);
                            if (innerLockset == null || innerLockset.size() <= 0) {
                                printMsg("innerLockset: " + (innerLockset == null ? "Lost Objects" : "Mysteriously Empty"));
                                this.lostObjects = true;
                            } else {
                                printMsg("innerLockset: " + innerLockset.toString());
                                Iterator<EquivalentValue> it = innerLockset.iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        break;
                                    }
                                    EquivalentValue lockEqVal = it.next();
                                    Value lock = lockEqVal.getValue();
                                    if (addFromSubanalysis(outInfo, la, stmt, lock).intValue() == 0) {
                                        this.lostObjects = true;
                                        printMsg("Lost Object in addFromSubanalysis()");
                                        break;
                                    }
                                }
                            }
                        } else {
                            this.lostObjects = true;
                            printMsg("Lost Object due to recursion " + stmt);
                        }
                    } else if (arrayList.size() <= 0) {
                        this.lostObjects = true;
                        printMsg("Lost Object from non-concrete method call at " + stmt);
                    }
                } else if (arrayList.size() <= 0) {
                    this.lostObjects = true;
                    printMsg("Lost Object SOMEHOW at " + stmt);
                }
                Iterator<Value> usesIt = arrayList.iterator();
                while (usesIt.hasNext() && !this.lostObjects) {
                    Value use2 = usesIt.next();
                    if (use2 instanceof InstanceFieldRef) {
                        InstanceFieldRef ifr = (InstanceFieldRef) use2;
                        Local oldbase = (Local) ifr.getBase();
                        use = use2;
                        if (!(oldbase instanceof FakeJimpleLocal)) {
                            Local newbase = new FakeJimpleLocal("fakethis", oldbase.getType(), oldbase, this);
                            Value newInstanceFieldRef = Jimple.v().newInstanceFieldRef(newbase, ifr.getField().makeRef());
                            new EquivalentValue(newInstanceFieldRef);
                            use = newInstanceFieldRef;
                        }
                    } else {
                        boolean z = use2 instanceof ArrayRef;
                        use = use2;
                        if (z) {
                            ArrayRef ar2 = (ArrayRef) use2;
                            Local oldbase2 = (Local) ar2.getBase();
                            Value oldindex = ar2.getIndex();
                            use = use2;
                            if (!(oldbase2 instanceof FakeJimpleLocal)) {
                                Local newbase2 = new FakeJimpleLocal("fakethis", oldbase2.getType(), oldbase2, this);
                                if (oldindex instanceof Local) {
                                    value = new FakeJimpleLocal("fakeindex", oldindex.getType(), (Local) oldindex, this);
                                } else {
                                    value = oldindex;
                                }
                                Value newindex = value;
                                Value newArrayRef = Jimple.v().newArrayRef(newbase2, newindex);
                                new EquivalentValue(newArrayRef);
                                use = newArrayRef;
                            }
                        }
                    }
                    if (!out.containsKey(new EquivalentValue(use))) {
                        EquivalentValue equivalentValue = new EquivalentValue(use);
                        int i = groupNum;
                        groupNum = i + 1;
                        out.put(equivalentValue, Integer.valueOf(i));
                    }
                }
            }
        }
        if (this.graph.getBody().getUnits().getSuccOf((UnitPatchingChain) stmt) == this.begin) {
            out.clear();
        }
        if ((this.tn == null || this.tn.units.contains(stmt)) && !out.isEmpty() && (stmt instanceof DefinitionStmt) && !this.lostObjects) {
            DefinitionStmt ds = (DefinitionStmt) stmt;
            EquivalentValue lvalue = new EquivalentValue(ds.getLeftOp());
            if (ds.getLeftOp() instanceof InstanceFieldRef) {
                InstanceFieldRef ifr2 = (InstanceFieldRef) ds.getLeftOp();
                Local oldbase3 = (Local) ifr2.getBase();
                if (!(oldbase3 instanceof FakeJimpleLocal)) {
                    Local newbase3 = new FakeJimpleLocal("fakethis", oldbase3.getType(), oldbase3, this);
                    Value node = Jimple.v().newInstanceFieldRef(newbase3, ifr2.getField().makeRef());
                    EquivalentValue nodeEqVal = new EquivalentValue(node);
                    lvalue = nodeEqVal;
                }
            } else if (ds.getLeftOp() instanceof ArrayRef) {
                ArrayRef ar3 = (ArrayRef) ds.getLeftOp();
                Local oldbase4 = (Local) ar3.getBase();
                Value oldindex2 = ar3.getIndex();
                if (!(oldbase4 instanceof FakeJimpleLocal)) {
                    Local newbase4 = new FakeJimpleLocal("fakethis", oldbase4.getType(), oldbase4, this);
                    Value newindex2 = oldindex2 instanceof Local ? new FakeJimpleLocal("fakeindex", oldindex2.getType(), (Local) oldindex2, this) : oldindex2;
                    Value node2 = Jimple.v().newArrayRef(newbase4, newindex2);
                    EquivalentValue nodeEqVal2 = new EquivalentValue(node2);
                    lvalue = nodeEqVal2;
                }
            }
            EquivalentValue rvalue = new EquivalentValue(ds.getRightOp());
            if (ds.getRightOp() instanceof CastExpr) {
                rvalue = new EquivalentValue(((CastExpr) ds.getRightOp()).getOp());
            } else if (ds.getRightOp() instanceof InstanceFieldRef) {
                InstanceFieldRef ifr3 = (InstanceFieldRef) ds.getRightOp();
                Local oldbase5 = (Local) ifr3.getBase();
                if (!(oldbase5 instanceof FakeJimpleLocal)) {
                    Local newbase5 = new FakeJimpleLocal("fakethis", oldbase5.getType(), oldbase5, this);
                    Value node3 = Jimple.v().newInstanceFieldRef(newbase5, ifr3.getField().makeRef());
                    EquivalentValue nodeEqVal3 = new EquivalentValue(node3);
                    rvalue = nodeEqVal3;
                }
            } else if (ds.getRightOp() instanceof ArrayRef) {
                ArrayRef ar4 = (ArrayRef) ds.getRightOp();
                Local oldbase6 = (Local) ar4.getBase();
                Value oldindex3 = ar4.getIndex();
                if (!(oldbase6 instanceof FakeJimpleLocal)) {
                    Local newbase6 = new FakeJimpleLocal("fakethis", oldbase6.getType(), oldbase6, this);
                    Value newindex3 = oldindex3 instanceof Local ? new FakeJimpleLocal("fakeindex", oldindex3.getType(), (Local) oldindex3, this) : oldindex3;
                    Value node4 = Jimple.v().newArrayRef(newbase6, newindex3);
                    EquivalentValue nodeEqVal4 = new EquivalentValue(node4);
                    rvalue = nodeEqVal4;
                }
            }
            if (out.containsKey(lvalue)) {
                Integer lvaluevalue = out.get(lvalue);
                if (stmt instanceof IdentityStmt) {
                    if (out.containsKey(rvalue)) {
                        Integer rvaluevalue = out.get(rvalue);
                        for (Map.Entry<?, Integer> entry : out.entrySet()) {
                            if (entry.getValue() == lvaluevalue) {
                                entry.setValue(rvaluevalue);
                            }
                        }
                        for (Map.Entry<?, Integer> entry2 : outInfo.refToBaseGroup.entrySet()) {
                            if (entry2.getValue() == lvaluevalue) {
                                entry2.setValue(rvaluevalue);
                            }
                        }
                        for (Map.Entry<?, Integer> entry3 : outInfo.refToIndexGroup.entrySet()) {
                            if (entry3.getValue() == lvaluevalue) {
                                entry3.setValue(rvaluevalue);
                            }
                        }
                        return;
                    }
                    out.put(rvalue, lvaluevalue);
                    return;
                }
                if (out.containsKey(rvalue)) {
                    Integer rvaluevalue2 = out.get(rvalue);
                    for (Map.Entry<?, Integer> entry4 : out.entrySet()) {
                        if (entry4.getValue() == lvaluevalue) {
                            entry4.setValue(rvaluevalue2);
                        }
                    }
                    for (Map.Entry<?, Integer> entry5 : outInfo.refToBaseGroup.entrySet()) {
                        if (entry5.getValue() == lvaluevalue) {
                            entry5.setValue(rvaluevalue2);
                        }
                    }
                    for (Map.Entry<?, Integer> entry6 : outInfo.refToIndexGroup.entrySet()) {
                        if (entry6.getValue() == lvaluevalue) {
                            entry6.setValue(rvaluevalue2);
                        }
                    }
                } else if ((rvalue.getValue() instanceof Local) || (rvalue.getValue() instanceof StaticFieldRef) || (rvalue.getValue() instanceof Constant)) {
                    out.put(rvalue, lvaluevalue);
                } else if (rvalue.getValue() instanceof InstanceFieldRef) {
                    InstanceFieldRef ifr4 = (InstanceFieldRef) rvalue.getValue();
                    FakeJimpleLocal newbase7 = (FakeJimpleLocal) ifr4.getBase();
                    Local oldbase7 = newbase7.getRealLocal();
                    out.put(rvalue, lvaluevalue);
                    if (out.containsKey(new EquivalentValue(oldbase7))) {
                        baseGroup2 = out.get(new EquivalentValue(oldbase7));
                    } else {
                        int i2 = groupNum;
                        groupNum = i2 + 1;
                        baseGroup2 = new Integer(-i2);
                    }
                    if (!outInfo.refToBaseGroup.containsKey(ifr4)) {
                        outInfo.refToBaseGroup.put(ifr4, baseGroup2);
                    }
                    out.put(new EquivalentValue(oldbase7), baseGroup2);
                } else if (rvalue.getValue() instanceof ArrayRef) {
                    ArrayRef ar5 = (ArrayRef) rvalue.getValue();
                    FakeJimpleLocal newbase8 = (FakeJimpleLocal) ar5.getBase();
                    Local oldbase8 = newbase8.getRealLocal();
                    FakeJimpleLocal newindex4 = ar5.getIndex() instanceof FakeJimpleLocal ? (FakeJimpleLocal) ar5.getIndex() : null;
                    Value oldindex4 = newindex4 != null ? newindex4.getRealLocal() : ar5.getIndex();
                    out.put(rvalue, lvaluevalue);
                    if (out.containsKey(new EquivalentValue(oldindex4))) {
                        indexGroup = out.get(new EquivalentValue(oldindex4));
                    } else {
                        int i3 = groupNum;
                        groupNum = i3 + 1;
                        indexGroup = new Integer(-i3);
                    }
                    if (!outInfo.refToIndexGroup.containsKey(ar5)) {
                        outInfo.refToIndexGroup.put(ar5, indexGroup);
                    }
                    out.put(new EquivalentValue(oldindex4), indexGroup);
                    if (out.containsKey(new EquivalentValue(oldbase8))) {
                        baseGroup = out.get(new EquivalentValue(oldbase8));
                    } else {
                        int i4 = groupNum;
                        groupNum = i4 + 1;
                        baseGroup = new Integer(-i4);
                    }
                    if (!outInfo.refToBaseGroup.containsKey(ar5)) {
                        outInfo.refToBaseGroup.put(ar5, baseGroup);
                    }
                    out.put(new EquivalentValue(oldbase8), baseGroup);
                } else if (rvalue.getValue() instanceof AnyNewExpr) {
                    printMsg("Ignored Object (assigned new value) at " + stmt);
                } else {
                    printMsg("Lost Object (assigned unacceptable value) at " + stmt);
                    this.lostObjects = true;
                }
                out.remove(lvalue);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(LocksetFlowInfo sourceInfo, LocksetFlowInfo destInfo) {
        destInfo.groups.clear();
        destInfo.groups.putAll(sourceInfo.groups);
        destInfo.refToBaseGroup.clear();
        destInfo.refToBaseGroup.putAll(sourceInfo.refToBaseGroup);
        destInfo.refToIndexGroup.clear();
        destInfo.refToIndexGroup.putAll(sourceInfo.refToIndexGroup);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public LocksetFlowInfo newInitialFlow() {
        return new LocksetFlowInfo();
    }
}
