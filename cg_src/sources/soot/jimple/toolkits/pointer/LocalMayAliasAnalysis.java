package soot.jimple.toolkits.pointer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.Body;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/LocalMayAliasAnalysis.class */
public class LocalMayAliasAnalysis extends ForwardFlowAnalysis<Unit, Set<Set<Value>>> {
    private final Body body;

    public LocalMayAliasAnalysis(UnitGraph graph) {
        super(graph);
        this.body = graph.getBody();
        doAnalysis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(Set<Set<Value>> source, Unit unit, Set<Set<Value>> target) {
        target.addAll(source);
        if (unit instanceof DefinitionStmt) {
            DefinitionStmt def = (DefinitionStmt) unit;
            Value left = def.getLeftOp();
            Value right = def.getRightOp();
            if (right instanceof Constant) {
                Set<Value> leftSet = null;
                Iterator<Set<Value>> it = source.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Set<Value> s = it.next();
                    if (s.contains(left)) {
                        leftSet = s;
                        break;
                    }
                }
                if (leftSet == null) {
                    throw new RuntimeException("internal error");
                }
                target.remove(leftSet);
                HashSet<Value> setWithoutLeft = new HashSet<>(leftSet);
                setWithoutLeft.remove(left);
                target.add(setWithoutLeft);
                target.add(Collections.singleton(left));
                return;
            }
            Set<Value> leftSet2 = null;
            Set<Value> rightSet = null;
            Iterator<Set<Value>> it2 = source.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                Set<Value> s2 = it2.next();
                if (s2.contains(left)) {
                    leftSet2 = s2;
                    break;
                }
            }
            Iterator<Set<Value>> it3 = source.iterator();
            while (true) {
                if (!it3.hasNext()) {
                    break;
                }
                Set<Value> s3 = it3.next();
                if (s3.contains(right)) {
                    rightSet = s3;
                    break;
                }
            }
            if (leftSet2 == null || rightSet == null) {
                throw new RuntimeException("internal error");
            }
            target.remove(leftSet2);
            target.remove(rightSet);
            HashSet<Value> union = new HashSet<>(leftSet2);
            union.addAll(rightSet);
            target.add(union);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(Set<Set<Value>> source, Set<Set<Value>> target) {
        target.clear();
        target.addAll(source);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public Set<Set<Value>> entryInitialFlow() {
        Set<Set<Value>> res = new HashSet<>();
        for (ValueBox vb : this.body.getUseAndDefBoxes()) {
            res.add(Collections.singleton(vb.getValue()));
        }
        return res;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(Set<Set<Value>> source1, Set<Set<Value>> source2, Set<Set<Value>> target) {
        target.clear();
        target.addAll(source1);
        target.addAll(source2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public Set<Set<Value>> newInitialFlow() {
        return new HashSet();
    }

    public boolean mayAlias(Value v1, Value v2, Unit u) {
        for (Set<Value> set : getFlowBefore(u)) {
            if (set.contains(v1) && set.contains(v2)) {
                return true;
            }
        }
        return false;
    }

    public Set<Value> mayAliases(Value v, Unit u) {
        Set<Value> res = new HashSet<>();
        for (Set<Value> set : getFlowBefore(u)) {
            if (set.contains(v)) {
                res.addAll(set);
            }
        }
        return res;
    }

    public Set<Value> mayAliasesAtExit(Value v) {
        Set<Value> res = new HashSet<>();
        for (Unit u : this.graph.getTails()) {
            for (Set<Value> set : getFlowAfter(u)) {
                if (set.contains(v)) {
                    res.addAll(set);
                }
            }
        }
        return res;
    }
}
