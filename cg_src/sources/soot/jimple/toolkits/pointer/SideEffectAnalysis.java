package soot.jimple.toolkits.pointer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.G;
import soot.Local;
import soot.MethodOrMethodContext;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.TransitiveTargets;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/SideEffectAnalysis.class */
public class SideEffectAnalysis {
    private final Map<SootMethod, MethodRWSet> methodToNTReadSet;
    private final Map<SootMethod, MethodRWSet> methodToNTWriteSet;
    private final PointsToAnalysis pa;
    private final CallGraph cg;
    private final TransitiveTargets tt;

    private SideEffectAnalysis(PointsToAnalysis pa, CallGraph cg, TransitiveTargets tt) {
        this.methodToNTReadSet = new HashMap();
        this.methodToNTWriteSet = new HashMap();
        if (G.v().Union_factory == null) {
            G.v().Union_factory = new UnionFactory() { // from class: soot.jimple.toolkits.pointer.SideEffectAnalysis.1
                @Override // soot.jimple.toolkits.pointer.UnionFactory
                public Union newUnion() {
                    return FullObjectSet.v();
                }
            };
        }
        this.pa = pa;
        this.cg = cg;
        this.tt = tt;
    }

    public SideEffectAnalysis(PointsToAnalysis pa, CallGraph cg) {
        this(pa, cg, new TransitiveTargets(cg));
    }

    public SideEffectAnalysis(PointsToAnalysis pa, CallGraph cg, Filter filter) {
        this(pa, cg, new TransitiveTargets(cg, filter));
    }

    public void findNTRWSets(SootMethod method) {
        if (this.methodToNTReadSet.containsKey(method) && this.methodToNTWriteSet.containsKey(method)) {
            return;
        }
        MethodRWSet read = null;
        MethodRWSet write = null;
        Iterator<Unit> it = method.retrieveActiveBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit next = it.next();
            Stmt s = (Stmt) next;
            RWSet ntr = ntReadSet(method, s);
            if (ntr != null) {
                if (read == null) {
                    read = new MethodRWSet();
                }
                read.union(ntr);
            }
            RWSet ntw = ntWriteSet(method, s);
            if (ntw != null) {
                if (write == null) {
                    write = new MethodRWSet();
                }
                write.union(ntw);
            }
        }
        this.methodToNTReadSet.put(method, read);
        this.methodToNTWriteSet.put(method, write);
    }

    public RWSet nonTransitiveReadSet(SootMethod method) {
        findNTRWSets(method);
        return this.methodToNTReadSet.get(method);
    }

    public RWSet nonTransitiveWriteSet(SootMethod method) {
        findNTRWSets(method);
        return this.methodToNTWriteSet.get(method);
    }

    private RWSet ntReadSet(SootMethod method, Stmt stmt) {
        if (stmt instanceof AssignStmt) {
            return addValue(((AssignStmt) stmt).getRightOp(), method, stmt);
        }
        return null;
    }

    public RWSet readSet(SootMethod method, Stmt stmt) {
        RWSet ntr;
        RWSet ret = null;
        Iterator<MethodOrMethodContext> targets = this.tt.iterator(stmt);
        while (targets.hasNext()) {
            SootMethod target = (SootMethod) targets.next();
            if (target.isNative()) {
                if (ret == null) {
                    ret = new SiteRWSet();
                }
                ret.setCallsNative();
            } else if (target.isConcrete() && (ntr = nonTransitiveReadSet(target)) != null) {
                if (ret == null) {
                    ret = new SiteRWSet();
                }
                ret.union(ntr);
            }
        }
        if (ret == null) {
            return ntReadSet(method, stmt);
        }
        ret.union(ntReadSet(method, stmt));
        return ret;
    }

    private RWSet ntWriteSet(SootMethod method, Stmt stmt) {
        if (stmt instanceof AssignStmt) {
            return addValue(((AssignStmt) stmt).getLeftOp(), method, stmt);
        }
        return null;
    }

    public RWSet writeSet(SootMethod method, Stmt stmt) {
        RWSet ntw;
        RWSet ret = null;
        Iterator<MethodOrMethodContext> targets = this.tt.iterator(stmt);
        while (targets.hasNext()) {
            SootMethod target = (SootMethod) targets.next();
            if (target.isNative()) {
                if (ret == null) {
                    ret = new SiteRWSet();
                }
                ret.setCallsNative();
            } else if (target.isConcrete() && (ntw = nonTransitiveWriteSet(target)) != null) {
                if (ret == null) {
                    ret = new SiteRWSet();
                }
                ret.union(ntw);
            }
        }
        if (ret == null) {
            return ntWriteSet(method, stmt);
        }
        ret.union(ntWriteSet(method, stmt));
        return ret;
    }

    protected RWSet addValue(Value v, SootMethod m, Stmt s) {
        RWSet ret = null;
        if (v instanceof InstanceFieldRef) {
            InstanceFieldRef ifr = (InstanceFieldRef) v;
            PointsToSet base = this.pa.reachingObjects((Local) ifr.getBase());
            ret = new StmtRWSet();
            ret.addFieldRef(base, ifr.getField());
        } else if (v instanceof StaticFieldRef) {
            StaticFieldRef sfr = (StaticFieldRef) v;
            ret = new StmtRWSet();
            ret.addGlobal(sfr.getField());
        } else if (v instanceof ArrayRef) {
            ArrayRef ar = (ArrayRef) v;
            PointsToSet base2 = this.pa.reachingObjects((Local) ar.getBase());
            ret = new StmtRWSet();
            ret.addFieldRef(base2, PointsToAnalysis.ARRAY_ELEMENTS_NODE);
        }
        return ret;
    }

    public String toString() {
        return "SideEffectAnalysis: PA=" + this.pa + " CG=" + this.cg;
    }
}
