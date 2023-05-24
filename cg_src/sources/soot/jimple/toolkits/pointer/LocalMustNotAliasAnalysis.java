package soot.jimple.toolkits.pointer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import soot.Body;
import soot.Local;
import soot.RefType;
import soot.Unit;
import soot.Value;
import soot.jimple.DefinitionStmt;
import soot.jimple.NewExpr;
import soot.jimple.Stmt;
import soot.jimple.internal.AbstractNewExpr;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/LocalMustNotAliasAnalysis.class */
public class LocalMustNotAliasAnalysis extends ForwardFlowAnalysis<Unit, HashMap<Local, Set<NewExpr>>> {
    protected static final NewExpr UNKNOWN = new AbstractNewExpr() { // from class: soot.jimple.toolkits.pointer.LocalMustNotAliasAnalysis.1
        @Override // soot.jimple.internal.AbstractNewExpr
        public String toString() {
            return "UNKNOWN";
        }

        @Override // soot.jimple.internal.AbstractNewExpr, soot.Value
        public Object clone() {
            return this;
        }
    };
    protected final Set<Local> locals;

    public LocalMustNotAliasAnalysis(UnitGraph g) {
        this(g, g.getBody());
    }

    public LocalMustNotAliasAnalysis(DirectedGraph<Unit> directedGraph, Body b) {
        super(directedGraph);
        this.locals = new HashSet(b.getLocals());
        doAnalysis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(HashMap<Local, Set<NewExpr>> in1, HashMap<Local, Set<NewExpr>> in2, HashMap<Local, Set<NewExpr>> o) {
        for (Local l : this.locals) {
            Set<NewExpr> l1 = in1.get(l);
            Set<NewExpr> l2 = in2.get(l);
            Set<NewExpr> out = o.get(l);
            out.clear();
            if (l1.contains(UNKNOWN) || l2.contains(UNKNOWN)) {
                out.add(UNKNOWN);
            } else {
                out.addAll(l1);
                out.addAll(l2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(HashMap<Local, Set<NewExpr>> in, Unit unit, HashMap<Local, Set<NewExpr>> out) {
        out.clear();
        out.putAll(in);
        if (unit instanceof DefinitionStmt) {
            DefinitionStmt ds = (DefinitionStmt) unit;
            Value lhs = ds.getLeftOp();
            if (lhs instanceof Local) {
                HashSet<NewExpr> lv = new HashSet<>();
                out.put((Local) lhs, lv);
                Value rhs = ds.getRightOp();
                if (rhs instanceof NewExpr) {
                    lv.add((NewExpr) rhs);
                } else if (rhs instanceof Local) {
                    lv.addAll(in.get((Local) rhs));
                } else {
                    lv.add(UNKNOWN);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(HashMap<Local, Set<NewExpr>> source, HashMap<Local, Set<NewExpr>> dest) {
        dest.putAll(source);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public HashMap<Local, Set<NewExpr>> entryInitialFlow() {
        HashMap<Local, Set<NewExpr>> m = new HashMap<>();
        for (Local l : this.locals) {
            HashSet<NewExpr> s = new HashSet<>();
            s.add(UNKNOWN);
            m.put(l, s);
        }
        return m;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public HashMap<Local, Set<NewExpr>> newInitialFlow() {
        HashMap<Local, Set<NewExpr>> m = new HashMap<>();
        for (Local l : this.locals) {
            m.put(l, new HashSet());
        }
        return m;
    }

    public boolean hasInfoOn(Local l, Stmt s) {
        Set<NewExpr> info;
        HashMap<Local, Set<NewExpr>> flowBefore = getFlowBefore(s);
        return (flowBefore == null || (info = flowBefore.get(l)) == null || info.contains(UNKNOWN)) ? false : true;
    }

    public boolean notMayAlias(Local l1, Stmt s1, Local l2, Stmt s2) {
        Set<NewExpr> l1n = getFlowBefore(s1).get(l1);
        Set<NewExpr> l2n = getFlowBefore(s2).get(l2);
        if (l1n.contains(UNKNOWN) || l2n.contains(UNKNOWN)) {
            return false;
        }
        Set<NewExpr> n = new HashSet<>(l1n);
        n.retainAll(l2n);
        return n.isEmpty();
    }

    public RefType concreteType(Local l, Stmt s) {
        NewExpr singleNewExpr;
        Set<NewExpr> set = getFlowBefore(s).get(l);
        if (set.size() == 1 && (singleNewExpr = set.iterator().next()) != UNKNOWN) {
            return (RefType) singleNewExpr.getType();
        }
        return null;
    }
}
