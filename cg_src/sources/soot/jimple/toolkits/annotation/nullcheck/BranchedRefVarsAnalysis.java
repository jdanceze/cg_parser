package soot.jimple.toolkits.annotation.nullcheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.EquivalentValue;
import soot.Local;
import soot.NullType;
import soot.RefType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.DefinitionStmt;
import soot.jimple.EqExpr;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.MonitorStmt;
import soot.jimple.NeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArrayFlowUniverse;
import soot.toolkits.scalar.ArrayPackedSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardBranchedFlowAnalysis;
@Deprecated
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/nullcheck/BranchedRefVarsAnalysis.class */
public class BranchedRefVarsAnalysis extends ForwardBranchedFlowAnalysis<FlowSet<RefIntPair>> {
    private static final Logger logger = LoggerFactory.getLogger(BranchedRefVarsAnalysis.class);
    private static final boolean isNotConservative = false;
    private static final boolean isBranched = true;
    private static final boolean careForAliases = false;
    private static final boolean careForMethodCalls = true;
    public static final int kBottom = 0;
    public static final int kNull = 1;
    public static final int kNonNull = 2;
    public static final int kTop = 99;
    protected final FlowSet<RefIntPair> emptySet;
    protected final FlowSet<RefIntPair> fullSet;
    protected final Map<Unit, FlowSet<RefIntPair>> unitToGenerateSet;
    protected final Map<Unit, FlowSet<RefIntPair>> unitToPreserveSet;
    protected final Map<Unit, HashSet<Value>> unitToAnalyzedChecksSet;
    protected final Map<Unit, HashSet<Value>> unitToArrayRefChecksSet;
    protected final Map<Unit, HashSet<Value>> unitToInstanceFieldRefChecksSet;
    protected final Map<Unit, HashSet<Value>> unitToInstanceInvokeExprChecksSet;
    protected final Map<Unit, HashSet<Value>> unitToLengthExprChecksSet;
    protected final List<EquivalentValue> refTypeLocals;
    protected final List<EquivalentValue> refTypeInstFields;
    protected final List<EquivalentValue> refTypeInstFieldBases;
    protected final List<EquivalentValue> refTypeStaticFields;
    protected final List<EquivalentValue> refTypeValues;
    private final HashMap<Value, EquivalentValue> valueToEquivValue;
    private final HashMap<EquivalentValue, RefIntPair> kRefBotttomPairs;
    private final HashMap<EquivalentValue, RefIntPair> kRefNonNullPairs;
    private final HashMap<EquivalentValue, RefIntPair> kRefNullPairs;
    private final HashMap<EquivalentValue, RefIntPair> kRefTopPairs;
    protected FlowSet<RefIntPair> tempFlowSet;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.BranchedFlowAnalysis
    public /* bridge */ /* synthetic */ void flowThrough(Object obj, Unit unit, List list, List list2) {
        flowThrough((FlowSet) obj, unit, (List<FlowSet<RefIntPair>>) list, (List<FlowSet<RefIntPair>>) list2);
    }

    @Deprecated
    public BranchedRefVarsAnalysis(UnitGraph g) {
        super(g);
        this.valueToEquivValue = new HashMap<>(2293, 0.7f);
        this.kRefBotttomPairs = new HashMap<>(2293, 0.7f);
        this.kRefNonNullPairs = new HashMap<>(2293, 0.7f);
        this.kRefNullPairs = new HashMap<>(2293, 0.7f);
        this.kRefTopPairs = new HashMap<>(2293, 0.7f);
        this.tempFlowSet = null;
        this.refTypeLocals = new ArrayList();
        this.refTypeInstFields = new ArrayList();
        this.refTypeInstFieldBases = new ArrayList();
        this.refTypeStaticFields = new ArrayList();
        this.refTypeValues = new ArrayList();
        initRefTypeLists();
        int len = this.refTypeValues.size();
        RefIntPair[] universeArray = new RefIntPair[2 * len];
        for (int i = 0; i < len; i++) {
            int j = i * 2;
            EquivalentValue r = this.refTypeValues.get(i);
            universeArray[j] = getKRefIntPair(r, 1);
            universeArray[j + 1] = getKRefIntPair(r, 2);
        }
        ArrayPackedSet<RefIntPair> temp = new ArrayPackedSet<>(new ArrayFlowUniverse(universeArray));
        this.emptySet = temp;
        this.fullSet = temp.mo2534clone();
        temp.complement(this.fullSet);
        this.tempFlowSet = newInitialFlow();
        int cap = (this.graph.size() * 2) + 1;
        this.unitToGenerateSet = new HashMap(cap, 0.7f);
        this.unitToPreserveSet = new HashMap(cap, 0.7f);
        this.unitToAnalyzedChecksSet = new HashMap(cap, 0.7f);
        this.unitToArrayRefChecksSet = new HashMap(cap, 0.7f);
        this.unitToInstanceFieldRefChecksSet = new HashMap(cap, 0.7f);
        this.unitToInstanceInvokeExprChecksSet = new HashMap(cap, 0.7f);
        this.unitToLengthExprChecksSet = new HashMap(cap, 0.7f);
        initUnitSets();
        doAnalysis();
    }

    public EquivalentValue getEquivalentValue(Value v) {
        if (this.valueToEquivValue.containsKey(v)) {
            return this.valueToEquivValue.get(v);
        }
        EquivalentValue ev = new EquivalentValue(v);
        this.valueToEquivValue.put(v, ev);
        return ev;
    }

    public RefIntPair getKRefIntPair(EquivalentValue r, int v) {
        HashMap<EquivalentValue, RefIntPair> pairsMap;
        switch (v) {
            case 0:
                pairsMap = this.kRefBotttomPairs;
                break;
            case 1:
                pairsMap = this.kRefNullPairs;
                break;
            case 2:
                pairsMap = this.kRefNonNullPairs;
                break;
            case 99:
                pairsMap = this.kRefTopPairs;
                break;
            default:
                throw new RuntimeException("invalid constant (" + v + ")");
        }
        if (pairsMap.containsKey(r)) {
            return pairsMap.get(r);
        }
        RefIntPair pair = new RefIntPair(r, v, this);
        pairsMap.put(r, pair);
        return pair;
    }

    private static boolean isAlwaysNull(Value r) {
        return (r instanceof NullConstant) || (r.getType() instanceof NullType);
    }

    private static boolean isAlwaysTop(Value r) {
        return (r instanceof InstanceFieldRef) || (r instanceof StaticFieldRef);
    }

    private static boolean isAlwaysNonNull(Value ro) {
        return (ro instanceof NewExpr) || (ro instanceof NewArrayExpr) || (ro instanceof NewMultiArrayExpr) || (ro instanceof ThisRef) || (ro instanceof CaughtExceptionRef) || (ro instanceof StringConstant);
    }

    private static boolean isAnalyzedRef(Value r) {
        if (isAlwaysNull(r) || isAlwaysTop(r)) {
            return false;
        }
        if ((r instanceof Local) || (r instanceof InstanceFieldRef) || (r instanceof StaticFieldRef)) {
            Type rType = r.getType();
            return (rType instanceof RefType) || (rType instanceof ArrayType);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final int refInfo(EquivalentValue r, FlowSet<RefIntPair> fs) {
        boolean isNull = fs.contains(getKRefIntPair(r, 1));
        boolean isNonNull = fs.contains(getKRefIntPair(r, 2));
        if (isNull && isNonNull) {
            return 99;
        }
        if (isNull) {
            return 1;
        }
        if (isNonNull) {
            return 2;
        }
        return 0;
    }

    private int refInfo(Value r, FlowSet<RefIntPair> fs) {
        return refInfo(getEquivalentValue(r), fs);
    }

    public final int anyRefInfo(Value r, FlowSet<RefIntPair> f) {
        if (isAlwaysNull(r)) {
            return 1;
        }
        if (isAlwaysTop(r)) {
            return 99;
        }
        if (isAlwaysNonNull(r)) {
            return 2;
        }
        return refInfo(r, f);
    }

    private void uAddTopToFlowSet(EquivalentValue r, FlowSet<RefIntPair> genFS, FlowSet<RefIntPair> preFS) {
        RefIntPair nullPair = getKRefIntPair(r, 1);
        RefIntPair nullNonPair = getKRefIntPair(r, 2);
        if (genFS != preFS) {
            preFS.remove(nullPair, preFS);
            preFS.remove(nullNonPair, preFS);
        }
        genFS.add(nullPair, genFS);
        genFS.add(nullNonPair, genFS);
    }

    private void uAddTopToFlowSet(Value r, FlowSet<RefIntPair> genFS, FlowSet<RefIntPair> preFS) {
        uAddTopToFlowSet(getEquivalentValue(r), genFS, preFS);
    }

    private void uAddTopToFlowSet(Value r, FlowSet<RefIntPair> fs) {
        uAddTopToFlowSet(getEquivalentValue(r), fs, fs);
    }

    private void uAddTopToFlowSet(EquivalentValue r, FlowSet<RefIntPair> fs) {
        uAddTopToFlowSet(r, fs, fs);
    }

    private void uAddInfoToFlowSet(EquivalentValue r, int v, FlowSet<RefIntPair> genFS, FlowSet<RefIntPair> preFS) {
        int kill;
        switch (v) {
            case 1:
                kill = 2;
                break;
            case 2:
                kill = 1;
                break;
            default:
                throw new RuntimeException("invalid info");
        }
        if (genFS != preFS) {
            preFS.remove(getKRefIntPair(r, kill), preFS);
        }
        genFS.remove(getKRefIntPair(r, kill), genFS);
        genFS.add(getKRefIntPair(r, v), genFS);
    }

    private void uAddInfoToFlowSet(Value r, int v, FlowSet<RefIntPair> genF, FlowSet<RefIntPair> preF) {
        uAddInfoToFlowSet(getEquivalentValue(r), v, genF, preF);
    }

    private void uAddInfoToFlowSet(Value r, int v, FlowSet<RefIntPair> fs) {
        uAddInfoToFlowSet(getEquivalentValue(r), v, fs, fs);
    }

    private void uAddInfoToFlowSet(EquivalentValue r, int v, FlowSet<RefIntPair> fs) {
        uAddInfoToFlowSet(r, v, fs, fs);
    }

    private void uListAddTopToFlowSet(List<EquivalentValue> refs, FlowSet<RefIntPair> genFS, FlowSet<RefIntPair> preFS) {
        for (EquivalentValue ev : refs) {
            uAddTopToFlowSet(ev, genFS, preFS);
        }
    }

    private void initRefTypeLists() {
        for (Local l : ((UnitGraph) this.graph).getBody().getLocals()) {
            Type type = l.getType();
            if ((type instanceof RefType) || (type instanceof ArrayType)) {
                this.refTypeLocals.add(getEquivalentValue(l));
            }
        }
        this.refTypeValues.addAll(this.refTypeLocals);
        this.refTypeValues.addAll(this.refTypeInstFields);
        this.refTypeValues.addAll(this.refTypeStaticFields);
    }

    private void initRefTypeLists(ValueBox box) {
        Value val = box.getValue();
        if (!(val instanceof InstanceFieldRef)) {
            if (val instanceof StaticFieldRef) {
                StaticFieldRef sr = (StaticFieldRef) val;
                Type opType = sr.getType();
                if ((opType instanceof RefType) || (opType instanceof ArrayType)) {
                    EquivalentValue esr = getEquivalentValue(sr);
                    if (!this.refTypeStaticFields.contains(esr)) {
                        this.refTypeStaticFields.add(esr);
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        InstanceFieldRef ir = (InstanceFieldRef) val;
        Type opType2 = ir.getType();
        if ((opType2 instanceof RefType) || (opType2 instanceof ArrayType)) {
            EquivalentValue eir = getEquivalentValue(ir);
            if (!this.refTypeInstFields.contains(eir)) {
                this.refTypeInstFields.add(eir);
                EquivalentValue eirbase = getEquivalentValue(ir.getBase());
                if (!this.refTypeInstFieldBases.contains(eirbase)) {
                    this.refTypeInstFieldBases.add(eirbase);
                }
            }
        }
    }

    private void initUnitSets() {
        for (Unit s : this.graph) {
            FlowSet<RefIntPair> genSet = this.emptySet.mo2534clone();
            FlowSet<RefIntPair> preSet = this.fullSet.mo2534clone();
            if (((Stmt) s).containsInvokeExpr()) {
                uListAddTopToFlowSet(this.refTypeInstFields, genSet, preSet);
                uListAddTopToFlowSet(this.refTypeStaticFields, genSet, preSet);
            }
            for (ValueBox box : s.getDefBoxes()) {
                Value val = box.getValue();
                if (isAnalyzedRef(val)) {
                    uAddTopToFlowSet(val, genSet, preSet);
                }
            }
            if (s instanceof DefinitionStmt) {
                DefinitionStmt as = (DefinitionStmt) s;
                Value ro = as.getRightOp();
                if (ro instanceof CastExpr) {
                    ro = ((CastExpr) ro).getOp();
                }
                Value lo = as.getLeftOp();
                if (isAnalyzedRef(lo)) {
                    if (isAlwaysNonNull(ro)) {
                        uAddInfoToFlowSet(lo, 2, genSet, preSet);
                    } else if (isAlwaysNull(ro)) {
                        uAddInfoToFlowSet(lo, 1, genSet, preSet);
                    } else if (isAlwaysTop(ro)) {
                        uAddTopToFlowSet(lo, genSet, preSet);
                    }
                }
            }
            HashSet<Value> analyzedChecksSet = new HashSet<>(5, 0.7f);
            HashSet<Value> arrayRefChecksSet = new HashSet<>(5, 0.7f);
            HashSet<Value> instanceFieldRefChecksSet = new HashSet<>(5, 0.7f);
            HashSet<Value> instanceInvokeExprChecksSet = new HashSet<>(5, 0.7f);
            HashSet<Value> lengthExprChecksSet = new HashSet<>(5, 0.7f);
            for (ValueBox next : s.getUseBoxes()) {
                Value base = null;
                Value boxValue = next.getValue();
                if (boxValue instanceof InstanceFieldRef) {
                    base = ((InstanceFieldRef) boxValue).getBase();
                    instanceFieldRefChecksSet.add(base);
                } else if (boxValue instanceof ArrayRef) {
                    base = ((ArrayRef) boxValue).getBase();
                    arrayRefChecksSet.add(base);
                } else if (boxValue instanceof InstanceInvokeExpr) {
                    base = ((InstanceInvokeExpr) boxValue).getBase();
                    instanceInvokeExprChecksSet.add(base);
                } else if (boxValue instanceof LengthExpr) {
                    base = ((LengthExpr) boxValue).getOp();
                    lengthExprChecksSet.add(base);
                } else if (s instanceof ThrowStmt) {
                    base = ((ThrowStmt) s).getOp();
                } else if (s instanceof MonitorStmt) {
                    base = ((MonitorStmt) s).getOp();
                }
                if (base != null && isAnalyzedRef(base)) {
                    uAddInfoToFlowSet(base, 2, genSet, preSet);
                    analyzedChecksSet.add(base);
                }
            }
            for (ValueBox name : s.getDefBoxes()) {
                Value base2 = null;
                Value boxValue2 = name.getValue();
                if (boxValue2 instanceof InstanceFieldRef) {
                    base2 = ((InstanceFieldRef) boxValue2).getBase();
                    instanceFieldRefChecksSet.add(base2);
                } else if (boxValue2 instanceof ArrayRef) {
                    base2 = ((ArrayRef) boxValue2).getBase();
                    arrayRefChecksSet.add(base2);
                } else if (boxValue2 instanceof InstanceInvokeExpr) {
                    base2 = ((InstanceInvokeExpr) boxValue2).getBase();
                    instanceInvokeExprChecksSet.add(base2);
                } else if (boxValue2 instanceof LengthExpr) {
                    base2 = ((LengthExpr) boxValue2).getOp();
                    lengthExprChecksSet.add(base2);
                } else if (s instanceof ThrowStmt) {
                    base2 = ((ThrowStmt) s).getOp();
                } else if (s instanceof MonitorStmt) {
                    base2 = ((MonitorStmt) s).getOp();
                }
                if (base2 != null && isAnalyzedRef(base2)) {
                    uAddInfoToFlowSet(base2, 2, genSet, preSet);
                    analyzedChecksSet.add(base2);
                }
            }
            this.unitToGenerateSet.put(s, genSet);
            this.unitToPreserveSet.put(s, preSet);
            this.unitToAnalyzedChecksSet.put(s, analyzedChecksSet);
            this.unitToArrayRefChecksSet.put(s, arrayRefChecksSet);
            this.unitToInstanceFieldRefChecksSet.put(s, instanceFieldRefChecksSet);
            this.unitToInstanceInvokeExprChecksSet.put(s, instanceInvokeExprChecksSet);
            this.unitToLengthExprChecksSet.put(s, lengthExprChecksSet);
        }
    }

    protected void flowThrough(FlowSet<RefIntPair> in, Unit stmt, List<FlowSet<RefIntPair>> outFall, List<FlowSet<RefIntPair>> outBranch) {
        FlowSet<RefIntPair> out = this.tempFlowSet;
        FlowSet<RefIntPair> pre = this.unitToPreserveSet.get(stmt);
        FlowSet<RefIntPair> gen = this.unitToGenerateSet.get(stmt);
        in.intersection(pre, out);
        out.union(gen, out);
        if (stmt instanceof AssignStmt) {
            AssignStmt as = (AssignStmt) stmt;
            Value rightOp = as.getRightOp();
            if (rightOp instanceof CastExpr) {
                rightOp = ((CastExpr) rightOp).getOp();
            }
            Value leftOp = as.getLeftOp();
            if (isAnalyzedRef(leftOp) && isAnalyzedRef(rightOp)) {
                int roInfo = refInfo(rightOp, in);
                if (roInfo == 99) {
                    uAddTopToFlowSet(leftOp, out);
                } else if (roInfo != 0) {
                    uAddInfoToFlowSet(leftOp, roInfo, out);
                }
            }
        }
        for (FlowSet<RefIntPair> fs : outBranch) {
            copy(out, fs);
        }
        for (FlowSet<RefIntPair> fs2 : outFall) {
            copy(out, fs2);
        }
        if (stmt instanceof IfStmt) {
            Value cond = ((IfStmt) stmt).getCondition();
            Value op1 = ((BinopExpr) cond).getOp1();
            Value op2 = ((BinopExpr) cond).getOp2();
            if (isAlwaysTop(op1) || isAlwaysTop(op2)) {
                return;
            }
            if (isAnalyzedRef(op1) || isAnalyzedRef(op2)) {
                Value toGen = null;
                int toGenInfo = 0;
                int op1Info = anyRefInfo(op1, in);
                int op2Info = anyRefInfo(op2, in);
                boolean op2isKnown = op2Info == 1 || op2Info == 2;
                if (op1Info == 1 || op1Info == 2) {
                    if (!op2isKnown) {
                        toGen = op2;
                        toGenInfo = op1Info;
                    }
                } else if (op2isKnown) {
                    toGen = op1;
                    toGenInfo = op2Info;
                }
                if (toGen != null && isAnalyzedRef(toGen)) {
                    int fInfo = 0;
                    int bInfo = 0;
                    if (cond instanceof EqExpr) {
                        bInfo = toGenInfo;
                        if (toGenInfo == 1) {
                            fInfo = 2;
                        }
                    } else if (cond instanceof NeExpr) {
                        fInfo = toGenInfo;
                        if (toGenInfo == 1) {
                            bInfo = 2;
                        }
                    } else {
                        throw new RuntimeException("invalid condition");
                    }
                    if (fInfo != 0) {
                        for (FlowSet<RefIntPair> fs3 : outFall) {
                            copy(out, fs3);
                            uAddInfoToFlowSet(toGen, fInfo, fs3);
                        }
                    }
                    if (bInfo != 0) {
                        for (FlowSet<RefIntPair> fs4 : outBranch) {
                            copy(out, fs4);
                            uAddInfoToFlowSet(toGen, bInfo, fs4);
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(FlowSet<RefIntPair> in1, FlowSet<RefIntPair> in2, FlowSet<RefIntPair> out) {
        FlowSet<RefIntPair> inSet1Copy = in1.mo2534clone();
        FlowSet<RefIntPair> inSet2Copy = in2.mo2534clone();
        in1.intersection(in2, out);
        for (EquivalentValue r : this.refTypeValues) {
            int refInfoIn1 = refInfo(r, inSet1Copy);
            int refInfoIn2 = refInfo(r, inSet2Copy);
            if (refInfoIn1 != refInfoIn2) {
                if (refInfoIn1 == 99 || refInfoIn2 == 99) {
                    uAddTopToFlowSet(r, out);
                } else if (refInfoIn1 == 0) {
                    uAddInfoToFlowSet(r, refInfoIn2, out);
                } else if (refInfoIn2 == 0) {
                    uAddInfoToFlowSet(r, refInfoIn1, out);
                } else {
                    uAddTopToFlowSet(r, out);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(FlowSet<RefIntPair> source, FlowSet<RefIntPair> dest) {
        source.copy(dest);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<RefIntPair> newInitialFlow() {
        return this.emptySet.mo2534clone();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<RefIntPair> entryInitialFlow() {
        return this.fullSet.mo2534clone();
    }

    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public boolean treatTrapHandlersAsEntries() {
        return true;
    }
}
