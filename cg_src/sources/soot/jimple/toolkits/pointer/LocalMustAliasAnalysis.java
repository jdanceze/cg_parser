package soot.jimple.toolkits.pointer;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.EquivalentValue;
import soot.Local;
import soot.MethodOrMethodContext;
import soot.RefLikeType;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.CastExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/LocalMustAliasAnalysis.class */
public class LocalMustAliasAnalysis extends ForwardFlowAnalysis<Unit, HashMap<Value, Integer>> {
    protected Set<Value> localsAndFieldRefs;
    protected transient Map<Value, Integer> rhsToNumber;
    protected transient Map<Unit, Map<Value, Integer>> mergePointToValueToNumber;
    protected int nextNumber;
    protected SootMethod container;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !LocalMustAliasAnalysis.class.desiredAssertionStatus();
    }

    public LocalMustAliasAnalysis(UnitGraph g) {
        this(g, false);
    }

    public LocalMustAliasAnalysis(UnitGraph g, boolean tryTrackFieldAssignments) {
        super(g);
        this.nextNumber = 1;
        this.container = g.getBody().getMethod();
        this.localsAndFieldRefs = new HashSet();
        for (Local l : g.getBody().getLocals()) {
            if (l.getType() instanceof RefLikeType) {
                this.localsAndFieldRefs.add(l);
            }
        }
        if (tryTrackFieldAssignments) {
            this.localsAndFieldRefs.addAll(trackableFields());
        }
        this.rhsToNumber = new HashMap();
        this.mergePointToValueToNumber = new HashMap();
        doAnalysis();
        this.rhsToNumber = null;
        this.mergePointToValueToNumber = null;
    }

    private Set<Value> trackableFields() {
        Set<Value> usedFieldRefs = new HashSet<>();
        for (Unit unit : this.graph) {
            for (ValueBox useBox : unit.getUseBoxes()) {
                Value val = useBox.getValue();
                if (val instanceof FieldRef) {
                    FieldRef fieldRef = (FieldRef) val;
                    if (fieldRef.getType() instanceof RefLikeType) {
                        usedFieldRefs.add(new EquivalentValue(fieldRef));
                    }
                }
            }
        }
        if (!usedFieldRefs.isEmpty()) {
            if (!Scene.v().hasCallGraph()) {
                throw new IllegalStateException("No call graph found!");
            }
            ReachableMethods reachableMethods = new ReachableMethods(Scene.v().getCallGraph(), Collections.singletonList(this.container));
            reachableMethods.update();
            Iterator<MethodOrMethodContext> iterator = reachableMethods.listener();
            while (iterator.hasNext()) {
                SootMethod m = (SootMethod) iterator.next();
                if (m.hasActiveBody() && (!"<clinit>".equals(m.getName()) || !m.getDeclaringClass().equals(this.container.getDeclaringClass()))) {
                    Iterator<Unit> it = m.getActiveBody().getUnits().iterator();
                    while (it.hasNext()) {
                        Unit u = it.next();
                        for (ValueBox defBox : u.getDefBoxes()) {
                            Value value = defBox.getValue();
                            if (value instanceof FieldRef) {
                                usedFieldRefs.remove(new EquivalentValue(value));
                            }
                        }
                    }
                }
            }
        }
        return usedFieldRefs;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(Unit succUnit, HashMap<Value, Integer> inMap1, HashMap<Value, Integer> inMap2, HashMap<Value, Integer> outMap) {
        for (Value l : this.localsAndFieldRefs) {
            Integer i1 = inMap1.get(l);
            Integer i2 = inMap2.get(l);
            if (i1 == null) {
                outMap.put(l, i2);
            } else if (i2 == null) {
                outMap.put(l, i1);
            } else if (i1.equals(i2)) {
                outMap.put(l, i1);
            } else {
                Integer number = null;
                Map<Value, Integer> valueToNumber = this.mergePointToValueToNumber.get(succUnit);
                if (valueToNumber == null) {
                    valueToNumber = new HashMap<>();
                    this.mergePointToValueToNumber.put(succUnit, valueToNumber);
                } else {
                    number = valueToNumber.get(l);
                }
                if (number == null) {
                    int i = this.nextNumber;
                    this.nextNumber = i + 1;
                    number = Integer.valueOf(i);
                    valueToNumber.put(l, number);
                }
                outMap.put(l, number);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(HashMap<Value, Integer> in, Unit u, HashMap<Value, Integer> out) {
        out.clear();
        out.putAll(in);
        if (u instanceof DefinitionStmt) {
            DefinitionStmt ds = (DefinitionStmt) u;
            Value lhs = ds.getLeftOp();
            Value rhs = ds.getRightOp();
            if (rhs instanceof CastExpr) {
                rhs = ((CastExpr) rhs).getOp();
            }
            if (((lhs instanceof Local) || ((lhs instanceof FieldRef) && this.localsAndFieldRefs.contains(new EquivalentValue(lhs)))) && (lhs.getType() instanceof RefLikeType)) {
                if (rhs instanceof Local) {
                    Integer val = in.get(rhs);
                    if (val != null) {
                        out.put(lhs, val);
                    }
                } else if (rhs instanceof ThisRef) {
                    out.put(lhs, Integer.valueOf(thisRefNumber()));
                } else if (rhs instanceof ParameterRef) {
                    out.put(lhs, Integer.valueOf(parameterRefNumber((ParameterRef) rhs)));
                } else {
                    out.put(lhs, numberOfRhs(rhs));
                }
            }
        } else if (!$assertionsDisabled && !u.getDefBoxes().isEmpty()) {
            throw new AssertionError();
        }
    }

    private Integer numberOfRhs(Value rhs) {
        EquivalentValue equivValue = new EquivalentValue(rhs);
        if (this.localsAndFieldRefs.contains(equivValue)) {
            rhs = equivValue;
        }
        Integer num = this.rhsToNumber.get(rhs);
        if (num == null) {
            int i = this.nextNumber;
            this.nextNumber = i + 1;
            num = Integer.valueOf(i);
            this.rhsToNumber.put(rhs, num);
        }
        return num;
    }

    public static int thisRefNumber() {
        return 0;
    }

    public static int parameterRefNumber(ParameterRef r) {
        return (-1) - r.getIndex();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(HashMap<Value, Integer> sourceMap, HashMap<Value, Integer> destMap) {
        destMap.clear();
        destMap.putAll(sourceMap);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public HashMap<Value, Integer> entryInitialFlow() {
        return new HashMap<>();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public HashMap<Value, Integer> newInitialFlow() {
        return new HashMap<>();
    }

    public String instanceKeyString(Local l, Stmt s) {
        Integer ln = getFlowBefore(s).get(l);
        if (ln == null) {
            return null;
        }
        return ln.toString();
    }

    public boolean hasInfoOn(Local l, Stmt s) {
        return getFlowBefore(s) != null;
    }

    public boolean mustAlias(Local l1, Stmt s1, Local l2, Stmt s2) {
        Integer l1n = getFlowBefore(s1).get(l1);
        Integer l2n = getFlowBefore(s2).get(l2);
        return (l1n == null || l2n == null || !l1n.equals(l2n)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(HashMap<Value, Integer> in1, HashMap<Value, Integer> in2, HashMap<Value, Integer> out) {
        out.putAll(in1);
        for (Value val : in2.keySet()) {
            Integer i1 = in1.get(val);
            Integer i2 = in2.get(val);
            if (i2.equals(i1)) {
                out.put(val, i2);
            } else {
                throw new RuntimeException("Merge of different IDs not supported");
            }
        }
    }
}
