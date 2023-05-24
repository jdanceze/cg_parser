package soot.jimple.toolkits.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.util.Chain;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/base/JimpleConstructorFolder.class */
public class JimpleConstructorFolder extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(JimpleConstructorFolder.class);
    private static final boolean DEBUG_DUMP_BODIES = false;

    static Value rhs(Stmt s) {
        return ((AssignStmt) s).getRightOp();
    }

    static Value lhs(Stmt s) {
        return ((AssignStmt) s).getLeftOp();
    }

    static Local rhsLocal(Stmt s) {
        return (Local) rhs(s);
    }

    static Local lhsLocal(Stmt s) {
        return (Local) lhs(s);
    }

    static boolean isNew(Stmt s) {
        return (s instanceof AssignStmt) && (rhs(s) instanceof NewExpr);
    }

    static boolean isConstructor(Stmt s) {
        if (s instanceof InvokeStmt) {
            InvokeExpr expr = ((InvokeStmt) s).getInvokeExpr();
            if (expr instanceof SpecialInvokeExpr) {
                SpecialInvokeExpr sie = (SpecialInvokeExpr) expr;
                return "<init>".equals(sie.getMethodRef().getName());
            }
            return false;
        }
        return false;
    }

    static Local base(Stmt s) {
        InvokeStmt is = (InvokeStmt) s;
        InstanceInvokeExpr expr = (InstanceInvokeExpr) is.getInvokeExpr();
        return (Local) expr.getBase();
    }

    static void setBase(Stmt s, Local l) {
        InvokeStmt is = (InvokeStmt) s;
        InstanceInvokeExpr expr = (InstanceInvokeExpr) is.getInvokeExpr();
        expr.getBaseBox().setValue(l);
    }

    static boolean isCopy(Stmt s) {
        return (s instanceof AssignStmt) && (rhs(s) instanceof Local) && (lhs(s) instanceof Local);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/base/JimpleConstructorFolder$Fact.class */
    public static class Fact {
        private Map<Local, Stmt> varToStmt;
        private MultiMap<Stmt, Local> stmtToVar;
        private Stmt alloc;

        private Fact() {
            this.varToStmt = new HashMap();
            this.stmtToVar = new HashMultiMap();
            this.alloc = null;
        }

        /* synthetic */ Fact(Fact fact) {
            this();
        }

        public void add(Local l, Stmt s) {
            this.varToStmt.put(l, s);
            this.stmtToVar.put(s, l);
        }

        public Stmt get(Local l) {
            return this.varToStmt.get(l);
        }

        public Set<Local> get(Stmt s) {
            return this.stmtToVar.get(s);
        }

        public void removeAll(Stmt s) {
            for (Local var : this.stmtToVar.get(s)) {
                this.varToStmt.remove(var);
            }
            this.stmtToVar.remove(s);
        }

        public Stmt alloc() {
            return this.alloc;
        }

        public void setAlloc(Stmt newAlloc) {
            this.alloc = newAlloc;
        }

        public void copyFrom(Fact in) {
            this.varToStmt = new HashMap(in.varToStmt);
            this.stmtToVar = new HashMultiMap(in.stmtToVar);
            this.alloc = in.alloc;
        }

        public void mergeFrom(Fact in1, Fact in2) {
            this.varToStmt = new HashMap();
            for (Map.Entry<Local, Stmt> e : in1.varToStmt.entrySet()) {
                Local l = e.getKey();
                Stmt newStmt = e.getValue();
                if (in2.varToStmt.containsKey(l) && !newStmt.equals(in2.varToStmt.get(l))) {
                    throw new RuntimeException("Merge of different uninitialized values; are you sure this bytecode is verifiable?");
                }
                add(l, newStmt);
            }
            for (Map.Entry<Local, Stmt> e2 : in2.varToStmt.entrySet()) {
                add(e2.getKey(), e2.getValue());
            }
            Stmt alloc1 = in1.alloc;
            this.alloc = (alloc1 == null || !alloc1.equals(in2.alloc)) ? null : alloc1;
        }

        public boolean equals(Object other) {
            if (!(other instanceof Fact)) {
                return false;
            }
            Fact o = (Fact) other;
            if (this.alloc != null || o.alloc == null) {
                if (this.alloc == null || o.alloc != null) {
                    return (this.alloc == null || this.alloc.equals(o.alloc)) && this.stmtToVar.equals(o.stmtToVar);
                }
                return false;
            }
            return false;
        }

        public int hashCode() {
            int hash = (89 * 7) + Objects.hashCode(this.stmtToVar);
            return (89 * hash) + Objects.hashCode(this.alloc);
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/base/JimpleConstructorFolder$Analysis.class */
    private class Analysis extends ForwardFlowAnalysis<Unit, Fact> {
        public Analysis(DirectedGraph<Unit> graph) {
            super(graph);
            doAnalysis();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public Fact newInitialFlow() {
            return new Fact(null);
        }

        @Override // soot.toolkits.scalar.FlowAnalysis
        public void flowThrough(Fact in, Unit u, Fact out) {
            Stmt newStmt;
            Stmt s = (Stmt) u;
            copy(in, out);
            out.setAlloc(null);
            if (JimpleConstructorFolder.isNew(s)) {
                out.add(JimpleConstructorFolder.lhsLocal(s), s);
            } else if (JimpleConstructorFolder.isCopy(s)) {
                Stmt newStmt2 = out.get(JimpleConstructorFolder.rhsLocal(s));
                if (newStmt2 != null) {
                    out.add(JimpleConstructorFolder.lhsLocal(s), newStmt2);
                }
            } else if (JimpleConstructorFolder.isConstructor(s) && (newStmt = out.get(JimpleConstructorFolder.base(s))) != null) {
                out.removeAll(newStmt);
                out.setAlloc(newStmt);
            }
        }

        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public void copy(Fact source, Fact dest) {
            dest.copyFrom(source);
        }

        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public void merge(Fact in1, Fact in2, Fact out) {
            out.mergeFrom(in1, in2);
        }
    }

    @Override // soot.BodyTransformer
    public void internalTransform(Body b, String phaseName, Map<String, String> options) {
        JimpleBody body = (JimpleBody) b;
        if (Options.v().verbose()) {
            logger.debug("[" + body.getMethod().getName() + "] Folding Jimple constructors...");
        }
        Analysis analysis = new Analysis(new BriefUnitGraph(body));
        Chain<Unit> units = body.getUnits();
        for (Unit u : units) {
            Stmt s = (Stmt) u;
            if (!isCopy(s) && !isConstructor(s)) {
                Fact before = analysis.getFlowBefore(s);
                for (ValueBox usebox : s.getUseBoxes()) {
                    Value value = usebox.getValue();
                    if ((value instanceof Local) && before.get((Local) value) != null) {
                        throw new RuntimeException("Use of an unitialized value before constructor call; are you sure this bytecode is verifiable?\n " + s);
                    }
                }
                continue;
            }
        }
        Iterator<Unit> it = units.snapshotIterator();
        while (it.hasNext()) {
            Stmt s2 = (Stmt) it.next();
            if (isNew(s2)) {
                units.remove(s2);
            }
        }
        Jimple jimp = Jimple.v();
        Iterator<Unit> it2 = units.snapshotIterator();
        while (it2.hasNext()) {
            Stmt s3 = (Stmt) it2.next();
            if (isCopy(s3)) {
                if (analysis.getFlowBefore(s3).get(rhsLocal(s3)) != null) {
                    units.remove(s3);
                }
            } else if (analysis.getFlowAfter(s3).alloc() != null) {
                Fact before2 = analysis.getFlowBefore(s3);
                Local baseS = base(s3);
                Stmt newStmt = before2.get(baseS);
                setBase(s3, lhsLocal(newStmt));
                units.insertBefore(newStmt, s3);
                for (Local l : before2.get(newStmt)) {
                    if (!baseS.equals(l)) {
                        units.insertAfter(jimp.newAssignStmt(l, baseS), s3);
                    }
                }
            }
        }
    }
}
